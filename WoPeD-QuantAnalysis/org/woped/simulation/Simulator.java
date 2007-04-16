package org.woped.simulation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

import org.woped.graph.Arc;
import org.woped.graph.Node;
import org.woped.graph.WorkflowNetGraph;
import org.woped.quantanalysis.SimParameters;
import org.woped.resourcealloc.ResourceAllocation;
import org.woped.resourcealloc.ResourceUtilization;
import org.woped.simulation.output.SimOutputDialog;

public class Simulator {
	
	public static final int QD_FIFO	= 1;
	public static final int QD_LIFO = 2;
	
	public static final int STOP_NONE			= 0;
	public static final int STOP_CASE_DRIVEN	= 1;
	public static final int STOP_TIME_DRIVEN	= 2;
	public static final int STOP_BOTH			= 3;
	
	public static final int RES_USED		= 1;
	public static final int RES_NOT_USED	= 2;
	
//	private static final double VERY_LATE_TIME	= Double.POSITIVE_INFINITY;
	
	private WorkflowNetGraph process;
	private ResourceAllocation resAlloc;
	private ResourceUtilization resUtil;
	private int useResAlloc;
	private CaseGenerator caseGenerator;
	private int numRuns;
	private int countCaseFinished;
	private double clock;
	private int numCasesInSystem = 0;
	private int maxNumCasesInSystem = 0;
	private int caseNo = 0;
	private long seed = 0;
	private int typeOfDistForCases = 0;
	private int typeOfDistForServer = 0;
//	private double servParam1 = 0.0;
//	private double servParam2 = 0.0;
	private double caseParam1 = 0.0;
	private double caseParam2 = 0.0;
	private int queueDiscipline = 0;
	private int stopRule = 0;
	private double lambda = 1.0;
	private double timeOfPeriod = 8.0;
//	private Server firstServer
	private Random fstServChoice = new Random(new Date().getTime());
	private int[][] fstServList;
	
	private SimEvent nextEvent = null;
	//private ArrayList<Server> serverList = new ArrayList<Server>();
	private HashMap<String, Server> serverList = new HashMap<String, Server>();
	//private ArrayList<SimEvent> eventList = new ArrayList<SimEvent>();
	private PriorityQueue<SimEvent> eventList = new PriorityQueue<SimEvent>();
	private static ArrayList<ProtocolItem> protocol = new ArrayList<ProtocolItem>();
	private HashMap<Integer, Case> caseList	 = new HashMap<Integer, Case>();
	
//	public static boolean stopped = false;
	
	
	public Simulator(WorkflowNetGraph wfpn, ResourceUtilization ru, SimParameters sp){
		process = wfpn;
		resUtil = ru;
		resAlloc = ru.getResAlloc();
		
		this.numRuns = sp.getRuns();
		this.typeOfDistForCases = sp.getDistCases();
		this.typeOfDistForServer = sp.getDistServ();
		this.caseParam1 = sp.getCPara1();
		this.caseParam2 = sp.getCPara2();
//		this.servParam1 = sp.getSPara1();
//		this.servParam2 = sp.getSPara2();
		this.queueDiscipline = sp.getQueue();
		this.stopRule = sp.getStop();
		this.lambda = sp.getLambda();
		this.timeOfPeriod = sp.getTimeOfPeriod();
		this.useResAlloc = sp.getResUse();
		
//		generateServerList();
//		printServerList(); // <---------
		
		getFstServList();
	}
	
	public void start() {
		for (int i = 0; i < numRuns; i++){
			ProtocolItem pi = new ProtocolItem(this);
			String init = "simulation protocol\n run #: " + i + "\n\n";
			pi.setTime(0.0);
			pi.setDescription(init);
			protocolUpdate(pi);
			
			init();
			
			while (!shouldStopNow()){
				timing();
				nextEvent.invoke();
			}
			
			generateReport();
		}
	}
	
	private void init(){
		ProtocolItem pi = new ProtocolItem(this);
		pi.setTime(clock);
		String description = "Initialization started:\n";
		
		clock = 0.0;
		caseNo = 0;
		description += "Clock set to Zero.\n";
		description += "System starts 'empty and idle'.\n";
		
		generateServerList();
		description += "List of Servers generated.\n";
		
		initEventList();
		description += "List of Events initialized.\n";
		description += "Initialization finished. System is ready.\n\n";
		
		pi.setDescription(description);
		
		protocolUpdate(pi);
	}
	
	private void timing(){
		ProtocolItem pi = new ProtocolItem(this);
		
		// nextEvent bestimmen
		nextEvent = eventList.remove();
		
		// Systemuhr setzen
		clock += nextEvent.getMoment();
		
		// neuen Case erzeugen
		if (nextEvent instanceof ArrivalEvent){
			generateNextCase();
		}
		
		protocolUpdate(pi);
	}
	
	private void generateReport(){
		SimOutputDialog sod = new SimOutputDialog(null, true, this);
//		sod.setAlwaysOnTop(true);
		sod.setVisible(true);
	}
	
	public void protocolUpdate(ProtocolItem pi){
		protocol.add(pi);
	}
	
