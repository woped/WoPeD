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
		
		/*
//		c.setSysDepartureTime(time);
//		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Death.Case.Exit") + String.format("%,.2f", time), c.getId());
		
		sim.setFinishedCases(sim.getFinishedCases() + 1);
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Death.Cases.Finished") + sim.getFinishedCases());
		
//		sim.setThroughPut(sim.getThroughPut() + c.getSysDepartureTime() - c.getSysArrivalTime());
//		protocol.info(sim.clckS() + ENTRY.getString("Sim.Death.Throughput") + String.format("%,.2f", (sim.getThroughPut() / sim.getClock())));
		
		sim.setCaseBusy(sim.getCaseBusy() + c.getTimeService());
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Death.Time.Service") + String.format("%,.2f", sim.getCaseBusy()));
		
		sim.setCaseWait(sim.getCaseWait() + c.getTimeWait());
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Death.Time.Wait") + String.format("%,.2f", sim.getCaseWait()));
		
		sim.updateCaseNumStats(getTime(), sim.getTimeOfLastCaseNumChange());
		
		sim.getCaseList().remove(c.getId());
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Death.Case.Deleted"), c.getId());
		
		sim.setTimeOfLastCaseNumChange(getTime());
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Time.LastEvent.CaseNum") + String.format("%,.2f", time));
		*/
		
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Death.Case.Exit") + String.format("%,.2f", time), c.getId());
		
		sim.incAvgProcessWaitTime(c.getTimeWait());
		sim.incAvgProcessServiceTime(c.getTimeService());
		sim.incAvgProcessCompletionTime(time - c.getSysArrivalTime());
		
		sim.incFinishedCases();
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Number.Cases.Finished") + sim.getFinishedCases());
//		sim.getWd().getTxtArea().append("DE: (Case# " + c.getId() + "): " + time + "\n");
	}
}
