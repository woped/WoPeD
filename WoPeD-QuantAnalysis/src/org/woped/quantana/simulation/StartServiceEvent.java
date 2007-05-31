package org.woped.quantana.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.woped.quantana.gui.ActivityPanel;
import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class StartServiceEvent extends SimEvent {
	
	private int type = SimEvent.START_SERVICE_EVENT;
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
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

		String s = sim.clckS() + ENTRY.getString("Sim.StartService.Info.A");
		if (r != null) s += ENTRY.getString("Sim.StartService.Info.B") + r.getName() + ENTRY.getString("Sim.StartService.Info.C");
		else s += ENTRY.getString("Sim.StartService.Info.D");
		protocol.log(Level.INFO, s, new Object[] {c.getId(), server.getName(), server.getId()});

		server.updateUtilStats(time, sim.getTimeOfLastEvent());

		server.setStatus(Server.STATUS_BUSY);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Server.Busy"), new Object[] {server.getName(), server.getId()});

		server.incNumAccess(1);
		
		double wait = time - c.getCurrentArrivalTime();
		server.setWaitTime(server.getWaitTime() + wait);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.StartService.Cases.Serviced") + server.getNumAccess(), new Object[] {server.getName(), server.getId()});

		int cc = server.getQueue().size() + server.getNumCasesInParallel();
		server.setAvgNumCasesServing(server.getAvgNumCasesServing() + cc * (time - sim.getTimeOfLastEvent()));

		server.incNumCasesInParallel(1);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.StartService.Cases.Parallel") + server.getNumCasesInParallel(), new Object[] {server.getName(), server.getId()});

		int par = server.getNumCasesInParallel();
		int max = server.getMaxNumCasesInParallel();
		if (par > max) server.setMaxNumCasesInParallel(par);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.StartService.Cases.Par.Max") + server.getMaxNumCasesInParallel(), new Object[] {server.getName(), server.getId()});

		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.StartService.Time.Wait"), new Object[] {c.getId(), String.format("%,.2f", wait), server.getName(), server.getId()});

		if (wait > server.getMaxWaitTimeOfCase()) server.setMaxWaitTimeOfCase(wait);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.StartService.Time.Wait.Max") + String.format("%,.2f", server.getMaxWaitTimeOfCase()), new Object[] {server.getName(), server.getId()});

		if (server instanceof ANDJoinServer) {
			ANDJoinServer sv = (ANDJoinServer)server;
			CaseCopy copy = (CaseCopy)c;
			Case orig = sim.getCopiedCasesList().get(copy.getOriginal().getId());
			orig.cpyCnt += 1;
			if (sv.getBranches() == 0) sv.setBranches(orig.getCopies());
			if (orig.cpyCnt == orig.getCopies()){
				ArrayList<CaseCopy> list = ((ANDJoinServer)server).getCopyList().get(orig);
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
				
				if (r != null){
					ActivityPanel ap = new ActivityPanel(time, depart, server.getName() + " (" + server.getId() + ")", r.getName(), orig.getId(), r.getColor());
					sim.getActPanelList().add(ap);
				}
				
				protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.StartService.Time.Departure") + String.format("%,.2f", depart), new Object[] {orig.getId(), server.getName(), server.getId()});

				StopServiceEvent se = new StopServiceEvent(sim, depart, new Activity(orig, server, r));
				sim.getEventList().add(se);
				s = sim.clckS() + ENTRY.getString("Sim.Event.StopService") + se.getName() + ENTRY.getString("Sim.Generated.ForCase") + orig.getId();
				if (r != null) s  += ENTRY.getString("Sim.Generated.ForResource") + r.getName() + ENTRY.getString("Sim.Generated.Event.Birth");
				else s += ENTRY.getString("Sim.Generated");
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
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Time.Wait.Accumulated") + String.format("%,.2f", c.getTimeService()), c.getId());
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Time.Service.Accumulated") + String.format("%,.2f", c.getTimeService()), c.getId());

			c.setNextServiceTime(server.getNextServTime());
			double depart = time + c.getNextServiceTime();
			c.setCurrentDepartureTime(depart);
			
			if (r != null){
				ActivityPanel ap = new ActivityPanel(time, depart, server.getName() + " (" + server.getId() + ")", r.getName(), c.getId(), r.getColor());
				sim.getActPanelList().add(ap);
			}
			
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.StartService.Time.Departure") + String.format("%,.2f", depart), new Object[] {c.getId(), server.getName(), server.getId()});

			StopServiceEvent se = new StopServiceEvent(sim, depart, act);
			sim.getEventList().add(se);
			s = sim.clckS() + ENTRY.getString("Sim.Event.StopService") + se.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId();
			if (r != null) s  += ENTRY.getString("Sim.Generated.ForResource") + r.getName() + ENTRY.getString("Sim.Event.Generated");
			else s += ENTRY.getString("Sim.Generated");
			protocol.info(s);
		}

		sim.setTimeOfLastEvent(getTime());
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Time.LastEvent") + String.format("%,.2f", time));
	}
}
