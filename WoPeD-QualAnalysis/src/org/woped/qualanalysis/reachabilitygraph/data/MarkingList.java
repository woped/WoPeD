package org.woped.qualanalysis.reachabilitygraph.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

;

/**
 * List that contains all reachable markings which are created by the call
 * BuildReachability, each marking in this list is unique
 */
public class MarkingList {
	private HashMap<String, Marking> netMarkings;
	private Iterator it = null;
	//Store Reference for Coverability
	ReachabilityDataSet transactions=null;

	/**
	 * Method for creating a new empty MarkingList
	 */
	public MarkingList(ReachabilityDataSet transactions) {
		this.transactions=transactions;
		netMarkings = new HashMap<String, Marking>();
		it = netMarkings.values().iterator();
	}

	/**
	 * Method for adding a marking to the MarkingList
	 * 
	 * @param marking
	 *            to be added
	 * @return marking that has been added, important since another marking may
	 *         be returned if the marking to be added is found in the list
	 */
	public Marking addMarking(Marking marking) {
		Marking help;
		if(this.containsMarking(marking)) {
			return netMarkings.get(marking.getKey());
		}
		else if((help = MarkingGreater(marking)) != null) {
			//If object is covered remove all Transaction starting from it
			removefromTransactions(help,false);
			help.setreachabilitynotbuilt();
			help.setCoverability(marking);
			netMarkings.put(help.getKey(), help);
			return help;
		}

		netMarkings.put(marking.getKey(), marking);
		return marking;
	}

	/**
	 * Method for checking if a marking is already in the current list
	 * 
	 * @param marking
	 *            to be checked for
	 * @return true if marking is already in list, false if marking is not in
	 *         the list
	 */
	public boolean containsMarking(Marking marking) {
		if(netMarkings.containsKey(marking.getKey())) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Method to check if a marking that is going to be added covers another
	 * marking
	 * 
	 * @param marking
	 * @return null if the element does not cover another one, the covered
	 *         marking if the element covers another marking
	 */
	public Marking MarkingGreater(Marking marking) {
		Iterator marks = netMarkings.values().iterator();
		marking.setfirst();
		Marking current = null;
		while (marks.hasNext()) {
			if(marking.isGreaterThan(current = (Marking) marks.next())) {
				return current;
			}

		}
		marks = null;
		return null;
	}

	/**
	 * Method for printing the markinglist to the console, usefull for debuging
	 */
	public void print() {
		Iterator ma = netMarkings.values().iterator();
		while (ma.hasNext()) {
			Marking test = (Marking) ma.next();
			System.out.print(test.print());
		}
		System.out.println("");
	}

	/**
	 * Method that returns a value Iterator of the marking object in the
	 * MarkingList
	 */
	public void getIterator() {
		it = netMarkings.values().iterator();
	}

	/**
	 * check if the by getIterator() delivered Iterator has a next Element
	 * 
	 * @return true if it has more Elements, false if not
	 */
	public boolean hasNext() {
		return it.hasNext();
	}

	/**
	 * get the current Marking object on the iterator position
	 * 
	 * @return the next marking object
	 */
	public Marking getMarking() {
		return (Marking) it.next();
	}

	/**
	 * returns a specific marking where the key is know
	 * 
	 * @param Key
	 *            of the marking that is needed from the MarkingList
	 * @return the Marking object with the given key
	 */
	public Marking getMarking(String hash) {
		return (Marking) netMarkings.get(hash);
	}

	/**
	 * method that returns the size of the MarkingList
	 * 
	 * @return integer representing the size
	 */
	public int gross() {
		return netMarkings.size();
	}
/**
 * Method for removing outgoing transactions of a marking
 * @param marking from which transactions should be removed
 */
	public void removefromTransactions(Marking mark, boolean innercall){
		HashMap<String, TransitionObject> help=transactions.getMap();
		Iterator transIt=help.keySet().iterator();
		LinkedList<String> markstoremove=new LinkedList<String>();
		while(transIt.hasNext()){
			String gel;
			if((gel=(String)transIt.next()).startsWith(mark.getKey())){
				
				if(!help.get(gel).ende.iscoverObject()||innercall){
				markstoremove.add(help.get(gel).ende.getKey());
				transIt.remove();
				help.remove(gel);
				transIt=help.keySet().iterator();
				}
			}
		}
		for(int i=0;i<markstoremove.size();i++){
			//recurively remove transitions which are obsolete
			removefromTransactions(netMarkings.get(markstoremove.get(i)),true);
			netMarkings.remove(markstoremove.get(i));
		}
	}
	/**
	 * returns the MarkingList so it can be accessed from the outside (Graphic
	 * Representation)
	 * 
	 * @return HashMap<String, Marking> containing all markings created by the
	 *         class Build Reachability
	 */
	public HashMap<String, Marking> getMap() {
		return netMarkings;
	}
}
