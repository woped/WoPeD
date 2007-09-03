package org.woped.quantana.simulation;

import java.util.ResourceBundle;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class DeathEvent extends SimEvent {
	
	private int type = SimEvent.DEATH_EVENT;
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
	private Case c;
	
	public DeathEvent(Simulator sim, double time, Case c){
		super(sim, time);
		this.c = c;
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Death.Case.Exit") + String.format("%,.2f", time), c.getId());
		
		sim.incAvgProcessWaitTime(c.getTimeWait());
		sim.incAvgProcessServiceTime(c.getTimeService());
		sim.incAvgProcessCompletionTime(time - c.getSysArrivalTime());
		
		sim.incFinishedCases();
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Number.Cases.Finished") + sim.getFinishedCases());
	}
}
