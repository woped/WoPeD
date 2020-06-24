package org.woped.quantana.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import org.woped.gui.translations.Messages;
import org.woped.quantana.gui.ActivityPanel;
import org.woped.quantana.model.ResourceStats;
import org.woped.quantana.resourcealloc.Resource;
import org.woped.quantana.resourcealloc.ResourceAllocation;
import org.woped.quantana.resourcealloc.ResourceUtilization;

import umontreal.iro.lecuyer.rng.MRG32k3a;
import umontreal.iro.lecuyer.rng.RandomStream;


public class SimRunner implements Runnable{
	public static final int Q_FIFO = 0;
	public static final int Q_LIFO = 1;

	public static final int STOP_NONE = 0;
	public static final int STOP_CASE = 1;
	public static final int STOP_TIME = 2;
	public static final int STOP_BOTH = 3;

	public static final int RES_USED = 0;
	public static final int RES_NOT_USED = 1;

	public static final int IND = 0;
	public static final int MIN_VALUE = 1;
	public static final int MAX_VALUE = 2;

	public static final int MAX_ARRIVAL = 3;

	protected SimGraph graph = null;
	protected ResourceAllocation resAlloc = null;
	protected ResourceUtilization resUtil = null;
	protected SimParameters params = null;
	int[][] fstProb = null;
	
	Thread thr = null;
	
	RandomStream randStart = new MRG32k3a();
	PriorityQueue<SimulatorEvent> eventList = new PriorityQueue<SimulatorEvent>();
	protected HashMap<String, SimServer> serverList = new HashMap<String, SimServer>();
	SimCaseMaker caseMaker = new SimCaseMaker(this);
	ArrayList<SimRunStats> runStats;
	SimReportStats repStats = new SimReportStats();
	ArrayList<SimServer> queueingServers = new ArrayList<SimServer>();
	SimDistributionLogger distLogger = null;	
	ArrayList<String> log = new ArrayList<String>();
	SimLog xesLog = new SimLog();
	
	private ArrayList<ActivityPanel> actPanelList = new ArrayList<ActivityPanel>();

	SimulatorEvent nextEvent = null;
	double runClock = 0;
	int cntFinished = 0;
	int cntArrival = 0;
	int cntRun = 0;
	
	double avgWait = 0;
	double avgSvc = 0;
	double avgRunTime = 0; 
	
	
	boolean aborted = false;
		
	public SimRunner(SimGraph graph, ResourceUtilization resUtil,
			SimParameters sp) {
		this.graph = graph;
		this.resUtil = resUtil;		
		resAlloc = this.resUtil.getResAlloc();
		params = sp;
		createServerForBirth();		
	}

	private void createServerForBirth() {
		SimNode n = graph.getSource();
		ArrayList<SimArc> out = n.getarcOut();
		fstProb = new int[out.size()][3];
		if (out.size() == 1) {
			fstProb[0][IND] = 1;
			fstProb[0][MIN_VALUE] = 100;
			fstProb[0][MAX_VALUE] = 100;
		} else {
			for (int i = 0; i < out.size(); i++) {
				n = ((SimArc) out.get(i)).getTarget();
				fstProb[i][IND] = i;
				fstProb[i][MIN_VALUE] = Double.valueOf(
						((SimArc) out.get(i)).getProbability() * 100)
						.intValue();
				fstProb[i][MAX_VALUE] = (i > 0) ? fstProb[i][MIN_VALUE]
						+ fstProb[i - 1][MAX_VALUE] : fstProb[i][MIN_VALUE];
			}
		}
	}

	public SimServer getFirstServer() {
		int rnd = randStart.nextInt(0,99);
		int index = -1;
		for (int i = 0; i < fstProb.length; i++) {
			if (i == 0) {
				if (rnd < fstProb[0][MAX_VALUE])
					index = i;
			} else {
				if ((rnd >= fstProb[i - 1][MAX_VALUE])
						&& (rnd < fstProb[i][MAX_VALUE]))
					index = i;
			}
		}
		return serverList.get(graph.getSource().getarcOut().get(index)
				.getTarget().getid());
	}

