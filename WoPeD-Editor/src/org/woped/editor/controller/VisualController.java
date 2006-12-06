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
/*
 * Created on Aug 3, 2004
 */
package org.woped.editor.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.swing.undo.UndoManager;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractGraph;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.controller.vc.EditorVC;

/**
 * @author Thomas Pohl
 * 
 * The VisualController enables and disables, changes the visibility and
 * selection status. Changes are initiated from external events via
 * PropertyChangeEvents and GraphSelectionEvents.
 */
public class VisualController implements PropertyChangeListener,
		GraphSelectionListener, IClipboaredListener, MouseListener
{
	private ApplicationMediator am = null;

	private boolean active = true;

	public static final int IGNORE = -1;

	public static final int ALWAYS = 0;

	public static final int NEVER = 1;

	public static final int WITH_EDITOR = 2;

	public static final int NO_SELECTION = 3;

	public static final int ANY_SELECTION = 4;

	public static final int ARC_SELECTION = 5;

	public static final int ROUTED_ARC_SELECTION = 6;

	public static final int UNROUTED_ARC_SELECTION = 7;

	public static final int TRANSITION_SELECTION = 8;

	public static final int TRIGGERED_TRANSITION_SELECTION = 9;

	public static final int PLACE_SELECTION = 10;

	public static final int TOKEN_PLACE_SELECTION = 11;

	public static final int SUBPROCESS_SELECTION = 12;

	// public static final int NO_SELECTION_AND_COPY = 13;
	public static final int DRAWMODE_PLACE = 14;

	public static final int DRAWMODE_TRANSITION = 15;

	public static final int DRAWMODE_AND_SPLIT = 16;

	public static final int DRAWMODE_AND_JOIN = 17;

	public static final int DRAWMODE_XOR_SPLIT = 18;

	public static final int DRAWMODE_XOR_JOIN = 19;

	public static final int DRAWMODE_OR_SPLIT = 20;

	public static final int DRAWMODE_SUBPROCESS = 21;

	public static final int DRAWMODE = 22;

	public static final int TOKENGAME = 23;

	public static final int WOFLAN = 24;

	public static final int CAN_UNDO = 25;

	public static final int CAN_REDO = 26;

	public static final int DRAWMODE_XOR_SPLITJOIN = 27;

	public static final int ELEMENT_SELECTION = 28;

	public static final int CAN_PASTE = 29;

	public static final int DRAWMODE_AND_SPLITJOIN = 30;

	public static final int ARC_POINT = 31;

	public static final int SUBPROCESS_EDITOR = 32;

	private static final int MAX_ID = 33;

	private Vector[] m_enable = new Vector[MAX_ID + 1];

	private Vector[] m_visible = new Vector[MAX_ID + 1];

	private Vector[] m_selected = new Vector[MAX_ID + 1];

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
			m_enable[i] = new Vector();
			m_visible[i] = new Vector();
			m_selected[i] = new Vector();
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
	public void addElement(Object obj, int enable, int visible, int selected)
	{
		if (enable == 31 || visible == 31 || selected == 31)
		{
			// System.out.println(obj);
		}
		if (enable > IGNORE && enable <= MAX_ID)
		{
			if (setEnabled(obj, m_status[enable]))
			{
				m_enable[enable].add(obj);
			}
		}
		if (visible > IGNORE && visible <= MAX_ID)
		{
			if (setVisible(obj, m_status[visible]))
			{
				m_visible[visible].add(obj);
			}
		}
		if (selected > IGNORE && selected <= MAX_ID)
		{
			if (setSelected(obj, m_status[selected]))
			{
				m_selected[selected].add(obj);
			}
		}
	}

	/**
	 * Changes the enable-status of one Object
	 * 
	 * @param status
	 *            target enable-status
	 * @return tells wether the status has been changed successfully.
	 */
	protected static boolean setEnabled(Object obj, boolean status)
	{
		if (obj != null)
		{
			try
			{
				// Reflection!!
				Method enableMethod = obj.getClass().getMethod("setEnabled",
						new Class[] { Boolean.TYPE });
				enableMethod.invoke(obj, new Object[] { new Boolean(status) });
				// LoggerManager.debug(Constants.EDITOR_LOGGER,
				// ((WoPeDAction)obj).getValue(WoPeDAction.NAME) + " " +
				// status);
				return true;
			} catch (Exception e)
			{
				LoggerManager.warn(Constants.EDITOR_LOGGER,
						"Could not change enabling status (code: "
								+ obj.getClass() + ")");
			}

		}
		return false;
	}

	protected static boolean setSelected(Object obj, boolean status)
	{
		if (obj != null)
		{
			try
			{
				// Reflection!!
				Method enableMethod = obj.getClass().getMethod("setSelected",
						new Class[] { Boolean.TYPE });
				enableMethod.invoke(obj, new Object[] { new Boolean(status) });
				return true;
			} catch (Exception e)
			{
				LoggerManager.warn(Constants.EDITOR_LOGGER,
						"Could not change selection status (code: "
								+ obj.getClass() + ")");
			}

		}
		return false;
	}

	protected static boolean setVisible(Object obj, boolean status)
	{
		if (obj != null)
		{
			try
			{
				// Reflection!!
				Method enableMethod = obj.getClass().getMethod("setVisible",
						new Class[] { Boolean.TYPE });
				enableMethod.invoke(obj, new Object[] { new Boolean(status) });
				return true;
			} catch (Exception e)
			{
				LoggerManager.warn(Constants.EDITOR_LOGGER,
						"Could not change visibility status (code: "
								+ obj.getClass() + ")");
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
	protected static void setEnabled(Vector objects, boolean status)
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
	private static void setVisible(Vector objects, boolean status)
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
	private static void setSelected(Vector objects, boolean status)
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
	 * Chnages the selected the selected, visible and enable status for all
	 * subscribed objects.
	 * 
	 * @param attribute
	 * @param status
	 */
	private void setStatus(int attribute, boolean status)
	{
		if (m_status[attribute] != status)
		{
			m_status[attribute] = status;
			setSelected(m_selected[attribute], status);
			setVisible(m_visible[attribute], status);
			setEnabled(m_enable[attribute], status);
		}
	}

	/**
	 * PropertyChanges will be fired when one of the following happens:
	 * <ul>
	 * <li>The Number of InternalFrames(Editors) changes.</li>
	 * <li>A different InternalFrame gets selected.</li>
	 * <li>The Drawmode gets changes.</li>
	 * <li>The TokenGame gets started or stopped.</li>
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
				checkWoflan();
				checkUndoRedo();
			} else if ("FrameSelection".equals(arg0.getPropertyName()))
			{
				checkDrawMode();
				checkMode();
				checkSelection();
				checkUndoRedo();
				checkActiveEditor();

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
				checkWoflan();
			}
		}

	}

	/**
	 * Will be called when the Selection changes.
	 */
	public void valueChanged(GraphSelectionEvent arg0)
	{
		checkSelection();
	}

	/**
	 * Check what elements are selected and changes the status accordingly.
	 */
	protected void checkSelection()
	{
		if (isActive())
		{
			AbstractGraph graph = null;
			Object selectedCell = null;
			if (am.getUi().getEditorFocus() != null)
			{

				graph = am.getUi().getEditorFocus().getGraph();
				selectedCell = graph.getSelectionCell();
			}
			boolean noSelection = (selectedCell == null);
			boolean routedArcSelected = false;
			boolean unroutedArcSelected = false;
			boolean transitionSelected = false;
			boolean triggeredTransitionSelected = false;
			boolean placeSelected = false;
			boolean tokenPlaceSelected = false;
			boolean subprocessSelected = false;
			boolean arcSelected = false;

			while (selectedCell instanceof GroupModel)
			{
				selectedCell = ((GroupModel) selectedCell).getMainElement();
			}
			if (selectedCell instanceof ArcModel)
			{
				arcSelected = true;
				if (((ArcModel) selectedCell).isRoute())
				{
					routedArcSelected = true;
				} else
				{
					unroutedArcSelected = true;
				}
			} else if (selectedCell instanceof SubProcessModel)
			{
				subprocessSelected = true;
				transitionSelected = false; // changed, TF
			}

			else if (selectedCell instanceof TransitionModel
					|| selectedCell instanceof OperatorTransitionModel)
			{
				transitionSelected = true;
				if (((TransitionModel) selectedCell).hasTrigger())
				{
					triggeredTransitionSelected = true;
				}
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
			}

			setStatus(NO_SELECTION, noSelection);
			setStatus(ANY_SELECTION, !noSelection);
			setStatus(WOFLAN, ConfigurationManager.getConfiguration()
					.isUseWoflan());
			setStatus(SUBPROCESS_SELECTION, subprocessSelected);
			setStatus(PLACE_SELECTION, placeSelected);
			setStatus(TOKEN_PLACE_SELECTION, tokenPlaceSelected);
			setStatus(ROUTED_ARC_SELECTION, routedArcSelected);
			setStatus(UNROUTED_ARC_SELECTION, unroutedArcSelected);
			setStatus(TRANSITION_SELECTION, transitionSelected);
			setStatus(TRIGGERED_TRANSITION_SELECTION,
					triggeredTransitionSelected);
			setStatus(ARC_SELECTION, arcSelected);
			setStatus(ELEMENT_SELECTION, !noSelection && !arcSelected);
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
		boolean orSplit = false;
		boolean subprocess = false;

		if (am.getUi().getEditorFocus() != null)
		{
			switch (am.getUi().getEditorFocus().getCreateElementType())
			{
			case (PetriNetModelElement.PLACE_TYPE):
				place = true;
				break;
			case (PetriNetModelElement.TRANS_SIMPLE_TYPE):
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
		setStatus(DRAWMODE_OR_SPLIT, orSplit);
		setStatus(DRAWMODE_SUBPROCESS, subprocess);
	}

	/**
	 * Checks wether a Editor is active.
	 * 
	 */
	protected void checkActiveEditor()
	{
		boolean noEditors = (am.getUi().getAllEditors().size() < 1);
		setStatus(WITH_EDITOR, !noEditors);
		
		if (am.getUi().getEditorFocus() != null)
		{
			setStatus(SUBPROCESS_EDITOR, !am.getUi().getEditorFocus()
					.isSubprocessEditor());
		}
		else
		{
			setStatus(SUBPROCESS_EDITOR, false);
		}
	}

	/**
	 * 
	 * 
	 */
	protected void checkWoflan()
	{
		boolean woflan = ConfigurationManager.getConfiguration().isUseWoflan();
		setStatus(WOFLAN, woflan && am.getUi().getAllEditors().size() > 0);
	}

	protected void checkMode()
	{
		boolean tokengame = false;
		boolean drawMode = false;
		if (am.getUi().getEditorFocus() != null)
		{
			tokengame = ((EditorVC) am.getUi().getEditorFocus())
					.isTokenGameMode();
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
			if (undoManager != null)
			{
				undo = undoManager.canUndo();
				redo = undoManager.canRedo();
			}
		}
		setStatus(CAN_UNDO, undo);
		setStatus(CAN_REDO, redo);
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public void notify(boolean isEmpty)
	{
		setStatus(CAN_PASTE, am.getUi().getAllEditors().size() > 0 && !isEmpty);
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
		if (isActive())
		{
			boolean pointClicked = false;
			AbstractGraph graph = null;
			Object selectedCell = null;
			if (am.getUi().getEditorFocus() != null)
			{

				graph = am.getUi().getEditorFocus().getGraph();
				selectedCell = graph.getSelectionCell();
			}
			if (selectedCell instanceof ArcModel)
			{
				Point2D clickPoint = graph.fromScreen(arg0.getPoint());
				Point2D[] points = ((ArcModel) selectedCell).getPoints();
				double minDist = Double.MAX_VALUE;
				int pos = -1;
				for (int i = 1; i < points.length - 1; i++)
				{
					if (clickPoint.distance(points[i]) < minDist)
					{
						pos = i;
						minDist = clickPoint.distance(points[i]);
					}
				}
				if (pos != -1 && minDist < 10)
				{
					pointClicked = true;
				}
			}
			setStatus(ARC_POINT, pointClicked);
		}

	}

	public void mouseReleased(MouseEvent arg0)
	{
	}
}