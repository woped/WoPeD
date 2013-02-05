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
package org.woped.editor.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JComponent;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.editor.controller.EditorViewEvent;
import org.woped.gui.translations.Messages;

/**
 * @author Thomas Pohl
 * 
 * 
 * 13.10.2003
 */

@SuppressWarnings("serial")
public class WoPeDAction extends AbstractAction
{

    private boolean                    		m_selected = false;
    private AbstractApplicationMediator 	am         = null;
    private int                         	type       = -1;
    private int                         	order      = -1;
    private Object                      	data       = null;
    private ArrayList<JComponent>			target	   = new ArrayList<JComponent>();
         
    public boolean isSelected()
    {
        return m_selected;
    }
    
    public WoPeDAction(AbstractApplicationMediator am, int type, int order)
    {
        this(am, type, order, null, null);
    }

    public WoPeDAction(AbstractApplicationMediator am, int type, int order, Object data)
    {
        this(am, type, order, data, null);
    }

    public WoPeDAction(String propertiesPrefix)
    {
        this(null, -1, -1, null, propertiesPrefix);
    }

    public WoPeDAction(String propertiesPrefix, Object[] args)
    {
        this(null, -1, -1, null, propertiesPrefix, args);
    }

    /**
     * @param arg0
     */
    public WoPeDAction(AbstractApplicationMediator am, int type, int order, Object data, String propertiesPrefix)
    {
        this(am, type, order, data, propertiesPrefix, null);
    }

    /**
     * @param arg0
     */
    public WoPeDAction(AbstractApplicationMediator am, int type, int order, Object data, String propertiesPrefix, Object[] args)
    {
        super();
        this.type = type;
        this.order = order;
        this.am = am;
        this.data = data;
        if (propertiesPrefix != null)
        {
            putValue(NAME, Messages.getTitle(propertiesPrefix, args));
            putValue(SMALL_ICON, Messages.getImageIcon(propertiesPrefix));
            putValue(MNEMONIC_KEY, new Integer(Messages.getMnemonic(propertiesPrefix)));
            putValue(ACCELERATOR_KEY, Messages.getShortcut(propertiesPrefix));
            putValue(SHORT_DESCRIPTION, Messages.getTitle(propertiesPrefix, args));
        }
    }

    /**
     * @param arg0
     * @param arg1
     */
    public WoPeDAction(AbstractApplicationMediator am, String arg0, Icon icon)
    {
        super(arg0, icon);
    }

    public void actionPerformed(final ActionEvent e)
    {
        if (am != null)
        	am.viewEventPerformed(new EditorViewEvent(e.getSource(), type, order, data));
        
    }

	public void addTarget(JComponent target) {
		this.target.add(target);
		
	}
	
	public ArrayList<JComponent>getTarget() {
		return target;
	}
}