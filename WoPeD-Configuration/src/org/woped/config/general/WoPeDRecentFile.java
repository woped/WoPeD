package org.woped.config.general;

public class WoPeDRecentFile
{
    private String name = null;
    private String path = null;

    public WoPeDRecentFile(String name, String path)
    {
        this.name = name;
        this.path = path;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }
}