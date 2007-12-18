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
	private HashMap <String,TransitionObject> netTransitions;
	public ReachabilityDataSet(){
		netTransitions=new HashMap<String,TransitionObject>();
	}
	public void add(Marking start ,String transition, Marking ende){
		TransitionObject trans=new TransitionObject(start,transition,ende);
		netTransitions.put(trans.getKey(),trans);
	}

	public void print(){
		Iterator it=netTransitions.values().iterator();
		System.out.print("Transitionen: ");
		while(it.hasNext()){
			TransitionObject current=(TransitionObject) it.next();
			System.out.print("{"+current.print()+"}");
		}
		System.out.println("");
	}
	public HashMap<String,TransitionObject> getMap(){
		return netTransitions;
	}
}


//! Object that Contains a Transaction with its start and end status (Marking object)
class TransitionObject{
	public Marking start=null;
	public String transition=null;
	public Marking ende=null;
	public TransitionObject(Marking start,String transition,Marking ende){
		this.start=start;
		this.transition=transition;
		this.ende=ende;
		System.out.println(this.print()+" "+this.getKey());
	}
	public String print(){
		return start.print() + " "+transition+" "+ende.print();
	}
	public String getKey(){
		return ""+start.getKey()+transition.hashCode()+ende.getKey();
	}
	public String toString(){
		return this.print();
	}

}