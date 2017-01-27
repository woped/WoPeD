package org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.model.MpNode;

import java.util.EventObject;

public class ParentCheckCompletedEvent extends EventObject {

    private MpNode parent;
    private boolean parentActive;

    public ParentCheckCompletedEvent(Object source, MpNode parent, boolean isParentActive) {
        super(source);
        this.parent = parent;
        this.parentActive = isParentActive;
    }

    public MpNode getParent(){
        return  parent;
    }

    public boolean isParentActive(){
        return parentActive;
    }
}
