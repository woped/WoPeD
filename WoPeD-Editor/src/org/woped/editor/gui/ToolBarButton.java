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
package org.woped.editor.gui;

import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.VisualController;
import org.woped.gui.translations.Messages;

/**
 * Part of a small example showing basic use of JToolBar. The point here is that
 * dropping a regular JButton in a JToolBar (or adding an Action) doesn't give
 * you what you want -- namely a small button just enclosing the icon, and with
 * text labels (if any) below the icon, not to the right of it. 1999 Marty Hall,
 * http://www.apl.jhu.edu/~hall/java/
 */

@SuppressWarnings("serial")
public class ToolBarButton extends JToggleButton implements MouseListener
{
    //private static final Insets margins = new Insets(0, 0, 0, 0);

    public static final int TEXTORIENTATION_RIGHT  = 1;
    public static final int TEXTORIENTATION_LEFT   = 2;
    public static final int TEXTORIENTATION_TOP    = 3;
    public static final int TEXTORIENTATION_BOTTOM = 4;

    public ToolBarButton(Icon icon)
    {
        super(icon);
        //		setBorder(BorderFactory.createRaisedBevelBorder());
        //		addMouseListener(this);
        setBorderPainted(false);
        //		setMargin(margins);
        setVerticalTextPosition(BOTTOM);
        setHorizontalTextPosition(CENTER);
        //setSelected(true);
    }

    public ToolBarButton(URL imageURL)
    {
        this(new ImageIcon(imageURL));
    }

    public ToolBarButton(String imageFile)
    {
        this(new ImageIcon(imageFile));
    }

    public ToolBarButton(String imageFile, String text)
    {
        this(new ImageIcon(imageFile));
        setText(text);
    }

    public ToolBarButton(ImageIcon icon, String text)
    {
        this(icon);
        setText(text);
    }

    public ToolBarButton(ImageIcon icon, String text, int pos)
    {
        this(icon);
        setText(text);
        setVerticalTextPosition(CENTER);
        if (pos == TEXTORIENTATION_LEFT) setHorizontalTextPosition(LEFT);
        else setHorizontalTextPosition(RIGHT);
    }

    public void mouseEntered(MouseEvent me)
    {
    //		JToggleButton jb = (JToggleButton) me.getSource();
    //		jb.setBorderPainted(true);
    }

    public void mouseExited(MouseEvent me)
    {
    //JToggleButton jb = (JToggleButton) me.getSource();
    //jb.setBorderPainted(false);
    }

    public void mousePressed(MouseEvent me)
    {

    }

    public void mouseReleased(MouseEvent me)
    {}

    public void mouseClicked(MouseEvent me)
    {}

    /**
     * @deprecated
     * @param text
     * @param iconPath
     * @param action
     * @param enableing
     * @param visible
     * @param selected
     * @param toggleable
     * @return
     */

    public static AbstractButton createButton(String text, String iconPath, WoPeDAction action, int enableing, int visible, int selected, boolean toggleable)
    {
        ImageIcon icon = null;
        try
        {
            icon = new ImageIcon(ToolBarButton.class.getResource(iconPath));
        } catch (NullPointerException e)
        {
            //TODO Logausgabe
        }
        AbstractButton button;
        if (!toggleable)
        {
            button = new JButton(action);
        } else
        {
            button = new JToggleButton(action);
        }
        button.setIcon(icon);
        button.setToolTipText(text);
        //button.setMargin(new Insets(0, 0, 0, 0));

        VisualController.getInstance().addElement(action, enableing, visible, selected);
        return button;
    }

    /**
     * @deprecated
     * @param text
     * @param iconPath
     * @param action
     * @param enableing
     * @return
     */
    public static AbstractButton createButton(String text, String iconPath, WoPeDAction action, int enableing)
    {
        return createButton(text, iconPath, action, enableing, VisualController.IGNORE, VisualController.IGNORE, false);
    }

    /**
     * 
     * @deprecated
     * @param text
     * @param iconPath
     * @param toggleable
     * @return
     */
    public static AbstractButton createButton(String text, String iconPath, boolean toggleable)
    {
        return createButton(text, iconPath, null, VisualController.IGNORE, VisualController.IGNORE, VisualController.IGNORE, toggleable);
    }

    public static AbstractButton createButton(String propertiesPrefix, WoPeDAction action, int enableing, int visible, int selected, boolean toggleable)
    {
        return createButton(Messages.getTitle(propertiesPrefix), Messages.getIconLocation(propertiesPrefix), action, enableing, visible, selected, toggleable);
    }

    public static AbstractButton createButton(String text, WoPeDAction action, int enableing)
    {
        return createButton(text, action, enableing, VisualController.IGNORE, VisualController.IGNORE, false);
    }

    public static AbstractButton createButton(Action action)
    {
        return createButton(action, false);
    }

    /**
     * 
     * @param action
     *            The Action which should be executed whenever the new Button
     *            gets pressed.
     * @param toggleable
     *            Defines if the new Button should be toggleable
     * @return the newly created Button
     */
    public static AbstractButton createButton(Action action, boolean toggleable)
    {
        AbstractButton button;
        if (toggleable)
        {
            button = new JToggleButton(action);
        } else
        {
            button = new JButton(action);
        }
        button.setText(null);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setMnemonic(-1);
        return button;
    }

}