package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.jgraph.graph.AttributeMap;
import org.woped.qualanalysis.coverabilitygraph.assistant.CoverabilityGraphAssistant;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.CoverCheckCompletedEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.DeactivationCheckCompletedEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.DescendantsCheckCompletedEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.MonotonePruningAnalysisListener;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.NodeRelatedEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.OmegaCheckEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.ParentCheckCompletedEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNode;
import org.woped.qualanalysis.coverabilitygraph.assistant.event.CoverabilityGraphListener;
import org.woped.qualanalysis.coverabilitygraph.assistant.event.EdgeModifiedEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.event.EdgeReconnectedEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.event.NodeModifiedEvent;
import org.woped.qualanalysis.coverabilitygraph.events.EdgeEvent;
import org.woped.qualanalysis.coverabilitygraph.events.NodeEvent;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;
import org.woped.qualanalysis.soundness.marking.IMarking;

/**
 * This class triggers the events to notify listeners on graph or analysis events.
 */
class MonotonePruningEventTrigger {

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

    /**
     * Adds a listener that is interested in graph events.
     *
     * @param listener the listener to add
     */
    void addGraphListener(CoverabilityGraphListener listener) {
        graphListeners.add(listener);
    }

    /**
     * Fires the event that an edge has been added by the algorithm.
     *
     * @param newEdge the added edge
     */
    void fireEdgeAddedEvent(CoverabilityGraphEdge newEdge) {
        EdgeEvent event = new EdgeEvent(source, newEdge);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.edgeAdded(event);
        }
    }

    /**
     * Fires the event that an edge has been removed by the algorithm
     *
     * @param edge the removed edge
     */
    void fireEdgeRemovedEvent(CoverabilityGraphEdge edge) {

        EdgeEvent event = new EdgeEvent(source, edge);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.edgeRemoved(event);
        }
    }

    /**
     * Fires an event, that an edge has been reconnected by the algorithm.
     *  @param edge                     the reconnected edge
     * @param oldNode                  the old destination
     * @param newNode                  the new destination
     */
    void fireEdgeReconnectedEvent(CoverabilityGraphEdge edge, CoverabilityGraphNode oldNode, CoverabilityGraphNode newNode) {
        EdgeReconnectedEvent event = new EdgeReconnectedEvent(source, edge, oldNode, newNode, false);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.edgeReconnected(event);
        }
    }

    /**
     * Fires an event, that the attributes of an edge has been modified by the algorithm.
     *
     * @param edge               the modified edge
     * @param modifiedAttributes the modified attributes
     */
    void fireEdgeModifiedEvent(CoverabilityGraphEdge edge, AttributeMap modifiedAttributes) {
        EdgeModifiedEvent event = new EdgeModifiedEvent(source, edge, modifiedAttributes);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.edgeModified(event);
        }
    }

    /**
     * Fires an event that a node has been added by the algorithm
     *
     * @param newNode the new node
     */
    void fireNodeAddedEvent(MpNode newNode) {
        NodeEvent event = new NodeEvent(source, newNode);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.nodeAdded(event);
        }
    }

    /**
     * Fires an event, that a node has been removed by the algorithm.
     *
     * @param removedNode the removed node
     */
    void fireNodeRemovedEvent(MpNode removedNode) {
        NodeEvent event = new NodeEvent(source, removedNode);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.nodeRemoved(event);
        }
    }

    /**
     * Fires an event that a node has been modified by the algorithm.
     *
     * @param modifiedNode       the modified node
     * @param modifiedAttributes the modified attributes
     */
    void fireNodeModifiedEvent(MpNode modifiedNode, AttributeMap modifiedAttributes) {
        NodeModifiedEvent event = new NodeModifiedEvent(source, modifiedNode, modifiedAttributes);

        for (CoverabilityGraphListener listener : graphListeners) {
            listener.nodeModified(event);
        }
    }

    /**
     * Fires an event that there are no more changes in this analysis step and the listeners should refresh their output.
     */
    void fireRefreshRequest() {
        for (CoverabilityGraphListener listener : graphListeners) {
            listener.refreshRequested();
        }
    }

    /**
     * Fires an event that all listeners should prepare a restart of the algorithm.
     */
    void firePrepareRestartEvent() {
        for (CoverabilityGraphListener listener : graphListeners) {
            listener.prepareRestartRequested();
        }
    }

    /**
     * Fires an event that the algorithm has been reset.
     */
    void fireResetAssistantRequestEvent() {
        for (CoverabilityGraphListener listener : graphListeners) {
            listener.restartRequested();
        }
    }

    /**
     * Fires an event that all listeners should remove all highlighting
     */
    void fireDeselectGraphEvent() {
        for (CoverabilityGraphListener listener : graphListeners) {
            listener.deselectGraphRequested();
        }
    }

    /*
    * Analysis Events
    */

    /**
     * Adds a listener that is interested in events about the analysis.
     *
     * @param listener the listener to add
     */
    void addAnalysisListener(MonotonePruningAnalysisListener listener) {
        analysisListeners.add(listener);
    }

    /**
     * Fires the event that a random node should be processed.
     */
    void fireProcessRandomNodeRequestEvent() {
        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.processRandomNodeRequest();
        }
    }

    /**
     * Fires the event, that a specific node should be processed.
     *
     * @param node the node that should be processed.
     */
    void fireNodeProcessingRequest(MpNode node) {
        NodeEvent event = new NodeEvent(source, node);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.nodeProcessingRequested(event);
        }
    }

    /**
     * Fires an event, that the analysis of an specific node has started.
     *
     * @param node the node being processed
     */
    void fireNodeProcessingStarted(MpNode node) {
        NodeEvent event = new NodeEvent(source, node);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.nodeProcessingStarted(event);
        }
    }

    /**
     * Fires the event, that the processing of a node has been finished.
     *
     * @param node the node that has been processed
     */
    void fireNodeProcessingFinishedEvent(CoverabilityGraphNode node) {
        NodeEvent event = new NodeEvent(source, node);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.nodeProcessingFinished(event);
        }
    }

    /**
     * Fires the event, that a node has been selected by the algorithm.
     *
     * @param node the selected node
     */
    void fireNodeSelectedEvent(CoverabilityGraphNode node) {
        NodeEvent event = new NodeEvent(source, node);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.nodeSelected(event);
        }
    }

    /**
     * Fires the event, that a node is somehow related to the currently processed node.
     *
     * @param node the related node
     * @param relation the relation to the currently processed node
     */
    void fireNodeRelatedEvent(MpNode node, NodeRelatedEvent.Relation relation) {
        NodeRelatedEvent event = new NodeRelatedEvent(source, node, relation);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.nodeRelated(event);
        }
    }

    /**
     * Fires the event, that the parent check of the currently processed node has been completed.
     *
     * @param parent the parent of the currently processed node
     * @param parentActive indicator if the parent is active
     */
    void fireParentCheckCompletedEvent(MpNode parent, boolean parentActive) {
        ParentCheckCompletedEvent event = new ParentCheckCompletedEvent(source, parent, parentActive);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.parentCheckComplete(event);
        }
    }

    /**
     * Fires the event, that the omega check of the currently processed node has been completed.
     *
     * @param oldMarking the previous marking of the currently processed node
     * @param newMarking the resulting marking of the currently processed node
     * @param smallerNodes the active nodes whose marking is strictly less than the previous marking
     */
    void fireOmegaCheckCompletedEvent(IMarking oldMarking, IMarking newMarking, Set<MpNode> smallerNodes) {
        OmegaCheckEvent event = new OmegaCheckEvent(source, oldMarking, newMarking, smallerNodes);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.omegaCheckCompleted(event);
        }
    }

    /**
     * Fires the event, that the cover check of the currently processed node has been completed.
     *
     * @param largerNodes the active nodes whose marking covers the marking of the currently processed node
     */
    void fireCoverCheckCompletedEvent(Set<MpNode> largerNodes) {
        CoverCheckCompletedEvent event = new CoverCheckCompletedEvent(source, largerNodes);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.coverCheckCompleted(event);
        }
    }

    /**
     * Fires the event, that the deactivation check of the currently processed node has been completed.
     *
     * @param deactivatedNodes the active nodes, that has been deactivated by the currently processed node.
     */
    void fireDeactivationCheckCompleted(Set<MpNode> deactivatedNodes) {
        DeactivationCheckCompletedEvent event = new DeactivationCheckCompletedEvent(source, deactivatedNodes);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.deactivationCheckCompleted(event);
        }
    }

    /**
     * Fires the event, that the descendants check of the currently processed node has been completed.
     *
     * @param activatedTransitions the enabled transitions in the marking of the currently processed node
     */
    void fireDescendantsCheckCompletedEvent(Collection<TransitionNode> activatedTransitions) {
        DescendantsCheckCompletedEvent event = new DescendantsCheckCompletedEvent(source, activatedTransitions);

        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.descendantsCheckCompleted(event);
        }
    }

    /**
     * Fires the event, that the construction of the coverability tree has been completed.
     */
    void fireCoverabilityTreeCompletedEvent() {
        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.coverabilityTreeCompleted();
        }
    }

    /**
     * Fires the event, that the coverability tree should be converted into a graph.
     */
    void fireConvert2GraphRequestEvent() {
        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.convert2GraphRequest();
        }
    }

    /**
     * Fires the event, that the construction of the coverability graph has been completed.
     */
    void fireCoverabilityGraphCompletedEvent() {
        for (MonotonePruningAnalysisListener listener : analysisListeners) {
            listener.coverabilityGraphCompleted();
        }
    }
}

