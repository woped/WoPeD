package edu.stanford.nlp.international.arabic.pipeline;

import java.io.File;

/**
 * Generic interface for mapping one string to another given some contextual evidence.
 * 
 * @author Spence Green
 *
 */
public interface Mapper {
  
  /**
   * Perform initialization prior to the first call to <code>map</code>.
   * 
   * @param path A filename for data on disk used during mapping
   */
  public void setup(File path);
  
  /**
   * Maps from one string representation to another.
   * 
   * @param parent <code>element</code>'s context (e.g., the parent node in a parse tree)
   * @param element The string to be transformed.
   * @return The transformed string
   */
  public String map(String parent, String element);
  
  /**
   * Indicates whether <code>child</code> can be converted to another encoding. In the ATB, for example, 
   * if a punctuation character is labeled with the "PUNC" POS tag, then that character should not
   * be converted from Buckwalter to UTF-8.
   * 
   * @param parent <code>element</code>'s context (e.g., the parent node in a parse tree)
   * @param child The string to be transformed.
   * @return True if the string encoding can be changed. False otherwise.
   */
  public boolean canChangeEncoding(String parent, String child);
}
