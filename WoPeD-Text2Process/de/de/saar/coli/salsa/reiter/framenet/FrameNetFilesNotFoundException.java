/**
 * 
 * Copyright 2007-2010 by Nils Reiter.
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

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.HashSet;

/**
 * This exception is thrown, when the FrameNetDatabaseInterface object does
 * not find some of the FrameNet database files
 * @author Nils Reiter
 * @since 0.2
 *
 */
public class FrameNetFilesNotFoundException extends FileNotFoundException {
	private static final long serialVersionUID = 1L;
	
	private Set<String> notFound;
	
	/**
	 * The constructor, taking the filename of the file that has
	 * not been found
	 * @param filename The filename of the file not found
	 */
	public FrameNetFilesNotFoundException (String filename) {
		notFound = new HashSet<String>();
		notFound.add(filename);
	}
	
	/**
	 * The default constructor
	 *
	 */
	public FrameNetFilesNotFoundException () {
		notFound = new HashSet<String>();
	}
	
	/**
	 * Adds a filename to the list of not found files
	 * @param filename The filename to add
	 */
	public void notFound(String filename) {
		notFound.add(filename);
	}
	
	/**
	 * Returns the list of filenames that have not been found
	 * @return The list of filenames
	 */
	public Set<String> getNotFound() {
		return notFound;
	}
	
	/**
	 * Returns an error message
	 */
	public String getMessage() {
		StringBuffer sb = new StringBuffer("Files not found: ");
		for (String f : notFound) {
			sb.append(f + ", ");
		}
		return sb.toString();
	}
}
