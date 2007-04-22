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

	/*public int getTypeOfDist() {
		return typeOfDist;
	}

	public void setTypeOfDist(int typeOfDist) {
		this.typeOfDist = typeOfDist;
	}*/
	
	public Case generateNextCase(){
		Case next = new Case(++caseCount);
		/*switch (typeOfDist){
		case DIST_TYPE_UNIFORM:
			next.setSysArrivalTime(generator.nextDouble());
			break;
		case DIST_TYPE_GAUSS:
			next.setSysArrivalTime(generator.nextGaussian(0, 0));
			break;
		default:
			next.setSysArrivalTime(generator.nextExponential(0, 0));
		}*/
		
		lastArrivalTime += distribution.getNextRandomValue();
		next.setSysArrivalTime(lastArrivalTime);
		
		return next;
	}

	public double getLastArrivalTime() {
		return lastArrivalTime;
	}

	public void setLastArrivalTime(double lastArrivalTime) {
		this.lastArrivalTime = lastArrivalTime;
	}
}
