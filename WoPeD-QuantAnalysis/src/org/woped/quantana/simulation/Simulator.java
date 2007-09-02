package org.woped.quantana.simulation;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.PropertyResourceBundle;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.utilities.LoggerManager;
import org.woped.quantana.Constants;
import org.woped.quantana.graph.Arc;
import org.woped.quantana.graph.Node;
import org.woped.quantana.graph.WorkflowNetGraph;
import org.woped.quantana.gui.ActivityPanel;
import org.woped.quantana.gui.WaitDialog;
import org.woped.quantana.model.ReportServerStats;
import org.woped.quantana.model.ReportStats;
import org.woped.quantana.model.ResourceStats;
import org.woped.quantana.model.RunStats;
import org.woped.quantana.model.ServerStats;
import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceAllocation;
import org.woped.quantana.resourcealloc.ResourceUtilization;

public class Simulator {
	
	public static final int QD_FIFO	= 1;
	public static final int QD_LIFO = 2;
	
	public static final int STOP_NONE			= 0;
	public static final int STOP_CASE_DRIVEN	= 1;
	public static final int STOP_TIME_DRIVEN	= 2;
	public static final int STOP_BOTH			= 3;
	
	public static final int RES_USED		= 1;
	public static final int RES_NOT_USED	= 2;
	
	public static final int LIMIT_EVENT_ARRIVAL	= 3;
	
	private static final String         BUNDLE_NAME     = "org.woped.quantana.properties.ProtocolEntries";
    private static final Locale         LOCALE          = ConfigurationManager.getConfiguration().getLocale();
    public static final ResourceBundle	ENTRY			= PropertyResourceBundle.getBundle(BUNDLE_NAME, LOCALE);
	
	public static final Logger protocol = Logger.getLogger("org.woped.quantana.simulation");
	
	private ByteArrayOutputStream out;

	private String protocolPath;
	private String protocolName;

	private WorkflowNetGraph process;
	private ResourceAllocation resAlloc;
	private ResourceUtilization resUtil;
	private int useResAlloc;
	private CaseGenerator caseGenerator;
	private SeedGenerator seedGenerator;
	private int numRuns;
	private double clock;
//	private int maxNumCasesInSystem = 0;
//	private int caseCount = 0;
	private int finishedCases = 0;
	/*private double avgCasesInSystem = 0.0;
	private double timeOfLastEvent = 0.0;
	private double timeOfLastCaseNumChange = 0.0;
	private double throughPut = 0.0;
	private double caseBusy = 0.0;
	private double caseWait = 0.0;*/
	private int typeOfDistForCases = 0;
	private int typeOfDistForServer = 0;
	private double caseParam = 0.0;
	private double servParam = 0.0;
	private int queueDiscipline = 0;
	private int stopRule = 0;
	private double lambda = 50.0;
	private double period = 480.0;
	private Random fstServChoice = new Random(new Date().getTime());
	private int[][] fstServList;
	private double duration;
	private double avgProcessWaitTime = 0.0;
	private double avgProcessServiceTime = 0.0;
	private double avgProcessCompletionTime = 0.0;
	
	private SimEvent nextEvent = null;
	private ArrayList<Server> startServerList;
	private HashMap<String, Server> serverList = new HashMap<String, Server>();
	private PriorityQueue<SimEvent> eventList = new PriorityQueue<SimEvent>();
	private HashMap<Integer, Case> caseList	 = new HashMap<Integer, Case>();
	private HashMap<Integer, Case> copiedCasesList = new HashMap<Integer, Case>();
	
	private ArrayList<ActivityPanel> actPanelList = new ArrayList<ActivityPanel>();
	
	private ArrayList<RunStats> runStats;
	private ReportStats repStats = new ReportStats();
	
	private int cntArrivalEvents = 0;
	
//	private TmpProtocolDialog tmp;
	private WaitDialog wd;
	
