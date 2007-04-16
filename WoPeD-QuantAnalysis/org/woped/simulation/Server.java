package org.woped.simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Server {
	public static final int STATUS_IDLE	= 1;
	public static final int STATUS_BUSY = 2;
	
	private String id;
	private String name;
	private int numCalls = 0;
	private double busy = 0.0;				// B(t)
	private double queueLen = 0.0;			// Q(t)
	private double maxWaitTimeOfCase = 0.0;
	private int maxQueueLength = 0;
	private int zeroDelays = 0;
	private int numAccess = 0;
	private int numDeparture = 0;
	private int numCasesInParallel = 0;
	private int maxNumCasesInParallel = 0;
	private int status = 0;
	private ProbabilityDistribution distribution;
	private Case curCase = null;
	private LinkedList<Case> queue = new LinkedList<Case>();
	private ArrayList<SuccServer> successor = new ArrayList<SuccServer>();
	private Random choice;
	
	public Server(String id, String name, ProbabilityDistribution dist){
		this.id = id;
		this.name = name;
		this.choice = new Random();
		this.distribution = dist;
	}

	public double getBusy() {
		return busy;
	}

	public void setBusy(double busy) {
		this.busy = busy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumCalls() {
		return numCalls;
	}

	public void setNumCalls(int numCalls) {
		this.numCalls = numCalls;
	}

	public Case getCurCase() {
		return curCase;
	}

	public void setCurCase(Case curCase) {
		this.curCase = curCase;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public LinkedList<Case> getQueue() {
		return queue;
	}

	public void setQueue(LinkedList<Case> queue) {
		this.queue = queue;
	}

	public ArrayList<SuccServer> getSuccessor() {
		return successor;
	}

	public void setSuccessor(ArrayList<SuccServer> successor) {
		this.successor = successor;
	}
	
	public Server gotoNextServer(){
		int num = this.successor.size();
		
		switch (num){
		case 0:
			return null;
			
		case 1:
			return this.successor.get(0).getServer();
			
		default:
			int[][] probs = new int[num][3];
			for (int i = 0; i < num; i++){
				probs[i][0] = i;
				probs[i][1] = (Double.valueOf(successor.get(i).getProbability() * 100)).intValue();
				if (i > 0){
					probs[i][2] = probs[i][1] + probs[i-1][2];
				} else {
					probs[i][2] = probs[i][1];
				}
			}
			
			int rnd = choice.nextInt(100);
			int idx = -1;
			for (int i = 0; i < num; i++){
				if (i == 0){
					if (rnd < probs[i][2]){
						idx = i;
					}
				} else {
					if ((rnd >= probs[i-1][2]) && (rnd < probs[i][2])){
						idx = i;
					}
				}
			}
			
			return successor.get(idx).getServer();
		}
	}

	public double getNextServTime(){
		return distribution.getNextRandomValue();
	}
	
	/*public void deliverNewCase(Case c){
		if (status == Server.STATUS_IDLE){
			setCurCase(c);
			setStatus(Server.STATUS_BUSY);
		} else {
			getQueue().add(c);
		}
	}*/
	
	public String toString(){
		return name + "(" + id + ")";
	}

	public int getMaxNumCasesInParallel() {
		return maxNumCasesInParallel;
	}

	public void setMaxNumCasesInParallel(int maxNumCasesInParallel) {
		this.maxNumCasesInParallel = maxNumCasesInParallel;
	}

	public int getMaxQueueLength() {
		return maxQueueLength;
	}

	public void setMaxQueueLength(int maxQueueLength) {
		this.maxQueueLength = maxQueueLength;
	}

	public double getMaxWaitTimeOfCase() {
		return maxWaitTimeOfCase;
	}

	public void setMaxWaitTimeOfCase(double maxWaitTimeOfCase) {
		this.maxWaitTimeOfCase = maxWaitTimeOfCase;
	}

	public int getNumAccess() {
		return numAccess;
	}

	public void setNumAccess(int numAccess) {
		this.numAccess = numAccess;
	}

	public int getNumDeparture() {
		return numDeparture;
	}

	public void setNumDeparture(int numDeparture) {
		this.numDeparture = numDeparture;
	}

	public double getQueueLen() {
		return queueLen;
	}

	public void setQueueLen(double queueLen) {
		this.queueLen = queueLen;
	}

	public int getZeroDelays() {
		return zeroDelays;
	}

	public void setZeroDelays(int zeroDelays) {
		this.zeroDelays = zeroDelays;
	}

	public String getName() {
		return name;
	}
	
	public void incNumCalls(int n){
		numCalls += n;
	}
	
	public void incZeroDelays(int n){
		zeroDelays += n;
	}
	
	public void incNumAccess(int n){
		numAccess += n;
	}
	
	public void incNumDeparture(int n){
		numDeparture += n;
	}

	public int getNumCasesInParallel() {
		return numCasesInParallel;
	}

	public void setNumCasesInParallel(int numCasesInParallel) {
		this.numCasesInParallel = numCasesInParallel;
	}
	
	public void incNumCasesInParallel(int n){
		numCasesInParallel += n;
	}
}

