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
 * This interface provides methods for all annotated FrameNet data, 
 * i.e., all classes representing annotations are required to have these
 * methods.
 * 
 * @author Nils Reiter
 *
 */
public interface IHasTarget {
	/**
	 * Returns the target of the annotation
	 * @return The target of the annotation
	 */
	public IToken getTarget();
	
	/**
	 * This method sets the target of the annotation
	 * @param target The target of the annotation
	 */
	public void setTarget(IToken target);
	
	/**
	 * Returns true, if the target does not exist,
	 * i.e., if the frame or frame element is null instantiated.
	 * @return true or false
	 */
	public boolean isNullInstantiated();
	
	
}
