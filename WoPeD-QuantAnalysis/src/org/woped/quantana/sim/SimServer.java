package org.woped.quantana.sim;

import java.util.ArrayList;
import java.util.LinkedList;
import org.woped.quantana.resourcealloc.Resource;

import umontreal.iro.lecuyer.rng.MRG32k3a;
import umontreal.iro.lecuyer.rng.RandomStream;

public class SimServer {
	
	int tmpNumCaseParallel = 0;
	int numCaseParallel = 0;
	
	int maxParallel = 0;
	int maxQueueLength = 0;
	int cntDepartue = 0;
	int cntCalls = 0;
	int cnt0Delay = 0;	
	int cntAccess = 0;
	String role = "";
	String group = "";
	
	double servicetime = 0.0;
	double timeLastChange = 0.0;
	double maxWaitTime = 0.0;
	
	
	double timeQueueLastChange = 0.0;
	double timeWait = 0.0;
	

	double avgNumRes = 0.0;
	double queueLength = 0.0;
	SimRunner sim;
	
	ArrayList<Double> rProbs = new ArrayList<Double>();
	ArrayList<Double> qProbs = new ArrayList<Double>();

	SimDistributionLogger distLogger = null;
	
	RandomStream rand;
	SimDistribution dist = null;
	
	String id;
	String name;
	ArrayList<SimOutServer> out = new ArrayList<SimOutServer>();
	LinkedList<SimCase> queue = new LinkedList<SimCase>();	
	
	
	public SimServer(SimRunner sim, String id, String name, String role, String group, SimDistribution dist){
		this.id = id;
		this.sim =sim;
		this.role = role;
		this.group = group;
		this.dist = dist;	
		this.name = name;	
		if(dist!=null) 
			distLogger = new SimDistributionLogger(this.dist.getMean());
		rand = new MRG32k3a();
	}
	
