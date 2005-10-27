package org.woped.editor.gui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.VisualController;

public class PopupMenuUML extends JPopupMenu
{

    private static PopupMenuUML instance;
    //
    private JMenu               addMenu             = null;
    private JMenuItem           addActivityMenuItem = null;
    private JMenuItem           addStartMenuItem    = null;
    private JMenuItem           addStopMenuItem     = null;
    private JMenuItem           addAndMenuItem      = null;
    private JMenuItem           addXorMenuItem      = null;
    //
    private JMenu               arcMenu             = null;
    private JMenuItem           addPointMenuItem    = null;
    private JMenuItem           removePointMenuItem = null;
    private JMenuItem           routeMenuItem       = null;
    private JMenuItem           unrouteMenuItem     = null;
    //
    private JMenuItem           removeMenuItem      = null;

    private PopupMenuUML()
    {
        add(getAddMenu());
        add(getArcMenu());
        add(getRemoveMenuItem());
    }

    public static PopupMenuUML getInstance()
    {
        if (instance == null)
        {
            instance = new PopupMenuUML();
        }
        instance.pack();
        return instance;
    }

    private JMenu getArcMenu()
    {
        if (arcMenu == null)
        {
            arcMenu = new JMenu("Arc");
            VisualController.getInstance().addElement(arcMenu, VisualController.ARC_SELECTION, VisualController.ALWAYS, VisualController.IGNORE);
            arcMenu.add(getRouteMenuItem());
            arcMenu.add(getUnrouteMenuItem());
            arcMenu.add(getAddPointMenuItem());
            arcMenu.add(getRemovePointMenuItem());
        }
        return arcMenu;
    }

    private JMenuItem getAddPointMenuItem()
    {
        if (addPointMenuItem == null)
        {
            addPointMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_POINT));
        }
        return addPointMenuItem;
    }

    private JMenuItem getRemovePointMenuItem()
    {
        if (removePointMenuItem == null)
        {
            removePointMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REMOVE_POINT));
        }
        return removePointMenuItem;
    }

    private JMenuItem getRouteMenuItem()
    {
        if (routeMenuItem == null)
        {
            routeMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ACTIVATE_ROUTING));
        }
        return routeMenuItem;
    }

    private JMenuItem getUnrouteMenuItem()
    {
        if (unrouteMenuItem == null)
        {
            unrouteMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_DEACTIVATE_ROUTING));
        }
        return unrouteMenuItem;
    }

    private JMenu getAddMenu()
    {
        if (addMenu == null)
        {
            addMenu = new JMenu("Add");
            VisualController.getInstance().addElement(addMenu, VisualController.NO_SELECTION, VisualController.ALWAYS, VisualController.IGNORE);
            addMenu.add(getAddActivityItem());
            addMenu.add(getAddStartItem());
            addMenu.add(getAddStopItem());
            addMenu.add(getAddAndMenuItem());
            addMenu.add(getAddXorMenuItem());

        }
        return addMenu;
    }

    private JMenuItem getAddActivityItem()
    {
        if (addActivityMenuItem == null)
        {
            addActivityMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_ACTIVITY));
        }
        return addActivityMenuItem;
    }

    private JMenuItem getAddStartItem()
    {
        if (addStartMenuItem == null)
        {
            addStartMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_START));
        }
        return addStartMenuItem;
    }

    private JMenuItem getAddStopItem()
    {
        if (addStopMenuItem == null)
        {
            addStopMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_STOP));
        }
        return addStopMenuItem;
    }

    private JMenuItem getAddAndMenuItem()
    {
        if (addAndMenuItem == null)
        {
            addAndMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_AND));
        }
        return addAndMenuItem;
    }

    private JMenuItem getAddXorMenuItem()
    {
        if (addXorMenuItem == null)
        {
            addXorMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_ADD_XOR));
        }
        return addXorMenuItem;
    }
    
    private JMenuItem getRemoveMenuItem()
    {
        if (removeMenuItem == null)
        {
            removeMenuItem = new JMenuItem(ActionFactory.getStaticAction(ActionFactory.ACTIONID_REMOVE));
        }
        return removeMenuItem;
    }
}