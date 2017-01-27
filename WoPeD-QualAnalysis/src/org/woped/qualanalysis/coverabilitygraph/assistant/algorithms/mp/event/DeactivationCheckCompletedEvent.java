package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNode;

import java.util.Collection;
import java.util.EventObject;
import java.util.Set;

/**
 * Event that indicates, that the deactivation check in the processing of the current node has completed.
 */
public class DeactivationCheckCompletedEvent extends EventObject {

    private Set<MpNode> deactivatedNodes;

    /**
     * Constructs a new deactivation check completed event.
     *
     * @param source the source that has initiated the event
     * @param deactivatedNodes the nodes that has been deactivated by the currently processed node
     */
    public DeactivationCheckCompletedEvent(Object source, Set<MpNode> deactivatedNodes){
        super(source);
        this.deactivatedNodes = deactivatedNodes;
    }

    /**
     * Gets the nodes that has been deactivated by the currently processed node.
     *
     * @return the deactivated nodes
     */
    public Collection<MpNode> getDeactivatedNodes() {
        return deactivatedNodes;
    }
}