	public Simulator(WorkflowNetGraph wfpn, ResourceUtilization ru, SimParameters sp, WaitDialog wd){ //TmpProtocolDialog tmp){
		process = wfpn;
		resUtil = ru;
		resAlloc = ru.getResAlloc();
//		this.tmp = tmp;
		this.wd = wd;
		
		this.numRuns = sp.getRuns();
		this.typeOfDistForCases = sp.getDistCases();
		this.typeOfDistForServer = sp.getDistServ();
		this.caseParam = sp.getCParam();
		this.servParam = sp.getSParam();
		this.queueDiscipline = sp.getQueue();
		this.stopRule = sp.getStop();
		this.lambda = sp.getLambda();
		this.period = sp.getPeriod();
		this.useResAlloc = sp.getResUse();
		
		makeFstServList();
		startServerList = makeStartServerList();
		seedGenerator = new SeedGenerator();
	}
	
	public void start() {
		
		initProtocol();
		protocol.info(clckS() + ENTRY.getString("Sim.Start"));
		protocol.info(clckS() + ENTRY.getString("Sim.ResModel") + printResModUsed());
		protocol.info(clckS() + ENTRY.getString("Sim.QueueDiscipline") + printQueueDiscipline());
		protocol.log(Level.INFO, clckS() + ENTRY.getString("Sim.Period"), period);
		protocol.info(clckS() + ENTRY.getString("Sim.Lambda") + lambda);
		protocol.info(clckS() + ENTRY.getString("Sim.TerminationRule") + printTerminationRule());
		protocol.info(clckS() + ENTRY.getString("Sim.Distribution.Arrival") + printDistIAT());
		protocol.info(clckS() + ENTRY.getString("Sim.Distribution.Server") + printDistST());
		
		generateServerList();
//		LoggerManager.info(Constants.QUANTANA_LOGGER, printServerList());
		protocol.info(clckS() + ENTRY.getString("Sim.ServerList.Generated"));
		
		runStats = new ArrayList<RunStats>();
		
		protocol.info(clckS() + ENTRY.getString("Sim.Runs") + numRuns);
		for (int i = 0; i < numRuns; i++){
			protocol.info(clckS() + ENTRY.getString("Sim.Delimiter"));
			protocol.log(Level.INFO, clckS() + ENTRY.getString("Sim.Runs.Number"), (i + 1));
			
			protocol.info(clckS() + ENTRY.getString("Sim.Init"));
			init(i + 1);
			
			protocol.info(clckS() + ENTRY.getString("Sim.Start"));
			while (!shouldStopNow()){
//				protocol.info(clckS() + ENTRY.getString("Sim.Number.Cases.Finished") + finishedCases);
				
				timing();
				
				if (nextEvent != null){
//					LoggerManager.info(Constants.QUANTANA_LOGGER, printEventList() + "::" + nextEvent.getName());
					protocol.info(clckS() + nextEvent.getEventTypeName() + " (" + nextEvent.getName() + ")");
					nextEvent.invoke();
				}
				else
					break;
			}
			
			LoggerManager.info(Constants.QUANTANA_LOGGER, "Report wird erzeugt.");
			protocol.info(clckS() + ENTRY.getString("Sim.Report.Generated"));
			finishRun();
		}
		
		generateReport();
	}
	
