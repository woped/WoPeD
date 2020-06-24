package org.woped.qualanalysis.coverabilitygraph.model;

import java.util.Collection;

import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraph;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.CoverabilityGraphLayoutSettings;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphViewFactory;

/**
 * This interface defines the basic functionality for graph models.
 */
public interface CoverabilityGraphModel {

    /**
     * Gets the graph object to display
     *
     * @return the graph object to display
     */
    CoverabilityGraph getGraph();

    /**
     * Gets a string describing the positions of the places in a marking.
     * <p>
     * The id of a place is used for identification.
     *
     * @return a string describing the positions of a marking
     */
    String getLegendByID();

    /**
     * Gets a string describing the positions of the places in a marking.
     * <p>
     * The name of a place is used for identification.
     *
     * @return a string describing the positions of a marking
     */
    String getLegendByName();

    /**
     * Gets all Nodes of the coverability graph.
     *
     * @return the nodes of the graph
     */
    Collection<CoverabilityGraphNode> getNodes();

    /**
     * Gets all edges of the coverability graph.
     *
     * @return the edges of the graph
     */
    Collection<CoverabilityGraphEdge> getEdges();

    /**
     * Gets the view factory for the graph.
     *
     * @return the view factory for the graph
     */
    CoverabilityGraphViewFactory getViewFactory();

    /**
     * Gets the layout settings from the graph.
     *
     * @return the layout settings from the graph
     */
    CoverabilityGraphLayoutSettings getLayoutSettings();

    /**
     * Applies the configured layout to the graph.
     */
    void applyLayout();

    /**
     * Resets the model to the initial state.
     */
    void reset();
}
