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
/*
 * Created on Aug 3, 2004
 */
package org.woped.editor.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.undo.UndoManager;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractGraph;
import org.woped.core.controller.IEditor;
import org.woped.core.gui.ITokenGameController;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.EditorLayoutInfo;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.NameModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.controller.vc.SubprocessEditorVC;

/**
 * @author Thomas Pohl
 * 
 * The VisualController enables and disables, changes the visibility and
 * selection status. Changes are initiated from external events via
 * PropertyChangeEvents and GraphSelectionEvents.
 */
public class VisualController implements PropertyChangeListener, IClipboaredListener, GraphSelectionListener, MouseListener
{
	public static final int IGNORE 							= -1;
	public static final int ALWAYS 							= 0;
	public static final int NEVER 							= 1;
	public static final int WITH_EDITOR 					= 2;
	// Selections
	public static final int NO_SELECTION 					= 3;
	public static final int ANY_SELECTION 					= 4;
	public static final int NODE_SELECTION 					= 5;
	public static final int TRANSITION_SELECTION 			= 6;
	public static final int TRIGGERED_TRANSITION_SELECTION 	= 7;
	public static final int TIME_TRIGGER_SELECTION 			= 8;
	public static final int RESOURCE_TRIGGER_SELECTION 		= 9;
	public static final int MESSAGE_TRIGGER_SELECTION 		= 10;
	public static final int ANY_TRIGGER_SELECTION 			= 11;
	public static final int OPERATOR_SELECTION 				= 12;
	public static final int PLACE_SELECTION 				= 13;
	public static final int TOKEN_PLACE_SELECTION 			= 14;
	public static final int SUBPROCESS_SELECTION 			= 15;
	public static final int ARC_SELECTION 					= 16;
	public static final int ROUTED_ARC_SELECTION 			= 17;
	public static final int UNROUTED_ARC_SELECTION 			= 18;
	public static final int NODE_OR_XORARC_SELECTION 		= 19;
	public static final int NODE_OR_ARC_SELECTION = 60;
	public static final int ARCPOINT_SELECTION				= 20;
	public static final int MULTIPLE_SELECTION				= 21;
	public static final int GROUP_SELECTION					= 22;
	// Drawmode
	public static final int DRAWMODE_PLACE 					= 23;
	public static final int DRAWMODE_TRANSITION 			= 24;
	public static final int DRAWMODE_AND_SPLIT 				= 25;
	public static final int DRAWMODE_AND_JOIN 				= 26;
	public static final int DRAWMODE_XOR_SPLIT 				= 27;
	public static final int DRAWMODE_XOR_JOIN 				= 28;
	public static final int DRAWMODE_OR_SPLIT 				= 29;
	public static final int DRAWMODE_SUBPROCESS 			= 30;
	public static final int DRAWMODE_XOR_SPLITJOIN 			= 31;
	public static final int DRAWMODE_ANDJOIN_XORSPLIT 		= 32;
	public static final int DRAWMODE_AND_SPLITJOIN 			= 33;
	public static final int DRAWMODE_XORJOIN_ANDSPLIT 		= 34;
	public static final int DRAWMODE 						= 35;
	public static final int TOKENGAME 						= 36;
	public static final int CAN_UNDO 						= 38;
	public static final int CAN_REDO 						= 39;
	public static final int CAN_PASTE 						= 40;
	public static final int CAN_CUTCOPY 					= 41;
	public static final int CAN_RENAME	 					= 42;
	public static final int SUBPROCESS_EDITOR 				= 43;
	public static final int REACH_GRAPH_START 				= 44;
	public static final int COLORING 						= 45;
	public static final int ROTATE 							= 46;
	public static final int TREEVIEW_VISIBLE 				= 47;
	public static final int OVERVIEW_VISIBLE 				= 48;
	public static final int APROMORE_IMPORT 				= 49;
	public static final int APROMORE_EXPORT 				= 50;
	public static final int IS_REGISTERED					= 51;
	// Exactly one sub process 'transition' is active and can be executed
	public static final int TOKENGAME_SUBPROCESS_TRANSITION_ACTIVE = 52;
	// We are currently executing a sub process
	public static final int TOKENGAME_IN_SUBPROCESS		    = 53;
	// Exactly one transition is active and can occur
	public static final int TOKENGAME_TRANSITION_ACTIVE		= 54;

