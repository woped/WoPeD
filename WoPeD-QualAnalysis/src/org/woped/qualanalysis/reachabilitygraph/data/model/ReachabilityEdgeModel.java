package org.woped.qualanalysis.reachabilitygraph.data.model;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;
import org.woped.gui.translations.Messages;

import java.awt.geom.Point2D;

public class ReachabilityEdgeModel extends DefaultEdge {

    private static final long serialVersionUID = -6452423946402905220L;
    private boolean Outgoing = false;
    private boolean Ingoing = false;

    public ReachabilityEdgeModel(ReachabilityPlaceModel from, ReachabilityPlaceModel to, Object trigger) {
        super(trigger);

        this.setSource(from.getFirstChild());
        this.setTarget(to.getFirstChild());
    }

    public ReachabilityEdgeModel(Object to) {
        super(to);
        AttributeMap attributes = getAttributes();
        GraphConstants.setMoveable(attributes, true);
        GraphConstants.setEditable(attributes, false);
        GraphConstants.setSizeable(attributes, false);
        GraphConstants.setDisconnectable(attributes, false);
        GraphConstants.setLabelPosition(attributes, new Point2D.Double(GraphConstants.PERMILLE * 0.6, 0));
        GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_TECHNICAL);
        GraphConstants.setOpaque(attributes, true);
        setAttributes(attributes);
    }

    public String getToolTipText() {
        return "<html>" + Messages.getString("QuanlAna.ReachabilityGraph.Transition") + "<br>" + this.getUserObject().toString() + "<html>";
    }

    public boolean getIngoing() {
        return Ingoing;
    }

    public void setIngoing(boolean value) {
        Ingoing = value;
    }

    public boolean getOutgoing() {
        return Outgoing;
    }

    public void setOutgoing(boolean value) {
        Outgoing = value;
    }
}
