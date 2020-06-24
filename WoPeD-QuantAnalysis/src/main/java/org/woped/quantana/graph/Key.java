package org.woped.quantana.graph;

public class Key {
	String id;
	double runs;
	
	public Key(String s, double d){
		id = s;
		runs = d;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getRuns() {
		return runs;
	}

	public void setRuns(double runs) {
		this.runs = runs;
	}
}
