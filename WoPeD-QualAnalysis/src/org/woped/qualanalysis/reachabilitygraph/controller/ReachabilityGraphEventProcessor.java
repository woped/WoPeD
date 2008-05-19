package org.woped.qualanalysis.reachabilitygraph.controller;

import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameListener;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractEventProcessor;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IUserInterface;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityGraphModel;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityGraphVC;

public class ReachabilityGraphEventProcessor extends AbstractEventProcessor {

	private AbstractApplicationMediator mediator = null;
	private ReachabilityGraphVCListener rg_listener = null;

	public ReachabilityGraphEventProcessor(int vepID, AbstractApplicationMediator mediator) {
		super(vepID, mediator);
		this.mediator = mediator;
	}

	@Override
	public void processViewEvent(AbstractViewEvent event) {
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> processViewEvent " + this.getClass().getName());
		if(getMediator().getUi().getEditorFocus() instanceof IEditor /*&& !getMediator().getUi().getEditorFocus().isSubprocessEditor()*/){
			if(event.getOrder() == AbstractViewEvent.REACHGRAPH && getMediator().getUi().getEditorFocus() instanceof IEditor){
				IEditor editor = (IEditor) getMediator().getUi().getEditorFocus();
				IUserInterface dui = mediator.getUi();
				JDesktopPane desktop = (JDesktopPane) dui.getPropertyChangeSupportBean();
				JInternalFrame edit = desktop.getSelectedFrame();
				ReachabilityGraphVC toAdd = ReachabilityGraphVC.getInstance(dui);
				if(!desktop.isAncestorOf(toAdd)){
					rg_listener = new ReachabilityGraphVCListener(toAdd, mediator);
					edit.addInternalFrameListener(rg_listener);
					desktop.add(toAdd);
				} else {
					if(!hasAlreadyListener(edit, rg_listener)){
						edit.addInternalFrameListener(rg_listener);
					}
				}
				toAdd.setVisible(true);
				toAdd.validate();
				try { // to activate the JInternalFrame
					toAdd.setSelected(true);
				} catch (PropertyVetoException e) { //  if some component does not like this
					toAdd.moveToFront(); // then move it to front without activating
				}
				if(editor.isReachabilityEnabled()){ // is editor already added to RGVC ??
					toAdd.updatePanelsVisibility(editor);
				} else {
					toAdd.addEditor(editor);
					toAdd.refreshGraph(ReachabilityGraphModel.HIERARCHIC);
				}
				dui.getToolBar().getReachabilityGraphButton().setEnabled(false);
			}
		}
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- processViewEvent " + this.getClass().getName());
	}

	private boolean hasAlreadyListener(JInternalFrame frame, InternalFrameListener listener){
		InternalFrameListener[] listeners = frame.getInternalFrameListeners();
		for(InternalFrameListener oneofthem : listeners){
			if(oneofthem == listener){
				return true;
			}
		}
		return false;
	}

}
