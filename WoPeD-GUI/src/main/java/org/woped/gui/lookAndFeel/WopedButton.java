package org.woped.gui.lookAndFeel;

import java.awt.Font;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import org.woped.core.utilities.Platform;

/**
 * WopedButton class overrides the normal JButton to improve the look and feel for 
 * OSX platforms
 * 
 * @author Florian Adolf
 */

@SuppressWarnings("serial")
public class WopedButton extends JButton {
	
	

	public WopedButton(Action a) {
		super(a);
	}

	public WopedButton() {
		super();
	}

	public WopedButton(String string) {
		super(string);
	}

	/**
	 * setText(String text) Sets the button's text. This method overrides the
	 * default setText method and changes the font on a mac to the OSX default
	 * font face and size
	 * 
	 * @param text
	 *            the string used to set the text
	 */
	@Override
	public void setText(String text) {
		if (!Platform.isMac()) {
			super.setText(text);
		} else {
			super.setFont(new Font("Lucia Grande", Font.PLAIN, 13));
			super.setText(text);
		}
	}

	/**
	 * setIcon(Icon defaultIcon) Sets the button's default icon. This icon is
	 * also used as the "pressed" and "disabled" icon if there is no explicitly
	 * set pressed icon. This method overrides the default setIcon class and set
	 * icon only if the platform is not a mac.
	 * 
	 * @param defaultIcon
	 *            the icon used as default image.
	 */
	@Override
	public void setIcon(Icon defaultIcon) {
		if (!Platform.isMac()) {
			super.setIcon(defaultIcon);
		}
	}

}
