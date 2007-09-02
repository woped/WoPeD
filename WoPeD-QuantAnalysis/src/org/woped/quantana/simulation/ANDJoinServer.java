package org.woped.quantana.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.woped.quantana.resourcealloc.Resource;

public class ANDJoinServer extends Server {
	
	private HashMap<Case, ArrayList<CaseCopy>> copyList = new HashMap<Case, ArrayList<CaseCopy>>();
	
	private int branches = 0;

//	private HashMap<Integer,Case> queue = new HashMap<Integer,Case>();
	private HashMap<Integer,Case> caseList = new HashMap<Integer,Case>();
	
	public ANDJoinServer(Simulator sim, String id, String name, ProbabilityDistribution dist){
		super(sim, id, name, dist);
	}
	
	public void handleJoin(Simulator sim, double time, CaseCopy copy){
		ArrayList<CaseCopy> list;

		Case orig = sim.getOrig(copy);
		if (orig != null){
			sim.removeOrig(orig);
			updQStats(time, 1);
			enqueue(orig);
			orig.setCurrArrivalTime(time);
			orig.incCpyCnt();
			incNumCalls();
			if (branches == 0){
				branches = orig.getCopies();
			}
			list = new ArrayList<CaseCopy>();
			list.add(copy);
			copyList.put(orig, list);
		} else {
			orig = caseList.get(copy.getOriginal().getId());
			orig.incCpyCnt();
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

//				orig.setNextServTime(getNextServTime());
//				double depart = time + orig.getNextServTime();
				
				setTmpNumCParallel(this.getNumCasesInParallel());

				if (hasFreeCapacity()){
					Resource r = getResource();
					sim.bind(r);
					Activity act = new Activity(orig, this, r);
					StartServiceEvent ste = new StartServiceEvent(sim, time, act);
					sim.enroleEvent(ste);
					incZeroDelays();
					incTmpNumCParallel();
					
					updQStats(time, 1);
					caseList.remove(orig.getId());
					orig.setJoinFinished(true);
				} else {
					
				}
			} else {
				list = copyList.get(orig);
				list.add(copy);
			}
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
	
	/*private Double getWaitTimeServer(Case o, double time){
		ArrayList<CaseCopy> list = copyList.get(o);
		double at = time;
		for (CaseCopy c : list){
			double t = c.getCurrArrivalTime();
			if (t < at) at = t;
		}
		
		return new Double(time - at);
	}*/
	
	public void enqueue(Case c){
		caseList.put(c.getId(), c);
	}
	
	public void dequeueAJ(Simulator sim, double time){
		Iterator<Case> it = caseList.values().iterator();
		setTmpNumCParallel(getNumCasesInParallel());
		while(it.hasNext()){
			Case c = it.next();
			if (c.isJoinFinished()){
				boolean cap = hasFreeCapacity();
				if (cap){
					Resource r = getResource();
					updQStats(time, -1);
					caseList.remove(c.getId());
					Activity act = new Activity(c, this, r);
					StartServiceEvent st = new StartServiceEvent(sim, time, act);
					sim.enroleEvent(st);
					incTmpNumCParallel();
				}
			}
		}
	}
	
	public int getAJQueueLength(){
		return caseList.size();
	}
}
