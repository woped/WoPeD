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

import org.jgraph.graph.GraphConstants;
import org.woped.core.model.CreationMap;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 10.05.2003
 */
public class TriggerModel extends AbstractPetriNetModelElement
{

    private int             triggertype;
    private String          triggerId;
    private String          ownerId;

    public static final int TRIGGER_RESOURCE = 200;
    public static final int TRIGGER_EXTERNAL = 201;
    public static final int TRIGGER_TIME     = 202;

    public static final int DEFAULT_WIDTH    = 24;
    public static final int DEFAULT_HEIGHT   = 22;

    /**
     * Constructor for TriggerModel.
     * 
     * @param type
     * @param jGraphModel
     */
    public TriggerModel(String ownerId, CreationMap map)
    {
        super(map);
        this.setId("trigger"+map.getId());
        this.setTriggertype(map.getTriggerType());
        this.setOwnerId(ownerId);
//        AttributeMap attributes = getAttributes();
//        GraphConstants.setMoveable(attributes, true);
//        GraphConstants.setEditable(attributes, false);
//        GraphConstants.setSizeable(attributes, false);
//        GraphConstants.setBounds(attributes, new Rectangle(DEFAULT_WIDTH,DEFAULT_HEIGHT));
//        setAttributes(attributes);
    }

    public void setId(String triggerId)
    {
    // TODO Auto-generated method stub
    }

    public String getId()
    {
        return triggerId;
    }

    /**
     * Returns the triggertype.
     * 
     * @return int
     */
    public int getTriggertype()
    {
        return triggertype;
    }

    /**
     * Sets the triggertype.
     * 
     * @param triggertype
     *            The triggertype to set
     */
    public void setTriggertype(int triggertype)
    {
        this.triggertype = triggertype;
    }

    /**
     * Returns the ownerId.
     * 
     * @return String
     */
    public String getOwnerId()
    {
        return ownerId;
    }

    /**
     * Sets the ownerId.
     * 
     * @param ownerId
     *            The ownerId to set
     */
    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    /**
     * TODO: Documention
     */
    public int getDefaultWidth()
    {
        return DEFAULT_WIDTH;
    }

    /**
     * TODO: Documentation
     */
    public int getDefaultHeight()
    {
        return DEFAULT_HEIGHT;
    }

//    public int getX()
//    {
//        return (int) GraphConstants.getBounds(getAttributes()).getX();
//    }
//
//    public int getY()
//    {
//        return (int) GraphConstants.getBounds(getAttributes()).getY();
//    }

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

//    public Point getPosition()
//    {
//        Rectangle2D rect = GraphConstants.getBounds(getAttributes());
//        if (rect != null)
//        {
//            return new Point((int) rect.getX(), (int) rect.getY());
//        }
//        return null;
//    }

    public String getToolTipText()
    {
        return null;
    }

//    public void setPosition(Point2D p)
//    {
//        setPosition((int) p.getX(), (int) p.getY());
//    }
//
//    public void setPosition(int x, int y)
//    {
//        AttributeMap map = getAttributes();
//        Rectangle2D r = GraphConstants.getBounds(map);
//        if (r==null)
//            r=new Rectangle();
//        GraphConstants.setBounds(map, new Rectangle(x, y, (int) r.getWidth(), (int) r.getHeight()));
//        changeAttributes(map);
//    }

    public int getType()
    {
        return PetriNetModelElement.TRIGGER_TYPE;
    }
}