package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event;

import org.woped.qualanalysis.coverabilitygraph.events.NodeEvent;
import org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNode;

/**
 * This event occurs if a node is related to the node currently processed.
 */
public class NodeRelatedEvent extends NodeEvent {

    private Relation relation;

    /**
     * Creates a new event.
     *
     * @param node     the node related to the current processed node
     * @param relation the relation between the node and the currently processed node
     */
    public NodeRelatedEvent(Object source, MpNode node, Relation relation) {
        super(source, node);
        this.relation = relation;
    }

    /**
     * Gets the Relation between the node and the currently processed node
     *
     * @return the relation between the nodes
     */
    public Relation getRelation() {
        return relation;
    }

    /**
     * Contains values that describe the relationship between nodes and the currently processed node.
     */
    public enum Relation {
        /**
         * The node is the currently processed node
         */
        CURRENTLY_PROCESSED,

        /**
         * The node is larger than the currently processed node.
         */
        LARGER_THAN_CURRENT,

        /**
         * The node is less than the currently processed node.
         */
        LESS_THAN_CURRENT,

        /**
         * The node has been deactivated while processing the current node.
         */
        DEACTIVATED,

        /**
         * The node is not related to the currently processed node. (Used to remove highlighting)
         */
        NONE
    }
}
