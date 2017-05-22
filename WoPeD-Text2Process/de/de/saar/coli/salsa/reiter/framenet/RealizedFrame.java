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

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents a frame and a target (a "word" in a sentence). The
 * target may be some id, depending on the properties of the parsing procedure.
 * 
 * @author Nils Reiter
 * 
 */
public class RealizedFrame implements IHasTarget, IHasID {
	/**
	 * The frame
	 */
	Frame frame;
	/**
	 * The target
	 */
	IToken target;
	/**
	 * The sentence in which the target occurs.
	 */
	Sentence sentence = null;
	/**
	 * An XML id
	 */
	String id;
	/**
	 * A map of frame elements
	 */
	Map<String, RealizedFrameElement> frameElements;
	/**
	 * A map of generalizations
	 * 
	 * @deprecated
	 */
	@Deprecated
	Map<Frame, RealizedFrame> generalizations;

	/**
	 * character position of first character belonging to the target
	 * 
	 * @deprecated
	 */
	@Deprecated
	int start = -1;

	/**
	 * character position of last character belonging to the target
	 * 
	 * @deprecated
	 */
	@Deprecated
	int end = -1;

	/**
	 * The constructor
	 * 
	 * @param frame
	 *            The frame to be realized
	 * @param target
	 *            The target of the annotation
	 * @param id
	 *            An id
	 */
	public RealizedFrame(Frame frame, IToken target, String id) {
		this.frame = frame;
		this.target = target;
		this.id = id;
		start = 0;
		end = 0;
		// this.sentence = target;
		frameElements = new HashMap<String, RealizedFrameElement>();
		generalizations = new HashMap<Frame, RealizedFrame>();
	}

	/**
	 * Adds a realized frame element to the realized frame. The frame element
	 * has to belong to this frame
	 * 
	 * @param realizedFrameElement
	 *            The realized frame element
	 */
	public void addRealizedFrameElement(
			RealizedFrameElement realizedFrameElement) {
		// if (!
		// this.getFrame().frameElements().contains(realizedFrameElement.frameElement))
		// throw new FrameElementNotFoundException(this.getFrame(),
		// realizedFrameElement.frameElement);
		frameElements.put(realizedFrameElement.frameElement.name,
				realizedFrameElement);
	}

	/**
	 * Adds a new realized frame element. Sets xmlid to 0.
	 * 
	 * @param fename
	 *            The name of the frame element
	 * @param target
	 *            The target of the frame element
	 * @throws FrameElementNotFoundException
	 *             If the frame element does not belong to the frame
	 * @return The realized frame element
	 */
	public RealizedFrameElement addRealizedFrameElement(String fename,
			IToken target) throws FrameElementNotFoundException {
		RealizedFrameElement rfe = new RealizedFrameElement(this, frame
				.getFrameElement(fename), target, "");
		frameElements.put(fename, rfe);
		return rfe;
	}

	/**
	 * Adds a new realized frame element based on character positions
	 * 
	 * @param fename
	 *            The name of the frame element
	 * @param start
	 *            The first character of the frame element target
	 * @param end
	 *            The first character after the frame element target
	 * @throws FrameElementNotFoundException
	 *             This exception is thrown when the frame element does not
	 *             belong to the given frame
	 */
	/*
	 * public void addRealizedFrameElement(String fename, int start, int end)
	 * throws FrameElementNotFoundException{ frameElements.put(fename, new
	 * RealizedFrameElement(this, frame.getFrameElement(fename), start, end)); }
	 */

