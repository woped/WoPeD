package org.woped.qualanalysis.coverabilitygraph.assistant.event;

import java.util.EventListener;

import org.woped.qualanalysis.coverabilitygraph.events.EdgeEvent;
import org.woped.qualanalysis.coverabilitygraph.events.NodeEvent;

/**
 * The listener interface for receiving coverability graph events.
 * <p>
 * The class that is interested in processing graph events either implements this interface or extends the abstract
 * {@link CoverabilityGraphAdapter} class (overriding only the methods of interest).
 * <p>
 * The listener object is then registered with a provider using the providers {@code addCoverabilityGraphAssistantListener} method.
 * Graph events are created when a node or edge is added or removed or if an edge as been reconnected.
 */
public interface CoverabilityGraphListener extends EventListener {

    /**
     * Invoked when an edge has been added to the coverability graph.
     *
     * @param event the event object containing the
     */
    void edgeAdded(EdgeEvent event);

    /**
     * Invoked when an edge has been removed from the coverability graph.
     *
     * @param event the event object containing the removed edge
     */
    void edgeRemoved(EdgeEvent event);

    /**
     * Invoked after an end of an edge has been connected to a different node.
     *
     * @param event the event object containing the information of the reconnection
     */
    void edgeReconnected(EdgeReconnectedEvent event);

    /**
     * Invoked when an edge has modified is visual appearance.
     *
     * @param event the event object containing additional information
     */
    void edgeModified(EdgeModifiedEvent event);

    /**
     * Invoked, when an node has been added to the coverability graph
     *
     * @param event the event object containing additional information
     */
    void nodeAdded(NodeEvent event);

    /**
     * Invoked when a node has been removed from the coverability graph
     *
     * @param event the event object containing the removed node.
     */
    void nodeRemoved(NodeEvent event);

    /**
     * Invoked when a node needs to modify its visual appearance
     *
     * @param event the event object containing additional information
     */
    void nodeModified(NodeModifiedEvent event);

    /**
     * Invoked when the graph should be refreshed.
     */
    void refreshRequested();

    /**
     * Invoked before a restart is requested.
     * <p>
     * This event could be used to clean up states before a restart is performed.
     */
    void prepareRestartRequested();

    /**
     * Invoked when the assistant should be restarted.
     */
    void restartRequested();

    /**
     * Invoked when highlighting should be removed from the graph
     */
    void deselectGraphRequested();
}
