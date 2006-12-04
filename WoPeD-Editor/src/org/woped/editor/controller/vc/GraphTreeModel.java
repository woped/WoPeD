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
package org.woped.editor.controller.vc;

import java.util.Iterator;

import javax.swing.tree.DefaultTreeModel;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ModelElementContainer;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 20.09.2003
 */
public class GraphTreeModel extends DefaultTreeModel implements GraphModelListener
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

    public void graphChanged(GraphModelEvent e)
    {
        PopulateNode((NetInfo)getRoot(),
        		parentEditor.getModelProcessor().getElementContainer());
        reload();        
    }
    
    public void PopulateNode(NetInfo root,
    		ModelElementContainer elements)
    {
    	root.removeAllChildren();
		Iterator i=elements.getRootElements().iterator();
		while (i.hasNext())
		{
			AbstractElementModel currentNode =				
				(AbstractElementModel)i.next();
			root.add(new NodeNetInfo(currentNode, true));
		}
    }
}
