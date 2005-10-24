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
package org.woped.model.petrinet;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.GraphModel;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 20.09.2003
 */
public class GraphTreeModel extends DefaultTreeModel implements GraphModelListener
{

    public GraphTreeModel(GraphModel model)
    {
        super(new GraphModelTreeNode(model));
    }

    public void graphChanged(GraphModelEvent e)
    {
        reload();
    }

    public static class GraphModelTreeNode implements TreeNode
    {

        protected GraphModel model;

        public GraphModelTreeNode(GraphModel model)
        {
            this.model = model;
        }

        public Enumeration children()
        {
            Vector v = new Vector();
            for (int i = 0; i < model.getRootCount(); i++)
                v.add(model.getRootAt(i));
            return v.elements();
        }

        public boolean getAllowsChildren()
        {
            return true;
        }

        public TreeNode getChildAt(int childIndex)
        {
            return (TreeNode) model.getRootAt(childIndex);
        }

        public int getChildCount()
        {
            return model.getRootCount();
        }

        public int getIndex(TreeNode node)
        {
            return model.getIndexOfRoot(node);
        }

        public TreeNode getParent()
        {
            return null;
        }

        public boolean isLeaf()
        {
            return false;
        }

        public String toString()
        {
            return model.toString();
        }

    }

}

