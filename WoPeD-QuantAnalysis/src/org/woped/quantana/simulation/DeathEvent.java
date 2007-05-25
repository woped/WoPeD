package org.woped.quantana.simulation;

@SuppressWarnings("unused")
public class DeathEvent extends SimEvent {
	
	private int type = SimEvent.DEATH_EVENT;
	
	private Case c;
	
	public DeathEvent(Simulator sim, double time, Case c){
		super(sim, time);
		this.c = c;
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		
		c.setSysDepartureTime(time);
		protocol.info(sim.clckS() + "Case # " + c.getId() + " verläßt Prozess zur Zeit: " + String.format("%,.2f", time));
		
		sim.setFinishedCases(sim.getFinishedCases() + 1);
		protocol.info(sim.clckS() + "Anzahl beendeter Cases bisher: " + sim.getFinishedCases());
		
		sim.setThroughPut(sim.getThroughPut() + c.getSysDepartureTime() - c.getSysArrivalTime());
		protocol.info(sim.clckS() + "Durchschnittlicher Durchsatz des Prozesses bisher: " + String.format("%,.2f", (sim.getThroughPut() / sim.getClock())));
		
		sim.setCaseBusy(sim.getCaseBusy() + c.getTimeService());
		protocol.info(sim.clckS() + "Bedienzeit des Prozesses bisher: " + String.format("%,.2f", sim.getCaseBusy()));
		
		sim.setCaseWait(sim.getCaseWait() + c.getTimeWait());
		protocol.info(sim.clckS() + "Wartezeit des Prozesses bisher: " + String.format("%,.2f", sim.getCaseWait()));
		
		sim.updateCaseNumStats(getTime(), sim.getTimeOfLastCaseNumChange());
		
		sim.getCaseList().remove(c.getId());
		protocol.info(sim.clckS() + "Case # " + c.getId() + " wurde aus Liste entfernt.");
		
		sim.setTimeOfLastCaseNumChange(getTime());
		protocol.info(sim.clckS() + "Zeit der letzten Änderung der Anzahl von Cases ist " + String.format("%,.2f", time));
	}
}
