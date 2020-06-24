package org.woped.quantana.sim;

import java.awt.Color;

import org.woped.quantana.gui.ActivityPanel;
import org.woped.quantana.resourcealloc.Resource;

public class SimStartEvent extends SimulatorEvent{
	
	SimActivity act;
	SimServer s;
	SimCase c;
	Resource r;
	
	public SimStartEvent(SimRunner sim, double time, SimActivity a) {
		super(sim, time);
		act = a;
		s = a.getServer();
		c = a.getCase();
		r = a.getResource();	
	}
		
	public void invoke(){
		double serv = s.getNextServTime();
		c.setnextSvcTime(serv);
		SimRunner sim = getSim();
		double time = getTime();
		s.updateRunStats(time, 1);
		double wait = time - c.getcurrArrivalTime();		
		
		s.incWaitTime(wait);
		c.addWaitTime(wait);			
		if (r != null){
			r.setLastStartTime(time);
            ActivityPanel ap;
            if (c instanceof SimCaseCopy){
                  ((SimCaseCopy)c).getOriginal().getid();
                  Color rc = r.getColor();
                  Color co = new Color(rc.getRed(), rc.getGreen(), rc.getBlue(), ActivityPanel.AP_ALPHA);
                  ap = new ActivityPanel(time, (time + serv), s.getName() + " (" + s.getid() + ")", r.getName(), c, co);
            } else {
                  ap = new ActivityPanel(time, (time + serv), s.getName() + " (" + s.getid() + ")", r.getName(), c, r.getColor());
            }
            sim.getActPanelList().add(ap);
            if (c instanceof SimCaseCopy) 
            	sim.addLog(r.getName(), s.getName(), ((SimCaseCopy)c).getorigid(), serv, wait, time);
            else
            	sim.addLog(r.getName(), s.getName(), c.getid(), serv, wait, time);            	
		}
		
		SimStopEvent sp = new SimStopEvent(sim, (time + serv), act);
		sim.addEvent(sp);
		s.incCntAccess();
	}	
	
}
