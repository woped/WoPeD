/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 * <p>
 * This class was written by
 *
 * @author Benjamin Geiger
 */

package org.woped.qualanalysis.reachabilitygraph.gui.layout;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.AbstractReachabilityGraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityEdgeModel;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityPlaceModel;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityPortModel;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

public class HierarchicLayout implements IReachabilityLayout {

    private Map<ReachabilityPlaceModel, AttributeMap> edit;

    @Override
    public ReachabilityJGraph layoutGraph(ReachabilityJGraph graph, Dimension dimension) {
        edit = new HashMap<>();
        applyHierarchicLayout(graph);
        graph.getGraphLayoutCache().edit(edit, null, null, null);
        boolean colored = Boolean.parseBoolean(graph.getAttributeMap().get("reachabilityGraph.color"));
        AbstractReachabilityGraphModel.setGrayScale(graph, !colored);
        return graph;
    }

    /**
     * applies a hierarchic layout on a given graph.
     *
     * @param graph
     */
    private void applyHierarchicLayout(ReachabilityJGraph graph) {
        GraphModel model = graph.getModel();
        LinkedList<ReachabilityPlaceModel> markings = new LinkedList<ReachabilityPlaceModel>();
        // get all marking and reset them to not recursively touched
        for (int i = 0; i < model.getRootCount(); i++) {
            if (model.getRootAt(i) instanceof ReachabilityPlaceModel) {
                ((ReachabilityPlaceModel) model.getRootAt(i)).setIsSetRecursiveBounds(false);
                markings.add((ReachabilityPlaceModel) model.getRootAt(i));
            }
        }
        // get initial marking. the begin oh each graph.
        ReachabilityPlaceModel initialPlace = AbstractReachabilityGraphModel.lookupInitialMarking(markings);
        if (initialPlace != null) {
            // initialize a lot of things
            Rectangle2D bounds = GraphConstants.getBounds(initialPlace.getAttributes());
            LinkedList<ReachabilityPlaceModel> toProof = new LinkedList<ReachabilityPlaceModel>();
            HashMap<String, String> graphAttributes = graph.getAttributeMap();

            int verticalSpace = 80;
            int horizontalSpace = 25;
            if (graphAttributes.containsKey("reachabilityGraph.hierarchic.verticalSpace") && graphAttributes.containsKey("reachabilityGraph.hierarchic.horizontalSpace")) {
                verticalSpace = Integer.parseInt(graphAttributes.get("reachabilityGraph.hierarchic.verticalSpace"));
                horizontalSpace = Integer.parseInt(graphAttributes.get("reachabilityGraph.hierarchic.horizontalSpace"));
            }

            hierarcher(initialPlace, new Rectangle2D.Double(10, 0, bounds.getWidth(), bounds.getHeight()), horizontalSpace, verticalSpace, toProof);

            LinkedList<ReachabilityPlaceModel> toEdit = (LinkedList<ReachabilityPlaceModel>) toProof.clone();
            hierarcherProofer(toProof, horizontalSpace, verticalSpace);
            Iterator<ReachabilityPlaceModel> iter = toEdit.iterator();
            while (iter.hasNext()) {
                ReachabilityPlaceModel next = iter.next();
                edit.put(next, next.getAttributes());
            }
        }
    }

