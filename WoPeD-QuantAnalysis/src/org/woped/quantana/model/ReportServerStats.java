package org.woped.quantana.model;

public class ReportServerStats extends ServerStats {
	private double avgZeroDelays;
	private double avgCalls;
	private double avgAccesses;
	private double avgDepartures;
	private double avgMaxQLength;
	private double avgMaxResNumber;
	private double avgNumServedWhenStopped;
	private double avgQLengthWhenStopped;
	
	public ReportServerStats(String name, String id){
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
	
	public void incAvgZeroDelays(double v){
		avgZeroDelays += v;
	}
	
	public void incAvgCalls(double v){
		avgCalls += v;
	}
	
	public void incAvgAccesses(double v){
		avgAccesses += v;
	}
	
	public void incAvgDepartures(double v){
		avgDepartures += v;
	}
	
	public void incAvgMaxQLength(double v){
		avgMaxQLength += v;
	}
	
	public void incAvgMaxResNumber(double v){
		avgMaxResNumber += v;
	}
	
	public void incAvgNumServedWhenStopped(double v){
		avgNumServedWhenStopped += v;
	}
	
	public void incAvgQLengthWhenStopped(double v){
		avgQLengthWhenStopped += v;
	}
	
	public void incAvgAvgQLength(double v){
		avgQLength += v;
	}
	
	public void incAvgAvgResNumber(double v){
		avgResNumber += v;
	}
	
	public void incAvgAvgWaitTime(double v){
		avgWaitTime += v;
	}
	
	public void incAvgMaxWaitTime(double v){
		maxWaitTime += v;
	}
	
	public void incAvgServTime(double v){
		avgServTime += v;
	}
}
