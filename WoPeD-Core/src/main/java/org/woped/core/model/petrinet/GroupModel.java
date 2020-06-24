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
package org.woped.core.model.petrinet;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.ParentMap;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 21.09.2003
 */

@SuppressWarnings("serial")
public class GroupModel extends DefaultGraphCell
{

    private boolean              ungroupable = true;
    private AbstractPetriNetElementModel m_mainElement;

    /**
     * Constructor for GroupCell.
     */
    public GroupModel(AbstractPetriNetElementModel mainElement, NameModel name, Object[] additional, boolean ungroupable)
    {
        super();
        this.ungroupable = ungroupable;
        m_mainElement = mainElement;
        AttributeMap map = getAttributes();
        GraphConstants.setEditable(map, false);
        GraphConstants.setSizeable(map, false);
        GraphConstants.setChildrenSelectable(map, true);
        GraphConstants.setInset(map, 10);
        getAttributes().applyMap(map);
    }

    /**
     * Returns the ungroupable.
     * 
     * @return boolean
     */
    public boolean isUngroupable()
    {
        return ungroupable;
    }

    /**
     * Sets the ungroupable.
     * 
     * @param ungroupable
     *            The ungroupable to set
     */
    public void setUngroupable(boolean ungroupable)
    {
        this.ungroupable = ungroupable;
    }

    public AbstractPetriNetElementModel getMainElement()
    {
        return m_mainElement;
    }

    public ParentMap getParentMap()
    {
        ParentMap parentMap = new ParentMap();
        if (getChildCount() > 0)
        {
            for (int i = 0; i < getChildCount(); i++)
            {
                parentMap.addEntry(getChildAt(i), this);
            }
        }
        return parentMap;
    }

}