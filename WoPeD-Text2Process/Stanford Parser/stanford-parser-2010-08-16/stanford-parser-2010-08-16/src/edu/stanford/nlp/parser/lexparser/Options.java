package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.util.ErasureUtils;
import edu.stanford.nlp.util.Numberer;
import edu.stanford.nlp.util.StringUtils;

import java.io.*;
import java.util.*;


/**
 * Options to the parser which MUST be the SAME at both training and testing
 * (parsing) time in order for the parser to work properly.
 *
 * @author Dan Klein
 * @author Christopher Manning
 */
public class Options implements Serializable {

  public Options() {
    this(new EnglishTreebankParserParams());
  }

  public Options(TreebankLangParserParams tlpParams) {
    this.tlpParams = tlpParams;
  }

  /**
   * Set options based on a String array in the style of
   * commandline flags. This method goes through the array until it ends,
   * processing options, as for {@link #setOption}.
   *
   * @param flags Array of options (or as a varargs list of arguments).
   *      The options passed in should
   *      be specified like command-line arguments, including with an initial
   *      minus sign  for example,
   *          {"-outputFormat", "typedDependencies", "-maxLength", "70"}
   * @throws IllegalArgumentException If an unknown flag is passed in
   */
  public void setOptions(String... flags) {
    setOptions(flags, 0, flags.length);
  }

  /**
   * Set options based on a String array in the style of
   * commandline flags. This method goes through the array until it ends,
   * processing options, as for {@link #setOption}.
   *
   * @param flags Array of options.  The options passed in should
   *      be specified like command-line arguments, including with an initial
   *      minus sign  for example,
   *          {"-outputFormat", "typedDependencies", "-maxLength", "70"}
   * @param startIndex The index in the array to begin processing options at
   * @param endIndexPlusOne A number one greater than the last array index at
   *      which options should be processed
   * @throws IllegalArgumentException If an unknown flag is passed in
   */
  public void setOptions(final String[] flags, final int startIndex, final int endIndexPlusOne) {
    for (int i = startIndex; i < endIndexPlusOne;) {
      i = setOption(flags, i);
    }
  }

  /**
   * Set options based on a String array in the style of
   * commandline flags. This method goes through the array until it ends,
   * processing options, as for {@link #setOption}.
   *
   * @param flags Array of options (or as a varargs list of arguments).
   *      The options passed in should
   *      be specified like command-line arguments, including with an initial
   *      minus sign  for example,
   *          {"-outputFormat", "typedDependencies", "-maxLength", "70"}
   * @throws IllegalArgumentException If an unknown flag is passed in
   */
  public void setOptionsOrWarn(String... flags) {
    setOptionsOrWarn(flags, 0, flags.length);
  }

  /**
   * Set options based on a String array in the style of
   * commandline flags. This method goes through the array until it ends,
   * processing options, as for {@link #setOption}.
   *
   * @param flags Array of options.  The options passed in should
   *      be specified like command-line arguments, including with an initial
   *      minus sign  for example,
   *          {"-outputFormat", "typedDependencies", "-maxLength", "70"}
   * @param startIndex The index in the array to begin processing options at
   * @param endIndexPlusOne A number one greater than the last array index at
   *      which options should be processed
   * @throws IllegalArgumentException If an unknown flag is passed in
   */
  public void setOptionsOrWarn(final String[] flags, final int startIndex, final int endIndexPlusOne) {
    for (int i = startIndex; i < endIndexPlusOne;) {
      i = setOptionOrWarn(flags, i);
    }
  }

  /**
   * Set an option based on a String array in the style of
   * commandline flags. The option may
   * be either one known by the Options object, or one recognized by the
   * TreebankLangParserParams which has already been set up inside the Options
   * object, and then the option is set in the language-particular
   * TreebankLangParserParams.
   * Note that despite this method being an instance method, many flags
   * are actually set as static class variables in the Train and Test
   * classes (this should be fixed some day).
   * Some options (there are many others; see the source code):
   * <ul>
   * <li> <code>-maxLength n</code> set the maximum length sentence to parse (inclusively)
   * <li> <code>-printTT</code> print the training trees in raw, annotated, and annotated+binarized form.  Useful for debugging and other miscellany.
   * <li> <code>-printAnnotated filename</code> use only in conjunction with -printTT.  Redirects printing of annotated training trees to <code>filename</code>.
   * <li> <code>-forceTags</code> when the parser is tested against a set of gold standard trees, use the tagged yield, instead of just the yield, as input.
   * </ul>
   *
   * @param flags An array of options arguments, command-line style.  E.g. {"-maxLength", "50"}.
   * @param i The index in flags to start at when processing an option
   * @return The index in flags of the position after the last element used in
   *      processing this option. If the current array position cannot be processed as a valid
   *      option, then a warning message is printed to stderr and the return value is <code>i+1</code>
   */
  public int setOptionOrWarn(String[] flags, int i) {
    int j = setOptionFlag(flags, i);
    if (j == i) {
      j = tlpParams.setOptionFlag(flags, i);
    }
    if (j == i) {
      System.err.println("WARNING! lexparser.Options: Unknown option ignored: " + flags[i]);
      j++;
    }
    return j;
  }

