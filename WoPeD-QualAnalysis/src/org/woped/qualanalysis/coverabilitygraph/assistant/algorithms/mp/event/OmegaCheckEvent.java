package org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.model.MpNode;
import org.woped.qualanalysis.soundness.marking.IMarking;

import java.util.Collection;
import java.util.EventObject;
import java.util.Set;

public class OmegaCheckEvent extends EventObject {

    private IMarking previousMarking;
    private IMarking resultingMarking;
    private Set<MpNode> smallerNodes;

    public OmegaCheckEvent(Object source, IMarking previousMarking, IMarking resultingMarking, Set<MpNode> smallerNodes){
        super(source);
        this.previousMarking = previousMarking;
        this.resultingMarking = resultingMarking;
        this.smallerNodes = smallerNodes;
    }

    public IMarking getPreviousMarking() {
        return previousMarking;
    }

    public IMarking getResultingMarking() {
        return resultingMarking;
    }

    public Collection<MpNode> getSmallerNodes() {
        return smallerNodes;
    }
}