	public String getid(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<SimOutServer> getOutServer(){
		return out;
	}
	
	public void reset(){		
		timeWait = 0;
		tmpNumCaseParallel = 0;
		numCaseParallel = 0;		
		maxParallel = 0;
		cntDepartue = 0;
		cntCalls = 0;
		cnt0Delay = 0;
		queueLength = 0;
		servicetime = 0;
		queue.clear();		
		timeQueueLastChange = 0.0;
		timeLastChange = 0.0;
		maxQueueLength = 0;
		maxWaitTime = 0.0;
		cntAccess = 0;
		rProbs.clear();
		qProbs.clear();	
	}
	
	public void updateRunStats(double time, int type){
		int num = numCaseParallel;
		int max = maxParallel;
		double tnew = (time - timeLastChange)/time;
		avgNumRes = (1 - tnew)*avgNumRes + tnew*num;		
		if (max > 0){
			for (int i = 0; i < max; i++){
				try{
				double p = rProbs.get(i).doubleValue();
				p *= 1-tnew;
				if (num > i){
					p += tnew * num;
				}
				rProbs.set(i, new Double(p));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}		
		timeLastChange = time;		
		if (type > 0){
			if (num >= max){
				maxParallel = num + 1;
				while(rProbs.size()<maxParallel)
					rProbs.add(new Double(0));
			}			
			numCaseParallel++;
		} else {
			numCaseParallel--;
		}
	}
	
	public int getParallels(){
		return numCaseParallel;
	}
	
	public LinkedList<SimCase> getQueue(){
		return queue;
	}
	
	public void incDeparture(){
		cntDepartue++;
	}
	
	public void incCalls(){
		cntCalls++;
	}
	
	public void incServiceTime(double time){
		servicetime+=time;
		if(distLogger!=null)distLogger.addVal(time);
	}
	
	public void setCntTmpCaseParallel(int newVal){
		tmpNumCaseParallel = newVal;
	}
	
	public void incCntTmpCaseParallel(){
		tmpNumCaseParallel++;
	}
	
	public void incZeroDelays(){
		cnt0Delay++;
	}
	
	public void incWaitTime(double addVal){
		timeWait+=addVal;
		maxWaitTime = Math.max(addVal, maxWaitTime);
	}
	
	public boolean hasFreeCapacity(){		
		if (sim.isResUsed()){			
			if (!group.equals("") && !role.equals("")){
				ArrayList<Resource> rl = sim.getResUtil().getFreeResPerGroupRole(group, role);
				return (rl.size()>0);				
			} else return (tmpNumCaseParallel == 0);		
		} else return (tmpNumCaseParallel== 0);
	}
	
	public Resource getResource(){		
		Resource r = sim.getResUtil().chooseResourceFromFreeResources(group, role);
		if (r != null){			
			sim.getResUtil().reserveResource(r);
		}		
		return r;
	}
	
	public double getMaxWaitTime(){
		return maxWaitTime;
	}
	
	public double getNextServTime(){
		if(dist==null) return 0;
		else return dist.getNext();
	}
	
	public void incCntAccess(){
		cntAccess++;
	}
	
	public ArrayList<SimServer> getNextServers(){
		int num = out.size();
		ArrayList<SimServer> list = new ArrayList<SimServer>();
		switch (num){
		case 0:
			list = null;
			break;
		case 1:
			list.add(out.get(0).getserver());
			break;
		default:
			if (this instanceof SimSplitServer){
				for (SimOutServer s : out)
					list.add(s.getserver());
			} else if (this instanceof SimJoinSplitServer){
				for (SimOutServer s : out)
					list.add(s.getserver());
			} else {
				int[][] probs = new int[num][3];
				for (int i = 0; i < num; i++){
					probs[i][0] = i;
					probs[i][SimRunner.MIN_VALUE] = (Double.valueOf(out.get(i).getprobability() * 100)).intValue();
					if (i > 0){
						probs[i][SimRunner.MAX_VALUE] = probs[i][SimRunner.MIN_VALUE] + probs[i-1][SimRunner.MAX_VALUE];
					} else {
						probs[i][SimRunner.MAX_VALUE] = probs[i][SimRunner.MIN_VALUE];
					}
				}
				
				int rnd = rand.nextInt(0, 99);
				int idx = -1;
				for (int i = 0; i < num; i++){
					if (i == 0){
						if (rnd < probs[i][SimRunner.MAX_VALUE]){
							idx = i;
						}
					} else {
						if ((rnd >= probs[i-1][SimRunner.MAX_VALUE]) && (rnd < probs[i][SimRunner.MAX_VALUE])){
							idx = i;
						}
					}
				}
				if(out.get(idx).getserver()==null)
					list = null;
				else list.add(out.get(idx).getserver());
			}
		}
		return list;
	}
	
	SimCase getCaseFromQueue(){
		return (sim.getQueueDiscipline()==SimRunner.Q_FIFO)?queue.removeFirst():queue.removeLast();		
	}
	
	public void handleQueue(double time){
		tmpNumCaseParallel = numCaseParallel;			
		while (hasFreeCapacity() && !queue.isEmpty()){
			Resource r = getResource();
			SimCase c = getCaseFromQueue();
			updateQueue(time,-1);		
			SimActivity act = new SimActivity(c, this, r);
			SimStartEvent st = new SimStartEvent(sim, time, act);
			sim.addEvent(st);				
			tmpNumCaseParallel++;			
		}
		if(queue.isEmpty())
			sim.getQueueingServers().remove(this);
	}		
	
	public void updateQueue(double time, int type){
		int qlen = queue.size();		
		double tnew = (time - timeQueueLastChange)/time;
		queueLength = (1 - tnew)*queueLength + tnew*qlen;
		
		if (maxQueueLength > 0){
			for (int i = 0; i < maxQueueLength; i++){				
				double p = qProbs.get(i).doubleValue();
				p *= 1-tnew;
				if (qlen > i){
					p += tnew * qlen;
				}
				qProbs.set(i, new Double(p));
			}
		}
		
		timeQueueLastChange = time;
		if(time != timeQueueLastChange) maxQueueLength = qlen + 1;		
		if (type > 0){
			if (qlen >= maxQueueLength){
				maxQueueLength = qlen + 1;
				while(maxQueueLength>qProbs.size()){
					qProbs.add(new Double(0));					
				}
			}
		}
	}
	
	public int getCntDeparture(){
		return cntDepartue;
	}
	
	public void setTmpNumCaseParallel(int newVal){
		tmpNumCaseParallel = newVal;
	}
	
	public int getNumCaseInParallel(){
		return numCaseParallel;
	}
	
	public int getZeroDelays(){
		return cnt0Delay;
	}
	
	public int getCntCalls(){
		return cntCalls;
	}
	
	public int getCntAccess(){
		return cntAccess;
	}
	
	public double getQueueLength(){
		return queueLength;
	}
	
	public int getMaxQueueLength(){
		return maxQueueLength;
	}
	
	public ArrayList<Double> getQueueProps(){
		return qProbs;
	}	
	
	public double getAvgNumReserved(){
		return avgNumRes;
	}
	
	public int getMaxParallel(){
		return maxParallel;
	}
	
	public double getWaitTime(){
		return timeWait;
	}
	
	public double getSvcTime(){
		return servicetime; 
	}
	
	public SimDistributionLogger getDistLogger(){
		return distLogger;
	}
}
