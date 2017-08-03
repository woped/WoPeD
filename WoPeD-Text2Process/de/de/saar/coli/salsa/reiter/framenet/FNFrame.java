package de.saar.coli.salsa.reiter.framenet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

import org.dom4j.Element;
import org.jaxen.JaxenException;
import org.jaxen.dom4j.Dom4jXPath;

/**
 * This class implements the frame data from the original 
 * FrameNet database
 * @author Nils Reiter
 * @since 0.4
 *
 */
public class FNFrame extends Frame {
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * Do not use this method directly. Use the method getFrame() in the
	 * FrameNet-class.
	 * @param node The XML node in the FrameNet database that represents the frame.
	 * @param frameNet The FrameNet object.
	 * @see FrameNet#getFrame(String)
	 */
	@SuppressWarnings("unchecked")
	protected FNFrame(Element node, FrameNet frameNet) throws JaxenException {
		name = node.attributeValue("name");
		id = node.attributeValue("ID");
		cDate = node.attributeValue("cDate");
		if (node.attributeValue("Source") != null)
			source = node.attributeValue("Source");
		definition = node.element("definition").getText().replace('\n', ' ').getBytes();
				
		linkFrameNet(frameNet);
		
		// Lexical Units
		frameNet.log(Level.FINE, "Loading lexical units ...");
		lexicalUnits = new HashSet<LexicalUnit>();
		lexicalUnitsPerPOS = new HashMap<PartOfSpeech, Integer>();
		Dom4jXPath xpath = new Dom4jXPath("//berk:lexUnit");
		xpath.setNamespaceContext(frameNet.getNameSpaceContext());
		List lexunit_nodelist = xpath.selectNodes(node);
		for (int i = 0; i < lexunit_nodelist.size(); i++) {
			Element lu = (Element) lexunit_nodelist.get(i);
			
			LexicalUnit luo = new FNLexicalUnit(this, lu);
			lexicalUnits.add(luo);
			if (! lexicalUnitsPerPOS.containsKey(luo.getPartOfSpeech()))
				lexicalUnitsPerPOS.put(luo.getPartOfSpeech(), 1);
			else {
				Integer oldval = lexicalUnitsPerPOS.get(luo.getPartOfSpeech());
				lexicalUnitsPerPOS.put(luo.getPartOfSpeech(), 
					(oldval+1));
			}
		}
		
		// Frame Elements
		frameNet.log(Level.FINE, "Loading frame elements ...");
		frameElements = new HashMap<String, FrameElement>();
		xpath = new Dom4jXPath("//berk:FE");
		xpath.setNamespaceContext(frameNet.getNameSpaceContext());
		List fe_nodelist = xpath.selectNodes(node);
			for (int i = 0; i < fe_nodelist.size(); i++) {
				Element fe = (Element) fe_nodelist.get(i);
				frameElements.put(fe.attributeValue("name"), new FNFrameElement(this, fe));
			}
		
		
		framenet.log(Level.INFO, "Frame " + name + " newly created.");
	}
}
