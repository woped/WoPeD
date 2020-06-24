package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.coverabilitygraph.events.NodeEvent;

/**
 * An abstract adapter for receiving analysis events. All methods in this class are empty.
 * The class exists for convenience for creating listener objects.
 * <p>
 * Analysis events let you track the current state of the analysis of an coverability graph node and provide the necessary information
 * to create a textual description or to highlight the coverability graph.
 * <p>
 * Extend this class to create a {@link MonotonePruningAnalysisListener} and override the methods for the events of interest. Add an instance
 * of your class to the {@link org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.MonotonePruningEventTrigger#addAnalysisListener(MonotonePruningAnalysisListener)} method.
 * The relevant methods are invoked and the event object is passed to it when an event occurs.
 */
public abstract class MonotonePruningAnalysisAdapter implements MonotonePruningAnalysisListener {

    /**
     * Invoked after a node has been selected.
     *
     * @param event the event object containing additional information
     */
    @Override
    public void nodeSelected(NodeEvent event) {

    }

    /**
     * Invoked when a node is somehow related to the currently processed node.
     * <p>
     * The possible relation are defined in the enum {@link NodeRelatedEvent.Relation}.
     *
     * @param event the event object containing additional information
     */
    @Override
    public void nodeRelated(NodeRelatedEvent event) {

    }

    /**
     * Invoked when a node should be processed.
     *
     * @param event the event object containing additional information
     */
    @Override
    public void nodeProcessingRequested(NodeEvent event) {

    }

    /**
     * Invoked after the processing of a node has started.
     *
     * @param event the event object containing information about the event
     */
    @Override
    public void nodeProcessingStarted(NodeEvent event) {

    }

    /**
     * Invoked after the node has been processed.
     *
     * @param event the event object containing the processed node
     */
    @Override
    public void nodeProcessingFinished(NodeEvent event) {

    }

    /**
     * Invoked after the parent check has completed.
     * <p>
     * This test checks if the direct ancestor of the node is still active.
     * If it is, the analysis continues, otherwise the node is deactivated and the analysis is finished.
     *
     * @param event The event object containing the result of the parent check
     */
    @Override
    public void parentCheckComplete(ParentCheckCompletedEvent event) {

    }

    /**
     * Invoked after the omega check has completed.
     * <p>
     * This checks if active nodes are covered by the new created node. If so the marking of the new node is replaced by an
     * omega marking.
     *
     * @param event The event object containing the results of the omega check
     */
    @Override
    public void omegaCheckCompleted(OmegaCheckEvent event) {

    }

    /**
     * Invoked after the cover check has completed.
     * <p>
     * This test checks if an active node covers the new created node. If so, the new node is set inactive an tha analysis is finished.
     *
     * @param event The event object containing the results of the cover check
     */
    @Override
    public void coverCheckCompleted(CoverCheckCompletedEvent event) {

    }

    /**
     * Invoked after the deactivation check has completed.
     * <p>
     * This test checks, if the new node covers any active nodes or their ancestors.
     *
     * @param event the event object containing the result of the deactivation check.
     */
    @Override
    public void deactivationCheckCompleted(DeactivationCheckCompletedEvent event) {

    }

    /**
     * Invoked after the descendants check has completed.
     * <p>
     * This test checks if the marking if there exists any activated transitions.
     * If so it adds the resulting markings to the unprocessed nodes.
     *
     * @param event the event object containing the result of the descendants check
     */
    @Override
    public void descendantsCheckCompleted(DescendantsCheckCompletedEvent event) {

    }

    /**
     * Invoked after the last unprocessed node has been processed
     */
    @Override
    public void coverabilityTreeCompleted() {

    }

    /**
     * Invoked when the coverability tree should be converted into a coverability graph
     */
    @Override
    public void convert2GraphRequest() {

    }

    /**
     * Invoked after the construction of the minimal coverability graph has completed
     */
    @Override
    public void coverabilityGraphCompleted() {

    }

    /**
     * Invoked when a random unprocessed node should be processed.
     */
    @Override
    public void processRandomNodeRequest() {

    }

}
