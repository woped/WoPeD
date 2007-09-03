package org.woped.quantana.simulation;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class DepartureEvent extends SimEvent {
	
	private int type = SimEvent.DEPARTURE_EVENT;
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
	private WorkItem wi;
	private Server s;
	private Case c;
	
	public DepartureEvent(Simulator sim, double time, WorkItem wi) {
		super(sim, time);
		this.wi = wi;
		this.s = wi.getServer();
		this.c = wi.getCase();
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Departure.Info"), new Object[] {c.getId(), s.getName(), s.getId()});
		
		ArrayList<Server> slist = s.gotoNextServer();
		
		if (slist == null){
			DeathEvent de = new DeathEvent(sim, time, c);
			sim.enroleEvent(de);
			protocol.info(sim.clckS() + ENTRY.getString("Sim.Event.Death") + de.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId() + ENTRY.getString("Sim.Generated"));
		} else {
			if (s instanceof ANDSplitServer){
				ANDSplitServer as = (ANDSplitServer)s;
				as.handleSplit(sim, time, c, slist);
			} else {
				Server s_ = slist.get(0);
				WorkItem wi_ = new WorkItem(c, s_);
				ArrivalEvent ae = new ArrivalEvent(sim, time, wi_);
				sim.enroleEvent(ae);
				protocol.info(sim.clckS() + ENTRY.getString("Sim.Event.Arrival") + ae.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId() + ENTRY.getString("Sim.Generated"));
			}
		}
		
		int qd = sim.getQueueDiscipline();
		
		if (qd == Simulator.QD_FIFO)
			s.handleFIFO(time);
		else
			s.handleLIFO(time);
	}
}
