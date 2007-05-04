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
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		ResourceUtilization ru = sim.getResUtil();
		
		protocol.info(sim.clckS() + "Case # " + c.getId() + " verläßt Server \"" + server.getName() + "(" + server.getId() + ")\".");
		
		server.updateUtilStats(time, sim.getTimeOfLastEvent());
		
		/*if (server.hasFreeCapacity() && server.isIdle() && !(server.getQueue().isEmpty())){
			Case c2 = server.dequeue();
			Resource r = ru.chooseResourceFromFreeResources(server.getGroup(), server.getRole());
			
			StartServiceEvent se = new StartServiceEvent(sim, time, server, c2, r);
			sim.getEventList().add(se);
		}*/
		
		if (!(server.getQueue().isEmpty())){
			protocol.info(sim.clckS() + "Inhalt der Warteschlange von Server \"" + server.getName() + "(" + server.getId() + ")\": " + server.printQueue());
			Case c2 = server.dequeue();
			protocol.info(sim.clckS() + "Case # " + c2.getId() + " wurde aus Warteschlange entfernt.");
			
			if (sim.getUseResAlloc() == Simulator.RES_NOT_USED){
				if (server.isIdle())
					nextStartServiceEvent(sim, time, c2, ru);
			} else {
				if (server.hasFreeCapacity())
					nextStartServiceEvent(sim, time, c2, ru);
			}
		}
		
		Server nextServer = server.gotoNextServer();
		if (nextServer == null){
			DeathEvent de = new DeathEvent(sim, time, c);
			sim.getEventList().add(de);
			protocol.info(sim.clckS() + "Case # " + c.getId() + " verläßt den Prozess.");
			protocol.info(sim.clckS() + "DEATH_EVENT \"" + de.getName() + "\" für Case # " + c.getId() + " wurde erzeugt.");
		} else {
			ArrivalEvent ae = new ArrivalEvent(sim, time, nextServer, c);
			sim.getEventList().add(ae);
			protocol.info(sim.clckS() + "Case # " + c.getId() + " wird an Server \"" + nextServer.getName() + "(" + nextServer.getId() + ")\" weitergeleitet.");
			protocol.info(sim.clckS() + "ARRIVAL_EVENT \"" + ae.getName() + "\" für Case # " + c.getId() + " wurde erzeugt.");
		}
		
		server.incNumDeparture(1);
		protocol.info(sim.clckS() + "Anzahl der von Server \"" + server.getName() + "(" + server.getId() + ")\" bedienten Cases beträgt " + server.getNumDeparture());
		
		sim.setTimeOfLastEvent(time);
		protocol.info(sim.clckS() + "Zeit des letzten Ereignisses ist " + time);
	}
	
	private void nextStartServiceEvent(Simulator sim, double time, Case c, ResourceUtilization ru){
		Resource r = sim.getResUtil().chooseResourceFromFreeResources(server.getGroup(), server.getRole());
		
		if (sim.getUseResAlloc() == Simulator.RES_USED && r == null) return;
		if (r != null) {
			protocol.info(sim.clckS() + "Ressource \"" + r.getName() + "\" wird Server \"" + server.getName() + "(" + server.getId() + ")\" und Case # " + c.getId() + " zugeordnet.");
			protocol.info(sim.clckS() + "Freie Ressourcen: " + ru.printFreeResources());
			protocol.info(sim.clckS() + "Gebundene Ressourcen: " + ru.printUsedResources());
		}
		
		StartServiceEvent se = new StartServiceEvent(sim, time, server, c, r);
		sim.getEventList().add(se);
		protocol.info(sim.clckS() + "START_SERVICE_EVENT \"" + se.getName() + "\" für Case # " + c.getId() + " am Server \"" + server.getName() + "(" + server.getId() + ")\" wurde erzeugt.");
	}
}
