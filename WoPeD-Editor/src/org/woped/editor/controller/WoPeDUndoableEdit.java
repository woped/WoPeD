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
 * Created on Feb 11, 2005
 */
package org.woped.editor.controller;

import java.util.Arrays;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import org.jgraph.graph.DefaultGraphModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.GroupModel;
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
     * @see javax.swing.undo.UndoableEdit#undo()
     */
    public void undo() throws CannotUndoException
    {
    	// @TODO: The order in which we update the model is determined by the fact that we are using the view
    	// to update the model here, e.g. we use the view's undo manager to restore information about deleted arcs.
    	// This is why we have to perform insert operations into the model after its view has been updated.
    	
    	// Perform insert undo for model
        deleteElements(m_inserted);
        // Perform undo for view
        m_innerEdit.undo();
        // Perform delete undo for model
        insertElements(m_removed);
        
//        LoggerManager.debug(Constants.EDITOR_LOGGER, "UNDO\n" + toString());
    }    

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#redo()
     */
    public void redo() throws CannotRedoException
    {
    	// @TODO: The order in which we update the model is determined by the fact that we are using the view
    	// to update the model here, e.g. we use the view's undo manager to restore information about deleted arcs.
    	// This is why we have to perform insert operations into the model after its view has been updated
    	
    	// Perform delete redo for model
        deleteElements(m_removed);
        // Perform undo for view
        m_innerEdit.redo();    	
        // Perform insert redo for model
        insertElements(m_inserted);

 //       LoggerManager.debug(Constants.EDITOR_LOGGER, "REDO\n" + toString());
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
                if (elements[i] instanceof AbstractPetriNetElementModel)
                {
                    m_editor.getModelProcessor().getElementContainer().addElement((AbstractPetriNetElementModel) elements[i]);
                } else if (elements[i] instanceof ArcModel)
                {
                	m_editor.getGraph().connect(((ArcModel) elements[i]), false);
                    m_editor.getModelProcessor().getElementContainer().addReference((ArcModel) elements[i]);
                    ((PetriNetModelProcessor)m_editor.getModelProcessor()).insertArc(((ArcModel) elements[i]), true);
                } else if (elements[i] instanceof GroupModel)
                {
                    AbstractPetriNetElementModel mainModel = ((GroupModel)elements[i]).getMainElement();
                    if (!Arrays.asList(elements).contains(mainModel))
                        m_editor.getModelProcessor().getElementContainer().addElement(mainModel);
                } else if (elements[i] instanceof TriggerModel)
                {
                    TriggerModel trigger = (TriggerModel) elements[i];
                    ((TransitionModel) ((GroupModel) trigger.getParent()).getMainElement()).getToolSpecific().setTrigger((TriggerModel) elements[i]);
                } else
                {
                    LoggerManager.debug(Constants.EDITOR_LOGGER, "Could not insert type:" + elements[i].getClass());
                }
            }
        }
        // Update the associated tree model for the side-bar view. 
        // @TODO: This is sort of hackish, but in fact the whole solution is:
        //
        // The tree model is a model trying to follow another model (the one used for the jgraph based
        // views). The tree model is fed with content of the other model, but changes are triggered
        // by its view. Now in the case of undo / redo, we adjust the model after updating the view,
        // because we use some information from the view to restore the model.
        // This means that when the tree model update is triggered, we really don't have our jgraph model
        // in a state where it can be used as a reference (it will still reflect the state
        // before redo / undo).
        //
        // Hence, we manually trigger another update of the tree model here, once the undo / redo
        // has been applied to the jgraph model
        m_editor.getEditorPanel().GetTreeModel().refresh();
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
                if (removed[i] != null)
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