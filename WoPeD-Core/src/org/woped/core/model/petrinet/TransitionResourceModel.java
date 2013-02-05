/*
 * Created on 25.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.woped.core.model.petrinet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.woped.core.model.CreationMap;

/**
 * @author waschtl
 */

@SuppressWarnings("serial")
public class TransitionResourceModel extends DefaultGraphCell
{
    public static final int DEFAULT_WIDTH  = 60;

    public static final int DEFAULT_HEIGHT = 22; // must be even

    private String          transResourceId;

    private String          transRoleName;

    private String          transOrgUnitName;

    private String          ownerId;

    public TransitionResourceModel(CreationMap creationMap)
    {
        super();
        this.setId("transResource" + creationMap.getId());
        this.setOwnerId(creationMap.getId());
        this.transOrgUnitName = creationMap.getResourceOrgUnit();
        this.transRoleName = creationMap.getResourceRole();

        AttributeMap attrMap = getAttributes();
        GraphConstants.setOpaque(attrMap, false);
        GraphConstants.setBorderColor(attrMap, Color.black);
        GraphConstants.setEditable(attrMap, false);
        GraphConstants.setMoveable(attrMap, true);
        GraphConstants.setSizeable(attrMap, false);
        GraphConstants.setSize(attributes, new Dimension(getDefaultWidth(), getDefaultHeight()));
        // GraphConstants.setAutoSize(attrMap, true);
        // GraphConstants.setInset(attrMap, 1);
        setAttributes(attrMap);

        if (creationMap.getResourcePosition() != null)
        {
            setPosition(new Point(creationMap.getResourcePosition().x, creationMap.getResourcePosition().y));
        }
    }

    public void setId(String triggerId)
    {
    // TODO Auto-generated method stub
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

    public String getId()
    {
        return transResourceId;
    }

    /**
     * @return Returns the transOrgUnitName.
     */
    public String getTransOrgUnitName()
    {
        return transOrgUnitName;
    }

    /**
     * @param transOrgUnitName
     *            The transOrgUnitName to set.
     */
    public void setTransOrgUnitName(String transOrgUnitName)
    {
        this.transOrgUnitName = transOrgUnitName;
    }

    /**
     * @return Returns the transRoleName.
     */
    public String getTransRoleName()
    {
        return transRoleName;
    }

    /**
     * @param transRoleName
     *            The transRoleName to set.
     */
    public void setTransRoleName(String transRoleName)
    {
        this.transRoleName = transRoleName;
    }

    public String getToolTipText()
    {
        return null;
    }

    /* ### general methods ### */

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

    public int getX()
    {
        return (int) GraphConstants.getBounds(getAttributes()).getX();
    }

    public int getY()
    {
        return (int) GraphConstants.getBounds(getAttributes()).getY();
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

}