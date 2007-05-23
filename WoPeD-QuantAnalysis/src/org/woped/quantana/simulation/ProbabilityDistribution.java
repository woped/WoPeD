package org.woped.quantana.simulation;

import flanagan.math.PsRandom;

public class ProbabilityDistribution {
	
	public static final int DIST_TYPE_UNIFORM	= 1;
	public static final int DIST_TYPE_EXP		= 2;
	public static final int DIST_TYPE_GAUSS		= 3;
	
	private int typeOfDist = 0;
	private double param = 0.0;
	private long seed = 0;
	
	private double mean;
	private double variance;
	private PsRandom generator;
	
	public ProbabilityDistribution(int type, double mean, double param, long seed){
		this.typeOfDist = type;
		this.mean = mean;
		this.param = param;
		this.seed = seed;
		
		generator = new PsRandom(seed);
		
		getCharacteristics();
	}

	public double getParam() {
		return param;
	}

	public void setParam(double param) {
		this.param = param;
	}

	public int getTypeOfDist() {
		return typeOfDist;
	}

	public void setTypeOfDist(int typeOfDist) {
		this.typeOfDist = typeOfDist;
	}
	
	private void getCharacteristics(){
		switch (typeOfDist){
		case 1:
			variance = mean * param;
			break;
		case 2:
			variance = mean * mean;
			break;
		case 3:
			variance = param * param;
			break;
		default:
			mean = 0.0;
			variance = 0.0;
		}
	}

	public double getMean() {
		return mean;
	}

	public double getVariance() {
		return variance;
	}
	
	public double getNextRandomValue(){
		double result = 0;
		switch (typeOfDist){
		case 1:
			result = ((generator.nextDouble() * 2 - 1) * param + 1) * mean;
			break;
		case 2:
			result = generator.nextExponential(param, mean);
			break;
		case 3:
			result = generator.nextGaussian(mean, param);
		}
		
		return result;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public long getSeed() {
		return seed;
	}
}
