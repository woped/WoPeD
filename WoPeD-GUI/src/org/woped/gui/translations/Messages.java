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
/*
 * Created on 29.08.2004
 *
 */
package org.woped.gui.translations;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.gui.IUserInterface;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Platform;

/**
 * @author Thomas Pohl
 */
public abstract class Messages
{

    private static final String         BUNDLE_NAME     = "org.woped.gui.translations.Messages";               //$NON-NLS-1$

    public static boolean exists(String key)
    {
    	try
        {
    		PropertyResourceBundle.getBundle(BUNDLE_NAME, ConfigurationManager.getConfiguration().getLocale()).getString(key);
            return true;
        } catch (MissingResourceException e)
        {
        	return false;
        }
    }

    public static String getString(String key)
    {
        try
        {
            return PropertyResourceBundle.getBundle(BUNDLE_NAME, ConfigurationManager.getConfiguration().getLocale()).getString(key);
        } catch (MissingResourceException e)
        {
            LoggerManager.debug(Constants.TRANSLATIONS_LOGGER, "Resource not found: " + key);
            return '!' + key + '!';
        }
    }

    public static String getString(String key, Object[] params)
    {
        StringBuffer result = new StringBuffer(getString(key));
        if (params != null)
        {
            for (int i = 0; i < params.length; i++)
            {
                if (params[i] != null)
                {
                    int pos = -1;
                    while ((pos = result.indexOf("%" + i)) > -1)
                    {
                        result.replace(pos, pos + ("%" + i).length(), params[i].toString());
                    }
                }
            }
        }
        return result.toString();
    }

    public static String getStringForLocale(String key, Locale locale)
    {
        PropertyResourceBundle tempRB = (PropertyResourceBundle) PropertyResourceBundle.getBundle(BUNDLE_NAME, locale);
        try
        {
            return tempRB.getString(key);
        } catch (MissingResourceException e)
        {
            LoggerManager.debug(Constants.TRANSLATIONS_LOGGER, "Resource not found: " + key);
            return '!' + key + '!';
        }
    }

    public static String getTitle(String propertiesPrefix)
    {
        return getString(propertiesPrefix + ".Title");
    }

    public static String getTitle(String propertiesPrefix, Object[] args)
    {
        return getString(propertiesPrefix + ".Title", args);
    }

    public static String getIconLocation(String propertiesPrefix)
    {
        return getString(propertiesPrefix + ".Icon");
    }

    public static String getImageLocation(String propertiesPrefix)
    {
        return getString(propertiesPrefix + ".Image");
    }

    public static String getCursorIconLocation(String propertiesPrefix)
    {
        return getString(propertiesPrefix + ".Icon");
    }

