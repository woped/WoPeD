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

import java.util.Properties;

import de.uniheidelberg.cl.reiter.util.Range;

/**
 * This class represents an annotated frame element.
 * 
 * @author Nils Reiter
 * 
 */
public class RealizedFrameElement implements IHasTarget, IHasID {
	/**
	 * The frame element to be realized
	 */
	FrameElement frameElement;
	/**
	 * The realized frame to which this frame element belongs
	 */
	RealizedFrame realizedFrame;
	/**
	 * the target of the annotation
	 */
	IToken target;

	/**
	 * The XML identifier
	 */
	String id;

	/**
	 * The first character of the target of the FE
	 * 
	 * @deprecated
	 */
	@Deprecated
	int start;

	/**
	 * The first character after the target
	 * 
	 * @deprecated
	 */
	@Deprecated
	int end;

	/**
	 * Stores, whether the frame element is instantiated or not Default: false
	 */
	boolean nullInstantiated = false;

	/**
	 * Stores the instantiaton type, if the frame element is null instantiated
	 */
	String iType;

	/**
	 * Stores additional data, such as grammatical function (which seems to be
	 * frame-dependent)
	 */
	Properties data = null;

	/**
	 * The constructor of the realized frame element.
	 * 
	 * @param realizedFrame
	 *            The realized frame to which this belongs
	 * @param frameElement
	 *            The frame element to be realized
	 * @param target
	 *            The target of the annotation
	 * @param id
	 *            An XML id
	 */
	public RealizedFrameElement(RealizedFrame realizedFrame,
			FrameElement frameElement, IToken target, String id) {
		this.target = target;
		this.id = id;
		this.frameElement = frameElement;
		this.realizedFrame = realizedFrame;
	}

	/**
	 * This constructor uses a sentence and given character positions to extract
	 * the target of the frame element
	 * 
	 * @param realizedFrame
	 *            The realized frame to which this realized frame element
	 *            belongs
	 * @param frameElement
	 *            The frame element
	 * @param start
	 *            The first character of the target
	 * @param end
	 *            The first character after the target
	 * @deprecated
	 */
	@Deprecated
	public RealizedFrameElement(RealizedFrame realizedFrame,
			FrameElement frameElement, int start, int end) {
		this.frameElement = frameElement;
		this.realizedFrame = realizedFrame;
		this.start = start;
		this.end = end;
		target = realizedFrame.getSentence().getToken(new Range(start, end));
	}

	/**
	 * This constructor creates a new RealizedFrameElement based on the frame
	 * and the the frame element. It is used to create frame elements that are
	 * not instantiated (i.e., have no target in the sentence)
	 * 
	 * @param realizedFrame
	 *            The realized frame to which this realized frame element
	 *            belongs
	 * @param frameElement
	 *            The frame element
	 */
	public RealizedFrameElement(RealizedFrame realizedFrame,
			FrameElement frameElement) {
		this.frameElement = frameElement;
		this.realizedFrame = realizedFrame;
		nullInstantiated = true;
	}

	/**
	 * Returns a String representation of this realized frame element
	 */
	@Override
	public String toString() {
		if (isNullInstantiated())
			return getIType() + ": " + frameElement.name;
		return "\"" + target + "\": " + frameElement.name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the target
	 */
	public IToken getTarget() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(IToken target) {
		this.target = target;
	}

	/**
	 * Returns the {@link FrameElement} realized by this
	 * 
	 * @return the frameElement
	 */
	public FrameElement getFrameElement() {
		return frameElement;
	}

	/**
	 * Returns the {@link RealizedFrame} in which this frame element has been
	 * realized
	 * 
	 * @return the realizedFrame
	 */
	public RealizedFrame getRealizedFrame() {
		return realizedFrame;
	}

	/**
	 * Returns the end position of the target of this frame element. If the FE
	 * is null instantiated, the method returns -1.
	 * 
	 * @return The end position of the target or -1
	 */
	public int getEnd() {
		if (isNullInstantiated())
			return -1;
		return target.getRange().getElement2();
	}

	/**
	 * @deprecated
	 * @param end
	 */
	@Deprecated
	protected void setEnd(int end) {
		setNullInstantiated(false);
		this.end = end;
	}

	/**
	 * Returns the starting position of the target of this frame element. If the
	 * FE is null instantiated, the method returns -1.
	 * 
	 * @return The starting position of the target or -1
	 */
	public int getStart() {
		if (isNullInstantiated())
			return -1;
		return target.getRange().getElement1();
	}

	@Deprecated
	protected void setStart(int start) {
		setNullInstantiated(false);
		this.start = start;
	}

	public boolean isNullInstantiated() {
		return nullInstantiated;
	}

	public void setNullInstantiated(boolean nullInstantiated) {
		this.nullInstantiated = nullInstantiated;
	}

	/**
	 * Returns a string that represents the instantiation type of this FE
	 * 
	 * @return the instantiation type
	 */
	public String getIType() {
		return iType;
	}

	/**
	 * Sets the instantiation type of this FE
	 * 
	 * @param type
	 *            The intantiation type of this FE (a string)
	 */
	public void setIType(String type) {
		iType = type;
	}

	/**
	 * Sets the property named "key" to the value "value"
	 * 
	 * @param key
	 *            The key of the property
	 * @param value
	 *            The value
	 * @since 0.4
	 */
	public void setProperty(String key, String value) {
		if (data == null)
			data = new Properties();
		data.setProperty(key, value);
	}

	/**
	 * Retrieves the property with key "key"
	 * 
	 * @param key
	 * @return The value of the property
	 * @since 0.4
	 */
	public String getProperty(String key) {
		return data.getProperty(key);
	}

	/**
	 * Returns true, if the sentence, in which the target of the frame appears,
	 * is the same sentence than the sentence, in which the target of this frame
	 * element appears.
	 * 
	 * @return true or false
	 */
	public boolean isCrossingSentenceBoundaries() {
		return getRealizedFrame().getTarget().getSentence() == getTarget()
				.getSentence();
	}
}
