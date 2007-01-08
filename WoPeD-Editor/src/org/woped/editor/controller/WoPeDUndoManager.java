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

package org.woped.editor.controller;

import java.beans.PropertyChangeEvent;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoableEdit;

import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphUndoManager;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.controller.vc.EditorVC;

/**
 * @author Thomas Pohl TODO: DOCUMENTATION (xraven)
 * 
 * Created on: 16.03.2005 Last Change on: 16.03.2005
 */

@SuppressWarnings("serial")
public class WoPeDUndoManager extends GraphUndoManager
{
    protected EditorVC m_editor;

    protected boolean  m_enabled = true;

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param petriNet
     */
    public WoPeDUndoManager(EditorVC petriNet)
    {
        m_editor = petriNet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.UndoableEditListener#undoableEditHappened(javax.swing.event.UndoableEditEvent)
     */
    public void undoableEditHappened(UndoableEditEvent e)
    {
        super.undoableEditHappened(e);
        VisualController.getInstance().propertyChange(new PropertyChangeEvent(this, "UndoRedo", null, null));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jgraph.graph.GraphUndoManager#undo(java.lang.Object)
     */
    public void undo(Object arg0)
    {
        super.undo(arg0);
        VisualController.getInstance().propertyChange(new PropertyChangeEvent(this, "UndoRedo", null, null));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jgraph.graph.GraphUndoManager#redo(java.lang.Object)
     */
    public void redo(Object arg0)
    {
        super.redo();
        VisualController.getInstance().propertyChange(new PropertyChangeEvent(this, "UndoRedo", null, null));
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoManager#discardAllEdits()
     */
    public synchronized void discardAllEdits()
    {
        super.discardAllEdits();
        VisualController.getInstance().propertyChange(new PropertyChangeEvent(this, "UndoRedo", null, null));
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.undo.UndoableEdit#addEdit(javax.swing.undo.UndoableEdit)
     */
    public synchronized boolean addEdit(UndoableEdit arg0)
    {
        if (m_enabled)
        {
            WoPeDUndoableEdit edit = new WoPeDUndoableEdit((DefaultGraphModel.GraphModelEdit) arg0, m_editor);
            LoggerManager.debug(Constants.EDITOR_LOGGER, edit.toString());
            return super.addEdit(edit);
        }
        return false;
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @return
     */
    public boolean isEnabled()
    {
        return m_enabled;
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param disabled
     */
    public void setEnabled(boolean disabled)
    {
        m_enabled = disabled;
    }
}