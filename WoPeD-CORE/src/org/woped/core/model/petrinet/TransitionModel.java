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
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
package org.woped.core.model.petrinet;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.core.model.CreationMap;

/**
 * @author Simon Landes
 * 
 * 
 * 29.03.2003
 */
public class TransitionModel extends PetriNetModelElement
{

    private Toolspecific    toolSpecific;

    private boolean         fireing   = false;
    private boolean         activated = false;

    public static final int WIDTH     = 40;
    public static final int HEIGHT    = 40;

    private int             transType = 0;

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
    
    public boolean hasResource()
    {
        return (getToolSpecific().getTransResource() !=null);
    }

    /**
     * @return
     */
    public boolean isFireing()
    {
        return fireing;
    }

    /**
     * @param b
     */
    public void setFireing(boolean b)
    {
        fireing = b;
    }

    public int getDefaultWidth()
    {
        return WIDTH;
    }

    public int getDefaultHeight()
    {
        return HEIGHT;
    }

    /**
     * @return Returns the activated.
     */
    public boolean isActivated()
    {
        return activated;
    }

    public CreationMap getCreationMap()
    {
        CreationMap map = super.getCreationMap();
        if (hasTrigger())
        {
            map.setTriggerType(getToolSpecific().getTrigger().getTriggertype());
            map.setTriggerPosition(getToolSpecific().getTrigger().getPosition());
        }
        if (hasResource())
        {
            map.setResourceOrgUnit(getToolSpecific().getTransResource().getTransOrgUnitName());
            map.setResourceRole(getToolSpecific().getTransResource().getTransRoleName());
        }

        return map;
    }

    /**
     * @param activated
     *            The activated to set.
     */
    public void setActivated(boolean activated)
    {
        this.activated = activated;
    }

    public String getToolTipText()
    {
        return "Transition\nID: " + getId() + "\nName: " + getNameValue();
    }

    public int getType()
    {
        return PetriNetModelElement.TRANS_SIMPLE_TYPE;
    }
    
    
}