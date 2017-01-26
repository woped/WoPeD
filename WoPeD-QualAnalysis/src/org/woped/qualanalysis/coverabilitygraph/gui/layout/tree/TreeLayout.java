package org.woped.qualanalysis.coverabilitygraph.gui.layout.tree;

import org.abego.treelayout.Configuration;
import org.abego.treelayout.util.DefaultConfiguration;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.CoverabilityGraphLayoutBase;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.CoverabilityGraphLayoutSettings;

import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * Applies a tree like layout to the graph. Works for trees and for graphs.
 */
public class TreeLayout extends CoverabilityGraphLayoutBase {

    /**
     * Sets the node bounds of the layout.
     */
    @Override
    protected void setNodeBounds() {
        Collection<CoverabilityGraphNode> nodes = getNodes();
        if(nodes.isEmpty()) return;

        CoverabilityGraphNode initialNode = getInitialNode();
        TreeNode root = new TreeNode(initialNode);

        CoverabilityGraphLayoutSettings settings = getSettings();
        Map<CoverabilityGraphNode, Rectangle2D.Double> nodeBounds;
        // Calculate node bounds
        if (hasCycles(root)) {
            GraphLayout graphLayout = new GraphLayout(initialNode, settings.horizontalGap, settings.verticalGap);
            nodeBounds = graphLayout.getNodeBounds();
        } else {
            Configuration<CoverabilityGraphNode> config = new DefaultConfiguration<>(settings.verticalGap, settings.horizontalGap);
            org.abego.treelayout.TreeLayout<CoverabilityGraphNode> tree = new org.abego.treelayout.TreeLayout<>(root, new NodeExtentProvider(), config);
            nodeBounds = tree.getNodeBounds();
        }

        // Create changeMap
        Map<CoverabilityGraphNode, AttributeMap> changeMap = new HashMap<>();
        for(CoverabilityGraphNode node : nodeBounds.keySet()){
            AttributeMap transport = new AttributeMap(1);
            Rectangle2D rectangle2D = nodeBounds.get(node);
            GraphConstants.setBounds(transport , rectangle2D);
            changeMap.put(node, transport);
        }

        getLayoutCache().edit(changeMap);
    }

    private boolean hasCycles(TreeNode root) {

        Set<CoverabilityGraphNode> visitedNodes = new HashSet<>();
        CoverabilityGraphNode initialNode = root.getRoot();
        visitedNodes.add(initialNode);

        return hasCycles(root, initialNode, visitedNodes);
    }

    private boolean hasCycles(TreeNode root, CoverabilityGraphNode node, Set<CoverabilityGraphNode> visitedNodes) {

        List<CoverabilityGraphNode> childrenList = root.getChildrenList(node);
        for (CoverabilityGraphNode n : childrenList) {
            if (visitedNodes.contains(n)) return true;
            visitedNodes.add(node);

            if (hasCycles(root, n, visitedNodes)) return true;
        }

        return false;
    }
}
