package org.woped.core.model.petrinet;

import java.io.Serializable;

/**
 * @author waschtl
 */

@SuppressWarnings("serial")
public class ResourceModel implements Serializable
{
    private String name = null;

    public ResourceModel(String name)
    {
        this.name = name;
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