package org.woped.quantana.sim;


import org.woped.quantana.resourcealloc.Resource;

public class SimArriveEvent extends SimulatorEvent{
	
	SimServer s;
	SimCase c;
	
	public SimArriveEvent(SimRunner sim, double time, SimWorkItem wi) {
		super(sim, time);
		s = wi.getServer();
		c = wi.getCase();
		sim.incCntArrival();		
	}
	
	public void invoke(){
		SimRunner sim = getSim();
		double time = getTime();	
		if (c instanceof SimCaseCopy)
			getSim().addLog( ((SimCaseCopy)c).getorigid(),s.getName(),time);
		else
			getSim().addLog(c.getid(),s.getName(),time);
		
		if (s instanceof SimJoinServer){
			((SimJoinServer)s).handleJoin(sim, time, (SimCaseCopy)c);
			SimBirthEvent be = new SimBirthEvent(sim, time);
			sim.addEvent(be);
		} else if (s instanceof SimJoinSplitServer){
			((SimJoinSplitServer)s).handleJoin(sim, time, (SimCaseCopy)c);
			SimBirthEvent be = new SimBirthEvent(sim, time);
			sim.addEvent(be);
		} else {
			c.setcurrArrivalTime(time);
			s.incCalls();
			SimBirthEvent be = new SimBirthEvent(sim, time);
			sim.addEvent(be);
			s.setTmpNumCaseParallel(s.getNumCaseInParallel());
			if (s.hasFreeCapacity()){
				Resource r = s.getResource();
				sim.bindRes(r);
				SimActivity act = new SimActivity(c, s, r);
				SimStartEvent st = new SimStartEvent(sim, time, act);
				sim.addEvent(st);				
				s.incZeroDelays();				
				s.incCntTmpCaseParallel();
			} else {
				s.getQueue().add(c);	
				s.updateQueue(time,1);
				sim.addQueueServer(s);
			}
		}
		sim.decCntArrival();
	}

}
