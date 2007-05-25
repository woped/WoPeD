package org.woped.quantana.simulation;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public class BirthEvent extends SimEvent {
	
	private int type = SimEvent.BIRTH_EVENT;
	
	public BirthEvent(Simulator sim, double time){
		super(sim, time);
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		CaseGenerator cg = sim.getCaseGenerator();
		
		Case c = cg.generateNextCase();
		protocol.info(sim.clckS() + "Neuer Case # " + c.getId() + " wurde erzeugt. Ankunft im Prozess: " + String.format("%,.2f", c.getSysArrivalTime()));

		sim.updateCaseNumStats(getTime(), sim.getTimeOfLastCaseNumChange());
		
		sim.getCaseList().put(c.getId(), c);
		protocol.info(sim.clckS() + "Neuer Case wurde in Liste aufgenommen.");
//		sim.setCaseCount(sim.getCaseCount() + 1);
//		
//		int cc = sim.getCaseCount();
//		
//		if (cc > sim.getMaxNumCasesInSystem()) sim.setMaxNumCasesInSystem(cc);
//		protocol.info(sim.clckS() + "Maximale Anzahl von Cases im Prozess bisher ist " + sim.getMaxNumCasesInSystem());
		
		WorkItem wi = new WorkItem(c, sim.getStartServer());
		ArrivalEvent ae = new ArrivalEvent(sim, c.getSysArrivalTime(), wi);
		sim.getEventList().add(ae);
		protocol.info(sim.clckS() + "ARRIVAL_EVENT \"" + ae.getName() + "\" für Case # " + c.getId() + " wurde erzeugt.");
		
		sim.setTimeOfLastCaseNumChange(time);
		protocol.info(sim.clckS() + "Zeit der letzten Änderung der Anzahl von Cases ist " + String.format("%,.2f", getTime()));
	}
}
