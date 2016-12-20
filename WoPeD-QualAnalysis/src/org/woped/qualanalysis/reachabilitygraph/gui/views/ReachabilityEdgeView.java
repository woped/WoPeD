package org.woped.qualanalysis.reachabilitygraph.gui.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityEdgeModel;

public class ReachabilityEdgeView extends EdgeView {
	
	private static final long serialVersionUID = -1042289353330966443L;
	
	private Color OutgoiningColor = new Color(153,0,0);
	private Color IngoingColor = new Color(180,203,35);
	private Color NormalColor = Color.black;
	
	protected ReachabilityEdgeRenderer renderer;
	
	public ReachabilityEdgeView(Object cell){
		super(cell);
		this.setAttributes(((ReachabilityEdgeModel)cell).getAttributes());
		this.renderer = new ReachabilityEdgeRenderer();
		setLineColor(cell);	
	}
	
	public void setLineColor(Object cell){
		ReachabilityEdgeModel edge = (ReachabilityEdgeModel) cell;
		if (edge.getOutgoing())
			GraphConstants.setLineColor(edge.getAttributes(), OutgoiningColor);
		else if (edge.getIngoing())	
			GraphConstants.setLineColor(edge.getAttributes(), IngoingColor);
		else 
			GraphConstants.setLineColor(edge.getAttributes(), NormalColor);						
	}
	
	public CellViewRenderer getRenderer(){
		return renderer;
	}
	
	private static class ReachabilityEdgeRenderer extends EdgeRenderer implements CellViewRenderer {

		private static final long serialVersionUID = -741470296333158450L;
		
		public ReachabilityEdgeRenderer(){
			super();
		}

		public Dimension getPreferredSize() {
			return super.getPreferredSize();
		}

		public void paint(Graphics g) {
			super.paint(g);
		}
	}
}
