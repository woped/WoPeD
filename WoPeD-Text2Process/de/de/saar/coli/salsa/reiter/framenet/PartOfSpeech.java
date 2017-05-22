/**
 * 
 * Copyright 2010 by Nils Reiter.
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
 * This enum is used to represent the different parts of speech appearing in the
 * FrameNet corpus.
 * 
 * @author reiter
 * @since 0.4.1
 */
public enum PartOfSpeech {
	Noun, Verb, Adjective, Adverb, Determiner, Preposition, Pronoun, Interjection, Numeral, SCON, Conjunction;

	/**
	 * Maps the string-representation of a part of speech onto the enum type.
	 * 
	 * @param pos
	 *            The string representation of the pos
	 * @return The enum type
	 */
	public static PartOfSpeech getPartOfSpeech(String pos) {
		if (pos.startsWith("V") || pos.startsWith("v"))
			return Verb;
		if (pos.startsWith("N") || pos.startsWith("n"))
			return Noun;
		if (pos.equals("A"))
			return Adjective;
		if (pos.equals("ADV"))
			return Adverb;
		if (pos.equals("ART"))
			return Determiner;
		if (pos.equals("PREP"))
			return Preposition;
		if (pos.equals("INTJ"))
			return Interjection;
		if (pos.equals("PRON"))
			return Pronoun;
		if (pos.equals("C"))
			return Conjunction;
		if (pos.equals("SCON"))
			return SCON;
		if (pos.equals("Num"))
			return Numeral;
		System.err.println(pos);
		return null;
	}
}