    // The following describe states that may occur while token game mode
    // is enabled
    // Transitions have occurred, so there is history information
	public static final int TOKENGAME_TRANSITION_HISTORY	= 55;
	// The token game is in auto-play mode
	public static final int TOKENGAME_AUTOPLAY_MODE			= 56;
    // Auto play is running
    public static final int TOKENGAME_AUTOPLAY_PLAYING		= 57;
	public static final int P2T	 							= 58;
	public static final int T2P	 							= 59;
	public static final int CAN_DECREASE_ARC_WEIGHT = 60;

	private static final int MAX_ID = 61;
	private static VisualController instance = null;
    private AbstractApplicationMediator am = null;
    private boolean active = true;

    // For each WoPeD action, we store a corresponding visibility configuration
	private Map<WoPeDAction, IVisibility> actionVisibilityMap = new HashMap<WoPeDAction,IVisibility>();
	
	private boolean arcpointSelected   = false;
	
	private boolean[] m_status = new boolean[MAX_ID + 1];

	public VisualController(ApplicationMediator am)
	{
		super();
		instance = this;
		this.am = am;
		for (int i = 0; i <= MAX_ID; i++)
		{
			m_status[i] = false;
		}
		m_status[ALWAYS] = true;
		m_status[NO_SELECTION] = true;
	}

    public static VisualController getInstance() {
        return instance;
    }

	/**
	 * Changes the enabling status of all targets belonging to action
     *
     * @param action	associated WoPeD action
	 * @param status	target enabling status
	 * @return tells whether the status has been changed successfully.
	 */
	public static boolean setEnabled(WoPeDAction action, boolean status)
	{
		if (action != null)
		{
			try
			{
				ArrayList<JComponent> targets = action.getTarget();
				Iterator<JComponent> it = targets.iterator();
                while (it.hasNext()) {
                    JComponent target = it.next();

                    if (!(target instanceof JMenuItem)) {
						target.setEnabled(status);
					}
					//TODO: Workaround for OSXMenuItem
                    if ((target instanceof JMenuItem)) {
						if( target.getClass().getName().equals("org.woped.starter.osxMenu.OSXMenuItem") || target instanceof JCheckBoxMenuItem){
							target.setEnabled(status);
                            target.setVisible(true);
                        }
					}


                }

				return true;
            } catch (Exception e)
			{
				LoggerManager.warn(Constants.EDITOR_LOGGER, "Could not change enabling status (code: " + action.getValue("NAME") + "): " + status);
			}
		}

		return false;
	}

	/**
	 * Changes the selection status of all targets belonging to action
     *
     * @param action	associated WoPeD action
	 * @param status target selection status
	 * @return tells whether the status has been changed successfully.
	 */
	protected static boolean setSelected(WoPeDAction action, boolean status)
	{
		if (action != null)
		{
			try
			{
				ArrayList<JComponent> targets = action.getTarget();
				Iterator<JComponent> it = targets.iterator();
                while (it.hasNext()) {
					JComponent target = it.next();
                    if (target instanceof JCommandButton) {
						((JCommandButton)target).setFlat(!status);
                    } else if (target instanceof JCheckBox) {
						((JCheckBox)target).setSelected(status);
					}
				}

				return true;
            } catch (Exception e)
			{
				LoggerManager.warn(Constants.EDITOR_LOGGER, "Could not change selection status (code: " + action.getValue("NAME") + "): " + status);
			}
		}

		return false;
	}

	/**
	 * Changes the visibility status of all targets belonging to action
     *
     * @param action	associated WoPeD action
	 * @param status target visibility status
	 * @return tells whether the status has been changed successfully.
	 */
	protected static boolean setVisible(WoPeDAction action, boolean status)
	{
		if (action != null)
		{
			try
			{
				ArrayList<JComponent> targets = action.getTarget();
				Iterator<JComponent> it = targets.iterator();
                while (it.hasNext()) {
					JComponent target = it.next();
					//TODO: Workaround for OSXMenuItem - Check depenencies
                    if (target instanceof JMenuItem) {
						if( target.getClass().getName().equals("org.woped.starter.osxMenu.OSXMenuItem") || target instanceof JCheckBoxMenuItem)
							target.setVisible(true);
						else
							target.setVisible(status);
					}
				}

				return true;
            } catch (Exception e)
			{
				LoggerManager.warn(Constants.EDITOR_LOGGER, "Could not change visibility status (code: " + action.getValue("NAME") + "): " + status);
			}
		}

		return false;
	}

