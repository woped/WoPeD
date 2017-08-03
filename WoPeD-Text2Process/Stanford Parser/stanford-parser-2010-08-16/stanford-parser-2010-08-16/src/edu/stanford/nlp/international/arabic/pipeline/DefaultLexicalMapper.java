package edu.stanford.nlp.international.arabic.pipeline;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.regex.*;

/**
 * Applies a default set of lexical transformations that have been empirically validated
 * in various Arabic tasks. This class automatically detects the input encoding and applies
 * the appropriate set of transformations.
 *
 * @author Spence Green
 *
 */
public class DefaultLexicalMapper implements Mapper, Serializable {

  private static final long serialVersionUID = -197782849766133026L;

  private static final Pattern utf8ArabicChart = Pattern.compile("[\u0600-\u06FF]");

  //Buckwalter patterns
  private static final String bwAlefChar = "A"; //U+0627
  private static final Pattern bwDiacritics = Pattern.compile("F|N|K|a|u|i|\\~|o");
  private static final Pattern bwTatweel = Pattern.compile("_");
  private static final Pattern bwAlef = Pattern.compile("\\{|\\||>|<");
  private static final Pattern bwQuran = Pattern.compile("`");

  private static final Pattern latinPunc = Pattern.compile("[\u0021-\u002F\u003A-\u0040\\u005B\u005C\\u005D\u005E-\u0060\u007B-\u007E\u00A1-\u00BF\u2010-\u2027\u2030-\u205E\u20A0-\u20B5]+");
  private static final Pattern arabicPunc = Pattern.compile("[\u0609-\u060D\u061B-\u061F\u066A\u066C-\u066D\u06D4]+");

  //TODO Extend coverage to entire Arabic code chart
  //Obviously Buckwalter is a lossful conversion, but no assumptions should be made about
  //UTF-8 input from "the wild"
  private static final Pattern utf8Diacritics = Pattern.compile("َ|ً|ُ|ٌ|ِ|ٍ|ّ|ْ");
  private static final Pattern utf8Tatweel = Pattern.compile("ـ");
  private static final Pattern utf8Alef = Pattern.compile("ا|إ|أ|آ|\u0671");
  private static final Pattern utf8Quran = Pattern.compile("[\u0615-\u061A\u06D6-\u06E5]");

  //Patterns to fix segmentation issues observed in the ATB
  private static final Pattern cliticMarker = Pattern.compile("^-|-$");

  // reserved symbols like -LRB-, -PLUS-
  private static final Pattern reservedSymbol = Pattern.compile("-[A-Z]+-");

  private static final Pattern hasNum = Pattern.compile("\\d+");
  private final Set<String> parentTagsToEscape;

  public DefaultLexicalMapper() {

    //Tags for the canChangeEncoding() method
    parentTagsToEscape = new HashSet<String>();
    parentTagsToEscape.add("PUNC");
    parentTagsToEscape.add("LATIN");
    parentTagsToEscape.add("-NONE-");
  }

  private static String mapUtf8(String element) {
    Matcher latinPuncOnly = latinPunc.matcher(element);
    Matcher arbPuncOnly = arabicPunc.matcher(element);
    if(latinPuncOnly.matches() || arbPuncOnly.matches()) return element;

    //Remove diacritics
    Matcher rmDiacritics = utf8Diacritics.matcher(element);
    element = rmDiacritics.replaceAll("");

    if(element.length() > 1) {
      Matcher rmTatweel = utf8Tatweel.matcher(element);
      element = rmTatweel.replaceAll("");
    }

    //Normalize alef
    Matcher normAlef = utf8Alef.matcher(element);
    element = normAlef.replaceAll("ا");

    //Remove characters that only appear in the Qur'an
    Matcher rmQuran = utf8Quran.matcher(element);
    element = rmQuran.replaceAll("");

    if(element.length() > 1) {
      Matcher rmCliticMarker = cliticMarker.matcher(element);
      element = rmCliticMarker.replaceAll("");
    }

    return element;
  }

  private static String mapBuckwalter(String element) {
    Matcher puncOnly = latinPunc.matcher(element);
    if(puncOnly.matches()) return element;

    //Remove diacritics
    Matcher rmDiacritics = bwDiacritics.matcher(element);
    element = rmDiacritics.replaceAll("");

    //Remove tatweel
    if(element.length() > 1) {
      Matcher rmTatweel = bwTatweel.matcher(element);
      element = rmTatweel.replaceAll("");
    }

    //Normalize alef
    Matcher normAlef = bwAlef.matcher(element);
    element = normAlef.replaceAll(bwAlefChar);

    //Remove characters that only appear in the Qur'an
    Matcher rmQuran = bwQuran.matcher(element);
    element = rmQuran.replaceAll("");

    // cdm change Nov 2009: don't let this clobber reserved symbols like -LRB- etc.
    if (element.length() > 1 && ! reservedSymbol.matcher(element).matches()) {
      Matcher rmCliticMarker = cliticMarker.matcher(element);
      element = rmCliticMarker.replaceAll("");
    }

    return element;
  }

  public String map(String parent, String element) {
    String elem = element.trim();

    if(parentTagsToEscape.contains(parent))
      return elem;

    Matcher utf8Encoding = utf8ArabicChart.matcher(elem);
    return (utf8Encoding.find()) ? mapUtf8(elem) : mapBuckwalter(elem);
  }

  public void setup(File path) {}

  //Whether or not the encoding of this word can be converted to another encoding
  //from its current encoding (Buckwalter or UTF-8)
  public boolean canChangeEncoding(String parent, String element) {
    parent = parent.trim();
    element = element.trim();

    //Hack for LDC2008E22 idiosyncrasy
    //This is NUMERIC_COMMA in the raw trees. We allow conversion of this
    //token to UTF-8 since it would appear in this encoding in arbitrary
    //UTF-8 text input
    if(parent.contains("NUMERIC_COMMA") || (parent.contains("PUNC") && element.equals("r"))) //Numeric comma
      return true;

    Matcher numMatcher = hasNum.matcher(element);
    return !(numMatcher.find() || parentTagsToEscape.contains(parent));
  }

  public static void main(String[] args) {
    Mapper m = new DefaultLexicalMapper();

    System.out.printf("< :-> %s\n",m.map(null, "FNKqq"));
  }
}
