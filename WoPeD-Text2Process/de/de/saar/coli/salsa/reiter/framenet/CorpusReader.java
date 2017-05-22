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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This abstract class is the base class for all classes reading in a specific
 * format representing text with annotated frames and frame elements.
 * 
 * @author Nils Reiter
 * @since 0.2
 * 
 */
public abstract class CorpusReader {
	/**
	 * If set to true, the parsing process is aborted if, for instance, a frame
	 * is found which does not occurr in the FrameNet database. If set to false,
	 * the parser will try to continue with the next item.
	 */
	boolean abortOnError = false;

	/**
	 * If set to true, some debug information is printed to the standard output
	 * 
	 * @deprecated
	 */
	@Deprecated
	boolean debug = false;

	/**
	 * A pointer to the FrameNet object corresponding to this corpus
	 */
	protected FrameNet frameNet = null;

	/**
	 * An ordered list of sentences in this corpus. Note, that the sentences
	 * should be ordered as they are in the file.
	 */
	List<Sentence> sentences;

	/**
	 * An index, mapping the sentence id to the sentence object
	 */
	Map<String, Sentence> sentenceIndex;

	/**
	 * The Logger for debug, warn and other outputs
	 */
	Logger logger;

	/**
	 * The basic constructor takes only the FrameNet object as argument. It
	 * initialises the list and map and sets the debug property to the same
	 * value as it is set in the FrameNet object
	 * 
	 * @param frameNet
	 *            The FrameNet object
	 */
	public CorpusReader(FrameNet frameNet, Logger logger) {
		this.frameNet = frameNet;
		this.logger = logger;
		sentences = new LinkedList<Sentence>();
		sentenceIndex = new HashMap<String, Sentence>();
	};

	/**
	 * This method has to be implemented in any subclass. It does the actual
	 * parsing process. The only argument it takes is the file to parse.
	 * 
	 * @param fileOrDirectory
	 *            the file
	 * @throws FrameNotFoundException
	 *             when a frame is used in the corpus which does not exist in
	 *             the FrameNet database
	 * @throws FrameElementNotFoundException
	 *             When a frame element is used in the corpus which does not
	 *             exist in the FN database OR does not exist in the frame in
	 *             which it occurred.
	 * @throws ParsingException
	 *             If any other parsing error occurred
	 * @throws FileNotFoundException
	 *             If the file does not exist
	 */
	public abstract void parse(File fileOrDirectory)
			throws FrameNotFoundException, FrameElementNotFoundException,
			ParsingException, FileNotFoundException;

	/**
	 * Returns a list of realized frames found in the parsed file(s), no matter
	 * in which sentence
	 * 
	 * @return A list of RealizedFrame objects
	 */
	public List<RealizedFrame> getRealizedFrames() {
		List<RealizedFrame> ret = new LinkedList<RealizedFrame>();
		for (Sentence sentence : sentences) {
			if (logger != null)
				logger.fine("Adding realized frames for sentence "
						+ sentence.getId());
			ret.addAll(sentence.getRealizedFrames());
		}
		return ret;
	}

	/**
	 * Returns a list of realized frames occurring in a sentence with the given
	 * sentence id.
	 * 
	 * @param sentenceId
	 *            The sentence id
	 * @return A list of RealizedFrame objects or an empty list if the sentence
	 *         id is not found
	 */
	public List<RealizedFrame> getRealizedFrames(String sentenceId) {
		Sentence sentence = sentenceIndex.get(sentenceId);
		if (sentence == null)
			return new LinkedList<RealizedFrame>();
		return sentence.getRealizedFrames();
	}

	/**
	 * Returns a collection of all sentence ids in the corpus
	 * 
	 * @return A collection of all sentence ids in the corpus
	 */
	public Collection<String> getSentenceIds() {
		return sentenceIndex.keySet();
	}

	/**
	 * Returns true if the parser is set to abort if an error occurs
	 * 
	 * @return true if the parser aborts on an error
	 */
	public boolean isAbortOnError() {
		return abortOnError;
	}

	/**
	 * If set to true, the parser aborts, if an error occurs. Otherwise, it
	 * tries to continue with the next item
	 * 
	 * @param abortOnError
	 *            The parameter
	 */
	public void setAbortOnError(boolean abortOnError) {
		this.abortOnError = abortOnError;
	}

	/**
	 * Returns true, if the parser generates some debugging output
	 * 
	 * @return True, if the parser is set to generate some debugging output
	 * @deprecated
	 */
	@Deprecated
	public boolean isDebug() {
		return debug;
	}

	/**
	 * If set to true, the parser will generate some debugging output
	 * 
	 * @param debug
	 *            true or false
	 * @deprecated
	 */
	@Deprecated
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Returns the FrameNet object
	 * 
	 * @return the FrameNet object
	 */
	public FrameNet getFrameNet() {
		return frameNet;
	}

	/**
	 * Returns the index of sentence ids and sentences
	 * 
	 * @return a map containing the sentence id and a Sentence object
	 */
	public Map<String, Sentence> getSentenceIndex() {
		return sentenceIndex;
	}

	/**
	 * Returns an ordered list of Sentence objects
	 * 
	 * @return The sentences in this corpus
	 */
	public List<Sentence> getSentences() {
		return sentences;
	}

	/**
	 * Returns the logger set for this corpus reader.
	 * 
	 * @return The logger for this reader
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Sets the logger for this corpus reader.
	 * 
	 * @param logger
	 *            The logger. If set to null, no logging is done.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
