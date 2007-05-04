package org.woped.quantana.simulation;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class StartServiceEvent extends SimEvent {
	
	private int type = SimEvent.START_SERVICE_EVENT;
	
	private Server server;
	private Case c;
	private Resource r;
	
	public StartServiceEvent(Simulator sim, double time, Server server, Case c, Resource r){
		super(sim, time);
		this.server = server;
		this.c = c;
		this.r = r;
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		ResourceUtilization ru = sim.getResUtil();
		
		String s = sim.clckS() + "Bedienung von Case # " + c.getId() + " am Server \"" + server.getName() + "(" + server.getId() + ")\"";
		if (r != null) s += " durch Ressource \"" + r.getName() + "\" beginnt.";
		else s += " beginnt.";
		protocol.info(s);
		
		if (sim.getUseResAlloc() == Simulator.RES_NOT_USED || r != null){
			server.updateUtilStats(time, sim.getTimeOfLastEvent());

			server.setStatus(Server.STATUS_BUSY);
			protocol.info(sim.clckS() + "Server \"" + server.getName() + "(" + server.getId() + ")\" ist beschäftigt.");
			
			//server.setCurCase(c);
			server.incNumAccess(1);
			protocol.info(sim.clckS() + "Anzahl der bedienten Cases am Server \"" + server.getName() + "(" + server.getId() + ")\" steigt auf " + server.getNumAccess());
			
			server.incNumCasesInParallel(1);
			protocol.info(sim.clckS() + "Anzahl der parallel bearbeiteten Cases am Server \"" + server.getName() + "(" + server.getId() + ")\" ist " + server.getNumCasesInParallel());

			int par = server.getNumCasesInParallel();
			int max = server.getMaxNumCasesInParallel();
			if (par > max) server.setMaxNumCasesInParallel(par);
			protocol.info(sim.clckS() + "Maximale Anzahl von parallel bearbeiteten Cases am Server \"" + server.getName() + "(" + server.getId() + ")\" bisher ist " + server.getMaxNumCasesInParallel());

			double wait = time - c.getCurrentArrivalTime();
			protocol.info(sim.clckS() + "Case # " + c.getId() + " hat " + wait + " Zeiteinheiten am Server \"" + server.getName() + "(" + server.getId() + ")\" gewartet.");
			
			if (wait > server.getMaxWaitTimeOfCase()) server.setMaxWaitTimeOfCase(wait);
			protocol.info(sim.clckS() + "Maximale Wartezeit am Server \"" + server.getName() + "(" + server.getId() + ")\" bisher beträgt " + server.getMaxWaitTimeOfCase());
			
			c.setTimeWait(c.getTimeWait() + wait);
			c.setTimeService(c.getTimeService() + c.getNextServiceTime());
			protocol.info(sim.clckS() + "Bisherige Wartezeit von Case # " + c.getId() + " kumuliert: " + c.getTimeService());
			protocol.info(sim.clckS() + "Bisherige Bedienzeit von Case # " + c.getId() + " kumuliert: " + c.getTimeService());
			
			double depart = time + c.getNextServiceTime();
			c.setCurrentDepartureTime(depart);
			protocol.info(sim.clckS() + "Ende der Bedienung von Case # " + c.getId() + " am Server \"" + server.getName() + "(" + server.getId() + ")\" " + depart);

			ru.useResource(r);
			if (r != null) {
				r.setLastStartTime(time);
				protocol.info(sim.clckS() + "Ressource \"" + r.getName() + "\" wird an Server \"" + server.getName() + "(" + server.getId() + ")\"  gebunden.");
				protocol.info(sim.clckS() + "Freie Ressourcen: " + ru.printFreeResources());
				protocol.info(sim.clckS() + "Gebundene Ressourcen: " + ru.printUsedResources());
			}
			
			StopServiceEvent se = new StopServiceEvent(sim, depart, server, c, r);
			sim.getEventList().add(se);
			s = sim.clckS() + "STOP_SERVICE_EVENT \"" + se.getName() + "\" für Case # " + c.getId();
			if (r != null) s  += " und Ressource \"" + r.getName() + "\" wurde erzeugt.";
			else s += " wurde erzeugt.";
			protocol.info(s);

			sim.setTimeOfLastEvent(getTime());
			protocol.info(sim.clckS() + "Zeit des letzten Ereignisses ist " + time);
		}
	}
}
