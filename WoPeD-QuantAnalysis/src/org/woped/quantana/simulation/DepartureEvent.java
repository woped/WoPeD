package org.woped.quantana.simulation;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class DepartureEvent extends SimEvent {
	
	private int type = SimEvent.DEPARTURE_EVENT;
	
	private Server server;
	private Case c;
	
	public DepartureEvent(Simulator sim, double time, Server server, Case c){
		super(sim, time);
		this.server = server;
		this.c = c;
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		ResourceUtilization ru = sim.getResUtil();
		
		server.updateUtilStats(time, sim.getTimeOfLastEvent());
		
		/*if (server.hasFreeCapacity() && server.isIdle() && !(server.getQueue().isEmpty())){
			Case c2 = server.dequeue();
			Resource r = ru.chooseResourceFromFreeResources(server.getGroup(), server.getRole());
			
			StartServiceEvent se = new StartServiceEvent(sim, time, server, c2, r);
			sim.getEventList().add(se);
		}*/
		
		if (!(server.getQueue().isEmpty())){
			Case c2 = server.dequeue();
			
			if (sim.getUseResAlloc() == Simulator.RES_NOT_USED){
				if (server.isIdle())
					nextStartServiceEvent(sim, time, c2);
			} else {
				if (server.hasFreeCapacity())
					nextStartServiceEvent(sim, time, c2);
			}
		}
		
		Server nextServer = server.gotoNextServer();
		if (nextServer == null){
			DeathEvent de = new DeathEvent(sim, time, c);
			sim.getEventList().add(de);
		} else {
			ArrivalEvent ae = new ArrivalEvent(sim, time, nextServer, c);
			sim.getEventList().add(ae);
		}
		
		server.incNumDeparture(1);
		
		sim.setTimeOfLastEvent(time);
	}
	
	private void nextStartServiceEvent(Simulator sim, double time, Case c){
		Resource r = sim.getResUtil().chooseResourceFromFreeResources(server.getGroup(), server.getRole());
		
		if (sim.getUseResAlloc() == Simulator.RES_USED && r == null) return;
		
		StartServiceEvent se = new StartServiceEvent(sim, time, server, c, r);
		sim.getEventList().add(se);
	}
}
