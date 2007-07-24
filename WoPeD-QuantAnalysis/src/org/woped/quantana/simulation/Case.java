package org.woped.quantana.simulation;

public class Case {
	private int id;
	private double timeService = 0.0;
	private double timeWait = 0.0;
	private double sysArrivalTime = 0.0;
//	private double sysDepartureTime = 0.0;
	private double currArrivalTime = 0.0;
//	private double currDepartureTime = 0.0;
	private double nextServTime = 0.0;
	private double timeOfSplit = 0.0;
	
	private boolean joinFinished = true;
	
	private int copies = 0;
	private int cpyCnt = 0;
	
	public Case(int id){
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getTimeService() {
		return timeService;
	}

	public void setTimeService(double timeService) {
		this.timeService = timeService;
	}

	public double getTimeWait() {
		return timeWait;
	}

	public void setTimeWait(double timeWait) {
		this.timeWait = timeWait;
	}

	public double getSysArrivalTime() {
		return sysArrivalTime;
	}

	public void setSysArrivalTime(double sysArrivalTime) {
		this.sysArrivalTime = sysArrivalTime;
//		this.currArrivalTime = sysArrivalTime;
	}

	/*public double getSysDepartureTime() {
		return sysDepartureTime;
	}

	public void setSysDepartureTime(double sysDepartureTime) {
		this.sysDepartureTime = sysDepartureTime;
	}*/

	public double getCurrArrivalTime() {
		return currArrivalTime;
	}

	public void setCurrArrivalTime(double currArrivalTime) {
		this.currArrivalTime = currArrivalTime;
	}

	public double getNextServTime() {
		return nextServTime;
	}

	public void setNextServTime(double nextServTime) {
		this.nextServTime = nextServTime;
	}

	/*public double getCurrentDepartureTime() {
		return currDepartureTime;
	}

	public void setCurrentDepartureTime(double currentDepartureTime) {
		this.currDepartureTime = currentDepartureTime;
	}*/

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}

	public double getTimeOfSplit() {
		return timeOfSplit;
	}

	public void setTimeOfSplit(double timeOfSplit) {
		this.timeOfSplit = timeOfSplit;
	}
	
	public void updServTime(){
		timeService += nextServTime;
	}

	public int getCpyCnt() {
		return cpyCnt;
	}

	public void setCpyCnt(int cpyCnt) {
		this.cpyCnt = cpyCnt;
	}
	
	public void addWaitTime(double time){
		timeWait += time;
	}
	
	public void addServiceTime(double time){
		timeService += time;
	}
	
	public void incCpyCnt(){
		cpyCnt++;
	}
	
	public boolean copiesCollected(){
		return cpyCnt == copies;
	}

	public boolean isJoinFinished() {
		return joinFinished;
	}

	public void setJoinFinished(boolean joinFinished) {
		this.joinFinished = joinFinished;
	}
}