	private void generateServerList(){ // <--------  private Methode !!!
		Node[] nodes = process.getNodeArray();
		for (int i = 0; i < nodes.length; i++){
			String id = nodes[i].getId();
			String name = nodes[i].getName();
			double t = nodes[i].getTime();
			if (process.isTransition(id)){
				Server s = new Server(id, name, new ProbabilityDistribution(typeOfDistForServer, 1/t, 1.0, ++seed));
				s.setStatus(Server.STATUS_IDLE);
				serverList.put(id, s);
			}
		}
		
		for (Server s : serverList.values()){
			String id = s.getId();
			int idx = process.getNodeIdx(id);
			Node n = (process.getNodeArray())[idx];
			for (Arc a : n.getSuccessor()){
				String id2 = a.getTarget().getId();
				int idx2 = process.getNodeIdx(id2);
				Node m = (process.getNodeArray())[idx2];
				for (Arc b : m.getSuccessor()){
					s.getSuccessor().add(new SuccServer(serverList.get(b.getTarget().getId()), a.getProbability() * b.getProbability()));
				}
			}
		}
	}
	
	private void initEventList(){
		caseGenerator = new CaseGenerator(new ProbabilityDistribution(typeOfDistForCases, caseParam1, caseParam2, ++seed), this);
//		DepartureEvent unreachableDepartureEvent = new DepartureEvent(this, getStartServer(), VERY_LATE_TIME, 0);
//		eventList.add(unreachableDepartureEvent);
		
		generateNextCase();
	}

	public HashMap<String, Server> getServerList() {
		return serverList;
	}

	public void setServerList(HashMap<String, Server> serverList) {
		this.serverList = serverList;
	}
	
	/*private void printServerList(){
		String text = "";
		for (Server s : serverList.values()){
			text += "\n" + s + " --> ";
			for (SuccServer t : s.getSuccessor()){
				text += t.getServer() + "(" + t.getProbability() + "), ";
			}
		}
		JOptionPane.showMessageDialog(null, text);
	}*/
	
	private boolean isCaseNumReached(){
		return countCaseFinished >= lambda;
	}
	
	private boolean isTimeRunOut(){
		return clock >= timeOfPeriod;
	}
	
	private boolean shouldStopNow(){
		switch (stopRule){
		case Simulator.STOP_CASE_DRIVEN:
			return isCaseNumReached();
		case Simulator.STOP_TIME_DRIVEN:
			return isTimeRunOut();
		case Simulator.STOP_BOTH:
			return isCaseNumReached() || isTimeRunOut();
		default:
			return true;
		}
	}
	
	private Server getStartServer(){
		Node start = process.getStartPlace();
		int succs = start.getSuccessor().size();
		int rnd = fstServChoice.nextInt(100);
		int idx = -1;
		for (int i = 0; i < succs; i++){
			if (i == 0){
				if (rnd < (fstServList[i][2] * 100)){
					idx = i;
				}
			} else {
				if ((rnd >= fstServList[i-1][2]) && (rnd < fstServList[i][2])){
					idx = i;
				}
			}
		}
		
		return serverList.get(start.getSuccessor().get(idx).getTarget().getId());
	}
	
	private void getFstServList(){
		Node start = process.getStartPlace();
		int succs = start.getSuccessor().size();
		fstServList = new int[succs][3];
		
		switch (succs){
		case 1:
			fstServList[0][0] = 1;
			fstServList[0][1] = 1;
			fstServList[0][2] = 1;
			break;
		default:
			for (int i = 0; i < succs; i++){
				fstServList[i][0] = i;
				fstServList[i][1] = (Double.valueOf(start.getSuccessor().get(i).getProbability() * 100)).intValue();
				if (i > 0){
					fstServList[i][2] = fstServList[i][1] + fstServList[i-1][2];
				} else {
					fstServList[i][2] = fstServList[i][1];
				}
			}
		}
	}

	public double getClock() {
		return clock;
	}

	public void setClock(double clock) {
		this.clock = clock;
	}
	
	private void generateNextCase(){
		Case c = caseGenerator.generateNextCase();
		caseNo++;
		caseList.put(Integer.valueOf(c.getId()), c);
		ArrivalEvent ae = new ArrivalEvent(this, getStartServer(), c.getCurrentArrivalTime(), c);
		eventList.add(ae);
	}

	public int getMaxNumCasesInSystem() {
		return maxNumCasesInSystem;
	}

	public void setMaxNumCasesInSystem(int maxNumCasesInSystem) {
		this.maxNumCasesInSystem = maxNumCasesInSystem;
	}

	public int getNumCasesInSystem() {
		return numCasesInSystem;
	}

	public void setNumCasesInSystem(int numCasesInSystem) {
		this.numCasesInSystem = numCasesInSystem;
	}

	public int getQueueDiscipline() {
		return queueDiscipline;
	}

	public void setQueueDiscipline(int queueDiscipline) {
		this.queueDiscipline = queueDiscipline;
	}

	public ResourceAllocation getResAlloc() {
		return resAlloc;
	}

	public void setResAlloc(ResourceAllocation resAlloc) {
		this.resAlloc = resAlloc;
	}

	public ResourceUtilization getResUtil() {
		return resUtil;
	}

	public void setResUtil(ResourceUtilization resUtil) {
		this.resUtil = resUtil;
	}

	public int getUseResAlloc() {
		return useResAlloc;
	}

	public void setUseResAlloc(int useResAlloc) {
		this.useResAlloc = useResAlloc;
	}

	public PriorityQueue<SimEvent> getEventList() {
		return eventList;
	}

	public void setEventList(PriorityQueue<SimEvent> eventList) {
		this.eventList = eventList;
	}
}
