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
package org.woped.editor.controller.vc;

import java.util.Iterator;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.qualanalysis.sidebar.expert.components.NetInfo;
import org.woped.qualanalysis.sidebar.expert.components.NodeNetInfo;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 20.09.2003
 */

//! Our graph tree model can receive update events from both the graph model as well as 
//! other tree models to keep tree models in sync with each other

@SuppressWarnings("serial")
public class GraphTreeModel extends DefaultTreeModel implements GraphModelListener, TreeModelListener
{

	private IEditor parentEditor = null;
	
    public GraphTreeModel(IEditor currentEditor)
    {
    	super(new NetInfo(currentEditor.getGraph().getModel().toString()));
    	parentEditor = currentEditor;
    	PopulateNode((NetInfo)getRoot(),
    			parentEditor.getModelProcessor().getElementContainer());
    	reload();
    }

    public void refresh()
    {
        PopulateNode((NetInfo)getRoot(),
        		parentEditor.getModelProcessor().getElementContainer());
        reload();            	
    }
    public void graphChanged(GraphModelEvent e)
    {
    	refresh();
    }
    
    public void PopulateNode(NetInfo root,
    		ModelElementContainer elements)
    {
    	root.removeAllChildren();
		Iterator<AbstractPetriNetElementModel> i=elements.getRootElements().iterator();
		while (i.hasNext())
		{
			AbstractPetriNetElementModel currentNode = i.next();
			root.add(new NodeNetInfo(currentNode, true));
		}
    }
   
    public void treeNodesChanged(TreeModelEvent e)
    {
    	refresh();
    }
    public void treeNodesInserted(TreeModelEvent e)
    {
    	refresh();
    }
    public void treeNodesRemoved(TreeModelEvent e)
    {
    	refresh();
    }
    public void treeStructureChanged(TreeModelEvent e)
    {
    	refresh();
    }
}
