package org.woped.qualanalysis.reachabilitygraph.controller;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.IEditorFrame;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityGraphVC;

/**
 * This class observes the related editor frame and notifies the reachgraphVC on changes
 */
public class ReachabilityGraphVCListener implements InternalFrameListener {

    private ReachabilityGraphVC reachGraphVC = null;
    private AbstractApplicationMediator aam = null;

    public ReachabilityGraphVCListener(ReachabilityGraphVC rgvc, AbstractApplicationMediator aam) {
        this.reachGraphVC = rgvc;
        this.aam = aam;
    }

    public void internalFrameActivated(InternalFrameEvent arg0) {
        reachGraphVC.updatePanelsVisibility(aam.getUi().getEditorFocus());
 //       aam.getUI.getCoverabilityGraphButton().setEnabled(true);
    }

    public void internalFrameClosed(InternalFrameEvent arg0) {
        IEditor editor = ((IEditorFrame) arg0.getSource()).getEditor();
        this.reachGraphVC.removePanel(editor);
//        aam.getUi().getCoverabilityGraphButton().setEnabled(false);
        reachGraphVC.updatePanelsVisibility(aam.getUi().getEditorFocus());
    }

    public void internalFrameClosing(InternalFrameEvent arg0) {
        reachGraphVC.updatePanelsVisibility(aam.getUi().getEditorFocus());
    }

    public void internalFrameDeactivated(InternalFrameEvent arg0) {}

    public void internalFrameDeiconified(InternalFrameEvent arg0) {}

    public void internalFrameIconified(InternalFrameEvent arg0) {}

    public void internalFrameOpened(InternalFrameEvent arg0) {}
}
