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
package org.woped.file;

import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlOptions;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractModelProcessor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.oldpnml.Arc;
import org.woped.oldpnml.Net;
import org.woped.oldpnml.Place;
import org.woped.oldpnml.PnmlDocument;
import org.woped.oldpnml.Transition;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * The Import Class parses an <br>
 * @see org.woped.editor.core.model.PetriNetModelProcessor <br>
 *      defined in PNML Format. To get more Information about PNML look at <br>
 *      <a
 *      href="http://www.informatik.hu-berlin.de/top/pnml">http://www.informatik.hu-berlin.de/top/pnml
 *      </a> <br>
 *      <br>
 *      In Order to parse the extended Notation (WF-Nets from W.v.d.Aalst) its
 *      necessary to read toolspecific elements additionaly. <br>
 *      <br>
 *      Created on 29.04.2003 <br>
 *      Last change 01.09.2005 (S.Landes) <br>
 */
public class OLDPNMLImport2 extends PNMLImport
{
    private IEditor[]  editor  = null;

    private XmlOptions opt     = new XmlOptions();

    // private boolean loadSuccess = false;
    PnmlDocument       pnmlDoc = null;

    /**
     * @see java.lang.Object#Object()
     */
    public OLDPNMLImport2(ApplicationMediator am)
    {
        super(am, null);
        opt.setUseDefaultNamespace();
        Map<String, String> map = new HashMap<String, String>();
        map.put("", "oldpnml.woped.org");
        map.put("pnml.woped.org", "oldpnml.woped.org");
        opt.setLoadSubstituteNamespaces(map);
    }

    /**
     * Load an XML document using the generated PNMLFactory class
     * 
     * @param filename
     *            An existing XML file name
     */
    public boolean run(String absolutePath)
    {
        InputStream is;
        try
        {
            is = new FileInputStream(absolutePath);
            return run(is);
        } catch (FileNotFoundException e)
        {
            LoggerManager.warn(Constants.FILE_LOGGER, "File does not exists. " + absolutePath);
            return false;
        }

    }

    /**
     * 
     * @param is
     * @return
     */
    public boolean run(InputStream is)
    {
        LoggerManager.debug(Constants.FILE_LOGGER, "##### START PNML IMPORT (OLD VERSION) #####");
        try
        {
            pnmlDoc = PnmlDocument.Factory.parse(is, opt);
            createEditorFromBeans();
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            LoggerManager.warn(Constants.FILE_LOGGER, "   ... Could not load PNML file. When saving, new pnml file-format will be created.");
            return false;
        } finally
        {
            LoggerManager.debug(Constants.FILE_LOGGER, "##### END PNML IMPORT (OLD VERSION) #####");
        }
    }

    private void createEditorFromBeans() throws Exception
    {
        createEditorFromBeans(pnmlDoc.getPnml());
    }

    public IEditor[] getEditor()
    {
        return editor;
    }

    private void createEditorFromBeans(PnmlDocument.Pnml pnml) throws Exception
    {
        // parse through PNML an create Model Elements
        Net aNet = pnml.getNet();
        editor = new IEditor[1];

        editor[0] = getMediator().createEditor(AbstractModelProcessor.MODEL_PROCESSOR_PETRINET, true);
        // attr. id
        ((PetriNetModelProcessor) editor[0].getModelProcessor()).setId(aNet.getId());
        // attr. type
        // Type no other types
        // name
        importPlaces(aNet.getPlaceArray(), 0);
        importTransitions(aNet.getTransitionArray(), 0);
        // important... import arcs in the end
        importArcs(aNet.getArcArray(), 0);

    }