	/**
	 * Returns a string representation of this realized frame element
	 * 
	 */
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer(target + ": \"" + frame.getName()
				+ "\" (");
		for (String fename : frameElements.keySet()) {
			s.append(frameElements.get(fename));
			s.append(", ");
		}
		s.append(")");
		return s.toString();
	}

	/**
	 * Returns a sorted set of realized frame elements that have been set for
	 * this frame The realized frame elements are sorted according to the
	 * starting character position of the target. If none has been specified,
	 * the ordering is undefined.
	 * 
	 * @return A sorted set of RealizedFrameElements
	 */
	public SortedSet<RealizedFrameElement> frameElements() {
		SortedSet<RealizedFrameElement> ret = new TreeSet<RealizedFrameElement>(
				new Comparator<RealizedFrameElement>() {
					public int compare(RealizedFrameElement r1,
							RealizedFrameElement r2) {
						if (r1.isNullInstantiated())
							return -1;
						if (r2.isNullInstantiated())
							return 1;
						if (r1.getStart() == r2.getStart()) {
							if (r1.getEnd() == r2.getEnd()) {
								return -1;
							}
							return new Integer(r2.getEnd()).compareTo(r2
									.getEnd());
						}
						return new Integer(r1.getStart()).compareTo(r2
								.getStart());
					}
				});
		ret.addAll(frameElements.values());
		return ret;
	}

	/**
	 * This method returns a sorted set of realized frame elements that are
	 * overt, i.e., not null instantiated. The ordering work as in
	 * {@link RealizedFrame#frameElements()}.
	 * 
	 * @return A sorted set
	 * @since 0.4.1
	 */
	public SortedSet<RealizedFrameElement> overtFrameElements() {
		SortedSet<RealizedFrameElement> ret = frameElements();
		for (RealizedFrameElement rfe : ret) {
			if (rfe.isNullInstantiated())
				ret.remove(rfe);
		}
		return ret;
	}

	/**
	 * Generalizes all content of this frame. Returns all possible
	 * generalizations
	 * 
	 * @return A collection of realized frames, but more general than this one
	 */
	public Collection<RealizedFrame> generalizeAll() throws IOException {
		Collection<RealizedFrame> rfs = new HashSet<RealizedFrame>();
		for (Frame f : frame.inheritsFrom()) {
			if (generalizable(f)) {
				RealizedFrame gen = generalize(f);
				if (gen.generalizable()) {
					rfs.addAll(gen.generalizeAll());
				} else {
					rfs.add(gen);
				}
			}
		}
		return rfs;
	}

	/**
	 * Returns true if this is generalizable to the given frame
	 * 
	 * @param frame
	 *            The frame to which one wants to generalize
	 * @return true or false
	 */
	public boolean generalizable(Frame frame) {
		return (generalize(frame) != this);
	}

	/**
	 * This method returns true if there is a frame to which this frame can be
	 * generalized
	 * 
	 * @return true or false
	 */
	public boolean generalizable() {
		for (Frame frame : this.frame.inheritsFrom()) {
			if (generalizable(frame))
				return true;
		}
		return false;
	}

	/**
	 * Tries to generalize this frame to the level of given frame, i.e., tries
	 * to realize the given frame with the targets of this frame.
	 * 
	 * @param frame
	 *            The frame to which one wants to generalize
	 * @return The generalized realized frame or this, if no generalization is
	 *         possible
	 */
	public RealizedFrame generalize(Frame frame) {
		// TODO (fixed): Generalization of realized frames does not work
		// completly ...
		// some frame elements are broken
		// Fixed in rev. 48

		RealizedFrame gFrame = frame.realize(target, id);
		if (getSentence() != null) {
			gFrame.setSentence(getSentence());
		}
		if (getTarget() != null)
			gFrame.setTarget(getTarget());
		if (getFrame().inheritsFrom().isEmpty()
				|| !getFrame().inheritsFrom().contains(frame))
			return this;

		for (RealizedFrameElement rfe : frameElements()) {
			// System.err.println(rfe);
			FrameElement gFrameElement = rfe.frameElement.inheritsFrom(frame);
			if (gFrameElement == null)
				return this;
			RealizedFrameElement gfe = gFrameElement.realize(gFrame, rfe
					.getTarget(), rfe.getId());
			if (rfe.getTarget() != null)
				gfe.setTarget(getTarget());
			gfe.setIType(rfe.getIType());
		}
		return gFrame;

	}

	/**
	 * Returns the frame realized by this RealizedFrame
	 * 
	 * @return the frame
	 */
	public Frame getFrame() {
		return frame;
	}

	/**
	 * Returns the realized frame elements in this realized frame. Note, that
	 * this method only returns the frame elements that are actually used.
	 * 
	 * @return the frameElements
	 */
	public Map<String, RealizedFrameElement> getFrameElements() {
		return frameElements;
	}

	/**
	 * This method returns realized frame elements that are overt, i.e., not
	 * null instantiated.
	 * 
	 * @return The overt realized frame elements
	 * @since 0.4.1
	 */
	public Map<String, RealizedFrameElement> getOvertFrameElements() {
		Map<String, RealizedFrameElement> map = new HashMap<String, RealizedFrameElement>();

		for (RealizedFrameElement rfe : frameElements.values()) {
			if (!rfe.isNullInstantiated())
				map.put(rfe.getFrameElement().getName(), rfe);
		}

		return map;
	}

	/**
	 * @return the target
	 */
	public IToken getTarget() {
		return target;
	}

	/**
	 * @return the xmlid
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the sentence in which the target occurs.
	 * 
	 * @return the sentence
	 */
	public Sentence getSentence() {
		return sentence;
	}

	/**
	 * Sets the sentence in which this realized frame element occurrs.
	 * 
	 * @param sentence
	 *            the sentence to set
	 */
	public void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(IToken target) {
		this.target = target;
	}

	/**
	 * Returns the end position of the target of the realized frame
	 * 
	 * @return An integer giving the last position
	 */
	public int getEnd() {
		return target.getRange().getElement2();
	}

	/**
	 * @deprecated
	 * 
	 * @param end
	 */
	@Deprecated
	protected void setEnd(int end) {
		this.end = end;
	}

	/**
	 * Returns the start position of the target of the realized frame
	 * 
	 * @return An integer giving the starting position
	 */
	public int getStart() {
		return target.getRange().getElement1();
	}

	/**
	 * @deprecated
	 * @param start
	 */
	@Deprecated
	protected void setStart(int start) {
		this.start = start;
	}

	public boolean isNullInstantiated() {
		return false;
	}
}
