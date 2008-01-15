package org.woped.qualanalysis.reachabilitygraph.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

//! Datastructure for storing all possible Transactions with
//! their start and end marking object
//! and the transition that was activated leading from start to end

import org.woped.core.model.petrinet.TransitionModel;

//! Datastructure for storing all possible Transactions with
//! their start and end marking object
//! and the transition that was activated leading from start to end
//! aswell as the name displayed in the graphical represantation
public class ReachabilityDataSet {

	// Hash Map that Contains all Transactions
	private HashMap<String, TransitionObject> netTransitions;

	public ReachabilityDataSet() {
		netTransitions = new HashMap<String, TransitionObject>();
	}

	// adding a new Transaction to the Data Structure
	public void add(Marking start, String transition, Marking ende,
			String transition_name, boolean subpr) {
		TransitionObject trans = new TransitionObject(start, transition, ende,
				transition_name, subpr);
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
	// method for rebuilding the Reachability
	// remove obsolete objects
	public void rebuild(MarkingList markings) {
		HashMap<String, Marking> mlist = markings.getMap();
		Iterator mit = mlist.keySet().iterator();
		LinkedList<String> MarkingsToRemove = new LinkedList();
		while (mit.hasNext()) {
			String aktuellID = (String) mit.next();
			if (mlist.get(aktuellID).centerPlace()) {
				// Remove the Marking with a Token in the Center Place

				// Get the Reachabilityset where the Marking is the end marking
				Iterator rsit = netTransitions.keySet().iterator();
				boolean treffer = false;
				String sourceid = "";
				while (rsit.hasNext() && !treffer) {
					sourceid = (String) rsit.next();
					if (netTransitions.get(sourceid).ende.getKey().equals(
							mlist.get(aktuellID).getKey())) {
						treffer = true;
					}
				}
				// Get the Reachabilityset where the Current Marking is the
				// Start Marking
				// and set the StartMarking to the previously found
				rsit = netTransitions.keySet().iterator();
				while (rsit.hasNext()) {
					String targid = (String) rsit.next();
					if (netTransitions.get(targid).start.getKey().equals(
							mlist.get(aktuellID).getKey())) {
						netTransitions.get(targid).start = mlist
								.get(netTransitions.get(sourceid).start
										.getKey());
					}
				}
				// Remember the Markings to be removed
				MarkingsToRemove.add(aktuellID);
				netTransitions.remove(sourceid);

			}
		}
		for (int i = 0; i < MarkingsToRemove.size(); i++) {
			mlist.remove(MarkingsToRemove.get(i));
		}

	}
}

// ! Object that Contains a Transaction with its start and end status (Marking
// object)
class TransitionObject {
	public Marking start = null;
	public String transition = null;
	public Marking ende = null;
	public String transition_name = null;
	public boolean subpr = false;

	public TransitionObject(Marking start, String transition, Marking ende,
			String transition_name, boolean subpr) {
		this.start = start;
		this.transition = transition;
		this.subpr = subpr;

		this.ende = ende;
		
		//Change the Name of the Transition for Graphical Display
		if (!this.transition.equals(transition_name)) {
			this.transition_name = transition_name + " (" + transition + ")";
		} else {
			this.transition_name = transition_name;
		}
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