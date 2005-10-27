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
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
/*
 * Created on 29.08.2004
 *
 */
package org.woped.core.utilities;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.woped.core.Constants;
import org.woped.core.config.LoggerManager;
import org.woped.core.gui.IUserInterface;

/**
 * @author Thomas Pohl
 */
public abstract class Messages {

	private static final String BUNDLE_NAME = "org.woped.properties.Messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			LoggerManager.debug(Constants.CORE_LOGGER, "Resource not found: " + key);
			return '!' + key + '!';
		}
	}

	public static String getTitle(String propertiesPrefix) {
		return getString(propertiesPrefix + ".Title");
	}

	public static String getIconLocation(String propertiesPrefix) {
		return getString(propertiesPrefix + ".Icon");
	}

	public static ImageIcon getImageIcon(String propertiesPrefix) {
		String iconLocation = getIconLocation(propertiesPrefix);
		URL iconURL = IUserInterface.class.getResource(iconLocation);
		if (iconURL != null) {
			ImageIcon result = null;
			try {
				result = new ImageIcon(iconURL);
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			return result;
		}
		return null;
	}

	public static int getMnemonic(String propertiesPrefix) {
		// if (true) return KeyEvent.VK_N;
		String mnemonicStr = getString(propertiesPrefix + ".Mnemonic");
		if (mnemonicStr != null && mnemonicStr.length() > 0
				&& mnemonicStr.charAt(0) != '!') {
			char mnChar;
			mnChar = Character.toUpperCase(mnemonicStr.charAt(0));
			return (int) mnChar;
		}

		return KeyEvent.VK_UNDEFINED;
	}

	public static KeyStroke getShortcut(String propertiesPrefix) {
		String shortcutString = getString(propertiesPrefix + ".Shortcut");
		KeyStroke result = KeyStroke.getKeyStroke(shortcutString);
		return result;
	}

	public static String getWoPeDVersion(boolean withStamp) {
		String version = getString("Application.Version");
		String builtstamp = getString("Application.Builtstamp");
		if ("@builtstamp@".equals(builtstamp) | !withStamp) {
			return version;
		} else {
			return version + "." + builtstamp;
		}
	}

}