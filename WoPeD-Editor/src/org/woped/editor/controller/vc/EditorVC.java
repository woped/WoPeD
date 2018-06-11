package org.woped.editor.controller.vc;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.*;
import org.woped.editor.action.EditorViewEvent;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.*;
import org.woped.core.controller.IViewListener;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.*;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Platform;
import org.woped.core.utilities.Utils;
import org.woped.editor.Constants;
import org.woped.editor.controller.*;
import org.woped.editor.graphbeautifier.AdvancedDialog;
import org.woped.editor.graphbeautifier.SGYGraph;
import org.woped.editor.gui.IEditorProperties;
import org.woped.editor.view.ViewFactory;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;
import org.woped.qualanalysis.simulation.TokenGameController;
import org.woped.qualanalysis.structure.NetAlgorithms;
import org.woped.qualanalysis.structure.StructuralAnalysis;
import org.woped.qualanalysis.structure.components.ArcConfiguration;
import org.woped.quantana.gui.QuantitativeSimulationDialog;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.List;

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

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         <p>
 *         The Editor is the basic Class for Modelling Perti-Nets. Each Editor
 *         contains one Petri-Net (Model), and one WoPeDGraph (JGraph).
 *         <p>
 *         <p>
 *         Created on 29.04.2003
 */

public class EditorVC implements KeyListener, MouseWheelListener, GraphModelListener, ClipboardOwner, GraphSelectionListener, IEditor, InternalFrameListener {
    public static final String ID_PREFIX = "EDITOR_VC_";
    // zoom
    public static final double MIN_SCALE = 0.2;
    public static final double MAX_SCALE = 5;
    public ViewFactory viewFactory = new ViewFactory(this);
    // ! Store a reference to the application mediator.
    // ! It is used to create a new subprocess editor if required
    public AbstractApplicationMediator m_mediator = null;
    // GRAPHICAL Components
    protected WoPeDJGraph m_graph = null;
    ModelElementContainer tempContainer = null;
    // ModelID for DB
    private int modelid = -1;
    private EditorPanel editorPanel;
    private String id = null;
    private BasicMarqueeHandler marqueehandler;
    private boolean doConfirmation = true;
    private boolean undoSupport;
    // Petri net model
    private PetriNetModelProcessor modelProcessor = null;
    private String m_filepath = null;
    private String m_pathname = null;
    private int m_defaultFileType = -1;
    private boolean copyFlag = true;
    // not needed private boolean m_keyPressed = false;
    private int m_createElementType = -1;
    private boolean m_saved = true;
    // not needed private double m_zoomScale = 1;
    private boolean m_drawingMode = false;
    private boolean m_reachGraphEnabled = false;
    private boolean tokenGameEnabled = false;
    private Point2D m_lastMousePosition = null;
    private PropertyChangeSupport m_propertyChangeSupport = null;
    private EditorClipboard m_clipboard = null;
    private boolean smartEditActive = true;
    private IEditorProperties elementProperties = null;

	// Metrics team variables// Metrics team variables
	//ViewControll
	private Vector<IViewListener> viewListener = new Vector<IViewListener>(1, 3);
private TokenGameController m_tokenGameController;
	/**
     * Invoked after any changes in the net.
	*
	* @see GraphSelectionListener#valueChanged(org.jgraph.event.GraphSelectionEvent)
	*/

	private boolean valueChangedActive = false;

	private QuantitativeSimulationDialog simDlg;

    public EditorVC(String id, EditorClipboard clipboard, boolean undoSupport, AbstractApplicationMediator mediator) {
        this(id, clipboard, undoSupport, mediator, true);
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     *
     * @param clipboard
     */
    public EditorVC(String id, EditorClipboard clipboard, boolean undoSupport, AbstractApplicationMediator mediator, boolean loadUI) {
        this.m_propertyChangeSupport = new PropertyChangeSupport(this);
        this.m_propertyChangeSupport.addPropertyChangeListener(VisualController.getInstance());
        this.m_mediator = mediator;

        this.undoSupport = undoSupport;
        // initialize
        this.m_clipboard = clipboard;

        marqueehandler = new PetriNetMarqueeHandler(this, mediator);
        this.modelProcessor = new PetriNetModelProcessor();
        if ( loadUI )
            this.m_graph = new WoPeDJGraph(new WoPeDJGraphGraphModel(this), marqueehandler, undoSupport ? new WoPeDUndoManager(this) : null, viewFactory);

        editorPanel = new EditorPanel(this, mediator, m_propertyChangeSupport, loadUI);

        this.viewListener = new Vector<IViewListener>();
        this.id = id;
        // Listener for the graph
        if ( loadUI ) {
            getGraph().getSelectionModel().addGraphSelectionListener(VisualController.getInstance());
            getGraph().getSelectionModel().addGraphSelectionListener(this);
            getGraph().getModel().addGraphModelListener(this);
            getGraph().addKeyListener(this);
            getGraph().addMouseWheelListener(this);
            this.m_tokenGameController = new TokenGameController(this, m_propertyChangeSupport);
        }
    }

	/**
     * Empty constructor only for testing purposes.
     */
    public EditorVC() {
        this.m_graph = new WoPeDJGraph(new WoPeDJGraphGraphModel(this), null, null, viewFactory);
    }

	/* ########## ELEMENT CREATION METHODS ########### */

    public void closeEditor() {
        if ( getGraph() == null ) return;
        this.fireViewEvent(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.CLOSE, null));
        clearYourself();
        System.gc();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
    }

    /**
     * Updates the Net. For some changes in the Model it's nessecary to call.
     */
    public void updateNet() {
        long begin = System.currentTimeMillis();
        // Perhaps more needed.
        getGraph().refreshNet();
        getGraph().repaint();

        LoggerManager.debug(Constants.EDITOR_LOGGER, "Net updated. (" + (System.currentTimeMillis() - begin) + " ms)");
    }

    /**
     * Creates a Trigger. Needs the ID of the owner transition and the type of
     * the Trigger.
     *
     * @param map
     */
    public TriggerModel createTrigger(CreationMap map) {
        AbstractPetriNetElementModel transition = getModelProcessor().getElementContainer().getElementById(map.getId());
        if ( transition != null && transition instanceof TransitionModel ) {
            if ( ((TransitionModel) transition).hasTrigger() ) {
                deleteCell(((TransitionModel) transition).getToolSpecific().getTrigger(), true);
            }

			if (transition.getParent() instanceof GroupModel) {
				GroupModel group = (GroupModel) transition.getParent();
				TriggerModel triggerModel =  getModelProcessor()
				.newTrigger(map);

                if ( map.getTriggerPosition() != null ) {
                    triggerModel.setPosition(map.getTriggerPosition());
                } else {
                    if ( isRotateSelected() ) {
                        triggerModel.setPosition(map.getPosition().x - 25, map.getPosition().y + 10);
                    } else {
                        triggerModel.setPosition(map.getPosition().x + 10, map.getPosition().y - 20);
                    }

                }
                ParentMap pm = new ParentMap();
                pm.addEntry(triggerModel, group);
                HashMap<GroupModel, AttributeMap> hm = new HashMap<GroupModel, AttributeMap>();
                hm.put(group, group.getAttributes());

                getGraph().getModel().insert(new Object[]{triggerModel}, hm, null, pm, null);

                return triggerModel;
            }
        }
        return null;
    }

    public TriggerModel createTriggerForPaste(CreationMap map, TransitionModel transition) {

		if (transition != null && transition instanceof TransitionModel) {
			if ( transition.hasTrigger()) {
				deleteCell( transition.getToolSpecific()
						.getTrigger(), true);
			}

			if (transition.getParent() instanceof GroupModel) {
				GroupModel group = (GroupModel) transition.getParent();
				TriggerModel triggerModel =  getModelProcessor()
						.newTrigger(map);

                if ( map.getTriggerPosition() != null ) {
                    triggerModel.setPosition(map.getTriggerPosition());
                } else {
                    if ( isRotateSelected() ) {
                        triggerModel.setPosition(map.getPosition().x - 25, map.getPosition().y + 10);
                    } else {
                        triggerModel.setPosition(map.getPosition().x + 10, map.getPosition().y - 20);
                    }

                }
                ParentMap pm = new ParentMap();
                pm.addEntry(triggerModel, group);
                HashMap<GroupModel, AttributeMap> hm = new HashMap<GroupModel, AttributeMap>();
                hm.put(group, group.getAttributes());

                getGraph().getModel().insert(new Object[]{triggerModel}, hm, null, pm, null);

                return triggerModel;
            }
        }
        return null;
    }

