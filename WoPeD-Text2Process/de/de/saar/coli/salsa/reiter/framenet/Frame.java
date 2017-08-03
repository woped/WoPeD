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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.FlyweightAttribute;

/**
 * Represents a single frame.
 * 
 * Frame objects are not created directly, but instead in the FrameNet object
 * with the method {@link FrameNet#getFrame(String) getFrame(String)}. The frame
 * object can provide any information that is present in the XML file (in
 * version 1.3). <br/>
 * The XML data name, ID, cDate and definition are directly available via the
 * corresponding get-methods. {@link Frame#getFrameElements()} and
 * {@link Frame#getLexicalUnits()} provide access to frame elements and lexical
 * units. <br/>
 * There are several methods providing access to the frame hierarchy.
 * 
 * @author Nils Reiter
 * 
 */
public abstract class Frame implements Serializable, IFrameNetObject, IHasTrees {

	/**
	 * The name of the frame.
	 */
	String name = null;

	/**
	 * The ID of the frame, as noted in the framenet database files.
	 */
	String id = null;

	/**
	 * The cdate of the frame, as noted in the framenet database files. Creation
	 * date?
	 */
	String cDate = null;

	/**
	 * The textual definition of the frame, as noted in the database file.
	 */
	byte[] definition = null;

	/**
	 * The source attribute has been added by Salsa.
	 * 
	 * @since 0.2
	 */
	String source = "";

	/**
	 * The framenet object.
	 */
	FrameNet framenet = null;

	/**
	 * The storage for the frame elements.
	 */
	Map<String, FrameElement> frameElements = null;

	/**
	 * The cache for distances to other frames;
	 */
	Map<Frame, Integer> distances = null;

	/**
	 * The storage for the lexical units.
	 */
	Collection<LexicalUnit> lexicalUnits = null;

	private static final long serialVersionUID = 17L;

	Map<PartOfSpeech, Integer> lexicalUnitsPerPOS = null;

	protected Frame() {
		frameElements = new HashMap<String, FrameElement>(5);
	}

	protected boolean linkFrameNet(FrameNet fn) {
		if (!(getName() != null && getCDate() != null && getId() != null && getDefinition() != null))
			return false;
		framenet = fn;
		framenet.allFrames.put(getName(), this);
		fn.log(Level.INFO, "Frame " + getName() + " has been registered.");
		return true;
	}

	/**
	 * Returns the given frame element.
	 * 
	 * @param fename
	 * @return The frame element with the given name.
	 * @throws FrameElementNotFoundException
	 *             If the frame does not contain the given frame element.
	 */
	public FrameElement getFrameElement(String fename)
			throws FrameElementNotFoundException {
		if (!frameElements.containsKey(fename))
			throw new FrameElementNotFoundException(this, fename);
		return frameElements.get(fename);
	}

	/**
	 * 
	 * @deprecated Use inheritsFrom() instead.
	 * @return A collection of frames.
	 */
	@Deprecated
	public Collection<Frame> superFrames() {
		return inheritsFrom();
	}

