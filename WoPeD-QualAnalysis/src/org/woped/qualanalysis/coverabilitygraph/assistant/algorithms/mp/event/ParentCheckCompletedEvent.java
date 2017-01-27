package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNode;

import java.util.EventObject;

/**
 * Event that indicates, that the parent check in the processing of the current node has completed.
 */
public class ParentCheckCompletedEvent extends EventObject {

    private MpNode parent;
    private boolean parentActive;

    /**
     * Constructs a new parent check completed event.
     *
     * @param source the source that has initiated the event
     * @param parent the parent of the currently processed node
     * @param isParentActive true if the parent is active, otherwise false
     */
    public ParentCheckCompletedEvent(Object source, MpNode parent, boolean isParentActive) {
        super(source);
        this.parent = parent;
        this.parentActive = isParentActive;
    }

    /**
     * Gets the parent of the current processed node.
     *
     * @return the parent of the current processed node
     */
    public MpNode getParent(){
        return  parent;
    }

    /**
     * Returns if the parent of the current node is active.
     *
     * @return true if the parent is active, otherwise false
     */
    public boolean isParentActive(){
        return parentActive;
    }
}
