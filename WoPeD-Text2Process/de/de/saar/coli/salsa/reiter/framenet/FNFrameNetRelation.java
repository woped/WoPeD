package de.saar.coli.salsa.reiter.framenet;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

import org.dom4j.Element;
import org.jaxen.JaxenException;
import org.jaxen.dom4j.Dom4jXPath;

/**
 * This class provides the Frame Relation data from the original FrameNet XML files
 * @author Nils Reiter
 * @since 0.4
 */
public class FNFrameNetRelation extends FrameNetRelation {
private final static long serialVersionUID = 1L;
	
	/**
	 * Creates a FrameNetRelation object. 
	 * @param frameNet The FrameNet object in which this relation is used.
	 * @param relationnode The XML node representing the relation in the 
	 * XML file.
	 */
	protected FNFrameNetRelation(FrameNet frameNet, Element relationnode) throws JaxenException {
		this.frameNet = frameNet;
		this.name = relationnode.attributeValue("name");
		this.id = relationnode.attributeValue("ID");
		this.superName = relationnode.attributeValue("superFrameName");
		this.subName = relationnode.attributeValue("subFrameName");
		this.frameRelations = new HashSet<FrameRelation>();
		
		frameNet.log(Level.INFO, "FrameNetRelation.FrameNetRelation(): Node loaded. " + name);
		init(relationnode);
		
	}
	
	@SuppressWarnings("unchecked")
	private void init(Element xmlnode) throws JaxenException {
		Dom4jXPath xpath = new Dom4jXPath("berk:frameRelation");
		xpath.setNamespaceContext(this.frameNet.getNameSpaceContext());
		List list = xpath.selectNodes(xmlnode);

		for (Object item : list) {
			try {
				FrameRelation frel = new FrameRelation(this.frameNet, this, (Element) (item));
				frameRelations.add(frel);
			} catch (FrameNotFoundException e) {
				frameNet.log(Level.WARNING, e.getFrameName() + " not found in the FrameNet database, but used in frame relation " + id + ". Files not consistent");
			} catch (FrameElementNotFoundException e) {
				frameNet.log(Level.WARNING, e.getFrameElement() + " is not in frame " + e.getFrame().getName() + ", but specified in frame relation " + id + ". Files not consistent.");
			} catch (Exception e) {
				frameNet.log(Level.SEVERE, e.toString());
			}
		}

	}
}
