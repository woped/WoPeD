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
 * This interface is used by classes whose objects are sorted in some sort of
 * hierarchy. The interface provides methods to display the hierarchy in tree
 * form as ASCII output. What relation the backbone of the hierarchy is, is up
 * to the class.
 * 
 * @author Nils Reiter
 * 
 */
public interface IHasTrees {
	/**
	 * Returns a string containing an ASCII tree directed downwards.
	 * 
	 * @return A tree-like string
	 */
	public String treeDown();

	/**
	 * Returns a string containing an ASCII tree directed upwards.
	 * 
	 * @return A tree-like string
	 */
	public String treeUp();

	/**
	 * Returns a string describing the upwards tree.
	 * 
	 * @return A description of the upwards tree
	 */
	public String treeUpInfo();

	/**
	 * Returns a string describing the downwards tree.
	 * 
	 * @return A description of the downwards tree
	 */
	public String treeDownInfo();
}
