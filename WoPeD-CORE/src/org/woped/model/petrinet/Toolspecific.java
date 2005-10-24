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
package org.woped.model.petrinet;

import org.woped.model.CreationMap;
import org.woped.utilities.WoPeDLogger;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 29.04.2003
 */
public class Toolspecific implements WoPeDLogger
{

    private String                  operatorId    = null;
    private int                     operatorType  = -1;
    private boolean                 subprocess    = false;
    private TriggerModel            trigger       = null;
    private TransitionResourceModel transResource = null;
    private String                  ownerId;

    /**
     * Constructor for Toolspecific. ownerid must not be null.
     */
    public Toolspecific(String ownerId)
    {
        super();
        this.ownerId = ownerId;
    }

    /**
     * Returns the operatorId.
     * 
     * @return String
     */
    public String getOperatorId()
    {
        return operatorId;
    }

    /**
     * Returns the operatorType.
     * 
     * @return int
     */
    public int getOperatorType()
    {
        return operatorType;
    }

    /**
     * Sets the operatorId.
     * 
     * @param aalstId
     *            The operatorId to set
     */
    public void setOperatorId(String operatorId)
    {
        this.operatorId = operatorId;
    }

    /**
     * Sets the operatorType.
     * 
     * @param operatorType
     *            The operatorType to set
     */
    public void setOperatorType(int operatorType)
    {
        this.operatorType = operatorType;
    }

    /**
     * TODO: Documentation
     * 
     * @param triggertype
     * @return
     */
    public TriggerModel setTrigger(CreationMap map)
    {
        this.trigger = new TriggerModel(getOwnerId(), map);
        logger.debug("Trigger added to " + getOwnerId() + ".");
        return trigger;
    }

    public TriggerModel setTrigger(TriggerModel trigger)
    {
        this.trigger = trigger;
        return trigger;
    }

    /**
     * TODO: Documentation
     *  
     */
    public void removeTrigger()
    {
        this.trigger = null;
        logger.debug("Trigger deleted.");
    }

    /**
     * Returns the ownerId.
     * 
     * @return String
     */
    public String getOwnerId()
    {
        return ownerId;
    }

    /**
     * Sets the ownerId.
     * 
     * @param ownerId
     *            The ownerId to set
     */
    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    /**
     * TODO: Documentation
     * 
     * @return
     */
    public TriggerModel getTrigger()
    {
        return trigger;
    }

    /**
     * Returns the subprocess.
     * 
     * @return boolean
     */
    public boolean isSubprocess()
    {
        return subprocess;
    }

    /**
     * Sets the subprocess.
     * 
     * @param subprocess
     *            The subprocess to set
     */
    public void setSubprocess(boolean subprocess)
    {
        this.subprocess = subprocess;
        //setOperatorType(PetriNetModelElement.SUBP_TYPE);
    }

    /**
     * @return Returns the transResource.
     */
    public TransitionResourceModel getTransResource()
    {
        return transResource;
    }

    /**
     * TODO: Documentation (waschtl)
     *  
     */
    public void removeTransResource()
    {
        this.transResource = null;
        logger.debug("ResourceModel deleted.");
    }
    /**
     * @param transResource
     *            The transResource to set.
     */
    public TransitionResourceModel setTransResource(TransitionResourceModel transResource)
    {
        this.transResource = transResource;
        return transResource;
    }

    public TransitionResourceModel setTransResource(CreationMap map)
    {
        this.transResource = new TransitionResourceModel(getOwnerId(), map);
        logger.debug("TransResource added to " + getOwnerId() + ".");
        return transResource;
    }
}