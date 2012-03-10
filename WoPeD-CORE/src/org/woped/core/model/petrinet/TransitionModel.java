/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.core.model.petrinet;

import java.awt.Point;
import java.util.Iterator;
import java.util.Map;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.core.Constants;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.petrinet.Toolspecific.OperatorPosition;
import org.woped.core.utilities.LoggerManager;

/**
 * @author Simon Landes
 * 
 * 
 * 29.03.2003
 */

@SuppressWarnings("serial")
public class TransitionModel extends AbstractPetriNetElementModel
{

    private Toolspecific    toolSpecific;
    public static final int WIDTH     = 40;
    public static final int HEIGHT    = 40;
    // it is important only in Subprocess
    private boolean incommingTarget ;
    private boolean outgoingSource;
    
    public TransitionModel(CreationMap map)
    {
        super(map);
        toolSpecific = new Toolspecific(getId());
        AttributeMap attributes = getAttributes();
        GraphConstants.setMoveable(attributes, true);
        GraphConstants.setEditable(attributes, false);
        GraphConstants.setSizeable(attributes, false);
        setAttributes(attributes);
    }

    /**
     * Returns the toolSpecific.
     * 
     * @return Toolspecific
     */
    public Toolspecific getToolSpecific()
    {
        return toolSpecific;
    }

    /**
     * Sets the toolSpecific.
     * 
     * @param toolSpecific
     *            The toolSpecific to set
     */
    public void setToolSpecific(Toolspecific toolSpecific)
    {
        this.toolSpecific = toolSpecific;
    }

    public boolean hasTrigger()
    {
        return (getToolSpecific().getTrigger() != null);
    }

    public int getTriggerType()
    {
        return (getToolSpecific().getTrigger().getTriggertype());
    }

    public boolean hasResource()
    {
        return (getToolSpecific().getTransResource() != null);
    }
    
    public Point getTriggerPosition()
    {
    	if (hasTrigger())
    		return getToolSpecific().getTrigger().getPosition();
    	else
    		if(toolSpecific.getOperatorPosition() == OperatorPosition.NORTH || toolSpecific.getOperatorPosition() == OperatorPosition.SOUTH)
    		{
    			return new Point(getX() - 25, getY() + 10);
    		}
    		else
    		{
    			return new Point(getX() + 10, getY() - 20);
    		}
    }

    public Point getResourcePosition()
    {
    	if (hasResource())
    		return getToolSpecific().getTransResource().getPosition();
    	else
    		if(toolSpecific.getOperatorPosition() == OperatorPosition.NORTH || toolSpecific.getOperatorPosition() == OperatorPosition.SOUTH)
    		{
    			return new Point(getX() - 65, getY() - TransitionResourceModel.DEFAULT_HEIGHT + 5);
    		}
    		else
    		{
    			return new Point(getX() + 10, getY() - TransitionResourceModel.DEFAULT_HEIGHT - 25);
    		}
    }

    public int getDefaultWidth()
    {
        return WIDTH;
    }

    public int getDefaultHeight()
    {
        return HEIGHT;
    }
    
    public CreationMap getCreationMap()
    {
        CreationMap map = super.getCreationMap();
        if (hasTrigger())
        {
            map.setTriggerType(getToolSpecific().getTrigger().getTriggertype());
            map.setTriggerPosition(new Point(getToolSpecific().getTrigger().getPosition()));
        }
        else
        	// If no trigger exists, our creation map must reflect this
        	// (there might have been a trigger before which was removed
        	// during editing)
        	map.setTriggerType(-1);
        if (hasResource())
        {
            map.setResourceOrgUnit(getToolSpecific().getTransResource().getTransOrgUnitName());
            map.setResourceRole(getToolSpecific().getTransResource().getTransRoleName());
            map.setResourcePosition(new Point(getToolSpecific().getTransResource().getPosition()));
        }
        else
        {
        	// If no resource org unit exists, our creation map must reflect this
        	// (there might have been a resource unit before which was removed
        	// during editing)
        	map.setResourceOrgUnit(null);
        	map.setResourceRole(null);
        	map.setResourcePosition(null);
        }
        // Extract transition service time and transition service
        // time unit
        map.setTransitionTime(getToolSpecific().getTime());
        map.setTransitionTimeUnit(getToolSpecific().getTimeUnit());
       
        return map;
    }

    public String getToolTipText()
    {
        return "Transition\nID: " + getId() + "\nName: " + getNameValue();
    }

    public int getType()
    {
        return AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE;
    }

	public void setIncommingTarget(boolean incommingTarget) {
		this.incommingTarget = incommingTarget;
	}

	public boolean isIncommingTarget() {
		return incommingTarget;
	}

	public void setOutgoingSource(boolean outgoingSource) {
		this.outgoingSource = outgoingSource;
	}

	public boolean isOutgoingSource() {
		return outgoingSource;
	}
	
    /**
     * Counts the token-filled Places which are the source of the Map filled Arcs.
     * @return Number of places that contain at least one token 
     */
    public int getNumIncomingActivePlaces() {
        Map<String, ArcModel> arcsFromPlaces = getRootOwningContainer().getIncomingArcs(getId());
    	
        Iterator<String> incomingArcsIter = arcsFromPlaces.keySet().iterator();
        int activePlaces = 0;
        while (incomingArcsIter.hasNext()) {
            ArcModel arc = getRootOwningContainer().getArcById(incomingArcsIter.next());
            try {
                PlaceModel place = (PlaceModel) getRootOwningContainer().getElementById(arc.getSourceId());
                if (place != null && place.getVirtualTokenCount() > 0) {
                    // TODO: when ARC WEIGTH implemented check tokens >= weigth
                    activePlaces++;
                }
            } catch (ClassCastException cce) {
                LoggerManager.warn(Constants.CORE_LOGGER, "TokenGame: Source not a Place. Ignore arc: "
                        + arc.getId());
            }
        }
        return activePlaces;
    }	
    
    /**
     * Calculate and return the number of incoming arcs for this transition
     * @return Number of incoming arcs
     */
    public int getNumInputPlaces() {
    	Map<String, ArcModel> arcsFromPlaces = getRootOwningContainer().getIncomingArcs(getId());
    	return arcsFromPlaces.size();
    }
		
    public boolean isActivated()    
    {
    	return (getNumInputPlaces()==getNumIncomingActivePlaces());
    }    	
}