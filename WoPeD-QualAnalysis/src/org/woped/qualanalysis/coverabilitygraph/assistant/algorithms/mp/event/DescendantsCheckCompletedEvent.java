package org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

import java.util.Collection;
import java.util.EventObject;

public class DescendantsCheckCompletedEvent extends EventObject {

    private Collection<TransitionNode> activeTransitions;

    public DescendantsCheckCompletedEvent(Object source, Collection<TransitionNode> activeTransitions){
        super(source);
        this.activeTransitions = activeTransitions;
    }

    public Collection<TransitionNode> getActiveTransitions() {
        return activeTransitions;
    }
}
