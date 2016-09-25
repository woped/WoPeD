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

import org.jgraph.graph.DefaultPort;
import org.woped.core.model.*;
import org.woped.core.model.petrinet.Toolspecific.OperatorDirection;
import org.woped.core.model.petrinet.Toolspecific.OperatorPosition;

import java.awt.*;
import java.util.Map;


/**
 * An OperatorTransitionModel is the abstract base class for complex transition types.
 * <p>
 * Complex transition are common structures of classic places and transitions such as Xor-Split or And-Join.
 * They can be used as abbreviations to increase the understandability of larger petri nets.
 * <p>
 * The OperatorTransitionModel acts as a {@link TransitionModel} but it contains its own {@link ModelElementContainer},
 * where it stores the required sub net.
 * <p>
 * Note that the AND-Join/XOR-Split and the XOR-Split operator are semantically equivalent but they have a different
 * visual representation. The same is true for the XOR-Join/AND-Split and the AND-Split operator.
 *
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a>
 * @since 29.04.2003
 */

@SuppressWarnings("serial")
public abstract class OperatorTransitionModel extends TransitionModel implements InnerElementContainer {

    public static final String INNERID_SEPERATOR_OLD = "#";
    public static final String INNERID_SEPERATOR = "_";
    public static final String OPERATOR_SEPERATOR_TRANSITION = INNERID_SEPERATOR + "op" + INNERID_SEPERATOR;
    public static final String OPERATOR_SEPERATOR_PLACE = "P" + INNERID_SEPERATOR + "CENTER" + INNERID_SEPERATOR;
    public static final int AND_SPLIT_TYPE = 101;
    public static final int AND_JOIN_TYPE = 102;
    public static final int OR_SPLIT_TYPE = 103;
    public static final int XOR_SPLIT_TYPE = 104;
    public static final int XOR_JOIN_TYPE = 105;
    public static final int XORJOIN_XORSPLIT_TYPE = 106;
    public static final int AND_SPLITJOIN_TYPE = 107;
    //! The AND-Join XOR-Split is just an alias for the XOR split operator
    //! It has a different visual representation but behaves exactly the same
    public static final int ANDJOIN_XORSPLIT_TYPE = 108;
    //! The XOR-Join AND-Split is just an alias for the XOR join operator
    //! It has a different visual representation but behaves exactly the same
    public static final int XORJOIN_ANDSPLIT_TYPE = 109;
    //    public final static int[][][] OR_SPLIT_TABLE = {{{0}}, {{1}}, {{1, 0}, {1, 1}, {0, 1}}, {{1, 0, 0}, {1, 1, 0}, {0, 1, 0}, {1, 0, 1}, {1, 1, 1}, {0, 1, 1}, {0, 0, 1}}, {{1, 0, 0, 0}, {1, 1, 0, 0}, {0, 1, 0, 0}, {1, 0, 1, 0}, {1, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 1, 0}, {1, 0, 0, 1}, {1, 1, 0, 1}, {0, 0, 1, 1}, {0, 1, 0, 1}, {1, 0, 1, 1}, {1, 1, 1, 1}, {0, 1, 1, 1}, {0, 0, 0, 1}}, {{1, 0, 0, 0, 0}, {1, 1, 0, 0, 0}, {0, 1, 0, 0, 0}, {1, 0, 1, 0, 0}, {1, 1, 1, 0, 0}, {0, 1, 1, 0, 0}, {0, 0, 1, 0, 0}, {1, 0, 0, 1, 0}, {1, 1, 0, 1, 0}, {0, 0, 1, 1, 0}, {0, 1, 0, 1, 0}, {1, 0, 1, 1, 0}, {1, 1, 1, 1, 0}, {0, 1, 1, 1, 0}, {0, 0, 0, 1, 0}, {0, 0, 0, 0, 1}, {0, 0, 0, 1, 1}, {0, 0, 1, 1, 1}, {0, 0, 1, 0, 1}, {0, 1, 0, 0, 1}, {0, 1, 0, 1, 1}, {0, 1, 1, 1, 1}, {0, 1, 1, 0, 1}, {1, 0, 0, 0, 1}, {1, 0, 0, 1, 1}, {1, 0, 1, 1, 1}, {1, 0, 1, 0, 1}, {1, 1, 0, 0, 1}, {1, 1, 0, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 0, 1}}};
    // ... contains all Transition that are connected;
    private int simpleTransCounter = 0;
    private ModelElementContainer simpleTransContainer = null;
    private PlaceModel centerPlace = null;

