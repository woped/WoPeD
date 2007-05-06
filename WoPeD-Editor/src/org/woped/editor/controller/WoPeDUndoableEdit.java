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
 * Created on Feb 11, 2005
 */
package org.woped.editor.controller;

import java.util.Arrays;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import org.jgraph.graph.DefaultGraphModel;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Utils;
import org.woped.editor.Constants;
import org.woped.editor.controller.vc.EditorVC;

/**
 * @author Thomas Pohl TODO: DOCUMENTATION (xraven)
 */
public class WoPeDUndoableEdit implements UndoableEdit
{

    private DefaultGraphModel.GraphModelEdit m_innerEdit;
    private EditorVC                         m_editor;
    protected Object[]                       m_inserted;
    protected Object[]                       m_removed;

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param innerEdit
     * @param editor
     */
    WoPeDUndoableEdit(DefaultGraphModel.GraphModelEdit innerEdit, EditorVC editor)
    {
        m_innerEdit = innerEdit;
        m_editor = editor;
        m_inserted = innerEdit.getInserted();
        m_removed = innerEdit.getRemoved();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#die()
     */
    public void die()
    {
        LoggerManager.debug(Constants.EDITOR_LOGGER, "Edit is dying!");
        m_innerEdit.die();
        m_inserted = null;
        m_removed = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#redo()
     */
    public void redo() throws CannotRedoException
    {
        deleteElements(m_innerEdit.getInserted());
        m_innerEdit.redo();
        insertElements(m_innerEdit.getInserted());
        LoggerManager.debug(Constants.EDITOR_LOGGER, "REDO\n" + toString());
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param removed
     */
    private void deleteElements(Object[] removed)
    {
        if (removed != null)
        {
            m_editor.deleteOnlyCells(removed, false);
        }
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param inserted
     */
    private void insertElements(Object[] elementsInput)
    {
        Object[] elements = Utils.sortArcsLast(elementsInput);
        if (elements != null)
        {
            for (int i = 0; i < elements.length; i++)
            {
                LoggerManager.debug(Constants.EDITOR_LOGGER, "Trying to create " + elements[i].getClass());
                if (elements[i] instanceof PetriNetModelElement)
                {
                    // m_editor.createPetriNetElement(((PetriNetModelElement)
                    // elements[i]).getCreationMap(), false);
                    m_editor.getModelProcessor().getElementContainer().addElement((PetriNetModelElement) elements[i]);
                } else if (elements[i] instanceof ArcModel)
                {
                    // m_editor.createArc(((ArcModel)
                    // elements[i]).getCreationMap(), false);
//                    m_editor.getModelProcessor().getElementContainer().addReference((ArcModel) elements[i]);
                    ArcModel newArc = m_editor.getModelProcessor().createArc(((ArcModel)elements[i]).getSourceId(), ((ArcModel)elements[i]).getTargetId());
                    newArc.setAttributes(((ArcModel)elements[i]).getAttributes());
                    //PetriNetModelProcessor createArc
                } else if (elements[i] instanceof GroupModel)
                {
                    AbstractElementModel mainModel = ((GroupModel)elements[i]).getMainElement();
                    if (!Arrays.asList(elements).contains(mainModel))
                        m_editor.getModelProcessor().getElementContainer().addElement(mainModel);
                } else if (elements[i] instanceof TriggerModel)
                {
                    TriggerModel trigger = (TriggerModel) elements[i];
                    ((TransitionModel) ((GroupModel) trigger.getParent()).getMainElement()).getToolSpecific().setTrigger((TriggerModel) elements[i]);
                    // m_editor.getPetriNet().getElementContainer().getElementById(trigger.getOwnerId());
                    // m_editor.createTrigger((TransitionModel)m_editor.getPetriNet().getElementContainer().getElementById(trigger.getOwnerId()),
                    // trigger.getTriggertype(), false);
                } else
                {
                    LoggerManager.debug(Constants.EDITOR_LOGGER, "Could not insert type:" + elements[i].getClass());
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#undo()
     */
    public void undo() throws CannotUndoException
    {
        deleteElements(m_innerEdit.getInserted());
        m_innerEdit.undo();
        insertElements(m_innerEdit.getInserted());
        LoggerManager.debug(Constants.EDITOR_LOGGER, "UNDO\n" + toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#canRedo()
     */
    public boolean canRedo()
    {
        return m_innerEdit.canRedo();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#canUndo()
     */
    public boolean canUndo()
    {
        return m_innerEdit.canUndo();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#isSignificant()
     */
    public boolean isSignificant()
    {
        return m_innerEdit.isSignificant();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#getPresentationName()
     */
    public String getPresentationName()
    {
        return m_innerEdit.getPresentationName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#getRedoPresentationName()
     */
    public String getRedoPresentationName()
    {
        return m_innerEdit.getRedoPresentationName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#getUndoPresentationName()
     */
    public String getUndoPresentationName()
    {
        return m_innerEdit.getUndoPresentationName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#addEdit(javax.swing.undo.UndoableEdit)
     */
    public boolean addEdit(UndoableEdit arg0)
    {
        return m_innerEdit.addEdit(arg0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#replaceEdit(javax.swing.undo.UndoableEdit)
     */
    public boolean replaceEdit(UndoableEdit arg0)
    {
        return m_innerEdit.replaceEdit(arg0);
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @return
     */
    public DefaultGraphModel.GraphModelEdit getInnerEdit()
    {
        return m_innerEdit;
    }

    public String toString()
    {
        Object[] inserted = getInnerEdit().getInserted();
        Object[] removed = getInnerEdit().getRemoved();
        StringBuffer out = new StringBuffer();
        if (inserted == null || inserted.length == 0)
        {
            out.append("None inserted.\n");
        } else
        {
            out.append("Inserted:");
            for (int i = 0; i < inserted.length; i++)
            {
                if (inserted != null)
                {
                    out.append(inserted[i].getClass());
                    out.append(":");
                }
                out.append(inserted[i]);
                out.append("\n");
            }
        }
        if (removed == null || removed.length == 0)
        {
            out.append("None removed.\n");
        } else
        {
            out.append("Removed:");
            for (int i = 0; i < removed.length; i++)
            {
                if (removed != null)
                {
                    out.append(removed[i].getClass());
                    out.append(":");
                }
                out.append(removed[i]);
                out.append("\n");
            }
        }
        return out.toString();
    }
}