package org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.model.MpNode;
import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.model.MpNodeState;

import java.util.EventObject;

public class NodeChangedEvent extends EventObject {

    private MpNode source;
    private MpNodeState previousState;
    private MpNodeState newState;

    public NodeChangedEvent(MpNode source, MpNodeState previousState, MpNodeState newState){
        super(source);
        this.source = source;
        this.previousState = previousState;
        this.newState = newState;
    }

    public MpNode getNode(){
        return source;
    }

    public MpNodeState getPreviousState(){
        return previousState;
    }

    public MpNodeState getNewState(){
        return newState;
    }
}
