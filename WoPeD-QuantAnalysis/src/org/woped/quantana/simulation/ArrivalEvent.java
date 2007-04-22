package org.woped.quantana.simulation;

import org.woped.quantana.resourcealloc.Resource;

@SuppressWarnings("unused")
public class ArrivalEvent extends SimEvent{
	
	private int type = SimEvent.ARRIVAL_EVENT;
	
	private Server server;
	private Case c;
	
	public ArrivalEvent(Simulator sim, double time, Server serv, Case c){
		super(sim, time);
		server = serv;
		this.c = c;
	}
	
	public void invoke(){
		/*ProtocolItem pi = new ProtocolItem(getSim());
		
		switch (server.getStatus()){
		case Server.STATUS_IDLE:
			updateServerStatistics(server);
			server.setStatus(Server.STATUS_BUSY);
			scheduleDeparture(server, c);
			break;
		default:
			putCaseInQueue(server, c);
		}
		
		getSim().protocolUpdate(pi);*/
		
		Simulator sim = getSim();
		double time = getTime();
		
		c.setCurrentArrivalTime(time);
		c.setNextServiceTime(server.getNextServTime());
		server.incNumCalls(1);
		
		server.updateUtilStats(time, sim.getTimeOfLastEvent());
		
		if (server.hasFreeCapacity() && server.isIdle()) {
			server.incZeroDelays(1);
			Resource r = sim.getResUtil().chooseResourceFromFreeResources(server.getGroup(), server.getRole());
			
			StartServiceEvent se = new StartServiceEvent(sim, time, server, c, r);
			sim.getEventList().add(se);
		} else {
			server.enqueue(c);
		}
		
		int caseCount = sim.getCaseCount();
		if ((sim.getStopRule() == Simulator.STOP_CASE_DRIVEN) || (sim.getStopRule() == Simulator.STOP_BOTH)){
			if (caseCount < sim.getLambda()){
				generateBirthEvent();
			}
		} else {
			generateBirthEvent();
		}
		
		sim.setTimeOfLastEvent(time);
	}
	
	private void generateBirthEvent(){
		BirthEvent be = new BirthEvent(getSim(), getTime());
		getSim().getEventList().add(be);
	}
	
	/*private void updateServerStatistics(Server s){
		s.incNumCalls(1);
		s.incZeroDelays(1);
		s.incNumAccess(1);
		
		if (getSim().getUseResAlloc() == Simulator.RES_NOT_USED){
			s.incNumCasesInParallel(1);
		}
		
		int len = s.getQueue().size();
		if (len > s.getMaxQueueLength()) s.setMaxQueueLength(len);
		
		int par = s.getNumCasesInParallel();
		if (par > s.getMaxNumCasesInParallel()) s.setMaxNumCasesInParallel(par);
		
		s.setCurCase(c);
	}
	
	private void scheduleDeparture(Server s, Case c){
		double t = s.getNextServTime() + getTime();
		DepartureEvent de = new DepartureEvent(getSim(), t, s, c);
		getSim().getEventList().add(de);
	}
	
	private void putCaseInQueue(Server s, Case c){
		s.getQueue().add(c);
	}*/
}