	private void init(int run){

//		protocol.info(clckS() + ENTRY.getString("Sim.Clock.Init"));
		clock = 0.0;
		
		cntArrivalEvents = 0;
		
		// sämtliche Counter zurücksetzen
//		protocol.info(clckS() + ENTRY.getString("Sim.Counters.Reset"));
		/*avgCasesInSystem = 0.0;
		caseCount = 0;*/
		finishedCases = 0;
		duration = 0;
		avgProcessServiceTime = 0;
		avgProcessWaitTime = 0;
		avgProcessCompletionTime = 0;
		//maxNumCasesInSystem = 0;
		//numCasesInSystem = 0;
		/*throughPut = 0.0;
		timeOfLastCaseNumChange = 0.0;
		timeOfLastEvent = 0.0;*/
		
		caseList.clear();
		
		nextEvent = null;
		
		// alle Server zurücksetzen
//		protocol.info(clckS() + ENTRY.getString("Sim.Servers.Reset"));
		for (Server s : serverList.values()){
			s.reset();
		}
		
		// alle Ressourcen befreien
//		protocol.info(clckS() + ENTRY.getString("Sim.Resources.Freed"));
		for (Object o : resUtil.getReservedResources().values().toArray()){
			resUtil.useResource((Resource)o);
		}
		
		for (Object o : resUtil.getUsedResources().values().toArray()){
			resUtil.freeResource((Resource)o);
		}
		
		// alle Resourcen zurücksetzen
		for (Resource r : resAlloc.getResources().values())
			r.reset();
		
//		protocol.info(clckS() + ENTRY.getString("Sim.EventList.Init"));
		initEventList();
		
		LoggerManager.info(Constants.QUANTANA_LOGGER, "Simulation run # " + run + " initialized.");
	}
	
	private void timing(){
		// nextEvent bestimmen
		if (!(eventList.isEmpty()))
			nextEvent = eventList.remove();
		else
			nextEvent = null;
		
		String evt = ENTRY.getString("Sim.Event.Next");
		if (nextEvent != null) evt += nextEvent.getName();
//		protocol.info(clckS() + evt);
//		protocol.info(clckS() + ENTRY.getString("Sim.EventList.Content") + printEventList());
		
		// Systemuhr setzen
//		protocol.info(clckS() + ENTRY.getString("Sim.Clock.NextEvent.Set"));
		if (nextEvent != null) clock = nextEvent.getTime();
	}
	
	private void finishRun(){
		RunStats stats = new RunStats();
		HashMap<Server, ServerStats> sStats = stats.getServStats();
		HashMap<Resource, ResourceStats> rStats = stats.getResStats();
		
		for (Server s : serverList.values()){
			ServerStats sst = new ServerStats(s.getName(), s.getId());
			sst.setNumServedWhenStopped(s.getNumCasesInParallel());
			sst.setQLengthWhenStopped(s.getQueue().size());
			
			sStats.put(s, sst);
		}
		
		Object[] events = eventList.toArray();
		for (Object o : events){
			SimEvent se = (SimEvent)o;
			if (se instanceof DepartureEvent){
				DepartureEvent dp = (DepartureEvent)se;
				dp.invoke();
				eventList.remove(se);
			}
		}
		
		events = eventList.toArray();
		for (Object o : events){
			SimEvent se = (SimEvent)o;
			if (se instanceof DeathEvent){
				DeathEvent de = (DeathEvent)se;
				de.invoke();
				eventList.remove(se);
			}
		}
		
		for (Server s : serverList.values()){
			s.updQStats(clock, 0);
			s.updRStats(clock, 0);
			
//			ServerStats sst = new ServerStats(s.getName(), s.getId());
			ServerStats sst = sStats.get(s);
			int nd = s.getNumDeparture();
			sst.setZeroDelays(s.getZeroDelays());
			sst.setCalls(s.getNumCalls());
			sst.setAccesses(s.getNumAccess());
			sst.setDepartures(nd);
			sst.setAvgQLength(s.getQueueLength());
			sst.setMaxQLength(s.getMaxQLength());
			sst.setQueueProportions(s.getQProps());
			sst.setAvgResNumber(s.getAvgNumRes());
			sst.setMaxResNumber(s.getMaxNumCasesInParallel());
			sst.setResNumProperties(s.getRProps());
			
			if (nd == 0)
				sst.setAvgWaitTime(0);
			else
				sst.setAvgWaitTime(s.getWaitTime() / s.getNumDeparture());
			
			sst.setMaxWaitTime(s.getMaxWaitTime());
			sst.setWTable(s.getWaitTimes());
			
			if (nd == 0)
				sst.setAvgServTime(0);
			else
				sst.setAvgServTime(s.getServiceTime() / s.getNumDeparture());
			
			sStats.put(s, sst);
		}
		
//		stats.setServStats(sStats);
		
		for (Resource r : resAlloc.getResources().values()){
			ResourceStats rst = new ResourceStats(r.getName());
			rst.setIdleTime(clock - r.getBusyTime());
			rst.setUtilizationRatio(r.getBusyTime() / clock);
			rStats.put(r, rst);
		}
		
//		stats.setResStats(rStats);
		
		stats.setDuration(clock);
		stats.setFinishedCases(finishedCases);
		stats.setProcWaitTime(avgProcessWaitTime / finishedCases);
		stats.setProcServTime(avgProcessServiceTime / finishedCases);
		stats.setProcCompTime(avgProcessCompletionTime / finishedCases);
		stats.setThroughPut(finishedCases / clock * period);
		
		runStats.add(stats);
	}
	
