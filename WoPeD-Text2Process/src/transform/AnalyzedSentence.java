/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;




import etc.Constants;
import processing.ProcessingUtils;
import text.T2PSentence;
import tools.Configuration;
import transform.ConjunctionElement.ConjunctionType;
import worldModel.Action;
import worldModel.Actor;
import worldModel.ExtractedObject;
import worldModel.Resource;
import worldModel.SpecifiedElement;
import worldModel.Specifier;
import worldModel.WorldModel;
import worldModel.Specifier.SpecifierType;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TypedDependency;

public class AnalyzedSentence {
		
	private T2PSentence f_sentence;
	private int f_sentenceNumber;
	
	private Tree f_root;
	private List<Tree> f_fullSentence;
	private Collection<TypedDependency> f_dependencies;
	private WorldModel f_world;

	private ArrayList<String> f_sentenceTags = new ArrayList<String>(3);


	private ArrayList<ConjunctionElement> f_conjs = new ArrayList<ConjunctionElement>();
	private ArrayList<Action> f_actions = new ArrayList<Action>();
	private final boolean f_ignoreNPSubSentences = "1".equals(Configuration.getInstance().getProperty(Constants.CONF_GENERATE_IGNORE_SBAR_ON_NP));
	
	
	
	/**
	 * @param s
	 * @param sentenceNumber
	 */
	public AnalyzedSentence(T2PSentence sentence, int sentenceNumber) {
		f_sentence = sentence;
		setSentenceNumber(sentenceNumber);
		f_sentenceTags.add("S");
		f_sentenceTags.add("SBAR");
		f_sentenceTags.add("SINV");
	}
	
	
	public void analyze(@SuppressWarnings("unused") TextAnalyzer textA, WorldModel world) {
		f_world = world;
		//main objects which we need
		f_root = f_sentence.getTree();
		//filterPRN(f_root);
		
		f_fullSentence = f_root.getLeaves();
		f_dependencies = f_sentence.getGrammaticalStructure().typedDependenciesCollapsed();
				
		Tree _mainSentence = f_root.getChild(0); //this is always the case
		analyzseSentence(_mainSentence,f_dependencies);	
		checkGlobalConjunctions();		
		Collections.sort(f_actions);	
		//add all actions to the world
		for(Action a:f_actions) {
			f_world.addAction(a);
		}
		if(Constants.DEBUG_EXTRACTION_FINAL) {
			PrintUtils.printExtractedActions(this);
		}
	}


	
	
	/**
	 * @param f_root2
	 */
	@SuppressWarnings("unused")
	private void filterPRN(Tree rootNode) {
		for(int i=0;i<rootNode.children().length;i++) {
			Tree _t = rootNode.children()[i];
			if(_t.value().equals("PRN")) {
				rootNode.removeChild(i);
				i--;
			}else {
				filterPRN(_t);
			}
		}
	}


	/**
	 *  determines if there are conjunctions connecting Actions over sub-sentence boundaries
	 */
	private void checkGlobalConjunctions() {
		//checking global sentence conjunctions
		List<TypedDependency> _conj = SearchUtils.findDependency("conj", f_dependencies);
		if(_conj.size() > 0) {
			for(TypedDependency td:_conj) {
				Action _a = getActionContaining(td.gov());
				Action _b = getActionContaining(td.dep());
				if(_a != null && _b != null) {				
					buildLink(_a, td, _b);				
				}
			}				
		}
	}


	/**
	 * @param gov
	 * @return
	 */
	private Action getActionContaining(TreeGraphNode gov) {
		for(Action a:f_actions) {
			if(a.getWordIndex() == gov.index() 
					|| (a.getXcomp() != null && a.getXcomp().getWordIndex() == gov.index())
					|| (a.getCopIndex() == gov.index())
					|| (a.getObject() != null && a.getObject().getWordIndex() == gov.index())
					/*|| (a.getObject() != null && a.getObject().getWordIndex() == gov.index())*/) {
				return a;
			}
		}
		return null;
	}


