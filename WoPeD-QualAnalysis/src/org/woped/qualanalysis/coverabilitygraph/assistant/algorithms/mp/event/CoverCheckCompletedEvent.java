package org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.model.MpNode;

import java.util.Collection;
import java.util.EventObject;
import java.util.Set;

public class CoverCheckCompletedEvent extends EventObject {

    private Set<MpNode> largerNodes;

    public CoverCheckCompletedEvent(Object source, Set<MpNode> largerNodes){
        super(source);
        this.largerNodes = largerNodes;
    }

    public Collection<MpNode> getLargerNodes(){
        return largerNodes;
    }
}
