package org.woped.qualanalysis.coverabilitygraph.events;

/**
 * Defines mouse events on a coverability graph.
 */
public interface CoverabilityGraphMouseListener {

    /**
     * Invoked when the user has clicked on a node of the graph.
     *
     * @param event additional information about the action
     */
    void nodeClicked(NodeClickedEvent event);

    /**
     * Invoked when the user has clicked on an edge of the graph.
     *
     * @param event additional information about the action
     */
    void edgeClicked(EdgeClickedEvent event);

    /**
     * Invoked when the user has clicked on an empty space of the graph.
     *
     * @param event additional information about the action
     */
    void emptySpaceClicked(EmptySpaceClickedEvent event);
}
