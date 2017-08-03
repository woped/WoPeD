package de.saar.coli.salsa.reiter.framenet;

import java.util.List;

import org.dom4j.Element;
import org.jaxen.JaxenException;
import org.jaxen.dom4j.Dom4jXPath;

/**
 * This class provides the Semantic Type data from the original FrameNet XML files
 * @author Nils Reiter
 * @since 0.4
 *
 */
public class FNSemanticType extends SemanticType {
	
	private final static long serialVersionUID = 1l;
	
	/**
	 * This constructor loads the semantic type directly from
	 * the XML node in the file "semtypes.xml". It takes the 
	 * node and the FrameNet object as arguments. 
	 * 
	 * 
	 * @param frameNet The FrameNet object
	 * @param node The XML node representing this semantic type
	 */
	protected FNSemanticType(FrameNet frameNet, Element node) throws JaxenException {
		super();
		this.frameNet = frameNet;
		supplyNode(node);
		
	}
	
	
	
	/**
	 * Used to complete a semantic type by providing only the XML node. 
	 * This method overwrittes all data that have been preliminarily stored
	 * within this semantic type.
	 * 
	 * @param node The XML node
	 * @return true if the node has been successfully loaded, false if it was
	 * already loaded
	 */
	@SuppressWarnings("unchecked")
	protected Boolean supplyNode(Element node) throws JaxenException {
		if (! this.nodeSupplied) {
			this.id = node.attributeValue("ID");
			this.name = node.attributeValue("name");
			this.abbreviation = node.attributeValue("abbrev");
			this.definition = node.element("definition").getText().replace('\n', ' ');
			Dom4jXPath xpath = new Dom4jXPath("berk:superType");
			xpath.setNamespaceContext(this.frameNet.getNameSpaceContext());
			List list = xpath.selectNodes(node);

			for (Object item : list) {
				Element st = (Element) item;
				this.superTypes.add(frameNet.getSemanticType(st.attributeValue("superTypeName"), true));
				try {
					this.frameNet.getSemanticType(st.attributeValue("superTypeName")).registerSubType(this);
				} catch (SemanticTypeNotFoundException  e) {
					// TODO: Collect inconsistencies in the FrameNet database
				}
			}
			this.nodeSupplied = true;
			return true;
		}
		return false;
	}
}
