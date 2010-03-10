package org.woped.editor.gui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.VisualController;

@SuppressWarnings("serial")
public class PopupMenuUML extends JPopupMenu
{

    private static PopupMenuUML instance;
    //
    private JMenu               addMenu             = null;
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
        }
        return addMenu;
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