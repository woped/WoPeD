package org.woped.qualanalysis.coverabilitygraph.events;

import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;

/**
 * An event which indicates that the user has clicked on a node.
 */
public class NodeClickedEvent extends NodeEvent {

    private int clickCount;

    /**
     * Constructs a new node clicked event.
     *
     * @param source     the object where the event has occurred.
     * @param node       the related node
     * @param clickCount the click count
     */
    public NodeClickedEvent(Object source, CoverabilityGraphNode node, int clickCount) {
        super(source, node);
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
