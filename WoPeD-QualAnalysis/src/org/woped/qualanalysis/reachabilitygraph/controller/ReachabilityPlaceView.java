package org.woped.qualanalysis.reachabilitygraph.controller;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;

public class ReachabilityPlaceView extends VertexView {
	
	private static final long serialVersionUID = 3973287972414121220L;
	
	protected ReachabilityPlaceRenderer renderer;

	public ReachabilityPlaceView(Object cell){
		super(cell);
		this.renderer = new ReachabilityPlaceRenderer();
	}
	
	public CellViewRenderer getRenderer(){
		return renderer;
	}
	
	private static class ReachabilityPlaceRenderer extends VertexRenderer implements CellViewRenderer
	{
		
		private static final long serialVersionUID = 1075415120773973965L;

		public ReachabilityPlaceRenderer()
		{
			super();
		}

		// Elliptic
		
		/*public Dimension getPreferredSize() {
			Dimension d = super.getPreferredSize();
			d.width += d.width / 8;
			d.height += d.height / 2;
			return d;
		}

		public void paint(Graphics g) {
			int b = borderWidth;
			Graphics2D g2 = (Graphics2D) g;
			Dimension d = getSize();
			boolean tmp = selected;
			if (super.isOpaque()) {
				g.setColor(super.getBackground());
				if (gradientColor != null && !preview) {
					setOpaque(false);
					g2.setPaint(new GradientPaint(0, 0, getBackground(),
							getWidth(), getHeight(), gradientColor, true));
				}
				g.fillOval(b - 1, b - 1, d.width - b, d.height - b);
			}
			try {
				setBorder(null);
				setOpaque(false);
				selected = false;
				super.paint(g);
			} finally {
				selected = tmp;
			}
			if (bordercolor != null) {
				g.setColor(bordercolor);
				g2.setStroke(new BasicStroke(b));
				g.drawOval(b - 1, b - 1, d.width - b, d.height - b);
			}
			if (selected) {
				g2.setStroke(GraphConstants.SELECTION_STROKE);
				g.setColor(highlightColor);
				g.drawOval(b - 1, b - 1, d.width - b, d.height - b);
			}
		}*/
	}
}
