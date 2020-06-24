package org.woped.qualanalysis.coverabilitygraph.gui.layout.tree;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;

/**
 * This class applies a tree like layout to graph that contains loops.
 */
class GraphLayout {

    private ArrayList<LevelInfo> levels = new ArrayList<>();
    private Set<CoverabilityGraphNode> consideredNodes = new HashSet<>();

    private CoverabilityGraphNode root;
    private int horizontalGap;
    private int verticalGap;
    private Map<CoverabilityGraphNode, Rectangle2D.Double> nodeBounds;
    private NodeExtentProvider nep;

    /**
     * Constructs a new graph layout with the provided horizontal and vertical gaps
     * @param root the root node of the graph
     * @param horizontalGap the horizontal gap between nodes
     * @param verticalGap the vertical gap between nodes
     */
    GraphLayout(CoverabilityGraphNode root, int horizontalGap, int verticalGap) {
        this.root = root;
        this.horizontalGap = horizontalGap;
        this.verticalGap = verticalGap;

        this.nodeBounds = new HashMap<>();
        nep = new NodeExtentProvider();

        LevelInfo li = new LevelInfo();
        li.elements.add(root);
        consideredNodes.add(root);
        int level = 0;
        levels.add(level, li);

        registerNodes(level);
        alignNodes();
    }

    /**
     * Calculates the positions of the nodes according to their size and their first occurrence in the graph.
     * @return a map containing the bounds for each node.
     */
    Map<CoverabilityGraphNode, Rectangle2D.Double> getNodeBounds() {
        return nodeBounds;
    }

    private void registerNodes(int level) {

        LevelInfo thisLevel = levels.get(level);
        LevelInfo nextLevel = new LevelInfo();

        for(CoverabilityGraphNode node: thisLevel.elements){

            double nodeWidth = nep.getWidth(node);
            thisLevel.width = thisLevel.width == 0 ? nodeWidth : thisLevel.width + horizontalGap + nodeWidth;

            for (CoverabilityGraphNode child : node.getDirectDescendants()) {
                if(consideredNodes.contains(child)) continue;
                consideredNodes.add(child);
                nextLevel.elements.add(child);
            }
        }

        if(!nextLevel.elements.isEmpty()){
            levels.add(level+1, nextLevel);
            registerNodes(level+1);
        }
    }

    private void alignNodes() {
        LevelInfo maxLevel = new LevelInfo();
        double height = 0;
        double nodeHeight = nep.getHeight(root);

        for (LevelInfo li : levels) {

            if (li.width > maxLevel.width){
                maxLevel = li;
            }

            li.heightOffset = height;
            height += nodeHeight + verticalGap;
        }

        for (LevelInfo li : levels) {
            double left = (maxLevel.width - li.width) / 2;
            double top = li.heightOffset;
            for (CoverabilityGraphNode n : li.elements) {
                double width = nep.getWidth(n);
                height = nep.getHeight(n);

                Rectangle2D.Double bounds = new Rectangle2D.Double(left, top, width, height);
                nodeBounds.put(n, bounds);

                left += width + horizontalGap;
            }
        }
    }

    private class LevelInfo {
        double width;
        double heightOffset;
        List<CoverabilityGraphNode> elements;

        LevelInfo() {
            width = 0;
            heightOffset = 0;
            elements = new LinkedList<>();
        }
    }
}
