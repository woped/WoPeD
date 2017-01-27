package org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.reachabilitygraph.assistant.CoverabilityGraphAssistant;
import org.woped.qualanalysis.reachabilitygraph.assistant.sidebar.SidebarVC;
import org.woped.qualanalysis.reachabilitygraph.assistant.event.CoverabilityGraphAdapter;
import org.woped.qualanalysis.reachabilitygraph.assistant.event.CoverabilityGraphListener;
import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.model.MpNode;
import org.woped.qualanalysis.reachabilitygraph.data.model.CoverabilityGraphNode;
import org.woped.qualanalysis.reachabilitygraph.gui.views.formatters.NodeFormatter;
import org.woped.qualanalysis.soundness.builder.BuilderFactory;
import org.woped.qualanalysis.soundness.builder.lowlevelpetrinet.AbstractLowLevelPetriNetBuilder;
import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

public class CoverabilityGraphAssistantUsingMonotonePruning implements CoverabilityGraphAssistant {

    private final MonotonePruningEventTrigger eventTrigger;
    private final MonotonePruningVisualStateManager stateManager;
    private final MonotonePruningInfoProvider infoProvider;
    private final MonotonePruningGraphBuilder builder;
    private IEditor editor;

    public CoverabilityGraphAssistantUsingMonotonePruning(IEditor editor, SidebarVC sidebarVC, NodeFormatter formatter) {
        this.editor = editor;
        eventTrigger = new MonotonePruningEventTrigger(this);
        builder = new MonotonePruningGraphBuilder(eventTrigger);
        stateManager = new MonotonePruningVisualStateManager(eventTrigger, formatter);
        infoProvider = new MonotonePruningInfoProvider(eventTrigger, sidebarVC);

        addCoverabilityGraphListener(new GraphListener());
    }

    @Override
    public void initialize() {

        AbstractLowLevelPetriNetBuilder converter = BuilderFactory.createLowLevelPetriNetWithoutTStarBuilder(editor);
        ILowLevelPetriNet lowLevelPetriNet = converter.getLowLevelPetriNet();
        IMarkingNet markingNet = new MarkingNet(lowLevelPetriNet);
        builder.initialize(markingNet);
    }

    @Override
    public void selectNode(CoverabilityGraphNode node) {
        deselect();
        eventTrigger.fireNodeSelectedEvent(node);
    }

    @Override
    public void processNode(CoverabilityGraphNode node) {
        deselect();
        eventTrigger.fireNodeProcessingRequest((MpNode) node);
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


    private class GraphListener extends CoverabilityGraphAdapter {

        @Override
        public void deselectGraphRequested() {
            deselect();
        }
    }
}
