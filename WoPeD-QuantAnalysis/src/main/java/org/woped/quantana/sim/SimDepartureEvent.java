package org.woped.quantana.sim;

import java.util.ArrayList;

public class SimDepartureEvent extends SimulatorEvent{	
	
	SimServer s;
	SimCase c;
	
	public SimDepartureEvent(SimRunner sim, double time, SimWorkItem wi) {
		super(sim, time);
		this.s = wi.getServer();
		this.c = wi.getCase();		
	}
	
	public void invoke(){
		ArrayList<SimServer> slist = s.getNextServers();		
		if (slist == null){
			SimDeathEvent e = new SimDeathEvent(sim, time, c);
			sim.addEvent(e);			
		} else {
			if (s instanceof SimSplitServer){				
				((SimSplitServer)s).handleSplit(sim, time, c, slist);
			} else 	if (s instanceof SimJoinSplitServer){	
				((SimJoinSplitServer)s).handleSplit(sim, time, c, slist);
			} else {			
				SimWorkItem wi = new SimWorkItem(c, slist.get(0));				
				SimArriveEvent e = new SimArriveEvent(sim, time, wi);
				sim.addEvent(e);						
			}
		}		
	}	
}