	public void start() {
		thr = new Thread(this);
		thr.start();		
	}
		
		
	public void run() {		
		createServerList();
		runStats = new ArrayList<SimRunStats>();
		distLogger = new SimDistributionLogger(params.getPeriod()/params.getLambda());
		caseMaker.setDistLogger(distLogger);		
		for (cntRun = 0; cntRun < params.getRuns(); cntRun++) {
			initRun(cntRun + 1);
			while (!(hasToStop()||aborted)) {
				if (eventList.isEmpty())
					break;
				else {
					nextEvent = eventList.remove();
					runClock = nextEvent.getTime();
					nextEvent.invoke();
				}
			}
			finishRun();
			if (aborted)break;			
		}		
		generateReport();		
		// to finish progress
		aborted = true;
		caseMaker = null;		
		Thread.currentThread().interrupt();		
	}
	
	private boolean hasToStop() {
		switch (params.getStop()) {
		case STOP_BOTH:
			return (cntFinished >= params.getLambda())
					|| (runClock >= params.getPeriod());
		case STOP_CASE:
			return cntFinished >= params.getLambda();
		case STOP_TIME:
			return runClock >= params.getPeriod();
		case STOP_NONE:
		default:
			return true;
		}
	}

	private void initRun(int numRun) {
		runClock = 0;
		cntFinished = 0;
		cntArrival = 0;
		eventList.clear();		
		avgWait = 0;
		avgSvc = 0;
		avgRunTime = 0;
		log.clear();
		xesLog.clear();
		
		eventList = new PriorityQueue<SimulatorEvent>();
			SimulatorEvent.reset();
		// reset all Servers
		for (SimServer s : serverList.values())
			s.reset();
		for (Object o : resUtil.getReservedResources().values().toArray())
			resUtil.useResource((Resource)o);
		for(Object o: resUtil.getUsedResources().values().toArray()){
			resUtil.freeResource((Resource)o);
		}		
		for (Resource r : resAlloc.getResources().values())
			r.reset();
		caseMaker.reset();
		SimDistribution dist = new SimDistribution(params.getDistCases(),
				params.getPeriod() / params.getLambda(), params.getCParam());
		caseMaker.setDistribution(dist);
		// create the first event
		eventList.add(new SimBirthEvent(this, runClock));		
		queueingServers.clear();
		actPanelList.clear();
	}
	
	public void addQueueServer(SimServer s){
		if (queueingServers.indexOf(s)<0) queueingServers.add(s);
	}
	
	public ArrayList<SimServer> getQueueingServers(){
		return queueingServers;
	}

	public SimCaseMaker getCaseMaker() {
		return caseMaker;
	}

