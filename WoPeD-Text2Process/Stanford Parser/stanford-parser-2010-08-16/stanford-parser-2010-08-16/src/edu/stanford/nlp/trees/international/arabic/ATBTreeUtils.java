package edu.stanford.nlp.trees.international.arabic;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeFactory;
import edu.stanford.nlp.trees.international.arabic.ArabicTreeNormalizer.ArabicEmptyFilter;
import edu.stanford.nlp.util.Filter;

/**
 * Various static convenience methods for processing Arabic parse trees.
 *
 * @author Spence Green
 *
 */
public class ATBTreeUtils {

  private static final Filter<Tree> emptyFilter = new ArabicEmptyFilter();
  private static final TreeFactory tf = new LabeledScoredTreeFactory();

  private ATBTreeUtils() {}  // static class

  /**
   * Escapes tokens from flat strings that are reserved for usage in the ATB.
   *
   * @param s - An Arabic string
   * @return A string with all reserved words replaced by the appropriate tokens
   */
  public static String escape(String s) {
    if(s == null) return null;

    //LDC escape sequences (as of ATB3p3)
    s = s.replaceAll("\\(", "-LRB-");
    s = s.replaceAll("\\)", "-RRB-");
    s = s.replaceAll("\\+", "-PLUS-");

    return s;
  }

  /**
   * Reverts escaping from a flat string.
   *
   * @param s - An Arabic string
   * @return A string with all reserved words inserted into the appropriate locations
   */
  public static String unEscape(String s) {
    if(s == null) return null;

    //LDC escape sequences (as of ATB3p3)
    s = s.replaceAll("-LRB-", "(");
    s = s.replaceAll("-RRB-", ")");
    s = s.replaceAll("-PLUS-", "+");

    return s;
  }

  /**
   * Returns the string associated with the input parse tree. Traces and
   * ATB-specific escape sequences (e.g., "-RRB-" for ")") are removed.
   *
   * @param t - A parse tree
   * @return The yield of the input parse tree
   */
  public static String flattenTree(Tree t) {
    t = t.prune(emptyFilter, tf);

    String flatString = t.yield().toString();

    return flatString;
  }
  
  /**
   * Converts a parse tree into a string of tokens. Each token is a word and
   * its POS tag separated by the delimiter specified by
   * {@link edu.stanford.nlp.ling.TaggedWord#setDivider(String)}
   *
   * @param t - A parse tree
   * @param removeEscaping - If true, remove LDC escape characters. Otherwise, leave them.
   * @return A string of tagged words
   */
  public static String taggedStringFromTree(Tree t, boolean removeEscaping) {
    t = t.prune(emptyFilter, tf);

    Sentence<TaggedWord> taggedSentence = t.taggedYield();
    for(TaggedWord token : taggedSentence)
      token.setWord((removeEscaping) ? unEscape(token.word()) : token.word());

    return taggedSentence.toString(false);
  }

  public static void main(String[] args) {
    String debug = "( the big lion ) + (the small rabbit)";
    String escaped = ATBTreeUtils.escape(debug);
    System.out.println(escaped);
  }

}
