package de.saar.coli.salsa.reiter.framenet;

import java.text.ParseException;
import java.util.List;

import org.dom4j.Element;
import org.jaxen.JaxenException;
import org.jaxen.dom4j.Dom4jXPath;

/**
 * This class provides the Frame Element data from the original FrameNet XML files
 * @author Nils Reiter
 * @since 0.4
 */
public class FNFrameElement extends FrameElement {
	
	private static final long serialVersionUID = 1L;

	
	/**
	 * Creates a new FrameElement object. 
	 * The XML node should provide the following attributes: name, abbrev, cDate, coreType and ID.
	 * There should be a definition element present as a sub node of the given node.
	 * 
	 * @param frame The frame to which this frame element belongs.
	 * @param node The node in the XML document
	 */
	@SuppressWarnings("unchecked")
	protected FNFrameElement(Frame frame, Element node) throws JaxenException {
		super();
		name = node.attributeValue("name");
		abbreviation = node.attributeValue("abbrev");
		try {
			//System.out.println(FrameNet.dateFormat.format(new Date()));
			creationDate = FrameNet.dateFormat.parse(node.attributeValue("cDate"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		coreType = CoreType.fromString(node.attributeValue("coreType"));
		id = node.attributeValue("ID");
		definition = node.element("definition").getText().getBytes();
		
		this.frame = frame;
		
		
		// May cause problems when there is no semTypes element. 
		// In 1.3, there are semTypes-elements, even when there is no semType-element.
		Dom4jXPath xpath = new Dom4jXPath("berk:semType");
		xpath.setNamespaceContext(frame.framenet.getNameSpaceContext());
		List st_nodelist = xpath.selectNodes(node); //((Element) node.getElementsByTagName("semTypes").item(0)).getElementsByTagName("semType");
		this.semanticTypes = new SemanticType[st_nodelist.size()];
		for (int i = 0; i < st_nodelist.size(); i++) {
			Element stnode = (Element) st_nodelist.get(i);
			SemanticType st = frame.framenet.getSemanticType(stnode.attributeValue("name"), true);
			this.semanticTypes[i] = st;
			st.registerFrameElement(this);
		}
	}
}
