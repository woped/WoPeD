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
 * This exception is thrown when a someone tries to get a frame
 * element that is not defined in the respective frame.
 * 
 * @author Nils Reiter
 *
 */
public class FrameElementNotFoundException extends FrameNetException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * The frame for which the frame element does not exist.
	 */
	Frame frame = null;
	
	/**
	 * The name of the frame element, that does not exist.
	 */
	String frameElement = null;
	
	/**
	 * Creates a new exception object that stores the relevant data.
	 * @param frame The frame, for which the frame element could not be found.
	 * @param frameElement The name of the frame element not existing.
	 */
	public FrameElementNotFoundException(Frame frame, String frameElement) {
		super("Frame " + frame.getName() + 
				" does not contain the frame element " + frameElement);
		this.frame = frame;
		this.frameElement = frameElement;
	}


	/**
	 * Returns the frame in which was looked for the frame element
	 * 
	 * @return the frame
	 */
	public Frame getFrame() {
		return frame;
	}


	/**
	 * Returns the name of the frame element that was searched
	 * 
	 * @return the frameElement
	 */
	public String getFrameElement() {
		return frameElement;
	}

	
}