  /**
   * Set an option based on a String array in the style of
   * commandline flags. The option may
   * be either one known by the Options object, or one recognized by the
   * TreebankLangParserParams which has already been set up inside the Options
   * object, and then the option is set in the language-particular
   * TreebankLangParserParams.
   * Note that despite this method being an instance method, many flags
   * are actually set as static class variables in the Train and Test
   * classes (this should be fixed some day).
   * Some options (there are many others; see the source code):
   * <ul>
   * <li> <code>-maxLength n</code> set the maximum length sentence to parse (inclusively)
   * <li> <code>-printTT</code> print the training trees in raw, annotated, and annotated+binarized form.  Useful for debugging and other miscellany.
   * <li> <code>-printAnnotated filename</code> use only in conjunction with -printTT.  Redirects printing of annotated training trees to <code>filename</code>.
   * <li> <code>-forceTags</code> when the parser is tested against a set of gold standard trees, use the tagged yield, instead of just the yield, as input.
   * </ul>
   *
   * @param flags An array of options arguments, command-line style.  E.g. {"-maxLength", "50"}.
   * @param i The index in flags to start at when processing an option
   * @return The index in flags of the position after the last element used in
   *      processing this option.
   * @throws IllegalArgumentException If the current array position cannot be
   *      processed as a valid option
   */
  public int setOption(String[] flags, int i) {
    int j = setOptionFlag(flags, i);
    if (j == i) {
      j = tlpParams.setOptionFlag(flags, i);
    }
    if (j == i) {
      throw new IllegalArgumentException("Unknown option: " + flags[i]);
    }
    return j;
  }

