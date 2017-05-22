package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.ling.CategoryWordTag;

import java.util.Set;
import java.io.PrintWriter;

/**
 * Non-language-specific options for training a grammar from a treebank.
 * These options are not used at parsing time.
 * But they are all static so it isn't possible to train multiple parsers
 * in multiple threads at present with different options, until this is
 * changed.
 *
 * @author Dan Klein
 * @author Christopher Manning
 */
public class Train {

  public static String trainTreeFile = null; // same for me -- Teg

  /* THESE OPTIONS AFFECT ONLY TRAIN TIME */

  private Train() {}

  /**
   * if true, leave all PTB (functional tag) annotations (bad)
   */
  public static int leaveItAll = 0;

  /** Add all test set trees to training data for PCFG.
   *  (Currently only supported in FactoredParser main.)
   */
  public static boolean cheatPCFG = false;

  public static boolean markovFactor = false;
  public static int markovOrder = 1;
  public static boolean hSelSplit = false; // good with true;
  public static int HSEL_CUT = 10;

  /** Whether or not to mark final states in binarized grammar.
   *  This must be off to get most value out of grammar compaction.
   */
  public static boolean markFinalStates = true;

  /**
   * A POS tag has to have been attributed to more than this number of word
   * types before it is regarded as an open-class tag.  Unknown words will
   * only possibly be tagged as open-class tags (unless flexiTag is on).
   * If flexiTag is on, unknown words will be able to be tagged any POS for
   * which the unseenMap has nonzero count (that is, the tag was seen for
   * a new word after unseen signature counting was started).
   */
  public static int openClassTypesThreshold = 50;

  /**
   * Start to aggregate signature-tag pairs only for words unseen in the first
   * this fraction of the data.
   */
  public static double fractionBeforeUnseenCounting = 0.5;

  /**
   * If true, declare early -- leave this on except maybe with markov on.
   * @return Whether to do outside factorization in binarization of the grammar
   */
  public static boolean outsideFactor() {
    return !markovFactor;
  }

  /**
   * This variable controls doing parent annotation of phrasal nodes.  Good.
   */
  public static boolean PA = true;
  /**
   * This variable controls doing 2 levels of parent annotation.  Bad.
   */
  public static boolean gPA = false;

  public static boolean postPA = false;
  public static boolean postGPA = false;

  /**
   * Only split the "common high KL divergence" parent categories.... Good.
   */
  public static boolean selectiveSplit = false; //true;

  public static double selectiveSplitCutOff = 0.0;

  public static boolean selectivePostSplit = false;

  public static double selectivePostSplitCutOff = 0.0;

  /** Whether, in post-splitting of categories, nodes are annotated with the
   *  (grand)parent's base category or with its complete subcategorized
   *  category.
   */
  public static boolean postSplitWithBaseCategory = false;

  /**
   * Selective Sister annotation.
   */
  public static boolean sisterAnnotate = false;

  public static Set<String> sisterSplitters;

  /**
   * Mark all unary nodes specially.  Good for just PCFG. Bad for factored.
   * markUnary affects phrasal nodes. A value of 0 means to do nothing;
   * a value of 1 means to mark the parent (higher) node of a unary rewrite.
   * A value of 2 means to mark the child (lower) node of a unary rewrie.
   * Values of 1 and 2 only apply if the child (lower) node is phrasal.
   * (A value of 1 is better than 2 in combos.)  A value of 1 corresponds
   * to the old boolean -unary flag.
   */
  public static int markUnary = 0;

  /** Mark POS tags which are the sole member of their phrasal constituent.
   *  This is like markUnary=2, applied to POS tags.
   */
  public static boolean markUnaryTags = false;


  /**
   * Mark all pre-preterminals (also does splitBaseNP: don't need both)
   */
  public static boolean splitPrePreT = false;


  /**
   * Parent annotation on tags.  Good (for PCFG?)
   */
  public static boolean tagPA = false;//true;

  /**
   * Do parent annotation on tags selectively.  Neutral, but less splits.
   */
  public static boolean tagSelectiveSplit = false;

  public static double tagSelectiveSplitCutOff = 0.0;

  public static boolean tagSelectivePostSplit = false;

  public static double tagSelectivePostSplitCutOff = 0.0;

  /**
   * Right edge is right-recursive (X << X) Bad. (NP only is good)
   */
  public static boolean rightRec = false;//true;

  /**
   * Left edge is right-recursive (X << X)  Bad.
   */
  public static boolean leftRec = false;

