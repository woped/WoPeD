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
package org.woped.core.controller;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ToolTipManager;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellView;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.Port;
import org.woped.core.Constants;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.core.model.petrinet.NameModel;
import org.woped.core.utilities.LoggerManager;

/**
 * 
 *  @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *
 * TODO DOCUMENTATION (lai)
 */
public abstract class AbstractGraph extends JGraph implements Printable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private BasicMarqueeHandler editorMarquee      = null;
    private GraphUndoManager    undoManager        = null;
    private Object lastEdited = null;

    public AbstractGraph(DefaultGraphModel model, BasicMarqueeHandler editorMarquee, GraphUndoManager undoManager, AbstractViewFactory viewFactory)
    {
        if (undoManager != null)
        {
            this.undoManager = undoManager;
            this.getModel().addUndoableEditListener(undoManager);
        }
        this.setModel(model);
        this.setGraphLayoutCache(new LayoutCache(model, viewFactory));
        this.setGridVisible(ConfigurationManager.getConfiguration().isShowGrid());
        ToolTipManager.sharedInstance().registerComponent(this);
        this.setMarqueeHandler(editorMarquee);
        this.editorMarquee = editorMarquee;

    }

    abstract public void undo();

    abstract public void redo();

    abstract public boolean isValidConnection(AbstractPetriNetElementModel sourceCell, AbstractPetriNetElementModel targetCell);

    abstract public void drawNet(PetriNetModelProcessor processor);

    /**
     * Groups an <code>NameModel</code> ans all additional cells to their
     * <code>PetriNetModelElement</code>.
     * 
     * @param mainElement
     *            PetriNetModelElement
     * @param name
     *            NameModel
     * @param additional
     *            other cells to group
     * @param ungroupable
     *            flag for ungrouping
     * @return
     */
    public GroupModel groupName(AbstractPetriNetElementModel mainElement, NameModel name, Object[] additional, boolean ungroupable)
    {
        return group(mainElement, name, additional, ungroupable);
    }

    public void groupSelection()
    {
        GroupModel group = groupName(null, null, getSelectionCells(), true);
        if (group != null)
        {
            getGraphLayoutCache().insertGroup(group, getSelectionCells());
            setSelectionCell(group);
       }        
     }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#getToolTipText(java.awt.event.MouseEvent)
     */
    public String getToolTipText(MouseEvent event)
    {
        Object cell = getFirstCellForLocation(event.getX(), event.getY());
        if (cell instanceof GroupModel)
        {
            cell = ((GroupModel) cell).getMainElement();
        }
        if (cell instanceof AbstractPetriNetElementModel)
        {
            return ((AbstractPetriNetElementModel) cell).getToolTipText();
        } else
        {
            return null;
        }
    }

    /**
     * Groups an <code>NameModel</code> to its
     * <code>PetriNetModelElement</code>.
     * 
     * @param mainElement
     * @param name
     * @return
     */
    public GroupModel groupName(AbstractPetriNetElementModel mainElement, NameModel name)
    {
        return groupName(mainElement, name, null, true);
    }

    public void ungroupSelection()
    {
        ungroup(getSelectionCells());
    }

    /**
     * TODO: DOCUMENTATION (xraven, silenco)
     * 
     * @param enable
     */
    public void enableMarqueehandler(boolean enable)
    {
        if (enable)
        {
            setMarqueeHandler(getEditorMarqueeHandler());
        } else
        {
            setMarqueeHandler(null);
        }
    }

    /**
     * TODO: DOCUMENTATION (xraven, silenco)
     * 
     * @return
     */
    public BasicMarqueeHandler getEditorMarqueeHandler()
    {
        return editorMarquee;
    }

    /**
     * TODO: DOCUMENTATION (xraven, silenco)
     * 
     * @return
     */
    public GraphUndoManager getUndoManager()
    {
        return undoManager;
    }


    /**
     * TODO: DOCUMENTATION (silenco)
     * 
     * @param edge
     */
    public void connect(DefaultEdge edge, boolean insertIntoCache)
    {
        Port source = (Port) edge.getSource();
        Port target = (Port) edge.getTarget();
        // TODO: move validation in Editor
        if (isValidConnection(source, target))
        {
            source.addEdge(edge);
            target.addEdge(edge);
            // Use Edge to Connect Source and Target
            ConnectionSet cs = new ConnectionSet(edge, source, target);
            // Create a Map that holds the attributes for the edge
            AttributeMap attr = edge.getAttributes();// GraphConstants.createMap();
            // Construct a Map from cells to Maps (for insert)
            Hashtable<DefaultEdge, AttributeMap> nest = new Hashtable<DefaultEdge, AttributeMap>();
            // Associate the Edge with its Attributes
            nest.put(edge, attr);
            // Insert wants an Object-Array
            Object[] arg = new Object[] { edge };
            // Insert the Edge and its Attributes
            if (insertIntoCache){
            	getGraphLayoutCache().insert(arg, nest, cs, null, null);	
            }
            //
        } else
        {
            LoggerManager.warn(Constants.CORE_LOGGER, "Not a valid connection, did nothing!");
        }

    }

    /**
     * Does the Mapping Cell <->View new. TODO: Check Performance for huge nets.
     *  
     */
    public void refreshNet()
    {
        getGraphLayoutCache().reload();
    }

    /**
     * TODO: DOCUMENTATION (silenco) TODO: move validation in Editor
     * 
     * @param source
     * @param target
     * @return
     */
    public boolean isValidConnection(Port source, Port target)
    {
        AbstractPetriNetElementModel sourceCell = (AbstractPetriNetElementModel) ((DefaultPort) source).getParent();
        AbstractPetriNetElementModel targetCell = (AbstractPetriNetElementModel) ((DefaultPort) target).getParent();
        return isValidConnection(sourceCell, targetCell);
    }

    /**
     * Groups all cells in the Array.
     * 
     * @param cells
     */
    public GroupModel group(AbstractPetriNetElementModel mainElement, NameModel name, Object[] additional, boolean ungroupable)
    {
        if (mainElement != null && name != null)
        {
            // Create Group Cell
            GroupModel group = new GroupModel(mainElement, name, additional, ungroupable);
            LoggerManager.debug(Constants.CORE_LOGGER, "Grouping of Elements created.");
            return group;
        } else
        {
            GroupModel group = new GroupModel(mainElement, name, additional, ungroupable);
            return group;
        }
    }

    /**
     * Ungroups all cells in the Array.
     * 
     * @param cells
     */
    public void ungroup(Object[] cells)
    {
        // Shortcut to the model
        GraphModel graphModel = getModel();
        GroupModel aGroup;
        // If any Cells
        if (cells != null && cells.length > 0)
        {
            // List that Holds the Groups
            ArrayList<Object> groups = new ArrayList<Object>();
            // List that Holds the Children
            ArrayList<Object> children = new ArrayList<Object>();
            // Loop Cells
            for (int i = 0; i < cells.length; i++)
            {
                // If Cell is a Group
                if (isGroup(cells[i]))
                {
                    aGroup = (GroupModel) cells[i];
                    if (aGroup.isUngroupable())
                    {
                        // Add to List of Groups
                        groups.add(cells[i]);
                        // Loop Children of Cell
                        for (int j = 0; j < graphModel.getChildCount(cells[i]); j++) {
                            // Get Child from Model
                            children.add(graphModel.getChild(cells[i], j));
                        }
                    }
                }
                // Remove Groups from Model (Without Children)
                graphModel.remove(groups.toArray());
                // Select Children
                setSelectionCells(children.toArray());
                LoggerManager.debug(Constants.CORE_LOGGER, "Grouping of Elements deleted. ");
            }
        }
    }

    /**
     * Returns <code>true</code> if the cell is a member in a group.
     * 
     * @param cell
     * @return boolean
     */
    public boolean isGroup(Object cell)
    {
        CellView view = getGraphLayoutCache().getMapping(cell, false);
        if (view != null) return !view.isLeaf();
        return false;
    }
    
	public Vector<Object> getAllCellsForLocation(double x, double y) {
		Object cell = getFirstCellForLocation(x, y);
		Object topMostCell = cell;
		Vector<Object> allCells = new Vector<Object>();
		allCells.add(cell);

		for (cell = getNextCellForLocation(cell, x, y); cell != topMostCell; cell = getNextCellForLocation(cell, x, y)) {
			allCells.add(cell);
		}
		return allCells;
	}
	
	/**
	 * @see org.jgraph.jgraph.startEditingAtCell
	 */
	@Override
	public void startEditingAtCell (Object cell){
		super.startEditingAtCell(cell);
		lastEdited = cell;
	}
	
	/**
	 * 
	 * @return the last edited cell of the graph
	 */
	public NameModel getLastEdited(){
		if(lastEdited != null){
			if(lastEdited.getClass() == NameModel.class){
				return (NameModel) lastEdited;
			}
		} 
		return null;
	}
	
	/**
	 * set the last edited cell of the graph to null
	 */
	public void setLastEditedNull(){
		lastEdited = null;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		Container parent = this.getParent();
		// Look for a parent that is an IEditor. Quite possibly, there isn't
		while ((parent!=null)&&(!(parent instanceof IEditor)))		
			parent = parent.getParent();
		if (parent != null) {
			if (((IEditor) parent).isShowingTStar())
				super.setEnabled(false);
			else
				super.setEnabled(enabled);
		}else{
			super.setEnabled(enabled);
		}
	}
	
	public abstract void setMinPreferredWidth(int width);
	
	public abstract void setMinPreferredHeight(int height);


}
