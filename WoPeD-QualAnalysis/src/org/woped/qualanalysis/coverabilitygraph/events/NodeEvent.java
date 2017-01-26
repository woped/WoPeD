package org.woped.qualanalysis.coverabilitygraph.events;

import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;

import java.util.EventObject;

/**
 * An event which indicates that something happened to an node in a coverability graph.
 * <p>
 * Possible reasons for this event are e.g. the creation, the modification or the removal of an node.
 */
public class NodeEvent extends EventObject {

    private CoverabilityGraphNode node;

    /**
     * Constructs a new node event.
     *
     * @param source the object where the event has occurred.
     * @param node the related node
     */
    public NodeEvent(Object source, CoverabilityGraphNode node){
        super(source);
        this.node = node;
    }

    /**
     * Gets the related node to this event.
     *
     * @return the related node
     */
    public CoverabilityGraphNode getNode() {
        return node;
    }
}
