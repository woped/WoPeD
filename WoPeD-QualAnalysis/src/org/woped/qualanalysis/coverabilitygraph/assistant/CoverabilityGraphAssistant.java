package org.woped.qualanalysis.coverabilitygraph.assistant;

import org.woped.qualanalysis.coverabilitygraph.assistant.event.CoverabilityGraphListener;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.coverabilitygraph.events.CoverabilityGraphMouseListener;

/**
 * Defines the interactions a coverability graph assistant should provide.
 * <p>
 * An assistant implements an specific algorithm to build an coverability graph. It has to provide a graphical an textual representation
 * of the performed steps. The construction of the nodes and edges is done by the {@link CoverabilityGraphAssistantModel} class.
 * It communicates with the graph model via raising the events defined in the {@link CoverabilityGraphListener} interface.
 * The {@code CoverabilityGraphAssistantModel} provides the functionality to recognize mouse interactions and calls the related methods defined in
 * this interface if an interaction occurs.
 */
public interface CoverabilityGraphAssistant {

    /**
     * Invoked once after the creation of the assistant to setup the initial state.
     */
    void initialize();

    /**
     * Invoked when the user has selected a node in the coverability graph.
     *
     * @param node the node that has been selected by the user.
     */
    void selectNode(CoverabilityGraphNode  node);

    /**
     * Invoked when a user wants to process a specific node.
     * <p>
     * A double click on a node triggers this action.
     *
     * @param node the node to process.
     */
    void processNode(CoverabilityGraphNode node);

    /**
     * Invoked when the user wants to remove all highlighting and selections from the graph.
     */
    void deselect();

    /**
     * Invoked when the user wants to restart the assistant.
     */
    void reset();

    void addCoverabilityGraphListener(CoverabilityGraphListener listener);

    /**
     * Gets the listener that handel's the graph mouse events.
     * <p>
     * Via this listener the assistant receives notifications about user interaction with the graph.
     * e.g. the user has clicked on a node
     *
     * @return the graph mouse listener or null if no listener is needed
     */
    CoverabilityGraphMouseListener getMouseListener();
}
