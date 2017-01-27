package org.woped.qualanalysis.coverabilitygraph.gui;

import org.jgraph.graph.Edge;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.CoverabilityGraphLayout;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphColorScheme;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.MarkingFormatter;

import java.awt.*;

/**
 * Represents the customizable settings of the coverability graph.
 */
public class CoverabilityGraphSettings {

    /**
     * The formatter used to display markings
     */
    public MarkingFormatter markingFormatter;

    /**
     * The default color scheme
     */
    public CoverabilityGraphColorScheme colorScheme;

    /**
     * Indicator if the graph supports the color scheme
     */
    public boolean colorSchemeSupported;

    /**
     * The routing algorithm for edges
     */
    public Edge.Routing edgeRouting;

    /**
     * The minimal size of a coverability graph node
     */
    public Dimension minNodeSize;

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
}
