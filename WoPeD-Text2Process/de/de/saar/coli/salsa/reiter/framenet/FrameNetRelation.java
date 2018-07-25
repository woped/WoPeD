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
 * This class represents a relation between frames. The name as well as the
 * frames on both ends can be retrieved.
 * 
 * <p>
 * This class is not instantiated directly, but through the method
 * {@link FrameNet#getFrameNetRelation(String)} or
 * {@link FrameNet#getFrameNetRelations()}. An instance of this class does not
 * represent a single relation between a frame and another frame, but rather all
 * relations of the given kind.
 * </p>
 * 
 * <h3>Example</h3> <code>
 * // Gets the FrameNetRelation object<br>
 * FrameNetRelation inheritance = frameNet.getFrameRelation("Inheritance");<br>
 * 
 * // Gets all frames inherited by the Attack frame<br>
 * Collection< {@link Frame} > super = inheritance.getSuper(frameNet.getFrame("Attack")); <br>
 * 
 * // The same results can be achieved with<br>
 * Collection< Frame > super = frameNet.getFrame("Attack").inheritsFrom(); <br>
 * // or<br>
 * Collection< Frame > super = frameNet.getFrame("Attack").relatedFrames("Inheritance", FrameNetRelationDirection.UP);
 * </code>
 * 
 * 
 * @author Nils Reiter
 * 
 */
public abstract class FrameNetRelation implements Serializable, IHasName,
		IHasID {

	/**
	 * The name of the frame relation.
	 */
	String name;

	/**
	 * The identifier, as found in the XML file.
	 */
	String id;

	/**
	 * The name of the super frame.
	 */
	String superName;

	/**
	 * The name of the sub frame.
	 */
	String subName;

	/**
	 * The framenet object.
	 */
	FrameNet frameNet;

	Collection<FrameRelation> frameRelations = null;

	private final static long serialVersionUID = 2L;

	/**
	 * Returns the super frame of the given frame in this specific relation.
	 * 
	 * @param frame
	 *            The sub frame.
	 * @return A collection of super frames for frame.
	 */
	public Collection<Frame> getSuper(Frame frame) {
		Collection<Frame> ret = new HashSet<Frame>();
		for (FrameRelation frameRelation : frameRelations) {
			if (frameRelation.getSubFrame().equals(frame)) {
				ret.add(frameRelation.getSuperFrame());
			}
		}
		return ret;
	};

	/**
	 * Returns the sub frames of the given frame in this specific relation.
	 * 
	 * @param frame
	 *            The super frame.
	 * @return A collection of sub frames for frame.
	 */
	public Collection<Frame> getSub(Frame frame) {
		Collection<Frame> ret = new HashSet<Frame>();
		for (FrameRelation frameRelation : frameRelations) {
			if (frameRelation.getSuperFrame().equals(frame)) {
				ret.add(frameRelation.getSubFrame());
			}
		}
		return ret;
	};

	/**
	 * Returns the other frame(s) in this relation. This may either be the super
	 * or sub frame, depending on the direction given
	 * 
	 * 
	 * @param frame
	 *            The frame
	 * @param direction
	 *            The direction
	 * @return A list of frames
	 */
	public Collection<Frame> getOther(Frame frame,
			FrameNetRelationDirection direction) {
		if (direction == FrameNetRelationDirection.UP) {
			return getSuper(frame);
		}
		return getSub(frame);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the name of the sub frames, i.e., "Child" in the case of
	 * inheritance
	 * 
	 * @return the subName
	 */
	public String getSubName() {
		return subName;
	}

	/**
	 * Returns the name of the super frames, i.e., "Parent" in the case of
	 * inheritance
	 * 
	 * @return the superName
	 */
	public String getSuperName() {
		return superName;
	}

	/**
	 * Returns the frame relations in this FrameNet relation
	 * 
	 * @return a collection of frame relations
	 */
	public Collection<FrameRelation> getFrameRelations() {
		return frameRelations;
	}

	/**
	 * Detects, whether a given frame occurs in this relation
	 * 
	 * @param frame
	 *            The frame ti occur
	 * @return true, if the frame is connected, false otherwise
	 */
	public boolean isRelated(Frame frame) {
		for (FrameRelation frel : getFrameRelations()) {
			if (frel.getSubFrame().equals(frame)
					|| frel.getSuperFrame().equals(frame))
				return true;
		}
		return false;
	}

	/**
	 * Detects, whether a given frame element occurs in this relation
	 * 
	 * @param frameElement
	 *            The frame element to occur
	 * @return true, if the frame element is connected, false otherwise
	 */
	public boolean isRelated(FrameElement frameElement) {
		if (!this.isRelated(frameElement.getFrame()))
			return false;
		Frame frame = frameElement.getFrame();
		for (FrameRelation frel : getFrameRelations()) {
			if (frel.getSubFrame().equals(frame)
					|| frel.getSuperFrame().equals(frame))
				for (FrameElementRelation ferel : frel
						.getFrameElementRelations()) {
					if (ferel.getSubFrameElement().equals(frameElement)
							|| ferel.getSuperFrameElement()
									.equals(frameElement))
						return true;
				}
		}
		return false;
	}

}
