package org.woped.simulation;

import flanagan.math.PsRandom;

public class CaseGenerator {
//	public static final int DIST_TYPE_UNIFORM	= 1;
//	public static final int DIST_TYPE_EXP		= 2;
//	public static final int DIST_TYPE_GAUSS		= 3;
	
//	private PsRandom generator = new PsRandom(0);
//	private int typeOfDist = 0;
	private ProbabilityDistribution distribution;
	private int caseCount = 0;
	private double lastArrivalTime = 0.0;
	
	public CaseGenerator(ProbabilityDistribution dist){
		this.distribution = dist;
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
		next.setSysArrivalTime(distribution.getNextRandomValue());
		
		return next;
	}
}
