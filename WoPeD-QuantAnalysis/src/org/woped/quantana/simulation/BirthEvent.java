package org.woped.quantana.simulation;

@SuppressWarnings("unused")
public class BirthEvent extends SimEvent {
	
	private int type = SimEvent.BIRTH_EVENT;
	
	public BirthEvent(Simulator sim, double time){
		super(sim, time);
	}
	
	public void invoke(){
		Simulator sim = getSim();
		CaseGenerator cg = sim.getCaseGenerator();
		Case c = cg.generateNextCase();
		
		c.setSysArrivalTime(getTime());
		sim.getCaseList().put(c.getId(), c);
		sim.updateCaseNumStats(getTime(), sim.getTimeOfLastCaseNumChange());
		sim.setCaseCount(sim.getCaseCount() + 1);
		ArrivalEvent ae = new ArrivalEvent(sim, sim.getClock(), sim.getStartServer(), c);
		sim.getEventList().add(ae);
		
//		s.setTimeOfLastEvent(getTime());
	}
}
