package org.woped.editor.controller;

import java.util.Arrays;
import java.util.Map;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.ParentMap;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.model.petrinet.TriggerModel;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Utils;
import org.woped.editor.Constants;
import org.woped.editor.controller.vc.EditorVC;

public class WoPeDJGraphGraphModel extends DefaultGraphModel {

	private static final long serialVersionUID = -3150678294281824631L;
	private EditorVC editor;

    public WoPeDJGraphGraphModel(EditorVC editor) {
        super();
        this.editor = editor;
    }

    /**
     * createEdit(inserted, removed, attributes, cs, pm, edits)
     * <p>
     * This method is overwritten to supply a custom undoable edit object
     * that will take care of the custom model (ModelElementContainer et al)
     * while performing undo / redo
     */
    protected DefaultGraphModel.GraphModelEdit createEdit(Object[] inserted,
                                                          Object[] removed,
                                                          Map attributes,
                                                          ConnectionSet cs,
                                                          ParentMap pm,
                                                          UndoableEdit[] edits) {

        DefaultGraphModel.GraphModelEdit edit = new WoPeDUndoableEdit(inserted,
                removed, attributes, cs, pm, editor);
        if (edit != null) {
            if (edits != null)
                for (int i = 0; i < edits.length; i++)
                    edit.addEdit(edits[i]);
            edit.end();
        }
        return edit;
    }

    @SuppressWarnings("serial")
	public class WoPeDUndoableEdit extends DefaultGraphModel.GraphModelEdit
	{
	    Object[] inserted;
        Object[] removed;
        private EditorVC m_editor;

	    /**
	     * TODO: DOCUMENTATION (xraven)
         *
         * @param inserted
         * @param removed
         * @param attributes
         * @param connectionSet
         * @param parentMap
         * @param editor
         */
        WoPeDUndoableEdit(Object[] inserted, Object[] removed,
                          Map<?, ?> attributes, ConnectionSet connectionSet, ParentMap parentMap,
                          EditorVC editor) {
	    	super(inserted, removed, attributes, connectionSet, parentMap);
	    	this.inserted = inserted;
	    	this.removed = removed;
	        m_editor = editor;
	    }

	    /*
         * (non-Javadoc)
	     *
	     * @see javax.swing.undo.UndoableEdit#die()
	     */
	    public void die()
	    {
	        LoggerManager.debug(Constants.EDITOR_LOGGER, "Edit is dying!");
	        super.die();
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
	        deleteElements(inserted);
	        // Perform undo for view
	        super.undo();
	        // Perform delete undo for model
	        insertElements(removed);

//	        LoggerManager.debug(Constants.EDITOR_LOGGER, "UNDO\n" + toString());
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
	        deleteElements(removed);
	        // Perform undo for view
            super.redo();
            // Perform insert redo for model
	        insertElements(inserted);

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
         * @param elementsInput
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
                        m_editor.getModelProcessor().insertArc(((ArcModel) elements[i]), true);
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

	    public String toString()
	    {
	        Object[] inserted = getInserted();
	        Object[] removed = getRemoved();
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
	
}