	protected SimRunStats finishRun() {
		SimRunStats stats = new SimRunStats();
		HashMap<SimServer, SimServerStats> sStats = stats.getServStats();
		HashMap<Resource, ResourceStats> rStats = stats.getResStats();
		
		for (SimServer s : serverList.values()){
			SimServerStats sst = new SimServerStats(s.getName(), s.getid());			
			sst.setNumServedWhenStopped(s.getParallels());
			sst.setQLengthWhenStopped(s.getQueue().size());
			sst.setDistributionLogger(s.getDistLogger());
			sStats.put(s, sst);
		}
		
		// fire events to finsh started events
		Object[] evts = eventList.toArray();
		for (Object o: evts){
			SimulatorEvent e = (SimulatorEvent)o;
			if(e instanceof SimDepartureEvent){
				((SimDepartureEvent)e).invoke();
				eventList.remove(e);
			}
		}
				
		for(Object o: eventList.toArray()){		
			SimulatorEvent e = (SimulatorEvent)o;
			if(e instanceof SimDeathEvent){
				((SimDeathEvent)e).invoke();
				eventList.remove(e);
			}
		}
		
		for (SimServer s : serverList.values()){
			s.updateRunStats(runClock, 0);
			s.updateQueue(runClock,-1);
			SimServerStats sst = sStats.get(s);
			int nd = s.getCntDeparture();
			sst.setZeroDelays(s.getZeroDelays());
			sst.setCalls(s.getCntCalls());
			sst.setAccesses(s.getCntAccess());
			sst.setDepartures(nd);
			sst.setAvgQLength(s.getQueueLength());
			sst.setMaxQLength(s.getMaxQueueLength());
			sst.setQueueProportions(s.getQueueProps());
			sst.setAvgResNumber(s.getAvgNumReserved());
			sst.setMaxResNumber(s.getMaxParallel());
			if (nd != 0){				
				sst.setAvgWaitTime(s.getWaitTime() / nd);
				sst.setAvgServTime(s.getSvcTime() / nd);
			}
			sst.setMaxWaitTime(s.getMaxWaitTime());
			sStats.put(s, sst);
		}
		
		for (Resource r : resAlloc.getResources().values()){
			ResourceStats rst = new ResourceStats(r.getName());
			rst.setIdleTime(runClock - r.getBusyTime());
			rst.setUtilizationRatio(r.getBusyTime() / runClock);
			rStats.put(r, rst);
		}		
		stats.setDuration(runClock);		
		
		
		stats.setFinishedCases(cntFinished);
		if (cntFinished>0){
			stats.setProcWaitTime(avgWait / cntFinished);
			stats.setProcServTime(avgSvc / cntFinished);
			stats.setProcCompTime(avgRunTime / cntFinished);
			stats.setThroughPut(cntFinished / runClock * params.getPeriod());
		}
		runStats.add(stats);		
		return stats; //CN:modified
	}
	
	protected void generateReport(){		
		int sum = 0;
		double sumDur = 0;
		double sumPST = 0;
		double sumPWT = 0;
		double sumPCT = 0;
		double sumThp = 0;
		
		for (SimRunStats rs : runStats){
			sum += rs.getFinishedCases();
			sumDur += rs.getDuration();
			sumPST += rs.getProcServTime();
			sumPWT += rs.getProcWaitTime();
			sumPCT += rs.getProcCompTime();
			sumThp += rs.getThroughPut();
			
			generateReportServerStats(rs);
			
			generateReportResourceStats(rs);
		}
		
		repStats.setAvgFinishedCases(sum / params.getRuns());
		repStats.setDuration(sumDur / params.getRuns());
		repStats.setProcWaitTime(sumPWT / params.getRuns());
		repStats.setProcServTime(sumPST / params.getRuns());
		repStats.setProcCompTime(sumPCT / params.getRuns());
		repStats.setThroughPut(sumThp / params.getRuns());
		
		generateReportRepStats();
		
		runStats.add(repStats);
	}

	public void generateReportRepStats() {
		for (SimServerStats ss : repStats.getServStats().values()){
			SimReportServerStats rss = (SimReportServerStats)ss;
			rss.setAvgZeroDelays(rss.getAvgZeroDelays() / params.getRuns());
			rss.setAvgCalls(rss.getAvgCalls() / params.getRuns());
			rss.setAvgAccesses(rss.getAvgAccesses() / params.getRuns());
			rss.setAvgDepartures(rss.getAvgDepartures() / params.getRuns());
			rss.setAvgMaxQLength(rss.getAvgMaxQLength() / params.getRuns());
			rss.setAvgMaxResNumber(rss.getAvgMaxResNumber() / params.getRuns());
			rss.setAvgNumServedWhenStopped(rss.getAvgNumServedWhenStopped() / params.getRuns());
			rss.setAvgQLengthWhenStopped(rss.getAvgQLengthWhenStopped() / params.getRuns());
			rss.setAvgQLength(rss.getAvgQLength() / params.getRuns());
			rss.setAvgResNumber(rss.getAvgResNumber() / params.getRuns());
			rss.setAvgWaitTime(rss.getAvgWaitTime() / params.getRuns());
			rss.setMaxWaitTime(rss.getMaxWaitTime() / params.getRuns());
			rss.setAvgServTime(rss.getAvgServTime() / params.getRuns());
		}
		
		for (ResourceStats rrs : repStats.getResStats().values()){
			rrs.setIdleTime(rrs.getIdleTime() / params.getRuns());
			rrs.setUtilizationRatio(rrs.getUtilizationRatio() / params.getRuns());
		}
	}

