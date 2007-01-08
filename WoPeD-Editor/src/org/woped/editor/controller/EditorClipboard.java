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
package org.woped.editor.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         Clipboard for all Editors in one MDI. <br>
 *         Created on: 16.02.2005
 */
public class EditorClipboard
{
    private HashMap<String, CreationMap> copiedElementsList = new HashMap<String, CreationMap>();
    private HashMap<String, CreationMap> copiedArcsList     = new HashMap<String, CreationMap>();
    
    private Vector<IClipboaredListener> m_listeners = new Vector<IClipboaredListener>();

    public EditorClipboard()
    {
        super();
    }

    public void clearClipboard()
    {
        copiedArcsList.clear();
        copiedElementsList.clear();
        fireClipboardChange();
    }

    public Map getCopiedArcsList()
    {
        return (Map)copiedArcsList.clone();
    }

    public Map getCopiedElementsList()
    {
        return (Map)copiedElementsList.clone();
    }

     public void addClipboardListener(IClipboaredListener listener)
    {
        m_listeners.add(listener);
    }
    
    public boolean isEmpty()
    {
        return (getCopiedArcsList().size()==0 && getCopiedElementsList().size()==0);
    }
    
    private void fireClipboardChange()
    {
        for (int i = 0;i<m_listeners.size();i++)
        {
            ((IClipboaredListener)m_listeners.get(i)).notify(isEmpty());
        }
    }
    
    public void putArc(ArcModel arc)
    {
        copiedArcsList.put(arc.getId(), (CreationMap) arc.getCreationMap().clone());
        fireClipboardChange();
    }
    
    public void putElement(AbstractElementModel element)
    {
        copiedElementsList.put(element.getId(), (CreationMap) element.getCreationMap().clone());
        fireClipboardChange();
    }
    
    public boolean containsElement(String key)
    {
        return copiedElementsList.containsKey(key);
    }
}