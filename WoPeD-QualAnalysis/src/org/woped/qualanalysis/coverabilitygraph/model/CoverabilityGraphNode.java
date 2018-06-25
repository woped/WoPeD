/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 * <p>
 * This class was written by
 *
 * @author Benjamin Geiger
 */

package org.woped.qualanalysis.coverabilitygraph.model;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.qualanalysis.soundness.marking.Marking;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.SortedSet;

/**
 * This class represents a reachable marking in a coverability graph.
 */
public class CoverabilityGraphNode extends DefaultGraphCell {

    private static final long serialVersionUID = 8798017712097428249L;
    private boolean isBoundSet = false;
    private boolean highlight = false;
    private IMarking marking;

    /**
     * Constructs a new instance of a coverability graph node.
     *
     * @param marking the marking the node should represent
     */
    public CoverabilityGraphNode(IMarking marking) {
        super(marking);
        this.marking = marking;

        createPort();
        initializeAttributes();
    }

    /**
     * Gets the marking the node stands for.
     *
     * @return the marking of the node
     */
    public IMarking getMarking() {
        return this.marking;
    }

    /**
     * Sets the marking the node should represent.
     *
     * @param marking the new marking
     */
    public void setMarking(IMarking marking) {
        this.marking = marking;
    }

    // TODO: The methods recursive Bounds are only used for hierarchic layout and should be moved from this class to a specific wrapper
    /**
     * gets the marking of a place if it has been used in a recursive algorithm
     *
     * @return
     */
    public boolean isSetRecursiveBounds() {
        return this.isBoundSet;
    }

    /**
     * marks a cell as processed for recursive algorithms
     *
     * @param set
     */
    public void setIsSetRecursiveBounds(boolean set) {
        this.isBoundSet = set;
    }

    /**
     * Gets the html encoded tool tip message of the node.
     *
     * @return a message to be displayed as tooltip
     */
    public String getToolTipText() {

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<table>");
        sb.append("<tr><th>ID</th><th>#</th></tr>");

        SortedSet<PlaceNode> places = this.marking.getPlaces();
        for (PlaceNode place : places) {
            String token = this.marking.isPlaceUnbound(place) ? Marking.UNBOUND_SIGN : String.valueOf(this.marking.getTokens(place));
            sb.append(String.format("<tr><td>%s</td><td>%s</td></tr>", place.getName(), token));
        }

        sb.append("</table>");
        sb.append("</html>");
        return sb.toString();
    }

    /**
     * Gets if the node is currently highlighted.
     *
     * @return true if the node is highlighted, otherwise false
     */
    public boolean isHighlighted() {
        return this.highlight;
    }

    /**
     * Sets the highlighted state of the node to the provided value.
     *
     * @param highlighted the new state
     */
    public void setHighlighted(boolean highlighted) {
        this.highlight = highlighted;
    }

    /**
     * Gets the direct ancestors of the node.
     *
     * @return the direct ancestors of the node
     */
    public java.util.Collection<CoverabilityGraphNode> getDirectAncestors() {

        Collection<CoverabilityGraphNode> ancestors = new HashSet<>();
        Collection<CoverabilityGraphEdge> incomingEdges = getEdges(EdgeDirection.Incoming);

        for (CoverabilityGraphEdge edge : incomingEdges) {
            ancestors.add(edge.getSourceNode());
        }

        return ancestors;
    }

    /**
     * Gets the direct descendants of the node.
     *
     * @return the direct descendants
     */
    public java.util.Collection<CoverabilityGraphNode> getDirectDescendants() {
        java.util.List<CoverabilityGraphNode> children = new LinkedList<>();
        Collection<CoverabilityGraphEdge> edges = getEdges(EdgeDirection.Outgoing);

        for (CoverabilityGraphEdge edge : edges) {
            CoverabilityGraphPort childPort = (CoverabilityGraphPort) edge.getTarget();
            children.add((CoverabilityGraphNode) childPort.getParent());
        }

        return children;
    }

    /**
     * Gets all incoming edges of the node.
     * <p>
     * Incoming edges are edges where this node is the target of the edge.
     *
     * @return the incoming edges
     */
    public Collection<CoverabilityGraphEdge> getIncomingEdges() {
        return getEdges(EdgeDirection.Incoming);
    }

    /**
     * Gets all outgoing edges of the node.
     * <p>
     * Outgoing edges are edges where this node is the source of the edge.
     *
     * @return the outgoing edges
     */
    public Collection<CoverabilityGraphEdge> getOutgoingEdges() {
        return getEdges(EdgeDirection.Outgoing);
    }

    /**
     * Gets all connected edges of this node.
     * @return all connected edges.
     */
    public Collection<CoverabilityGraphEdge> getEdges() {
        return getEdges(EdgeDirection.Both);
    }

    private void createPort() {
        CoverabilityGraphPort port = new CoverabilityGraphPort();
        this.add(port);
        port.setParent(this);
    }

    private void initializeAttributes() {
        AttributeMap attributes = getAttributes();
        GraphConstants.setMoveable(attributes, true);
        GraphConstants.setEditable(attributes, false);
        GraphConstants.setSizeable(attributes, false);
        GraphConstants.setBounds(attributes, new Rectangle2D.Double(0, 0, 1, 1));
        GraphConstants.setLineWidth(attributes, 1);
        GraphConstants.setLineColor(attributes, Color.black);
        GraphConstants.setForeground(attributes, Color.black);
        GraphConstants.setOpaque(attributes, true);
    }

    private Collection<CoverabilityGraphEdge> getEdges(EdgeDirection edgeDirection) {

        java.util.Set<CoverabilityGraphEdge> edges = new HashSet<>();

        if (this.getChildCount() == 0) return edges;
        CoverabilityGraphPort port = (CoverabilityGraphPort) this.getFirstChild();

        for (Object elem : port.getEdges()) {
            if (!(elem instanceof CoverabilityGraphEdge)) continue;
            CoverabilityGraphEdge edge = (CoverabilityGraphEdge) elem;

            CoverabilityGraphNode source = (CoverabilityGraphNode) ((CoverabilityGraphPort) edge.getSource()).getParent();
            CoverabilityGraphNode target = (CoverabilityGraphNode) ((CoverabilityGraphPort) edge.getTarget()).getParent();
            if (source == null || target == null) continue;

            if (source.equals(this) && (edgeDirection == EdgeDirection.Outgoing || edgeDirection == EdgeDirection.Both)) {
                edges.add(edge);
            }

            if (target.equals(this) && (edgeDirection == EdgeDirection.Incoming || edgeDirection == EdgeDirection.Both)) {
                edges.add(edge);
            }
        }

        return edges;
    }

    private enum EdgeDirection {
        Incoming, Outgoing, Both;
    }
}
