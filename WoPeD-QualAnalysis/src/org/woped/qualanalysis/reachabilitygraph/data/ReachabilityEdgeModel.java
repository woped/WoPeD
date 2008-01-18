package org.woped.qualanalysis.reachabilitygraph.data;

import java.awt.geom.Point2D;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

public class ReachabilityEdgeModel extends DefaultEdge {

	private static final long serialVersionUID = -6452423946402905220L;

	public ReachabilityEdgeModel(TransitionObject to){
		super(to);
        AttributeMap attributes = getAttributes();
        GraphConstants.setMoveable(attributes, false);
        GraphConstants.setEditable(attributes, false);
        GraphConstants.setSizeable(attributes, false);
        GraphConstants.setDisconnectable(attributes, false);
        GraphConstants.setLabelPosition(attributes, new Point2D.Double(GraphConstants.PERMILLE*6/8, -20));
        GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_CLASSIC);
        setAttributes(attributes);
	}
	
}
