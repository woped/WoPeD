package org.woped.quantana.sim;


public abstract class SimulatorEvent implements Comparable<SimulatorEvent>{
	
	public static final int EVT_BIRTH 		= 0;
	public static final int EVT_ARRIVE 		= 1;
	public static final int EVT_SVC_START 	= 2;
	public static final int EVT_SVC_END 	= 3;
	public static final int EVT_DEPARTURE 	= 4;
	public static final int EVT_DEATH 		= 6;
	
	private static int cntBIR = 0;
	private static int cntARR = 0;
	private static int cntSTA = 0;
	private static int cntSTO = 0;
	private static int cntDEP = 0;
	private static int cntDEA = 0;
	private static int cntUKN = 0;
		
	double time;
	SimRunner sim;
	String name;
	
	public SimulatorEvent(SimRunner sim, double time){
		this.sim = sim;
		this.time = time;	
		name = getNewName();
	}
	
	public String getName(){
		return name;
	}
	
	public double getTime(){
		return time;
	}
	
	public SimRunner getSim(){
		return sim;		
	}
	
	String form(double val){
		return String.format("%2.2f", val);
	}
	
	public abstract void invoke();	
	
	public int compareTo(SimulatorEvent e){
		double t = e.getTime();
		if (this.time < t) return -1;
		else if (t < this.time) return 1;
		else return 0;
	}
	String getNewName(){
		if(this instanceof SimBirthEvent){
			return "BE"+(++cntBIR);			
		}else if(this instanceof SimArriveEvent){
			return "AE"+(++cntARR);			
		}else if(this instanceof SimStartEvent){
			return "ST"+(++cntSTA);
		}else if(this instanceof SimStopEvent){
			return "SP"+(++cntSTO);
		}else if(this instanceof SimDepartureEvent){
			return "DP"+(++cntDEP);
		}else if(this instanceof SimDeathEvent){
			return "DE"+(++cntDEA);	
		}else return "evtUnknown"+(++cntUKN);		
	}
	
	public static void reset(){
		cntBIR = 0;
		cntARR = 0;
		cntSTA = 0;
		cntSTO = 0;
		cntDEP = 0;
		cntDEA = 0;
		cntUKN = 0;
	}	
}
