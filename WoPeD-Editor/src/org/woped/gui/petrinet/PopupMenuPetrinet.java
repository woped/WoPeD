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
package org.woped.gui.petrinet;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.woped.controller.ActionFactory;
import org.woped.controller.VisualController;

/**
 * Creates the Popupmenu and adds the MenuItems to the VisualController.
 * Elements will be created when the get-Method is called the first time. All
 * MenuItems are JMenuItems.
 * 
 * @author Thomas Pohl
 *  
 */
public class PopupMenuPetrinet extends JPopupMenu
{

    private static PopupMenuPetrinet c_instance                = null;

    private JMenuItem                m_openSubprocessMenuItem  = null;
    private JMenuItem                m_propertiesMenuItem      = null;

    private JMenu                    m_arcMenu                 = null;
    private JMenuItem                m_routeMenuItem           = null;
    private JMenuItem                m_unrouteMenuItem         = null;
    private JMenuItem                m_addPointMenuItem        = null;
    private JMenuItem                m_removePointMenuItem     = null;

    private JMenu                    m_triggerMenu             = null;
    private JMenuItem                m_externalTriggerMenuItem = null;
    private JMenuItem                m_resourceTriggerMenuItem = null;
    private JMenuItem                m_timeTriggerMenuItem     = null;
    private JMenuItem                m_removeTriggerMenuItem   = null;

    private JMenu                    m_tokenMenu               = null;
    private JMenuItem                m_addTokenMenuItem        = null;
    private JMenuItem                m_removeTokenMenuItem     = null;

    private JMenu                    m_editMenu                = null;
    private JMenuItem                m_removeMenuItem          = null;
    private JMenuItem                m_editMenuItem            = null;
    private JMenuItem                m_copyMenuItem            = null;
    private JMenuItem                m_cutMenuItem             = null;
    private JMenuItem                m_pasteMenuItem           = null;

    private JMenu                    m_addMenu                 = null;
    private JMenuItem                m_addPlaceMenuItem        = null;
    private JMenuItem                m_addTransitionMenuItem   = null;
    private JMenuItem                m_addAndSplitMenuItem     = null;
    private JMenuItem                m_addAndJoinMenuItem      = null;
    private JMenuItem                m_addXorSplitMenuItem     = null;
    private JMenuItem                m_addXorJoinMenuItem      = null;
    private JMenuItem                m_addXorSplitJoinMenuItem = null;
    private JMenuItem                m_addOrSplitMenuItem      = null;
    private JMenuItem                m_subProcessMenuItem      = null;

    private JMenu                    m_netMenu                 = null;
    private JMenuItem                m_routeAllItem            = null;
    private JMenuItem                m_routeNoneItem           = null;
    private JMenuItem                m_annealingLayoutMenuItem = null;