	public void generateReportResourceStats(SimRunStats rs) {
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

	public void generateReportServerStats(SimRunStats rs) {
		for (SimServerStats ss : rs.getServStats().values()){
			SimServer s = serverList.get(ss.getId());
			if (repStats.getServStats().containsKey(s)){
				SimReportServerStats rss = (SimReportServerStats)repStats.getServStats().get(s);
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
				SimReportServerStats rss = new SimReportServerStats(ss.getName(), ss.getId());
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
				rss.setDistributionLogger(ss.getDistributionLogger());
				repStats.getServStats().put(s, rss);
			}
		}
	}

	public int getRunNumber() {
		return cntRun;
	}

	private void createServerList() {
		// create for each transition a server which stores the statistic values
		createTransitionServer();
		// create the connections between the servers		
		createServerConnections();
		// find corresponding join to the splits
		for (SimServer s : serverList.values()) {
			if(s instanceof SimSplitServer) ((SimSplitServer)s).findJoin();
			if(s instanceof SimJoinSplitServer) ((SimJoinSplitServer)s).findJoin();
		}		
	}

	public void createServerConnections() {
		for (SimServer s : serverList.values()) {
			SimNode n = graph.getNodes().get(s.getid());
			for (SimArc a : n.getarcOut()) {
				String id2 = a.getTarget().getid();				
				SimNode m = graph.getNodes().get(id2);
				if((m.getid().equals(graph.getSink().getid()))&&(n.getarcOut().size()>1)){					
					s.getOutServer().add(new SimOutServer(null, a.getProbability()));					
				}
				for (SimArc b : m.getarcOut()) {				
					SimOutServer os = new SimOutServer(serverList.get(b.getTarget().getid()), a.getProbability()* b.getProbability());
					s.getOutServer().add(os);
				}
				
			}
		}
	}

	public void createTransitionServer() {
		for (SimNode n : graph.getNodes().values()) {
			if (n.isTransition()) {
				SimServer s;
				SimDistribution sd = null;
				if (n.gettime()>0) 
					sd = new SimDistribution(params.getDistServ(),n.gettime(), params.getSParam());
				if (n.isAndJoin() && !n.isAndSplit()) {
					s = new SimJoinServer(this, n.getid(), n.getname(), n
							.getrole(), n.getgroup(), sd);
				} else if (n.isAndSplit() && !n.isAndJoin()) {
					s = new SimSplitServer(this, n.getid(), n.getname(), n
							.getrole(), n.getgroup(), sd);
				} else if (n.isAndSplit() && n.isAndJoin() ) {
					s = new SimJoinSplitServer(this, n.getid(), n.getname(), n
							.getrole(), n.getgroup(), sd);
				} else {
					s = new SimServer(this, n.getid(), n.getname(),
							n.getrole(), n.getgroup(), sd);
				}				
				serverList.put(n.getid(), s);
			}
		}
	}

	public void finishCase(SimCase c){
		cntFinished++;
		avgWait+=c.getwaitTime();
		avgSvc+=c.getsvcTime();
		avgRunTime+=runClock-c.getsysArrivalTime();		
	}

	public void addEvent(SimulatorEvent e) {
		eventList.add(e);
	}

	public void bindRes(Resource r) {
		resUtil.useResource(r);
	}

	public void unbindRes(Resource r) {
		resUtil.freeResource(r);
	}

	public int getCntArrival() {
		return cntArrival;
	}

	public void incCntArrival() {
		cntArrival++;
	}

	public void decCntArrival() {
		cntArrival--;
	}

	public boolean isResUsed() {
		return (params.getResUse() == RES_USED);
	}

	public ResourceUtilization getResUtil() {
		return resUtil;
	}

	public int getQueueDiscipline() {
		return params.getQueue();
	}

	public PriorityQueue<SimulatorEvent> getEventList() {
		return eventList;
	}

	public String printEventList() {
		String s = "[";
		Object[] list = eventList.toArray();
		int l = list.length;

		if (l > 1) {
			for (int i = 0; i < l - 1; i++) {
				s += ((SimulatorEvent) list[i]).getName() + " : "
						+ ((SimulatorEvent) list[l - 1]).getTime() + ",";
			}
		}

		if (l > 0)
			s += ((SimulatorEvent) list[l - 1]).getName() + " : "
					+ ((SimulatorEvent) list[l - 1]).getTime();

		return s + "]";
	}

	public void setAbort(boolean newVal) {
		aborted = newVal;		
	}

	public boolean getAbort(){		
		return aborted;
	}

	public int getFinished() {		
		return cntFinished;
	}
	
	public HashMap<String, SimServer> getServerList(){ 
		return serverList;
	}
	
	public ArrayList<SimRunStats> getRunStats(){
		return runStats;
	}
	
	public SimReportStats getRepStats(){
		return repStats;
	}

	public double getLambda() {
		return params.getLambda();
	}

	public double getPeriod() {
		return params.getPeriod();
	}

	public int[] getDistributionValues() {
		return distLogger.getValues();
	}

	public double getRunClock() {
		return runClock;
	}
	
	public ArrayList<ActivityPanel> getActPanelList() {
		return actPanelList;
	}
	
	public int getMaxRun(){
		return params.getRuns();
	}

	public void addLog(int id, String name, double time) {
		if(params.getWriteLog())
// Joerg Evermann, Feb 18 2016
			log.add(Messages.getString("QuantAna.Simulation.Log.CaseArrival") + " CASE_" + id + " " +
					Messages.getString("QuantAna.Simulation.Log.At") + " TASK '" + name+ "' " +
					Messages.getString("QuantAna.Simulation.Log.AtTime") + String.format( " %.2f", time) + "\n");
//			log.add("CASE_"+id+" an TASK '"+name+"' um "+String.format("%.2f", time)+" angekommen\n");		
	}
	
	public void addLog(String res, String task, int caseid, double svctime, double wttime, double runtime) {
		if(params.getWriteLog())
// Joerg Evermann, Feb 18 2016
			log.add(Messages.getString("QuantAna.Simulation.Log.ResourceBinding") + " RES " + res + " " +
					Messages.getString("QuantAna.Simulation.Log.To") + " TASK '" + task + "' " + 
					Messages.getString("QuantAna.Simulation.Log.AtTime") + String.format(" %.2f", runtime) + "; CASE_" + caseid + " " +
					Messages.getString("QuantAna.Simulation.Log.ServiceTime") + String.format(" %.2f", svctime)+ " " +
					Messages.getString("QuantAna.Simulation.Log.WaitTime") + String.format(" %.2f", wttime)+"\n");
			xesLog.addEvent(caseid, new SimLogEvent(task, res, runtime, svctime));
			
//			log.add("RES "+res+" an TASK '"+task+"' um "+
//					String.format("%.2f", runtime)+" gebunden; CASE_"+caseid+" Bedienzeit "+
//					String.format("%.2f", svctime)+" Wartezeit "+
//					String.format("%.2f", wttime)+"\n");
	}
	
	public ArrayList<String> getLog(){
		return log;
	}
	
	public SimLog getXESLog(){
		return xesLog;
	}
	
}
