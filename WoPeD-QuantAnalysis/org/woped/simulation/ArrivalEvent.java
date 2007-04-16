package org.woped.simulation;

public class ArrivalEvent extends SimEvent{
	
	public ArrivalEvent(Simulator sim, Server serv, double time, Case c){
		super(SimEvent.ARRIVAL_EVENT, sim, serv, time, c);
	}
	
	public void invoke(){
		ProtocolItem pi = new ProtocolItem(getSim());
		
		Server s = getServer();
		Case c = getCase();
		
		switch (s.getStatus()){
		case Server.STATUS_IDLE:
			updateServerStatistics(s);
			s.setStatus(Server.STATUS_BUSY);
			scheduleDeparture(s, c);
			break;
		default:
			putCaseInQueue(s, c);
		}
		
		getSim().protocolUpdate(pi);
	}
	
	private void updateServerStatistics(Server s){
		s.incNumCalls(1);
		s.incZeroDelays(1);
		s.incNumAccess(1);
		
		if (getSim().getUseResAlloc() == Simulator.RES_NOT_USED){
			s.incNumCasesInParallel(1);
		}
		
		int len = s.getQueue().size();
		if (len > s.getMaxQueueLength()) s.setMaxQueueLength(len);
		
		int par = s.getNumCasesInParallel();
		if (par > s.getMaxNumCasesInParallel()) s.setMaxNumCasesInParallel(par);
		
		s.setCurCase(getCase());
	}
	
	private void scheduleDeparture(Server s, Case c){
		double t = s.getNextServTime() + getMoment();
		DepartureEvent de = new DepartureEvent(getSim(), s, t, c);
		getSim().getEventList().add(de);
	}
	
	private void putCaseInQueue(Server s, Case c){
		s.getQueue().add(c);
	}
}
