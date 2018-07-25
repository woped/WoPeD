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
package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;
import org.jaxen.JaxenException;
import org.jaxen.dom4j.Dom4jXPath;

import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.ParsingException;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.uniheidelberg.cl.reiter.util.Range;

/**
 * This class represents a single sentence in the FrameNet corpus. Multiple
 * annotation sets belong to one sentence.
 * 
 * 
 * @author Nils Reiter
 * @since 0.2
 */
public class Sentence extends de.saar.coli.salsa.reiter.framenet.Sentence {
	// String nid;

	/**
	 * The FN corpus
	 */
	FrameNetCorpus corpus;

	/**
	 * A list of annotation sets for this sentence
	 */
	List<AnnotationSet> annotationSets = new LinkedList<AnnotationSet>();

	/**
	 * This constructor takes a FrameNetCorpus object and the XML element node
	 * representing a single sentence in the data file.
	 * 
	 * @param corpus
	 *            The FrameNetCorps
	 * @param node
	 *            The sentence node
	 * @throws FrameElementNotFoundException
	 * @throws FrameNotFoundException
	 * @throws ParsingException
	 */
	@SuppressWarnings("unchecked")
	public Sentence(FrameNetCorpus corpus, Element node)
			throws FrameElementNotFoundException, FrameNotFoundException,
			ParsingException, JaxenException {
		super(node.attributeValue("ID"), node.element("text").getText());
		this.corpus = corpus;

		// Search for all tokens, add them to the list
		List allTokens = new Dom4jXPath(
				"annotationSets/annotationSet/layers/layer/labels/label[@start and @end]")
				.selectNodes(node);
		for (Object tokenObj : allTokens) {
			Element token = (Element) tokenObj;
			Range range = new Range(new Integer(token.attributeValue("start")),
					new Integer(token.attributeValue("end")) + 1);
			addToken(range);
		}

		List asets = new Dom4jXPath("annotationSets/annotationSet[@frameName]")
				.selectNodes(node);

		// NodeList asets = node.getElementsByTagName("annotationSet");
		// if (asets != null) {
		for (Object aseto : asets) {
			Element aset = (Element) aseto;
			if (aset.attributeValue("frameName") != "") {
				try {
					annotationSets.add(new AnnotationSet(this, aset));
				} catch (ParsingException e) {
					getCorpus().getLogger().severe(e.getMessage());
					if (corpus.isAbortOnError())
						throw new ParsingException(e.getMessage());
				} catch (FrameNotFoundException e) {
					getCorpus().getLogger().warning(e.getMessage());
					if (corpus.isAbortOnError())
						throw new FrameNotFoundException(e.getFrameName());
				}
			}
		}

	}

	/**
	 * This constructor is to be used with AnnotationCorpus.
	 * 
	 * @param annotationCorpus
	 *            The corpus
	 * @param element
	 *            The XML element of the sentence
	 * @throws JaxenException
	 */
	@SuppressWarnings("unchecked")
	public Sentence(AnnotationCorpus annotationCorpus, Element element)
			throws JaxenException {
		super(element.attributeValue("ID"), element.element("text").getText());

		Dom4jXPath xpath = new Dom4jXPath("berk:annotationSet/berk:layer/berk:label[@start and @end]");
		xpath.setNamespaceContext(annotationCorpus.getFrameNet().getNameSpaceContext());
		List allTokens = xpath.selectNodes(element);
		for (Object tokenObj : allTokens) {
			Element token = (Element) tokenObj;
			Range range = new Range(new Integer(token.attributeValue("start")),
					new Integer(token.attributeValue("end")) + 1);
			addToken(range);
		}
	}

	@Override
	public List<RealizedFrame> getRealizedFrames() {
		List<RealizedFrame> ret = new LinkedList<RealizedFrame>();
		for (AnnotationSet aset : annotationSets) {
			ret.add(aset.getRealizedFrame());
		}
		return ret;
	}

	/**
	 * Returns a reference to the FrameNet corpus
	 * 
	 * @return the FrameNet corpus
	 */
	public FrameNetCorpus getCorpus() {
		return corpus;
	}

	@Override
	protected void addToken(Range range) {
		Token token = new Token(this, range);
		tokenList.put(range, token);
	}
}
