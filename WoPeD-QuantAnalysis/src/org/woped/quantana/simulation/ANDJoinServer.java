package org.woped.quantana.simulation;

import java.util.ArrayList;
import java.util.HashMap;

import org.woped.quantana.gui.ActivityPanel;
import org.woped.quantana.resourcealloc.Resource;

public class ANDJoinServer extends Server {
	
	private HashMap<Case, ArrayList<CaseCopy>> copyList = new HashMap<Case, ArrayList<CaseCopy>>();
	
	private int branches = 0;
	
	public ANDJoinServer(Simulator sim, String id, String name, ProbabilityDistribution dist){
		super(sim, id, name, dist);
	}
	
	public void handleJoin(Simulator sim, double time, Case c, Resource r){
		CaseCopy copy = (CaseCopy)c;
		ArrayList<CaseCopy> list;
		
		Case orig = sim.getCopiedCasesList().get(copy.getOriginal().getId());
		orig.incCpyCnt();
		
		if (branches == 0){
			branches = orig.getCopies();
		}
		
		if (orig.copiesCollected()){
			list = copyList.get(orig);
			int sz = list.size();
			double st = copy.getTimeService();
			
			for (int i = 0; i < sz; i++){
				Case cs = list.get(i);
				st += cs.getTimeService();
			}
			
			st /= orig.getCopies();
			double wt = time - orig.getTimeOfSplit() - st;
			orig.addServiceTime(st);
			orig.addWaitTime(wt);
			getWaitTimes().add(getWaitTimeServer(orig, time));
			sim.getCopiedCasesList().remove(orig.getId());
			orig.setNextServTime(getNextServTime());
			
			double depart = time + orig.getNextServTime();
			if (r != null){
				ActivityPanel ap = new ActivityPanel(time, depart, getName() + " (" + getId() + ")", r.getName(), orig, r.getColor());
				sim.getActPanelList().add(ap);
			}
			
			Activity act = new Activity(orig, this, r);
			StopServiceEvent sp = new StopServiceEvent(sim, time, act);
			sim.enroleEvent(sp);
			updRStats(time, 1);
		} else {
			if (copyList.containsKey(orig)){
				list = copyList.get(orig);
			} else {
				list = new ArrayList<CaseCopy>();
				copyList.put(orig, list);
			}
			
			list.add(copy);
		}
	}

	public HashMap<Case, ArrayList<CaseCopy>> getCopyList() {
		return copyList;
	}

	public void setCopyList(HashMap<Case, ArrayList<CaseCopy>> copyList) {
		this.copyList = copyList;
	}

	public int getBranches() {
		return branches;
	}

	public void setBranches(int branches) {
		this.branches = branches;
	}
	
	private Double getWaitTimeServer(Case o, double time){
		ArrayList<CaseCopy> list = copyList.get(o);
		double at = time;
		for (CaseCopy c : list){
			double t = c.getCurrArrivalTime();
			if (t < at) at = t;
		}
		
		return new Double(time - at);
	}
}
