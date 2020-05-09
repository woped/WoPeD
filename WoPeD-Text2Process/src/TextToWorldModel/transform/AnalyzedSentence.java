/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package TextToWorldModel.transform;

import java.util.*;

import TextToWorldModel.Constants;
import TextToWorldModel.processing.ProcessingUtils;
import edu.stanford.nlp.ling.IndexedWord;
import worldModel.T2PSentence;
import TextToWorldModel.transform.ConjunctionElement.ConjunctionType;
import worldModel.Action;
import worldModel.Actor;
import worldModel.ExtractedObject;
import worldModel.Resource;
import worldModel.SpecifiedElement;
import worldModel.Specifier;
import worldModel.WorldModel;
import worldModel.Specifier.SpecifierType;
import edu.stanford.nlp.trees.Tree;
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
	 * Constructor of a new AnalyzedSentence
	 * @param sentence current T2P-Sentence, that is to be analyzed
	 * @param sentenceNumber number of the sentence in the whole analyzed text
	 */
	public AnalyzedSentence(T2PSentence sentence, int sentenceNumber) {
		f_sentence = sentence;
		setSentenceNumber(sentenceNumber);
		f_sentenceTags.add("S");
		f_sentenceTags.add("SBAR");
		f_sentenceTags.add("SINV");
	}

	/**
	 * Analyze every sentence of the input text and add extracted Objects to the WorldModel
	 * @param world WorldModel that should result from the analysis
	 */
	public void analyze(WorldModel world) {
		f_world = world;
		f_root = f_sentence.getTree();
		f_fullSentence = f_root.getLeaves();
		f_dependencies = f_sentence.getGrammaticalStructure().typedDependenciesEnhancedPlusPlus();
				
		Tree _mainSentence = f_root.getChild(0); //this is always the case
		analyzeSentence(f_root, _mainSentence,f_dependencies);
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
	 *  determines if there are conjunctions connecting Actions over sub-sentence boundaries
	 */
	private void checkGlobalConjunctions() {
		//checking global sentence conjunctions
		List<TypedDependency> _conj = SearchUtils.findDependency("conj", f_dependencies);
		if(_conj.size() > 0) {
			for(TypedDependency td:_conj) {
				Action _a = getActionContaining(td.gov().index());
				Action _b = getActionContaining(td.dep().index());
				if(_a != null && _b != null) {				
					buildLink(_a, td, _b);				
				}
			}				
		}
	}


	/**
	 * @param index Index of the Tree gov() or dep() of a TypedDependency
	 * @return Action a which relates to the dependency
	 */
	private Action getActionContaining(int index) {
		for(Action a:f_actions) {
			if(a.getWordIndex() == index
					|| (a.getXcomp() != null && a.getXcomp().getWordIndex() == index)
					|| (a.getCopIndex() == index)
					|| (a.getObject() != null && a.getObject().getWordIndex() == index)) {
				return a;
			}
		}
		return null;
	}


	/**
	 * Analyze the sentence end extracts elements
	 * @param f_root whole sentence that is going to get analyzed in a tree structure
	 * @param _mainSentence Tree structure of the sentence without the root node
	 * @param dependencies Collection of the dependencies of the sentence
	 */
	private void analyzeSentence(Tree f_root, Tree _mainSentence, Collection<TypedDependency> dependencies ) {
		//checking if the sentence contains several sub-sentences
		int _sCount = determineSubSentenceCount(_mainSentence);
		if(_sCount == 0) {
			//no, this is a simple sentence, it is okay to proceed
			extractElements(f_root, _mainSentence,dependencies);
		}else if(_sCount == 1) {
			//we have a sub-sentence with something surrounding it here
			//analyzing on a lower level
			Tree _subSentence = findSubSentences(_mainSentence).get(0);
			List<TypedDependency> _dependenciesFiltered =  filterDependencies(_subSentence,dependencies);
			analyzeSentence(f_root, _subSentence,_dependenciesFiltered);
			//it only was a relative part - maybe something that created a condition
			Tree _copy = _mainSentence.deepCopy();
			removeChildInCopy(_mainSentence,_copy,_subSentence);
			List<TypedDependency> _fd = new ArrayList<TypedDependency>(dependencies);
			removeAll(_fd,_dependenciesFiltered);
			if(SearchUtils.findDependency(ListUtils.getList("nsubj","agent","nsubjpass","dobj"), _fd).size()>0) {
				//but, only check if it has information in it!
				extractElements(f_root, _copy, _fd);
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
				analyzeSentence(f_root, sentP, _dependenciesFiltered); //analyzing this new, smaller chunk
			}
		}
	}	
	
	/**
	 * Removes all dependencies of the filtered dependencies list from the Dependency list, if the Dependency is not "rcmod"
	 * @param _fd Dependency list that is to be cleaned
	 * @param filtered list of dependencies that ahould get deleted from the list _fd
	 */
	private void removeAll(List<TypedDependency> _fd,List<TypedDependency> filtered) {
		for(TypedDependency td:filtered) {
			if(!td.reln().getShortName().equals("rcmod")) {
				_fd.remove(td);
			}
		}
	}


	/**
	 * Removes childnodes of the copy tree, that are defined in the remove tree
	 * @param original
	 * @param copy of the original tree
	 * @param remove nodes that should get removed
	 */
	private boolean removeChildInCopy(Tree original, Tree copy, Tree remove) {
		if(original.objectIndexOf(remove) != -1) {
			ProcessingUtils.removeChild(copy,original.objectIndexOf(remove));
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
	 * @param sentence Tree structure of the sentence without the root node
	 * @return List<Tree> of subsentences that the sentence contains
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


	/** Determine the number of subsentence that the current sentence contains
	 * @param sentence
	 * @return int SubSentenceCount
	 */
	private int determineSubSentenceCount(Tree sentence) {
		int _result = SearchUtils.count(f_sentenceTags,sentence.getChildrenAsList());
		//ignore it if the root is the only node (e.g. for relative sentence TextToWorldModel.processing)
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
	 * @param startIndex
	 * @return the int Index of the sentence which corresponds to the Treestructure
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
	 * @return List<TypedDependency>
	 */
	private List<TypedDependency> filterDependencies(Tree sentP,Collection<TypedDependency> dependencies) {
		List<Tree> _partSentence = sentP.getLeaves();
		int _startIndex = SearchUtils.getIndex(f_fullSentence,_partSentence);
		int _endIndex = _startIndex + _partSentence.size();
		List<TypedDependency> _dependenciesFiltered = SearchUtils.filter(f_dependencies,_startIndex,_endIndex);
		return _dependenciesFiltered;
		
	}
	
	/**
	 * Extracts all elements (Actors, Actions and Resources) that can be found in the sentence
	 * @param f_root
	 * @param mainsentence Tree structure of the sentence without the root node
	 * @param dependencies Collection of all the dependencies of the mainsentence
	 */
	private void extractElements(Tree f_root, Tree mainsentence,Collection<TypedDependency> dependencies) {
		if(Constants.DEBUG_EXTRACTION) System.out.println("-----------------------------------");
		if(Constants.DEBUG_EXTRACTION) System.out.println("extracting from:" +PrintUtils.toString(mainsentence.getLeaves()));
		boolean _active = isActive(f_root, mainsentence, dependencies);
		List<Actor> _actors = determineSubjects(f_root, mainsentence, dependencies,_active);
		List<Action> _verbs = determineVerb(f_root, mainsentence,_active,dependencies);
		removeExamples(_verbs);
		List<Action> _actions = new ArrayList<Action>();
		List<SpecifiedElement> _allObjects = new ArrayList<SpecifiedElement>();
		for(Action verb:_verbs) {
			List<ExtractedObject> _objects = determineObject(mainsentence,_active,verb,dependencies,_active);
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
		
		//List<Action> _finalActions = new ArrayList<Action>();
		//combining elements
		//TRYOUT
		/*
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
		*/
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
		for(Action a:_actions) {
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
	
	
	/**Checks if verb is part of an example sentence, if yes it removes the verb from the list
	 * @param verbs list of all verbs that could get extracted from the sentences
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
	 * @param actors list of all actors that were extracted from the sentences
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
				if(sp.getWordIndex() == a.getWordIndex()){
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
	 * Checks whether the sentence is an active or passive sentence by analyzing the dependencies that indicate the presence of an active/passive sentence
	 * @param f_root Treestructure of the T2P Sentence
	 * @param mainsentence Tree structure of the sentence without the root node
	 * @param dependencies Collection of the dependencies of the sentence
	 * @return boolean that is true when the sentence is an active sentence
	 */
	private boolean isActive(Tree f_root, Tree mainsentence, Collection<TypedDependency> dependencies ) {
		List<TypedDependency> _nsubj = SearchUtils.findDependency(ListUtils.getList("nsubj","csubj","dobj"),dependencies);
		excludeRelativeClauses(f_root, mainsentence,_nsubj);
		if(_nsubj.size() > 0) {
			return true;
		}	
		List<TypedDependency> _nsubjpass = SearchUtils.findDependency(ListUtils.getList("nsubjpass","csubjpass","agent"),dependencies);
		excludeRelativeClauses(f_root, mainsentence,_nsubj);
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
	 * @param f_root Treestructure of the T2P Sentence
	 * @param mainsentence Tree structure of the sentence without the root node
	 * @param dependencies Collection of the dependencies of the sentence
	 * @param active boolean value that is true when the sentence is an active sentence
     * @return List of Actors that could get extracted from the sentence
	 */	
	private List<Actor> determineSubjects(Tree f_root, Tree mainsentence, Collection<TypedDependency> dependencies, boolean active ) {
		ArrayList<Actor> _result = new ArrayList<Actor>();
		//determine subject
		IndexedWord _mainActor = null;
		if(active) {
			List<TypedDependency> _nsubj = SearchUtils.findDependency("nsubj",dependencies);
			List<TypedDependency> _nmod_to = SearchUtils.findDependency("nmod",dependencies);
			List<TypedDependency> _det = SearchUtils.findDependency("det",dependencies);
			excludeRelativeClauses(f_root, mainsentence,_nsubj);
			if(_nsubj.size() > 0){
				ArrayList<IndexedWord> allActors = new ArrayList<>();
				if(_det.size()>0){
					for(TypedDependency TDsubj : _nsubj){
						for(TypedDependency TDdet : _det){
							if(TDsubj.dep().value().equals(TDdet.gov().value())){
								Tree helpNode = f_fullSentence.get(TDsubj.dep().index()-1);
								if (!helpNode.parent(f_root).label().value().equals("NNP")){
                                    allActors.add(TDsubj.dep());
                                }
							}
						}
					}
				}
				if(_nmod_to.size()>0){
					for(TypedDependency TDdet : _det){
						for(TypedDependency TDnmod : _nmod_to){
							if(TDdet.gov().value().equals(TDnmod.dep().value())){
								allActors.add(TDdet.gov());
							}
						}
					}
				}
				if (allActors.size()>1){
					System.out.println("Sentence has more than one subject");
					if (Constants.DEBUG_EXTRACTION) printToConsole(_nsubj);
					for(IndexedWord mainActor: allActors){
						Actor actor = ElementsBuilder.createActor(f_root, f_sentence, f_fullSentence, mainActor, dependencies);
						_result.add(actor);
					}
				}
				else if (allActors.size()==1){
					_mainActor = allActors.get(0);
				}
			}
		}else {
			//passive sentence
			List<TypedDependency> _agent = SearchUtils.findDependency("agent",dependencies);
			excludeRelativeClauses(f_root, mainsentence,_agent);
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
			Actor _actor = ElementsBuilder.createActor(f_root, f_sentence, f_fullSentence, _mainActor, dependencies);
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


	/**
	 * @param dependencies
	 * @param current
	 * @param object
	 * @param active boolean value that is true when the sentence is an active sentence
	 * @return ArrayList<SpecifiedElement>
	 */
	private ArrayList<SpecifiedElement> checkConjunctions(Collection<TypedDependency> dependencies, SpecifiedElement current, boolean object, boolean active) {
		return checkConjunctions(dependencies, current, object, false, active);
	}		
	
	/**
	 * checks if our main subject is connected to someone else via a conjunction (or, and)
	 * because then those are also actors (... has to be checked by the CEO -and- the COO)
	 * @param dependencies
	 * @param current
	 * @param object
	 * @param actor
	 * @param active boolean value that is true when the sentence is an active sentence
	 */
	private ArrayList<SpecifiedElement> checkConjunctions(Collection<TypedDependency> dependencies,SpecifiedElement current,boolean object,boolean actor,boolean active) {
		// checking conjunctions
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
					IndexedWord _otherNode = td.dep();
					Tree _otherNodeTGN = f_fullSentence.get(_otherNode.index()-1);
					SpecifiedElement _newEle = null;
					if(object) {
						if(actor) {
							_newEle = ElementsBuilder.createActor(f_root, f_sentence, f_fullSentence, _otherNode, dependencies);
						}else {
							_newEle = ElementsBuilder.createObject(f_root, f_sentence, f_fullSentence, _otherNode, dependencies);
							checkNPForSubsentence(_otherNodeTGN,dependencies,(ExtractedObject)_newEle);
						}
					}else {
						if(_xcompHit) {
							//copy full action and only replace xcomp
							_newEle = a.clone();
								((Action) _newEle).setXcomp(ElementsBuilder.createAction(f_sentence, f_fullSentence, _otherNodeTGN, dependencies, true, f_root));
						}else {
								_newEle = ElementsBuilder.createAction(f_sentence, f_fullSentence, _otherNodeTGN, dependencies, active, f_root);
						}
					}
					if(td.gov().index() != td.dep().index()) {
						_result.add(_newEle);
							buildLink(current, td, _newEle);
					}
				}
			}				
		}
		return _result;		
	}

	/**
	 *
	 * @param current
	 * @param td
	 * @param _newEle
	 */
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
	 * Extracts the relevant verbs from the sentence in order to create the Actions for the WorldModel.
	 * A distinction is made between active and passive sentences
	 * @param f_root Tree structure of the whole sentence with the root node included
	 * @param mainsentence Tree structure of the sentence without the root node
	 * @param active Indicates if the sentence is an active or passive sentence
	 * @param dependencies List of all dependencies of the Sentence
	 * @return List of all Actions that could be found in the sentence
	 */
	private List<Action> determineVerb(Tree f_root, Tree mainsentence,boolean active,Collection<TypedDependency> dependencies) {
 		IndexedWord _mainPredicate = null;
		List<Action> _result = new ArrayList<Action>();
		boolean foundVerb = false;
		if(active) {
			List<TypedDependency> _nsubj = SearchUtils.findDependency("nsubj",dependencies);
			List<TypedDependency> _dobj = SearchUtils.findDependency("dobj",dependencies);
			List<TypedDependency> _nmod = SearchUtils.findDependency("nmod", dependencies);
            List<TypedDependency> _advmod = SearchUtils.findDependency("advmod", dependencies);
			excludeRelativeClauses(f_root, mainsentence,_nsubj);
			if(_nsubj.size() > 1) {
				System.out.println("Sentence has more than one active predicate");
                ArrayList<IndexedWord> _allPredicates = new ArrayList<>();
				for (TypedDependency tdNsubj : _nsubj){
				    for(TypedDependency tdDobj: _dobj){
				        if(tdNsubj.gov().value() == tdDobj.gov().value()){
				        	Tree treeNode = f_fullSentence.get(tdNsubj.gov().index()-1);
				        	if(!treeNode.parent(f_root).label().value().equals("VBP")){
								_allPredicates.add(tdNsubj.gov());
							}
                        }
                    }
                }
                for(IndexedWord verb: _allPredicates) {
                    Tree _PredicatesNode = f_fullSentence.get(verb.index() - 1);
                    Tree _vpHead = SearchUtils.getFullPhraseTree("VP", _PredicatesNode, f_root);
                    Action _a = ElementsBuilder.createAction(f_sentence, f_fullSentence, _PredicatesNode, dependencies, active, f_root);
                    checkForSubSentences(_vpHead, dependencies, _a, false);
                    _result.add(_a);
                    foundVerb = true;
                }
				if (Constants.DEBUG_EXTRACTION) printToConsole(_nsubj);
			}
			else if(_nsubj.size()==1){
				for (TypedDependency tdNsubj : _nsubj) {
					for (TypedDependency tdDobj : _dobj) {
						if (tdNsubj.gov().value() == tdDobj.gov().value()) {
							Tree treeNode = f_fullSentence.get(tdNsubj.gov().index()-1);
							if(!treeNode.parent(f_root).label().value().equals("VBP")) {
								_mainPredicate = tdNsubj.gov();
								foundVerb = true;
							}
						}
					}
				}
			}
			else {
			    ArrayList<IndexedWord> _allPredicates = new ArrayList<>();
			    if(_advmod.size()>0){
                    for (TypedDependency tdAdvmod : _advmod) {
                        for (TypedDependency tdDobj : _dobj) {
                            if (tdAdvmod.gov().value() == tdDobj.gov().value()) {
                                    _allPredicates.add(tdAdvmod.gov());
                                    foundVerb = true;
                            }
                        }
                    }
                }
                else {
                    for (TypedDependency tdNmod : _nmod) {
                        for (TypedDependency tdDobj : _dobj) {
                            if (tdNmod.gov().value() == tdDobj.gov().value()) {
                                Tree treeNode = f_fullSentence.get(tdNmod.gov().index() - 1);
                                if (!treeNode.parent(f_root).label().value().equals("VBP")) {
                                    _allPredicates.add(tdNmod.gov());
                                    foundVerb = true;
                                }
                            }
                        }
                    }
                }
                for (int i = 0; i < _allPredicates.size()-1; i++) {
                    IndexedWord a = _allPredicates.get(i);
                    IndexedWord b = _allPredicates.get(i+1);
                         if (a.index() == b.index()){
                             _allPredicates.remove(a);
                             i--;
                         }
                }
                for(IndexedWord verb: _allPredicates) {
                    Tree _PredicatesNode = f_fullSentence.get(verb.index() - 1);
                    Tree _vpHead = SearchUtils.getFullPhraseTree("VP", _PredicatesNode, f_root);
                    Action _a = ElementsBuilder.createAction(f_sentence, f_fullSentence, _PredicatesNode, dependencies, active, f_root);
                    checkForSubSentences(_vpHead, dependencies, _a, false);
                    _result.add(_a);
                    foundVerb = true;
                }
            }
		}else {
			//passive sentence
			List<TypedDependency> _nsubjpass = SearchUtils.findDependency("nsubjpass",dependencies);
			excludeRelativeClauses(f_root, mainsentence,_nsubjpass);
			if(_nsubjpass.size() > 1) {
				System.out.println("Sentence has more than one passive predicate:");
				if(Constants.DEBUG_EXTRACTION) printToConsole(_nsubjpass);
				_mainPredicate = _nsubjpass.get(0).gov();
			}else if(_nsubjpass.size() == 1) {
				_mainPredicate = _nsubjpass.get(0).gov();
			}			
		}
		if(!foundVerb) {
			//determine through syntax tree only - less accurate
			List<Tree> _verbSentences = SearchUtils.find("VP",mainsentence,ListUtils.getList("SBAR","S")); //prüfen
			if(_verbSentences.size() == 0) {
				System.out.println("Sentence contains no action?!?");
			}else if(_verbSentences.size() > 1) {
				System.out.println("Sentence has more than one verb phrase!");
			}
		}else if(_mainPredicate !=null){
			Tree _mainPredicateNode = f_fullSentence.get(_mainPredicate.index()-1);
                Tree _vpHead = SearchUtils.getFullPhraseTree("VP", _mainPredicateNode, f_root);
				Action _a = ElementsBuilder.createAction(f_sentence, f_fullSentence, _mainPredicateNode, dependencies, active, f_root);
				checkForSubSentences(_vpHead, dependencies, _a, false);
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
	 * @param object
	 * @param isNP
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
				analyzeSentence(f_root, head, filterDependencies(head, dependencies));
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
	 * @param sentence Tree structure of the sentence without the root node
	 * @param _active boolean value that is true when the sentence is an active sentence
	 * @param verb
	 * @param dependencies
	 * @param active
	 * enables additional checks in case no dobj relation is found
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
			excludeRelativeClauses(f_root, sentence,_nsubjpass);
			if(_nsubjpass.size() == 0) {
				//use dobj if available instead
				determineObjectFromDOBJ(verb, dependencies, _result);	
			}else if(_nsubjpass.size() > 1) {
				System.out.println("Passive sentence with more than one subject!?!?");
				if(Constants.DEBUG_EXTRACTION) printToConsole(_nsubjpass);
				IndexedWord _object = _nsubjpass.get(0).dep();
				ExtractedObject _obj = ElementsBuilder.createObject(f_root, f_sentence, f_fullSentence, _object,dependencies);
				_obj.setSubjectRole(true); //although it is an object it is the syntactic subject of the sentence
				_result.add(_obj);
				Tree _objectTGN = f_fullSentence.get(_object.index()-1);
				checkNPForSubsentence(_objectTGN,dependencies,_obj);
			}else {				
				IndexedWord _object = _nsubjpass.get(0).dep();
				ExtractedObject _obj = ElementsBuilder.createObject(f_root, f_sentence, f_fullSentence, _object,dependencies);
				_obj.setSubjectRole(true); //although it is an object it is the syntactic subject of the sentence
				_result.add(_obj);
				Tree _objectNode = f_fullSentence.get(_object.index()-1);
				checkNPForSubsentence(_objectNode,dependencies,_obj);
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


    /**
     *
     * @param verb
     * @param dependencies
     * @param _result
     */
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
					Tree _object = f_fullSentence.get(_cop.get(0).gov().index()-1);
					if(_object.parent(f_root).parent(f_root).value().equals("NP")) { //only if it is directly part of a noun phrase
						ExtractedObject _obj = ElementsBuilder.createObject(f_root, f_sentence, f_fullSentence, _cop.get(0).gov(),dependencies);
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
				Tree _object = f_fullSentence.get(_myPrep.get(0).dep().index()-1);
				if(_object.parent(f_root).parent(f_root).value().equals("NP")) { //only if it is directly part of a noun phrase
					ExtractedObject _obj = ElementsBuilder.createObject(f_root ,f_sentence, f_fullSentence, _myPrep.get(0).dep(),dependencies);
					_result.add(_obj);
					checkNPForSubsentence(_object,dependencies,_obj);	
				}else {
					if(Constants.DEBUG_EXTRACTION) System.out.println("No Object found!");
				}
			}
		}else {
			Tree _object = f_fullSentence.get(_myDobj.get(0).dep().index()-1);
			ExtractedObject _obj = ElementsBuilder.createObject(f_root, f_sentence, f_fullSentence, _myDobj.get(0).dep(),dependencies);
			checkNPForSubsentence(_object,dependencies,_obj);			
			_result.add(_obj);
		}
	}


	
	
	
	/**
     * Checks if noun phrase contains subsentences
	 * @param node
	 * @param dependencies collection of all TypedDependencies of the sentence
	 * @param obj
	 */
	private void checkNPForSubsentence(Tree node,Collection<TypedDependency> dependencies,ExtractedObject obj) {
		if(!f_ignoreNPSubSentences) {
			Tree _head = SearchUtils.getFullPhraseTree("NP",node, f_root);
			checkForSubSentences(_head, dependencies, obj,true);
		}
	}

	/**
     * @param f_root Tree structure of the whole sentence with the root node included
	 * @param sentence Tree structure of the sentence without the root node
	 * @param list list of all dependencies
	 */
	private void excludeRelativeClauses(Tree f_root, Tree sentence, List<TypedDependency> list) {
		for(int i=0;i<list.size();i++) {
			TypedDependency _td = list.get(i);
			if(_td.reln().getShortName().equals("rcmod")) {
				continue;
			}
			Tree _dep = f_fullSentence.get(_td.gov().index()-1);
			int _sentenceIndex = SearchUtils.getIndex(f_fullSentence, sentence.getLeaves());
            //get the top most sentence node
			while(!(_dep = _dep.parent(f_root)).value().equals("ROOT")) {
				if(sentence.value().equals(_dep.parent(f_root).value())) {
					int _partIndex = SearchUtils.getIndex(f_fullSentence, _dep.parent(f_root).getLeaves());
					if(_sentenceIndex >= _partIndex) {
						break;
					}
				}

				if((_dep.parent(f_root).value().equals("SBAR") || _dep.parent(f_root).value().equals("S") || _dep.parent(f_root).value().equals("PRN"))
						&& (!_dep.parent(f_root).parent(f_root).value().equals("SBAR"))) { //hack for relative clause TextToWorldModel.processing
					list.remove(i);
					i--;
					break;
				}
			}
		}
	}


	/** Prints out all dependencies to the console
	 * @param list of all dependencies of the sentence
	 */
	private void printToConsole(List<TypedDependency> list) {
		for(TypedDependency td:list) {
			Tree dep = f_fullSentence.get(td.dep().index()-1);
			System.out.println(td+" - "+SearchUtils.getFullNounPhrase(dep, f_root));
		}
	}

	/**
	 * @param f_sentenceNumber
	 */
	public void setSentenceNumber(int f_sentenceNumber) {
		this.f_sentenceNumber = f_sentenceNumber;
	}

	/**
	 * @return int SentenceNumber of the currently analyzed sentence
	 */
	public int getSentenceNumber() {
		return f_sentenceNumber;
	}

	/**
	 * @return T2PSentence, return the currently analyzed sentence
	 */
	public T2PSentence getBaseSentence() {
		return f_sentence;
	}

	/**
	 * @return a list of all the actions that could get extracted from the text
	 */
	public List<Action> getExtractedActions(){
		return f_actions;
	}


	/**
	 * @return the Conjunction elements in a list that were extracted from the text
	 */
	public List<ConjunctionElement> getExtractedConjunctions() {
		return f_conjs;
	}


	/**removes action me from the list of Actions extracted
	 * @param me Action that should be removed
	 */
	public void removeAction(Action me) {
		f_actions.remove(me);
	}
	
	@Override
	public String toString() {
		return f_sentence.toString();
	}
	
	}
