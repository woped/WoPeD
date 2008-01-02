package org.woped.qualanalysis.reachabilitygraph.data;

import java.util.HashMap;
import java.util.Iterator;

//! Datastructure for storing all possible Transactions with
//! their start and end marking object
//! and the transition that was activated leading from start to end

import org.woped.core.model.petrinet.TransitionModel;

//! Datastructure for storing all possible Transactions with
//! their start and end marking object
//! and the transition that was activated leading from start to end
public class ReachabilityDataSet {
	
	// Hash Map that Contains all Transactions
	private HashMap<String, TransitionObject> netTransitions;

	public ReachabilityDataSet() {
		netTransitions = new HashMap<String, TransitionObject>();
	}
	
	//adding a new Transaction to the Data Structure
	public void add(Marking start, String transition, Marking ende,
			String transition_name) {
		TransitionObject trans = new TransitionObject(start, transition, ende,
				transition_name);
		netTransitions.put(trans.getKey(), trans);
	}

	// Printing all Transactions
	public void print() {
		Iterator it = netTransitions.values().iterator();
		System.out.print("Transitionen: ");
		while (it.hasNext()) {
			TransitionObject current = (TransitionObject) it.next();
			System.out.print("{" + current.print() + "}");
		}
		System.out.println("");
	}

	public HashMap<String, TransitionObject> getMap() {
		return netTransitions;
	}
}

// ! Object that Contains a Transaction with its start and end status (Marking
// object)
class TransitionObject {
	public Marking start = null;
	public String transition = null;
	public Marking ende = null;
	public String transition_name = null;

	public TransitionObject(Marking start, String transition, Marking ende,
			String transition_name) {
		this.start = start;
		this.transition = transition;
		this.ende = ende;
		this.transition_name = transition_name + " (" + transition + ")";
	}

	public String print() {
		return start.print() + " " + transition + " " + ende.print();
	}
	// Generate a unique Hash Value
	public String getKey() {
		return "" + start.getKey() + transition.hashCode() + ende.getKey();
	}

	public String toString() {
		return transition_name;
	}

}