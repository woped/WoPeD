package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Options to the parser which affect performance only at testing (parsing)
 * time.
 *
 * @author Dan Klein
 */
public class Test {

  static final String DEFAULT_PRE_TAGGER =
     "/u/nlp/data/pos-tagger/wsj3t0-18-bidirectional/bidirectional-wsj-0-18.tagger";

  private Test() {}  // just static flags

  /**
   * If false, then failure of the PCFG parser to parse a sentence
   * will trigger allowing all tags for words in parse recovery mode,
   * with a log probability of -1000.
   * If true, these extra taggings are not added.
   * It is false by default. Use option -noRecoveryTagging to set
   * to true.
   */
  public static boolean noRecoveryTagging = false;

  /** If true, then  failure of the PCFG factor to parse a sentence
   *  will trigger parse recovery mode.
   */
  public static boolean doRecovery = true;

  /**
   * If true, the n^4 "speed-up" is not used with the Factored Parser.
   */
  public static boolean useN5 = false;

  /** If true, use approximate factored algorithm, which just rescores
   *  PCFG k best, rather than exact factored algorithm.  This algorithm
   *  requires the dependency grammar to exist for rescoring, but not for
   *  the dependency grammar to be run.  Hence the correct usage for
   *  guarding code only required for exact A* factored parsing is now
   *  if (op.doPCFG && op.doDep && ! Test.useFastFactored).
   */
  public static boolean useFastFactored = false;


  /** If true, use faster iterative deepening CKY algorithm. */
  public static boolean iterativeCKY = false;

  /**
   * The maximum sentence length (including punctuation, etc.) to parse.
   */
  public static int maxLength = -0xDEADBEEF;
  // initial value is -0xDEADBEEF (actually positive because of 2s complement)

  /**
   * The maximum number of edges and hooks combined that the factored parser
   * will build before giving up.  This number should probably be relative to
   * the sentence length parsed. In general, though, if the parser cannot parse
   * a sentence after this much work then there is no good parse consistent
   * between the PCFG and Dependency parsers.  (Normally, depending on other
   * flags), the parser will then just return the best PCFG parse.)
   */
  public static int MAX_ITEMS = 200000;

  /**
   *  The amount of smoothing put in (as an m-estimate) for unknown words.
   *  If negative, set by the code in the lexicon class.
   */
  public static double unseenSmooth = -1.0;

  /**
   * Parse trees in test treebank in order of increasing length.
   */
  public static boolean increasingLength = false;

  /**
   * Tag the sentences first, then parse given those (coarse) tags.
   */
  public static boolean preTag = false;

  /**
   * Parse using only tags given from correct answer or the POS tagger
   */
  public static boolean forceTags = preTag;
  
  public static boolean forceTagBeginnings = false;

  /**
   * POS tagger model used when preTag is enabled.
   */
  public static String taggerSerializedFile = DEFAULT_PRE_TAGGER;

  /**
   * Only valid with force tags - strips away functionals when forcing
   * the tags, meaning tags have to start
   * appropriately but the parser will assign the functional part.
   */
  public static boolean noFunctionalForcing = preTag;

  /**
   * Write EvalB-readable output files.
   */
  public static boolean evalb = false;

  /**
   * Print a lot of extra output as you parse.
   */
  public static boolean verbose = false; // Don't change this; set with -v

  public static final boolean exhaustiveTest = false;

  /** If this variable is true, and the sum of the inside and outside score
   *  for a constituent is worse than the best known score for a sentence by
   *  more than <code>pcfgThresholdValue</code>, then -Inf is returned as the
   *  outside Score by <code>oScore()</code> (while otherwise the true
   *  outside score is returned).
   */
  public static final boolean pcfgThreshold = false;
  public static final double pcfgThresholdValue = -2.0;

  /**
   * Print out all best PCFG parses.
   */
  public static boolean printAllBestParses = false;

  /**
   * Weighting on dependency log probs.  The dependency grammar negative log
   * probability scores are simply multiplied by this number.
   */
  public static double depWeight = 1.0;
  public static boolean prunePunc = false;

  /** If a token list does not have sentence final punctuation near the
   *  end, then automatically add the default one.
   *  This might help parsing if the treebank is all punctuated.
   *  Not done if reading a treebank.
   */
  public static boolean addMissingFinalPunctuation;


  /**
   * Determines format of output trees: choose among penn, oneline
   */
  public static String outputFormat = "penn";
  public static String outputFormatOptions = "";


  /** If true, write files parsed to a new file with the same name except
   *  for an added ".stp" extension.
   */
  public static boolean writeOutputFiles;

