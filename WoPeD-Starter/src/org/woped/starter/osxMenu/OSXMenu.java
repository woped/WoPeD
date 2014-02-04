/*
 *
 */
package org.woped.starter.osxMenu;

import java.awt.Toolkit;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import org.woped.gui.translations.Messages;

// TODO: Auto-generated Javadoc
/**
 * The Class OSXMenu.
 */
public class OSXMenu extends JMenu {

   /**
	 * 
	 */
	private static final long serialVersionUID = 8385613195699694481L;

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
    * @return the OSX menu item
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public OSXMenuItem addMenuItem(String itemName) {
	  OSXMenuItem item = new OSXMenuItem(itemName);
      this.add(item);
      return item;
   }
   /**
    * Adds the menu item and reads the shortcut (and shortcutmodifier) from Messages_xx.properties
    *
    * @param itemName the item name
    * @param propertiesPrefixForShortcuts the prefix in Messages_xx.properties
    * @return the OSX menu item
    * @param ignoreDefaultKeyMask pass true if the default key (eg. Win CTRL; Mac cmd) should be ignored
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public OSXMenuItem addMenuItem(String itemName, String propertiesPrefixForShortcuts, Boolean ignoreDefaultKeyMask) {
	   OSXMenuItem item = new OSXMenuItem(itemName);
	   if(!ignoreDefaultKeyMask){
			item.setAccelerator(KeyStroke.getKeyStroke(Messages.getShortcutKey(propertiesPrefixForShortcuts), (Messages.getShortcutModifier(propertiesPrefixForShortcuts) | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		}
		else{
			item.setAccelerator(KeyStroke.getKeyStroke(Messages.getShortcutKey(propertiesPrefixForShortcuts), (Messages.getShortcutModifier(propertiesPrefixForShortcuts))));
		}
	   this.add(item);
	   return item;
   }
   /**
    * Adds the menu item and reads the shortcut (and shortcutmodifier) from Messages_xx.properties
    *
    * @param itemName the item name
    * @param propertiesPrefixForShortcuts the prefix in Messages_xx.properties
    * @return the OSX menu item
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public OSXMenuItem addMenuItem(String itemName, String propertiesPrefixForShortcuts) {
	   return addMenuItem(itemName, propertiesPrefixForShortcuts, false);
   }
//   public OSXMenuItem addMenuItemWithShortcut(String itemName, int shortcut) {
//	  OSXMenuItem item = new OSXMenuItem(itemName);
//      item.setAccelerator(KeyStroke.getKeyStroke(shortcut, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//      this.add(item);
//      return item;
//   }
//   public OSXMenuItem addMenuItemWithShortcut(String itemName, int shortcut, int modifier) {
//	  OSXMenuItem item = new OSXMenuItem(itemName);
//      item.setAccelerator(KeyStroke.getKeyStroke(shortcut, (modifier | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
//      this.add(item);
//      return item;
//   }
//   public OSXMenuItem addMenuItemWithSingleShortcut(String itemName, int shortcut) {
//	   OSXMenuItem item = new OSXMenuItem(itemName);
//	   item.setAccelerator(KeyStroke.getKeyStroke(shortcut,0));
//	   this.add(item);
//	   return item;
//   }
   /**
    * Adds the menu item and reads the shortcut (and shortcutmodifier) from Messages_xx.properties
    *
    * @param itemName the item name
    * @param synchronizedItem the checkbox to synchronize
    * @return the OSXCheckboxMenuItem
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public OSXCheckboxMenuItem addCheckboxMenuItem(String itemName, final JCheckBox synchronizedItem) {
		  OSXCheckboxMenuItem item = new OSXCheckboxMenuItem(itemName, synchronizedItem);
	      this.add(item);
	      return item;
   }
   /**
    * Adds the menu item and reads the shortcut (and shortcutmodifier) from Messages_xx.properties
    *
    * @param itemName the item name
    * @param propertiesPrefixForShortcuts the prefix in Messages_xx.properties
    * @param synchronizedItem the checkbox to synchronize
    * @param ignoreDefaultKeyMask pass true if the default key (eg. Win CTRL; Mac cmd) should be ignored
    * @return the OSXCheckboxMenuItem
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public OSXCheckboxMenuItem addCheckboxMenuItem(String itemName, String propertiesPrefixForShortcuts, final JCheckBox synchronizedItem, Boolean ignoreDefaultKeyMask) {
	   OSXCheckboxMenuItem item = new OSXCheckboxMenuItem(itemName, synchronizedItem);
	   if(!ignoreDefaultKeyMask){
			item.setAccelerator(KeyStroke.getKeyStroke(Messages.getShortcutKey(propertiesPrefixForShortcuts), (Messages.getShortcutModifier(propertiesPrefixForShortcuts) | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		}
		else{
			item.setAccelerator(KeyStroke.getKeyStroke(Messages.getShortcutKey(propertiesPrefixForShortcuts), (Messages.getShortcutModifier(propertiesPrefixForShortcuts))));
		}
	   this.add(item);
	   return item;
   }
   /**
    * Adds the menu item and reads the shortcut (and shortcutmodifier) from Messages_xx.properties
    *
    * @param itemName the item name
    * @param propertiesPrefixForShortcuts the prefix in Messages_xx.properties
    * @param synchronizedItem the checkbox to synchronize
    * @return the OSXCheckboxMenuItem
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public OSXCheckboxMenuItem addCheckboxMenuItem(String itemName, String propertiesPrefixForShortcuts, final JCheckBox synchronizedItem) {
	   return addCheckboxMenuItem(itemName, propertiesPrefixForShortcuts, synchronizedItem, false);
   }
//   public OSXCheckboxMenuItem addCheckboxMenuItemWithShortcut(String itemName, int shortcut, final JCheckBox synchronizedItem) {
//	  OSXCheckboxMenuItem item = new OSXCheckboxMenuItem(itemName, synchronizedItem);
//      item.setAccelerator(KeyStroke.getKeyStroke(shortcut, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//      this.add(item);
//      return item;
//   }
//   public OSXCheckboxMenuItem addCheckboxMenuItemWithShortcut(String itemName, int shortcut, int modifier, final JCheckBox synchronizedItem) {
//	  OSXCheckboxMenuItem item = new OSXCheckboxMenuItem(itemName, synchronizedItem);
//      item.setAccelerator(KeyStroke.getKeyStroke(shortcut, (modifier | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
//      this.add(item);
//      return item;
//   }
//   public OSXCheckboxMenuItem addMenuItemWithSingleShortcut(String itemName, int shortcut, final JCheckBox synchronizedItem) {
//	   OSXCheckboxMenuItem item = new OSXCheckboxMenuItem(itemName, synchronizedItem);
//      item.setAccelerator(KeyStroke.getKeyStroke(shortcut,0));
//      this.add(item);
//      return item;
//   }

public void addSubMenu(OSXMenu subMenu) {
	this.add(subMenu);
}



}
