package org.woped.core.model.petrinet;

import java.awt.Color;

import org.woped.core.model.CreationMap;


public abstract class CombiOperatorTransitionModel 
	extends OperatorTransitionModel 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CombiOperatorTransitionModel(CreationMap map, int operatorType)
    {
    	super(map,operatorType);
    }
    
    
    public void setSecondaryUnderstandabilityColor(Color c) {
    	// alpha 0-255, the lower the brighter 
    	int alpha = 180;
    	this.secondaryUnderstandabilityColor = new Color(c.getRed(),
				c.getGreen(), c.getBlue(), alpha);
	}	
	
	public Color getSecondaryUnderstandabilityColor(){
		return this.secondaryUnderstandabilityColor;
	}
	
	//! Overridden to also reset the secondary color for understandability
	public void ResetUnderstandabilityColor() {
		
		super.ResetUnderstandabilityColor();
		setSecondaryUnderstandabilityColor(defaultUnderstandabilityColor);		
	}    
    
    private Color secondaryUnderstandabilityColor = defaultUnderstandabilityColor;
}
