package org.woped.editor.view.petrinet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import org.woped.core.controller.IEditor;
import org.woped.core.model.petrinet.CombiOperatorTransitionModel;

@SuppressWarnings("serial")
public class CombiOperatorView extends TransSimpleView {
	
    public CombiOperatorView(Object cell, IEditor editor) {
    	super(cell, editor);
    }
	
    protected abstract class  CombiOperatorRenderer extends PetriNetElementRenderer {

    	public CombiOperatorRenderer(Object cell) {
    		super(cell);
    	}
    	
    	//! Draw the background of the combi-operator, taking into account
    	//! the different background colors for the left and the right part of the
    	//! operator as well as the specified border width 
    	//! @param g Graphics object to be used for drawing
    	//! @param b Width of the border to be spared in pixels    	
    	protected void drawOperatorBackground(Graphics g, int b) {    		
            Dimension d = getSize();
    		
            g.setColor(getFillColor());
            g.fillRect(b, b, d.width/3, d.height - b - 1);
            g.setColor(getFillColor(Color.white));	
            g.fillRect(d.width/3, b, d.width/3, d.height - b - 1);            
            g.setColor(getSecondaryFillColor());
            g.fillRect( 2*d.width/3, b, d.width/3, d.height - b - 1);            
    	}
 
    	
    	//! Get the secondary fill color to be used 
    	//! @return Color to be used
    	protected Color getSecondaryFillColor() {
    		return getFillColor(((CombiOperatorTransitionModel) 
    				(CombiOperatorView.this.getCell())).getSecondaryUnderstandabilityColor());    		
    	}
    }
}
