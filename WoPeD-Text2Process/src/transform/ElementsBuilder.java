/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import etc.Constants;
import processing.FrameNetWrapper;
import processing.ProcessingUtils;
import processing.WordNetWrapper;
import text.T2PSentence;
import worldModel.Action;
import worldModel.Actor;
import worldModel.ExtractedObject;
import worldModel.Resource;
import worldModel.SpecifiedElement;
import worldModel.Specifier;
import worldModel.Specifier.SpecifierType;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TypedDependency;

public class ElementsBuilder {
	
	public static Actor createActor(T2PSentence origin, List<Tree> fullSentence, TreeGraphNode node,Collection<TypedDependency> dependencies) {
		Actor _a = null;					
		String _fullNoun = getFullNoun(node, dependencies);
		if(!WordNetWrapper.canBePersonOrSystem(_fullNoun, node.value().toLowerCase())) {		
			//try to extract the real actor here?
			if(node.parent().value().equals("CD") || WordNetWrapper.canBeGroupAction(node.value())) { //one of the physicians
				List<TypedDependency> _preps = SearchUtils.findDependency("prep", dependencies);
				for(TypedDependency spec: _preps) {
					if(Constants.f_realActorPPIndicators.contains(spec.reln().getSpecific()) && spec.gov().equals(node)) {
						//possible candidate of the real actor
						_fullNoun = getFullNoun(spec.dep(), dependencies);
						if(WordNetWrapper.canBePersonOrSystem(_fullNoun,spec.dep().value())) {
							_a = createActorInternal(origin, fullSentence, spec.dep(),dependencies);
							break;
						}					
					}							
				}
			}
			if(_a == null) {
				_a = createActorInternal(origin, fullSentence, node,dependencies);
				_a.setUnreal(true);
			}
		}else {
			_a = createActorInternal(origin, fullSentence, node,dependencies);
		}
		if(Constants.DEBUG_EXTRACTION) System.out.println("Identified actor: "+_a);
		return _a;
	}

	private static Actor createActorInternal(T2PSentence origin,
			 List<Tree> fullSentence, TreeGraphNode node,
			Collection<TypedDependency> dependencies) {
		Actor _a = new Actor(origin,node.index(),node.value().toLowerCase());
		determineNounSpecifiers(origin, fullSentence, node, dependencies, _a);
		if(WordNetWrapper.isMetaActor(getFullNoun(node, dependencies),node.value())) {
			_a.setMetaActor(true);
		}	
		return _a;
	}

	public static Action createAction(T2PSentence origin, List<Tree> fullSentence, TreeGraphNode node,Collection<TypedDependency> dependencies,boolean active) {
		Action _result = new Action(origin,node.index(),node.value());
		//search for an auxiliary verb
		String _aux = getAuxiliaries(node, dependencies);
		if(_aux.length() > 0)
			_result.setAux(_aux);
		TreeGraphNode _mod = getModifiers(node, dependencies);
		if(_mod != null) {
			_result.setMod(_mod.value());
			_result.setModPos(_mod.index());			
		}
		_result.setNegated(isNegated(node,dependencies));	
		TreeGraphNode _cop = getCop(node, dependencies);
		if(_cop != null) {
			_result.setCop(_cop.value(),_cop.index());
		}	
		String _prt = getPrt(node, dependencies);
		if(_prt.length() > 0) {
			_result.setPrt(_prt);
		}	
		TreeGraphNode _iobj = getIObj(node, dependencies);
		if(_iobj != null) {
			Specifier _sp = new Specifier(origin,_iobj.index(),PrintUtils.toString(_iobj.getLeaves()));
			_sp.setSpecifierType(SpecifierType.IOBJ);
			_result.addSpecifiers(_sp);
		}	
		if(!active) {
			checkDobj(node,dependencies,_result,origin,fullSentence);			
		}
		//search for xcomp		
		List<TypedDependency> _toCheck = SearchUtils.findDependency(ListUtils.getList("xcomp","dep"),dependencies);
		for(TypedDependency td:_toCheck) {
			if(td.gov().equals(node)) {
				if(td.reln().getShortName().equals("dep")) {
					//only consider verbs and forwards dependencies
					if(!td.dep().parent().value().startsWith("V") || (td.dep().index()<td.gov().index())) {						
						continue;
					}
				}
				//found something
				TreeGraphNode _xcompNode = td.dep();
				Action _xcomp = createAction(origin, fullSentence, _xcompNode, dependencies, true);
				_result.setXcomp(_xcomp);				
				break;
			}
		}
		//extracting further information and specifiers
		Tree _vpHead = SearchUtils.getFullPhraseTree("VP",node);	
		extractSBARSpecifier(origin, fullSentence, _result, _vpHead,node);
		extractPPSpecifier(origin, fullSentence, _result, node,dependencies);
		extractRCMODSpecifier(origin, _result, node,dependencies);
		if(Constants.DEBUG_EXTRACTION) System.out.println("Identified Action: "+_result);	
		return _result;
	}

