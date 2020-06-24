package org.woped.qualanalysis.coverabilitygraph.events;

import java.util.EventObject;

/**
 * An event which indicates that the user has clicked on an empty space of the graph.
 */
public class EmptySpaceClickedEvent extends EventObject {
    private int clickCount;

    /**
     * Constructs a empty space clicked Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public EmptySpaceClickedEvent(Object source, int clickCount) {
        super(source);
        this.clickCount = clickCount;
    }

    /**
     * Gets the amount of clicks.
     *
     * @return the amount of clicks
     */
    public int getClickCount() {
        return clickCount;
    }
}
