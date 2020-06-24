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
package org.woped.file;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.xmlbeans.XmlOptions;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.IntPair;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.ModelElementFactory;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.EditorLayoutInfo;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.model.petrinet.SimulationModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.VisualController;
import org.woped.editor.controller.WoPeDUndoManager;
import org.woped.editor.controller.bpel.Assign;
import org.woped.editor.controller.bpel.Empty;
import org.woped.editor.controller.bpel.Invoke;
import org.woped.editor.controller.bpel.Receive;
import org.woped.editor.controller.bpel.Reply;
import org.woped.editor.controller.bpel.Wait;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.gui.translations.Messages;
import org.woped.pnml.AnnotationGraphisType;
import org.woped.pnml.ArcType;
import org.woped.pnml.DimensionType;
import org.woped.pnml.GraphicsSimpleType;
import org.woped.pnml.NetType;
import org.woped.pnml.NetType.Page;
import org.woped.pnml.NodeType;
import org.woped.pnml.OccuredtransitionType;
import org.woped.pnml.OrganizationUnitType;
import org.woped.pnml.PlaceType;
import org.woped.pnml.PnmlDocument;
import org.woped.pnml.PnmlType;
import org.woped.pnml.PositionType;
import org.woped.pnml.ResourceMappingType;
import org.woped.pnml.ResourceType;
import org.woped.pnml.RoleType;
import org.woped.pnml.SimulationType;
import org.woped.pnml.SuperModelType;
import org.woped.pnml.TAssign;
import org.woped.pnml.TEmpty;
import org.woped.pnml.TInvoke;
import org.woped.pnml.TPartnerLink;
import org.woped.pnml.TPartnerLinks;
import org.woped.pnml.TReceive;
import org.woped.pnml.TReply;
import org.woped.pnml.TVariable;
import org.woped.pnml.TVariables;
import org.woped.pnml.TWait;
import org.woped.pnml.TextType;
import org.woped.pnml.TextType.Phrase;
import org.woped.pnml.TransitionType;

// TODO: BUG in import. When import toolspec mit splitjoin. import ONLY one arc
// !!!

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         <p>
 *         The Import Class parses an <code>PetriNet </code> defined in PNML
 *         Format. To get more Information about PNML look at <br>
 *         <a
 *         href="http://www.informatik.hu-berlin.de/top/pnml">http://www.informatik
 *         .hu-berlin.de/top/pnml </a> <br>
 *         <br>
 *         In Order to parse the extended Notation (WF-Nets from W.v.d.Aalst)
 *         its necessary to read toolspecific elements additionaly. <br>
 * @see org.woped.core.model.PetriNetModelProcessor
 * <br>
 * Created on 29.04.2003 <br>
 * Last change 05.12.2004 (S.Landes) <br>
 */
public class PNMLImport {
    /**
     * Contains a collection of warnings which has occurred during import.
     * <p>
     * Access is package local for testing purposes.
     */
    Vector<String> warnings = new Vector<>();
    private IEditor[] editor = null;
    private PnmlDocument pnmlDoc = null;
    private XmlOptions opt = new XmlOptions();
    // private IStatusBar[] statusBars = null;
    private AbstractApplicationMediator mediator = null;

    /**
     * TODO: DOCUMENTATION (silenco)
     *
     * @param am
     */
    public PNMLImport(AbstractApplicationMediator am) {
        opt.setUseDefaultNamespace();
        mediator = am;
        Map<String, String> map = new HashMap<String, String>();
        map.put("", "pnml.woped.org");
        // Interpret pnml documents according to foreign standardized pnml
        // schema as our own
        map.put("http://www.pnml.org/version-2009/grammar/pnml", "pnml.woped.org");

        opt.setLoadSubstituteNamespaces(map);
        if (true) {
            opt.setCompileNoUpaRule();
            opt.setCompileNoPvrRule();
            opt.setCompileNoValidation();
        }
    }

    public boolean run(String absolutePath) {
        return run(absolutePath, true);
    }

    /**
     * Load an XML document using the generated PNMLFactory class
     *
     * @param absolutePath An existing XML file name
     */
    public boolean run(String absolutePath, boolean showUI) {
        InputStream is;
        try {
            is = new FileInputStream(absolutePath);
            return run(is, null, showUI);
        } catch (FileNotFoundException e) {
            LoggerManager.warn(Constants.FILE_LOGGER, "File does not exists. " + absolutePath);
            return false;
        }

    }

    public boolean run(InputStream is) {
        return run(is, null, true);
    }

