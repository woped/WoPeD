package edu.stanford.nlp.parser.lexparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.io.EncodingPrintWriter;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Numberer;

/**
 * This is a basic unknown word model for English.  It supports 5 different
 * types of feature modeling; see {@link #getSignature(String, int)}.
 *
 * <i>Implementation note: the contents of this class tend to overlap somewhat
 * with {@link ArabicUnknownWordModel} and were originally included in {@link BaseLexicon}.
 *
 * @author Dan Klein
 * @author Galen Andrew
 * @author Christopher Manning
 * @author Anna Rafferty
 */
public class EnglishUnknownWordModel extends BaseUnknownWordModel {

  private static final long serialVersionUID = 4825624957364628770L;

  private static final boolean DEBUG_UWM = false;

  protected boolean smartMutation = false;


  /**
   * We cache the last signature looked up, because it asks for the same one
   * many times when an unknown word is encountered! (Note that under the
   * current scheme, one unknown word, if seen sentence-initially and
   * non-initially, will be parsed with two different signatures....)
   */
  protected transient int lastSignatureIndex = -1;

  protected transient int lastSentencePosition = -1;

  protected transient int lastWordToSignaturize = -1;

  private static final boolean DOCUMENT_UNKNOWNS = false;

  protected int unknownSuffixSize = 0;
  protected int unknownPrefixSize = 0;

  private static final int MIN_UNKNOWN = 0;

  private static final int MAX_UNKNOWN = 7;

  public EnglishUnknownWordModel(Options.LexOptions op, Lexicon lex) {
    super(op, lex);
    unknownLevel = op.useUnknownWordSignatures;
    if (unknownLevel < MIN_UNKNOWN || unknownLevel > MAX_UNKNOWN) {
      System.err.println("Invalid value for useUnknownWordSignatures: " + unknownLevel);
      if (unknownLevel < MIN_UNKNOWN) {
        unknownLevel = MIN_UNKNOWN;
      } else if (unknownLevel > MAX_UNKNOWN) {
        unknownLevel = MAX_UNKNOWN;
      }
    }
    this.smartMutation = op.smartMutation;
    this.unknownSuffixSize = op.unknownSuffixSize;
    this.unknownPrefixSize = op.unknownPrefixSize;
  }

  /**
   * Trains this lexicon on the Collection of trees.
   */
  @Override
  public void train(Collection<Tree> trees) {
    train(trees, 1.0, false);
  }


  /**
   * Trains this lexicon on the Collection of trees.
   */
  public void train(Collection<Tree> trees, boolean keepTagsAsLabels) {
    train(trees, 1.0, keepTagsAsLabels);
  }

  public void train(Collection<Tree> trees, double weight) {
    train(trees, weight, false);
  }

  public void train(Collection<Tree> trees, double weight, boolean keepTagsAsLabels) {
    // Records the number of times word/tag pair was seen in training data.
    ClassicCounter<IntTaggedWord> seenCounter = new ClassicCounter<IntTaggedWord>();

    // scan data
    int tNum = 0;
    int tSize = trees.size();
    int indexToStartUnkCounting = (int) (tSize * Train.fractionBeforeUnseenCounting);
    Numberer wNumberer = wordNumberer();
    Numberer tNumberer = tagNumberer();
    if (DOCUMENT_UNKNOWNS) {
      System.err.println("Collecting " + Lexicon.UNKNOWN_WORD + " from trees " +
                         (indexToStartUnkCounting + 1) + " to " + tSize);
    }

    for (Tree tree : trees) {
      tNum++;
      List<IntTaggedWord> taggedWords = treeToEvents(tree, keepTagsAsLabels);
      for (int w = 0, sz = taggedWords.size(); w < sz; w++) {
        IntTaggedWord iTW = taggedWords.get(w);
        IntTaggedWord iT = new IntTaggedWord(nullWord, iTW.tag);
        IntTaggedWord iW = new IntTaggedWord(iTW.word, nullTag);
        seenCounter.incrementCount(iW, weight);
        IntTaggedWord i = new IntTaggedWord(nullWord, nullTag);

        if (tNum > indexToStartUnkCounting) {
          // start doing this once some way through trees; tNum is 1 based counting
          if (seenCounter.getCount(iW) < 2) {
            // it's an entirely unknown word
            int s = getSignatureIndex(iTW.word, w);
            if (DOCUMENT_UNKNOWNS) {
              String wStr = (String) wNumberer.object(iTW.word);
              String tStr = (String) tNumberer.object(iTW.tag);
              String sStr = (String) wNumberer.object(s);
              EncodingPrintWriter.err.println("Unknown word/tag/sig:\t" +
                                              wStr + '\t' + tStr + '\t' + sStr,
                                              "UTF-8");
            }
            IntTaggedWord iTS = new IntTaggedWord(s, iTW.tag);
            IntTaggedWord iS = new IntTaggedWord(s, nullTag);
            unSeenCounter.incrementCount(iTS, weight);
            unSeenCounter.incrementCount(iT, weight);
            unSeenCounter.incrementCount(iS, weight);
            unSeenCounter.incrementCount(i, weight);
            // rules.add(iTS);
            // sigs.add(iS);
          } // else {
          // if (seenCounter.getCount(iTW) < 2) {
          // it's a new tag for a known word
          // do nothing for now
          // }
          // }
        }
      }
    }
    // make sure the unseen counter isn't empty!  If it is, put in
    // a uniform unseen over tags
    if (unSeenCounter.isEmpty()) {
      int numTags = tagNumberer().total();
      for (int tt = 0; tt < numTags; tt++) {
        if ( ! Lexicon.BOUNDARY_TAG.equals(tagNumberer().object(tt))) {
          IntTaggedWord iT = new IntTaggedWord(nullWord, tt);
          IntTaggedWord i = new IntTaggedWord(nullWord, nullTag);
          unSeenCounter.incrementCount(iT, weight);
          unSeenCounter.incrementCount(i, weight);
        }
      }
    }
    // index the possible tags for each word
    // numWords = wNumberer.total();
    // unknownWordIndex = Numberer.number("words",Lexicon.UNKNOWN_WORD);
    // initRulesWithWord();
  }


