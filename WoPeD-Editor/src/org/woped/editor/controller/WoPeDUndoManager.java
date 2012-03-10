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

package org.woped.editor.controller;

import java.beans.PropertyChangeEvent;

import javax.swing.JOptionPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphUndoManager;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.SubProcessModel;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.translations.Messages;

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
    
    public void clear(){
    	m_editor = null;
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
    	Boolean doIt = true;
    	if (arg0 == null || !isInProgress())
    		super.undo();
    	else {
    		WoPeDUndoableEdit edit = (WoPeDUndoableEdit) super.editToBeUndone();
    		if (edit == null)
    			throw new CannotUndoException();
    		doIt = true;
    		if (edit.m_inserted != null){
    			for (int i = 0; i < edit.m_inserted.length; i++){
    				
    				if (edit.m_inserted[i] instanceof SubProcessModel){
    				Object[] options = {
    						Messages.getString("Popup.Confirm.SubProcess.Ok"),
    						Messages.getString("Popup.Confirm.SubProcess.No") };
    				int j = JOptionPane.showOptionDialog(null,
    						Messages.getString("Popup.Confirm.SubProcess.Info"),
    						Messages.getString("Popup.Confirm.SubProcess.Warn"),
    						JOptionPane.DEFAULT_OPTION,
    						JOptionPane.WARNING_MESSAGE, null, options, options[0]);
    				if ( j != 0) doIt = false;
    				}
    			}
    		}
    		if (doIt) super.undoTo(edit);
    	}
    	
//        super.undo(arg0);
        VisualController.getInstance().propertyChange(new PropertyChangeEvent(this, "UndoRedo", null, null));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jgraph.graph.GraphUndoManager#redo(java.lang.Object)
     */
    public void redo(Object arg0)
    {Boolean doIt = true;
	if (arg0 == null || !isInProgress())
		super.redo();
	else {
		WoPeDUndoableEdit edit = (WoPeDUndoableEdit) super.editToBeRedone();
		if (edit == null)
			throw new CannotRedoException();
		doIt = true;
		if (edit.m_removed != null){
			for (int i = 0; i < edit.m_removed.length; i++){
				
				if (edit.m_removed[i] instanceof SubProcessModel){
				Object[] options = {
						Messages.getString("Popup.Confirm.SubProcess.Ok"),
						Messages.getString("Popup.Confirm.SubProcess.No") };
				int j = JOptionPane.showOptionDialog(null,
						Messages.getString("Popup.Confirm.SubProcess.Info"),
						Messages.getString("Popup.Confirm.SubProcess.Warn"),
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				if ( j != 0) doIt = false;
				}
			}
		}
		if (doIt) super.redoTo(edit);
	}
	
    //    super.redo();
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
//            LoggerManager.debug(Constants.EDITOR_LOGGER, edit.toString());
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
    
    
    // Added by Gregor Becker. Makes sure that Subprocess Start and End Token are not deleted!
    public boolean canUndo(ModelElementContainer container){
    	if (super.canUndo())
    	{WoPeDUndoableEdit edit = (WoPeDUndoableEdit) super.editToBeUndone();
    	
    	if (edit.m_inserted != null)    
    		return (container.getIdMap().size() > 2);
    	else return true;
    }return false;
    }
    
}