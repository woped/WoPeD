package org.woped.quantana.simulation;

public class Case {
	private int id;
	private double timeService = 0.0;
	private double timeWait = 0.0;
	private double sysArrivalTime = 0.0;
	private double sysDepartureTime = 0.0;
	private double currentArrivalTime = 0.0;
	private double currentDepartureTime = 0.0;
	private double nextServiceTime = 0.0;
	
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
		this.currentArrivalTime = sysArrivalTime;
	}

	public double getSysDepartureTime() {
		return sysDepartureTime;
	}

	public void setSysDepartureTime(double sysDepartureTime) {
		this.sysDepartureTime = sysDepartureTime;
	}

	public double getCurrentArrivalTime() {
		return currentArrivalTime;
	}

	public void setCurrentArrivalTime(double currentArrivalTime) {
		this.currentArrivalTime = currentArrivalTime;
	}

	public double getNextServiceTime() {
		return nextServiceTime;
	}

	public void setNextServiceTime(double nextServiceTime) {
		this.nextServiceTime = nextServiceTime;
	}

	public double getCurrentDepartureTime() {
		return currentDepartureTime;
	}

	public void setCurrentDepartureTime(double currentDepartureTime) {
		this.currentDepartureTime = currentDepartureTime;
	}
}
