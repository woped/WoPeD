package org.woped.quantana.sim;

public class SimCase {	
	private int id = 0;	
	private boolean joinFinished = true;	
	private double nextSvcTime = 0.0;
	private double timeService = 0.0;
	private double timeOfSplit = 0.0;
	private double sysArrivalTime = 0.0;
	private double currArrivalTime = 0.0;
	private double waitTime = 0.0;
	
	public SimCase(int id){
		this.id = id;
	}
	
	public double getnextSvcTime(){
		return nextSvcTime;
	}
	
	public void setnextSvcTime(double newVal){
		nextSvcTime = newVal;
	}
	
	public void updateSvcTime(){
		timeService+=nextSvcTime;
	}
	
	public void addSvcTime(double addVal){
		timeService+=addVal;
	}
	
	public void addWaitTime(double addVal){
		waitTime+=addVal;		
	}
	
	public double getsvcTime(){
		return timeService;
	}	
	
	public double getsplitTime(){
		return timeOfSplit;
	}
	
	public void setsplitTime(double newVal){
		timeOfSplit = newVal;
	}
	
	public void setsysArrivalTime(double newVal){
		sysArrivalTime = newVal;
	}

	public double getsysArrivalTime(){
		return sysArrivalTime;
	}
	
	public double getcurrArrivalTime() {
		return currArrivalTime;
	}

	public void setcurrArrivalTime(double newVal) {
		currArrivalTime = newVal;
	}
	
	public int getid(){
		return id;
	}
		
	public void setjoinFinished(boolean newVal){
		joinFinished = newVal;
	}
	
	public boolean getjoinFinished(){
		return joinFinished;
	}
	
	public double getwaitTime(){
		return waitTime;
	}
}