	/**
	 * @deprecated Use isInheritedBy() instead.
	 * @return A collection of the frames that inherit this frame.
	 */
	@Deprecated
	public Collection<Frame> subFrames() {
		return isInheritedBy();
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */
	public Collection<Frame> isInheritedBy() {
		return relatedFrames("Inheritance", FrameNetRelationDirection.DOWN);

	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> inheritsFrom() {
		return relatedFrames("Inheritance", FrameNetRelationDirection.UP);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> subframeOf() {
		return relatedFrames("Subframe", FrameNetRelationDirection.UP);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> hasSubframe() {
		return relatedFrames("Subframe", FrameNetRelationDirection.DOWN);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> usedBy() {
		return relatedFrames("Using", FrameNetRelationDirection.DOWN);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> uses() {
		return relatedFrames("Using", FrameNetRelationDirection.UP);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> later() {
		return relatedFrames("Precedes", FrameNetRelationDirection.DOWN);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> earlier() {
		return relatedFrames("Precedes", FrameNetRelationDirection.UP);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> neutral() {
		return relatedFrames("Perspective_on", FrameNetRelationDirection.UP);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> perspectivized() {
		return relatedFrames("Perspective_on", FrameNetRelationDirection.DOWN);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> referring() {
		return relatedFrames("See_also", FrameNetRelationDirection.DOWN);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> referred() {
		return relatedFrames("See_also", FrameNetRelationDirection.UP);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> inchoative() {
		return relatedFrames("Inchoative_of", FrameNetRelationDirection.UP);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> inchoativeStative() {
		return relatedFrames("Inchoative_of", FrameNetRelationDirection.DOWN);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> causative() {
		return relatedFrames("Causative_of", FrameNetRelationDirection.UP);
	}

	/**
	 * Retrieves related frames. See
	 * {@link Frame#relatedFrames(String, FrameNetRelationDirection)} for
	 * details.
	 * 
	 * @return A collection of frames.
	 */

	public Collection<Frame> causativeStative() {
		return relatedFrames("Causative_of", FrameNetRelationDirection.DOWN);
	}

	/**
	 * Collects the frame that are in the given relation in the given direction
	 * with this frame. There are specific methods for every relation and every
	 * direction, which are more convenient to use.
	 * 
	 * In FrameNet, eight relations between frames are defined. All are
	 * accessible in this package, but the naming is somewhat difficult. The
	 * following table provides an overview on the different frame relations and
	 * how the elements of the relations are called. Every relation is directed,
	 * which means that there are different names for the two ends of relations.
	 * <table border="1">
	 * <tr>
	 * <th>Relation</th>
	 * <th>Name of super element</th>
	 * <th>Method to access super</th>
	 * <th>Name of sub element</th>
	 * <th>Method to access sub</th>
	 * </tr>
	 * <tr>
	 * <td>Inheritance</td>
	 * <td>Parent</td>
	 * <td> {@link Frame#inheritsFrom() inheritsFrom()}</td>
	 * <td>Child</td>
	 * <td> {@link Frame#isInheritedBy() isInheritedBy()}</td>
	 * </tr>
	 * <tr>
	 * <td>Precedes</td>
	 * <td>Earlier</td>
	 * <td> {@link Frame#earlier() earlier()}</td>
	 * <td>Later</td>
	 * <td> {@link Frame#later() later()}</td>
	 * </tr>
	 * <tr>
	 * <td>Subframe</td>
	 * <td>Complex</td>
	 * <td> {@link Frame#subframeOf() subframeOf()}</td>
	 * <td>Component</td>
	 * <td> {@link Frame#hasSubframe() hasSubframe()}</td>
	 * </tr>
	 * <tr>
	 * <td>Using</td>
	 * <td>Parent</td>
	 * <td> {@link Frame#uses() uses()}</td>
	 * <td>Child</td>
	 * <td> {@link Frame#usedBy() usedBy()}</td>
	 * </tr>
	 * <tr>
	 * <td>Perspective_on</td>
	 * <td>Neutral</td>
	 * <td> {@link Frame#neutral() neutral()}</td>
	 * <td>Perspectivized</td>
	 * <td> {@link Frame#perspectivized() perspectivized()}</td>
	 * </tr>
	 * <tr>
	 * <td>See_also</td>
	 * <td>Main Entry</td>
	 * <td> {@link Frame#referred() referred()}</td>
	 * <td>Referring Entry</td>
	 * <td> {@link Frame#referring() referring()}</td>
	 * </tr>
	 * <tr>
	 * <td>Inchoative_of</td>
	 * <td>Inchoative</td>
	 * <td> {@link Frame#inchoative() inchoative() }</td>
	 * <td>State</td>
	 * <td> {@link Frame#inchoativeStative() inchoativeStative() }</td>
	 * </tr>
	 * <tr>
	 * <td>Causative_of</td>
	 * <td>Causative</td>
	 * <td> {@link Frame#causative() causative() }</td>
	 * <td>State</td>
	 * <td> {@link Frame#causativeStative() causativeStative() }</td>
	 * </tr>
	 * </table>
	 * 
	 * 
	 * @param relation
	 *            The relation. Has to be given exactly as it is in the framenet
	 *            database.
	 * @param frameNetRelationDirection
	 *            The direction. This is mostly trial-and-error.
	 * @return The frames that are directly related with this frame.
	 * @see FrameNetRelation#getSub(Frame)
	 * @see FrameNetRelation#getSuper(Frame)
	 * 
	 */
	public Collection<Frame> relatedFrames(String relation,
			FrameNetRelationDirection frameNetRelationDirection) {

		return framenet.getFrameNetRelation(relation).getOther(this,
				frameNetRelationDirection);
		/*
		 * this.frameRelations = new HashMap<FrameNetRelation,
		 * Map<FrameNetRelationDirection, Collection<Frame>>>();
		 * FrameNetRelation fnrel = framenet.getFrameNetRelation(relation); if
		 * (! frameRelations.containsKey(fnrel)) frameRelations.put(fnrel, new
		 * HashMap<FrameNetRelationDirection, Collection<Frame>>()); if (!
		 * frameRelations.get(fnrel).containsKey(frameNetRelationDirection))
		 * frameRelations.get(fnrel).put(frameNetRelationDirection,
		 * fnrel.getOther(this, frameNetRelationDirection)); return
		 * frameRelations.get(fnrel).get(frameNetRelationDirection);
		 */

		/*
		 * Collection<FrameRelation> rels; Collection<Frame> ret = new
		 * HashSet<Frame>(); if (frameNetRelationDirection ==
		 * FrameNetRelationDirection.DOWN) { if (!
		 * this.getRelationsAsSuper().containsKey(fnrel)) return ret; rels =
		 * this.getRelationsAsSuper().get(fnrel); } else { if (!
		 * this.getRelationsAsSub().containsKey(fnrel)) return ret; rels =
		 * this.getRelationsAsSub().get(fnrel); } for (FrameRelation frel :
		 * rels) { if (frameNetRelationDirection ==
		 * FrameNetRelationDirection.UP) { ret.add(frel.getSubFrame()); } else {
		 * ret.add(frel.getSuperFrame()); } } return ret;
		 */
		/*
		 * if (frameRelations.containsKey(relation)) { if
		 * (frameRelations.get(relation).containsKey(direction)) { return
		 * frameRelations.get(relation).get(direction); } } if (framenet.debug)
		 * System.err.print("Frame.relatedFrames("+relation+", "+direction+"): "
		 * + this.getName() + " - "); Collection<Frame> ret =
		 * framenet.getRelatedFrames(name, relation, direction); if
		 * (framenet.debug) System.err.println(" Newly retrieved."); if (!
		 * frameRelations.containsKey(relation)) { frameRelations.put(relation,
		 * new HashMap<Integer, Collection<Frame>>()); } if (!
		 * frameRelations.get(relation).containsKey(direction)) {
		 * frameRelations.get(relation).put(direction, ret); } return ret;
		 */
	}

	/**
	 * Checks, whether this frame is at the top of the inheritance tree.
	 * 
	 * @return True, if no super frames can be found, false otherwise.
	 */
	public boolean isRootFrame() {
		return inheritsFrom().isEmpty();
	}

	/**
	 * Checks, whether this frame is at the bottom of the inheritance tree.
	 * 
	 * @return True, if the no frame inherits this one.
	 */
	public boolean isLeafFrame() {
		return isInheritedBy().isEmpty();
	}

	/**
	 * Returns a RealizedFrame object, in which every frame element has been
	 * realized using the dummy token. The dummy string is also stored as target
	 * of the frame itself.
	 * 
	 * @param dummy
	 *            The dummy token.
	 * @return A realized frame.
	 */
	public RealizedFrame realizeAll(IToken dummy) {
		RealizedFrame rf = this.realize(dummy);
		for (FrameElement fe : frameElements()) {
			fe.realize(rf, dummy);
		}
		return rf;
	}

	/**
	 * Connects the frame with a target text and an Id and returns a realized
	 * frame.
	 * 
	 * @param target
	 *            The target of the frame.
	 * @param xmlid
	 *            An XML id of the target of the frame.
	 * @return A realized frame.
	 */
	public RealizedFrame realize(IToken target, String xmlid) {
		return new RealizedFrame(this, target, xmlid);
	}

	/**
	 * Connects the frame with a target text and returns a realized frame.
	 * 
	 * @param target
	 *            The target of the frame.
	 * @return A realized frame.
	 */
	public RealizedFrame realize(IToken target) {
		return realize(target, "");
	}

	/**
	 * Returns a string representation of the frame.
	 * 
	 * @return The name of the frame.
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Collects all frames that inherit from this frame, either direct or
	 * indirect.
	 * 
	 * @return All frames that inherit this frame. Includes transitivity.
	 * @since 0.1
	 */
	public Collection<Frame> allInheritingFrames() {
		Set<Frame> ret = new HashSet<Frame>();
		for (Frame f : isInheritedBy()) {
			ret.add(f);
			ret.addAll(f.allInheritingFrames());
		}
		return ret;
	}

	/**
	 * Like <code>allInheritingFrames()</code>, but with respect to the frames
	 * from which this has inherited.
	 * 
	 * @return A collection of frames.
	 * @since 0.1
	 */
	public Collection<Frame> allInheritedFrames() {
		Set<Frame> ret = new HashSet<Frame>();
		for (Frame f : inheritsFrom()) {
			ret.add(f);
			ret.addAll(f.allInheritedFrames());
		}
		return ret;
	}

	/**
	 * Compares two frames. If they have the same name, then they should be
	 * considered equal.
	 * 
	 * @param other
	 *            The other frame.
	 * @return True if the names of the frames are identical, false otherwise.
	 */
	public boolean equals(Frame other) {
		return (other.getName() == getName());
	}

	/**
	 * Calculates the distance between two frames.
	 * <p>
	 * With distance, we mean the number of steps one has to go upwards in the
	 * inheritance hierarchy to reach the other frame. Therefore, the distance
	 * is directed: <code>f1.distance(f2)</code> is only the same as
	 * <code>f2.distance(f1)</code> if <code>f1 == f2</code>.
	 * </p>
	 * <p>
	 * If the frames are identical, 0 (zero) is returned. If the other frame is
	 * not among the ancestors of this frame,
	 * {@link java.lang.Integer#MAX_VALUE Integer.MAX_VALUE} is returned.
	 * </p>
	 * 
	 * <p>
	 * The behaviour of this method can be expected to change in future
	 * versions. It is planned to implement real tree distance.
	 * </p>
	 * 
	 * @param other
	 *            The other frame
	 * @return The distance (=number of inheritance steps upwards)
	 */
	public Integer distance(Frame other) {
		if (this.equals(other))
			return new Integer(0);

		if (distances == null)
			distances = new HashMap<Frame, Integer>();
		else if (distances.containsKey(other))
			return distances.get(other);

		framenet.log(Level.INFO, "Searching for distance between " + getName()
				+ " and " + other.getName());

		int adist = 1;
		Set<Frame> work = new HashSet<Frame>();
		work.addAll(inheritsFrom());
		while (!work.isEmpty()) {
			Collection<Frame> nextWork = new HashSet<Frame>();
			for (Frame sf : work) {
				Integer ret = new Integer(adist);
				distances.put(other, ret);
				if (sf.equals(other)) {
					return ret;
				}
				nextWork.addAll(sf.inheritsFrom());
			}
			adist++;
			work.clear();
			work.addAll(nextWork);
			nextWork.clear();
		}
		Integer ret = Integer.MAX_VALUE;
		distances.put(other, ret);
		return ret;
	}

	/**
	 * Generates a tree of the inheritance hierarchy. Uses indentation to mark
	 * the descendent-levels
	 * 
	 * @return A string contain a tree-like representation of the inheritance
	 *         hierarchy.
	 * @param prefix
	 *            A String containing whitespaces. Is used to represent the
	 *            depth from here on.
	 * @param up
	 *            True if the true should follow the hierarchy upwards, false
	 *            otherwise
	 */
	private String tree(String prefix, boolean up) {
		StringBuffer ret = new StringBuffer();
		ret.append(prefix + name + "\n");
		Collection<Frame> others;
		if (up)
			others = inheritsFrom();
		else
			others = isInheritedBy();
		for (Frame f : others) {
			ret.append(f.tree(prefix + " ", up));
		}
		return ret.toString();
	};

	/**
	 * Generates an ASCII-art tree-like representaton of the inheritance
	 * hierarchy of the frame. Looks upwards.
	 * 
	 * @return A string containing the tree. Contains several newlines.
	 */
	public String treeUp() {
		return tree("", true);
	}

	/**
	 * Generates an ASCII-art tree-like representation of the inheritance
	 * hierarchy of the frame, but looks down. Stores the frames that inherit
	 * from this frame.
	 * 
	 * @return The string containing the tree.
	 */
	public String treeDown() {
		return tree("", false);
	}

	public String treeUpInfo() {
		return "Inheritance tree upwards";
	}

	public String treeDownInfo() {
		return "Inheritance tree downwards";
	}

	public String getCDate() {
		return cDate;
	}

	public String getDefinition() {
		return new String(definition);
	}

	/**
	 * Returns a collection of the frame element objects.
	 * 
	 * @return All frame elements in this frame.
	 */
	public Collection<FrameElement> frameElements() {
		return getFrameElements().values();
	}

	/**
	 * Returns the map of frame element names to frame element objects. Use
	 * {@link Frame#frameElements() frameElements()} to get a simple list of the
	 * frame element objects (or use <code>getFrameElements().value()</code>,
	 * which is the same ... )
	 * 
	 * @return The frameElements as a map.
	 */
	public Map<String, FrameElement> getFrameElements() {
		return frameElements;
	}

	/**
	 * Returns the lexical units of this frame as collection of LexicalUnit
	 * objects.
	 * 
	 * @return The lexicalUnits.
	 */
	public Collection<LexicalUnit> getLexicalUnits() {
		return lexicalUnits;
	}

	/**
	 * Returns the lexical unit with the given name or null if it does not exist
	 * in this frame.
	 * 
	 * @param luName
	 *            The name of the lexical unit as given in the XML files.
	 * @return The lexical unit
	 * @since 0.2
	 */
	public LexicalUnit getLexicalUnit(String luName) {
		for (LexicalUnit lu : getLexicalUnits()) {
			if (lu.getName().equalsIgnoreCase(luName))
				return lu;
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	/**
	 * Returns the number of lexical units for the parts of speech.
	 * 
	 * @return A map containing the part of speech and the number of lexical
	 *         units
	 * @deprecated Use {@link #getNumberOfLexicalUnitsPerPOS()} instead
	 */
	@Deprecated
	public Map<PartOfSpeech, Integer> getLexicalUnitsPerPOS() {
		return getNumberOfLexicalUnitsPerPOS();
	}

	/**
	 * Returns the number of lexical units for the parts of speech.
	 * 
	 * @return A map containing the part of speech and the number of lexical
	 *         units
	 */
	public Map<PartOfSpeech, Integer> getNumberOfLexicalUnitsPerPOS() {
		return lexicalUnitsPerPOS;
	}

	/**
	 * This method returns true if the frame is isolated. "Isolated" means, that
	 * it has no frames that either inherit from it or are inherited by it.
	 * 
	 * @return true or false.
	 */
	public boolean isIsolated() {
		return (inheritsFrom().isEmpty() && isInheritedBy().isEmpty());

	}

	protected FrameNet getFramenet() {
		return framenet;
	}

	/**
	 * This method returns the source-attribute, as used by the SalTo-tool.
	 * 
	 * @return The source of the frame
	 */
	public String getSource() {
		return source;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setId(String id) {
		this.id = id;
	}

	protected void setCDate(String date) {
		cDate = date;
	}

	protected void setDefinition(String definition) {
		this.definition = definition.getBytes();
	}

	protected void setSource(String source) {
		this.source = source;
	}

	/**
	 * This method can be used to export the frame definition of this frame into
	 * an XML-format that can be embedded in SalsaTigerXML-files.
	 * 
	 * @return An XML element containing the name and frame elements of this
	 *         frame.
	 * @since 0.4.1
	 */
	public Element exportToSalsaTiger() {
		Namespace ns = new Namespace("fd",
				"http://www.clt-st.de/framenet/frame-database");

		Element elem = new DefaultElement("frame", ns);

		elem.add(new FlyweightAttribute("name", getName(), ns));

		for (FrameElement fe : frameElements()) {
			Element fee = new DefaultElement("element", ns);
			fee.add(new FlyweightAttribute("name", fe.getName(), ns));
			fee.add(new FlyweightAttribute("optional", String.valueOf(!fe
					.isCore()), ns));

			elem.add(fee);
		}

		return elem;

	}
}
