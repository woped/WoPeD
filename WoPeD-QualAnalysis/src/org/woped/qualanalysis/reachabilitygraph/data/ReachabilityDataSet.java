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
	private HashMap <Integer ,TransitionObject> netTransitions;
	public ReachabilityDataSet(){
		netTransitions=new HashMap<Integer,TransitionObject>();
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
	public HashMap<Integer,TransitionObject> getMap(){
		return netTransitions;
	}
}


//! Object that Contains a Transaction with its start and end status (Marking object)
class TransitionObject{
	Marking start=null;
	String transition=null;
	Marking ende=null;
	public TransitionObject(Marking start,String transition,Marking ende){
		this.start=start;
		this.transition=transition;
		this.ende=ende;
	}
	public String print(){
		return start.print() + " "+transition+" "+ende.print();
	}
	public String toString(){
		return this.print();
	}
	public Integer getKey(){
		return start.getKey()+transition.hashCode()+ende.getKey();
	}
}