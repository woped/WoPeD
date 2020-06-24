package org.woped.qualanalysis.coverabilitygraph.gui.layout;

import java.awt.Dimension;

import org.jgraph.graph.Edge;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.tree.TreeLayout;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;

/**
 * Represents the customizable settings for the layout of the coverability graph.
 */
public class CoverabilityGraphLayoutSettings {

    /**
     * The routing algorithm for edges
     */
    public Edge.Routing edgeRouting;

    /**
     * The minimal size of a coverability graph node
     */
    public Dimension minNodeSize;

    /**
     * The padding between the border and the content of the node
     */
    @SuppressWarnings("WeakerAccess")
    public double nodePadding;

    /**
     * The horizontal gap between two nodes
     */
    public int horizontalGap;

    /**
     * The vertical gap between two nodes
     */
    public int verticalGap;

    /**
     * The layout algorithm for the placement of the nodes
     */
    public CoverabilityGraphLayout layout;

    /**
     * Gets the default layout settings.
     *
     * @return the default layout settings
     */
    public static CoverabilityGraphLayoutSettings getDefaultSettings() {
        CoverabilityGraphLayoutSettings settings = new CoverabilityGraphLayoutSettings();

        settings.edgeRouting = new CoverabilityGraphEdge.CgRouting();
        settings.minNodeSize = new Dimension(80, 20);
        settings.nodePadding = 2.0;
        settings.horizontalGap = 25;
        settings.verticalGap = 80;
        settings.layout = new TreeLayout();

        return settings;
    }
}
