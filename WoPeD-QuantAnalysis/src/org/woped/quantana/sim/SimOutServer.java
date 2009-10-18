package org.woped.quantana.sim;

public class SimOutServer{
	SimServer server;
	double probability = 1.0;
	
	public SimOutServer(SimServer s, double prob){
		server = s;
		probability = prob;
	}
	
	public SimServer getserver(){
		return server;
	}
	
	public double getprobability(){
		return probability;
	}

}
