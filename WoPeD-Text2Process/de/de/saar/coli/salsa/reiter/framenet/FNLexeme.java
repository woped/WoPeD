package de.saar.coli.salsa.reiter.framenet;

import org.dom4j.Element;

/**
 * This class provides the Lexeme data from the original FrameNet XML files
 * @author Nils Reiter
 * @since 0.4
 */
public class FNLexeme extends Lexeme {
	private static final long serialVersionUID = 2l;
	
	/**
	 * The constructor builds a new Lexeme object based on an XML node
	 * @param node The XML node
	 */
	protected FNLexeme(Element node) {
		id = node.attributeValue("ID");
		pos = PartOfSpeech.getPartOfSpeech(node.attributeValue("POS"));
		breakBefore = node.attributeValue("breakBefore").equalsIgnoreCase("true");
		headword = node.attributeValue("headword").equalsIgnoreCase("true");
		value = node.getStringValue();
	}
	
}
