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
 * This exception is thrown when a frame element should be generalized in a given frame, but 
 * has no inheritance relation with a frame element in that frame.
 * 
 * 
 * @author Nils Reiter
 *
 */
public class NoSuperFrameElementException extends FrameNetException {
	
	static final long serialVersionUID = 1L;
	
	/**
	 * The frame element.
	 */
	FrameElement frameElement = null;
	
	/**
	 * The frame, in which the frame element has no ancestor.
	 */
	Frame superFrame = null;
	
	/**
	 * Constructs the exception.
	 * @param frameElement The frame element.
	 * @param frame The supposed to be super frame.
	 */
	public NoSuperFrameElementException(FrameElement frameElement, Frame frame) {
		
		super(frameElement.toString() + " has no ancestor in " + frame.getName());
		this.frameElement = frameElement;
		this.superFrame = frame;
	}

	/**
	 * Returns the frame element to be generalized
	 * 
	 * @return the frameElement
	 */
	public FrameElement getFrameElement() {
		return frameElement;
	}

	/**
	 * Returns the frame in which the given frame element should be generalized
	 * 
	 * @return the superFrame
	 */
	public Frame getSuperFrame() {
		return superFrame;
	}
}
