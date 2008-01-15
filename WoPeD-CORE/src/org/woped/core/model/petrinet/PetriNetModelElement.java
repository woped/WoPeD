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
package org.woped.core.model.petrinet;

import java.awt.Dimension;
import java.util.Vector;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.core.Constants;
import org.woped.core.model.CreationMap;
import org.woped.core.utilities.LoggerManager;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * Basic modelelement class. 29.04.2003
 */
public abstract class PetriNetModelElement extends AbstractPetriNetModelElement
{
    private Vector m_unknownToolspecific = null;
    private BaseActivity BpelData = null;

    /**
     * Each PetriNetElement is able to return its attributes in a CreationMap;
     * 
     * @return
     */
    public CreationMap getCreationMap()
    {
        return super.getCreationMap();
    }

    /**
     * Constructor for PetriNetModelElement.
     * 
     * @param map
     */
    public PetriNetModelElement(CreationMap map)
    {
        super(map);
        if (map.getId() != null)
        {
            // ToolSpec
            if (map.getUnknownToolSpec() != null) setUnknownToolSpecs(map.getUnknownToolSpec());
        } else
        {
            LoggerManager.error(Constants.CORE_LOGGER, "It's not allowed to create a Element without id. Please use ModelElementFactory instead.");
        }
    }

    public Vector getUnknownToolSpecs()
    {
        return m_unknownToolspecific;
    }

    public void setUnknownToolSpecs(Vector unknownToolSpecs)
    {
        m_unknownToolspecific = unknownToolSpecs;
    }

    public void setSize(Dimension dim)
    {
        AttributeMap map = getAttributes();
        GraphConstants.setSize(map, dim);
        getAttributes().applyMap(map);
    }

    public void setSize(int width, int height)
    {
        setSize(new Dimension(width, height));
    }

    public int getHeight()
    {
        return (int) (GraphConstants.getSize(getAttributes()) == null ? -1 : GraphConstants.getSize(getAttributes()).getHeight());
    }

    public int getWidth()
    {
        return (int) (GraphConstants.getSize(getAttributes()) == null ? -1 : GraphConstants.getSize(getAttributes()).getWidth());
    }
    
    public void setBaseActivity(BaseActivity arg)
    {
    	this.BpelData = arg;
    }
    
    public BaseActivity getBpelData()
    {
    	Assign assign = new Assign();
    	return assign;
    	//return this.BpelData;
    }

}