  protected List<IntTaggedWord> treeToEvents(Tree tree, boolean keepTagsAsLabels) {
    if (!keepTagsAsLabels) { return treeToEvents(tree); }
    List<LabeledWord> labeledWords = tree.labeledYield();
//  for (LabeledWord tw : labeledWords) {
//  System.err.println(tw);
//  }
    return listOfLabeledWordsToEvents(labeledWords);
  }

  protected List<IntTaggedWord> treeToEvents(Tree tree) {
    List<TaggedWord> taggedWords = tree.taggedYield();
    return listToEvents(taggedWords);
  }

  protected List<IntTaggedWord> listToEvents(List<TaggedWord> taggedWords) {
    List<IntTaggedWord> itwList = new ArrayList<IntTaggedWord>();
    for (TaggedWord tw : taggedWords) {
      IntTaggedWord iTW = new IntTaggedWord(wordNumberer().number(tw.word()),
          tagNumberer().number(tw.tag()));
      itwList.add(iTW);
    }
    return itwList;
  }

  protected List<IntTaggedWord> listOfLabeledWordsToEvents(List<LabeledWord> taggedWords) {
    List<IntTaggedWord> itwList = new ArrayList<IntTaggedWord>();
    for (LabeledWord tw : taggedWords) {
      IntTaggedWord iTW = new IntTaggedWord(wordNumberer().number(tw.word()),
          tagNumberer().number(tw.tag()));
      itwList.add(iTW);
    }
    return itwList;
  }


  @Override
  public float score(IntTaggedWord iTW, int loc, double c_Tseen, double total, double smooth) {
    double pb_T_S = scoreProbTagGivenWordSignature(iTW, loc, smooth);
    double p_T = (c_Tseen / total);
    double p_W = 1.0 / total;
    double pb_W_T = Math.log(pb_T_S * p_W / p_T);

    if (pb_W_T > -100.0) {
      if (DEBUG_UWM) {
        System.err.println(iTW + " tagging has probability " + pb_W_T);
      }
      return (float) pb_W_T;
    }
    if (DEBUG_UWM) {
      System.err.println(iTW + " tagging is impossible.");
    }
    return Float.NEGATIVE_INFINITY;
  } // end score()


  /** Calculate P(Tag|Signature) with Bayesian smoothing via just P(Tag|Unknown) */
  public double scoreProbTagGivenWordSignature(IntTaggedWord iTW, int loc, double smooth) {
    int word = iTW.word;
    short tag = iTW.tag;

    // iTW.tag = nullTag;
    // double c_W = ((BaseLexicon) l).getCount(iTW);
    // iTW.tag = tag;

    // unknown word model for P(T|S)

    iTW.word = getSignatureIndex(iTW.word, loc);
    double c_TS = unSeenCounter.getCount(iTW);
    iTW.tag = nullTag;
    double c_S = unSeenCounter.getCount(iTW);
    iTW.word = nullWord;
    double c_U = unSeenCounter.getCount(iTW);
    iTW.tag = tag;
    double c_T = unSeenCounter.getCount(iTW);
    iTW.word = word;

    double p_T_U = c_T / c_U;
    if (unknownLevel == 0) {
      c_TS = 0;
      c_S = 0;
    }
    return (c_TS + smooth * p_T_U) / (c_S + smooth);
  }