    public boolean run(InputStream is, String editorName) {
        return run(is, editorName, true);
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     *
     * @param is
     * @return
     */
    public boolean run(InputStream is, String editorName, boolean showUI) {
        LoggerManager.debug(Constants.FILE_LOGGER, "##### START PNML Version (1.3.2) IMPORT #####");

        long begin = System.currentTimeMillis();
        try {
            pnmlDoc = PnmlDocument.Factory.parse(is, opt);
            if (editorName != null) createEditorFromBeans(editorName, showUI);
            else createEditorFromBeans(showUI);
            if (!warnings.isEmpty()) {
                StringBuilder warningStrings = new StringBuilder();
                warningStrings.append("Imported a not valid PNML:\n");

                for (Iterator<String> iter = warnings.iterator(); iter.hasNext(); ) {
                    warningStrings.append(iter.next() + "\n");
                }
                LoggerManager.warn(Constants.FILE_LOGGER, warningStrings.toString());
            }
            return true;
        } catch (FileNotFoundException e) {
            LoggerManager.warn(Constants.FILE_LOGGER, "PNML file not found");
            return false;
        } catch (Exception e) {
            LoggerManager.warn(Constants.FILE_LOGGER, "   ... Could parse PNML file. Perhaps OLD PNML file-format. When saving, new pnml file-format will be created.");
            e.printStackTrace();

            JOptionPane.showMessageDialog(null, Messages.getString("Import.PNML.Text"), Messages.getString("Import.PNML.Title"), JOptionPane.WARNING_MESSAGE);
            return false;
        } finally {
            LoggerManager.debug(Constants.FILE_LOGGER, "##### END PNML IMPORT Version (1.3.2) ##### (" + (System.currentTimeMillis() - begin) + " ms)");
        }

    }

    public boolean runEx(String content) {
        return runEx(content, true);
    }

    /**
     * runEx()
     * <p>
     * load the Content from a given XML String
     * <p>
     *
     * @param content - Content of PNML File
     * @return
     */
    public boolean runEx(String content, boolean showUI) {
        LoggerManager.debug(Constants.FILE_LOGGER, "##### START PNML Version (1.3.2) IMPORT #####");

        long begin = System.currentTimeMillis();
        try {
            pnmlDoc = PnmlDocument.Factory.parse(content, opt);
            createEditorFromBeans(showUI);
            if (!warnings.isEmpty()) {
                LoggerManager.warn(Constants.FILE_LOGGER, "Imported a not valid PNML.");
                StringBuffer warningStrings = new StringBuffer();
                for (Iterator<String> iter = warnings.iterator(); iter.hasNext(); ) {
                    warningStrings.append(iter.next());
                }
                JOptionPane.showMessageDialog(null, Messages.getString("Import.PNML.Text"), Messages.getString("Import.PNML.Title"), JOptionPane.WARNING_MESSAGE);
            }
            return true;
        } catch (Exception e) {
            LoggerManager.warn(Constants.FILE_LOGGER, "   ... Could parse PNML file. Perhaps OLD PNML file-format. When saving, new pnml file-format will be created.");
            // return
            return false;
        } finally {
            LoggerManager.debug(Constants.FILE_LOGGER, "##### END PNML IMPORT Version (1.3.2) ##### (" + (System.currentTimeMillis() - begin) + " ms)");
        }

    }

    private void createEditorFromBeans(String editorName, boolean showUI) throws Exception {
        importNets(pnmlDoc.getPnml(), editorName, showUI);
    }

    private void createEditorFromBeans(boolean showUI) throws Exception {
        importNets(pnmlDoc.getPnml(), null, showUI);
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     *
     * @return
     */
    public IEditor[] getEditor() {
        return editor;
    }

    private void importNets(PnmlType pnml, String editorName, boolean showUI) throws Exception {
        editor = new IEditor[pnml.getNetArray().length];
        NetType currentNet;
        Dimension dim;
        Point location;
        PetriNetModelProcessor currentPetrinet;
        SimulationType[] simulations;

        for (int i = 0; i < pnml.getNetArray().length; i++) {
            simulations = null;
            boolean savedFlag = true;
            currentNet = pnml.getNetArray(i);
            editor[i] = mediator.createEditor(true, showUI);

            if (showUI) {
                if (editor[i].getGraph().getUndoManager() != null) {

                    ((WoPeDUndoManager) editor[i].getGraph().getUndoManager()).setEnabled(false);
                }
            }
            currentPetrinet = (editor[i].getModelProcessor());
            // attr. id
            currentPetrinet.setId(currentNet.getId());
            // attr. type
            // Type no other types
            // name
            if (currentNet.isSetName()) {
                currentPetrinet.setName(currentNet.getName().getText());
            }
            if (showUI) if (ConfigurationManager.getConfiguration().isImportToolspecific()) {
                // toolspecific
                for (int j = 0; j < currentNet.getToolspecificArray().length; j++) {
                    if (currentNet.getToolspecificArray(j).getTool().equals("WoPeD")) {
                        if (currentNet.getToolspecificArray(j).isSetBounds()) {
                            dim = new Dimension(currentNet.getToolspecificArray(j).getBounds().getDimension().getX().intValue(), currentNet.getToolspecificArray(j).getBounds().getDimension().getY().intValue());
                            location = new Point(currentNet.getToolspecificArray(j).getBounds().getPosition().getX().intValue(), currentNet.getToolspecificArray(j).getBounds().getPosition().getY().intValue());
                            if (editor[i] instanceof EditorVC) {
                                // Pass read layout information on to the
                                // editor
                                EditorLayoutInfo layout = new EditorLayoutInfo();
                                layout.setSavedSize(dim);
                                layout.setSavedLocation(location);

                                // try to import the type of Layout (false
                                // if vertical)
                                editor[i].setRotateSelected(currentNet.getToolspecificArray(j).getVerticalLayout());

                                // for importing a vertical net to change
                                // the rotate-button
                                if (currentNet.getToolspecificArray(j).getVerticalLayout() == true) {
                                    // EditorVC.setRotateSelected(true);
                                    VisualController.getInstance().propertyChange(new PropertyChangeEvent(this, "Import", null, null));

                                    // Update the UI representation
                                    // EditorVC.getGraph().updateUI();
                                }

                                if (currentNet.getToolspecificArray(j).isSetScale()) {
                                    // try to import the saved scale
                                    editor[i].getGraph().setScale(currentNet.getToolspecificArray(j).getScale() / 100.0);
                                }

                                // Only if also the remaining information is
                                // available,
                                // try to import the width of the tree view
                                // and the height of the overview panel
                                if (currentNet.getToolspecificArray(j).isSetTreeWidth()) {
                                    GraphicsSimpleType bounds = currentNet.getToolspecificArray(j).getBounds();
                                    DimensionType dimension = bounds.getDimension();
                                    int x = (int) (dimension.getX()).doubleValue();
                                    x = x - currentNet.getToolspecificArray(j).getTreeWidth();
                                    layout.setTreeViewWidthRight(x);
                                } else if (currentNet.getToolspecificArray(j).isSetTreeWidthRight()) {
                                    layout.setTreeViewWidthRight(currentNet.getToolspecificArray(j).getTreeWidthRight());
                                } else {
                                    layout.setTreeViewWidthRight(currentNet.getToolspecificArray(j).getBounds().getDimension().getX().intValue() - 100);
                                }

                                if (currentNet.getToolspecificArray(j).isSetOverviewPanelVisible()) {
                                    layout.setOverviewPanelVisible(currentNet.getToolspecificArray(j).getOverviewPanelVisible());
                                } else {
                                    layout.setOverviewPanelVisible(true);
                                }

                                if (currentNet.getToolspecificArray(j).isSetTreeHeightOverview()) {
                                    if (currentNet.getToolspecificArray(j).getTreeHeightOverview() < 1) {
                                        layout.setTreeHeightOverview(100);
                                    } else {
                                        layout.setTreeHeightOverview(currentNet.getToolspecificArray(j).getTreeHeightOverview());
                                    }
                                } else {
                                    layout.setTreeHeightOverview(100);
                                }

                                if (currentNet.getToolspecificArray(j).isSetTreePanelVisible()) {

                                    layout.setTreePanelVisible(currentNet.getToolspecificArray(j).getTreePanelVisible());

                                } else {
                                    layout.setTreePanelVisible(true);
                                }

                                ((EditorVC) editor[i]).getEditorPanel().setSavedLayoutInfo(layout);
                            }
                        }
                        if (currentNet.getToolspecificArray(j).isSetResources()) {
                            // ResourceMapType resourceMap =
                            // currentNet.getToolspecificArray(j).getResources().getResourceMap();
                            ResourceMappingType[] resourceMaps = currentNet.getToolspecificArray(j).getResources().getResourceMappingArray();

                            RoleType[] roles = currentNet.getToolspecificArray(j).getResources().getRoleArray();
                            ResourceClassModel roleModelTemp;
                            for (int k = 0; k < roles.length; k++) {
                                roleModelTemp = new ResourceClassModel(roles[k].getName(), ResourceClassModel.TYPE_ROLE);
                                currentPetrinet.addRole(roleModelTemp);
                                SuperModelType[] supermodels = roles[k].getSuperModelArray();
                                ResourceClassModel superModelTemp;
                                for (int l = 0; l < supermodels.length; l++) {
                                    superModelTemp = new ResourceClassModel(supermodels[l].getName(), ResourceClassModel.TYPE_ROLE);
                                    roleModelTemp.addSuperModel(superModelTemp);
                                }
                            }

                            OrganizationUnitType[] units = currentNet.getToolspecificArray(j).getResources().getOrganizationUnitArray();
                            ResourceClassModel orgUnitTemp;
                            for (int l = 0; l < units.length; l++) {
                                orgUnitTemp = new ResourceClassModel(units[l].getName(), ResourceClassModel.TYPE_ORGUNIT);
                                currentPetrinet.addOrgUnit(orgUnitTemp);

                                SuperModelType[] supermodels = units[l].getSuperModelArray();
                                ResourceClassModel superModelTemp;
                                for (int m = 0; m < supermodels.length; m++) {
                                    superModelTemp = new ResourceClassModel(supermodels[m].getName(), ResourceClassModel.TYPE_ORGUNIT);
                                    orgUnitTemp.addSuperModel(superModelTemp);
                                }
                            }

                            ResourceType[] resources = currentNet.getToolspecificArray(j).getResources().getResourceArray();
                            ResourceModel resourceModelTemp;
                            for (int m = 0; m < resources.length; m++) {
                                resourceModelTemp = new ResourceModel(resources[m].getName());
                                currentPetrinet.addResource(resourceModelTemp);
                            }

                            for (int n = 0; n < resourceMaps.length; n++) {
                                currentPetrinet.addResourceMapping(resourceMaps[n].getResourceClass(), resourceMaps[n].getResourceID());
                            }
                        }
                        if (currentNet.getToolspecificArray(j).isSetSimulations()) {
                            // only save the simulationsarray to local
                            // variable
                            // here - the import itself
                            // has to be done after import of the
                            // transitions
                            // because the simulation
                            // references transitions (which otherwise
                            // result in
                            // 'null')
                            // see "importSimulations([...])" below
                            simulations = currentNet.getToolspecificArray(j).getSimulations().getSimulationArray();
                        }

                        if (currentNet.getToolspecificArray(j).isSetPartnerLinks()) {
                            TPartnerLinks plist = currentNet.getToolspecificArray(j).getPartnerLinks();
                            for (int x = 0; x < plist.sizeOfPartnerLinkArray(); x++) {
                                TPartnerLink link = plist.getPartnerLinkArray(x);
                                if (link.isSetMyRole() && link.isSetPartnerRole()) {
                                    currentPetrinet.getElementContainer().addPartnerLink(link.getName(), link.getPartnerLinkType().getNamespaceURI(), link.getPartnerLinkType().getLocalPart(), link.getPartnerRole(), link.getMyRole(), link.getWSDL());
                                } else if (link.isSetMyRole()) {
                                    currentPetrinet.getElementContainer().addPartnerLinkWithoutPartnerRole(link.getName(), link.getPartnerLinkType().getNamespaceURI(), link.getPartnerLinkType().getLocalPart(), link.getMyRole(), link.getWSDL());
                                } else if (link.isSetPartnerRole()) {
                                    currentPetrinet.getElementContainer().addPartnerLinkWithoutMyRole(link.getName(), link.getPartnerLinkType().getNamespaceURI(), link.getPartnerLinkType().getLocalPart(), link.getPartnerRole(), link.getWSDL());
                                }
                            }
                        }

                        if (currentNet.getToolspecificArray(j).isSetVariables()) {
                            TVariables vlist = currentNet.getToolspecificArray(j).getVariables();
                            for (int x = 0; x < vlist.sizeOfVariableArray(); x++) {
                                TVariable var = vlist.getVariableArray(x);
                                currentPetrinet.getElementContainer().addVariable(var.getName(), var.getType().getLocalPart());
                            }
                        }
                    } else {
                        currentPetrinet.addUnknownToolSpecs(currentNet.getToolspecificArray(j));
                    }
                }

            }

            // Import the net into the current ModelElementContainer
            importNet(currentNet, editor[i].getModelProcessor().getElementContainer());

            // Import textual descriptions
            // if(currentNet.isSetText() == true){
            // importTextualDescription(currentPetrinet, currentNet, editor[i]);
            // }

            // Import the simulations if any exist
            if (simulations != null) {
                savedFlag = importSimulations(simulations, currentPetrinet);
            }

            // Now build the graph from the ModelElementContainer
            if (showUI) {
                editor[i].getGraph().drawNet(editor[i].getModelProcessor());
                editor[i].updateNet();

                editor[i].getGraph().clearSelection();
                if (editor[i].getGraph().getUndoManager() != null) {
                    ((WoPeDUndoManager) editor[i].getGraph().getUndoManager()).setEnabled(true);
                }
                editor[i].updateNet();
                if (editorName != null) {
                    editor[i].setName(editorName);
                    editor[i].setSaved(false);
                } else editor[i].setSaved(savedFlag);
            }
        }
    }

    /**
     * Import the specified net into the specified ModelElementContainer.
     * This is a rather new approach of importing a net which does not depend
     * on an actual editor window to be open
     * (Note that for sub-processes we do not have such a windows)
     *
     * @param currentNet       specifies the source XMLBean for the current net
     * @param currentContainer specifies the ModelElementContainer that will
     *                         receive all the places, transitions and arcs from the
     *                         net stored in the XMLBean
     */
    private void importNet(NetType currentNet, ModelElementContainer currentContainer) throws Exception {
        if ((currentNet.getPlaceArray().length == 0) && (currentNet.getTransitionArray().length == 0) && (currentNet.getPageArray().length == 1))
            importNet(currentNet.getPageArray()[0], currentContainer);
        else {
            importPlaces(currentNet.getPlaceArray(), currentContainer);
            importTransitions(currentNet, currentContainer);
            // important... import arcs in the end because it is required, that all other elements are already present.
            importArcs(currentNet.getArcArray(), currentContainer);
            importTextualDescription(currentNet, currentContainer);
        }
    }

    private void importTextualDescription(NetType currentNet, ModelElementContainer currentContainer) throws Exception {

        TextType text = currentNet.getText();

        if (currentNet.getText() != null) {

            try {
                Phrase[] phraseArray = text.getPhraseArray();
                for (int z = 0; z < phraseArray.length; z++) {

                    String[] description = new String[2];
                    description[0] = phraseArray[z].getIds().trim();
                    description[1] = phraseArray[z].getStringValue().trim();
                    // processor.getParaphrasingModel().addElement(phraseArray[z].getIds(),
                    // phraseArray[z].getStringValue());
                    currentContainer.getParaphrasingModel().addElement(description[0], description[1]);
                    LoggerManager.debug(Constants.FILE_LOGGER, " ... Description (ID:" + description[0] + ") imported");
                }
            } catch (Exception e) {
                warnings.add("- SKIP DESCRIPTION: Exception while importing textual description.\n");
            }

        }

    }

    private void importNameAndLayout(NodeType node, CreationMap target) {
        target.setId(node.getId());
        if (node.getGraphics() != null) {
            target.setPosition(node.getGraphics().getPosition().getX().intValue(), node.getGraphics().getPosition().getY().intValue());
            if (node.getGraphics().isSetDimension())
                target.setSize(new IntPair(new Dimension(node.getGraphics().getDimension().getX().intValue(), node.getGraphics().getDimension().getY().intValue())));
        }
        if (node.getName() != null) target.setName(node.getName().getText());
        else
            // Elements that don't have a name will have their id used instead
            target.setName(node.getId());

        if (node.getName() != null && node.getName().isSetGraphics() && node.getName().getGraphics().getOffsetArray() != null && node.getName().getGraphics().getOffsetArray().length > 0) {
            int x = node.getName().getGraphics().getOffsetArray(0).getX().intValue();
            int y = node.getName().getGraphics().getOffsetArray(0).getY().intValue();
            target.setNamePosition(x, y);
        }
    }

    private void importPlaces(PlaceType[] places, ModelElementContainer currentContainer) throws Exception {
        int tokens;
        CreationMap map;
        boolean doNOTcreate = false;
        for (int i = 0; i < places.length; i++) {
            map = CreationMap.createMap();
            map.setEditOnCreation(false);
            map.setType(AbstractPetriNetElementModel.PLACE_TYPE);
            try {
                importNameAndLayout(places[i], map);

                try {
                    if (places[i].isSetInitialMarking()) {
                        // Remove all flavors of whitespace before converting to
                        // an integer
                        String initialMarkingString = places[i].getInitialMarking().getText().replaceAll("\\s", "");
                        if ((tokens = Integer.parseInt(initialMarkingString)) > 0) {
                            map.setTokens(tokens);
                        }
                    }
                    // toolspecific
                    if (ConfigurationManager.getConfiguration().isImportToolspecific()) {
                        for (int j = 0; j < places[i].getToolspecificArray().length; j++) {
                            if (places[i].getToolspecificArray(j).getTool().equals("WoPeD")) {
                                switch (places[i].getToolspecificArray(j).getOperator().getType()) {
                                    case OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE:
                                    case OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE:
                                    case OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE:
                                        doNOTcreate = true;
                                        break;
                                    default:
                                }
                            } else {
                                map.addUnknownToolSpec(places[i].getToolspecificArray(j));
                            }
                        }
                    }
                } catch (Exception e) {
                    warnings.add("- PLACE LOST INFORMATION (" + places[i].getId() + ") Exception while importing lesser important information.\n");
                }
                if (!doNOTcreate) {
                    // Seems like we're supposed to actually create this place
                    // (A reason *not* to do so would be e.g. that the place was
                    // part of the inner
                    // workings of an XOR split-join)
                    // Create the element using the element factory and the
                    // already configured creation map and add it to the model
                    // element container
                    AbstractPetriNetElementModel element = ModelElementFactory.createModelElement(map);
                    currentContainer.addElement(element);

                    LoggerManager.debug(Constants.FILE_LOGGER, " ... Place (ID:" + places[i].getId() + ") imported");
                }
                doNOTcreate = false;
            } catch (Exception e) {
                warnings.add("- SKIP PLACE: Exception while importing important information.\n");
            }
            // increaseCurrent();

        }
    }

    private void importTransitions(NetType currentNet, ModelElementContainer currentContainer) throws Exception {
        // The model element processor can correctly create resources and
        // triggers
        // for transitions so we use it at this point to do so
        PetriNetModelProcessor processor = new PetriNetModelProcessor();
        processor.setElementContainer(currentContainer);

        TransitionType transitions[] = currentNet.getTransitionArray();
        CreationMap map;

        for (int i = 0; i < transitions.length; i++) {
            map = CreationMap.createMap();
            map.setEditOnCreation(false);
            map.setType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
            try {
                importNameAndLayout(transitions[i], map);
                try {
                    if (ConfigurationManager.getConfiguration().isImportToolspecific()) {
                        for (int j = 0; j < transitions[i].getToolspecificArray().length; j++) {
                            if (transitions[i].getToolspecificArray(j).getTool().equals("WoPeD")) {
                                if (transitions[i].getToolspecificArray(j).isSetTime()) {
                                    map.setTransitionTime(transitions[i].getToolspecificArray(j).getTime());
                                }
                                if (transitions[i].getToolspecificArray(j).isSetTimeUnit()) {
                                    map.setTransitionTimeUnit(transitions[i].getToolspecificArray(j).getTimeUnit());
                                }

                                // set operatorOrientation
                                if (transitions[i].getToolspecificArray(j).isSetOrientation()) {
                                    map.setOperatorPosition(transitions[i].getToolspecificArray(j).getOrientation());
                                }
                                if (transitions[i].getToolspecificArray(j).isSetOperator()) {
                                    map.setOperatorType(transitions[i].getToolspecificArray(j).getOperator().getType());
                                    map.setType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
                                    map.setId(transitions[i].getToolspecificArray(j).getOperator().getId());
                                } else if (transitions[i].getToolspecificArray(j).isSetSubprocess()) {
                                    map.setType(AbstractPetriNetElementModel.SUBP_TYPE);
                                }
                                if (transitions[i].getToolspecificArray(j).isSetTrigger()) {
                                    map.setTriggerType(transitions[i].getToolspecificArray(j).getTrigger().getType());
                                    map.setTriggerPosition(transitions[i].getToolspecificArray(j).getTrigger().getGraphics().getPosition().getX().intValue(), transitions[i].getToolspecificArray(j).getTrigger().getGraphics().getPosition().getY().intValue());
                                }
                                if (transitions[i].getToolspecificArray(j).isSetTransitionResource()) {
                                    // &&
                                    // transitions[i].getToolspecificArray(j).getTrigger().getType()
                                    // == 200
                                    map.setResourceOrgUnit(transitions[i].getToolspecificArray(j).getTransitionResource().getOrganizationalUnitName());
                                    map.setResourceRole(transitions[i].getToolspecificArray(j).getTransitionResource().getRoleName());
                                    map.setResourcePosition(transitions[i].getToolspecificArray(j).getTransitionResource().getGraphics().getPosition().getX().intValue(), transitions[i].getToolspecificArray(j).getTransitionResource().getGraphics().getPosition().getY().intValue());
                                }
                                if (transitions[i].getToolspecificArray(j).isSetEmpty()) {
                                    TEmpty empty = transitions[i].getToolspecificArray(j).getEmpty();
                                    Empty bpel = new Empty(empty.getName());
                                    map.setBpeldata(bpel);
                                }
                                if (transitions[i].getToolspecificArray(j).isSetAssign()) {
                                    TAssign assign = transitions[i].getToolspecificArray(j).getAssign();
                                    Assign bpel = new Assign(assign.getName(), assign.getCopyArray(0).getFrom().getVariable(), assign.getCopyArray(0).getTo().getVariable());
                                    map.setBpeldata(bpel);
                                }
                                if (transitions[i].getToolspecificArray(j).isSetInvoke()) {
                                    TInvoke invoke = transitions[i].getToolspecificArray(j).getInvoke();
                                    Invoke bpel = new Invoke(invoke.getName(), invoke.getPartnerLink(), invoke.getOperation(), "", invoke.getInputVariable(), invoke.getOutputVariable());
                                    map.setBpeldata(bpel);
                                }
                                if (transitions[i].getToolspecificArray(j).isSetReceive()) {
                                    TReceive receive = transitions[i].getToolspecificArray(j).getReceive();
                                    Receive bpel = new Receive(receive.getName(), receive.getPartnerLink(), receive.getOperation(), receive.getVariable());
                                    map.setBpeldata(bpel);
                                }
                                if (transitions[i].getToolspecificArray(j).isSetReply()) {
                                    TReply reply = transitions[i].getToolspecificArray(j).getReply();
                                    Reply bpel = new Reply(reply.getName(), reply.getPartnerLink(), reply.getOperation(), reply.getVariable());
                                    map.setBpeldata(bpel);
                                }
                                if (transitions[i].getToolspecificArray(j).isSetWait()) {
                                    TWait wait = transitions[i].getToolspecificArray(j).getWait();
                                    Wait bpel = new Wait(wait.getName(), wait.getTyp(), wait.getYear(), wait.getMonth(), wait.getDay(), wait.getHour(), wait.getMinute(), wait.getSecond());
                                    map.setBpeldata(bpel);
                                }
                            } else {

                                map.addUnknownToolSpec(transitions[i].getToolspecificArray(j));
                            }
                        }
                    }
                } catch (Exception e) {
                    warnings.add("- TRANSITION LOST INFORMATION (" + transitions[i].getId() + "): Exception while importing lesser important information.");
                }

                if (!currentContainer.containsElement(map.getId())) {
                    // There is one fun thing about transitions:
                    // They may be inner transitions which are treated specially
                    // by carrying
                    // some tool-specific information as to which operator they
                    // belong.
                    // Now, instead of the transition, an operator is created
                    // for each of those inner transitions
                    // and all but the first such instance are discarded here
                    // because an element
                    // with the same id already exists at this point
                    AbstractPetriNetElementModel element = processor.createElement(map);
                    currentContainer.addElement(element);
                    LoggerManager.debug(Constants.FILE_LOGGER, " ... Transition (ID:" + map.getId() + ") imported ");
                    // increaseCurrent();

                    if (element.getType() == AbstractPetriNetElementModel.SUBP_TYPE) {
                        // The element we just created is a sub-process element
                        // Get the corresponding page from the PNML document
                        // and restore the sub-process elements by recursively
                        // calling importNet()
                        Page pages[] = currentNet.getPageArray();
                        if (pages != null) {
                            for (int currentPage = 0; currentPage < pages.length; ++currentPage) {
                                if (pages[currentPage].getId().equals(element.getId())) {
                                    // Only one sub-process net per page may be
                                    // defined for now
                                    // Ignore the rest
                                    NetType subProcessNets[] = pages[currentPage].getNetArray();
                                    if (subProcessNets.length > 0) {
                                        if (subProcessNets.length > 1)
                                            warnings.add("- SKIP SUBPROCESS NET: Only one sub-process net may be defined per sub-process.");

                                        NetType subProcessNet = subProcessNets[0];
                                        ModelElementContainer container = ((SubProcessModel) element).getSimpleTransContainer();

                                        importNet(subProcessNet, container);

                                        // Now see whether we have any
                                        // tool-specific information
                                        // attached to the sub-net
                                        // This might contain layout information
                                        // for the sub-process editor
                                        for (int j = 0; j < subProcessNet.getToolspecificArray().length; ++j)
                                            if (subProcessNet.getToolspecificArray(j).getTool().equals("WoPeD")) {
                                                if (subProcessNet.getToolspecificArray(j).isSetBounds()) {
                                                    Dimension dim = new Dimension(subProcessNet.getToolspecificArray(j).getBounds().getDimension().getX().intValue(), subProcessNet.getToolspecificArray(j).getBounds().getDimension().getY().intValue());
                                                    Point location = new Point(subProcessNet.getToolspecificArray(j).getBounds().getPosition().getX().intValue(), subProcessNet.getToolspecificArray(j).getBounds().getPosition().getY().intValue());
                                                    EditorLayoutInfo layout = new EditorLayoutInfo();

                                                    layout.setSavedSize(dim);
                                                    layout.setSavedLocation(location);
                                                    // Only if also the
                                                    // remaining information is
                                                    // available,
                                                    // try to import the width
                                                    // of the tree view
                                                    if (subProcessNet.getToolspecificArray(j).isSetTreeWidth()) {
                                                        layout.setTreeViewWidthRight(subProcessNet.getToolspecificArray(j).getBounds().getDimension().getX().intValue() - subProcessNet.getToolspecificArray(j).getTreeWidth());
                                                    }

                                                    if (subProcessNet.getToolspecificArray(j).isSetTreeWidthRight()) {
                                                        layout.setTreeViewWidthRight(subProcessNet.getToolspecificArray(j).getTreeWidthRight());
                                                    }

                                                    if (subProcessNet.getToolspecificArray(j).isSetOverviewPanelVisible()) {
                                                        layout.setOverviewPanelVisible(subProcessNet.getToolspecificArray(j).getOverviewPanelVisible());
                                                    } else {
                                                        layout.setOverviewPanelVisible(true);
                                                    }

                                                    if (subProcessNet.getToolspecificArray(j).isSetTreeHeightOverview()) {
                                                        layout.setTreeHeightOverview(subProcessNet.getToolspecificArray(j).getTreeHeightOverview());

                                                    }

                                                    if (subProcessNet.getToolspecificArray(j).isSetTreePanelVisible()) {
                                                        layout.setTreePanelVisible(subProcessNet.getToolspecificArray(j).getTreePanelVisible());
                                                    } else {
                                                        layout.setTreePanelVisible(true);
                                                    }

                                                    container.setEditorLayoutInfo(layout);
                                                }
                                            }
                                    }
                                }
                            }
                        } else warnings.add("- EXPECTED PAGE NOT FOUND: Subprocess information lost during import.");
                    }
                }
            } catch (Exception e) {
                warnings.add("- SKIP TRANSITION: Exception while importing important information.");
            }
        }
    }

    /**
     * Imports the arc beans into the given ModelElementContainer.
     * <p>
     * The method uses its own temporary {@code PetriNetModelProcessor} to create the arcs and operators.
     *
     * @param arcs             the arcs to be imported.
     * @param currentContainer the target container to import the arcs to.
     */
    void importArcs(ArcType[] arcs, ModelElementContainer currentContainer) {

        PetriNetModelProcessor processor = new PetriNetModelProcessor();
        processor.setElementContainer(currentContainer);
        boolean importToolSpecificAttributes = ConfigurationManager.getConfiguration().isImportToolspecific();

        for (int i = 0; i < arcs.length; i++) {
            importArc(processor, arcs[i], importToolSpecificAttributes);
        }
    }

    /**
     * Imports the given arc bean in the {@code ModelElementContainer} form the given {@code PetriNetModelProcessor}.
     * <p>
     * If the parameter importToolSpecificAttributes is set to true, the method tries to generate the WoPeD specific
     * operator transitions. Otherwise the arc is going to be imported with simple transitions only.
     *
     * @param processor                    the processor used to create the arcs.
     * @param arcBean                      the bean containing the attributes of the arc.
     * @param importToolSpecificAttributes flag to determine if the tool specific attributes should be imported.
     */
    private void importArc(PetriNetModelProcessor processor, ArcType arcBean, boolean importToolSpecificAttributes) {

        try {

            // Create the arc instance
            ArcModel arc = createArc(arcBean, processor, importToolSpecificAttributes);

            if (arc == null) {
                return;
            }

            // Set additional attributes
            setArcAttributes(arcBean, arc, importToolSpecificAttributes);

            LoggerManager.debug(Constants.FILE_LOGGER, " ... Arc created: (ID:" + arc.getId() + ") " + arc.getSourceId() + " -> " + arc.getTargetId() + " ");
        } catch (Exception e) {
            warnings.add("- SKIP ARC: Exception while importing important information. ");
        }
    }

    /**
     * Creates the imported arc within the petrinet.
     * <p>
     * If the parameter importToolSpecificAttributes is set to true, the method tries to generate the WoPeD specific
     * operator transitions. Otherwise the arc is going to be imported with simple transitions only.
     *
     * @param arcBean                      the arc bean containing the attributes of the imported arc.
     * @param processor                    the processor used to create the arc
     * @param importToolSpecificAttributes flag to determine, if the tool specific attributes should be imported.
     * @return the created arc, or {@code null} if the arc could not be created.
     */
    private ArcModel createArc(ArcType arcBean, PetriNetModelProcessor processor, boolean importToolSpecificAttributes) {

        ArcModel createdArc;
        ModelElementContainer container = processor.getElementContainer();
        AbstractPetriNetElementModel source;
        AbstractPetriNetElementModel target;

        source = container.getElementById(arcBean.getSource());
        target = container.getElementById(arcBean.getTarget());

        if (!importToolSpecificAttributes) {

            // verify that source and target exists
            if (source == null || target == null) {
                warnings.add("- INVALID ARC (" + arcBean.getId() + "): Couldn't resolve source and/or target.");
                return null;
            }

            // create arc with an simple transition
            createdArc = processor.createArc(arcBean.getId(), arcBean.getSource(), arcBean.getTarget(), new Point2D[0], true);

        } else {

            String operatorId;

            // Neither source or target is an operator
            if (target != null && source != null) {

                // create the arc with an simple transition
                createdArc = processor.createArc(arcBean.getId(), arcBean.getSource(), arcBean.getTarget(), new Point2D[0], true);
            }

            // source is an possible operator
            else if (source == null && target != null) {

                // extract operator id
                operatorId = getParentId(arcBean.getSource());

                // verify operator
                if (!isValidOperator(container, operatorId)) {
                    warnings.add("- INVALID ARC (" + arcBean.getId() + "): Source is not a valid operator.");
                    return null;
                }

                // create arc
                createdArc = processor.createArc(arcBean.getId(), operatorId, arcBean.getTarget(), new Point2D[0], true);
            }

            // target is an possible operator
            else if (target == null && source != null) {

                // extract operator Id
                operatorId = getParentId(arcBean.getTarget());

                // verify operator
                if (!isValidOperator(container, operatorId)) {
                    warnings.add("- INVALID ARC (" + arcBean.getId() + "): Target is not a valid operator" + ".");
                    return null;
                }

                // create arc
                createdArc = processor.createArc(arcBean.getId(), arcBean.getSource(), operatorId, new Point2D[0], true);
            }

            // Arc is maybe an auto generated inner arc of an operator and could be ignored
            else {
                String sourceId = getParentId(arcBean.getSource());
                String targetId = getParentId(arcBean.getTarget());

                if (!isValidOperator(container, sourceId) || !isValidOperator(container, targetId)) {
                    warnings.add("- INVALID ARC (" + arcBean.getId() + "): Source and/or target could not be resolved.");
                    return null;
                }

                LoggerManager.debug(Constants.FILE_LOGGER, " ... Arc skipped: (ID:" + arcBean.getId() + ") " + arcBean.getSource() + " -> " + arcBean.getTarget() + " ");
                return null;
            }
        }

        return createdArc;
    }

    /**
     * Sets the additional attributes of the arc, which aren't set during arc creation.
     * <p>
     * Such attributes may be the arc weight, the graphical representation or other tool specific values.
     *
     * @param arcBean                      the arc bean containing the attributes. Not null.
     * @param arc                          the arc on which the attributes should be set. Not null.
     * @param importToolSpecificAttributes flag to determine, if the tool specific attributes should be set.
     */
    private void setArcAttributes(ArcType arcBean, ArcModel arc, boolean importToolSpecificAttributes) {
        if (arcBean.isSetGraphics() && arc != null) {
            // Create two standard points that need to be always present
            // When created using the editor, those points are added
            // automatically
            // by the view
            // but if we want to restore arc points from the PNML file
            // we have to add those points manually
//            arc.addPoint(arc.getAttributes().createPoint(10, 10));
//            arc.addPoint(arc.getAttributes().createPoint(20, 20));

            for (int j = 0; j < arcBean.getGraphics().getPositionArray().length; j++) {
                arc.addPoint(new Point2D.Double(arcBean.getGraphics().getPositionArray(j).getX().doubleValue(), arcBean.getGraphics().getPositionArray(j).getY().doubleValue()), j + 1);
            }
        }

        // Import weight
        arc.setInscriptionValue(arcBean.getInscription().getText());
        AnnotationGraphisType weightLabel = arcBean.getInscription().getGraphics();
        if ( weightLabel != null ) {
            PositionType[] offsets = weightLabel.getOffsetArray();
            if ( offsets.length != 0 ) {
                PositionType labelOffset = offsets[0];
                Point2D labelPosition = new Point2D.Double(labelOffset.getX().doubleValue(), labelOffset.getY().doubleValue());
                arc.setWeightLablePosition(labelPosition);
            }

        }

        if (arc != null && importToolSpecificAttributes) {
            // Check whether we actually have an arc. If not, it
            // does not make sense
            // to import any tool-specific information.
            // Note that internal arcs with no connection to any
            // outer nodes
            // will not be imported, they will be implicitly
            // re-generated,
            // e.g. XOR Split-Join
            // Import toolspecific information for the arc
            for (int j = 0; j < arcBean.getToolspecificArray().length; j++) {
                if ((arcBean.getToolspecificArray(j).getTool() != null) && (arcBean.getToolspecificArray(j).getTool().equals("WoPeD"))) {
                    if (arcBean.getToolspecificArray(j).isSetRoute() && arcBean.getToolspecificArray(j).getRoute())
                        arc.setRoute(true);
                    if (arcBean.getToolspecificArray(j).isSetProbability())
                        arc.setProbability(arcBean.getToolspecificArray(j).getProbability());
                    if (arcBean.getToolspecificArray(j).isSetDisplayProbabilityOn())
                        arc.displayProbability(arcBean.getToolspecificArray(j).getDisplayProbabilityOn());
                    if (arcBean.getToolspecificArray(j).isSetDisplayProbabilityPosition()) {
                        Point location = new Point(arcBean.getToolspecificArray(j).getDisplayProbabilityPosition().getX().intValue(), arcBean.getToolspecificArray(j).getDisplayProbabilityPosition().getY().intValue());
                        arc.setProbabilityLabelPosition(location);
                    }
                } else {
                    arc.addUnknownToolSpecs(arcBean.getToolspecificArray(j));
                }
            }
        }
    }

    /**
     * Extracts the id of the parent from the given child id.
     * <p>
     * The method checks if a known operator separator is contained in the child id, and if so, it extracts the id of
     * the parent from it.
     * <p>
     * The access is package local for testing purposes. The method should not be uses outside this class.
     *
     * @param childId the id of the child. Not null.
     * @return the id of the parent or null, if the id couldn't be extracted.
     */
    String getParentId(String childId) {

        String parentId = null;

        // Check for place separator
        if (childId.contains(OperatorTransitionModel.OPERATOR_SEPERATOR_PLACE)) {
            parentId = childId.replace(OperatorTransitionModel.OPERATOR_SEPERATOR_PLACE, "");
        }

        // Check for transition separator
        if (childId.contains(OperatorTransitionModel.OPERATOR_SEPERATOR_TRANSITION)) {
            parentId = childId.substring(0, childId.indexOf(OperatorTransitionModel.OPERATOR_SEPERATOR_TRANSITION));
        }

        // Check for old separator
        if (childId.contains(OperatorTransitionModel.INNERID_SEPERATOR_OLD)) {
            parentId = childId.substring(0, childId.indexOf(OperatorTransitionModel.INNERID_SEPERATOR_OLD));
        }

        return parentId;
    }

    /**
     * @param simulations     The simulationsarray which comes from the pnml-file
     * @param currentPetrinet The new PetriNetModelProcessor of the petrinet we restore at
     *                        the moment
     */
    private boolean importSimulations(SimulationType[] simulations, PetriNetModelProcessor currentPetrinet) throws Exception {
        boolean savedFlag = true;
        SimulationModel currSimulation;
        TransitionModel currTransition;
        String currSimulationID;
        int greatestSimulationIDnumber = 0;
        for (int k = 0; k < simulations.length; k++) {
            // collect the information about the current simulation in local
            // variables
            currSimulationID = simulations[k].getId();
            simulations[k].getSimulationdate();
            OccuredtransitionType[] occuredTransitions = simulations[k].getTransitionsequence().getOccuredtransitionArray();
            Vector<TransitionModel> currentTransitions = new Vector<TransitionModel>();
            String currTransitionID = null;
            String currTransitionName = null;
            String arcSource, arcTarget;
            for (int l = 0; l < occuredTransitions.length; l++) {
                currTransition = null;
                currTransitionID = occuredTransitions[l].getTransitionID();
                if (currTransitionID.charAt(0) == 'a') {
                    // for XOR-transitions the simulation has to fire arcs
                    // instead of simpletransitions
                    // because of the inner representation of van der
                    // Aalst-operators.
                    // Therefore a virtual helper-transition with the arcs' ID
                    // and a special name is created.

                    arcSource = currentPetrinet.getElementContainer().getArcById(currTransitionID).getSourceId();
                    arcTarget = currentPetrinet.getElementContainer().getArcById(currTransitionID).getTargetId();
                    if (arcSource.charAt(0) == 't') {
                        currTransitionName = arcSource + " -> (" + arcTarget + ")";
                    } else {
                        currTransitionName = "(" + arcSource + ") -> " + arcTarget;
                    }
                    CreationMap map = CreationMap.createMap();
                    map.setId(currTransitionID);
                    currTransition = new TransitionModel(map);
                    currTransition.setNameValue(currTransitionName);
                    LoggerManager.debug(Constants.FILE_LOGGER, " ... Simulation: HelperTransition for arc (" + currTransitionID + ") created");
                } else {
                    currTransition = (TransitionModel) currentPetrinet.getElementContainer().getElementById(currTransitionID);
                }
                currentTransitions.add(currTransition);
            }
            currSimulation = new SimulationModel(currSimulationID, simulations[k].getSimulationname(), currentTransitions, simulations[k].getNetFingerprint(), simulations[k].getSimulationdate().getTime());
            // check if current fingerprint of the net equals the imported one
            // if not ask the user if he want's to keep the simulation
            //
            // this check is performed as well on:
            // - fileexport
            // - loading a simulation
            // when you change it here please do at those locations as well
            int answer = 0;
            if (!currentPetrinet.isLogicalFingerprintEqual(simulations[k].getNetFingerprint())) {
                Object[] options = {Messages.getString("Tokengame.ChangedNetDialog.ButtonKeep"), Messages.getString("Tokengame.ChangedNetDialog.ButtonDelete")};
                // get the localized message text
                String message = Messages.getString("Tokengame.ChangedNetDialog.Import.Message");
                // fill the message text dynamically with the simulationname and
                // simulationdate
                message = message.replaceAll("##SIMULATIONNAME##", simulations[k].getSimulationname());
                message = message.replaceAll("##SIMULATIONDATE##", DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, ConfigurationManager.getConfiguration().getLocale()).format(simulations[k].getSimulationdate().getTime()));

                answer = JOptionPane.showOptionDialog(null, message, Messages.getString("Tokengame.ChangedNetDialog.Title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                // if the user didn't choose one of the buttons but closed the
                // OptionDialog don't drop the simulation
                if (answer == -1) {
                    answer = 0;
                }
            }
            if (answer == 0) {
                currentPetrinet.addSimulation(currSimulation);
                LoggerManager.debug(Constants.FILE_LOGGER, " ... Simulation (ID:" + currSimulationID + ")imported");
            } else {
                // set the petrinet opened to "not saved...."
                savedFlag = false;
                LoggerManager.debug(Constants.FILE_LOGGER, " ... Simulation (ID:" + currSimulationID + ") dropped by user");
            }

            try {
                int currSimulationIdNumber = Integer.parseInt(currSimulationID.substring(1));
                // select largest simulation-id number to correctly set the
                // simulationcounter
                if (currSimulationIdNumber > greatestSimulationIDnumber) {
                    greatestSimulationIDnumber = currSimulationIdNumber;
                }
            } catch (NumberFormatException nfe) {
                // if a simulationID doesn't have a correct format (correct =
                // 's1', 's13',...) increment
                // the counter by one to avoid ID-conflicts. In the worst case
                // this only leads to an unused id
                greatestSimulationIDnumber++;
                LoggerManager.debug(Constants.FILE_LOGGER, "WARNING - INVALID SIMULATION-ID FOUND: found a malformed simulation-id (ID: " + currSimulationID + ")");
            }
        }
        currentPetrinet.setSimulationCounter(greatestSimulationIDnumber);
        return savedFlag;
    }

    /**
     * Checks if the element with the given id is an valid operator.
     * <p>
     * The method checks, if the id exists and if the element is an instance of {@link OperatorTransitionModel}.
     *
     * @param elementContainer the container which should contain the element.
     * @param elementId        the id of the element to check.
     * @return true if the element is an valid operator, otherwise false
     */
    private boolean isValidOperator(ModelElementContainer elementContainer, String elementId) {
        return elementId != null && elementContainer.getElementById(elementId) != null && elementContainer.getElementById(elementId).getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE;
    }

    /**
     * @return Returns the mediator.
     */
    protected AbstractApplicationMediator getMediator() {
        return mediator;
    }
}