    /**
     * /TODO: documentation
     *
     * @param map
     * @return
     */
    public TransitionResourceModel createTransitionResource(CreationMap map) {
        AbstractPetriNetElementModel transition = getModelProcessor().getElementContainer().getElementById(map.getId());
        if ( transition != null && transition instanceof TransitionModel ) {
            if ( ((TransitionModel) transition).hasResource() ) {
                deleteCell(((TransitionModel) transition).getToolSpecific().getTransResource(), true);
            }

			if (transition.getParent() instanceof GroupModel) {
				GroupModel group = (GroupModel) transition.getParent();
				TransitionResourceModel transResourceModell =  getModelProcessor()
						.newTransResource(map);

                transResourceModell.setPosition(map.getResourcePosition().x, map.getResourcePosition().y);
                ParentMap pm = new ParentMap();
                pm.addEntry(transResourceModell, group);
                HashMap<GroupModel, AttributeMap> hm = new HashMap<GroupModel, AttributeMap>();
                hm.put(group, group.getAttributes());

                getGraph().getModel().insert(new Object[]{transResourceModell}, hm, null, pm, null);

                return transResourceModell;
            }
        }
        return null;
    }

    public GraphCell createElement(int type, int additionalType, Point2D p, boolean doNotEdit) {
        return createElement(type, additionalType, (int) p.getX(), (int) p.getY(), doNotEdit);
    }

