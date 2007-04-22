package org.woped.quantana.simulation;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class ResourceFreedEvent extends SimEvent {
	
	private int type = SimEvent.RESOURCE_FREED_EVENT;
	
	private Server server;
	
	public ResourceFreedEvent(Simulator sim, double time, Server server){
		super(sim, time);
		this.server = server;
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		ResourceUtilization ru = sim.getResUtil();
		
		server.updateUtilStats(time, sim.getTimeOfLastEvent());
		
		if ((!(server.getQueue().isEmpty())) && (server.hasFreeCapacity())){
			Case c2 = server.dequeue();
			Resource r = ru.chooseResourceFromFreeResources(server.getGroup(), server.getRole());
			
			StartServiceEvent se = new StartServiceEvent(sim, time, server, c2, r);
			sim.getEventList().add(se);
		}
	}
}