    private void importPlaces(Place[] places, int editorIndex) throws Exception
    {
        int tokens;
        CreationMap map;
        // ??? int x;
        // ??? int y;
        for (int i = 0; i < places.length; i++)
        {
            map = CreationMap.createMap();
            map.setEditOnCreation(false);
            map.setId(places[i].getId());
            map.setType(PetriNetModelElement.PLACE_TYPE);
            map.setPosition(places[i].getGraphics().getPositionArray(0).getX(), places[i].getGraphics().getPositionArray(0).getY());
            map.setName(places[i].getName().getValue());
            /*
             * ODO Offset if
             * (places[i].getName().getGraphics().getOffsetArray().length > 0)
             * map.setNamePosition(places[i].getName().getGraphics().getOffsetArray(0).getX(),
             * places[i].getName() .getGraphics().getOffsetArray(0).getY()); if
             * (places[i].getName().getGraphics().getOffsetArray().length > 1) {
             * tempWidth =
             * places[i].getName().getGraphics().getOffsetArray(1).getX() -
             * places[i].getName().getGraphics().getOffsetArray(0).getX();
             * tempHeight =
             * places[i].getName().getGraphics().getOffsetArray(1).getX() -
             * places[i].getName().getGraphics().getOffsetArray(0).getX();
             * map.setNameSize(new Dimension(tempWidth, tempHeight)); }
             */
            if (places[i].isSetInitialMarking() && (tokens = Integer.parseInt(places[i].getInitialMarking().getValue())) != -1)
            {
                map.setTokens(tokens);
            }
            getEditor()[editorIndex].createElement(map);
            LoggerManager.debug(Constants.FILE_LOGGER, "   ... Place (ID:" + places[i].getId() + ") imported");
        }
    }

    private void importTransitions(Transition[] transitions, int editorIndex) throws Exception
    {
        CreationMap map;

        for (int i = 0; i < transitions.length; i++)
        {
            map = CreationMap.createMap();
            map.setEditOnCreation(false);
            map.setType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
            map.setId(transitions[i].getId());
            map.setPosition(transitions[i].getGraphics().getPositionArray(0).getX(), transitions[i].getGraphics().getPositionArray(0).getY());
            map.setName(transitions[i].getName().getValue());
            /*
             * TOO Offset if
             * (transitions[i].getName().getGraphics().getOffsetArray().length >
             * 0)
             * map.setNamePosition(transitions[i].getName().getGraphics().getOffsetArray(0).getX(),
             * transitions[i]
             * .getName().getGraphics().getOffsetArray(0).getY()); if
             * (transitions[i].getName().getGraphics().getOffsetArray().length >
             * 1) { tempWidth =
             * transitions[i].getName().getGraphics().getOffsetArray(1).getX() -
             * transitions[i].getName().getGraphics().getOffsetArray(0).getY();
             * tempHeight =
             * transitions[i].getName().getGraphics().getOffsetArray(1).getX() -
             * transitions[i].getName().getGraphics().getOffsetArray(0).getY();
             * map.setNameSize(new Dimension(tempWidth, tempHeight)); }
             */
            if (ConfigurationManager.getConfiguration().isImportToolspecific() && transitions[i].isSetToolspecific())
            {
                if (transitions[i].getToolspecific().getTool().equals("WoPeD") || transitions[i].getToolspecific().getTool().equals("PWT"))
                {
                    if (transitions[i].getToolspecific().isSetOperator())
                    {
                        map.setOperatorType(transitions[i].getToolspecific().getOperator().getOperatorType());
                        map.setType(TransitionModel.TRANS_OPERATOR_TYPE);
                        map.setId(transitions[i].getToolspecific().getOperator().getId());
                    }
                    /*
                     * else if
                     * (transitions[i].getToolspecific().isSetSubprocess()) {
                     * map.setType(PetriNetModelElement.SUBP_TYPE); }
                     */
                    if (transitions[i].getToolspecific().isSetTrigger())
                    {
                        map.setTriggerType(transitions[i].getToolspecific().getTrigger().getTriggerType());
                        map.setTriggerPosition(transitions[i].getToolspecific().getTrigger().getGraphics().getPositionArray(0).getX(), transitions[i].getToolspecific().getTrigger().getGraphics()
                                .getPositionArray(0).getY());
                    }
                } else
                {
                    map.addUnknownToolSpec(transitions[i].getToolspecific());
                }
            }
            if (!getEditor()[editorIndex].getModelProcessor().getElementContainer().containsElement(map.getId()))
            {
                getEditor()[editorIndex].createElement(map);
                LoggerManager.debug(Constants.FILE_LOGGER, " ... Transition (ID:" + map.getId() + ")imported");
            }
        }
    }