	private void generateReport(){
		protocol.info(clckS() + ENTRY.getString("Sim.Stop"));
		
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		((Handler)((protocol.getHandlers())[0])).close();
		
		LoggerManager.info(Constants.QUANTANA_LOGGER, "Protokoll wurde erstellt.");

		int sum = 0;
		double sumDur = 0;
		double sumPST = 0;
		double sumPWT = 0;
		double sumPCT = 0;
		double sumThp = 0;
		
		for (RunStats rs : runStats){
			sum += rs.getFinishedCases();
			sumDur += rs.getDuration();
			sumPST += rs.getProcServTime();
			sumPWT += rs.getProcWaitTime();
			sumPCT += rs.getProcCompTime();
			sumThp += rs.getThroughPut();
			
			for (ServerStats ss : rs.getServStats().values()){
				Server s = serverList.get(ss.getId());
				if (repStats.getServStats().containsKey(s)){
					ReportServerStats rss = (ReportServerStats)repStats.getServStats().get(s);
					rss.incAvgZeroDelays(ss.getZeroDelays());
					rss.incAvgCalls(ss.getCalls());
					rss.incAvgAccesses(ss.getAccesses());
					rss.incAvgDepartures(ss.getDepartures());
					rss.incAvgMaxQLength(ss.getMaxQLength());
					rss.incAvgMaxResNumber(ss.getMaxResNumber());
					rss.incAvgNumServedWhenStopped(ss.getNumServedWhenStopped());
					rss.incAvgQLengthWhenStopped(ss.getQLengthWhenStopped());
					rss.incAvgAvgQLength(ss.getAvgQLength());
					rss.incAvgAvgResNumber(ss.getAvgResNumber());
					rss.incAvgAvgWaitTime(ss.getAvgWaitTime());
					rss.incAvgMaxWaitTime(ss.getMaxWaitTime());
					rss.incAvgServTime(ss.getAvgServTime());
				} else {
					ReportServerStats rss = new ReportServerStats(ss.getName(), ss.getId());
					rss.setAvgZeroDelays(ss.getZeroDelays());
					rss.setAvgCalls(ss.getCalls());
					rss.setAvgAccesses(ss.getAccesses());
					rss.setAvgDepartures(ss.getDepartures());
					rss.setAvgMaxQLength(ss.getMaxQLength());
					rss.setAvgMaxResNumber(ss.getMaxResNumber());
					rss.setAvgNumServedWhenStopped(ss.getNumServedWhenStopped());
					rss.setAvgQLengthWhenStopped(ss.getQLengthWhenStopped());
					rss.setAvgQLength(ss.getAvgQLength());
					rss.setAvgResNumber(ss.getAvgResNumber());
					rss.setAvgWaitTime(ss.getAvgWaitTime());
					rss.setMaxWaitTime(ss.getMaxWaitTime());
					rss.setAvgServTime(ss.getAvgServTime());
					
					repStats.getServStats().put(s, rss);
				}
			}
			
			for (ResourceStats rr : rs.getResStats().values()){
				Resource r = resAlloc.getResources().get(rr.getName());
				if (repStats.getResStats().containsKey(r)){
					ResourceStats rrs = repStats.getResStats().get(r);
					rrs.incIdleTime(rr.getIdleTime());
					rrs.incUtilizationRatio(rr.getUtilizationRatio());
				} else {
					ResourceStats rrs = new ResourceStats(rr.getName());
					rrs.setIdleTime(rr.getIdleTime());
					rrs.setUtilizationRatio(rr.getUtilizationRatio());
					
					repStats.getResStats().put(r, rrs);
				}
			}
		}
		
		repStats.setAvgFinishedCases(sum / numRuns);
		repStats.setDuration(sumDur / numRuns);
		repStats.setProcWaitTime(sumPWT / numRuns);
		repStats.setProcServTime(sumPST / numRuns);
		repStats.setProcCompTime(sumPCT / numRuns);
		repStats.setThroughPut(sumThp / numRuns);
		
		for (ServerStats ss : repStats.getServStats().values()){
			ReportServerStats rss = (ReportServerStats)ss;
			rss.setAvgZeroDelays(rss.getAvgZeroDelays() / numRuns);
			rss.setAvgCalls(rss.getAvgCalls() / numRuns);
			rss.setAvgAccesses(rss.getAvgAccesses() / numRuns);
			rss.setAvgDepartures(rss.getAvgDepartures() / numRuns);
			rss.setAvgMaxQLength(rss.getAvgMaxQLength() / numRuns);
			rss.setAvgMaxResNumber(rss.getAvgMaxResNumber() / numRuns);
			rss.setAvgNumServedWhenStopped(rss.getAvgNumServedWhenStopped() / numRuns);
			rss.setAvgQLengthWhenStopped(rss.getAvgQLengthWhenStopped() / numRuns);
			rss.setAvgQLength(rss.getAvgQLength() / numRuns);
			rss.setAvgResNumber(rss.getAvgResNumber() / numRuns);
			rss.setAvgWaitTime(rss.getAvgWaitTime() / numRuns);
			rss.setMaxWaitTime(rss.getMaxWaitTime() / numRuns);
			rss.setAvgServTime(rss.getAvgServTime() / numRuns);
		}
		
		for (ResourceStats rrs : repStats.getResStats().values()){
			rrs.setIdleTime(rrs.getIdleTime() / numRuns);
			rrs.setUtilizationRatio(rrs.getUtilizationRatio() / numRuns);
		}
		
		runStats.add(repStats);
	}
	