    /**
     * Creates a new instance of an OperatorTransitionModel.
     *
     * @param map the property object for the new element.
     */
    protected OperatorTransitionModel(CreationMap map) {

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

        OperatorPosition east_south;
        OperatorPosition west_north;

        //checks if the current modelling direction is vertical or horizontal
        //and sets the corresponding direction
        //verticalView = EditorLayoutInfo.m_verticalLayout;

        EditorLayoutInfo m_EditorLayoutInfo = new EditorLayoutInfo();

        if (m_EditorLayoutInfo.getVerticalLayout()) {
            east_south = OperatorPosition.SOUTH;
            west_north = OperatorPosition.NORTH;
        } else {
            east_south = OperatorPosition.EAST;
            west_north = OperatorPosition.WEST;
        }

        //XX
        switch (map.getOperatorType()) {
            case AND_SPLIT_TYPE:
                getToolSpecific().setOperatorDirection(OperatorDirection.IN);
                if (map.getOperatorPosition() == -1) {
                    getToolSpecific().setOperatorPosition(east_south);
                } else {
                    getToolSpecific().setOperatorPosition(OperatorPosition.values()[map.getOperatorPosition()]);
                }
                break;
            case AND_JOIN_TYPE:
                getToolSpecific().setOperatorDirection(OperatorDirection.IN);
                if (map.getOperatorPosition() == -1) {
                    getToolSpecific().setOperatorPosition(west_north);
                } else {
                    getToolSpecific().setOperatorPosition(OperatorPosition.values()[map.getOperatorPosition()]);
                }
                break;
            case OR_SPLIT_TYPE:
            case XOR_SPLIT_TYPE:
                getToolSpecific().setOperatorDirection(OperatorDirection.OUT);
                if (map.getOperatorPosition() == -1) {
                    getToolSpecific().setOperatorPosition(east_south);
                } else {
                    getToolSpecific().setOperatorPosition(OperatorPosition.values()[map.getOperatorPosition()]);
                }
                break;
            case XOR_JOIN_TYPE:
                getToolSpecific().setOperatorDirection(OperatorDirection.OUT);
                if (map.getOperatorPosition() == -1) {
                    getToolSpecific().setOperatorPosition(west_north);
                } else {
                    getToolSpecific().setOperatorPosition(OperatorPosition.values()[map.getOperatorPosition()]);
                }
                break;
            case XORJOIN_XORSPLIT_TYPE:
                getToolSpecific().setOperatorDirection(OperatorDirection.OUT);
                if (map.getOperatorPosition() == -1) {
                    getToolSpecific().setOperatorPosition(west_north);
                } else {
                    getToolSpecific().setOperatorPosition(OperatorPosition.values()[map.getOperatorPosition()]);
                }
                break;
            case AND_SPLITJOIN_TYPE:
                getToolSpecific().setOperatorDirection(OperatorDirection.IN);
                if (map.getOperatorPosition() == -1) {
                    getToolSpecific().setOperatorPosition(west_north);
                } else {
                    getToolSpecific().setOperatorPosition(OperatorPosition.values()[map.getOperatorPosition()]);
                }
                break;
            case ANDJOIN_XORSPLIT_TYPE:
                getToolSpecific().setOperatorDirection(OperatorDirection.IN);
                if (map.getOperatorPosition() == -1) {
                    getToolSpecific().setOperatorPosition(west_north);
                } else {
                    getToolSpecific().setOperatorPosition(OperatorPosition.values()[map.getOperatorPosition()]);
                }
                break;
            case XORJOIN_ANDSPLIT_TYPE:
                getToolSpecific().setOperatorDirection(OperatorDirection.OUT);
                if (map.getOperatorPosition() == -1) {
                    getToolSpecific().setOperatorPosition(west_north);
                } else {
                    getToolSpecific().setOperatorPosition(OperatorPosition.values()[map.getOperatorPosition()]);
                }
                break;
        }

    }

