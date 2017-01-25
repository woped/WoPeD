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
/*
 * Created on Oct 29, 2004
 *
 */
package org.woped.editor.action;


import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

/**
 * Disposes the Window the caller is in.
 * 
 * @author Thomas Pohl
 */

@SuppressWarnings("serial")
public class DisposeWindowAction extends WoPeDAction
{
    public DisposeWindowAction(String propertiesPrefix)
    {
        super(propertiesPrefix);
    }

    public DisposeWindowAction()
    {
        super("Window.Dispose");
    }

    public void actionPerformed(ActionEvent arg0)
    {
        Component source = (Component) arg0.getSource();
        while (source != null && !(source instanceof Window))
        {
            source = source.getParent();
        }
        if (source instanceof Window)
        {
            ((Window) source).dispose();
        }
    }

}