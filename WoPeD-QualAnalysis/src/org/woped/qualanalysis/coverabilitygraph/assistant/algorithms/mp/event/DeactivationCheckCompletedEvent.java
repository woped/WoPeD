package org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.model.MpNode;

import java.util.Collection;
import java.util.EventObject;
import java.util.Set;

public class DeactivationCheckCompletedEvent extends EventObject {

    private Set<MpNode> deactivatedNodes;

    public DeactivationCheckCompletedEvent(Object source, Set<MpNode> deactivatedNodes){
        super(source);
        this.deactivatedNodes = deactivatedNodes;
    }

    public Collection<MpNode> getDeactivatedNodes() {
        return deactivatedNodes;
    }
}
