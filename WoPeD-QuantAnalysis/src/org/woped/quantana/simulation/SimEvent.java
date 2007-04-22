package org.woped.quantana.simulation;

public abstract class SimEvent implements Comparable<SimEvent> {
	
	public static final int BIRTH_EVENT	= 1;
	public static final int ARRIVAL_EVENT	= 2;
	public static final int START_EVENT	= 3;
	public static final int RESOURCE_FREED_EVENT	= 4;
	public static final int STOP_EVENT	= 5;
	public static final int DEPARTURE_EVENT	= 6;
	public static final int DEATH_EVENT	= 7;
	
	private Simulator sim = null;
	private double time = 0.0;
//	private int type = 0;
//	private Server server = null;
//	private double moment = 0.0;
//	private Case c = null;
	
	public SimEvent(Simulator sim, double time){//, Server serv, double time, Case c){
//		this.type = type;
		this.sim = sim;
		this.time = time;
//		server = serv;
//		moment = time;
//		this.c = c;
	}

	/*public double getMoment() {
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
	}*/
	
	public abstract void invoke();

	/*public Case getCase() {
		return c;
	}

	public void setCase(Case c) {
		this.c = c;
	}*/

	public Simulator getSim() {
		return sim;
	}

	public void setSim(Simulator sim) {
		this.sim = sim;
	}
	
	public int compareTo(SimEvent e){
		double t = e.getTime();
		if (this.time < t) return -1;
		else if (t < this.time) return 1;
		else return 0;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
}
