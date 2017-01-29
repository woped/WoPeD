package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.coverabilitygraph.assistant.CoverabilityGraphAssistant;
import org.woped.qualanalysis.coverabilitygraph.assistant.CoverabilityGraphAssistantModel;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNodeState;
import org.woped.qualanalysis.coverabilitygraph.assistant.sidebar.SidebarVC;
import org.woped.qualanalysis.coverabilitygraph.assistant.event.CoverabilityGraphAdapter;
import org.woped.qualanalysis.coverabilitygraph.assistant.event.CoverabilityGraphListener;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNode;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.coverabilitygraph.events.CoverabilityGraphMouseAdapter;
import org.woped.qualanalysis.coverabilitygraph.events.CoverabilityGraphMouseListener;
import org.woped.qualanalysis.coverabilitygraph.events.EmptySpaceClickedEvent;
import org.woped.qualanalysis.coverabilitygraph.events.NodeClickedEvent;

/**
 * This class guides the user through the process of building the minimal coverability graph (set) introduced by
 * Pierre-Alain Reynier and Frederic Servais in their paper Minimal Coverability Set for Petri Nets: Karp and Miller Algorithm with Pruning
 */
public class CoverabilityGraphAssistantUsingMonotonePruning implements CoverabilityGraphAssistant {

    private final MonotonePruningEventTrigger eventTrigger;
    private final MonotonePruningVisualStateManager stateManager;
    private final MonotonePruningInfoProvider infoProvider;
    private final MonotonePruningGraphBuilder builder;
    private final GraphMouseListener graphMouseListener;

    /**
     * Constructs a new coverability graph assistant.
     *
     * @param editor the editor of the petri net
     * @param sidebarVC the controller of the sidebar
     * @param graphModel the model of the visual graph
     */
    public CoverabilityGraphAssistantUsingMonotonePruning(IEditor editor, SidebarVC sidebarVC, CoverabilityGraphAssistantModel graphModel) {
        eventTrigger = new MonotonePruningEventTrigger(this);
        builder = new MonotonePruningGraphBuilder(editor, eventTrigger);
        stateManager = new MonotonePruningVisualStateManager(eventTrigger, graphModel.getViewFactory().getNodeFormatter());
        infoProvider = new MonotonePruningInfoProvider(eventTrigger, sidebarVC, graphModel.getViewFactory().getNodeFormatter());

        graphMouseListener = new GraphMouseListener();

        this.addCoverabilityGraphListener(graphModel.getGraphListener());
        addCoverabilityGraphListener(new GraphListener());

        initialize();
    }

    @Override
    public void initialize() {
        builder.initialize();
    }

    @Override
    public void selectNode(CoverabilityGraphNode node) {
        deselect();
        eventTrigger.fireNodeSelectedEvent(node);
        eventTrigger.fireRefreshRequest();
    }

    @Override
    public void processNode(CoverabilityGraphNode node) {
        deselect();
        MpNode n = (MpNode) node;
        if(n.getState() == MpNodeState.UNPROCESSED) eventTrigger.fireNodeProcessingRequest(n);
        else selectNode(n);
    }

    @Override
    public void deselect() {
        infoProvider.clear();
        stateManager.removeHighlighting();
    }

    @Override
    public void reset() {
        eventTrigger.firePrepareRestartEvent();
        eventTrigger.fireResetAssistantRequestEvent();
    }

    @Override
    public void addCoverabilityGraphListener(CoverabilityGraphListener listener) {
        eventTrigger.addGraphListener(listener);
    }

    @Override
    public CoverabilityGraphMouseListener getMouseListener() {
        return graphMouseListener;
    }

    /**
     * Automatically calls the appropriate methods based on the user interaction with the graph.
     */
    private class GraphMouseListener extends CoverabilityGraphMouseAdapter{

        @Override
        public void nodeClicked(NodeClickedEvent event) {
            if(event.getClickCount() == 1) selectNode(event.getNode());
            else if( event.getClickCount() == 2) processNode(event.getNode());
        }

        @Override
        public void emptySpaceClicked(EmptySpaceClickedEvent event) {
            if(event.getClickCount() == 1) deselect();
            else if(event.getClickCount() == 2) eventTrigger.fireConvert2GraphRequestEvent();
        }
    }

    /**
     * Automatically calls the appropriate method when a graph event occurs.
     */
    private class GraphListener extends CoverabilityGraphAdapter {

        @Override
        public void deselectGraphRequested() {
            deselect();
        }
    }
}
