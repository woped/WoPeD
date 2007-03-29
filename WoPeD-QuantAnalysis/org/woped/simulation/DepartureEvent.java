package org.woped.simulation;

public abstract class DepartureEvent extends SimEvent {
	
	public DepartureEvent(Simulator sim, Server serv, double time, int caseID){
		super(SimEvent.DEPARTURE_EVENT, sim, serv, time, caseID);
	}
	
	public void invoke(){
		ProtocolItem pi = new ProtocolItem(getSim());

		Simulator.protocolUpdate(pi);
	}
}
