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
 * This inferface specifies that objects implementing it have a name.
 * This refers to most of the FrameNet entities that are encoded in 
 * the XML files.
 * 
 * @author Nils Reiter
 *
 */
public interface IHasName {
	
	/**
	 * Returns the name of an entity. The name is not necessarily nice to read.
	 * @return the name of an entity. 
	 */
	public String getName();
}
