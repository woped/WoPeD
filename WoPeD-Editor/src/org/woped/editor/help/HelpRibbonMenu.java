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
 * Created on Jan 20, 2014
 *
 *  */
package org.woped.editor.help;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.icon.ImageWrapperResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority;
import org.pushingpixels.flamingo.api.ribbon.RibbonTask;
import org.pushingpixels.flamingo.api.ribbon.resize.CoreRibbonResizePolicies;
import org.pushingpixels.flamingo.api.ribbon.resize.IconRibbonBandResizePolicy;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.vep.ViewEvent;
import org.woped.editor.gui.ToolBarButton;
import org.woped.editor.help.action.BrowserBackAction;
import org.woped.editor.help.action.BrowserCloseAction;
import org.woped.editor.help.action.BrowserContentsAction;
import org.woped.editor.help.action.BrowserForwardAction;
import org.woped.editor.help.action.BrowserHomeAction;
import org.woped.editor.help.action.BrowserRefreshAction;
import org.woped.editor.help.action.BrowserWebpageAction;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
 *
 */

@SuppressWarnings("serial")
public class HelpRibbonMenu extends JRibbon
{
    private static HelpRibbonMenu c_instance       = null;

    private HelpBrowser        helpBrowser      = null;

	private RibbonTask helpTask = null;
	private JRibbonBand fileBand, navigationBand = null;

    private JCommandButton     m_closeButton    = null;
    private JCommandButton     m_backButton     = null;
    private JCommandButton     m_homeButton     = null;
    private JCommandButton     m_contentsButton = null;
    private JCommandButton     m_refreshButton  = null;
    private JCommandButton     m_forwardButton  = null;
    private JCommandButton     m_wwwButton      = null;

    //Other Variables
	private static final int        stXsize              = 80;
	private static final int        stYsize              = 80;

