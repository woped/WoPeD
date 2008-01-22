package org.woped.qualanalysis.simulation;
import  org.woped.qualanalysis.simulation.controller.*;

/**
 * This class is needed for AutoPlayback Action in Tokengame.
 * To prevent any collisions with the main thread, in TokenGameBarVC this
 * Runnable Object is called and will move the token through the petrinet.
 * Currently all actions are implemented as methods in TokenGameBarVC.
 * A reference of TokenGameBarVC will be provided by constructor.
 * 
 * V.10	18.01.2007
 * Implemented main functionalities
 * 
 * @author Anthony Soprano
 * 
 */

public class TokenGameRunnableObject implements Runnable {
	
	TokenGameBarController tb = null;
	
	public TokenGameRunnableObject(TokenGameBarController tgbv)
	{
		tb = tgbv;
	}

	public void run() {
		if(tb.isBackward())
		{
			tb.moveBackward();
		}
		else
		{
			tb.moveForward();
		}

	}

}
