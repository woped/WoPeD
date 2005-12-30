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
package org.woped.editor.controller.vc;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.ParentMap;
import org.jgraph.graph.Port;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.AbstractGraph;
import org.woped.core.controller.AbstractMarqueeHandler;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IEditor;
import org.woped.core.controller.IViewListener;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.AbstractModelProcessor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.IntPair;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.UMLModelProcessor;
import org.woped.core.model.petrinet.GraphTreeModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.NameModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TransitionResourceModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.model.uml.AbstractUMLElementModel;
import org.woped.core.model.uml.ActivityModel;
import org.woped.core.model.uml.StateModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Utils;
import org.woped.editor.Constants;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.EditorClipboard;
import org.woped.editor.controller.PetriNetMarqueeHandler;
import org.woped.editor.controller.UMLMarqueeHandler;
import org.woped.editor.controller.VisualController;
import org.woped.editor.controller.WoPeDJGraph;
import org.woped.editor.controller.WoPeDUndoManager;
import org.woped.editor.gui.IEditorProperties;
import org.woped.editor.simulation.TokenGameController;
import org.woped.editor.utilities.ImageSelection;
import org.woped.editor.view.ViewFactory;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * The Editor is the basic Class for Modelling Perti-Nets. Each Editor contains
 * one Petri-Net (Model), and one WoPeDGraph (JGraph).
 * 
 * 
 * Created on 29.04.2003
 */
public class EditorVC extends JPanel implements KeyListener, GraphModelListener, ClipboardOwner, GraphSelectionListener, IEditor
{

    private String                 id                      = null;
    public static final String     ID_PREFIX               = "EDITOR_VC_";
    private JComponent             container               = null;
    private static ViewFactory     viewFactory             = new ViewFactory();
    // GRAPHICAL Components
    private WoPeDJGraph            m_graph                 = null;
    private JScrollPane            m_scrollPane            = null;

