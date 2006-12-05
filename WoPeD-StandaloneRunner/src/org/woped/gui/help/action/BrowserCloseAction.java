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
/*
 * Created on Jan 24, 2005
 *
 */
package org.woped.gui.help.action;

import java.awt.event.ActionEvent;

import org.woped.editor.action.WoPeDAction;
import org.woped.gui.help.HelpBrowser;

/**
 * @author Thomas Freytag
 *  
 */

@SuppressWarnings("serial")
public class BrowserCloseAction extends WoPeDAction
{
    private static final String       PROPERTIES_PREFIX = "Action.Browser.Close";
    private static BrowserCloseAction c_instance;
    private HelpBrowser               helpBrowser;

    public static BrowserCloseAction getInstance(HelpBrowser helpBrowser)
    {
        if (c_instance == null) c_instance = new BrowserCloseAction(helpBrowser);
        return c_instance;
    }

    private BrowserCloseAction(HelpBrowser helpBrowser)
    {
        super(PROPERTIES_PREFIX);
        this.helpBrowser = helpBrowser;
    }

    public void actionPerformed(ActionEvent arg0)
    {
        helpBrowser.close();
        helpBrowser.dispose();
    }
}