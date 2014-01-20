package org.woped.starter;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.woped.gui.translations.Messages;

public class GlobalShortcutEventDispatcher implements KeyEventDispatcher{

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
			//Action is a combination of ButtonActionEvent and MouseEvent
			//Special logic for clicking
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
		}
		return false;
	}


}
