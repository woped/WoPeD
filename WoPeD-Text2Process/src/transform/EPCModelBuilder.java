package transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nodes.Cluster;
import nodes.FlowObject;
import nodes.ProcessEdge;
import nodes.ProcessNode;
import models.EPCModel;
import models.ProcessModel;
import processing.WordNetWrapper;
import processing.FrameNetWrapper.PhraseType;
import tools.Configuration;
import worldModel.Action;
import worldModel.Actor;
import worldModel.ExtractedObject;
import worldModel.Flow;
import worldModel.Resource;
import worldModel.Specifier;
import worldModel.WorldModel;
import worldModel.Flow.FlowDirection;
import worldModel.Flow.FlowType;
import worldModel.Specifier.SpecifierType;
import epc.Association;
import epc.Connector;
import epc.ConnectorAND;
import epc.ConnectorOR;
import epc.ConnectorXOR;
import epc.Event;
import epc.File;
import epc.Function;
import epc.OrgCollection;
import epc.Organisation;
import epc.OrganisationCluster;
import epc.SequenceFlow;
import etc.Constants;
import etc.TextToProcess;

public class EPCModelBuilder extends ProcessModelBuilder {
	
private Configuration f_config = Configuration.getInstance();
	
	//Nodes
	private final boolean EVENTS_TO_LABELS = true;
	private final boolean REMOVE_LOW_ENTROPY_NODES = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_REMOVE_LOW_ENT_NODES));
	private final boolean HIGHLIGHT_LOW_ENTROPY = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_HIGHLIGHT_META_ACTIONS));
	//Labeling
	private final boolean ADD_UNKNOWN_PHRASETYPES = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_ADD_UNKNOWN_PT));
	private final int MAX_NAME_DEPTH = 3;
	//Model in General
	private final boolean BUILD_DATA_OBJECTS = "1".equals(f_config.getProperty(Constants.CONF_GENERATE_DATA_OBJECTS));
	
	private TextToProcess f_parent;
	
	private EPCModel f_model = new EPCModel("generated Model");
	
	
	private HashMap<Actor, String> f_ActorToName = new HashMap<Actor, String>();
	private HashMap<String, Organisation> f_NameToOrgCollection = new HashMap<String, Organisation>();		
	private HashMap<Action, FlowObject> f_elementsMap = new HashMap<Action, FlowObject>();
	private HashMap<FlowObject, Action> f_elementsMap2 = new HashMap<FlowObject,Action>();
	
	private ArrayList<FlowObject> f_notAssigned = new ArrayList<FlowObject>();
	private Organisation f_lastOrg = null;
	private OrganisationCluster f_mainOrg;
	private ArrayList<Event> f_events = new ArrayList<Event>();
	private HashMap<ProcessNode,String> f_CommLinks = new HashMap<ProcessNode, String>();
	private HashMap<String, OrgCollection> f_bbOrgcache = new HashMap<String, OrgCollection>();

	public EPCModelBuilder(TextToProcess parent) {
		f_parent = parent;
	}

	@Override
	public ProcessModel createProcessModel(WorldModel world) {
		f_mainOrg = new OrgCollection();
		f_model.addNode(f_mainOrg);
		
		createActions(world);		
		buildSequenceFlows(world);
		removeDummies(world);
		finishDanglingEnds();
		if(EVENTS_TO_LABELS) {
			eventsToLabels();
		}
		processMetaActivities(world);
		buildDataObjects(world);
		
		if(f_mainOrg.getProcessNodes().size() == 0) {
			f_model.removeNode(f_mainOrg);
		}
		f_parent.setElementMapping(f_elementsMap);
		return f_model;
	}

	@Override
	public void buildDataObjects(WorldModel world) {
		//to avoid double adding
		HashMap<Action,List<String>> _handeledDOs = new HashMap<Action, List<String>>();
		
		if(BUILD_DATA_OBJECTS) {
			for(ProcessNode a : new ArrayList<ProcessNode>(f_model.getNodes())) {
				if(!(a instanceof OrgCollection || a instanceof Organisation)) {
					List<ProcessNode> _succs = f_model.getSuccessors(a);
					for(ProcessNode n : new ArrayList<ProcessNode>(_succs)) {
						if(n instanceof Connector) {
							_succs.addAll(f_model.getSuccessors(n));
						}
					}
					Action _a = f_elementsMap2.get(a);
					Set<String> _candA = getDataObjectCandidates(_a);
					//now we got all pairs
					for(ProcessNode b:_succs) {
						Action _b = f_elementsMap2.get(b);
						Set<String> _candB = getDataObjectCandidates(_b);
						for(String s:_candB) {
							if(_candA.contains(s)) {
								//okay we need to generate this
								File _do = createFile(_a, s,false);
								//connect to other Node
								ProcessNode _target = f_elementsMap.get(_b);
								Association _asc = new Association(_do,_target);
								f_model.addEdge(_asc);	
								put(_handeledDOs,_a,s);
								put(_handeledDOs,_b,s);
							}
						}
					}				
					//checked all pairs, now check singles only
					for(String s:_candA) {
						if(_handeledDOs.get(_a) == null || !_handeledDOs.get(_a).contains(s)) {
							createFile(_a, s,true);
						}
					}
				}			
			}
		}
	}

	
	public File createFile(Action targetAc, String doName,
			boolean arriving) {
		ProcessNode _target = f_elementsMap.get(targetAc);
		Organisation _org = getOrganisationForNode(_target);
		if(_org != null) {
			File _do = new File (doName);
			f_model.addNode(_do);
			_org.addProcessNode(_do);			
			if(arriving) {
				Association _asc = new Association(_do,_target);
				f_model.addEdge(_asc);
			}else {
				Association _asc = new Association(_target,_do);
				f_model.addEdge(_asc);
			}
			return _do;
		}
		return null;
	}

	@Override
	public String getName(ExtractedObject a,boolean addDet,int level,boolean compact) {
		if(a == null) {
			return "null";
		}
		if(a.needsResolve() && a.getReference() instanceof ExtractedObject) {
			return getName((ExtractedObject)a.getReference(),addDet);
		}
		StringBuilder _b = new StringBuilder();
		if(addDet && Constants.f_wantedDeterminers.contains(a.getDeterminer())) {
			_b.append(a.getDeterminer());
			_b.append(' ');
		}
		for(Specifier s:a.getSpecifiers(SpecifierType.AMOD)) {
			_b.append(s.getName());
			_b.append(' ');
		}
		for(Specifier s:a.getSpecifiers(SpecifierType.NUM)) {
			_b.append(s.getName());
			_b.append(' ');
		}
		for(Specifier s:a.getSpecifiers(SpecifierType.NN)) {
			_b.append(s.getName());
			_b.append(' ');
		}		
		_b.append(a.getName());
		for(Specifier s:a.getSpecifiers(SpecifierType.NNAFTER)) {
			_b.append(' ');
			_b.append(s.getName());
		}
		if(level <= MAX_NAME_DEPTH)
		for(Specifier s:a.getSpecifiers(SpecifierType.PP)) {
			if(s.getPhraseType() == PhraseType.UNKNOWN && ADD_UNKNOWN_PHRASETYPES) {
				if(s.getName().startsWith("of") || 
						(!compact && s.getName().startsWith("into")) || 
						(!compact && s.getName().startsWith("under")) ||
						(!compact && s.getName().startsWith("about"))) {
					addSpecifier(level, _b, s,compact);
				}	
			}else if(considerPhrase(s)) {
				addSpecifier(level, _b, s,compact);
			}
			
		}
		if(!compact) {
			for(Specifier s:a.getSpecifiers(SpecifierType.INFMOD)) {
				_b.append(' ');
				_b.append(s.getName());
			}for(Specifier s:a.getSpecifiers(SpecifierType.PARTMOD)) {
				_b.append(' ');
				_b.append(s.getName());
			}
		}
		return _b.toString();
	}
	
	@Override
	protected String getName(ExtractedObject a,boolean addDet) {
		return getName(a, addDet, 1);
	}
	
	private String getName(ExtractedObject a,boolean addDet,int level) {
		return getName(a, addDet, level, false);
	}

	@Override
	public Map<ProcessNode, String> getCommLinks() {
		return f_CommLinks;
	}

	@Override
	protected void processMetaActivities(WorldModel world) {
		for(Action a:world.getActions()) {
			if(a.getActorFrom() != null && a.getActorFrom().isMetaActor()) {					
				if(WordNetWrapper.isVerbOfType(a.getName(),"end")){
					//found an end verb
					ProcessNode _pnode = f_elementsMap.get(a);
					List<ProcessNode> _succs = f_model.getSuccessors(_pnode);
//					boolean _allEnd = true;
//					for(ProcessNode n:_succs) {
//						if(!(n instanceof EndEvent)) {
//							_allEnd = false;
//							break;
//						}
//					}
//					if(_allEnd) {
						removeNode(a);
						if(a.getName().equals("terminate") && _succs.size()==1) {
							Event _ee = (Event) _succs.get(0);
						}
//					}					
				}else if(WordNetWrapper.isVerbOfType(a.getName(),"start")) {
					ProcessNode _pnode = f_elementsMap.get(a);
				}
			}
		}
	}

	@Override
	protected void removeDummies(WorldModel world) {
		for(Action a:world.getActions()) {
			if(a instanceof DummyAction || a.getTransient()) {
				removeNode(a);
			}else {
				if(f_elementsMap.get(a).getText().equals("Dummy")) {
					removeNode(a);
				}
			}
		}
	}

	@Override
	protected void eventsToLabels() {
		for(ProcessNode node : new ArrayList<ProcessNode>(f_model.getNodes())) {
			if(node instanceof Connector) {
				List<ProcessNode> _succs = f_model.getSuccessors(node);
				for(ProcessNode _succ : _succs) {
					if(_succ.getClass().getSimpleName().equals("Event")) { //only simple intermediate events
						List<ProcessNode> _succsIE = f_model.getSuccessors(_succ);
						if(_succsIE.size() == 1) {
							String _lbl = _succ.getName();
							SequenceFlow _newFlow = removeNode(_succ);
							_newFlow.setLabel(_lbl);
						}
					}else if(_succ instanceof Event) {
						Action _action = f_elementsMap2.get(_succ);
						List<Specifier> _specs = _action.getSpecifiers(SpecifierType.PP);
						if(_action.getActorFrom() != null) {
							_specs.addAll(_action.getActorFrom().getSpecifiers(SpecifierType.PP));
						}
						for(Specifier spec:_specs) {
							if(SearchUtils.startsWithAny(spec.getPhrase(),Constants.f_conditionIndicators)
									&& !Constants.f_conditionIndicators.contains(spec.getPhrase())) { //it should only be the start of a phrase, not the whole phrase!
								//found the phrase which can serve as a label
								SequenceFlow _sqf = (SequenceFlow) f_model.getConnectingEdge(node, _succ);
								_sqf.setLabel(spec.getPhrase());
								break;
							}
						}
						//}
						
					}
				}
			}
		}
	}

	protected SequenceFlow removeNode(Action a) {
		ProcessNode _node = toProcessNode(a);
//		if(f_model.getPredecessors(_node).size() == 0) {
//			//add a start node in front
//			StartEvent _start = new StartEvent();
//			f_model.addNode(_start);
//			Cluster _lane = f_model.getClusterForNode(_node);
//			if(_lane != null) {
//				_lane.addProcessNode(_start);
//			}
//			SequenceFlow _sqf = new SequenceFlow(_start,_node);
//			f_model.addFlow(_sqf);
	//	}
		return removeNode(_node);
	}

	/**
	 * removes a node from the model but keeps the predecessor and successor connected
	 * @param a
	 * 
	 */
	public SequenceFlow removeNode(ProcessNode node) {
		//remove this action and connect both nodes
		
		ProcessNode _pre = null;
		ProcessNode _succ = null;
		for(ProcessEdge edge:f_model.getEdges()) {
			if (edge.getTarget() != null && edge.getSource() != null){
				if(edge.getTarget().equals(node)) {
					_pre = edge.getSource();
				}
				if(edge.getSource().equals(node)) {
					_succ = edge.getTarget();
				}
			}
		}
		f_model.removeNode(node);
		if(_pre != null && _succ != null) {
			SequenceFlow _sqf = new SequenceFlow(_pre,_succ);
			f_model.addFlow(_sqf);
			return _sqf;
		}
		return null;
	}

	
	protected String createFunctionText(Action a) {
		StringBuilder _b = new StringBuilder();		
		if(a.isNegated()) {
			if(a.getAux() != null) {
				_b.append(a.getAux());
				_b.append(' ');
			}
			_b.append("not");
			_b.append(' ');
		}
		if(WordNetWrapper.isWeakAction(a) && canBeTransformed(a)) {
			if(a.getActorFrom() != null && a.getActorFrom().isUnreal() && hasHiddenAction(a.getActorFrom())) {
				_b.append(transformToAction(a.getActorFrom()));					
			}else if(a.getObject() != null && ((a.getObject() instanceof Resource) ||  !((Actor)a.getObject()).isUnreal())) {
				_b.append(transformToAction(a.getObject()));					
			}			
		}else {
			boolean _weak = WordNetWrapper.isWeakVerb(a.getName());
			if(!_weak) {
				_b.append(WordNetWrapper.getBaseForm(a.getName()));
				if(a.getPrt()!= null) {
					_b.append(' ');
					_b.append(a.getPrt());
				}
				_b.append(' ');
			}else {
				//a weak verb which cannot be transformed hmmm.....
				if(REMOVE_LOW_ENTROPY_NODES && (a.getActorFrom() == null || a.getActorFrom().isMetaActor()) &&
						a.getXcomp() == null) {
					return "Dummy";					
				}else {
					if(a.getXcomp() == null) { //hm we should add something here or the label is empty
						_b.append(getEventText(a));
						return _b.toString().replaceAll("  ", " ");
					}
				}
			}
			boolean _xCompAdded = false;
			boolean _modAdded = false;
			if(a.getObject() != null) {
				if(a.getMod() != null && (a.getModPos() < a.getObject().getWordIndex())) {
					addMod(a,_b);	
					_b.append(' ');
					_modAdded = true;
				}
				if(a.getXcomp()!= null && (a.getXcomp().getWordIndex() < a.getObject().getWordIndex())) {
					addXComp(a, _b,!_weak);
					_b.append(' ');
					_xCompAdded = true;
				}			
				if(a.getSpecifiers(SpecifierType.IOBJ).size() > 0) {
					for(Specifier spec:a.getSpecifiers(SpecifierType.IOBJ)) {
						_b.append(spec.getPhrase());
						_b.append(' ');
					}
				}
				_b.append(getName(a.getObject(),true));
				//
				for(Specifier _dob : a.getSpecifiers(SpecifierType.DOBJ)) {
					_b.append(' ');
					_b.append(getName(_dob.getObject(),true));
				}
				
			}			
			if(!_modAdded) {
				addMod(a, _b);
			}			
			if(!_xCompAdded && a.getXcomp() != null) {
				addSpecifiers(a, _b,a.getXcomp().getWordIndex(),true);
				addXComp(a, _b,!_weak || a.getObject() != null);
			}
			addSpecifiers(a, _b,getXCompPos(a.getXcomp()),false);
			if(!(a.getObject() == null)) { //otherwise addSpecifiers already did the work!
				for(Specifier sp:a.getSpecifiers(SpecifierType.PP)) {
					if((sp.getName().startsWith("to") || sp.getName().startsWith("in") || sp.getName().startsWith("about"))
							&& !(SearchUtils.startsWithAny(sp.getPhrase(), Constants.f_conditionIndicators)) && !(SearchUtils.startsWithAny(sp.getPhrase(), Constants.f_parallelIndicators))) {
						_b.append(' ');
						if(sp.getObject() != null) {
							_b.append(sp.getHeadWord());
							_b.append(' ');
							_b.append(getName(sp.getObject(),true));
						}else {
							_b.append(sp.getName());
							break; // one is enough
						}						
					}
				}
			}
		}
		return _b.toString().replaceAll("  ", " ");
	}

	@Override
	protected boolean considerPhrase(Specifier spec) {
		if(spec.getPhraseType() == PhraseType.PERIPHERAL || spec.getPhraseType() == PhraseType.EXTRA_THEMATIC) {
			return false;
		}else {
			if(spec.getPhraseType() == PhraseType.UNKNOWN && ADD_UNKNOWN_PHRASETYPES) {
				return true;
			}
		}
		return true; //always accept core and genetive
	}
	
	private Organisation getOrganisationForNode(ProcessNode node) {
		for(Cluster c: node.getParentClusters()) {
			if(c instanceof Organisation) {
				return (Organisation)c;
			}
		}
		return null;
	}
	
	private FlowObject toProcessNode(Action a) {
		
		FlowObject _obj = f_elementsMap.get(a);
		if(_obj == null) {
			if(a instanceof DummyAction) {
				Function _t = new Function();
				_t.setText("Dummy Task");
				f_elementsMap.put(a, _t);
				f_elementsMap2.put(_t, a);
				return _t;
			}
			System.out.println("error no FlowObject found!");
		}
		return _obj;
	}
	
	private void createActions(WorldModel world) {
		for(Action a:world.getActions()) {
			FlowObject _obj;
			if(!("if".equals(a.getMarker())) || a.isMarkerFromPP()) {
				_obj = createFunction(a);
			}else {				
				_obj = createEventNode(a);
				f_model.addFlowObject(_obj);
				_obj.setText(getEventText(a)+" "+_obj.getText());
			}
			if(HIGHLIGHT_LOW_ENTROPY) {
				if(a.getActorFrom() != null && a.getActorFrom().isMetaActor()); //{
				//	_obj.setBackground(Color.YELLOW);
			//	}
			}
			
			f_elementsMap.put(a,_obj);
			if(a.getXcomp() != null)
				f_elementsMap.put(a.getXcomp(), _obj);
			f_elementsMap2.put(_obj, a);
			Organisation _p = null;
			if(!WordNetWrapper.isWeakAction(a)) {
				_p = getOrg(a.getActorFrom());
			}
			if(_p == null) {
				if(f_lastOrg == null) {
					f_notAssigned.add(_obj);
				}else {
					f_lastOrg.addProcessNode(_obj);
				}
			}else {
				_p.addProcessNode(_obj);
				f_lastOrg = _p;
				if(f_notAssigned.size() > 0) {
					for(FlowObject fo:f_notAssigned) {
						f_lastOrg.addProcessNode(fo);
					}
					f_notAssigned.clear();
				}
			}
			if (_obj instanceof Event){
				Event e = (Event) _obj;
				f_model.addEvent(e);
			}
		}
	}
	
	/**
	 * @param actorFrom
	 * @return
	 */
	private Organisation getOrg(Actor a) {
		Actor original = a;
		if(a == null) {
			return null;
		}
		if(a.needsResolve()) {
			if(a.getReference() instanceof Actor) {
				a = (Actor) a.getReference();
			}else {
				return null;
			}
		}
		if(!a.isUnreal() && !a.isMetaActor() && original.isSubjectRole()) {			
			String _name = getName(a,false);
			f_ActorToName.put(a, _name);
			
			if(!f_NameToOrgCollection.containsKey(_name)) {
				Organisation _org = new Organisation(_name,100,f_mainOrg);
				f_mainOrg.addOrganisation(_org);
				f_model.addNode(_org);
				f_NameToOrgCollection.put(_name, _org);
				return _org;
			}
			return f_NameToOrgCollection.get(_name);
		}
		return null;
	}
	
	private Function createFunction(Action a) {
		Function _result = new Function();		
		String _name = createFunctionText(a);
		_result.setText(_name);
		_result.setAdvMod(a.getPreAdvMod());
		f_model.addFlowObject(_result);
		return _result;
	}
	
	private Event createEventNode(Action a) {
		for(Specifier spec:a.getSpecifiers()) {
			for(String word:spec.getPhrase().split(" ")) {
				if(WordNetWrapper.isTimePeriod(word)) {
					Event _result = new Event();
					_result.setText(spec.getPhrase());
					return _result;
				}
			}
		}
		
		if(WordNetWrapper.isVerbOfType(a.getName(), "send") || WordNetWrapper.isVerbOfType(a.getName(), "receive") /*isInteractionVerb(a)*/) {
			Event _mie = new Event();
			if(WordNetWrapper.isVerbOfType(a.getName(),"send")) {
				//_mie.setProperty(MessageIntermediateEvent.PROP_EVENT_SUBTYPE, MessageIntermediateEvent.EVENT_SUBTYPE_THROWING);
			}
			return _mie;
		}
		return new Event();
	}
	
	private void buildSequenceFlows(WorldModel world) {
		for(Flow f:world.getFlows()) {
			if(f.getType() == FlowType.sequence && f.getMultipleObjects().size() == 1) {
				SequenceFlow _sqf = new SequenceFlow();
				_sqf.setSource(toProcessNode(f.getSingleObject()));
				_sqf.setTarget(toProcessNode(f.getMultipleObjects().get(0)));
				f_model.addEdge(_sqf);
			}else {
				if(f.getType() == FlowType.exception) {
					Event _exc = new Event();
					f_model.addFlowObject(_exc);
					ProcessNode _task = (ProcessNode) toProcessNode(f.getSingleObject()); //should be a task
					_exc.setParentNode(_task);
					addToSameOrganisation(_task, _exc);		
					
					SequenceFlow _sqf = new SequenceFlow();
					_sqf.setSource(_exc);
					_sqf.setTarget(toProcessNode(f.getMultipleObjects().get(0)));
					f_model.addEdge(_sqf);
				}else {
					//is it a split?
					//create flow from single to gateway
					Connector _conn = createConnector(f);
					SequenceFlow _sf1 = new SequenceFlow();
					if(f.getDirection() == FlowDirection.split) {
						_sf1.setSource(toProcessNode(f.getSingleObject()));
						_sf1.setTarget(_conn);
						addToPrevalentOrg(f,_conn);
						for(Action action:f.getMultipleObjects()) {
							SequenceFlow _sf = new SequenceFlow();
							_sf.setSource(_conn);
							_sf.setTarget(toProcessNode(action));
							f_model.addEdge(_sf);
						}					
					}else if(f.getDirection() == FlowDirection.join) {
						_sf1.setSource(_conn);
						_sf1.setTarget(toProcessNode(f.getSingleObject()));
						addToPrevalentOrg(f,_conn);
						for(Action action:f.getMultipleObjects()) {
							SequenceFlow _sf = new SequenceFlow();
							_sf.setSource(toProcessNode(action));
							_sf.setTarget(_conn);
							f_model.addEdge(_sf);
						}	
					}
					f_model.addEdge(_sf1);
				}
			}
		}
	}
	
	/**
	 * @param source
	 * @param _gate
	 */
	private void addToSameOrganisation(ProcessNode source, FlowObject _gate) {
		Organisation _o = getOrganisationForNode(source);
		if(_o != null) {
			_o.addProcessNode(_gate);
		}
	}
	
	/**
	 * @param f
	 * @return
	 */
	private Connector createConnector(Flow f) {
		Connector _result;
		if(f.getType() == FlowType.concurrency) {
			_result = new ConnectorAND();
		}else if(f.getType() == FlowType.multiChoice) {
			_result = new ConnectorOR();
		}else {
			for(Action a: f.getMultipleObjects()) {
				ProcessNode _node = f_elementsMap.get(a);
				if(_node instanceof Event && !(_node.getClass() == Event.class)) {
					
				}else {
					//not all special events
					_result = new ConnectorXOR();
					f_model.addNode(_result);
					return _result;
				}
			}
			//all are special events (message, timer etc.)
			_result = new Connector();
		}
		f_model.addNode(_result);
		return _result;
	}
	
	/**
	 * @param f
	 * @param gate
	 */
	private void addToPrevalentOrg(Flow f, Connector conn) {
		HashMap<Organisation, Integer> _countMap = new HashMap<Organisation, Integer>();
		if(!(f.getSingleObject() instanceof DummyAction)) {
			Organisation _org1 = getOrganisationForNode(toProcessNode(f.getSingleObject()));
			_countMap.put(_org1, 1);
		}
		for(Action a:f.getMultipleObjects()) {
			if(!(a instanceof DummyAction)) {
				Organisation _org = getOrganisationForNode(toProcessNode(a));
				if(_countMap.containsKey(_org)) {
					_countMap.put(_org,_countMap.get(_org)+1);
				}else {
					_countMap.put(_org, 1);
				}
			}
		}
		Organisation _best = (Organisation) SearchUtils.getMaxCountElement(_countMap);
		//if best is null, there simply is no lane in the process!
		if(_best != null) {
			_best.addProcessNode(conn);
		}
	}
	
	@Override
	protected void addXComp(Action a, StringBuilder _b,boolean needsAux) {
		if(a.getXcomp() != null) {
			if(needsAux) {
				if(a.getXcomp().getAux() != null) {
					_b.append(' ');
					_b.append(a.getXcomp().getAux());
					_b.append(' ');
				}else {
					_b.append(" to ");
				}
			}
			_b.append(createFunctionText( a.getXcomp()));
		}		
	}
	
	/**
	 * 
	 */
	private void finishDanglingEnds() {
		for(ProcessNode node : new ArrayList<ProcessNode>(f_model.getNodes())) {
			if(node instanceof Function || node instanceof Connector || node instanceof Event) {
				//has to be the source somewhere
				int _inC = 0;
				int _outC = 0;
				for(ProcessEdge e:f_model.getEdges()) {
					if (e.getSource() != null){
						if(e.getSource().equals(node)) {
							_outC++;
						}else if (e.getTarget().equals(node)) {
							_inC++;
						}	
					}
				}
				if((_outC == 0) || (node instanceof Connector && _inC == 1 && _outC == 1)) {
					//need to finish this one
					Event _end = new Event();
					f_model.addNode(_end);
					addToSameOrganisation(node, _end);
					SequenceFlow _sqf = new SequenceFlow(node,_end);
					f_model.addFlow(_sqf);	
				}				
				if(_inC == 0) {
					if(node instanceof Event) {
						Event _ime = (Event) node;
						if(_ime.getParentNodeId() != null) {
							//it s an attached event
							continue;
						}
					}
					Event _start = new Event();
					f_model.addNode(_start);
					addToSameOrganisation(node, _start);
					SequenceFlow _sqf = new SequenceFlow(_start,node);
					f_model.addFlow(_sqf);	
				}
			}
		}
	}

	@Override
	public void layoutModel(ProcessModel _result) {
		// TODO Auto-generated method stub
		
	}
}
