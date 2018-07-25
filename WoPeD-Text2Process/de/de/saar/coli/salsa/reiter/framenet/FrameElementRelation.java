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
import java.util.HashMap;

import org.dom4j.Element;

/**
 * This class represents a relation between to frame elements.
 * 
 * 
 * @author Nils Reiter
 * 
 */
public class FrameElementRelation implements IHasID, Serializable, IHasName {
	/**
	 * 
	 */
	private final FrameRelation frameRelation;
	String id;
	FrameElement superFrameElement = null;
	FrameElement subFrameElement = null;

	private final static long serialVersionUID = 1L;

	protected FrameElementRelation(FrameRelation relation, Element xmlnode)
			throws FrameElementNotFoundException {
		frameRelation = relation;
		id = xmlnode.attributeValue("ID");
		superFrameElement = frameRelation.superFrame.getFrameElement(xmlnode
				.attributeValue("superFEName"));
		subFrameElement = frameRelation.subFrame.getFrameElement(xmlnode
				.attributeValue("subFEName"));

		if (!superFrameElement.getRelationsAsSuper().containsKey(
				frameRelation.getFrameNetRelation()))
			superFrameElement.getRelationsAsSuper().put(
					frameRelation.getFrameNetRelation(),
					new HashMap<Frame, FrameElementRelation>());
		if (!subFrameElement.getRelationsAsSub().containsKey(
				frameRelation.getFrameNetRelation()))
			subFrameElement.getRelationsAsSub().put(
					frameRelation.getFrameNetRelation(),
					new HashMap<Frame, FrameElementRelation>());

		superFrameElement.getRelationsAsSuper().get(
				frameRelation.getFrameNetRelation()).put(
				frameRelation.getSubFrame(), this);
		subFrameElement.getRelationsAsSub().get(
				frameRelation.getFrameNetRelation()).put(
				frameRelation.getSuperFrame(), this);

	}

	/**
	 * The sub frame element
	 * 
	 * @return the subFrameElement
	 */
	public FrameElement getSubFrameElement() {
		return subFrameElement;
	}

	/**
	 * The super frame element
	 * 
	 * @return the superFrameElement
	 */
	public FrameElement getSuperFrameElement() {
		return superFrameElement;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return frameRelation.getName();
	}
}
