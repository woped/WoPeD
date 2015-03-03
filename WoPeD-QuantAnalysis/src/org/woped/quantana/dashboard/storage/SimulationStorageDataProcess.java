package org.woped.quantana.dashboard.storage;


public class SimulationStorageDataProcess extends SimulationObject {
	
	private double FinishedCases;
	private double Duration;
	private double ProcServTime;
	private double ProcWaitTime;
	private double ProcCompTime;
	private double ThroughPut;
	
	private double AvgFinishedCases;
	private double AvgDuration;
	private double AvgProcServTime;
	private double AvgProcWaitTime;
	private double AvgProcCompTime;
	private double AvgThroughPut;
	
	
	
	//private double RessourceUtilisation = 0.0;
	//private double ThroughputRate = 0.0;
	
	private double DeliveryAdherence = 0.0; //Summe fertiggestellter Ergebnisse ohne Verzug / Summe aller fertiggestellten Ergebnisse
	private double AvgDeliveryAdherence = 0.0;
	
	private double Efficiency = 0.0; // prozess-statistik: Prozess-bedienzeit / prozess-durchlaufzeit
	private double AvgEfficiency = 0.0;
	
	public double getDeliveryAdherence() {
		return DeliveryAdherence;
	}

	private void setAvgDeliveryAdherence() {
		if( this.AvgFinishedCases != 0){
			//LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER, "setAvgDeliveryAdherence: " + UIDCreater.round((AvgThroughPut)) + "/" + UIDCreater.round((AvgFinishedCases)) + " = " + UIDCreater.round((AvgThroughPut / AvgFinishedCases)));
			this.AvgDeliveryAdherence = UIDCreater.round((AvgThroughPut / AvgFinishedCases));
		}
	}

	public double getAvgDeliveryAdherence() {
		return AvgDeliveryAdherence;
	}

	private void setDeliveryAdherence() {
		if( this.FinishedCases != 0 ){
			//LoggerManager.debug(Constants.DASHBOARDSTORE_LOGGER, "setDeliveryAdherence: " + UIDCreater.round((ThroughPut)) + "/" + UIDCreater.round((FinishedCases)) + " = " + UIDCreater.round((ThroughPut / FinishedCases)));

			this.DeliveryAdherence = UIDCreater.round((ThroughPut / FinishedCases));
		}
	}

	public double getEfficiency() {
		return Efficiency;
	}

	private void setEfficiency() {
		if(  ProcServTime != 0 ){
			this.Efficiency = UIDCreater.round(ProcServTime / ProcCompTime);
		}
	}

	public double getAvgEfficiency() {
		return AvgEfficiency;
	}

	public void setAvgEfficiency() {
		if(  AvgProcServTime != 0 ){
			this.AvgEfficiency = UIDCreater.round(AvgProcServTime / AvgProcCompTime);
		}
	}
	
	
	
	
	public SimulationStorageDataProcess() {
		super();
	
	}
	
	public SimulationStorageDataProcess(int finishedCases, double duration,
			double procServTime, double procWaitTime, double procCompTime,
			double throughPut) {
		super();
		FinishedCases = finishedCases;
		Duration = duration;
		ProcServTime = procServTime;
		ProcWaitTime = procWaitTime;
		ProcCompTime = procCompTime;
		ThroughPut = throughPut;
		
		
			
		if( finishedCases != 0 ){
			this.DeliveryAdherence = throughPut / finishedCases;
		}
		
		if(  ProcServTime != 0 ){
			this.Efficiency = ProcServTime / procCompTime;
		}
	}
	
	
	
	public double getFinishedCases() {
		return FinishedCases;
	}
	public void setFinishedCases(double finishedCases) {
		FinishedCases = finishedCases;
		setDeliveryAdherence();
	}
	public double getDuration() {
		return Duration;
	}
	public void setDuration(double duration) {
		Duration = duration;
	}
	public double getProcServTime() {
		return ProcServTime;
	}
	public void setProcServTime(double procServTime) {
		ProcServTime = procServTime;
		setEfficiency();
	}
	public double getProcWaitTime() {
		return ProcWaitTime;
	}
	public void setProcWaitTime(double procWaitTime) {
		ProcWaitTime = procWaitTime;
	}
	public double getProcCompTime() {
		return ProcCompTime;
	}
	public void setProcCompTime(double procCompTime) {
		ProcCompTime = procCompTime;
		setEfficiency();
	}
	public double getThroughPut() {
		return ThroughPut;
	}
	public void setThroughPut(double throughPut) {
		ThroughPut = throughPut;
		setDeliveryAdherence();
	}
	public double getAvgFinishedCases() {
		return AvgFinishedCases;
	}
	public void setAvgFinishedCases(double avgFinishedCases) {
		AvgFinishedCases = avgFinishedCases;
		setAvgDeliveryAdherence();
	}
	public double getAvgDuration() {
		return AvgDuration;
	}
	public void setAvgDuration(double avgDuration) {
		AvgDuration = avgDuration;
	}
	public double getAvgProcServTime() {
		return AvgProcServTime;
	}
	public void setAvgProcServTime(double avgProcServTime) {
		AvgProcServTime = avgProcServTime;
		setAvgEfficiency();
	}
	public double getAvgProcWaitTime() {
		return AvgProcWaitTime;
	}
	public void setAvgProcWaitTime(double avgProcWaitTime) {
		AvgProcWaitTime = avgProcWaitTime;
	}
	public double getAvgProcCompTime() {
		return AvgProcCompTime;
	}
	public void setAvgProcCompTime(double avgProcCompTime) {
		AvgProcCompTime = avgProcCompTime;
		setAvgEfficiency();
	}
	public double getAvgThroughPut() {
		return AvgThroughPut;
	}
	public void setAvgThroughPut(double avgThroughPut) {
		AvgThroughPut = avgThroughPut;
		setAvgDeliveryAdherence();
	}

}
