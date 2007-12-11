package org.woped.qualanalysis.reachabilitygraph.gui;

import javax.swing.JPanel;

import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;

public class ReachabilityGraphPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public ReachabilityGraphPanel(IEditor editor) {
		super();
		init();
	}
	
	private void init() {
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> init() " + this.getClass().getName());
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- init() " + this.getClass().getName());
	}

}