    /**
     * Adds a object which is supposed to be controlled, using DefaultVisibility.
     * Note that each aspect of an action's visibility (enabled, visible, selected)
     * must depend on exactly one attribute. If this is too simplistic for you,
     * please supply a custom IVisibility implementation using the method below
     *
     * @param obj      the newly added Object
     * @param enable   tells when the element is supposed to be enabled.
     * @param visible  tells when the element is supposed to be visible.
     * @param selected element is supposed to be selected.
     */
    public void addElement(WoPeDAction action, int enable, int visible, int selected) {
        IVisibility visibilityConfiguration = new DefaultVisibility(enable, visible, selected);
        addElement(action, visibilityConfiguration);
    }

	/**
     * Adds a object which is supposed to be controlled, using a custom visibility object.
     *
     * @param obj
     *            the newly added Object
     * @param visibility
     *            decide about visibility options for the specified WoPeDAction
     */
    public void addElement(WoPeDAction action, IVisibility visibilityConfiguration) {
        actionVisibilityMap.put(action, visibilityConfiguration);
        updateStatus(action);
    }

	/**
	 * Returns if the provided action is currently enabled.
	 * @param action the action to get the current enabled state
	 * @return {@code true} if the action is enabled, otherwise {@code false}
	 */
	public boolean isEnabled(WoPeDAction action){
    	return actionVisibilityMap.get(action).getEnabled(m_status);
	}

    /**
     * Changes the selected the selected, visible and enable status for all
	 * subscribed objects.
     *
	 * @param attribute
     * @param status
	 */
	private void setStatus(int attribute, boolean status)
	{
		m_status[attribute] = status;
        updateStatusAll();
    }

    private void updateStatusAll() {
        for (WoPeDAction action : this.actionVisibilityMap.keySet())
            updateStatus(action);
	}

	private void updateStatus(WoPeDAction action) {
		setEnabled(action, actionVisibilityMap.get(action).getEnabled(m_status));
		setVisible(action, actionVisibilityMap.get(action).getVisible(m_status));
        setSelected(action, actionVisibilityMap.get(action).getSelected(m_status));
    }

    /**
     * PropertyChanges will be fired when one of the following happens:
	 * <ul>
	 * <li>The Number of InternalFrames(Editors) changes.</li>
	 * <li>A different InternalFrame gets selected.</li>
	 * <li>The Drawmode gets changes.</li>
	 * <li>The TokenGame gets started or stopped.</li>
	 * <li>The Understandability Coloring gets activated.</li>
	 * </ol>
     *
	 * @see PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent arg0)
	{
		if (isActive())
		{
			if ("Registration".equals(arg0.getPropertyName())) {
				checkRegistration();
			}

			if ("InternalFrameCount".equals(arg0.getPropertyName()))
			{
				checkActiveEditor();
				checkSelection();
				checkUndoRedo();
				checkColoring();
				checkRotation();
				checkAproMoRe();
				checkSidebar();
				checkDrawMode();
				checkMode();
			} else if ("FrameSelection".equals(arg0.getPropertyName()))
			{
				checkActiveEditor();
				checkSelection();
				checkUndoRedo();
                checkColoring();
                checkSidebar();
                checkRotation();
				checkDrawMode();
				checkMode();
			} else if ("DrawMode".equals(arg0.getPropertyName()))
			{
				checkDrawMode();
			} else if ("TokenGameMode".equals(arg0.getPropertyName()))
			{
				checkMode();
			} else if ("TokenGameState".equals(arg0.getPropertyName()))
			{
				checkTokenGame((ITokenGameController.TokenGameStats)(arg0.getNewValue()));
			} else if ("UndoRedo".equals(arg0.getPropertyName()))
			{
				checkUndoRedo();
			} else if ("Update".equals(arg0.getPropertyName()))
			{
				checkColoring();
				checkRotation();
				checkAproMoRe();
			} else if ("Sidebar".equals(arg0.getPropertyName()))
			{
				checkSidebar();
			} else if ("Import".equals(arg0.getPropertyName()))
			{
				setStatus(ROTATE, true);
            }
			} else if (JSplitPane.DIVIDER_LOCATION_PROPERTY.equals(arg0.getPropertyName())){
				checkSidebarDivider();
        }
	}

	/**
	 * Will be called when the Selection changes.
	 */
	public void valueChanged(GraphSelectionEvent arg0)
	{
		checkSelection();
	}

