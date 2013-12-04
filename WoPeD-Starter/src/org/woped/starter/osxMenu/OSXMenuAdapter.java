/*
 * 
 */
package org.woped.starter.osxMenu;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

// TODO: Auto-generated Javadoc
/**
 * The Class OSXMenuAdapter.
 */
public class OSXMenuAdapter {
   
   /** The menu bar. */
   private JMenuBar menuBar;
   
   /**
    * Instantiates a new oSX menu adapter.
    *
    * @param frame the frame
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public OSXMenuAdapter(JFrame frame) {
      menuBar = new JMenuBar();
      frame.setJMenuBar(menuBar);
   }
   /**
    * Adds the menu.
    *
    * @param menuItem the menu item
    * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
    */
   public void addMenu(OSXMenu menuItem){
      this.menuBar.add(menuItem);
   }


}
