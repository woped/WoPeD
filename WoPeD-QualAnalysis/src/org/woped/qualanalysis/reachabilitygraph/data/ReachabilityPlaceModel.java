/**
 * ReachabilityGraph implementation was done by Manuel Fladt and Benjamin Geiger.
 * The code was written for a project at BA Karlsruhe in 2007/2008 under authority
 * of Prof. Dr. Thomas Freytag and Andreas Eckleder.
 * 
 * This class was written by
 * @author Benjamin Geiger
 */

package org.woped.qualanalysis.reachabilitygraph.data;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.woped.translations.Messages;

public class ReachabilityPlaceModel extends DefaultGraphCell
{

	private static final long serialVersionUID = 8798017712097428249L;
	private boolean isBoundSet = false;
	
    public ReachabilityPlaceModel(Object o){
    	super(o);
        AttributeMap attributes = getAttributes();
        GraphConstants.setMoveable(attributes, true);
        GraphConstants.setEditable(attributes, false);
        GraphConstants.setSizeable(attributes, false);
     	// (x,y,w,h)
		GraphConstants.setBounds(attributes, new Rectangle2D.Double(0,0,80,20));
		GraphConstants.setBackground(attributes, Color.orange);
		GraphConstants.setOpaque(attributes, true);
        setAttributes(attributes);
    }
    
    /**
     * gets the marking of a place if it has been used in a recursive algorithm
     * @return
     */
    public boolean isSetRecursiveBounds(){
    	return this.isBoundSet;
    }
    
    /**
     * marks a cell as processed for recursive algorithms
     * @param set
     */
    public void setIsSetRecursiveBounds(boolean set){
    	this.isBoundSet = set;
    }
    
    /**
     * returns the tooltip-message for this morking
     * @return
     */
    public String getToolTipText()
    {
    	return "<html>" + Messages.getString("QuanlAna.ReachabilityGraph.Marking") + "<br>" + this.getUserObject().toString() + "</html>";
    }
}
