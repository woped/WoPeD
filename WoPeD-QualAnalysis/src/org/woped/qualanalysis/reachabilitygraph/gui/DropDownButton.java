// Process Dashboard - Data Automation Tool for high-maturity processes
// Copyright (C) 2003 Software Process Dashboard Initiative
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//
// The author(s) may be contacted at:
// Process Dashboard Group
// c/o Ken Raisor
// 6137 Wardleigh Road
// Hill AFB, UT 84056-5843
//
// E-Mail POC:  processdash-devel@lists.sourceforge.net


package org.woped.qualanalysis.reachabilitygraph.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;


public class DropDownButton extends Box {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton mainButton;
    private JButton dropDownButton;
    private boolean dropDownEnabled = false;
    private boolean mainRunsDefaultMenuOption = true;
    private Icon enabledDownArrow, disDownArrow;
    private DropDownMenu menu;
    private MainButtonListener mainButtonListener = new MainButtonListener();


    public DropDownButton(JButton mainButton) {
        super(BoxLayout.X_AXIS);

        menu = new DropDownMenu();
        menu.getPopupMenu().addContainerListener(new MenuContainerListener());
        JMenuBar bar = new JMenuBar();
        bar.add(menu);
        bar.setMaximumSize(new Dimension(0,100));
        bar.setMinimumSize(new Dimension(0,1));
        bar.setPreferredSize(new Dimension(0,1));
        add(bar);

        this.mainButton = mainButton;
       
        mainButton.setMaximumSize(new Dimension(20,20));
        mainButton.setMinimumSize(new Dimension(20,20));
        mainButton.setPreferredSize(new Dimension(20,20));
        mainButton.setFocusable(false);
        mainButton.setBorderPainted(false);
        
        //mainButton.addActionListener(mainButtonListener);
        mainButton.setBorder
            (new RightChoppedBorder(mainButton.getBorder(), 2));
        mainButton.addMouseListener(new DropDownListener());
        
        add(mainButton);

        enabledDownArrow = new SmallDownArrow();
        disDownArrow = new SmallDisabledDownArrow();
        dropDownButton = new JButton(disDownArrow);
        dropDownButton.setDisabledIcon(disDownArrow);
        dropDownButton.addMouseListener(new DropDownListener());
        dropDownButton.setMaximumSize(new Dimension(11,100));
        dropDownButton.setMinimumSize(new Dimension(11,10));
        dropDownButton.setPreferredSize(new Dimension(11,10));
        dropDownButton.setFocusPainted(false);
        dropDownButton.setBorderPainted(false);
        add(dropDownButton);

        setEnabled(false);
    }

    public DropDownButton()                 { this(new JButton());     }
    public DropDownButton(Action a)         { this(new JButton(a));    }
    public DropDownButton(Icon icon)        { this(new JButton(icon)); }
    public DropDownButton(String text)      { this(new JButton(text)); }
    public DropDownButton(String t, Icon i) { this(new JButton(t, i)); }

    public JButton getButton()         { return mainButton;     }
    //public JButton getDropDownButton() { return dropDownButton; }
    public JMenu   getMenu()           { return menu;           }

    public void setEnabled(boolean enable) {
        mainButton.setEnabled(enable);
        dropDownButton.setEnabled(enable);
    }
    public boolean isEnabled() {
        return mainButton.isEnabled();
    }
    public boolean isEmpty() {
        return (menu.getItemCount() == 0);
    }
    
    public void setToolTipText(String value){
    	getButton().setToolTipText(value);
    	dropDownButton.setToolTipText(value);
    }

    /** Set the behavior of the main button.
     *
     * @param enable if true, a click on the main button will trigger
     *    an actionPerformed() on the first item in the popup menu.
     */
    public void setRunFirstMenuOption(boolean enable) {
        mainButton.removeActionListener(mainButtonListener);
        mainRunsDefaultMenuOption = enable;
        setEnabled(mainRunsDefaultMenuOption == false ||
                   isEmpty() == false);
        if (mainRunsDefaultMenuOption)
            mainButton.addActionListener(mainButtonListener);
    }

    /** @return true if a click on the main button will trigger an
     *    actionPerformed() on the first item in the popup menu.
     */
    public boolean getRunFirstMenuOption() {
        return mainRunsDefaultMenuOption;
    }

    private void setDropDownEnabled(boolean enable) {
        dropDownEnabled = enable;
        dropDownButton.setIcon(enable ? enabledDownArrow : disDownArrow);
        if (mainRunsDefaultMenuOption)
            setEnabled(enable);
    }

    /** This object responds to events on the main button. */
    private class MainButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (mainRunsDefaultMenuOption && !isEmpty()) {
                JMenuItem defaultItem = menu.getItem(0);
                if (defaultItem != null)
                    defaultItem.doClick(0);
            }
        }
    }

    /** This object responds to events on the drop-down button. */
    private class DropDownListener extends MouseAdapter {
        boolean pressHidPopup = false;
        public void mouseClicked(MouseEvent e) {
            if (dropDownEnabled && !pressHidPopup) menu.doClick(0);
        }
        public void mousePressed(MouseEvent e) {
            if (dropDownEnabled) menu.dispatchMouseEvent(e);
            if (menu.isPopupMenuVisible())
                pressHidPopup = false;
            else
                pressHidPopup = true;

        }
        public void mouseReleased(MouseEvent e) { }
    }

    /** This object watches for insertion/deletion of menu items in
     * the popup menu, and disables the drop-down button when the
     * popup menu becomes empty. */
    private class MenuContainerListener implements ContainerListener {
        public void componentAdded(ContainerEvent e) {
            setDropDownEnabled(true);
        }
        public void componentRemoved(ContainerEvent e) {
            setDropDownEnabled(!isEmpty());
        }
    }

    /** An adapter that wraps a border object, and chops some number of
     *  pixels off the right hand side of the border.
     */
    private class RightChoppedBorder implements Border {
        private Border b;
        private int w;

        public RightChoppedBorder(Border b, int width) {
            this.b = b;
            this.w = width;
        }

        public void paintBorder(Component c,
                                Graphics g,
                                int x,
                                int y,
                                int width,
                                int height) {
            Shape clipping = g.getClip();
            g.setClip(x, y, width, height);
            b.paintBorder(c, g, x, y, width + w, height);
            g.setClip(clipping);
        }

        public Insets getBorderInsets(Component c) {
            Insets i = b.getBorderInsets(c);
            return new Insets(i.top, i.left, i.bottom, i.right-w);
        }

        public boolean isBorderOpaque() {
            return b.isBorderOpaque();
        }
    }

    private class DropDownMenu extends JMenu {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void dispatchMouseEvent(MouseEvent e) {
            processMouseEvent(e);
        }
    }
    
    
    /** An icon to draw a small downward-pointing arrow.
     */
    private static class SmallDownArrow implements Icon {

        Color arrowColor = Color.black;

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(arrowColor);
            g.drawLine(x, y, x+4, y);
            g.drawLine(x+1, y+1, x+3, y+1);
            g.drawLine(x+2, y+2, x+2, y+2);
        }

        public int getIconWidth() {
            return 6;
        }

        public int getIconHeight() {
            return 4;
        }

    }

    /** An icon to draw a disabled small downward-pointing arrow.
     */
    private static class SmallDisabledDownArrow extends SmallDownArrow {

        public SmallDisabledDownArrow() {
            arrowColor = new Color(140, 140, 140);
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            super.paintIcon(c, g, x, y);
            g.setColor(Color.white);
            g.drawLine(x+3, y+2, x+4, y+1);
            g.drawLine(x+3, y+3, x+5, y+1);
        }
    }
}
