package org.woped.qualanalysis.reachabilitygraph.gui.layout.tree;

import org.abego.treelayout.Configuration;
import org.abego.treelayout.util.DefaultConfiguration;
import org.jgraph.graph.GraphConstants;
import org.woped.qualanalysis.reachabilitygraph.data.AbstractReachabilityGraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityPlaceModel;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;
import org.woped.qualanalysis.reachabilitygraph.gui.layout.IReachabilityLayout;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class TreeLayout implements IReachabilityLayout {
    /**
     * takes a ReachabilityJGraph to layout it in a hierarchic way.
     *
     * @param graph
     * @return
     */
    @Override
    public ReachabilityJGraph layoutGraph(ReachabilityJGraph graph, Dimension dimension) {

        Collection<ReachabilityPlaceModel> places = AbstractReachabilityGraphModel.getPlaces(graph);
        ReachabilityPlaceModel initialPlace = AbstractReachabilityGraphModel.lookupInitialMarking(places);

        TreeNode root = new TreeNode(initialPlace);

        HashMap<String, String> graphAttributes = graph.getAttributeMap();
        int horizontalSpace = Integer.parseInt(graphAttributes.get("reachabilityGraph.hierarchic.horizontalSpace"));
        int verticalSpace = Integer.parseInt(graphAttributes.get("reachabilityGraph.hierarchic.verticalSpace"));

        Configuration<ReachabilityPlaceModel> config = new DefaultConfiguration<>(verticalSpace, horizontalSpace);
        org.abego.treelayout.TreeLayout<ReachabilityPlaceModel> tree = new org.abego.treelayout.TreeLayout<>(root, new NodeExtendProvider(), config);
        Map<ReachabilityPlaceModel, Rectangle2D.Double> nodeBounds = tree.getNodeBounds();

        Rectangle2D.Double rootBounds = nodeBounds.get(initialPlace);
        Double offsetX = dimension.getWidth() / 2 - rootBounds.getWidth() / 2 - rootBounds.getX();
        double offsetY = 10;

        Hashtable nested = new Hashtable();
        for (ReachabilityPlaceModel place : nodeBounds.keySet()) {
            Rectangle2D.Double bounds = nodeBounds.get(place);
            Hashtable transportMap = new Hashtable();
            GraphConstants.setBounds(transportMap, new Rectangle2D.Double(bounds.getX() + offsetX, bounds.getY() + offsetY, bounds.getWidth(), bounds.getHeight()));
            nested.put(place, transportMap);
        }

        graph.getGraphLayoutCache().edit(nested);
        return graph;
    }
}