  private transient Numberer tagNumberer;

  private Numberer tagNumberer() {
    if (tagNumberer == null) {
      tagNumberer = Numberer.getGlobalNumberer("tags");
    }
    return tagNumberer;
  }

  private transient Numberer wordNumberer;

  private Numberer wordNumberer() {
    if (wordNumberer == null) {
      wordNumberer = Numberer.getGlobalNumberer("words");
    }
    return wordNumberer;
  }

  /**
   * Returns the index of the signature of the word numbered wordIndex, where
   * the signature is the String representation of unknown word features.
   * Caches the last signature index returned.
   */
  // TODO: Move this out of UnknownWordModel interface; it seems like it should have just stayed a performance optimization in BaseLexicon
  @Override
  public int getSignatureIndex(int wordIndex, int sentencePosition) {
    if (wordIndex == lastWordToSignaturize && sentencePosition == lastSentencePosition) {
      if (DEBUG_UWM) {
        System.err.println("Signature: cache mapped " + wordIndex + " to " + lastSignatureIndex);
      }
      return lastSignatureIndex;
    } else {
      String uwSig = getSignature((String) wordNumberer().object(wordIndex), sentencePosition);
      int sig = wordNumberer().number(uwSig);
      if (DEBUG_UWM) {
        System.err.println("Signature (" + unknownLevel + "): mapped " + wordNumberer().object(wordIndex)
                           + " (" + wordIndex + ") to " + uwSig + " (" + sig + ")");
      }
      lastSignatureIndex = sig;
      lastSentencePosition = sentencePosition;
      lastWordToSignaturize = wordIndex;
      return sig;
    }
  }

  /**
   * This routine returns a String that is the "signature" of the class of a
   * word. For, example, it might represent whether it is a number of ends in
   * -s. The strings returned by convention matches the pattern UNK(-.+)? ,
   * which is just assumed to not match any real word. Behavior depends on the
   * unknownLevel (-uwm flag) passed in to the class. The recognized numbers are
   * 1-5: 5 is fairly English-specific; 4, 3, and 2 look for various word
   * features (digits, dashes, etc.) which are only vaguely English-specific; 1
   * uses the last two characters combined with a simple classification by
   * capitalization.
   *
   * @param word The word to make a signature for
   * @param loc Its position in the sentence (mainly so sentence-initial
   *          capitalized words can be treated differently)
   * @return A String that is its signature (equivalence class)
   */
  @Override
  public String getSignature(String word, int loc) {
    StringBuilder sb = new StringBuilder("UNK");
    switch (unknownLevel) {
      case 7:
        getSignature7(word, loc, sb);
        break;
      case 6:
        getSignature6(word, loc, sb);
        break;
      case 5:
        getSignature5(word, loc, sb);
        break;
      case 4:
        getSignature4(word, loc, sb);
        break;
      case 3:
        getSignature3(word, loc, sb);
        break;
      case 2:
        getSignature2(word, loc, sb);
        break;
      case 1:
        getSignature1(word, loc, sb);
        break;
      default:
        // 0 = do nothing so it just stays as "UNK"
    } // end switch (unknownLevel)
    // System.err.println("Summarized " + word + " to " + sb.toString());
    return sb.toString();
  } // end getSignature()


