package org.woped.woflan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.woped.core.analysis.StructuralAnalysis;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;

//! Specifies a particular marking of a reachability / coverability graph
//! along with the corresponding model
public class Marking {

	//! Construct a marking from the specified ModelElementContainer
	//! @param source specifies the element container that should serve
	//!        as a source. The container will be kept as a reference
	//!        for preserving the structure only. The initial marking
	//!        will be copied and stored locally.
	public Marking(IEditor source)
	{
		StructuralAnalysis sa = new StructuralAnalysis(source);
		currentMarking = new HashMap<String,Integer>();
		// Store the marking of each place
		Iterator places = sa.getPlacesIterator();
		while (places.hasNext())
		{
			PlaceModel currentPlace = (PlaceModel)places.next();
			currentMarking.put(currentPlace.getId(), currentPlace.getMarking());
		}		
		// Store a reference to each transition		
		netTransitions = new HashSet<TransitionModel>();
		Iterator transitions = sa.getTransitionsIterator();
		while (transitions.hasNext())
		{
			TransitionModel currentTransition = (TransitionModel)transitions.next();
			netTransitions.add(currentTransition);
		}		
		
		netStructure = source.getModelProcessor().getElementContainer();
	}
	
	//! Return the active transitions of this marking.
	//! An active transition can be used to 
	//! create a follow-up marking
	//! @return Returns an iterator iterating through all active
	//! transitions under the current marking
	public Iterator<ReachabilityArc> getActiveTransitions()
	{
		return null;
	}

	//! Compares the current marking to otherMarking.
	//! This method is required to build the coverability graph.
	//! @param otherMarking specifies the marking to which
	//!        the current marking should be compared
	//! @return true if the current marking is bigger than otherMarking.
	//!         Returns false if otherMarking is smaller or equal
	//!         and if the two markings are not comparable
	boolean isGreaterThan(Marking otherMarking)
	{
		// Both markings must have the same size for this method to return true
		if (otherMarking.currentMarking.size()!=currentMarking.size())
			return false;
		boolean isGreater = true;
		Iterator currentThisMarking = currentMarking.values().iterator();
		Iterator currentOtherMarking = otherMarking.currentMarking.values().iterator();
		while (isGreater&&currentThisMarking.hasNext())
		{
			isGreater = 
				(((Integer)currentThisMarking.next()).compareTo((Integer)currentOtherMarking.next())>0);
		}
		return isGreater;
	}
		
	//! Stores a reference to the net structure which is useful
	//! to determine which transitions are active etc.
	private ModelElementContainer netStructure;
	//! Stores an array of int for the number of tokens in each place
	//! For effective transition handling this is a map
	//! ID->num_tokens
	private HashMap<String,Integer> currentMarking;
	//! Stores a set of transitions
	private HashSet<TransitionModel> netTransitions;
}
