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

import java.util.Map;

import org.jgraph.graph.DefaultPort;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.IntPair;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.ModelElementFactory;
import org.woped.core.model.PetriNetModelProcessor;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * The OperatorTransitionModel is a special Class. In the internal modelstructur
 * it is an ordinary
 * @see org.woped.core.model.petrinet.TransitionModel. But it contains itselfs
 *      Models, stored in an own
 * @see org.woped.model.ModelElementContainer.
 * 
 * 29.04.2003
 */

@SuppressWarnings("serial")
public class OperatorTransitionModel extends TransitionModel implements InnerElementContainer
{

    // ... contains all Transition that are connected;
    private int                   simpleTransCounter    = 0;

    private ModelElementContainer simpleTransContainer  = null;

    private PlaceModel            centerPlace           = null;

    public static final String    INNERID_SEPERATOR_OLD = "#";

    public static final String    INNERID_SEPERATOR     = "_";
    
    public static final String 	  OPERATOR_SEPERATOR    = INNERID_SEPERATOR + "op" + INNERID_SEPERATOR; 

    public static final int       AND_SPLIT_TYPE        = 101;

    public static final int       AND_JOIN_TYPE         = 102;

    public static final int       OR_SPLIT_TYPE         = 103;

    public static final int       XOR_SPLIT_TYPE        = 104;

    public static final int       XOR_JOIN_TYPE         = 105;

    public static final int       XOR_SPLITJOIN_TYPE    = 106;
    
    public static final int		  AND_SPLITJOIN_TYPE	= 107;
    
    //! The AND-Join XOR-Split is just an alias for the XOR split operator
    //! It has a different visual representation but behaves exactly the same
    public static final int       ANDJOIN_XORSPLIT_TYPE = 108;
    //! The XOR-Join AND-Split is just an alias for the XOR join operator
    //! It has a different visual representation but behaves exactly the same
    public static final int		  XORJOIN_ANDSPLIT_TYPE = 109;

    public final static int[][][] OR_SPLIT_TABLE        = {
            { { 0 } },
            { { 1 } },
            { { 1, 0 }, { 1, 1 }, { 0, 1 } },
            { { 1, 0, 0 }, { 1, 1, 0 }, { 0, 1, 0 }, { 1, 0, 1 }, { 1, 1, 1 }, { 0, 1, 1 }, { 0, 0, 1 }

            },
            { { 1, 0, 0, 0 }, { 1, 1, 0, 0 }, { 0, 1, 0, 0 }, { 1, 0, 1, 0 }, { 1, 1, 1, 0 }, { 0, 1, 1, 0 }, { 0, 0, 1, 0 }, { 1, 0, 0, 1 }, { 1, 1, 0, 1 }, { 0, 0, 1, 1 }, { 0, 1, 0, 1 },
            { 1, 0, 1, 1 }, { 1, 1, 1, 1 }, { 0, 1, 1, 1 }, { 0, 0, 0, 1 } },
            { { 1, 0, 0, 0, 0 }, { 1, 1, 0, 0, 0 }, { 0, 1, 0, 0, 0 }, { 1, 0, 1, 0, 0 }, { 1, 1, 1, 0, 0 }, { 0, 1, 1, 0, 0 }, { 0, 0, 1, 0, 0 }, { 1, 0, 0, 1, 0 }, { 1, 1, 0, 1, 0 },
            { 0, 0, 1, 1, 0 }, { 0, 1, 0, 1, 0 }, { 1, 0, 1, 1, 0 }, { 1, 1, 1, 1, 0 }, { 0, 1, 1, 1, 0 }, { 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 1 }, { 0, 0, 0, 1, 1 }, { 0, 0, 1, 1, 1 },
            { 0, 0, 1, 0, 1 }, { 0, 1, 0, 0, 1 }, { 0, 1, 0, 1, 1 }, { 0, 1, 1, 1, 1 }, { 0, 1, 1, 0, 1 }, { 1, 0, 0, 0, 1 }, { 1, 0, 0, 1, 1 }, { 1, 0, 1, 1, 1 }, { 1, 0, 1, 0, 1 },
            { 1, 1, 0, 0, 1 }, { 1, 1, 0, 1, 1 }, { 1, 1, 1, 1, 1 }, { 1, 1, 1, 0, 1 } } };

