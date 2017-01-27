package org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp;

import org.jgraph.graph.AttributeMap;
import org.woped.qualanalysis.reachabilitygraph.assistant.CoverabilityGraphAssistant;
import org.woped.qualanalysis.reachabilitygraph.assistant.event.*;
import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event.*;
import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.model.MpNode;
import org.woped.qualanalysis.reachabilitygraph.data.model.CoverabilityGraphEdge;
import org.woped.qualanalysis.reachabilitygraph.data.model.CoverabilityGraphNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.IMarking;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MonotonePruningEventTrigger {

    private Set<CoverabilityGraphListener> graphListeners;
    private Set<MonotonePruningAnalysisListener> analysisListeners;
    private CoverabilityGraphAssistant source;

    MonotonePruningEventTrigger(CoverabilityGraphAssistant source) {
        this.source = source;
        graphListeners = new HashSet<>();
        analysisListeners = new HashSet<>();
    }

    /*
    * Graph Events
    */
    void addGraphListener(CoverabilityGraphListener listener) {
        graphListeners.add(listener);
    }

    void fireEdgeAddedEvent(CoverabilityGraphEdge newEdge) {
        EdgeEvent event = new EdgeEvent(source, newEdge);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.edgeAdded(event);
        }
    }

    void fireEdgeRemovedEvent(CoverabilityGraphEdge edge) {

        EdgeEvent event = new EdgeEvent(source, edge);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.edgeRemoved(event);
        }
    }

    void fireEdgeReconnectedEvent(CoverabilityGraphEdge edge, CoverabilityGraphNode oldNode, CoverabilityGraphNode newNode, boolean source) {
        EdgeReconnectedEvent event = new EdgeReconnectedEvent(source, edge, oldNode, newNode, source);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.edgeReconnected(event);
        }
    }

    void fireEdgeModifiedEvent(CoverabilityGraphEdge edge, AttributeMap modifiedAttributes) {
        EdgeModifiedEvent event = new EdgeModifiedEvent(source, edge, modifiedAttributes);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.edgeModified(event);
        }
    }

    void fireNodeAddedEvent(MpNode newNode) {
        NodeEvent event = new NodeEvent(source, newNode);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.nodeAdded(event);
        }
    }

    void fireNodeRemovedEvent(MpNode removedNode) {
        NodeEvent event = new NodeEvent(source, removedNode);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.nodeRemoved(event);
        }
    }

    void fireNodeModifiedEvent(MpNode modifiedNode, AttributeMap modifiedAttributes) {
        NodeModifiedEvent event = new NodeModifiedEvent(source, modifiedNode, modifiedAttributes);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.nodeModified(event);
        }
    }

    void fireRefreshRequest() {
        for (CoverabilityGraphListener listener : graphListeners) {
            listener.refreshRequested();
        }
    }

    void firePrepareRestartEvent() {
        for (CoverabilityGraphListener listener : graphListeners) {
            listener.prepareRestartRequested();
        }
    }

    void fireResetAssistantRequestEvent() {
        for (CoverabilityGraphListener listener : graphListeners) {
            listener.restartRequested();
        }
    }

    void fireDeselectGraphEvent() {
        for (CoverabilityGraphListener listener : graphListeners) {
            listener.deselectGraphRequested();
        }
    }

    /*
    * Analysis Events
    */
    void addAnalysisListener(MonotonePruningAnalysisListener listener) {
        analysisListeners.add(listener);
    }

    void fireNodeProcessingRequest(MpNode node) {
        NodeEvent event = new NodeEvent(source, node);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.nodeProcessingRequested(event);
        }
    }

    void fireNodeProcessingStarted(MpNode node) {
        NodeEvent event = new NodeEvent(source, node);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.nodeProcessingStarted(event);
        }
    }

    void fireNodeProcessingFinishedEvent(CoverabilityGraphNode node) {
        NodeEvent event = new NodeEvent(source, node);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.nodeProcessingFinished(event);
        }
    }

    void fireNodeSelectedEvent(CoverabilityGraphNode node) {
        NodeEvent event = new NodeEvent(source, node);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.nodeSelected(event);
        }
    }

    void fireNodeRelatedEvent(MpNode node, NodeRelatedEvent.Relation relation) {
        NodeRelatedEvent event = new NodeRelatedEvent(source, node, relation);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.nodeRelated(event);
        }
    }

    void fireParentCheckCompletedEvent(MpNode parent, boolean parentActive) {
        ParentCheckCompletedEvent event = new ParentCheckCompletedEvent(source, parent, parentActive);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.parentCheckComplete(event);
        }
    }

    void fireOmegaCheckCompletedEvent(IMarking oldMarking, IMarking newMarking, Set<MpNode> smallerNodes) {
        OmegaCheckEvent event = new OmegaCheckEvent(source, oldMarking, newMarking, smallerNodes);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.omegaCheckCompleted(event);
        }
    }

    void fireCoverCheckCompletedEvent(Set<MpNode> largerNodes) {
        CoverCheckCompletedEvent event = new CoverCheckCompletedEvent(source, largerNodes);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.coverCheckCompleted(event);
        }
    }

    void fireDeactivationCheckCompleted(Set<MpNode> deactivatedNodes) {
        DeactivationCheckCompletedEvent event = new DeactivationCheckCompletedEvent(source, deactivatedNodes);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.deactivationCheckCompleted(event);
        }
    }

    void fireDescendantsCheckCompletedEvent(Collection<TransitionNode> activatedTransitions) {
        DescendantsCheckCompletedEvent event = new DescendantsCheckCompletedEvent(source, activatedTransitions);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.descendantsCheckCompleted(event);
        }
    }

    void fireCoverabilityTreeCompletedEvent() {
        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.coverabilityTreeCompleted();
        }
    }

    void fireConvert2GraphRequestEvent() {
        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.convert2GraphRequest();
        }
    }

    void fireCoverabilityGraphCompletedEvent() {
        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.coverabilityGraphCompleted();
        }
    }

    void fireProcessRandomNodeRequestEvent() {
        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.processRandomNodeRequest();
        }
    }
}

