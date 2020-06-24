package org.woped.quantana.dashboard.storage;

public class SimulationRessourceAllocData {

	
	
	private String server = "";
	
	private String ressource = "";
	
	double TimeStart = 0.0;
	
	double TimeStop = 0.0;

	
	
	
	public SimulationRessourceAllocData(String ressource , String server, double timeStart,
			double timeStop) {
		super();
		this.server = server;
		this.ressource = ressource;
		this.TimeStart = timeStart;
		this.TimeStop = timeStop;
	}

	
	
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public double getTimeStart() {
		return TimeStart;
	}

	public void setTimeStart(double timeStart) {
		TimeStart = timeStart;
	}

	public double getTimeStop() {
		return TimeStop;
	}

	public void setTimeStop(double timeStop) {
		TimeStop = timeStop;
	}
	
	
	public String getRessource() {
		return ressource;
	}

	public void setRessource(String ressource) {
		this.ressource = ressource;
	}
	
}