  /**
   * X over X is marked (subsumes baseNP marking) Bad.
   */
  public static boolean xOverX = false;

  /**
   * Promote/delete punctuation like Collins.  Bad (!)
   */
  public static boolean collinsPunc = false;

  /**
   * Set the splitter strings.  These are a set of parent and/or grandparent
   * annotated categories which should be split off.
   */
  public static Set<String> splitters;

  public static Set postSplitters;

  public static Set<String> deleteSplitters;

  /**
   * Just for debugging: check that your tree transforms work correctly.  This
   * will print the transformations of the first printTreeTransformations trees.
   */
  public static int printTreeTransformations = 0;

  public static PrintWriter printAnnotatedPW;
  public static PrintWriter printBinarizedPW;

  public static boolean printStates = false;

  /** How to compact grammars as FSMs.
   *  0 = no compaction [uses makeSyntheticLabel1],
   *  1 = no compaction but use label names that wrap from right to left in binarization [uses makeSyntheticLabel2],
   *  2 = wrapping labels and materialize unary at top rewriting passive to active,
   *  3 = ExactGrammarCompactor,
   *  4 = LossyGrammarCompactor,
   *  5 = CategoryMergingGrammarCompactor.
   *  (May 2007 CDM note: options 4 and 5 don't seem to be functioning sensibly.  0, 1, and 3
   *  seem to be the 'good' options. 2 is only useful as input to 3.  There seems to be
   *  no reason not to use 0, despite the default.)
   */
  public static int compactGrammar = 3; // exact compaction on by default

  public static boolean leftToRight = false; // whether to binarize left to right or head out

  public static int compactGrammar() {
    if (markovFactor) {
      return compactGrammar;
    }
    return 0;
  }

  public static boolean noTagSplit = false;

  /**
   * CHANGE ANYTHING BELOW HERE AT YOUR OWN RISK
   */

  public static boolean smoothing = false;
  public static boolean smoothedBound = false;

  /*  public static boolean factorOut = false;
  public static boolean rightBonus = false;
  public static boolean brokenDep = false;*/

  /** Discounts the count of BinaryRule's (only, apparently) in training data. */
  public static double ruleDiscount = 0.0;

  //public static boolean outsideFilter = false;

  public static boolean printAnnotatedRuleCounts = false;
  public static boolean printAnnotatedStateCounts = false;

  /** Where to use the basic or split tags in the dependency grammar */
  public static boolean basicCategoryTagsInDependencyGrammar = false;


  public static void display() {
    System.err.println("Train parameters: smooth=" + smoothing + " PA=" + PA + " GPA=" + gPA + " selSplit=" + selectiveSplit + " (" + selectiveSplitCutOff + ((deleteSplitters != null) ? ("; deleting " + deleteSplitters): "") + ")" + " mUnary=" + markUnary + " mUnaryTags=" + markUnaryTags + " sPPT=" + splitPrePreT + " tagPA=" + tagPA + " tagSelSplit=" + tagSelectiveSplit + " (" + tagSelectiveSplitCutOff + ")" + " rightRec=" + rightRec + " leftRec=" + leftRec + " xOverX=" + xOverX + " collinsPunc=" + collinsPunc + " markov=" + markovFactor + " mOrd=" + markovOrder + " hSelSplit=" + hSelSplit + " (" + HSEL_CUT + ")" + " compactGrammar=" + compactGrammar() + " leaveItAll=" + leaveItAll + " postPA=" + postPA + " postGPA=" + postGPA + " selPSplit=" + selectivePostSplit + " (" + selectivePostSplitCutOff + ")" + " tagSelPSplit=" + tagSelectivePostSplit + " (" + tagSelectivePostSplitCutOff + ")" + " postSplitWithBase=" + postSplitWithBaseCategory + " fractionBeforeUnseenCounting=" + fractionBeforeUnseenCounting + " openClassTypesThreshold=" + openClassTypesThreshold);
  }

  public static void printTrainTree(PrintWriter pw, String message, Tree t) {
    PrintWriter myPW;
    if (pw == null) {
      myPW = new PrintWriter(System.out, true);
    } else {
      myPW = pw;
    }
    if (message != null && pw == null) {
      // hard coded to not print message if using file output!
      myPW.println(message);
    }
    boolean previousState = CategoryWordTag.printWordTag;
    CategoryWordTag.printWordTag = false;
    t.pennPrint(myPW);
    CategoryWordTag.printWordTag = previousState;
  }

} // end class Train
