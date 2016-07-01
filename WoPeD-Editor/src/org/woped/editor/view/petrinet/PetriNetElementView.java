package org.woped.editor.view.petrinet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.VertexRenderer;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.model.petrinet.Toolspecific.OperatorDirection;
import org.woped.core.model.petrinet.Toolspecific.OperatorPosition;
import org.woped.core.view.AbstractElementView;


@SuppressWarnings("serial")
public class PetriNetElementView extends AbstractElementView {
	
	protected IEditor editor;
	
	public PetriNetElementView(Object cell, IEditor editor)
	{
		super(cell);
		this.editor = editor;
	}
 	protected abstract class PetriNetElementRenderer extends VertexRenderer {
    	private boolean readOnly;
    	private Color readOnlyColor = new Color(225, 225, 225);
    	private Color highLightedColor = new Color(255, 0, 0, 128);
    	private Color HighLightedRGColor = new Color(188, 235, 253);
    	public PetriNetElementRenderer(Object cell) {
    		AbstractPetriNetElementModel model = (AbstractPetriNetElementModel) cell;
    		readOnly = model.isReadOnly();
    	}

    	//! Get the fill color to be used 
    	//! @return Color to be used
    	protected Color getFillColor() {
    		return getFillColor(((AbstractPetriNetElementModel) 
    				(PetriNetElementView.this.getCell())).getColor());    		
    	}
    	
    	
    	//! Get the fill color to be used depending on the understandability color passed
    	//! as a parameter
    	//! @param understandabilityColor Specifies the color to be used if understandability
    	//!								  is switched on and is not superseded by some other color
    	//! @return Color to be used
    	protected Color getFillColor(Color understandabilityColor) {
    		Color result = null;
    		if (((AbstractPetriNetElementModel) (PetriNetElementView.this.getCell())).isHighlighted())
    			result = highLightedColor;
    		else if (isUnderstandabilityActive()){
    				result = understandabilityColor;
    				return result;
    		}
    		else if(((AbstractPetriNetElementModel) (PetriNetElementView.this.getCell())).isRGHighlighted()){
    			result = HighLightedRGColor;
    		}    		
    		else { 
    			if (readOnly) {
    				result = readOnlyColor;
    			} 		
    			else { 
    				if (isActive()) {
    					return (new Color(50, 200, 100, 25));
    				} 			
    				else {
	    				result = super.getBackground();
    				}
    			}
    		}
    		return result;
    	}

    	//! Get the color to be used for inner drawings
    	//! Note that this color is context-sensitive
    	//! and will change if an element is active
    	//! @return Color to be used for inner drawings
    	protected Color getInnerDrawingsColor() {
    		// When active during the token game,
    		// draw the inner symbols in gray
    		if (isActive())
    			return (new Color(50, 200, 100));
    		else {
    			if (bordercolor == null)
    				return Color.BLACK;
    			else
    				return bordercolor;
    		}
    	}

    	/**
    	 * @return
    	 */
    	public boolean isActive()
    	{
    		return PetriNetElementView.this.isActivated();
    	}

    	//! Draw one of the famous operator arrows
    	//! @param g specifies the graphical context to be used for drawing
    	//! @param positionRightSide if true, draw the arrow on the right third of the operator element
    	//! @param directionRightSide if true, draw the arrow such that it points to the right side of the element
    	protected void drawOperatorArrow(Graphics g, boolean positionRightSide, boolean directionRightSide) {
    		Dimension d = getSize();
    		int b = borderWidth;

    		int tipy = 0;
    		int tipx = 0;
    		int basex = 0;
    		int linex = 0;

    		// To be positioned on the right side ?
    		if (positionRightSide) {
    			// Determine direction of the arrow
    			tipy = d.height / 2;
    			tipx = directionRightSide ? (d.width - b) : (2 * d.width / 3);
    			basex = directionRightSide ? (2 * d.width / 3) : (d.width - b);
    			linex = 2 * d.width / 3;
    		} 
    		else {
    			// Determine direction of the arrow
    			tipy = d.height / 2;
    			tipx = directionRightSide ? (d.width / 3) : b;
    			basex = directionRightSide ? b : (d.width / 3);
    			linex = d.width / 3;
    		}

    		// draw light gray polygons
    		Color drawingColor = getInnerDrawingsColor();
    		g.setColor(new Color(drawingColor.getRed(),
    				drawingColor.getGreen(), drawingColor.getBlue(), 25));

    		Polygon p = new Polygon();
    		p.addPoint(tipx, tipy);
    		p.addPoint(tipx, b);
    		p.addPoint(basex, b);
    		g.fillPolygon(p);

    		p = new Polygon();
    		p.addPoint(tipx, tipy);
    		p.addPoint(tipx, d.height - b);
    		p.addPoint(basex, d.height - b);
    		g.fillPolygon(p);

    		// draw arrow
    		g.setColor(getInnerDrawingsColor());
    		g.drawLine(linex, b, linex, d.height - b);
    		g.drawLine(basex, b, tipx, tipy);
    		g.drawLine(basex, d.height - b, tipx, tipy);
    	}
    	