  @SuppressWarnings({"MethodMayBeStatic"})
  private void getSignature7(String word, int loc, StringBuilder sb) {
    // New Sep 2008. Like 2 but rely more on Caps somewhere than initial Caps
    // {-ALLC, -INIT, -UC somewhere, -LC, zero} +
    // {-DASH, zero} +
    // {-NUM, -DIG, zero} +
    // {lowerLastChar, zeroIfShort}
    boolean hasDigit = false;
    boolean hasNonDigit = false;
    boolean hasLower = false;
    boolean hasUpper = false;
    boolean hasDash = false;
    int wlen = word.length();
    for (int i = 0; i < wlen; i++) {
      char ch = word.charAt(i);
      if (Character.isDigit(ch)) {
        hasDigit = true;
      } else {
        hasNonDigit = true;
        if (Character.isLetter(ch)) {
          if (Character.isLowerCase(ch) || Character.isTitleCase(ch)) {
            hasLower = true;
          } else {
            hasUpper = true;
          }
        } else if (ch == '-') {
          hasDash = true;
        }
      }
    }
    if (wlen > 0 && hasUpper) {
      if ( ! hasLower) {
        sb.append("-ALLC");
      } else if (loc == 0) {
        sb.append("-INIT");
      } else {
        sb.append("-UC");
      }
    } else if (hasLower) { // if (Character.isLowerCase(word.charAt(0))) {
      sb.append("-LC");
    }
    // no suffix = no (lowercase) letters
    if (hasDash) {
      sb.append("-DASH");
    }
    if (hasDigit) {
      if (!hasNonDigit) {
        sb.append("-NUM");
      } else {
        sb.append("-DIG");
      }
    } else if (wlen > 3) {
      // don't do for very short words: "yes" isn't an "-es" word
      // try doing to lower for further densening and skipping digits
      char ch = word.charAt(word.length() - 1);
      sb.append(Character.toLowerCase(ch));
    }
    // no suffix = short non-number, non-alphabetic
  }


  private void getSignature6(String word, int loc, StringBuilder sb) {
    // New Sep 2008. Like 5 but rely more on Caps somewhere than initial Caps
    // { -INITC, -CAPS, (has) -CAP, -LC lowercase, 0 } +
    // { -KNOWNLC, 0 } + [only for INITC]
    // { -NUM, 0 } +
    // { -DASH, 0 } +
    // { -last lowered char(s) if known discriminating suffix, 0}
    int wlen = word.length();
    int numCaps = 0;
    boolean hasDigit = false;
    boolean hasDash = false;
    boolean hasLower = false;
    for (int i = 0; i < wlen; i++) {
      char ch = word.charAt(i);
      if (Character.isDigit(ch)) {
        hasDigit = true;
      } else if (ch == '-') {
        hasDash = true;
      } else if (Character.isLetter(ch)) {
        if (Character.isLowerCase(ch)) {
          hasLower = true;
        } else if (Character.isTitleCase(ch)) {
          hasLower = true;
          numCaps++;
        } else {
          numCaps++;
        }
      }
    }
    String lowered = word.toLowerCase();
    if (numCaps > 1) {
      sb.append("-CAPS");
    } else if (numCaps > 0) {
      if (loc == 0) {
        sb.append("-INITC");
        if (getLexicon().isKnown(lowered)) {
          sb.append("-KNOWNLC");
        }
      } else {
        sb.append("-CAP");
      }
    } else if (hasLower) { // (Character.isLowerCase(ch0)) {
      sb.append("-LC");
    }
    if (hasDigit) {
      sb.append("-NUM");
    }
    if (hasDash) {
      sb.append("-DASH");
    }
    if (lowered.endsWith("s") && wlen >= 3) {
      // here length 3, so you don't miss out on ones like 80s
      char ch2 = lowered.charAt(wlen - 2);
      // not -ess suffixes or greek/latin -us, -is
      if (ch2 != 's' && ch2 != 'i' && ch2 != 'u') {
        sb.append("-s");
      }
    } else if (word.length() >= 5 && !hasDash && !(hasDigit && numCaps > 0)) {
      // don't do for very short words;
      // Implement common discriminating suffixes
      if (lowered.endsWith("ed")) {
        sb.append("-ed");
      } else if (lowered.endsWith("ing")) {
        sb.append("-ing");
      } else if (lowered.endsWith("ion")) {
        sb.append("-ion");
      } else if (lowered.endsWith("er")) {
        sb.append("-er");
      } else if (lowered.endsWith("est")) {
        sb.append("-est");
      } else if (lowered.endsWith("ly")) {
        sb.append("-ly");
      } else if (lowered.endsWith("ity")) {
        sb.append("-ity");
      } else if (lowered.endsWith("y")) {
        sb.append("-y");
      } else if (lowered.endsWith("al")) {
        sb.append("-al");
        // } else if (lowered.endsWith("ble")) {
        // sb.append("-ble");
        // } else if (lowered.endsWith("e")) {
        // sb.append("-e");
      }
    }
  }


