package org.woped.simulation;

public class ProtocolItem {
	private Simulator sim = null;
	private double time = 0.0;
	private String description = "";
	
	public ProtocolItem(Simulator simulator){
		sim = simulator;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Simulator getSim() {
		return sim;
	}

	public void setSim(Simulator sim) {
		this.sim = sim;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
}
