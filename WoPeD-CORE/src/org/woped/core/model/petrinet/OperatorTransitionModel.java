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

import org.jgraph.graph.DefaultPort;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.IntPair;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.ModelElementFactory;

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
public class OperatorTransitionModel extends TransitionModel
{

    // ... contains all Transition that are connected;
    private int                   simpleTransCounter    = 0;

    private ModelElementContainer simpleTransContainer  = null;

    private PlaceModel            centerPlace           = null;

    public static final String    INNERID_SEPERATOR_OLD = "#";

    public static final String    INNERID_SEPERATOR     = "_";

    public static final int       AND_SPLIT_TYPE        = 101;

    public static final int       AND_JOIN_TYPE         = 102;

    public static final int       OR_SPLIT_TYPE         = 103;

    public static final int       XOR_SPLIT_TYPE        = 104;

    public static final int       XOR_JOIN_TYPE         = 105;

    public static final int       XOR_SPLITJOIN_TYPE    = 106;
    
    public static final int		  AND_SPLITJOIN_TYPE	= 107;

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
    public OperatorTransitionModel(CreationMap map)
    {

        // its an ordinary Model Element
        super(map);
        if (map.getOperatorType() != -1)
        {
            setOperatorType(map.getOperatorType());
        }
        simpleTransContainer = new ModelElementContainer();
        // Setting Toolspecific
        this.getToolSpecific().setOperatorId(this.getId());
        // inital ONE transition
        // TransitionModel initalSimpleTrans = createSimpleTransition();
        // getSimpleTransContainer().addElement(initalSimpleTrans);
        // this.initalSimpleTrans = initalSimpleTrans;

    }

    private TransitionModel createSimpleTransition()
    {

        TransitionModel aTranstitonModel;
        CreationMap map = CreationMap.createMap();
        map.setId(getnewSimpleTransId());
        map.setType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
        if (getPosition() != null)
        	map.setPosition(new IntPair(getPosition()));
        aTranstitonModel = (TransitionModel) ModelElementFactory.createModelElement(map);

        return aTranstitonModel;
    }

    public String getnewSimpleTransId()
    {
        simpleTransCounter++;
        return getId() + INNERID_SEPERATOR + "op" + INNERID_SEPERATOR + simpleTransCounter;
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
     * ONLY USE FOR XOR SPLITJOIN ! Does only work for splifoin anyway...
     * 
     * @return
     */
    public PlaceModel getCenterPlace()
    {
        if (centerPlace == null && getOperatorType() == XOR_SPLITJOIN_TYPE)
        {
            centerPlace = addNewCenterPlace();
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

    public TransitionModel getSimpleTrans(Object simpleTransId)
    {
        return (TransitionModel) getSimpleTransContainer().getElementById(simpleTransId);
    }
}