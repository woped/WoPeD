/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import ToolWrapper.WordNetFunctionality;
import edu.mit.jwi.item.POS;

import TextToWorldModel.Constants;
import TextToWorldModel.SentenceWordID;
import processing.ProcessingUtils;

import ToolWrapper.FrameNetFunctionality.PhraseType;
import text.T2PSentence;
import text.Text;
import transform.ConjunctionElement.ConjunctionType;
import worldModel.Action;
import worldModel.Actor;
import worldModel.DetermineConjResult;
import worldModel.ExtractedObject;
import worldModel.Flow;
import worldModel.Resource;
import worldModel.SpecifiedElement;
import worldModel.Specifier;
import worldModel.WorldModel;
import worldModel.Action.ActionLinkType;
import worldModel.DetermineConjResult.DLRStatusCode;
import worldModel.Flow.FlowDirection;
import worldModel.Flow.FlowType;
import worldModel.Specifier.SpecifierType;

import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TypedDependency;

public class TextAnalyzer {
	
	
	private static final int SUBJECT_ROLE_SCORE = 10;	
	private static final int OBJECT_ROLE_SCORE = 10; //for cop sentences
	private static final int ROLE_MATCH_SCORE = 20;	
	private static final int SENTENCE_DISTANCE_PENALTY = 15;

	public enum AnimateType{
		ANIMATE,
		INANIMATE,
		BOTH
	};
	
	private Text f_text;
	private ArrayList<AnalyzedSentence> f_analyzedSentences = new ArrayList<AnalyzedSentence>();
	
	private HashMap<SentenceWordID, SentenceWordID> f_referenceMap = new HashMap<SentenceWordID, SentenceWordID>();
	
	private WorldModel f_world = new WorldModel();
	private Flow f_lastSplit;
	
	/**
	 * 
	 */
	public TextAnalyzer() {
	}
	
