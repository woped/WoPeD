package org.woped.quantana.sim;

import java.util.ArrayList;

import org.woped.quantana.resourcealloc.Resource;

public class SimStopEvent extends SimulatorEvent{
	
	private SimServer s;
	private SimCase c;
	private Resource r;
	
	public SimStopEvent(SimRunner sim, double time, SimActivity a) {
		super(sim, time);
		this.s = a.getServer();
		this.c = a.getCase();
		this.r = a.getResource();		
	}
	
 	public void invoke(){
		SimRunner sim = getSim();		
		double time = getTime();
		s.updateRunStats(time, -1);		
		if (r != null){
			sim.unbindRes(r);
			r.updStats(time);
		}		
		// try to start servers with the same resource and has queue>1
		// 1 find servers with queue>1
		if(sim.getQueueingServers().size()>0){
			ArrayList<SimServer> al = (ArrayList<SimServer>)sim.getQueueingServers().clone();
			while(al.size()>0){				
				SimServer s=(SimServer)al.get((int)(Math.random()*al.size()));
				s.handleQueue(time);
				al.remove(s);				
			}
			al.size();			
		}	
		SimWorkItem wi = new SimWorkItem(c, s);
		SimDepartureEvent dp = new SimDepartureEvent(sim, time, wi);
		sim.addEvent(dp);		
		s.incDeparture();
		s.incServiceTime(c.getnextSvcTime());		
		c.updateSvcTime();
	}
	
	
}
