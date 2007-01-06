package org.woped.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.woped.core.Constants;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.uml.AbstractUMLElementModel;
import org.woped.core.model.uml.OperatorModel;
import org.woped.core.model.uml.StateModel;
import org.woped.core.utilities.LoggerManager;

public class ModelTranslator
{
    public static UMLModelProcessor translate(PetriNetModelProcessor petrinet)
    {
        long begin = System.currentTimeMillis();
        LoggerManager.debug(Constants.CORE_LOGGER, "### Translate Petrinet Model");

        UMLModelProcessor uml = new UMLModelProcessor();
        uml.setId(petrinet.getId());
        uml.setName(petrinet.getName());
        CreationMap tempCreateMap;
        AbstractPetriNetModelElement tempElement;
        OperatorTransitionModel tempOperator;
        HashMap idMapper = new HashMap();
        List startStopList = new ArrayList();
        Map tempSourceElements;
        Map tempTargetElements;
        AbstractElementModel tempSource;
        AbstractElementModel tempTarget;
        // ##### 1. Find XOR Split-Joins and Find START and STOP ##### //
        for (Iterator placeIter = petrinet.getElementContainer().getElementsByType(AbstractPetriNetModelElement.PLACE_TYPE).values().iterator(); placeIter.hasNext();)
        {
            PlaceModel place = (PlaceModel) placeIter.next();
            tempSourceElements = petrinet.getElementContainer().getSourceElements(place.getId());
            tempTargetElements = petrinet.getElementContainer().getTargetElements(place.getId());

            if ("START".equals(place.getNameValue()))
            {
                tempCreateMap = place.getCreationMap();
                tempCreateMap.setType(AbstractUMLElementModel.STATE_TYPE);
                tempCreateMap.setStateType(StateModel.STATE_START_TYPE);
                startStopList.add(tempCreateMap.getId());
                uml.createElement(tempCreateMap, "").setElementContext(place.getElementContext());
            } else if ("STOP".equals(place.getNameValue()))
            {
                tempCreateMap = place.getCreationMap();
                tempCreateMap.setType(AbstractUMLElementModel.STATE_TYPE);
                tempCreateMap.setStateType(StateModel.STATE_STOP_TYPE);
                startStopList.add(tempCreateMap.getId());
                uml.createElement(tempCreateMap, "").setElementContext(place.getElementContext());
            }

            // if (tempSourceElements.size() == 0)
            // {
            // tempCreateMap = place.getCreationMap();
            // tempCreateMap.setType(AbstractUMLElementModel.STATE_TYPE);
            // tempCreateMap.setStateType(StateModel.STATE_START_TYPE);
            // startStopList.add(tempCreateMap.getId());
            // uml.createElement(tempCreateMap).setElementContext(place.getElementContext());
            // } else if (tempTargetElements.size() == 0)
            // {
            // tempCreateMap = place.getCreationMap();
            // tempCreateMap.setType(AbstractUMLElementModel.STATE_TYPE);
            // tempCreateMap.setStateType(StateModel.STATE_STOP_TYPE);
            // startStopList.add(tempCreateMap.getId());
            // uml.createElement(tempCreateMap).setElementContext(place.getElementContext());
            // }
            else
            {
                // Check if split-join
                for (Iterator sourceIter = tempSourceElements.values().iterator(); sourceIter.hasNext();)
                {
                    tempSource = (AbstractElementModel) sourceIter.next();
                    if (tempSource.getType() == OperatorTransitionModel.XOR_JOIN_TYPE)
                    {
                        for (Iterator targetIter = tempTargetElements.values().iterator(); targetIter.hasNext();)
                        {
                            tempTarget = (AbstractElementModel) sourceIter.next();
                            if ((tempTarget.getType() == OperatorTransitionModel.XOR_SPLIT_TYPE)||
                            	(tempTarget.getType() == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE))
                            {
                                // referenc on each other
                                Object newId = uml.getNewElementId(tempTarget.getType());
                                idMapper.put(tempSource.getId(), newId);
                                idMapper.put(tempTarget.getId(), newId);
                            }
                        }
                    }
                }
            }

        }
        // ##### 2. Take over Transitions and And Operators ##### //
        for (Iterator iter = petrinet.getElementContainer().getRootElements().iterator(); iter.hasNext();)
        {
            tempElement = (AbstractPetriNetModelElement) iter.next();
            if (tempElement.getType() == AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE)
            {
                tempCreateMap = tempElement.getCreationMap();
                if (tempElement.getElementContext().isXORType())
                {
                    tempCreateMap.setType(AbstractUMLElementModel.OPERATOR_TYPE);
                    tempCreateMap.setOperatorType(OperatorModel.XOR_TYPE);
                } else if (tempElement.getElementContext().isANDType())
                {
                    tempCreateMap.setType(AbstractUMLElementModel.OPERATOR_TYPE);
                    tempCreateMap.setOperatorType(OperatorModel.AND_TYPE);
                } else
                {
                    tempCreateMap.setType(AbstractUMLElementModel.ACTIVITY_TYPE);
                }
                uml.createElement(tempCreateMap, "").setElementContext(tempElement.getElementContext());
            } else if (tempElement.getType() == AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE)
            {
                tempOperator = (OperatorTransitionModel) tempElement;
                tempCreateMap = tempElement.getCreationMap();
                tempCreateMap.setType(AbstractUMLElementModel.OPERATOR_TYPE);
                if (tempOperator.getOperatorType() == OperatorTransitionModel.AND_JOIN_TYPE || tempOperator.getOperatorType() == OperatorTransitionModel.AND_SPLIT_TYPE
                		|| tempOperator.getOperatorType() == OperatorTransitionModel.AND_SPLITJOIN_TYPE)
                {
                    tempCreateMap.setOperatorType(OperatorModel.AND_TYPE);
                    uml.createElement(tempCreateMap, "").setElementContext(tempElement.getElementContext());
                    // and.getElementContext().setANDType(true);
                } else if (tempOperator.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE || 
                		tempOperator.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE ||
                		tempOperator.getOperatorType() == OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE)
                {
                    tempCreateMap.setOperatorType(OperatorModel.XOR_TYPE);
                    // Change ID if Split-Join
                    if (idMapper.containsKey(tempOperator.getId()))
                    {
                        tempCreateMap.setId((String) idMapper.get(tempOperator.getId()));
                    }
                    uml.createElement(tempCreateMap, "").setElementContext(tempElement.getElementContext());
                }
            }
        }
        // ##### 3. Connect the elements with the info from the places ##### //
        ArcModel tempArc;
        for (Iterator arcIter = petrinet.getElementContainer().getArcMap().values().iterator(); arcIter.hasNext();)
        {
            tempArc = (ArcModel) arcIter.next();
            // if (startStopList.contains(tempArc.getTargetId()) ||
            // startStopList.contains(tempArc.getSourceId()))
            // {
            // uml.createArc(tempArc.getSourceId(), tempArc.getTargetId());
            // } else

            tempTarget = petrinet.getElementContainer().getElementById(tempArc.getTargetId());
            tempSource = petrinet.getElementContainer().getElementById(tempArc.getSourceId());
            if (uml.getElementContainer().containsElement(tempTarget.getId()))
            {
                if (uml.getElementContainer().containsElement(tempSource.getId()))
                {
                    uml.createArc(tempSource.getId(), tempTarget.getId());
                } else
                {
                    for (Iterator sourceIter = petrinet.getElementContainer().getSourceElements(tempSource.getId()).values().iterator(); sourceIter.hasNext();)
                    {
                        tempSource = (AbstractPetriNetModelElement) sourceIter.next();
                        if (uml.getElementContainer().containsElement(tempSource.getId()))
                        {
                            uml.createArc(tempSource.getId(), tempTarget.getId());
                        }
                    }

                }
            }

        }

        LoggerManager.debug(Constants.CORE_LOGGER, "### Translation done (" + (System.currentTimeMillis() - begin) + " ms)");

        return uml;
    }

