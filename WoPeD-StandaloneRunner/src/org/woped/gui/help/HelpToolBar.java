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
 *  */
package org.woped.gui.help;

import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JToolBar;

import org.woped.action.help.BrowserBackAction;
import org.woped.action.help.BrowserCloseAction;
import org.woped.action.help.BrowserContentsAction;
import org.woped.action.help.BrowserForwardAction;
import org.woped.action.help.BrowserHomeAction;
import org.woped.action.help.BrowserRefreshAction;
import org.woped.action.help.BrowserWebpageAction;
import org.woped.gui.ToolBarButton;

/**
 * @author Thomas Freytag
 *  
 */
public class HelpToolBar extends JToolBar
{
    private static HelpToolBar c_instance       = null;

    private HelpBrowser        helpBrowser      = null;

    private AbstractButton     m_closeButton    = null;
    private AbstractButton     m_backButton     = null;
    private AbstractButton     m_homeButton     = null;
    private AbstractButton     m_contentsButton = null;
    private AbstractButton     m_refreshButton  = null;
    private AbstractButton     m_forwardButton  = null;
    private AbstractButton     m_wwwButton      = null;

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * @param controlledWindow
     * @return
     */
    public static HelpToolBar getInstance(HelpBrowser controlledWindow)
    {
        if (c_instance == null)
        {
            c_instance = new HelpToolBar(controlledWindow);
        }
        return c_instance;
    }

    private HelpToolBar(HelpBrowser controlledWindow)
    {
        setBorder(BorderFactory.createEtchedBorder());
        setRollover(true);
        helpBrowser = controlledWindow;

        add(getCloseButton());
        addSeparator(new Dimension(12, 0));
        addSeparator(new Dimension(12, 0));
        add(getBackButton());
        add(getForwardButton());
        add(getRefreshButton());
        add(getHomeButton());
        add(getContentsButton());
        add(Box.createHorizontalGlue());
        add(getWWWButton());
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * @return
     */
    public AbstractButton getCloseButton()
    {
        if (m_closeButton == null)
        {
            m_closeButton = ToolBarButton.createButton(BrowserCloseAction.getInstance(helpBrowser));
        }
        return m_closeButton;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * @return
     */
    public AbstractButton getBackButton()
    {
        if (m_backButton == null)
        {
            m_backButton = ToolBarButton.createButton(BrowserBackAction.getInstance(helpBrowser));
            m_backButton.setEnabled(false);
        }
        return m_backButton;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * @return
     */
    public AbstractButton getRefreshButton()
    {
        if (m_refreshButton == null)
        {
            m_refreshButton = ToolBarButton.createButton(BrowserRefreshAction.getInstance(helpBrowser));
        }
        return m_refreshButton;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * @return
     */
    public AbstractButton getHomeButton()
    {
        if (m_homeButton == null)
        {
            m_homeButton = ToolBarButton.createButton(BrowserHomeAction.getInstance(helpBrowser));
        }
        return m_homeButton;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * @return
     */
    public AbstractButton getContentsButton()
    {
        if (m_contentsButton == null)
        {
            m_contentsButton = ToolBarButton.createButton(BrowserContentsAction.getInstance(helpBrowser));
        }
        return m_contentsButton;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     */
    public AbstractButton getForwardButton()
    {
        if (m_forwardButton == null)
        {
            m_forwardButton = ToolBarButton.createButton(BrowserForwardAction.getInstance(helpBrowser));
            m_forwardButton.setEnabled(false);

        }
        return m_forwardButton;
    }

    /**
     * TODO: DOCUMENTATION (tfreytag)
     * @return
     */
    public AbstractButton getWWWButton()
    {
        if (m_wwwButton == null)
        {
            m_wwwButton = ToolBarButton.createButton(BrowserWebpageAction.getInstance(helpBrowser));

        }
        return m_wwwButton;
    }
}