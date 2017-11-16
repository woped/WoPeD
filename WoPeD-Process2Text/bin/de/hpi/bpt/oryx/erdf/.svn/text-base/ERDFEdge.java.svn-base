package de.hpi.bpt.oryx.erdf;

import org.w3c.dom.Node;

import de.hpi.bpt.graph.abs.AbstractDirectedEdge;
import de.hpi.bpt.graph.abs.AbstractMultiDirectedGraph;

/**
 * 
 * @author Artem Polyvyanyy
 *
 */
public class ERDFEdge<V extends ERDFNode> extends AbstractDirectedEdge<V> implements IERDFObject {
	
	private IERDFObject obj = new ERDFObject();
	
	@SuppressWarnings("unchecked")
	protected ERDFEdge(AbstractMultiDirectedGraph g, V source, V target) {
		super(g, source, target);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.oryx.erdf.IERDFObject#getProperty(java.lang.String)
	 */
	public String getProperty(String name) {
		return this.obj.getProperty(name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.oryx.erdf.IERDFObject#setProperty(java.lang.String, java.lang.String)
	 */
	public String setProperty(String name, String value) {
		return this.obj.setProperty(name, value);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.oryx.erdf.IERDFObject#parseERDF(org.w3c.dom.Node)
	 */
	public void parseERDF(Node node) {
		this.setId(node.getAttributes().getNamedItem("id").getTextContent());
		
		this.obj.parseERDF(node);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.hpi.bpt.oryx.erdf.IERDFObject#serializeERDF()
	 */
	public Node serializeERDF() {
		return this.obj.serializeERDF();
	}
}
