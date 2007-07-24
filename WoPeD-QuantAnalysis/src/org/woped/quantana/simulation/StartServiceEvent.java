package org.woped.quantana.simulation;

import java.awt.Color;
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
	private Server s;
	private Case c;
	private Resource r;
	
	public StartServiceEvent(Simulator sim, double time, Activity a) {//Server server, Case c, Resource r){
		super(sim, time);
		act = a;
		this.s = a.getServer();
		this.c = a.getCase();
		this.r = a.getResource();
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		/*ResourceUtilization ru = sim.getResUtil();

		String s = sim.clckS() + ENTRY.getString("Sim.StartService.Info.A");
		if (r != null) s += ENTRY.getString("Sim.StartService.Info.B") + r.getName() + ENTRY.getString("Sim.StartService.Info.C");
		else s += ENTRY.getString("Sim.StartService.Info.D");
		protocol.log(Level.INFO, s, new Object[] {c.getId(), server.getName(), server.getId()});

//		server.updateUtilStats(time, sim.getTimeOfLastEvent());

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
				orig.setTimeService(orig.getTimeService()+st);
				orig.setTimeWait(orig.getTimeWait()+wt);

				sim.getCopiedCasesList().remove(orig.getId());
				orig.setNextServTime(server.getNextServTime());
				double depart = time + orig.getNextServTime();
//				orig.setCurrentDepartureTime(depart);
				
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
			c.setTimeService(c.getTimeService() + c.getNextServTime());
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Time.Wait.Accumulated") + String.format("%,.2f", c.getTimeService()), c.getId());
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Time.Service.Accumulated") + String.format("%,.2f", c.getTimeService()), c.getId());

			c.setNextServTime(server.getNextServTime());
			double depart = time + c.getNextServTime();
//			c.setCurrentDepartureTime(depart);
			
			if (r != null){
				ActivityPanel ap;
				if (c instanceof CaseCopy){
					int oID = ((CaseCopy)c).getOriginal().getId();
					ap = new ActivityPanel(time, depart, server.getName() + " (" + server.getId() + ")", r.getName(), oID, r.getColor());
				} else {
					ap = new ActivityPanel(time, depart, server.getName() + " (" + server.getId() + ")", r.getName(), c.getId(), r.getColor());
				}
				
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
		*/
		
		s.setStatus(Server.STATUS_BUSY);

		s.updRStats(time, 1);
		double wait = time - c.getCurrArrivalTime();
		s.incWaitTime(wait);
		s.getWaitTimes().add(new Double(wait));

		if (wait > s.getMaxWaitTime()){
			s.setMaxWaitTime(wait);
		}

		c.addWaitTime(wait);

		if (r != null){
			r.setLastStartTime(time);
		}

		double serv = s.getNextServTime();
		double depart = time + serv;
		c.setNextServTime(serv);

		if (r != null){
			ActivityPanel ap;
			if (c instanceof CaseCopy){
				int oID = ((CaseCopy)c).getOriginal().getId();
				Color rc = r.getColor();
				Color co = new Color(rc.getRed(), rc.getGreen(), rc.getBlue(), ActivityPanel.AP_ALPHA);
				ap = new ActivityPanel(time, depart, s.getName() + " (" + s.getId() + ")", r.getName(), c, co);
			} else {
				ap = new ActivityPanel(time, depart, s.getName() + " (" + s.getId() + ")", r.getName(), c, r.getColor());
			}

			sim.getActPanelList().add(ap);
		}

		StopServiceEvent sp = new StopServiceEvent(sim, depart, act);
		sim.enroleEvent(sp);

		s.incNumAccess();
		sim.getWd().getTxtArea().append("ST: (Case# " + c.getId() + ", Server: " + s + "): " + time + "\n");
	}
}