	protected void checkSidebarDivider() {
		IEditor editor = am.getUi().getEditorFocus();

		if (editor != null) {
			editor.checkMainSplitPaneDivider();
		}
	}

	protected void checkSidebar()
	{
		boolean status = false;
		EditorVC editor = ((EditorVC)(am.getUi().getEditorFocus()));

		status = editor != null && editor.getEditorPanel().isOverviewPanelVisible();
        setStatus(OVERVIEW_VISIBLE, status);

		status = editor != null && editor.getEditorPanel().isTreeviewPanelVisible();
        setStatus(TREEVIEW_VISIBLE, status);
	}

    /**
	 * Check what elements are selected and changes the status accordingly.
	 */
	protected void checkSelection()
	{
		IEditor 		editor = am.getUi().getEditorFocus();
		AbstractGraph 	graph = null;
		Object 			selectedCell = null, mainCell = null;

		if (isActive()) {
			graph = null;
            selectedCell = null;
			if (editor != null)
			{
				graph = editor.getGraph();
				selectedCell = graph.getSelectionCell();
				mainCell = selectedCell;
			}

			boolean noSelection = (selectedCell == null && !arcpointSelected);
			boolean routedArcSelected = false;
			boolean xorArcSelected = false;
			boolean unroutedArcSelected = false;
			boolean transitionSelected = false;
			boolean triggeredTransitionSelected = false;
			boolean noTimeTriggerSelected = false;
			boolean noMessageTriggerSelected = false;
			boolean noResourceTriggerSelected = false;
			boolean placeSelected = false;
			boolean tokenPlaceSelected = false;
			boolean subprocessSelected = false;
			boolean arcSelected = false;
			boolean arcCanDecreaseWeight = false;
			boolean nameSelected = false;
			boolean operatorSelected = false;
            boolean groupSelected = false;
			boolean multipleSelected = false;
			int triggerType = TriggerModel.TRIGGER_NONE;

			while (selectedCell instanceof GroupModel)
			{
				selectedCell = ((GroupModel) selectedCell).getMainElement();
			}

			if (graph != null && graph.isGroup(mainCell) && ((GroupModel) mainCell).isUngroupable()) {
                groupSelected = true;
            }
			else if (graph != null && graph.getSelectionCount() > 1) {
                multipleSelected = true;
			}
			else if (selectedCell instanceof ArcModel)
			{
				arcSelected = true;
				ArcModel a = (ArcModel)selectedCell;
                if (a.isXORsplit(editor.getModelProcessor())) {
                    xorArcSelected = true;
				}

				if (((ArcModel) selectedCell).isRoute())
				{
					routedArcSelected = true;
				} else {
					unroutedArcSelected = true;
                }

				if ( a.getInscriptionValue() > 1 ) {
					arcCanDecreaseWeight = true;
				}

			}
			else if (selectedCell instanceof SubProcessModel)
			{
				subprocessSelected = true;
			}
			else if (selectedCell instanceof OperatorTransitionModel)
			{
				transitionSelected = true;
				operatorSelected = true;

				if (((OperatorTransitionModel) selectedCell).hasTrigger()) {
					triggerType = ((OperatorTransitionModel)selectedCell).getTriggerType();
					triggeredTransitionSelected = true;
				}

				if (triggerType != TriggerModel.TRIGGER_MESSAGE) noMessageTriggerSelected = true;
                if (triggerType != TriggerModel.TRIGGER_TIME) noTimeTriggerSelected = true;
                if (triggerType != TriggerModel.TRIGGER_RESOURCE) noResourceTriggerSelected = true;

            } else if (selectedCell instanceof TransitionModel)
			{
				transitionSelected = true;

				if (((TransitionModel)selectedCell).hasTrigger()) {
					triggerType = ((TransitionModel)selectedCell).getTriggerType();
					triggeredTransitionSelected = true;
				}

				if (triggerType != TriggerModel.TRIGGER_MESSAGE) noMessageTriggerSelected = true;
                if (triggerType != TriggerModel.TRIGGER_TIME) noTimeTriggerSelected = true;
                if (triggerType != TriggerModel.TRIGGER_RESOURCE) noResourceTriggerSelected = true;

            } else if (selectedCell instanceof PlaceModel)
			{
				placeSelected = true;
				if (((PlaceModel) selectedCell).hasTokens())
				{
					tokenPlaceSelected = true;
				}
			}
			else if (selectedCell instanceof TriggerModel)
			{
				transitionSelected = true;
				triggeredTransitionSelected = true;

				triggerType = ((TriggerModel)selectedCell).getTriggertype();

				if (triggerType != TriggerModel.TRIGGER_MESSAGE) noMessageTriggerSelected = true;
                if (triggerType != TriggerModel.TRIGGER_TIME) noTimeTriggerSelected = true;
                if (triggerType != TriggerModel.TRIGGER_RESOURCE) noResourceTriggerSelected = true;
			}
			else if (selectedCell instanceof NameModel)
			{
				nameSelected = true;
            }

			setStatus(NO_SELECTION, noSelection);
            setStatus(ANY_SELECTION, !noSelection && !nameSelected && !arcpointSelected);
            setStatus(SUBPROCESS_SELECTION, subprocessSelected);
			setStatus(PLACE_SELECTION, placeSelected);
			setStatus(TOKEN_PLACE_SELECTION, tokenPlaceSelected);
			setStatus(ROUTED_ARC_SELECTION, routedArcSelected);
			setStatus(NODE_OR_XORARC_SELECTION, (transitionSelected | placeSelected | xorArcSelected) && !arcpointSelected);
			setStatus(NODE_OR_ARC_SELECTION, transitionSelected | placeSelected | arcSelected);
			setStatus(UNROUTED_ARC_SELECTION, unroutedArcSelected);
			setStatus(TRANSITION_SELECTION, transitionSelected);
			setStatus(NODE_SELECTION, transitionSelected | placeSelected | subprocessSelected);
			setStatus(TRIGGERED_TRANSITION_SELECTION, triggeredTransitionSelected);
			setStatus(TIME_TRIGGER_SELECTION, noTimeTriggerSelected);
			setStatus(RESOURCE_TRIGGER_SELECTION, noResourceTriggerSelected);
			setStatus(MESSAGE_TRIGGER_SELECTION, noMessageTriggerSelected);
			setStatus(TRIGGERED_TRANSITION_SELECTION, triggeredTransitionSelected);
			setStatus(ARC_SELECTION, arcSelected && !arcpointSelected);
			setStatus(CAN_DECREASE_ARC_WEIGHT, arcSelected && arcCanDecreaseWeight);
			setStatus(ARCPOINT_SELECTION, arcpointSelected);
			setStatus(OPERATOR_SELECTION, operatorSelected);
			setStatus(GROUP_SELECTION, groupSelected);
			setStatus(MULTIPLE_SELECTION, multipleSelected);
			setStatus(CAN_CUTCOPY, transitionSelected | placeSelected | multipleSelected | groupSelected | subprocessSelected);
            setStatus(CAN_PASTE, editor != null && !editor.isClipboardEmpty());
            setStatus(CAN_RENAME, subprocessSelected);
        }
    }