    /**
     * is a recursive helper-method for layout the graph in a hierarchic way.
     *
     * @param place
     * @param bounds
     * @param places
     * @return
     */
    private LinkedList<ReachabilityPlaceModel> hierarcher(ReachabilityPlaceModel place, Rectangle2D bounds, int horizontalSpace, int verticalSpace, LinkedList<ReachabilityPlaceModel> places) {
        // set bounds of given place and set it as drawn
        if (!place.isSetRecursiveBounds()) {
            GraphConstants.setBounds(place.getAttributes(), bounds);
            place.setIsSetRecursiveBounds(true);
            places.add(place);
        }
        bounds = GraphConstants.getBounds(place.getAttributes());
        LinkedList<ReachabilityEdgeModel> edges = new LinkedList<ReachabilityEdgeModel>();
        // get all ports from that node
        List<?> ports = place.getChildren();
        // iterate over the ports
        for (int portIndex = 0; portIndex < ports.size(); portIndex++) {
            if (ports.get(portIndex) instanceof ReachabilityPortModel) {
                ReachabilityPortModel port = (ReachabilityPortModel) ports.get(portIndex);
                Set<ReachabilityEdgeModel> edgeSet = port.getEdges();
                Iterator<ReachabilityEdgeModel> edgeIterator = edgeSet.iterator();
                // iterate over ports edges
                while (edgeIterator.hasNext()) {
                    ReachabilityEdgeModel next = edgeIterator.next();
                    if (ports.get(portIndex) == next.getSource()) {
                        edges.add(next);
                    }
                }
            }
        }
        Iterator<ReachabilityEdgeModel> edgesIter = edges.iterator();
        int edgeCount = 0;
        LinkedList<ReachabilityPlaceModel> childs = new LinkedList<ReachabilityPlaceModel>();
        // iterate over the edges that leave that port
        while (edgesIter.hasNext()) {
            ReachabilityEdgeModel edge = (ReachabilityEdgeModel) edgesIter.next();
            ReachabilityPortModel otherPort = getOtherPort(place, edge);
            // (x,y,w,h) x = left/right ; y = up/down
            ReachabilityPlaceModel childPlace = (ReachabilityPlaceModel) otherPort.getParent();
            if (!childPlace.isSetRecursiveBounds()) {
                int puffer = 0;
                if (edgeCount > 0) {
                    puffer = horizontalSpace;
                }
                GraphConstants.setBounds(childPlace.getAttributes(), new Rectangle2D.Double(25 + bounds.getX() + ((puffer + bounds.getWidth()) * edgeCount++), bounds.getY() + bounds.getHeight() + verticalSpace, bounds.getWidth(), bounds.getHeight()));
                childPlace.setIsSetRecursiveBounds(true);
                places.add(childPlace);
                childs.add(childPlace);
            }
        }
        Iterator<ReachabilityPlaceModel> iterChilds = childs.iterator();
        while (iterChilds.hasNext()) {
            ReachabilityPlaceModel actualChild = iterChilds.next();
            Rectangle2D childBound = GraphConstants.getBounds(actualChild.getAttributes());
            hierarcher(actualChild, childBound, horizontalSpace, verticalSpace, places);
        }
        return places;
    }

    /**
     * after the call of hierarcher() it's possible that some markings are covered by each other.
     * The hierarcherProofer looks for that vertices and corrects their layout.
     *
     * @param places
     */
    private void hierarcherProofer(LinkedList<ReachabilityPlaceModel> places, int horizontalSpace, int verticalSpace) {
        if (places.size() > 0) {
            // take the first of all places
            ReachabilityPlaceModel first = places.removeFirst();
            Iterator<ReachabilityPlaceModel> iter = places.iterator();
            boolean changedOne = false;
            // look if one of the rest has equal coordinates
            while (iter.hasNext()) {
                ReachabilityPlaceModel actual = iter.next();
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
                hierarcherProofer(places, horizontalSpace, verticalSpace);
            } else {
                hierarcherProofer(places, horizontalSpace, verticalSpace);
            }
        }
    }

    /**
     * takes a edge and place, gets the port where the edge is connected and then
     * returns the other port of the edge
     *
     * @param place
     * @param edge
     * @return
     */
    private ReachabilityPortModel getOtherPort(ReachabilityPlaceModel place, ReachabilityEdgeModel edge) {
        List<?> ports = place.getChildren();
        for (int portIndex = 0; portIndex < ports.size(); portIndex++) {
            if (edge.getSource() == place.getChildAt(portIndex)) {
                return (ReachabilityPortModel) edge.getTarget();
            } else if (edge.getTarget() == place.getChildAt(portIndex)) {
                return (ReachabilityPortModel) edge.getSource();
            }
        }
        return null;
    }

}
