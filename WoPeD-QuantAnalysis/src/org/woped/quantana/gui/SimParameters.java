package org.woped.quantana.gui;

public class SimParameters {
	private int runs;
	private int distCases;
	private int distServ;
	private int queue;
	private int stop;
	
	private double cPara1;
	private double cPara2;
//	private double sPara1;
//	private double sPara2;
	
	private double lambda;
	private double timeOfPeriod;
	
	private int resUse;
	
	public SimParameters(){
		super();
	}
	
	public SimParameters(int runs, int dc, int ds, int q, int s, double cp1, double cp2, int res){
		this.runs = runs;
		distCases = dc;
		distServ = ds;
		queue = q;
		stop = s;
		cPara1 = cp1;
		cPara2 = cp2;
//		sPara1 = sp1;
//		sPara2 = sp2;
		resUse = res;
	}

	public double getCPara1() {
		return cPara1;
	}

	public void setCPara1(double para1) {
		cPara1 = para1;
	}

	public double getCPara2() {
		return cPara2;
	}

	public void setCPara2(double para2) {
		cPara2 = para2;
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

	/*public double getSPara1() {
		return sPara1;
	}

	public void setSPara1(double para1) {
		sPara1 = para1;
	}

	public double getSPara2() {
		return sPara2;
	}

	public void setSPara2(double para2) {
		sPara2 = para2;
	}*/

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

	public double getTimeOfPeriod() {
		return timeOfPeriod;
	}

	public void setTimeOfPeriod(double timeOfPeriod) {
		this.timeOfPeriod = timeOfPeriod;
	}

	public int getResUse() {
		return resUse;
	}

	public void setResUse(int resUse) {
		this.resUse = resUse;
	}
}
