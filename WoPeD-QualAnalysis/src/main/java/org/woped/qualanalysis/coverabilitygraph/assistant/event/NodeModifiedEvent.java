package org.woped.qualanalysis.coverabilitygraph.assistant.event;

import org.jgraph.graph.AttributeMap;
import org.woped.qualanalysis.coverabilitygraph.events.NodeEvent;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;

/**
 * An event which indicates that a node has been modified in a coverability graph.
 */
public class NodeModifiedEvent extends NodeEvent {

    private AttributeMap modifiedAttributes;

    /**
     * Constructs a new node modified event.
     *
     * @param source the object where the event has occurred.
     * @param node the related node
     * @param modifiedAttributes the attributes that have been modified
     */
    public  NodeModifiedEvent (Object source, CoverabilityGraphNode node, AttributeMap modifiedAttributes){
        super(source, node);
        this.modifiedAttributes = modifiedAttributes;
    }

    /**
     * Gets the attributes that have been modified.
     *
     * @return the modified attributes
     */
    public AttributeMap getModifiedAttributes() {
        return modifiedAttributes;
    }
}
