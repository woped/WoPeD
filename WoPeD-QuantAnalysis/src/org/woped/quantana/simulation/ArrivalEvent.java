package org.woped.quantana.simulation;

import java.util.ResourceBundle;
import java.util.logging.Level;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class ArrivalEvent extends SimEvent{
	
	private int type = SimEvent.ARRIVAL_EVENT;
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
	private WorkItem wi;
	private Server server;
	private Case c;
	
	public ArrivalEvent(Simulator sim, double time, WorkItem wi) {
		super(sim, time);
		this.wi = wi;
		server = wi.getServer();
		this.c = wi.get_case();
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		ResourceUtilization ru = sim.getResUtil();
		
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Arrival.Info"), new Object[] {c.getId(), server.getName(), server.getId()});
		
		c.setCurrentArrivalTime(time);
		
		server.incNumCalls(1);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Arrival.Number") + server.getNumCalls(), new Object[] {server.getName(), server.getId()});
		
		server.updateUtilStats(time, sim.getTimeOfLastEvent());
		
		if (sim.getUseResAlloc() == Simulator.RES_NOT_USED){
			if (server.isIdle()){
				if (sim.getQueueDiscipline() == Simulator.QD_FIFO){
					if (server.getQueue().isEmpty()){
						nextStartServiceEvent(sim, time, c, ru);
						server.incZeroDelays(1);
						
					} else {
						wi.enqueue();
						nextStartServiceEvent(sim, time, server.dequeue().get_case(), ru);
					}
				} else {
					nextStartServiceEvent(sim, time, c, ru);
					server.incZeroDelays(1);
				}
			} else {
				wi.enqueue();
			}
		} else {
			if (server.hasFreeCapacity()){
				if (sim.getQueueDiscipline() == Simulator.QD_FIFO){
					if (server.getQueue().isEmpty()){
						nextStartServiceEvent(sim, time, c, ru);
						server.incZeroDelays(1);
						protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Arrival.ZeroDelays") + server.getZeroDelays(), new Object[] {server.getName(), server.getId()});
					} else {
						wi.enqueue();
						nextStartServiceEvent(sim, time, server.dequeue().get_case(), ru);
					}
				} else {
					nextStartServiceEvent(sim, time, c, ru);
					server.incZeroDelays(1);
					protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Arrival.ZeroDelays") + server.getZeroDelays(), new Object[] {server.getName(), server.getId()});
				}
			} else {
				wi.enqueue();
			}
		}
		
		if (sim.getStartServerList().contains(this)) sim.setCaseCount(sim.getCaseCount() + 1);
		int cc = sim.getCaseCount();
		if (cc > sim.getMaxNumCasesInSystem()) sim.setMaxNumCasesInSystem(cc);
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Arrival.Num.Case.Max") + sim.getMaxNumCasesInSystem());
		
		if ((sim.getStopRule() == Simulator.STOP_CASE_DRIVEN) || (sim.getStopRule() == Simulator.STOP_BOTH)){
			if (cc < sim.getLambda()){
				generateBirthEvent(sim);
			}
		} else {
			generateBirthEvent(sim);
		}
		
		sim.setTimeOfLastEvent(time);
		protocol.info(sim.clckS() + "Sim.Time.LastEvent" + String.format("%,.2f", time));
	}
	
	private void generateBirthEvent(Simulator sim){
		BirthEvent be = new BirthEvent(getSim(), getTime());
		getSim().getEventList().add(be);
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Event.Birth") + be.getName() + ENTRY.getString("Sim.Generated"));
	}
	
	private void nextStartServiceEvent(Simulator sim, double time, Case c, ResourceUtilization ru){
		Resource r = sim.getResUtil().chooseResourceFromFreeResources(server.getGroup(), server.getRole());
		
		if (r != null) {
			ru.useResource(r);
			r.setLastStartTime(time);
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Arrival.Resource.Bind"), new Object[] {r.getName(), server.getName(), server.getId()});
			protocol.info(sim.clckS() + ENTRY.getString("Sim.Resources.Free") + ru.printFreeResources());
			protocol.info(sim.clckS() + ENTRY.getString("Sim.Resources.Used") + ru.printUsedResources());
		}
		
		Activity act = new Activity(c, server, r);
		StartServiceEvent se = new StartServiceEvent(sim, time, act);
		sim.getEventList().add(se);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Event.StartService") + se.getName() + ENTRY.getString("Sim.Generated.Event.Server"), new Object[] {c.getId(), server.getName(), server.getId()});
	}
}
