package org.woped.quantana.simulation;

@SuppressWarnings("unused")
public class CaseGenerator {
	private ProbabilityDistribution distribution;
	private int caseCount = 0;
	private double lastArrivalTime = 0.0;
	private Simulator sim = null;
	
	public CaseGenerator(ProbabilityDistribution dist, Simulator sim){
		this.distribution = dist;
		this.sim = sim;
	}

	public int getCaseCount() {
		return caseCount;
	}

	public void setCaseCount(int caseCount) {
		this.caseCount = caseCount;
	}

	public Case generateNextCase(){
		Case next = new Case(++caseCount);

		double time = distribution.getNextRandomValue();
		lastArrivalTime += time;
		next.setSysArrivalTime(lastArrivalTime);
		sim.getProtocol().info(sim.clckS() + Simulator.getENTRY().getString("Sim.Case.Time.Interarrival") + String.format("%,.2f", time));
		
		return next;
	}

	public double getLastArrivalTime() {
		return lastArrivalTime;
	}

	public void setLastArrivalTime(double lastArrivalTime) {
		this.lastArrivalTime = lastArrivalTime;
	}
}