  private void getSignature5(String word, int loc, StringBuilder sb) {
    // Reformed Mar 2004 (cdm); hopefully better now.
    // { -CAPS, -INITC ap, -LC lowercase, 0 } +
    // { -KNOWNLC, 0 } + [only for INITC]
    // { -NUM, 0 } +
    // { -DASH, 0 } +
    // { -last lowered char(s) if known discriminating suffix, 0}
    int wlen = word.length();
    int numCaps = 0;
    boolean hasDigit = false;
    boolean hasDash = false;
    boolean hasLower = false;
    for (int i = 0; i < wlen; i++) {
      char ch = word.charAt(i);
      if (Character.isDigit(ch)) {
        hasDigit = true;
      } else if (ch == '-') {
        hasDash = true;
      } else if (Character.isLetter(ch)) {
        if (Character.isLowerCase(ch)) {
          hasLower = true;
        } else if (Character.isTitleCase(ch)) {
          hasLower = true;
          numCaps++;
        } else {
          numCaps++;
        }
      }
    }
    char ch0 = word.charAt(0);
    String lowered = word.toLowerCase();
    if (Character.isUpperCase(ch0) || Character.isTitleCase(ch0)) {
      if (loc == 0 && numCaps == 1) {
        sb.append("-INITC");
        if (getLexicon().isKnown(lowered)) {
          sb.append("-KNOWNLC");
        }
      } else {
        sb.append("-CAPS");
      }
    } else if (!Character.isLetter(ch0) && numCaps > 0) {
      sb.append("-CAPS");
    } else if (hasLower) { // (Character.isLowerCase(ch0)) {
      sb.append("-LC");
    }
    if (hasDigit) {
      sb.append("-NUM");
    }
    if (hasDash) {
      sb.append("-DASH");
    }
    if (lowered.endsWith("s") && wlen >= 3) {
      // here length 3, so you don't miss out on ones like 80s
      char ch2 = lowered.charAt(wlen - 2);
      // not -ess suffixes or greek/latin -us, -is
      if (ch2 != 's' && ch2 != 'i' && ch2 != 'u') {
        sb.append("-s");
      }
    } else if (word.length() >= 5 && !hasDash && !(hasDigit && numCaps > 0)) {
      // don't do for very short words;
      // Implement common discriminating suffixes
      if (lowered.endsWith("ed")) {
        sb.append("-ed");
      } else if (lowered.endsWith("ing")) {
        sb.append("-ing");
      } else if (lowered.endsWith("ion")) {
        sb.append("-ion");
      } else if (lowered.endsWith("er")) {
        sb.append("-er");
      } else if (lowered.endsWith("est")) {
        sb.append("-est");
      } else if (lowered.endsWith("ly")) {
        sb.append("-ly");
      } else if (lowered.endsWith("ity")) {
        sb.append("-ity");
      } else if (lowered.endsWith("y")) {
        sb.append("-y");
      } else if (lowered.endsWith("al")) {
        sb.append("-al");
        // } else if (lowered.endsWith("ble")) {
        // sb.append("-ble");
        // } else if (lowered.endsWith("e")) {
        // sb.append("-e");
      }
    }
  }


  @SuppressWarnings({"MethodMayBeStatic"})
  private void getSignature4(String word, int loc, StringBuilder sb) {
    boolean hasDigit = false;
    boolean hasNonDigit = false;
    boolean hasLetter = false;
    boolean hasLower = false;
    boolean hasDash = false;
    boolean hasPeriod = false;
    boolean hasComma = false;
    for (int i = 0; i < word.length(); i++) {
      char ch = word.charAt(i);
      if (Character.isDigit(ch)) {
        hasDigit = true;
      } else {
        hasNonDigit = true;
        if (Character.isLetter(ch)) {
          hasLetter = true;
          if (Character.isLowerCase(ch) || Character.isTitleCase(ch)) {
            hasLower = true;
          }
        } else {
          if (ch == '-') {
            hasDash = true;
          } else if (ch == '.') {
            hasPeriod = true;
          } else if (ch == ',') {
            hasComma = true;
          }
        }
      }
    }
    // 6 way on letters
    if (Character.isUpperCase(word.charAt(0)) || Character.isTitleCase(word.charAt(0))) {
      if (!hasLower) {
        sb.append("-AC");
      } else if (loc == 0) {
        sb.append("-SC");
      } else {
        sb.append("-C");
      }
    } else if (hasLower) {
      sb.append("-L");
    } else if (hasLetter) {
      sb.append("-U");
    } else {
      // no letter
      sb.append("-S");
    }
    // 3 way on number
    if (hasDigit && !hasNonDigit) {
      sb.append("-N");
    } else if (hasDigit) {
      sb.append("-n");
    }
    // binary on period, dash, comma
    if (hasDash) {
      sb.append("-H");
    }
    if (hasPeriod) {
      sb.append("-P");
    }
    if (hasComma) {
      sb.append("-C");
    }
    if (word.length() > 3) {
      // don't do for very short words: "yes" isn't an "-es" word
      // try doing to lower for further densening and skipping digits
      char ch = word.charAt(word.length() - 1);
      if (Character.isLetter(ch)) {
        sb.append('-');
        sb.append(Character.toLowerCase(ch));
      }
    }
  }


