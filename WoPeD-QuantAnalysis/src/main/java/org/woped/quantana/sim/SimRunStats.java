package org.woped.quantana.sim;

import java.util.HashMap;

import org.woped.quantana.model.ResourceStats;
import org.woped.quantana.resourcealloc.Resource;

public class SimRunStats {
	double duration;
	double procServTime;
	double procWaitTime;
	double procCompTime;
	double throughPut;
	int finishedCases;
	
	HashMap<SimServer, SimServerStats> servStats = new HashMap<SimServer, SimServerStats>();
	HashMap<Resource, ResourceStats> resStats = new HashMap<Resource, ResourceStats>();
	
	public double getDuration() {
		return duration;
	}
	
	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	public int getFinishedCases() {
		return finishedCases;
	}
	
	public void setFinishedCases(int finishedCases) {
		this.finishedCases = finishedCases;
	}
	
	public double getProcCompTime() {
		return procCompTime;
	}
	
	public void setProcCompTime(double procCompTime) {
		this.procCompTime = procCompTime;
	}
	
	public double getProcServTime() {
		return procServTime;
	}
	
	public void setProcServTime(double procServTime) {
		this.procServTime = procServTime;
	}
	
	public double getProcWaitTime() {
		return procWaitTime;
	}
	
	public void setProcWaitTime(double procWaitTime) {
		this.procWaitTime = procWaitTime;
	}
	
	public double getThroughPut() {
		return throughPut;
	}
	
	public void setThroughPut(double throughPut) {
		this.throughPut = throughPut;
	}

	public HashMap<Resource, ResourceStats> getResStats() {
		return resStats;
	}

	public void setResStats(HashMap<Resource, ResourceStats> resStats) {
		this.resStats = resStats;
	}

	public HashMap<SimServer, SimServerStats> getServStats() {
		return servStats;
	}

	public void setServStats(HashMap<SimServer, SimServerStats> servStats) {
		this.servStats = servStats;
	}
}


