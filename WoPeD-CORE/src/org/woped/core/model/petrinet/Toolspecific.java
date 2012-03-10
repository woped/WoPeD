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

import java.awt.Color;
import java.io.Serializable;

import org.woped.core.Constants;
import org.woped.core.model.CreationMap;
import org.woped.core.utilities.LoggerManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 29.04.2003
 */

@SuppressWarnings("serial")
public class Toolspecific implements Serializable
{

    private String                  operatorId    = null;

    private int                     operatorType  = -1;

    private boolean                 subprocess    = false;

    private TriggerModel            trigger       = null;

    private TransitionResourceModel transResource = null;

    private String                  ownerId;

    //! Stores the timing for this transition
    //! Currently, this is a dummy value
    //! It is stored to the PNML file as a tool-specific extension
    //! and read back but not used for anything.
    //! It will, however, be used in the future for quantitative analysis of 
    //! workflow nets. Time of this writing: 2006/12/18, A.Eckleder 
    private int						time = 0;
    //! Specifies the unit for the time value (enum). The exact values are 
    //! not defined yet
    private int						timeUnit = 1;
    
    private boolean				    UnderstandColoringActive = false;

    private Color				    UnderstandColor          = Color.white;
    
    public static enum OperatorPosition {NORTH, EAST, SOUTH, WEST}
    public static enum OperatorDirection { IN, OUT }
    

    private OperatorPosition position = OperatorPosition.EAST; 
    private OperatorDirection direction = OperatorDirection.IN;


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
        this.trigger = new TriggerModel(map);
        LoggerManager.debug(Constants.CORE_LOGGER, "Trigger added to " + getOwnerId() + ".");
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
        LoggerManager.debug(Constants.CORE_LOGGER, "Trigger deleted.");
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
        // setOperatorType(PetriNetModelElement.SUBP_TYPE);
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
        LoggerManager.debug(Constants.CORE_LOGGER, "ResourceModel deleted.");
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
        this.transResource = new TransitionResourceModel(map);
        LoggerManager.debug(Constants.CORE_LOGGER, "TransResource added to " + getOwnerId() + ".");
        return transResource;
    }

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(int timeUnit) {
		this.timeUnit = timeUnit;
	}
	
    public void setUnderstandColoringActive(boolean active) {
		this.UnderstandColoringActive= active;
	}	
	
	public boolean getUnderstandColoringActive(){
		return this.UnderstandColoringActive;
	}
    
    public void setUnderstandColor(Color UnderstandColor) {
		this.UnderstandColor  = UnderstandColor;
	}	
	
	public Color getUnderstandColor(){
		return this.UnderstandColor;
	}
	
	public OperatorPosition getOperatorPosition(){
		return this.position;
	}
	
	public void setOperatorPosition (OperatorPosition position){
		this.position = position;
	}
   public OperatorDirection getOperatorDirection() {
    	return this.direction;
    }
    
    public void setOperatorDirection(OperatorDirection direction) {
    	this.direction = direction;
    }
    
    public OperatorPosition getOperatorOppositePosition() {
     	return OperatorPosition.values()[(position.ordinal()+2)%OperatorPosition.values().length];
    }
    
    public OperatorDirection getOperatorOppositeDirection() {
     	return OperatorDirection.values()[(direction.ordinal()+1)%OperatorDirection.values().length];
    }
    



}