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
package org.woped.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *  
 */

@SuppressWarnings("serial")
public class ElementContext implements Serializable
{
    private HashMap<Object, Object> contextMap = null;

    public static String UML_CONTEXT_AND_TYPE = "AND_TYPE";
    public static String UML_CONTEXT_XOR_TYPE = "XOR_TYPE";

    public ElementContext()
    {
        contextMap = new HashMap<Object, Object>();
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

    public Iterator<Object> getKeyIterator()
    {
        return contextMap.keySet().iterator();
    }

}
