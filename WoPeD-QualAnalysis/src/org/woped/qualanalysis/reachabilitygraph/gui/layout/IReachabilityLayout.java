package org.woped.qualanalysis.reachabilitygraph.gui.layout;

import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;

import java.awt.*;

public interface IReachabilityLayout {
    /**
     * Applies the layout to the given graph
     *
     * @param graph the graph to layout
     * @param dimension the dimension the layout can use
     * @return the graph with the applied layout
     */
    ReachabilityJGraph layoutGraph(ReachabilityJGraph graph, Dimension dimension);
}
