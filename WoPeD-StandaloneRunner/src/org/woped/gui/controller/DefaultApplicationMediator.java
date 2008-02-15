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
package org.woped.gui.controller;

import java.io.File;

import org.woped.config.gui.ConfEditorPanel;
import org.woped.config.gui.ConfFilePanel;
import org.woped.config.gui.ConfLanguagePanel;
import org.woped.config.gui.ConfToolsPanel;
import org.woped.core.config.IConfiguration;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IViewController;
import org.woped.core.gui.IUserInterface;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.vc.ConfigVC;
import org.woped.editor.controller.vc.TaskBarVC;
import org.woped.editor.controller.vep.ViewEvent;
import org.woped.file.controller.vep.FileEventProcessor;
import org.woped.gui.DefaultUserInterface;
import org.woped.gui.RunWoPeD;
import org.woped.gui.controller.vc.MenuBarVC;
import org.woped.gui.controller.vc.StatusBarVC;
import org.woped.gui.controller.vc.ToolBarVC;
import org.woped.gui.controller.vep.GUIViewEventProcessor;
import org.woped.qualanalysis.reachabilitygraph.controller.ReachabilityGraphEventProcessor;
import org.woped.translations.Messages;

/**
 * 
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *
 * TODO DOCUMENTATION (lai)
 */
public class DefaultApplicationMediator extends ApplicationMediator
{
    public static final int VIEWCONTROLLER_TOOLBAR   = 10;
    public static final int VIEWCONTROLLER_MENU      = 11;
    public static final int VIEWCONTROLLER_STATUSBAR = 12;

    private int             toolbarCounter           = 0;
    private int             menuCounter              = 0;
    private int             statusCounter            = 0;

    public DefaultApplicationMediator(IUserInterface ui, IConfiguration conf, String[] args)
    {
        super(ui, conf);
        getVepController().register(ViewEvent.VIEWEVENTTYPE_GUI, new GUIViewEventProcessor(ViewEvent.VIEWEVENTTYPE_GUI, this));
        getVepController().register(ViewEvent.VIEWEVENTTYPE_FILE, new FileEventProcessor(ViewEvent.VIEWEVENTTYPE_FILE, this));
        if (ui == null)
        {
            ToolBarVC toolbar = (ToolBarVC) this.createViewController(VIEWCONTROLLER_TOOLBAR);
            addViewController(toolbar);
            MenuBarVC menubar = (MenuBarVC) this.createViewController(VIEWCONTROLLER_MENU);
            addViewController(menubar);
            TaskBarVC taskbar = (TaskBarVC) this.createViewController(VIEWCONTROLLER_TASKBAR);
            addViewController(taskbar);
            StatusBarVC statusbar = (StatusBarVC) this.createViewController(VIEWCONTROLLER_STATUSBAR);
            addViewController(statusbar);
            ConfigVC config = (ConfigVC) this.createViewController(ApplicationMediator.VIEWCONTROLLER_CONFIG);
            addViewController(config);
            // Add Conf Node-Panels
            // TODO: addConfNodePanel(null, new ConfGuiPanel("GUI"));
            config.addConfNodePanel(null, new ConfEditorPanel(Messages.getString("Configuration.Editor.Title")));
            config.addConfNodePanel(null, new ConfFilePanel(Messages.getString("Configuration.Files.Title")));
            config.addConfNodePanel(null, new ConfToolsPanel(Messages.getString("Configuration.Tools.Title")));
            config.addConfNodePanel(null, new ConfLanguagePanel(Messages.getString("Configuration.Language.Title")));
            config.setSelectedPanel(Messages.getString("Configuration.Editor.Title"));

            ui = new DefaultUserInterface(toolbar, menubar, taskbar, statusbar);
            setUi(ui);
            if (args != null)
            {
                for (int i = 0; i < args.length; i++)
                {
                    if (!RunWoPeD.isApplet()) {
                    	File f = new File(args[i]);
                    	fireViewEvent(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.OPEN, f));
                    } else {
                    	if (args[i].equals("0")) {
                    		fireViewEvent(new ViewEvent(this,AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.NEW,null));
                    	} else {
                    		fireViewEvent(new ViewEvent(this,AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.OPENWEBSERVICE,args[i]));
                    	}
                    	
                    }
                    
                }
            }
        }
    }

    public IViewController createViewController(int type)
    {

        IViewController vc = null;
        if (type == VIEWCONTROLLER_TOOLBAR)
        {
            vc = new ToolBarVC(ToolBarVC.ID_PREFIX + toolbarCounter);
            toolbarCounter++;
        } else if (type == VIEWCONTROLLER_MENU)
        {
            vc = new MenuBarVC(MenuBarVC.ID_PREFIX + menuCounter);
            menuCounter++;
        } else if (type == VIEWCONTROLLER_STATUSBAR)
        {
            vc = new StatusBarVC(StatusBarVC.ID_PREFIX + statusCounter);
        } else
        {
            vc = super.createViewController(type);
        }
        return vc;
    }

    public void addViewController(IViewController viewController)
    {
        super.addViewController(viewController);
    }
}
