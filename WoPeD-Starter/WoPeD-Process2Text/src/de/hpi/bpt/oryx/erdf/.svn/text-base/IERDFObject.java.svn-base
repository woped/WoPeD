package de.hpi.bpt.oryx.erdf;

import org.w3c.dom.Node;

/**
 * Interface to the eRDF object (node or edge)
 * eRDF object is <name,value> pair collection
 * 
 * @author Artem Polyvyanyy
 */
public interface IERDFObject {

	/**
	 * Get eRDF object property 
	 * @param name Property name
	 * @return Property value, <code>null</code> if value for the property does not exist
	 */
	public String getProperty(String name);
	
	/**
	 * Set eRDF object property
	 * @param name Property name
	 * @param value New property value
	 * @return Old property value, <code>null</code> if value was not previously set
	 */
	public String setProperty(String name, String value);
	
	/**
	 * Parse eRDF object form eRDF DOM Node
	 * @param erdfNode Object eRDF DOM Node
	 */
	public void parseERDF(Node erdfNode);

	/**
	 * Get eRDF object serialization DOM Node
	 * @return eRDF serialization string of the model
	 */
	public Node serializeERDF();
}
