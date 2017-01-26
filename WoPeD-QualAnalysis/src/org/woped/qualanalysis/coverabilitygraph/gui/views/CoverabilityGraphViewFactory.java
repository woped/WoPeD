package org.woped.qualanalysis.coverabilitygraph.gui.views;

import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexView;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.DefaultNodeFormatter;
import org.woped.qualanalysis.coverabilitygraph.gui.views.formatters.NodeFormatter;


@SuppressWarnings("serial")
public class CoverabilityGraphViewFactory extends DefaultCellViewFactory {

    private NodeFormatter nodeFormatter;

    public CoverabilityGraphViewFactory() {
        this.nodeFormatter = new DefaultNodeFormatter();
    }

    @Override
    protected EdgeView createEdgeView(Object cell) {
        return new EdgeView(cell);
    }

    @Override
    protected PortView createPortView(Object cell) {
        return new PortView(cell);
    }

    @Override
    protected VertexView createVertexView(Object cell) {
        return new CoverabilityGraphNodeView(cell, nodeFormatter);
    }

    /**
     * Gets the formatter used to format all vertices if no specific format is set.
     *
     * @return the current node formatter
     */
    public NodeFormatter getNodeFormatter() {
        return nodeFormatter;
    }
}