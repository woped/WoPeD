package org.woped.qualanalysis.reachabilitygraph.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;

/**
 * Datastructure for storing all possible Transactions with their start and end
 * marking object and the transition that was activated leading from start to
 * end aswell as the name displayed in the graphical represantation
 */
public class ReachabilityDataSet {

	// Hash Map that Contains all Transactions
	private HashMap<String, TransitionObject> netTransitions;
	/**
	 * Method to initially create a ReachabilityDataSet
	 */
	public ReachabilityDataSet() {
		netTransitions = new HashMap<String, TransitionObject>();
	}

/**
 * Method to add a transaction to the dataset
 * @param start Marking object where a transaction starts from
 * @param transition id leading to the ende state
 * @param end state where the transitiona activation leads to
 * @param transition_name the actual name of the transition
 * @param subpr value if the transition represents a subprocess
 */
	public void add(Marking start, String transition, Marking ende, String transition_name, boolean subpr) {
		TransitionObject trans = new TransitionObject(start, transition, ende, transition_name, subpr);
		netTransitions.put(trans.getKey(), trans);
	}
	/**
	 * Method for printing all transactions to the console (debuging)
	 */
	public void print() {
		Iterator it = netTransitions.values().iterator();
		String output = "";
		output += "Transitionen: ";
		while (it.hasNext()) {
			TransitionObject current = (TransitionObject) it.next();
			output += "{" + current.print() + "}";
		}
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- print() " + output + " " + this.getClass().getName());
	}
	/**
	 * Method to get the transaction set
	 * @return HashMap<String, TransitionObject>
	 */
	public HashMap<String, TransitionObject> getMap() {
		return netTransitions;
	}
	/**
	 * Method to remove obsolete objects dueto the center places
	 * @param marking list created
	 */
	public void rebuild(MarkingList markings) {
		HashMap<String, Marking> mlist = markings.getMap();
		Iterator mit = mlist.keySet().iterator();
		LinkedList<String> MarkingsToRemove = new LinkedList();
		while (mit.hasNext()) {
			String aktuellID = (String) mit.next();
			if(mlist.get(aktuellID).centerPlace()) {
				// Remove the Marking with a Token in the Center Place
				// Get the Reachabilityset where the Marking is the end marking
				LinkedList <String> targ=new LinkedList<String>();
				LinkedList <String> sourc=new LinkedList<String>();
				Iterator rsit = netTransitions.keySet().iterator();
				boolean treffer = false;
				String sourceid = "";
				while (rsit.hasNext() && !treffer) {
					sourceid = (String) rsit.next();
					if(netTransitions.get(sourceid).ende.getKey().equals(mlist.get(aktuellID).getKey())) {
						targ.add(sourceid);
					}
				}
				System.out.println(targ);
				// Get the Reachabilityset where the Current Marking is the
				// Start Marking
				// and set the StartMarking to the previously found
				rsit = netTransitions.keySet().iterator();
				while (rsit.hasNext()) {
					String targid = (String) rsit.next();
					if(netTransitions.get(targid).start.getKey().equals(mlist.get(aktuellID).getKey())) {
						sourc.add(targid);
					}
				}
				// Remember the Markings to be removed
				MarkingsToRemove.add(aktuellID);
				//Create new Transitions
				for(int i=0;i<sourc.size();i++){
					for(int u=0;u<targ.size();u++){
						TransitionObject tmp=new TransitionObject(netTransitions.get(targ.get(i)).start,
								netTransitions.get(sourc.get(u)).transition,
								netTransitions.get(sourc.get(u)).ende,
								netTransitions.get(sourc.get(u)).transition_name,
								false);
						netTransitions.put(tmp.getKey(),tmp);
					}
				}
				//remove old transitions
				
				for(int i=0;i<sourc.size();i++){
					 netTransitions.remove(sourc.get(i));
				}
				for(int u=0;u<targ.size();u++){
					netTransitions.remove(targ.get(u));
				}
				
			}
			//remove transactions which are not in marking list
			Iterator transIt=netTransitions.keySet().iterator();
			while(transIt.hasNext()){
				String curr=(String) transIt.next();
				if(!markings.containsMarking(netTransitions.get(curr).start)||!markings.containsMarking(netTransitions.get(curr).ende)){
					transIt.remove();
					netTransitions.remove(curr);
					transIt=netTransitions.keySet().iterator();
				}
			}
			//Remove Markings which are not initial but have no incoming arc
			
				Marking tmpmark=mlist.get(aktuellID);
				if(!tmpmark.isInitial()){
					boolean obs=true;
					Iterator transit=netTransitions.keySet().iterator();
					while(transit.hasNext()){
						if(netTransitions.get(transit.next()).ende.getKey().equals(tmpmark.getKey()))
						{
							obs=false;
						}
					}
					if(obs){
						transit=netTransitions.keySet().iterator();
						while(transit.hasNext())
						{
							String key;
							if(netTransitions.get(key=(String)transit.next()).start.getKey().equals(tmpmark.getKey())){
								transit.remove();
								netTransitions.remove(key);
								transit=netTransitions.keySet().iterator();
							}
						}
					}
				}
			
			//Remove Same Transitions, refresh keys
			transIt=netTransitions.keySet().iterator();
			while(transIt.hasNext()){
				String key=(String)transIt.next();
				if(!key.equals(netTransitions.get(key).getKey())){
					TransitionObject tmp=netTransitions.get(key);
					transIt.remove();
					netTransitions.remove(key);
					netTransitions.put(tmp.getKey(), tmp);
					transIt=netTransitions.keySet().iterator();
				}
			}
			
			
			
		}

		for(int i = 0; i < MarkingsToRemove.size(); i++) {
			mlist.remove(MarkingsToRemove.get(i));
		}

	}
}

// ! Object that Contains a Transaction with its start and end status (Marking
// object)

/**
 * Class that Contains a Transaction with its start and end status (Marking object)
 * aswell as the transition name and subprocess information
 */
class TransitionObject {
	public Marking start = null;
	public String transition = null;
	public Marking ende = null;
	public String transition_name = null;
	public boolean subpr = false;
/**
 * 
 * @param start Marking object where a transaction starts from
 * @param transition id leading to the ende state
 * @param end state where the transitiona activation leads to
 * @param transition_name the actual name of the transition
 * @param subpr value if the transition represents a subprocess
 */
	public TransitionObject(Marking start, String transition, Marking ende, String transition_name, boolean subpr) {
		this.start = start;
		this.transition = transition;
		this.subpr = subpr;

		this.ende = ende;

		// Change the Name of the Transition for Graphical Display
		if(!this.transition.equals(transition_name)) {
			this.transition_name = transition_name + " (" + transition + ")";
		}
		else {
			this.transition_name = transition_name;
		}
	}
/**
 * method that returns a string represantation of the current transaction object
 * @return string represantation of the current transaction object
 */
	public String print() {
		return start.print() + " " + transition + " " + ende.print();
	}
/**
 * method that returns a unique key
 * @return String containing the hash
 */
	// Generate a unique Hash Value
	public String getKey() {
		return "" + start.getKey() + transition.hashCode() + ende.getKey();
	}
/**
 * Method that returns the current transition name
 * @return String containing the name
 */
	public String toString() {
		return transition_name;
	}

}