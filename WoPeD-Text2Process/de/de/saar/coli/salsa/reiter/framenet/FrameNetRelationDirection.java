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
 * Used to specify the direction of a relation between frames
 * or frame elements. The direction is specified from the point
 * of view of *this* frame or frame element (what ever that may be 
 * in the given context). 
 * 
 * @author Nils Reiter
 *
 */
public enum FrameNetRelationDirection {
	/**
	 * The frame defined as "super" in the FN database
	 */
	UP, 
	
	/**
	 * The frame defined as "sub" in the FN database
	 */
	DOWN
}
