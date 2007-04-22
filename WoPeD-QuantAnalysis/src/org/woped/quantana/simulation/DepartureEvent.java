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
		/*ProtocolItem pi = new ProtocolItem(getSim());
		
		if (!(server.getQueue().isEmpty())){
			updateServerStatistics(server, c);
			scheduleDeparture(c);
		} else {
			server.setStatus(Server.STATUS_IDLE);
			server.incNumCasesInParallel(-1);
		}
		
		double t = getSim().getClock();
		double wt = t - c.getCurrentArrivalTime();
		if (wt > server.getMaxWaitTimeOfCase()) server.setMaxWaitTimeOfCase(wt);

		if (server.getSuccessor().size() == 0) c.setSysDepartureTime(t);
		else {
			ArrivalEvent ae = new ArrivalEvent(getSim(), t, server.gotoNextServer(), c);
			getSim().getEventList().add(ae);
		}
		
		getSim().protocolUpdate(pi);*/
		
		Simulator sim = getSim();
		double time = getTime();
		ResourceUtilization ru = sim.getResUtil();
		
		server.updateUtilStats(time, sim.getTimeOfLastEvent());
		
		if (server.hasFreeCapacity() && server.isIdle() && !(server.getQueue().isEmpty())){
			Case c2 = server.dequeue();
			Resource r = ru.chooseResourceFromFreeResources(server.getGroup(), server.getRole());
			
			StartServiceEvent se = new StartServiceEvent(sim, time, server, c2, r);
			sim.getEventList().add(se);
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
	
	/*private void updateServerStatistics(Server s, Case c){
		s.incNumCalls(1);
		s.incNumDeparture(1);
	}
	
	private void scheduleDeparture(Case c){
		
	}*/
}
