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
		Simulator sim = getSim();
		double time = getTime();
		
		c.setCurrentArrivalTime(time);
		c.setNextServiceTime(server.getNextServTime());
		server.incNumCalls(1);
		
		server.updateUtilStats(time, sim.getTimeOfLastEvent());
		
		/*if (server.hasFreeCapacity() && server.isIdle()) {
			server.incZeroDelays(1);
			Resource r = sim.getResUtil().chooseResourceFromFreeResources(server.getGroup(), server.getRole());
			
			if ((sim.getUseResAlloc() == Simulator.RES_NOT_USED) || (r != null)){
				StartServiceEvent se = new StartServiceEvent(sim, time, server, c, r);
				sim.getEventList().add(se);
			}
		} else {
			server.enqueue(c);
		}*/
		
		if (sim.getUseResAlloc() == Simulator.RES_NOT_USED){
			if (server.isIdle()){
				if (sim.getQueueDiscipline() == Simulator.QD_FIFO){
					if (server.getQueue().isEmpty()){
						nextStartServiceEvent(sim, time, c);
					} else {
						server.enqueue(c);
						nextStartServiceEvent(sim, time, server.dequeue());
					}
				} else {
					nextStartServiceEvent(sim, time, c);
				}
			} else {
				server.enqueue(c);
			}
		} else {
			if (server.hasFreeCapacity()){
				if (sim.getQueueDiscipline() == Simulator.QD_FIFO){
					if (server.getQueue().isEmpty()){
						nextStartServiceEvent(sim, time, c);
					} else {
						server.enqueue(c);
						nextStartServiceEvent(sim, time, server.dequeue());
					}
				} else {
					nextStartServiceEvent(sim, time, c);
				}
			} else {
				server.enqueue(c);
			}
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
	
	private void nextStartServiceEvent(Simulator sim, double time, Case c){
		server.incZeroDelays(1);
		Resource r = sim.getResUtil().chooseResourceFromFreeResources(server.getGroup(), server.getRole());
		
		if (sim.getUseResAlloc() == Simulator.RES_USED && r == null) return;
		
		StartServiceEvent se = new StartServiceEvent(sim, time, server, c, r);
		sim.getEventList().add(se);
	}
}