    /**
     * Constructor for OperatorTransitionModel.
     * 
     * @param type
     * @param jGraphModel
     */
    public OperatorTransitionModel(CreationMap map, int operatorType)
    {

        // its an ordinary Model Element
        super(map);
        setOperatorType(map.getOperatorType());

        simpleTransContainer = new ModelElementContainer();
        // The simple transition container
        // is owned by the operator
        simpleTransContainer.setOwningElement(this);
        // Setting Toolspecific
        this.getToolSpecific().setOperatorId(this.getId());
        // inital ONE transition
        // TransitionModel initalSimpleTrans = createSimpleTransition();
        // getSimpleTransContainer().addElement(initalSimpleTrans);
        // this.initalSimpleTrans = initalSimpleTrans;

    }
    
    //! This method is called by the PetriNetModelProcessor to register
    //! a new incoming connection
    //! This will update the arcs of the inner model of the operator
    //! @param processor specifies the model element processor that handles the model element
    //!        container in which the connection was made
    //! @param sourceModel specifies the source model of the arc that has been created
    public void registerIncomingConnection(
    		PetriNetModelProcessor processor,
    		AbstractElementModel sourceModel)
    {
    	// get simple trans
    	Object simpleTransId = getSimpleTransContainer().getElementsByType(
    			PetriNetModelElement.TRANS_SIMPLE_TYPE)
    			.keySet().iterator().next();
    	TransitionModel simpleTrans;
    	if ((simpleTrans = (TransitionModel) getElement(simpleTransId)) == null)
    	{
    		simpleTrans = addNewSimpleTrans();
    	}
    	// dann f�ge nur die Reference hinzu
    	addReference(processor.getNexArcId(),
    			(DefaultPort) sourceModel.getChildAt(0),
    			(DefaultPort) simpleTrans.getChildAt(0));
    }
    
    //! This method is called by the PetriNetModelProcessor to register
    //! a new outgoing connection
    //! This will update the arcs of the inner model of the operator
    //! The implementation in this class is the default implementation.
    //! It will maintain one single simple transition that will mimic the connections
    //! of its owning operator. This is used for AND SPLIT-JOIN, AND JOIN, AND SPLIT operators
    //! You may want to OVERRIDE THIS METHOD if you need any complex arc management for your operator!
    //! @param processor specifies the model element processor that handles the model element
    //!        container in which the connection was made
    //! @param targetModel specifies the target model of the arc that has been created
    public void registerOutgoingConnection(
    		PetriNetModelProcessor processor,    		
    		AbstractElementModel targetModel)
    {
		// get simple trans
		Object simpleId = getSimpleTransContainer()
				.getElementsByType(
						PetriNetModelElement.TRANS_SIMPLE_TYPE)
				.keySet().iterator().next();
		TransitionModel simpleTrans;
		if ((simpleTrans = (TransitionModel) getElement(simpleId)) == null)
		{
			simpleTrans = addNewSimpleTrans();
		}
		// create an reference entry
		addReference(processor.getNexArcId(),
				(DefaultPort) simpleTrans.getChildAt(0),
				(DefaultPort) targetModel.getChildAt(0));    	    	
    }
    
    //! This method is called when an outgoing arc is deleted from the model
    //! It give the operator a chance to update its inner arcs and transitions
    //! accordingly
    //! @param processor specifies the model element processor that handles the model element
    //!        container from which the connection was removed
    //! @param otherModel specifies the model of the object the connection to which has been removed    
    public void registerOutgoingConnectionRemoval(
    		PetriNetModelProcessor processor,
    		AbstractElementModel otherModel)
    {}
    
    //! This method is called when an incoming arc is deleted from the model
    //! It give the operator a chance to update its inner arcs and transitions
    //! accordingly
    //! @param processor specifies the model element processor that handles the model element
    //!        container from which the connection was removed
    //! @param otherModel specifies the model of the object the connection to which has been removed    
    public void registerIncomingConnectionRemoval(
    		PetriNetModelProcessor processor,
    		AbstractElementModel otherModel)
    {}

    private TransitionModel createSimpleTransition()
    {

        TransitionModel aTranstitonModel;
        CreationMap map = CreationMap.createMap();
        map.setId(getNewElementId());
        map.setType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
        if (getPosition() != null)
        	map.setPosition(new IntPair(getPosition()));
        else
        	map.setPosition(new IntPair(0,0));
        aTranstitonModel = (TransitionModel) ModelElementFactory.createModelElement(map);

        return aTranstitonModel;
    }