	protected void checkDrawMode()
	{
		boolean place = false;
		boolean transition = false;
		boolean andSplit = false;
		boolean andJoin = false;
		boolean xorSplit = false;
		boolean xorJoin = false;
		boolean xorSplitJoin = false;
		boolean andSplitJoin = false;
		boolean andJoinXorSplit = false;
		boolean xorJoinAndSplit = false;
		boolean orSplit = false;
		boolean subprocess = false;

		if (am.getUi().getEditorFocus() != null) {

			switch (am.getUi().getEditorFocus().getCreateElementType())
			{
			case (AbstractPetriNetElementModel.PLACE_TYPE):
				place = true;
				break;
			case (AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE):
				transition = true;
				break;
			case (OperatorTransitionModel.AND_SPLIT_TYPE):
				andSplit = true;
				break;
			case (OperatorTransitionModel.AND_JOIN_TYPE):
				andJoin = true;
				break;
			case (OperatorTransitionModel.AND_SPLITJOIN_TYPE):
				andSplitJoin = true;
				break;
			case (OperatorTransitionModel.XOR_SPLIT_TYPE):
				xorSplit = true;
				break;
                case (OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE):
                xorSplitJoin = true;
				break;
			case (OperatorTransitionModel.XOR_JOIN_TYPE):
				xorJoin = true;
				break;
			case (OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE):
				andJoinXorSplit = true;
				break;
			case (OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE):
				xorJoinAndSplit = true;
				break;
			case (OperatorTransitionModel.OR_SPLIT_TYPE):
				orSplit = true;
				break;
			case (OperatorTransitionModel.SUBP_TYPE):
				subprocess = true;
				break;
			}
		}

		setStatus(DRAWMODE_PLACE, place);
		setStatus(DRAWMODE_TRANSITION, transition);
		setStatus(DRAWMODE_AND_SPLIT, andSplit);
		setStatus(DRAWMODE_AND_JOIN, andJoin);
		setStatus(DRAWMODE_AND_SPLITJOIN, andSplitJoin);
		setStatus(DRAWMODE_XOR_SPLIT, xorSplit);
		setStatus(DRAWMODE_XOR_JOIN, xorJoin);
		setStatus(DRAWMODE_XOR_SPLITJOIN, xorSplitJoin);
		setStatus(DRAWMODE_ANDJOIN_XORSPLIT, andJoinXorSplit);
		setStatus(DRAWMODE_XORJOIN_ANDSPLIT, xorJoinAndSplit);
		setStatus(DRAWMODE_OR_SPLIT, orSplit);
        setStatus(DRAWMODE_SUBPROCESS, subprocess);
    }

