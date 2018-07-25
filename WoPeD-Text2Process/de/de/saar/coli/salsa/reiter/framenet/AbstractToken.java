package de.saar.coli.salsa.reiter.framenet;

import de.uniheidelberg.cl.reiter.util.Range;
import java.util.Properties;
import java.util.Collection;

/**
 * Makes a basic implementation of the IToken interface. 
 * Different corpus readers extend it.
 * 
 * @author Nils Reiter
 * @since 0.4
 */
public abstract class AbstractToken implements IToken {
	
	
	Properties data;

	/**
	 * Constructor. 
	 */
	public AbstractToken() {
		data = new Properties();
	}
	
	abstract public Range getRange();

	abstract public Sentence getSentence();

	public void setProperty(String key, String value) {
		data.setProperty(key, value);
	}

	public String getProperty(String key) {
		return data.getProperty(key);
	}
	
	public Collection<String> getPropertyKeys() {
		return data.stringPropertyNames();
	}
	
}