    public static PetriNetModelProcessor translate(UMLModelProcessor uml)
    {
        long begin = System.currentTimeMillis();
        LoggerManager.debug(Constants.CORE_LOGGER, "### Translate UML Model");

        PetriNetModelProcessor petrinet = new PetriNetModelProcessor();
        petrinet.setId(uml.getId());
        petrinet.setName(uml.getName());
        /*
         * idmapper contains the old id of an xor split/join with a list as
         * object containing the nwe element ids! ATTENTION first entry is the
         * join (incoming arcs) second entry is the split (outgoing arcs)
         */
        HashMap idMapper = new HashMap();
        CreationMap tempMap;
        AbstractElementModel tempElement;
        AbstractElementModel tempPetriElement;
        AbstractElementModel tempPlaceElement;
        // ##### 1. Take over elements ##### //
        for (Iterator iter = uml.getElementContainer().getRootElements().iterator(); iter.hasNext();)
        {
            tempElement = (AbstractUMLElementModel) iter.next();
            tempMap = tempElement.getCreationMap();
            if (tempElement.getType() == AbstractUMLElementModel.ACTIVITY_TYPE)
            {
                tempMap.setType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
                petrinet.createElement(tempMap, "").setElementContext(tempElement.getElementContext());
            } else if (tempElement.getType() == AbstractUMLElementModel.OPERATOR_TYPE)
            {
                Map outgoingArcs = uml.getElementContainer().getOutgoingArcs(tempElement.getId());
                Map incomingArcs = uml.getElementContainer().getIncomingArcs(tempElement.getId());
                // IF XOR Element... count incoming and outgoing
                if (((OperatorModel) tempElement).getOperatorType() == OperatorModel.XOR_TYPE)
                {
                    tempMap.setType(PetriNetModelElement.TRANS_OPERATOR_TYPE);
                    // okay: check if more than 1 incoming AND outgoing !!
                    if (outgoingArcs.size() > 1 && incomingArcs.size() > 1)
                    {
                        LoggerManager.debug(Constants.CORE_LOGGER, "XOR Split-Join found!");
                        // Create a XOR-Join and a XOR-Split
                        CreationMap tempMap2 = (CreationMap) tempMap.clone();
                        tempMap.setId(tempMap.getId() + "_1");
                        tempMap.setOperatorType(OperatorTransitionModel.XOR_JOIN_TYPE);
                        tempMap2.setId(tempMap2.getId() + "_2");
                        tempMap2.setOperatorType(OperatorTransitionModel.XOR_SPLIT_TYPE);
                        // Create a place in between ...
                        CreationMap placeMap = CreationMap.createMap();
                        placeMap.setType(PetriNetModelElement.PLACE_TYPE);
                        placeMap.setPosition(tempMap.getPosition());
                        // .. and connect them
                        petrinet.createElement(tempMap, "").setElementContext(tempElement.getElementContext());
                        petrinet.createElement(tempMap2, "").setElementContext(tempElement.getElementContext());
                        tempPlaceElement = petrinet.createElement(placeMap, "");
                        petrinet.createArc(tempMap.getId(), tempPlaceElement.getId());
                        petrinet.createArc(tempPlaceElement.getId(), tempMap2.getId());
                        // Store them in the ID Mapper
                        ArrayList ids = new ArrayList();
                        ids.add(tempMap.getId()); // join
                        ids.add(tempMap2.getId()); // split
                        idMapper.put(tempElement.getId(), ids);
                    } else if (outgoingArcs.size() > 1 && incomingArcs.size() <= 1)
                    {
                        // Only Split
                        tempMap.setOperatorType(OperatorTransitionModel.XOR_SPLIT_TYPE);
                        petrinet.createElement(tempMap, "").setElementContext(tempElement.getElementContext());

                    } else if (outgoingArcs.size() <= 1 && incomingArcs.size() > 1)
                    {
                        // Only Join
                        tempMap.setOperatorType(OperatorTransitionModel.XOR_JOIN_TYPE);
                        petrinet.createElement(tempMap, "").setElementContext(tempElement.getElementContext());
                    } else
                    {
                        // Simple
                        tempMap.setType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
                        tempMap.setOperatorType(-1);
                        tempPetriElement = petrinet.createElement(tempMap, "");
                        tempPetriElement.setElementContext(tempElement.getElementContext());
                        tempPetriElement.getElementContext().setXORType(true);
                    }

                } else if (((OperatorModel) tempElement).getOperatorType() == OperatorModel.AND_TYPE)
                {
                    // AND Splist & Joins will be not translated
                    tempMap.setType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
                    tempMap.setOperatorType(-1);
                    tempPetriElement = petrinet.createElement(tempMap, "");
                    tempPetriElement.setElementContext(tempElement.getElementContext());
                    tempPetriElement.getElementContext().setANDType(true);
                }
            } else if (tempElement.getType() == AbstractUMLElementModel.STATE_TYPE)
            {
                tempMap.setType(PetriNetModelElement.PLACE_TYPE);
                if (((StateModel) tempElement).getStateType() == StateModel.STATE_START_TYPE)
                {
                    tempMap.setName("START");
                } else if (((StateModel) tempElement).getStateType() == StateModel.STATE_STOP_TYPE)
                {
                    tempMap.setName("STOP");
                }
                petrinet.createElement(tempMap, "").setElementContext(tempElement.getElementContext());
            }
        }
        // ##### 2. Try Connect old arcs... if fails create new one ##### //
        AbstractElementModel tempTarget, tempSource;
        //
        Object selectedSourceId = null;
        Object selectedTargetId = null;
        for (Iterator iter = uml.getElementContainer().getArcMap().values().iterator(); iter.hasNext();)
        {
            ArcModel arc = (ArcModel) iter.next();
            // memo all outgoing and incoming arcs
            tempSource = uml.getElementContainer().getElementById(arc.getSourceId());
            tempTarget = uml.getElementContainer().getElementById(arc.getTargetId());
            // Check if Target or source is listed in the idMapper... then
            // correct Ids!
            if (idMapper.containsKey(tempSource.getId()))
            {
                selectedSourceId = ((List) idMapper.get(tempSource.getId())).get(1); // It's
                // the
                // Split!
            } else
            {
                selectedSourceId = tempSource.getId();
            }
            if (idMapper.containsKey(tempTarget.getId()))
            {
                selectedTargetId = ((List) idMapper.get(tempTarget.getId())).get(0); // It's
                // the
                // Join!
            } else
            {
                selectedTargetId = tempTarget.getId();
            }

            if (AbstractUMLElementModel.ACTIVITY_TYPE == tempSource.getType() || tempSource.getType() == AbstractUMLElementModel.OPERATOR_TYPE)
            {
                if (AbstractUMLElementModel.ACTIVITY_TYPE == tempTarget.getType() || tempTarget.getType() == AbstractUMLElementModel.OPERATOR_TYPE)
                {
                    CreationMap tempPlace = CreationMap.createMap();
                    tempPlace.setType(AbstractPetriNetModelElement.PLACE_TYPE);
                    tempPlace.setPosition((tempSource.getX() + tempTarget.getX()) / 2, (tempSource.getY() + tempTarget.getY()) / 2);
                    tempPlaceElement = petrinet.createElement(tempPlace, "");
                    petrinet.createArc(selectedSourceId, tempPlaceElement.getId());
                    petrinet.createArc(tempPlaceElement.getId(), selectedTargetId);
                } else if (AbstractUMLElementModel.STATE_TYPE == tempTarget.getType())
                {
                    petrinet.createArc(selectedSourceId, selectedTargetId);
                }
            } else if (AbstractUMLElementModel.STATE_TYPE == tempSource.getType())
            {
                if (AbstractUMLElementModel.ACTIVITY_TYPE == tempTarget.getType() || tempTarget.getType() == AbstractUMLElementModel.OPERATOR_TYPE)
                {
                    petrinet.createArc(selectedSourceId, selectedTargetId);
                } else
                {
                    // ???
                }
            }
        }
        LoggerManager.debug(Constants.CORE_LOGGER, "### Translation done (" + (System.currentTimeMillis() - begin) + " ms)");
        return petrinet;
    }
}