    private void importArcs(Arc[] arcs, int editorIndex) throws Exception
    {
        PetriNetModelElement currentSourceModel = null;
        PetriNetModelElement currentTargetModel = null;
        ArcModel arc = null;
        CreationMap map;
        for (int i = 0; i < arcs.length; i++)
        {
            currentSourceModel = (PetriNetModelElement) getEditor()[editorIndex].getModelProcessor().getElementContainer().getElementById(arcs[i].getSource());
            currentTargetModel = (PetriNetModelElement) getEditor()[editorIndex].getModelProcessor().getElementContainer().getElementById(arcs[i].getTarget());
            String tempID;

            if (ConfigurationManager.getConfiguration().isImportToolspecific())
            {
                if (currentTargetModel == null && currentSourceModel != null)
                {
                    if (arcs[i].getTarget().indexOf(OperatorTransitionModel.INNERID_SEPERATOR) != 0)
                    {
                        tempID = arcs[i].getTarget().substring(0, arcs[i].getTarget().indexOf(OperatorTransitionModel.INNERID_SEPERATOR));
                    } else
                    {
                        tempID = arcs[i].getTarget().substring(0, arcs[i].getTarget().indexOf(OperatorTransitionModel.INNERID_SEPERATOR_OLD));
                    }
                    if (isOperator(getEditor()[editorIndex].getModelProcessor(), tempID))
                    {
                        map = CreationMap.createMap();
                        map.setArcSourceId(arcs[i].getSource());
                        map.setArcTargetId(tempID);
                        arc = getEditor()[editorIndex].createArc(map);
                    }
                }
                if (currentSourceModel == null && currentTargetModel != null)
                {
                    if (arcs[i].getSource().indexOf(OperatorTransitionModel.INNERID_SEPERATOR) != 0)
                    {
                        tempID = arcs[i].getSource().substring(0, arcs[i].getSource().indexOf(OperatorTransitionModel.INNERID_SEPERATOR));
                    } else
                    {
                        tempID = arcs[i].getSource().substring(0, arcs[i].getSource().indexOf(OperatorTransitionModel.INNERID_SEPERATOR_OLD));
                    }

                    if (isOperator(getEditor()[editorIndex].getModelProcessor(), tempID))
                    {
                        map = CreationMap.createMap();
                        map.setArcSourceId(tempID);
                        map.setArcTargetId(arcs[i].getTarget());
                        arc = getEditor()[editorIndex].createArc(map);
                    }
                }
                if (currentTargetModel != null && currentSourceModel != null)
                {
                    map = CreationMap.createMap();
                    map.setArcSourceId(arcs[i].getSource());
                    map.setArcTargetId(arcs[i].getTarget());
                    arc = getEditor()[editorIndex].createArc(map);
                }
            } else
            {
                map = CreationMap.createMap();
                map.setArcSourceId(arcs[i].getSource());
                map.setArcTargetId(arcs[i].getTarget());
                arc = getEditor()[editorIndex].createArc(map);
            }
            if (arcs[i].isSetGraphics() && arc != null)
            {
                for (int j = 0; j < arcs[i].getGraphics().getPositionArray().length; j++)
                {
                    arc.addPoint(new Point2D.Double(arcs[i].getGraphics().getPositionArray(j).getX(), arcs[i].getGraphics().getPositionArray(j).getY()));
                }
            }
            LoggerManager.debug(Constants.FILE_LOGGER, " ... Arc (ID:" + arcs[i].getId() + "( " + arcs[i].getSource() + " -> " + arcs[i].getTarget() + ") created");
        }
    }

    private boolean isOperator(AbstractModelProcessor net, String elementId) throws Exception
    {
        if (elementId != null && net.getElementContainer().getElementById(elementId) != null
                && net.getElementContainer().getElementById(elementId).getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
        {
            return true;
        } else
        {
            return false;
        }
    }
}
