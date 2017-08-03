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
 * This exception is thrown when two frames are not in relation, but
 * are thought to be. 
 * @author Nils Reiter
 *
 */
public class NoFrameRelationException extends FrameNetException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * The supposed to be sub frame
	 */
	Frame subFrame = null;
	
	/**
	 * The supposed to be super frame
	 */
	Frame superFrame = null;
	
	/**
	 * Stores the two frames and creates the exception.
	 * @param superFrame The frame that is supposed to be the super frame.
	 * @param subFrame The frame that is supposed to be the sub frame.
	 */
	public NoFrameRelationException(Frame superFrame, Frame subFrame) {
		super(subFrame.getName() + " does not inherit from " + superFrame.getName());
		this.subFrame = subFrame;
		this.superFrame = superFrame;
	}

	/**
	 * The frame used as sub frame of the not existing relation
	 * 
	 * @return the subFrame
	 */
	public Frame getSubFrame() {
		return subFrame;
	}

	/**
	 * The frame used as super frame of the not existing relation
	 * @return the superFrame
	 */
	public Frame getSuperFrame() {
		return superFrame;
	};
	
	
	
}
