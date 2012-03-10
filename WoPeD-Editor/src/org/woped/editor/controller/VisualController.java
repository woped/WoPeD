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
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JCheckBox;
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
	private AbstractApplicationMediator am = null;

	private boolean active = true;

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

	public static final int WITH_MAIN_EDITOR	 			= 37;

	public static final int CAN_UNDO 						= 38;
	
	public static final int CAN_REDO 						= 39;

	public static final int CAN_PASTE 						= 40;

	public static final int CAN_CUTCOPY 					= 41;

	public static final int SUBPROCESS_EDITOR 				= 42;
	
	public static final int REACH_GRAPH_START 				= 43;

	public static final int COLORING 						= 44;
	
	public static final int ROTATE 							= 45;

	public static final int TREEVIEW_VISIBLE 				= 46;

	public static final int OVERVIEW_VISIBLE 				= 47;

	public static final int APROMORE_IMPORT 				= 48;
	
	public static final int APROMORE_EXPORT 				= 49;
	
	private static final int MAX_ID 						= 50;	
	
	private ArrayList<Vector<WoPeDAction>> m_enable = new ArrayList<Vector<WoPeDAction>>();

	private ArrayList<Vector<WoPeDAction>> m_visible = new ArrayList<Vector<WoPeDAction>>();

	private ArrayList<Vector<WoPeDAction>> m_selected = new ArrayList<Vector<WoPeDAction>>();
	
	private boolean arcpointSelected   = false;
	
	private boolean[] m_status = new boolean[MAX_ID + 1];

	private static VisualController instance = null;

	
	public static VisualController getInstance()
	{
		return instance;
	}

	public VisualController(ApplicationMediator am)
	{
		super();
		instance = this;
		this.am = am;
		for (int i = 0; i <= MAX_ID; i++)
		{
			m_enable.add(new Vector<WoPeDAction>());
			m_visible.add(new Vector<WoPeDAction>());
			m_selected.add(new Vector<WoPeDAction>());
			m_status[i] = false;
		}
		m_status[ALWAYS] = true;
		m_status[NO_SELECTION] = true;
	}

	/**
	 * Adds a object which is supposed to be controlled.
	 * 
	 * @param obj
	 *            the newly added Object
	 * @param enable
	 *            tells when the element is supposed to be enabled.
	 * @param visible
	 *            tells when the element is supposed to be visible.
	 * @param selected
	 *            element is supposed to be selected.
	 */
	public void addElement(WoPeDAction action, int enable, int visible, int selected)
	{
		if (enable > IGNORE && enable <= MAX_ID)
		{
			if (setEnabled(action, m_status[enable]))
			{
				m_enable.get(enable).add(action);
			}
		}
		if (visible > IGNORE && visible <= MAX_ID)
		{
			if (setVisible(action, m_status[visible]))
			{
				m_visible.get(visible).add(action);
			}
		}
		if (selected > IGNORE && selected <= MAX_ID)
		{
			if (setSelected(action, m_status[selected]))
			{
				m_selected.get(selected).add(action);
			}
		}
	}

	/**
	 * Changes the enabling status of all targets belonging to action
	 * 
	 * @param action	associated WoPeD action
	 * @param status	target enabling status
	 * @return tells whether the status has been changed successfully.
	 */
	protected static boolean setEnabled(WoPeDAction action, boolean status)
	{
		if (action != null)
		{
			try
			{
				ArrayList<JComponent> targets = action.getTarget();
				Iterator<JComponent> it = targets.iterator();
				while (it.hasNext()) 
				{
					JComponent target = it.next();	
					if (! (target instanceof JMenuItem)) 
					{
						target.setEnabled(status);
					}
				}
				
				return true;
			} 
			catch (Exception e)
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
				while (it.hasNext()) 
				{
					JComponent target = it.next();
					if (target instanceof JCommandButton) 
					{
						((JCommandButton)target).setFlat(!status);
					} 
					else if (target instanceof JCheckBox) 
					{
						((JCheckBox)target).setSelected(status);
					}
				}

				return true;
			} 
			catch (Exception e)
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
				while (it.hasNext()) 
				{
					JComponent target = it.next();
					if (target instanceof JMenuItem) 
					{
						target.setVisible(status);
					}
				}

				return true;
			} 
			catch (Exception e)
			{
				LoggerManager.warn(Constants.EDITOR_LOGGER, "Could not change visibility status (code: " + action.getValue("NAME") + "): " + status);
			}
		}
		
		return false;
	}

	/**
	 * Changes enable status of all objects within a vector.
	 * 
	 * @param objects
	 * @param status
	 *            the new enable status.
	 */
	protected static void setEnabled(Vector<WoPeDAction> objects, boolean status)
	{
		if (objects != null)
		{
			for (int i = 0; i < objects.size(); i++)
			{
				setEnabled(objects.get(i), status);
			}
		}
	}

	/**
	 * Changes visible status of all objects within a vector.
	 * 
	 * @param objects
	 * @param status
	 *            the new visible status.
	 */
	private static void setVisible(Vector<WoPeDAction> objects, boolean status)
	{
		if (objects != null)
		{
			for (int i = 0; i < objects.size(); i++)
			{
				setVisible(objects.get(i), status);
			}
		}
	}

	/**
	 * Changes selected status of all objects within a vector.
	 * 
	 * @param objects
	 * @param status
	 *            the new selected status.
	 */
	private static void setSelected(Vector<WoPeDAction> objects, boolean status)
	{
		if (objects != null)
		{
			for (int i = 0; i < objects.size(); i++)
			{
				setSelected(objects.get(i), status);
			}
		}
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
		
		setSelected(m_selected.get(attribute), status);
		setVisible(m_visible.get(attribute), status);
		setEnabled(m_enable.get(attribute), status);
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
//				checkSubprocess();
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
		
		status = (editor == null) ? false : editor.getEditorPanel().isOverviewPanelVisible();
		setStatus(OVERVIEW_VISIBLE, status);
		
		status = (editor == null) ? false : editor.getEditorPanel().isTreeviewPanelVisible();
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
		
		if (isActive())
		{	
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
			boolean nameSelected = false;
			boolean operatorSelected = false;	
			boolean groupSelected = false;
			boolean multipleSelected = false;
			
			while (selectedCell instanceof GroupModel)
			{
				selectedCell = ((GroupModel) selectedCell).getMainElement();
			}
			
			if (graph != null && graph.isGroup(mainCell) && ((GroupModel)mainCell).isUngroupable()) 
			{
				groupSelected = true;
			} 
			else if (graph != null && graph.getSelectionCount() > 1) 
			{
				multipleSelected = true;
			}
			else if (selectedCell instanceof ArcModel)
			{
				arcSelected = true;
				ArcModel a = (ArcModel)selectedCell;
				if (a.isXORsplit(editor.getModelProcessor())) 
				{
					xorArcSelected = true;
				}
				if (((ArcModel) selectedCell).isRoute())
				{
					routedArcSelected = true;
				} 
				else
				{
					unroutedArcSelected = true;
				} 					
			} 
			else if (selectedCell instanceof SubProcessModel)
			{
				subprocessSelected = true;
				transitionSelected = true;
			}
			else if (selectedCell instanceof OperatorTransitionModel)
			{
				transitionSelected = true;
				operatorSelected = true;
				
				if (((TransitionModel) selectedCell).hasTrigger())
				{
					triggeredTransitionSelected = true;
				}
			} 
			else if (selectedCell instanceof TransitionModel)
			{
				int triggerType = TriggerModel.TRIGGER_NONE;
				transitionSelected = true;
				
				if (((TransitionModel)selectedCell).hasTrigger()) {
					triggerType = ((TransitionModel)selectedCell).getTriggerType();
					triggeredTransitionSelected = true;
				}
					
				if (triggerType != TriggerModel.TRIGGER_MESSAGE) 
					noMessageTriggerSelected = true;
				if (triggerType != TriggerModel.TRIGGER_TIME) 
					noTimeTriggerSelected = true;
				if (triggerType != TriggerModel.TRIGGER_RESOURCE) 
					noResourceTriggerSelected = true;				

			} 	
			else if (selectedCell instanceof PlaceModel)
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
				
				int triggerType = ((TriggerModel)selectedCell).getTriggertype();
				
				if (triggerType != TriggerModel.TRIGGER_MESSAGE) 
					noMessageTriggerSelected = true;
				if (triggerType != TriggerModel.TRIGGER_TIME) 
					noTimeTriggerSelected = true;
				if (triggerType != TriggerModel.TRIGGER_RESOURCE) 
					noResourceTriggerSelected = true;
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
			setStatus(UNROUTED_ARC_SELECTION, unroutedArcSelected);
			setStatus(TRANSITION_SELECTION, transitionSelected);
			setStatus(NODE_SELECTION, transitionSelected | placeSelected);
			setStatus(TRIGGERED_TRANSITION_SELECTION, triggeredTransitionSelected);
			setStatus(TIME_TRIGGER_SELECTION, noTimeTriggerSelected);
			setStatus(RESOURCE_TRIGGER_SELECTION, noResourceTriggerSelected);
			setStatus(MESSAGE_TRIGGER_SELECTION, noMessageTriggerSelected);
			setStatus(TRIGGERED_TRANSITION_SELECTION, triggeredTransitionSelected);
			setStatus(ARC_SELECTION, arcSelected && !arcpointSelected);
			setStatus(ARCPOINT_SELECTION, arcpointSelected);
			setStatus(OPERATOR_SELECTION, operatorSelected);
			setStatus(GROUP_SELECTION, groupSelected);
			setStatus(MULTIPLE_SELECTION, multipleSelected);
			setStatus(CAN_CUTCOPY, transitionSelected | placeSelected | multipleSelected | groupSelected);
			setStatus(CAN_PASTE, editor == null ? false : !editor.isClipboardEmpty());
		};		
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
			case (OperatorTransitionModel.XOR_SPLITJOIN_TYPE):
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

	protected void checkSubprocess() 
	{
		IEditor editor = am.getUi().getEditorFocus();
		
		if (editor != null) {
			if (editor instanceof SubprocessEditorVC)
				setStatus(WITH_MAIN_EDITOR, ((SubprocessEditorVC)editor).getParentEditor().isTokenGameEnabled());
			else
				setStatus(WITH_MAIN_EDITOR, true);
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
			UndoManager undoManager = ((WoPeDJGraph) am.getUi()
					.getEditorFocus().getGraph()).getUndoManager();
			if (undoManager != null) {

				if (am.getUi().getEditorFocus() instanceof SubprocessEditorVC)
					undo = ((WoPeDUndoManager) undoManager).canUndo(am.getUi()
							.getEditorFocus().getModelProcessor()
							.getElementContainer());	
			
				else
				undo = undoManager.canUndo();
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
		
	public void notify(boolean isEmpty)
	{
		setStatus(CAN_PASTE, am.getUi().getAllEditors().size() > 0 && !isEmpty);
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
}