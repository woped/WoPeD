package org.woped.qualanalysis.reachabilitygraph.controller;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractEventProcessor;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IUserInterface;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityGraphVC;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityWarning;

import javax.swing.*;
import javax.swing.event.InternalFrameListener;
import java.beans.PropertyVetoException;

public class CoverabilityGraphEventProcessor extends AbstractEventProcessor {

    private AbstractApplicationMediator mediator = null;
    private ReachabilityGraphVCListener rg_listener = null;
    private ReachabilityGraphVC graphVC;

    public CoverabilityGraphEventProcessor(AbstractApplicationMediator mediator) {
        super(mediator);
        this.mediator = mediator;
    }

    @Override
    public void processViewEvent(AbstractViewEvent event) {
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> processViewEvent " + this.getClass().getName());

        switch (event.getOrder()) {
            case AbstractViewEvent.REACHGRAPH:
                createGraphFrame();
                break;
            case CoverabilityGraphViewEvents.CLOSE_FRAME:
                graphVC.close();
                break;
            case CoverabilityGraphViewEvents.REFRESH:
                graphVC.refresh();
                break;
            case CoverabilityGraphViewEvents.UNSELECT:
                graphVC.unselect();
                break;
            case CoverabilityGraphViewEvents.SHOW_SETTINGS:
                graphVC.showSettingsDialog();
                break;
            case CoverabilityGraphViewEvents.EXPORT:
                graphVC.exportImage();
                break;
            case CoverabilityGraphViewEvents.CHANGE_LAYOUT:
                switchLayout(event);
                break;
            case CoverabilityGraphViewEvents.ZOOM_SET:
                setZoom(event);
                break;
            case CoverabilityGraphViewEvents.ZOOM_IN:
                graphVC.zoomIn();
                break;
            case CoverabilityGraphViewEvents.ZOOM_OUT:
                graphVC.zoomOut();
                break;
        }

        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- processViewEvent " + this.getClass().getName());
    }

    private void setZoom(AbstractViewEvent event) {
        try {
            double factor = (double) event.getData();
            graphVC.setZoom(factor);
        } catch (ClassCastException ex) {
            LoggerManager.warn(Constants.QUALANALYSIS_LOGGER, "Could not extract zoom factor from event: " + ex
                    .getLocalizedMessage());
        }
    }

    private void switchLayout(AbstractViewEvent event){
        int type = (int) event.getData();
        graphVC.switchLayout(type);
    }

    private boolean createGraphFrame() {
        if (getMediator().getUi().getEditorFocus() instanceof IEditor) {
            IEditor editor = (IEditor) getMediator().getUi().getEditorFocus();
            IUserInterface userInterface = mediator.getUi();
            JDesktopPane desktop = (JDesktopPane) userInterface.getPropertyChangeSupportBean();

            // when the TokenGame is enabled there is shown a warning message
            if (editor.isTokenGameEnabled()) {
                ReachabilityWarning.showReachabilityWarning(desktop, "QuanlAna.ReachabilityGraph.SimulationWarning");
                return true;
            }
            JInternalFrame editorFrame = desktop.getSelectedFrame();
            graphVC = ReachabilityGraphVC.getInstance(userInterface);

            if (!desktop.isAncestorOf(graphVC)) {
                rg_listener = new ReachabilityGraphVCListener(graphVC, mediator);
                editorFrame.addInternalFrameListener(rg_listener);
                desktop.add(graphVC);
            } else {
                if (!hasAlreadyListener(editorFrame, rg_listener)) {
                    editorFrame.addInternalFrameListener(rg_listener);
                }
            }

            graphVC.setVisible(true);
            graphVC.validate();

            try { // to activate the JInternalFrame
                graphVC.setSelected(true);
            } catch (PropertyVetoException e) { //  if some component does not like this
                graphVC.moveToFront(); // then move it to front without activating
            }

            if (editor.isReachabilityEnabled()) { // is editor already added to RGVC ??
                graphVC.updatePanelsVisibility(editor);
            } else {
                graphVC.addEditor(editor);
                graphVC.refresh();
//                graphVC.refreshGraph(AbstractReachabilityGraphModel.HIERARCHIC);
            }
//				dui.getToolBar().getReachabilityGraphButton().setEnabled(false);
        }
        return false;
    }

    private boolean hasAlreadyListener(JInternalFrame frame, InternalFrameListener listener) {
        InternalFrameListener[] listeners = frame.getInternalFrameListeners();
        for (InternalFrameListener oneofthem : listeners) {
            if (oneofthem == listener) {
                return true;
            }
        }
        return false;
    }
}
