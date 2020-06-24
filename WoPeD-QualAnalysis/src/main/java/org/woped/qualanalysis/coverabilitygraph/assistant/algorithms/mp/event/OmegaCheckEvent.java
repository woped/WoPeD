package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event;

import java.util.Collection;
import java.util.EventObject;
import java.util.Set;

import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNode;
import org.woped.qualanalysis.soundness.marking.IMarking;

/**
 * Event that indicates, that the omega check has completed in the processing of the current node.
 */
public class OmegaCheckEvent extends EventObject {

    private IMarking previousMarking;
    private IMarking resultingMarking;
    private Set<MpNode> smallerNodes;

    /**
     * Constructs a new omega check completed event.
     *
     * @param source the source that has initiated the event
     * @param previousMarking the previous marking of the current node
     * @param resultingMarking the resulting marking of the current node
     * @param smallerNodes the nodes whose markings are strictly smaller than the previous marking
     */
    public OmegaCheckEvent(Object source, IMarking previousMarking, IMarking resultingMarking, Set<MpNode> smallerNodes){
        super(source);
        this.previousMarking = previousMarking;
        this.resultingMarking = resultingMarking;
        this.smallerNodes = smallerNodes;
    }

    /**
     * Gets the previous marking of the current node.
     *
     * @return the previous marking of the current node
     */
    public IMarking getPreviousMarking() {
        return previousMarking;
    }

    /**
     * Gets the resulting marking of the current node.
     *
     * @return the resulting marking of the current node
     */
    public IMarking getResultingMarking() {
        return resultingMarking;
    }

    /**
     * Gets the nodes, whose markings are strictly smaller than the previous marking.
     *
     * @return the smaller nodes
     */
    public Collection<MpNode> getSmallerNodes() {
        return smallerNodes;
    }
}
