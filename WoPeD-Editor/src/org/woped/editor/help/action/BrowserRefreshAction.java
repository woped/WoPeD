/*
 * 
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * 
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *  
 */
/*
 * Created on Jan 20, 2005
 * 
 * @author Thomas Freytag
 *  
 */

package org.woped.editor.help.action;

import java.awt.event.ActionEvent;

import org.woped.editor.action.WoPeDAction;
import org.woped.editor.help.HelpBrowser;

@SuppressWarnings("serial")
public class BrowserRefreshAction extends WoPeDAction
{
    private HelpBrowser                 helpBrowser;

    private static final String         PROPERTIES_PREFIX = "Action.Browser.Refresh";
    private static BrowserRefreshAction c_instance;

    public static BrowserRefreshAction getInstance(HelpBrowser helpBrowser)
    {
        if (c_instance == null) c_instance = new BrowserRefreshAction(helpBrowser);
        return c_instance;
    }

    private BrowserRefreshAction(HelpBrowser helpBrowser)
    {
        super(PROPERTIES_PREFIX);
        this.helpBrowser = helpBrowser;
    }

    public void actionPerformed(ActionEvent arg0)
    {
        helpBrowser.refresh();
    }
}