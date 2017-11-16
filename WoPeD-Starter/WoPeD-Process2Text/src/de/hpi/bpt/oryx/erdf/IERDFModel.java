package de.hpi.bpt.oryx.erdf;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


/**
 * Interface to an eRDF model
 * 
 * @author Artem Polyvyanyy
 */
public interface IERDFModel<E extends ERDFEdge<V>, V extends ERDFNode> {
	
	/**
	 * Parse eRDF model form eRDF string
	 * @param erdfString String containing eRDF encoding
	 */
	public void parseERDF(String erdfString) throws SAXException, IOException, ParserConfigurationException;
	
	/**
	 * Parse eRDF model form eRDF file
	 * @param erdfFile File containing eRDF encoding
	 */
	public void parseERDFFile(String erdfFile) throws SAXException, IOException, ParserConfigurationException;

	/**
	 * Get eRDF model serialization string
	 * @return eRDF serialization string of the model
	 */
	public String serializeERDF();
	
	public V createNode(String type);
	
	public E createEdge(String type, V s, V t);
}
