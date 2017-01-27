package org.woped.tests;

import org.jgraph.graph.CellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraph;
import org.woped.qualanalysis.coverabilitygraph.gui.views.CoverabilityGraphViewFactory;

/**
 * Provides the functionality to create graphs for testing purposes
 */
public class DemoGraphGenerator {

    /**
     * Creates a new demo graph for testing purposes.
     *
     * @return a demo graph
     */
    public CoverabilityGraph createGraph(){
        DefaultGraphModel model = new DefaultGraphModel();
        CellViewFactory viewFactory = new CoverabilityGraphViewFactory();
        GraphLayoutCache view = new GraphLayoutCache(model, viewFactory);
        view.setAutoSizeOnValueChange(true);
        view.setSelectsAllInsertedCells(false);
        return new CoverabilityGraph(model, view);
    }
}
