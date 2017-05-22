package edu.stanford.nlp.parser.lexparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import edu.stanford.nlp.trees.Tree;


/**
 * An interface for lexicons interfacing to lexparser.  Its primary
 * responsibility is to provide a conditional probability
 * P(word|tag), which is fulfilled by the {#score} method.
 * Inside the lexparser,
 * Strings are interned and tags and words are usually represented as integers.
 *
 * @author Galen Andrew
 */
public interface Lexicon extends Serializable {

  String UNKNOWN_WORD = "UNK";  // if UNK were a word, counts would merge
  String BOUNDARY = ".$.";      // boundary word -- assumed not a real word
  String BOUNDARY_TAG = ".$$."; // boundary tag -- assumed not a real tag


  /**
   * Set the model via which unknown words should be scored by this lexicon
   */
  //void setUnknownWordModel(UnknownWordModel uwModel);

  /**
   * Returns the number of times this word/tag pair has been seen;
   * 0 returned if never previously seen
   */
//  double getCount(IntTaggedWord w);

  /**
   * Checks whether a word is in the lexicon.
   *
   * @param word The word as an int
   * @return Whether the word is in the lexicon
   */
  boolean isKnown(int word);

  /**
   * Checks whether a word is in the lexicon.
   *
   * @param word The word as a String
   * @return Whether the word is in the lexicon
   */
  boolean isKnown(String word);

  /**
   * Get an iterator over all rules (pairs of (word, POS)) for this word.
   *
   *  @param word The word, represented as an integer in Numberer
   *  @param loc  The position of the word in the sentence (counting from 0).
   *                <i>Implementation note: The BaseLexicon class doesn't
   *                actually make use of this position information.</i>
   *  @return An Iterator over a List ofIntTaggedWords, which pair the word
   *                with possible taggings as integer pairs.  (Each can be
   *                thought of as a <code>tag -&gt; word<code> rule.)
   */
  Iterator<IntTaggedWord> ruleIteratorByWord(int word, int loc);

  /** Returns the number of rules (tag rewrites as word) in the Lexicon.
   *  This method assumes that the lexicon has been initialized.
   *
   * @return The number of rules (tag rewrites as word) in the Lexicon.
   */
  public int numRules();

  /**
   * Trains this lexicon on the Collection of trees.
   *
   * @param trees Trees to train on
   */
  void train(Collection<Tree> trees);

  /**
   * Add additional words with expansion of subcategories.
   */
  // void trainWithExpansion(Collection<TaggedWord> taggedWords);

  /**
   * Get the score of this word with this tag (as an IntTaggedWord) at this
   * loc.
   * (Presumably an estimate of P(word | tag).)
   *
   * @param iTW An IntTaggedWord pairing a word and POS tag
   * @param loc The position in the sentence.  <i>In the default implementation
   *               this is used only for unknown words to change their
   *               probability distribution when sentence initial.</i>
   * @return A score, usually, log P(word|tag)
   */
  float score(IntTaggedWord iTW, int loc);

  /**
   * Write the lexicon in human-readable format to the Writer.
   * (An optional operation.)
   *
   * @param w The writer to output to
   * @throws IOException If any I/O problem
   */
  public void writeData(Writer w) throws IOException;

  /**
   * Read the lexicon from the BufferedReader in the format written by
   * writeData.
   * (An optional operation.)
   *
   * @param in The BufferedReader to read from
   * @throws IOException If any I/O problem
   */
  public void readData(BufferedReader in) throws IOException;

  public UnknownWordModel getUnknownWordModel();

  public void setUnknownWordModel(UnknownWordModel uwm);

}
