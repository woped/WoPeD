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
 * Created on 13.08.2004
 */
package org.woped.editor.gui;

import java.awt.EventQueue;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.woped.core.controller.AbstractGraph;
import org.woped.core.model.ArcModel;
import org.woped.core.model.petrinet.GroupModel;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.ApplicationMediator;
import org.woped.editor.controller.VisualController;

/**
 * Creates the Popupmenu and adds the MenuItems to the VisualController.
 * Elements will be created when the get-Method is called the first time. All
 * MenuItems are JMenuItems.
 * 
 * @author Thomas Pohl
 *  
 */

@SuppressWarnings("serial")
public class PopupMenuPetrinet extends JPopupMenu
{
    private static PopupMenuPetrinet c_instance                = null;

    private JMenuItem                m_openSubprocessMenuItem  = null;
    private JMenuItem                m_propertiesMenuItem      = null;

    private JMenuItem                m_addPointMenuItem        = null;
    private JMenuItem                m_removePointMenuItem     = null;

    private JMenuItem                m_externalTriggerMenuItem = null;
    private JMenuItem                m_resourceTriggerMenuItem = null;
    private JMenuItem                m_timeTriggerMenuItem     = null;
    private JMenuItem                m_removeTriggerMenuItem   = null;

    private JMenuItem                m_addTokenMenuItem        = null;
    private JMenuItem                m_removeTokenMenuItem     = null;

    private JMenuItem                m_removeMenuItem          = null;
    private JMenuItem                m_renameMenuItem          = null;
    private JMenuItem                m_copyMenuItem            = null;
    private JMenuItem                m_cutMenuItem             = null;
    private JMenuItem                m_pasteMenuItem           = null;

    private JMenuItem                m_addPlaceMenuItem        = null;
    private JMenuItem                m_addTransitionMenuItem   = null;
    private JMenuItem                m_addAndSplitMenuItem     = null;
    private JMenuItem                m_addAndJoinMenuItem      = null;
    private JMenuItem		     	 m_addAndSplitJoinMenuItem = null;
    private JMenuItem                m_addXorSplitMenuItem     = null;
    private JMenuItem                m_addXorJoinMenuItem      = null;
    private JMenuItem                m_addXorSplitJoinMenuItem = null;
    private JMenuItem                m_addAndJoinXorSplitMenuItem = null;
    private JMenuItem                m_addXorJoinAndSplitMenuItem = null;
    private JMenuItem                m_subProcessMenuItem      = null;

    /*
    private JMenu                    m_arcMenu                 = null;
    private JMenu                    m_netMenu                 = null;
    private JMenuItem                m_routeAllItem            = null;
    private JMenuItem                m_routeNoneItem           = null;
    private JMenuItem                m_routeMenuItem           = null;
    private JMenuItem                m_unrouteMenuItem         = null;
    */

