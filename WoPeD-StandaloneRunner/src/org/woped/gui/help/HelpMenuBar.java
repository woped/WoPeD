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
package org.woped.gui.help;

/*
 * Created on 31.07.2004
 * @author Thomas Freytag
 */
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.woped.editor.utilities.Messages;
import org.woped.gui.help.action.BrowserBackAction;
import org.woped.gui.help.action.BrowserCloseAction;
import org.woped.gui.help.action.BrowserContentsAction;
import org.woped.gui.help.action.BrowserForwardAction;
import org.woped.gui.help.action.BrowserHomeAction;
import org.woped.gui.help.action.BrowserRefreshAction;
import org.woped.gui.help.action.BrowserWebpageAction;

/**
 * @author <a href="mailto:freytag@ba-karlsruhe.de">Thomas Freytag </a> <br>
 *         TODO: DOCUMENTATION (tfreytag) Represents the HelpMenubar
 *  
 */

@SuppressWarnings("serial")
public class HelpMenuBar extends JMenuBar
{

    private static HelpMenuBar c_instance         = null;

    private HelpBrowser        m_containingWindow;

    private JMenu              m_fileMenu         = null;
    private JMenuItem          m_closeMenuItem    = null;

    private JMenu              m_navigateMenu     = null;
    private JMenuItem          m_backMenuItem     = null;
    private JMenuItem          m_homeMenuItem     = null;
    private JMenuItem          m_contentsMenuItem = null;
    private JMenuItem          m_refreshMenuItem  = null;
    private JMenuItem          m_forwardMenuItem  = null;
    private JMenuItem          m_wwwMenuItem      = null;

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * 
     * @param controlledWindow
     * @return
     */
    public static HelpMenuBar getInstance(HelpBrowser controlledWindow)
    {
        if (c_instance == null)
        {
            c_instance = new HelpMenuBar(controlledWindow);
        }
        return c_instance;
    }

    /**
     * Creates a new MenuBar for the HelpBrowser.
     * 
     * @param containingWindow
     *            Specifies the Window which contains this MenuBar.
     */
    private HelpMenuBar(HelpBrowser containingWindow)
    {
        m_containingWindow = containingWindow;
        add(getFileMenu());
        add(getNavigateMenu());
    }

    /**
     * @return Returns the containingWindow.
     */
    public HelpBrowser getContainingWindow()
    {
        return m_containingWindow;
    }

    /**
     * Creates the navigateMenu if necessary.
     * 
     * @return Returns the navigateMenu.
     */
    public JMenu getNavigateMenu()
    {
        if (m_navigateMenu == null)
        {
            m_navigateMenu = new JMenu(Messages.getString("Menu.Browser.Navigate.Title"));
            m_navigateMenu.setMnemonic(KeyEvent.VK_N);

            m_navigateMenu.add(getBackMenuItem());
            m_navigateMenu.add(getForwardMenuItem());
            m_navigateMenu.addSeparator();
            m_navigateMenu.add(getHomeMenuItem());
            m_navigateMenu.add(getContentsMenuItem());
            m_navigateMenu.add(getRefreshMenuItem());
            m_navigateMenu.addSeparator();
            m_navigateMenu.add(getWWWMenuItem());
        }
        return m_navigateMenu;
    }

    /**
     * Creates the fileMenu if necessary.
     * 
     * @return Returns the fileMenu.
     */
    public JMenu getFileMenu()
    {
        if (m_fileMenu == null)
        {
            m_fileMenu = new JMenu(Messages.getString("Menu.Browser.File.Title"));
            m_fileMenu.setMnemonic(KeyEvent.VK_F);
            m_fileMenu.add(getCloseMenuItem());

        }
        return m_fileMenu;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * 
     * @return
     */
    public JMenuItem getCloseMenuItem()
    {
        if (m_closeMenuItem == null)
        {
            m_closeMenuItem = new JMenuItem(BrowserCloseAction.getInstance(getContainingWindow()));
        }
        return m_closeMenuItem;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * 
     * @return
     */
    public JMenuItem getRefreshMenuItem()
    {
        if (m_refreshMenuItem == null)
        {
            m_refreshMenuItem = new JMenuItem(BrowserRefreshAction.getInstance(getContainingWindow()));
        }
        return m_refreshMenuItem;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * 
     * @return
     */
    public JMenuItem getHomeMenuItem()
    {
        if (m_homeMenuItem == null)
        {
            m_homeMenuItem = new JMenuItem(BrowserHomeAction.getInstance(getContainingWindow()));
        }
        return m_homeMenuItem;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * 
     * @return
     */
    public JMenuItem getContentsMenuItem()
    {
        if (m_contentsMenuItem == null)
        {
            m_contentsMenuItem = new JMenuItem(BrowserContentsAction.getInstance(getContainingWindow()));
        }
        return m_contentsMenuItem;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * 
     * @return
     */
    public JMenuItem getForwardMenuItem()
    {
        if (m_forwardMenuItem == null)
        {
            m_forwardMenuItem = new JMenuItem(BrowserForwardAction.getInstance(getContainingWindow()));
        }
        return m_forwardMenuItem;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * 
     * @return
     */
    public JMenuItem getBackMenuItem()
    {
        if (m_backMenuItem == null)
        {
            m_backMenuItem = new JMenuItem(BrowserBackAction.getInstance(getContainingWindow()));
        }
        return m_backMenuItem;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * 
     * @return
     */
    public JMenuItem getWWWMenuItem()
    {
        if (m_wwwMenuItem == null)
        {
            m_wwwMenuItem = new JMenuItem(BrowserWebpageAction.getInstance(getContainingWindow()));
        }
        return m_wwwMenuItem;
    }
}