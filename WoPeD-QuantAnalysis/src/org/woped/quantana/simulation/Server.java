package org.woped.quantana.simulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.woped.quantana.resourcealloc.Resource;

public class Server {
	public static final int STATUS_IDLE	= 1;
	public static final int STATUS_BUSY = 2;
	
	private Logger protocol;
	
	private String id;
	private String name;
	private Simulator sim;
	private int numCalls = 0;
//	private double busy = 0.0;				// B(t)
	private double avgNumRes = 0.0;
	private double queueLength = 0.0;			// Q(t)
	private double maxWaitTime = 0.0;
	private double waitTime = 0.0;
	private double serviceTime = 0.0;
	private double avgNumCasesServing = 0.0;
	private double avgNumCasesAtServer = 0.0;
	private int maxQLength = 0;
	private int zeroDelays = 0;
	private int numAccess = 0;
	private int numDeparture = 0;
	private int numCasesInParallel = 0;
	private int maxNumCasesInParallel = 0;
	private int status = 0;
	private ProbabilityDistribution distribution;
	private LinkedList<Case> queue = new LinkedList<Case>();
//	private LinkedList<WorkItem> queue = new LinkedList<WorkItem>();
	private ArrayList<SuccServer> successor = new ArrayList<SuccServer>();
	private Random choice;
	
	private String role = null;
	private String group = null;
	
//	private int lastNumCasesInParallel;  // Anzahl gerade bedienter Fälle, als Sim stoppte
//	private int lastQueueLength;		 // Anzahl Fälle in Queue, als Simulation stoppte
	private ArrayList<Double> waitTimes; // = new ArrayList<Double>(); // speichert die Wartezeiten aller Fälle
	private int tmpNumCParallel;
	private double timeQueueChangedLast = 0.0;
	private double timeNumResChangedLast = 0.0;
	private ArrayList<Double> qProps; // = new ArrayList<Double>();
	private ArrayList<Double> rProps; // = new ArrayList<Double>();
	
	private LinkedList<Resource> reservedResources = new LinkedList<Resource>();
	
	private static final ResourceBundle ENTRY = Simulator.getENTRY();
	
	public Server(Simulator sim, String id, String name, ProbabilityDistribution dist){
		this.id = id;
		this.name = name;
		this.sim = sim;
		this.choice = new Random();
		this.distribution = dist;
		protocol = sim.getProtocol();
	}

	/*public double getBusy() {
		return busy;
	}

	public void setBusy(double busy) {
		this.busy = busy;
	}*/

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

	/*public LinkedList<WorkItem> getQueue() {
		return queue;
	}

	public void setQueue(LinkedList<WorkItem> queue) {
		this.queue = queue;
	}*/
	
	public LinkedList<Case> getQueue() {
		return queue;
	}

	/*public void setQueue(LinkedList<Case> queue) {
		this.queue = queue;
	}*/

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
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Server.Time.Service") + String.format("%,.2f", time), new Object[] {name, id});
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

	public int getMaxQLength() {
		return maxQLength;
	}

	public void setMaxQLength(int maxQueueLength) {
		this.maxQLength = maxQueueLength;
	}

