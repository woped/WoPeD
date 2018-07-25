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

import de.saar.coli.salsa.reiter.framenet.CorpusReader;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNet;
import de.saar.coli.salsa.reiter.framenet.ParsingException;

import java.util.List;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.jaxen.dom4j.Dom4jXPath;
import org.jaxen.JaxenException;

import java.io.File;

/**
 * This class reads in and organized the FrameNet corpus - or, to be precise, parts
 * of it. It can retrieve information about the frame and frame element assignment in
 * the different sentences. 
 * 
 * @since 0.3
 * @author Nils Reiter
 *
 */
public class FrameNetCorpus extends CorpusReader {
	/**
	 * The constructor
	 * @param frameNet The FrameNet object
	 * @throws FrameNotFoundException This exception is thrown when a frame is used 
	 * in the annotation which does not exist in the FrameNet database
	 * @throws FrameElementNotFoundException This exception is thrown when a frame element
	 * is used that does not exist in the FN database OR does not belong to the 
	 * frame in which it was used
	 */
	public FrameNetCorpus(FrameNet frameNet, Logger logger) 
	throws FrameNotFoundException, FrameElementNotFoundException {
		super(frameNet, logger);
	}

	
	
	/**
	 * This method does all the parsing stuff
	 * @throws FrameNotFoundException This exception is thrown when a frame is used 
	 * in the annotation which does not exist in the FrameNet database
	 * @throws FrameElementNotFoundException This exception is thrown when a frame element
	 * is used that does not exist in the FN database OR does not belong to the 
	 * frame in which it was used
	 */
	@SuppressWarnings("unchecked")
	public void parse(File file) 
	throws FrameNotFoundException, FrameElementNotFoundException, ParsingException {
		
		// Make document
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			document = reader.read(file);
		} catch (DocumentException e) {
			this.getLogger().severe(e.getMessage());
		}
		if (this.getLogger() != null)
			this.getLogger().info("XML Document ("+ file.getAbsolutePath() + ") has been read.");
	
		try {
			Dom4jXPath xpath = new Dom4jXPath("/corpus/documents/document/paragraphs/paragraph/sentences/sentence");
			List sentences = xpath.selectNodes(document);

			for (Object sent : sentences) {				
				Element sentence = (Element) sent;
				if (sent != null) {
					Sentence s = new Sentence(this, sentence);
					getSentences().add(s);
					getSentenceIndex().put(s.getId(), s);
				}
			}
			
		} catch (JaxenException e) {
			e.printStackTrace();
		}
	}


}
