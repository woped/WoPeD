package org.woped.qualanalysis.coverabilitygraph.gui;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractEventProcessor;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.CoverabilityGraphLayout;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphView;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphViewEvents;

/**
 * This class listens for application events related to the coverability graph and calls the appropriates methods if an event occurs.
 */
public class CoverabilityGraphEventProcessor extends AbstractEventProcessor {

    private AbstractApplicationMediator mediator = null;
    private CoverabilityGraphFrameController graphVC;

    /**
     * Constructs a new coverability graph event processor.
     *
     * @param mediator the mediator of the application
     */
    public CoverabilityGraphEventProcessor(AbstractApplicationMediator mediator) {
        super(mediator);
        this.mediator = mediator;

        // cg frame controller can't be initialized here because the user interface has not been created at this point
    }

    @Override
    public void processViewEvent(AbstractViewEvent event) {
        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "-> processViewEvent " + this.getClass().getName());

        if (graphVC == null) initializeGraphFrame();
        chooseAction(event);

        LoggerManager.debug(Constants.QUALANALYSIS_LOGGER, "<- processViewEvent " + this.getClass().getName());
    }

    private void chooseAction(AbstractViewEvent event) {
        switch (event.getOrder()) {
            case AbstractViewEvent.REACHGRAPH:
                addCoverabilityGraph();
                break;
            case CoverabilityGraphViewEvents.CLOSE_FRAME:
                removeCoverabilityGraph();
                break;
            case CoverabilityGraphViewEvents.REFRESH:
                graphVC.synchronizeEditor();
                break;
            case CoverabilityGraphViewEvents.UNSELECT:
                graphVC.getActiveGraph().removeHighlighting();
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
                zoom(true);
                break;
            case CoverabilityGraphViewEvents.ZOOM_OUT:
                zoom(false);
                break;
            case CoverabilityGraphViewEvents.ASSISTANT_CLOSE:
                graphVC.enableResultView();
                break;
            case CoverabilityGraphViewEvents.ASSISTANT_START:
                graphVC.enableAssistantView();
                break;
            case CoverabilityGraphViewEvents.ASSISTANT_RESET:
                graphVC.getActiveGraph().reset();
                break;
        }
    }

    private void addCoverabilityGraph() {
        IEditor editor = getMediator().getUi().getEditorFocus();
        graphVC.showGraph(editor);
    }

    private void removeCoverabilityGraph(){
        IEditor editor = getMediator().getUi().getEditorFocus();
        graphVC.removeGraph(editor);
    }

    private void initializeGraphFrame() {
        this.graphVC = CoverabilityGraphFrameController.getInstance(mediator.getUi());
    }

    private void zoom(boolean zoomIn) {
        CoverabilityGraphView view = graphVC.getActiveGraph().getActiveView();

        if (zoomIn) view.getZoomController().zoomIn();
        else view.getZoomController().zoomOut();
    }

    private void setZoom(AbstractViewEvent event) {
        try {
            double factor = (double) event.getData();
            graphVC.getActiveGraph().getActiveView().getZoomController().setZoom(factor);
        } catch (ClassCastException ex) {
            LoggerManager.warn(Constants.QUALANALYSIS_LOGGER, "Could not extract zoom factor from event: " + ex
                    .getLocalizedMessage());
        }
    }

    private void switchLayout(AbstractViewEvent event) {
        CoverabilityGraphLayout layout = (CoverabilityGraphLayout) event.getData();
        CoverabilityGraphView view = graphVC.getActiveGraph().getActiveView();
        CoverabilityGraphSettings settings = view.getSettings();
        settings.layout = layout;
        view.applySettings(settings);
    }
}
