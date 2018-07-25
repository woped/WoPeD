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

/**
 * This exception is thrown, when a string could not be parsed, 
 * for instance in {@link FrameNet#getFrameElement(String)}. The 
 * message should contain the critical string.
 * 
 * @author Nils Reiter
 *
 */
public class ParsingException extends FrameNetException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new exception
	 * @param message The message to be stored
	 */
	public ParsingException(String message) {
		super(message);
	}
}
