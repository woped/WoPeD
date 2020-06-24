package org.woped.core.gui;

public interface ITokenGameController {
	
	/**
	 * Status object sent as argument with propertychange events.
	 * Allows controls and other UI elements to update depending
	 * on token game state
	 * 
	 * @author weirdsoul
	 *
	 */
	public class TokenGameStats {
		public int numActiveTransitions;
		public int numActiveSubprocesses;
		public boolean inSubprocess;
		public boolean hasHistory;
		public boolean autoPlayMode;
		public boolean autoPlayPlaying;
	}

	void start();
	void stop();

}
