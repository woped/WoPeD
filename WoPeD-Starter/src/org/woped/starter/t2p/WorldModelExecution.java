package org.woped.starter.t2p;

import gui.Initiator;

public class WorldModelExecution implements Runnable {

	static Initiator init = null;

	public void run() {
		//System.out.println("Hello from a thread!");
		init = new Initiator();

	}

	public static Initiator getInitiator() {
		return init;
	}

}