    /**
     * Gets the instance of the RibbonMenu.
     *
     * @param the controlled window
     * @return instance of RibbonMenu
	 * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    public static HelpRibbonMenu getInstance(HelpBrowser controlledWindow)
    {
        if (c_instance == null){
            c_instance = new HelpRibbonMenu(controlledWindow);
        }
        return c_instance;
    }

    /**
     * Instantiates a new RibbonMenu.
     *
     * @param the controlled window
	 * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    private HelpRibbonMenu(HelpBrowser controlledWindow)
    {
    	helpBrowser = controlledWindow;
    	this.addTask(getHelpTask());
    }

	public static ResizableIcon getResizableIcon(String resource) {
		  return ImageWrapperResizableIcon.getIcon(Messages.getImageIcon(resource).getImage(), new Dimension(stXsize, stYsize));
	}

	/* TASK */
	/*********/
	private RibbonTask getHelpTask() {

		if (helpTask == null) {
			helpTask = new RibbonTask(Messages.getTitle("Help"), getFileBand(), getNavigationBand());
		}
		return helpTask;
	}
	/*********/
	/* BANDS */
	/*********/
    private JRibbonBand getFileBand() {
    	if (fileBand == null) {
    		fileBand = new JRibbonBand(Messages.getTitle("Menu.Browser.File"), null);

    		fileBand.addCommandButton(getCloseButton(), RibbonElementPriority.MEDIUM);

    		fileBand.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(fileBand.getControlPanel()), new IconRibbonBandResizePolicy(fileBand.getControlPanel())));

		}
		return fileBand;
	}

	private JRibbonBand getNavigationBand() {
		if (navigationBand == null) {
			fileBand = new JRibbonBand(Messages.getTitle("Menu.Browser.Navigate"), null);

			fileBand.addCommandButton(getBackButton(), RibbonElementPriority.MEDIUM);
	    	fileBand.addCommandButton(getForwardButton(), RibbonElementPriority.MEDIUM);
	    	fileBand.addCommandButton(getHomeButton(), RibbonElementPriority.MEDIUM);
	    	fileBand.addCommandButton(getContentsButton(), RibbonElementPriority.MEDIUM);
	    	fileBand.addCommandButton(getRefreshButton(), RibbonElementPriority.MEDIUM);
	    	fileBand.addCommandButton(getWWWButton(), RibbonElementPriority.MEDIUM);

	    	fileBand.setResizePolicies((List) Arrays.asList(new CoreRibbonResizePolicies.None(fileBand.getControlPanel()), new IconRibbonBandResizePolicy(fileBand.getControlPanel())));
		}
		return fileBand;
	}

    public JCommandButton getCloseButton()
    {
        if (m_closeButton == null)
        {
            m_closeButton = new JCommandButton(Messages.getTitle("Action.Browser.Close"), getResizableIcon("Action.Browser.Close"));
            m_closeButton.addActionListener(new ActionListener() {
            	WoPeDAction action = BrowserCloseAction.getInstance(helpBrowser);
				public void actionPerformed(ActionEvent arg0) {
					action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.VIEWEVENTTYPE_GUI));
				}
            });
        }
        return m_closeButton;
    }

    public JCommandButton getBackButton()
    {
        if (m_backButton == null)
        {
            m_backButton = new JCommandButton(Messages.getTitle("Action.Browser.Back"), getResizableIcon("Action.Browser.Back"));
            m_backButton.addActionListener(new ActionListener() {
            	WoPeDAction action = BrowserBackAction.getInstance(helpBrowser);
				public void actionPerformed(ActionEvent arg0) {
					action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.VIEWEVENTTYPE_GUI));
				}
            });
            m_backButton.setEnabled(false);
        }
        return m_backButton;
    }

    public JCommandButton getRefreshButton()
    {
        if (m_refreshButton == null)
        {
            m_refreshButton = new JCommandButton(Messages.getTitle("Action.Browser.Refresh"), getResizableIcon("Action.Browser.Refresh"));
            m_refreshButton.addActionListener(new ActionListener() {
            	WoPeDAction action = BrowserRefreshAction.getInstance(helpBrowser);
				public void actionPerformed(ActionEvent arg0) {
					action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.VIEWEVENTTYPE_GUI));
				}
            });
        }
        return m_refreshButton;
    }

    public JCommandButton getHomeButton()
    {
        if (m_homeButton == null)
        {
            m_homeButton = new JCommandButton(Messages.getTitle("Action.Browser.Home"), getResizableIcon("Action.Browser.Home"));
            m_homeButton.addActionListener(new ActionListener() {
            	WoPeDAction action = BrowserHomeAction.getInstance(helpBrowser);
				public void actionPerformed(ActionEvent arg0) {
					action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.VIEWEVENTTYPE_GUI));
				}
            });
        }
        return m_homeButton;
    }

    public JCommandButton getContentsButton()
    {
        if (m_contentsButton == null)
        {
            m_contentsButton = new JCommandButton(Messages.getTitle("Action.Browser.Contents"), getResizableIcon("Action.Browser.Contents"));
            m_contentsButton.addActionListener(new ActionListener() {
            	WoPeDAction action = BrowserContentsAction.getInstance(helpBrowser);
				public void actionPerformed(ActionEvent arg0) {
					action.actionPerformed(null);
				}
            });
        }
        return m_contentsButton;
    }

    public JCommandButton getForwardButton()
    {
        if (m_forwardButton == null)
        {
            m_forwardButton = new JCommandButton(Messages.getTitle("Action.Browser.Forward"), getResizableIcon("Action.Browser.Forward"));
            m_forwardButton.addActionListener(new ActionListener() {
            	WoPeDAction action = BrowserForwardAction.getInstance(helpBrowser);
				public void actionPerformed(ActionEvent arg0) {
					action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.VIEWEVENTTYPE_GUI));
				}
            });
            m_forwardButton.setEnabled(false);
        }
        return m_forwardButton;
    }

    public JCommandButton getWWWButton()
    {
        if (m_wwwButton == null)
        {
            m_wwwButton = new JCommandButton(Messages.getTitle("Action.Browser.WWW"), getResizableIcon("Action.Browser.WWW"));
            m_wwwButton.addActionListener(new ActionListener() {
            	WoPeDAction action = BrowserWebpageAction.getInstance(helpBrowser);
				public void actionPerformed(ActionEvent arg0) {
					action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.VIEWEVENTTYPE_GUI));
				}
            });
        }
        return m_wwwButton;
    }
}