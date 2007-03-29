package org.woped.simulation;

public abstract class SimEvent {
	public static final int ARRIVAL_EVENT	= 1;
	public static final int DEPARTURE_EVENT	= 2;
	
	private Simulator sim = null;
	private int type = 0;
	private Server server = null;
	private double moment = 0;
	private int caseID = 0;
	
	public SimEvent(int type, Simulator sim, Server serv, double time, int caseID){
		this.type = type;
		this.sim = sim;
		server = serv;
		moment = time;
		this.caseID = caseID;
	}

	public double getMoment() {
		return moment;
	}

	public void setMoment(double moment) {
		this.moment = moment;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public abstract void invoke();

	public int getCaseID() {
		return caseID;
	}

	public void setCaseID(int caseID) {
		this.caseID = caseID;
	}

	public Simulator getSim() {
		return sim;
	}

	public void setSim(Simulator sim) {
		this.sim = sim;
	}
}
