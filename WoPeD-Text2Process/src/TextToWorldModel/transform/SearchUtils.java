/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package TextToWorldModel.transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import worldModel.Action;
import worldModel.SpecifiedElement;
import worldModel.Specifier;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * 
 * provides utility methods for searching syntax trees and dependency relations
 *
 */
public class SearchUtils {
	

	/**
	 * 
	 * Counts how many of the children Trees given in the list match one of the provided terms
	 * @param terms
	 * @param childrenAsList
	 * @return
	 */
	public static int count(ArrayList<String> terms, List<Tree> childrenAsList) {
		int _result = 0;
		outer: for(Tree t:childrenAsList) {
			for(String s:terms) {
				if(t.value().equals(s)) {
					_result++;
					continue outer;
				}
			}
		}
		return _result;
	}
	
	/**
	 * @param _dependencies
	 * @param index
	 * @param index2
	 * @return
	 */
	public static List<TypedDependency> filter(
			Collection<TypedDependency> _dependencies, int start, int end) {
		ArrayList<TypedDependency> _result = new ArrayList<TypedDependency>();
		for(TypedDependency td:_dependencies) {
			if(td.reln().getShortName().equals("rcmod") ||
			   (( start <= td.gov().label().index() && (end >= td.gov().label().index())) 
					&&
			   ( start <= td.dep().label().index() && (end >= td.dep().label().index())))) {
				_result.add(td);
			}
		}
		return _result;
	}

	/**
	 * @param string
	 * @param typedDependenciesCollapsed
	 * @return
	 */
	public static List<TypedDependency> findDependency(String relName,
			Collection<TypedDependency> dependencies) {
		List<TypedDependency> _result = new ArrayList<TypedDependency>();
		for(TypedDependency td: dependencies) {
			if(td.reln().getShortName().equals(relName)) {
				_result.add(td);
			}
		}
		return _result;
	}
	
	
	public static List<TypedDependency> filterByGov(Action verb,List<TypedDependency> dep) {
		List<TypedDependency> _result = new ArrayList<TypedDependency>(); 
		for(TypedDependency td:dep) {				
			if(verb.getWordIndex() == td.gov().index() ||
				verb.getCopIndex() == td.gov().index() 
				/*|| (verb.getXcomp() != null && verb.getXcomp().getWordIndex() == td.gov().index())*/) {
				_result.add(td);
			}
		}
		return _result;
	}
	
	/**
	 * @param string
	 * @param typedDependenciesCollapsed
	 * @return
	 */
	public static List<TypedDependency> findDependency(List<String> relNames,
			Collection<TypedDependency> dependencies) {
		List<TypedDependency> _result = new ArrayList<TypedDependency>();
		for(String rn: relNames) {
			_result.addAll(findDependency(rn, dependencies));
		}
		return _result;
	}

	
	
	/**
	 * find the first occurrence of the given tag in a tree.
	 * depth-first search from left to right
	 * @param lookFor
	 * @param t
	 * @return
	 */
	public static Tree findFirst(String lookFor, Tree t) {
		return findFirst(lookFor, t, null);
	}
	
	public static Tree findFirst(String lookFor, Tree t,List<String> excludeTags) {
		if(t.value().equals(lookFor)) {
			return t;
		}//else
		for(Tree child:t.children()) {
			if(excludeTags != null && excludeTags.contains(child.value())) {
				continue;
			}
			Tree _found = findFirst(lookFor, child);
			if(_found != null) {
				return _found;
			}
		}
		return null;
	}

	/**
	 * @param string
	 * @param _syntax
	 * @return
	 */
	public static List<Tree> find(String lookFor, Tree t) {
		ArrayList<String> _lf = new ArrayList<String>(1);
		_lf.add(lookFor);
		return find(_lf,t);
	}
	
	/**
	 * @param string
	 * @param _syntax
	 * @return
	 */
	public static List<Tree> find(String lookFor, Tree t,List<String> excludeTags) {
		ArrayList<String> _lf = new ArrayList<String>(1);
		_lf.add(lookFor);
		return find(_lf,t,excludeTags);
	}
	
	/**
	 * @param string
	 * @param _syntax
	 * @return
	 */
	public static List<Tree> find(List<String> lookFor, Tree t) {
		return find(lookFor, t,null);
	}
	
	/**
	 * @param string
	 * @param _syntax
	 * @return
	 */
	public static List<Tree> find(List<String> lookFor, Tree t,List<String> excludeTags) {
		List<Tree> _result = new ArrayList<Tree>();
		boolean _found = false;
		for(String l:lookFor) {			
			if(t.value().equals(l)) {
				_result.add(t);
				_found = true;
				break;
			}//else
		}
		if(!_found) {
			for(Tree child:t.children()) {
				if(excludeTags != null && excludeTags.contains(child.value())) continue;
				_result.addAll(find(lookFor,child,excludeTags));
			}
		}		
		return _result;
	}
	
