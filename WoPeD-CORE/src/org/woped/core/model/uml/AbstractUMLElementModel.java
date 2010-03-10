package org.woped.core.model.uml;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphConstants;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.AbstractModelProcessor;
import org.woped.core.model.CreationMap;

public abstract class AbstractUMLElementModel extends AbstractElementModel implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// UML Types
    public static final int ACTIVITY_TYPE = 20;
    public static final int STATE_TYPE    = 21;
    public static final int OPERATOR_TYPE = 22;

    public AbstractUMLElementModel(CreationMap creationMap)
    {
        this(creationMap, null);
    }

    public AbstractUMLElementModel(CreationMap creationMap, Object userObject)
    {
        super(creationMap, userObject, AbstractModelProcessor.MODEL_PROCESSOR_UML);
        AttributeMap attributes = getAttributes();
        GraphConstants.setMoveable(attributes, true);
        GraphConstants.setEditable(attributes, false);
        GraphConstants.setSizeable(attributes, false);
        setAttributes(attributes);
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

}
