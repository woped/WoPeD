package org.woped.quantana.simulation;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class StopServiceEvent extends SimEvent {
	
	private int type = SimEvent.STOP_EVENT;
	
	private Server server;
	private Case c;
	private Resource r;
	
	public StopServiceEvent(Simulator sim, double time, Server server, Case c, Resource r){
		super(sim, time);
		this.server = server;
		this.c = c;
		this.r = r;
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		ResourceUtilization ru = sim.getResUtil();
		
		server.updateUtilStats(time, sim.getTimeOfLastEvent());
		
		server.incNumCasesInParallel(-1);
		server.setCurCase(null);
		
		r.setBusyTime(r.getBusyTime() + time - r.getLastStartTime());
		ru.freeResource(r);
		
		if (sim.getUseResAlloc() == Simulator.RES_NOT_USED){
			server.setStatus(Server.STATUS_IDLE);
		} else {
			ResourceFreedEvent re = new ResourceFreedEvent(sim, time, server);
			sim.getEventList().add(re);
			
			if (server.getNumCasesInParallel() == 0 && server.getQueue().size() == 0){
				server.setStatus(Server.STATUS_IDLE);
			}
		}
		
		DepartureEvent de = new DepartureEvent(sim, time, server, c);
		sim.getEventList().add(de);
		
		sim.setTimeOfLastEvent(time);
	}
}