	private void analyzseSentence(Tree _mainSentence,Collection<TypedDependency> dependencies ) {		
		//checking if the sentence contains several sub-sentences
		int _sCount = determineSubSentenceCount(_mainSentence);
		if(_sCount == 0) {
			//no, this is a simple sentence, it okay to proceed
			extractElements(_mainSentence,dependencies);
		}else if(_sCount == 1) {
			//we have a sub-sentence with something surrounding it here
			//analyzing on a lower level
			Tree _subSentence = findSubSentences(_mainSentence).get(0);
			List<TypedDependency> _dependenciesFiltered =  filterDependencies(_subSentence,dependencies);
			analyzseSentence(_subSentence,_dependenciesFiltered);
			//it only was a relative part - maybe something that created a condition
			Tree _copy = _mainSentence.deepCopy();
			removeChildInCopy(_mainSentence,_copy,_subSentence);
			List<TypedDependency> _fd = new ArrayList<TypedDependency>(dependencies);
			removeAll(_fd,_dependenciesFiltered);
			if(SearchUtils.findDependency(ListUtils.getList("nsubj","agent","nsubjpass","dobj"), _fd).size()>0) {
				//but, only check if it has information in it!
				extractElements(_copy, _fd);
			}
		}else {
			//we have to deal with a complex sentence
			//splitting it up and process each part individually
			List<Tree> _sentencesParts = SearchUtils.findChildren(f_sentenceTags,_mainSentence);
			int _startIndex = 0;
			int _endIndex = 0;
			for(Tree sentP:_sentencesParts) {
				List<Tree> _partSentence = sentP.getLeaves();
				_startIndex = indexOf(_partSentence.get(0),f_fullSentence,_endIndex)+1;
				_endIndex = _startIndex +_partSentence.size();
				List<TypedDependency> _dependenciesFiltered = SearchUtils.filter(f_dependencies,_startIndex,_endIndex);
				analyzseSentence(sentP, _dependenciesFiltered); //analyzing this new, smaller chunk
			}
		}
	}	
	
	/**
	 * @param _fd
	 * @param filtered
	 */
	private void removeAll(List<TypedDependency> _fd,List<TypedDependency> filtered) {
		for(TypedDependency td:filtered) {
			if(!td.reln().getShortName().equals("rcmod")) {
				_fd.remove(td);
			}
		}
	}


