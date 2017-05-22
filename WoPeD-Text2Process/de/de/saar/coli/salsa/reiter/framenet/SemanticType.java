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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * This class represents the semantic types in FrameNet. It is a very rudimental
 * implementation. It collects all frame elements that use a specific semantic
 * type, but it does not allow browsing through the hierarchy of semantic types.
 * 
 * That is left for future versions.
 * 
 * @author Nils Reiter
 * 
 */
public abstract class SemanticType implements Serializable, IHasNameAndID,
		IHasDefinition, IHasTrees {

	/**
	 * The set of frame elements, that use this semantic type.
	 */
	Collection<FrameElement> frameElements;

	/**
	 * The identifier of this semantic type, as found in the XML file.
	 */
	String id;

	/**
	 * The name of this semantic type.
	 */
	String name;

	/**
	 * The definition of this semantic type
	 */
	String definition;

	/**
	 * The abbreviation of this semantic type
	 */
	String abbreviation;

	/**
	 * The FrameNet object
	 */
	FrameNet frameNet;

	/**
	 * Is true, when the XML node representing this semantic type from
	 * semtypes.xml has been loaded
	 */
	Boolean nodeSupplied = false;

	/**
	 * Contains the super types of this semantic type
	 */
	Collection<SemanticType> superTypes = null;

	/**
	 * Contains the sub types of this semantic type
	 */
	Collection<SemanticType> subTypes = null;

	private static final long serialVersionUID = 7L;

	/**
	 * Adds a frame element to the list of frame elements that use this semantic
	 * type.
	 * 
	 * @param frameElement
	 *            The frame element to add.
	 */
	protected void addFrameElement(FrameElement frameElement) {
		frameElements.add(frameElement);
	}

	/**
	 * 
	 *
	 */
	private void init() {
		if (frameElements == null)
			frameElements = new HashSet<FrameElement>();
		if (superTypes == null)
			superTypes = new HashSet<SemanticType>();
		if (subTypes == null)
			subTypes = new HashSet<SemanticType>();

	}

	/**
	 * Adds a {@link FrameElement} to the list of frame elements which use this
	 * semantic type
	 * 
	 * @param frameElement
	 *            A frame element that uses this ST.
	 */
	protected void registerFrameElement(FrameElement frameElement) {
		init();
		frameElements.add(frameElement);
	}

	/**
	 * Stores a semantic type as subtype of this ST.
	 * 
	 * @param semanticType
	 *            The new subtype
	 */
	protected void registerSubType(SemanticType semanticType) {
		init();
		subTypes.add(semanticType);
	}

	protected SemanticType() {
		init();
	}

	/**
	 * Returns a string representation of this semantic type.
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		if (id.length() == 0) {
			id = String.valueOf(Math.random());
		}
		return id;
	}

	/**
	 * Returns the abbreviation
	 * 
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * Returns the supertypes
	 * 
	 * @return the superTypes
	 */
	public Collection<SemanticType> getSuperTypes() {
		return superTypes;
	}

	/**
	 * Returns a list of frame elements that use this semantic type
	 * 
	 * @return {@link FrameElement}s using this semantic type
	 */
	public Collection<FrameElement> getFrameElements() {
		return frameElements;
	}

	/**
	 * Returns a tree-like representation of the inheritance hierarchy below
	 * this semantic type
	 */
	public String treeDown() {
		return tree("", false);
	}

	/**
	 * Returns a tree-like representation of the inheritance hierarchy on top of
	 * this hierarchy
	 */
	public String treeUp() {
		return tree("", true);
	}

	public String treeUpInfo() {
		return "Inheritance tree upwards";
	}

	public String treeDownInfo() {
		return "Inheritance tree downwards";
	}

	/**
	 * 
	 * 
	 * 
	 * @param prefix
	 * @param up
	 * @return
	 */
	private String tree(String prefix, boolean up) {
		StringBuffer ret = new StringBuffer();
		ret.append(prefix + name + "\n");
		Collection<SemanticType> others;
		if (up)
			others = getSuperTypes();
		else
			others = getSubTypes();
		for (SemanticType f : others) {
			ret.append(f.tree(prefix + " ", up));
		}
		return ret.toString();
	}

	/**
	 * Returns the subtypes of this semantic type
	 * 
	 * @return The subTypes
	 */
	public Collection<SemanticType> getSubTypes() {
		return subTypes;
	};

}
