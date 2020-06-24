package org.woped.quantana.sim;

import java.util.HashMap;

import org.woped.quantana.resourcealloc.Resource;


public class SimJoinServer extends SimServer {
	HashMap<Integer, WaitList> waitingList = new HashMap<Integer, WaitList>();	
		
	public SimJoinServer(SimRunner sim, String id, String name, String role, String group, SimDistribution dist){
		super(sim,id,name,role,group,dist);		
	}
	
	public void handleJoin(SimRunner sim, double time, SimCaseCopy copy){
		WaitList wl = waitingList.get(copy.getOriginal().getid());		
		wl.incDone();
		if(wl.allDone()){			
			incCalls();
			// calculate value for origin remove all out of waiting list and start else put into queue
			double svcTime = wl.getSvcTime();
			copy.getOriginal().addSvcTime(svcTime);
			copy.getOriginal().addWaitTime(wl.getWaitTime());			
			waitingList.remove(copy.getOriginal().getid());		
			copy.getOriginal().setcurrArrivalTime(time);
			setTmpNumCaseParallel(this.getNumCaseInParallel());
			copy.getOriginal().setjoinFinished(true);			
			if (hasFreeCapacity()){
				Resource r = getResource();
				sim.bindRes(r);
				SimActivity act = new SimActivity(copy.getOriginal(), this, r);
				SimStartEvent sta = new SimStartEvent(sim, time, act);
				sim.addEvent(sta);
				incZeroDelays();
				incCntTmpCaseParallel();							
			} else {				
				getQueue().add(copy.getOriginal());
				updateQueue(time,1);
				sim.addQueueServer(this);
			}
		}
	}	
	
	public void reset(){
		waitingList.clear();		
		super.reset();
	}
	
	class WaitList {
		int done = 0;
		HashMap<Integer,SimCaseCopy> items = new HashMap<Integer,SimCaseCopy>();
		
		public HashMap<Integer,SimCaseCopy> getItems(){
			return items;
		}
		
		public void addItem(SimCaseCopy cc){
			items.put(cc.getid(), cc);
		}
		
		public boolean allDone(){
			return (done == items.size()); 
		}
		public void incDone(){
			++done;
		}
		
		public double getSvcTime(){						
			// get the maximum from the several copied cases			
			double res = 0.0;
			Object[] ol = items.values().toArray();			
			for(int i=0;i<(ol.length-1);i++){
				SimCaseCopy s = (SimCaseCopy)ol[i];
				SimCaseCopy s1 = (SimCaseCopy)ol[i+1];
				res=Math.max(s1.getsvcTime(), s.getsvcTime());
			}
			return res;
		}
		
		public double getWaitTime(){						
			// get the maximum from the several copied cases			
			double res = 0.0;
			Object[] ol = items.values().toArray();			
			for(int i=0;i<(ol.length-1);i++){
				SimCaseCopy s = (SimCaseCopy)ol[i];
				SimCaseCopy s1 = (SimCaseCopy)ol[i+1];
				res=Math.max(s1.getwaitTime(), s.getwaitTime());
			}
			return res;
		}		
	}
	
	public void addWaitingCase(int originId,SimCaseCopy cc){		
		if(waitingList.get(originId)==null){
			waitingList.put(originId, new WaitList());
		}		
		if(waitingList.get(originId).getItems().get(cc.getid())==null){
			waitingList.get(originId).addItem(cc);
		}		
	}
	
}
