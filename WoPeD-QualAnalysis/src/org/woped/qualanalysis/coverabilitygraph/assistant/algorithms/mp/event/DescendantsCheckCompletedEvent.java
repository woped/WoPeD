package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

import java.util.Collection;
import java.util.EventObject;

/**
 * Event that indicates, that the descendants check in the processing of the current node has been completed.
 */
public class DescendantsCheckCompletedEvent extends EventObject {

    private Collection<TransitionNode> activeTransitions;

    /**
     * Constructs a new descendants check completed event.
     *
     * @param source the source that has initiated the event.
     * @param activeTransitions the enabled transition in the resulting marking of the current processed node
     */
    public DescendantsCheckCompletedEvent(Object source, Collection<TransitionNode> activeTransitions){
        super(source);
        this.activeTransitions = activeTransitions;
    }

    /**
     * Gets the enabled transitions in the resulting marking of the currently processed node.
     *
     * @return the enabled transitions
     */
    public Collection<TransitionNode> getActiveTransitions() {
        return activeTransitions;
    }
}
