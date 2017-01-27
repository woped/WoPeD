package org.woped.qualanalysis.coverabilitygraph.gui.layout.circular;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraph;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.CoverabilityGraphLayoutBase;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class CircularLayout extends CoverabilityGraphLayoutBase {

    private Map<CoverabilityGraphNode, AttributeMap> edit;

    /**
     * Sets the node bounds of the layout.
     */
    @Override
    protected void setNodeBounds() {
        CoverabilityGraph graph = getGraphModel().getGraph();

        // Needs the size of the parent of the JScrollPane,
        // otherwise zooming creates an infinite loop with the component resize listener
        Dimension size = graph.getParent().getParent().getParent().getSize();

        layoutGraph(graph, size);
    }

    private CoverabilityGraph layoutGraph(CoverabilityGraph graph, Dimension dimension) {
        edit = new HashMap<>();

        Collection<CoverabilityGraphNode> places = getNodes();
        LinkedList<Point> coordinates = CircleCoordinates.getCircleCoordinates(dimension.width, dimension.height, places.size());
        CoverabilityGraphNode initialPlace = getInitialNode();

        if (initialPlace != null) {
            int width = getSettings().minNodeSize.width;
            int height = getSettings().minNodeSize.height;

            Rectangle2D bounds = GraphConstants.getBounds(initialPlace.getAttributes());
            bounds = new Rectangle2D.Double(bounds.getX(), bounds.getY(), width, height);
            GraphConstants.setBounds(initialPlace.getAttributes(), bounds);
            setPlacesOnCircle(coordinates, places);
        }
        graph.getGraphLayoutCache().edit(edit);
        return graph;
    }

    private void setPlacesOnCircle(LinkedList<Point> coordinates, Collection<CoverabilityGraphNode> places) {
        CoverabilityGraphNode initial = getInitialNode();

        Point2D position = coordinates.removeFirst();
        Rectangle2D bounds = GraphConstants.getBounds(initial.getAttributes());
        bounds = new Rectangle2D.Double(position.getX(), position.getY(), bounds.getWidth(), bounds.getHeight());
        GraphConstants.setBounds(initial.getAttributes(), bounds);
        edit.put(initial, initial.getAttributes());

        places.remove(initial);

        for (CoverabilityGraphNode actual : places) {
            bounds = GraphConstants.getBounds(initial.getAttributes());
            position = coordinates.removeFirst();
            GraphConstants.setBounds(actual.getAttributes(), new Rectangle2D.Double(position.getX(), position.getY(), bounds.getWidth(), bounds.getHeight()));
            edit.put(actual, actual.getAttributes());
        }
    }
}
