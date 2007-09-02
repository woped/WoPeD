package org.woped.quantana.simulation;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

@SuppressWarnings("unused")
public class DepartureEvent extends SimEvent {
	
	private int type = SimEvent.DEPARTURE_EVENT;
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
	private WorkItem wi;
	private Server s;
	private Case c;
	
	public DepartureEvent(Simulator sim, double time, WorkItem wi) {//Server server, Case c){
		super(sim, time);
		this.wi = wi;
		this.s = wi.getServer();
		this.c = wi.getCase();
		
		setName(getNewName());
	}
	
	public void invoke(){
		Simulator sim = getSim();
		double time = getTime();
		/*ResourceUtilization ru = sim.getResUtil();
		
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Departure.Info"), new Object[] {c.getId(), server.getName(), server.getId()});
		
//		server.updateUtilStats(time, sim.getTimeOfLastEvent());
		
		if (!(server.getQueue().isEmpty())){
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Queue.Content") + server.printQueue(), new Object[] {server.getName(), server.getId()});
			Case c2 = server.dequeue().getCase();
//			protocol.info(sim.clckS() + ENTRY.getString("Case # " + c2.getId() + " wurde aus Warteschlange entfernt.");
			
			if (sim.getUseResAlloc() == Simulator.RES_NOT_USED){
				if (server.isIdle())
					nextStartServiceEvent(sim, time, c2, ru);
			} else {
				if (server.hasFreeCapacity())
					nextStartServiceEvent(sim, time, c2, ru);
			}
		}
		
		ArrayList<Server> nextServer = server.gotoNextServer();
		if (nextServer == null){
			DeathEvent de = new DeathEvent(sim, time, c);
			sim.getEventList().add(de);
//			protocol.info(sim.clckS() + ENTRY.getString("Case # " + c.getId() + " verl‰ﬂt den Prozess.");
			protocol.info(sim.clckS() + ENTRY.getString("Sim.Event.Death") + de.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId() + ENTRY.getString("Sim.Generated"));
		} else {
			if (server instanceof ANDSplitServer){
				sim.getCopiedCasesList().put(c.getId(), c);
				int copies = nextServer.size();
				c.setCopies(copies);
				c.cpyCnt = 0;
				c.setTimeOfSplit(time);
				CaseGenerator cg = sim.getCaseGenerator();
				for (Server s : nextServer){
					int count = cg.getCaseCount() + 1;
					cg.setCaseCount(count);
					Case cc = new CaseCopy(count, c);

					ArrivalEvent ae = new ArrivalEvent(sim, time, new WorkItem(cc, s));
					sim.getEventList().add(ae);
					protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Departure.Convey"), new Object[] {cc.getId(), s.getName(), s.getId()});
					protocol.info(sim.clckS() + ENTRY.getString("Sim.Event.Arrival") + ae.getName() + ENTRY.getString("Sim.Generated.ForCase") + cc.getId() + ENTRY.getString("Sim.Generated"));
				}
			} else {
				Server s = nextServer.get(0);
				ArrivalEvent ae = new ArrivalEvent(sim, time, new WorkItem(c, s));
				sim.getEventList().add(ae);
				protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Departure.Convey"), new Object[] {c.getId(), s.getName(), s.getId()});
				protocol.info(sim.clckS() + ENTRY.getString("Sim.Event.Arrival") + ae.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId() + ENTRY.getString("Sim.Generated"));
			}
		}
		
		server.incNumDeparture(1);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.StartService.Cases.Serviced") + server.getNumDeparture(), new Object[] {server.getName(), server.getId()});
		
		sim.setTimeOfLastEvent(time);
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Time.LastEvent") + String.format("%,.2f", time));
	}
	
	private void nextStartServiceEvent(Simulator sim, double time, Case c, ResourceUtilization ru){
		Resource r = sim.getResUtil().chooseResourceFromFreeResources(server.getGroup(), server.getRole());
		
//		if (sim.getUseResAlloc() == Simulator.RES_USED && r == null) return;
		if (r != null) {
			ru.useResource(r);
			protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Arrival.Resource.Bind"), new Object[] {r.getName(), server.getName(), server.getId()});
			protocol.info(sim.clckS() + ENTRY.getString("Sim.Resources.Free") + ru.printFreeResources());
			protocol.info(sim.clckS() + ENTRY.getString("Sim.Resources.Used") + ru.printUsedResources());
		}
		
		Activity act = new Activity(wi, r);
		StartServiceEvent se = new StartServiceEvent(sim, time, act);
		sim.getEventList().add(se);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Event.StartService") + se.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId() + ENTRY.getString("Sim.StopService.ForServer"), new Object[] {server.getName(), server.getId()});
		*/
		
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Departure.Info"), new Object[] {c.getId(), s.getName(), s.getId()});
		
		ArrayList<Server> slist = s.gotoNextServer();
		
		if (slist == null){
			DeathEvent de = new DeathEvent(sim, time, c);
			sim.enroleEvent(de);
			protocol.info(sim.clckS() + ENTRY.getString("Sim.Event.Death") + de.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId() + ENTRY.getString("Sim.Generated"));
		} else {
			if (s instanceof ANDSplitServer){
				ANDSplitServer as = (ANDSplitServer)s;
				as.handleSplit(sim, time, c, slist);
			} else {
				Server s_ = slist.get(0);
				WorkItem wi_ = new WorkItem(c, s_);
				ArrivalEvent ae = new ArrivalEvent(sim, time, wi_);
				sim.enroleEvent(ae);
				protocol.info(sim.clckS() + ENTRY.getString("Sim.Event.Arrival") + ae.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId() + ENTRY.getString("Sim.Generated"));
			}
		}
		
		int qd = sim.getQueueDiscipline();
		
		if (qd == Simulator.QD_FIFO)
			s.handleFIFO(time);
		else
			s.handleLIFO(time);
	}
}
