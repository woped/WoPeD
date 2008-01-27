package org.woped.qualanalysis.reachabilitygraph.controller;

import java.awt.Dimension;
import java.awt.Graphics;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.woped.core.utilities.LoggerManager;
import org.woped.qualanalysis.Constants;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityEdgeModel;

public class ReachabilityEdgeView extends EdgeView {
	
	private static final long serialVersionUID = -1042289353330966443L;
	
	protected ReachabilityEdgeRenderer renderer;
	
	public ReachabilityEdgeView(Object cell){
		super(cell);
		this.setAttributes(((ReachabilityEdgeModel)cell).getAttributes());
		this.renderer = new ReachabilityEdgeRenderer();
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