    private PopupMenuPetrinet()
    {
        add(getAddPlaceMenuItem());
        add(getAddTransitionMenuItem());
        add(getAddAndSplitMenuItem());
        add(getAddXorSplitMenuItem());
        add(getAddAndJoinMenuItem());
        add(getAddXorJoinMenuItem());
        add(getAddAndSplitJoinMenuItem());
        add(getAddXorSplitJoinMenuItem());
        add(getAddAndJoinXorSplitMenuItem());
        add(getAddXorJoinAndSplitMenuItem());
        add(getSubProcessMenuItem());

//        addSeparator();
        add(getPropertiesMenuItem());
        add(getRenameMenuItem());
//        addSeparator();
        add(getRemoveMenuItem());
        add(getCutMenuItem());
        add(getCopyMenuItem());
        add(getPasteMenuItem());  
//        addSeparator();
        add(getAddTokenMenuItem());
        add(getRemoveTokenMenuItem());
        add(getExternalTriggerMenuItem());
        add(getResourceTriggerMenuItem());
        add(getTimeTriggerMenuItem());
        add(getRemoveTriggerMenuItem());
//        add(getRouteMenuItem());
//        add(getUnrouteMenuItem());
        add(getAddPointMenuItem());
        add(getRemovePointMenuItem());
        add(getOpenSubprocessMenuItem());
        pack();
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @return
     */
    public static PopupMenuPetrinet getInstance()
    {
       	if (c_instance == null)
        {
            c_instance = new PopupMenuPetrinet();
        }
        c_instance.pack();
        return c_instance;
    }

    public void show(final Object obj, final AbstractGraph graph, final int x, final int y)
    {
    	// Wait with displaying the popup until the
    	// selection event has been processed.
    	// This way, the size of the popup menu will be correct
    	// according to the selected item
    	Runnable runner = new Runnable() {
            public void run() {
    			Object selectedCell = graph.getSelectionCell();            	            	
                if (selectedCell == null 
                    	|| selectedCell instanceof GroupModel 
                    	|| selectedCell instanceof ArcModel) 
                    {
                    	pack();
                        PopupMenuPetrinet.super.show(graph, x, y);
                    }
              };
    	};
    	EventQueue.invokeLater(runner);
    }
    
    private JMenuItem getRenameMenuItem()
    {
        if (m_renameMenuItem == null)
        {
            m_renameMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_RENAME));
            VisualController.getInstance().addElement(m_renameMenuItem, VisualController.ELEMENT_SELECTION, VisualController.ELEMENT_SELECTION, VisualController.IGNORE);
        }
        return m_renameMenuItem;
    }

    private JMenuItem getRemoveMenuItem()
    {
        if (m_removeMenuItem == null)
        {
            m_removeMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REMOVE));
            VisualController.getInstance().addElement(m_removeMenuItem, VisualController.ELEMENT_SELECTION, VisualController.ANY_SELECTION, VisualController.IGNORE);
        }
        return m_removeMenuItem;
    }

     private JMenuItem getCopyMenuItem()
    {
        if (m_copyMenuItem == null)
        {
            m_copyMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_COPY));
            VisualController.getInstance().addElement(m_copyMenuItem, VisualController.ELEMENT_SELECTION, VisualController.ANY_SELECTION, VisualController.IGNORE);
        }
        return m_copyMenuItem;
    }

    private JMenuItem getCutMenuItem()
    {
        if (m_cutMenuItem == null)
        {
            m_cutMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_CUT));
            VisualController.getInstance().addElement(m_cutMenuItem, VisualController.ELEMENT_SELECTION, VisualController.ANY_SELECTION, VisualController.IGNORE);
        }
        return m_cutMenuItem;
    }

    private JMenuItem getExternalTriggerMenuItem()
    {
        if (m_externalTriggerMenuItem == null)
        {
            m_externalTriggerMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_EXT_TRIGGER));
            VisualController.getInstance().addElement(m_externalTriggerMenuItem, VisualController.TRANSITION_SELECTION, VisualController.TRANSITION_SELECTION, VisualController.IGNORE);
        }
        return m_externalTriggerMenuItem;
    }

    private JMenuItem getResourceTriggerMenuItem()
    {
        if (m_resourceTriggerMenuItem == null)
        {
            m_resourceTriggerMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_RES_TRIGGER));
            VisualController.getInstance().addElement(m_resourceTriggerMenuItem, VisualController.TRANSITION_SELECTION, VisualController.TRANSITION_SELECTION, VisualController.IGNORE);
        }
        return m_resourceTriggerMenuItem;
    }

    private JMenuItem getTimeTriggerMenuItem()
    {
        if (m_timeTriggerMenuItem == null)
        {

            m_timeTriggerMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_TIME_TRIGGER));
            VisualController.getInstance().addElement(m_timeTriggerMenuItem, VisualController.TRANSITION_SELECTION, VisualController.TRANSITION_SELECTION, VisualController.IGNORE);
        }
        return m_timeTriggerMenuItem;
    }

    private JMenuItem getRemoveTriggerMenuItem()
    {
        if (m_removeTriggerMenuItem == null)
        {
            m_removeTriggerMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REMOVE_TIRGGER));
            VisualController.getInstance().addElement(m_removeTriggerMenuItem, VisualController.TRIGGERED_TRANSITION_SELECTION, VisualController.TRANSITION_SELECTION, VisualController.IGNORE);
        }
        return m_removeTriggerMenuItem;
    }

    private JMenuItem getAddPlaceMenuItem()
    {
        if (m_addPlaceMenuItem == null)
        {
            m_addPlaceMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_PLACE));
            VisualController.getInstance().addElement(m_addPlaceMenuItem, VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        }
        return m_addPlaceMenuItem;
    }

    private JMenuItem getAddTokenMenuItem()
    {
        if (m_addTokenMenuItem == null)
        {
            m_addTokenMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_TOKEN));
            VisualController.getInstance().addElement(m_addTokenMenuItem, VisualController.PLACE_SELECTION, VisualController.PLACE_SELECTION, VisualController.IGNORE);
        }
        return m_addTokenMenuItem;
    }

    private JMenuItem getRemoveTokenMenuItem()
    {
        if (m_removeTokenMenuItem == null)
        {
            m_removeTokenMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REMOVE_TOKEN));
            VisualController.getInstance().addElement(m_removeTokenMenuItem, VisualController.TOKEN_PLACE_SELECTION, VisualController.PLACE_SELECTION, VisualController.IGNORE);
        }
        return m_removeTokenMenuItem;
    }

    private JMenuItem getOpenSubprocessMenuItem()
    {
        if (m_openSubprocessMenuItem == null)
        {
            m_openSubprocessMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_OPEN_SUBPROCESS));
            VisualController.getInstance().addElement(m_openSubprocessMenuItem, VisualController.SUBPROCESS_SELECTION, VisualController.SUBPROCESS_SELECTION, VisualController.IGNORE);
        }
        return m_openSubprocessMenuItem;
    }

    private JMenuItem getAddAndJoinMenuItem()
    {
        if (m_addAndJoinMenuItem == null)
        {
            m_addAndJoinMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_ANDJOIN));
            VisualController.getInstance().addElement(m_addAndJoinMenuItem, VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        }
        return m_addAndJoinMenuItem;
    }

    private JMenuItem getAddAndSplitMenuItem()
    {
        if (m_addAndSplitMenuItem == null)
        {
            m_addAndSplitMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_ANDSPLIT));
            VisualController.getInstance().addElement(m_addAndSplitMenuItem, VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        }
        return m_addAndSplitMenuItem;
    }
    
    private JMenuItem getAddAndSplitJoinMenuItem()
    {
    	if (m_addAndSplitJoinMenuItem == null)
    	{
    		m_addAndSplitJoinMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_ANDSPLITJOIN));
    		VisualController.getInstance().addElement(m_addAndSplitJoinMenuItem, VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);    		
    	}
    	return m_addAndSplitJoinMenuItem;    	
    }

    private JMenuItem getAddTransitionMenuItem()
    {
        if (m_addTransitionMenuItem == null)
        {
            m_addTransitionMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_TRANSITION));
            VisualController.getInstance().addElement(m_addTransitionMenuItem, VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        }
        return m_addTransitionMenuItem;
    }

    private JMenuItem getAddXorJoinMenuItem()
    {
        if (m_addXorJoinMenuItem == null)
        {
            m_addXorJoinMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_XORJOIN));
            VisualController.getInstance().addElement(m_addXorJoinMenuItem, VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        }
        return m_addXorJoinMenuItem;
    }

    private JMenuItem getAddXorSplitMenuItem()
    {
        if (m_addXorSplitMenuItem == null)
        {
            m_addXorSplitMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_XORSPLIT));
            VisualController.getInstance().addElement(m_addXorSplitMenuItem, VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        }
        return m_addXorSplitMenuItem;
    }

    private JMenuItem getAddXorSplitJoinMenuItem()
    {
        if (m_addXorSplitJoinMenuItem == null)
        {
            m_addXorSplitJoinMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_XORSPLITJOIN));
            VisualController.getInstance().addElement(m_addXorSplitJoinMenuItem, VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        }
        return m_addXorSplitJoinMenuItem;
    }

    private JMenuItem getAddAndJoinXorSplitMenuItem()
    {
        if (m_addAndJoinXorSplitMenuItem == null)
        {
        	m_addAndJoinXorSplitMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_ANDJOINXORSPLIT));
            VisualController.getInstance().addElement(m_addAndJoinXorSplitMenuItem, VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        }
        return m_addAndJoinXorSplitMenuItem;
    }

    private JMenuItem getAddXorJoinAndSplitMenuItem()
    {
        if (m_addXorJoinAndSplitMenuItem == null)
        {
        	m_addXorJoinAndSplitMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_XORJOINANDSPLIT));
            VisualController.getInstance().addElement(m_addXorJoinAndSplitMenuItem, VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        }
        return m_addXorJoinAndSplitMenuItem;
    }
    
    private JMenuItem getSubProcessMenuItem()
    {
        if (m_subProcessMenuItem == null)
        {
            m_subProcessMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_SUBPROCESS));
            VisualController.getInstance().addElement(m_subProcessMenuItem, VisualController.NO_SELECTION, VisualController.NO_SELECTION, VisualController.IGNORE);
        }
        return m_subProcessMenuItem;
    }

    private JMenuItem getPasteMenuItem()
    {
        if (m_pasteMenuItem == null)
        {
            m_pasteMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_PASTE));
            VisualController.getInstance().addElement(m_pasteMenuItem, VisualController.CAN_PASTE, VisualController.IGNORE, VisualController.IGNORE);
        }
        return m_pasteMenuItem;
    }

    private JMenuItem getAddPointMenuItem()
    {
        if (m_addPointMenuItem == null)
        {
            m_addPointMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_POINT));
            VisualController.getInstance().addElement(m_addPointMenuItem, VisualController.ARC_SELECTION, VisualController.ARC_SELECTION, VisualController.IGNORE);
        }
        return m_addPointMenuItem;
    }

    private JMenuItem getRemovePointMenuItem()
    {
        if (m_removePointMenuItem == null)
        {
            m_removePointMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REMOVE_POINT));
            VisualController.getInstance().addElement(m_removePointMenuItem, VisualController.ARC_POINT, VisualController.ARC_SELECTION, VisualController.IGNORE);
        }
        return m_removePointMenuItem;
    }

    private JMenuItem getPropertiesMenuItem()
    {
        if (m_propertiesMenuItem == null)
        {
            m_propertiesMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_OPEN_PROPERTIES));
            //VisualController.getInstance().addElement(m_propertiesMenuItem, VisualController.ELEMENT_SELECTION, VisualController.ELEMENT_SELECTION, VisualController.IGNORE);
            VisualController.getInstance().addElement(m_propertiesMenuItem, VisualController.ANY_SELECTION, VisualController.ANY_SELECTION, VisualController.IGNORE);
        }
        return m_propertiesMenuItem;
    }
