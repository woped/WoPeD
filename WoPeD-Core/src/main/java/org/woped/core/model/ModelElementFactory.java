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
package org.woped.core.model;

import org.jgraph.graph.DefaultPort;
import org.woped.core.Constants;
import org.woped.core.model.petrinet.ANDJoinOperatorTransitionModel;
import org.woped.core.model.petrinet.ANDJoinXORSplitOperatorTransitionModel;
import org.woped.core.model.petrinet.ANDSplitJoinOperatorTransitionModel;
import org.woped.core.model.petrinet.ANDSplitOperatorTransitionModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.XORJoinANDSplitOperatorTransitionModel;
import org.woped.core.model.petrinet.XORJoinOperatorTransitionModel;
import org.woped.core.model.petrinet.XORJoinSplitOperatorTransitionModel;
import org.woped.core.model.petrinet.XORSplitOperatorTransitionModel;
import org.woped.core.utilities.LoggerManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * The <code>ModelElementFactory</code> could create
 * <code>PetriNetModelElement</code>. It creates initial id's for the
 * Elements. 29.04.2003
 */
public class ModelElementFactory
{

    /**
     * Method createModelElement. Creates a ModelElement.
     * 
     * @param map
     * @return
     */
    public static AbstractPetriNetElementModel createModelElement(CreationMap map)
    {

        AbstractPetriNetElementModel modElement = null;
        if (map.getId() != null)
        {
            try
            {
                // Check basetype of Element
                if (map.getType() == AbstractPetriNetElementModel.PLACE_TYPE)
                {
                    // Creating a new ModelElement with
                    modElement = new PlaceModel(map);
                    
                    // Tokens
                    if (map.getTokens() > 0)
                    	((PlaceModel) modElement).setTokens(map.getTokens());
                    
                } else if (map.getType() == AbstractPetriNetElementModel.SUBP_TYPE)
                {
                    /* CREATING A SUBPROCESS */
                    modElement = new SubProcessModel(map);
                } else if (map.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE && map.getOperatorType() == -1)
                {
                    /* CREATING A SIMPLE TRANSITION */
                    modElement = new TransitionModel(map);
                } else if (map.getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE)
                {
                    /* CREATING A AALST TRANSITION */
                	switch (map.getOperatorType())
                	{
                	case OperatorTransitionModel.XOR_SPLIT_TYPE:
                		modElement = new XORSplitOperatorTransitionModel(map);
                		break;
                	case OperatorTransitionModel.XOR_JOIN_TYPE:
                		modElement = new XORJoinOperatorTransitionModel(map);
                		break;
                        case OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE:
                            modElement = new XORJoinSplitOperatorTransitionModel(map);
                            break;
                	case OperatorTransitionModel.AND_SPLIT_TYPE:
                		modElement = new ANDSplitOperatorTransitionModel(map);
                		break;
                	case OperatorTransitionModel.AND_JOIN_TYPE:
                		modElement = new ANDJoinOperatorTransitionModel(map);
                		break;
                	case OperatorTransitionModel.AND_SPLITJOIN_TYPE:
                		modElement = new ANDSplitJoinOperatorTransitionModel(map);
                		break;
                	case OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE:
                		modElement = new ANDJoinXORSplitOperatorTransitionModel(map);
                		break;
                	case OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE:
                		modElement = new XORJoinANDSplitOperatorTransitionModel(map);
                		break;
                	default:
                        LoggerManager.error(Constants.CORE_LOGGER, "Unknown Operator Type (" + map.getOperatorType() + ")");
                	}
                    // modElement.setId(id);
                    // create inital simple trans
                   ((OperatorTransitionModel) modElement).addNewSimpleTrans();
                    // ((OperatorTransitionModel)
                    // modElement).setInitalSimpleTrans(simpleTrans);
                }
                if (modElement != null)
                {
                    // add the DefaultPort as child to the DefaultGraphCell
                    PortCell port = new PortCell();
                    modElement.add(port);
                }
                return modElement;

            } catch (Exception ee)
            {
                LoggerManager.error(Constants.CORE_LOGGER, "Could not create model! Type not supported (" + map.getType() + ")");
                ee.printStackTrace();
                return null;
            }
        } else
        {
            LoggerManager.error(Constants.CORE_LOGGER, "ID must be set");
            return null;
        }

    }

    public static ArcModel createArcModel(String arcId, DefaultPort source, DefaultPort target)
    {

        if (arcId != null)
        {
            ArcModel arc = new ArcModel();
            arc.setId(arcId);
            arc.setSource(source);
            arc.setTarget(target);
            return arc;
        } else
        {
            LoggerManager.error(Constants.CORE_LOGGER, "ID must be set");
            return null;
        }
    }
}