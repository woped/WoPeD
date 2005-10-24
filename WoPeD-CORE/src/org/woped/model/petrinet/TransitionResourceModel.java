/*
 * Created on 25.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.woped.model.petrinet;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.model.CreationMap;

/**
 * @author waschtl
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TransitionResourceModel extends AbstractPetriNetModelElement
{
    public static final int DEFAULT_WIDTH  = 60;
    public static final int DEFAULT_HEIGHT = 22; // must be even
    private String          transResourceId;
    private String          transRoleName;
    private String          transOrgUnitName;
    private String          ownerId;

    public TransitionResourceModel(String ownerId, CreationMap map)
    {
        super(map);
        this.setId("transResource" + map.getId());
        this.setOwnerId(ownerId);
        this.transOrgUnitName = map.getResourceOrgUnit();
        this.transRoleName = map.getResourceRole();
        // AttributeMap attributes = getAttributes();
        // GraphConstants.setMoveable(attributes, true);
        // GraphConstants.setEditable(attributes, false);
        // GraphConstants.setSizeable(attributes, false);
        // GraphConstants.setBounds(attributes, new Rectangle(DEFAULT_WIDTH,
        // DEFAULT_HEIGHT));
        // setAttributes(attributes);
    }

    public void setId(String triggerId)
    {
    // TODO Auto-generated method stub
    }

    public String getId()
    {
        return transResourceId;
    }

    /**
     * @return Returns the ownerId.
     */
    public String getOwnerId()
    {
        return ownerId;
    }

    /**
     * @param ownerId
     *            The ownerId to set.
     */
    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
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

    public int getX()
    {
        return (int) GraphConstants.getBounds(getAttributes()).getX();
    }

    public int getY()
    {
        return (int) GraphConstants.getBounds(getAttributes()).getY();
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

    public Point getPosition()
    {
        Rectangle2D rect = GraphConstants.getBounds(getAttributes());
        if (rect != null)
        {
            return new Point((int) rect.getX(), (int) rect.getY());
        }
        return null;
    }

    public String getToolTipText()
    {
        return null;
    }

    public void setPosition(Point2D p)
    {
        setPosition((int) p.getX(), (int) p.getY());
    }

    public void setPosition(int x, int y)
    {
        AttributeMap map = getAttributes();
        Rectangle2D r = GraphConstants.getBounds(map);
        if (r == null) r = new Rectangle();
        GraphConstants.setBounds(map, new Rectangle(x, y, (int) r.getWidth(), (int) r.getHeight()));
        changeAttributes(map);
    }

    public void setType(int type)
    {
    // NOT POSSIBLE
    }

    public int getType()
    {
        return PetriNetModelElement.RESOURCE_TYPE;
    }

}