	/**
	 * @param node
	 * @param dependencies
	 * @param fullSentence 
	 * @return
	 */
	private static void checkDobj(TreeGraphNode node,Collection<TypedDependency> dependencies,Action result,T2PSentence origin, List<Tree> fullSentence) {
		List<String> _lookFor = ListUtils.getList("dobj");
		List<TypedDependency> _toCheck = SearchUtils.findDependency(_lookFor,dependencies);
		for(TypedDependency td:_toCheck) {
			if(td.gov().equals(node)) {
				System.err.println("HEy we found a dobj in a passive sentence!!!"+td);
				Specifier _sp = new Specifier(origin,td.dep().index(),getFullNoun(td.dep(), dependencies));
				_sp.setSpecifierType(SpecifierType.DOBJ);
				ExtractedObject _obj = ElementsBuilder.createObject(origin, fullSentence, td.dep(), dependencies);
				_sp.setObject(_obj);
				result.addSpecifiers(_sp);
			}
		}		
	}

	/**
	 * @param node
	 * @param dependencies
	 * @return
	 */
	private static TreeGraphNode getCop(TreeGraphNode node, Collection<TypedDependency> dependencies) {
		List<TypedDependency> _toCheck = SearchUtils.findDependency(ListUtils.getList("cop"),dependencies);
		for(TypedDependency td:_toCheck) {
			if(td.dep().equals(node)) {
				//found something
				return td.gov();					
			}
		}
		return null;
	}
	
	/**
	 * @param node
	 * @param dependencies
	 * @return
	 */
	private static String getPrt(TreeGraphNode node, Collection<TypedDependency> dependencies) {
		List<String> _lookFor = ListUtils.getList("prt");
		return findDependants(node, dependencies, _lookFor,true);	
	}
	
	/**
	 * @param node
	 * @param dependencies
	 * @return
	 */
	private static TreeGraphNode getIObj(TreeGraphNode node, Collection<TypedDependency> dependencies) {
		List<TypedDependency> _toCheck = SearchUtils.findDependency(ListUtils.getList("iobj"),dependencies);
		for(TypedDependency td:_toCheck) {
			if(td.dep().equals(node)) {
				//found something
				return td.gov();					
			}
		}
		return null;	
	}

	/**
	 * cop(case-6, is-3)
	 * neg(case-6, not-4)
	 * @param node
	 * @param dependencies
	 * @return
	 */
	private static boolean isNegated(TreeGraphNode node,Collection<TypedDependency> dependencies) {
		TreeGraphNode _node = node;
		//setting node to the object in case of a cop sentence (see example in documentation)
		List<TypedDependency> _toCheck = SearchUtils.findDependency("cop",dependencies);
		for(TypedDependency td:_toCheck) {
			if(td.dep().equals(_node)){
				_node = td.gov();
				break;
			}
		}
		_toCheck = SearchUtils.findDependency("neg",dependencies);
		for(TypedDependency td:_toCheck) {
			if(td.gov().equals(_node)) {
				return true;
			}
		}		
		return false;
	}

	private static String getAuxiliaries(TreeGraphNode node, Collection<TypedDependency> dependencies) {
		List<String> _lookFor = ListUtils.getList("aux","auxpass");
		return findDependants(node, dependencies, _lookFor,true);
	}

	private static String findDependants(TreeGraphNode node,Collection<TypedDependency> dependencies, List<String> lookFor,boolean isGovernor) {
		List<TypedDependency> _toCheck = SearchUtils.findDependency(lookFor,dependencies);
		StringBuilder _b = new StringBuilder();
		for(TypedDependency td:_toCheck) {
			if(isGovernor) {
				if(td.gov().equals(node)) {
					//found something
					_b.append(td.dep().value());
					_b.append(" ");
				}
			}else {
				if(td.dep().equals(node)) {
					//found something
					_b.append(td.gov().value());
					_b.append(" ");
				}	
			}
		}
		if(_b.length() > 0)_b.deleteCharAt(_b.length()-1);
		return _b.toString();
	}
	