  @SuppressWarnings({"MethodMayBeStatic"})
  private void getSignature3(String word, int loc, StringBuilder sb) {
    // This basically works right, except note that 'S' is applied to all
    // capitalized letters in first word of sentence, not just first....
    sb.append('-');
    char lastClass = '-'; // i.e., nothing
    int num = 0;
    for (int i = 0; i < word.length(); i++) {
      char ch = word.charAt(i);
      char newClass;
      if (Character.isUpperCase(ch) || Character.isTitleCase(ch)) {
        if (loc == 0) {
          newClass = 'S';
        } else {
          newClass = 'L';
        }
      } else if (Character.isLetter(ch)) {
        newClass = 'l';
      } else if (Character.isDigit(ch)) {
        newClass = 'd';
      } else if (ch == '-') {
        newClass = 'h';
      } else if (ch == '.') {
        newClass = 'p';
      } else {
        newClass = 's';
      }
      if (newClass != lastClass) {
        lastClass = newClass;
        sb.append(lastClass);
        num = 1;
      } else {
        if (num < 2) {
          sb.append('+');
        }
        num++;
      }
    }
    if (word.length() > 3) {
      // don't do for very short words: "yes" isn't an "-es" word
      // try doing to lower for further densening and skipping digits
      char ch = Character.toLowerCase(word.charAt(word.length() - 1));
      sb.append('-');
      sb.append(ch);
    }
  }


  @SuppressWarnings({"MethodMayBeStatic"})
  private void getSignature2(String word, int loc, StringBuilder sb) {
    // {-ALLC, -INIT, -UC, -LC, zero} +
    // {-DASH, zero} +
    // {-NUM, -DIG, zero} +
    // {lowerLastChar, zeroIfShort}
    boolean hasDigit = false;
    boolean hasNonDigit = false;
    boolean hasLower = false;
    int wlen = word.length();
    for (int i = 0; i < wlen; i++) {
      char ch = word.charAt(i);
      if (Character.isDigit(ch)) {
        hasDigit = true;
      } else {
        hasNonDigit = true;
        if (Character.isLetter(ch)) {
          if (Character.isLowerCase(ch) || Character.isTitleCase(ch)) {
            hasLower = true;
          }
        }
      }
    }
    if (wlen > 0
            && (Character.isUpperCase(word.charAt(0)) || Character.isTitleCase(word.charAt(0)))) {
      if (!hasLower) {
        sb.append("-ALLC");
      } else if (loc == 0) {
        sb.append("-INIT");
      } else {
        sb.append("-UC");
      }
    } else if (hasLower) { // if (Character.isLowerCase(word.charAt(0))) {
      sb.append("-LC");
    }
    // no suffix = no (lowercase) letters
    if (word.indexOf('-') >= 0) {
      sb.append("-DASH");
    }
    if (hasDigit) {
      if (!hasNonDigit) {
        sb.append("-NUM");
      } else {
        sb.append("-DIG");
      }
    } else if (wlen > 3) {
      // don't do for very short words: "yes" isn't an "-es" word
      // try doing to lower for further densening and skipping digits
      char ch = word.charAt(word.length() - 1);
      sb.append(Character.toLowerCase(ch));
    }
    // no suffix = short non-number, non-alphabetic
  }


  @SuppressWarnings({"MethodMayBeStatic"})
  private void getSignature1(String word, int loc, StringBuilder sb) {
    sb.append('-');
    sb.append(word.substring(Math.max(word.length() - 2, 0), word.length()));
    sb.append('-');
    if (Character.isLowerCase(word.charAt(0))) {
      sb.append("LOWER");
    } else {
      if (Character.isUpperCase(word.charAt(0))) {
        if (loc == 0) {
          sb.append("INIT");
        } else {
          sb.append("UPPER");
        }
      } else {
        sb.append("OTHER");
      }
    }
  }

}
