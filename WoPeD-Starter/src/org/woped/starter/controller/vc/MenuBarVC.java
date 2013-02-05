package org.woped.starter.controller.vc;

import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.IViewController;
import org.woped.core.controller.IViewListener;

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
/**
 */

public class MenuBarVC extends JMenuBar implements IViewController {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public static final String ID_PREFIX          = "MENUBAR_VC_";
	
	/**
	 * 
	 */
    private Vector<IViewListener>   viewListener  = new Vector<IViewListener>(1, 3);
    private JMenuBar  menuBar;
    private JMenu     fileMenu;
    private JMenuItem openFile;
	
    /**
	 * 
	 */
    private String id = "";

    /**
     * 
     */
    public MenuBarVC() {
        initialize();
    }

    /**
     * 
     */
    private void initialize() { 
    	menuBar = new JMenuBar();
    	fileMenu = new JMenu("File");
    	openFile = new JMenuItem("Open");
    	fileMenu.add(openFile);
    	menuBar.add(fileMenu);
    }
    
    public void addViewListener(IViewListener listener)
    {
        viewListener.addElement(listener);
    }

    public String getId()
    {
        return id;
    }

    public void removeViewListener(IViewListener listenner)
    {
        viewListener.removeElement(listenner);
    }

    public int getViewControllerType()
    {
        return DefaultApplicationMediator.VIEWCONTROLLER_MENUBAR;
    }

    /**
	 *
     */
 	public final void fireViewEvent(AbstractViewEvent viewevent) {
    }

}