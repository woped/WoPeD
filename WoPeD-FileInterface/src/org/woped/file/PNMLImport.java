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
import org.woped.core.controller.IEditor;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.IntPair;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.ModelElementFactory;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.EditorLayoutInfo;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.model.petrinet.SimulationModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.VisualController;
import org.woped.editor.controller.WoPeDUndoManager;
import org.woped.editor.controller.bpel.Assign;
import org.woped.editor.controller.bpel.Empty;
import org.woped.editor.controller.bpel.Invoke;
import org.woped.editor.controller.bpel.Receive;
import org.woped.editor.controller.bpel.Reply;
import org.woped.editor.controller.bpel.Wait;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.pnml.ArcType;
import org.woped.pnml.DimensionType;
import org.woped.pnml.GraphicsSimpleType;
import org.woped.pnml.NetType;
import org.woped.pnml.NodeType;
import org.woped.pnml.OccuredtransitionType;
import org.woped.pnml.OrganizationUnitType;
import org.woped.pnml.PlaceType;
import org.woped.pnml.PnmlDocument;
import org.woped.pnml.PnmlType;
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
import org.woped.pnml.TransitionType;
import org.woped.pnml.NetType.Page;
import org.woped.translations.Messages;

// TODO: BUG in import. When import toolspec mit splitjoin. import ONLY one arc
// !!!
/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * The Import Class parses an <code>PetriNet </code> defined in PNML Format. To
 * get more Information about PNML look at <br>
 * <a
 * href="http://www.informatik.hu-berlin.de/top/pnml">http://www.informatik.hu-berlin.de/top/pnml
 * </a> <br>
 * <br>
 * In Order to parse the extended Notation (WF-Nets from W.v.d.Aalst) its
 * necessary to read toolspecific elements additionaly. <br>
 * @see org.woped.editor.core.model.PetriNetModelProcessor <br>
 *      <br>
 *      Created on 29.04.2003 <br>
 *      Last change 05.12.2004 (S.Landes) <br>
 */
