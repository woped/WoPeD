package org.woped.quantana.simulation;

public class DepartureEvent extends SimEvent {
	
	public DepartureEvent(Simulator sim, Server serv, double time, Case c){
		super(SimEvent.DEPARTURE_EVENT, sim, serv, time, c);
	}
	
	public void invoke(){
		ProtocolItem pi = new ProtocolItem(getSim());
		
		Server s = getServer();
		Case c = getCase();
		
		if (!(s.getQueue().isEmpty())){
			updateServerStatistics(s, c);
			scheduleDeparture(c);
		} else {
			s.setStatus(Server.STATUS_IDLE);
			s.incNumCasesInParallel(-1);
		}
		
		double t = getSim().getClock();
		double wt = t - c.getCurrentArrivalTime();
		if (wt > s.getMaxWaitTimeOfCase()) s.setMaxWaitTimeOfCase(wt);

		if (s.getSuccessor().size() == 0) c.setSysDepartureTime(t);
		else {
			ArrivalEvent ae = new ArrivalEvent(getSim(), s.gotoNextServer(), t, c);
			getSim().getEventList().add(ae);
		}
		
		getSim().protocolUpdate(pi);
	}
	
	private void updateServerStatistics(Server s, Case c){
		s.incNumCalls(1);
		s.incNumDeparture(1);
	}
	
	private void scheduleDeparture(Case c){
		
	}
}
