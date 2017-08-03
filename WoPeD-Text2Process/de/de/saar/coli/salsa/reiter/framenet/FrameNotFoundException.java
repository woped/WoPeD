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
 * This exception is thrown, when a program tries to get a frame that is not defined in the
 * FrameNet database.
 * @author Nils Reiter
 *
 */
public class FrameNotFoundException extends FrameNetException {
	static final long serialVersionUID = 1L;

	/**
	 * The name of the frame
	 */
	String frameName = null;
	
	/**
	 * Constructs the exception and stores the name of the frame.
	 * @param name The name of the frame.
	 */
	public FrameNotFoundException(String name) {
		super(name + " does not exist in the FrameNet database.");
		frameName = name;
	}

	/**
	 * Returns the name of the frame that has not been found
	 * 
	 * @return the frameName
	 */
	public String getFrameName() {
		return frameName;
	}
	
}
