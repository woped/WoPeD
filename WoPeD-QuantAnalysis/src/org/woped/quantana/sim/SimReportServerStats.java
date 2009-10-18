package org.woped.quantana.sim;

public class SimReportServerStats extends SimServerStats {
	double avgZeroDelays = 0.0;
	double avgCalls = 0.0;
	double avgAccesses = 0.0;
	double avgDepartures = 0.0;
	double avgMaxQLength = 0.0;
	double avgMaxResNumber = 0.0;
	double avgNumServedWhenStopped = 0.0;
	double avgQLengthWhenStopped = 0.0;
	
	public SimReportServerStats(String name, String id){
		super(name, id);
	}

	public double getAvgAccesses() {
		return avgAccesses;
	}

	public void setAvgAccesses(double avgAccesses) {
		this.avgAccesses = avgAccesses;
	}

	public double getAvgCalls() {
		return avgCalls;
	}

	public void setAvgCalls(double avgCalls) {
		this.avgCalls = avgCalls;
	}

	public double getAvgDepartures() {
		return avgDepartures;
	}

	public void setAvgDepartures(double avgDepartures) {
		this.avgDepartures = avgDepartures;
	}

	public double getAvgMaxQLength() {
		return avgMaxQLength;
	}

	public void setAvgMaxQLength(double avgMaxQLength) {
		this.avgMaxQLength = avgMaxQLength;
	}

	public double getAvgMaxResNumber() {
		return avgMaxResNumber;
	}

	public void setAvgMaxResNumber(double avgMaxResNumber) {
		this.avgMaxResNumber = avgMaxResNumber;
	}

	public double getAvgNumServedWhenStopped() {
		return avgNumServedWhenStopped;
	}

	public void setAvgNumServedWhenStopped(double avgNumServedWhenStopped) {
		this.avgNumServedWhenStopped = avgNumServedWhenStopped;
	}

	public double getAvgQLengthWhenStopped() {
		return avgQLengthWhenStopped;
	}

	public void setAvgQLengthWhenStopped(double avgQLengthWhenStopped) {
		this.avgQLengthWhenStopped = avgQLengthWhenStopped;
	}

	public double getAvgZeroDelays() {
		return avgZeroDelays;
	}

	public void setAvgZeroDelays(double avgZeroDelays) {
		this.avgZeroDelays = avgZeroDelays;
	}
	
	public void incAvgZeroDelays(double addVal){
		avgZeroDelays += addVal;
	}
	
	public void incAvgCalls(double addVal){
		avgCalls += addVal;
	}
	
	public void incAvgAccesses(double addVal){
		avgAccesses += addVal;
	}
	
	public void incAvgDepartures(double addVal){
		avgDepartures += addVal;
	}
	
	public void incAvgMaxQLength(double addVal){
		avgMaxQLength += addVal;
	}
	
	public void incAvgMaxResNumber(double addVal){
		avgMaxResNumber += addVal;
	}
	
	public void incAvgNumServedWhenStopped(double addVal){
		avgNumServedWhenStopped += addVal;
	}
	
	public void incAvgQLengthWhenStopped(double addVal){
		avgQLengthWhenStopped += addVal;
	}
	
	public void incAvgAvgQLength(double addVal){
		avgQLength += addVal;
	}
	
	public void incAvgAvgResNumber(double addVal){
		avgResNumber += addVal;
	}
	
	public void incAvgAvgWaitTime(double addVal){
		avgWaitTime += addVal;
	}
	
	public void incAvgMaxWaitTime(double addVal){
		maxWaitTime += addVal;
	}
	
	public void incAvgServTime(double addVal){
		avgServTime += addVal;
	}
}
