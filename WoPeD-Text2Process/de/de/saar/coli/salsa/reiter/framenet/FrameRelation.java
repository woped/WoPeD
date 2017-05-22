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

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;
import org.jaxen.JaxenException;
import org.jaxen.dom4j.Dom4jXPath;

/**
 * This class represents a single relation between two individual frames.
 * 
 * 
 * 
 * @author Nils Reiter
 * 
 */
public class FrameRelation implements IHasNameAndID, Serializable {

	/**
	 * 
	 */
	FrameNetRelation frameNetRelation;

	/**
	 * The XML id
	 */
	String id;

	/**
	 * The frame marked as super in the XML document
	 */
	Frame superFrame = null;

	/**
	 * The frame marked as sub in the XML document
	 */
	Frame subFrame = null;

	/**
	 * 
	 */
	Collection<FrameElementRelation> frameElementRelations = null;

	private FrameNet frameNet;

	private final static long serialVersionUID = 1L;

	/**
	 * The constructor. Should not be invoked manually, but rather through a
	 * {@link FrameNetRelation} object.
	 * 
	 * 
	 * @param relation
	 *            The relation
	 * @param xmlnode
	 *            The XML node
	 * @throws FileNotFoundException
	 *             If the FN data file can not be found
	 * @throws FrameNotFoundException
	 *             If the frame is not defined in the FN database
	 * @throws FrameElementNotFoundException
	 *             The frame element is not found or does not belong to the
	 *             frame
	 */
	public FrameRelation(FrameNet frameNet,FrameNetRelation relation, Element xmlnode)
			throws FileNotFoundException, FrameNotFoundException,
			FrameElementNotFoundException, JaxenException {
		this.frameNet = frameNet;
		frameNetRelation = relation;
		frameElementRelations = new LinkedList<FrameElementRelation>();
		id = xmlnode.attributeValue("ID");
		superFrame = frameNetRelation.frameNet.getFrame(xmlnode
				.attributeValue("superFrameName"));
		subFrame = frameNetRelation.frameNet.getFrame(xmlnode
				.attributeValue("subFrameName"));
		// frameNetRelation.frameNet.getLogger().fine("Node loaded. " +
		// subFrame.getName() + " - " + superFrame.getName());
		init(xmlnode);
	}

	/*
	 * public FrameRelation() { frameNetRelation = null; };
	 */
	public FrameRelation(FrameRelation relation) {
		frameNetRelation = relation.getFrameNetRelation();
		frameElementRelations = new LinkedList<FrameElementRelation>();
		id = relation.getId();
		superFrame = relation.getSuperFrame();
		subFrame = relation.getSubFrame();
	}

	@SuppressWarnings("unchecked")
	private void init(Element xmlnode) throws FrameElementNotFoundException,
			JaxenException {
		Dom4jXPath xpath = new Dom4jXPath("berk:FERelation");
		xpath.setNamespaceContext(this.frameNet.getNameSpaceContext());
		List list = xpath.selectNodes(xmlnode);
		for (Object item : list) {
			FrameElementRelation ferel = new FrameElementRelation(this,
					(Element) item);
			frameElementRelations.add(ferel);
		}
	}

	/**
	 * Returns the XML id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * The frame marked as sub frame in the XML files.
	 * 
	 * @return the subFrame
	 */
	public Frame getSubFrame() {
		return subFrame;
	}

	/**
	 * The frame marked as superframe in the XML files.
	 * 
	 * @return the superFrame
	 */
	public Frame getSuperFrame() {
		return superFrame;
	}

	/**
	 * @return the frameNetRelation
	 */
	protected FrameNetRelation getFrameNetRelation() {
		return frameNetRelation;
	}

	/**
	 * Returns the name of the framenet relation this frame relation belongs to.
	 */
	@Override
	public String toString() {
		return getFrameNetRelation().getName();
	}

	public String getName() {
		return toString();
	}

	protected void setFrameNetRelation(FrameNetRelation frameNetRelation) {
		this.frameNetRelation = frameNetRelation;
	}

	protected Collection<FrameElementRelation> getFrameElementRelations() {
		return frameElementRelations;
	}
}
