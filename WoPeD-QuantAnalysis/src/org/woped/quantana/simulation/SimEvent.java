package org.woped.quantana.simulation;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public abstract class SimEvent implements Comparable<SimEvent> {
	
	public static final int BIRTH_EVENT				= 1;
	public static final int ARRIVAL_EVENT			= 2;
	public static final int START_SERVICE_EVENT		= 3;
	public static final int RESOURCE_FREED_EVENT	= 4;
	public static final int STOP_SERVICE_EVENT		= 5;
	public static final int DEPARTURE_EVENT			= 6;
	public static final int DEATH_EVENT				= 7;
	
	private static int count = 0;
	private static int cntBT = 0;
	private static int cntAR = 0;
	private static int cntST = 0;
	private static int cntRF = 0;
	private static int cntSP = 0;
	private static int cntDP = 0;
	private static int cntDT = 0;
	private static int cntUK = 0;
	
//	private int type;
	private Simulator sim = null;
	private double time = 0.0;
	private String name;

	protected Logger protocol;
	
	public SimEvent(Simulator sim, double time){
		this.sim = sim;
		this.time = time;
		this.protocol = sim.getProtocol();
//		name = getNewName();
	}

	public abstract void invoke();

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
	
	public String getEventTypeName(){
		/*switch(type){
		case 1:
			return "BIRTH_EVENT";
		case 2:
			return "ARRIVAL_EVENT";
		case 3:
			return "START_SERVICE_EVENT";
		case 4:
			return "RESOURCE_FREED_EVENT";
		case 5:
			return "STOP_SERVICE_EVENT";
		case 6:
			return "DEPARTURE_EVENT";
		case 7:
			return "DEATH_EVENT";
		default:
			return "UNKNOWN_EVENT";
		}*/
		
		if (this instanceof BirthEvent) 				return "BIRTH_EVENT";
		else if (this instanceof ArrivalEvent) 			return "ARRIVAL_EVENT";
		else if (this instanceof StartServiceEvent) 	return "START_SERVICE_EVENT";
		else if (this instanceof ResourceFreedEvent)	return "RESOURCE_FREED_EVENT";
		else if (this instanceof StopServiceEvent) 		return "STOP_SERVICE_EVENT";
		else if (this instanceof DepartureEvent) 		return "DEPARTURE_EVENT";
		else if (this instanceof DeathEvent) 			return "DEATH_EVENT";
		else return "UNKNOWN_EVENT";
	}
	
	public static int getCount(SimEvent se){
//		count += 1;
		
		if (se instanceof BirthEvent) {
			cntBT++;
			count = cntBT;
		}
		else if (se instanceof ArrivalEvent) {
			cntAR++;
			count = cntAR;
		}
		else if (se instanceof StartServiceEvent) {
			cntST++;
			count = cntST;
		}
		else if (se instanceof ResourceFreedEvent)	{
			cntRF++;
			count = cntRF;
		}
		else if (se instanceof StopServiceEvent) {
			cntSP++;
			count = cntSP;
		}
		else if (se instanceof DepartureEvent) {
			cntDP++;
			count = cntDP;
		}
		else if (se instanceof DeathEvent) {
			cntDT++;
			count = cntDT;
		}
		else {
			cntUK++;
			count = cntUK;
		}
		
		return count;
	}
	
	public String getNewName(){
		String s = "";
		/*switch(type){
		case 1:
			s += "BT" + getCount();
			break;
		case 2:
			s += "AR" + getCount();
			break;
		case 3:
			s += "ST" + getCount();
			break;
		case 4:
			s += "RF" + getCount();
			break;
		case 5:
			s += "SP" + getCount();
			break;
		case 6:
			s += "DP" + getCount();
			break;
		case 7:
			s += "DT" + getCount();
			break;
		default:
			s += "UK" + getCount();
		}*/
		
		if (this instanceof BirthEvent) 				s += "BT" + SimEvent.getCount(this);
		else if (this instanceof ArrivalEvent) 			s += "AR" + SimEvent.getCount(this);
		else if (this instanceof StartServiceEvent) 	s += "ST" + SimEvent.getCount(this);
		else if (this instanceof ResourceFreedEvent)	s += "RF" + SimEvent.getCount(this);
		else if (this instanceof StopServiceEvent) 		s += "SP" + SimEvent.getCount(this);
		else if (this instanceof DepartureEvent) 		s += "DP" + SimEvent.getCount(this);
		else if (this instanceof DeathEvent) 			s += "DT" + SimEvent.getCount(this);
		else s += "UK" + SimEvent.getCount(this);
		
		return s;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
}
