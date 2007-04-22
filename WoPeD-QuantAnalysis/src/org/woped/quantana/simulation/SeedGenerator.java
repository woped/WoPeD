package org.woped.quantana.simulation;

import java.util.Date;

public class SeedGenerator {
	
	private long lastSeed = 0;
	
	public long nextSeed(){
		Date d = new Date();
		long t = d.getTime();
		
		while (!(t > lastSeed)){
			t++;
		}
		
		lastSeed = t;
		
		return t;
	}
}
