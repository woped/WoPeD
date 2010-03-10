package org.woped.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

@SuppressWarnings("serial")
public class ModelProcessorContext implements Serializable
{
    private HashMap<Object, Object> contextMap = null;

    public ModelProcessorContext()
    {
        contextMap = new HashMap<Object, Object>();
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

    public Iterator<Object> getKeyIterator()
    {
        return contextMap.keySet().iterator();
    }
}
