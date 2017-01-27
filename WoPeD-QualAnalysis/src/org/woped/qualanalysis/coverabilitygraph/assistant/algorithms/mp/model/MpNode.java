package org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.model;

import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event.NodeChangedEvent;
import org.woped.qualanalysis.reachabilitygraph.assistant.algorithms.mp.event.NodeChangedEventListener;
import org.woped.qualanalysis.reachabilitygraph.data.model.CoverabilityGraphNode;
import org.woped.qualanalysis.soundness.marking.IMarking;

import java.util.*;

/**
 * This class is represents a node in the coverability graph built with the monotone pruning algorithm.
 * <p>
 * It basically extends with a state and information about the process.
 */
public class MpNode extends CoverabilityGraphNode {
    private MpNodeState state;
    private List<NodeChangedEventListener> nodeChangedListeners;
    private int processedInStep = -1;
    private int deactivatedInStep = -1;
    private MpNode deactivatedBy = null;

    /**
     * Constructs a new instance which is by default in the UNPROCESSED state.
     *
     * @param marking the marking the node should represent
     */
    public MpNode(IMarking marking) {
        this(marking, MpNodeState.UNPROCESSED);
    }

    /**
     * Constructs a new instance with the provided state.
     *
     * @param marking the marking the node should represent
     * @param state   the initial state of the node
     */
    public MpNode(IMarking marking, MpNodeState state) {
        super(marking);
        this.state = state;
        this.nodeChangedListeners = new LinkedList<>();
    }

    public MpNodeState getState() {
        return this.state;
    }

    public void setState(MpNodeState newState) {
        MpNodeState currentState = this.state;
        this.state = newState;

        if (currentState != newState)
            stateChanged(currentState, newState);
    }

    /**
     * Gets the parent node of the current node.
     *
     * @return the parent of the node
     */
    public MpNode getParentNode() {

        Collection<CoverabilityGraphNode> directAncestors = this.getDirectAncestors();

        if(directAncestors.isEmpty()) return null;

        if(directAncestors.size()> 1) throw new IllegalStateException("There is more than one parent.");
        return (MpNode) directAncestors.iterator().next();
    }

    public Set<MpNode> getAncestors() {
        Set<MpNode> ancestors = new HashSet<>();

        MpNode node = this;
        while (node.getParentNode() != null) {
            ancestors.add(node);
            node = (MpNode) node.getParentNode();
        }
        ancestors.add(node);
        return ancestors;
    }

    public void addNodeChangedListener(NodeChangedEventListener listener) {
        this.nodeChangedListeners.add(listener);
    }

    private void stateChanged(MpNodeState previous, MpNodeState newState) {
        NodeChangedEvent event = new NodeChangedEvent(this, previous, newState);

        for (NodeChangedEventListener listener : nodeChangedListeners) {
            listener.NodeChanged(event);
        }
    }

    @Override
    public String getToolTipText() {
        if (state == MpNodeState.UNPROCESSED) return "click twice to process";
        return super.getToolTipText();
    }

    public int getProcessedInStep() {
        return processedInStep;
    }

    public void setProcessedInStep(int step) {
        this.processedInStep = step;
    }

    public int getDeactivatedInStep() {
        return deactivatedInStep;
    }

    public void setDeactivatedInStep(int step) {
        this.deactivatedInStep = step;
    }

    public MpNode getDeactivationNode() {
        return deactivatedBy;
    }

    public void setDeactivationNode(MpNode node) {
        this.deactivatedBy = node;
    }
}
