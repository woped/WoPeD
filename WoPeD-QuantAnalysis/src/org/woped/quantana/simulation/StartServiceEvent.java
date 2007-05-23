package org.woped.quantana.simulation;

import java.util.ArrayList;
import java.util.HashMap;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class StartServiceEvent extends SimEvent {
	
	private int type = SimEvent.START_SERVICE_EVENT;
	
	private Activity act;
	private Server server;
	private Case c;
	private Resource r;
	
	public StartServiceEvent(Simulator sim, double time, Activity a) {//Server server, Case c, Resource r){
		super(sim, time);
		act = a;
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
		if (r != null) s += " durch Ressource \"" + r.getName() + "\" beginnt.";
		else s += " beginnt.";
		protocol.info(s);

		server.updateUtilStats(time, sim.getTimeOfLastEvent());

		server.setStatus(Server.STATUS_BUSY);
		protocol.info(sim.clckS() + "Server \"" + server.getName() + "(" + server.getId() + ")\" ist beschäftigt.");

		//server.setCurCase(c);
		server.incNumAccess(1);
		double wait = time - c.getCurrentArrivalTime();
		server.setWaitTime(server.getWaitTime() + wait);
		protocol.info(sim.clckS() + "Anzahl der bedienten Cases am Server \"" + server.getName() + "(" + server.getId() + ")\" steigt auf " + server.getNumAccess());

		int cc = server.getQueue().size() + server.getNumCasesInParallel();
		server.setAvgNumCasesServing(server.getAvgNumCasesServing() + cc * (time - sim.getTimeOfLastEvent()));

		server.incNumCasesInParallel(1);
		protocol.info(sim.clckS() + "Anzahl der parallel bearbeiteten Cases am Server \"" + server.getName() + "(" + server.getId() + ")\" ist " + server.getNumCasesInParallel());

		int par = server.getNumCasesInParallel();
		int max = server.getMaxNumCasesInParallel();
		if (par > max) server.setMaxNumCasesInParallel(par);
		protocol.info(sim.clckS() + "Maximale Anzahl von parallel bearbeiteten Cases am Server \"" + server.getName() + "(" + server.getId() + ")\" bisher ist " + server.getMaxNumCasesInParallel());

		protocol.info(sim.clckS() + "Case # " + c.getId() + " hat " + wait + " Zeiteinheiten am Server \"" + server.getName() + "(" + server.getId() + ")\" gewartet.");

		if (wait > server.getMaxWaitTimeOfCase()) server.setMaxWaitTimeOfCase(wait);
		protocol.info(sim.clckS() + "Maximale Wartezeit am Server \"" + server.getName() + "(" + server.getId() + ")\" bisher beträgt " + server.getMaxWaitTimeOfCase());

		if (server instanceof ANDJoinServer) {
			CaseCopy copy = (CaseCopy)c;
			Case orig = sim.getCopiedCasesList().get(copy.getOriginal().getId());
			orig.cpyCnt += 1;
			if (orig.cpyCnt == orig.getCopies()){
				ArrayList<CaseCopy> list = ((ANDJoinServer)server).getCopyList().get(orig);
//				list.add(copy);
				double st = copy.getTimeService();
				for (Case _case : list){
					st += _case.getTimeService();
				}
				st /= orig.getCopies();
				double wt = time - orig.getTimeOfSplit() - st;
				orig.setTimeService(st);
				orig.setTimeWait(wt);

				sim.getCopiedCasesList().remove(orig.getId());
				orig.setNextServiceTime(server.getNextServTime());
				double depart = time + orig.getNextServiceTime();
				orig.setCurrentDepartureTime(depart);
				protocol.info(sim.clckS() + "Ende der Bedienung von Case # " + orig.getId() + " am Server \"" + server.getName() + "(" + server.getId() + ")\" " + depart);

				StopServiceEvent se = new StopServiceEvent(sim, depart, new Activity(orig, server, r));
				sim.getEventList().add(se);
				s = sim.clckS() + "STOP_SERVICE_EVENT \"" + se.getName() + "\" für Case # " + orig.getId();
				if (r != null) s  += " und Ressource \"" + r.getName() + "\" wurde erzeugt.";
				else s += " wurde erzeugt.";
				protocol.info(s);
			} else {
				HashMap<Case, ArrayList<CaseCopy>> copyList = ((ANDJoinServer)server).getCopyList();
				ArrayList<CaseCopy> list;
				if (copyList.containsKey(orig)){
					list = copyList.get(orig);
				} else {
					list = new ArrayList<CaseCopy>();
					copyList.put(orig, list);
				}
				list.add(copy);
			}
		} else {
			c.setTimeWait(c.getTimeWait() + wait);
			c.setTimeService(c.getTimeService() + c.getNextServiceTime());
			protocol.info(sim.clckS() + "Bisherige Wartezeit von Case # " + c.getId() + " kumuliert: " + c.getTimeService());
			protocol.info(sim.clckS() + "Bisherige Bedienzeit von Case # " + c.getId() + " kumuliert: " + c.getTimeService());

			c.setNextServiceTime(server.getNextServTime());
			double depart = time + c.getNextServiceTime();
			c.setCurrentDepartureTime(depart);
			protocol.info(sim.clckS() + "Ende der Bedienung von Case # " + c.getId() + " am Server \"" + server.getName() + "(" + server.getId() + ")\" " + depart);

			StopServiceEvent se = new StopServiceEvent(sim, depart, act);
			sim.getEventList().add(se);
			s = sim.clckS() + "STOP_SERVICE_EVENT \"" + se.getName() + "\" für Case # " + c.getId();
			if (r != null) s  += " und Ressource \"" + r.getName() + "\" wurde erzeugt.";
			else s += " wurde erzeugt.";
			protocol.info(s);
		}

		if (r != null) {
			ru.useResource(r);
			r.setLastStartTime(time);
			protocol.info(sim.clckS() + "Ressource \"" + r.getName() + "\" wird an Server \"" + server.getName() + "(" + server.getId() + ")\"  gebunden.");
			protocol.info(sim.clckS() + "Freie Ressourcen: " + ru.printFreeResources());
			protocol.info(sim.clckS() + "Gebundene Ressourcen: " + ru.printUsedResources());
		}

		sim.setTimeOfLastEvent(getTime());
		protocol.info(sim.clckS() + "Zeit des letzten Ereignisses ist " + time);
	}
}