/*
    private JMenu getNetMenu()
    {
        if (m_netMenu == null)
        {
            m_netMenu = new JMenu("Routing");
            VisualController.getInstance().addElement(m_arcMenu, VisualController.ALWAYS, VisualController.ALWAYS, VisualController.IGNORE);
            m_netMenu.add(getRouteAllMenuItem());
            m_netMenu.add(getRouteNoneMenuItem());
            // TODO: lai m_netMenu.add(getAnnealingLayoutMenuItem());
        }
        return m_netMenu;
    }

    private JMenuItem getRouteAllMenuItem()
    {
        if (m_routeAllItem == null)
        {
            m_routeAllItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ACTIVATE_ALL_ROUTING));
        }
        return m_routeAllItem;
    }

    private JMenuItem getRouteNoneMenuItem()
    {
        if (m_routeNoneItem == null)
        {
            m_routeNoneItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DEACTIVATE_ALL_ROUTING));
        }
        return m_routeNoneItem;
    }
    
    private JMenuItem getRouteMenuItem()
    {
        if (m_routeMenuItem == null)
        {
            m_routeMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ACTIVATE_ROUTING));
            VisualController.getInstance().addElement(m_routeMenuItem, VisualController.UNROUTED_ARC_SELECTION, VisualController.ARC_SELECTION, VisualController.IGNORE);
        }
        return m_routeMenuItem;
    }

    private JMenuItem getUnrouteMenuItem()
    {
        if (m_unrouteMenuItem == null)
        {
            m_unrouteMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DEACTIVATE_ROUTING));
            VisualController.getInstance().addElement(m_unrouteMenuItem, VisualController.ROUTED_ARC_SELECTION, VisualController.ARC_SELECTION, VisualController.IGNORE);
        }
        return m_unrouteMenuItem;
    }
    */
}