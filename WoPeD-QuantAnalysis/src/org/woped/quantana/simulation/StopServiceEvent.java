package org.woped.quantana.simulation;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class StopServiceEvent extends SimEvent {
	
	private int type = SimEvent.STOP_SERVICE_EVENT;
	
	private Server server;
	private Case c;
	private Resource r;
	
	public StopServiceEvent(Simulator sim, double time, Activity a) {//Server server, Case c, Resource r){
		super(sim, time);
		this.server = a.getServer();
		this.c = a.get_case();
		this.r = a.getResource();
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		ResourceUtilization ru = sim.getResUtil();
		
		String s = sim.clckS() + "Bedienung von Case # " + c.getId() + " am Server \"" + server.getName() + "(" + server.getId() + ")\"";
		if (r != null) s += " durch Ressource \"" + r.getName() + "\" abgeschlossen.";
		else s += " abgeschlossen.";
		protocol.info(s);
		
		server.updateUtilStats(time, sim.getTimeOfLastEvent());
		
		server.incNumCasesInParallel(-1);
		protocol.info(sim.clckS() + "Anzahl der parallel bearbeiteten Cases am Server \"" + server.getName() + "(" + server.getId() + ")\" ist " + server.getNumCasesInParallel());
		
		server.setAvgServiceTime(server.getAvgServiceTime() + time - c.getCurrentArrivalTime());
		
		if (r != null) {
			r.setBusyTime(r.getBusyTime() + time - r.getLastStartTime());
			ru.freeResource(r);
			protocol.info(sim.clckS() + "Ressource \"" + r.getName() + "\" wird befreit.");
			protocol.info(sim.clckS() + "Servicezeit von Ressource \"" + r.getName() + "\" bisher: " + r.getBusyTime());
			protocol.info(sim.clckS() + "Freie Ressourcen: " + ru.printFreeResources());
			protocol.info(sim.clckS() + "Gebundene Ressourcen: " + ru.printUsedResources());
		}
		
		if (sim.getUseResAlloc() == Simulator.RES_NOT_USED){
			server.setStatus(Server.STATUS_IDLE);
			protocol.info(sim.clckS() + "Server \"" + server.getName() + "(" + server.getId() + ")\" ist untätig.");
		} else {
			ResourceFreedEvent re = new ResourceFreedEvent(sim, time, server);
			sim.getEventList().add(re);
			protocol.info(sim.clckS() + "RESOURCE_FREED_EVENT \"" + re.getName() + "\" für Server \"" + server.getName() + "(" + server.getId() + ")\" wurde erzeugt.");
			
			if (server.getNumCasesInParallel() == 0 && server.getQueue().size() == 0){
				server.setStatus(Server.STATUS_IDLE);
				protocol.info(sim.clckS() + "Server \"" + server.getName() + "(" + server.getId() + ")\" ist untätig.");
			}
		}
		
		WorkItem wi = new WorkItem(c, server);
		DepartureEvent de = new DepartureEvent(sim, time, wi);
		sim.getEventList().add(de);
		protocol.info(sim.clckS() + "DEPARTURE_EVENT \"" + de.getName() + "\" für Case # " + c.getId() + " vom Server \"" + server.getName() + "(" + server.getId() + ")\" wurde erzeugt.");
		
		sim.setTimeOfLastEvent(time);
		protocol.info(sim.clckS() + "Zeit des letzten Ereignisses ist " + time);
	}
}
