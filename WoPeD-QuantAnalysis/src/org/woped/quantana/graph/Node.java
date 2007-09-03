package org.woped.quantana.graph;

import java.util.ArrayList;

public class Node {
	public final static int NOT_STARTED	=	1;
	public final static int BUSY		=	2;
	public final static int FINISHED	=	3;
	
	public final static int TYPE_PLACE			=	1;
	public final static int TYPE_TRANS_SIMPLE	=	2;
	public final static int TYPE_AND_JOIN		=	3;
	public final static int TYPE_AND_SPLIT		=	4;
	public final static int TYPE_XOR_JOIN		=	5;
	public final static int TYPE_XOR_SPLIT		=	6;
	public final static int TYPE_SUBP			=	9;
	
	private String id;
	private String name;
	private int markiert;
	private int type;
	
	private boolean isFork = false;
	private boolean foundCycle = false;
	
	private ArrayList<Arc> successor;
	private ArrayList<Arc> predecessor;
	
	private double numOfRuns = 0.0;
	private int iteration = 0;
	private double tempRuns = 0.0;
	private boolean andJoin = false;
	private boolean andSplit = false;
	private boolean joinReached = false;
	
	private double time = 0.0;
	private int timeUnit = 2;
	
	private String role = "none";
	private String group = "none";
	
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
	
	public void incIteration(){
		this.iteration++;
	}

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

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getGroupRole(){
		return getGroup() + " / " + getRole();
	}

	public boolean isAndSplit() {
		return andSplit;
	}

	public void setAndSplit(boolean andSplit) {
		this.andSplit = andSplit;
	}
	
	public boolean isTransition(){
		return (type == TYPE_TRANS_SIMPLE 
				|| type == TYPE_AND_JOIN 
				|| type == TYPE_AND_SPLIT 
				|| type == TYPE_XOR_JOIN
				|| type == TYPE_XOR_SPLIT
				|| type == TYPE_SUBP);
	}
}
