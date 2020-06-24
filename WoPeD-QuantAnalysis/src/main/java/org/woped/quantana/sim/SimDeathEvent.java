package org.woped.quantana.sim;

public class SimDeathEvent extends SimulatorEvent {

	SimCase Case;

	public SimDeathEvent(SimRunner sim, double time, SimCase Case) {
		super(sim, time);
		this.Case = Case;
	}

	public void invoke() {								
		sim.finishCase(Case);		
		Case = null;		
	}
	
}
