package org.woped.quantana.sim;

import java.util.ArrayList;

public class SimSplitServer extends SimServer{
	
	SimServer sv_join = null;
	
	public SimSplitServer(SimRunner sim, String id, String name, String role, String group, SimDistribution dist){
		super(sim,id,name,role,group,dist);		
	}
	
	public void findJoin(){		
		ArrayList<SimServer> innerSplitt = new ArrayList<SimServer>();
		SimOutServer o =this.out.get(0);
		while(sv_join ==null){
			if(o.server instanceof SimSplitServer){
				innerSplitt.add(o.server);				
			} else if (o.server instanceof SimJoinSplitServer) {
				if (innerSplitt.isEmpty()){
					sv_join = o.server;
					break;
				}
				else{
					innerSplitt.remove(0);					
					innerSplitt.add(o.server);				
				}								
			} else if(o.server instanceof SimJoinServer){
				if (innerSplitt.isEmpty()){
					sv_join = o.server;
					break;
				}
				else{
					innerSplitt.remove(0);					
				}				
			}
			// get next server
			o = o.server.out.get(0);			
		}		
	}	
	
	public void handleSplit(SimRunner sim, double time, SimCase c, ArrayList<SimServer> slist){		
		c.setjoinFinished(false);
		c.setsplitTime(time);
		c.setcurrArrivalTime(time);		
		while(slist.size()>0){			
			int idx = (int)(Math.random()*slist.size());
			SimServer s = slist.get(idx);
			SimCaseCopy cc = new SimCaseCopy(sim.getCaseMaker().getNextId(),c);
			cc.setcurrArrivalTime(time);	
			if (sv_join instanceof SimJoinServer)
				((SimJoinServer)sv_join).addWaitingCase(c.getid(),cc);			
			if (sv_join instanceof SimJoinSplitServer)
				((SimJoinSplitServer)sv_join).addWaitingCase(c.getid(),cc);			
			SimWorkItem wi = new SimWorkItem(cc,s);
			SimArriveEvent ae = new SimArriveEvent(sim, time, wi);			
			sim.addEvent(ae);			
			slist.remove(s);
		}
	}
}