	private static TreeGraphNode getModifiers(TreeGraphNode node, Collection<TypedDependency> dependencies) {
		List<TypedDependency> _toCheck = SearchUtils.findDependency(ListUtils.getList("advmod","acomp"),dependencies);
		for(TypedDependency td:_toCheck) {
			if(td.gov().equals(node)) {
				//only take following modifiers as other are adverbs (e.g. quickly walk vs. walk >out<)
				if(td.gov().index() < td.dep().index() && !Constants.f_sequenceIndicators.contains(td.dep().value())) {
					//found something
					return td.dep();			
				}
			}
		}
		return null;
	}
	
	public static Action createActionSyntax(T2PSentence origin, List<Tree> fullSentence, Tree vpHead,boolean active) {
		List<Tree> _verbParts = extractVerbParts(vpHead,active);
		int index = 0;
		if(vpHead.getLeaves().get(0) instanceof TreeGraphNode) {
			index = ((TreeGraphNode)vpHead.getLeaves().get(0)).index();
		}else {
			index = SearchUtils.getIndex(fullSentence, vpHead.getLeaves());
		}		
		Action _result = new Action(origin,index,PrintUtils.toString(_verbParts));
		//extracting further information and specifiers
		extractSBARSpecifier(origin, fullSentence, _result, vpHead,null);		
		//determineLinkedActions
		extractPPSpecifierSyntax(origin, fullSentence, _result, vpHead);	
		if(Constants.DEBUG_EXTRACTION) System.out.println("Identified Action: "+_result);	
		return _result;
	}
	
	/**
	 * @param active 
	 * @param _vp
	 * @return
	 */
	private static List<Tree> extractVerbParts(Tree node, boolean active) {
		ArrayList<Tree> _result = new ArrayList<Tree>();
		if((node.isLeaf())) {
			/*&& (
			node.label().value().startsWith("V")||
			node.label().value().equals("TO")
			)
			||
			node.label().value().equals("PP")) { *///Indicates some Verb Form
			_result.add(node);
		}else {
			for(Tree t:node.children()) {
				if(!t.value().equals("SBAR") && !t.value().equals("NP") && !t.value().equals("ADJP") && !t.value().equals("ADVP") && !t.value().equals("PRN")) {
					if(!(node.value().equals("PP"))) {
						_result.addAll(extractVerbParts(t,active));
					}
				}
			}
		}
		return _result;
	}
	
	/**
	 * creates a new specified elements which can either be a Resource
	 * or an Actor
	 * @param origin
	 * @param world
	 * @param fullSentence
	 * @param node
	 * @param dependencies
	 * @return
	 */
	public static ExtractedObject createObject(T2PSentence origin, List<Tree> fullSentence, TreeGraphNode node,Collection<TypedDependency> dependencies) {
		String _fullNoun = getFullNoun(node, dependencies);
		//TODO systems should be marked so a reference resolution of resources can refer to them
		if(WordNetWrapper.canBePersonOrSystem(_fullNoun, node.value().toLowerCase()) || ProcessingUtils.canBePersonPronoun(node.value())) {
			Actor _a = createActorInternal(origin, fullSentence, node, dependencies);	
			_a.setSubjectRole(false);			
			if(Constants.DEBUG_EXTRACTION) System.out.println("Identified object: "+_a);
			return _a;
		}
		Resource _r = new Resource(origin,node.index(),node.value().toLowerCase());
		_r.setSubjectRole(false);
		determineNounSpecifiers(origin, fullSentence, node, dependencies, _r);		
		
		if(Constants.DEBUG_EXTRACTION) System.out.println("Identified object: "+_r);
		return _r;				
	}

	private static String getFullNoun(TreeGraphNode node,
			Collection<TypedDependency> dependencies) {
		List<TypedDependency> _toCheck = SearchUtils.findDependency(ListUtils.getList("nn","dep"),dependencies);
		//extracting full compound name
		StringBuilder _builder = new StringBuilder();
		StringBuilder _addAfter = new StringBuilder();
		if(_toCheck.size() > 0) {
			for(TypedDependency td:_toCheck) {
				if(td.gov().equals(node)) {					
					if(td.reln().getShortName().equals("dep")) {
						if(td.gov().index()+1 !=  td.dep().index()) {
							continue; //skip this one
						}
						_addAfter.append(' ');
						_addAfter.append(td.dep().value());						
					}else {
						_builder.append(td.dep().value());
						_builder.append(' ');
					}
				}
			}
		}
		_builder.append(node.value());
		_builder.append(_addAfter.toString());
		String fullNoun = _builder.toString().toLowerCase();
		return fullNoun;
	}
	
