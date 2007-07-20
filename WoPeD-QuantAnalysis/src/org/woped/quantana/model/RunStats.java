package org.woped.quantana.model;

import java.util.HashMap;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.simulation.Server;

public class RunStats {
	private double duration;
	private double procServTime;
	private double procWaitTime;
	private double procCompTime;
	private double throughPut;
	private int finishedCases;
	
//	private ArrayList<ServerStats> servStats = new ArrayList<ServerStats>();
//	private ArrayList<ResourceStats> resStats = new ArrayList<ResourceStats>();
	private HashMap<Server, ServerStats> servStats = new HashMap<Server, ServerStats>();
	private HashMap<Resource, ResourceStats> resStats = new HashMap<Resource, ResourceStats>();
	
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
	
	/*public ArrayList<ResourceStats> getResStats() {
		return resStats;
	}
	
	public void setResStats(ArrayList<ResourceStats> resStats) {
		this.resStats = resStats;
	}
	
	public ArrayList<ServerStats> getServStats() {
		return servStats;
	}
	
	public void setServStats(ArrayList<ServerStats> servStats) {
		this.servStats = servStats;
	}*/
	
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

	public HashMap<Server, ServerStats> getServStats() {
		return servStats;
	}

	public void setServStats(HashMap<Server, ServerStats> servStats) {
		this.servStats = servStats;
	}
}
