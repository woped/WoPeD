package org.woped.quantana.sim;


public class SimArc {
	SimNode source = null;
	SimNode target = null;
	double propability = 1;
	
	public SimArc(SimNode source, SimNode target, double probability){
		this.source = source;
		this.target = target;
		this.propability = probability;		
	}
	
	public SimArc(SimNode source, SimNode target){
		this.source = source;
		this.target = target;			
	}
	
	public SimNode getTarget(){
		return target;
	}
	
	public double getProbability(){
		return propability;
	}

}