    public GraphCell createElement(int type, int additionalType, int x, int y, boolean doNotEdit) {
        CreationMap map = CreationMap.createMap();
        map.setType(type);
        if ( doNotEdit ) {
            map.setEditOnCreation(false);
        }
        if ( type == OperatorTransitionModel.TRANS_OPERATOR_TYPE ) {
            map.setOperatorType(additionalType);
        }
        if ( x != -1 && y != -1 ) {
            map.setPosition(x, y);
        }
        return createElement(map, true, doNotEdit);
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     *
     * @param map
     * @return
     */
    protected GraphCell createElement(CreationMap map, boolean insertIntoCache, boolean editNameTag) {
        if ( map.isValid() ) {
            // Create Element, this will assign a new id if none has been
            // defined
            // This id will be unique even across sub-process borders by
            // prepending the
            // id of the sub-process
            AbstractPetriNetElementModel element = getModelProcessor().createElement(map);

			// ensure that There is an Position
			if (map.getPosition() != null) {
				Point point = new Point(map.getPosition().x,
						map.getPosition().y);
				// map.setPosition(new IntPair());
				element.setPosition(getGraph().snap(point));
			} else if (getLastMousePosition() != null) {
				Point point = new Point(
						(int) ((getLastMousePosition().getX()
								/ getGraph().getScale() - element.getWidth() / 2)),
						(int) ((getLastMousePosition().getY()
								/ getGraph().getScale() - element.getHeight() / 2)));
				// map.setPosition(new IntPair((Point) getGraph().snap(point)));
				element.setPosition(getGraph().snap(point));
			} else {
				map.setPosition(30, 30);
			}
			if (element instanceof AbstractPetriNetElementModel) {
				// Name position
				if (map.getNamePosition() == null) {
					if (isRotateSelected()) {
						 element.getNameModel()
								.setPosition(
										(element.getX() + element.getWidth()),
										(element.getY()) + 1);
					} else {
						 element.getNameModel()
								.setPosition((element.getX() - 1),
										(element.getY() + element.getHeight()));
					}
				} else {
					 element.getNameModel()
							.setPosition(map.getNamePosition().x,
									map.getNamePosition().y);
				}
				if (map.getName() == null) {
					 element.setNameValue(element
							.getId().toString());
				} else {
					 element
							.setNameValue(map.getName());
				}
				if (map.getReadOnly() != null) {
					 element.setReadOnly(map
							.getReadOnly());
				}
				// Grouping
				GroupModel group = getGraph().groupName(element,
						 element.getNameModel());
				group.setUngroupable(false);
				// System.err.println("In createElement Method - the created elemetn"
				// + element.toString());
				group.add(element);
				group.add(element.getNameModel());
				if (insertIntoCache) {
					getGraph().getGraphLayoutCache().insert(group);
				}

                // edit
                if ( editNameTag && ConfigurationManager.getConfiguration().isEditingOnCreation()
                        && map.isEditOnCreation() && isSmartEditActive() ) {
                    getGraph().startEditingAtCell(element.getNameModel());
                }
                getEditorPanel().autoRefreshAnalysisBar();
                return group;
            }

            getEditorPanel().getUnderstandColoring().update();
            return element;
        } else {
            LoggerManager.error(Constants.EDITOR_LOGGER, "Could not create Element. CreationMap is NOT VALID");
            return null;
        }
    }

    /**
     * Creates an Arc. Checks if the requested Arc represents a valid Petri-Net
     * connection.
     *
     * @param source
     * @param target
     * @return ArcModel
     */
    public ArcModel createArc(Port source, Port target) {
        String sourceId = ((AbstractPetriNetElementModel) ((DefaultPort) source).getParent()).getId();
        String targetId = ((AbstractPetriNetElementModel) ((DefaultPort) target).getParent()).getId();
        return createArc(sourceId, targetId);
    }

    /**
     * Creates an Arc. Checks if the requested Arc represents a valid Petri-Net
     * connection.
     *
     * @param sourceId
     * @param targetId
     * @return
     */
    public ArcModel createArc(String sourceId, String targetId) {

        CreationMap map = CreationMap.createMap();
        map.setArcSourceId(sourceId);
        map.setArcTargetId(targetId);
        return createArc(map, true);

    }

    /**
     * TODO: DOCUMENTATION (silenco, xraven)
     *
     * @param map
     * @return
     */
    public ArcModel createArc(CreationMap map, boolean insertIntoCache) {
        ArcModel arc;
        Point2D[] pointArray = map.getArcPoints().toArray(new Point2D[0]);

        arc = createArc(map, pointArray);

        if ( arc == null ) {
            return null;
        }

        getGraph().connect(arc, insertIntoCache);

        getEditorPanel().getUnderstandColoring().update();

        if ( arc != null ) {
            // arc.setRoute(map.isArcRoute());
        }
        getEditorPanel().autoRefreshAnalysisBar();
        return arc;

    }

    ArcModel createArc(CreationMap map, Point2D[] pointArray) {

        ArcModel arc = null;

        String sourceId = map.getArcSourceId();
        String targetId = map.getArcTargetId();

        // CHECK if connection is valid
        if ( isValidConnection(sourceId, targetId) ) {
            if ( connectionExists(sourceId, targetId) ) {
                LoggerManager.debug(Constants.EDITOR_LOGGER, "Connection already exits. Discarded.");
            } else {

                arc = getModelProcessor().createArc(map.getArcId(), sourceId, targetId, pointArray, true);

                arc.setInscriptionValue(map.getArcWeight());
                arc.setWeightLablePosition(map.getArcWeightLabelPosition());

                // Manually copy arc points
                for ( int i = 0; i < pointArray.length; ++i ) {
                    addPointToArc(arc, pointArray[i]);
                }
                // Copy probability state of the creation map
                arc.setProbability(map.getArcProbability());
                arc.displayProbability(map.getDisplayArcProbability());
                // If there is a label position, copy it
                if ( map.getArcLabelPosition() != null ) {
                    arc.setProbabilityLabelPosition(new Point2D.Double(map.getArcLabelPosition().getX(), map.getArcLabelPosition().getY()));
                }
            }
        } else {
            LoggerManager.warn(Constants.EDITOR_LOGGER, "Requested connection (" + sourceId + " -> " + targetId + ") is not valid, did nothing!");
        }

        return arc;
    }

    private boolean connectionExists(String sourceId, String targetId) {
        return getModelProcessor().getElementContainer().hasReference(sourceId, targetId);
    }

    private boolean isValidConnection(String sourceId, String targetId) {
        return getGraph().isValidConnection(getModelProcessor().getElementContainer().getElementById(sourceId), getModelProcessor().getElementContainer().getElementById(targetId));
    }

    public void deleteCells(Object[] toDelete) {
        deleteCells(toDelete, true);
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     *
     * @param toDelete
     * @param withGraph
     */
    public void deleteCells(Object[] toDelete, boolean withGraph) {
        // A single NameModel can't be deleted
        if ( toDelete.length == 1 ) {
            if ( toDelete[0] instanceof GroupModel ) {

            } else {
                if ( toDelete[0] instanceof NameModel ) {
                    return;
                }
            }
        }

        // Expand groups --> result will contain all the group elements themselves
        // plus their content

        LinkedList<Object> toBeProcessed = new LinkedList<Object>(Arrays.asList(toDelete));
        Vector<Object> result = new Vector<Object>();

        while ( !toBeProcessed.isEmpty() ) {
            Object currentElement = toBeProcessed.poll();
            if ( currentElement instanceof GroupModel ) {
                GroupModel tempGroup = (GroupModel) currentElement;
                if ( !tempGroup.isUngroupable() ) {
                    Object cell = tempGroup;
                    while ( cell instanceof GroupModel ) {
                        cell = ((GroupModel) cell).getMainElement();
                    }
                    if ( cell instanceof AbstractPetriNetElementModel && !((AbstractPetriNetElementModel) cell).isReadOnly() ) {
                        result.add(tempGroup);
                        for ( int j = 0; j < tempGroup.getChildCount(); j++ ) {
                            result.add(tempGroup.getChildAt(j));
                        }
                    }
                } else {
                    result.add(currentElement);
                    for ( int j = 0; j < tempGroup.getChildCount(); j++ )
                        toBeProcessed.offer(tempGroup.getChildAt(j));
                }
            } else {
                result.add(currentElement);
            }
        }

        HashSet<Object> uniqueResult = new HashSet<Object>();
        for ( int i = 0; i < result.size(); i++ ) {
            uniqueResult.add(result.get(i));
            if ( result.get(i) instanceof AbstractPetriNetElementModel && ((AbstractPetriNetElementModel) result.get(i)).getPort() != null ) {
                Iterator<?> edges = ((AbstractPetriNetElementModel) result.get(i)).getPort().edges();
                while ( edges.hasNext() ) {
                    uniqueResult.add(edges.next());
                }
            }
        }
        deleteOnlyCells(uniqueResult.toArray(), withGraph);
        getEditorPanel().getUnderstandColoring().update();
        getEditorPanel().autoRefreshAnalysisBar();
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     *
     * @param toDelete
     */
    public void deleteOnlyCells(Object[] toDelete, boolean withGraph) {

        toDelete = Utils.sortArcsFirst(toDelete);
        Vector<Object> allPorts = new Vector<Object>();
        Vector<Object> allCells = new Vector<Object>();
        copyFlag = false;

        if ( doConfirmation ) {
            // Check if SubProcessModell Shall be deleted. Ask for Confirmation!
            for ( int i = 0; i < toDelete.length; i++ ) {
                if ( toDelete[i] instanceof SubProcessModel ) {
                    Object[] options = {Messages.getString("Popup.Confirm.SubProcess.Ok"), Messages.getString("Popup.Confirm.SubProcess.No")};
                    int j = 0;
                    j = JOptionPane.showOptionDialog(null, Messages.getString("Popup.Confirm.SubProcess.Info"), Messages.getString("Popup.Confirm.SubProcess.Warn"), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if ( j == 0 ) break;
                    else return;
                }

            }
        }

        for ( int i = 0; i < toDelete.length; i++ ) {

            if ( toDelete[i] instanceof ArcModel ) {
                allPorts.add(toDelete[i]);
                getModelProcessor().removeArc(((ArcModel) toDelete[i]).getId());
            } else if ( toDelete[i] instanceof TriggerModel ) {
                TransitionModel owner = (TransitionModel) getModelProcessor().getElementContainer().getElementById(((TriggerModel) toDelete[i]).getOwnerId());
                if ( owner != null && owner.getToolSpecific().getTrigger() != null ) {
                    if ( owner.getToolSpecific().getTrigger().getTriggertype() == TriggerModel.TRIGGER_RESOURCE && owner.getToolSpecific().getTransResource() != null ) {
                        owner.getToolSpecific().removeTransResource();
                    }
                    owner.getToolSpecific().removeTrigger();
                }
                allPorts.add(toDelete[i]);
            } else if ( toDelete[i] instanceof TransitionResourceModel ) {
                TransitionModel owner = (TransitionModel) getModelProcessor().getElementContainer().getElementById(((TransitionResourceModel) toDelete[i]).getOwnerId());
                if ( owner != null ) {
                    owner.getToolSpecific().removeTransResource();
                }
                allPorts.add(toDelete[i]);
            } else if ( toDelete[i] instanceof NameModel ) {
                allPorts.add(toDelete[i]);
            } else if ( toDelete[i] instanceof GroupModel ) {
                allPorts.add(toDelete[i]);
            } else if ( toDelete[i] instanceof AbstractPetriNetElementModel ) {

                AbstractPetriNetElementModel element = (AbstractPetriNetElementModel) toDelete[i];
                // if there are trigger, delete their jgraph model
                if ( toDelete[i] instanceof TransitionModel ) {
                    if ( ((TransitionModel) toDelete[i]).getToolSpecific().getTrigger() != null ) {
                        deleteCell(((TransitionModel) getModelProcessor().getElementContainer().getElementById(element.getId())).getToolSpecific().getTrigger(), withGraph);
                    }
                }

                // if there are connected arcs delete their model
                allPorts.add(element.getPort());
                allPorts.add(toDelete[i]);
                getModelProcessor().getElementContainer().removeOnlyElement(element.getId());

            }
        }
        if ( withGraph ) {
            Vector<Object> allDeletedObjects = new Vector<Object>();
            allDeletedObjects.addAll(allPorts);
            allDeletedObjects.addAll(allCells);
            getGraph().getModel().remove(allDeletedObjects.toArray());
        }
        updateNet();
    }

    public void deleteCell(DefaultGraphCell cell) {
        deleteCell(cell, true);
    }

	/*########## ELEMENT MODIFICATION METHODS ########### */

    /**
	 * TODO: DOCUMENTATION (xraven)
	 * 
	 * @param cell
	 */
	public void deleteCell(DefaultGraphCell cell, boolean withGraph) {
		if (cell != null) {
			deleteCells(new Object[] { cell }, withGraph);
		}
	}

    /**
     * Deletes all selected Elements. All connected Arc of selected Elements
     * will be deleted too.
     */
    public void deleteSelection() {
        // Delete atomically
        getGraph().getModel().beginUpdate();
        deleteCells(getGraph().getSelectionCells(), true);
        getGraph().getModel().endUpdate();
    }

    /**
     * Edits the given element. <br>
     * NOTE: Currently only the name of the Element is editable.
     *
     * @param cell
     */
    public void edit(Object cell) {
        if ( (cell instanceof NameModel) || (cell instanceof ArcModel) ) {
            getGraph().startEditingAtCell(cell);
        } else if ( cell instanceof AbstractPetriNetElementModel ) {
            getGraph().startEditingAtCell(((AbstractPetriNetElementModel) cell).getNameModel());
        } else {
            LoggerManager.warn(Constants.EDITOR_LOGGER, "No editing possible.");
        }
    }

    /**
     * Adds a Point to the currently selected Arc at the current MousePosition.
     */
    public void addPointToArc(ArcModel arc, Point2D point) {
        arc.addPoint(point);
        updateArc(arc);
    }

    private void updateArc(ArcModel arc) {
        Map<ArcModel, AttributeMap> map = new HashMap<ArcModel, AttributeMap>();
        map.put(arc, arc.getAttributes());
        getGraph().getModel().edit(map, null, null, null);
    }

    public void addPointToSelectedArc() {
        DefaultGraphCell cell = (DefaultGraphCell) getGraph().getNextCellForLocation(null, getLastMousePosition().getX(), getLastMousePosition().getY());
        if ( cell instanceof ArcModel ) {
            addPointToArc((ArcModel) cell, getGraph().fromScreen(getLastMousePosition()));
        }
    }

    /**
     * Remove the Point closest to the Mouse Position of the currently selected
     * Arc.
     */
    public void removeSelectedPoint() {
        Point2D l = m_lastMousePosition;
        ArcModel arc = (ArcModel) getGraph().getFirstCellForLocation(l.getX(), l.getY());
        arc.removePoint(l);
        getGraph().getModel().insert(new Object[]{arc}, null, null, null, null);
    }

    public void increaseWeightOfSelectedArc() {
        ArcModel arc = getSelectedArc();
        if ( arc == null ) return;

        arc.setInscriptionValue(arc.getInscriptionValue() + 1);
        updateArc(arc);
    }

    public void decreaseWeightOfSelectedArc() {
        ArcModel arc = getSelectedArc();
        if ( arc == null ) return;

        arc.setInscriptionValue(arc.getInscriptionValue() - 1);
        updateArc(arc);
    }

    private ArcModel getSelectedArc() {

        ArcModel selectedArc = null;
        DefaultGraphCell cell = (DefaultGraphCell) getGraph().getNextCellForLocation(null, getLastMousePosition().getX(), getLastMousePosition().getY());

        if ( cell instanceof ArcModel ) {
            selectedArc = (ArcModel) cell;
        }

        return selectedArc;
    }

	/**
	 * TODO: DOCUMENTATION (xraven)
	 */

	public void undo() {
		doConfirmation = false; // Confirmation is done in Undo Handling
		copyFlag = false;
		getGraph().undo();
		doConfirmation = true; // Enable Confirmation again
	}

	/**
	 * TODO: DOCUMENTATION (xraven)
	 */

	public void redo() {
		doConfirmation = false; // Confirmation is done in Redo Handling
		copyFlag = false;
		getGraph().redo();
		doConfirmation = true; // Enable Confirmation again
	}

    /**
     * TODO: DOCUMENTATION (silenco)
     */
    public void copySelection() {
        getGraph().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        long begin = System.currentTimeMillis();

        m_clipboard.clearClipboard();
        m_clipboard.setM_sourceEditor(this);
        // get the Selection
        Object[] cells = getGraph().getSelectionCells();
        // Fill processing queue with selected cells
        LinkedList<Object> toBeProcessed = new LinkedList<Object>(Arrays.asList(cells));
        // Arcs to be added to the clipboard
        LinkedList<ArcModel> clipboardArcs = new LinkedList<ArcModel>();

        while ( !toBeProcessed.isEmpty() ) {
            Object currentElement = toBeProcessed.poll();
            if ( currentElement instanceof GroupModel ) {
                GroupModel tempGroup = (GroupModel) currentElement;
                if ( tempGroup.isUngroupable() ) {
                    // Regular group of multiple elements, add
                    // its elements instead of the group itself
                    for ( int j = 0; j < tempGroup.getChildCount(); j++ )
                        toBeProcessed.offer(tempGroup.getChildAt(j));
                    // Do not process the group itself
                    currentElement = null;
                } else {
                    // The is just the usual group of main element, name label and other stuff.
                    // We are only interested in the main model in this case
                    currentElement = ((GroupModel) currentElement).getMainElement();
                }
            } else if ( currentElement instanceof NameModel ) {
                currentElement = getModelProcessor().getElementContainer().getElementById(((NameModel) currentElement).getOwnerId());
            } else if ( currentElement instanceof TriggerModel ) {
                currentElement = getModelProcessor().getElementContainer().getElementById(((TriggerModel) currentElement).getOwnerId());
            }
            if ( currentElement instanceof ArcModel ) {
                ArcModel tempArc = (ArcModel) currentElement;
                clipboardArcs.add(tempArc);
            }
            if ( currentElement instanceof AbstractPetriNetElementModel ) {
                AbstractPetriNetElementModel tempElement = (AbstractPetriNetElementModel) currentElement;
                // copy the element
                m_clipboard.putElement(tempElement);
            }
        }
        // Add all arcs after all other cells, because those cells need to exist in the clipboard
        // for a connecting arc to be added
        for ( ArcModel currentArc : clipboardArcs ) {
            if ( m_clipboard.containsElement(currentArc.getSourceId()) || m_clipboard.containsElement(currentArc.getTargetId()) ) {
                m_clipboard.putArc(currentArc);
            }
        }
        LoggerManager.debug(Constants.EDITOR_LOGGER, "Elements copied. (" + (System.currentTimeMillis() - begin) + " ms)");
        getGraph().setCursor(Cursor.getDefaultCursor());
    }

    /**
     * calls the paste method with the last remembered mouse position
     */
    public void pasteAtMousePosition() {

        copyFlag = true;
        if ( getLastMousePosition() != null ) paste(getLastMousePosition());
        else paste();
    }

    /**
     * paste selected elements into active frame. if active frame = source
     * frame, paste elements with an offset or to the mouse position (if point
     * is given) if active frame != source frame, paste elements in middle of
     * new frame or at mouse position (if point is given)
     */
    public void paste(Point2D... points) {
        // set cursor to wait
        getGraph().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        // get current time, why??
        long begin = System.currentTimeMillis();
        // get elements from clipboard
        Map<String, CreationMap> pasteElements = m_clipboard.getCopiedElementsList();

        // Start an atomic transaction on the graph (to make paste appear as a
        // single undoable operation
        getGraph().getModel().beginUpdate();

        copyFlag = true;

        Map<String, CreationMap> pasteArcs = m_clipboard.getCopiedArcsList();

		/* delta for Position */
        int deltaX = 20, deltaY = 20;

		/* insert elements */
        CreationMap sourceMap, tempMap = null, currentArcMap;
        HashMap<String, Object> correctedSourceId = new HashMap<String, Object>();
        HashMap<String, Object> correctedTargetId = new HashMap<String, Object>();
        Point currentPosition;
        Point middleOfSelection = null;

        AbstractPetriNetElementModel tempElement;
        String oldElementId;
        Vector<Object> toSelectElements = new Vector<Object>();
        Iterator<String> eleIter = pasteElements.keySet().iterator();

        boolean originalName = true;
        String originalNameStr = null;

        while ( eleIter.hasNext() ) {
            sourceMap = pasteElements.get(eleIter.next());
            tempMap = (CreationMap) sourceMap.clone();
            // position for element
            currentPosition = sourceMap.getPosition();

            // set new delta values if a mouse position is given
            if ( points.length > 0 && middleOfSelection == null ) {
                middleOfSelection = getMiddleOfSelection(pasteElements);

                deltaX = (int) points[0].getX() - middleOfSelection.x - tempMap.getSize().getX1() / 2;
                deltaY = (int) points[0].getY() - middleOfSelection.y - tempMap.getSize().getX2() / 2;
            }

            // set new position
            tempMap.setPosition(currentPosition.x + deltaX, currentPosition.y + deltaY);
            // position for name
            currentPosition = (Point) sourceMap.getNamePosition().clone();
            tempMap.setNamePosition(currentPosition.x + deltaX, currentPosition.y + deltaY);

            // position for trigger
            if ( (currentPosition = sourceMap.getTriggerPosition()) != null ) {
                tempMap.setTriggerPosition(currentPosition.x + deltaX, currentPosition.y + deltaY);
            }

            // copy in another frame than the source frame
            if ( this != m_clipboard.getM_sourceEditor() ) {
                // determine middle of selected elements
                if ( middleOfSelection == null ) {
                    middleOfSelection = getMiddleOfSelection(pasteElements);
                }

                // position for element
                if ( points.length > 0 ) {
                    currentPosition = new Point((int) (((int) points[0].getX() / 2) - tempMap.getSize().getX1() / 2 + tempMap.getPosition().getX() - middleOfSelection.x), (int) (((int) points[0].getY() / 2) - tempMap.getSize().getX2() / 2 + tempMap.getPosition().getY() - middleOfSelection.y));
                } else {
                    currentPosition = new Point((int) ((getEditorPanel().getWidth() / 2) - tempMap.getSize().getX1() / 2 + tempMap.getPosition().getX() - middleOfSelection.x), (int) ((getEditorPanel().getHeight() / 2) - tempMap.getSize().getX2() / 2 + tempMap.getPosition().getY() - middleOfSelection.y));

                }
                tempMap.setPosition(currentPosition.x, currentPosition.y);

                // position for name
                if ( isRotateSelected() ) {
                    tempMap.setNamePosition(currentPosition.x + tempMap.getSize().getX1(), currentPosition.y - 1);
                } else {
                    tempMap.setNamePosition(currentPosition.x - 1, currentPosition.y + tempMap.getSize().getX2());
                }

                if ( isRotateSelected() ) {
                    tempMap.setTriggerPosition(currentPosition.x - 20, currentPosition.y + 7);
                } else {
                    tempMap.setTriggerPosition(currentPosition.x + 7, currentPosition.y - 20);

                }

            }

            // keep the sourceMapID
            oldElementId = sourceMap.getId();
            originalNameStr = sourceMap.getName();
            originalName = isOriginalName(originalNameStr, oldElementId);
            tempMap.setEditOnCreation(false);
            tempMap.setReadOnly(false);
            // set ID to null, to get new ID
            tempMap.setId(null);
            // get tempGroupModel with new ID
            GroupModel tempGroupModel = (GroupModel) (create(tempMap, true));
            // get form the group an AbstractElementModel (extends GraphCell)

            tempElement = tempGroupModel.getMainElement();
            // check if tempElement TransitionModel (PetriNetModelElement)

            if ( tempElement instanceof TransitionModel ) {
                // GroupModel currentTrans = (GroupModel) (create(currentMap));
                // new element exactly transitionModel
                TransitionModel tempTrans = (TransitionModel) tempElement;
                // copy time
                if ( (sourceMap.getTransitionTime() != -1) && (sourceMap.getTransitionTimeUnit() != -1) ) {
                    tempTrans.getToolSpecific().setTime(sourceMap.getTransitionTime());
                    tempTrans.getToolSpecific().setTimeUnit(sourceMap.getTransitionTimeUnit());
                }
                // copy trigger model
                CreationMap map = tempTrans.getCreationMap();
                if ( sourceMap.getTriggerType() != -1 ) {
                    if ( map != null ) {

                        map.setTriggerType(sourceMap.getTriggerType());
                        createTriggerForPaste(map, tempTrans);
                        Point p = tempTrans.getPosition();
                        // Why deleteCell?
                        // deleteCell(tempTrans.getToolSpecific().getTrigger(),
                        // true);
                        map.setTriggerPosition(p.x, p.y);
                        // copy resource model
                        if ( (sourceMap.getResourceOrgUnit() != null) && sourceMap.getResourceRole() != null ) {
                            map.setResourceOrgUnit(sourceMap.getResourceOrgUnit());
                            map.setResourceRole(sourceMap.getResourceRole());
                        }
                    }

                } else map.setType(1);

            }
            /* change arc source/target */
            Iterator<String> arcIter = pasteArcs.keySet().iterator();
            while ( arcIter.hasNext() ) {
                currentArcMap = pasteArcs.get(arcIter.next());
                if ( (this.getEditorPanel().getContainer().getActionMap().get(currentArcMap.getArcSourceId()) != null) || ((currentArcMap.getArcSourceId().equals(oldElementId)) && (!correctedSourceId.containsKey(currentArcMap.getArcId()))) ) {
                    currentArcMap.setArcSourceId(tempElement.getId());
                    correctedSourceId.put(currentArcMap.getArcId(), null);
                }
                if ( (currentArcMap.getArcTargetId().equals(oldElementId)) && (!correctedTargetId.containsKey(currentArcMap.getArcId())) ) {
                    currentArcMap.setArcTargetId(tempElement.getId());
                    correctedTargetId.put(currentArcMap.getArcId(), null);
                }
            }
            /* */
            if ( originalName ) tempElement.setNameValue(tempElement.getId());
            else tempElement.setNameValue(originalNameStr);
            toSelectElements.add(tempElement.getParent());
        }

        pasteArcs(pasteArcs, deltaX, deltaY, toSelectElements);


        // End of atomic graph update
        getGraph().getModel().endUpdate();

        // select the new element
        LoggerManager.debug(Constants.EDITOR_LOGGER, "Elements pasted. (" + (System.currentTimeMillis() - begin) + " ms)");
        checkSubprocessEditors(toSelectElements);
        updateNet();

        getGraph().setSelectionCells(toSelectElements.toArray());
        copySelection();
        getGraph().setCursor(Cursor.getDefaultCursor());
        getEditorPanel().getUnderstandColoring().update();


    }

    void pasteArcs(Map<String, CreationMap> pasteArcs, int deltaX, int deltaY, Vector<Object> toSelectElements) {
        CreationMap currentArcMap;/* insert arcs */
        Iterator<String> arcIter = pasteArcs.keySet().iterator();
        ArcModel tempArc = null;
        Point2D point;

        while ( arcIter.hasNext() ) {
            currentArcMap = pasteArcs.get(arcIter.next());
            tempArc = pasteArc(currentArcMap, deltaX, deltaY);

            if ( tempArc == null ) continue;

            toSelectElements.add(tempArc);
        }
    }

    ArcModel pasteArc(CreationMap arcMap, int deltaX, int deltaY) {

        if ( (arcMap.getArcSourceId() == null) || (arcMap.getArcTargetId()) == null ) {
            return null;
        }

        CreationMap map = (CreationMap) arcMap.clone();

        // FIXME: Leaving the original points in the map leads to a different result.
        // This is because of the 2 standard points added to each arc while drawing.
        map.setArcPoints(new Vector<>());

        ArcModel arc = createArc(map, true);

        for ( Point2D point : arcMap.getArcPoints() ) {
            point.setLocation(point.getX() + deltaX, point.getY() + deltaY);
            arc.addPoint(point);
        }

        return arc;
    }

    private Point getMiddleOfSelection(Map<String, CreationMap> maps) {
        int minX = 999999, minY = 999999, maxX = -1, maxY = -1;

        Iterator<String> eleIterTemp = maps.keySet().iterator();
        CreationMap iterMap;
        while ( eleIterTemp.hasNext() ) {
            iterMap = maps.get(eleIterTemp.next());

            minX = Math.min(minX, iterMap.getPosition().x);
            minY = Math.min(minY, iterMap.getPosition().y);
            maxX = Math.max(maxX, iterMap.getPosition().x);
            maxY = Math.max(maxY, iterMap.getPosition().y);
        }
        return new Point((maxX + minX) / 2, (maxY + minY) / 2);
    }

    /**
     * method to "move" selected elements to another position implemented as
     * create copy of all elements and delete originals afterwards
     */
    public void cutSelection() {
        copyFlag = true;
        if ( getGraph().getSelectionCells().length == 1 && getGraph().getSelectionCell() instanceof ArcModel ) {
            LoggerManager.debug(Constants.EDITOR_LOGGER, "cannot cut arc");
        } else {
            copySelection();
            deleteSelection();
        }
        getEditorPanel().getUnderstandColoring().update();
    }

	/**
	 * Moves all Elementes in the Object-Array <code>toMove</code>
	 * <code>dx</code> in x-direction and <code>dy</code> in y-direction.
	 * 
	 * @param toMove
	 * @param dx
	 * @param dy
	 */
	public void move(Object toMove[], int dx, int dy) {
		move(toMove, dx, dy, null, false);
	}

	/* ########## View and utils methods ########### */private void move(Object toMove[], int dx, int dy,
			HashMap<GraphCell, AttributeMap> changes, boolean isrecursiv) {
		if (changes == null) {
			changes = new HashMap<GraphCell, AttributeMap>();
		}
		for (short i = 0; i < toMove.length; i++) {
			if (toMove[i] instanceof DefaultGraphCell) {
				DefaultGraphCell tempCell = (DefaultGraphCell) toMove[i];
				if (tempCell.getChildCount() > 0) {
					move(tempCell.getChildren().toArray(), dx, dy, changes,
							true);
				}
			}
			if (toMove[i] instanceof GraphCell) {
				GraphCell noGroupElement = (GraphCell) toMove[i];
				AttributeMap tempMap = (AttributeMap) noGroupElement
						.getAttributes().clone();
				AttributeMap newMap = new AttributeMap();
				Rectangle2D bounds = GraphConstants.getBounds(tempMap);
				List<?> points = GraphConstants.getPoints(tempMap);
				if (bounds != null) {
					bounds = new Rectangle((int) bounds.getX() + dx,
							(int) bounds.getY() + dy, (int) bounds.getWidth(),
							(int) bounds.getHeight());
					AttributeMap changeMap = changes.get(noGroupElement);
					if (changeMap == null) {
						changeMap = new AttributeMap();
						changes.put(noGroupElement, changeMap);
					}
					changeMap.applyValue(GraphConstants.BOUNDS, bounds);
					// tempMap.applyValue(GraphConstants.BOUNDS, bounds);
					// noGroupElement.changeAttributes(tempMap);
				}
				if (points != null) {
					Vector<Point2D> newPoints = new Vector<Point2D>();
					Point2D tempPoint;
					for (short k = 0; k < points.size(); k++) {
						if (points.get(k) instanceof PortView) {
							tempPoint = ((PortView) points.get(k))
									.getLocation();
						} else {
							tempPoint = (Point2D) points.get(k);
						}
						if (k == 0 || k == points.size() - 1) {
							newPoints.add(new Point2D.Double(tempPoint.getX(),
									tempPoint.getY()));
						} else {
							newPoints.add(new Point2D.Double(tempPoint.getX()
									+ dx, tempPoint.getY() + dy));
						}
					}
					if (newPoints.size() > 2) {
						GraphConstants.setPoints(newMap, newPoints);
					}
				}
				if (newMap.size() > 0) {
					changes.put(noGroupElement, newMap);
				}

            }
        }
        if ( !isrecursiv ) {
            getGraph().getGraphLayoutCache().edit(changes, null, null, null);
            getGraph().setSelectionCells(toMove);
            updateNet();
        }
    }

	/* ########## View and utils methods ########### */

	/* ########## View and utils methods ########### */

    /**
     * Method scaleNet changes the coordinates of all elements by multiplicating
     * with <code>factor</code>.<br>
     * The values will be converted to int. This method is usefull for nets
     * created with other tools using different cooridates.
     *
     * @param factor
     */
    public void scaleNet(double factor) {

        Iterator<AbstractPetriNetElementModel> iter = getModelProcessor().getElementContainer().getRootElements().iterator();
        AbstractPetriNetElementModel aModel;
        while ( iter.hasNext() ) {
            aModel = iter.next();
            aModel.setPosition((int) (aModel.getX() * factor), (int) (aModel.getY() * factor));
            aModel.getNameModel().setPosition((int) (aModel.getNameModel().getX() * factor), (int) (aModel.getNameModel().getY() * factor));
            if ( aModel instanceof OperatorTransitionModel && ((OperatorTransitionModel) aModel).hasTrigger() ) {
                TriggerModel trigger = ((OperatorTransitionModel) aModel).getToolSpecific().getTrigger();
                trigger.setPosition((int) (trigger.getX() * factor), (int) (trigger.getY() * factor));
            }
        }
    }

    /**
     * Enable TokenGame Mode for this net. <br>
     * In TokenGame-Mode the net is not editable, but you call pefrorm a simple
     * token movements.
     *
     * @see TokenGameController
     */
    public void enableTokenGame() {
        if ( isTokenGameEnabled() ) {
            LoggerManager.error(Constants.EDITOR_LOGGER, "TokenGame already running");
            return;
        }

        LoggerManager.debug(Constants.EDITOR_LOGGER, "START TokenGame");
        tokenGameEnabled = true;
        setDrawingMode(false);
        m_tokenGameController.start();

        m_propertyChangeSupport.firePropertyChange("TokenGameMode", null, null);
    }
	/* ########## View and utils methods ########### */
	/**
	 * Disable TokenGame Mode for this net. <br>
	 * In TokenGame-Mode the net is not editable, but you call pefrorm a simple
	 * token movements.
	 * 
	 * @see TokenGameController
	 */
	public void disableTokenGame() {
		if (!isTokenGameEnabled()) {
			LoggerManager.error(Constants.EDITOR_LOGGER, "TokenGame not running");
			return;
		}
		
		LoggerManager.debug(Constants.EDITOR_LOGGER, "STOP TokenGame");
		tokenGameEnabled = false;
		m_tokenGameController.stop();
		m_mediator.getUi().refreshFocusOnFrames();
		
		m_propertyChangeSupport.firePropertyChange("TokenGameMode", null, null);

	}
	
	/**
	 * Terminates a running token game session this net is part of.
	 * A token game session spans across multiple nets if sub processes exist.
	 * This method will close all sub processes and reset the token game to 
	 * it's initial state, then will disable the token game for the top net
	 */
	public void terminateTokenGameSession() {
		if (!isTokenGameEnabled()) {
			LoggerManager.error(Constants.EDITOR_LOGGER, "TokenGame not running");
			return;
		}
		
		m_tokenGameController.getRemoteControl().terminateTokenGameSession();
	}

	/* ########## LISTENER METHODS ########## */

    /**
     * Toggles the TokenGame Mode. <br>
     * In TokenGame-Mode the net is not editable, but you call pefrorm a simple
     * token movements.
     *
     * @see TokenGameController
     */
    public void toggleTokenGame() {
        if ( isTokenGameEnabled() ) {
            disableTokenGame();
        } else {
            enableTokenGame();
        }
    }

    /**
     * Zooms the net. <br>
     * The factor should be between <code>MIN_SCALE</code> and
     * <code>MAX_SCALE</code>. Multiplicate the factor with 100 to get the
     * percent value. Set <code>absolute</code>=<code>true</code> in order to
     * zoom the to the factor, not by the the factor.
     *
     * @param factor
     * @param absolute
     */
    public void zoom(double factor, boolean absolute) {
        /**
         * scale = Math.max(Math.min(scale, 16), .01);
         *
         *
         * if (graphpad.getCurrentGraph() .getScale() < 8) { //
         * "Zero Length String passed to TextLayout constructor"
         * graphpad.getCurrentDocument() . setScale(getGraph().getScale() * 2);
         * if (getGraph().getSelectionCell() != null)
         * getGraph().scrollCellToVisible
         * (graphpad.getCurrentGraph().getSelectionCell()); }
         *
         *
         */
        getGraph().stopEditing();
        Rectangle2D oldVisRect = getGraph().fromScreen(getEditorPanel().m_scrollPane.getViewport().getViewRect());
        double scale;
        // Check if absolute
        if ( absolute ) {
            scale = factor / 100;
        } else {
            scale = getGraph().getScale() + factor;
        }
        // ste to max resp. min if out of range
        if ( scale < MIN_SCALE ) {
            scale = MIN_SCALE;
        }
        if ( scale > MAX_SCALE ) {
            scale = MAX_SCALE;
        }
        // set scale and move to center of old visible rect

        getGraph().setScale(scale);
        oldVisRect = getGraph().toScreen(oldVisRect);
        Rectangle2D visibleRect = getEditorPanel().m_scrollPane.getViewport().getViewRect();
        Rectangle newVisRect = new Rectangle2D.Double(visibleRect.getX() + oldVisRect.getCenterX() - visibleRect.getCenterX(), visibleRect.getY() + oldVisRect.getCenterY() - visibleRect.getCenterY(), visibleRect.getWidth(), visibleRect.getHeight()).getBounds();
        getGraph().scrollRectToVisible(newVisRect);
        if ( getEditorPanel().m_statusbar != null ) {
            getEditorPanel().m_statusbar.updateStatus();
        }

		fireViewEvent(new EditorViewEvent(this,
				AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.ZOOMED,
				new Double(scale * 100)));
	}



	public void valueChanged(GraphSelectionEvent arg0) {
		if (valueChangedActive) {
			// Do not call ourselves endlessly
			// We have to make a call to setSelectionCells()
			// which once again would trigger this method call
			// This is by design.
			return;
		}

        // Before doing anything else,
        // select all PetriNetModelElement objects
        // in the tree view
        Object cells[] = arg0.getCells();

        // If the selected Cell(s) are any PetriNetModel their respective
        // parents get selected.
        // Elements can only be dragged together with their name.
        ArrayList<Object> addedCells = new ArrayList<Object>();
        for ( int i = 0; i < cells.length; ++i ) {
            if ( arg0.isAddedCell(cells[i]) ) {
                Object toBeAdded = cells[i];
                if ( toBeAdded instanceof AbstractPetriNetElementModel ) {
                    TreeNode parent = ((DefaultGraphCell) toBeAdded).getParent();
                    if ( parent != null ) {
                        toBeAdded = parent;
                    }
                }
                addedCells.add(toBeAdded);
            }
        }
        valueChangedActive = true;
        getGraph().addSelectionCells(addedCells.toArray());
        valueChangedActive = false;
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     */
    public void lostOwnership(Clipboard arg0, Transferable arg1) {
        LoggerManager.debug(Constants.EDITOR_LOGGER, "Lost Ownership");
    }

    /**
     * Invoked after a cell has changed in some way. The vertex/vertices may
     * have changed bounds or altered adjacency, or other attributes have
     * changed that may affect presentation.
     */
    public void graphChanged(GraphModelEvent e) {
        setSaved(false);
        // to enable renaming of simpleTransitions in Operators we have to call
        // the method setNameValue() after changing the name with jgraph-method
        // startEditingAtCell()
        if ( getGraph().getLastEdited() != null ) {
            NameModel nM = getGraph().getLastEdited();
            AbstractPetriNetElementModel aem = getModelProcessor().getElementContainer().getElementById(nM.getOwnerId());
            if ( aem != null ) {
                aem.setNameValue(nM.getNameValue());
            }
            getGraph().setLastEditedNull();
        }
    }

    /**
     * @see KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent e) {
        // setKeyPressed(false);
        getGraph().setCursor(Cursor.getDefaultCursor());
        setDrawingMode(false);
        setCreateElementType(-1);
        smartEditActive = true;
    }

    /**
     * @see KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Shortcuts for the Editor are defined here.
     *
     * @see KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e) {
        smartEditActive = false;
        // Listen for Delete Key Press
        if ( e.getKeyCode() == KeyEvent.VK_DELETE )
        // Execute Remove Action on Delete Key Press
        {
            deleteSelection();
        } else if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
            setDrawingMode(false);
            smartEditActive = false;
            ((AbstractMarqueeHandler) getGraph().getMarqueeHandler()).cancelSmartArcDrawing();
        } else if ( getGraph().getSelectionCells() != null ) {
            // setKeyPressed(true);
            m_createElementType = 0;
            /* TODO: Arrow Key Move */
            if ( e.getKeyCode() == KeyEvent.VK_UP ) {
                move(getGraph().getSelectionCells(), 0, (int) -getGraph().getGridSize());
            } else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
                move(getGraph().getSelectionCells(), 0, (int) +getGraph().getGridSize());
            } else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
                move(getGraph().getSelectionCells(), (int) -getGraph().getGridSize(), 0);
            } else if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
                move(getGraph().getSelectionCells(), (int) getGraph().getGridSize(), 0);
            } else if ( e.getKeyCode() == KeyEvent.VK_MINUS ) {
                zoom(-0.5, false);
            } else if ( e.getKeyCode() == KeyEvent.VK_PLUS ) {
                zoom(+0.5, false);
            } else if ( e.getKeyCode() == KeyEvent.VK_1 ) {
                setCreateElementType(AbstractPetriNetElementModel.PLACE_TYPE);
                setDrawingMode(true);
            } else if ( e.getKeyCode() == KeyEvent.VK_2 ) {
                setCreateElementType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
                setDrawingMode(true);
            } else if ( e.getKeyCode() == KeyEvent.VK_3 ) {
                setCreateElementType(OperatorTransitionModel.AND_SPLIT_TYPE);
                setDrawingMode(true);
            } else if ( e.getKeyCode() == KeyEvent.VK_4 ) {
                setCreateElementType(OperatorTransitionModel.XOR_SPLIT_TYPE);
                setDrawingMode(true);
            } else if ( e.getKeyCode() == KeyEvent.VK_5 ) {
                setCreateElementType(OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);
                setDrawingMode(true);
            } else if ( e.getKeyCode() == KeyEvent.VK_6 ) {
                setCreateElementType(OperatorTransitionModel.AND_JOIN_TYPE);
                setDrawingMode(true);
            } else if ( e.getKeyCode() == KeyEvent.VK_7 ) {
                setCreateElementType(OperatorTransitionModel.XOR_JOIN_TYPE);
                setDrawingMode(true);
            } else if ( e.getKeyCode() == KeyEvent.VK_8 ) {
                setCreateElementType(OperatorTransitionModel.SUBP_TYPE);
                setDrawingMode(true);
            }
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
		if (!Platform.isMac()) {
			if ( notches < 0 ) {
				zoom(+0.1, false);
			} else {
				zoom(-0.1, false);
			}
        }
    }

    public void addViewListener(IViewListener listener) {
        viewListener.addElement(listener);
    }

    public String getId() {
        return id;
    }

    public void removeViewListener(IViewListener listenner) {
        viewListener.removeElement(listenner);
    }

    public int getViewControllerType() {
        return ApplicationMediator.VIEWCONTROLLER_EDITOR;
    }

    /**
     * Fires a ViewEvent to each listener as long as the event is not consumed.
     * The event is also set with a reference to the current listener.
     */
    public final void fireViewEvent(AbstractViewEvent viewevent) {
        if ( viewevent == null ) {
            return;
        }
        Vector<IViewListener> vector;
        synchronized (viewListener) {
            vector = (Vector<IViewListener>) viewListener.clone();
        }
        if ( vector == null ) {
            return;
        }
        int i = vector.size();
        for ( int j = 0; !viewevent.isConsumed() && j < i; j++ ) {
            IViewListener viewlistener = vector.elementAt(j);
            viewevent.setViewListener(viewlistener);
            viewlistener.viewEventPerformed(viewevent);
        }
    }

    /**
     * Calls the algorithms for rotating the view and the elements
     *
     * @deprecated Use {@link org.woped.editor.controller.vc.EditorPanel#rotateLayout(org.woped.editor.controller.vc.EditorVC)} instead
     */
    public void rotateLayout() {
        editorPanel.rotateLayout(this);
    }

    /**
     * rotates the Transition
     */
    public void rotateTransLeft(Object cell) {
        if ( cell instanceof TransitionModel ) {
            getEditorPanel().m_orientation.rotateTransLeft((TransitionModel) cell);

            getGraph().drawNet(getModelProcessor());
            updateNet();
            setSaved(false);
            getGraph().setSelectionCell(cell);
        }
    }

    public void rotateTransRight(Object cell) {
        if ( cell instanceof TransitionModel ) {
            getEditorPanel().m_orientation.rotateTransRight((TransitionModel) cell);

            getGraph().drawNet(getModelProcessor());
            updateNet();
            setSaved(false);
            getGraph().setSelectionCell(cell);
        }
    }

    /**
     * starts the advanced dialog for beautifying the graph
     */
    public void advancedBeautifyDialog() {
        JFrame frame = new JFrame();
        @SuppressWarnings("unused") AdvancedDialog dialog = new AdvancedDialog(frame, this);
    }

    /**
     * Calls the algorithm for layered drawing of the graph
     */
    public void startBeautify(int ixIntervall, int iyIntervall, int counter) {
        // checking WF-Net-Conformity
        StructuralAnalysis sAnalysis = new StructuralAnalysis(this);

        if ( !sAnalysis.isWorkflowNet() ) {
            JOptionPane.showMessageDialog(this.getEditorPanel(), Messages.getString("File.Error.GraphBeautifier.NoNet.Text"));
        } else {
            SGYGraph graph = new SGYGraph(this);
            graph.beautify(counter);
            graph.draw(ixIntervall, iyIntervall);
        }
        // Check the orientation. If the orientation is 'vertical' rotate
        // the graph.
        if ( isRotateSelected() ) {
            getEditorPanel().rotateLayout(this);
            getEditorPanel().setRotateSelected(true);
            getEditorPanel().m_EditorLayoutInfo.setVerticalLayout(true);
        }
    }

    public AbstractApplicationMediator getMediator() {
        return m_mediator;
    }

    public boolean isUndoSupport() {
        return undoSupport;
    }

    public EditorClipboard getM_clipboard() {
        return m_clipboard;
    }

    public Point2D getLastMousePosition() {
        return m_lastMousePosition;
    }

    public void setLastMousePosition(Point2D point) {
        this.m_lastMousePosition = point;
    }

    /**
     * Returns the type of the element, which will be created in drawing mode.
     *
     * @return int
     * @see AbstractPetriNetElementModel for element types
     */
    public int getCreateElementType() {
        return this.m_createElementType;
    }

    /**
     * Sets the type of the element, which will be created in drawing mode.
     *
     * @param createElementType
     * @see AbstractPetriNetElementModel for element types
     */
    public void setCreateElementType(int createElementType) {
        int oldValue = m_createElementType;
        this.m_createElementType = createElementType;

        VisualController.getInstance().propertyChange(new PropertyChangeEvent(m_mediator, "DrawMode", oldValue, createElementType));
    }

    /**
     * Returns if the editor is in tokengame mode.
     *
     * @return true if tokengame mode
     * @see TokenGameController
     */
    public boolean isTokenGameEnabled() {
        return tokenGameEnabled;
    }

    /**
     * Returns the WoPeDJGraph (Graph Controller) USE WITH CARE!
     *
     * @return WoPeDJGraph
     */
    public AbstractGraph getGraph() {
        return m_graph;
    }

    public WoPeDJGraph getWoPeDJGraph() {
        return m_graph;
    }

    /**
     * Returns the default filetype for saving the net.
     *
     * @return int
     */
    public int getDefaultFileType() {
        return m_defaultFileType;
    }

    /**
     * Sets the default filetype for saving the net.
     *
     * @param defaultFileType The defaultFileType to set
     */
    public void setDefaultFileType(int defaultFileType) {
        this.m_defaultFileType = defaultFileType;
    }

    /**
     * Returns the pathname if the net was saved before or was opened from a
     * file.
     *
     * @return String
     */
    public String getPathName() {
        return m_pathname;
    }

    /**
     * Sets the pathname. Should be called when the net was saved in a file.
     *
     * @param pathname The filePath to set
     */
    public void setPathName(String pathname) {
        this.m_pathname = pathname;
	}

    /**
     * Returns the filepath if the net was saved before or was opened from a
     * file.
     *
     * @return String
     */
    public String getFilePath() {
        return m_filepath;
    }

    /**
     * Sets the filepath. Should be called when the net was saved in a file.
     *
     * @param filepath The filePath to set
     */
    public void setFilePath(String filepath) {
        this.m_filepath = filepath;
    }

    /**
     * Returns the saved flag for the editor.
     *
     * @return boolean
     */
    public boolean isSaved() {
        return m_saved;
    }

    /**
     * Sets the saved flag for the editor. true when net was saved, or just
     * loaded.
     *
     * @param savedFlag The savedFlag to set
     */
    public void setSaved(boolean savedFlag) {
        this.m_saved = savedFlag;
        if ( getEditorPanel().m_statusbar != null ) {
            getEditorPanel().m_statusbar.updateStatus();
        }
    }

    /**
     * Returns the drawing mode. If the net is in drawing mode, clicking the
     * left mouse button will draw the Element with the set creation type.
     *
     * @return drawing mode
     * @see EditorVC#getCreateElementType()
     */
    public boolean isDrawingMode() {
        return m_drawingMode;
    }

    public boolean isReachabilityEnabled() {
        return m_reachGraphEnabled;
    }

	public void setReachabilityEnabled(boolean flag) {
        m_reachGraphEnabled = flag;
    }public void setDrawMode(int type, boolean active)
    {
        setDrawingMode(active);
        setCreateElementType(type);
    }

	/**
	 * Sets the drawing mode. If the net is in drawing mode, clicking the left
	 * mouse button will draw the Element with the set creation type.
	 *
	 * @param flag
	 *@see EditorVC#getCreateElementType()
     */
	public void setDrawingMode(boolean flag) {
		m_drawingMode = flag;
		if (flag == false)
			setCreateElementType(-1);

	}

	public boolean isSmartEditActive() {
		return smartEditActive;
	}

    public void setSmartEditActive(boolean smartEditActive) {
        this.smartEditActive = smartEditActive;
    }

    public IEditorProperties getElementProperties() {
        return elementProperties;
    }

    public void setElementProperties(IEditorProperties elementProperties) {
        this.elementProperties = elementProperties;
    }

    public PetriNetModelProcessor getModelProcessor() {
        return modelProcessor;
    }

    /**
     * @param modelProcessor The modelProcessor to set.
     */
    public void setModelProcessor(PetriNetModelProcessor modelProcessor) {
        this.modelProcessor = modelProcessor;
        getGraph().drawNet(modelProcessor);
    }

    @Override
    public String toString() {
        return getName();
    }

    public void registerStatusBar(EditorStatusBarVC statusBar) {
        getEditorPanel().m_statusbar = statusBar;
    }

    public JScrollPane getScrollPane() {
        return getEditorPanel().m_scrollPane;
    }

    public boolean isClipboardEmpty() {
        return getM_clipboard().isEmpty();
    }

    public boolean isSubprocessEditor() {
        return false;
    }

    public void internalFrameActivated(InternalFrameEvent e) {
    }

    public void internalFrameClosed(InternalFrameEvent e) {
    }

	public void internalFrameClosing(InternalFrameEvent e) {}

	public void internalFrameDeactivated(InternalFrameEvent e) {}

	public void internalFrameDeiconified(InternalFrameEvent e) {}

	public void internalFrameIconified(InternalFrameEvent e) {}

    public void internalFrameOpened(InternalFrameEvent e) {
    }


    // ! Open a sub-process for the specified sub process model
    // ! and enable the token game for it
    // ! The source of the sub-process will receive a virtual token for the game
    // ! @param subProcess specifies the sub-process to be opened
    public void openTokenGameSubProcess(SubProcessModel subProcess) {
        EditorVC newEditorWindow = (EditorVC) m_mediator.createSubprocessEditor(true, this, subProcess);
        newEditorWindow.getModelProcessor().getElementContainer();
        IQualanalysisService qualanService = QualAnalysisServiceFactory.createNewQualAnalysisService(newEditorWindow);
        if ( qualanService.getSourcePlaces().size() == 1 ) {
            // Hand an initial token to the sub-process for the token game
            ((PlaceModel) qualanService.getSourcePlaces().iterator().next()).receiveToken();
        }
        // Enable token game mode
        newEditorWindow.toggleTokenGame();
    }

    public GraphCell create(CreationMap map) {
        return create(map, true, true);
    }

    public GraphCell create(CreationMap map, boolean doNotEdit) {
        return create(map, true, doNotEdit);
    }

    public GraphCell create(CreationMap map, boolean insertIntoCache, boolean doNotEdit) {

        if ( map.getArcSourceId() != null ) {// Maybe there should be a
            // ArcCreationMap
            return createArc(map, insertIntoCache);
        } else {

            return createElement(map, insertIntoCache, doNotEdit);
        }
    }

    public GraphCell[] createAll(CreationMap[] maps) {
        Vector<GraphCell> result = new Vector<GraphCell>();
        for ( int i = 0; i < maps.length; i++ ) {
            if ( maps[i] != null ) {
                GraphCell element = create(maps[i], false, true);
                result.add(element);
            }
        }
        GraphCell[] resultArray = result.toArray(new GraphCell[]{});
        getGraph().getGraphLayoutCache().insert(resultArray);
        return resultArray;
    }

    public int getModelid() {
        return modelid;
    }

    public void setModelid(int modelid) {
        this.modelid = modelid;
    }

    /**
     * Get status of understandability coloring
     *
     * @return true if coloring is enabled
     */
    public boolean isUnderstandabilityColoringEnabled() {
        return editorPanel.isUnderstandabilityColoringEnabled();
    }

    /**
     * Set status of understandability coloring
     *
     * @param active true to enable, false to disable
     */
    public void setUnderstandabilityColoringEnabled(boolean active) {
        editorPanel.setUnderstandabilityColoringEnabled(active);
    }

    public void toggleUnderstandColoring() {

        if ( isUnderstandabilityColoringEnabled() ) {
            LoggerManager.debug(Constants.EDITOR_LOGGER, "DEACTIVATE Understandability ");
            getEditorPanel().setUnderstandabilityColoringEnabled(false);
        } else {
            LoggerManager.debug(Constants.EDITOR_LOGGER, "ACTIVATE Understandability ");
            getEditorPanel().setUnderstandabilityColoringEnabled(true);
        }
        getEditorPanel().getUnderstandColoring().update();
        // Update the UI representation
        getGraph().updateUI();
        m_propertyChangeSupport.firePropertyChange("Update", null, null);
    }

    public void setReadOnly(boolean readonly) {
        getGraph().setEnabled(readonly);
        getGraph().enableMarqueehandler(readonly);
        getGraph().clearSelection();
        getEditorPanel().setEnabled(readonly);
        getGraph().setPortsVisible(readonly);
    }

    public QuantitativeSimulationDialog getSimDlg() {
        return simDlg;
    }

    public void setSimDlg(QuantitativeSimulationDialog newVal) {
        simDlg = newVal;
    }

    /**
     * Check whether rotation is selected
     *
     * @return true if rotation is selected
     */
    public boolean isRotateSelected() {
        return editorPanel.isRotateSelected();
    }

    /**
     * Set rotation selection status
     *
     * @param rotateSelected true if rotate should be selected
     */
    public void setRotateSelected(boolean rotateSelected) {
        editorPanel.setRotateSelected(rotateSelected);
    }


    /**
     * Returns whether the T* transition is currently being shown for workflow nets
     *
     * @return true if T* transition is shown
     */
    public boolean isShowingTStar() {
        return editorPanel.isShowingTStar();
    }

    /**
     * Specify whether T* transition should be shown for workflow nets
     *
     * @param tStarEnabled true if T* transition should be shown
     */
    public void setTStarEnabled(boolean tStarEnabled) {
        this.editorPanel.setTStarEnabled(tStarEnabled);
    }

    /**
     * sets on of the Arrows of the mainSplitPane-divider visible or invisible
     * based on window sizes and divider position
     */
    public void checkMainSplitPaneDivider() {
        editorPanel.checkMainSplitPaneDivider();
    }

    protected void clearYourself() {
        ((WoPeDUndoManager) getGraph().getUndoManager()).clear();
        getEditorPanel().getContainer().removeAll();
        getEditorPanel().removeAll();
        getEditorPanel().getParent().remove(this.getEditorPanel());

        ((AbstractMarqueeHandler) marqueehandler).clear();

        getEditorPanel().setComponentOrientation(null);
        m_graph = null;
        getEditorPanel().m_scrollPane = null;
        modelProcessor = null;
        getEditorPanel().m_orientation = null;
        getEditorPanel().m_EditorLayoutInfo = null;
        m_propertyChangeSupport = null;
        m_clipboard = null;
        elementProperties = null;
        viewListener = null;
        getEditorPanel().m_statusbar = null;
        getEditorPanel().m_rightSideTreeView = null;
        getEditorPanel().m_mainSplitPane = null;
        getEditorPanel().mainsplitPaneWithAnalysisBar = null;
        getEditorPanel().m_treeObject = null;
        getEditorPanel().m_treeModel = null;
        getEditorPanel().editorSize = null;
        m_mediator = null;
        getEditorPanel().setUnderstandColoring(null);
    }

    public void checkSubprocessEditors(Vector<Object> selectedElement) {
        Iterator<Object> iterator = selectedElement.iterator();
        GroupModel element;
        ArcConfiguration arcConfig = new ArcConfiguration();
        IEditor subEditor;
        while ( iterator.hasNext() ) {
            try {
                element = (GroupModel) iterator.next();
                if ( element.getMainElement().getType() == AbstractPetriNetElementModel.SUBP_TYPE ) {
                    SubProcessModel subprocess = (SubProcessModel) element.getMainElement();
                    NetAlgorithms.getArcConfiguration(subprocess, arcConfig);
                    IEditor editor = getMediator().getUi().getEditorFocus();
                    if ( (arcConfig.m_numIncoming != 0) || (arcConfig.m_numOutgoing != 0) ) {
                        subEditor = getMediator().createSubprocessEditor(true, editor, (SubProcessModel) element.getMainElement());
                        this.fireViewEvent(new ViewEvent(subEditor, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.CLOSE, null));
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
        }
    }

    public boolean isOriginalName(String name, String id) {
        boolean result = false;
        if ( name.matches(id) ) result = true;
        return result;
    }

    public boolean isCopyFlag() {
        return copyFlag;
    }

    public void setCopyFlag(boolean copyFlag) {
        this.copyFlag = copyFlag;
    }

    public EditorPanel getEditorPanel() {
        return editorPanel;
    }

    /**
     * Sets the editor panel.
     * <p>
     * Used for testing purposes
     *
     * @param panel the new panel to set.
     */
    public void setEditorPanel(EditorPanel panel) {
        this.editorPanel = panel;
    }

    public String getName() {
        return getEditorPanel().getName();
    }

    public void setName(String name) {
        getEditorPanel().setName(name);
    }

    public void hideAnalysisBar() {
        getEditorPanel().hideAnalysisBar();
    }

    public void hideMetricsBar() {
        getEditorPanel().hideMetricsBar();
    }

    public TokenGameController getTokenGameController() {
        return m_tokenGameController;
    }

    public void repaint() {
        getGraph().refreshNet();
        getGraph().repaint();
    }



	public String getPathname() {
		return m_pathname;
	}

	public void setPathname(String absolutePath) {
        m_pathname = absolutePath;

    }@Override
	public void hideP2TBar() {
		editorPanel.hideP2TBar();
	}
	
}