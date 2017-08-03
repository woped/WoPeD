package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.util.Numberer;
import edu.stanford.nlp.util.StringUtils;

import java.io.Serializable;

/** Represents a WordTag (in the sense that equality is defined
 *  on both components), where each half is represented by an
 *  int indexed by a Numberer.  In this representation, -1 is
 *  used to represent the wildcard ANY value, and -2 is used
 *  to represent a STOP value (i.e., no more dependents).
 *
 *  @author Dan Klein
 *  @author Christopher Manning
 */
public class IntTaggedWord implements Serializable, Comparable<IntTaggedWord> {

  public static final int ANY_WORD_INT = -1;
  public static final int ANY_TAG_INT = -1;
  public static final int STOP_WORD_INT = -2;
  public static final int STOP_TAG_INT = -2;

  public static final String ANY = ".*.";
  public static final String STOP = "STOP";

  private static Numberer wordNumberer;
  private static Numberer tagNumberer;


  static Numberer wordNumberer() {
    if (wordNumberer == null) {
      wordNumberer = Numberer.getGlobalNumberer("words");
    }
    return wordNumberer;
  }

  static Numberer tagNumberer() {
    if (tagNumberer == null) {
      tagNumberer = Numberer.getGlobalNumberer("tags");
    }
    return tagNumberer;
  }

  public static void setWordNumberer(Numberer wordNumberer) {
    IntTaggedWord.wordNumberer = wordNumberer;
  }

  public static void setTagNumberer(Numberer tagNumberer) {
    IntTaggedWord.tagNumberer = tagNumberer;
  }

  static void resetNumberers() {
    wordNumberer = null;
    tagNumberer = null;
  }


  public int word;
  public short tag;

  public int tag() {
    return tag;
  }

  public int word() {
    return word;
  }

  public String wordString() {
    String wordStr;
    if (word >= 0) {
      wordStr = (String) wordNumberer().object(word);
    } else if (word == ANY_WORD_INT) {
      wordStr = ANY;
    } else {
      wordStr = STOP;
    }
    return wordStr;
  }

  public String tagString() {
    String tagStr;
    if (tag >= 0) {
      tagStr = tagNumberer().object(tag).toString();
    } else if (tag == ANY_TAG_INT) {
      tagStr = ANY;
    } else {
      tagStr = STOP;
    }
    return tagStr;
  }

  public Label tagLabel() {
    return (Label)(tagNumberer().object(tag));
  }

  @Override
  public int hashCode() {
    return word ^ (tag << 16);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o instanceof IntTaggedWord) {
      IntTaggedWord i = (IntTaggedWord) o;
      return (word == i.word && tag == i.tag);
    } else {
      return false;
    }
  }

  public int compareTo(IntTaggedWord that) {
    if (tag != that.tag) {
      return tag - that.tag;
    } else {
      return word - that.word;
    }
  }

  private static final char[] charsToEscape = new char[]{'\"'};


  public String toLexicalEntry() {
    String wordStr = wordString();
    String tagStr = tagString();
    return '\"' + StringUtils.escapeString(tagStr, charsToEscape, '\\') + "\" -> \"" + StringUtils.escapeString(wordStr, charsToEscape, '\\') + '\"';
  }

  @Override
  public String toString() {
    return wordString()+ '/' +tagString();
  }

  public String toString(String arg) {
    if (arg.equals("verbose")) {
      return wordString() + '[' + word + "]/" + tagString() + '[' + tag + ']';
    } else {
      return toString();
    }
  }

  public IntTaggedWord(int word, int tag) {
    this.word = word;
    this.tag = (short) tag;
  }

  public TaggedWord toTaggedWord() {
    String wordStr = wordString();
    String tagStr = tagString();
    return new TaggedWord(wordStr, tagStr);
  }

  /**
   * Creates an IntTaggedWord given by the String representation
   * of the form &lt;word&gt;|&lt;tag*gt;
   */
  public IntTaggedWord(String s, char splitChar) {
    // awkward, calls s.indexOf(splitChar) twice
    this(extractWord(s, splitChar), extractTag(s, splitChar));
    //    System.out.println("s: " + s);
    //    System.out.println("tagNumberer: " + tagNumberer);
    //    System.out.println("word: " + word);
    //    System.out.println("tag: " + tag);
  }

  private static String extractWord(String s, char splitChar) {
    int n = s.lastIndexOf(splitChar);
    String result = s.substring(0, n);
    //    System.out.println("extracted word: " + result);
    return result;
  }

  private static String extractTag(String s, char splitChar) {
    int n = s.lastIndexOf(splitChar);
    String result = s.substring(n + 1);
    //    System.out.println("extracted tag: " + result);
    return result;
  }

  /**
   * Creates an IntTaggedWord given by the tagString and wordString
   */
  public IntTaggedWord(String wordString, String tagString) {
    if (wordString.equals(ANY)) {
      word = ANY_WORD_INT;
    } else if (wordString.equals(STOP)) {
      word = STOP_WORD_INT;
    } else {
      word = wordNumberer().number(wordString);
    }
    if (tagString.equals(ANY)) {
      tag = (short) ANY_TAG_INT;
    } else if (tagString.equals(STOP)) {
      tag = (short) STOP_TAG_INT;
    } else {
      tag = (short) tagNumberer().number(tagString);
    }
  }

  private static final long serialVersionUID = 1L;

} // end class IntTaggedWord
