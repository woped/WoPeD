package org.woped.qualanalysis.coverabilitygraph.model;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jgraph.graph.AbstractCellView;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.woped.core.utilities.ShortLexStringComparator;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.soundness.datamodel.TransitionNode;

/**
 * This class represents the connection between 2 reachable markings when firing a specific transition.
 */
public class CoverabilityGraphEdge extends DefaultEdge {

    static final String TRANSITION_DELIMITER = "\u2228"; // logical or
    private static final long serialVersionUID = -6452423946402905221L;

    private SortedSet<TransitionNode> triggers;

    /**
     * Constructs a new coverability graph edge without source and target.
     *
     * @param trigger the transition that triggers this edge
     */
    public CoverabilityGraphEdge(TransitionNode trigger) {
        this(null, null, trigger);
    }

    /**
     * Constructs a new coverability graph edge between the provided source and target.
     *
     * @param from the source of the edge
     * @param to the target of the edgw
     * @param trigger the transition that triggers the edge
     */
    public CoverabilityGraphEdge(CoverabilityGraphNode from, CoverabilityGraphNode to, TransitionNode trigger) {
        super(null);

        if (from != null)
            this.setSource(from.getFirstChild());

        if (to != null)
            this.setTarget(to.getFirstChild());

        this.triggers = new TreeSet<>(new Comparator<TransitionNode>() {
            ShortLexStringComparator comparator = new ShortLexStringComparator();

            @Override
            public int compare(TransitionNode o1, TransitionNode o2) {
                return comparator.compare(o1.getId(), o2.getId());
            }
        });
        triggers.add(trigger);

        installAttributes();
    }

    /**
     * Gets the source node of the edge.
     *
     * @return the source node of the edge
     */
    public CoverabilityGraphNode getSourceNode() {
        return getNode(this.getSource());
    }

    /**
     * Gets the target node of the edge.
     *
     * @return the target node of the edge
     */
    public CoverabilityGraphNode getTargetNode() {
        return getNode(this.getTarget());
    }

    /**
     * Gets a html formatted String that can be uses as tooltip.
     *
     * @return the tool tip text
     */
    public String getToolTipText() {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("<html><h4>%s</h4><table><tr><th>ID</th><th>Name</th></tr>",
                Messages.getString("QuanlAna.ReachabilityGraph.Transition")));

        for (TransitionNode t : triggers) {
            sb.append(String.format("<tr><td>%s</td><td>%s</td></tr>", t.getId(), t.getName()));
        }

        sb.append("</table></html>");
        return sb.toString();
    }

    /**
     * Returns if the edge is parallel to the provided edge.
     *
     * @param other the edge to compare against
     * @return true if the edge is parallel to the other edge, otherwise false
     */
    public boolean isParallel(CoverabilityGraphEdge other) {
        return this.getSourceNode().equals(other.getSourceNode()) &&
                this.getTargetNode().equals(other.getTargetNode());
    }

    /**
     * Adds a trigger to the edge.
     * <p>
     * This can be used to combine edges if the only differ in the trigger (same source and target).
     *
     * @param trigger the transition that triggers the edge
     */
    public void addTrigger(TransitionNode trigger) {
        this.triggers.add(trigger);
    }

    /**
     * Adds all triggers to the edge.
     * <p>
     * Duplicates will be ignored.
     *
     * @param triggers the triggers to add.
     */
    public void addTriggers(Collection<TransitionNode> triggers){
        this.triggers.addAll(triggers);
    }

    /**
     * Gets the transitions that triggers the edge.
     *
     * @return the transitions that triggers the edge
     */
    public Collection<TransitionNode> getTriggers() {
        return triggers;
    }

    @Override
    public String toString() {
        return convertTriggersToString();
    }

    private void installAttributes() {
        AttributeMap attributes = getAttributes();
        GraphConstants.setEditable(attributes, false);
        GraphConstants.setMoveable(attributes, true);
        GraphConstants.setSizeable(attributes, true);
        GraphConstants.setBendable(attributes, true);
        GraphConstants.setSelectable(attributes, true);
        GraphConstants.setConnectable(attributes, false);
        GraphConstants.setDisconnectable(attributes, false);
        GraphConstants.setLabelPosition(attributes, new Point2D.Double(GraphConstants.PERMILLE * 0.33, 0));
        GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_CLASSIC);
        GraphConstants.setEndFill(attributes, true);
        GraphConstants.setOpaque(attributes, true);

        GraphConstants.setForeground(attributes, Color.DARK_GRAY);
        GraphConstants.setLineColor(attributes, Color.DARK_GRAY);
        GraphConstants.setLineWidth(attributes, 1);
    }

    private CoverabilityGraphNode getNode(Object port) {
        return (CoverabilityGraphNode) ((CoverabilityGraphPort) port).getParent();
    }

    private String convertTriggersToString() {
        StringBuilder sb = new StringBuilder();

        int added = 0;
        for (TransitionNode t : triggers) {
            sb.append(t.getName());
            added++;

            if (added < triggers.size()) {
                sb.append(String.format(" %s ", TRANSITION_DELIMITER));
            }
        }

        return sb.toString();
    }

    /**
     * Routing that moves the self loop under the node (default is above)
     */
    public static class CgRouting extends DefaultEdge.DefaultRouting {
        protected java.util.List routeLoop(GraphLayoutCache cache, EdgeView edge) {
            java.util.List newPoints = new ArrayList();
            newPoints.add(edge.getSource());
            CellView sourceParent = (edge.getSource() != null) ? edge
                    .getSource().getParentView() : edge.getSourceParentView();
            if (sourceParent != null) {
                Point2D from = AbstractCellView.getCenterPoint(sourceParent);
                Rectangle2D rect = sourceParent.getBounds();
                double width = rect.getWidth();
                double height2 = rect.getHeight() / 2;
                double loopWidth = Math.min(20, Math.max(10, width / 8));
                double loopHeight = Math.min(30, Math.max(12, Math.max(
                        loopWidth + 4, height2 / 2)));
                newPoints.add(edge.getAttributes().createPoint(
                        from.getX() - loopWidth,
                        from.getY() + height2 + loopHeight * 1.2));
                newPoints.add(edge.getAttributes().createPoint(from.getX(),
                        from.getY() + height2 + 1.5 * loopHeight));
                newPoints.add(edge.getAttributes().createPoint(
                        from.getX() + loopWidth,
                        from.getY() + height2 + loopHeight * 1.2));
                newPoints.add(edge.getTarget());
                return newPoints;
            }
            return null;
        }
    }
}