	private static void determineNounSpecifiers(T2PSentence origin,
			List<Tree> fullSentence, TreeGraphNode node,
			Collection<TypedDependency> dependencies, ExtractedObject element) {
		
		
		findDeterminer(node, dependencies, element);
		findAMODSpecifiers(origin, node, dependencies, element);
		findNNSpecifiers(origin, node, dependencies, element);
		findINFMODSpecifiers(origin, node, dependencies, element);
		getPARTMODSpecifiers(origin, node, dependencies, element);
		getSpecifierFromDependencies(origin,node,dependencies,element,"num",SpecifierType.NUM);
		
		//extracting further information and specifiers
		Tree _tree = SearchUtils.getFullPhraseTree("NP",node);	
		extractSBARSpecifier(origin, fullSentence, element, _tree,node);		
		extractPPSpecifier(origin, fullSentence, element, node,dependencies);				
		if(Constants.f_relativeResolutionTags.contains(node.parent().value()) ||
				Constants.f_relativeResolutionWords.contains(node.value())) {
			if(node.parent().parent().children().length == 1) {
				for(Specifier spec:element.getSpecifiers(SpecifierType.PP)) {
					if("of".equals(spec.getHeadWord())) {
						return;
					}
				}
				element.setResolve(true);	
			}
		}
	}

	

	/**
	 * @param origin
	 * @param node
	 * @param dependencies
	 * @param element
	 */
	private static void findINFMODSpecifiers(T2PSentence origin,TreeGraphNode node, Collection<TypedDependency> dependencies,
			SpecifiedElement element) {
		List<TypedDependency> _toCheck = SearchUtils.findDependency("infmod",dependencies);
		if(_toCheck.size() > 0) {
			StringBuilder _builder = new StringBuilder();
			for(TypedDependency td:_toCheck) {
				if(td.gov().equals(node)) {					
					//found it
					_toCheck = SearchUtils.findDependency(ListUtils.getList("aux","cop","neg"),dependencies);
					for(TypedDependency acn:_toCheck) {
						if(acn.gov().equals(td.dep())) {
							_builder.append(acn.dep().value());
							_builder.append(" ");
						}
					}
					_builder.append(td.dep().value());
					Specifier _sp = new Specifier(origin,td.dep().index(),_builder.toString());
					_sp.setSpecifierType(SpecifierType.INFMOD);
					element.addSpecifiers(_sp);
					break;				
				}
			}			
		}
	}
	
	
	/**
	 * @param origin
	 * @param node
	 * @param dependencies
	 * @param element
	 */
	private static void getPARTMODSpecifiers(T2PSentence origin,
			TreeGraphNode node, Collection<TypedDependency> dependencies,
			ExtractedObject element) {
		List<TypedDependency> _toCheck = SearchUtils.findDependency("partmod",dependencies);
		if(_toCheck.size() > 0) {
			for(TypedDependency td:_toCheck) {
				if(td.gov().equals(node)) {					
					String _phr = SearchUtils.getFullPhrase("VP", td.dep());
					//found it					
					Specifier _sp = new Specifier(origin,td.dep().index(),_phr);
					_sp.setSpecifierType(SpecifierType.PARTMOD);
					element.addSpecifiers(_sp);
					break;				
				}
			}			
		}
	}

	private static void extractPPSpecifierSyntax(T2PSentence origin,
			List<Tree> fullSentence, SpecifiedElement element, Tree fullPhrase) {
		//search for a PP determiner
		List<Tree> _ppList = getPPSpecifierSyntax(fullPhrase);
		for(Tree pp:_ppList){
			Specifier _sp = new Specifier(origin,SearchUtils.getIndex(fullSentence, _ppList),PrintUtils.toString(pp));
			_sp.setSpecifierType(SpecifierType.PP);
			element.addSpecifiers(_sp);
		}
	}
	
