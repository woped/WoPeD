package org.woped.qualanalysis.reachabilitygraph.controller;

import javax.swing.JDesktopPane;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractEventProcessor;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IUserInterface;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityGraphVC;

public class ReachabilityGraphEventProcessor extends AbstractEventProcessor {

	AbstractApplicationMediator mediator = null;
	
	public ReachabilityGraphEventProcessor(int vepID, AbstractApplicationMediator mediator) {
		super(vepID, mediator);
		this.mediator = mediator;
	}

	@Override
	public void processViewEvent(AbstractViewEvent event) {
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> processViewEvent " + this.getClass().getName());
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-- processViewEvent " + this.getClass().getName() + " ViewEventType: " + event.getOrder());
		if(event.getOrder() == AbstractViewEvent.REACHGRAPH && getMediator().getUi().getEditorFocus() instanceof IEditor){
			IEditor editor = (IEditor) getMediator().getUi().getEditorFocus();
			IUserInterface dui = mediator.getUi();
			JDesktopPane desktop = (JDesktopPane) dui.getPropertyChangeSupportBean();
			ReachabilityGraphVC toAdd = ReachabilityGraphVC.getInstance(editor);
			if(!desktop.isAncestorOf(toAdd)){
				desktop.add(toAdd);
				toAdd.setVisible(true);
				LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-- processViewEvent " + this.getClass().getName() + " Added new JInternalFrame");
			} else {
				toAdd.setVisible(true);
				LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-- processViewEvent " + this.getClass().getName() + " Called ReachGrah with an IEditor");
			}
		}
		LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- processViewEvent " + this.getClass().getName());
	}
}
