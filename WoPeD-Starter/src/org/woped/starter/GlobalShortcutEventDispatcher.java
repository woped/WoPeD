package org.woped.starter;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;

import javax.swing.SwingUtilities;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.woped.gui.translations.Messages;

public class GlobalShortcutEventDispatcher implements KeyEventDispatcher{
	private long delay = 500; //repeat rate

	private long lastKeyPressed = 0;
	/** prefix in messages.properties */
	private String prefix = null;

	/** button */
	private JCommandButton button = null;

	/** ignore System-default-key-mask ?*/
	private boolean ignoreDefaultKeyMask = false;

	/** button requires a mouse click ?*/
	private boolean buttonRequiresMouseClick = false;

	private GlobalShortcutEventDispatcher(JCommandButton observerButton, String propertiesPrefixForShortcuts){
		prefix = propertiesPrefixForShortcuts;
		button = observerButton;
	}

	private GlobalShortcutEventDispatcher(JCommandButton observedButton, String propertiesPrefixForShortcuts, boolean ignoreDefaultKeyMask){
		prefix = propertiesPrefixForShortcuts;
		button = observedButton;
		this.ignoreDefaultKeyMask = ignoreDefaultKeyMask;
	}

	/**
	 * Adds a shortcut listener for a button
	 *
	 * @param observedButton the which action should be triggered
	 * @param propertiesPrefixForShortcuts the prefix in messages.properties
	 */
	public static void addShortcutListener(JCommandButton observedButton, String propertiesPrefixForShortcuts) {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new GlobalShortcutEventDispatcher(observedButton, propertiesPrefixForShortcuts));
	}

	/**
	 * Adds a shortcut listener for a button
	 *
	 * @param observedButton the which action should be triggered
	 * @param propertiesPrefixForShortcuts the prefix in messages.properties
	 * @param ignoreDefaultKeyMask ignore system default
	 */
	public static void addShortcutButtonListener(JCommandButton observedButton, String propertiesPrefixForShortcuts, boolean ignoreDefaultKeyMask) {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new GlobalShortcutEventDispatcher(observedButton, propertiesPrefixForShortcuts, ignoreDefaultKeyMask));
	}

	/**
	 * Adds a shortcut listener for a button which requires a click action.
	 *
	 * @param observedButton the which action should be triggered
	 * @param propertiesPrefixForShortcuts the prefix in messages.properties
	 */
	public static void addShortcutClickListener(JCommandButton observedButton, String propertiesPrefixForShortcuts) {
		GlobalShortcutEventDispatcher shortcutClickListener = new GlobalShortcutEventDispatcher(observedButton, propertiesPrefixForShortcuts);
		shortcutClickListener.buttonRequiresMouseClick = true;
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(shortcutClickListener);
	}

	/**
	 * Adds a shortcut listener for a button which requires a click action.
	 *
	 * @param observedButton the which action should be triggered
	 * @param propertiesPrefixForShortcuts the prefix in messages.properties
	 * @param ignoreDefaultKeyMask ignore system default
	 */
	public static void addShortcutClickListener(JCommandButton observedButton, String propertiesPrefixForShortcuts, Boolean ignoreDefaultKeyMask) {
		GlobalShortcutEventDispatcher shortcutClickListener = new GlobalShortcutEventDispatcher(observedButton, propertiesPrefixForShortcuts, ignoreDefaultKeyMask);
		shortcutClickListener.buttonRequiresMouseClick = true;
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(shortcutClickListener);

	}

	/**
	 * Button click logic
	 */
	private void doButtonClick(){
		button.setVisible(false);
		if(buttonRequiresMouseClick){
			 SwingUtilities.invokeLater(new Runnable() {
				    public void run() {

				    	KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
				    	Window currentWindow = keyboardFocusManager.getActiveWindow();
				    	//Window currentWindow = (Window) SwingUtilities.getRoot((Component) button);
						DefaultUserInterface dui = (DefaultUserInterface) currentWindow;

						dui.getFrame().getProcessTab().setSelectedIndex(1);
						dui.getAllEditors().get(0).setDrawingMode(false);
						button.doActionClick();
						dui.getFrame().getProcessTab().setSelectedIndex(0);
				    }
				  });
		}
		else{
			//standard-button: therefore we can perform the attached action
			button.doActionClick();
		}
		button.setVisible(true);
	}

	/* (non-Javadoc)
	 * @see java.awt.KeyEventDispatcher#dispatchKeyEvent(java.awt.event.KeyEvent)
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {

		if(e.getID() == KeyEvent.KEY_RELEASED){
			if(e.getWhen() - lastKeyPressed > delay){

				if(!ignoreDefaultKeyMask){
					if(e.getKeyCode() == Messages.getShortcutKey(prefix) && e.getModifiers() == (Messages.getShortcutModifier(prefix) | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())){
						doButtonClick();
					}

				}
				else{
					if(e.getKeyCode() == Messages.getShortcutKey(prefix) && e.getModifiers() == Messages.getShortcutModifier(prefix)){
						doButtonClick();
					}
				}
				lastKeyPressed = System.currentTimeMillis();
			}
		}
		return false;
	}
}
