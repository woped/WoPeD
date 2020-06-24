package org.woped.qualanalysis.coverabilitygraph.gui.layout.tree;

import java.awt.geom.Rectangle2D;

import org.jgraph.graph.GraphConstants;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;

class NodeExtentProvider implements org.abego.treelayout.NodeExtentProvider<CoverabilityGraphNode> {

    /**
     * Returns the width of the given treeNode.
     *
     * @param node the node to get the width from
     * @return the width of the node
     */
    @Override
    public double getWidth(CoverabilityGraphNode node) {

        Rectangle2D bounds = GraphConstants.getBounds(node.getAttributes());
        return bounds.getWidth();
    }

    /**
     * Returns the height of the given treeNode.
     *
     * @param node the node to get the height from
     * @return the height of the node
     */
    @Override
    public double getHeight(CoverabilityGraphNode node) {
        Rectangle2D bounds = GraphConstants.getBounds(node.getAttributes());
        return bounds.getHeight();
    }
}