	/**
	 * does not take the root node which is provided into account
	 * @param string
	 * @param _syntax
	 * @return
	 */
	public static List<Tree> findChildren(List<String> lookFor, Tree t) {
		List<Tree> _result = new ArrayList<Tree>();
		for(Tree child:t.children()) {
			_result.addAll(find(lookFor,child));
		}
		return _result;
	}
	
	/**
	 * returns the actions from the list of actions which
	 * contains the specified element in some way (actor, resource, xcomp...)
	 * 
	 * @param to
	 * @param actions 
	 * @return
	 */
	public static Action getAction(SpecifiedElement to, List<Action> actions) {
		if(to instanceof Action) {
			return (Action)to;
		}
		
		for(Action a:actions) {
			if(specifiersContain(a.getSpecifiers(), to)) {
				return a;
			}
			if(a.getActorFrom() != null && (to.equals(a.getActorFrom()) || specifiersContain(a.getActorFrom().getSpecifiers(),to))) {
				return a;
			}
			if(a.getObject() != null && (to.equals(a.getObject()) || specifiersContain(a.getObject().getSpecifiers(),to))) {
				return a;
			}
			if(a.getXcomp() != null) {
				if(specifiersContain(a.getXcomp().getSpecifiers(),to)) {
					return a;
				}else if(a.getXcomp().getObject() != null && 
						(to.equals(a.getXcomp().getObject()) || specifiersContain(a.getXcomp().getObject().getSpecifiers(),to) )) {
					return a;
				}
			}
		}
		return null;
	}
	
	
	/**
	 * @param specifiers
	 * @param to
	 * @return
	 */
	private static boolean specifiersContain(ArrayList<Specifier> specifiers,
			SpecifiedElement to) {
		for(Specifier spec: specifiers) {
			if(spec.getObject() != null) {
				if(spec.getObject().equals(to) || specifiersContain(spec.getObject().getSpecifiers(), to)) {
					return true;
				}
			}				
		}
		return false;
	}

	public static String getFullNounPhrase(TreeGraphNode node) {
		return getFullPhrase("NP", node);
	}
	
	public static String getFullPhrase(String type, TreeGraphNode node) {
		Tree _subject = getFullPhraseTree(type, node);
		return PrintUtils.toString(_subject.getLeaves());
	}

	public static Tree getFullPhraseTree(String type, TreeGraphNode node) {		
		TreeGraphNode _subject = node;
		boolean going_up = true;
		while((going_up) || _subject.parent().label().value().equals(type)) {
			_subject = (TreeGraphNode)_subject.parent();
			if(_subject == null) {
				return null;
			}
			if(_subject.label().value().startsWith(type) || _subject.label().value().startsWith("W")) {
				going_up = false;
			}
		}
		return _subject;
	}
	
	/**
	 * @param fullSentence
	 * @param list
	 * @return
	 */
	public static int getIndex(List<Tree> fullSentence, List<Tree> list) {
		return getIndex(fullSentence, new ArrayList<Tree>(list), 0);
	}

	/**
	 * @param fullSentence
	 * @param list
	 * @return
	 */
	private static int getIndex(List<Tree> fullSentence, List<Tree> list,int startIndex) {
		for(int i=startIndex;i<fullSentence.size();) {
			for(int j=0;j<list.size();j++) {
				if(!fullSentence.get(i+j).value().equals(list.get(j).value())) {
					return getIndex(fullSentence,list,i+j+1);
				}
			}
			return i+1;
		}
		if(list.size() > 0) {
			list.remove(0);
			return getIndex(fullSentence, list, 0);
		}
		return -1;
	}

	/**
	 * @param phrase
	 * @param indicator
	 * @return
	 */
	public static boolean startsWithAny(String phrase, ArrayList<String> indicator) {
		for(String s:indicator) {
			if(phrase.startsWith(s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * returns the object of the keySet which has the highest associated count
	 * if the map is empty, null will be returned
	 * @param _countMap
	 * @return
	 */
	public static Object getMaxCountElement(HashMap<? extends Object, Integer> _countMap) {
		Object _best = null;
		int _bestCount = Integer.MIN_VALUE;
		for(Object obj:_countMap.keySet()) {
			if(_countMap.get(obj) > _bestCount) {
				_best = obj;
				_bestCount = _countMap.get(obj);
			}			
		}
		return _best;
	}

	/**
	 * @param from
	 * @param _dobj
	 * @return
	 */
	public static List<TypedDependency> filterByIndex(Action verb,List<TypedDependency> dep,boolean smaller) {
		List<TypedDependency> _result = new ArrayList<TypedDependency>(); 
		for(TypedDependency td:dep) {				
			if((verb.getWordIndex() < td.dep().index()) == smaller ) {
				_result.add(td);
			}
		}
		return _result;
	}

	/**
	 * @param gov
	 * @param dependencies
	 * @return
	 */
	public static List<TypedDependency> filterByGov(TreeGraphNode gov,Collection<TypedDependency> dep) {
		List<TypedDependency> _result = new ArrayList<TypedDependency>(); 
		for(TypedDependency td:dep) {				
			if(gov.index() == td.gov().index()) {
				_result.add(td);
			}
		}
		return _result;
	}
	
}
