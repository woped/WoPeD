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

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import de.uniheidelberg.cl.reiter.util.Range;

/**
 * This abstract class is the base class for any sentence reading class for a
 * specific corpus format. It basically provides the fields id and text,
 * representing a sentence identifier and the surface string of the sentence
 * itself.
 * 
 * @author Nils Reiter
 * @since 0.2
 */
public abstract class Sentence implements IHasID {
	/**
	 * An identifier of the sentence
	 */
	String id;

	/**
	 * The surface string of the sentence
	 */
	String text;

	/**
	 * A list of tokens in this sentence
	 */
	protected SortedMap<Range, IToken> tokenList;

	/**
	 * A constructor taking only the identifier. Optimally used in combination
	 * with {@link Sentence#setText(String)}.
	 * 
	 * @param id
	 *            The identifier
	 */
	public Sentence(String id) {
		this.id = id;
		text = "";

		init();
	}

	/**
	 * A constructor setting both the id and the text
	 * 
	 * @param id
	 *            The identifier
	 * @param text
	 *            The surface string of the sentence
	 */
	public Sentence(String id, String text) {
		this.id = id;
		this.text = text;

		init();
	}

	private void init() {
		tokenList = new TreeMap<Range, IToken>();
	}

	/**
	 * This abstract method returns a (ordered) list of realized frames which
	 * occur in this sentence. Has to be implemented in inheriting classes,
	 * since it heavily depends on the actual representation of the frames.
	 * 
	 * @return A list of RealizedFrame objects
	 */
	public abstract List<RealizedFrame> getRealizedFrames();

	public String getId() {
		return id;
	}

	/**
	 * Sets the identifier
	 * 
	 * @param id
	 *            the identifier
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the complete text of the sentence
	 * 
	 * @return the text of the sentence
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the surface string of the sentence
	 * 
	 * @param text
	 *            the sentence
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns an unsorted collection of tokens in this sentence.
	 * 
	 * @return A collection of tokens
	 */
	public Collection<IToken> getTokens() {
		return tokenList.values();
	}

	protected abstract void addToken(Range range);

	/**
	 * Returns a token containing a specific string. Returns null if the string
	 * does not appear in the sentence.
	 * 
	 * @param s
	 *            The string
	 * @return A token object or null
	 */
	public IToken getTokenForString(String s) {
		Range r = getRangeForString(s);
		if (r != null)
			return getToken(r);
		return null;
	}

	/**
	 * Returns a token object for the given range. Creates the object if non
	 * existing.
	 * 
	 * @param range
	 *            The range
	 * @return The token
	 */
	public IToken getToken(Range range) {
		if (!tokenList.containsKey(range)) {
			addToken(range);
		}
		return tokenList.get(range);
	}

	/**
	 * Returns the surface string for the given range.
	 * 
	 * @param range
	 *            The range we want to have a string for
	 * @return A substring of the sentence
	 */
	public String getSurface(Range range) {
		return getText().substring(range.getElement1(), range.getElement2());
	}

	/**
	 * Returns a range that contains the string s
	 * 
	 * @param s
	 *            The string we want to have the range for
	 * @return A range object
	 */
	public Range getRangeForString(String s) {
		int begin = getText().indexOf(s);
		if (begin == -1) {
			return null;
		}
		Range r = new Range(begin, begin + s.length());
		return r;
	}

	/**
	 * Returns the complete text of the sentence, as returned by
	 * {@link Sentence#getText()}.
	 */
	@Override
	public String toString() {
		return getText();
	}
}
