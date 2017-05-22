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
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class represents a single frame element. It is not instantiated
 * directly, but together with the frame it belongs to. Use
 * {@link Frame#getFrameElements()} or {@link Frame#getFrameElement(String)} to
 * get an instance of this class.
 * 
 * @author Nils Reiter
 * 
 */
public abstract class FrameElement implements Serializable, IFrameNetObject,
		IHasTrees {
	/**
	 * The name of the frame element
	 */
	String name;

	/**
	 * The abbreviation as defined in the FrameNet database.
	 */
	String abbreviation;

	/**
	 * The creation date.
	 */
	Date creationDate;

	/**
	 * The core type of the frame element. Currently, its either "Core",
	 * "Peripheral", "Extra-Thematic" or "Core-Unexpressed".
	 */
	CoreType coreType;

	/**
	 * The definition as given in the database.
	 */
	byte[] definition;

	/**
	 * The identifier.
	 */
	String id;

	/**
	 * A boolean value, whether the frame element is inherited from others or
	 * not
	 */
	Boolean root = null;

	/**
	 * The frame to which the FE belongs.
	 */
	Frame frame;

	/**
	 * The set of semantic types for the filler for this frame element.
	 */
	SemanticType[] semanticTypes = null;

	/**
	 * A map, associating inherited frames with the corresponding frame element.
	 */
	Map<Frame, FrameElement> superFrameElements = null;

	/**
	 * A map associating inheriting frames with the corresponding frame element.
	 * There should be an inheriting frame element in every inheriting frame of
	 * the frame to which this FE belongs.
	 */
	Map<Frame, FrameElement> subFrameElements = null;

	/**
	 * Collects all frame elements, that are in a certain distance with this.
	 */
	SortedMap<Integer, Set<FrameElement>> superFrameElementsDistance = null;

	Map<FrameNetRelation, Map<Frame, FrameElementRelation>> relationsAsSuper = null;
	Map<FrameNetRelation, Map<Frame, FrameElementRelation>> relationsAsSub = null;

	private static final long serialVersionUID = 10L;

	protected FrameElement() {

	};

	/**
	 * Returns a string representation of the frame element in the form
	 * FRAMENAME.FRAMEELEMENTNAME
	 * 
	 * @return A string representation of the frame element.
	 */
	@Override
	public String toString() {
		return frame.getName() + "." + name;
	};

	/**
	 * Realizes the frame element. The method returns a RealizedFrameElement,
	 * which is basically an annotated version of a frame element. Note that a
	 * frame element can only be realized with respect to a given realized
	 * frame.
	 * 
	 * @param realizedFrame
	 *            The realized frame.
	 * @param target
	 *            The target of the frame element annotation.
	 * @param xmlid
	 *            An identifier, that can be used to identify non-terminal nodes
	 *            in parse trees.
	 * @return The realized frame element.
	 */
	public RealizedFrameElement realize(RealizedFrame realizedFrame,
			IToken target, String xmlid) {
		RealizedFrameElement realizedFrameElement = new RealizedFrameElement(
				realizedFrame, this, target, xmlid);
		realizedFrame.addRealizedFrameElement(realizedFrameElement);
		return realizedFrameElement;
	}

	/**
	 * As {@link FrameElement#realize(RealizedFrame, IToken)}, but without the
	 * need to provide an identifer.
	 * 
	 * @param realizedFrame
	 *            The realized frame in which this frame element is used.
	 * @param target
	 *            The target of the frame element.
	 * @return The realized frame element.
	 */
	public RealizedFrameElement realize(RealizedFrame realizedFrame,
			IToken target) {
		return realize(realizedFrame, target, "");
	}

	/**
	 * This method returns a collection of related frame elements. You simply
	 * specify the relation (e.g. "Inheritance") and the direction (0 or 1). See
	 * {@link FrameNetRelation} for details on relations.
	 * 
	 * 
	 * @param relation
	 *            The relation
	 * @param direction
	 *            The direction of the relation
	 * @return A collection of frame elements
	 */
	public Collection<FrameElement> relatedFrameElements(String relation,
			FrameNetRelationDirection direction) {
		FrameNetRelation fnrel = frame.framenet.getFrameNetRelation(relation);
		Collection<FrameElement> ret = new HashSet<FrameElement>();
		if (direction == FrameNetRelationDirection.DOWN) {
			if (!getRelationsAsSuper().containsKey(fnrel))
				return ret;
			for (FrameElementRelation ferel : getRelationsAsSuper().get(fnrel)
					.values()) {
				ret.add(ferel.getSubFrameElement());
			}
		} else {
			if (!getRelationsAsSub().containsKey(fnrel))
				return ret;
			for (FrameElementRelation ferel : getRelationsAsSub().get(fnrel)
					.values()) {
				ret.add(ferel.getSuperFrameElement());
			}
		}
		return ret;
	}

	/**
	 * This method returns a related frame element in a given frame with a given
	 * relation in a given direction. See {@link FrameNetRelation} on details
	 * for relations. The method returns null, if the given frame is not related
	 * to the frame of this frame element or the given frame contains no frame
	 * element that is related to this.
	 * 
	 * @param relation
	 *            The relation (e.g. "Inheritance")
	 * @param direction
	 *            The direction
	 * @param frame
	 *            The frame
	 * @return The frame element or null
	 */
	public FrameElement relatedFrameElement(String relation,
			FrameNetRelationDirection direction, Frame frame) {
		FrameNetRelation fnrel = this.frame.framenet
				.getFrameNetRelation(relation);
		FrameElementRelation ferel;
		if (direction == FrameNetRelationDirection.DOWN) {
			if (!getRelationsAsSuper().containsKey(fnrel))
				return null;
			if (!getRelationsAsSuper().get(fnrel).containsKey(frame))
				return null;
			ferel = getRelationsAsSuper().get(fnrel).get(frame);
			return ferel.getSubFrameElement();
		} else {
			if (!getRelationsAsSub().containsKey(fnrel))
				return null;
			if (!getRelationsAsSub().get(fnrel).containsKey(frame))
				return null;
			ferel = getRelationsAsSub().get(fnrel).get(frame);
			return ferel.getSuperFrameElement();
		}
	}

	/**
	 * The method returns a collection of frame elements from which this frame
	 * element inherits that do not inherit from any other frame element: The
	 * most general frame elements from which this inherits.
	 * 
	 * @return A collection of frame elements.
	 */
	public Collection<FrameElement> rootFrameElements() {
		Collection<FrameElement> ret = new HashSet<FrameElement>();
		for (FrameElement fe : this.inheritsFrom()) {
			if (fe.isRootFrameElement()) {
				ret.add(fe);
			} else {
				ret.addAll(fe.rootFrameElements());
			}
		}
		return ret;
	}

	/**
	 * The frame element from which this frame element inherits within the given
	 * frame. May return null, if the given frame is not a frame from which the
	 * frame of this frame element does inherit.
	 * 
	 * @param frame
	 *            The frame
	 * @return The frame element
	 */
	public FrameElement inheritsFrom(Frame frame) {
		return relatedFrameElement("Inheritance", FrameNetRelationDirection.UP,
				frame);
	}

	/**
	 * The frame element which inherits this frame element within the given
	 * frame. May return null, if the given frame does not inherit from this
	 * frame.
	 * 
	 * @param frame
	 *            The inheriting frame
	 * @return The frame element
	 */
	public FrameElement isInheritedBy(Frame frame) {
		return relatedFrameElement("Inheritance",
				FrameNetRelationDirection.DOWN, frame);
	}

	/**
	 * The method returns a collection of frame elements from which this frame
	 * element directly inherits.
	 * 
	 * @return A collection of frame elements
	 */
	public Collection<FrameElement> inheritsFrom() {
		return relatedFrameElements("Inheritance", FrameNetRelationDirection.UP);
	}

	/**
	 * The method return s a collection of frame elements which directly inherit
	 * this frame element.
	 * 
	 * @return A collection of frame elements
	 */
	public Collection<FrameElement> isInheritedBy() {
		return relatedFrameElements("Inheritance",
				FrameNetRelationDirection.DOWN);
	}

	/**
	 * Returns a list of all frame elements from which this inherits. The list
	 * is retrieved from every frame from which the frame of this frame element
	 * inherits.
	 * 
	 * @return A collection of frame elements.
	 * @deprecated Use {@link #allInheritedFrameElements()} instead
	 * 
	 */
	@Deprecated
	public Collection<FrameElement> allSuperFrameElements() {
		return allInheritedFrameElements();
	}

	/**
	 * Returns a list of all frame elements from which this inherits. The list
	 * is retrieved from every frame from which the frame of this frame element
	 * inherits.
	 * 
	 * @return A collection of frame elements.
	 * 
	 */
	public Collection<FrameElement> allInheritedFrameElements() {
		Collection<FrameElement> ret = new HashSet<FrameElement>();
		ret.add(this);
		for (Frame f : getFrame().inheritsFrom()) {
			FrameElement sfe = this.inheritsFrom(f);
			if (sfe != null)
				ret.addAll(sfe.allInheritedFrameElements());
		}
		return ret;
	}

	/**
	 * Returns a list of all frame elements which inherit this frame element.
	 * The list is retrieved from every frame which inherits from the frame of
	 * this frame element.
	 * 
	 * @return A collection of frame elements.
	 */
	public Collection<FrameElement> allInheritingFrameElements() {
		Collection<FrameElement> ret = new HashSet<FrameElement>();
		ret.add(this);
		for (Frame f : getFrame().isInheritedBy()) {
			FrameElement sfe = this.isInheritedBy(f);
			if (sfe != null)
				ret.addAll(sfe.allInheritingFrameElements());
		}
		return ret;
	}

	/**
	 * Returns a collection of all frame elements that are related to this frame
	 * element using the given relation in the given direction
	 * 
	 * @param relation
	 *            The relation
	 * @param direction
	 *            The direction of the relation
	 * @return A collection of frame elements
	 */
	public Collection<FrameElement> allRelatedFrameElements(
			FrameNetRelation relation, FrameNetRelationDirection direction) {
		Collection<FrameElement> ret = new HashSet<FrameElement>();
		ret.add(this);
		for (Frame f : getFrame().relatedFrames(relation.getName(), direction)) {
			FrameElement sfe = relatedFrameElement(relation.getName(),
					direction, f);
			if (sfe != null)
				ret.addAll(sfe.allRelatedFrameElements(relation, direction));
		}
		return ret;
	}

	/**
	 * @deprecated Use {@link FrameElement#allInheritedFrameElements()} and
	 *             {@link Frame#distance(Frame)} instead.
	 * 
	 * @throws FrameNetException
	 * @throws IOException
	 */
	@Deprecated
	public SortedMap<Integer, Set<FrameElement>> allSuperFrameElementsDistance()
			throws FrameNetException, IOException {
		if (superFrameElementsDistance != null)
			return superFrameElementsDistance;
		superFrameElementsDistance = new TreeMap<Integer, Set<FrameElement>>();
		for (FrameElement fe : allInheritedFrameElements()) {
			Integer d = frame.distance(fe.frame);
			if (!superFrameElementsDistance.containsKey(d))
				superFrameElementsDistance.put(d, new HashSet<FrameElement>());
			superFrameElementsDistance.get(d).add(fe);

		}
		return superFrameElementsDistance;
	}

	/**
	 * The method returns true if the frame to which the frame element belongs
	 * is isolated or the frame element neither inherits from another frame
	 * element nor is inherited by another frame element.
	 * 
	 * @return true or false
	 */

	public boolean isIsolated() {
		if (getFrame().isIsolated())
			return true;
		if (relatedFrameElements("Inheritance", FrameNetRelationDirection.UP)
				.isEmpty()
				&& relatedFrameElements("Inheritance",
						FrameNetRelationDirection.DOWN).isEmpty())
			return true;
		return false;
	}

	/**
	 * Determines if this frame element inherits from the given one.
	 * 
	 * @param frameElement
	 *            The frame element in question.
	 * @return true or false.
	 */
	public boolean inheritsFrom(FrameElement frameElement) {
		if (this.equals(frameElement))
			return true;
		for (Frame f : frame.inheritsFrom()) {
			if (inheritsFrom(f).inheritsFrom(frameElement))
				return true;
		}
		return false;
	}

	/**
	 * Checks, whether there exists a frame element from which this frame
	 * element inherits.
	 * 
	 * @return true or false
	 */
	public boolean isRootFrameElement() {
		if (root != null)
			return root;
		if (frame.isRootFrame()) {
			root = true;
			return root;
		}
		if (this.inheritsFrom().isEmpty()) {
			root = true;
			return true;
		}
		root = false;
		return false;
	}

	/**
	 * Returns true, if this frame element has the same name and frame as the
	 * given one.
	 * 
	 * @param frameElement
	 *            The other frame element.
	 * @return true or false.
	 */
	public boolean equals(FrameElement frameElement) {
		if (getName().equalsIgnoreCase(frameElement.getName())
				&& getFrame().getName().equalsIgnoreCase(
						frameElement.getFrame().getName()))
			return true;
		return false;
	}

	private String tree(String prefix, boolean up) {
		StringBuffer ret = new StringBuffer();
		ret.append(prefix + frame.getName() + "." + name + "\n");
		Collection<Frame> others;
		if (up)
			others = frame.inheritsFrom();
		else
			others = frame.isInheritedBy();
		for (Frame f : others) {
			FrameElement other = null;
			if (up)
				other = this.inheritsFrom(f);
			else
				other = this.isInheritedBy(f);
			if (other != null)
				ret.append(other.tree(prefix + " ", up));
		}
		return ret.toString();
	}

	/**
	 * This method parses a string describing a frame element. The string has to
	 * be given in the form FRAMENAME.FRAMEELEMENTNAME.
	 * 
	 * @param element
	 *            The string describing the frame element
	 * @param framenet
	 *            The FrameNet object
	 * @return A FrameElement object
	 * @throws FileNotFoundException
	 *             If the FrameNet files can not be found
	 * @throws FrameNotFoundException
	 *             The given frame does not exist in the FrameNet database
	 * @throws FrameElementNotFoundException
	 *             The given frame element can not be found or is not defined in
	 *             this frame
	 */
	public static FrameElement getFromString(String element, FrameNet framenet)
			throws FileNotFoundException, FrameNotFoundException,
			FrameElementNotFoundException {
		if (element.contains(".")) {
			String[] parts = element.split("\\.");
			Frame frame = framenet.getFrame(parts[0]);
			return frame.getFrameElement(parts[1]);
		}
		return null;
	}

	/**
	 * As {@link Frame#treeUp()}, but for a frame element.
	 * 
	 * @return A string containing the tree.
	 * @throws IOException
	 *             In case the data files could not be read.
	 */
	public String treeUp() {
		return tree("", true);
	}

	/**
	 * As {@link Frame#treeDown()}, but for a frame element.
	 * 
	 * @return A string containing the tree.
	 * @throws IOException
	 *             In case the data files could not be read.
	 */
	public String treeDown() {
		return tree("", false);
	}

	public String treeDownInfo() {
		return "Inheritance tree downwards";
	}

	public String treeUpInfo() {
		return "Inheritance tree upwards";
	}

	/**
	 * Returns the frame to which this frame element belongs
	 * 
	 * @return the frame
	 */
	public Frame getFrame() {
		return frame;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the abbreviation of this frame element
	 * 
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * @return the cdate
	 */
	public String getCDate() {
		return FrameNet.dateFormat.format(creationDate);
	}

	/**
	 * Returns the core type of this frame element, as provided in the XML file.
	 * Can be "Core", "Peripheral", "Extra-Thematic" or "Core-Unexpressed".
	 * 
	 * @return the coreType
	 */
	public String getCoreTypeString() {
		return coreType.name();
	}

	/**
	 * Returns the core type of this frame element.
	 * 
	 * @return The CoreType
	 */
	public CoreType getCoreType() {
		return coreType;
	}

	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return new String(definition);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the semantic types this frame element belongs to
	 * 
	 * @return the semanticTypes
	 */
	public SemanticType[] getSemanticTypes() {
		return semanticTypes;
	}

	/**
	 * @return the subFrameElements
	 * @deprecated
	 */
	@Deprecated
	protected Map<Frame, FrameElement> getSubFrameElements() {
		return subFrameElements;
	}

	/**
	 * @return the superFrameElements
	 * @deprecated
	 */
	@Deprecated
	protected Map<Frame, FrameElement> getSuperFrameElements() {
		return superFrameElements;
	}

	/**
	 * @return the relationsAsSub
	 */
	protected Map<FrameNetRelation, Map<Frame, FrameElementRelation>> getRelationsAsSub() {
		if (relationsAsSub == null)
			relationsAsSub = new HashMap<FrameNetRelation, Map<Frame, FrameElementRelation>>(
					5);
		return relationsAsSub;
	}

	/**
	 * @return the relationsAsSuper
	 */
	protected Map<FrameNetRelation, Map<Frame, FrameElementRelation>> getRelationsAsSuper() {
		if (relationsAsSuper == null)
			relationsAsSuper = new HashMap<FrameNetRelation, Map<Frame, FrameElementRelation>>(
					5);
		return relationsAsSuper;
	}

	/**
	 * Returns true, if this frame element is a core frame element
	 * 
	 * @return true or false
	 */
	public boolean isCore() {
		return getCoreType() == CoreType.Core;
	}

	public Date getCreationDate() {
		return creationDate;
	}
}