    //! This method is called by the PetriNetModelProcessor to register
    //! a new incoming connection
    //! This will update the arcs of the inner model of the operator
    //! @param processor specifies the model element processor that handles the model element
    //!        container in which the connection was made
    //! @param sourceModel specifies the source model of the arc that has been created
    public void registerIncomingConnection(PetriNetModelProcessor processor, AbstractPetriNetElementModel sourceModel) {
        // get simple trans
        Object simpleTransId = getSimpleTransContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE).keySet().iterator().next();
        TransitionModel simpleTrans;
        if ((simpleTrans = (TransitionModel) getElement(simpleTransId)) == null) {
            simpleTrans = addNewSimpleTrans();
        }
        // dann f�ge nur die Reference hinzu
        addReference(getNextFreeArcID(processor), (DefaultPort) sourceModel.getChildAt(0), (DefaultPort) simpleTrans.getChildAt(0));
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
    public void registerOutgoingConnection(PetriNetModelProcessor processor, AbstractPetriNetElementModel targetModel) {
        // get simple trans
        Object simpleId = getSimpleTransContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE).keySet().iterator().next();
        TransitionModel simpleTrans;
        if ((simpleTrans = (TransitionModel) getElement(simpleId)) == null) {
            simpleTrans = addNewSimpleTrans();
        }
        // create an reference entry
        addReference(getNextFreeArcID(processor), (DefaultPort) simpleTrans.getChildAt(0), (DefaultPort) targetModel.getChildAt(0));
    }

    //! This method is called when an outgoing arc is deleted from the model
    //! It give the operator a chance to update its inner arcs and transitions
    //! accordingly
    //! @param processor specifies the model element processor that handles the model element
    //!        container from which the connection was removed
    //! @param otherModel specifies the model of the object the connection to which has been removed    
    public void registerOutgoingConnectionRemoval(PetriNetModelProcessor processor, AbstractPetriNetElementModel otherModel) {
    }

    //! This method is called when an incoming arc is deleted from the model
    //! It give the operator a chance to update its inner arcs and transitions
    //! accordingly
    //! @param processor specifies the model element processor that handles the model element
    //!        container from which the connection was removed
    //! @param otherModel specifies the model of the object the connection to which has been removed    
    public void registerIncomingConnectionRemoval(PetriNetModelProcessor processor, AbstractPetriNetElementModel otherModel) {
    }

    private TransitionModel createSimpleTransition() {

        TransitionModel transition;
        CreationMap map = CreationMap.createMap();
        map.setId(getNewElementId());
        if (getNameValue() != null) map.setName(getNameValue());
        else map.setName(getId());
        map.setType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        if (getPosition() != null) map.setPosition(getPosition());
        else map.setPosition(new Point(0, 0));
        transition = (TransitionModel) ModelElementFactory.createModelElement(map);

        return transition;
    }

    public String getNewElementId() {
        simpleTransCounter++;
        return getId() + OPERATOR_SEPERATOR_TRANSITION + simpleTransCounter;
    }

    /**
     * Returns the operatorType.
     *
     * @return int
     */
    public int getOperatorType() {
        return getToolSpecific().getOperatorType();
    }

    /**
     * Sets the operatorType.
     *
     * @param operatorType The operatorType to set
     */
    private void setOperatorType(int operatorType) {
        getToolSpecific().setOperatorType(operatorType);
    }

    public int getType() {
        return AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE;
    }

    /**
     * Returns the simpleTransContainer.
     *
     * @return ModelElementContainer
     */
    public ModelElementContainer getSimpleTransContainer() {
        return simpleTransContainer;
    }

    public CreationMap getCreationMap() {
        CreationMap map = super.getCreationMap();
        if (map != null) {
            map.setOperatorType(getOperatorType());
        }
        return map;
    }

    public TransitionModel addNewSimpleTrans() {
        return (TransitionModel) getSimpleTransContainer().addElement(createSimpleTransition());
    }

    /**
     * ONLY USE FOR XOR SPLITJOIN !
     *
     * @return
     */
    private PlaceModel addNewCenterPlace() {
        PlaceModel centerPlace;
        CreationMap map = CreationMap.createMap();
        map.setId(OPERATOR_SEPERATOR_PLACE + getId());
        map.setName(getNameValue());
        map.setType(AbstractPetriNetElementModel.PLACE_TYPE);
        map.setPosition(getPosition());
        centerPlace = (PlaceModel) ModelElementFactory.createModelElement(map);
        getSimpleTransContainer().addElement(centerPlace);
        return centerPlace;
    }

    /**
     * ONLY USE FOR XOR SPLITJOIN, AND-JOIN XOR SPLIT and XOR JOIN AND-SPLIT !
     *
     * @return
     */
    public PlaceModel getCenterPlace() {
        if (centerPlace == null) {
            switch (getOperatorType()) {
                case XORJOIN_XORSPLIT_TYPE:
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

    public AbstractPetriNetElementModel addElement(AbstractPetriNetElementModel element) {
        return getSimpleTransContainer().addElement(element);
    }

    public void addReference(String arcId, DefaultPort sourceId, DefaultPort targetId) {
        getSimpleTransContainer().addReference(ModelElementFactory.createArcModel(arcId, sourceId, targetId));
    }

    public AbstractPetriNetElementModel getElement(Object elementId) {
        return getSimpleTransContainer().getElementById(elementId);
    }

    //! Check whether our only inner transition so far is unused
    //! If so, return it. Otherwise,
    //! create a new simple transition
    TransitionModel getCreateUnusedSimpleTrans() {
        TransitionModel result = null;

        Map<String, AbstractPetriNetElementModel> existingTransitions = getSimpleTransContainer().getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
        if (existingTransitions.size() == 1) {
            // One single transition exists
            // Check whether it has any connections
            // If so, discard it
            result = (TransitionModel) getSimpleTransContainer().getElementById(existingTransitions.keySet().iterator().next());
            Map<String, AbstractPetriNetElementModel> sourceElements = getSimpleTransContainer().getSourceElements(result.getId());
            Map<String, AbstractPetriNetElementModel> targetElements = getSimpleTransContainer().getTargetElements(result.getId());
            if (((sourceElements != null) && (sourceElements.size() > 0)) || ((targetElements != null) && (targetElements.size() > 0)))
                result = null;
        }
        if (result == null) {
            // No reusable transition found
            // Create a new one
            result = this.addNewSimpleTrans();
        }
        return result;
    }

    //! Create a unique arc id for the inner container
    //! Note that you may *never ever* use the next free arc returned by the outer
    //! model processor directly because that arc may already exist inside the
    //! inner transition container and would be overwritten which results in a broken net
    String getNextFreeArcID(PetriNetModelProcessor processor) {
        // Connect the new source object to our IN transition
        String nextArcId;
        ArcModel exists;
        do {
            nextArcId = processor.getNexArcId();
            exists = getSimpleTransContainer().getArcById(nextArcId);
        } while (exists != null);
        return nextArcId;
    }

    @Override
    public void setNameValue(String name) {
        getNameModel().setUserObject(name);
        if (simpleTransCounter > 0) {
            for (AbstractPetriNetElementModel aem : simpleTransContainer.getRootElements()) {
                if (aem.getRootOwningContainer().getOwningElement() == this) aem.setNameValue(name);
            }
        }
    }
}