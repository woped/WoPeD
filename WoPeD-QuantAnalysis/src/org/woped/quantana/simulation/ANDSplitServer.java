package org.woped.quantana.simulation;

import java.util.ArrayList;

public class ANDSplitServer extends Server {
	
	public ANDSplitServer(Simulator sim, String id, String name, ProbabilityDistribution dist){
		super(sim, id, name, dist);
	}
	
	public void handleSplit(Simulator sim, double time, Case c, ArrayList<Server> slist){
		sim.addToCopyList(c);
		c.setJoinFinished(false);
		int copies = slist.size();
		c.setCopies(copies);
		c.setCpyCnt(0);
		c.setTimeOfSplit(time);
		CaseGenerator cg = sim.getCaseGenerator();
		
		for (int i = 0; i < copies; i++){
			Server s = slist.get(i);
			int cnt = cg.getCaseCount() + 1;
			cg.setCaseCount(cnt);
			CaseCopy cc = new CaseCopy(cnt, c);
			ArrivalEvent ae = new ArrivalEvent(sim, time, new WorkItem(cc, s));
			sim.enroleEvent(ae);
		}
	}
}
