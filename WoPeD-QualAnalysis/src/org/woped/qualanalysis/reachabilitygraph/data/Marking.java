package org.woped.qualanalysis.reachabilitygraph.data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.woped.core.analysis.StructuralAnalysis;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;

//! Specifies a particular marking of a reachability / coverability graph
//! along with the corresponding model
public class Marking {

	// ! Construct a marking from the specified ModelElementContainer
	// ! @param source specifies the element container that should serve
	// ! as a source. The container will be kept as a reference
	// ! for preserving the structure only. The initial marking
	// ! will be copied and stored locally.
	public Marking(IEditor source) {
		StructuralAnalysis sa = new StructuralAnalysis(source);
		currentMarking = new TreeMap<String, Integer>();
		// Store the marking of each place
		Iterator places = sa.getPlacesIterator();
		while (places.hasNext()) {
			PlaceModel currentPlace = (PlaceModel) places.next();
			if (currentPlace.getId().contains("CENTER_PLACE_")&&currentPlace.getVirtualTokenCount()>0) {
				centerPlace = true;
			}
			currentMarking.put(currentPlace.getId(), currentPlace
					.getVirtualTokenCount());
		}
		// Store the active transitions
		netTransitions = new HashSet<String>();
		allTransitions = source.getModelProcessor().getElementContainer()
				.getElementsByType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
		allTransitions.putAll(source.getModelProcessor().getElementContainer()
				.getElementsByType(PetriNetModelElement.TRANS_OPERATOR_TYPE));
		allTransitions.putAll(source.getModelProcessor().getElementContainer()
				.getElementsByType(PetriNetModelElement.SUBP_TYPE));
		Iterator transitions = allTransitions.values().iterator();
		while (transitions.hasNext()) {
			TransitionModel currentTransition = (TransitionModel) transitions
					.next();
			if (currentTransition.isActivated()) {
				netTransitions.add(currentTransition.getId());
			}
		}

		netStructure = source.getModelProcessor().getElementContainer();
	}
	// ! Return the active transitions of this marking.
	// ! An active transition can be used to
	// ! create a follow-up marking
	// ! @return Returns an iterator iterating through all active
	// ! transitions under the current marking
	public Iterator<ReachabilityArc> getActiveTransitions() {
		return null;
	}

	// ! Compares the current marking to otherMarking.
	// ! This method is required to build the coverability graph.
	// ! @param otherMarking specifies the marking to which
	// ! the current marking should be compared
	// ! @return true if the current marking is bigger than otherMarking.
	// ! Returns false if otherMarking is smaller or equal
	// ! and if the two markings are not comparable
	boolean isGreaterThan(Marking otherMarking) {
		// Both markings must have the same size for this method to return true
		if (otherMarking.currentMarking.size() != currentMarking.size())
			return false;
		boolean isGreater = true;
		Iterator currentThisMarking = currentMarking.values().iterator();
		Iterator currentOtherMarking = otherMarking.currentMarking.values()
				.iterator();
		Integer oh;
		while (isGreater && currentThisMarking.hasNext()) {
			int currint= (Integer) currentThisMarking.next();
			int otherint=(Integer) currentOtherMarking.next();
			if(currint>=otherint&&otherint!=0){
				isGreater=true;
			}
			else if(currint==otherint){
				isGreater=true;
			}
			else 
				isGreater=false;
		}
		if (isGreater && otherMarking.isfirst()) {
			this.setnotfirst();
			return false;
		} else {
			return isGreater;
		}

	}

	// Method for setting the Coverability Object
	public void setCoverability(Marking otherMarking) {
		Iterator currentThisMarking = currentMarking.keySet().iterator();
		Iterator currentOtherMarking = otherMarking.currentMarking.values()
				.iterator();
		String wert = null;
		while (currentThisMarking.hasNext()) {
			if ((Integer) currentMarking.get(
					(wert = (String) currentThisMarking.next())).compareTo(
					(Integer) currentOtherMarking.next()) < 0
					&& currentMarking.get(wert) < 60000) {
				currentMarking.put(wert, 63000);
				BuildReachability.reachBuilt = true;
				this.setreachabilitynotbuilt();
			}
		}
	}

	// Prints the Current Objects Tokenstatus (for Console)
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
	//Return a String of the Marking, for graphic
	public String toString() {
		String value = "( ";
		Iterator it = currentMarking.keySet().iterator();
		while (it.hasNext()) {
			String gel = (String) it.next();
			if (!gel.contains("CENTER_PLACE_")) {
				if (currentMarking.get(gel) >= 60000) {
					value = value + "w" + " ";
				} else {
					value = value + currentMarking.get(gel).toString() + " ";
				}
			}
		}
		value = value + ")";
		return value;
	}
	//Prints the Placekeyset
	public void printseq() {
		Iterator it = currentMarking.keySet().iterator();
		while (it.hasNext()) {
			System.out.print(it.next() + " ");
		}
		System.out.println("");
	}

	public HashSet<String> getTransitions() {
		return netTransitions;
	}

	public boolean equals(Object o) {
		Marking compare = (Marking) o;
		return this.currentMarking.equals(compare.currentMarking);
	}

	public TreeMap<String, Integer> getMarking() {
		return currentMarking;
	}

	public boolean reachabilitybuilt() {
		return reachabilityBuilt;
	}

	public void setreachabilitybuilt() {
		reachabilityBuilt = true;
	}

	public void setreachabilitynotbuilt() {
		reachabilityBuilt = false;
	}

	public String getKey() {
		Iterator it = currentMarking.keySet().iterator();
		String hash = "";
		while (it.hasNext()) {
			String temp = (String) it.next();
			hash = hash + temp + "_" + currentMarking.get(temp).intValue();
		}
		return (hash);
	}

	// Help value for Coverability
	public void setfirst() {
		first = true;
	}

	public void setnotfirst() {
		first = false;
	}

	public boolean isfirst() {
		return first;
	}
	public boolean centerPlace(){
		return centerPlace;
	}
	public void setInitial(){
		isInitial=true;
	}
	public boolean isInitial(){
		return isInitial;
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
	private boolean isInitial=false;
}
