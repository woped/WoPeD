package org.woped.quantana.simulation;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class StartServiceEvent extends SimEvent {
	
	private int type = SimEvent.START_EVENT;
	
	private Server server;
	private Case c;
	private Resource r;
	
	public StartServiceEvent(Simulator sim, double time, Server server, Case c, Resource r){
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
		
		server.setStatus(Server.STATUS_BUSY);
		server.setCurCase(c);
		server.incNumAccess(1);
		server.incNumCasesInParallel(1);
		
		int par = server.getNumCasesInParallel();
		int max = server.getMaxNumCasesInParallel();
		if (par > max) server.setMaxNumCasesInParallel(par);
		
		double wait = time - c.getCurrentArrivalTime();
		if (wait > server.getMaxWaitTimeOfCase()) server.setMaxWaitTimeOfCase(wait);
		c.setTimeWait(c.getTimeWait() + wait);
		c.setTimeService(c.getTimeService() + c.getNextServiceTime());
		
		double depart = time + c.getNextServiceTime();
		c.setCurrentDepartureTime(depart);
		
		ru.useResource(r);
		if (r != null) r.setLastStartTime(time);
		
		StopServiceEvent se = new StopServiceEvent(sim, depart, server, c, r);
		sim.getEventList().add(se);
		
		sim.setTimeOfLastEvent(getTime());
	}
}
