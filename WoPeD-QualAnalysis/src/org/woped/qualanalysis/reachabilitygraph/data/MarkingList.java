package org.woped.qualanalysis.reachabilitygraph.data;

import java.util.HashMap;
import java.util.Iterator;;


public class MarkingList {
	private HashMap <String,Marking> netMarkings;
	private Iterator it=null;
	public MarkingList(){
		netMarkings = new HashMap<String,Marking>();
		it=netMarkings.values().iterator();
	}
	public Marking addMarking(Marking marking){
		netMarkings.put(marking.getKey(),marking);
		return marking;
	}
	public boolean containsMarking(Marking marking){
		if(netMarkings.containsKey(marking.getKey())){
			return true;
		}
		else{
			return false;
		}
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
	public Marking getMarking(String hash){
		return (Marking) netMarkings.get(hash);
	}
	public int gross(){
		return netMarkings.size();
	}
	public HashMap <String,Marking> getMap(){
		return netMarkings;
	}
}
