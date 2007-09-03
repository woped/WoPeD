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
	
	public StartServiceEvent(Simulator sim, double time, Activity a) {
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
		
		String x = sim.clckS() + ENTRY.getString("Sim.StartService.Info.A");
		if (r != null) x += ENTRY.getString("Sim.StartService.Info.B") + r.getName() + ENTRY.getString("Sim.StartService.Info.C");
		else x += ENTRY.getString("Sim.StartService.Info.D");
		protocol.log(Level.INFO, x, new Object[] {c.getId(), s.getName(), s.getId()});
		
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
		
		x = sim.clckS() + ENTRY.getString("Sim.Event.StopService") + sp.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId();
		if (r != null) x  += ENTRY.getString("Sim.Generated.ForResource") + r.getName() + ENTRY.getString("Sim.Event.Generated");
		else x += ENTRY.getString("Sim.Generated");
		protocol.info(x);

		s.incNumAccess();
	}
}
