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
package org.woped.gui.controller.vc;

import java.io.File;

import javax.swing.JFrame;

import org.woped.core.config.IGeneralConfiguration;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IViewController;
import org.woped.core.gui.IUserInterface;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.vc.ConfigVC;
import org.woped.editor.controller.vc.TaskBarVC;
import org.woped.editor.controller.vep.ViewEvent;
import org.woped.file.controller.vep.FileEventProcessor;
import org.woped.gui.Constants;
import org.woped.gui.DefaultUserInterface;
import org.woped.gui.controller.vep.GUIViewEventProcessor;
import org.woped.qualanalysis.simulation.ReferenceProvider;

/**
 * 
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *
 * TODO DOCUMENTATION (lai)
 */
public class DefaultApplicationMediator extends ApplicationMediator
{
    public static final int VIEWCONTROLLER_TOOLBAR      = 10;
    public static final int VIEWCONTROLLER_MENU         = 11;
    public static final int VIEWCONTROLLER_STATUSBAR    = 12; 
    public static final int VIEWCONTROLLER_SIMULATORBAR = 13;
 
    private int             statusCounter            = 0;
    
	public DefaultApplicationMediator(IUserInterface ui, IGeneralConfiguration conf, String[] args)
    {
        super(ui, conf);
		ReferenceProvider helper = new ReferenceProvider();
		helper.setMediatorReference(this);
        getVepController().register(ViewEvent.VIEWEVENTTYPE_GUI, new GUIViewEventProcessor(ViewEvent.VIEWEVENTTYPE_GUI, this));
        getVepController().register(ViewEvent.VIEWEVENTTYPE_FILE, new FileEventProcessor(ViewEvent.VIEWEVENTTYPE_FILE, this));
        if (ui == null)
        {
            TaskBarVC taskbar = (TaskBarVC) this.createViewController(VIEWCONTROLLER_TASKBAR);
            addViewController(taskbar);
            StatusBarVC statusbar = (StatusBarVC) this.createViewController(VIEWCONTROLLER_STATUSBAR);
            addViewController(statusbar);
            ConfigVC config = (ConfigVC) this.createViewController(ApplicationMediator.VIEWCONTROLLER_CONFIG);
            addViewController(config);
            ui = new DefaultUserInterface(taskbar, statusbar);
            setUi(ui);
            setDisplayUI((JFrame)ui);
            
            ui.initialize(this);  
            

            if (args != null)
            {
                for (int i = 0; i < args.length; i++)
                {
                    	File f = new File(args[i]);
        				LoggerManager.info(Constants.GUI_LOGGER, "OPENING FILE " + args[i]);  
        				
                    	fireViewEvent(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_FILE, AbstractViewEvent.OPEN, f));
                } 
            }      
        }
    }

    public IViewController createViewController(int type)
    {
        IViewController vc = null;
        if (type == VIEWCONTROLLER_TOOLBAR)
        {
//            vc = new ToolBarVC(ToolBarVC.ID_PREFIX + toolbarCounter);
//            toolbarCounter++;
        } else if (type == VIEWCONTROLLER_MENU)
        {
//            vc = new MenuBarVC(MenuBarVC.ID_PREFIX + menuCounter);
//            menuCounter++;
        } else if (type == VIEWCONTROLLER_STATUSBAR)
        {
        	vc = new StatusBarVC(StatusBarVC.ID_PREFIX + statusCounter);
        }
        else if (type == VIEWCONTROLLER_SIMULATORBAR)
        {
        	//vc = new SimulatorBarVC(SimulatorBarVC.ID_PREFIX + toolbarCounter, tgbController, acoChoiceItems, ahxHistoryContent );
//        	toolbarCounter++;
        }  
        else
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
