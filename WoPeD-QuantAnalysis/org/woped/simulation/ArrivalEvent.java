package org.woped.simulation;

public class ArrivalEvent extends SimEvent{
	
	public ArrivalEvent(Simulator sim, Server serv, double time, int caseID){
		super(SimEvent.ARRIVAL_EVENT, sim, serv, time, caseID);
	}
	
	public void invoke(){
		ProtocolItem pi = new ProtocolItem(getSim());
		
		Simulator.protocolUpdate(pi);
	}
}