public class PNMLImport {
	private IEditor[] editor = null;
	private PnmlDocument pnmlDoc = null;
	private XmlOptions opt = new XmlOptions();
	private Vector<String> warnings = new Vector<String>();
	//private IStatusBar[] statusBars = null;
	private ApplicationMediator mediator = null;

	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param statusBar
	 */
	public PNMLImport(ApplicationMediator am) {
		opt.setUseDefaultNamespace();
		mediator = am;
		Map<String, String> map = new HashMap<String, String>();
		map.put("", "pnml.woped.org");
		// Interpret pnml documents according to foreign standardized pnml schema as our own
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
	 * @param filename
	 *            An existing XML file name
	 */
	public boolean run(String absolutePath, boolean showUI) {
		InputStream is;
		try {
			is = new FileInputStream(absolutePath);
			return run(is, showUI);
		} catch (FileNotFoundException e) {
			LoggerManager.warn(Constants.FILE_LOGGER, "File does not exists. "
					+ absolutePath);
			return false;
		}

	}

	public boolean run(InputStream is) {
		return run(is, true);
	}
	
	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @param is
	 * @return
	 */
	public boolean run(InputStream is, boolean showUI) {
		LoggerManager.debug(Constants.FILE_LOGGER,
				"##### START PNML Version (1.3.2) IMPORT #####");

		long begin = System.currentTimeMillis();
		try {
			pnmlDoc = PnmlDocument.Factory.parse(is, opt);
			createEditorFromBeans(showUI);
			if (!warnings.isEmpty()) {
				LoggerManager.warn(Constants.FILE_LOGGER,
						"Imported a not valid PNML.");
				StringBuffer warningStrings = new StringBuffer();
				for (Iterator<String> iter = warnings.iterator(); iter.hasNext();) {
					warningStrings.append(iter.next());
				}
				JOptionPane.showMessageDialog(null, Messages
						.getString("Import.PNML.Text"), Messages
						.getString("Import.PNML.Title"),
						JOptionPane.WARNING_MESSAGE);
			}
			return true;
		} catch (Exception e) { e.printStackTrace();
			// e.printStackTrace();
			LoggerManager
					.warn(
							Constants.FILE_LOGGER,
							"   ... Could parse PNML file. Perhaps OLD PNML file-format. When saving, new pnml file-format will be created.");
			// e.printStackTrace();
			/*
			 * PNMLImport oldPnml = new PNMLImport(); if
			 * (oldPnml.loadExistingInstance(is)) { PetriNet petriNet =
			 * oldPnml.getPetriNet(); // TODO build editor return loadSuccess =
			 * true; } else { return loadSuccess = false; }
			 */
			// return
			return false;
		} finally {
			LoggerManager.debug(Constants.FILE_LOGGER,
					"##### END PNML IMPORT Version (1.3.2) ##### ("
							+ (System.currentTimeMillis() - begin) + " ms)");
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
	 * @param content -
	 *            Content of PNML File
	 * @return
	 */
	public boolean runEx(String content, boolean showUI) {
		LoggerManager.debug(Constants.FILE_LOGGER,
				"##### START PNML Version (1.3.2) IMPORT #####");

		long begin = System.currentTimeMillis();
		try {
			pnmlDoc = PnmlDocument.Factory.parse(content, opt);
			createEditorFromBeans(showUI);
			if (!warnings.isEmpty()) {
				LoggerManager.warn(Constants.FILE_LOGGER,
						"Imported a not valid PNML.");
				StringBuffer warningStrings = new StringBuffer();
				for (Iterator<String> iter = warnings.iterator(); iter.hasNext();) {
					warningStrings.append(iter.next());
				}
				JOptionPane.showMessageDialog(null, Messages
						.getString("Import.PNML.Text"), Messages
						.getString("Import.PNML.Title"),
						JOptionPane.WARNING_MESSAGE);
			}
			return true;
		} catch (Exception e) {
			LoggerManager
					.warn(
							Constants.FILE_LOGGER,
							"   ... Could parse PNML file. Perhaps OLD PNML file-format. When saving, new pnml file-format will be created.");
			// return
			return false;
		} finally {
			LoggerManager.debug(Constants.FILE_LOGGER,
					"##### END PNML IMPORT Version (1.3.2) ##### ("
							+ (System.currentTimeMillis() - begin) + " ms)");
		}

	}

	private void createEditorFromBeans(boolean showUI) throws Exception {
		importNets(pnmlDoc.getPnml(), showUI);
	}


	/**
	 * TODO: DOCUMENTATION (silenco)
	 * 
	 * @return
	 */
	public IEditor[] getEditor() {
		return editor;
	}

	private void importNets(PnmlType pnml, boolean showUI) throws Exception {
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
			if(showUI){
				if (((WoPeDUndoManager) editor[i].getGraph().getUndoManager()) != null) {

				((WoPeDUndoManager) editor[i].getGraph().getUndoManager())
						.setEnabled(false);
			}
			}
			currentPetrinet = ((PetriNetModelProcessor) editor[i]
					.getModelProcessor());
			// attr. id
			currentPetrinet.setId(currentNet.getId());
			// attr. type
			// Type no other types
			// name
			if (currentNet.isSetName()) {
				currentPetrinet.setName(currentNet.getName().getText());
			}
			if(showUI) if (ConfigurationManager.getConfiguration().isImportToolspecific()) {
				// toolspecific
				for (int j = 0; j < currentNet.getToolspecificArray().length; j++) {
					if (currentNet.getToolspecificArray(j).getTool().equals(
							"WoPeD")) {
						if (currentNet.getToolspecificArray(j).isSetBounds()) {
							dim = new Dimension(currentNet
									.getToolspecificArray(j).getBounds()
									.getDimension().getX().intValue(),
									currentNet.getToolspecificArray(j)
											.getBounds().getDimension().getY()
											.intValue());
							location = new Point(currentNet
									.getToolspecificArray(j).getBounds()
									.getPosition().getX().intValue(),
									currentNet.getToolspecificArray(j)
											.getBounds().getPosition().getY()
											.intValue());
							if (editor[i] instanceof EditorVC) {
								// Pass read layout information on to the editor
								EditorLayoutInfo layout = new EditorLayoutInfo();
								layout.setSavedSize(dim);
								layout.setSavedLocation(location);
								// try to import the type of Layout (false if vertical)
								
								((EditorVC) editor[i]).setRotateSelected(currentNet
										.getToolspecificArray(j)
										.getVerticalLayout());
								
									//for importing a vertical net to change the rotate-button	
									if (currentNet
												.getToolspecificArray(j)
												.getVerticalLayout()== true){
											//EditorVC.setRotateSelected(true);
										VisualController.getInstance().propertyChange(new PropertyChangeEvent(this, "Import", null, null));
											
										
										// Update the UI representation
											//EditorVC.getGraph().updateUI();	
										}
								// Only if also the remaining information is
								// available,
								// try to import the width of the tree view and the height of the overview panel
									if (currentNet.getToolspecificArray(j).isSetTreeWidth()){
										GraphicsSimpleType bounds = currentNet.getToolspecificArray(j).getBounds();
										DimensionType dimension = bounds.getDimension();
										int x =(int) (dimension.getX()).doubleValue();
										x = x - currentNet.getToolspecificArray(j).getTreeWidth();
										layout.setTreeViewWidthRight(x);
									} else if (currentNet.getToolspecificArray(j).isSetTreeWidthRight()){
										layout.setTreeViewWidthRight(currentNet.getToolspecificArray(j).getTreeWidthRight());
									}
									else {
										layout.setTreeViewWidthRight(currentNet
												.getToolspecificArray(j).getBounds()
												.getDimension().getX().intValue() - 100);										
									}
									
									if(currentNet.getToolspecificArray(j).isSetOverviewPanelVisible()){
										layout.setOverviewPanelVisible(currentNet.getToolspecificArray(j).getOverviewPanelVisible());
									}else{
										layout.setOverviewPanelVisible(true);
									}
									
									if (currentNet.getToolspecificArray(j).isSetTreeHeightOverview()){
										if(currentNet.getToolspecificArray(j).getTreeHeightOverview() < 1){
											layout.setTreeHeightOverview(100);
										}else{
										layout.setTreeHeightOverview(currentNet.getToolspecificArray(j).getTreeHeightOverview());
										}
									}else{
										layout.setTreeHeightOverview(100);
									}
									
									if(currentNet.getToolspecificArray(j).isSetTreePanelVisible()){

										layout.setTreePanelVisible(currentNet.getToolspecificArray(j).getTreePanelVisible());
											
									}else{
										layout.setTreePanelVisible(true);
									}
									
									((EditorVC) editor[i]).getEditorPanel().setSavedLayoutInfo(layout);
								}
							}
						if (currentNet.getToolspecificArray(j).isSetResources()) {
							// ResourceMapType resourceMap =
							// currentNet.getToolspecificArray(j).getResources().getResourceMap();
							ResourceMappingType[] resourceMaps = currentNet
									.getToolspecificArray(j).getResources()
									.getResourceMappingArray();

							RoleType[] roles = currentNet.getToolspecificArray(
									j).getResources().getRoleArray();
							ResourceClassModel roleModelTemp;
							for (int k = 0; k < roles.length; k++) {
								roleModelTemp = new ResourceClassModel(roles[k]
										.getName(),
										ResourceClassModel.TYPE_ROLE);
								currentPetrinet.addRole(roleModelTemp);
								SuperModelType[] supermodels = roles[k].getSuperModelArray();
								ResourceClassModel superModelTemp;
								for( int l=0;l<supermodels.length;l++){
									superModelTemp = new ResourceClassModel(supermodels[l]
									     .getName(),ResourceClassModel.TYPE_ROLE);
									roleModelTemp.addSuperModel(superModelTemp);
								}
							}
							 
							

							OrganizationUnitType[] units = currentNet
									.getToolspecificArray(j).getResources()
									.getOrganizationUnitArray();
							ResourceClassModel orgUnitTemp;
							for (int l = 0; l < units.length; l++) {
								orgUnitTemp = new ResourceClassModel(units[l]
										.getName(),
										ResourceClassModel.TYPE_ORGUNIT);
								currentPetrinet.addOrgUnit(orgUnitTemp);
								
								SuperModelType[] supermodels = units[l].getSuperModelArray();
								ResourceClassModel superModelTemp;
								for( int m=0;m<supermodels.length;m++){
									superModelTemp = new ResourceClassModel(supermodels[m]
									     .getName(),ResourceClassModel.TYPE_ORGUNIT);
									orgUnitTemp.addSuperModel(superModelTemp);
								}
							}

							ResourceType[] resources = currentNet
									.getToolspecificArray(j).getResources()
									.getResourceArray();
							ResourceModel resourceModelTemp;
							for (int m = 0; m < resources.length; m++) {
								resourceModelTemp = new ResourceModel(
										resources[m].getName());
								currentPetrinet.addResource(resourceModelTemp);
							}

							for (int n = 0; n < resourceMaps.length; n++) {
								currentPetrinet.addResourceMapping(
										resourceMaps[n].getResourceClass(),
										resourceMaps[n].getResourceID());
							}
						}
						if (currentNet.getToolspecificArray(j)
								.isSetSimulations()) {
							// only save the simulationsarray to local variable
							// here - the import itself
							// has to be done after import of the transitions
							// because the simulation
							// references transitions (which otherwise result in
							// 'null')
							// see "importSimulations([...])" below
							simulations = currentNet.getToolspecificArray(j)
									.getSimulations().getSimulationArray();
						}

						if (currentNet.getToolspecificArray(j)
								.isSetPartnerLinks()) {
							TPartnerLinks plist = currentNet
									.getToolspecificArray(j).getPartnerLinks();
							for (int x = 0; x < plist.sizeOfPartnerLinkArray(); x++) {
								TPartnerLink link = plist
										.getPartnerLinkArray(x);
								if (link.isSetMyRole()
										&& link.isSetPartnerRole()) {
									currentPetrinet.getElementContainer()
											.addPartnerLink(
													link.getName(),
													link.getPartnerLinkType()
															.getNamespaceURI(),
													link.getPartnerLinkType()
															.getLocalPart(),
													link.getPartnerRole(),
													link.getMyRole(),
													link.getWSDL());
								} else if (link.isSetMyRole()) {
									currentPetrinet.getElementContainer()
											.addPartnerLinkWithoutPartnerRole(
													link.getName(),
													link.getPartnerLinkType()
															.getNamespaceURI(),
													link.getPartnerLinkType()
															.getLocalPart(),
													link.getMyRole(),
													link.getWSDL());
								} else if (link.isSetPartnerRole()) {
									currentPetrinet.getElementContainer()
											.addPartnerLinkWithoutMyRole(
													link.getName(),
													link.getPartnerLinkType()
															.getNamespaceURI(),
													link.getPartnerLinkType()
															.getLocalPart(),
													link.getPartnerRole(),
													link.getWSDL());
								}
							}
						}

						if (currentNet.getToolspecificArray(j).isSetVariables()) {
							TVariables vlist = currentNet.getToolspecificArray(
									j).getVariables();
							for (int x = 0; x < vlist.sizeOfVariableArray(); x++) {
								TVariable var = vlist.getVariableArray(x);
								currentPetrinet.getElementContainer()
										.addVariable(var.getName(),
												var.getType().getLocalPart());
							}
						}
					} else {
						currentPetrinet.addUnknownToolSpecs(currentNet
								.getToolspecificArray(j));
					}
				}

			}

			// Import the net into the current ModelElementContainer
			importNet(currentNet, editor[i].getModelProcessor()
					.getElementContainer());

			// Import the simulations if any exist
			if (simulations != null) {
				savedFlag = importSimulations(simulations, currentPetrinet);
			}

			// Now build the graph from the ModelElementContainer
			if(showUI){
				getEditor()[i].getGraph().drawNet(editor[i].getModelProcessor());
				getEditor()[i].updateNet();
	
				getEditor()[i].getGraph().clearSelection();
				if (editor[i].getGraph().getUndoManager() != null) {
					((WoPeDUndoManager) editor[i].getGraph().getUndoManager())
							.setEnabled(true);
				}
				getEditor()[i].updateNet();
				getEditor()[i].setSaved(savedFlag);
			}
		}
	}

	// ! Import the specified net into the specified ModelElementContainer
	// ! This is a rather new approach of importing a net which does not depend
	// ! on an actual editor window to be open
	// ! (Note that for sub-processes we do not have such a windows)
	// ! @param currentNet specifies the source XMLBean for the current net
	// ! @param currentContainer specifies the ModelElementContainer that will
	// receive all the places,
	// ! transitions and arcs from the net stored in the XMLBean
	private void importNet(NetType currentNet,
			ModelElementContainer currentContainer) throws Exception {
		if ((currentNet.getPlaceArray().length==0)&&
			(currentNet.getTransitionArray().length==0)&&
			(currentNet.getPageArray().length==1))
			importNet(currentNet.getPageArray()[0], currentContainer);
		else
		{
			importPlaces(currentNet.getPlaceArray(), currentContainer);
			importTransitions(currentNet, currentContainer);
			// important... import arcs in the end
			importArcs(currentNet.getArcArray(), currentContainer);
		}
	}
	
	private void importNameAndLayout(NodeType node, CreationMap target)
	{
		target.setId(node.getId());
		if (node.getGraphics()!=null)
		{
			target.setPosition(node.getGraphics().getPosition().getX()
					.intValue(), node.getGraphics().getPosition()
					.getY().intValue());
			if (node.getGraphics().isSetDimension())
				target.setSize(new IntPair(new Dimension(node
				                                             .getGraphics().getDimension().getX().intValue(),
				                                             node.getGraphics().getDimension().getY()
				                                             .intValue())));
		}
		if (node.getName()!=null)				
			target.setName(node.getName().getText());
		else
			// Elements that don't have a name will have their id used instead
			target.setName(node.getId());
		
		if (node.getName()!=null
				&& node.getName().isSetGraphics()
				&& node.getName().getGraphics()
						.getOffsetArray() != null
				&& node.getName().getGraphics()
						.getOffsetArray().length > 0) {
			int x = node.getName().getGraphics().getOffsetArray(0)
					.getX().intValue();
			int y = node.getName().getGraphics().getOffsetArray(0)
					.getY().intValue();
			target.setNamePosition(x, y);		
		}		
	}

	private void importPlaces(PlaceType[] places,
			ModelElementContainer currentContainer) throws Exception {
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
					if (places[i].isSetInitialMarking()
							&& (tokens = Integer.parseInt(places[i]
									.getInitialMarking().getText())) > 0) {
						map.setTokens(tokens);
					}
					// toolspecific
					if (ConfigurationManager.getConfiguration()
							.isImportToolspecific()) {
						for (int j = 0; j < places[i].getToolspecificArray().length; j++) {
							if (places[i].getToolspecificArray(j).getTool()
									.equals("WoPeD")) {
								switch (places[i].getToolspecificArray(j)
										.getOperator().getType()) {
								case OperatorTransitionModel.XOR_SPLITJOIN_TYPE:
								case OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE:
								case OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE:
									doNOTcreate = true;
									break;
								default:
								}
								;
							} else {
								map.addUnknownToolSpec(places[i]
										.getToolspecificArray(j));
							}
						}
					}
				} catch (Exception e) {
					warnings
							.add("- PLACE LOST INFORMATION ("
									+ places[i].getId()
									+ ") Exception while importing lesser important information.\n");
				}
				if (!doNOTcreate) {
					// Seems like we're supposed to actually create this place
					// (A reason *not* to do so would be e.g. that the place was
					// part of the inner
					// workings of an XOR split-join)
					// Create the element using the element factory and the
					// already configured creation map and add it to the model
					// element container
					AbstractPetriNetElementModel element = ModelElementFactory
							.createModelElement(map);
					currentContainer.addElement(element);
				}
				doNOTcreate = false;
				LoggerManager.debug(Constants.FILE_LOGGER, "   ... Place (ID:"
						+ places[i].getId() + ") imported");
			} catch (Exception e) {
				warnings
						.add("- SKIP PLACE: Exception while importing important information.\n");
			}
			// increaseCurrent();

		}
	}

	private void importTransitions(NetType currentNet,
			ModelElementContainer currentContainer) throws Exception {
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
					if (ConfigurationManager.getConfiguration()
							.isImportToolspecific()) {
						for (int j = 0; j < transitions[i]
								.getToolspecificArray().length; j++) {
							if (transitions[i].getToolspecificArray(j)
									.getTool().equals("WoPeD")) {
								if (transitions[i].getToolspecificArray(j)
										.isSetTime()) {
									map.setTransitionTime(transitions[i]
											.getToolspecificArray(j).getTime());
								}
								if (transitions[i].getToolspecificArray(j)
										.isSetTimeUnit()) {
									map.setTransitionTimeUnit(transitions[i]
											.getToolspecificArray(j)
											.getTimeUnit());
								}
								
								//set operatorOrientation
								if (transitions[i].getToolspecificArray(j)
										.isSetOrientation()) {
									map.setOperatorPosition(transitions[i]
											.getToolspecificArray(j)
											.getOrientation());
								} 
								if (transitions[i].getToolspecificArray(j)
										.isSetOperator()) {
									map.setOperatorType(transitions[i]
											.getToolspecificArray(j)
											.getOperator().getType());
									map
											.setType(TransitionModel.TRANS_OPERATOR_TYPE);
									map.setId(transitions[i]
											.getToolspecificArray(j)
											.getOperator().getId());
								} else if (transitions[i].getToolspecificArray(
										j).isSetSubprocess()) {
									map.setType(AbstractPetriNetElementModel.SUBP_TYPE);
								}
								if (transitions[i].getToolspecificArray(j)
										.isSetTrigger()) {
									map.setTriggerType(transitions[i]
											.getToolspecificArray(j)
											.getTrigger().getType());
									map.setTriggerPosition(transitions[i]
											.getToolspecificArray(j)
											.getTrigger().getGraphics()
											.getPosition().getX().intValue(),
											transitions[i]
													.getToolspecificArray(j)
													.getTrigger().getGraphics()
													.getPosition().getY()
													.intValue());
								}
								if (transitions[i].getToolspecificArray(j)
										.isSetTransitionResource()) {
									// &&
									// transitions[i].getToolspecificArray(j).getTrigger().getType()
									// == 200
									map.setResourceOrgUnit(transitions[i]
											.getToolspecificArray(j)
											.getTransitionResource()
											.getOrganizationalUnitName());
									map.setResourceRole(transitions[i]
											.getToolspecificArray(j)
											.getTransitionResource()
											.getRoleName());
									map.setResourcePosition(transitions[i]
											.getToolspecificArray(j)
											.getTransitionResource()
											.getGraphics().getPosition().getX()
											.intValue(), transitions[i]
											.getToolspecificArray(j)
											.getTransitionResource()
											.getGraphics().getPosition().getY()
											.intValue());
								}
								if (transitions[i].getToolspecificArray(j)
										.isSetEmpty()) {
									TEmpty empty = transitions[i]
											.getToolspecificArray(j).getEmpty();
									Empty bpel = new Empty(empty.getName());
									map.setBpeldata(bpel);
								}
								if (transitions[i].getToolspecificArray(j)
										.isSetAssign()) {
									TAssign assign = transitions[i]
											.getToolspecificArray(j)
											.getAssign();
									Assign bpel = new Assign(assign.getName(),
											assign.getCopyArray(0).getFrom()
													.getVariable(), assign
													.getCopyArray(0).getTo()
													.getVariable());
									map.setBpeldata(bpel);
								}
								if (transitions[i].getToolspecificArray(j)
										.isSetInvoke()) {
									TInvoke invoke = transitions[i]
											.getToolspecificArray(j)
											.getInvoke();
									Invoke bpel = new Invoke(invoke.getName(),
											invoke.getPartnerLink(), invoke
													.getOperation(), "", invoke
													.getInputVariable(), invoke
													.getOutputVariable());
									map.setBpeldata(bpel);
								}
								if (transitions[i].getToolspecificArray(j)
										.isSetReceive()) {
									TReceive receive = transitions[i]
											.getToolspecificArray(j)
											.getReceive();
									Receive bpel = new Receive(receive
											.getName(), receive
											.getPartnerLink(), receive
											.getOperation(), receive
											.getVariable());
									map.setBpeldata(bpel);
								}
								if (transitions[i].getToolspecificArray(j)
										.isSetReply()) {
									TReply reply = transitions[i]
											.getToolspecificArray(j).getReply();
									Reply bpel = new Reply(reply.getName(),
											reply.getPartnerLink(), reply
													.getOperation(), reply
													.getVariable());
									map.setBpeldata(bpel);
								}
								if (transitions[i].getToolspecificArray(j)
										.isSetWait()) {
									TWait wait = transitions[i]
											.getToolspecificArray(j).getWait();
									Wait bpel = new Wait(wait.getName(), wait
											.getTyp(), wait.getYear(), wait
											.getMonth(), wait.getDay(), wait
											.getHour(), wait.getMinute(), wait
											.getSecond());
									map.setBpeldata(bpel);
								}
							} else {

								map.addUnknownToolSpec(transitions[i]
										.getToolspecificArray(j));
							}
						}
					}
				} catch (Exception e) {
					warnings
							.add("- TRANSITION LOST INFORMATION ("
									+ transitions[i].getId()
									+ "): Exception while importing lesser important information.");
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
					LoggerManager.debug(Constants.FILE_LOGGER,
							" ... Transition (ID:" + map.getId() + ")imported");
					// increaseCurrent();

					if (element.getType() == AbstractPetriNetElementModel.SUBP_TYPE) {
						// The element we just created is a sub-process element
						// Get the corresponding page from the PNML document
						// and restore the sub-process elements by recursively
						// calling importNet()
						Page pages[] = currentNet.getPageArray();
						if (pages != null) {
							for (int currentPage = 0; currentPage < pages.length; ++currentPage) {
								if (pages[currentPage].getId().equals(
										element.getId())) {
									// Only one sub-process net per page may be
									// defined for now
									// Ignore the rest
									NetType subProcessNets[] = pages[currentPage]
											.getNetArray();
									if (subProcessNets.length > 0) {
										if (subProcessNets.length > 1)
											warnings
													.add("- SKIP SUBPROCESS NET: Only one sub-process net may be defined per sub-process.");

										NetType subProcessNet = subProcessNets[0];
										ModelElementContainer container = ((SubProcessModel) element)
												.getSimpleTransContainer();

										importNet(subProcessNet, container);

										// Now see whether we have any
										// tool-specific information
										// attached to the sub-net
										// This might contain layout information
										// for the sub-process editor
										for (int j = 0; j < subProcessNet
												.getToolspecificArray().length; ++j)
											if (subProcessNet
													.getToolspecificArray(j)
													.getTool().equals("WoPeD")) {
												if (subProcessNet
														.getToolspecificArray(j)
														.isSetBounds()) {
													Dimension dim = new Dimension(
															subProcessNet
																	.getToolspecificArray(
																			j)
																	.getBounds()
																	.getDimension()
																	.getX()
																	.intValue(),
															subProcessNet
																	.getToolspecificArray(
																			j)
																	.getBounds()
																	.getDimension()
																	.getY()
																	.intValue());
													Point location = new Point(
															subProcessNet
																	.getToolspecificArray(
																			j)
																	.getBounds()
																	.getPosition()
																	.getX()
																	.intValue(),
															subProcessNet
																	.getToolspecificArray(
																			j)
																	.getBounds()
																	.getPosition()
																	.getY()
																	.intValue());
													EditorLayoutInfo layout = new EditorLayoutInfo();

													layout.setSavedSize(dim);
													layout
															.setSavedLocation(location);
													// Only if also the
													// remaining information is
													// available,
													// try to import the width
													// of the tree view
													if (subProcessNet.getToolspecificArray(j).isSetTreeWidth()){
														layout.setTreeViewWidthRight(subProcessNet.getToolspecificArray(j).getBounds()
																		.getDimension().getX().intValue() - subProcessNet.getToolspecificArray(j).getTreeWidth());
													}
													
													if (subProcessNet.getToolspecificArray(j).isSetTreeWidthRight()){
														layout.setTreeViewWidthRight(subProcessNet.getToolspecificArray(j).getTreeWidthRight());
													}
														
													if(subProcessNet.getToolspecificArray(j).isSetOverviewPanelVisible()){
														layout.setOverviewPanelVisible(subProcessNet.getToolspecificArray(j).getOverviewPanelVisible());
													}else{
														layout.setOverviewPanelVisible(true);
													}
													
													if(subProcessNet.getToolspecificArray(j).isSetTreeHeightOverview()){
														layout.setTreeHeightOverview(subProcessNet.getToolspecificArray(j).getTreeHeightOverview());
												
													}
													
													if(subProcessNet.getToolspecificArray(j).isSetTreePanelVisible()){
														layout.setTreePanelVisible(subProcessNet.getToolspecificArray(j).getTreePanelVisible());
													}else{
														layout.setTreePanelVisible(true);
													}
													
													container.setEditorLayoutInfo(layout);
												}
											}
									}
								}
							}
						} else
							warnings
									.add("- EXPECTED PAGE NOT FOUND: Subprocess information lost during import.");
					}
				}
			} catch (Exception e) {
				warnings
						.add("- SKIP TRANSITION: Exception while importing important information.");
			}
		}
	}

	private void importArcs(ArcType[] arcs,
			ModelElementContainer currentContainer) throws Exception {
		// The model element processor is the only object that knows how to
		// properly connect
		// petri-net model elements, taking into account inner transitions
		// for operators
		// So we will be using it here to instantiate the serialized arcs
		PetriNetModelProcessor processor = new PetriNetModelProcessor();
		processor.setElementContainer(currentContainer);

		for (int i = 0; i < arcs.length; i++) {
			AbstractPetriNetElementModel currentSourceModel = null;
			AbstractPetriNetElementModel currentTargetModel = null;
			ArcModel arc = null;

			try {
				currentSourceModel = (AbstractPetriNetElementModel) currentContainer
						.getElementById(arcs[i].getSource());
				currentTargetModel = (AbstractPetriNetElementModel) currentContainer
						.getElementById(arcs[i].getTarget());
				String tempID;

				if (ConfigurationManager.getConfiguration()
						.isImportToolspecific()) {
					try {
						if (currentTargetModel == null
								&& currentSourceModel != null) {
							// if the current arcs target is an operator this
							// block is executed
							if (arcs[i].getTarget().indexOf(
									OperatorTransitionModel.INNERID_SEPERATOR) != 0) {
								tempID = arcs[i]
										.getTarget()
										.substring(
												0,
												arcs[i]
														.getTarget()
														.indexOf(
																OperatorTransitionModel.OPERATOR_SEPERATOR_TRANSITION));
							} else {
								tempID = arcs[i]
										.getTarget()
										.substring(
												0,
												arcs[i]
														.getTarget()
														.indexOf(
																OperatorTransitionModel.INNERID_SEPERATOR_OLD));
							}
							if (isOperator(currentContainer, tempID)) {
								String sourceId = arcs[i].getSource();
								String targetId = tempID;
								arc = processor.createArc(arcs[i].getId(),
										sourceId, targetId, new Point2D[0],
										true);
							}
						}
						if (currentSourceModel == null
								&& currentTargetModel != null) {
							// if the current arcs source is an operator this
							// block is executed
							if (arcs[i].getSource().indexOf(
									OperatorTransitionModel.INNERID_SEPERATOR) != 0) {
								tempID = arcs[i]
										.getSource()
										.substring(
												0,
												arcs[i]
														.getSource()
														.indexOf(
																OperatorTransitionModel.OPERATOR_SEPERATOR_TRANSITION));
							} else {
								tempID = arcs[i]
										.getSource()
										.substring(
												0,
												arcs[i]
														.getSource()
														.indexOf(
																OperatorTransitionModel.INNERID_SEPERATOR_OLD));
							}

							if (isOperator(currentContainer, tempID)) {
								String sourceId = tempID;
								String targetId = arcs[i].getTarget();
								arc = processor.createArc(arcs[i].getId(),
										sourceId, targetId, new Point2D[0],
										true);
							}
						}
						if (currentTargetModel != null
								&& currentSourceModel != null) {
							// in the easiest case if the imported arc isn't
							// connected to an operator this block is executed
							String sourceId = arcs[i].getSource();
							String targetId = arcs[i].getTarget();
							arc = processor.createArc(arcs[i].getId(),
									sourceId, targetId, new Point2D[0], true);
						}
						if (arc != null) {
							// Check whether we actually have an arc. If not, it
							// does not make sense
							// to import any tool-specific information.
							// Note that internal arcs with no connection to any
							// outer nodes
							// will not be imported, they will be implicitly
							// re-generated,
							// e.g. XOR Split-Join
							// Import toolspecific information for the arc
							for (int j = 0; j < arcs[i].getToolspecificArray().length; j++) {
								if ((arcs[i].getToolspecificArray(j).getTool() != null)
										&& (arcs[i].getToolspecificArray(j)
												.getTool().equals("WoPeD"))) {
									if (arcs[i].getToolspecificArray(j)
											.isSetRoute()
											&& arcs[i].getToolspecificArray(j)
													.getRoute())
										arc.setRoute(true);
									if (arcs[i].getToolspecificArray(j)
											.isSetProbability())
										arc.setProbability(arcs[i]
												.getToolspecificArray(j)
												.getProbability());
									if (arcs[i].getToolspecificArray(j)
											.isSetDisplayProbabilityOn())
										arc.setDisplayOn(arcs[i]
												.getToolspecificArray(j)
												.getDisplayProbabilityOn());
									if (arcs[i].getToolspecificArray(j)
											.isSetDisplayProbabilityPosition()) {
										Point location = new Point(
												arcs[i]
														.getToolspecificArray(j)
														.getDisplayProbabilityPosition()
														.getX().intValue(),
												arcs[i]
														.getToolspecificArray(j)
														.getDisplayProbabilityPosition()
														.getY().intValue());
										arc.setLabelPosition(location);
									}
								} else {
									arc.addUnknownToolSpecs(arcs[i]
											.getToolspecificArray(j));
								}
							}
						}
					} catch (Exception e) {
						warnings
								.add("- ARC LOST INFORMATION ("
										+ arcs[i].getId()
										+ "): Exception while importing lesser important information.");
					}
				} else {
					// if toolspecific import is disabled then import the arc
					// "as is" (and don't replace transitions with toolspecific
					// operators)
					String sourceId = arcs[i].getSource();
					String targetId = arcs[i].getTarget();
					arc = processor.createArc(arcs[i].getId(), sourceId,
							targetId, new Point2D[0], true);
				}
				if (arcs[i].isSetGraphics() && arc != null) {
					// Create two standard points that need to be always present
					// When created using the editor, those points are added
					// automatically
					// by the view
					// but if we want to restore arc points from the PNML file
					// we have to
					// add those points manually
					arc.addPoint(arc.getAttributes().createPoint(10, 10));
					arc.addPoint(arc.getAttributes().createPoint(20, 20));

					for (int j = 0; j < arcs[i].getGraphics()
							.getPositionArray().length; j++) {
						arc.addPoint(new Point2D.Double(arcs[i].getGraphics()
								.getPositionArray(j).getX().doubleValue(),
								arcs[i].getGraphics().getPositionArray(j)
										.getY().doubleValue()), j + 1);
					}
				}
				LoggerManager.debug(Constants.FILE_LOGGER, " ... Arc (ID:"
						+ arcs[i].getId() + "( " + arcs[i].getSource() + " -> "
						+ arcs[i].getTarget() + ") created");
				// increaseCurrent();
			} catch (Exception e) {
				warnings
						.add("- SKIP ARC: Exception while importing important information.");
			}
		}
	}

	/**
	 * @param simulations
	 *            The simulationsarray which comes from the pnml-file
	 * @param currentPetrinet
	 *            The new PetriNetModelProcessor of the petrinet we restore at
	 *            the moment
	 */
	private boolean importSimulations(SimulationType[] simulations,
			PetriNetModelProcessor currentPetrinet) throws Exception {
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
			OccuredtransitionType[] occuredTransitions = simulations[k]
					.getTransitionsequence().getOccuredtransitionArray();
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

					arcSource = currentPetrinet.getElementContainer()
							.getArcById(currTransitionID).getSourceId();
					arcTarget = currentPetrinet.getElementContainer()
							.getArcById(currTransitionID).getTargetId();
					if (arcSource.charAt(0) == 't') {
						currTransitionName = arcSource + " -> (" + arcTarget
								+ ")";
					} else {
						currTransitionName = "(" + arcSource + ") -> "
								+ arcTarget;
					}
					CreationMap map = CreationMap.createMap();
					map.setId(currTransitionID);
					currTransition = new TransitionModel(map);
					currTransition.setNameValue(currTransitionName);
					LoggerManager.debug(Constants.FILE_LOGGER,
							" ... Simulation: HelperTransition for arc ("
									+ currTransitionID + ") created");
				} else {
					currTransition = (TransitionModel) currentPetrinet
							.getElementContainer().getElementById(
									currTransitionID);
				}
				currentTransitions.add(currTransition);
			}
			currSimulation = new SimulationModel(currSimulationID,
					simulations[k].getSimulationname(), currentTransitions,
					simulations[k].getNetFingerprint(), simulations[k]
							.getSimulationdate().getTime());
			// check if current fingerprint of the net equals the imported one
			// if not ask the user if he want's to keep the simulation
			//
			// this check is performed as well on:
			// - fileexport
			// - loading a simulation
			// when you change it here please do at those locations as well
			int answer = 0;
			if (!currentPetrinet.isLogicalFingerprintEqual(simulations[k]
					.getNetFingerprint())) {
				Object[] options = {
						Messages
								.getString("Tokengame.ChangedNetDialog.ButtonKeep"),
						Messages
								.getString("Tokengame.ChangedNetDialog.ButtonDelete") };
				// get the localized message text
				String message = Messages
						.getString("Tokengame.ChangedNetDialog.Import.Message");
				// fill the message text dynamically with the simulationname and
				// simulationdate
				message = message.replaceAll("##SIMULATIONNAME##",
						simulations[k].getSimulationname());
				message = message.replaceAll("##SIMULATIONDATE##", DateFormat
						.getDateTimeInstance(
								DateFormat.MEDIUM,
								DateFormat.MEDIUM,
								ConfigurationManager.getConfiguration()
										.getLocale()).format(
								simulations[k].getSimulationdate().getTime()));

				answer = JOptionPane.showOptionDialog(null, message, Messages
						.getString("Tokengame.ChangedNetDialog.Title"),
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
						null, options, options[0]);
				// if the user didn't choose one of the buttons but closed the
				// OptionDialog don't drop the simulation
				if (answer == -1) {
					answer = 0;
				}
			}
			if (answer == 0) {
				currentPetrinet.addSimulation(currSimulation);
				LoggerManager
						.debug(Constants.FILE_LOGGER, " ... Simulation (ID:"
								+ currSimulationID + ")imported");
			} else {
				// set the petrinet opened to "not saved...."
				savedFlag = false;
				LoggerManager.debug(Constants.FILE_LOGGER,
						" ... Simulation (ID:" + currSimulationID
								+ ") dropped by user");
			}

			try {
				int currSimulationIdNumber = Integer.parseInt(currSimulationID
						.substring(1));
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
				LoggerManager.debug(Constants.FILE_LOGGER,
						"WARNING - INVALID SIMULATION-ID FOUND: found a malformed simulation-id (ID: "
								+ currSimulationID + ")");
			}
		}
		currentPetrinet.setSimulationCounter(greatestSimulationIDnumber);
		return savedFlag;
	}

	private boolean isOperator(ModelElementContainer elementContainer,
			String elementId) throws Exception {
		if (elementId != null
				&& elementContainer.getElementById(elementId) != null
				&& elementContainer.getElementById(elementId).getType() == AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return Returns the mediator.
	 */
	protected ApplicationMediator getMediator() {
		return mediator;
	}
}