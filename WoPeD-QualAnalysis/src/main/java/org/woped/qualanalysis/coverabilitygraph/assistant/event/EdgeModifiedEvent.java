package org.woped.qualanalysis.coverabilitygraph.assistant.event;

import org.jgraph.graph.AttributeMap;
import org.woped.qualanalysis.coverabilitygraph.events.EdgeEvent;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;

/**
 * An event which indicates that an edge has been modified in a coverability graph.
 */
public class EdgeModifiedEvent extends EdgeEvent {
    private AttributeMap modifiedAttributes;

    /**
     * Constructs an new modified edge event with the specified source, the related node and the changed attributes.
     *
     * @param source the object on which the event initially occurred.
     * @param edge the modified edge
     * @param modifiedAttributes the edges modified attributes
     */
    public EdgeModifiedEvent(Object source, CoverabilityGraphEdge edge, AttributeMap modifiedAttributes){
        super(source, edge);
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
