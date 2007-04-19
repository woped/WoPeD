package org.woped.quantana.simulation;

import flanagan.math.PsRandom;

public class ProbabilityDistribution {
	
	public static final int DIST_TYPE_UNIFORM	= 1;
	public static final int DIST_TYPE_EXP		= 2;
	public static final int DIST_TYPE_GAUSS		= 3;
	
	private int typeOfDist = 0;
	private double param1 = 0.0;
	private double param2 = 0.0;
	private long seed = 0;
	
	private double mean;
	private double variance;
	private PsRandom generator;
	
	public ProbabilityDistribution(int type, double param1, double param2, long seed){
		this.typeOfDist = type;
		this.param1 = param1;
		this.param2 = param2;
		this.seed = seed;
		
		generator = new PsRandom(seed);
		
		getCharacteristics();
	}

	public double getParam1() {
		return param1;
	}

	public void setParam1(double param1) {
		this.param1 = param1;
	}

	public double getParam2() {
		return param2;
	}

	public void setParam2(double param2) {
		this.param2 = param2;
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
			// param1 = untere Grenze
			// param2 = obere Grenze
			mean = (param1 + param2)/2;
			variance = (param2 - param1)/2;
			break;
		case 2:
			// param1 = Erwartungswert
			// param2 = Position (i.d.R. = 0)
			mean = param1;
			variance = param1 * param1;
			break;
		case 3:
			// param1 = Erwartungswert
			// param2 = Standardabweichung
			mean = param1;
			variance = param2 * param2;
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
			result = generator.nextDouble() * (param1 - param2) + param1;
			break;
		case 2:
			result = generator.nextExponential(0, 1/param1);
			break;
		case 3:
			result = generator.nextGaussian(param1, param2);
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