    // Petrinet
    // private PetriNetModelProcessor m_itsPetriNet = null;
    private AbstractModelProcessor modelProcessor          = null;
    private String                 m_filePath              = null;
    private int                    m_defaultFileType       = -1;
    // TokenGame
    private TokenGameController    m_tokenGameController   = null;
    // zoom
    public static final double     MIN_SCALE               = 0.1;
    public static final double     MAX_SCALE               = 5;
    // not nedded private boolean m_keyPressed = false;
    private int                    m_createElementType     = -1;
    private boolean                m_saved                 = true;
    private Dimension              m_savedSize             = null;
    private Point                  m_savedLocation         = null;
    // not needed private double m_zoomScale = 1;
    private boolean                m_drawingMode           = false;
    private boolean                m_tokenGameMode         = false;
    private Point2D                m_lastMousePosition     = null;
    private PropertyChangeSupport  m_propertyChangeSupport = null;
    private EditorClipboard        m_clipboard             = null;
    private boolean                smartEditActive         = true;
    private IEditorProperties      elementProperties       = null;
    // ViewControll
    private Vector                 viewListener            = new Vector(1, 3);

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param clipboard
     */
    public EditorVC(String id, EditorClipboard clipboard, int modelProcessorType, boolean undoSupport)
    {
        // initialize
        this.setLayout(new BorderLayout());
        this.m_clipboard = clipboard;
        BasicMarqueeHandler marqueehandler;
        if (modelProcessorType == AbstractModelProcessor.MODEL_PROCESSOR_PETRINET)
        {
            marqueehandler = new PetriNetMarqueeHandler(this);
            this.modelProcessor = new PetriNetModelProcessor();
            this.m_graph = new WoPeDJGraph(new DefaultGraphModel(), marqueehandler, undoSupport ? new WoPeDUndoManager(this) : null, viewFactory, modelProcessorType);
        } else if (modelProcessorType == AbstractModelProcessor.MODEL_PROCESSOR_UML)
        {
            marqueehandler = new UMLMarqueeHandler(this);
            this.modelProcessor = new UMLModelProcessor();
            this.m_graph = new WoPeDJGraph(new DefaultGraphModel(), marqueehandler, undoSupport ? new WoPeDUndoManager(this) : null, viewFactory, modelProcessorType);
        }
        this.m_propertyChangeSupport = new PropertyChangeSupport(this);
        this.m_propertyChangeSupport.addPropertyChangeListener(VisualController.getInstance());
        this.viewListener = new Vector();
        this.id = id;
        // Listener for the graph
        getGraph().getSelectionModel().addGraphSelectionListener(VisualController.getInstance());
        getGraph().getSelectionModel().addGraphSelectionListener(this);
        getGraph().getModel().addGraphModelListener(this);
        getGraph().addKeyListener(this);
        if (!DefaultStaticConfiguration.ACTIVATE_TREE_VIEW)
        {
            m_scrollPane = new JScrollPane(getGraph());
            add(m_scrollPane);
        } else
        { // Furute Feature with treeview
            GraphTreeModel gtModel = new GraphTreeModel(getGraph().getModel());
            JTree tree = new JTree(gtModel);
            getGraph().getModel().addGraphModelListener(gtModel);
            tree.setRootVisible(false);
            m_scrollPane = new JScrollPane(getGraph());
            JScrollPane sTree = new JScrollPane(tree);
            JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sTree, m_scrollPane);
            split.setDividerLocation(100);
            add(split);
        }
        if (modelProcessorType == AbstractModelProcessor.MODEL_PROCESSOR_PETRINET)
        {
            this.m_tokenGameController = new TokenGameController(((PetriNetModelProcessor) getModelProcessor()), getGraph());
        }
    }

    // IS NOT WORKING YET
    // /**
    // * ATTENTION: Be careful with this !!
    // *
    // * @param graphModel
    // * @param marqueehandler
    // * @param undoManager
    // * @param viewFactory
    // * @param modelProcessorType
    // */
    // public void changeGraph(DefaultGraphModel graphModel, BasicMarqueeHandler
    // marqueehandler, WoPeDUndoManager undoManager, ViewFactory viewFactory,
    // int modelProcessorType)
    // {
    // this.remove(getGraph());
    // this.m_graph = new WoPeDJGraph(graphModel, marqueehandler, undoManager,
    // viewFactory, modelProcessorType);
    // if (modelProcessorType ==
    // AbstractModelProcessor.MODEL_PROCESSOR_PETRINET)
    // {
    // this.modelProcessor = new PetriNetModelProcessor();
    // } else if (modelProcessorType ==
    // AbstractModelProcessor.MODEL_PROCESSOR_UML)
    // {
    // this.modelProcessor = new UMLModelProcessor();
    // }
    // getGraph().getSelectionModel().addGraphSelectionListener(VisualController.getInstance());
    // getGraph().getSelectionModel().addGraphSelectionListener(this);
    // getGraph().getModel().addGraphModelListener(this);
    // getGraph().addKeyListener(this);
    // this.m_scrollPane = new JScrollPane(getGraph());
    // this.add(m_scrollPane);
    // }

    /**
     * Updates the Net. For some changes in the Model it's nessecary to call.
     */
    public void updateNet()
    {
        long begin = System.currentTimeMillis();
        // Perhaps more needed.
        getGraph().refreshNet();
        getGraph().repaint();

        LoggerManager.debug(Constants.EDITOR_LOGGER, "Net updated. (" + (System.currentTimeMillis() - begin) + " ms)");
    }

    /* ########## ELEMENT CREATION METHODS ########### */

    /**
     * Creates a Trigger. Needs the ID of the owner transition and the type of
     * the Trigger.
     * 
     * @param transitionId
     * @param triggertype
     */
    public TriggerModel createTrigger(CreationMap map)
    {
        if (getModelProcessor().getProcessorType() == AbstractModelProcessor.MODEL_PROCESSOR_PETRINET)
        {
            AbstractElementModel transition = getModelProcessor().getElementContainer().getElementById(map.getId());
            if (transition != null && transition instanceof TransitionModel)
            {
                if (((TransitionModel) transition).hasTrigger())
                {
                    deleteCell(((TransitionModel) transition).getToolSpecific().getTrigger(), true);
                }

                if (transition.getParent() instanceof GroupModel)
                {
                    GroupModel group = (GroupModel) transition.getParent();
                    TriggerModel triggerModel = ((PetriNetModelProcessor) getModelProcessor()).newTrigger(map);
                    if (map.getTriggerPosition() == null)
                    {
                        map.setTriggerPosition(transition.getX() + 10, transition.getY() - 20);
                    } else
                    {
                        map.setTriggerPosition(map.getTriggerPosition().getX1() == -1 ? transition.getX() + 10 : map.getTriggerPosition().getX1(), map.getTriggerPosition().getX2() == -1 ? transition
                                .getY() - 20 : map.getTriggerPosition().getX2());
                    }
                    triggerModel.setPosition(map.getTriggerPosition().getX1(), map.getTriggerPosition().getX2());
                    ParentMap pm = new ParentMap();
                    pm.addEntry(triggerModel, group);
                    HashMap hm = new HashMap();
                    hm.put(group, group.getAttributes());

                    getGraph().getModel().insert(new Object[] { triggerModel }, hm, null, pm, null);

                    return triggerModel;
                }
            }
        }
        return null;
    }

    /**
     * /TODO: documentation
     * 
     * @param transition
     * @param transResourceId
     * @return
     */
    public TransitionResourceModel createTransitionResource(CreationMap map)
    {
        AbstractElementModel transition = getModelProcessor().getElementContainer().getElementById(map.getId());
        if (transition != null && transition instanceof TransitionModel)
        {
            if (((TransitionModel) transition).hasResource())
            {
                deleteCell(((TransitionModel) transition).getToolSpecific().getTransResource(), true);
            }

            if (transition.getParent() instanceof GroupModel)
            {
                GroupModel group = (GroupModel) transition.getParent();
                TransitionResourceModel transResourceModell = ((PetriNetModelProcessor) getModelProcessor()).newTransResource(map);
                if (map.getResourcePosition() == null)
                {
                    map.setResourcePosition(transition.getX() - TransitionResourceModel.DEFAULT_WIDTH - 5, transition.getY() - TransitionResourceModel.DEFAULT_HEIGHT - 5);
                } else
                {
                    map.setResourcePosition(map.getResourcePosition().getX1() == -1 ? transition.getX() - 65 : map.getResourcePosition().getX1(), map.getResourcePosition().getX2() == -1 ? transition
                            .getY() - 20 : map.getResourcePosition().getX2());
                }
                transResourceModell.setPosition(map.getResourcePosition().getX1(), map.getResourcePosition().getX2());
                ParentMap pm = new ParentMap();
                pm.addEntry(transResourceModell, group);
                HashMap hm = new HashMap();
                hm.put(group, group.getAttributes());

                getGraph().getModel().insert(new Object[] { transResourceModell }, hm, null, pm, null);

                return transResourceModell;
            }
        }
        return null;
    }

    public AbstractElementModel createElement(int type, int additionalType, Point2D p, boolean doNotEdit)
    {
        return createElement(type, additionalType, (int) p.getX(), (int) p.getY(), doNotEdit);
    }

    public AbstractElementModel createElement(int type, int additionalType, int x, int y, boolean doNotEdit)
    {
        CreationMap map = CreationMap.createMap();
        map.setType(type);
        if (doNotEdit) map.setEditOnCreation(false);
        if (type == OperatorTransitionModel.TRANS_OPERATOR_TYPE || type == AbstractUMLElementModel.OPERATOR_TYPE) map.setOperatorType(additionalType);
        if (type == StateModel.STATE_TYPE) map.setStateType(additionalType);
        if (x != -1 && y != -1) map.setPosition(x, y);
        return createElement(map);
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param map
     * @return
     */
    public AbstractElementModel createElement(CreationMap map)
    {
        if (map.isValid())
        {
            // ensure that There is an Position
            if (map.getPosition() != null)
            {
                Point point = new Point((int) (map.getPosition().getX1() / getGraph().getScale()), (int) (map.getPosition().getX2() / getGraph().getScale()));
                map.setPosition(new IntPair((Point) getGraph().snap(point)));
            } else if (getLastMousePosition() != null)
            {
                Point point = new Point((int) ((getLastMousePosition().getX() - 20) / getGraph().getScale()), (int) ((getLastMousePosition().getY() - 20) / getGraph().getScale()));
                map.setPosition(new IntPair((Point) getGraph().snap(point)));
            } else map.setPosition(10, 10);

            // Create Element
            AbstractElementModel element = getModelProcessor().createElement(map);

            if (element instanceof PetriNetModelElement)
            {
                // Name
                if (map.getNamePosition() == null) map.setNamePosition((int) (element.getX() - 1), (int) (element.getY() + (element.getHeight())));
                if (map.getName() == null) map.setName(element.getId().toString());
                ((PetriNetModelElement) element).getNameModel().setPosition(map.getNamePosition().getX1(), map.getNamePosition().getX2());
                ((PetriNetModelElement) element).setNameValue(map.getName());

                // Grouping
                GroupModel group = getGraph().groupName((PetriNetModelElement) element, ((PetriNetModelElement) element).getNameModel());
                group.setUngroupable(false);
                getGraph().getGraphLayoutCache().insertGroup(group, new Object[] { element, ((PetriNetModelElement) element).getNameModel() });
                if (map.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE || map.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE || map.getType() == PetriNetModelElement.SUBP_TYPE)
                {
                    // Trigger
                    if (map.getTriggerType() != -1) createTrigger(map);
                    if (map.getTriggerPosition() != null) ((TransitionModel) element).getToolSpecific().getTrigger().setPosition(map.getTriggerPosition().getX1(), map.getTriggerPosition().getX2());
                    if (map.getResourceOrgUnit() != null && map.getResourceRole() != null) createTransitionResource(map);
                    if (map.getResourcePosition() != null) ((TransitionModel) element).getToolSpecific().getTransResource().setPosition(map.getResourcePosition().getX1(),
                            map.getResourcePosition().getX2());
                } else if (map.getType() == PetriNetModelElement.PLACE_TYPE)
                {
                    // Tokens
                    if (map.getTokens() > 0) ((PlaceModel) element).setTokens(map.getTokens());
                }
                // edit
                if (ConfigurationManager.getConfiguration().isEditingOnCreation() && map.isEditOnCreation() && isSmartEditActive())
                {
                    getGraph().startEditingAtCell(((PetriNetModelElement) element).getNameModel());
                }
            } else if (element instanceof AbstractUMLElementModel)
            {
                if (element.getType() == AbstractUMLElementModel.ACTIVITY_TYPE && map.getImageIcon() != null)
                {
                    ((ActivityModel) element).setIcon(map.getImageIcon());
                    if (map.getSize() != null)
                    {
                        ((ActivityModel) element).setSize(map.getSize().getX1(), map.getSize().getX2());
                    } else
                    {
                        ((ActivityModel) element).setSize(map.getImageIcon().getIconWidth() + 4, map.getImageIcon().getIconHeight() + 4);
                    }
                }
                getGraph().getGraphLayoutCache().insert(element);
                // edit
                if (ConfigurationManager.getConfiguration().isEditingOnCreation() && map.isEditOnCreation() && isSmartEditActive())
                {
                    getGraph().startEditingAtCell(element);
                }

            }

            // getGraph().getGraphLayoutCache().valueForCellChanged(petriNetElement.getNameModel(),
            // petriNetElement.getNameValue());
            return element;
        } else
        {
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
    public ArcModel createArc(Port source, Port target)
    {
        String sourceId = ((AbstractElementModel) ((DefaultPort) source).getParent()).getId();
        String targetId = ((AbstractElementModel) ((DefaultPort) target).getParent()).getId();
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
    public ArcModel createArc(String sourceId, String targetId)
    {

        CreationMap map = CreationMap.createMap();
        map.setArcSourceId(sourceId);
        map.setArcTargetId(targetId);
        return createArc(map);

    }

    /**
     * TODO: DOCUMENTATION (silenco, xraven)
     * 
     * @param map
     * @return
     */
    public ArcModel createArc(CreationMap map)
    {
        ArcModel arc = null;
        String sourceId = map.getArcSourceId();
        String targetId = map.getArcTargetId();
        List points = map.getArcPoints();
        Point2D[] pointArray = new Point2D[points.size()];
        for (int i = 0; i < points.size(); i++)
        {
            if (points.get(i) instanceof Point2D)
            {
                pointArray[i] = (Point2D) points.get(i);
            }
        }

        // CHECK if connection is valid
        if (getGraph().isValidConnection(getModelProcessor().getElementContainer().getElementById(sourceId), getModelProcessor().getElementContainer().getElementById(targetId)))
        {
            if (!getModelProcessor().getElementContainer().hasReference(sourceId, targetId))
            {
                arc = getModelProcessor().createArc(map.getArcId(), sourceId, targetId, pointArray, true);
                getGraph().connect(arc);
            } else if (getModelProcessor().getElementContainer().getElementById(sourceId).getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE
                    || getModelProcessor().getElementContainer().getElementById(targetId).getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE)
            {
                arc = getModelProcessor().createArc(map.getArcId(), sourceId, targetId, pointArray, true);
                getGraph().connect(arc);
                LoggerManager.debug(Constants.EDITOR_LOGGER, "TODO: check this: " + sourceId + " -> " + targetId + " does already exists. DID IT ANYWAY ");
            } else
            {
                LoggerManager.debug(Constants.EDITOR_LOGGER, "?");
            }
        } else
        {
            LoggerManager.warn(Constants.EDITOR_LOGGER, "Requested connection (" + sourceId + " -> " + targetId + ") is not vaild, did nothing!");
        }

        if (arc != null)
        {
            // arc.setRoute(map.isArcRoute());
        }
        return arc;

    }

    public void deleteCells(Object toDelete[])
    {
        deleteCells(toDelete, true);
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param toDelete
     * @param withGraph
     */
    public void deleteCells(Object toDelete[], boolean withGraph)
    {
        Vector result = new Vector();
        for (int i = 0; i < toDelete.length; i++)
        {
            if (toDelete[i] instanceof GroupModel && !((GroupModel) toDelete[i]).isUngroupable())
            {
                GroupModel tempGroup = (GroupModel) toDelete[i];
                result.add(tempGroup);
                for (int j = 0; j < tempGroup.getChildCount(); j++)
                {
                    result.add(tempGroup.getChildAt(j));
                }
            } else
            {
                result.add(toDelete[i]);
            }
        }
        for (int i = 0; i < result.size(); i++)
        {
            if (result.get(i) instanceof AbstractElementModel && ((AbstractElementModel) result.get(i)).getPort() != null)
            {
                Iterator edges = ((AbstractElementModel) result.get(i)).getPort().edges();
                while (edges.hasNext())
                {
                    result.add(edges.next());
                }
            }
        }
        deleteOnlyCells(result.toArray(), withGraph);

    }

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param toDelete
     */
    public void deleteOnlyCells(Object toDelete[], boolean withGraph)
    {

        toDelete = Utils.sortArcsFirst(toDelete);
        Object tempToDelete[] = new Object[1];
        for (short i = 0; i < toDelete.length; i++)
        {

            if (toDelete[i] instanceof ArcModel)
            {
                getModelProcessor().removeArc(((ArcModel) toDelete[i]).getId());
            } else if (toDelete[i] instanceof TriggerModel)
            {
                TransitionModel owner = (TransitionModel) getModelProcessor().getElementContainer().getElementById(((TriggerModel) toDelete[i]).getOwnerId());
                if (owner != null)
                {
                    if (owner.getToolSpecific().getTrigger().getTriggertype() == TriggerModel.TRIGGER_RESOURCE)
                    {
                        if (owner.getToolSpecific().getTransResource() != null)
                        {
                            owner.getToolSpecific().removeTransResource();
                        }
                    }
                    // getGraph().getModel().remove(new Object[] {
                    // owner.getToolSpecific().getTrigger() });
                    owner.getToolSpecific().removeTrigger();
                }
            } else if (toDelete[i] instanceof TransitionResourceModel)
            {
                TransitionModel owner = (TransitionModel) getModelProcessor().getElementContainer().getElementById(((TransitionResourceModel) toDelete[i]).getOwnerId());
                owner.getToolSpecific().removeTransResource();
            } else if (toDelete[i] instanceof NameModel)
            {
                // toDelete[i] = new DefaultGraphCell();
            } else if (toDelete[i] instanceof GroupModel)
            {
                // Deleteall Elements in the Group
                // deleteCells(((GroupModel)
                // toDelete[i]).getChildren().toArray(), withGraph);
            } else if (toDelete[i] instanceof AbstractElementModel)
            {

                AbstractElementModel element = (AbstractElementModel) toDelete[i];
                // if there are trigger, delete their jgraph model
                if (toDelete[i] instanceof TransitionModel || toDelete[i] instanceof OperatorTransitionModel)
                {
                    if (((TransitionModel) getModelProcessor().getElementContainer().getElementById(element.getId())).getToolSpecific().getTrigger() != null)
                    {
                        deleteCell(((TransitionModel) getModelProcessor().getElementContainer().getElementById(element.getId())).getToolSpecific().getTrigger(), withGraph);
                    }
                }

                // if there are connected arcs delete their model
                if (element.getChildCount() > 0 && false)
                {
                    Set edges = ((DefaultPort) element.getChildAt(0)).getEdges();
                    Iterator edgesIter = edges.iterator();
                    while (edgesIter.hasNext())
                    {
                        tempToDelete[0] = edgesIter.next();
                        getModelProcessor().removeArc(((ArcModel) tempToDelete[0]).getId());
                        if (withGraph) getGraph().getModel().remove(tempToDelete);
                    }
                }

                getModelProcessor().getElementContainer().removeOnlyElement(element.getId());

            }
        }
        if (withGraph) getGraph().getModel().remove(toDelete);
        updateNet();
        getGraph().repaint();
    }

    public void deleteCell(DefaultGraphCell cell)
    {
        deleteCell(cell, true);
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param cell
     */
    public void deleteCell(DefaultGraphCell cell, boolean withGraph)
    {
        if (cell != null)
        {
            deleteCells(new Object[] { cell }, withGraph);
        }
    }

    /**
     * Deletes all selected Elements. All connected Arc of selected Elements
     * will be deleted too.
     */
    public void deleteSelection()
    {
        deleteCells(getGraph().getSelectionCells(), true);
    }

    /* ########## ELEMENT MODIFICATION METHODS ########### */
    /**
     * Edits the given element. <br>
     * NOTE: Currently only the name of the Element is editable.
     * 
     * @param cell
     */
    public void edit(Object cell)
    {
        if ((cell instanceof NameModel) || (cell instanceof ArcModel) || cell instanceof ActivityModel)
        {
            getGraph().startEditingAtCell(cell);
        } else if (cell instanceof PetriNetModelElement)
        {
            getGraph().startEditingAtCell(((PetriNetModelElement) cell).getNameModel());
        } else
        {
            LoggerManager.warn(Constants.EDITOR_LOGGER, "No editing possible.");
        }
    }

    /**
     * Adds a Point to the currently selected Arc at the current MousePosition.
     */
    public void addPointToArc(ArcModel arc, Point2D point)
    {
        arc.addPoint(point);
        Map map = new HashMap();
        map.put(arc, arc.getAttributes());
        getGraph().getModel().edit(map, null, null, null);
    }

    public void addPointToSelectedArc()
    {
        DefaultGraphCell cell = (DefaultGraphCell) getGraph().getNextCellForLocation(null, getLastMousePosition().getX(), getLastMousePosition().getY());
        if (cell instanceof ArcModel)
        {
            addPointToArc((ArcModel) cell, getLastMousePosition());
        }
    }

    /**
     * Remove the Point closest to the Mouse Position of the currently selected
     * Arc.
     */
    public void removeSelectedPoint()
    {
        Point2D l = m_lastMousePosition;
        ArcModel arc = (ArcModel) getGraph().getFirstCellForLocation(l.getX(), l.getY());
        arc.removePoint(l);
        getGraph().getModel().insert(new Object[] { arc }, null, null, null, null);
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     *  
     */
    public void undo()
    {
        getGraph().undo();
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     *  
     */
    public void redo()
    {
        getGraph().redo();
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     */
    public void copySelection()
    {
        getGraph().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        long begin = System.currentTimeMillis();
        // get the Selection
        Object[] cells = getGraph().getSelectionCells();
        m_clipboard.clearClipboard();
        AbstractElementModel tempElement;
        ArcModel tempArc;
        for (int idx = 0; idx < cells.length; idx++)
        {
            if (cells[idx] instanceof GroupModel)
            {
                cells[idx] = ((GroupModel) cells[idx]).getMainElement();
            } else if (cells[idx] instanceof NameModel)
            {
                cells[idx] = getModelProcessor().getElementContainer().getElementById(((NameModel) cells[idx]).getOwnerId());
            } else if (cells[idx] instanceof TriggerModel)
            {
                cells[idx] = getModelProcessor().getElementContainer().getElementById(((TriggerModel) cells[idx]).getOwnerId());
            }
            if (cells[idx] instanceof AbstractElementModel)
            {
                tempElement = (AbstractElementModel) cells[idx];
                // copy the element
                m_clipboard.getCopiedElementsList().put(tempElement.getId(), tempElement.getCreationMap().clone());
                Iterator arcIter = getModelProcessor().getElementContainer().getOutgoingArcs(tempElement.getId()).keySet().iterator();
                /*
                 * copy all the elements arcs TODO: Release 0.9.0 "implicite arc
                 * copy" while (arcIter.hasNext()) { tempArc =
                 * getPetriNet().getElementContainer().getArcById(arcIter.next());
                 * if
                 * (!m_clipboard.getCopiedArcsList().containsKey(tempArc.getId()))
                 * m_clipboard.getCopiedArcsList().put(tempArc.getId(),
                 * tempArc.getCreationMap().clone()); } arcIter =
                 * getPetriNet().getElementContainer().getIncomingArcs(tempElement.getId()).keySet().iterator();
                 * while (arcIter.hasNext()) { tempArc =
                 * getPetriNet().getElementContainer().getArcById(arcIter.next());
                 * if
                 * (!m_clipboard.getCopiedArcsList().containsKey(tempArc.getId()))
                 * m_clipboard.getCopiedArcsList().put(tempArc.getId(),
                 * tempArc.getCreationMap().clone()); }
                 */
            }
        }
        // TODO: delete this in "implicite Arc copy" perhaps in configuration?
        for (int idx = 0; idx < cells.length; idx++)
        {
            if (cells[idx] instanceof ArcModel)
            {
                tempArc = (ArcModel) cells[idx];
                if (m_clipboard.getCopiedElementsList().containsKey(tempArc.getSourceId()) && m_clipboard.getCopiedElementsList().containsKey(tempArc.getTargetId()))
                {
                    m_clipboard.getCopiedArcsList().put(tempArc.getId(), tempArc.getCreationMap().clone());
                }
            }
        }
        LoggerManager.debug(Constants.EDITOR_LOGGER, "Elements copied. (" + (System.currentTimeMillis() - begin) + " ms)");
        getGraph().setCursor(Cursor.getDefaultCursor());
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     *  
     */
    public void paste()
    {
        pasteAtPosition(-1, -1);
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     *  
     */
    public void pasteAtLastMousePosition()
    {
        if (getLastMousePosition() != null)
        {
            pasteAtPosition((int) getLastMousePosition().getX(), (int) getLastMousePosition().getY());
        } else
        {
            LoggerManager.warn(Constants.EDITOR_LOGGER, "No last mouse position found. Elements pasted free will instead.");
            pasteAtPosition(-1, -1);
        }
    }

    /**
     * TODO: DOCUMENTATION (silenco)
     */
    public void pasteAtPosition(int x, int y)
    {
        getGraph().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        long begin = System.currentTimeMillis();
        HashMap pasteElements = (HashMap) m_clipboard.getCopiedElementsList().clone();
        HashMap pasteArcs = (HashMap) m_clipboard.getCopiedArcsList().clone();

        /* find delta for Position */
        int deltaX, deltaY;
        if (x == -1 && y == -1)
        {
            deltaX = 10;
            deltaY = 10;
        } else
        {
            CreationMap leftestElement;
            // find delta
            Iterator eleIter = pasteElements.keySet().iterator();
            leftestElement = (CreationMap) pasteElements.get(eleIter.next());
            CreationMap currentMap;
            while (eleIter.hasNext())
            {
                currentMap = (CreationMap) pasteElements.get(eleIter.next());
                if (leftestElement.getPosition().getX1() > currentMap.getPosition().getX1()) leftestElement = (CreationMap) currentMap.clone();
                else if (leftestElement.getPosition().getX1() == currentMap.getPosition().getX1() && leftestElement.getPosition().getX2() < currentMap.getPosition().getX2()) leftestElement = (CreationMap) currentMap
                        .clone();
            }
            IntPair position = leftestElement.getPosition();
            deltaX = x - position.getX1();
            deltaY = y - position.getX2();
        }

        /* insert elements */
        CreationMap currentMap, currentArcMap;
        HashMap correctedSourceId = new HashMap();
        HashMap correctedTargetId = new HashMap();
        IntPair currentPosition;
        AbstractElementModel tempElement;
        String oldElementId;
        Vector selectElements = new Vector();
        Iterator eleIter = pasteElements.keySet().iterator();
        while (eleIter.hasNext())
        {
            currentMap = (CreationMap) pasteElements.get(eleIter.next());
            // position for element
            currentPosition = currentMap.getPosition();
            currentMap.setPosition(currentPosition.getX1() + deltaX, currentPosition.getX2() + deltaY);
            // position for name
            currentPosition = currentMap.getNamePosition();
            currentMap.setNamePosition(currentPosition.getX1() + deltaX, currentPosition.getX2() + deltaY);
            // position for trigger
            if ((currentPosition = currentMap.getTriggerPosition()) != null)
            {
                currentMap.setTriggerPosition(currentPosition.getX1() + deltaX, currentPosition.getX2() + deltaY);
            }
            oldElementId = currentMap.getId();
            currentMap.setId(null);
            if (ConfigurationManager.getConfiguration().isInsertCOPYwhenCopied()) currentMap.setName(currentMap.getName() + "_copy");
            currentMap.setEditOnCreation(false);
            tempElement = createElement(currentMap);
            /* change arc source/target */
            Iterator arcIter = pasteArcs.keySet().iterator();
            while (arcIter.hasNext())
            {
                currentArcMap = (CreationMap) pasteArcs.get(arcIter.next());
                if (currentArcMap.getArcSourceId().equals(oldElementId) && !correctedSourceId.containsKey(currentArcMap.getArcId()))
                {
                    currentArcMap.setArcSourceId(tempElement.getId());
                    correctedSourceId.put(currentArcMap.getArcId(), null);
                }
                if (currentArcMap.getArcTargetId().equals(oldElementId) && !correctedTargetId.containsKey(currentArcMap.getArcId()))
                {
                    currentArcMap.setArcTargetId(tempElement.getId());
                    correctedTargetId.put(currentArcMap.getArcId(), null);
                }
            }
            /* */
            selectElements.add(tempElement.getParent());
        }

        /* insert arcs */
        Iterator arcIter = pasteArcs.keySet().iterator();
        ArcModel tempArc;
        Point2D point;
        CreationMap cmap = CreationMap.createMap();
        while (arcIter.hasNext())
        {
            currentArcMap = (CreationMap) pasteArcs.get(arcIter.next());
            if (getModelProcessor().getElementContainer().getElementById(currentArcMap.getArcSourceId()) != null
                    && getModelProcessor().getElementContainer().getElementById(currentArcMap.getArcTargetId()) != null)
            {
                cmap.setArcSourceId(currentArcMap.getArcSourceId());
                cmap.setArcTargetId(currentArcMap.getArcTargetId());
                tempArc = createArc(cmap);
                for (short k = 0; k < currentArcMap.getArcPoints().size(); k++)
                {
                    point = (Point2D) ((Point2D) currentArcMap.getArcPoints().get(k)).clone();
                    point.setLocation(point.getX() + deltaX, point.getY() + deltaY);
                    tempArc.addPoint(point);
                }
                selectElements.add(tempArc);
            }
        }
        // select the new element
        LoggerManager.debug(Constants.EDITOR_LOGGER, "Elements pasted. (" + (System.currentTimeMillis() - begin) + " ms)");
        updateNet();
        getGraph().setSelectionCells(selectElements.toArray());
        getGraph().setCursor(Cursor.getDefaultCursor());
    }

    /**
     * TODO: DOCUMENTATION
     */
    public void cutSelection()
    {
        if (getGraph().getSelectionCells().length == 1 && getGraph().getSelectionCell() instanceof ArcModel)
        {
            LoggerManager.debug(Constants.EDITOR_LOGGER, "cannot cut arc");
        } else
        {
            copySelection();
            deleteSelection();
        }
    }

    /**
     * Moves all Elementes in the Object-Array
     * <code>toMove</code> <code>dx</code> in x-direction and
     * <code>dy</code> in y-direction.
     * 
     * @param toMove
     * @param dx
     * @param dy
     */
    public void move(Object toMove[], int dx, int dy)
    {
        move(toMove, dx, dy, null, false);
    }

    private void move(Object toMove[], int dx, int dy, HashMap changes, boolean isrecursiv)
    {
        AbstractElementModel currentModel;
        Object tempMove[];
        if (changes == null)
        {
            changes = new HashMap();
        }
        for (short i = 0; i < toMove.length; i++)
        {
            if (toMove[i] instanceof DefaultGraphCell)
            {
                DefaultGraphCell tempCell = (DefaultGraphCell) toMove[i];
                if (tempCell.getChildCount() > 0)
                {
                    move(tempCell.getChildren().toArray(), dx, dy, changes, true);
                }
            }
            if (toMove[i] instanceof GraphCell)
            {
                GraphCell noGroupElement = (GraphCell) toMove[i];
                AttributeMap tempMap = noGroupElement.getAttributes();
                AttributeMap newMap = new AttributeMap();
                Rectangle2D bounds = GraphConstants.getBounds(tempMap);
                List points = GraphConstants.getPoints(tempMap);
                if (bounds != null)
                {

                    bounds = new Rectangle2D.Double(bounds.getX() + dx, bounds.getY() + dy, bounds.getWidth(), bounds.getHeight());
                    GraphConstants.setBounds(newMap, bounds);
                    // noGroupElement.changeAttributes(tempMap);
                }
                if (points != null)
                {
                    Vector newPoints = new Vector();
                    Point2D tempPoint;
                    for (short k = 0; k < points.size(); k++)
                    {
                        tempPoint = (Point2D) points.get(k);
                        if (k == 0 || k == points.size() - 1)
                        {
                            newPoints.add(new Point2D.Double(tempPoint.getX(), tempPoint.getY()));
                        } else
                        {
                            newPoints.add(new Point2D.Double(tempPoint.getX() + dx, tempPoint.getY() + dy));
                        }
                    }
                    if (newPoints.size() > 2)
                    {
                        GraphConstants.setPoints(newMap, newPoints);
                    }
                }
                if (newMap.size() > 0)
                {
                    changes.put(noGroupElement, newMap);
                }

            }
        }
        if (!isrecursiv)
        {
            getGraph().getGraphLayoutCache().edit(changes, null, null, null);
            getGraph().setSelectionCells(toMove);
            updateNet();
        }
    }

    /**
     * Method scaleNet changes the coordinates of all elements by multiplicating
     * with <code>factor</code>.<br>
     * The values will be converted to int. This method is usefull for nets
     * created with other tools using different cooridates.
     * 
     * @param factor
     */
    public void scaleNet(double factor)
    {

        Iterator iter = getModelProcessor().getElementContainer().getRootElements().iterator();
        PetriNetModelElement aModel;
        while (iter.hasNext())
        {
            aModel = (PetriNetModelElement) iter.next();
            aModel.setPosition((int) (aModel.getX() * factor), (int) (aModel.getY() * factor));
            aModel.getNameModel().setPosition((int) (aModel.getNameModel().getX() * factor), (int) (aModel.getNameModel().getY() * factor));
            if (aModel instanceof OperatorTransitionModel && ((OperatorTransitionModel) aModel).hasTrigger())
            {
                TriggerModel trigger = ((OperatorTransitionModel) aModel).getToolSpecific().getTrigger();
                trigger.setPosition((int) (trigger.getX() * factor), (int) (trigger.getY() * factor));
            }
        }
    }

    /* ########## View and utils methods ########### */
    /**
     * Toggles the TokenGame Mode. <br>
     * In TokenGame-Mode the net is not editable, but you call pefrorm a simple
     * token movements.
     * 
     * @see TokenGameController
     */
    public void toggleTokenGame()
    {
        if (isTokenGameMode())
        {
            LoggerManager.debug(Constants.EDITOR_LOGGER, "STOP TokenGame");
            m_tokenGameMode = false;
            m_tokenGameController.stop();
        } else
        {
            LoggerManager.debug(Constants.EDITOR_LOGGER, "START TokenGame");
            m_tokenGameMode = true;
            setDrawingMode(false);
            m_tokenGameController.start();
        }
        m_propertyChangeSupport.firePropertyChange("TokenGameMode", null, null);
    }

    /**
     * Copies a image of the current to the SystemClipboard TODO: move to Utils?
     * makeScreenshot(JGraph graph)
     */
    public void makeScreenshot()
    {
        getGraph().clearSelection();
        Object[] cells = getGraph().getRoots();
        if (cells.length > 0)
        {
            Rectangle2D bounds = getGraph().getCellBounds(cells);

            getGraph().toScreen(bounds);
            // Create a Buffered Image
            Dimension d = bounds.getBounds().getSize();
            BufferedImage img = new BufferedImage(d.width + 10, d.height + 10, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();
            graphics.translate(-bounds.getX() + 10, -bounds.getY() + 10);
            getGraph().paint(graphics);

            try
            {
                ImageSelection myImageSelection = new ImageSelection(img);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(myImageSelection, this);
                LoggerManager.debug(Constants.EDITOR_LOGGER, "Copied Net to Clipboard.");

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Zooms the net. <br>
     * The factor should be between <code>MIN_SCALE</code> and
     * <code>MAX_SCALE</code>. Multiplicate the factor with 100 to get the
     * percent value. Set <code>absolute</code>=<code>true</code> in order
     * to zoom the to the factor, not by the the factor.
     * 
     * @param factor
     * @param absolute
     */
    public void zoom(double factor, boolean absolute)
    {
        Rectangle2D oldVisRect = getGraph().fromScreen(m_scrollPane.getViewport().getViewRect());
        double scale;
        if (absolute)
        {
            scale = factor / 100;
        } else
        {
            scale = getGraph().getScale() + factor;
        }
        if (scale < MIN_SCALE)
        {
            scale = MIN_SCALE;
        }
        if (scale > MAX_SCALE)
        {
            scale = MAX_SCALE;
        }
        getGraph().setScale(scale);
        oldVisRect = getGraph().toScreen(oldVisRect);
        Rectangle2D newVisRect = m_scrollPane.getViewport().getViewRect();
        getGraph().scrollRectToVisible(
                new Rectangle2D.Double(newVisRect.getX() + oldVisRect.getCenterX() - newVisRect.getCenterX(), newVisRect.getY() + oldVisRect.getCenterY() - newVisRect.getCenterY(), newVisRect
                        .getWidth(), newVisRect.getHeight()).getBounds());
    }

    /* ########## LISTENER METHODS ########## */

    /**
     * Invoked after any changes in the net.
     * 
     * @see GraphSelectionListener#valueChanged(org.jgraph.event.GraphSelectionEvent)
     */
    public void valueChanged(GraphSelectionEvent arg0)
    {
        // If the selected Cell is any PetriNetModel the parent gets selected.
        // Elements can only be draged together with their name.
        if (arg0.getCell() instanceof PetriNetModelElement)
        {
            TreeNode parent = ((PetriNetModelElement) arg0.getCell()).getParent();
            if (parent != null)
            {
                getGraph().setSelectionCell(parent);
            }
        }
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     */
    public void lostOwnership(Clipboard arg0, Transferable arg1)
    {
        LoggerManager.debug(Constants.EDITOR_LOGGER, "Lost Ownership");
    }

    /**
     * Invoked after a cell has changed in some way. The vertex/vertices may
     * have changed bounds or altered adjacency, or other attributes have
     * changed that may affect presentation.
     */
    public void graphChanged(GraphModelEvent e)
    {
        setSaved(false);
    }

    /**
     * @see KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent e)
    {
        // setKeyPressed(false);
        getGraph().setCursor(Cursor.getDefaultCursor());
        setDrawingMode(false);
        setCreateElementType(-1);
        smartEditActive = true;
    }

    /**
     * @see KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e)
    {}

    /**
     * Shortcuts for the Editor are defined here.
     * 
     * @see KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e)
    {
        smartEditActive = false;
        // Listen for Delete Key Press
        if (e.getKeyCode() == KeyEvent.VK_DELETE)
        // Execute Remove Action on Delete Key Press
        {
            deleteSelection();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            setDrawingMode(false);
            smartEditActive = false;
            ((AbstractMarqueeHandler) getGraph().getMarqueeHandler()).cancelSmartArcDrawing();
        } else if (getGraph().getSelectionCells() != null)
        {
            // setKeyPressed(true);
            m_createElementType = 0;
            /* TODO: Arrow Key Move */
            if (e.getKeyCode() == KeyEvent.VK_UP)
            {
                move(getGraph().getSelectionCells(), 0, (int) -getGraph().getGridSize());
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            {
                move(getGraph().getSelectionCells(), 0, (int) +getGraph().getGridSize());
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            {
                move(getGraph().getSelectionCells(), (int) -getGraph().getGridSize(), 0);
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                move(getGraph().getSelectionCells(), (int) getGraph().getGridSize(), 0);
            } else

            if (e.getKeyCode() == KeyEvent.VK_MINUS)
            {
                zoom(-0.5, false);
            } else if (e.getKeyCode() == KeyEvent.VK_PLUS)
            {
                zoom(+0.5, false);
            } else if (e.getKeyCode() == KeyEvent.VK_1)
            {
                setCreateElementType(PetriNetModelElement.PLACE_TYPE);
                setDrawingMode(true);
            } else if (e.getKeyCode() == KeyEvent.VK_2)
            {
                setCreateElementType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
                setDrawingMode(true);
            } else if (e.getKeyCode() == KeyEvent.VK_3)
            {
                setCreateElementType(OperatorTransitionModel.AND_SPLIT_TYPE);
                setDrawingMode(true);
            } else if (e.getKeyCode() == KeyEvent.VK_4)
            {
                setCreateElementType(OperatorTransitionModel.XOR_SPLIT_TYPE);
                setDrawingMode(true);
            } else if (e.getKeyCode() == KeyEvent.VK_5)
            {
                setCreateElementType(OperatorTransitionModel.XOR_SPLITJOIN_TYPE);
                setDrawingMode(true);
            } else if (e.getKeyCode() == KeyEvent.VK_6)
            {
                setCreateElementType(OperatorTransitionModel.AND_JOIN_TYPE);
                setDrawingMode(true);
            } else if (e.getKeyCode() == KeyEvent.VK_7)
            {
                setCreateElementType(OperatorTransitionModel.XOR_JOIN_TYPE);
                setDrawingMode(true);
            } else if (e.getKeyCode() == KeyEvent.VK_8)
            {
                setCreateElementType(OperatorTransitionModel.SUBP_TYPE);
                setDrawingMode(true);
            }
            // else if (e.getKeyCode() == KeyEvent.VK_7)
            // {
            // m_createElementType = OperatorTransitionModel.OR_SPLIT_TYPE;
            // getGraph().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            // setDrawingMode(true);
            // }
        }
        // if (isDrawingMode())
        // getGraph().setCursor(Utils.getElementCreationCursor(getCreateElementType()));
        // else getGraph().setCursor(Cursor.getDefaultCursor());
    }

    public void addViewListener(IViewListener listener)
    {
        viewListener.addElement(listener);
    }

    public String getId()
    {
        return id;
    }

    public void removeViewListener(IViewListener listenner)
    {
        viewListener.removeElement(listenner);
    }

    public int getViewControllerType()
    {
        return ApplicationMediator.VIEWCONTROLLER_EDITOR;
    }

    /**
     * Fires a ViewEvent to each listener as long as the event is not consumed.
     * The event is also set with a reference to the current listener.
     */
    public final void fireViewEvent(AbstractViewEvent viewevent)
    {
        if (viewevent == null) return;
        java.util.Vector vector;
        synchronized (viewListener)
        {
            vector = (java.util.Vector) viewListener.clone();
        }
        if (vector == null) return;
        int i = vector.size();
        for (int j = 0; !viewevent.isConsumed() && j < i; j++)
        {
            IViewListener viewlistener = (IViewListener) vector.elementAt(j);
            viewevent.setViewListener(viewlistener);
            viewlistener.viewEventPerformed(viewevent);
        }
    }

    /* ########## GETTER & SETTER ########## */

    public Point2D getLastMousePosition()
    {
        return m_lastMousePosition;
    }

    public void setLastMousePosition(Point2D point)
    {
        this.m_lastMousePosition = point;
    }

    /**
     * Returns the location of the Editor. Only used in a MDI.
     * 
     * @return Returns the savedLocation.
     */
    public Point getSavedLocation()
    {
        return this.m_savedLocation;
    }

    /**
     * Stes the location of the Editor. Only used in a MDI.
     * 
     * @param location
     */
    public void setSavedLocation(Point location)
    {
        this.m_savedLocation = location;
    }

    /**
     * Returns the type of the element, which will be created in drawing mode.
     * 
     * @see PetriNetModelElement for element types
     * @return int
     */
    public int getCreateElementType()
    {
        return this.m_createElementType;
    }

    /**
     * Sets the type of the element, which will be created in drawing mode.
     * 
     * @see PetriNetModelElement for element types
     * @param createElementType
     *  
     */
    public void setCreateElementType(int createElementType)
    {
        int oldValue = m_createElementType;
        this.m_createElementType = createElementType;
        m_propertyChangeSupport.firePropertyChange("DrawMode", oldValue, createElementType);
    }

    /**
     * Returns if the editor is in tokengame mode.
     * 
     * @see TokenGameController
     * @return true if tokengame mode
     */
    public boolean isTokenGameMode()
    {
        return m_tokenGameMode;
    }

    /**
     * Returns the WoPeDJGraph (Graph Controller) USE WITH CARE!
     * 
     * @return WoPeDJGraph
     */
    public AbstractGraph getGraph()
    {
        return m_graph;
    }

    /**
     * Returns the default filetype for saving the net.
     * 
     * @return int
     */
    public int getDefaultFileType()
    {
        return m_defaultFileType;
    }

    /**
     * Sets the default filetype for saving the net.
     * 
     * @param defaultFileType
     *            The defaultFileType to set
     */
    public void setDefaultFileType(int defaultFileType)
    {
        this.m_defaultFileType = defaultFileType;
    }

    /**
     * Returns the filename if the net was saved before or was opened from a
     * file.
     * 
     * @return String
     */
    public String getName()
    {
        return super.getName() == null ? "Untitled" : super.getName();
    }

    /**
     * Returns the filepath if the net was saved before or was opened from a
     * file.
     * 
     * @return String
     */
    public String getFilePath()
    {
        return m_filePath;
    }

    /**
     * Sets the filepath. Should be called when the net was saved in a file.
     * 
     * @param filePath
     *            The filePath to set
     */
    public void setFilePath(String filePath)
    {
        this.m_filePath = filePath;
    }

    // Not nedded /**
    // * The keypressed method is used for internal use in the Editor. Some
    // * Shortcut are realizied. see the keyTyped Method for more.
    // *
    // * @param keyPressed
    // * The keyPressed to set
    // */
    // private void setKeyPressed(boolean keyPressed)
    // {
    // this.m_keyPressed = keyPressed;
    // }

    /**
     * Returns the saved flag for the editor.
     * 
     * @return boolean
     */
    public boolean isSaved()
    {
        return m_saved;
    }

    /**
     * Sets the saved flag for the editor. true when net was saved, or just
     * loaded.
     * 
     * @param savedFlag
     *            The savedFlag to set
     */
    public void setSaved(boolean savedFlag)
    {
        this.m_saved = savedFlag;
    }

    /**
     * Returns the saved Size of the Editor.
     * 
     * @return Dimension
     */
    public Dimension getSavedSize()
    {
        return m_savedSize;
    }

    /**
     * Sets the saved Size of the Editor.
     * 
     * @param savedSize
     *            The savedSize to set
     */
    public void setSavedSize(Dimension savedSize)
    {
        this.m_savedSize = savedSize;
    }

    /**
     * Returns the drawing mode. If the net is in drawing mode, clicking the
     * left mouse button will draw the Element with the set creation type.
     * 
     * @see getCreateElementType
     * @return drawing mode
     */
    public boolean isDrawingMode()
    {
        return m_drawingMode;
    }

    /**
     * Sets the drawing mode. If the net is in drawing mode, clicking the left
     * mouse button will draw the Element with the set creation type.
     * 
     * @see getCreateElementType
     * @param flag
     */
    public void setDrawingMode(boolean flag)
    {
        m_drawingMode = flag;
    }

    // /**
    // * Returns the PetriNet (Model Controllet) USE WITH CARE!
    // *
    // * @return
    // */
    // public PetriNetModelProcessor getPetriNet()
    // {
    // if (m_itsPetriNet == null)
    // {
    // m_itsPetriNet = new PetriNetModelProcessor();
    // }
    // return this.m_itsPetriNet;
    // }

    public boolean isSmartEditActive()
    {
        return smartEditActive;
    }

    public void setSmartEditActive(boolean smartEditActive)
    {
        this.smartEditActive = smartEditActive;
    }

    public JComponent getContainer()
    {
        return container;
    }

    public void setContainer(JComponent container)
    {
        this.container = container;
    }

    public IEditorProperties getElementProperties()
    {
        return elementProperties;
    }

    public void setElementProperties(IEditorProperties elementProperties)
    {
        this.elementProperties = elementProperties;
    }

    public AbstractModelProcessor getModelProcessor()
    {
        return modelProcessor;
    }

    /**
     * @param modelProcessor
     *            The modelProcessor to set.
     */
    public void setModelProcessor(AbstractModelProcessor modelProcessor)
    {
        this.modelProcessor = modelProcessor;
        getGraph().drawNet(modelProcessor);
    }

    public String toString()
    {
        return getName();
    }
}