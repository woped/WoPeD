package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model;

/**
 * Defines the states a {@link MpNode} can be in.
 */
public enum MpNodeState {

    /**
     * The node has not been processed yet
     */
    UNPROCESSED,

    /**
     * The node has been processed and its marking is not covered by the marking of any other processed node.
     */
    ACTIVE,

    /**
     * The node has been processed and its marking is covered by the marking of another processed node.
     */
    INACTIVE
}
