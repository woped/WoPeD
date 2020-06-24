package org.woped.quantana.model;

public class ResourceStats {
	private double idleTime;
	private double utilizationRatio;
	
	private String name;
	
	public ResourceStats(String name){
		this.name = name;
	}

	public double getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(double idleTime) {
		this.idleTime = idleTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getUtilizationRatio() {
		return utilizationRatio;
	}

	public void setUtilizationRatio(double utilizationsRatio) {
		this.utilizationRatio = utilizationsRatio;
	}
	
	public void incIdleTime(double v){
		idleTime += v;
	}
	
	public void incUtilizationRatio(double v){
		utilizationRatio += v;
	}
}