    public static ImageIcon getImageIcon(String propertiesPrefix)
    {
        String iconLocation = getIconLocation(propertiesPrefix);
        URL iconURL = IUserInterface.class.getResource(iconLocation);
        if (iconURL != null)
        {
            ImageIcon result = null;
            try
            {
                result = new ImageIcon(iconURL);
            } catch (RuntimeException e)
            {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }

    public static Image getImageSource(String propertiesPrefix)
    {
        String imageLocation = getImageLocation(propertiesPrefix);
        URL iconURL = IUserInterface.class.getResource(imageLocation);
        if (iconURL != null)
        {
            Image result = null;
            try
            {
                result = ImageIO.read(iconURL);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }

    public static ImageIcon getCursorImageIcon(String propertiesPrefix)
    {
        String iconLocation = getCursorIconLocation(propertiesPrefix);
        URL iconURL = IUserInterface.class.getResource(iconLocation);
        if (iconURL != null)
        {
            ImageIcon result = null;
            try
            {
                result = new ImageIcon(iconURL);
            } catch (RuntimeException e)
            {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }


    public static int getMnemonic(String propertiesPrefix)
    {
        // if (true) return KeyEvent.VK_N;
        String mnemonicStr = getString(propertiesPrefix + ".Mnemonic");
        if (mnemonicStr != null && mnemonicStr.length() > 0 && mnemonicStr.charAt(0) != '!')
        {
            char mnChar;
            mnChar = Character.toUpperCase(mnemonicStr.charAt(0));
            return (int) mnChar;
        }

        return KeyEvent.VK_UNDEFINED;
    }

    public static KeyStroke getShortcut(String propertiesPrefix)
    {
        String shortcutString = getString(propertiesPrefix + ".Shortcut");
        KeyStroke result = KeyStroke.getKeyStroke(shortcutString);

        return result;
    }

	/**
	 * HashMaps for converting from String to KeyEvent/Input Event
	 *
	 * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
	 */
    private static HashMap<String, Integer> shortcutKeyCodeFromString = null;
	private static HashMap<String, Integer> modifierKeyCodeFromString = null;

	/**
	 * Create Mapping from String to KeyEvent
	 *
	 * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
	 */
    private static void initShortcutMap(){
    	// converts names to codes
        shortcutKeyCodeFromString = new HashMap<String,Integer>();
    	modifierKeyCodeFromString = new HashMap<String,Integer>();
    	try {
    		// Get all of the fields in KeyEvent
    		Field[] fields = KeyEvent.class.getFields();
    		for (int i = 0; i < fields.length; i++) {

    			String fieldName = fields[i].getName();
    			// get all elements in field representing key codes
    			if (fieldName.startsWith("VK")) {
    				int keyCode = fields[i].getInt(null);
    				String keyName = fieldName.substring(3);//VK_
    				shortcutKeyCodeFromString.put(keyName,keyCode);
    			}
    		}
    		shortcutKeyCodeFromString.put("",0);
    		// Get all of the fields in InputEvent
    		fields = java.awt.event.InputEvent.class.getFields();
    		for (int i = 0; i < fields.length; i++) {
    			String fieldName = fields[i].getName();
    			int keyCode = (int)fields[i].getLong(null);
    			String keyName = fieldName.substring(0, (fieldName.length()-5));//xxx_MASK
    			modifierKeyCodeFromString.put(keyName,keyCode);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	/**
	 * Create Mapping from String to InputEvent
	 *
	 * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
	 */
    private static void initModifierMap(){
    	// converts names to codes
    	modifierKeyCodeFromString = new HashMap<String,Integer>();
    	try {
    		// Get all of the fields in InputEvent
    		Field[] fields = java.awt.event.InputEvent.class.getFields();
    		for (int i = 0; i < fields.length; i++) {
    			String fieldName = fields[i].getName();
    			int keyCode = (int)fields[i].getLong(null);
    			String keyName = fieldName.substring(0, (fieldName.length()-5));//xxx_MASK
    			modifierKeyCodeFromString.put(keyName,keyCode);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    /**
     * Gets the shortcut key. Returns 0 (no modifier) if no entry is found.
     *
     * @param propertiesPrefix the properties prefix
     * @return the shortcut key
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    public static int getShortcutKey(String propertiesPrefix)
    {
    	if(shortcutKeyCodeFromString == null){
    		initShortcutMap();
    	}
    	String osIdentifier = "";
    	if(Platform.isMac()){
    		osIdentifier = "_Mac";
    	}
    	else if(Platform.isWindows()){
    		osIdentifier = "_Windows";
    	}
    	else if(Platform.isUnix()){
    		osIdentifier = "_Unix";
    	}

    	if(exists(propertiesPrefix + ".Shortcut" + osIdentifier )){
    		String shortcutString = getString(propertiesPrefix + ".Shortcut" + osIdentifier);
    		return shortcutKeyCodeFromString.get(shortcutString.toUpperCase());
    	}
    	else if(exists(propertiesPrefix + ".Shortcut")){
    		String shortcutString = getString(propertiesPrefix + ".Shortcut");
    		return shortcutKeyCodeFromString.get(shortcutString.toUpperCase());
    	}
    	else
    		return 0;
    }
    /**
     * Gets the modifier key. Returns 0 (no modifier) if no entry is found.
     *
     * @param propertiesPrefix the properties prefix
     * @return the modifier key
     * @author <a href="mailto:lukas-riegel@freenet.de">Lukas Riegel</a> <br>
     */
    public static int getShortcutModifier(String propertiesPrefix)
    {
    	if(modifierKeyCodeFromString == null){
    		initModifierMap();
    	}
    	String osIdentifier = "";
    	if(Platform.isMac()){
    		osIdentifier = "_Mac";
    	}
    	else if(Platform.isWindows()){
    		osIdentifier = "_Windows";
    	}
    	else if(Platform.isUnix()){
    		osIdentifier = "_Unix";
    	}
    	if(exists(propertiesPrefix + ".ShortcutModifier" + osIdentifier)){
    		String modifierString = getString(propertiesPrefix + ".ShortcutModifier" + osIdentifier);
    		return modifierKeyCodeFromString.get(modifierString.toUpperCase());
    	}
    	else if(exists(propertiesPrefix + ".ShortcutModifier")){
    		String modifierString = getString(propertiesPrefix + ".ShortcutModifier");
    		return modifierKeyCodeFromString.get(modifierString.toUpperCase());
    	}
    	else
    		return 0;
    }

    public static String getWoPeDVersionWithTimestamp() {
        String version = getString("Application.Version");
        String builtstamp = getString("Application.Builtstamp");
        if ("@builtstamp@".equals(builtstamp)) {
            return version;
        }
        else {
            return version + "." + builtstamp;
        }
    }

    /**
     *
     * @param key
     * @param args
     * @return returns localized String, placeholders replaced by args
     */
    public static String getStringReplaced(String key, String[] args)
    {
        return MessageFormat.format(Messages.getString(key), (Object[]) args);
    }

}