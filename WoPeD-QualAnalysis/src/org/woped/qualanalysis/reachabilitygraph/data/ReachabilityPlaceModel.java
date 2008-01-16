package org.woped.qualanalysis.reachabilitygraph.data;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

@SuppressWarnings("serial")
public class ReachabilityPlaceModel extends DefaultGraphCell
{

    public ReachabilityPlaceModel(Object o){
    	super(o);
        AttributeMap attributes = getAttributes();
        GraphConstants.setMoveable(attributes, true);
        GraphConstants.setEditable(attributes, false);
        GraphConstants.setSizeable(attributes, false);
        //GraphConstants.setAutoSize(attributes, true);
     	// (x,y,w,h)
		GraphConstants.setBounds(attributes, new Rectangle2D.Double(0,0,80,20));
		//GraphConstants.setBounds(attributes, new Rectangle2D.Double(140,140,80,20));
		GraphConstants.setGradientColor(attributes, Color.red);
		GraphConstants.setOpaque(attributes, true);
        setAttributes(attributes);
    }
    
    public String getToolTipText()
    {
        return "MyTooltipText";
    }
    
}
