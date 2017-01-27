package org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event;

import java.util.EventListener;

/**
 * The listener interface for receiving events when nodes are related to the currently processed node.
 * <p>
 * This information can be used e.g. to highlight nodes or create process descriptions.
 */
public interface NodeRelatedListener extends EventListener {

    /**
     * Invoked when a node is related to the currently processed node.
     *
     * @param event the event object containing information about the relation
     */
    void nodeRelated(NodeRelatedEvent event);
}