    protected void checkAproMoRe() {
		setStatus(APROMORE_IMPORT, ConfigurationManager.getConfiguration().getApromoreUse());
		setStatus(APROMORE_EXPORT, ConfigurationManager.getConfiguration().getApromoreUse() & am.getUi().getEditorFocus() != null);
	}

	/**
	 * Checks whether an Editor is active.
     *
	 */
	protected void checkActiveEditor()
	{
		boolean noEditors = (am.getUi().getAllEditors().size() < 1);
		setStatus(WITH_EDITOR, !noEditors);

		if (am.getUi().getEditorFocus() != null)
		{
			setStatus(SUBPROCESS_EDITOR, !(am.getUi().getEditorFocus()
					 instanceof SubprocessEditorVC));
		}
		else
		{
			setStatus(SUBPROCESS_EDITOR, false);
		}
	}

    protected void checkMode()
	{
		boolean tokengame = false;
		boolean drawMode = false;
		IEditor editor = am.getUi().getEditorFocus();

		if (editor != null) {
			tokengame = editor.isTokenGameEnabled();
			drawMode = !tokengame;
		}
		setStatus(TOKENGAME, tokengame);
		setStatus(DRAWMODE, drawMode);
	}

	protected void checkUndoRedo()
	{
		boolean undo = false;
		boolean redo = false;
		if (am.getUi().getEditorFocus() != null)
		{
            UndoManager undoManager = am.getUi().getEditorFocus().getGraph().getUndoManager();
            if (undoManager != null) {

				if (am.getUi().getEditorFocus() instanceof SubprocessEditorVC)
					undo = ((WoPeDUndoManager) undoManager).canUndo(am.getUi()
							.getEditorFocus().getModelProcessor().getElementContainer());

                else undo = undoManager.canUndo();
				redo = undoManager.canRedo();
			}
		}

		setStatus(CAN_UNDO, undo);
		setStatus(CAN_REDO, redo);
	}

	protected void checkColoring(){
		boolean coloringActive = false;
		if (am.getUi().getEditorFocus() != null)
		{
			coloringActive = (am.getUi().getEditorFocus()).isUnderstandabilityColoringEnabled();
		}
		setStatus(COLORING, coloringActive);
	}

