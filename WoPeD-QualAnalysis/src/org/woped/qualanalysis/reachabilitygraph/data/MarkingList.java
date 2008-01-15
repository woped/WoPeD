package org.woped.qualanalysis.reachabilitygraph.data;

import java.util.HashMap;
import java.util.Iterator;;

//! List that contains all Markings used in the Reachability Graph
public class MarkingList {
	private HashMap <String,Marking> netMarkings;
	private Iterator it=null;
	public MarkingList(){
		netMarkings = new HashMap<String,Marking>();
		it=netMarkings.values().iterator();
	}
	public Marking addMarking(Marking marking){
		Marking help;
		if(this.containsMarking(marking)){
			return netMarkings.get(marking.getKey());
		}
		else if((help=MarkingGreater(marking))!=null){
			help.setCoverability(marking);
			return help;
		}

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
	public Marking MarkingGreater(Marking marking){
		Iterator marks=netMarkings.values().iterator();
		marking.setfirst();
		Marking current=null;
		while(marks.hasNext()){
			if(marking.isGreaterThan(current=(Marking)marks.next()))
			 {
				return current;
			 }
			
		}
		marks=null;
		return null;
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
