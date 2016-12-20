package org.woped.qualanalysis.reachabilitygraph.gui.layout.tree;

import org.jgraph.graph.GraphConstants;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityPlaceModel;

import java.awt.geom.Rectangle2D;

class NodeExtendProvider implements org.abego.treelayout.NodeExtentProvider<ReachabilityPlaceModel> {

    /**
     * Returns the width of the given treeNode.
     *
     * @param place &nbsp;
     * @return [result &gt;= 0]
     */
    @Override
    public double getWidth(ReachabilityPlaceModel place) {

        Rectangle2D bounds = GraphConstants.getBounds(place.getAttributes());
        return bounds.getWidth();
    }

    /**
     * Returns the height of the given treeNode.
     *
     * @param place &nbsp;
     * @return [result &gt;= 0]
     */
    @Override
    public double getHeight(ReachabilityPlaceModel place) {
        Rectangle2D bounds = GraphConstants.getBounds(place.getAttributes());
        return bounds.getHeight();
    }
}
