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

import javax.swing.tree.DefaultMutableTreeNode;

import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.woped.core.Constants;
import org.woped.core.utilities.LoggerManager;

/**
 * @author Thomas Pohl TODO: DOCUMENTATION (xraven)
 */
/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * Created on: 16.03.2005 Last Change on: 16.03.2005
 */

@SuppressWarnings("serial")
public class LayoutCache extends GraphLayoutCache
{

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param model
     * @param factory
     */
    public LayoutCache(GraphModel model, AbstractViewFactory factory)
    {
        super(model, factory);
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param model
     * @param factory
     * @param partial
     */
    public LayoutCache(GraphModel model, AbstractViewFactory factory, boolean partial)
    {
        super(model, factory, partial);
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param model
     * @param factory
     * @param cellViews
     * @param hiddenCellViews
     * @param partial
     */
    public LayoutCache(GraphModel model, AbstractViewFactory factory, CellView[] cellViews, CellView[] hiddenCellViews, boolean partial)
    {
        super(model, factory, cellViews, hiddenCellViews, partial);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jgraph.graph.GraphLayoutCache#valueForCellChanged(java.lang.Object,
     *      java.lang.Object)
     */
    public void valueForCellChanged(Object cell, Object newValue)
    {
        if (cell instanceof DefaultMutableTreeNode)
        {
            if (((DefaultMutableTreeNode) cell).getUserObject() == null)
            {
                if (newValue == null)
                {
                    LoggerManager.debug(Constants.CORE_LOGGER, "Edit not added: No change.");
                } else
                {
                    super.valueForCellChanged(cell, newValue);
                }
            } else if (!((DefaultMutableTreeNode) cell).getUserObject().equals(newValue))
            {
                super.valueForCellChanged(cell, newValue);
            } else
            {
                LoggerManager.debug(Constants.CORE_LOGGER, "Edit not added: No change.");
            }
        } else
        {
            super.valueForCellChanged(cell, newValue);
        }
    }
}
