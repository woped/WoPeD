package org.woped.qualanalysis.coverabilitygraph.events;

/**
 * An abstract class for receiving mouse events from a coverability graph. The methods in this class are empty.
 * This class exists for convenience for creating listeners objects.
 * <p>
 * Extend this class if you are only interested in a subset of events provided by listener interface.
 */
public abstract class CoverabilityGraphMouseAdapter implements CoverabilityGraphMouseListener{
    /**
     * Invoked when the user has clicked on a node of the graph.
     *
     * @param event additional information about the action
     */
    @Override
    public void nodeClicked(NodeClickedEvent event) {

    }

    /**
     * Invoked when the user has clicked on an edge of the graph.
     *
     * @param event additional information about the action
     */
    @Override
    public void edgeClicked(EdgeClickedEvent event) {

    }

    /**
     * Invoked when the user has clicked on an empty space of the graph.
     *
     * @param event additional information about the action
     */
    @Override
    public void emptySpaceClicked(EmptySpaceClickedEvent event) {

    }
}
