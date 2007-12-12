package org.woped.qualanalysis.reachabilitygraph.data;

import java.util.HashMap;
import java.util.Iterator;


public class MarkingList {
	private HashMap <Integer,Marking> netMarkings;
	Iterator it=null;
	public MarkingList(){
		netMarkings = new HashMap<Integer,Marking>();
		it=netMarkings.values().iterator();
	}
	public Marking addMarking(Marking marking){
		if(netMarkings.containsKey(marking.getKey())){
			return netMarkings.get(marking.getKey());
		}
		
		netMarkings.put(marking.getKey(),marking);
		return marking;
		
	}
	public void print(){
		Iterator ma=netMarkings.values().iterator();
		while(ma.hasNext()){
			Marking test=(Marking) ma.next();
			System.out.print(test.print());
		}
		System.out.println("");
	}
	public void getIterator(){
		it= netMarkings.values().iterator();
	}
	public boolean hasNext(){
		return it.hasNext();
	}
	public Marking getMarking(){
		return (Marking) it.next();
	}
	public int gross(){
		return netMarkings.size();
	}
}
