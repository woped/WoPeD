package org.woped.core.model.petrinet;

import java.io.Serializable;

/**
 * @author waschtl
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ResourceClassModel implements Serializable
{
    private String          name         = null;
    private int             type         = -1;

    public static final int TYPE_ROLE    = 0;
    public static final int TYPE_ORGUNIT = 1;

    public ResourceClassModel(String name, int type)
    {
        this.name = name;
        this.type = type;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the type.
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getName();
    }
}