	private void generateServerList(){
		Node[] nodes = process.getNodeArray();
		for (int i = 0; i < nodes.length; i++){
			Node n = nodes[i];
			String id = n.getId();
			String name = n.getName();
			double t = n.getTime();
			if (process.isTransition(id)){
				Server s;
				if (n.isAndJoin()) {
					s = new ANDJoinServer(this, id, name, new ProbabilityDistribution(typeOfDistForServer, t, servParam, seedGenerator.nextSeed()));
				}
				else if (n.isAndSplit()) {
					s = new ANDSplitServer(this, id, name, new ProbabilityDistribution(typeOfDistForServer, t, servParam, seedGenerator.nextSeed()));
				}
				else {
					s = new Server(this, id, name, new ProbabilityDistribution(typeOfDistForServer, t, servParam, seedGenerator.nextSeed()));
				}
				s.setStatus(Server.STATUS_IDLE);
				String nid = name + " (" + id + ")";
				s.setRole(resAlloc.getRole(nid));
				s.setGroup(resAlloc.getGroup(nid));
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
		
		LoggerManager.info(Constants.QUANTANA_LOGGER, "List of servers for quantitative simulation generated.");
	}
	
	private void initEventList(){
		eventList.clear();
		SimEvent.reset();
		
		caseGenerator = new CaseGenerator(new ProbabilityDistribution(typeOfDistForCases, period / lambda, caseParam, seedGenerator.nextSeed()), this);
		
		BirthEvent be = new BirthEvent(this, clock);
		eventList.add(be);
		protocol.info(clckS() + ENTRY.getString("Sim.Event.Birth") + be.getName() + ENTRY.getString("Sim.Event.Generated"));
	}

	public HashMap<String, Server> getServerList() {
		return serverList;
	}

	public void setServerList(HashMap<String, Server> serverList) {
		this.serverList = serverList;
	}
	
	public String printServerList(){
		String text = "";
		for (Server s : serverList.values()){
			text += "\n" + s + " --> ";
			for (SuccServer t : s.getSuccessor()){
				text += t.getServer() + "(" + t.getProbability() + "), ";
			}
		}

		return text;
	}
	
	private boolean isCaseNumReached(){
		return finishedCases >= lambda;
	}
	
	private boolean isTimeRunOut(){
		return clock >= period;
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
			LoggerManager.info(Constants.QUANTANA_LOGGER, "No termination rule defined. Simulation stopped immediately.");
			return true;
		}
	}
	
	public Server getStartServer(){
		Node start = process.getStartPlace();
		int succs = start.getSuccessor().size();
		int rnd = fstServChoice.nextInt(100);
		int idx = -1;
		for (int i = 0; i < succs; i++){
			if (i == 0){
				if (rnd < (fstServList[i][2])){
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
	
	private void makeFstServList(){
		Node start = process.getStartPlace();
		int succs = start.getSuccessor().size();
		fstServList = new int[succs][3];
		
		switch (succs){
		case 1:
			fstServList[0][0] = 1;
			fstServList[0][1] = 100;
			fstServList[0][2] = 100;
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
	
	/*public int getMaxNumCasesInSystem() {
		return maxNumCasesInSystem;
	}

	public void setMaxNumCasesInSystem(int maxNumCasesInSystem) {
		this.maxNumCasesInSystem = maxNumCasesInSystem;
	}*/

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
	
	public void enroleEvent(SimEvent se){
		eventList.add(se);
	}
	
	public void bind(Resource r){
		resUtil.useResource(r);
	}
	
	public void free(Resource r){
		resUtil.freeResource(r);
	}
	
	public void reserveResource(Server s, Resource r){
		resUtil.reserveResource(r);
		s.addReservedResource(r);
	}

	/*public PriorityQueue<SimEvent> getEventList() {
		return eventList;
	}

	public void setEventList(PriorityQueue<SimEvent> eventList) {
		this.eventList = eventList;
	}*/

	/*public double getAvgCasesInSystem() {
		return avgCasesInSystem;
	}

	public void setAvgCasesInSystem(double avgCasesInSystem) {
		this.avgCasesInSystem = avgCasesInSystem;
	}*/

	public CaseGenerator getCaseGenerator() {
		return caseGenerator;
	}

	public void setCaseGenerator(CaseGenerator caseGenerator) {
		this.caseGenerator = caseGenerator;
	}

	public int getFinishedCases() {
		return finishedCases;
	}

	public void setFinishedCases(int finishedCases) {
		this.finishedCases = finishedCases;
	}

	/*public double getTimeOfLastEvent() {
		return timeOfLastEvent;
	}

	public void setTimeOfLastEvent(double timeOfLastEvent) {
		this.timeOfLastEvent = timeOfLastEvent;
	}*/

	public HashMap<Integer, Case> getCaseList() {
		return caseList;
	}

	public void setCaseList(HashMap<Integer, Case> caseList) {
		this.caseList = caseList;
	}

	public int getStopRule() {
		return stopRule;
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	/*public int getCaseCount() {
		return caseCount;
	}

	public void setCaseCount(int caseCount) {
		this.caseCount = caseCount;
	}

	public double getThroughPut() {
		return throughPut;
	}

	public void setThroughPut(double throughPut) {
		this.throughPut = throughPut;
	}
	
	public void updateCaseNumStats(double now, double lastEvent){
		int cc = caseList.size();
		avgCasesInSystem += cc * (now - lastEvent);
		
		protocol.info(clckS() + ENTRY.getString("Sim.Number.Cases.Current") + cc);
		protocol.info(clckS() + ENTRY.getString("Sim.Number.Cases.Average") + String.format("%,.2f", (avgCasesInSystem / clock)));
	}

	public double getTimeOfLastCaseNumChange() {
		return timeOfLastCaseNumChange;
	}

	public void setTimeOfLastCaseNumChange(double timeOfLastCaseNumChange) {
		this.timeOfLastCaseNumChange = timeOfLastCaseNumChange;
	}

	public double getCaseBusy() {
		return caseBusy;
	}

	public void setCaseBusy(double caseBusy) {
		this.caseBusy = caseBusy;
	}

	public double getCaseWait() {
		return caseWait;
	}

	public void setCaseWait(double caseWait) {
		this.caseWait = caseWait;
	}*/
	
	private void initProtocol(){
		protocolPath = ConfigurationManager.getConfiguration().getLogdir();
		protocolName = protocolPath + "simproto.xml";
		
		protocol.setLevel(Level.ALL);
		protocol.setUseParentHandlers(false);
		
		out = new ByteArrayOutputStream();
		Handler handler = new StreamHandler(new BufferedOutputStream(out), new SimXMLFormatter(protocolPath));
		
		protocol.addHandler(handler);
		
		LoggerManager.info(Constants.QUANTANA_LOGGER, "Protocol for quantitative simulation initialized.");
	}
	
	private String printResModUsed(){
		switch(useResAlloc){
		case RES_USED:
			return ENTRY.getString("Sim.Yes");
		default:
			return ENTRY.getString("Sim.No");
		}
	}
	
	private String printQueueDiscipline(){
		switch(queueDiscipline){
		case QD_LIFO:
			return ENTRY.getString("Sim.QueueDiscipline.LIFO");
		default:
			return ENTRY.getString("Sim.QueueDiscipline.FIFO");
		}
	}

	public String getProtocolName() {
		return protocolName;
	}
	
	public Logger getProtocol(){
		return protocol;
	}
	
	public byte[] getProtocolContent(){
		return out.toByteArray();
	}

	public String clckS() {
		String c = String.format("%,.2f", clock);
		return "(" + c + "): ";
	}
	
	public String printEventList(){
		String s = "[";
		Object[] list = eventList.toArray();
		int l = list.length;
		
		if (l > 1){
			for (int i = 0; i < l - 1; i++){
				s += ((SimEvent)list[i]).getName() + ",";
			}
		}
		
		if (l > 0) s += ((SimEvent)list[l - 1]).getName();
		
		return s + "]";
	}

	public String getProtocolPath() {
		return protocolPath;
	}

	public void setProtocolPath(String protocolPath) {
		this.protocolPath = protocolPath;
	}

	public HashMap<Integer, Case> getCopiedCasesList() {
		return copiedCasesList;
	}

	public void setCopiedCasesList(HashMap<Integer, Case> copiedCasesList) {
		this.copiedCasesList = copiedCasesList;
	}
	
	public void updStatistics(int type){
		switch (type) {
		case SimEvent.BIRTH_EVENT:
			
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
			
			break;
		default:
		}
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}
	
	private String printTerminationRule(){
		switch (stopRule){
		case STOP_CASE_DRIVEN:
			return ENTRY.getString("Sim.TerminationRule.CaseDriven");
		case STOP_TIME_DRIVEN:
			return ENTRY.getString("Sim.TerminationRule.TimeDriven");
		case STOP_BOTH:
			return ENTRY.getString("Sim.TerminationRule.Both");
		default:
			return ENTRY.getString("Sim.TerminationRule.No");
		}
	}
	
	private String printDistIAT(){
		switch (typeOfDistForCases){
		case ProbabilityDistribution.DIST_TYPE_UNIFORM:
			return ENTRY.getString("Sim.Distribution.Uniform") + String.format("%,.2f", caseParam);
		case ProbabilityDistribution.DIST_TYPE_GAUSS:
			return ENTRY.getString("Sim.Distribution.Gaussian") + String.format("%,.2f", caseParam);
		default:
			return ENTRY.getString("Sim.Distribution.Exponential");
		}
	}
	
	private String printDistST(){
		switch (typeOfDistForServer){
		case ProbabilityDistribution.DIST_TYPE_UNIFORM:
			return ENTRY.getString("Sim.Distribution.Uniform") + String.format("%,.2f", servParam);
		case ProbabilityDistribution.DIST_TYPE_GAUSS:
			return ENTRY.getString("Sim.Distribution.Gaussian") + String.format("%,.2f", servParam);
		default:
			return ENTRY.getString("Sim.Distribution.Exponential");
		}
	}
	
	public ArrayList<Server> makeStartServerList(){
		ArrayList<Server> list = new ArrayList<Server>();
		Node start = process.getStartPlace();
		
		for (int i = 0; i < start.getSuccessor().size(); i++){
			Node n = start.getSuccessor().get(i).getTarget();
			String id = n.getId();
			Server s = serverList.get(id);
			list.add(s);
		}
		
		return list;
	}

	public ArrayList<Server> getStartServerList() {
		return startServerList;
	}

	public static ResourceBundle getENTRY() {
		return ENTRY;
	}

	public ArrayList<ActivityPanel> getActPanelList() {
		return actPanelList;
	}

	public double getAvgProcessCompletionTime() {
		return avgProcessCompletionTime;
	}

	public void setAvgProcessCompletionTime(double avgProcessCompletionTime) {
		this.avgProcessCompletionTime = avgProcessCompletionTime;
	}

	public double getAvgProcessServiceTime() {
		return avgProcessServiceTime;
	}

	public void setAvgProcessServiceTime(double avgProcessServiceTime) {
		this.avgProcessServiceTime = avgProcessServiceTime;
	}

	public double getAvgProcessWaitTime() {
		return avgProcessWaitTime;
	}

	public void setAvgProcessWaitTime(double avgProcessWaitTime) {
		this.avgProcessWaitTime = avgProcessWaitTime;
	}
	
	public void incFinishedCases(){
		finishedCases++;
	}
	
	public void incAvgProcessWaitTime(double time){
		avgProcessWaitTime += time;
	}
	
	public void incAvgProcessServiceTime(double time){
		avgProcessServiceTime += time;
	}

	public void incAvgProcessCompletionTime(double time){
		avgProcessCompletionTime += time;
	}
	
	public void addToCopyList(Case c){
		copiedCasesList.put(c.getId(), c);
	}
	
	/*public boolean containsOrig(CaseCopy copy){
		return copiedCasesList.containsKey(copy.getId());
	}*/
	
	public Case getOrig(CaseCopy copy){
		int id = copy.getOriginal().getId();
		if (copiedCasesList.containsKey(id))
			return copiedCasesList.get(id);
		else
			return null;
	}
	
	public void removeOrig(Case c){
		copiedCasesList.remove(c.getId());
	}
	
	public Iterator<SimEvent> getEventListIterator(){
		return eventList.iterator();
	}

	public ArrayList<RunStats> getRunStats() {
		return runStats;
	}
	
	public void incCntArrivalEvents(){
		cntArrivalEvents++;
	}
	
	public void decCntArrivalEvents(){
		cntArrivalEvents--;
	}

	public int getCntArrivalEvents() {
		return cntArrivalEvents;
	}

	public WaitDialog getWd() {
		return wd;
	}
	
	public ReportStats getReportStats(){
		return repStats;
	}

	public double getPeriod() {
		return period;
	}
}