	/**
	 * @param _copy
	 * @param sentence
	 */
	private boolean removeChildInCopy(Tree original, Tree copy, Tree remove) {
		if(original.indexOf(remove) != -1) {
			ProcessingUtils.removeChild(copy,original.indexOf(remove));
			return true;
		}
		for(int i=0;i<copy.children().length;i++) {
			Tree t = copy.children()[i];
			Tree orig = original.children()[i];
			if(removeChildInCopy(orig,t, remove)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param sentence
	 * @return
	 */
	private List<Tree> findSubSentences(Tree sentence) {
		List<Tree> _result = SearchUtils.findChildren(f_sentenceTags, sentence);
		for(Tree t:sentence.children()) {
			if(t.value().equals("PP") || t.value().equals("ADVP")) {
				_result.addAll(SearchUtils.findChildren(f_sentenceTags, t));
				for(Tree t2:t.children()) {
					if(t2.value().equals("NP")) {
						_result.addAll(SearchUtils.findChildren(f_sentenceTags, t2));
					}					
				}
			}			
		}
		return _result;
	}


	/**
	 * @param sentence
	 * @return
	 */
	private int determineSubSentenceCount(Tree sentence) {
		int _result = SearchUtils.count(f_sentenceTags,sentence.getChildrenAsList());
		//ignore it if the root if the only node (e.g. for relaitve sentence processing)
		if(_result == 1 && sentence.getChild(0).value().equals("WHNP")){
			_result --;
		}
		for(Tree t:sentence.children()) {
			if(t.value().equals("PP") || t.value().equals("ADVP")) {
				_result += SearchUtils.count(f_sentenceTags,t.getChildrenAsList());
				for(Tree t2:t.children()) {
					if(t2.value().equals("NP")) {
						_result += SearchUtils.count(f_sentenceTags,t2.getChildrenAsList());
					}					
				}
			}			
		}
		return _result;
	}


	/**
	 * @param tree
	 * @param sentence
	 * @param index
	 * @return
	 */
	private int indexOf(Tree tree, List<Tree> sentence, int startIndex) {
		for(int i=startIndex;i<sentence.size();i++) {
			if(sentence.get(i).equals(tree)) {
				return i;
			}
		}
		return -1;
	}

	/** 
	 * @param sentP
	 * @param dependencies
	 * @return
	 */
	private List<TypedDependency> filterDependencies(Tree sentP,Collection<TypedDependency> dependencies) {
		List<Tree> _partSentence = sentP.getLeaves();
		int _startIndex = SearchUtils.getIndex(f_fullSentence,_partSentence);
		int _endIndex = _startIndex + _partSentence.size();
		List<TypedDependency> _dependenciesFiltered = SearchUtils.filter(f_dependencies,_startIndex,_endIndex);
		return _dependenciesFiltered;
		
	}
	
	/**
	 * @param sentence
	 * @param filterRC 
	 */
	private void extractElements(Tree sentence,Collection<TypedDependency> dependencies) {
		if(Constants.DEBUG_EXTRACTION) System.out.println("-----------------------------------");
		if(Constants.DEBUG_EXTRACTION) System.out.println("extracting from:" +PrintUtils.toString(sentence.getLeaves()));
		boolean _active = isActive(sentence,dependencies);
		List<Actor> _actors = determineSubjects(sentence,dependencies,_active);
		List<Action> _verbs = determineVerb(sentence,_active,dependencies);
		removeExamples(_verbs);
		List<Action> _actions = new ArrayList<Action>();
		List<SpecifiedElement> _allObjects = new ArrayList<SpecifiedElement>();
		for(Action verb:_verbs) {
			List<ExtractedObject> _objects = determineObject(sentence,_active,verb,dependencies,_active);
			filterVerb(verb,_actors,_objects);
			
			_allObjects.addAll(_objects);
			if(_objects.size() > 0) {
				for(ExtractedObject el:_objects) {
					Action _ac = verb.clone();
					_ac.setObject(el);					
					_actions.add(_ac);
				}
			}else {
				_actions.add(verb);
			}
		}
		
		List<Action> _finalActions = new ArrayList<Action>();
		//combining elements
		if(_actors.size() > 0) {
			for(Actor a:_actors) {
				for(Action v:_actions) {
					Action _ac = v.clone();
					_ac.setActorFrom(a);
					_finalActions.add(_ac);
				}
			}		
		}else {
			_finalActions.addAll(_actions);
		}		
		//adding everything to the worldModel
		for(Actor a:_actors) {
			f_world.addActor(a);
		}
		for(SpecifiedElement se:_allObjects) {
			if(se instanceof Actor) {
				f_world.addActor((Actor) se);
			}else {
				f_world.addResource((Resource) se);
			}
		}
		for(Action a:_finalActions) {
			f_actions.add(a);
			if(a.getXcomp() != null) {
				if(a.getXcomp().getActorFrom() != null)
					f_world.addActor(a.getXcomp().getActorFrom());
				if(a.getXcomp().getObject() != null) {
					if(a.getXcomp().getObject() instanceof Actor) {
						f_world.addActor((Actor) a.getXcomp().getObject());
					}else {
						f_world.addResource((Resource) a.getXcomp().getObject());
					}
				}
			}
		}
	}
	
	
	/**
	 * @param _verbs
	 */
	private void removeExamples(List<Action> verbs) {
		for(int i=0;i<verbs.size();i++) {
			Action _ac = verbs.get(i);
			for(Specifier sp:_ac.getSpecifiers()) {
				if(Constants.f_exampleIndicators.contains(sp.getPhrase())){
					verbs.remove(i);
					i--;
				}
			}
		}
	}


	/**
	 * if one of the specifiers contains a subject (in passive sentences)
	 * it is removed to make the verb leaner
	 * @param verb
	 * @param actors
	 */
	private void filterVerb(Action verb, List<Actor> actors,List<ExtractedObject> objects) {
		ArrayList<SpecifiedElement> _toCheck = new ArrayList<SpecifiedElement>();
		_toCheck.addAll(actors);
		_toCheck.addAll(objects);
		ArrayList<Specifier> _specToCheck = new ArrayList<Specifier>();
		_specToCheck.addAll(verb.getSpecifiers(SpecifierType.PP));
		_specToCheck.addAll(verb.getSpecifiers(SpecifierType.DOBJ));
		_specToCheck.addAll(verb.getSpecifiers(SpecifierType.RCMOD));
		for(SpecifiedElement a:_toCheck) {
			for(Specifier sp :_specToCheck) {			
				if(sp.getWordIndex() == a.getWordIndex()/*checkContainment(sp.getPhrase(),a.getName())*/){
					if(Constants.DEBUG_EXTRACTION) System.out.println("removing specifier: "+sp);
					verb.removeSpecifier(sp);
					
				}
			}
			if(verb.getCop() != null && verb.getCop().equals(a.getName())) {
				if(Constants.DEBUG_EXTRACTION) System.out.println("removing cop: "+verb.getCop());
				verb.setCop(null,-1);
				//also remove all specifiers which actually belong to the cop object
				for(Specifier _objSpec : a.getSpecifiers(SpecifierType.PP)) {
					for(Specifier sp :_specToCheck) {	
						if(sp.getPhrase().equalsIgnoreCase(_objSpec.getPhrase())) {
							verb.removeSpecifier(sp);
							if(Constants.DEBUG_EXTRACTION) System.out.println("removing cop-specifier: "+sp.getPhrase());
							
						}
					}
				}
			}
		}
		_specToCheck.clear();
		_specToCheck.addAll(verb.getSpecifiers(SpecifierType.SBAR));
		_toCheck.clear();
		_toCheck.addAll(f_actions);
			
		for(Specifier sp :_specToCheck) {
			int _start = sp.getWordIndex();
			int _end = sp.getWordIndex()+sp.getName().split(" ").length;//num of words
			for(SpecifiedElement act:_toCheck) {
				if(_start < act.getWordIndex() && _end > act.getWordIndex()) {
					verb.removeSpecifier(sp);
				}
			}
		}
		
		if(Constants.DEBUG_EXTRACTION) System.out.println("Filtered Verb: "+verb);
		
		
		
		
	}


	/**
	 * @param phrase
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean checkContainment(String phrase, String name) {
		int _id1 = phrase.indexOf(name);
		if(_id1 > -1) {
			if(_id1-1 > -1) {
				if(! (phrase.charAt(_id1-1) == ' ')) {
					return false;
				}
			}
			int _id2 = _id1+name.length();
			if((_id2+1) < phrase.length()) {
				if(! (phrase.charAt(_id2+1) == ' ')) {
					return false;
				}
			}
			return true;
		}
		return false;
		
	}


	/**
	 * @param sentence 
	 * @param dependencies
	 * @return
	 */
	private boolean isActive(Tree sentence, Collection<TypedDependency> dependencies ) {
		List<TypedDependency> _nsubj = SearchUtils.findDependency(ListUtils.getList("nsubj","csubj","dobj"),dependencies);
		excludeRelativeClauses(sentence,_nsubj);
		if(_nsubj.size() > 0) {
			return true;
		}	
		List<TypedDependency> _nsubjpass = SearchUtils.findDependency(ListUtils.getList("nsubjpass","csubjpass","agent"),dependencies);
		excludeRelativeClauses(sentence,_nsubjpass);
		if(_nsubjpass.size() > 0) {
			return false;
		}
		if(Constants.DEBUG_EXTRACTION) System.out.println("It is not clear wether this sentence is active or passive!");
		return false; // return passive by default
	}


	/**
	 * extracts the subjects (actors) of the activity
	 * can be null (in case of a passive sentence) or a relative pronoun
	 * which then has to be resolved
	 * @param sentence 
	 * @param dependencies
	 * @param active 
	 */	
	private List<Actor> determineSubjects(Tree sentence, Collection<TypedDependency> dependencies, boolean active ) {
		ArrayList<Actor> _result = new ArrayList<Actor>();
		//determine subject
		TreeGraphNode _mainActor = null;
		if(active) {
			List<TypedDependency> _nsubj = SearchUtils.findDependency("nsubj",dependencies);
			excludeRelativeClauses(sentence,_nsubj);
			if(_nsubj.size() == 0) {
				if(Constants.DEBUG_EXTRACTION) System.out.println("No active subject was found!");
			}else {
				if(_nsubj.size() > 1) {
					System.out.println("Sentence has more then one subject");
					if(Constants.DEBUG_EXTRACTION) printToConsole(_nsubj);
				}else {
					_mainActor = _nsubj.get(0).dep();
				}			
			}
		}else {
			//passive sentence
			List<TypedDependency> _agent = SearchUtils.findDependency("agent",dependencies);
			excludeRelativeClauses(sentence,_agent);
			if(_agent.size() == 0) {	
				if(Constants.DEBUG_EXTRACTION) System.out.println("Sentence contains no subject!");
			}else {
				if(_agent.size() > 1) {
					System.out.println("Sentence has more then one agent:");
					if(Constants.DEBUG_EXTRACTION) printToConsole(_agent);
				}else {
					_mainActor = _agent.get(0).dep();
				}
			}
		}		
		if(_mainActor != null) {
			Actor _actor = ElementsBuilder.createActor(f_sentence, f_fullSentence, _mainActor, dependencies);
			_actor.setSubjectRole(true);
			_actor.setPassive(!active);
			_result.add(_actor);
			ArrayList<SpecifiedElement> _list = checkConjunctions(dependencies,_actor,true,true,active);
			for(SpecifiedElement se:_list) {
				((Actor)se).setSubjectRole(true);
				((Actor)se).setPassive(!active);
				_result.add((Actor)se);
			}
		}
		return _result;
	}

	private ArrayList<SpecifiedElement> checkConjunctions(Collection<TypedDependency> dependencies,SpecifiedElement current,boolean object,boolean active) {
		return checkConjunctions(dependencies, current, object, false, active);
	}		
	
	/**
	 * checks if our main subject is connected to someone else via a conjunction (or, and)
	 * because then those are also actors (... has to be checked by the CEO -and- the COO)
	 * @param dependencies
	 * @param depType
	 * @param currentActor
	 * @param flowType
	 */
	private ArrayList<SpecifiedElement> checkConjunctions(Collection<TypedDependency> dependencies,SpecifiedElement current,boolean object,boolean actor,boolean active) {
		// checking or conjunctions
		ArrayList<SpecifiedElement> _result = new ArrayList<SpecifiedElement>();
		List<TypedDependency> _conj = SearchUtils.findDependency("conj", dependencies);
		List<TypedDependency> _cop = SearchUtils.findDependency("cop", dependencies);
		if(_conj.size() > 0) {
			Action a = null;
			if(current instanceof Action) {
				a = (Action) current;
			}
			for(TypedDependency td:_conj) {
				boolean _xcompHit = (a != null && a.getXcomp() != null && a.getXcomp().getVerb().contains(td.gov().value()));	
				if((td.gov().value().equalsIgnoreCase(current.getName()) 
						&& (SearchUtils.filterByGov(td.gov(), _cop)).size()==0) 
						|| _xcompHit) {
					TreeGraphNode _otherNode = td.dep();
					SpecifiedElement _newEle;
					if(object) {
						if(actor) {
							_newEle = ElementsBuilder.createActor(f_sentence, f_fullSentence, _otherNode, dependencies);
						}else {
							_newEle = ElementsBuilder.createObject(f_sentence, f_fullSentence, _otherNode, dependencies);
							checkNPForSubsentence(_otherNode,dependencies,(ExtractedObject)_newEle);
						}
					}else {
						if(_xcompHit) {
							//copy full action and only replace xcomp
							_newEle = a.clone();
							((Action)_newEle).setXcomp(ElementsBuilder.createAction(f_sentence, f_fullSentence, _otherNode, dependencies,true));
						}else {
							_newEle = ElementsBuilder.createAction(f_sentence, f_fullSentence, _otherNode, dependencies,active);
						}
					}
					if(td.gov().index() != td.dep().index()) {
						_result.add(_newEle);					
						//_result.addAll(checkConjunctions(dependencies, _newEle, object,active));
						//connecting actors and activities					
						//adding link
						//if(!(current instanceof Action)) {
							buildLink(current, td, _newEle);
						//}
					}
				}
			}				
		}
		return _result;		
	}


	private void buildLink(SpecifiedElement current, TypedDependency td, SpecifiedElement _newEle) {
		ConjunctionElement _conj = null;
		if(td.reln().getSpecific().equals("or")) {
			_conj = new ConjunctionElement(current,_newEle,ConjunctionType.OR);
		}else if(td.reln().getSpecific().equals("and")) {
			_conj = new ConjunctionElement(current,_newEle,ConjunctionType.AND);
		}else if(td.reln().getSpecific().equals("and\\/or")) {
			_conj = new ConjunctionElement(current,_newEle,ConjunctionType.ANDOR);
		}else {
			if(!td.reln().getSpecific().equals("but")) {
				//but is ignored - for now
				System.err.println("Undefined conjunction relation: "+td.reln().getSpecific());
			}
		}
		if(_conj != null) {
			f_conjs.add(_conj);
		}
	}


	/**
	 * @param sentence
	 * @param active
	 * @param dependencies
	 * @return
	 */
	private List<Action> determineVerb(Tree sentence,boolean active,Collection<TypedDependency> dependencies) {
		TreeGraphNode _mainPredicate = null;
		List<Action> _result = new ArrayList<Action>();
		if(active) {
			List<TypedDependency> _nsubj = SearchUtils.findDependency("nsubj",dependencies);
			excludeRelativeClauses(sentence,_nsubj);		
			if(_nsubj.size() == 0) {
				//hmm...
				//could be an imperative, look for a dobj relation
				List<TypedDependency> _dobj = SearchUtils.findDependency("dobj",dependencies);
				excludeRelativeClauses(sentence,_dobj);
				if(_dobj.size() >= 1) {
					_mainPredicate = _dobj.get(0).gov(); //here several are possible "leave the house and close the window" -> conj will find the rest
				}
			}else if(_nsubj.size() > 1) {
				System.out.println("Sentence has more than one active predicate");
				if(Constants.DEBUG_EXTRACTION) printToConsole(_nsubj);
			}else if(_nsubj.size() == 1) {
				_mainPredicate = _nsubj.get(0).gov();
				//check if we do not have a cop present, if so we have to change the main verb
				List<TypedDependency> _cop = SearchUtils.findDependency("cop",dependencies);
				excludeRelativeClauses(sentence,_cop);		
				for(TypedDependency td: _cop) {
					if(td.gov().equals(_mainPredicate)) {
						//okay found it
						_mainPredicate = td.dep();
						break;						
					}
				}
				
			}						
		}else {
			//passive sentence
			List<TypedDependency> _nsubjpass = SearchUtils.findDependency("nsubjpass",dependencies);
			excludeRelativeClauses(sentence,_nsubjpass);			
			if(_nsubjpass.size() > 1) {
				System.out.println("Sentence has more than one passive predicate:");
				if(Constants.DEBUG_EXTRACTION) printToConsole(_nsubjpass);
				_mainPredicate = _nsubjpass.get(0).gov();
			}else if(_nsubjpass.size() == 1) {
				_mainPredicate = _nsubjpass.get(0).gov();
			}			
		}
		if(_mainPredicate == null) {
			//determine through syntax tree only - less accurate
			List<Tree> _verbs = SearchUtils.find("VP",sentence,ListUtils.getList("SBAR","S"));
			if(_verbs.size() == 0) {
				System.out.println("Sentence contains no action?!?");
			}else if(_verbs.size() > 1) {
				System.out.println("Sentence has more than one verb phrase!");
			}else {
				Tree _vp = _verbs.get(0);
				Action _a = ElementsBuilder.createActionSyntax(f_sentence, f_fullSentence, _vp, active);
				checkForSubSentences(_vp,dependencies,_a,false);
				_result.add(_a);

				
			}	
		}else {
			Tree _vpHead = SearchUtils.getFullPhraseTree("VP",_mainPredicate);	
			Action _a = ElementsBuilder.createAction(f_sentence, f_fullSentence, _mainPredicate, dependencies,active);
			checkForSubSentences(_vpHead,dependencies,_a,false);
			_result.add(_a);
		}
		if(_result.size()>0) {			
			for(SpecifiedElement el: checkConjunctions(dependencies, _result.get(0), false,active)){
				_result.add((Action) el);
			}
		}
		return _result;	
	}	
	
	/**
	 * @param head
	 * @param dependencies 
	 */
	private void checkForSubSentences(Tree head, Collection<TypedDependency> dependencies,SpecifiedElement object,boolean isNP) {
		if(f_sentenceTags.contains(head.value())) {
			List<Tree> _leaves = head.getLeaves();
			int _start = SearchUtils.getIndex(f_fullSentence, _leaves);
			int _end = _start + _leaves.size();
			//exclude sentences which are already captured in a ccomp relationship
			for(TypedDependency td:SearchUtils.findDependency("ccomp", dependencies)) {
				if(_start < td.dep().index() && _end > td.dep().index()) {
					//possible candidate
					for(TypedDependency td2:SearchUtils.findDependency("complm", dependencies)) {
						if(td2.gov().equals(td.dep()) && td2.dep().value().equals("that")) {
							//ignore this one!
							return;
						}
					}
				}
			}
			Action mainAction = null;
			if(object instanceof Action) {
				mainAction = (Action)object;
			}
			if(mainAction == null || mainAction.getXcomp()== null || ((_start > mainAction.getXcomp().getWordIndex()) || (_end < mainAction.getXcomp().getWordIndex()))) {
				analyzseSentence(head, filterDependencies(head, dependencies));
			}
		}else {
			if(head.value().equals("PP") || head.value().equals("VP") || (isNP && head.value().equals("NP"))) {
				for(Tree t:head.children()) {
					checkForSubSentences(t,dependencies,object,isNP);
				}
			}
		}
	}


	/**
	 * @param sentence
	 * @param dependencies 
	 * @param sentence2 
	 * @param _active 
	 * @param verbPhrase - enables additional checks in case no dobj relation is found
	 */
	private List<ExtractedObject> determineObject(Tree sentence, boolean _active, Action verb, Collection<TypedDependency> dependencies,boolean active) {
		List<ExtractedObject> _result = new ArrayList<ExtractedObject>();
		//check if verb has an xcomp object first (needed in determineObjectFromDOBJ)
		if(verb.getXcomp() != null) {
			ArrayList<ExtractedObject> _xcompObj = new ArrayList<ExtractedObject>(1);
			determineObjectFromDOBJ(verb.getXcomp(), dependencies, _xcompObj);
			if(_xcompObj.size() > 0) {
				verb.getXcomp().setObject(_xcompObj.get(0));
			}
		}
		//trying to determine the object!
		if(!_active) {
			//passive sentence -  the beauty of an nsubjpass relation			
			List<TypedDependency> _nsubjpass = SearchUtils.findDependency("nsubjpass", dependencies);
			excludeRelativeClauses(sentence,_nsubjpass);		
			if(_nsubjpass.size() == 0) {
				//use dobj if available instead
				determineObjectFromDOBJ(verb, dependencies, _result);	
			}else if(_nsubjpass.size() > 1) {
				System.out.println("Passive sentence with more than one subject!?!?");
				if(Constants.DEBUG_EXTRACTION) printToConsole(_nsubjpass);
				TreeGraphNode _object = _nsubjpass.get(0).dep();				
				ExtractedObject _obj = ElementsBuilder.createObject(f_sentence, f_fullSentence, _object,dependencies);
				_obj.setSubjectRole(true); //although it is an object it is the syntactic subject of the sentence
				_result.add(_obj);
				checkNPForSubsentence(_object,dependencies,_obj);
			}else {				
				TreeGraphNode _object = _nsubjpass.get(0).dep();				
				ExtractedObject _obj = ElementsBuilder.createObject(f_sentence, f_fullSentence, _object,dependencies);
				_obj.setSubjectRole(true); //although it is an object it is the syntactic subject of the sentence
				_result.add(_obj);
				checkNPForSubsentence(_object,dependencies,_obj);				
			}			
		}else {			
			determineObjectFromDOBJ(verb, dependencies, _result);			
		}
		if(_result.size() > 0) {
			List<SpecifiedElement> _conj = checkConjunctions(dependencies, _result.get(0),true,active);
			for(SpecifiedElement sp:_conj) {
				if(sp instanceof ExtractedObject)
					_result.add((ExtractedObject) sp);
			}
		}
		return _result;
	}


	private void determineObjectFromDOBJ(Action verb,
			Collection<TypedDependency> dependencies,
			List<ExtractedObject> _result) {
		List<TypedDependency> _dobj = SearchUtils.findDependency(ListUtils.getList("dobj"), dependencies);
		List<TypedDependency> _myDobj = SearchUtils.filterByGov(verb, _dobj);
		if(_myDobj.size() == 0) {
			if(!(verb.getXcomp()!= null && verb.getXcomp().getObject() != null)) {
				//if we already have an xcomp object, we do not have to check ands (incident management - BPMN standard)
				for(ConjunctionElement l:f_conjs) {
					if(l.getTo().equals(verb)) {
						_myDobj = SearchUtils.filterByGov((Action) l.getFrom(), _dobj);
						_myDobj = SearchUtils.filterByIndex((Action) l.getTo(), _dobj,true);
					}else if(l.getFrom().equals(verb)) {
						_myDobj = SearchUtils.filterByGov((Action) l.getTo(), _dobj);
						_myDobj = SearchUtils.filterByIndex((Action) l.getFrom(), _dobj,true);
					}
				}
			}
		}			
		if(_myDobj.size() == 0) {
			//search in directly connected prepositional phrases
			List<TypedDependency> _prep = SearchUtils.findDependency("prep", dependencies);
			List<TypedDependency> _myPrep = new ArrayList<TypedDependency>();
			//filtering only relevant preps
			for(TypedDependency td:_prep) {
				if(verb.getVerb().contains(td.gov().value()) && td.gov().index() > verb.getWordIndex()) {
					_myPrep.add(td);
				}
			}				
			if(_myPrep.size() == 0) {
				//maybe we have a copular sentence?!?
				List<TypedDependency> _cop = SearchUtils.findDependency("cop", dependencies);
				if(_cop.size() == 0) {
					if(Constants.DEBUG_EXTRACTION) System.out.println("No Object found!");
				}else if(_cop.size() > 1) {
					System.out.println("Sentence with more than one copluar object!?!?");
					if(Constants.DEBUG_EXTRACTION) printToConsole(_cop);
				}else {
					TreeGraphNode _object = _cop.get(0).gov();
					if(_object.parent().parent().value().equals("NP")) { //only if it is directly part of a noun phrase
						ExtractedObject _obj = ElementsBuilder.createObject(f_sentence, f_fullSentence, _object,dependencies);
						_result.add(_obj);
						checkNPForSubsentence(_object,dependencies,_obj);
					}else {
						if(Constants.DEBUG_EXTRACTION) System.out.println("No Object found!");
					}
				}
			}else if(_myPrep.size() > 1) {
				System.out.println("Sentence with more than one prepositional object!?!?");
				if(Constants.DEBUG_EXTRACTION) printToConsole(_myPrep);
			}else {
				//this is our relation
				TreeGraphNode _object = _myPrep.get(0).dep();
				if(_object.parent().parent().value().equals("NP")) { //only if it is directly part of a noun phrase
					ExtractedObject _obj = ElementsBuilder.createObject(f_sentence, f_fullSentence, _object,dependencies);
					_result.add(_obj);
					checkNPForSubsentence(_object,dependencies,_obj);	
				}else {
					if(Constants.DEBUG_EXTRACTION) System.out.println("No Object found!");
				}
			}		
			
			//no direct object relation found - maybe it was not recognized and is only a dep
			//hmm not sure if this is needed
			/*
			Tree _np = SearchUtils.findFirst("NP", verbPhrase);
			List<Tree> _leaves = _np.getLeaves();
			int _npStart = f_fullSentence.indexOf(_leaves.get(0))+1;
			int _npEnd = f_fullSentence.indexOf(_leaves.get(_leaves.size()-1))+1;

			_leaves = verbPhrase.getLeaves();
			int _vpStart = f_fullSentence.indexOf(_leaves.get(0))+1;
			int _vpEnd = f_fullSentence.indexOf(_leaves.get(_leaves.size()-1))+1;
			
			//checking with dependencies
			List<TypedDependency> _dep = SearchUtils.findDependency("dep", dependencies);
			for(TypedDependency td:_dep) {
				if((inBetween(td.gov().index(),_vpStart,_vpEnd))
					&& (inBetween(td.dep().index(),_npStart,_npEnd))
					&& !(inBetween(td.gov().index(),_npStart,_npEnd))){
						//this is okay!
						_r = 
							ElementsBuilder.createResource(f_sentence, f_world, f_fullSentence, td.dep(),dependencies);
						break;
						
					}					
			}		*/				
//		}else if(_myDobj.size() > 1) {
//			System.out.println("Sentence with more than one direct object!?!?");
//			if(Constants.DEBUG_EXTRACTION) printToConsole(_myDobj);
		}else {
			TreeGraphNode _object = _myDobj.get(0).dep();
			ExtractedObject _obj = ElementsBuilder.createObject(f_sentence, f_fullSentence, _object,dependencies);
			checkNPForSubsentence(_object,dependencies,_obj);			
			_result.add(_obj);
		}
	}


	
	
	
	/**
	 * 
	 */
	private void checkNPForSubsentence(TreeGraphNode tgNode,Collection<TypedDependency> dependencies,ExtractedObject obj) {
		if(!f_ignoreNPSubSentences) {
			Tree _head = SearchUtils.getFullPhraseTree("NP",tgNode);	
			checkForSubSentences(_head, dependencies, obj,true);
		}
	}


	/* part of not sure if needed in determineObject
	 * @param index
	 * @param start
	 * @param end
	 * @return
	 
	private boolean inBetween(int index, int start, int end) {
		return index >= start && index <= end;
	}*/

	
	/**
	 * @param sentence 
	 * @param list
	 */
	private void excludeRelativeClauses(Tree sentence, List<TypedDependency> list) {
		for(int i=0;i<list.size();i++) {
			TypedDependency _td = list.get(i);
			if(_td.reln().getShortName().equals("rcmod")) {
				continue;
			}
			TreeGraphNode _dep = _td.gov();
			int _sentenceIndex = SearchUtils.getIndex(f_fullSentence, sentence.getLeaves());
			while(!(_dep = (TreeGraphNode) _dep.parent()).value().equals("ROOT")) {
				if(sentence.value().equals(_dep.parent().value())) {
					//did we arrive at the top most sentence node already?
					int _partIndex = SearchUtils.getIndex(f_fullSentence, _dep.parent().getLeaves());
					if(_sentenceIndex >= _partIndex) {
						break;
					}
				}
				
				if((_dep.parent().value().equals("SBAR") || _dep.parent().value().equals("S") || _dep.parent().value().equals("PRN"))
						&& (!_dep.parent().parent().value().equals("SBAR"))) { //hack for relative clause processing	
					list.remove(i);
					i--;
					break;
				}
			}
		}
	}

	

	/**
	 * @param _agent
	 */
	private void printToConsole(List<TypedDependency> list) {
		for(TypedDependency td:list) {
			System.out.println(td+" - "+SearchUtils.getFullNounPhrase(td.dep()));
		}
	}


	public void setSentenceNumber(int f_sentenceNumber) {
		this.f_sentenceNumber = f_sentenceNumber;
	}


	public int getSentenceNumber() {
		return f_sentenceNumber;
	}

	public T2PSentence getBaseSentence() {
		return f_sentence;
	}
	
	public List<Action> getExtractedActions(){
		return f_actions;
	}


	/**
	 * @return
	 */
	public List<ConjunctionElement> getExtractedConjunctions() {
		return f_conjs;
	}


	/**
	 * @param me
	 */
	public void removeAction(Action me) {
		f_actions.remove(me);
	}
	
	@Override
	public String toString() {
		return f_sentence.toString();
	}
	
}
