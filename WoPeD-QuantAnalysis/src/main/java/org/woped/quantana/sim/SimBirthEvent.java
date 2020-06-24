package org.woped.quantana.sim;

public class SimBirthEvent extends SimulatorEvent{
	
	public SimBirthEvent(SimRunner sim,double time){
		super(sim, time);
	}
	
	public void invoke(){		
		SimRunner sim = getSim();
		SimCaseMaker cg = sim.getCaseMaker();
		if (sim.getCntArrival() < SimRunner.MAX_ARRIVAL){			
			SimCase c = cg.getCase();
			SimWorkItem wi = new SimWorkItem(c, sim.getFirstServer());
			SimArriveEvent e = new SimArriveEvent(sim, c.getsysArrivalTime(), wi);				
			sim.addEvent(e);			
		}				
	}
		
}
