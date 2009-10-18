package org.woped.quantana.sim;

public class SimCaseMaker {
	SimRunner sim = null;
	SimDistribution distrib = null;
	int cntCase = 0;
	double lastArrivalTime = 0.0;
	SimDistributionLogger distLogger;	
	
	public SimCaseMaker(SimRunner sim){
		this.sim = sim;
	}
	
	public void setDistribution(SimDistribution distrib){
		this.distrib = distrib;
	}
	
	public void reset(){
		cntCase = 0;
		lastArrivalTime = 0.0;
	}
	
	public SimCase getCase(){
		SimCase next = new SimCase(++cntCase);
		double newVal = distrib.getNext();
		distLogger.addVal(newVal);
		lastArrivalTime += newVal;		
		next.setsysArrivalTime(lastArrivalTime);
		return next;
	}
	
	public void setCnt(int newVal){
		cntCase = newVal;
	}
	
	public int getCnt(){
		return cntCase;
	}
	
	public int getNextId(){
		return ++cntCase;
	}

	public void setDistLogger(SimDistributionLogger distLogger) {
		this.distLogger = distLogger;		
	}
	
	

}
