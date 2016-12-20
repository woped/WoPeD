package org.woped.qualanalysis.reachabilitygraph.gui.views;

import java.awt.*;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;
import org.woped.qualanalysis.reachabilitygraph.data.model.ReachabilityPlaceModel;
import org.woped.qualanalysis.soundness.marking.IMarking;

public class ReachabilityPlaceView extends VertexView {
	
	private static final long serialVersionUID = 3973287972414121220L;
	
	protected ReachabilityPlaceRenderer renderer;
	private Color grayscaledHighlightedColor = new Color(188,235,253);
	private Color grayscaledRootColor = new Color(168,168,168);
	private Color grayscaledNormalColor = Color.lightGray;
	
	private Color coloredHighlightedColor = new Color(188,235,253);
	private Color coloredRootColor = Color.green;
	private Color coloredNormalColor = Color.orange;

	public ReachabilityPlaceView(Object cell){
		super(cell);
		this.renderer = new ReachabilityPlaceRenderer();
		setBackgroundColor(cell);	
	}
	
	public void setBackgroundColor(Object cell){
		ReachabilityPlaceModel place = (ReachabilityPlaceModel) cell;
		if(place.getGrayscaled()){
    		if(place.getHighlight())
    			GraphConstants.setBackground(place.getAttributes(), grayscaledHighlightedColor);    		
    		else if (((IMarking) place.getUserObject()).isInitial()) 
    			GraphConstants.setBackground(place.getAttributes(), grayscaledRootColor);
    		else 
    			GraphConstants.setBackground(place.getAttributes(), grayscaledNormalColor);
    	}
    	else{
    		if(place.getHighlight())
    			GraphConstants.setBackground(place.getAttributes(), coloredHighlightedColor);    		
    		else if (((IMarking) place.getUserObject()).isInitial()) 
    			GraphConstants.setBackground(place.getAttributes(), coloredRootColor);
    		else 
    			GraphConstants.setBackground(place.getAttributes(), coloredNormalColor);    		
    	}		
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

		public Dimension getPreferredSize() {
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
		}
	}
}