	protected void checkRotation(){
		boolean rotateActive = false;
		if (am.getUi().getEditorFocus() != null)
		{
			rotateActive = am.getUi().getEditorFocus().isRotateSelected();
			EditorLayoutInfo.m_verticalLayout = rotateActive;
		}
		setStatus(ROTATE, rotateActive);
	}

    protected void checkRegistration() {
		boolean isRegistered = ConfigurationManager.getConfiguration().isRegistered();
		setStatus(IS_REGISTERED, isRegistered);
	}

    public void notify(boolean isEmpty)
	{
		setStatus(CAN_PASTE, am.getUi().getAllEditors().size() > 0 && !isEmpty);
	}

	protected void checkTokenGame(ITokenGameController.TokenGameStats tokenGameStats) {
		setStatus(TOKENGAME_SUBPROCESS_TRANSITION_ACTIVE, tokenGameStats.numActiveSubprocesses == 1);
		setStatus(TOKENGAME_IN_SUBPROCESS, tokenGameStats.inSubprocess);
		setStatus(TOKENGAME_TRANSITION_ACTIVE, tokenGameStats.numActiveTransitions == 1);
		setStatus(TOKENGAME_TRANSITION_HISTORY, tokenGameStats.hasHistory);
		setStatus(TOKENGAME_AUTOPLAY_MODE, tokenGameStats.autoPlayMode);
		setStatus(TOKENGAME_AUTOPLAY_PLAYING, tokenGameStats.autoPlayPlaying);
	}

    public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public void mouseClicked(MouseEvent arg0)
	{
	}

	public void mouseEntered(MouseEvent arg0)
	{
	}

	public void mouseExited(MouseEvent arg0)
	{
	}

	public void mousePressed(MouseEvent arg0)
	{
	}

	public void mouseReleased(MouseEvent arg0)
	{
	}

	public void setArcpointSelection(boolean status) {
		arcpointSelected = status;
		checkSelection();

	}

	/**
     * Objects implementing IVisibility control the visual representation
     * of WoPeDAction instances in menus etc.
     * Whether an action is visible, enabled or selected depends on attributes
     * describing application-wide state, such as whether an editor window is currently
     * open and in focus (WITH_EDITOR) or whether a token game is active (TOKENGAME).
     *
     * For convenience, there is a default implementation called DefaultVisibility,
     * covering simple cases where visual representation depends on only one
     * attribute for each visible, enabled and selected.
     *
     * @author weirdsoul
     *
     */
    public interface IVisibility {
        /**
         * Return whether an associated action should be visible, depending on the
         * specified status
         *
         * @param status An array of attributes that are either enabled or disabled
         * @return true if action should be visible, false otherwise
         */
        boolean getVisible(boolean status[]);

        /**
         * Return whether an associated action should be enabled, depending on the
         * specified status
         *
         * @param status An array of attributes that are either enabled or disabled
         * @return true if action should be enabled, false otherwise
         */
        boolean getEnabled(boolean status[]);

        /**
         * Return whether an associated action should be selected, depending on the
         * specified status
         *
         * @param status An array of attributes that are either enabled or disabled
         * @return true if action should be selected, false otherwise
         */
        boolean getSelected(boolean status[]);
    }

    private class DefaultVisibility implements IVisibility {

        private int attributeVisible;
        private int attributeEnabled;
		private int attributeSelected;
		
		public DefaultVisibility(int attributeEnabled,
				int attributeVisible, int attributeSelected) {
			this.attributeVisible = attributeVisible;
            this.attributeEnabled = attributeEnabled;
            this.attributeSelected = attributeSelected;
        }

        public boolean getVisible(boolean status[]) {
            return ((attributeVisible > IGNORE && attributeVisible <= MAX_ID) && status[attributeVisible]);
        }

        public boolean getEnabled(boolean status[]) {
            return ((attributeEnabled > IGNORE && attributeEnabled <= MAX_ID) && status[attributeEnabled]);
        }

        public boolean getSelected(boolean status[]) {
            return ((attributeSelected > IGNORE && attributeSelected <= MAX_ID) && status[attributeSelected]);
        }
	}		
	
}