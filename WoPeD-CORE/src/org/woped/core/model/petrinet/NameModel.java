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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.model.CreationMap;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 20.09.2003
 */

@SuppressWarnings("serial")
public class NameModel extends DefaultGraphCell
{

    public static final int DEFAULT_WIDTH  = 40;
    public static final int DEFAULT_HEIGHT = 18;
    private String          m_ownerId;

    /**
     * Constructor for NameModel.
     */
    public NameModel(CreationMap creationMap)
    {
        super(creationMap.getName());
//        if (creationMap.getName() != null)
//        {
//            setUserObject(creationMap.getName());
//        }
        this.m_ownerId = creationMap.getId();
//        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        AttributeMap map = getAttributes();
        GraphConstants.setFont(map, DefaultStaticConfiguration.DEFAULT_SMALLLABEL_FONT);
//        GraphConstants.setOpaque(map, false);
        // GraphConstants.setBorderColor(map, Color.black);
        GraphConstants.setEditable(map, true);
        GraphConstants.setMoveable(map, true);
        GraphConstants.setSizeable(map, false);
        GraphConstants.setAutoSize(map, true);
        GraphConstants.setInset(map, 2);
        setAttributes(map);

    }

    /**
     * Returns the ownerID.
     * 
     * @return String
     */
    public Object getOwnerId()
    {
        return m_ownerId;
    }

    /**
     * Sets the ownerID.
     * 
     * @param ownerID
     *            The ownerID to set
     */
    public void setOwnerID(String ownerId)
    {
        this.m_ownerId = ownerId;
    }

    public String getNameValue()
    {
        return (String) getUserObject();
    }

    public int getDefaultWidth()
    {
        return DEFAULT_WIDTH;
    }

    public int getDefaultHeight()
    {
        return DEFAULT_HEIGHT;
    }

    public int getX()
    {
        return (int) GraphConstants.getBounds(getAttributes()).getX();
    }

    public int getY()
    {
        return (int) GraphConstants.getBounds(getAttributes()).getY();
    }

    public void setId(String id)
    {
    // TODO Auto-generated method stub

    }

    public String getId()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getType()
    {
        return AbstractPetriNetElementModel.NAME_TYPE;
    }

    //  

    public void setSize(Dimension dim)
    {
    // NOT POSSIBLE

    }

    public void setSize(int width, int height)
    {
    // NOT POSSIBLE
    }

    public int getHeight()
    {
        return (int) (GraphConstants.getSize(getAttributes()) == null ? -1 : GraphConstants.getSize(getAttributes()).getHeight());
    }

    public int getWidth()
    {
        return (int) (GraphConstants.getSize(getAttributes()) == null ? -1 : GraphConstants.getSize(getAttributes()).getWidth());
    }

    public void setPosition(Point2D p)
    {
        setPosition((int) p.getX(), (int) p.getY());
    }

    public Point getPosition()
    {
        Rectangle2D rect = GraphConstants.getBounds(getAttributes());
        if (rect != null)
        {
            return new Point((int) rect.getX(), (int) rect.getY());
        }
        return null;
    }

    public void setPosition(int x, int y)
    {
        AttributeMap map = getAttributes();
        GraphConstants.setBounds(map, new Rectangle(x, y, getWidth(), getHeight()));
        getAttributes().applyMap(map);
    }

    public String getToolTipText()
    {
        return null;
    }

    public void setType(int type)
    {
    // NOT POSSIBLE
    }

}