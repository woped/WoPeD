package org.woped.quantana.simulation;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class ArrivalEvent extends SimEvent{
	
	private int type = SimEvent.ARRIVAL_EVENT;
	
	private WorkItem wi;
	private Server server;
	private Case c;
	
	public ArrivalEvent(Simulator sim, double time, WorkItem wi) {//Server serv, Case c){
		super(sim, time);
		this.wi = wi;
		server = wi.getServer();//serv;
		this.c = wi.get_case();//c;
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		ResourceUtilization ru = sim.getResUtil();
		
		protocol.info(sim.clckS() + "Case # " + c.getId() + " kommt am Server \"" + server.getName() + "(" + server.getId() + ")\" an.");
		
		c.setCurrentArrivalTime(time);
		c.setNextServiceTime(server.getNextServTime());
//		protocol.info(sim.clckS() + "Bedienzeit für Case # " + c.getId() + " ist " + c.getNextServiceTime());
		
		server.incNumCalls(1);
		protocol.info(sim.clckS() + "Anzahl Ankünfte am Server \"" + server.getName() + "(" + server.getId() + ")\": " + server.getNumCalls());
		
		server.updateUtilStats(time, sim.getTimeOfLastEvent());
		
		if (sim.getUseResAlloc() == Simulator.RES_NOT_USED){
			if (server.isIdle()){
				if (sim.getQueueDiscipline() == Simulator.QD_FIFO){
					if (server.getQueue().isEmpty()){
						nextStartServiceEvent(sim, time, c, ru);
					} else {
						server.enqueue(wi);
						nextStartServiceEvent(sim, time, server.dequeue().get_case(), ru);
					}
				} else {
					nextStartServiceEvent(sim, time, c, ru);
				}
			} else {
				server.enqueue(new WorkItem(c, server));
			}
		} else {
			if (server.hasFreeCapacity()){
				if (sim.getQueueDiscipline() == Simulator.QD_FIFO){
					if (server.getQueue().isEmpty()){
						nextStartServiceEvent(sim, time, c, ru);
					} else {
						server.enqueue(new WorkItem(c, server));
						nextStartServiceEvent(sim, time, server.dequeue().get_case(), ru);
					}
				} else {
					nextStartServiceEvent(sim, time, c, ru);
				}
			} else {
				server.enqueue(new WorkItem(c, server));
			}
		}
		
		int caseCount = sim.getCaseCount();
		if ((sim.getStopRule() == Simulator.STOP_CASE_DRIVEN) || (sim.getStopRule() == Simulator.STOP_BOTH)){
			if (caseCount < sim.getLambda()){
				generateBirthEvent(sim);
			}
		} else {
			generateBirthEvent(sim);
		}
		
		sim.setTimeOfLastEvent(time);
		protocol.info(sim.clckS() + "Zeit des letzten Ereignisses ist " + time);
	}
	
	private void generateBirthEvent(Simulator sim){
		BirthEvent be = new BirthEvent(getSim(), getTime());
		getSim().getEventList().add(be);
		protocol.info(sim.clckS() + "BIRTH_EVENT \"" + be.getName() + "\" wurde erzeugt.");
	}
	
	private void nextStartServiceEvent(Simulator sim, double time, Case c, ResourceUtilization ru){
		server.incZeroDelays(1);
		protocol.info(sim.clckS() + "Anzahl Cases ohne Wartezeit von Server \"" + server.getName() + "(" + server.getId() + ")\" erhöht auf " + server.getZeroDelays());
		
		Resource r = sim.getResUtil().chooseResourceFromFreeResources(server.getGroup(), server.getRole());
		
		if (sim.getUseResAlloc() == Simulator.RES_USED && r == null) return;
		if (r != null) {
			protocol.info(sim.clckS() + "Ressource \"" + r.getName() + "\" wird Server \"" + server.getName() + "(" + server.getId() + ")\" und Case # " + c.getId() + " zugeordnet.");
			protocol.info(sim.clckS() + "Freie Ressourcen: " + ru.printFreeResources());
			protocol.info(sim.clckS() + "Gebundene Ressourcen: " + ru.printUsedResources());
		}
		
		Activity act = new Activity(c, server, r);
		StartServiceEvent se = new StartServiceEvent(sim, time, act);
		sim.getEventList().add(se);
		protocol.info(sim.clckS() + "START_SERVICE_EVENT \"" + se.getName() + "\" für Case # " + c.getId() + " am Server \"" + server.getName() + "(" + server.getId() + ")\" wurde erzeugt.");
	}
}
