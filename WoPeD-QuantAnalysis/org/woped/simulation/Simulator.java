package org.woped.simulation;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

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
	
	private static final double VERY_LATE_TIME	= Double.POSITIVE_INFINITY;
	
	private WorkflowNetGraph process;
	private ResourceAllocation resAlloc;
	private ResourceUtilization resUtil;
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
	private double servParam1 = 0.0;
	private double servParam2 = 0.0;
	private double caseParam1 = 0.0;
	private double caseParam2 = 0.0;
	private int queueDiscipline = 0;
	private int stopRule = 0;
	private double lambda = 1.0;
	private double timeOfPeriod = 8.0;
	
	private SimEvent nextEvent = null;
	//private ArrayList<Server> serverList = new ArrayList<Server>();
	private HashMap<String, Server> serverList = new HashMap<String, Server>();
	private ArrayList<SimEvent> eventList = new ArrayList<SimEvent>();
	private static ArrayList<ProtocolItem> protocol = new ArrayList<ProtocolItem>();
	
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
		this.servParam1 = sp.getSPara1();
		this.servParam2 = sp.getSPara2();
		this.queueDiscipline = sp.getQueue();
		this.stopRule = sp.getStop();
		this.lambda = sp.getLambda();
		this.timeOfPeriod = sp.getTimeOfPeriod();
		
//		generateServerList(); // <---------
//		printServerList(); // <---------
	}
	
	public void start() {
		for (int i = 0; i < numRuns; i++){
			ProtocolItem pi = new ProtocolItem(this);
			String init = "Simulationsprotokoll\n Durchlauf: " + i + "\n\n";
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
		
		clock = 0.0;
		caseNo = 0;
		
		generateServerList();
		initEventList();
		
		protocolUpdate(pi);
	}
	
	private void timing(){
		ProtocolItem pi = new ProtocolItem(this);
		
		// nextEvent bestimmen
		clock += nextEvent.getMoment();
		
		protocolUpdate(pi);
	}
	
	private void generateReport(){
		SimOutputDialog sod = new SimOutputDialog(null, true, this);
		sod.setAlwaysOnTop(true);
		sod.setVisible(true);
	}
	
	public static void protocolUpdate(ProtocolItem pi){
		protocol.add(pi);
	}
	
	public void generateServerList(){ // <--------  private Methode !!!
		Node[] nodes = process.getNodeArray();
		for (int i = 0; i < nodes.length; i++){
			String id = nodes[i].getId();
			String name = nodes[i].getName();
			if (process.isTransition(id)){
				Server s = new Server(id, name, new ProbabilityDistribution(typeOfDistForServer, servParam1, servParam2, ++seed));
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
		caseGenerator = new CaseGenerator(new ProbabilityDistribution(typeOfDistForCases, caseParam1, caseParam2, ++seed));
		
	}

	public HashMap<String, Server> getServerList() {
		return serverList;
	}

	public void setServerList(HashMap<String, Server> serverList) {
		this.serverList = serverList;
	}
	
	private void printServerList(){
		String text = "";
		for (Server s : serverList.values()){
			text += "\n" + s + " --> ";
			for (SuccServer t : s.getSuccessor()){
				text += t.getServer() + "(" + t.getProbability() + "), ";
			}
		}
		JOptionPane.showMessageDialog(null, text);
	}
	
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
			return false;
		}
	}
}
