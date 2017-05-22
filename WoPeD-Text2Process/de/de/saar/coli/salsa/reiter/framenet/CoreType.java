/**
 * 
 * Copyright 2010 by Nils Reiter.
 * 
 * This FrameNet API is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3.
 *
 * This FrameNet API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this FrameNet API.  If not, see www.gnu.org/licenses/gpl.html.
 * 
 */

package de.saar.coli.salsa.reiter.framenet;

/**
 * This enum is used to differentiate between the different types of a frame
 * element. Since 0.4.1 we are not using Strings anymore.
 * 
 * @author reiter
 * @since 0.4.1
 * 
 */
public enum CoreType {
	Core, Core_Unexpressed, Extra_Thematic, Peripheral;

	/**
	 * This method maps the String-description from the XML file onto an enum
	 * type.
	 * 
	 * @param s
	 *            The string
	 * @return The CoreType
	 */
	public static CoreType fromString(String s) {
		if (s.equals("Core"))
			return Core;
		if (s.equals("Core-Unexpressed"))
			return Core_Unexpressed;
		if (s.equals("Extra-Thematic"))
			return Extra_Thematic;
		if (s.equals("Peripheral"))
			return Peripheral;
		return null;
	}

}
