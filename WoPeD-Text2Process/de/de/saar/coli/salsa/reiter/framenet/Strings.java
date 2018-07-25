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
 * This utility class contains static strings that represent the annotation
 * status of FrameNet frame elements. This class is likely to be changed into an
 * enum soon.
 * 
 * @author reiter
 * @since 0.4.1
 * 
 */
public class Strings {

	/**
	 * Used for the status "Finished_Initial"
	 */
	static String Status_Finished_Initial = "Finished_Initial";

	/**
	 * Used for the status "Created"
	 */
	static String Status_Created = "Created";

	/**
	 * Used for the status "In_Use"
	 */
	static String Status_In_Use = "In_Use";

	/**
	 * Used for the status "FN1_Sent"
	 */
	static String Status_FN1_Sent = "FN1_Sent";

	/**
	 * This method maps a non-static string onto a static one. This is used in
	 * order to reduce memory consumption.
	 * 
	 * @param s
	 *            The string to map
	 * @return A static String
	 */
	protected static String getString(String s) {
		if (s.equals(Status_Finished_Initial))
			return Status_Finished_Initial;
		if (s.equals(Status_Created))
			return Status_Created;
		if (s.equals(Status_In_Use))
			return Status_In_Use;
		if (s.equals(Status_FN1_Sent))
			return Status_FN1_Sent;
		return s;
	}
}
