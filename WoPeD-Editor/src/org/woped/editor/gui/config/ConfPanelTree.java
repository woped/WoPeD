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
package org.woped.editor.gui.config;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.controller.vc.ConfigVC;
import org.woped.editor.utilities.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         The Tree listing the <code>AbstractNodePanel</code>s. Created on:
 *         26.11.2004 Last Change on: 26.11.2004
 */

@SuppressWarnings("serial")
public class ConfPanelTree extends JTree
{
    private DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

    /**
     * Constructor for ConfPanelTree.
     */
    public ConfPanelTree()
    {
        super(new DefaultMutableTreeNode(Messages.getString("Configuration.Title")));
        // this.setRootVisible(false);
        this.putClientProperty("JTree.lineStyle", "Angled");
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        renderer.setBackground(ConfigVC.BACK_COLOR);
        this.setCellRenderer(renderer);
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    public void setSelectedNode(String name)
    {
        DefaultMutableTreeNode node = findNode(name, (DefaultMutableTreeNode) getModel().getRoot());
        if (node != null)
        {
            TreePath path = new TreePath(node);
            setSelectionPath(path);
        } else
        {
            LoggerManager.warn(Constants.EDITOR_LOGGER, "Could not select node \"" + name + "\"");
        }
    }

    /**
     * Adds an <code>AbstractNodePanel</code> with the given name to the Tree.
     * 
     * @param nodePanelName
     */
    protected void addConfNodePanel(String nodePanelName)
    {
        addConfNodePanel(null, nodePanelName);
    }

    /**
     * Adds an <code>AbstractNodePanel</code> with the given name to the Tree.
     * 
     * @param nodePanelName
     */
    public void addConfNodePanel(String parentNodeName, String nodePanelName)
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodePanelName);
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) getModel().getRoot();
        if (parentNodeName == null || ((parentNode = findNode(parentNodeName, parentNode)) != null))
        {
            parentNode.add(node);
        } else
        {
            LoggerManager.warn(Constants.EDITOR_LOGGER, "Could not add \"" + nodePanelName + "\". Parent node \"" + parentNodeName + "\" does not exists.");
        }
    }

    private DefaultMutableTreeNode findNode(String name, DefaultMutableTreeNode startNode)
    {
        DefaultMutableTreeNode found = null;
        for (int i = 0; i < startNode.getChildCount(); i++)
        {
            if (((DefaultMutableTreeNode) startNode.getChildAt(i)).getUserObject().equals(name))
            {
                return (DefaultMutableTreeNode) startNode.getChildAt(i);
            } else
            {
                if ((found = findNode(name, (DefaultMutableTreeNode) startNode.getChildAt(i))) != null)
                {
                    return found;
                }
            }
        }
        return null;
    }

    /**
     * @return Returns the renderer.
     */
    protected DefaultTreeCellRenderer getRenderer()
    {
        return renderer;
    }

    /**
     * @param renderer
     *            The renderer to set.
     */
    protected void setRenderer(DefaultTreeCellRenderer renderer)
    {
        this.renderer = renderer;
    }
}