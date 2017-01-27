package org.woped.qualanalysis.coverabilitygraph.assistant.event;

import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;

import java.util.EventObject;

/**
 * An event which indicates that an edge has been reconnected in the coverability graph.
 */
public class EdgeReconnectedEvent extends EventObject {

    private CoverabilityGraphEdge edge;
    private CoverabilityGraphNode oldNode;
    private CoverabilityGraphNode newNode;
    private boolean sourceOfEdge;

    /**
     * Constructs an new edge reconnected event.
     *
     * @param source the object on which the event has initially occurred.
     * @param edge the reconnected edge
     * @param oldNode the old destination node of the edge
     * @param newNode the new destination node of the edge
     * @param sourceOfEdge true if the reconnected end is the source of the edge, otherwise false.
     */
    public EdgeReconnectedEvent(Object source, CoverabilityGraphEdge edge, CoverabilityGraphNode oldNode, CoverabilityGraphNode newNode, boolean sourceOfEdge) {
        super(source);
        this.edge = edge;
        this.oldNode = oldNode;
        this.newNode = newNode;
        this.sourceOfEdge = sourceOfEdge;
    }

    /**
     * Gets the old destination node of the edge.
     *
     * @return the old destination node of the edge
     */
    @SuppressWarnings("unused")
    public CoverabilityGraphNode getOldNode() {
        return oldNode;
    }

    /**
     * Gets the new destination node of the edge.
     *
     * @return the new destination node of the edge
     */
    public CoverabilityGraphNode getNewNode() {
        return newNode;
    }

    /**
     * Determines if the reconnected destination of the edge is the source or the target.
     *
     * @return true is the source has been reconnected, otherwise false
     */
    public boolean isSourceOfEdge() {
        return sourceOfEdge;
    }

    /**
     * Gets the edge, that has been reconnected
     *
     * @return the reconnected edge
     */
    public CoverabilityGraphEdge getEdge() {
        return edge;
    }
}
