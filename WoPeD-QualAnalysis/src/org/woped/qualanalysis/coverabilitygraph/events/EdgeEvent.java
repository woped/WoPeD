package org.woped.qualanalysis.coverabilitygraph.events;

import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;

import java.util.EventObject;

/**
 * An event which indicates that something happened to an edge in a coverability graph.
 * <p>
 * Possible reasons for this event are e.g. the creation, modification or the removal of an edge.
 */
public class EdgeEvent extends EventObject {
    private CoverabilityGraphEdge edge;

    /**
     * Constructs an edge event.
     *
     * @param source the object on which the event initially occurred.
     * @param edge the related edge
     * @throws IllegalArgumentException if source is null.
     */
    public EdgeEvent(Object source, CoverabilityGraphEdge edge) {
        super(source);
        this.edge = edge;
    }

    /**
     * Gets the related edge.
     *
     * @return the related edge
     */
    public CoverabilityGraphEdge getEdge() {
        return edge;
    }
}
