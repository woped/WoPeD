package org.woped.t2p;

import TextToWorldModel.WorldModelBuilder;


public class WorldModelExecution implements Runnable {

	static WorldModelBuilder WMBuilder = null;
	public WorldModelExecution(String ProcessDescriptionText){
		WMBuilder= new WorldModelBuilder(ProcessDescriptionText);
	}
	public void run() {
		//System.out.println("Hello from a thread!");


	}

	public static WorldModelBuilder getWorldModelBuilder() {
		return WMBuilder;
	}

}
