/*
 * 
 */
package org.woped.starter.osxMenu;

import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.controller.VisualController;
import org.woped.editor.controller.vep.ViewEvent;


// TODO: Auto-generated Javadoc
/**
 * The Class OSXMenuItem.
 */
public class OSXMenuItem extends JMenuItem {
    
    /**
     * The listener interface for receiving shortcutAction events.
     * The class that is interested in processing a shortcutAction
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addShortcutActionListener<code> method. When
     * the shortcutAction event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ShortcutActionEvent
     */
    class ShortcutActionListener implements ActionListener {

		/** The event_id. */
		private int			event_id;
		/** The action_id. */
		private String      action_id;
		/** The action. */
		private WoPeDAction	action;				
		/**
		 * Instantiates a new shortcut action listener.
		 *
		 * @param mediator the mediator
		 * @param action_id the action_id
		 * @param event_id the event_id
		 * @param target the target
		 * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
		 */
		public ShortcutActionListener(AbstractApplicationMediator mediator, String action_id, int event_id, JComponent target) {
			action = ActionFactory.getStaticAction(action_id);
			//VisualController.getInstance().addElement(action, VisualController.WITH_EDITOR, VisualController.WITH_EDITOR, VisualController.IGNORE);
			ActionFactory.addTarget(mediator, action_id, target);
			this.event_id = event_id;
			this.action_id = action_id;
			target.setName(action_id);
			//target.setEnabled(false);
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {	
			action.actionPerformed(new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_GUI, event_id));
		}
    }
  
	/**
	 * Instantiates a new oSX menu item.
	 *
	 * @param itemName the item name
	 * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
	 */
	public OSXMenuItem(String itemName) {
		super(itemName);
	}
	
	/**
	 * Adds the action.
	 *
	 * @param mediator the mediator
	 * @param action_id the action_id
	 * @param viewEvent the view event
	 * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
	 */
	public void addAction(AbstractApplicationMediator mediator, String action_id, int viewEvent){
		this.addActionListener(new ShortcutActionListener(mediator, action_id, viewEvent, this));
	}



}
