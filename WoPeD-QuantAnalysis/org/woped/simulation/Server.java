package org.woped.simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import flanagan.math.PsRandom;

public class Server {
	public static final int STATUS_IDLE	= 1;
	public static final int STATUS_BUSY = 2;
	
//	public static final int DIST_TYPE_UNIFORM	= 1;
//	public static final int DIST_TYPE_EXP		= 2;
//	public static final int DIST_TYPE_GAUSS		= 3;
	
	private String id;
	private String name;
	private int numCalls = 0;
	private double busy = 0.0;
	private double curServTime = 0.0;
	private int maxQueueLength = 0;
	private int status = 0;
	private ProbabilityDistribution distribution;
//	private int typeOfDist = 0;
	//private int curCase = 0;
	private Case curCase = null;
	//private LinkedList<ArrivalEvent> queue = new LinkedList<ArrivalEvent>();
	private LinkedList<Case> queue = new LinkedList<Case>();
	private ArrayList<SuccServer> successor = new ArrayList<SuccServer>();
	private Random choice;
//	private PsRandom generator;
//	private long seed = 0;
	
	public Server(String id, String name, ProbabilityDistribution dist){
		this.id = id;
		this.name = name;
//		this.seed = seed;
		this.choice = new Random();
//		this.generator = new PsRandom(seed);
//		this.typeOfDist = type;
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

	/*public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}*/
	
	public double getNextServTime(){
		/*double time = 0;
		switch (typeOfDist){
		case DIST_TYPE_UNIFORM:
			time = generator.nextDouble();
			break;
		case DIST_TYPE_GAUSS:
			time = generator.nextGaussian(0, 0);
			break;
		default:
			time = generator.nextExponential(0, 0);
		}
		
		return time;*/
		return distribution.getNextRandomValue();
	}
	
	public void deliverNewCase(Case c){
		if (status == Server.STATUS_IDLE){
			setCurCase(c);
			setStatus(Server.STATUS_BUSY);
		} else {
			getQueue().add(c);
		}
	}
	
	public String toString(){
		return name + "(" + id + ")";
	}
}

