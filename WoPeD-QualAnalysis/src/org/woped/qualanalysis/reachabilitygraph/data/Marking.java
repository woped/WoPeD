package org.woped.qualanalysis.reachabilitygraph.data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.woped.core.analysis.StructuralAnalysis;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;

/**
 * Specifies a particular marking of a reachability / coverability object each
 * marking contains every place and its token state aswell as the transitions
 * that can be activated from the state
 * 
 */
public class Marking {

	/**
	 * Construct a marking from the specified ModelElementContainer
	 * 
	 * @param source
	 *            specifies the element container that should serve as a source.
	 *            The container will be kept as a reference for preserving the
	 *            structure only. The initial marking will be copied and stored
	 *            locally.
	 */
	public Marking(IEditor source, String sourceT) {
		StructuralAnalysis sa = new StructuralAnalysis(source);
		this.source = source;
		this.sourceT.add(sourceT);
		currentMarking = new TreeMap<String, Integer>(new Comp_mf());
		// Store the marking of each place
		Iterator places = sa.getPlacesIterator();
		while (places.hasNext()) {
			PlaceModel currentPlace = (PlaceModel) places.next();
			if(currentPlace.getId().contains("CENTER_PLACE_") && currentPlace.getVirtualTokenCount() > 0) {
				centerPlace = true;
			}
			if(currentPlace.getVirtualTokenCount() > 60000) {
				iscoverObject = true;
			}
			currentMarking.put(currentPlace.getId(), currentPlace.getVirtualTokenCount());
		}
		// Store the active transitions
		netTransitions = new HashSet<String>();
		allTransitions = source.getModelProcessor().getElementContainer().getElementsByType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
		allTransitions.putAll(source.getModelProcessor().getElementContainer().getElementsByType(PetriNetModelElement.TRANS_OPERATOR_TYPE));
		allTransitions.putAll(source.getModelProcessor().getElementContainer().getElementsByType(PetriNetModelElement.SUBP_TYPE));
		Iterator transitions = allTransitions.values().iterator();
		while (transitions.hasNext()) {
			TransitionModel currentTransition = (TransitionModel) transitions.next();
			if(currentTransition.isActivated()) {
				netTransitions.add(currentTransition.getId());
			}
		}

		netStructure = source.getModelProcessor().getElementContainer();
	}

	/**
	 * Compares the current marking to otherMarking. This method is required to
	 * build the coverability graph.
	 * 
	 * @param otherMarking
	 *            specifies the marking to which the current marking should be
	 *            compared
	 * @return true if the current marking is bigger than otherMarking. Returns
	 *         false if otherMarking is smaller or equal and if the two markings
	 *         are not comparable
	 */
	boolean isGreaterThan(Marking otherMarking) {
		// Both markings must have the same size for this method to return true
		if(otherMarking.currentMarking.size() != currentMarking.size())
			return false;
		// If Transitions leading to the state are not equal return false
		TreeSet<String> sourceThelp = (TreeSet<String>) this.sourceT.clone();
		TreeSet<String> othersourceThelp = (TreeSet<String>) otherMarking.sourceT.clone();
		if(!this.iscoverObject && !otherMarking.iscoverObject && !sourceThelp.containsAll(othersourceThelp)) {
			return false;
		}
		boolean isGreater = true;
		Iterator currentThisMarking = currentMarking.values().iterator();
		Iterator currentOtherMarking = otherMarking.currentMarking.values().iterator();
		Integer oh;
		while (isGreater && currentThisMarking.hasNext()) {
			int currint = (Integer) currentThisMarking.next();
			int otherint = (Integer) currentOtherMarking.next();
			if(otherint > 60000) {
				otherint = otherint - 63000;
			}
			if(currint >= otherint && otherint != 0) {
				isGreater = true;
			}
			else if(currint == otherint) {
				isGreater = true;
			}
			else
				isGreater = false;
		}
		if(isGreater && otherMarking.isfirst()) {
			this.setnotfirst();
			return false;
		}
		else {
			return isGreater;
		}

	}

	/**
	 * Sets the current marking to a coverability object of the other marking
	 * 
	 * @param marking
	 *            object which the calling object is greather than
	 */
	public void setCoverability(Marking otherMarking) {
		Iterator currentThisMarking = currentMarking.keySet().iterator();
		Iterator currentOtherMarking = otherMarking.currentMarking.values().iterator();
		String wert = null;
		while (currentThisMarking.hasNext()) {
			wert = (String) currentThisMarking.next();
			int thismark = (Integer) currentMarking.get(wert);
			int othermark = ((Integer) currentOtherMarking.next());
			if(othermark>60000){
				othermark=othermark-60000;
			}
			if(thismark < othermark && thismark < 60000) {
				currentMarking.put(wert, 63000 + thismark);
				BuildReachability.reachBuilt = true;
				this.setreachabilitynotbuilt();
			}
		}
		iscoverObject = true;
	}

	/**
	 * Prints the Current Objects Tokenstatus on the Console for checking the
	 * current marking
	 */
	public String print() {
		String value = "( ";
		Iterator it = currentMarking.keySet().iterator();
		while (it.hasNext()) {
			String id = it.next().toString();
			value = value + currentMarking.get(id) + " ";
		}
		value = value + ")";
		return value;
	}

