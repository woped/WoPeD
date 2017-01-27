package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.coverabilitygraph.events.NodeEvent;

/**
 * This class defines the basic events that can occur while building the coverability graph with the monotone pruning algorithm.
 */
public interface MonotonePruningAnalysisListener {

    /**
     * Invoked when a node should be processed.
     *
     * @param event the event object containing additional information
     */
    void nodeProcessingRequested(NodeEvent event);

    /**
     * Invoked after the processing of a node has started.
     *
     * @param event the event object containing information about the event
     */
    void nodeProcessingStarted(NodeEvent event);

    /**
     * Invoked after the node has been processed.
     *
     * @param event the event object containing the processed node
     */
    void nodeProcessingFinished(NodeEvent event);

    /**
     * Invoked after a node has been selected.
     *
     * @param event the event object containing additional information
     */
    void nodeSelected(NodeEvent event);

    /**
     * Invoked when a node is somehow related to the currently processed node.
     * <p>
     * The possible relation are defined in the enum {@link NodeRelatedEvent.Relation}.
     *
     * @param event the event object containing additional information
     */
    void nodeRelated(NodeRelatedEvent event);

    /**
     * Invoked after the parent check has completed.
     * <p>
     * This test checks if the direct ancestor of the node is still active.
     * If it is, the analysis continues, otherwise the node is deactivated and the analysis is finished.
     *
     * @param event The event object containing the result of the parent check
     */
    void parentCheckComplete(ParentCheckCompletedEvent event);

    /**
     * Invoked after the omega check has completed.
     * <p>
     * This checks if active nodes are covered by the new created node. If so the marking of the new node is replaced by an
     * omega marking.
     *
     * @param event The event object containing the results of the omega check
     */
    void omegaCheckCompleted(OmegaCheckEvent event);

    /**
     * Invoked after the cover check has completed.
     * <p>
     * This test checks if an active node covers the new created node. If so, the new node is set inactive an tha analysis is finished.
     *
     * @param event The event object containing the results of the cover check
     */
    void coverCheckCompleted(CoverCheckCompletedEvent event);

    /**
     * Invoked after the deactivation check has completed.
     * <p>
     * This test checks, if the new node covers any active nodes or their ancestors.
     *
     * @param event the event object containing the result of the deactivation check.
     */
    void deactivationCheckCompleted(DeactivationCheckCompletedEvent event);

    /**
     * Invoked after the descendants check has completed.
     * <p>
     * This test checks if the marking if there exists any activated transitions.
     * If so it adds the resulting markings to the unprocessed nodes.
     *
     * @param event the event object containing the result of the descendants check
     */
    void descendantsCheckCompleted(DescendantsCheckCompletedEvent event);

    /**
     * Invoked after the last unprocessed node has been processed
      */
    void coverabilityTreeCompleted();

    /**
     * Invoked when the coverability tree should be converted into a coverability graph
     */
    void convert2GraphRequest();

    /**
     * Invoked after the construction of the minimal coverability graph has completed
     */
    void coverabilityGraphCompleted();

    /**
     * Invoked when the algorithm should choose the next node to process.
     */
    void processRandomNodeRequest();
}