    public String getNewElementId()
    {
        simpleTransCounter++;
        return getId() + OPERATOR_SEPERATOR + simpleTransCounter;
    }

    /**
     * Returns the operatorType.
     * 
     * @return int
     */
    public int getOperatorType()
    {
        return getToolSpecific().getOperatorType();
    }

    /**
     * Sets the operatorType.
     * 
     * @param aalsttype
     *            The operatorType to set
     */
    public void setOperatorType(int operatorType)
    {
        getToolSpecific().setOperatorType(operatorType);
    }

    public int getType()
    {
        return PetriNetModelElement.TRANS_OPERATOR_TYPE;
    }

    /**
     * Returns the simpleTransContainer.
     * 
     * @return ModelElementContainer
     */
    public ModelElementContainer getSimpleTransContainer()
    {
        return simpleTransContainer;
    }

    public CreationMap getCreationMap()
    {
        CreationMap map = super.getCreationMap();
        if (map != null)
        {
            map.setOperatorType(getOperatorType());
        }
        return map;
    }

    public TransitionModel addNewSimpleTrans()
    {
        return (TransitionModel) getSimpleTransContainer().addElement(createSimpleTransition());
    }

    /**
     * ONLY USE FOR XOR SPLITJOIN !
     * 
     * @return
     */
    private PlaceModel addNewCenterPlace()
    {
        PlaceModel centerPlace;
        CreationMap map = CreationMap.createMap();
        map.setId("CENTER_PLACE_" + getId());
        map.setType(PetriNetModelElement.PLACE_TYPE);
        map.setPosition(new IntPair(getPosition()));
        centerPlace = (PlaceModel) ModelElementFactory.createModelElement(map);
        getSimpleTransContainer().addElement(centerPlace);
        return centerPlace;
    }

    /**
     * ONLY USE FOR XOR SPLITJOIN, AND-JOIN XOR SPLIT and XOR JOIN AND-SPLIT ! 
     * 
     * 
     * @return
     */
    public PlaceModel getCenterPlace()
    {
        if (centerPlace == null)
        {
        	switch (getOperatorType())
        	{        	
        	case XOR_SPLITJOIN_TYPE:
        	case ANDJOIN_XORSPLIT_TYPE:
        	case XORJOIN_ANDSPLIT_TYPE:
                centerPlace = addNewCenterPlace();
                break;
            default:
            	// The center place is available only for certain
            	// types of operators
            	centerPlace = null;
        	}
        }
        return centerPlace;
    }

    public AbstractElementModel addElement(AbstractElementModel element)
    {
        return getSimpleTransContainer().addElement(element);
    }

    public void addReference(String arcId, DefaultPort sourceId, DefaultPort targetId)
    {
        getSimpleTransContainer().addReference(ModelElementFactory.createArcModel(arcId, sourceId, targetId));
    }

    public AbstractElementModel getElement(Object elementId)    
    {
        return (AbstractElementModel) getSimpleTransContainer().getElementById(elementId);
    }
    
    //! Check whether our only inner transition so far is unused
    //! If so, return it. Otherwise,
    //! create a new simple transition
    protected TransitionModel getCreateUnusedSimpleTrans()
    {
    	TransitionModel result = null;
    	
    	Map existingTransitions = getSimpleTransContainer().getElementsByType(
				PetriNetModelElement.TRANS_SIMPLE_TYPE);
    	if (existingTransitions.size() == 1)
    	{
    		// One single transition exists
    		// Check whether it has any connections
    		// If so, discard it
    		result = (TransitionModel)getSimpleTransContainer().getElementById(
    				existingTransitions.keySet().iterator().next());
    		Map sourceElements = getSimpleTransContainer().getSourceElements(result.getId()); 
    		Map targetElements = getSimpleTransContainer().getTargetElements(result.getId());     		
    		if (((sourceElements!=null)&&(sourceElements.size()>0))||
    			((targetElements!=null)&&(targetElements.size()>0)))
    			result = null;
    	}
    	if (result == null)
    	{
    		// No reusable transition found
    		// Create a new one
    		result = this.addNewSimpleTrans();
    	}
    	return result;
    }	    
}