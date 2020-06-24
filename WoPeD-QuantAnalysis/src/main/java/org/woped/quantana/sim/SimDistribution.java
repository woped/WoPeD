package org.woped.quantana.sim;

import umontreal.iro.lecuyer.randvar.NormalGen;
import umontreal.iro.lecuyer.randvar.PoissonGen;
import umontreal.iro.lecuyer.randvar.RandomVariateGen;
import umontreal.iro.lecuyer.rng.MRG32k3a;
import umontreal.iro.lecuyer.rng.RandomStream;

public class SimDistribution {
	public static final int CONSTANT 		= 0;
	public static final int POISSON		 	= 1;
	public static final int EXPOTENTIONAL	= 2;  
	int type = POISSON;
	double mean = 0.0;
	double param = 0.0;
	int factor = 1;
	RandomStream streamDemand = null;	
	RandomVariateGen gen = null;	
	
	public SimDistribution(int type, double mean, double param) {
		streamDemand = new MRG32k3a();
		this.type = type;
		this.mean = mean;
		this.param = param;
		switch(this.type){
		case POISSON:
			while(this.mean<100){
				factor*=10;
				this.mean*=10;
			}			
			gen = new PoissonGen(streamDemand,this.mean);
			break;
		case EXPOTENTIONAL:			
			while(this.mean<100){
				factor*=10;	
				this.param*=10;
				this.mean*=10;
			}
			gen = new NormalGen(streamDemand,this.mean,this.param);			
			break;	
		}		
	}
	
	public int getType(){
		return type;
	}
	
	public void setType(int newVal){
		type = newVal;
	}
	
	public double getNext(){
		double res = 0;
		switch(type){
		case CONSTANT:
			return ((streamDemand.nextDouble()*2-1)*param+1)*mean;					
		case POISSON:					
			do{
				res = ((double)((PoissonGen)gen).nextInt()/factor);
			}while(res<0);					
			return res;						
		case EXPOTENTIONAL: 	
			do{
				res = ((NormalGen)gen).nextDouble();
			}while(res<0);			
			return res/factor;
		default: return 0.0;		
		}		
	}

	public double getMean() {
		return mean/factor;
	}
}