    	//XX
    	protected void drawOperatorArrow2( Graphics g, OperatorPosition arrowPosition, OperatorDirection arrowDirection) {

        	// the infamous arrow -- orientation remix
        	
        	//position = arrowPosition; // save location
        	
        	int height = getSize().height;
        	int width = getSize().width;
        	int bw = borderWidth;

        	g.setColor(getInnerDrawingsColor());
        	// g.setColor(java.awt.Color.BLUE);
        	
        	Polygon p = new Polygon();

         	switch( arrowPosition ) {
	        	case NORTH:
		        	if ( arrowDirection == OperatorDirection.IN ) {
		        		p.addPoint( bw, bw );
		        		p.addPoint( width/2, height/3 );
		        		p.addPoint( width-bw, bw );
		        		p.addPoint( width-bw, height/3 );
		        		p.addPoint( bw, height/3 );
		        	} else { 
		        		p.addPoint( bw, bw );
		        		p.addPoint( width-bw, bw );
		        		p.addPoint( width-bw, height/3 );
		        		p.addPoint( width/2, bw );
		        		p.addPoint( bw, height/3 );
		        		g.drawLine( bw, height/3, width-bw, height/3 );
		        	}
		        	break;
	        	case EAST:
		        	if ( arrowDirection == OperatorDirection.IN ) {
			        	p.addPoint( width*2/3, bw );
			        	p.addPoint( width-bw, bw );
			        	p.addPoint( width*2/3, height/2 );
			        	p.addPoint( width-bw, height-bw );
			        	p.addPoint( width*2/3, height-bw );
		        	} else {
			        	p.addPoint( width*2/3, bw );
			        	p.addPoint( width-bw, bw );
			        	p.addPoint( width-bw, height-bw );
			        	p.addPoint( width*2/3, height-bw );
			        	p.addPoint( width-bw, height/2 );
			        	g.drawLine( width*2/3, bw, width*2/3, height-bw );
		        	}
		        	break;
	        	case SOUTH:
	        		if ( arrowDirection == OperatorDirection.IN ) {
	        			p.addPoint( bw, height*2/3 );
	        			p.addPoint( width-bw, height*2/3 );
	        			p.addPoint( width-bw, height-bw );
	        			p.addPoint( width/2, height*2/3 );
	        			p.addPoint( bw, height-bw );
	        		} else {
	        			p.addPoint( bw, height*2/3 );
	        			p.addPoint( width/2, height-bw );
	        			p.addPoint( width-bw, height*2/3 );
	        			p.addPoint( width-bw, height-bw );
	        			p.addPoint( bw, height-bw );
	        			g.drawLine( bw, height*2/3, width-bw, height*2/3 );
	        		}
	        		break;
	        	case WEST:
	        		if ( arrowDirection == OperatorDirection.IN ) {
	        			p.addPoint( bw, bw );
	        			p.addPoint( width/3, bw );
	        			p.addPoint( width/3, height-bw );
	        			p.addPoint( bw, height-bw );
	        			p.addPoint( width/3, height/2 );
	        		} else {
	        			p.addPoint( bw, bw );
	        			p.addPoint( width/3, bw );
	        			p.addPoint( bw, height/2 );
	        			p.addPoint( width/3, height-bw );
	        			p.addPoint( bw, height-bw );
	        			g.drawLine( width/3, bw, width/3, height-bw );
	        		}

         	}
        	
        	g.drawPolygon(p);
    		Color drawingColor = getInnerDrawingsColor();
    		g.setColor(new Color(drawingColor.getRed(),
    				drawingColor.getGreen(), drawingColor.getBlue(), 25));
        	g.fillPolygon(p);
        	

        }
    	
    }
    
    private String getAbbrev(int u){
    	switch (u){
    	case 0:
    		return "s";
    	case 1:
    		return "m";
    	case 2:
    		return "h";
    	case 3:
    		return "d";
    	case 4:
    		return "w";
    	case 5:
    		return "M";
    	case 6:
    		return "y";
    	default:
    		return "";
    	}
    }
    
    public void drawTime(Graphics2D g2, Dimension d){
    	TransitionModel trans = (TransitionModel)this.getCell();
    	
    	String time = Integer.toString(trans.getToolSpecific().getTime());
    	int timeUnit = trans.getToolSpecific().getTimeUnit();
    	String tuString = getAbbrev(timeUnit);
    	
    	if (!time.equals("0") &&
    		trans.getToolSpecific().getTrigger() != null &&
			trans.getToolSpecific().getTrigger().getTriggertype() == TriggerModel.TRIGGER_RESOURCE) {
    		Font timeFont = DefaultStaticConfiguration.DEFAULT_SMALLLABEL_FONT;
    		g2.setColor(DefaultStaticConfiguration.DEFAULT_TIME_COLOR);
    		g2.setFont(timeFont);
    		Rectangle2D bounds = timeFont.getStringBounds(time, g2.getFontRenderContext());
    		int xCoord = (int)((d.width - bounds.getWidth())/2);
    		int yCoord = (int)((d.height - bounds.getHeight() - bounds.getY())/2);
    		g2.drawString(time, xCoord, yCoord);
    		Font unitFont = DefaultStaticConfiguration.DEFAULT_TINYLABEL_FONT;
    		g2.setFont(unitFont);
    		bounds = unitFont.getStringBounds(tuString, g2.getFontRenderContext());
    		xCoord = (int)((d.width - bounds.getWidth())/2);
    		g2.drawString(tuString, xCoord, yCoord + 15);
    	}
    }	
			
    public boolean isActivated() {
    	return ((AbstractPetriNetElementModel) getCell()).isActivated() && editor.isTokenGameEnabled();
    }
    
    public boolean isUnderstandabilityActive() {
    	return ((AbstractPetriNetElementModel) getCell()).isUnderstandabilityColoringActive();
    }

}