  /**
   * Set an option in this object, based on a String array in the style of
   * commandline flags.  The option is only processed with respect to
   * options directly known by the Options object.
   * Some options (there are many others; see the source code):
   * <ul>
   * <li> <code>-maxLength n</code> set the maximum length sentence to parse (inclusively)
   * <li> <code>-printTT</code> print the training trees in raw, annotated, and annotated+binarized form.  Useful for debugging and other miscellany.
   * <li> <code>-printAnnotated filename</code> use only in conjunction with -printTT.  Redirects printing of annotated training trees to <code>filename</code>.
   * <li> <code>-forceTags</code> when the parser is tested against a set of gold standard trees, use the tagged yield, instead of just the yield, as input.
   * </ul>
   *
   * @param args An array of options arguments, command-line style.  E.g. {"-maxLength", "50"}.
   * @param i The index in args to start at when processing an option
   * @return The index in args of the position after the last element used in
   *      processing this option, or the value i unchanged if a valid option couldn't
   *      be processed starting at position i.
   */
  private int setOptionFlag(String[] args, int i) {
    if (args[i].equalsIgnoreCase("-PCFG")) {
      doDep = false;
      doPCFG = true;
      i++;
    } else if (args[i].equalsIgnoreCase("-dep")) {
      doDep = true;
      doPCFG = false;
      i++;
    } else if (args[i].equalsIgnoreCase("-factored")) {
      doDep = true;
      doPCFG = true;
      Test.useFastFactored = false;
      i++;
    } else if (args[i].equalsIgnoreCase("-fastFactored")) {
      doDep = true;
      doPCFG = true;
      Test.useFastFactored = true;
      i++;
    } else if (args[i].equalsIgnoreCase("-noRecoveryTagging")) {
      Test.noRecoveryTagging = true;
      i++;
    } else if (args[i].equalsIgnoreCase("-useLexiconToScoreDependencyPwGt")) {
      Test.useLexiconToScoreDependencyPwGt = true;
      i++;
    } else if (args[i].equalsIgnoreCase("-useSmoothTagProjection")) {
      MLEDependencyGrammar.useSmoothTagProjection = true;
      i++;
    } else if (args[i].equalsIgnoreCase("-useUnigramWordSmoothing")) {
      MLEDependencyGrammar.useUnigramWordSmoothing = true;
      i++;
    } else if (args[i].equalsIgnoreCase("-useNonProjectiveDependencyParser")) {
      Test.useNonProjectiveDependencyParser = true;
      i++;
    } else if (args[i].equalsIgnoreCase("-maxLength") && (i + 1 < args.length)) {
      Test.maxLength = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-MAX_ITEMS") && (i + 1 < args.length)) {
      Test.MAX_ITEMS = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-iterativeCKY")) {
      Test.iterativeCKY = true;
      i++;
    } else if (args[i].equalsIgnoreCase("-vMarkov") && (i + 1 < args.length)) {
      int order = Integer.parseInt(args[i + 1]);
      if (order <= 1) {
        Train.PA = false;
        Train.gPA = false;
      } else if (order == 2) {
        Train.PA = true;
        Train.gPA = false;
      } else if (order >= 3) {
        Train.PA = true;
        Train.gPA = true;
      }
      i += 2;
    } else if (args[i].equalsIgnoreCase("-vSelSplitCutOff") && (i + 1 < args.length)) {
      Train.selectiveSplitCutOff = Double.parseDouble(args[i + 1]);
      Train.selectiveSplit = Train.selectiveSplitCutOff > 0.0;
      i += 2;
    } else if (args[i].equalsIgnoreCase("-vSelPostSplitCutOff") && (i + 1 < args.length)) {
      Train.selectivePostSplitCutOff = Double.parseDouble(args[i + 1]);
      Train.selectivePostSplit = Train.selectivePostSplitCutOff > 0.0;
      i += 2;
    } else if (args[i].equalsIgnoreCase("-deleteSplitters") && (i+1 < args.length)) {
      String[] toDel = args[i+1].split(" *, *");
      Train.deleteSplitters = new HashSet<String>(Arrays.asList(toDel));
      i += 2;
    } else if (args[i].equalsIgnoreCase("-postSplitWithBaseCategory")) {
      Train.postSplitWithBaseCategory = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-vPostMarkov") && (i + 1 < args.length)) {
      int order = Integer.parseInt(args[i + 1]);
      if (order <= 1) {
        Train.postPA = false;
        Train.postGPA = false;
      } else if (order == 2) {
        Train.postPA = true;
        Train.postGPA = false;
      } else if (order >= 3) {
        Train.postPA = true;
        Train.postGPA = true;
      }
      i += 2;
    } else if (args[i].equalsIgnoreCase("-hMarkov") && (i + 1 < args.length)) {
      int order = Integer.parseInt(args[i + 1]);
      if (order >= 0) {
        Train.markovOrder = order;
        Train.markovFactor = true;
      } else {
        Train.markovFactor = false;
      }
      i += 2;
    } else if (args[i].equalsIgnoreCase("-distanceBins") && (i + 1 < args.length)) {
      int numBins = Integer.parseInt(args[i + 1]);
      if (numBins <= 1) {
        distance = false;
      } else if (numBins == 4) {
        distance = true;
        coarseDistance = true;
      } else if (numBins == 5) {
        distance = true;
        coarseDistance = false;
      } else {
        throw new IllegalArgumentException("Invalid value for -distanceBin: " + args[i+1]);
      }
      i += 2;
    } else if (args[i].equalsIgnoreCase("-noStop")) {
      genStop = false;
      i++;
    } else if (args[i].equalsIgnoreCase("-nonDirectional")) {
      directional = false;
      i++;
    } else if (args[i].equalsIgnoreCase("-depWeight") && (i + 1 < args.length)) {
      Test.depWeight = Double.parseDouble(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-printPCFGkBest") && (i + 1 < args.length)) {
      Test.printPCFGkBest = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-printFactoredKGood") && (i + 1 < args.length)) {
      Test.printFactoredKGood = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-smoothTagsThresh") && (i + 1 < args.length)) {
      lexOptions.smoothInUnknownsThreshold = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-unseenSmooth") && (i + 1 < args.length)) {
      Test.unseenSmooth = Double.parseDouble(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-fractionBeforeUnseenCounting") && (i + 1 < args.length)) {
      Train.fractionBeforeUnseenCounting = Double.parseDouble(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-hSelSplitThresh") && (i + 1 < args.length)) {
      Train.HSEL_CUT = Integer.parseInt(args[i + 1]);
      Train.hSelSplit = Train.HSEL_CUT > 0;
      i += 2;
    } else if (args[i].equalsIgnoreCase("-tagPA")) {
      Train.tagPA = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-tagSelSplitCutOff") && (i + 1 < args.length)) {
      Train.tagSelectiveSplitCutOff = Double.parseDouble(args[i + 1]);
      Train.tagSelectiveSplit = Train.tagSelectiveSplitCutOff > 0.0;
      i += 2;
    } else if (args[i].equalsIgnoreCase("-tagSelPostSplitCutOff") && (i + 1 < args.length)) {
      Train.tagSelectivePostSplitCutOff = Double.parseDouble(args[i + 1]);
      Train.tagSelectivePostSplit = Train.tagSelectivePostSplitCutOff > 0.0;
      i += 2;
    } else if (args[i].equalsIgnoreCase("-noTagSplit")) {
      Train.noTagSplit = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-uwm") && (i + 1 < args.length)) {
      lexOptions.useUnknownWordSignatures = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-unknownSuffixSize") && (i + 1 < args.length)) {
      lexOptions.unknownSuffixSize = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-unknownPrefixSize") && (i + 1 < args.length)) {
      lexOptions.unknownPrefixSize = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-uwModel") && (i + 1 < args.length)) {
      lexOptions.uwModel = args[i+1];
      i += 2;
    } else if (args[i].equalsIgnoreCase("-openClassThreshold") && (i + 1 < args.length)) {
      Train.openClassTypesThreshold = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-leaveItAll") && (i + 1 < args.length)) {
      Train.leaveItAll = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-unary") && i+1 < args.length) {
      Train.markUnary = Integer.parseInt(args[i+1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-unaryTags")) {
      Train.markUnaryTags = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-mutate")) {
      lexOptions.smartMutation = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-useUnicodeType")) {
      lexOptions.useUnicodeType = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-rightRec")) {
      Train.rightRec = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-noRightRec")) {
      Train.rightRec = false;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-preTag")) {
      Test.preTag = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-forceTags")) {
      Test.forceTags = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-taggerSerializedFile")) {
      Test.taggerSerializedFile = args[i+1];
      i += 2;
    } else if (args[i].equalsIgnoreCase("-forceTagBeginnings")) {
      Test.forceTagBeginnings = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-noFunctionalForcing")) {
      Test.noFunctionalForcing = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-scTags")) {
      dcTags = false;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-dcTags")) {
      dcTags = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-basicCategoryTagsInDependencyGrammar")) {
      Train.basicCategoryTagsInDependencyGrammar = true;
      i+= 1;
    } else if (args[i].equalsIgnoreCase("-evalb")) {
      Test.evalb = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-v") || args[i].equalsIgnoreCase("-verbose")) {
      Test.verbose = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-outputFilesDirectory") && i+1 < args.length) {
      Test.outputFilesDirectory = args[i+1];
      i += 2;
    } else if (args[i].equalsIgnoreCase("-outputFilesExtension") && i+1 < args.length) {
      Test.outputFilesExtension = args[i+1];
      i += 2;
    } else if (args[i].equalsIgnoreCase("-writeOutputFiles")) {
      Test.writeOutputFiles = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-printAllBestParses")) {
      Test.printAllBestParses = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-outputTreeFormat") || args[i].equalsIgnoreCase("-outputFormat")) {
      Test.outputFormat = args[i + 1];
      i += 2;
    } else if (args[i].equalsIgnoreCase("-outputTreeFormatOptions") || args[i].equalsIgnoreCase("-outputFormatOptions")) {
      Test.outputFormatOptions = args[i + 1];
      i += 2;
    } else if (args[i].equalsIgnoreCase("-addMissingFinalPunctuation")) {
      Test.addMissingFinalPunctuation = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-flexiTag")) {
      lexOptions.flexiTag = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-lexiTag")) {
      lexOptions.flexiTag = false;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-useSignatureForKnownSmoothing")) {
      lexOptions.useSignatureForKnownSmoothing = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-compactGrammar")) {
      Train.compactGrammar = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-markFinalStates")) {
      Train.markFinalStates = args[i + 1].equalsIgnoreCase("true");
      i += 2;
    } else if (args[i].equalsIgnoreCase("-leftToRight")) {
      Train.leftToRight = args[i + 1].equals("true");
      i += 2;
    } else if (args[i].equalsIgnoreCase("-cnf")) {
      forceCNF = true;
      i += 1;
    } else if (args[i].equalsIgnoreCase("-nodePrune") && i+1 < args.length) {
      nodePrune = args[i+1].equalsIgnoreCase("true");
      i += 2;
    } else if (args[i].equalsIgnoreCase("-acl03pcfg")) {
      doDep = false;
      doPCFG = true;
      // lexOptions.smoothInUnknownsThreshold = 30;
      Train.markUnary = 1;
      Train.PA = true;
      Train.gPA = false;
      Train.tagPA = true;
      Train.tagSelectiveSplit = false;
      Train.rightRec = true;
      Train.selectiveSplit = true;
      Train.selectiveSplitCutOff = 400.0;
      Train.markovFactor = true;
      Train.markovOrder = 2;
      Train.hSelSplit = true;
      lexOptions.useUnknownWordSignatures = 2;
      lexOptions.flexiTag = true;
      // DAN: Tag double-counting is BAD for PCFG-only parsing
      dcTags = false;
      // don't increment i so it gets language specific stuff as well
    } else if (args[i].equalsIgnoreCase("-jenny")) {
      doDep = false;
      doPCFG = true;
      // lexOptions.smoothInUnknownsThreshold = 30;
      Train.markUnary = 1;
      Train.PA = false;
      Train.gPA = false;
      Train.tagPA = false;
      Train.tagSelectiveSplit = false;
      Train.rightRec = true;
      Train.selectiveSplit = false;
//      Train.selectiveSplitCutOff = 400.0;
      Train.markovFactor = false;
//      Train.markovOrder = 2;
      Train.hSelSplit = false;
      lexOptions.useUnknownWordSignatures = 2;
      lexOptions.flexiTag = true;
      // DAN: Tag double-counting is BAD for PCFG-only parsing
      dcTags = false;
      // don't increment i so it gets language specific stuff as well
    } else if (args[i].equalsIgnoreCase("-goodPCFG")) {
      doDep = false;
      doPCFG = true;
      // op.lexOptions.smoothInUnknownsThreshold = 30;
      Train.markUnary = 1;
      Train.PA = true;
      Train.gPA = false;
      Train.tagPA = true;
      Train.tagSelectiveSplit = false;
      Train.rightRec = true;
      Train.selectiveSplit = true;
      Train.selectiveSplitCutOff = 400.0;
      Train.markovFactor = true;
      Train.markovOrder = 2;
      Train.hSelSplit = true;
      lexOptions.useUnknownWordSignatures = 2;
      lexOptions.flexiTag = true;
      // DAN: Tag double-counting is BAD for PCFG-only parsing
      dcTags = false;
      String[] delSplit = new String[] { "-deleteSplitters",
                                         "VP^NP,VP^VP,VP^SINV,VP^SQ" };
      if (this.setOptionFlag(delSplit, 0) != 2) {
        System.err.println("Error processing deleteSplitters");
      }
      // don't increment i so it gets language specific stuff as well
    } else if (args[i].equalsIgnoreCase("-linguisticPCFG")) {
      doDep = false;
      doPCFG = true;
      // op.lexOptions.smoothInUnknownsThreshold = 30;
      Train.markUnary = 1;
      Train.PA = true;
      Train.gPA = false;
      Train.tagPA = true;        // on at the moment, but iffy
      Train.tagSelectiveSplit = false;
      Train.rightRec = false;    // not for linguistic
      Train.selectiveSplit = true;
      Train.selectiveSplitCutOff = 400.0;
      Train.markovFactor = true;
      Train.markovOrder = 2;
      Train.hSelSplit = true;
      lexOptions.useUnknownWordSignatures = 5;   // different from acl03pcfg
      lexOptions.flexiTag = false;       // different from acl03pcfg
      // DAN: Tag double-counting is BAD for PCFG-only parsing
      dcTags = false;
      // don't increment i so it gets language specific stuff as well
    } else if (args[i].equalsIgnoreCase("-ijcai03")) {
      doDep = true;
      doPCFG = true;
      Train.markUnary = 0;
      Train.PA = true;
      Train.gPA = false;
      Train.tagPA = false;
      Train.tagSelectiveSplit = false;
      Train.rightRec = false;
      Train.selectiveSplit = true;
      Train.selectiveSplitCutOff = 300.0;
      Train.markovFactor = true;
      Train.markovOrder = 2;
      Train.hSelSplit = true;
      Train.compactGrammar = 0; /// cdm: May 2005 compacting bad for factored?
      lexOptions.useUnknownWordSignatures = 2;
      lexOptions.flexiTag = false;
      dcTags = true;
      // op.nodePrune = true;  // cdm: May 2005: this doesn't help
      // don't increment i so it gets language specific stuff as well
    } else if (args[i].equalsIgnoreCase("-goodFactored")) {
      doDep = true;
      doPCFG = true;
      Train.markUnary = 0;
      Train.PA = true;
      Train.gPA = false;
      Train.tagPA = false;
      Train.tagSelectiveSplit = false;
      Train.rightRec = false;
      Train.selectiveSplit = true;
      Train.selectiveSplitCutOff = 300.0;
      Train.markovFactor = true;
      Train.markovOrder = 2;
      Train.hSelSplit = true;
      Train.compactGrammar = 0; /// cdm: May 2005 compacting bad for factored?
      lexOptions.useUnknownWordSignatures = 5;  // different from ijcai03
      lexOptions.flexiTag = false;
      dcTags = true;
      // op.nodePrune = true;  // cdm: May 2005: this doesn't help
      // don't increment i so it gets language specific stuff as well
    } else if (args[i].equalsIgnoreCase("-chineseFactored")) {
      // Single counting tag->word rewrite is also much better for Chinese
      // Factored.  Bracketing F1 goes up about 0.7%.
      dcTags = false;
      lexOptions.useUnicodeType = true;
      Train.markovOrder = 2;
      Train.hSelSplit = true;
      Train.markovFactor = true;
      Train.HSEL_CUT = 50;
      // Train.openClassTypesThreshold=1;  // so can get unseen punctuation
      // Train.fractionBeforeUnseenCounting=0.0;  // so can get unseen punctuation
      // don't increment i so it gets language specific stuff as well
    } else if (args[i].equalsIgnoreCase("-arabicFactored")) {
      doDep = true;
      doPCFG = true;
      dcTags = false;   // "false" seems to help Arabic about 0.1% F1
      Train.markovFactor = true;
      Train.markovOrder = 2;
      Train.hSelSplit = true;
      Train.HSEL_CUT = 75;  // 75 bit better than 50, 100 a bit worse
      Train.PA = true;
      Train.gPA = false;
      Train.selectiveSplit = true;
      Train.selectiveSplitCutOff = 300.0;
      Train.markUnary = 1;  // Helps PCFG and marginally factLB
      // Train.compactGrammar = 0;  // Doesn't seem to help or only 0.05% F1
      lexOptions.useUnknownWordSignatures = 9;
      lexOptions.unknownPrefixSize = 1;
      lexOptions.unknownSuffixSize = 1;
      Test.MAX_ITEMS = 500000; // Arabic sentences are long enough that this helps a fraction
      // don't increment i so it gets language specific stuff as well
    } else if (args[i].equalsIgnoreCase("-chinesePCFG")) {
      doDep = false;
      doPCFG = true;
      // Single counting tag->word rewrite is also much better for Chinese PCFG
      // Bracketing F1 is up about 2% and tag accuracy about 1% (exact by 6%)
      dcTags = false;
    } else if (args[i].equalsIgnoreCase("-printTT") && (i+1 < args.length)) {
      Train.printTreeTransformations = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-printAnnotatedRuleCounts")) {
      Train.printAnnotatedRuleCounts = true;
      i++;
    } else if (args[i].equalsIgnoreCase("-printAnnotatedStateCounts")) {
      Train.printAnnotatedStateCounts = true;
      i++;
    } else if (args[i].equalsIgnoreCase("-printAnnotated") && (i + 1 < args.length)) {
      try {
        Train.printAnnotatedPW = tlpParams.pw(new FileOutputStream(args[i + 1]));
      } catch (IOException ioe) {
        Train.printAnnotatedPW = null;
      }
      i += 2;
    } else if (args[i].equalsIgnoreCase("-printBinarized") && (i + 1 < args.length)) {
      try {
        Train.printBinarizedPW = tlpParams.pw(new FileOutputStream(args[i + 1]));
      } catch (IOException ioe) {
        Train.printBinarizedPW = null;
      }
      i += 2;
    } else if (args[i].equalsIgnoreCase("-printStates")) {
      Train.printStates = true;
      i++;
    } else if (args[i].equalsIgnoreCase("-evals")) {
      Test.evals = StringUtils.stringToProperties(args[i+1], Test.evals);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-fastFactoredCandidateMultiplier")) {
      Test.fastFactoredCandidateMultiplier = Integer.parseInt(args[i + 1]);
      i += 2;
    } else if (args[i].equalsIgnoreCase("-fastFactoredCandidateAddend")) {
      Test.fastFactoredCandidateAddend = Integer.parseInt(args[i + 1]);
      i += 2;
    }
    return i;
  }

  public static class LexOptions implements Serializable {

    /**
     * Whether to use suffix and capitalization information for unknowns.
     * Within the BaseLexicon model options have the following meaning:
     * 0 means a single unknown token.  1 uses suffix, and capitalization.
     * 2 uses a variant (richer) form of signature.  Good.
     * Use this one.  Using the richer signatures in versions 3 or 4 seems
     * to have very marginal or no positive value.
     * 3 uses a richer form of signature that mimics the NER word type
     * patterns.  4 is a variant of 2.  5 is another with more English
     * specific morphology (good for English unknowns!).
     * 6-9 are options for Arabic.  9 codes some patterns for numbers and
     * derivational morophology, but also supports unknownPrefixSize and
     * unknownSuffixSize.
     * For German, 0 means a single unknown token, and non-zero means to use
     * capitalization of first letter and a suffix of length
     * unknownSuffixSize.
     */
    public int useUnknownWordSignatures = 0;

    /**
     * Words more common than this are tagged with MLE P(t|w). Default 100. The
     * smoothing is sufficiently slight that changing this has little effect.
     * But set this to 0 to be able to use the parser as a vanilla PCFG with
     * no smoothing (not as a practical parser but for exposition or debugging).
     */
    public int smoothInUnknownsThreshold = 100;

    /**
     * Smarter smoothing for rare words.
     */
    public boolean smartMutation = false;

    /**
     * Make use of unicode code point types in smoothing.
     */
    public boolean useUnicodeType = false;

    /** For certain Lexicons, a certain number of word-final letters are
     *  used to subclassify the unknown token. This gives the number of
     *  letters.
     */
    public int unknownSuffixSize = 1;

    /** For certain Lexicons, a certain number of word-initial letters are
     *  used to subclassify the unknown token. This gives the number of
     *  letters.
     */
    public int unknownPrefixSize = 1;

    /**
     * Model for unknown words that the lexicon should use
     */
    public String uwModel; // = null;

    /* If this option is false, then all words that were seen in the training
     * data (even once) are constrained to only have seen tags.  That is,
     * mle is used for the lexicon.
     * If this option is true, then if a word has been seen more than
     * smoothInUnknownsThreshold, then it will still only get tags with which
     * it has been seen, but rarer words will get all tags for which the
     * unknown word model (or smart mutation) does not give a score of -Inf.
     * This will normally be all open class tags.
     * If floodTags is invoked by the parser, all other tags will also be
     * given a minimal non-zero, non-infinite probability.
     */
    public boolean flexiTag = false;

    /** Whether to use signature rather than just being unknown as prior in
     *  known word smoothing.  Currently only works if turned on for English.
     */
    public boolean useSignatureForKnownSmoothing;




    private static final long serialVersionUID = 2805351374506855632L;

    private static final String[] params = { "useUnknownWordSignatures",
                                             "smoothInUnknownsThreshold",
                                             "smartMutation",
                                             "useUnicodeType",
                                             "unknownSuffixSize",
                                             "unknownPrefixSize",
                                             "flexiTag",
                                             "useSignatureForKnownSmoothing"};

    @Override
    public String toString() {
      return params[0] + " " + useUnknownWordSignatures + "\n" +
        params[1] + " " + smoothInUnknownsThreshold + "\n" +
        params[2] + " " + smartMutation + "\n" +
        params[3] + " " + useUnicodeType + "\n" +
        params[4] + " " + unknownSuffixSize + "\n" +
        params[5] + " " + unknownPrefixSize + "\n" +
        params[6] + " " + flexiTag + "\n" +
        params[7] + " " + useSignatureForKnownSmoothing + "\n";
    }

    public void readData(BufferedReader in) throws IOException {
      for (int i = 0; i < params.length; i++) {
        String line = in.readLine();
        int idx = line.indexOf(' ');
        String key = line.substring(0, idx);
        String value = line.substring(idx + 1);
        if ( ! key.equalsIgnoreCase(params[i])) {
          System.err.println("Yikes!!! Expected " + params[i] + " got " + key);
        }
        switch (i) {
        case 0:
          useUnknownWordSignatures = Integer.parseInt(value);
          break;
        case 1:
          smoothInUnknownsThreshold = Integer.parseInt(value);
          break;
        case 2:
          smartMutation = Boolean.parseBoolean(value);
          break;
        case 3:
          useUnicodeType = Boolean.parseBoolean(value);
          break;
        case 4:
          unknownSuffixSize = Integer.parseInt(value);
          break;
        case 5:
          unknownPrefixSize = Integer.parseInt(value);
          break;
        case 6:
          flexiTag = Boolean.parseBoolean(value);
        }
      }
    }

  } // end class LexOptions


  public int numStates = -1;

  public LexOptions lexOptions = new LexOptions();

  /**
   * The treebank-specific parser parameters  to use.
   */
  public TreebankLangParserParams tlpParams;

  /**
   * @return The treebank language pack for the treebank the parser
   * is trained on.
   */
  public TreebankLanguagePack langpack() {
    return tlpParams.treebankLanguagePack();
  }


  /**
   * Forces parsing with strictly CNF grammar -- unary chains are converted
   * to XP&YP symbols and back
   */
  public boolean forceCNF = false;

  /**
   * Do a PCFG parse of the sentence.  If both variables are on,
   * also do a combined parse of the sentence.
   */
  public boolean doPCFG = true;

  /**
   * Do a dependency parse of the sentence.
   */
  public boolean doDep = true;

  /**
   * if true, any child can be the head (seems rather bad!)
   */
  public boolean freeDependencies = false;

  /**
   * Whether dependency grammar considers left/right direction. Good.
   */
  public boolean directional = true;
  public boolean genStop = true;

  /**
   * Use distance bins in the dependency calculations
   */
  public boolean distance = true;
  /**
   * Use coarser distance (4 bins) in dependency calculations
   */
  public boolean coarseDistance = false;

  /**
   * "double count" tags rewrites as word in PCFG and Dep parser.  Good for
   * combined parsing only (it used to not kick in for PCFG parsing).  This
   * option is only used at Test time, but it is now in Options, so the
   * correct choice for a grammar is recorded by a serialized parser.
   * You should turn this off for a vanilla PCFG parser.
   */
  public boolean dcTags = true;

  /**
   * If true, inside the factored parser, remove any node from the final
   * chosen tree which improves the PCFG score. This was added as the
   * dependency factor tends to encourage 'deep' trees.
   */
  public boolean nodePrune = false;


  public void display() {
//    try {
      System.err.println("Options parameters:");
      writeData(new PrintWriter(System.err));
      if (Train.printStates) {
        dumpNumberer(Numberer.getGlobalNumberer("states"), "states",
                     tlpParams.pw(System.err));
      }
/*    } catch (IOException e) {
      e.printStackTrace();
    }*/
  }

  public void writeData(Writer w) {//throws IOException {
    numStates = Numberer.getGlobalNumberer("states").total();
    PrintWriter out = new PrintWriter(w);
    StringBuilder sb = new StringBuilder();
    sb.append("numStates ").append(numStates).append("\n");
    sb.append(lexOptions.toString());
    sb.append("parserParams ").append(tlpParams.getClass().getName()).append("\n");
    sb.append("forceCNF ").append(forceCNF).append("\n");
    sb.append("doPCFG ").append(doPCFG).append("\n");
    sb.append("doDep ").append(doDep).append("\n");
    sb.append("freeDependencies ").append(freeDependencies).append("\n");
    sb.append("directional ").append(directional).append("\n");
    sb.append("genStop ").append(genStop).append("\n");
    sb.append("distance ").append(distance).append("\n");
    sb.append("coarseDistance ").append(coarseDistance).append("\n");
    sb.append("dcTags ").append(dcTags).append("\n");
    sb.append("nPrune ").append(nodePrune).append("\n");
    out.print(sb.toString());
    out.flush();
  }

  @SuppressWarnings("unchecked")
  private static void dumpNumberer(Numberer num, String name, PrintWriter pw) {
    pw.println("### Sorted contents of " + name);
    List<Comparable> lis = new ArrayList<Comparable>(ErasureUtils.<Collection<Comparable>>uncheckedCast(num.objects()));
    Collections.sort(lis);
    for (Object obj : lis) {
      pw.println(obj);
    }
    pw.println("### End sorted contents of " + name);
    pw.flush();
  }


  /**
   * Populates data in this Options from the character stream.
   * @param in The Reader
   * @throws IOException If there is a problem reading data
   */
  public void readData(BufferedReader in) throws IOException {
    String line, value;
    // skip old variables if still present
    do {
      line = in.readLine();
    } while (line != null && ! line.matches("^numStates.*"));
    if (line == null) {
      throw new IOException("Bad Options format: no numStates");
    }
    value = line.substring(line.indexOf(' ') + 1);
    numStates = Integer.parseInt(value);
    lexOptions.readData(in);
    line = in.readLine();
    value = line.substring(line.indexOf(' ') + 1);
    try {
      tlpParams = (TreebankLangParserParams) Class.forName(value).newInstance();
    } catch (Exception e) {
      IOException ioe = new IOException("Problem instantiating parserParams: " + line);
      ioe.initCause(e);
      throw ioe;
    }
    line = in.readLine();
    // ensure backwards compatibility
    if (line.matches("^forceCNF.*")) {
      value = line.substring(line.indexOf(' ') + 1);
      forceCNF = Boolean.parseBoolean(value);
      line = in.readLine();
    }
    value = line.substring(line.indexOf(' ') + 1);
    doPCFG = Boolean.parseBoolean(value);
    line = in.readLine();
    value = line.substring(line.indexOf(' ') + 1);
    doDep = Boolean.parseBoolean(value);
    line = in.readLine();
    value = line.substring(line.indexOf(' ') + 1);
    freeDependencies = Boolean.parseBoolean(value);
    line = in.readLine();
    value = line.substring(line.indexOf(' ') + 1);
    directional = Boolean.parseBoolean(value);
    line = in.readLine();
    value = line.substring(line.indexOf(' ') + 1);
    genStop = Boolean.parseBoolean(value);
    line = in.readLine();
    value = line.substring(line.indexOf(' ') + 1);
    distance = Boolean.parseBoolean(value);
    line = in.readLine();
    value = line.substring(line.indexOf(' ') + 1);
    coarseDistance = Boolean.parseBoolean(value);
    line = in.readLine();
    value = line.substring(line.indexOf(' ') + 1);
    dcTags = Boolean.parseBoolean(value);
    line = in.readLine();
    if ( ! line.matches("^nPrune.*")) {
      throw new RuntimeException("Expected nPrune, found: " + line);
    }
    value = line.substring(line.indexOf(' ') + 1);
    nodePrune = Boolean.parseBoolean(value);
    line = in.readLine(); // get rid of last line
    if (line.length() != 0) {
      throw new RuntimeException("Expected blank line, found: " + line);
    }
  }

  private static final long serialVersionUID = 4L;

} // end class Options
