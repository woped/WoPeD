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
	
	public StopServiceEvent(Simulator sim, double time, Activity a) {//Server server, Case c, Resource r){
		super(sim, time);
		this.s = a.getServer();
		this.c = a.getCase();
		this.r = a.getResource();
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		/*ResourceUtilization ru = sim.getResUtil();
		
		String s = sim.clckS() + ENTRY.getString("Sim.StartService.Info.A");
		if (r != null) s += ENTRY.getString("Sim.StartService.Info.B") + r.getName() + ENTRY.getString("Sim.StopService.Info.C");
		else s += ENTRY.getString("Sim.StopService.Info.D");
		protocol.log(Level.INFO, s, new Object[] {c.getId(), server.getName(), server.getId()});
		
//		server.updateUtilStats(time, sim.getTimeOfLastEvent());

		int cc = server.getQueue().size() + server.getNumCasesInParallel();
		server.setAvgNumCasesServing(server.getAvgNumCasesServing() + cc * (time - sim.getTimeOfLastEvent()));
		
		server.incNumCasesInParallel(-1);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.StartService.Cases.Parallel") + server.getNumCasesInParallel(), new Object[] {server.getName(), server.getId()});
		
		server.setAvgServiceTime(server.getAvgServiceTime() + time - c.getCurrentArrivalTime());
		
		if (r != null) {
			r.setBusyTime(r.getBusyTime() + time - r.getLastStartTime());
			ru.freeResource(r);
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.StopService.Resource.Free"), r.getName());
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Resources.Time.Service") + String.format("%,.2f", r.getBusyTime()), r.getName());
			protocol.info(sim.clckS() + ENTRY.getString("Sim.Resources.Free") + ru.printFreeResources());
			protocol.info(sim.clckS() + ENTRY.getString("Sim.Resources.Used") + ru.printUsedResources());
		}
		
		if (sim.getUseResAlloc() == Simulator.RES_NOT_USED || r == null){
			server.setStatus(Server.STATUS_IDLE);
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Server.Idle"), new Object[] {server.getName(), server.getId()});
		} else {
			ResourceFreedEvent re = new ResourceFreedEvent(sim, time, server);
			sim.getEventList().add(re);
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Event.ResourceFreed") + re.getName() + ENTRY.getString("Sim.StopService.ForServer"), new Object[] {server.getName(), server.getId()});
			
			if (server.getNumCasesInParallel() == 0 && server.getQueue().size() == 0){
				server.setStatus(Server.STATUS_IDLE);
				protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Server.Idle"), new Object[] {server.getName(), server.getId()});
			}
		}
		
		WorkItem wi = new WorkItem(c, server);
		DepartureEvent de = new DepartureEvent(sim, time, wi);
		sim.getEventList().add(de);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Event.Departure") + de.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId() + ENTRY.getString("Sim.StopService.ForServer"), new Object[] {server.getName(), server.getId()});
		
		sim.setTimeOfLastEvent(time);
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Time.LastEvent") + String.format("%,.2f", time));
		*/
		
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
//		sim.getWd().getTxtArea().append("SP: (Case# " + c.getId() + ", Server: " + s + "): " + time + "\n");
	}
}
