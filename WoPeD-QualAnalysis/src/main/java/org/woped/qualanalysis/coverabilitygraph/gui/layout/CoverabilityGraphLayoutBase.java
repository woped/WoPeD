package org.woped.qualanalysis.coverabilitygraph.gui.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraph;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphModel;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;

/**
 * This class provides the common layout functionality to resize the nodes before doing the layout.
 */
public abstract class CoverabilityGraphLayoutBase implements CoverabilityGraphLayout {

    private CoverabilityGraphModel graphModel;
    private Collection<CoverabilityGraphNode> nodes;

    @Override
    public void applyLayout(CoverabilityGraphModel model) {
        this.graphModel = model;
        this.nodes = model.getNodes();

        resizeNodes();
        setNodeBounds();

        addOffset();

        adjustSelfLoopEdgeLabels();
    }

    private void adjustSelfLoopEdgeLabels() {
        Map<CoverabilityGraphEdge, AttributeMap> changeMap = new HashMap<>();

        for(CoverabilityGraphEdge edge: getGraphModel().getEdges()){
            if(edge.getSourceNode().equals(edge.getTargetNode())){
                AttributeMap map = new AttributeMap();

                GraphConstants.setLabelPosition(map, new Point2D.Double(GraphConstants.PERMILLE * 0.38, 20));
                GraphConstants.setRouting(map, new CoverabilityGraphEdge.CgRouting());
                changeMap.put(edge, map);
            }
        }

        getLayoutCache().edit(changeMap);
    }

    /**
     * Sets the node bounds of the layout.
     */
    protected abstract void setNodeBounds();

    /**
     * Gets the graph model to layout.
     *
     * @return the graph model to layout
     */
    protected CoverabilityGraphModel getGraphModel(){
        return graphModel;
    }

    /**
     * Gets the layout cache for the graph.
     *
     * @return the layout cache for the graph
     */
    protected GraphLayoutCache getLayoutCache(){
        return graphModel.getGraph().getGraphLayoutCache();
    }

    /**
     * Gets the layout settings.
     *
     * @return the layout settings
     */
    protected CoverabilityGraphLayoutSettings getSettings() {
        return graphModel.getLayoutSettings();
    }

    /**
     * Gets the nodes of the graph.
     *
     * @return the nodes of the graph.
     */
    protected Collection<CoverabilityGraphNode> getNodes() {
        return this.nodes;
    }

    /**
     * Gets the root node of the graph.
     * <p>
     * The root node contains the initial marking or the petri net.
     *
     * @return the initial node of the graph
     */
    protected CoverabilityGraphNode getInitialNode() {
        Collection<CoverabilityGraphNode> initialNodes = new HashSet<>();

        for (CoverabilityGraphNode node : nodes) {
            if (node.getMarking().isInitial()) {
                initialNodes.add(node);
            }
        }

        if (initialNodes.isEmpty())
            throw new IllegalArgumentException("There is no initial marking in this graph");

        if (initialNodes.size() > 1)
            throw new IllegalArgumentException("There are more than one initial markings in the graph");

        return initialNodes.iterator().next();
    }

    private void resizeNodes() {

        adjustSizeToFitContent();
        ensureMinNodeSize();
    }

    private void adjustSizeToFitContent() {

        Hashtable<DefaultGraphCell, AttributeMap> modifiedCells = new Hashtable<>();
        AttributeMap transportMap = new AttributeMap(1);
        GraphConstants.setResize(transportMap, true);

        for (CoverabilityGraphNode node : nodes) {
            modifiedCells.put(node, transportMap);
        }

        getLayoutCache().edit(modifiedCells);
    }

    private void ensureMinNodeSize() {

        Hashtable<DefaultGraphCell, AttributeMap> modifiedCells = new Hashtable<>();

        for (CoverabilityGraphNode node : nodes) {

            Rectangle2D.Double nodeSize = calculateNodeSize(node);

            AttributeMap transportMap = new AttributeMap();
            GraphConstants.setBounds(transportMap, nodeSize);
            modifiedCells.put(node, transportMap);
        }

         getLayoutCache().edit(modifiedCells);
    }

    private Rectangle2D.Double calculateNodeSize(CoverabilityGraphNode node) {
        Rectangle2D bounds = GraphConstants.getBounds(node.getAttributes());
        double width = Math.max(graphModel.getLayoutSettings().minNodeSize.getWidth(), bounds.getWidth() + 2 * graphModel.getLayoutSettings().nodePadding);
        double height = Math.max(graphModel.getLayoutSettings().minNodeSize.getHeight(), bounds.getHeight() + 2 * graphModel.getLayoutSettings().nodePadding);
        return new Rectangle2D.Double(bounds.getX(), bounds.getY(), width, height);
    }

    /**
     * Sometimes the graph does not fit to the bounds. Especially with self loops at the root node.
     * In this cases the an offset is added to each node.
     */
    private void addOffset() {

        CoverabilityGraph graph = graphModel.getGraph();
        Rectangle2D cellBounds = graph.getCellBounds(graph.getRoots());

        if(cellBounds.getHeight() >= 0 && cellBounds.getY() >= 0) return;

        double offsetX = Math.abs(cellBounds.getX());
        double offsetY = Math.abs(cellBounds.getY()) + 20;

        Collection<CoverabilityGraphNode> nodes = graphModel.getNodes();
        Map<CoverabilityGraphNode, AttributeMap> changeMap = new HashMap<>(nodes.size());

        for(CoverabilityGraphNode node : nodes){
            Rectangle2D bounds = GraphConstants.getBounds(node.getAttributes());
            AttributeMap map = new AttributeMap(1);
            Rectangle2D newBounds = new Rectangle2D.Double(bounds.getX() + offsetX, bounds.getY() + offsetY, bounds.getWidth(), bounds.getHeight());
            GraphConstants.setBounds(map, newBounds);

            changeMap.put(node, map);
        }

        graph.getGraphLayoutCache().edit(changeMap);
    }
}
