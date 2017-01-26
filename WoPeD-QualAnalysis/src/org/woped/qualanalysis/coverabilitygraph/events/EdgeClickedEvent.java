package org.woped.qualanalysis.coverabilitygraph.events;

import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;

/**
 * An event which indicates that the user has clicked on an edge.
 */
public class EdgeClickedEvent extends EdgeEvent {

    private int clickCount;

    /**
     * Constructs an edge clicked event.
     *
     * @param source     the object on which the event initially occurred.
     * @param edge       the related edge
     * @param clickCount the amount of clicks
     * @throws IllegalArgumentException if source is null.
     */
    public EdgeClickedEvent(Object source, CoverabilityGraphEdge edge, int clickCount) {
        super(source, edge);
        this.clickCount = clickCount;
    }

    /**
     * Gets the amount of clicks.
     *
     * @return the amount of clicks
     */
    @SuppressWarnings("unused")
    public int getClickCount() {
        return clickCount;
    }
}
