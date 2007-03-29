package org.woped.graph;

import java.util.ArrayList;

public class Node {
	public final static int NOT_STARTED	=	1;
	public final static int BUSY		=	2;
	public final static int FINISHED	=	3;
	
	public final static int TYPE_PLACE	=	1;
	public final static int TYPE_TRANS	=	2;
	
	String id;
	String name;
	int markiert;
	int type;
	
	//int dfs;
	//int lowlink;
	boolean isFork = false;
	boolean foundCycle = false;
	//boolean useSum = false;
	
	ArrayList<Arc> successor;
	ArrayList<Arc> predecessor;
	
	double numOfRuns = 0.0;
	//double tempSum = 0.0;
	int iteration = 0;
	//double multiply = 1.0;
	double tempRuns = 0.0;
	boolean andJoin = false;
	boolean joinReached = false;
	
	double time = 0.0;
	int timeUnit = 2;
	
	ArrayList<Arc> tempArcs = new ArrayList<Arc>();
	
	public Node(String id, String s)
	{
		this.id = id;
		name = s;
		successor = new ArrayList<Arc>();
		predecessor = new ArrayList<Arc>();
	}

	public String toString()
	{
		//return " " + id + " " + name + ",";
		return " " + id + ",";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Arc> getSuccessor() {
		return successor;
	}

	public void setSuccessor(ArrayList<Arc> successor) {
		this.successor = successor;
	}

	public ArrayList<Arc> getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(ArrayList<Arc> predecessor) {
		this.predecessor = predecessor;
	}

	public int getMarkiert() {
		return markiert;
	}

	public void setMarkiert(int markiert) {
		this.markiert = markiert;
	}

	public double getNumOfRuns() {
		return numOfRuns;
	}

	public void setNumOfRuns(double numOfRuns) {
		this.numOfRuns = numOfRuns;
	}

	public boolean isFork() {
		return isFork;
	}

	public void setFork(boolean stacked) {
		this.isFork = stacked;
	}

	/*public double getTempSum() {
		return tempSum;
	}

	public void setTempSum(double tempSum) {
		this.tempSum = tempSum;
	}*/

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public boolean isFoundCycle() {
		return foundCycle;
	}

	public void setFoundCycle(boolean foundCycle) {
		this.foundCycle = foundCycle;
	}

	/*public boolean isUseSum() {
		return useSum;
	}

	public void setUseSum(boolean useSum) {
		this.useSum = useSum;
	}*/

	public ArrayList<Arc> getTempArcs() {
		return tempArcs;
	}

	public void setTempArcs(ArrayList<Arc> tempArcs) {
		this.tempArcs = tempArcs;
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	/*public double getMultiply() {
		return multiply;
	}

	public void setMultiply(double multiply) {
		this.multiply = multiply;
	}*/

	public double getTempRuns() {
		return tempRuns;
	}

	public void setTempRuns(double tempRuns) {
		this.tempRuns = tempRuns;
	}

	public boolean isAndJoin() {
		return andJoin;
	}

	public void setAndJoin(boolean andJoin) {
		this.andJoin = andJoin;
	}

	public boolean isJoinReached() {
		return joinReached;
	}

	public void setJoinReached(boolean joinReached) {
		this.joinReached = joinReached;
	}

	public int getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(int timeUnit) {
		this.timeUnit = timeUnit;
	}
}