	public void analyze(Text textToAnalyze) {
		try{
			T2PSentence.resetIDs();
			f_world.clear();
			f_analyzedSentences.clear();
			f_text = textToAnalyze;			
			for(int i = 0;i<f_text.getSentences().size();i++) {
				T2PSentence s = f_text.getSentences().get(i);
				analyzeSentence(s,i,false);					
			}
			
			referenceResolution();
			markerDetection();
			combineActions();
			determineLinks();
			buildFlows();			
		
			
			if(Constants.DEBUG_FINAL_ACTIONS_RESULT) {
				for(AnalyzedSentence s:f_analyzedSentences) {
					PrintUtils.printExtractedActions(s);
				}
				for(Flow f:f_world.getFlows()) {
					System.out.println(f);
				}	
			}					
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}	

	

	/**
	 * 
	 */
	private void determineLinks() {
		List<Action> _actions = f_world.getActions();
		for(int i=_actions.size()-1;i>0;i--) {
			for(int j=i-1;j>=0;j--) {
				Action _a1 = _actions.get(i);
				Action _a2 = _actions.get(j);
				if(islinkable(_a1, _a2)) {
					_a1.setLink(_a2);
					_a1.setLinkType(determineLinkType(_a1,_a2));
					if(Constants.DEBUG_FINAL_ACTIONS_RESULT)
						System.out.println("Linkable: "+_a1 +" --- "+_a2);
				}
			}
		}
	}

	/**
	 * @param _a1
	 * @param _a2
	 * @return
	 */
	private ActionLinkType determineLinkType(Action linkSource, Action linkTarget) {
		if("if".equals(linkSource.getMarker())) {
			for(Specifier spec:linkSource.getSpecifiers()) {
				if(SearchUtils.startsWithAny(spec.getPhrase(), Constants.f_acceptedForForwardLink)) {
					return ActionLinkType.FORWARD;
				}
			}
			AnalyzedSentence _tOrigin = f_analyzedSentences.get(linkTarget.getOrigin().getID());
			ArrayList<Action> _result = new ArrayList<Action>();
			DetermineConjResult _linkResult = determineConjunctElements(_tOrigin.getExtractedConjunctions(), linkTarget, _result, _tOrigin.getExtractedActions());
			if(_linkResult.getType() == ConjunctionType.OR || "if".equals(linkTarget.getMarker()) 
					|| Constants.f_sequenceIndicators.contains(linkTarget.getPreAdvMod())) {
				return ActionLinkType.JUMP;
			}
		}else {
			//has no "if"
			//check if it can be a loop
			if(Constants.f_acceptedAMODforLoops.contains(linkSource.getMod())){
				return ActionLinkType.LOOP;
			}
			//also check certain specifiers
			List<Specifier> _toCheck = new ArrayList<Specifier>(linkSource.getSpecifiers(SpecifierType.AMOD));
			if(linkSource.getObject() != null)
				_toCheck.addAll(linkSource.getObject().getSpecifiers(SpecifierType.AMOD));
			
			for(Specifier spec:_toCheck) {
				if(Constants.f_acceptedForForwardLink.contains(spec.getName())){
					return ActionLinkType.LOOP;
				}
			}
		}
		return ActionLinkType.NONE;
	}

	private boolean islinkable(Action _a1, Action _a2) {
		if(_a2 == null) {
			return false;
		}
		if(_a1.getVerb().equals(_a2.getVerb())){
			if(_a1.isNegated() != _a2.isNegated()) {
				return false;
			}
			if(_a1.getCop() != null && !_a1.getCop().equals(_a2.getCop())) {
					return false;
			}
			if(!(_a1.getActorFrom() == null && _a1.getActorFrom() == null)) {
				if(_a1.getActorFrom() == null) {
					return false;
				}
				Actor _from1 = _a1.getActorFrom();
				if(_from1.needsResolve() && _from1.getReference() instanceof Actor) {
					_from1 = (Actor) _from1.getReference();
				}
				Actor _from2 = _a2.getActorFrom();
				if(_from2 != null) {
					if(_from2.needsResolve() && _from2.getReference() instanceof Actor) {
						_from2 = (Actor) _from2.getReference();
					}
				}
				if(_from2 == null || !exObEquals(_from1,_from2)) {
					return false;
				}
			}
			
			if(!(_a1.getObject() == null && _a2.getObject() == null)) {
				if(_a1.getObject() == null) {
					return false;
				}
				ExtractedObject _to1 = _a1.getObject();
				if(_to1.needsResolve() && _to1.getReference() instanceof Actor) {
					_to1 = (Actor) _to1.getReference();
				}
				ExtractedObject _to2 = _a2.getObject();
				if(_to2 != null) {
					if(_to2.needsResolve() && _to2.getReference() instanceof Actor) {
						_to2 = (Actor) _to2.getReference();
					}
				}
				if(_to2 == null || !exObEquals(_to1,_to2)) {
					return false;
				}
			}		
			if(!(_a1.getXcomp() == null && _a2.getXcomp() == null)) {
				if(_a1.getXcomp() == null) {
					return false;
				}
				return islinkable(_a1.getXcomp(), _a2.getXcomp());
			}
			if(!checkSpecifierEqual(_a1, _a2,SpecifierType.PP,ListUtils.getList("to","about"))) {
				return false;
			}
			//all tests passed
			return true;
		}
		return false;
	}

	/**
	 * @param _from1
	 * @param _from2
	 * @return
	 */
	private boolean exObEquals(ExtractedObject _from1, ExtractedObject _from2) {
		if(_from1.getName().endsWith("s") == _from2.getName().endsWith("s")) { //check plural (EX5.txt)
			WordNetFunctionality wnf = new WordNetFunctionality();
			String _name1 = wnf.getBaseForm(_from1.getName(),false,POS.NOUN);
			String _name2 = wnf.getBaseForm(_from2.getName(),false,POS.NOUN);
			if(_name1.equals(_name2)) {
				//checking if the "no" determiner is equal
				if("no".equals(_from1.getDeterminer()) || "no".equals(_from2.getDeterminer())){
					if(_from1.getDeterminer() != null) {
						if(!_from1.getDeterminer().equals(_from2.getDeterminer())) {
							return false;
						}
					}else {
						return false;
					}
				}
				if(checkSpecifierEqual(_from1, _from2,SpecifierType.AMOD,"")) {
					if(checkSpecifierEqual(_from1, _from2,SpecifierType.PP,"for")) {
						if(checkSpecifierEqual(_from1, _from2,SpecifierType.NN,"")) {
							if(checkSpecifierEqual(_from1, _from2,SpecifierType.NNAFTER,"")) {
								if(checkSpecifierEqual(_from1, _from2,SpecifierType.PP,"about")) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * if headWord is "" it is not checked
	 * @param _from1
	 * @param _from2
	 * @param toCheck
	 * @param headWord
	 * @return
	 */
	private boolean checkSpecifierEqual(SpecifiedElement _from1,SpecifiedElement _from2,SpecifierType toCheck,String headWord) {		
		outer: for(Specifier spec1:_from1.getSpecifiers(toCheck)) {
			//only check if it is not an acceptable AMOD (next, following)
			if(headWord.length()!=0 && !headWord.equals(spec1.getHeadWord())) {
				continue;
			}
			if(!Constants.f_acceptedAMODforLoops.contains(spec1.getName())){
				for(Specifier spec2:_from2.getSpecifiers(toCheck)) {
					if(spec1.getName().equals(spec2.getName())) {
						continue outer;
					}
				}	
				//looked through all of them, not found
				return false;
			}
		}
		//found all
		return true;		
	}
	
	/**
	 * if headWord is null it is not checked
	 * @param _from1
	 * @param _from2
	 * @param toCheck
	 * @param headWord
	 * @return
	 */
	private boolean checkSpecifierEqual(SpecifiedElement _from1,SpecifiedElement _from2,SpecifierType toCheck,List<String> headWordForUnknowns) {		
		outer: for(Specifier spec1:_from1.getSpecifiers(toCheck)) {
			boolean _check = false;
			if(spec1.getPhraseType() == PhraseType.CORE || spec1.getPhraseType() == PhraseType.GENITIVE) {
				_check = true;
			}
			if(spec1.getPhraseType() == PhraseType.UNKNOWN && headWordForUnknowns.contains(spec1.getHeadWord())) {
				_check = true;
			}
			if(_check) {
				if(!Constants.f_acceptedAMODforLoops.contains(spec1.getName())){
					for(Specifier spec2:_from2.getSpecifiers(toCheck)) {
						if(spec1.getName().equals(spec2.getName())) {
							continue outer;
						}
					}	
					//looked through all of them, not found
					return false;
				}
			}
		}
		//found all
		return true;		
	}
	
	private boolean hasIncomingLink(Action linkTarget,ActionLinkType type) {
		for(Action a:f_world.getActions()) {
			if(linkTarget.equals(a.getLink())) {
				if(type == null || a.getLinkType().equals(type)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 */
	private void buildFlows() {
		f_lastSplit = null;
		List<Action> _cameFrom = new ArrayList<Action>();
		List<Action> _openSplit = new ArrayList<Action>();
		for(AnalyzedSentence sentence:f_analyzedSentences) {
			T2PSentence _base = sentence.getBaseSentence();
			List<Action> _actions = sentence.getExtractedActions();					
			List<ConjunctionElement> _conjs = sentence.getExtractedConjunctions();			
			ArrayList<Action> _processed = new ArrayList<Action>();
			for(Action a:_actions) {
				if(a.getLink() != null && a.getLinkType().equals(ActionLinkType.JUMP)) {
					System.out.println("!!!!!!!! BUILDING JUMP !!!!!!!!!!!");
					_cameFrom.clear();
					_cameFrom.add(a.getLink());
					clearSplit(_openSplit);
					a.setTransient(true);
					continue;//this node is not processed itself, but rather stands for the link
				}
				if(!_processed.contains(a)) {
					Flow _flow = new Flow(_base);
					ArrayList<Action> _conjoined = new ArrayList<Action>();
					DetermineConjResult _type = determineConjunctElements(_conjs, a, _conjoined,_actions);
					if(_conjoined.size() == 1) {						
						handleSingleAction(_base,_flow,_conjoined.get(0),_cameFrom,_openSplit);
					}else {						
						if(_cameFrom.size() > 0) {
							_flow.setSingleObject(_cameFrom.get(0));//effectively only for size == 1
						}
						//okay we found some links
						if(_type.getType().equals(ConjunctionType.OR) || _type.getType().equals(ConjunctionType.ANDOR) || ProcessingUtils.hasFrequencyAttached(_conjoined.get(0))) {
							createDummyStartNode(_cameFrom,_flow);
							FlowType _ft = _type.getType().equals(ConjunctionType.ANDOR) ? FlowType.multiChoice : FlowType.choice;
							buildGateway(_cameFrom, _openSplit, _base,_processed, _flow, _conjoined,_ft);
						}else if(_type.getType().equals(ConjunctionType.AND)) {
							if(_type.getStatusCode() == DLRStatusCode.ACTOR_SUBJECT) {
								createDummyStartNode(_cameFrom,_flow);
								buildGateway(_cameFrom, _openSplit, _base,_processed, _flow, _conjoined,FlowType.concurrency);
							}else {								
								if(_cameFrom.size() == 1) {
									//simple sequence case 
									handleSingleAction(_base,_flow, _conjoined.get(0), _cameFrom,_openSplit);
									_processed.add(_conjoined.get(0));
								}else if(_cameFrom.size() > 1) {
									buildJoin(_flow,_cameFrom, _conjoined.get(0));
									f_world.addFlow(_flow);
									_processed.add(_conjoined.get(0));
								}else { //camefrom.size == 0
									_cameFrom.add(_conjoined.get(0));
								}
							}
						}else if(_type.getType().equals(ConjunctionType.MIXED)) {
							//we need a special handling for more than 2 activities here
							createDummyStartNode(_cameFrom,_flow);
							if(_cameFrom.size() > 1) {
								_flow = joinOnDummyNode(_cameFrom, _base, _flow);
							}
							handleMixedSituation(_base,_flow,_conjs,_actions,_cameFrom);
							_processed.addAll(_actions);
						}
					}					
				}						
			}	
			for(Action a:_actions) {
				if(a.getLink() != null) {
					//"weak" link
					if(a.getLinkType() == ActionLinkType.FORWARD) {
						List<Action> _end = getEnd(a.getLink());
						if(_end.size()==1/* && _end.get(0).equals(a.getLink())*/) {
							//was not used before already!
							_cameFrom.add(_end.get(0));
						}
					}else if(a.getLinkType() == ActionLinkType.LOOP){ //"strong" link
						//building the "iteration" block now
						Action _link = a.getLink();
						Flow _fIn = findFlow(a,true);
						Flow _fOut = findFlow(a,false);
						DummyAction _da = new DummyAction(a);			
						f_world.addAction(_da);
						if(_fIn.getDirection() == FlowDirection.join) {
							_fIn.setSingleObject(_da);
							
							_fOut.setSingleObject(_da);
							_fOut.getMultipleObjects().add(a);
							_fOut.setType(FlowType.choice);
						}else {
							if(_fOut != null) {
								_fOut.setSingleObject(a);
							}
							_fIn.getMultipleObjects().add(_da);
							_fIn.setType(FlowType.choice);
							
						}
						
						//build a join in front of the link target
						_fIn = findFlow(_link, true);
						if(_fIn.getDirection() == FlowDirection.split) {
							DummyAction _da2 = new DummyAction(a);
							f_world.addAction(_da2);
							_fIn.getMultipleObjects().add(_da2);
							_fIn.getMultipleObjects().remove(_link);
							Flow _newFLow = new Flow(_base); //this will be the join
							_newFLow.setSingleObject(_link);
							_newFLow.getMultipleObjects().add(_da2);
							_newFLow.getMultipleObjects().add(a);
							_newFLow.setType(FlowType.choice);
							_newFLow.setDirection(FlowDirection.join);
							f_world.addFlow(_newFLow);
						}else {
							_fIn.getMultipleObjects().add(a);
						}
						if(_cameFrom.contains(a)) {
							_cameFrom.remove(a);		
							_cameFrom.add(_da);
						}
					}
				}
			}	
		}		
	}

	private Flow joinOnDummyNode(List<Action> cameFrom, T2PSentence base,Flow flow) {
		//building a join on a dummy node
		DummyAction _da = new DummyAction(cameFrom.get(0));
		f_world.addAction(_da);
		buildJoin(flow,cameFrom, _da);
		f_world.addFlow(flow);
		//creating new flow starting from join
		flow = new Flow(base);
		flow.setSingleObject(_da);
		return flow;
	}

	/**
	 * special case where an and split and something else (or, and/or) are joined in one sentence
	 * "A and B do C or D". In such a case we have to build the and-splt first and then 2 or-splits.
	 * @param from 
	 * 
	 * @param _base
	 * @param _flow
	 * @param _conjs
	 * @param _actions
	 */
	private void handleMixedSituation(T2PSentence base, Flow flow, List<ConjunctionElement> conjs, List<Action> allActions, List<Action> from) {
		//first, split up on dummy nodes for each and
		HashSet<Actor> _actors = new HashSet<Actor>();
		for(ConjunctionElement l:new ArrayList<ConjunctionElement>(conjs)) {
			if(l.getType() == ConjunctionType.AND) {
				if(l.getFrom() instanceof Actor)
					_actors.add((Actor)l.getFrom());
				if(l.getTo() instanceof Actor)
					_actors.add((Actor)l.getTo());
				conjs.remove(l);
			}
		}
		//we collected all Actors
		ArrayList<Action> _exits = new ArrayList<Action>();
		ArrayList<Action> _entries = new ArrayList<Action>();
		for(Actor ac:_actors) {
			List<Action> _actions = getActionsFor(ac,allActions);
			//build one block for each actor
			DummyAction _daStart = new DummyAction(_actions.get(0));
			f_world.addAction(_daStart);
			_entries.add(_daStart);
			DummyAction _daEnd = new DummyAction(_actions.get(0));
			f_world.addAction(_daEnd);
			_exits.add(_daEnd);			
			buildBlock(base,_daStart,_daEnd,_actions,conjs);			
		}
		
		flow.setMultipleObjects(_entries);
		flow.setType(FlowType.concurrency);
		f_world.addFlow(flow);
		

		DummyAction _da = new DummyAction(allActions.get(0));
		f_world.addAction(_da);
		
		Flow _join = new Flow(base);
		_join.setDirection(FlowDirection.join);
		_join.setType(FlowType.concurrency);
		_join.setMultipleObjects(_exits);
		_join.setSingleObject(_da);
		f_world.addFlow(_join);
		
		from.clear();
		from.add(_da);	
	}

	/**
	 * @param base 
	 * @param endPoint 
	 * @param _da
	 * @param _actions
	 * @param conjs
	 */
	private void buildBlock(T2PSentence base, DummyAction startingPoint, DummyAction endPoint, List<Action> actions,List<ConjunctionElement> conjs) {
		Flow _split = new Flow(base);
		Flow _join = new Flow(base);
		_join.setDirection(FlowDirection.join);
		_split.setSingleObject(startingPoint);
		_join.setSingleObject(endPoint);
		if(conjs.get(0).getType() == ConjunctionType.ANDOR) {
			_split.setType(FlowType.multiChoice);
			_join.setType(FlowType.multiChoice);
		}else{
			_split.setType(FlowType.choice);
			_join.setType(FlowType.choice);
		}
		_split.setMultipleObjects(actions);
		_join.setMultipleObjects(actions);
		f_world.addFlow(_split);
		f_world.addFlow(_join);
	}

	/**
	 * retrieves all actions of the List where "ac" is the ActorFrom
	 * @param ac
	 * @param allActions
	 * @return
	 */
	private List<Action> getActionsFor(Actor ac, List<Action> allActions) {
		ArrayList<Action> _result = new ArrayList<Action>();
		for(Action action:allActions) {
			if(action.getActorFrom().equals(ac)) {
				_result.add(action);
			}
		}
		return _result;
	}

	private void createDummyStartNode(List<Action> _cameFrom, Flow flow) {
		if(_cameFrom.size() == 0) {
			//we need a starting point
			DummyAction _da = new DummyAction();
			f_world.addAction(_da);
			_cameFrom.add(_da);
			flow.setSingleObject(_da);
		}
	}

	private void buildGateway(List<Action> cameFrom, List<Action> openSplit,
			T2PSentence base, ArrayList<Action> processed, Flow flow,
			ArrayList<Action> gatewayActions,FlowType type) {
		if("if".equals(gatewayActions.get(0).getMarker()) || "otherwise".equals(gatewayActions.get(0).getPreAdvMod()) ||
				Constants.f_sequenceIndicators.contains(gatewayActions.get(0).getPreAdvMod())){ 
			if(!Constants.f_sequenceIndicators.contains(gatewayActions.get(0).getPreAdvMod())) {
				openSplit.clear();
			}
			if(f_lastSplit != null) {
				//adding gateway block in a sub branch of this if/else split
				DummyAction _da = new DummyAction(gatewayActions.get(0));
				f_world.addAction(_da);
				openSplit.addAll(getEnds(f_lastSplit.getMultipleObjects())); //marking all open ends	
				f_lastSplit.getMultipleObjects().add(_da);
				cameFrom.clear();
				cameFrom.add(_da);
				flow.setSingleObject(_da);
			}			
			//clear in case of an otherwise, as it is the last action
			if("otherwise".equals(gatewayActions.get(0).getPreAdvMod())) {
				cameFrom.addAll(openSplit);		
				clearSplit(openSplit);
			}
		}else {
			//clearing open split
			cameFrom.addAll(openSplit);		
			clearSplit(openSplit);	
		}
		//we need to do this with an xor
		if(cameFrom.size() > 1) {
			flow = joinOnDummyNode(cameFrom, base, flow);
		}
		flow.setMultipleObjects(gatewayActions);
		flow.setType(type);
		cameFrom.clear();
		for(Action a:gatewayActions) {
			if(!hasIncomingLink(a, ActionLinkType.JUMP)) {
				cameFrom.add(a);
			}else {
				System.out.println("Left out action, due to JUMP link!");
			}			
		}	
		//it could happen that we cleaned all branches!
		if(cameFrom.size() == 0) {
			DummyAction _da = new DummyAction(gatewayActions.get(0));
			f_world.addAction(_da);
			flow.getMultipleObjects().add(_da);
			cameFrom.add(_da);
		}
		processed.addAll(gatewayActions);
		f_world.addFlow(flow);
	}	
	
	/**
	 * returns a boolean indicating if a new flow was created or not
	 * @param _base 
	 * @param openSplit 
	 */
	private void handleSingleAction(T2PSentence _base, Flow flow,Action action,List<Action> cameFrom, List<Action> openSplit) {
		if(!"if".equals(action.getMarker()) && !"otherwise".equals(action.getPreAdvMod()) && !Constants.f_sequenceIndicators.contains(action.getPreAdvMod()) && openSplit.size()>0) {
			//finishing all the open ends
			cameFrom.addAll(openSplit);		
			clearSplit(openSplit);
		}
		if(cameFrom.size() == 0) {
			createDummyStartNode(cameFrom, flow);		
		}
		if(cameFrom.size() >= 1) {			
			if(cameFrom.size() > 1)  {
				Flow _dummyFlow = new Flow(_base);
				DummyAction _da = new DummyAction(action);
				f_world.addAction(_da);
				buildJoin(_dummyFlow,cameFrom,_da);
				clearSplit(openSplit);
				f_world.addFlow(_dummyFlow);
			}
			//do we need to create something in parallel
			if("while".equals(action.getMarker())) {
				if(f_world.getLastFlowAdded() != null) {
					f_world.getLastFlowAdded().getMultipleObjects().add(action);
					f_world.getLastFlowAdded().setType(FlowType.concurrency);
					cameFrom.add(action);
					return;
				}
			}
			//do we need to create a split?
			else if("whereas".equals(action.getMarker()) || "if".equals(action.getMarker()) || "otherwise".equals(action.getPreAdvMod())) {
				if("except".equals(action.getPrepc())) {
					flow.setType(FlowType.exception);	
					clearSplit(openSplit);
				}else {
					if(f_lastSplit != null || "whereas".equals(action.getMarker()) || "otherwise".equals(action.getPreAdvMod())) {
						if(f_lastSplit == null && ("whereas".equals(action.getMarker()) || "otherwise".equals(action.getPreAdvMod()))) {
							f_lastSplit = f_world.getLastFlowAdded();
						}
						//okay, we can add our flow here
						openSplit.clear();
						openSplit.addAll(getEnds(f_lastSplit.getMultipleObjects())); //marking all open ends
						f_lastSplit.getMultipleObjects().add(action);
						cameFrom.clear();
						cameFrom.add(action); //setting new came from 
						if("whereas".equals(action.getMarker())) {
							cameFrom.addAll(openSplit);		
							clearSplit(openSplit);
						}
						return;
					}
					flow.setType(FlowType.choice);
					f_lastSplit = flow;
				}
				flow.setSingleObject(cameFrom.get(0));
				ArrayList<Action> _actions = new ArrayList<Action>();
				_actions.add(action);
				flow.setMultipleObjects(_actions); //is only 1 action
				cameFrom.clear();
				cameFrom.add(action); //setting new came from				
			}else {
				//standard sequence
				standardSequence(cameFrom, flow, action);	
				if(!Constants.f_sequenceIndicators.contains(action.getPreAdvMod()))
					clearSplit(openSplit);
			}
			f_world.addFlow(flow);
		}
	}

	private void clearSplit(List<Action> openSplit) {
		f_lastSplit = null;
		openSplit.clear();
	}


	/**
	 * @param multipleObjects
	 * @return
	 */
	private List<Action> getEnds(List<Action> multipleObjects) {
		ArrayList<Action> _result = new ArrayList<Action>();
		for(Action a:multipleObjects) {
			if(!hasIncomingLink(a, ActionLinkType.JUMP)) {
				_result.addAll(getEnd(a));
			}else {
				System.out.println("Left out action, due to JUMP link!");
			}
		}
		return _result;
	}

	/**
	 * @param a
	 * @return
	 */
	private List<Action> getEnd(Action a) {
		for(Flow f:f_world.getFlows()) {
			if(f.getDirection() == FlowDirection.split) {
				if(f.getSingleObject().equals(a)) {
					return getEnds(f.getMultipleObjects());
				}
			}else {
				if(f.getMultipleObjects().contains(a)) {
					return getEnd(f.getSingleObject());
				}
			}
		}
		ArrayList<Action> _result = new ArrayList<Action>();
		_result.add(a);
		return _result;
	}

	/**
	 * @param action
	 * @return
	 
		private boolean hasLinks(Action action) {
			for(Action a:f_world.getActions()) {
				if(a.getLink()!= null && a.getLink().equals(action)) {
					return true;
				}
			}
			return false;
	}*/

	private void buildJoin(Flow _flow,List<Action> cameFrom ,Action action) {
		//several actions in came from -> make a join
		_flow.setDirection(FlowDirection.join);
		_flow.setSingleObject(action);
		_flow.setMultipleObjects(new ArrayList<Action>(cameFrom));
		Flow _otherFlow = findSplit(cameFrom.get(0));
		if(_otherFlow != null) {
			_flow.setType(_otherFlow.getType());
		}
		cameFrom.clear();
		cameFrom.add(action);	
		//f_lastSplit = null;		
	}

	/**
	 * back-tracing mechanism to find the corresponding split for a join
	 * @param action
	 * @return
	 */
	private Flow findSplit(Action action) {
		Flow f = findFlow(action,true);
		if(f != null && f.getDirection().equals(FlowDirection.split)) {
			if(!f.getType().equals(FlowType.sequence)) {
				return f;
			}//else{
			return findSplit(f.getSingleObject());
			//}				
		}		
		return null;
	}
	
	private Flow findFlow(Action action,boolean target) {
		ArrayList<Flow> _lookHere = new ArrayList<Flow>(f_world.getFlows());
		Collections.reverse(_lookHere);
		for(Flow f:_lookHere) {
			if(target ^ f.getDirection() == FlowDirection.join) { //XOR fixes the need to have another if split....
				if(f.getMultipleObjects().contains(action)) {
					return f;
				}			
			}else {
				if(f.getSingleObject().equals(action)) {
					return f;
				}
			}
		}
		return null;
	}	

	private DetermineConjResult determineConjunctElements(List<ConjunctionElement> conjunctions, Action action,
			List<Action> resultList, List<Action> actions) {
		ConjunctionType _type = null;
		resultList.add(action);
		DLRStatusCode _status = DLRStatusCode.NOT_CONTAINED;
		for(ConjunctionElement l:conjunctions) {
			if((_type == null || _type.equals(l.getType()))) {
				_status = isPartOf(l.getFrom(),resultList);
				if(_status == DLRStatusCode.NOT_CONTAINED) {
					_status = isPartOf(l.getTo(),resultList);
				}
				if(_status != DLRStatusCode.NOT_CONTAINED){
					Action _link = SearchUtils.getAction(l.getTo(),actions);
					if(_link != null) {
						resultList.add(_link);
						if(_type == null) {
							_type = l.getType();
						}
					}else {
						System.err.println("unable to determine Action for Link: "+l.getTo());
					}
				}
			}else if(_type != null) {
				//this mean we have another type in here
				if(_type == ConjunctionType.AND && _status==DLRStatusCode.ACTOR_SUBJECT && l.getType()!=ConjunctionType.AND) {
					//okay this is serious
					_type = ConjunctionType.MIXED;
				}else {
					//we don't care
				}
			}
		}
		return new DetermineConjResult(_type,_status);
	}

	

	/**
	 * returns a status code to show the kind of relationship
	 * 0 - not contained
	 * 1 - contained as action
	 * 2 - contained as actor (subject)
	 * 3 - contained as actor (object)
	 * 4 - contained as resource 
	 * @param part
	 * @param resultList
	 * @return
	 */
	private DLRStatusCode isPartOf(SpecifiedElement part, List<Action> resultList) {
		if(part instanceof Action) {
			//simplest case
			Action _ac = (Action) part;
			if( resultList.contains(_ac)) {
				return DLRStatusCode.ACTION;
			}
			return DLRStatusCode.NOT_CONTAINED;
		}
		
		for(Action ac:resultList) {
			if(ac.getActorFrom() != null && ac.getActorFrom().equals(part)) {
				return DLRStatusCode.ACTOR_SUBJECT;
			}
			if(ac.getObject() != null && ac.getObject().equals(part)) {
				if(ac.getObject() instanceof Actor) {
					return DLRStatusCode.ACTOR_OBJECT;
				}
				return DLRStatusCode.RESOURCE;
			}
			
			
		}		
		return DLRStatusCode.NOT_CONTAINED;
	}

	private void standardSequence(List<Action> cameFrom, Flow flow,Action action) {
		flow.setType(FlowType.sequence);
		flow.setSingleObject(cameFrom.get(0));
		List<Action> _actions = new ArrayList<Action>();
		_actions.add(action);
		flow.setMultipleObjects(_actions); //is only 1 action
		cameFrom.clear();
		cameFrom.add(_actions.get(0)); //setting new came from 
	}

	/**
	 * @param a
	 * @param arrayList 
	 * @return
	 
	private Flow findFlowWithTarget(Action a, List<Flow> list) {
		for(Flow f:list) {
			if(f.getDirection() == FlowDirection.split) {
				if(f.getMultipleObjects().contains(a)) {
					return f;
				}
			}else {
				if(f.getSingleObject().equals(a)) {
					return f;
				}
			}
		}
		return null;
	}*/

	private void referenceResolution() {
		List<ExtractedObject> _toCheck = new ArrayList<ExtractedObject>(f_world.getActors());
		_toCheck.addAll(f_world.getResources());
		for(ExtractedObject a:_toCheck) {
			if(a.needsResolve()) {			
				if(Constants.DEBUG_REFERENCE_RESOLUTION) System.out.println("resolving:"+a);				
				//check manual resolutions
				SentenceWordID _swid = new SentenceWordID(a);
				if(f_referenceMap.containsKey(_swid)) {
					//was resolved manually before
					SentenceWordID _target = f_referenceMap.get(_swid);
					SpecifiedElement _t = toElement(_target);
					a.setReference(_t);
					if(Constants.DEBUG_REFERENCE_RESOLUTION) System.out.println("manual resolution: "+_t);
				}else {				
					//an actor can point to another actor (PRP) or a whole action (DT)
					int _id = a.getOrigin().getID();
					if(_id >= 0 ) {
						if(ProcessingUtils.isActionResolutionDeterminer(a.getName())) {
							Action _act = findAction(_id,a);
							a.setReference(_act);
							if(Constants.DEBUG_REFERENCE_RESOLUTION) System.out.println("resolution result: "+_act);
						}else {
							AnimateType _animate = determineAnimateType(a);
							Action _containingAction = SearchUtils.getAction(a, f_world.getActions(a.getOrigin()));
							boolean _invertRoleMatch = _containingAction.getCop()!= null || ProcessingUtils.isRCPronoun(a.getName());
							ExtractedObject _ref = findReference(_id,a,_animate,_invertRoleMatch );
							a.setReference(_ref);							
							if(Constants.DEBUG_REFERENCE_RESOLUTION) System.out.println("resolution result: "+_ref);
						}
					}
				}
			}
				
			
		}		
	}
	
	

	/**
	 * @param a
	 * @return
	 */
	private AnimateType determineAnimateType(ExtractedObject a) {
		if(a instanceof Resource) {
			return AnimateType.INANIMATE;
		}else {
			//a instaceof Actor
			if(((Actor)a).isUnreal()) {
				return AnimateType.INANIMATE;
			}else {
				if(ProcessingUtils.canBeObjectPronoun(a.getName())){
					return AnimateType.BOTH;
				}
			}
		}
		return AnimateType.ANIMATE;
		 
	}

	/**
	 * @param _target
	 * @return
	 */
	private SpecifiedElement toElement(SentenceWordID _target) {
		T2PSentence _origin = f_text.getSentence(_target.getSentenceID());
		ArrayList<SpecifiedElement> _toCheck = new ArrayList<SpecifiedElement>(f_world.getActions(_origin));
		for(SpecifiedElement a:new ArrayList<SpecifiedElement>(_toCheck)) {
			Action xc = ((Action)a).getXcomp();
			if(xc != null) {
				_toCheck.add(xc);				
			}
		}
		_toCheck.addAll(f_world.getActors(_origin));
		_toCheck.addAll(f_world.getResources(_origin));
		for(SpecifiedElement elem:_toCheck) {
			if(elem.getWordIndex() == _target.getWordID()) {
				return elem;
			}
		}
		System.err.println("Could not resolve the target of a manual resolution!!!!");
		return null;
	}

	/**
	 * 
	 */
	private void combineActions() {
		for(Action a:new ArrayList<Action>(f_world.getActions())) {
			Action _refAction = null;
			if(a.getActorFrom() != null && a.getActorFrom().getReference() instanceof Action) {
				_refAction = (Action) a.getActorFrom().getReference();
			}else if(a.getObject() != null && a.getObject().getReference() != null) {
				if(a.getObject().getReference() instanceof Action) {
					_refAction = (Action) a.getObject().getReference();
				}else {					
					_refAction =  SearchUtils.getAction(a.getObject().getReference(), f_world.getActions());
				}
			}
			if(_refAction != null) {
				if(canBeMerged(_refAction,a,false)) {
					if(Constants.DEBUG_MARKING) System.out.println("merging: -"+_refAction +"- and -"+a+"-");
					merge(_refAction,a,false);
				}else if(canBeMerged(_refAction,a,true)) {
					if(Constants.DEBUG_MARKING) System.out.println("copying attributes: -"+_refAction +"- to -"+a+"-");
					copy(_refAction,a);					
				}
			}			
		}		
	}

	/**
	 * @param action
	 * @param a
	 * @param b
	 */
	private void copy(Action strong, Action weak) {
		weak.setActorFrom(strong.getActorFrom());
		weak.setObject(strong.getObject());
		weak.setCop(strong.getCop(),strong.getCopIndex());
	}

	/**
	 * @param action
	 * @param a
	 */
	private void merge(Action a, Action b, boolean copyOnly) {
		Action _main = null;
		Action _mergeMe = null;
		WordNetFunctionality wnf = new WordNetFunctionality();
		if(wnf.isWeakAction(a)) {
			//merging a into b
			_mergeMe = a;
			_main = b;
		}else {
			_mergeMe = b;
			_main = a;
		}
		//action is merged
		if(_mergeMe.getActorFrom() != null) {
			if(_main.getActorFrom() == null) {				
				Actor _check = null;
				if(!_mergeMe.getActorFrom().needsResolve()) {
					_check = _mergeMe.getActorFrom();
				}else{
					if(_mergeMe.getActorFrom().getReference() instanceof Actor) {
						_check = (Actor) _mergeMe.getActorFrom().getReference();
					}
				}
				if(_check != null) {
					_main.setActorFrom(_mergeMe.getActorFrom());
				}								
			}else {
				if(_main.getActorFrom().needsResolve() && !_mergeMe.getActorFrom().needsResolve()) {
					_main.setActorFrom(_mergeMe.getActorFrom());
				}
			}
		}
		if(_main.getObject() == null || _main.getObject().needsResolve()) {
			if(_mergeMe.getObject() != null && !_mergeMe.getObject().needsResolve()) {
				_main.setObject(_mergeMe.getObject());
			}
		}
		
		for(Specifier spec:_mergeMe.getSpecifiers(SpecifierType.PP)) {
			_main.addSpecifiers(spec);
		}
		if(!copyOnly) {
			for(AnalyzedSentence s:f_analyzedSentences) {
				if(s.getExtractedActions().contains(_mergeMe)) {
					s.removeAction(_mergeMe);
				}
			}
			f_world.removeAction(_mergeMe);
		}
	}

	/**
	 * @param action
	 * @param a
	 * @return
	 */
	private boolean canBeMerged(Action a, Action b,boolean onlyShareProps) {
		if(onlyShareProps || (a.isNegated() == b.isNegated())) {
			WordNetFunctionality wnf = new WordNetFunctionality();
			if((wnf.isWeakAction(a) ) || (wnf.isWeakAction(b))) {
				if((a.getMarker()==null && b.getMarker()==null) || a.getMarker().equals(b.getMarker())) {
					if(a.getActorFrom() == null || a.getActorFrom().needsResolve() || a.getActorFrom().isMetaActor() ||
					   b.getActorFrom() == null || b.getActorFrom().needsResolve() || b.getActorFrom().isMetaActor()) {
						if(a.getObject() == null || a.getObject().needsResolve() /*|| ProcessingUtils.isMetaActor(a.getObject())*/ || 
						   b.getObject() == null || b.getObject().needsResolve() /*|| ProcessingUtils.isMetaActor(b.getObject())*/) {
							return true;
						}						
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 */
	private void markerDetection() {
		for(AnalyzedSentence sentence:f_analyzedSentences) {
			T2PSentence _base = sentence.getBaseSentence();
			Collection<TypedDependency> _deps = _base.getGrammaticalStructure().typedDependenciesCollapsed();
			List<TypedDependency> _markers = SearchUtils.findDependency("mark",_deps);
			for(TypedDependency td:_markers) {
				TreeGraphNode _lookFor = td.gov();
				Action _a = findAction(_lookFor,sentence.getExtractedActions(),_deps);
				if(_a != null) {
					String _val = td.dep().value().toLowerCase();
					if(Constants.DEBUG_MARKING)					
						System.out.println("marking: "+_a +" with (marker)"+_val);
					_a.setMarker(_val);	
				}
			}			
			_markers = SearchUtils.findDependency(ListUtils.getList("advmod"),_deps);
			for(TypedDependency td:_markers) {
				TreeGraphNode _lookFor = td.gov();
				Action _a = findAction(_lookFor,sentence.getExtractedActions(),_deps);
				if(_a != null && _a.getWordIndex()>td.dep().index()) {
					String _val = td.dep().value().toLowerCase();
					if(Constants.f_parallelIndicators.contains(_val)) {
						_a.setMarker("while");
					}else {
						if(!_val.equals("also")) { // make exclusion list?
							if(Constants.DEBUG_MARKING)
								System.out.println("marking: "+_a +" with (advmod) "+_val);
							_a.setPreAdvMod(_val,td.dep().index());
						}
					}
				}
			}
			_markers = SearchUtils.findDependency("prepc",_deps); //e.g. except
			for(TypedDependency td:_markers) {
				TreeGraphNode _lookFor = td.dep();
				Action _a = findAction(_lookFor,sentence.getExtractedActions(),_deps);
				if(_a != null) {
					if(Constants.DEBUG_MARKING)
						System.out.println("marking: "+_a +" with (prepc) "+td.reln().getSpecific().toLowerCase());
					_a.setPrepc(td.reln().getSpecific().toLowerCase());
				}
			}
			_markers = SearchUtils.findDependency("complm",_deps); //whether
			for(TypedDependency td:_markers) {
				if(!td.dep().value().equals("that")) {
					TreeGraphNode _lookFor = td.gov();
					Action _a = findAction(_lookFor,sentence.getExtractedActions(),_deps);
					if(_a != null) {
						String _val = td.dep().value().toLowerCase();
						if(Constants.f_conditionIndicators.contains(_val)) {
							_val = "if-complm";
						}
						if(Constants.DEBUG_MARKING)						
							System.out.println("marking: "+_a +" with (marker-complm) "+_val);
						_a.setMarker(_val);
					}
				}
			}
		}
		//determination of compound indicators in PP or SBAR phrases
		for(AnalyzedSentence sentence:f_analyzedSentences) {
			for(Action a:f_world.getActions(sentence.getBaseSentence())) {
				List<Specifier> _check = a.getSpecifiers(SpecifierType.PP);
				if(a.getObject() != null) {
					_check.addAll(a.getObject().getSpecifiers(SpecifierType.PP));
				}
				_check.addAll(a.getSpecifiers(SpecifierType.RCMOD));
				_check.addAll(a.getSpecifiers(SpecifierType.SBAR));
				for(Specifier spec:_check) {
					if(SearchUtils.startsWithAny(spec.getPhrase(),Constants.f_conditionIndicators)) {
						if(a.getMarker() == null) {
							if(Constants.DEBUG_MARKING)						
								System.out.println("marking: "+a +" with (marker) "+spec.getPhrase() +"(if)");
							a.setMarker("if");
							if(!Constants.f_conditionIndicators.contains(spec.getPhrase())) {
								//the indicator is just part of the whole phrase (and not the whole phrase itself)
								a.setMarkerFromPP(true);
							}
						}
					}
					for(String indic:Constants.f_sequenceIndicators) {
						if(spec.getPhrase().startsWith(indic)) {
							if(a.getPreAdvMod() == null) {
								a.setPreAdvMod(indic, spec.getWordIndex());
							}
						}
					}
					if(Constants.f_parallelIndicators.contains(spec.getPhrase())) {
						if(a.getMarker() == null) {
							if(Constants.DEBUG_MARKING)						
								System.out.println("marking: "+a +" with (marker) "+spec.getPhrase() +"(while)");
							a.setMarker("while");
						}
					}
				}		
			}
		}
		//now setting implicit markers (if no marker set so far)
		String _nextMark = null;
		List<Action> _linked = new ArrayList<Action>();
		for(AnalyzedSentence sentence:f_analyzedSentences) {
			_linked.clear();
			_nextMark = null;
			List<Action> _actions = f_world.getActions(sentence.getBaseSentence());
			for(Action a:_actions) {
				if(_nextMark != null) {
					if(a.getPreAdvMod() == null) {
						a.setPreAdvMod(_nextMark, -1);
						if(Constants.DEBUG_MARKING)
							System.out.println("marking: "+a +" with implicit (advmod) "+_nextMark);
					}
				}
				if(!_linked.contains(a)) {
					_nextMark = null;
				}
				if((Constants.f_conditionIndicators.contains(a.getMarker()) && !a.isMarkerFromPP()) || 
					Constants.f_conditionIndicators.contains(a.getPreAdvMod())) {
					_nextMark = "then";					
					determineConjunctElements(sentence.getExtractedConjunctions(), a,_linked,_actions);					
				}				
			}
		}
		//propagation of signaling words through Conjunctions
		for(AnalyzedSentence sentence:f_analyzedSentences) {
			List<Action> _actions = sentence.getExtractedActions();
			for(Action a:_actions) {
				_linked = new ArrayList<Action>();
				/*DetermineConjResult _type = */determineConjunctElements(sentence.getExtractedConjunctions(), a,_linked,_actions);
				if(_linked.size() > 1 /*&& _type.getType().equals(ConjunctionType.AND)*/) {
					//propagate preadvmod
					for(int i = 1;i<_linked.size();i++) {
						Action _linkedAction = _linked.get(i);
						if(_linkedAction.getPreAdvMod() == null) {
							_linkedAction.setPreAdvMod(a.getPreAdvMod(), -1);
						}
					}
				}
			}
		}		
		//now that everything is marked, correct order in marked sentences
		for(AnalyzedSentence sentence:f_analyzedSentences) {
			List<Action> _actions = sentence.getExtractedActions();
			for(int i=0;i<_actions.size();i++) {
				Action a = _actions.get(i);
				if("if-complm".equals(a.getMarker())) {
					a.setMarker("if"); //removing complm for whether sentences
				}else if("if".equals(a.getMarker())) {	
					if(i>0) {
						//okay switch those items
						Action _switcher = _actions.get(i-1);
						_actions.set(i, _switcher);
						_actions.set(i-1, a);
						f_world.switchPositions(a,_switcher);
					}
					//only one if per sentence is treated
					//further ifs stay as they are!	
					break;
				}				
			}
		}		
	}
	
	

	/**
	 * @param deps 
	 * @param for1
	 * @param extractedActions
	 * @return
	 */
	private Action findAction(TreeGraphNode node, List<Action> list, Collection<TypedDependency> deps) {
		for(Action a:list) {
			if(a.getWordIndex() == node.index()) {
				return a;
			}
		}
		List<TypedDependency> _cops = SearchUtils.findDependency("cop",deps);
		for(TypedDependency td:_cops) {
			if(td.gov().equals(node)) {
				return findAction(td.dep(), list, deps);
			}
		}		
		return null;
	}

	
	/**
	 * for reference resolution
	 * @param _id
	 * @return
	 */
	private Action findAction(int id,SpecifiedElement forThis) {
		if(id < 0) return null;
		T2PSentence _lookHere = f_text.getSentence(id);
		List<Action> _act = new ArrayList<Action>(f_world.getActions(_lookHere));
		Collections.reverse(_act);
		for(Action a:_act) {
			if(_lookHere.equals(forThis.getOrigin())){
				if(a.getWordIndex() < forThis.getWordIndex()){
					return a;
				}
			}else {
				return a;
			}
		}
		return findAction(id -1,forThis);
	}

	
	/**
	 * @param a
	 * @param _animate
	 * @return
	 */
	private ExtractedObject findReference(int id,ExtractedObject resolveMe, AnimateType type, boolean copSentence) {		
		HashMap<ExtractedObject, Integer> _candidates = getReferenceCandidates(id,resolveMe,type,copSentence);
		int max = ROLE_MATCH_SCORE + (copSentence ? OBJECT_ROLE_SCORE :SUBJECT_ROLE_SCORE);
		while((id >=0) && getMaxScore(_candidates) < max) {
			max -= SENTENCE_DISTANCE_PENALTY;
			id--;
			HashMap<ExtractedObject, Integer> _newCand = getReferenceCandidates(id,resolveMe,type,copSentence);
			if(_newCand != null)
				_candidates.putAll(_newCand);
		}
		return getMaxScoreElements(_candidates);
		
	}
	
	/**
	 * @param _candidates
	 * @return
	 */
	private ExtractedObject getMaxScoreElements(HashMap<ExtractedObject, Integer> candidates) {
		Integer _score = Integer.MIN_VALUE;
		ExtractedObject _result = null;
		for(Entry<ExtractedObject, Integer> e:candidates.entrySet()) {
			if(e.getValue() >= _score) {
				if(e.getValue() > _score) {
					_score = e.getValue();
					_result = e.getKey();
				}else {
					if(e.getKey().getOrigin().getID() > _result.getOrigin().getID()) {
						_score = e.getValue();
						_result = e.getKey();
					}else {
						if(e.getKey().getOrigin().getID() == _result.getOrigin().getID()
								&& e.getKey().getWordIndex() > _result.getWordIndex()) {
							_score = e.getValue();
							_result = e.getKey();
						}
					}
				}
			}
		}
		return _result;
	}

	/**
	 * @param _candidates
	 * @return
	 */
	private int getMaxScore(HashMap<ExtractedObject, Integer> candidates) {
		Integer _result = Integer.MIN_VALUE;
		for(Entry<ExtractedObject, Integer> e:candidates.entrySet()) {
			if(e.getValue() > _result) {
				_result = e.getValue();
			}
		}
		return _result;
	}

	private HashMap<ExtractedObject,Integer> getReferenceCandidates(int id,ExtractedObject resolveMe, AnimateType type, boolean cop){
		if(id < 0) return null;
		T2PSentence _lookHere = f_text.getSentence(id);
		List<ExtractedObject> _toCheck = new ArrayList<ExtractedObject>(f_world.getActors(_lookHere));
		for(int i=0;i<_toCheck.size();i++) {
			Actor a = (Actor) _toCheck.get(i);
			//remove all Meta Actors
			if(a.isMetaActor()) {
				_toCheck.remove(i);
				i--;		
				continue;
			}
			//each is an Actor, we know that
			if(type == AnimateType.ANIMATE) {
				//remove everything which is not animated
				if(((Actor)a).isUnreal()) {
					_toCheck.remove(i);
					i--;
					continue;
				}
			}else {
				if(type == AnimateType.INANIMATE) {
					//remove everything which is animated
					if(!((Actor)a).isUnreal()) {
						_toCheck.remove(i);
						i--;
						continue;
					}
				}
			}
			
		}
		if(type != AnimateType.ANIMATE) {
			_toCheck.addAll(f_world.getResources(_lookHere));
		}
		Collections.reverse(_toCheck);
		//everybody in that list is a potential candidate
		HashMap<ExtractedObject, Integer> _result = new HashMap<ExtractedObject, Integer>();
		for(ExtractedObject exObj:_toCheck) {
			if(exObj.needsResolve()) {
				if(exObj.getReference() != null && exObj.getReference() instanceof ExtractedObject) {
					exObj = (ExtractedObject) exObj.getReference();
				}else {
					continue;
				}
			}
			if(_lookHere.equals(resolveMe.getOrigin())){
				//no Cataphoras!
				if((exObj.getWordIndex() > resolveMe.getWordIndex())){
					continue;
				}
			}
			//getting score based on sentence distance (dist(0)=0,dist(-1)=-2,dist(-2)=-4) (at penalty=2)
			Integer _score = ((id - resolveMe.getOrigin().getID())) * TextAnalyzer.SENTENCE_DISTANCE_PENALTY;
			if(exObj.isSubjectRole() == (cop ? !resolveMe.isSubjectRole():resolveMe.isSubjectRole())){
				//some syntactic role
				_score += TextAnalyzer.ROLE_MATCH_SCORE;
			}
			if(!cop && exObj.isSubjectRole()) {
				_score += TextAnalyzer.SUBJECT_ROLE_SCORE;
			}else if(cop && !exObj.isSubjectRole()) {
				_score += TextAnalyzer.OBJECT_ROLE_SCORE;
			}		
			_result.put(exObj, _score);				
		}
		return _result;
		
	}
	
	
	public void analyzeSentence(T2PSentence s, int sentenceNumber,boolean singleSentence) {		
		AnalyzedSentence _sentence = new AnalyzedSentence(s,sentenceNumber);
		_sentence.analyze(this, f_world);
		if(!singleSentence) {
			f_analyzedSentences.add(_sentence);
		}else {
			PrintUtils.printExtractedActions(_sentence);
		}
	}

	/**
	 * @return
	 */
	public Text getText() {
		return f_text;
	}

	public WorldModel getWorld() {
		return f_world;
	}

	/**
	 * @param index
	 * @return
	 */
	public AnalyzedSentence getAnalyzedSentence(int index) {
		return f_analyzedSentences.get(index);
	}

	/**
	 * @param sentenceWordID
	 * @param sentenceWordID2
	 */
	public void addManualReference(SentenceWordID resolveMe,SentenceWordID reference) {
		f_referenceMap.put(resolveMe, reference);
	}

	/**
	 * 
	 */
	public void clear() {
		f_referenceMap.clear();
	}

	/**
	 * @return
	 */
	public int getNumberOfReferences() {
		int _result = 0;
		for(Actor actor:f_world.getActors()) {
			if(actor.needsResolve()) _result++;
		}
		for(Resource res:f_world.getResources()) {
			if(res.needsResolve()) _result++;
		}
		return _result;		
	}
	
	/**
	 * @return
	 */
	public int getNumberOfLinks() {
		int _result = 0;
		for(Action action:f_world.getActions()) {
			if(action.getLink() != null){
				_result++;
			}
		}
		return _result;		
	}
	
}