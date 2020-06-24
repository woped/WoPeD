package org.woped.core.model.petrinet;

import java.awt.Color;

import org.woped.core.model.CreationMap;


@SuppressWarnings("serial")
public abstract class CombiOperatorTransitionModel 
	extends OperatorTransitionModel 
{
	private Color secondaryUnderstandabilityColor = defaultUnderstandabilityColor;


	public CombiOperatorTransitionModel(CreationMap map, int operatorType)
    {
		super(map);
	}
	
	public Color getSecondaryUnderstandabilityColor(){
		return this.secondaryUnderstandabilityColor;
	}

	public void setSecondaryUnderstandabilityColor(Color c) {
		// alpha 0-255, the lower the brighter
		int alpha = 180;
		this.secondaryUnderstandabilityColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}

	//! Overridden to also reset the secondary color for understandability
	public void ResetUnderstandabilityColor() {

		super.ResetUnderstandabilityColor();
		setSecondaryUnderstandabilityColor(defaultUnderstandabilityColor);
	}
}
