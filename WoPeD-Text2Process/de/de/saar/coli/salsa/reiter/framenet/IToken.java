package de.saar.coli.salsa.reiter.framenet;

import de.uniheidelberg.cl.reiter.util.Range;
import java.util.Collection;

/**
 * Interface for token-based annotation. 
 * Internally, a token is represented with a range and a sentence. 
 * The range is begin and end of the token in the sentence, such 
 * that {@link java.lang.String#substring(int, int)} returns the
 * correct surface.
 * 
 * @author Nils Reiter
 * @since 0.4
 *
 */
public interface IToken {
	
	/**
	 * Returns the range of the token. 
	 * @return The range
	 */
	Range getRange();
	
	/**
	 * Returns the surface of the token.
	 * @return A string
	 */
	String toString();
	
	/**
	 * Gives a reference to the sentence in which this
	 * token appears. 
	 * @return A sentence
	 */
	Sentence getSentence();
	
	/**
	 * Sets a property of the token. 
	 * @param key The key
	 * @param value The value
	 */
	void setProperty(String key, String value);
	
	/**
	 * Retrieves the property with the given key.
	 * @param key The key
	 * @return The value of the property.
	 */
	String getProperty(String key);
	
	/**
	 * Returns a collection of all keys defined in this token.
	 * @return A collection of strings
	 */
	Collection<String> getPropertyKeys();
}
