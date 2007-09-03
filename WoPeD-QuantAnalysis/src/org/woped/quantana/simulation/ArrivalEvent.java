package org.woped.quantana.simulation;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class ArrivalEvent extends SimEvent{
	
	private int type = SimEvent.ARRIVAL_EVENT;
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
	private WorkItem wi;
	private Server s;
	private Case c;
	
	public ArrivalEvent(Simulator sim, double time, WorkItem wi) {
		super(sim, time);
		this.wi = wi;
		s = wi.getServer();
		this.c = wi.getCase();
		
		setName(getNewName());
		
		sim.incCntArrivalEvents();
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Arrival.Info"), new Object[] {c.getId(), s.getName(), s.getId()});
		
		if (s instanceof ANDJoinServer){
			((ANDJoinServer)s).handleJoin(sim, time, (CaseCopy)c);
		} else {
			c.setCurrArrivalTime(time);
			s.incNumCalls();
			BirthEvent be = new BirthEvent(sim, time);
			sim.enroleEvent(be);
			protocol.info(sim.clckS() + ENTRY.getString("Sim.Event.Birth") + be.getName() + ENTRY.getString("Sim.Event.Generated"));

			s.setTmpNumCParallel(s.getNumCasesInParallel());

			if (s.hasFreeCapacity()){
				Resource r = s.getResource();
				sim.bind(r);
				Activity act = new Activity(c, s, r);
				StartServiceEvent st = new StartServiceEvent(sim, time, act);
				sim.enroleEvent(st);
				String x = "";
				if (r != null) x = r.getName();
				protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Event.StartService") + st.getName() + ENTRY.getString("Sim.Generated.Event.Server"), new Object[] {c.getId(), x, s.getName(), s.getId()});
				
				s.incZeroDelays();
				s.incTmpNumCParallel();
			} else {
				s.updQStats(time, 1);
				s.enqueue(c);
			}
		}

		sim.decCntArrivalEvents();
	}
}
