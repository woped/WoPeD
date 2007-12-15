package org.woped.qualanalysis.reachabilitygraph.controller;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityGraphVC;

public class ReachabilityGraphVCListener implements InternalFrameListener{
	
	private ReachabilityGraphVC reachGraphVC = null;
	private AbstractApplicationMediator aam = null;
	private IEditor toBeClosed = null;
	
	public ReachabilityGraphVCListener(ReachabilityGraphVC rgvc, AbstractApplicationMediator aam) {
		this.reachGraphVC = rgvc;
		this.aam = aam;
	}
	
	public void internalFrameActivated(InternalFrameEvent arg0) {
		reachGraphVC.updatePanelsVisibility(aam.getUi().getEditorFocus());
	}

	public void internalFrameClosed(InternalFrameEvent arg0) {
		if(this.toBeClosed == null){
			this.reachGraphVC.updatePanelsVisibility(aam.getUi().getEditorFocus());
		} else {
			this.reachGraphVC.removePanel(toBeClosed);
		}
	}

	public void internalFrameClosing(InternalFrameEvent arg0) {
		this.toBeClosed = aam.getUi().getEditorFocus();
	}

	public void internalFrameDeactivated(InternalFrameEvent arg0) {
		
	}

	public void internalFrameDeiconified(InternalFrameEvent arg0) {
		
	}

	public void internalFrameIconified(InternalFrameEvent arg0) {
		
	}

	public void internalFrameOpened(InternalFrameEvent arg0) {
		
	}
}
