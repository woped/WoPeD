package org.woped.quantana.sim;

public class SimParameters {
	private int runs;
	private int distCases;
	private int distServ;
	private int queue;
	private int stop;
	private boolean wlog;
	
	private double cParam	= 0.0;
	private double sParam	= 0.0;
	
	private double lambda;
	private double period;
	
	private int resUse;
	
	public SimParameters(){
		super();
	}
	
	public SimParameters(double l, double pd, int r, int dc, int ds, int q,
			int s, double cp, double sp, int res, boolean writelog) {
		lambda = l;
		period = pd;
		runs = r;
		distCases = dc;
		distServ = ds;
		queue = q;
		stop = s;
		cParam = cp;
		sParam = sp;
		resUse = res;
		wlog = writelog;
	}
	
	public SimParameters(double l, double pd){
		lambda = l;
		period = pd;
	}

	public double getCParam() {
		return cParam;
	}

	public void setCParam(double cp) {
		cParam = cp;
	}

	public double getSParam() {
		return sParam;
	}

	public void setSParam(double sp) {
		sParam = sp;
	}

	public int getDistCases() {
		return distCases;
	}

	public void setDistCases(int distCases) {
		this.distCases = distCases;
	}

	public int getDistServ() {
		return distServ;
	}

	public void setDistServ(int distServ) {
		this.distServ = distServ;
	}

	public int getQueue() {
		return queue;
	}

	public void setQueue(int queue) {
		this.queue = queue;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getStop() {
		return stop;
	}

	public void setStop(int stop) {
		this.stop = stop;
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public double getPeriod() {
		return period;
	}

	public void setPeriod(double timeOfPeriod) {
		this.period = timeOfPeriod;
	}

	public int getResUse() {
		return resUse;
	}

	public void setResUse(int resUse) {
		this.resUse = resUse;
	}
	
	public void setWriteLog(boolean writelog){
		wlog = writelog;
	}
	
	public boolean getWriteLog(){
		return wlog;
	}
}
