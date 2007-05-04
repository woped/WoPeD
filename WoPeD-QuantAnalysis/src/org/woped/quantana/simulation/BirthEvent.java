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
		CaseGenerator cg = sim.getCaseGenerator();
		
		Case c = cg.generateNextCase();
		protocol.info(sim.clckS() + "Neuer Case # " + c.getId() + " wurde erzeugt. Ankunft im Prozess: " + c.getSysArrivalTime());
		
		//c.setSysArrivalTime(getTime());
		sim.getCaseList().put(c.getId(), c);
		protocol.info(sim.clckS() + "Neuer Case wurde in Liste aufgenommen.");
		
		sim.updateCaseNumStats(getTime(), sim.getTimeOfLastCaseNumChange());
		sim.setCaseCount(sim.getCaseCount() + 1);
		
		int cc = sim.getCaseCount();
		protocol.info(sim.clckS() + "Aktuelle Anzahl von Cases im System ist " + sim.getCaseCount());
		
		if (cc > sim.getMaxNumCasesInSystem()) sim.setMaxNumCasesInSystem(cc);
		protocol.info(sim.clckS() + "Maximale Anzahl von Cases im Prozess bisher ist " + sim.getMaxNumCasesInSystem());
		
		ArrivalEvent ae = new ArrivalEvent(sim, c.getCurrentArrivalTime(), sim.getStartServer(), c);
		sim.getEventList().add(ae);
		protocol.info(sim.clckS() + "ARRIVAL_EVENT \"" + ae.getName() + "\" für Case # " + c.getId() + " wurde erzeugt.");
		
		sim.setTimeOfLastCaseNumChange(getTime());
		protocol.info(sim.clckS() + "Zeit der letzten Änderung der Anzahl von Cases ist " + getTime());
	}
}