	/**
	 * Return a String of the calling marking which is used for the places in
	 * the graphic representation
	 * 
	 * @return returns a string represantation of the calling marking object
	 */
	public String toString() {
		String value = "( ";
		Iterator it = currentMarking.keySet().iterator();
		while (it.hasNext()) {
			String gel = (String) it.next();
			if(!gel.contains("CENTER_PLACE_")) {
				if(currentMarking.get(gel) >= 60000) {
					value = value + "w" + " ";
				}
				else {
					value = value + currentMarking.get(gel).toString() + " ";
				}
			}
		}
		value = value + ")";
		return value;
	}

	/**
	 * Prints the order of the places in a marking object on the console
	 */
	public void printseq() {
		Iterator it = currentMarking.keySet().iterator();
		String output = "";
		while (it.hasNext()) {
			output += (it.next() + " ");
		}
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- printseq() " + output + this.getClass().getName());
	}

	/**
	 * returns the place order of a marking object which is used on the graphic
	 * represantation for the legend
	 * 
	 * @return LinkedList containing the ordered PlaceObjects
	 */
	public LinkedList<PlaceModel> getKeySet() {
		LinkedList<PlaceModel> erg = new LinkedList<PlaceModel>();
		Iterator keyit = currentMarking.keySet().iterator();
		while (keyit.hasNext()) {
			String curr = keyit.next().toString();
			if(!curr.contains("CENTER_PLACE_")) {
				erg.add((PlaceModel) source.getModelProcessor().getElementContainer().getElementById(curr));
			}
		}
		return erg;
	}

	/**
	 * method for receiving all transitions that can be activated from the
	 * current marking used to built the reachability from a marking
	 * 
	 * @return unordered hash set containing all active transitions
	 */
	public HashSet<String> getTransitions() {
		return netTransitions;
	}

	/**
	 * method for getting the current marking token state
	 * 
	 * @return ordered TreeMap where the key is the placeid and the value is the
	 *         token count on the place
	 */
	public TreeMap<String, Integer> getMarking() {
		return currentMarking;
	}

	/**
	 * method to check if all transitions from the current marking have been
	 * activated gives information if the reachability starting from the current
	 * marking has been built
	 * 
	 * @return true if reachability built, false if not
	 */
	public boolean reachabilitybuilt() {
		return reachabilityBuilt;
	}

	/**
	 * method to set the reachability built status to true
	 */
	public void setreachabilitybuilt() {
		// Always rebuild reachability if it is a coverability object
		if(!iscoverObject) {
			reachabilityBuilt = true;
		}

	}

	/**
	 * method to set the reachability built status to false
	 */
	public void setreachabilitynotbuilt() {
		reachabilityBuilt = false;
	}

	/**
	 * method to create a unique key for each marking, required for the marking
	 * list to identify markings
	 * 
	 * @return a unique key for a marking, same markings also have the same key
	 */
	public String getKey() {
		Iterator it = currentMarking.keySet().iterator();
		String hash = "";
		while (it.hasNext()) {
			String temp = (String) it.next();
			hash = hash + temp + "_" + currentMarking.get(temp).intValue();
		}

		return (hash);
	}

	/**
	 * sets a marking status to the first of its kind used for coverability
	 * graph so it is able to maintain the first marking which can be covered if
	 * this is set a marking can notbe covered by another one
	 */
	public void setfirst() {
		first = true;
	}

	/**
	 * sets a marking status not to the first of its kind used for coverability
	 * graph so it is able to maintain the first marking which can be covered if
	 * this is set a marking is free to be covered
	 */
	public void setnotfirst() {
		first = false;
	}

	/**
	 * gives information about if a marking is the first one and so it can't be
	 * covered
	 * 
	 * @return true if it is the initial marking
	 */
	public boolean isfirst() {
		return first;
	}

	/**
	 * checks a marking if it contains centerPlaces which are used for
	 * xor-join-splits
	 * 
	 * @return true if a marking contains a center place
	 */
	public boolean centerPlace() {
		return centerPlace;
	}

	/**
	 * sets a marking to the initial (start) marking
	 */
	public void setInitial() {
		isInitial = true;
	}

	/**
	 * check if a marking is the initial (start) marking from which the
	 * reachability building is
	 * 
	 * @return true if a marking is the initial (start) setting of the built
	 *         graph, false if it is a otherMarking
	 */
	public boolean isInitial() {
		return isInitial;
	}

	/**
	 * Method that that returns if a object is a coverability object
	 * 
	 * @return boolean representing the status
	 */
	public boolean iscoverObject() {
		return iscoverObject;
	}

	public void addSourceT(String sourceT) {
		this.sourceT.add(sourceT);
	}

	// ! Stores a reference to the net structure which is useful
	// ! to determine which transitions are active etc.
	private ModelElementContainer netStructure;
	// ! Stores an array of int for the number of tokens in each place
	// ! For effective transition handling this is a map
	// ! ID->num_tokens
	private TreeMap<String, Integer> currentMarking;
	private boolean first = false;
	private boolean centerPlace = false;
	// ! Stores a set of transitions
	private HashSet<String> netTransitions;
	private Map<String, AbstractElementModel> allTransitions = null;
	private boolean reachabilityBuilt = false;
	private boolean isInitial = false;
	private boolean iscoverObject = false;
	private IEditor source = null;
	private TreeSet<String> sourceT = new TreeSet<String>();
}

class Comp_mf implements Comparator {
	public int compare(Object o1, Object o2) {
		String s1 = (String) o1;
		String s2 = (String) o2;
		if(s1.startsWith("p") && s2.startsWith("p")) {
			int i1 = Integer.parseInt(s1.substring(1));
			int i2 = Integer.parseInt(s2.substring(1));
			return i1 - i2;
		}
		else {
			return s1.compareTo(s2);
		}
	}
}
