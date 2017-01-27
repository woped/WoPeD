package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNode;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNodeState;

import java.util.EventObject;

/**
 * Event that indicates, that a node has changed its state during the processing of the current node.
 */
public class NodeChangedEvent extends EventObject {

    private MpNode source;
    private MpNodeState previousState;
    private MpNodeState newState;

    /**
     * Constructs a new node changed event.
     *
     * @param source the node that has changed its state
     * @param previousState the previous state of the node
     * @param newState the new state of the node
     */
    public NodeChangedEvent(MpNode source, MpNodeState previousState, MpNodeState newState){
        super(source);
        this.source = source;
        this.previousState = previousState;
        this.newState = newState;
    }

    /**
     * Gets the node that has changed its state.
     *
     * @return the node that has changed
     */
    public MpNode getNode(){
        return source;
    }

    /**
     * Gets the previous state of the node.
     *
     * @return the previous state of the node
     */
    public MpNodeState getPreviousState(){
        return previousState;
    }

    /**
     * Gets the new state of the node.
     *
     * @return the new state of the node
     */
    public MpNodeState getNewState(){
        return newState;
    }
}
