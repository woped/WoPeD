package de.saar.coli.salsa.reiter.framenet.fncorpus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import de.saar.coli.salsa.reiter.framenet.LexicalUnit;
import de.saar.coli.salsa.reiter.framenet.Lexeme;
import de.saar.coli.salsa.reiter.framenet.Frame;
import de.saar.coli.salsa.reiter.framenet.FrameNotFoundException;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;

import org.dom4j.Element;
import org.jaxen.dom4j.Dom4jXPath;
import org.jaxen.JaxenException;

/**
 * This class represents one luXML-file, i.e., a lexical unit with
 * its example annotations and realizations (valence patterns).
 * 
 * @author reiter
 * @since 0.4
 *
 */
public class AnnotatedLexicalUnit {
	/**
	 * The lexical unit in the FrameNet database
	 */
	LexicalUnit lexicalUnit = null;
	
	/**
	 * The definition string
	 */
	String definition = null;
	
	/**
	 * A list of incorporated FEs
	 */
	String incorporatedFE = null;
	
	/**
	 * The corpus
	 */
	AnnotationCorpus annotationCorpus = null;
	
	/**
	 * A list of sentences containing this lexical unit
	 */
	List<Sentence> sentences = null;
	
	/**
	 * A list of FE(Group)Realizations which holds the identified valence patterns for
	 * the different FrameElements
	 */
	List<FEGroupRealization> realizations = null;
	
	/**
	 * The frame in which this lexical unit is listed
	 */
	Frame frame = null;
	
	@SuppressWarnings("unchecked")
	/**
	 * The constructor processes the XML node(s).
	 */
	public AnnotatedLexicalUnit(AnnotationCorpus annotationCorpus, Element element, LexicalUnit lu) 
	throws JaxenException, FrameNotFoundException{
		this.lexicalUnit = lu;
		this.annotationCorpus = annotationCorpus;
		this.incorporatedFE = element.attributeValue("incorporatedFE");
		this.definition = element.valueOf("definition");
		this.sentences = new LinkedList<Sentence>();
		this.realizations = new LinkedList<FEGroupRealization>();
		this.frame = annotationCorpus.getFrameNet().getFrame(element.attributeValue("frame"));
		
		//adding all realizations
		//scanning through FE(Group)Realizations
		Element val_element = element.element("valences");
		List<Element> relizations = new ArrayList<Element>(val_element.elements("FERealization"));
		relizations.addAll(val_element.elements("FEGroupRealization"));
				
		Iterator fer_iter = relizations.iterator();
		while (fer_iter.hasNext()) {
			Element fegr_element = (Element) fer_iter.next();
			realizations.add(new FEGroupRealization(fegr_element,this.lexicalUnit));			
		}
		
		//scanning through corpus elements
		if(annotationCorpus.scanSubCorpuses) {
			Dom4jXPath xpath = new Dom4jXPath("berk:subCorpus");
			xpath.setNamespaceContext(annotationCorpus.getFrameNet().getNameSpaceContext());
			Iterator sc_iter = xpath.selectNodes(element).iterator();
			while (sc_iter.hasNext()) {
				Element subcorpus = (Element) sc_iter.next();
				
				xpath = new Dom4jXPath("berk:sentence");
				xpath.setNamespaceContext(annotationCorpus.getFrameNet().getNameSpaceContext());
				Iterator sent_iter = xpath.selectNodes(subcorpus).iterator();
				
				
				while(sent_iter.hasNext()) {
					Element sentence_element = (Element) sent_iter.next();
					Sentence sentence = new Sentence(this.annotationCorpus, sentence_element);
					sentences.add(sentence);
					
					xpath = new Dom4jXPath("berk:annotationSet");
					xpath.setNamespaceContext(annotationCorpus.getFrameNet().getNameSpaceContext());
					Iterator as_iter = xpath.selectNodes(sentence_element).iterator();
					while(as_iter.hasNext()) {
						Element annotationSet_element = (Element) as_iter.next();
						try {
							new AnnotationSet(this.annotationCorpus,sentence, annotationSet_element, frame, lexicalUnit);
						} catch (FrameElementNotFoundException e) {
							e.printStackTrace();
						}
					}
				}				
			}
		}
		
	}

	/**
	 * Returns the definition of the lexical unit
	 * @return a string
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * Returns the incorporated frame element.
	 * @return A string
	 */
	public String getIncorporatedFE() {
		return incorporatedFE;
	}
	
	/**
	 * Returns a collection of lexemes in this lexical unit
	 * @return The lexemes of this lexical unit
	 */
	public List<Lexeme> getLexemes() {
		return this.lexicalUnit.getLexemes();
	}

	/**
	 * Returns a list of sentences containing this lexical unit
	 * @return A list of sentences
	 */
	public List<Sentence> getSentences() {
		return sentences;
	}
	
	/**
	 * Returns a list of FE(Group)Realizations derived from the annotated material
	 * @return A list of sentences
	 */
	public List<FEGroupRealization> getFERealizations() {
		return realizations;
	}

	/**
	 * Returns the frame 
	 * @return a frame
	 */
	public Frame getFrame() {
		return frame;
	}
}
