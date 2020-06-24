package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.NodeChangedEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event.NodeChangedEventListener;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.soundness.marking.IMarking;

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

    /**
     * Gets the current state of the node.
     *
     * @return the current state of the node
     */
    public MpNodeState getState() {
        return this.state;
    }

    /**
     * Sets the state of the node to the provided state.
     *
     * @param newState the state to set
     */
    public void setState(MpNodeState newState) {
        MpNodeState currentState = this.state;
        this.state = newState;

        if (currentState != newState)
            notifyStateChangedListeners(currentState, newState);
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

    /**
     * Gets the nodes ancestors.
     *
     * @return the nodes ancestors
     */
    public Set<MpNode> getAncestors() {
        Set<MpNode> ancestors = new HashSet<>();

        MpNode node = this;
        while (node.getParentNode() != null) {
            ancestors.add(node);
            node = node.getParentNode();
        }
        ancestors.add(node);
        return ancestors;
    }

    /**
     * Adds an {@link NodeChangedEventListener} to this node.
     *
     * @param listener the listener to add
     */
    public void addNodeChangedListener(NodeChangedEventListener listener) {
        this.nodeChangedListeners.add(listener);
    }

    /* non java-doc
     * Hides the node details on unprocessed nodes
     */
    @Override
    public String getToolTipText() {
        if (state == MpNodeState.UNPROCESSED) return "click twice to process";
        return super.getToolTipText();
    }

    /**
     * Gets the process step in which the node has been processed.
     *
     * @return the step in which the node has been processed
     * @throws IllegalStateException if the node has not been processed
     */
    public int getProcessedInStep() throws IllegalStateException {
        if(processedInStep == -1) throw new IllegalStateException("The node has not been processed yet.");

        return processedInStep;
    }

    /**
     * Sets the processes step in which the node has been processed.
     *
     * @param step the step in which the node has been processed
     */
    public void setProcessedInStep(int step) {
        this.processedInStep = step;
    }

    /**
     * Gets the step in which the node has been deactivated.
     *
     * @return the step in which the node has been deactivated
     * @throws IllegalStateException if the node has not been deactivated
     */
    public int getDeactivatedInStep() {
        if(deactivatedInStep < 0) throw new IllegalStateException("The node has not been deactivated yet");
        return deactivatedInStep;
    }

    /**
     * Sets the process step in which the node has been deactivated.
     *
     * @param step the step in which the node has been deactivated
     */
    public void setDeactivatedInStep(int step) {
        this.deactivatedInStep = step;
    }

    /**
     * Gets the node that was the reason for the deactivation.
     *
     * @return the node that has caused the deactivation
     */
    public MpNode getDeactivationNode() {
        return deactivatedBy;
    }

    /**
     * Sets the node that has caused the deactivation.
     *
     * @param node the node that has caused the deactivation or null if the has not been deactivated yet.
     */
    public void setDeactivationNode(MpNode node) {
        this.deactivatedBy = node;
    }

    private void notifyStateChangedListeners(MpNodeState previous, MpNodeState newState) {
        NodeChangedEvent event = new NodeChangedEvent(this, previous, newState);

        for (NodeChangedEventListener listener : nodeChangedListeners) {
            listener.NodeChanged(event);
        }
    }
}
