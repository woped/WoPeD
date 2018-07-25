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

import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;

import org.jaxen.dom4j.Dom4jXPath;
import org.jaxen.JaxenException;

import java.util.List;

import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.IHasID;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;
import de.saar.coli.salsa.reiter.framenet.ParsingException;
import de.saar.coli.salsa.reiter.framenet.RealizedFrame;
import de.saar.coli.salsa.reiter.framenet.RealizedFrameElement;

import de.uniheidelberg.cl.reiter.util.*;

/**
 * This class represents an annotation set from the FrameNet annotation.
 * An annotation set is connected to a single realized frame and a 
 * sentence. 
 * 
 * @author Nils Reiter
 * @since 0.2
 */
public class AnnotationSet implements IHasID {
	/**
	 * The identifier of the annotation set
	 */
	String id;
	
	/**
	 * The status of the annotation set
	 */
	String status;
	
	/**
	 * The frame occurring in this annotation set
	 */
	Frame frame;
	
	/**
	 * The lexical unit evoking the frame
	 */
	LexicalUnit lexicalUnit;
	
	/**
	 * The XML element "frameRef"
	 */
	String frameRef;
	
	/**
	 * The XML element "luRef"
	 */
	String luRef;
	
	/**
	 * The realized frame
	 */
	RealizedFrame realizedFrame;
	
	/**
	 * The sentence which is annotated by this AnnotationSet.
	 */
	Sentence sentence;

	private AnnotationCorpus annotationCorpus;
	
	/**
	 * The constructor used for constructing the AnnotationSet object
	 * out of an XML element node. 
	 * 
	 * @param sentence The sentence which is annotated
 	 * @param element The XML element representing the annotation set
	 * @throws FrameNotFoundException
	 * @throws FrameElementNotFoundException
	 * @throws ParsingException
	 */
	public AnnotationSet(Sentence sentence, Element element) 
	throws FrameNotFoundException, FrameElementNotFoundException, ParsingException, JaxenException {
		this.sentence = sentence;
		init(element);
		frameRef = element.attributeValue("frameRef");
		luRef = element.attributeValue("lexUnitRef");
		String frameName = element.attributeValue("frameName");
		String luName = element.attributeValue("luName");
		if (frameName != null) {
			frame = sentence.corpus.getFrameNet().getFrame(frameName);
		}
		if (luName != null) {
			lexicalUnit = frame.getLexicalUnit(luName);
		}
		
		Element targetlabel = (Element) new Dom4jXPath("layers/layer[@name='Target']/labels/label").selectSingleNode(element);
		if (targetlabel != null && targetlabel.attributeValue("start") != null) {
			int start = Integer.parseInt(targetlabel.attributeValue("start"));
			int end = Integer.parseInt(targetlabel.attributeValue("end")) + 1;
			realizedFrame = new RealizedFrame(frame, sentence.getToken(new Range( start, end )), "");
		}
		
		this.processFrameElements(element);
	} 
	
	/**
	 * This constructor is to be used with the AnnotationCorpus class
	 * @param annotationCorpus 
	 * 
	 * @param sentence The sentence
	 * @param element The XML element
	 * @param frame The frame
	 * @param lexicalUnit The lexical unit
	 * @throws JaxenException
	 * @throws FrameElementNotFoundException
	 */
	public AnnotationSet(AnnotationCorpus annotationCorpus, Sentence sentence, Element element, Frame frame, LexicalUnit lexicalUnit) 
	throws JaxenException, FrameElementNotFoundException {
		init(element);
		this.annotationCorpus = annotationCorpus;
		this.frame = frame;
		this.lexicalUnit = lexicalUnit;
		this.sentence = sentence;
		Dom4jXPath xpath = new Dom4jXPath("/berk:layer[@name='Target']/berk:label");
		xpath.setNamespaceContext(annotationCorpus.getFrameNet().getNameSpaceContext());
		Element targetlabel = (Element) xpath.selectSingleNode(element);
		if (targetlabel != null && targetlabel.attributeValue("start") != null) {
			int start = Integer.parseInt(targetlabel.attributeValue("start"));
			int end = Integer.parseInt(targetlabel.attributeValue("end")) + 1;
			realizedFrame = new RealizedFrame(frame, sentence.getToken(new Range( start, end )), "");
		}
		
		this.processFrameElements(element);
	}
	
	private void init(Element element) {
		id = element.attributeValue("ID");
		status = element.attributeValue("status");
	}
	
	@SuppressWarnings("unchecked")
	private void processFrameElements(Element element) throws FrameElementNotFoundException, JaxenException {
		Dom4jXPath xpath = new Dom4jXPath("berk:layer[@name='FE']/berk:label");
		xpath.setNamespaceContext(this.annotationCorpus.getFrameNet().getNameSpaceContext());
		List felabels = xpath.selectNodes(element);
		
		for (Object lab : felabels) {
			Element label = (Element) lab;
			if (label != null) {
				if (realizedFrame != null) {
					try {
						if (label.attributeValue("start") != null && label.attributeValue("end") != null) {
							int start,end;
							start = Integer.parseInt(label.attributeValue("start"));
							end = Integer.parseInt(label.attributeValue("end"));
							String fename = label.attributeValue("name");
							Range range = new Range(start,end+1);
							if (frame != null) {
								RealizedFrameElement rfe = realizedFrame.addRealizedFrameElement(fename, sentence.getToken(range));

								xpath = new Dom4jXPath("../berk:layer[@name='GF']/berk:label[@start='"+start+"' and @end='"+end+"']/@name");
								xpath.setNamespaceContext(this.annotationCorpus.getFrameNet().getNameSpaceContext());
								DefaultAttribute gfAttribute = (DefaultAttribute) xpath.selectSingleNode(label);
								if (gfAttribute != null)
									rfe.setProperty("GF", gfAttribute.getStringValue());

								xpath = new Dom4jXPath("../berk:layer[@name='PT']/berk:label[@start='"+start+"' and @end='"+end+"']/@name");
								xpath.setNamespaceContext(this.annotationCorpus.getFrameNet().getNameSpaceContext());
								DefaultAttribute ptAttribute = (DefaultAttribute) xpath.selectSingleNode(label);
								if (gfAttribute != null)
									rfe.setProperty("PT", ptAttribute.getStringValue());
							}
							
						} else if (label.attributeValue("itype") != null) {
							String iType = "";
							iType = label.attributeValue("itype");
							RealizedFrameElement rfe = new RealizedFrameElement(realizedFrame, 
									realizedFrame.getFrame().getFrameElement(label.attributeValue("name")));
							rfe.setNullInstantiated(true);
							rfe.setIType(iType);
							realizedFrame.addRealizedFrameElement(rfe);
						}
					}
					catch (FrameElementNotFoundException e) {
						sentence.getCorpus().getLogger().severe(e.getMessage());
						if (sentence.getCorpus().isAbortOnError()) 
							throw new FrameElementNotFoundException(e.getFrame(), e.getFrameElement());
					}
				}
			}
		}
	}
	
	
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the RealizedFrame object of this annotation set
	 * @return the realized frame
	 */
	public RealizedFrame getRealizedFrame() {
		return realizedFrame;
	}
}
