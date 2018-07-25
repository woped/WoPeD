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
 * The abstract exception for every FrameNet related error that occurs.
 * 
 * @author Nils Reiter
 *
 */
public abstract class FrameNetException extends Exception {
	static final long serialVersionUID = 1;
	
	/**
	 * The message
	 */
	String message;
	
	/**
	 * A constructor with an empty message
	 *
	 */
	public FrameNetException () {
		message = "";
	}
	
	/**
	 * A constructor with message
	 * @param msg The message
	 */
	public FrameNetException (String msg) {
		message = msg;
	}
	
	/**
	 * This method returns the method
	 */
	public String getMessage() {
		return message;
	}
}
