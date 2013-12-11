/*
 * 
 */
package org.woped.starter.osxMenu;

import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

// TODO: Auto-generated Javadoc
/**
 * The Class OSXMenu.
 */
public class OSXMenu extends JMenu {
   
   /**
    * Instantiates a new oSX menu.
    *
    * @param string the string
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public OSXMenu(String string){
      super(string);
   }
   
   /**
    * Adds the menu seperator.
    *
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public void addMenuSeperator() {
      this.addSeparator();
   }
   
   /**
    * Adds the menu item.
    *
    * @param itemName the item name
    * @return the oSX menu item
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public OSXMenuItem addMenuItem(String itemName) {
	  OSXMenuItem item = new OSXMenuItem(itemName);
      this.add(item);
      return item;
   }
   
   /**
    * Adds the menu item with shortcut.
    *
    * @param itemName the item name
    * @param shortcut the shortcut
    * @return the oSX menu item
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public OSXMenuItem addMenuItemWithShortcut(String itemName, int shortcut) {
	  OSXMenuItem item = new OSXMenuItem(itemName);
      item.setAccelerator(KeyStroke.getKeyStroke(shortcut, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      this.add(item);
      return item;
   }
   
   /**
    * Adds the menu item with shortcut.
    *
    * @param itemName the item name
    * @param shortcut the shortcut
    * @param modifier the modifier
    * @return the oSX menu item
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public OSXMenuItem addMenuItemWithShortcut(String itemName, int shortcut, int modifier) {
	  OSXMenuItem item = new OSXMenuItem(itemName);
      item.setAccelerator(KeyStroke.getKeyStroke(shortcut, (modifier | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
      this.add(item);
      return item;
   }
   
   /**
    * Adds the menu item with single shortcut.
    *
    * @param itemName the item name
    * @param shortcut the shortcut
    * @return the oSX menu item
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public OSXMenuItem addMenuItemWithSingleShortcut(String itemName, int shortcut) {
	  OSXMenuItem item = new OSXMenuItem(itemName);
      item.setAccelerator(KeyStroke.getKeyStroke(shortcut,0));
      this.add(item);
      return item;
   }
   
   public OSXCheckboxMenuItem addCheckboxMenuItem(String itemName, final JCheckBox synchronizedItem) {
		  OSXCheckboxMenuItem item = new OSXCheckboxMenuItem(itemName, synchronizedItem);
	      this.add(item);
	      return item;
   }
   public OSXCheckboxMenuItem addCheckboxMenuItemWithShortcut(String itemName, int shortcut, final JCheckBox synchronizedItem) {
	  OSXCheckboxMenuItem item = new OSXCheckboxMenuItem(itemName, synchronizedItem);
      item.setAccelerator(KeyStroke.getKeyStroke(shortcut, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      this.add(item);
      return item;
   }
   public OSXCheckboxMenuItem addCheckboxMenuItemWithShortcut(String itemName, int shortcut, int modifier, final JCheckBox synchronizedItem) {
	  OSXCheckboxMenuItem item = new OSXCheckboxMenuItem(itemName, synchronizedItem);
      item.setAccelerator(KeyStroke.getKeyStroke(shortcut, (modifier | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
      this.add(item);
      return item;
   }
   public OSXCheckboxMenuItem addMenuItemWithSingleShortcut(String itemName, int shortcut, final JCheckBox synchronizedItem) {
	   OSXCheckboxMenuItem item = new OSXCheckboxMenuItem(itemName, synchronizedItem);
      item.setAccelerator(KeyStroke.getKeyStroke(shortcut,0));
      this.add(item);
      return item;
   }

public void addSubMenu(OSXMenu subMenu) {
	this.add(subMenu);
}
   
}
