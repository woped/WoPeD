package org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.event;

import java.util.EventListener;

/**
 * Defines the methods a listener should implement to receive change events of a {@link org.woped.qualanalysis.coverabilitygraph.assistant.algorithms.mp.model.MpNode}.
 */
public interface NodeChangedEventListener extends EventListener{

    /**
     * Invoked after the state of the node has changed.
     *
     * @param event the event object containing additional information
     */
    void NodeChanged(NodeChangedEvent event);
}