    private PopupMenuPetrinet()
    {
        add(getAddMenu());
        add(getEditMenu());
        add(getArcMenu());
        add(getTokenMenu());
        add(getTriggerMenu());
        // add(getNetMenu());
        add(getOpenSubprocessMenuItem());
        add(getPropertiesMenuItem());
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

    private JMenuItem getRemoveMenuItem()
    {
        if (m_removeMenuItem == null)
        {
            m_removeMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REMOVE));
        }
        return m_removeMenuItem;
    }

    private JMenuItem getRouteMenuItem()
    {
        if (m_routeMenuItem == null)
        {
            m_routeMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ACTIVATE_ROUTING));
        }
        return m_routeMenuItem;
    }

    private JMenuItem getUnrouteMenuItem()
    {
        if (m_unrouteMenuItem == null)
        {
            m_unrouteMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DEACTIVATE_ROUTING));
        }
        return m_unrouteMenuItem;
    }

    private JMenuItem getCopyMenuItem()
    {
        if (m_copyMenuItem == null)
        {
            m_copyMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_COPY));
        }
        return m_copyMenuItem;
    }

    private JMenuItem getCutMenuItem()
    {
        if (m_cutMenuItem == null)
        {
            m_cutMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_PASTE));
        }
        return m_cutMenuItem;
    }

    private JMenuItem getEditMenuItem()
    {
        if (m_editMenuItem == null)
        {

            m_editMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_EDIT));
        }
        return m_editMenuItem;
    }

    private JMenuItem getExternalTriggerMenuItem()
    {
        if (m_externalTriggerMenuItem == null)
        {

            m_externalTriggerMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_EXT_TRIGGER));
        }
        return m_externalTriggerMenuItem;
    }

    private JMenuItem getResourceTriggerMenuItem()
    {
        if (m_resourceTriggerMenuItem == null)
        {

            m_resourceTriggerMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_RES_TRIGGER));
        }
        return m_resourceTriggerMenuItem;
    }

    private JMenuItem getTimeTriggerMenuItem()
    {
        if (m_timeTriggerMenuItem == null)
        {

            m_timeTriggerMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_TIME_TRIGGER));
        }
        return m_timeTriggerMenuItem;
    }

    private JMenuItem getRemoveTriggerMenuItem()
    {
        if (m_removeTriggerMenuItem == null)
        {

            m_removeTriggerMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REMOVE_TIRGGER));
        }
        return m_removeTriggerMenuItem;
    }

    private JMenu getAddMenu()
    {
        if (m_addMenu == null)
        {
            m_addMenu = new JMenu("Add");
            VisualController.getInstance().addElement(m_addMenu, VisualController.NO_SELECTION, VisualController.ALWAYS, VisualController.IGNORE);
            m_addMenu.add(getAddPlaceMenuItem());
            m_addMenu.add(getAddTransitionMenuItem());
            m_addMenu.add(getAddAndSplitMenuItem());
            m_addMenu.add(getAddXorSplitMenuItem());
            m_addMenu.add(getAddAndJoinMenuItem());
            m_addMenu.add(getAddXorJoinMenuItem());
            m_addMenu.add(getAddXorSplitJoinMenuItem());
            // m_addMenu.add(getAddOrSplitMenuItem());
            m_addMenu.add(getSubProcessMenuItem());
        }
        return m_addMenu;
    }

    private JMenuItem getAddPlaceMenuItem()
    {
        if (m_addPlaceMenuItem == null)
        {
            m_addPlaceMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_PLACE));
        }
        return m_addPlaceMenuItem;
    }

    private JMenuItem getAddTokenMenuItem()
    {
        if (m_addTokenMenuItem == null)
        {
            m_addTokenMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_TOKEN));
        }
        return m_addTokenMenuItem;
    }

    private JMenuItem getRemoveTokenMenuItem()
    {
        if (m_removeTokenMenuItem == null)
        {
            m_removeTokenMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REMOVE_TOKEN));
        }
        return m_removeTokenMenuItem;
    }

    private JMenuItem getOpenSubprocessMenuItem()
    {
        if (m_openSubprocessMenuItem == null)
        {
            m_openSubprocessMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_OPEN_SUBPROCESS));
        }
        return m_openSubprocessMenuItem;
    }

    private JMenuItem getAddAndJoinMenuItem()
    {
        if (m_addAndJoinMenuItem == null)
        {
            m_addAndJoinMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_ANDJOIN));
        }
        return m_addAndJoinMenuItem;
    }

    private JMenuItem getAddAndSplitMenuItem()
    {
        if (m_addAndSplitMenuItem == null)
        {
            m_addAndSplitMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_ANDSPLIT));
        }
        return m_addAndSplitMenuItem;
    }

    private JMenuItem getAddTransitionMenuItem()
    {
        if (m_addTransitionMenuItem == null)
        {
            m_addTransitionMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_TRANSITION));
        }
        return m_addTransitionMenuItem;
    }

    private JMenuItem getAddXorJoinMenuItem()
    {
        if (m_addXorJoinMenuItem == null)
        {
            m_addXorJoinMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_XORJOIN));
        }
        return m_addXorJoinMenuItem;
    }

    private JMenuItem getAddXorSplitMenuItem()
    {
        if (m_addXorSplitMenuItem == null)
        {
            m_addXorSplitMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_XORSPLIT));
        }
        return m_addXorSplitMenuItem;
    }

    private JMenuItem getAddXorSplitJoinMenuItem()
    {
        if (m_addXorSplitJoinMenuItem == null)
        {
            m_addXorSplitJoinMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_XORSPLITJOIN));
        }
        return m_addXorSplitJoinMenuItem;
    }

    private JMenuItem getSubProcessMenuItem()
    {
        if (m_subProcessMenuItem == null)
        {
            m_subProcessMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_SUBPROCESS));
        }
        return m_subProcessMenuItem;
    }

    private JMenuItem getPasteMenuItem()
    {
        if (m_pasteMenuItem == null)
        {
            m_pasteMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_PASTE));
        }
        return m_pasteMenuItem;
    }

    private JMenu getEditMenu()
    {
        if (m_editMenu == null)
        {
            m_editMenu = new JMenu("Edit");
            VisualController.getInstance().addElement(m_editMenu, VisualController.ALWAYS, VisualController.ALWAYS, VisualController.IGNORE);
            m_editMenu.add(getRemoveMenuItem());
            m_editMenu.add(getEditMenuItem());
            m_editMenu.add(getCutMenuItem());
            m_editMenu.add(getCopyMenuItem());
            m_editMenu.add(getPasteMenuItem());
        }
        return m_editMenu;
    }

    private JMenu getTokenMenu()
    {
        if (m_tokenMenu == null)
        {
            m_tokenMenu = new JMenu("Token");
            VisualController.getInstance().addElement(m_tokenMenu, VisualController.PLACE_SELECTION, VisualController.ALWAYS, VisualController.IGNORE);
            m_tokenMenu.add(getAddTokenMenuItem());
            m_tokenMenu.add(getRemoveTokenMenuItem());
        }
        return m_tokenMenu;
    }

    private JMenu getTriggerMenu()
    {
        if (m_triggerMenu == null)
        {
            m_triggerMenu = new JMenu("Trigger");
            VisualController.getInstance().addElement(m_triggerMenu, VisualController.TRANSITION_SELECTION, VisualController.ALWAYS, VisualController.IGNORE);
            m_triggerMenu.add(getExternalTriggerMenuItem());
            m_triggerMenu.add(getResourceTriggerMenuItem());
            m_triggerMenu.add(getTimeTriggerMenuItem());
            m_triggerMenu.add(getRemoveTriggerMenuItem());
        }
        return m_triggerMenu;
    }

    private JMenu getArcMenu()
    {
        if (m_arcMenu == null)
        {
            m_arcMenu = new JMenu("Arc");
            VisualController.getInstance().addElement(m_arcMenu, VisualController.ARC_SELECTION, VisualController.ALWAYS, VisualController.IGNORE);
            m_arcMenu.add(getRouteMenuItem());
            m_arcMenu.add(getUnrouteMenuItem());
            m_arcMenu.add(getAddPointMenuItem());
            m_arcMenu.add(getRemovePointMenuItem());
        }
        return m_arcMenu;
    }

    private JMenuItem getAddPointMenuItem()
    {
        if (m_addPointMenuItem == null)
        {
            m_addPointMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_POINT));
        }
        return m_addPointMenuItem;
    }

    private JMenuItem getRemovePointMenuItem()
    {
        if (m_removePointMenuItem == null)
        {
            m_removePointMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REMOVE_POINT));
        }
        return m_removePointMenuItem;
    }

    private JMenuItem getPropertiesMenuItem()
    {
        if (m_propertiesMenuItem == null)
        {
            m_propertiesMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_OPEN_PROPERTIES));
        }
        return m_propertiesMenuItem;
    }

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
}