	private static void extractPPSpecifier(T2PSentence origin, List<Tree> fullSentence, SpecifiedElement element,TreeGraphNode node, Collection<TypedDependency> dependencies) {
		//search for a PP determiner
		List<TypedDependency> _toCheck = SearchUtils.findDependency(ListUtils.getList("prep","prepc"),dependencies);
		List<TypedDependency> _rcMod = SearchUtils.findDependency(ListUtils.getList("rcmod"),dependencies);
		for(TypedDependency td:_toCheck) {
			String _cop = null;
			if(element instanceof Action) {
				Action _act = (Action)element;
				_cop = _act.getCop();
			}
			if((td.gov().equals(node) || td.gov().value().equals(_cop)) && !partOfrcMod(_rcMod, td)) {				
				//found something
				Tree _phraseTree = SearchUtils.getFullPhraseTree("PP", td.dep());
				if(!_phraseTree.parent().value().equals("PRN")) {
					_phraseTree = deleteBranches(ListUtils.getList("S","SBAR"),_phraseTree);
					String _phrase = PrintUtils.toString(_phraseTree);
					String _specific = null;
					if(_phrase.indexOf(' ') >= 0) { //does not have to S(interact ...) 
						if(td.reln().getSpecific() != null) {// ... is reviewed, resulting in.... (BPMN MRG Ex5)
							_phrase = _phrase.substring(_phrase.indexOf(' ')); //cutting of first word (is included in getSpecific)
							_specific = td.reln().getSpecific().replace('_', ' ');
							_phrase = _specific+_phrase;
						}
						Specifier _sp = new Specifier(origin,td.dep().index(),_phrase);
						_sp.setSpecifierType(SpecifierType.PP);
						if(td.dep().parent().parent().value().startsWith("NP")) {
							ExtractedObject _object = createObject(origin, fullSentence, td.dep(), dependencies);
							_sp.setObject(_object);	
							//TODO add conjunct elements							
						}
						_sp.setHeadWord(_specific);
						FrameNetWrapper.determineSpecifierFrameElement(element, _sp);
						element.addSpecifiers(_sp);				
				}
				}
			}
		}
	}
	
	private static void extractRCMODSpecifier(T2PSentence origin, SpecifiedElement element,TreeGraphNode node, Collection<TypedDependency> dependencies) {
		//search for a rcmod determiner
		List<TypedDependency> _toCheck = SearchUtils.findDependency(ListUtils.getList("rcmod"),dependencies);
		for(TypedDependency td:_toCheck) {
			String _cop = null;
			if(element instanceof Action) {
				Action _act = (Action)element;
				_cop = _act.getCop();
			}
			if(td.dep().equals(node) || td.dep().value().equals(_cop)) {				
				//found something
				Tree _phraseTree = SearchUtils.getFullPhraseTree("PP", td.gov());
				if(_phraseTree != null) {//it was not a PP, but e.g. an SBAR
					_phraseTree = deleteBranches(ListUtils.getList("S","SBAR"),_phraseTree);
					String _phrase = PrintUtils.toString(_phraseTree);
					Specifier _sp = new Specifier(origin,td.dep().index(),_phrase);
					_sp.setSpecifierType(SpecifierType.RCMOD);
					element.addSpecifiers(_sp);
				}
			}
		}
	}

