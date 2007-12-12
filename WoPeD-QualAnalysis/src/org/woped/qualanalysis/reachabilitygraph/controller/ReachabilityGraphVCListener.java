package org.woped.qualanalysis.reachabilitygraph.controller;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityGraphVC;

public class ReachabilityGraphVCListener implements InternalFrameListener{
	
	ReachabilityGraphVC reachGraphVC = null;
	AbstractApplicationMediator aam = null;
	
	public ReachabilityGraphVCListener(ReachabilityGraphVC rgvc, AbstractApplicationMediator aam) {
		this.reachGraphVC = rgvc;
		this.aam = aam;
	}

	public void internalFrameActivated(InternalFrameEvent arg0) {
		//LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-- activated " + this.getClass().getName() + " Focus on " + aam.getUi().getEditorFocus().getName());
		reachGraphVC.updatePanelsVisibility(aam.getUi().getEditorFocus());
	}

	public void internalFrameClosed(InternalFrameEvent arg0) {
		//LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-- closed " + this.getClass().getName() + " ");
	}

	public void internalFrameClosing(InternalFrameEvent arg0) {
		
	}

	public void internalFrameDeactivated(InternalFrameEvent arg0) {
		//LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-- deactivated " + this.getClass().getName() + " ");
	}

	public void internalFrameDeiconified(InternalFrameEvent arg0) {
		
	}

	public void internalFrameIconified(InternalFrameEvent arg0) {
		
	}

	public void internalFrameOpened(InternalFrameEvent arg0) {
		
	}
}
