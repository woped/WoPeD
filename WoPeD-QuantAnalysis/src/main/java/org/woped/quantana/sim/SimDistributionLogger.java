package org.woped.quantana.sim;

public class SimDistributionLogger{
	int[] values = null;
	double mean = 0.0;
	
	
	public SimDistributionLogger(double mean){
		values = new int[200];
		for(int i=0;i<values.length;i++)
			values[i]=0;
		this.mean = mean;
		
	}
	
	public void addVal(double val){
		if(mean==0)return;		
		int idx =  (int)((val/mean)*100);
		if((idx>=0)&&(idx<=199)){
			values[idx]++;
		}			
	}
	
	public int[] getValues(){
		return values;
	}

}
