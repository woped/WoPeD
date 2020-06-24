package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event;

import java.util.Collection;
import java.util.EventObject;
import java.util.Set;

import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNode;

/**
 * Event that indicates, that the cover check in the processing of the current node has completed.
 */
public class CoverCheckCompletedEvent extends EventObject {

    private Set<MpNode> largerNodes;

    /**
     * Constructs a new cover check completed event.
     * @param source the source that has initiated the event
     * @param largerNodes the set of nodes that covers the current processed node
     */
    public CoverCheckCompletedEvent(Object source, Set<MpNode> largerNodes) {
        super(source);
        this.largerNodes = largerNodes;
    }

    /**
     * Gets the set of nodes that covers the current processed node.
     *
     * @return the nodes that covers the currently processed node
     */
    public Collection<MpNode> getLargerNodes() {
        return largerNodes;
    }
}
