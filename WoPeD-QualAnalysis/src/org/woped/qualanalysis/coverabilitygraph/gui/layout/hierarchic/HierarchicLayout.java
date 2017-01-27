package org.woped.qualanalysis.coverabilitygraph.gui.layout.hierarchic;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraph;
import org.woped.qualanalysis.coverabilitygraph.model.AbstractReachabilityGraphModel;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphEdge;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphNode;
import org.woped.qualanalysis.coverabilitygraph.model.CoverabilityGraphPort;
import org.woped.qualanalysis.coverabilitygraph.gui.layout.CoverabilityGraphLayoutBase;

import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 * <p>
 * This class was written by
 *
 * @author Benjamin Geiger
 */
public class HierarchicLayout extends CoverabilityGraphLayoutBase {

    private Map<CoverabilityGraphNode, AttributeMap> edit;

    /**
     * Sets the node bounds of the layout.
     */
    @Override
    protected void setNodeBounds() {
        CoverabilityGraph graph = getGraphModel().getGraph();
        layoutGraph(graph);
    }

    private CoverabilityGraph layoutGraph(CoverabilityGraph graph) {
        edit = new HashMap<>();
        applyHierarchicLayout(graph);
        graph.getGraphLayoutCache().edit(edit, null, null, null);
        return graph;
    }

    @SuppressWarnings("unchecked")
    private void applyHierarchicLayout(CoverabilityGraph graph) {
        GraphModel model = graph.getModel();
        LinkedList<CoverabilityGraphNode> markings = new LinkedList<>();
        // get all marking and reset them to not recursively touched
        for (int i = 0; i < model.getRootCount(); i++) {
            if (model.getRootAt(i) instanceof CoverabilityGraphNode) {
                ((CoverabilityGraphNode) model.getRootAt(i)).setIsSetRecursiveBounds(false);
                markings.add((CoverabilityGraphNode) model.getRootAt(i));
            }
        }
        // get initial marking. the begin oh each graph.
        CoverabilityGraphNode initialPlace = AbstractReachabilityGraphModel.lookupInitialMarking(markings);
        if (initialPlace != null) {
            // initialize a lot of things
            Rectangle2D bounds = GraphConstants.getBounds(initialPlace.getAttributes());
            LinkedList<CoverabilityGraphNode> toProof = new LinkedList<>();

            int verticalSpace = getSettings().verticalGap;
            int horizontalSpace = getSettings().horizontalGap;

            hierarcher(initialPlace, new Rectangle2D.Double(10, 0, bounds.getWidth(), bounds.getHeight()), horizontalSpace, verticalSpace, toProof);

            LinkedList<CoverabilityGraphNode> toEdit = (LinkedList<CoverabilityGraphNode>) toProof.clone();
            hierarcherProofer(toProof, horizontalSpace);
            for (CoverabilityGraphNode next : toEdit) {
                edit.put(next, next.getAttributes());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private LinkedList<CoverabilityGraphNode> hierarcher(CoverabilityGraphNode place, Rectangle2D bounds, int horizontalSpace, int verticalSpace, LinkedList<CoverabilityGraphNode> places) {
        // set bounds of given place and set it as drawn
        if (!place.isSetRecursiveBounds()) {
            GraphConstants.setBounds(place.getAttributes(), bounds);
            place.setIsSetRecursiveBounds(true);
            places.add(place);
        }
        bounds = GraphConstants.getBounds(place.getAttributes());
        LinkedList<CoverabilityGraphEdge> edges = new LinkedList<>();
        // get all ports from that node
        List<?> ports = place.getChildren();
        // iterate over the ports
        for (Object port1 : ports) {
            if (port1 instanceof CoverabilityGraphPort) {
                CoverabilityGraphPort port = (CoverabilityGraphPort) port1;
                Set<CoverabilityGraphEdge> edgeSet = port.getEdges();
                // iterate over ports edges
                for (CoverabilityGraphEdge next : edgeSet) {
                    if (port1 == next.getSource()) {
                        edges.add(next);
                    }
                }
            }
        }
        Iterator<CoverabilityGraphEdge> edgesIter = edges.iterator();
        int edgeCount = 0;
        LinkedList<CoverabilityGraphNode> children = new LinkedList<>();
        // iterate over the edges that leave that port
        while (edgesIter.hasNext()) {
            CoverabilityGraphEdge edge = edgesIter.next();
            CoverabilityGraphPort otherPort = getOtherPort(place, edge);
            // (x,y,w,h) x = left/right ; y = up/down
            CoverabilityGraphNode childPlace = null;
            if (otherPort != null) {
                childPlace = (CoverabilityGraphNode) otherPort.getParent();
            }
            if ((childPlace != null) && !childPlace.isSetRecursiveBounds()) {
                int puffer = 0;
                if (edgeCount > 0) {
                    puffer = horizontalSpace;
                }
                GraphConstants.setBounds(childPlace.getAttributes(), new Rectangle2D.Double(25 + bounds.getX() + ((puffer + bounds.getWidth()) * edgeCount++), bounds.getY() + bounds.getHeight() + verticalSpace, bounds.getWidth(), bounds.getHeight()));
                childPlace.setIsSetRecursiveBounds(true);
                places.add(childPlace);
                children.add(childPlace);
            }
        }
        for (CoverabilityGraphNode actualChild : children) {
            Rectangle2D childBound = GraphConstants.getBounds(actualChild.getAttributes());
            hierarcher(actualChild, childBound, horizontalSpace, verticalSpace, places);
        }
        return places;
    }

    private void hierarcherProofer(LinkedList<CoverabilityGraphNode> places, int horizontalSpace) {
        if (places.size() > 0) {
            // take the first of all places
            CoverabilityGraphNode first = places.removeFirst();
            Iterator<CoverabilityGraphNode> iter = places.iterator();
            boolean changedOne = false;
            // look if one of the rest has equal coordinates
            while (iter.hasNext()) {
                CoverabilityGraphNode actual = iter.next();
                if (GraphConstants.getBounds(actual.getAttributes()).getX() == GraphConstants.getBounds(first.getAttributes()).getX() &&
                        GraphConstants.getBounds(actual.getAttributes()).getY() == GraphConstants.getBounds(first.getAttributes()).getY()) {
                    // found one !
                    Rectangle2D bounds = GraphConstants.getBounds(actual.getAttributes());
                    // change it: take actual position, add the width and add the horizontal spacing
                    GraphConstants.setBounds(actual.getAttributes(), new Rectangle2D.Double(bounds.getX() + horizontalSpace + bounds.getWidth(), bounds.getY(), bounds.getWidth(), bounds.getHeight()));
                    changedOne = true;
                }
            }
            if (changedOne) {
                places.addFirst(first);
                hierarcherProofer(places, horizontalSpace);
            } else {
                hierarcherProofer(places, horizontalSpace);
            }
        }
    }

    private CoverabilityGraphPort getOtherPort(CoverabilityGraphNode place, CoverabilityGraphEdge edge) {
        List<?> ports = place.getChildren();
        for (int portIndex = 0; portIndex < ports.size(); portIndex++) {
            if (edge.getSource() == place.getChildAt(portIndex)) {
                return (CoverabilityGraphPort) edge.getTarget();
            } else if (edge.getTarget() == place.getChildAt(portIndex)) {
                return (CoverabilityGraphPort) edge.getSource();
            }
        }
        return null;
    }
}