  /** If the writeOutputFiles option is true, then output files appear in
   *  this directory.  An unset value (<code>null</code>) means to use
   *  the directory of the source files.  Use <code>""</code> or <code>.</code>
   *  for the current directory.
   */
  public static String outputFilesDirectory;

  /** If the writeOutputFiles option is true, then output files appear with
   *  this extension. An unset value (<code>null</code>) means to use
   *  the default of "stp".  Use <code>""</code> for no extension.
   */
  public static String outputFilesExtension;

  /**
   * The largest span to consider for word-hood.  Used for parsing unsegmented
   * Chinese text and parsing lattices.  Keep it at 1 unless you know what
   * you're doing.
   */
  public static int maxSpanForTags = 1;

  /**
   * Turns on normalizing scores for sentence length.  Makes no difference
   * (except decreased efficiency) unless maxSpanForTags is greater than one.
   * Works only for PCFG (so far).
   */
  public static boolean lengthNormalization = false;

  /**
   * When you want to force the parse to parse a particular subsequence
   * into a particular state.  Parses will only be made where there is
   * a constituent over the given span which matches (as regular expression)
   * the state Pattern given.
   */
  public static List<Constraint> constraints = null;

  /**
   * Used when you want to generate sample parses instead of finding the best
   * parse.  (NOT YET USED.)
   */
  public static boolean sample = false;

  /** Printing k-best parses from PCFG, when k &gt; 0. */
  public static int printPCFGkBest = 0;

  /** Printing k-best parses from PCFG, when k &gt; 0. */
  public static int printFactoredKGood = 0;

  /** What evaluations to report and how to report them
   *  (using LexicalizedParser). Known evaluations
   *  are: pcfgLB, pcfgCB, pcfgDA, pcfgTA, pcfgLL, pcfgRUO, pcfgCUO, pcfgCatE,
   *  depDA, depTA, depLL,
   *  factLB, factCB, factDA, factTA, factLL.
   *  The default is pcfgLB,depDA,factLB,factTA.  You need to negate those
   *  ones out (e.g., <code>-evals "depDA=false"</code>) if you don't want
   *  them.
   *  LB = ParseEval labeled bracketing,
   *  CB = crossing brackets and zero crossing bracket rate,
   *  DA = dependency accuracy, TA = tagging accuracy,
   *  LL = log likelihood score,
   *  RUO/CUO = rules/categories under and over proposed,
   *  CatE = evaluation by phrasal category.
   *  Known styles are: runningAverages, summary, tsv.
   *  The default style is summary.
   *  You need to negate it out if you don't want it.
   *  Invalid names in the argument to this option are not reported!
   */
  public static Properties evals;

  static {
    // static initialization block
    evals = new Properties();
    evals.setProperty("pcfgLB", "true");
    evals.setProperty("depDA", "true");
    evals.setProperty("factLB", "true");
    evals.setProperty("factTA", "true");
    evals.setProperty("summary", "true");
  }

  /** This variable says to find k good fast factored parses, how many times
   *  k of the best PCFG parses should be examined.
   */
  public static int fastFactoredCandidateMultiplier = 3;

  /** This variable says to find k good factored parses, how many added on
   *  best PCFG parses should be examined.
   */
  public static int fastFactoredCandidateAddend = 50;


  /** If this is true, the Lexicon is used to score P(w|t) in the backoff inside the 
   *  dependency grammar.  (Otherwise, a MLE is used is w is seen, and a constant if
   *  w is unseen.
   */
  public static boolean useLexiconToScoreDependencyPwGt = false;

  /** If this is true, perform non-projective dependency parsing.
   */
  public static boolean useNonProjectiveDependencyParser = false;

  /** A Constraint represents a restriction on possible parse trees to
   *  consider.  It says that a parse cannot be postulated that would
   *  contradict having a constituent from position start to end, and that
   *  any constituent postulated with this span must match the regular
   *  expression given by state.  Note, however, that it does not strictly
   *  guarantee that such a constituent exists in a returned parse.
   */
  public static class Constraint implements Serializable {
    public int start;
    public int end;
    public Pattern state;

    private static final long serialVersionUID = 4955237758572202093L;
  }

  /**
   * Determines method for print trees on output.
   *
   * @param tlpParams The treebank parser params
   * @return A suitable tree printing object
   */
  public static TreePrint treePrint(TreebankLangParserParams tlpParams) {
    TreebankLanguagePack tlp = tlpParams.treebankLanguagePack();
    return new TreePrint(outputFormat, outputFormatOptions, tlp, tlpParams.headFinder());
  }


  public static void display() {
    String str = "Test parameters maxLength=" + maxLength + " preTag=" + preTag + " outputFormat=" + outputFormat + " outputFormatOptions=" + outputFormatOptions + " printAllBestParses=" + printAllBestParses;
    System.err.println(str);
  }

}
