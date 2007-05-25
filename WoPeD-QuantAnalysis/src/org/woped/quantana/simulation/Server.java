package org.woped.quantana.simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;

import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceUtilization;

public class Server {
	public static final int STATUS_IDLE	= 1;
	public static final int STATUS_BUSY = 2;
	
	private Logger protocol;
	
	private String id;
	private String name;
	private Simulator sim;
	private int numCalls = 0;
	private double busy = 0.0;				// B(t)
	private double queueLen = 0.0;			// Q(t)
	private double maxWaitTimeOfCase = 0.0;
	private double waitTime = 0.0;
	private double avgServiceTime = 0.0;
	private double avgNumCasesServing = 0.0;
	private double avgNumCasesAtServer = 0.0;
	private int maxQueueLength = 0;
	private int zeroDelays = 0;
	private int numAccess = 0;
	private int numDeparture = 0;
	private int numCasesInParallel = 0;
	private int maxNumCasesInParallel = 0;
	private int status = 0;
	private ProbabilityDistribution distribution;
//	private LinkedList<Case> queue = new LinkedList<Case>();
	private LinkedList<WorkItem> queue = new LinkedList<WorkItem>();
	private ArrayList<SuccServer> successor = new ArrayList<SuccServer>();
	private Random choice;
	
	private String role = null;
	private String group = null;
	
//	private int type;
	
	public Server(Simulator sim, String id, String name, ProbabilityDistribution dist){
		this.id = id;
		this.name = name;
		this.sim = sim;
		this.choice = new Random();
		this.distribution = dist;
		protocol = sim.getProtocol();
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public LinkedList<WorkItem> getQueue() {
		return queue;
	}

	public void setQueue(LinkedList<WorkItem> queue) {
		this.queue = queue;
	}

	public ArrayList<SuccServer> getSuccessor() {
		return successor;
	}

	public void setSuccessor(ArrayList<SuccServer> successor) {
		this.successor = successor;
	}
	
	public ArrayList<Server> gotoNextServer(){
		int num = this.successor.size();
		ArrayList<Server> list = new ArrayList<Server>();

		switch (num){
		case 0:
			list = null;
			break;
		case 1:
			list.add(this.successor.get(0).getServer());
			break;
		default:
			if (this instanceof ANDSplitServer){
				for (SuccServer s : successor)
					list.add(s.getServer());
			} else {
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

				list.add(successor.get(idx).getServer());
			}
		}

		return list;
	}

	public double getNextServTime(){
		double time = distribution.getNextRandomValue();
		protocol.info(sim.clckS() + "(Server \"" + name + "(" + id + ")\")Bedienzeit: " + String.format("%,.2f", time));
		return time;
	}
	
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

	public double getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(double waitTime) {
		this.waitTime = waitTime;
	}
	
	public boolean hasFreeCapacity(){
		if (sim.getUseResAlloc() == Simulator.RES_NOT_USED){
			if (status == Server.STATUS_IDLE) return true;
			else return false;
		} else {
			ResourceUtilization ru = sim.getResUtil();
			if ((!group.equals("")) && (!role.equals(""))) {
				ArrayList<Resource> r = ru.getFreeResPerGroupRole(group, role);
				if (r.size() > 0) return true;
				else return false;
			} else {
				return true;
			}
		}
	}
	
	public boolean isIdle(){
		if (status == Server.STATUS_IDLE){
			return true;
		} else {
			return false;
		}
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
	
	public void enqueue(WorkItem wi){
		queue.add(wi);
		protocol.info(sim.clckS() + "Case # " + wi.get_case().getId() + " zur Warteschlange von \"" + name + "(" + id + ")\" hinzugefügt.");
		protocol.info(sim.clckS() + "Warteschlange von Server \"" + name + "(" + id + ")\" : " + printQueue());
		
		int l = queue.size();
		if (l > maxQueueLength) maxQueueLength += 1;
		protocol.info(sim.clckS() + "Maximale Länge der Warteschlange bisher ist " + maxQueueLength);
		
		
	}
	
	public WorkItem dequeue(){
		int qDisc = sim.getQueueDiscipline();
		WorkItem wi;
		
		if (qDisc == Simulator.QD_FIFO) wi = queue.removeFirst();
		else wi = queue.removeLast();
		
		protocol.info(sim.clckS() + "Case # " + wi.get_case().getId() + " wurde aus Warteschlange von Server \"" + name + "(" + id + ")\" entfernt.");
		protocol.info(sim.clckS() + "Warteschlange von Server \"" + name + "(" + id + ")\" : " + printQueue());
		
		return wi;
	}
	
	public void updateUtilStats(double now, double lastEvent){
		queueLen += queue.size() * (now - lastEvent);
		protocol.info(sim.clckS() + "Durchschnittliche Warteschlangenlänge von Server \"" + name + "(" + id + ")\" bisher ist: " + String.format("%,.2f", (queueLen / sim.getClock())));
		
		avgNumCasesServing += numCasesInParallel * (now - lastEvent);
		
		avgNumCasesAtServer += (numCasesInParallel + queue.size()) * (now - lastEvent);
		
		if (status == STATUS_BUSY) busy += now - lastEvent;
		protocol.info(sim.clckS() + "Durchschnittliche Bedienzeit von Server \"" + name + "(" + id + ")\" bisher ist: " + String.format("%,.2f", (busy / sim.getClock())));
	}
	
	public void reset(){
		status = STATUS_IDLE;
		
		busy = 0.0;
		maxNumCasesInParallel = 0;
		maxQueueLength = 0;
		maxWaitTimeOfCase = 0.0;
		numAccess = 0;
		numCalls = 0;
		numCasesInParallel = 0;
		numDeparture = 0;
		queueLen = 0.0;
		waitTime = 0.0;
		avgServiceTime = 0.0;
		avgNumCasesServing = 0.0;
		avgNumCasesAtServer = 0.0;
		
		queue.clear();
	}
	
	public String printQueue(){
		int l = queue.size();
		String s = "[";
		
		if (l > 1){
			for (int i = 0; i < l - 1; i++) {
				s += queue.get(i).get_case().getId() + ",";
			}
		}
		
		if (l > 0) s += queue.get(l - 1).get_case().getId();
		
		s += "]";
		
		return s;
	}

	public double getAvgNumCasesServing() {
		return avgNumCasesServing;
	}

	public void setAvgNumCasesServing(double avgNumCasesServing) {
		this.avgNumCasesServing = avgNumCasesServing;
	}

	public double getAvgServiceTime() {
		return avgServiceTime;
	}

	public void setAvgServiceTime(double avgServiceTime) {
		this.avgServiceTime = avgServiceTime;
	}

	public void doService(){
		
	}
	
	public void updStatistics(int type){
		switch (type) {
		case SimEvent.BIRTH_EVENT:
			// Nichts zu tun
			break;
		case SimEvent.ARRIVAL_EVENT:
			
			break;
		case SimEvent.START_SERVICE_EVENT:
			
			break;
		case SimEvent.STOP_SERVICE_EVENT:
			
			break;
		case SimEvent.RESOURCE_FREED_EVENT:
			
			break;
		case SimEvent.DEPARTURE_EVENT:
			
			break;
		case SimEvent.DEATH_EVENT:
			 // Nichts zu tun
			break;
		default:
		}
	}

	public double getAvgNumCasesAtServer() {
		return avgNumCasesAtServer;
	}

	public void setAvgNumCasesAtServer(double avgNumCasesAtServer) {
		this.avgNumCasesAtServer = avgNumCasesAtServer;
	}
}