	private static boolean partOfrcMod(List<TypedDependency> _rcMod,TypedDependency td) {
		for(TypedDependency rcm:_rcMod) {
			if(rcm.gov().equals(td.dep())) {
				Tree _phraseTree = SearchUtils.getFullPhraseTree("PP", td.dep());
				_phraseTree = deleteBranches(ListUtils.getList("S","SBAR"),_phraseTree);
				String _phrase = PrintUtils.toString(_phraseTree).toLowerCase();
				if(Constants.f_conditionIndicators.contains(_phrase)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @param string
	 * @param tree
	 */
	private static Tree deleteBranches(List<String> list, Tree input) {
		Tree _result = input.deepCopy();
		deleteBranchesInternal(list, _result);
		return _result;
	}
	
	/**
	 * only pass copies in here!
	 * @param string
	 * @param tree
	 */
	private static void deleteBranchesInternal(List<String> list, Tree tree) {
		for(int i=0;i<tree.children().length;i++) {
			Tree t=tree.children()[i];
			if(list.contains(t.value())) {
				ProcessingUtils.removeChild(tree, i);
				i--;
			}else {
				deleteBranches(list, t);
			}
		}
	}

	private static List<Tree> getPPSpecifierSyntax(Tree fullPhrase) {
		ArrayList<String> _excludes = new ArrayList<String>();
		_excludes.add("SBAR");
		_excludes.add("S");
		_excludes.add("NP");
		_excludes.add("PRN");
		//search for a PP determiner
		return SearchUtils.find("PP", fullPhrase,_excludes);
		
	}

	private static void extractSBARSpecifier(T2PSentence origin,List<Tree> fullSentence, SpecifiedElement element, Tree phraseHead, TreeGraphNode node) {
		//search for an SBAR determiner
		ArrayList<String> _excludes = new ArrayList<String>();
		List<Tree> _sbarList = SearchUtils.find("SBAR", phraseHead,_excludes);
		for(Tree sbar:_sbarList){
			Tree _sbarNode = sbar.getLeaves().get(0);
			Tree _phraseNode = phraseHead.getLeaves().get(0);
			int idx1=0;
			int idx2=0;
			if(_sbarNode instanceof TreeGraphNode && node != null) {
				idx1 = ((TreeGraphNode)sbar.getLeaves().get(0)).index();
				idx2 = node.index();
			}else {
				idx1 = SearchUtils.getIndex(fullSentence, _sbarNode.getLeaves());
				idx2 = SearchUtils.getIndex(fullSentence, _phraseNode.getLeaves());
			}			
			if( idx1 > idx2) {
				Specifier _sp = new Specifier(origin,SearchUtils.getIndex(fullSentence, sbar.getLeaves()),PrintUtils.toString(sbar));
				_sp.setSpecifierType(SpecifierType.SBAR);
				element.addSpecifiers(_sp);
			}
		}
	}
	
	/**
	 * @param origin
	 * @param node
	 * @param dependencies
	 * @param _a
	 */
	private static void findAMODSpecifiers(T2PSentence origin,
			TreeGraphNode node, Collection<TypedDependency> dependencies,
			SpecifiedElement element) {
		getSpecifierFromDependencies(origin, node, dependencies, element, "amod", SpecifierType.AMOD);
	}

	private static void findNNSpecifiers(T2PSentence origin,TreeGraphNode node, Collection<TypedDependency> dependencies,
			SpecifiedElement element) {
		getSpecifierFromDependencies(origin, node, dependencies, element,"nn",SpecifierType.NN);
		List<TypedDependency> _toCheck = SearchUtils.findDependency("dep",dependencies);
		//extracting compound names which were only recognized as dependencies
		if(_toCheck.size() > 0) {
			for(TypedDependency td:_toCheck) {
				if(td.gov().equals(node)) {					
					if(td.gov().index()+1 !=  td.dep().index()) {
						continue; //skip this one
					}					
					Specifier _sp = new Specifier(origin,td.dep().index(),td.dep().value().toLowerCase());
					_sp.setSpecifierType(SpecifierType.NNAFTER);
					element.addSpecifiers(_sp);
				}
			}
			
		}
	}

	private static void getSpecifierFromDependencies(T2PSentence origin,
			TreeGraphNode node, Collection<TypedDependency> dependencies,
			SpecifiedElement element, String depType, SpecifierType specifierType) {
		//search for specifiers
		List<TypedDependency> _toCheck = SearchUtils.findDependency(depType,dependencies);
		int _index = -1;
		if(_toCheck.size() > 0) {
			StringBuilder _builder = new StringBuilder();
			for(TypedDependency td:_toCheck) {
				if(td.gov().equals(node)) {					
					_builder.append(td.dep().value());
					_builder.append(' ');
					List<TypedDependency> _toCheck2 = SearchUtils.findDependency("conj",dependencies);
					for(TypedDependency td2:_toCheck2) {
						if(td2.gov().equals(td.dep())) {
							//found a conjunction -> also add this one
							_builder.append(td2.reln().getSpecific());
							_builder.append(' ');
							_builder.append(td2.dep().value());
							_builder.append(' ');
						}
					}
					if(_index == -1) _index = td.dep().index();
				}
			}
			if(_index != -1) {
				_builder.deleteCharAt(_builder.length()-1);
				Specifier _sp = new Specifier(origin,_index,_builder.toString());
				_sp.setSpecifierType(specifierType);
				element.addSpecifiers(_sp);
			}
		}
	}
	

	private static void findDeterminer(TreeGraphNode node,Collection<TypedDependency> dependencies, ExtractedObject _r) {
		//search for a determiner/article etc.
		List<TypedDependency> _toCheck = new ArrayList<TypedDependency>();
		_toCheck.addAll(SearchUtils.findDependency("poss",dependencies));
		_toCheck.addAll(SearchUtils.findDependency("det",dependencies));
		for(TypedDependency td:_toCheck) {
			if(td.gov().equals(node)) {
				//found something
				_r.setDeterminer(td.dep().value());
				break;
			}
		}
	}

	

}
