package org.woped.quantana.sim;

import java.util.ArrayList;

public class SimServerStats {
	protected int zeroDelays;
	protected int calls;
	protected int accesses;
	protected int departures;
	protected double avgQLength;
	protected int maxQLength;
	protected ArrayList<Double> queueProportions;
	protected double avgResNumber;
	protected int maxResNumber;
	protected double avgWaitTime;
	protected double maxWaitTime;
	protected int numServedWhenStopped;
	protected int qLengthWhenStopped;
	protected double avgServTime;
	
	protected String name;
	protected String id;
	SimDistributionLogger distLogger;
	
	public SimServerStats(String name, String id){
		this.name = name;
		this.id = id;
	}

	public int getAccesses() {
		return accesses;
	}

	public void setAccesses(int accesses) {
		this.accesses = accesses;
	}

	public double getAvgQLength() {
		return avgQLength;
	}

	public void setAvgQLength(double avgQLength) {
		this.avgQLength = avgQLength;
	}

	public double getAvgResNumber() {
		return avgResNumber;
	}

	public void setAvgResNumber(double avgResNumber) {
		this.avgResNumber = avgResNumber;
	}

	public double getAvgWaitTime() {
		return avgWaitTime;
	}

	public void setAvgWaitTime(double avgWaitTime) {
		this.avgWaitTime = avgWaitTime;
	}

	public int getCalls() {
		return calls;
	}

	public void setCalls(int calls) {
		this.calls = calls;
	}

	public int getDepartures() {
		return departures;
	}

	public void setDepartures(int departures) {
		this.departures = departures;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMaxQLength() {
		return maxQLength;
	}

	public void setMaxQLength(int maxQLength) {
		this.maxQLength = maxQLength;
	}

	public int getMaxResNumber() {
		return maxResNumber;
	}

	public void setMaxResNumber(int maxResNumber) {
		this.maxResNumber = maxResNumber;
	}

	public double getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(double maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumServedWhenStopped() {
		return numServedWhenStopped;
	}

	public void setNumServedWhenStopped(int numServicedWhenStopped) {
		this.numServedWhenStopped = numServicedWhenStopped;
	}

	public int getQLengthWhenStopped() {
		return qLengthWhenStopped;
	}

	public void setQLengthWhenStopped(int lengthWhenStopped) {
		qLengthWhenStopped = lengthWhenStopped;
	}

	public ArrayList<Double> getQueueProportions() {
		return queueProportions;
	}

	public void setQueueProportions(ArrayList<Double> queueProportions) {
		this.queueProportions = queueProportions;
	}

	public int getZeroDelays() {
		return zeroDelays;
	}

	public void setZeroDelays(int zeroDelays) {
		this.zeroDelays = zeroDelays;
	}

	public double getAvgServTime() {
		return avgServTime;
	}

	public void setAvgServTime(double avgServTime) {
		this.avgServTime = avgServTime;
	}

	public void setDistributionLogger(SimDistributionLogger distLogger) {
		this.distLogger = distLogger;		
	}
	
	public SimDistributionLogger getDistributionLogger() {
		return distLogger;		
	}

}
