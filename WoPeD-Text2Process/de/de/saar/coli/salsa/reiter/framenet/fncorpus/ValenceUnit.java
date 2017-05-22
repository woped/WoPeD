package de.saar.coli.salsa.reiter.framenet.fncorpus;

import org.dom4j.Element;

import de.saar.coli.salsa.reiter.framenet.FrameElement;
import de.saar.coli.salsa.reiter.framenet.FrameElementNotFoundException;
import de.saar.coli.salsa.reiter.framenet.LexicalUnit;

/**
 * @author Fabian Friedrich
 * Object which stores a Valence Unit as occurring in a pattern
 * <valenceUnit GF="Ext" PT="NP" FE="Copy"/>
 *
 */
public class ValenceUnit {

	/**
	 * value of the GF attribute
	 */
	String grammaticalFunction;
	
	/**
	 * value of the PT attribute
	 */
	String phraseType;
	
	/**
	 * Value of the FE attribute
	 */
	FrameElement frameElement;
	
	/**
	 * @throws FrameElementNotFoundException 
	 * 
	 */
	public ValenceUnit(Element element, LexicalUnit lu) {
		grammaticalFunction = element.attributeValue("GF");
		phraseType = element.attributeValue("PT");
		try {
			frameElement = lu.getFrame().getFrameElement(element.attributeValue("FE"));
		}catch(FrameElementNotFoundException ex){
			System.err.println("Error in lu"+lu.getId());
			//ex.printStackTrace();			
		}
	}

	/**
	 * @return the grammaticalFunction
	 */
	public String getGrammaticalFunction() {
		return grammaticalFunction;
	}

	/**
	 * @return the phraseType
	 */
	public String getPhraseType() {
		return phraseType;
	}

	/**
	 * @return the frameElement
	 */
	public FrameElement getFrameElement() {
		return frameElement;
	}
	
}
