package org.woped.quantana.simulation;

public class SuccServer {
	private Server server;
	private double probability = 1.0;
	
	public SuccServer(Server s){
		this.server = s;
	}
	
	public SuccServer(Server s, double prob){
		this.server = s;
		probability = prob;
	}
	
	public double getProbability() {
		return probability;
	}
	
	public void setProbability(double probability) {
		this.probability = probability;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server s) {
		this.server = s;
	}
}
