package org.woped.core.model;

import java.io.Serializable;
import java.util.HashMap;

public class ElementContext implements Serializable
{
    private HashMap      contextMap           = null;

    public static String UML_CONTEXT_AND_TYPE = "AND_TYPE";
    public static String UML_CONTEXT_XOR_TYPE = "XOR_TYPE";

    public ElementContext()
    {
        contextMap = new HashMap();
    }

    public void setANDType(boolean splitType)
    {
        addContext(UML_CONTEXT_AND_TYPE, new Boolean(splitType));
    }

    public void setXORType(boolean xorType)
    {
        addContext(UML_CONTEXT_XOR_TYPE, new Boolean(xorType));
    }

    public boolean isXORType()
    {
        if (containsContext(UML_CONTEXT_XOR_TYPE))
        {
            return (boolean) ((Boolean) getContext(UML_CONTEXT_XOR_TYPE)).booleanValue();
        }
        return false;
    }

    public boolean isANDType()
    {
        if (containsContext(UML_CONTEXT_AND_TYPE))
        {
            return (boolean) ((Boolean) getContext(UML_CONTEXT_AND_TYPE)).booleanValue();
        }
        return false;
    }

    public void addContext(Object key, Object value)
    {
        contextMap.put(key, value);
    }

    public void removeContext(Object key)
    {
        contextMap.remove(key);
    }

    public Object getContext(Object key)
    {
        return contextMap.get(key);
    }

    public boolean containsContext(Object key)
    {
        return contextMap.containsKey(key);
    }

}
