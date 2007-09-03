package org.woped.quantana.simulation;

import java.util.ResourceBundle;
import java.util.logging.Level;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class StopServiceEvent extends SimEvent {
	
	private int type = SimEvent.STOP_SERVICE_EVENT;
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
	private Server s;
	private Case c;
	private Resource r;
	
	public StopServiceEvent(Simulator sim, double time, Activity a) {
		super(sim, time);
		this.s = a.getServer();
		this.c = a.getCase();
		this.r = a.getResource();
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		
		String x = sim.clckS() + ENTRY.getString("Sim.StartService.Info.A");
		if (r != null) x += ENTRY.getString("Sim.StartService.Info.B") + r.getName() + ENTRY.getString("Sim.StopService.Info.C");
		else x += ENTRY.getString("Sim.StopService.Info.D");
		protocol.log(Level.INFO, x, new Object[] {c.getId(), s.getName(), s.getId()});
		
		s.updRStats(time, -1);
		
		if (r != null){
			sim.free(r);
			r.updStats(time);
		}
		
		int num = s.getNumCasesInParallel();
		int ql = s.getQueue().size();
		
		if ((num == 0) && (ql == 0)) s.setStatus(Server.STATUS_IDLE);
		
		WorkItem wi = new WorkItem(c, s);
		DepartureEvent dp = new DepartureEvent(sim, time, wi);
		sim.enroleEvent(dp);
		
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Event.Departure") + dp.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId() + ENTRY.getString("Sim.StopService.ForServer"), new Object[] {s.getName(), s.getId()});
		
		s.incNumDeparture();
		s.incServiceTime(c.getNextServTime());
		c.updServTime();
	}
}