	public double getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(double maxWaitTimeOfCase) {
		this.maxWaitTime = maxWaitTimeOfCase;
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

	public double getQueueLength() {
		return queueLength;
	}

	public void setQueueLength(double queueLen) {
		this.queueLength = queueLen;
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
	
	public void incNumCalls(){
		numCalls++;
	}
	
	public void incZeroDelays(){
		zeroDelays++;
	}
	
	public void incNumAccess(){
		numAccess++;
	}
	
	public void incNumDeparture(){
		numDeparture++;
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
	
	public void incWaitTime(double time){
		waitTime += time;
	}
	
	public boolean hasFreeCapacity(){
		/*if (sim.getUseResAlloc() == Simulator.RES_NOT_USED){
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
		}*/
		
		boolean free;
		
		if (sim.getUseResAlloc() == Simulator.RES_USED){
			boolean g = group.equals("");
			boolean r = role.equals("");
			
			if (!g && !r){
				ArrayList<Resource> rl = sim.getResUtil().getFreeResPerGroupRole(group, role);
				
				if (rl.size() > 0) free = true;
				else free = false;
			} else {
				free = (tmpNumCParallel == 0);
			}
		} else {
			free = (tmpNumCParallel == 0);
		}
		
		return free;
	}
	
	/*public boolean isIdle(){
		if (status == Server.STATUS_IDLE){
			return true;
		} else {
			return false;
		}
	}*/

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
	
	/*public void enqueue(WorkItem wi){
		queue.add(wi);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Queue.Enqueue"), new Object[] {wi.getCase().getId(), name, id});
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Queue.Content") + printQueue(), new Object[] {name, id});
		
		//int l = queue.size();
		//if (l > maxQueueLength) maxQueueLength += 1;
		protocol.info(sim.clckS() + ENTRY.getString("Sim.Queue.Length.Max") + maxQueueLength);
		
		
	}*/
	
	public void enqueue(Case c){
		queue.add(c);
	}
	
	/*public WorkItem dequeue(){
		int qDisc = sim.getQueueDiscipline();
		WorkItem wi;
		
		if (qDisc == Simulator.QD_FIFO) wi = queue.removeFirst();
		else wi = queue.removeLast();
		
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Queue.Dequeued"), new Object[] {wi.getCase().getId(), name, id});
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Queue.Content") + printQueue(), new Object[] {name, id});
		
		return wi;
	}*/
	
	public Case dequeue(){
		int qDisc = sim.getQueueDiscipline();
		Case c;
		
		if (qDisc == Simulator.QD_FIFO) c = queue.removeFirst();
		else c = queue.removeLast();
		
		return c;
	}
	
	/*public void updateUtilStats(double now, double lastEvent){
		queueLen += queue.size() * (now - lastEvent);
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Queue.Length.Average") + String.format("%,.2f", (queueLen / sim.getClock())), new Object[] {name, id});
		
		avgNumCasesServing += numCasesInParallel * (now - lastEvent);
		
		avgNumCasesAtServer += (numCasesInParallel + queue.size()) * (now - lastEvent);
		
		if (status == STATUS_BUSY) busy += now - lastEvent;
		protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Server.Time.Service.Average") + String.format("%,.2f", (busy / sim.getClock())), new Object[] {name, id});
	}*/
	
	public void reset(){
		status = STATUS_IDLE;
		
//		busy = 0.0;
		maxNumCasesInParallel = 0;
		maxQLength = 0;
		maxWaitTime = 0.0;
		numAccess = 0;
		numCalls = 0;
		numCasesInParallel = 0;
		numDeparture = 0;
		queueLength = 0.0;
		avgNumRes = 0.0;
		waitTime = 0.0;
		serviceTime = 0.0;
		avgNumCasesServing = 0.0;
		avgNumCasesAtServer = 0.0;
		timeNumResChangedLast = 0.0;
		timeQueueChangedLast = 0.0;
		zeroDelays = 0;
		
		queue.clear();
		reservedResources.clear();
		
		qProps = new ArrayList<Double>();
		rProps = new ArrayList<Double>();
		waitTimes = new ArrayList<Double>();
	}
	
	public String printQueue(){
		int l = queue.size();
		String s = "[";
		
		if (l > 1){
			for (int i = 0; i < l - 1; i++) {
//				s += queue.get(i).getCase().getId() + ",";
				s += queue.get(i).getId() + ",";
			}
		}
		
		if (l > 0) //s += queue.get(l - 1).getCase().getId();
			s += queue.get(l - 1).getId();
		
		s += "]";
		
		return s;
	}

	public double getAvgNumCasesServing() {
		return avgNumCasesServing;
	}

	public void setAvgNumCasesServing(double avgNumCasesServing) {
		this.avgNumCasesServing = avgNumCasesServing;
	}

	public double getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
	}
	
	public void incServiceTime(double time){
		serviceTime += time;
	}

	/*public void doService(){
		
	}*/
	
	/*public void updStatistics(int type){
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
	}*/

	public double getAvgNumCasesAtServer() {
		return avgNumCasesAtServer;
	}

	public void setAvgNumCasesAtServer(double avgNumCasesAtServer) {
		this.avgNumCasesAtServer = avgNumCasesAtServer;
	}

	/*public int getLastNumCasesInParallel() {
		return lastNumCasesInParallel;
	}

	public void setLastNumCasesInParallel(int lastNumCasesInParallel) {
		this.lastNumCasesInParallel = lastNumCasesInParallel;
	}

	public int getLastQueueLength() {
		return lastQueueLength;
	}

	public void setLastQueueLength(int lastQueueLength) {
		this.lastQueueLength = lastQueueLength;
	}*/

	public ArrayList<Double> getWaitTimes() {
		return waitTimes;
	}

	public void setWaitTimes(ArrayList<Double> waitTimes) {
		this.waitTimes = waitTimes;
	}

	public int getTmpNumCParallel() {
		return tmpNumCParallel;
	}

	public void setTmpNumCParallel(int tmpNumCParallel) {
		this.tmpNumCParallel = tmpNumCParallel;
	}
	
	public void incTmpNumCParallel(){
		tmpNumCParallel++;
	}
	
	public Resource getResource(){
		Resource r = sim.getResUtil().chooseResourceFromFreeResources(group, role);
		if (r != null){
			sim.reserveResource(this, r);
		}
		
		return r;
	}
	
	public void updQStats(double time, int type){
		int qlen; // = queue.size();
		if (this instanceof ANDJoinServer){
			qlen = ((ANDJoinServer)this).getAJQueueLength();
		} else {
			qlen = this.queue.size();
		}
		double tnew = (time - timeQueueChangedLast)/time;
		queueLength = (1 - tnew)*queueLength + tnew*qlen;
		
		if (maxQLength > 0){
			for (int i = 0; i < maxQLength; i++){
				double p = qProps.get(i).doubleValue();
				p *= 1-tnew;

				if (qlen > i){
					p += tnew * qlen;
				}

				qProps.set(i, new Double(p));
			}
		}
		
		timeQueueChangedLast = time;
		
		if (type > 0){
			if (qlen >= maxQLength){
				maxQLength = qlen + 1;
				qProps.add(new Double(0));
			}
		}
	}
	
	public void updRStats(double time, int type){
		int num = numCasesInParallel;
		int max = maxNumCasesInParallel;
		double tnew = (time - timeNumResChangedLast)/time;
		avgNumRes = (1 - tnew)*avgNumRes + tnew*num;
		
		if (max > 0){
			for (int i = 0; i < max; i++){
				double p = rProps.get(i).doubleValue();
				p *= 1-tnew;

				if (num > i){
					p += tnew * num;
				}

				rProps.set(i, new Double(p));
			}
		}
		
		timeNumResChangedLast = time;
		
		if (type > 0){
			if (num >= max){
				maxNumCasesInParallel = num + 1;
				rProps.add(new Double(0));
			}
			
			numCasesInParallel++;
		} else {
			numCasesInParallel--;
		}
	}
	
	public void handleFIFO(double time){
		tmpNumCParallel = numCasesInParallel;
		boolean cap = hasFreeCapacity();
		boolean qe = queue.isEmpty();
		updQStats(time, -1);
		
		while (cap && !qe){
			Resource r = getResource();
			Case c; // = dequeue();
			
			if (this instanceof ANDJoinServer){
				ANDJoinServer aj = (ANDJoinServer)this;
				aj.dequeueAJ(sim, time);
			} else {
				c = dequeue();
				Activity act = new Activity(c, this, r);
				StartServiceEvent st = new StartServiceEvent(sim, time, act);
				sim.enroleEvent(st);
				protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Event.StartService") + st.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId() + ENTRY.getString("Sim.StopService.ForServer"), new Object[] {this.getName(), this.getId()});
				tmpNumCParallel++;
				cap = hasFreeCapacity();
				qe = queue.isEmpty();
			}
		}
	}
	
	public void handleLIFO(double time){
		tmpNumCParallel = numCasesInParallel;
		boolean cap = hasFreeCapacity();
		
		if (cap){
			Iterator<SimEvent> it = sim.getEventListIterator();
			
			while (it.hasNext()){
				SimEvent next = it.next();
				
				if (next instanceof ArrivalEvent){
					double t = next.getTime();
					
					if (t <= time){
						it.remove();
						next.invoke();
						tmpNumCParallel++;
					}
				}
			}
		}
		
		cap = hasFreeCapacity();
		boolean qe = queue.isEmpty();
		updQStats(time, -1);
		
		while (cap && !qe){
			Resource r = getResource();
			Case c; // = dequeue();

			if (this instanceof ANDJoinServer){
				ANDJoinServer aj = (ANDJoinServer)this;
				aj.dequeueAJ(sim, time);
			} else {
				c = dequeue();
				Activity act = new Activity(c, this, r);
				StartServiceEvent st = new StartServiceEvent(sim, time, act);
				sim.enroleEvent(st);
				protocol.log(Level.INFO, sim.clckS() + ENTRY.getString("Sim.Event.StartService") + st.getName() + ENTRY.getString("Sim.Generated.ForCase") + c.getId() + ENTRY.getString("Sim.StopService.ForServer"), new Object[] {this.getName(), this.getId()});
				tmpNumCParallel++;
				cap = hasFreeCapacity();
				qe = queue.isEmpty();
			}
		}
	}

	public double getAvgNumRes() {
		return avgNumRes;
	}

	public void setAvgNumRes(double avgNumRes) {
		this.avgNumRes = avgNumRes;
	}

	public ArrayList<Double> getQProps() {
		return qProps;
	}

	public ArrayList<Double> getRProps() {
		return rProps;
	}
	
	public void addReservedResource(Resource r){
		reservedResources.add(r);
	}
	
	public Resource useReservedResource(){
		return reservedResources.remove();
	}
}
