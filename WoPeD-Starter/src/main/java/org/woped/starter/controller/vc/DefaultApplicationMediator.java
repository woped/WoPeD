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
package org.woped.starter.controller.vc;

import java.io.File;

import javax.swing.JFrame;

import org.woped.core.config.IGeneralConfiguration;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.ViewEvent;
import org.woped.core.gui.IUserInterface;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.vc.TaskBarVC;
import org.woped.file.controller.vep.FileEventProcessor;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraphEventProcessor;
import org.woped.qualanalysis.simulation.ReferenceProvider;
import org.woped.starter.DefaultUserInterface;
import org.woped.starter.controller.vep.GUIViewEventProcessor;

/**
 * 
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *
 */
public class DefaultApplicationMediator extends ApplicationMediator
{
    public static final int VIEWCONTROLLER_MENUBAR      = 11;
    public static final int VIEWCONTROLLER_STATUSBAR    = 12; 
 
    private int             statusCounter            = 0;
    private TaskBarVC		taskbar;
    private StatusBarVC		statusbar;
   
	public DefaultApplicationMediator(IUserInterface ui, IGeneralConfiguration conf) {
        super(ui, conf);
		ReferenceProvider helper = new ReferenceProvider();
		helper.setMediatorReference(this);
        getVepController().register(ViewEvent.VIEWEVENTTYPE_GUI, new GUIViewEventProcessor(this));
        getVepController().register(ViewEvent.VIEWEVENTTYPE_FILE, new FileEventProcessor(this));
        getVepController().register(ViewEvent.VIEWEVENTTYPE_COVERABILITY_GRAPH, new CoverabilityGraphEventProcessor(this));
   	}
        
	public void startUI(String[] filesToOpen) {
        if (ui == null)
        {
            taskbar = (TaskBarVC) this.createViewController(VIEWCONTROLLER_TASKBAR);
            addViewController(taskbar);
            statusbar = (StatusBarVC) this.createViewController(VIEWCONTROLLER_STATUSBAR);
            addViewController(statusbar);
        }
        ui = new DefaultUserInterface(taskbar, statusbar);
        setUi(ui);
        setDisplayUI((JFrame)ui);          
        ui.initialize(this);  
        
        if (filesToOpen != null && filesToOpen.length > 0) {
			for (int i = 0; i < filesToOpen.length; i++) {
				File f = new File(filesToOpen[i]);
				
				fireViewEvent(new ViewEvent(this,
						AbstractViewEvent.VIEWEVENTTYPE_FILE,
						AbstractViewEvent.OPEN, f));
			}
		}
    }
	
    public IViewController createViewController(int type)
    {
        IViewController vc = null;
        if (type == VIEWCONTROLLER_STATUSBAR)
        {
        	vc = new StatusBarVC(StatusBarVC.ID_PREFIX + statusCounter);
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
