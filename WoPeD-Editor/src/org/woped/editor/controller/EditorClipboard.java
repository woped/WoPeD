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

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         Clipboard for all Editors in one MDI. <br>
 *         Created on: 16.02.2005
 */
public class EditorClipboard
{
    private HashMap copiedElementsList = new HashMap();
    private HashMap copiedArcsList     = new HashMap();

    public void clearClipboard()
    {
        getCopiedArcsList().clear();
        getCopiedElementsList().clear();
    }

    public HashMap getCopiedArcsList()
    {
        return copiedArcsList;
    }

    public void setCopiedArcsList(HashMap copiedArcsList)
    {
        this.copiedArcsList = copiedArcsList;
    }

    public HashMap getCopiedElementsList()
    {
        return copiedElementsList;
    }

    public void setCopiedElementsList(HashMap copiedElementsList)
    {
        this.copiedElementsList = copiedElementsList;
    }
}