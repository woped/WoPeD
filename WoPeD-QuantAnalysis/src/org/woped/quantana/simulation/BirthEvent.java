package org.woped.quantana.simulation;

import java.util.ResourceBundle;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class BirthEvent extends SimEvent {
	
	private int type = SimEvent.BIRTH_EVENT;
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
	public BirthEvent(Simulator sim, double time){
		super(sim, time);
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		CaseGenerator cg = sim.getCaseGenerator();
		
		if (sim.getCntArrivalEvents() < Simulator.LIMIT_EVENT_ARRIVAL){
			Case c = cg.generateNextCase();
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Birth.Info") + String.format("%,.2f", c.getSysArrivalTime()), c.getId());
			
			Server s = sim.getStartServer();
			WorkItem wi = new WorkItem(c, s);
			ArrivalEvent ae = new ArrivalEvent(sim, c.getSysArrivalTime(), wi);
			sim.enroleEvent(ae);
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Event.Arrival") + ae.getName() + ENTRY.getString("Sim.Generated.Event"), c.getId());
		}
	}
}
