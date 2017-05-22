package edu.stanford.nlp.parser.lexparser;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;
import java.util.regex.*;

import edu.stanford.nlp.international.arabic.Buckwalter;
import edu.stanford.nlp.io.RuntimeIOException;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.metrics.AbstractEval;
import edu.stanford.nlp.process.SerializableFunction;
import edu.stanford.nlp.process.WordSegmentingTokenizer;
import edu.stanford.nlp.process.WordSegmenter;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.international.arabic.*;
import edu.stanford.nlp.trees.tregex.*;
import edu.stanford.nlp.util.Filter;
import edu.stanford.nlp.util.Function;
import edu.stanford.nlp.util.Pair;


/**
 * A {@link TreebankLangParserParams} implementing class for
 * the Penn Arabic Treebank.  The baseline feature set works with either
 * UTF-8 or Buckwalter input, although the behavior of some unused features depends
 * on the input encoding.
 *
 * @author Roger Levy
 * @author Christopher Manning
 * @author Spence Green
 */
public class ArabicTreebankParserParams extends AbstractTreebankParserParams {

  private String optionsString = "ArabicTreebankParserParams\n";

  private boolean retainNPTmp = false;
  private boolean retainNPSbj = false;
  private boolean retainPRD = false;
  private boolean retainPPClr = false;
  private boolean changeNoLabels = false;
  private boolean collinizerRetainsPunctuation = false;
  private Pattern collinizerPruneRegex = null;
  private boolean discardX = false;

  private static final String[] EMPTY_STRING_ARRAY = new String[0];
  private static final TregexPatternCompiler tregexPatternCompiler = new TregexPatternCompiler(new ArabicHeadFinder());

  public ArabicTreebankParserParams() {
    super(new ArabicTreebankLanguagePack());

    initializeAnnotationPatterns();
  }

  /**
   * Creates an {@link ArabicTreeReaderFactory} with parameters set
   * via options passed in from the command line.
   *
   * @return An {@link ArabicTreeReaderFactory}
   */
  public TreeReaderFactory treeReaderFactory() {
    return new ArabicTreeReaderFactory(retainNPTmp, retainPRD,
        changeNoLabels, discardX,
        retainNPSbj, false, retainPPClr);
  }

  @Override
  public AbstractEval ppAttachmentEval() {
    // done by reflection so that Stanford Parser doesn't require libraries
    try {
      return (AbstractEval) Class.forName("edu.stanford.nlp.parser.lexparser.ArabicAttachmentEval").newInstance();
    } catch (ClassNotFoundException cnfe) {
      return null;
    } catch (IllegalAccessException iae) {
      return null;
    } catch (InstantiationException ie) {
      return null;
    }
  }

  //NOTE (WSG): This method is called by main() to load the test treebank
  @Override
  public MemoryTreebank memoryTreebank() {
    return new MemoryTreebank(treeReaderFactory());
  }

  //NOTE (WSG): This method is called to load the training treebank
  @Override
  public DiskTreebank diskTreebank() {
    return new DiskTreebank(treeReaderFactory());
  }

  @Override
  public HeadFinder headFinder() {
    return new ArabicHeadFinder(treebankLanguagePack());
  }

  /**
   * Returns a lexicon for Arabic.  At the moment this is just a BaseLexicon.
   *
   * @return A lexicon
   */
  @Override
  public Lexicon lex() {
    return new BaseLexicon();
  }

  /**
   * Returns a lexicon for Arabic.  At the moment this is just a BaseLexicon.
   *
   * @param op Lexicon options
   * @return A Lexicon
   */
  @Override
  public Lexicon lex(Options.LexOptions op) {
    if(op.uwModel == null) {
      op.uwModel = "edu.stanford.nlp.parser.lexparser.ArabicUnknownWordModel";
    }
    return new BaseLexicon(op);
  }

  /** 
   * There are two differences between this "Collinizer" and TreeCollinizer:
   *  <ul>
   *  <li>Implementation of collinizerPruneRegex.</li>
   *  <li>A punctuation rejection filter that is sensitive to mis-labeled leaves.</li>
   *  </ul>
   */
  private static class ArabicCollinizer implements TreeTransformer, Serializable {

    private TreebankLanguagePack tlp;
    private boolean retainPunctuation;
    private Pattern collinizerPruneRegex;
    private Filter<Tree> punctuationRejecter;

    public ArabicCollinizer(TreebankLanguagePack tlp,
        boolean retainPunctuation, Pattern collinizerPruneRegex) {
      this.tlp = tlp;
      this.retainPunctuation = retainPunctuation;
      this.collinizerPruneRegex = collinizerPruneRegex;
      punctuationRejecter = new PunctuationTreeRejectFilter(tlp);
    }

    public Tree transformTree(Tree t) {
      if (tlp.isStartSymbol(t.value())) {
        t = t.firstChild();
      }
      Tree result = t.deepCopy();
      result = result.prune(new Filter<Tree>() {
        private static final long serialVersionUID = 1669994102700201499L;

        public boolean accept(Tree tree) {
          return collinizerPruneRegex == null || tree.label() == null || ! collinizerPruneRegex.matcher(tree.label().value()).matches();
        }
      });
      if (result == null) {
        return null;
      }
      for (Tree node : result) {
        // System.err.print("ATB collinizer: " + node.label().value()+" --> ");
        if (node.label() != null && ! node.isLeaf()) {
          node.label().setValue(tlp.basicCategory(node.label().value()));
        }
        if (node.label().value().equals("ADVP")) {
          node.label().setValue("PRT");
        }
        // System.err.println(node.label().value());
      }
      if (retainPunctuation) {
        return result;
      } else {
        return result.prune(punctuationRejecter);
      }
    }

    private static final long serialVersionUID = 730039284985950249L;
  }

  /** This Filter&lt;Tree&gt; doesn't accept punctuation pre-terminal
   *  (POS tag) nodes according to a {@link TreebankLanguagePack} examining the
   *  node's value().
   *  <p>
   *  It seems like this is a facility that should be promoted to the trees
   *  package.
   */
  private static class PunctuationTreeRejectFilter implements Filter<Tree> {

    private final Filter<String> punctLabelFilter;

    private static final Pattern utf8ArabicNonPuncChars = Pattern.compile("[\u0621-\u063F\u0641-\u0669]+");

    PunctuationTreeRejectFilter(TreebankLanguagePack tlp) {
      punctLabelFilter = tlp.punctuationTagRejectFilter();
    }

    /**
     * Unlike the PTB, the ATB uses a PUNC tag for all punctuation. We don't want to strip
     * leaves mis-labeled as PUNC by the parser. Presently, we retain a PUNC leaf if it
     * contains at least one non-punctuation character.
     * <p>
     * TODO: We need to add support for Buckwalter trees.
     */
    public boolean accept(Tree tree) {
      if(tree.isPreTerminal()) {
        Matcher m = utf8ArabicNonPuncChars.matcher(tree.firstChild().value());
        return punctLabelFilter.accept(tree.value()) || m.find();
      }
      return true;
    }

    private static final long serialVersionUID = -8181189532150691093L;
  }

  protected class ArabicSubcategoryStripper implements TreeTransformer {

    protected final TreeFactory tf = new LabeledScoredTreeFactory();

    public Tree transformTree(Tree tree) {
      Label lab = tree.label();
      String s = lab.value();

      String tag = null;
      if (lab instanceof HasTag) {
        tag = ((HasTag) lab).tag();
      }

      if (tree.isLeaf()) {
        Tree leaf = tf.newLeaf(lab);
        leaf.setScore(tree.score());
        return leaf;
      } else if(tree.isPhrasal()) {
        if(retainNPTmp && s.startsWith("NP-TMP")) {
          s = "NP-TMP";
        } else if(retainNPSbj && s.startsWith("NP-SBJ")) {
          s = "NP-SBJ";
        } else if(retainPRD && s.matches("VB[^P].*PRD.*")) {
          s = tlp.basicCategory(s);
          s += "-PRD";
        } else {
          s = tlp.basicCategory(s);
        }
      } else if(tree.isPreTerminal()) {
        s = tlp.basicCategory(s);
        if(tag != null)
          tag = tlp.basicCategory(tag);
      } else {
        System.err.printf("Encountered a non-leaf/phrasal/pre-terminal node %s\n",s);
        //Normalize by default
        s = tlp.basicCategory(s);
      }

      int numKids = tree.numChildren();
      List<Tree> children = new ArrayList<Tree>(numKids);
      for (int cNum = 0; cNum < numKids; cNum++) {
        Tree child = tree.getChild(cNum);
        Tree newChild = transformTree(child);
        children.add(newChild);
      }

      CategoryWordTag newLabel = new CategoryWordTag(lab);
      newLabel.setCategory(s);
      if(tag != null)
        newLabel.setTag(tag);

      Tree node = tf.newTreeNode(newLabel, children);
      node.setScore(tree.score());

      return node;
    }
  }

  /**
   * Returns a TreeTransformer that retains categories
   * according to the following options supported by setOptionFlag:
   * <p>
   * <code>-retainNPTmp</code> Retain temporal NP marking on NPs.
   * <code>-retainNPSbj</code> Retain NP subject function tags
   * <code>-markPRDverbs</code> Retain PRD verbs.
   * </p>
   */
  //NOTE (WSG): This is applied to both the best parse by getBestParse()
  //and to the gold eval tree by testOnTreebank()
  @Override
  public TreeTransformer subcategoryStripper() {
    return new ArabicSubcategoryStripper();
  }


  /**
   * The collinizer eliminates punctuation
   */
  @Override
  public TreeTransformer collinizer() {
    // return new TreeCollinizer(tlp, true, false);
    return new ArabicCollinizer(tlp, collinizerRetainsPunctuation, collinizerPruneRegex);
  }

  /**
   * Stand-in collinizer does nothing to the tree.
   */
  @Override
  public TreeTransformer collinizerEvalb() {
    return collinizer();
  }

  @Override
  public String[] sisterSplitters() {
    return EMPTY_STRING_ARRAY;
  }

  private HashMap<TregexPattern,Function<TregexMatcher,String>> activeAnnotations = new HashMap<TregexPattern,Function<TregexMatcher,String>>();

  @Override
  public Tree transformTree(Tree t, Tree root) {

    StringBuilder newCategory = new StringBuilder(t.label().value());
    for (Map.Entry<TregexPattern,Function<TregexMatcher,String>> e : activeAnnotations.entrySet()) {
      TregexMatcher m = e.getKey().matcher(root);
      if (m.matchesAt(t)) {
        newCategory.append(e.getValue().apply(m));
        //      System.out.println("node match " + e.getValue()); //testing
        //      t.pennPrint(); //testing
      }
    }
    String newCat = newCategory.toString();
    t.label().setValue(newCat);
    // cdm Mar 2005: the equivalent of the below wasn't being done in the old
    // code, but it really needs to be!
    if (t.isPreTerminal()) {
      HasTag lab = (HasTag) t.label();
      lab.setTag(newCat);
    }
    //  t.pennPrint(); //testing

    return t;
  }

  @Override
  public void display() {
    System.err.println(optionsString);
  }


  /**
   * These are the annotations included when the user selects the -arabicFactored option.
   */
  private static final List<String> baselineAnnotations = new ArrayList<String>();
  static {
    baselineAnnotations.add("-discardX");
    baselineAnnotations.add("-markNounNPargTakers");
    baselineAnnotations.add("-genitiveMark");
    baselineAnnotations.add("-splitPUNC");
    baselineAnnotations.add("-markContainsVerb");
    baselineAnnotations.add("-markStrictBaseNP");
    baselineAnnotations.add("-markOneLevelIdafa");
    baselineAnnotations.add("-splitIN");
    baselineAnnotations.add("-markMasdarVP");
    
    //Added since COLING paper
    baselineAnnotations.add("-containsSVO");
    baselineAnnotations.add("-splitCC");
    baselineAnnotations.add("-markFem");
  }


  private Map<String,Pair<TregexPattern,Function<TregexMatcher,String>>> annotationPatterns = new HashMap<String,Pair<TregexPattern,Function<TregexMatcher,String>>>();


  /** This doesn't/can't really pick out genitives,
   *  but just any NP following an NN head.
   */
  private static final String genitiveNodeTregexString = "@NP > @NP $- /^N/";

  private void initializeAnnotationPatterns() {
    try {
      // ******************
      // Baseline features
      // ******************
      annotationPatterns.put("-genitiveMark",new Pair<TregexPattern,Function<TregexMatcher,String>>(TregexPattern.compile(genitiveNodeTregexString),new SimpleStringFunction("-genitive")));
      annotationPatterns.put("-markStrictBaseNP",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP !< (__ < (__ < __))"),new SimpleStringFunction("-base"))); // NP with no phrasal node in it
      annotationPatterns.put("-markOneLevelIdafa",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP < (@NP < (__ < __)) !< (/^[^N]/ < (__ < __)) !< (__ < (__ < (__ < __)))"),new SimpleStringFunction("-idafa1")));
      annotationPatterns.put("-markNounNPargTakers",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NN|NNS|NNP|NNPS|DTNN|DTNNS|DTNNP|DTNNPS ># (@NP < @NP)"),new SimpleStringFunction("-NounNParg")));
      annotationPatterns.put("-markContainsVerb",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ << (/^[CIP]?V/ < (__ !< __))"),new SimpleStringFunction("-withV")));
      annotationPatterns.put("-splitIN",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@IN < __=word"), new AddRelativeNodeFunction("-","word", false)));
      annotationPatterns.put("-splitPUNC",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@PUNC < __=" + AnnotatePunctuationFunction.key),new AnnotatePunctuationFunction()));
      annotationPatterns.put("-markMasdarVP", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@VP < @VBG|VN"), new SimpleStringFunction("-masdar")));

      //Features for inclusion in baseline
      annotationPatterns.put("-containsSVO", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ << (@S < (@NP . @VP))"), new SimpleStringFunction("-hasSVO")));
      annotationPatterns.put("-splitCC",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@CC|CONJ . __=term , __"),new AddEquivalencedConjNode("-","term")));
      annotationPatterns.put("-markFem", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ < /ة$/"), new SimpleStringFunction("-fem")));

      //Features under test (promising; could tweak)
      annotationPatterns.put("-splitCC1",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@CC|CONJ < __=term"),new AddRelativeNodeRegexFunction("-","term", "-*([^-].*)")));
      annotationPatterns.put("-splitCC2",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@CC . __=term , __"),new AddRelativeNodeFunction("-","term", true)));
      annotationPatterns.put("-idafaJJ1", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP <, (@NN $+ @NP) <+(@NP) @ADJP"), new SimpleStringFunction("-idafaJJ")));
      annotationPatterns.put("-idafaJJ2", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP <, (@NN $+ @NP) <+(@NP) @ADJP !<< @SBAR"), new SimpleStringFunction("-idafaJJ")));

      
      //Didn't work
      annotationPatterns.put("-properBaseNP", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP !<< @NP < /NNP/ !< @PUNC|CD"), new SimpleStringFunction("-prop")));
      annotationPatterns.put("-interrog", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ << هل|ماذا|لماذا|اين|متى"), new SimpleStringFunction("-inter")));
      annotationPatterns.put("-splitPseudo", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NN < مع|بعد|بين"), new SimpleStringFunction("-pseudo")));
      annotationPatterns.put("-nPseudo", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP < (@NN < مع|بعد|بين)"), new SimpleStringFunction("-npseudo")));
      annotationPatterns.put("-pseudoArg", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP < @NP $, (@NN < مع|بعد|بين)"), new SimpleStringFunction("-pseudoArg")));
      annotationPatterns.put("-eqL1", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ < (@S !< @VP|S)"), new SimpleStringFunction("-haseq")));
      annotationPatterns.put("-eqL1L2", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ < (__ < (@S !< @VP|S)) | < (@S !< @VP|S)"), new SimpleStringFunction("-haseq")));
      annotationPatterns.put("-fullQuote", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ < ((@PUNC < \") $ (@PUNC < \"))"), new SimpleStringFunction("-fq")));
      annotationPatterns.put("-brokeQuote", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ < ((@PUNC < \") !$ (@PUNC < \"))"), new SimpleStringFunction("-bq")));
      annotationPatterns.put("-splitVP", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@VP <# __=term1"), new AddRelativeNodeFunction("-","term1",true)));
      annotationPatterns.put("-markFemP", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP|ADJP < (__ < /ة$/)"), new SimpleStringFunction("-femP")));      
      annotationPatterns.put("-embedSBAR", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP|PP <+(@NP|PP) @SBAR"), new SimpleStringFunction("-embedSBAR")));
      annotationPatterns.put("-complexVP", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ << (@VP < (@NP $ @NP)) > __"), new SimpleStringFunction("-complexVP")));
      annotationPatterns.put("-containsJJ", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP <+(@NP) /JJ/"), new SimpleStringFunction("-hasJJ")));
      annotationPatterns.put("-markMasdarVP2", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ << @VN|VBG"), new SimpleStringFunction("-masdar")));
      annotationPatterns.put("-coordNP", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP|ADJP <+(@NP|ADJP) (@CC|PUNC $- __ $+ __)"), new SimpleStringFunction("-coordNP")));
      annotationPatterns.put("-coordWa", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ << (@CC , __ < و-)"), new SimpleStringFunction("-coordWA")));
      annotationPatterns.put("-NPhasADJP", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP <+(@NP) @ADJP"), new SimpleStringFunction("-NPhasADJP")));
      annotationPatterns.put("-NPADJP", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP < @ADJP"), new SimpleStringFunction("-npadj")));
      annotationPatterns.put("-NPJJ", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP < /JJ/"), new SimpleStringFunction("-npjj")));
      annotationPatterns.put("-NPCC", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP <+(@NP) @CC"), new SimpleStringFunction("-npcc")));
      annotationPatterns.put("-NPCD", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP < @CD"), new SimpleStringFunction("-npcd")));
      annotationPatterns.put("-NPNNP", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP < /NNP/"), new SimpleStringFunction("-npnnp")));
      annotationPatterns.put("-SVO", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@S < (@NP . @VP)"), new SimpleStringFunction("-svo")));
      annotationPatterns.put("-containsSBAR", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ << @SBAR"), new SimpleStringFunction("-hasSBAR")));


      //WSGDEBUG - Template
      //annotationPatterns.put("", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile(""), new SimpleStringFunction("")));


      // ************
      // Old and unused features (in various states of repair)
      // *************
      annotationPatterns.put("-markGappedVP",new Pair<TregexPattern,Function<TregexMatcher,String>>(TregexPattern.compile("@VP > @VP $- __ $ /^(?:CC|CONJ)/ !< /^V/"),new SimpleStringFunction("-gappedVP")));
      annotationPatterns.put("-markGappedVPConjoiners",new Pair<TregexPattern,Function<TregexMatcher,String>>(TregexPattern.compile("/^(?:CC|CONJ)/ $ (@VP > @VP $- __ !< /^V/)"),new SimpleStringFunction("-gappedVP")));
      annotationPatterns.put("-markGenitiveParent",new Pair<TregexPattern,Function<TregexMatcher,String>>(TregexPattern.compile("@NP < (" + genitiveNodeTregexString + ')'),new SimpleStringFunction("-genitiveParent")));
      // maSdr: this pattern is just a heuristic classification, which matches on
      // various common maSdr pattterns, but probably also matches on a lot of other
      // stuff.  It marks NPs with possible maSdr.
      // Roger's old pattern:
      annotationPatterns.put("-maSdrMark",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("/^N/ <<# (/^[t\\u062a].+[y\\u064a].$/ > @NN|NOUN|DTNN)"),new SimpleStringFunction("-maSdr")));
      // chris' attempt
      annotationPatterns.put("-maSdrMark2",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("/^N/ <<# (/^(?:[t\\u062a].+[y\\u064a].|<.{3,}|A.{3,})$/ > @NN|NOUN|DTNN)"),new SimpleStringFunction("-maSdr")));
      annotationPatterns.put("-maSdrMark3",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("/^N/ <<# (/^(?:[t\\u062a<A].{3,})$/ > @NN|NOUN|DTNN)"),new SimpleStringFunction("-maSdr")));
      annotationPatterns.put("-maSdrMark4",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("/^N/ <<# (/^(?:[t\\u062a<A].{3,})$/ > (@NN|NOUN|DTNN > (@NP < @NP)))"),new SimpleStringFunction("-maSdr")));
      annotationPatterns.put("-maSdrMark5",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("/^N/ <<# (__ > (@NN|NOUN|DTNN > (@NP < @NP)))"),new SimpleStringFunction("-maSdr")));
      annotationPatterns.put("-mjjMark",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@JJ|DTJJ < /^m/ $+ @PP ># @ADJP "),new SimpleStringFunction("-mjj")));
      //annotationPatterns.put(markPRDverbString,new Pair<TregexPattern,Function<TregexMatcher,String>>(TregexPattern.compile("/^V[^P]/ > VP $ /-PRD$/"),new SimpleStringFunction("-PRDverb"))); // don't need this pattern anymore, the functionality has been moved to ArabicTreeNormalizer
      // PUNC is PUNC in either raw or Bies POS encoding
      annotationPatterns.put("-markNPwithSdescendant",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ !< @S << @S [ >> @NP | == @NP ]"),new SimpleStringFunction("-inNPdominatesS")));
      annotationPatterns.put("-markRightRecursiveNP",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ <<- @NP [>>- @NP | == @NP]"),new SimpleStringFunction("-rrNP")));
      annotationPatterns.put("-markBaseNP",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP !< @NP !< @VP !< @SBAR !< @ADJP !< @ADVP !< @S !< @QP !< @UCP !< @PP"),new SimpleStringFunction("-base")));
      // allow only a single level of idafa as Base NP; this version works!
      annotationPatterns.put("-markBaseNPplusIdafa",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP !< (/^[^N]/ < (__ < __)) !< (__ < (__ < (__ < __)))"),new SimpleStringFunction("-base")));
      annotationPatterns.put("-markTwoLevelIdafa",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP < (@NP < (@NP < (__ < __)) !< (/^[^N]/ < (__ < __))) !< (/^[^N]/ < (__ < __)) !< (__ < (__ < (__ < (__ < __))))"),new SimpleStringFunction("-idafa2")));
      annotationPatterns.put("-markDefiniteIdafa",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP < (/^(?:NN|NOUN)/ !$,, /^[^AP]/) <+(/^NP/) (@NP < /^DT/)"), new SimpleStringFunction("-defIdafa")));
      annotationPatterns.put("-markDefiniteIdafa1",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP < (/^(?:NN|NOUN)/ !$,, /^[^AP]/) < (@NP < /^DT/) !< (/^[^N]/ < (__ < __)) !< (__ < (__ < (__ < __)))"), new SimpleStringFunction("-defIdafa1")));
      annotationPatterns.put("-markContainsSBAR",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ << @SBAR"),new SimpleStringFunction("-withSBAR")));
      annotationPatterns.put("-markPhrasalNodesDominatedBySBAR",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ < (__ < __) >> @SBAR"),new SimpleStringFunction("-domBySBAR")));
      annotationPatterns.put("-markCoordinateNPs",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP < @CC|CONJ"),new SimpleStringFunction("-coord")));
      //annotationPatterns.put("-markCopularVerbTags",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("/^V/ < " + copularVerbForms),new SimpleStringFunction("-copular")));
      //annotationPatterns.put("-markSBARVerbTags",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("/^V/ < " + sbarVerbForms),new SimpleStringFunction("-SBARverb")));
      annotationPatterns.put("-markNounAdjVPheads",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NN|NNS|NNP|NNPS|JJ|DTJJ|DTNN|DTNNS|DTNNP|DTNNPS ># @VP"),new SimpleStringFunction("-VHead")));
      // a better version of the below might only mark clitic pronouns, but
      // since most pronouns are clitics, let's try this first....
      annotationPatterns.put("-markPronominalNP",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP < @PRP"),new SimpleStringFunction("-PRP")));
      // try doing coordination parallelism -- there's a lot of that in Arabic (usually the same, sometimes different CC)
      annotationPatterns.put("-markMultiCC", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ < (@CC $.. @CC)"), new SimpleStringFunction("-multiCC"))); // this unfortunately didn't seem helpful for capturing CC parallelism; should try again
      annotationPatterns.put("-markHasCCdaughter", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ < @CC"), new SimpleStringFunction("-CCdtr")));
      annotationPatterns.put("-markAcronymNP",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@NP !<  (__ < (__ < __)) < (/^NN/ < /^.$/ $ (/^NN/ < /^.$/)) !< (__ < /../)"), new SimpleStringFunction("-acro")));
      annotationPatterns.put("-markAcronymNN",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("/^NN/ < /^.$/ $ (/^NN/ < /^.$/) > (@NP !<  (__ < (__ < __)) !< (__ < /../))"), new SimpleStringFunction("-acro")));
      //PP Specific patterns
      annotationPatterns.put("-markPPwithPPdescendant",new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ !< @PP << @PP [ >> @PP | == @PP ]"),new SimpleStringFunction("-inPPdominatesPP")));
      annotationPatterns.put("-gpAnnotatePrepositions",new Pair<TregexPattern,Function<TregexMatcher,String>>(TregexPattern.compile("/^(?:IN|PREP)$/ > (__ > __=gp)"),new AddRelativeNodeFunction("^^","gp", false)));
      annotationPatterns.put("-gpEquivalencePrepositions",new Pair<TregexPattern,Function<TregexMatcher,String>>(TregexPattern.compile("/^(?:IN|PREP)$/ > (@PP >+(/^PP/) __=gp)"),new AddEquivalencedNodeFunction("^^","gp")));
      annotationPatterns.put("-gpEquivalencePrepositionsVar",new Pair<TregexPattern,Function<TregexMatcher,String>>(TregexPattern.compile("/^(?:IN|PREP)$/ > (@PP >+(/^PP/) __=gp)"),new AddEquivalencedNodeFunctionVar("^^","gp")));
      annotationPatterns.put("-markPPParent", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@PP=max !< @PP"),new AddRelativeNodeRegexFunction("^^","max","^(\\w)")));
      annotationPatterns.put("-whPP", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@PP <- (@SBAR <, /^WH/)"),new SimpleStringFunction("-whPP")));
      //    annotationPatterns.put("-markTmpPP", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@PP !<+(__) @PP"),new LexicalCategoryFunction("-TMP",temporalNouns)));
      annotationPatterns.put("-deflateMin", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("__ < (__ < من)"),new SimpleStringFunction("-min")));
      annotationPatterns.put("-v2MarkovIN", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@IN > (@__=p1 > @__=p2)"),new AddRelativeNodeFunction("^","p1","p2", false)));
      annotationPatterns.put("-pleonasticMin", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@PP <, (IN < من) > @S"),new SimpleStringFunction("-pleo")));
      annotationPatterns.put("-v2MarkovPP", new Pair<TregexPattern,Function<TregexMatcher,String>>(tregexPatternCompiler.compile("@PP > (@__=p1 > @__=p2)"), new AddRelativeNodeFunction("^","p1","p2", false)));

    } catch (ParseException e) {
      int nth = annotationPatterns.size() + 1;
      String nthStr = (nth == 1) ? "1st": ((nth == 2) ? "2nd": nth + "th");
      System.err.println("Parse exception on " + nthStr + " annotation pattern initialization:" + e);
    }
  }

  // these still need to be turned into UTF-8....
  // Also, for >, coding is different for IBM Arabic vs. raw Arabic.  Yuck.
  //private static final String copularVerbForms = "/^(kAn|kAnt|ykwn|sykwn|tkwn|ykn|stkwn|ykwnw|ybdw|tbdw|sybdw|stbdw|bdY|ybdy|tbdy|stbdy|sybdy)$/";
  private static final String sbarVerbForms = "/^(qAl|\\>DAf|AEln|\\>wDH|ymkn|\\>Eln|\\*krt|\\>kd|AElnt|Akd|qAlt|\\>DAft|AfAd|y\\*kr|yjb|\\{Etbr|\\>wDHt|AEtbr|sbq|\\*kr|tAbE|nqlt|SrH|r\\>Y|\\>fAd|AfAdt|yqwl|\\>kdt|\\>Elnt|Akdt|yrY|tEtbr|AEtqd|yEtbr|tfyd|ytwqE|AEtbrt|ynbgy|Tlbt|qrr|ktbt|\\>blg|\\>\\$Ar|ywDH|t\\&kd|Tlb|r\\>t|yEny|nryd|nEtbr|yftrD|k\\$f|\\{Etbrt|AwDH|ytEyn|ykfy|y\\&kd|yErf|ydrk|tZhr|tqwl|tbd\\>|nEtqd|nErf|AErf|Elm|Awrdt|AwDHt|AqtrH|yryd|yErfAn|yElm|ybd\\>tstTyE|tHAwl|tEny|nrY|n\\>ml|)$/";


  private static class SimpleStringFunction implements SerializableFunction<TregexMatcher,String> {

    public SimpleStringFunction(String result) {
      this.result = result;
    }

    private String result;

    public String apply(TregexMatcher tregexMatcher) {
      return result;
    }

    @Override
    public String toString() {
      return "SimpleStringFunction[" + result + ']';
    }

    private static final long serialVersionUID = 1L;

  }


  private static class AddRelativeNodeFunction implements SerializableFunction<TregexMatcher,String> {

    private String annotationMark;
    private String key;
    private String key2;
    private boolean doBasicCat = false;
  	
    //TODO WSGDEBUG Should change the member tlp to static so that we don't have to do this
    private static final TreebankLanguagePack tlp = new ArabicTreebankLanguagePack();

    public AddRelativeNodeFunction(String annotationMark, String key, boolean basicCategory) {
      this.annotationMark = annotationMark;
      this.key = key;
      this.key2 = null;
      doBasicCat = basicCategory;
    }

    public AddRelativeNodeFunction(String annotationMark, String key1, String key2, boolean basicCategory) {
      this(annotationMark,key1,basicCategory);
    	this.key2 = key2;
    }

    public String apply(TregexMatcher m) {
      if(key2 == null)
        return annotationMark + ((doBasicCat) ? tlp.basicCategory(m.getNode(key).label().value()) : m.getNode(key).label().value());
      else {
      	String annot1 = (doBasicCat) ? tlp.basicCategory(m.getNode(key).label().value()) : m.getNode(key).label().value();
      	String annot2 = (doBasicCat) ? tlp.basicCategory(m.getNode(key2).label().value()) : m.getNode(key2).label().value();
      	return annotationMark + annot1 + annotationMark + annot2;
    }
    }

    @Override
    public String toString() {
      if(key2 == null)
        return "AddRelativeNodeFunction[" + annotationMark + ',' + key + ']';
      else
        return "AddRelativeNodeFunction[" + annotationMark + ',' + key + ',' + key2 + ']';
    }

    private static final long serialVersionUID = 1L;

  }


  private static class AddRelativeNodeRegexFunction implements SerializableFunction<TregexMatcher,String> {

    private String annotationMark;
    private String key;
    private Pattern pattern;

    private String key2 = null;
    private Pattern pattern2;
    
    public AddRelativeNodeRegexFunction(String annotationMark, String key, String regex) {
      this.annotationMark = annotationMark;
      this.key = key;
      try {
        this.pattern = Pattern.compile(regex);
      } catch (PatternSyntaxException pse) {
        System.err.println("Bad pattern: " + regex);
        pattern = null;
      }
    }
    
    public AddRelativeNodeRegexFunction(String annotationMark, String key, String regex, String key2, String regex2) {
      this(annotationMark,key,regex);
    	this.key2 = key2;
      try {
        this.pattern2 = Pattern.compile(regex2);
      } catch (PatternSyntaxException pse) {
        System.err.println("Bad pattern: " + regex2);
        pattern2 = null;
      }
    }
    
    public String apply(TregexMatcher m) {
      String val = m.getNode(key).label().value();
      if (pattern != null) {
        Matcher mat = pattern.matcher(val);
        if (mat.find()) {
          val = mat.group(1);
        }
      }
      
      if(key2 != null && pattern2 != null) {
      	String val2 = m.getNode(key2).label().value();
      	Matcher mat2 = pattern2.matcher(val2);
      	if(mat2.find()) {
      		val = val + annotationMark + mat2.group(1);
      	} else {
      		val = val + annotationMark + val2;
      	}
      }
      
      return annotationMark + val;
    }

    @Override
    public String toString() {
      return "AddRelativeNodeRegexFunction[" + annotationMark + ',' + key + ',' + pattern + ']';
    }

    private static final long serialVersionUID = 1L;

  }


  /** This one only distinguishes VP, S and Other (mainly nominal) contexts.
   *  These seem the crucial distinctions for Arabic true prepositions,
   *  based on raw counts in data.
   */
  private static class AddEquivalencedNodeFunction implements SerializableFunction<TregexMatcher,String> {

    private String annotationMark;
    private String key;

    public AddEquivalencedNodeFunction(String annotationMark, String key) {
      this.annotationMark = annotationMark;
      this.key = key;
    }

    public String apply(TregexMatcher m) {
      String node = m.getNode(key).label().value();
      if (node.startsWith("S")) {
        return annotationMark + 'S';
      } else if (node.startsWith("V")) {
        return annotationMark + 'V';
      } else {
        return "";
      }
    }

    @Override
    public String toString() {
      return "AddEquivalencedNodeFunction[" + annotationMark + ',' + key + ']';
    }

    private static final long serialVersionUID = 1L;

  }


  /** This one only distinguishes VP, S*, A* versus other (mainly nominal) contexts. */
  private static class AddEquivalencedNodeFunctionVar implements SerializableFunction<TregexMatcher,String> {

    private String annotationMark;
    private String key;

    public AddEquivalencedNodeFunctionVar(String annotationMark, String key) {
      this.annotationMark = annotationMark;
      this.key = key;
    }

    public String apply(TregexMatcher m) {
      String node = m.getNode(key).label().value();
      // We also tried if (node.startsWith("V")) [var2] and if (node.startsWith("V") || node.startsWith("S")) [var3]. Both seemed markedly worse than the basic function or this var form (which seems a bit better than the basic equiv option).
      if (node.startsWith("S") || node.startsWith("V") || node.startsWith("A")) {
        return annotationMark + "VSA";
      } else {
        return "";
      }
    }

    @Override
    public String toString() {
      return "AddEquivalencedNodeFunctionVar[" + annotationMark + ',' + key + ']';
    }

    private static final long serialVersionUID = 1L;

  }

  private static class AnnotatePunctuationFunction implements SerializableFunction<TregexMatcher,String> {
    static final String key = "term";

    private static final Pattern quote = Pattern.compile("^\"$");
    
    public String apply(TregexMatcher m) {
      
    	final String punc = m.getNode(key).value();
      
      if (punc.equals("."))
        return "-fs";
      else if (punc.equals("?"))
      	return "-quest";
      else if (punc.equals(","))
        return "-comma";
      else if (punc.equals(":") || punc.equals(";"))
         return "-colon";
      else if (punc.equals("-LRB-"))
        return "-lrb";
      else if (punc.equals("-RRB-"))
        return "-rrb";
      else if (punc.equals("-PLUS-"))
      	return "-plus";
      else if (punc.equals("-"))
        return "-dash";
      else if (quote.matcher(punc).matches())
        return "-quote";
      //if(slash.matcher(punc).matches())
      //  return "-slash";
      //if(percent.matcher(punc).matches())
      // return "-percent";
      //if(ellipses.matcher(punc).matches())
      //  return "-ellipses";
      return "";
    }

    @Override
    public String toString() {
      return "AnnotatePunctuationFunction";
    }

    private static final long serialVersionUID = 1L;

  }

  private static class AddEquivalencedConjNode implements SerializableFunction<TregexMatcher,String> {

    private String annotationMark;
    private String key;

    private static final String nnTags = "DTNN DTNNP DTNNPS DTNNS NN NNP NNS NNPS";
    private static final Set<String> nnTagClass;
    
    private static final String jjTags = "ADJ_NUM DTJJ DTJJR JJ JJR";
    private static final Set<String> jjTagClass;

    private static final String vbTags = "VBD VBP";
    private static final Set<String> vbTagClass;
    static {
      nnTagClass = new HashSet<String>(Arrays.asList(nnTags.split("\\s+")));
      jjTagClass = new HashSet<String>(Arrays.asList(jjTags.split("\\s+")));
      vbTagClass = new HashSet<String>(Arrays.asList(vbTags.split("\\s+")));
    }
    
    private static final TreebankLanguagePack tlp = new ArabicTreebankLanguagePack();
    
    public AddEquivalencedConjNode(String annotationMark, String key) {
      this.annotationMark = annotationMark;
      this.key = key;
    }

    public String apply(TregexMatcher m) {
      String node = m.getNode(key).value();
      String eqClass = tlp.basicCategory(node);
      
      if(nnTagClass.contains(eqClass))
        eqClass = "noun";
      else if(jjTagClass.contains(eqClass))
        eqClass = "adj";
      else if(vbTagClass.contains(eqClass))
        eqClass = "vb";
      
      return annotationMark + eqClass;
    }

    @Override
    public String toString() { return "AddEquivalencedConjNode[" + annotationMark + ',' + key + ']'; }

    private static final long serialVersionUID = 1L;
  }

  private static class SplitSFunction implements SerializableFunction<TregexMatcher,String> {

  	private static final String annotationMark = "-";
  	private static final String ignoreList = "CC PUNC";
  	private static final Set<String> ignore = new HashSet<String>();
  	static {
  		ignore.addAll(Arrays.asList(ignoreList.split("\\s+")));
  	}
  	private static final TreebankLanguagePack tlp = new ArabicTreebankLanguagePack();
  	
  	public String apply(TregexMatcher m) {
  		final Tree t = m.getMatch();
  		
  		for(int i = 0; i < t.numChildren(); i++) {
  			Tree kid = t.getChild(i);
  			String lab = tlp.basicCategory(kid.value());
  			if(! ignore.contains(lab))
  				return annotationMark + lab;
  		}
  		
	    return "";
    }

    private static final long serialVersionUID = -5897455993871421262L;
  }
  
 

  /** Some options for setOptionFlag:
   *
   * <p>
   * <code>-retainNPTmp</code> Retain temporal NP marking on NPs.
   * <code>-retainNPSbj</code> Retain NP subject function tags
   * <code>-markGappedVP</code> marked gapped VPs.
   * <code>-collinizerRetainsPunctuation</code> does what it says.
   * </p>
   *
   * @param args flag arguments (usually from commmand line
   * @param i index at which to begin argument processing
   * @return Index in args array after the last processed index for option
   */
  @Override
  public int setOptionFlag(String[] args, int i) {
    //System.err.println("Setting option flag: "  + args[i]);

    //lang. specific options
    boolean didSomething = false;
    if (annotationPatterns.keySet().contains(args[i])) {
      Pair<TregexPattern,Function<TregexMatcher,String>> p = annotationPatterns.get(args[i]);
      activeAnnotations.put(p.first(),p.second());
      optionsString += "Option " + args[i] + " added annotation pattern " + p.first() + " with annotation " + p.second() + '\n';
      didSomething = true;
    } else if (args[i].equals("-retainNPTmp")) {
      optionsString += "Retaining NP-TMP marking.\n";
      retainNPTmp = true;
      didSomething = true;
    } else if (args[i].equals("-retainNPSbj")) {
      optionsString += "Retaining NP-SBJ dash tag.\n";
      retainNPSbj = true;
      didSomething = true;
    } else if (args[i].equals("-retainPPClr")) {
      optionsString += "Retaining PP-CLR dash tag.\n";
      retainPPClr = true;
      didSomething = true;
    } else if (args[i].equals("-discardX")) {
      optionsString += "Discarding X trees.\n";
      discardX = true;
      didSomething = true;
    } else if (args[i].equals("-changeNoLabels")) {
      optionsString += "Change no labels.\n";
      changeNoLabels = true;
      didSomething = true;
    } else if (args[i].equals("-markPRDverbs")) {
      optionsString += "Mark PRD.\n";
      retainPRD = true;
      didSomething = true;
    } else if (args[i].equals("-collinizerRetainsPunctuation")) {
      optionsString += "Collinizer retains punctuation.\n";
      collinizerRetainsPunctuation = true;
      didSomething = true;
    } else if (args[i].equals("-collinizerPruneRegex")) {
      optionsString += "Collinizer prune regex: " + args[i+1] + '\n';
      collinizerPruneRegex = Pattern.compile(args[i+1]);
      i++;
      didSomething = true;
    } else if (args[i].equals("-arabicFactored")) {
      for(String annotation : baselineAnnotations) {
        String[] a = {annotation};
        setOptionFlag(a,0);
      }
      didSomething = true;
    } else if (args[i].equals("-arabicTokenizerModel")) {
      String modelFile = args[i+1];
      try {
        WordSegmenter aSeg = (WordSegmenter) Class.forName("edu.stanford.nlp.wordseg.ArabicSegmenter").newInstance();
        aSeg.loadSegmenter(modelFile);
        System.out.println("aSeg=" + aSeg);
        TokenizerFactory<Word> aTF = WordSegmentingTokenizer.factory(aSeg);
        ((ArabicTreebankLanguagePack) treebankLanguagePack()).setTokenizerFactory(aTF);
      } catch (RuntimeIOException ex) {
        System.err.println("Couldn't load ArabicSegmenter " + modelFile);
        ex.printStackTrace();
      } catch (Exception e) {
        System.err.println("Couldn't instantiate segmenter: edu.stanford.nlp.wordseg.ArabicSegmenter");
        e.printStackTrace();
      }
      i++; // 2 args
      didSomething = true;
    }
    if (didSomething) {
      i++;
    }
    return i;
  }

  /**
   * Return a default sentence for the language (for testing).
   * This test sentence is Buckwalter encoded, so it only works if you're
   * using a Buckwalter encoding parser.
   */
  public Sentence<Word> defaultTestSentence() {
    return Sentence.toSentence("w", "lm", "tfd", "mElwmAt", "En",
        "ADrAr", "Aw", "DHAyA", "HtY", "AlAn", ".");
  }

  private static final long serialVersionUID = 1L;


  /**
   * Loads Arabic Treebank files from the first argument and prints all the trees below length specified by second arg.
   * @param args Command line arguments, as above
   */
  public static void main(String[] args) {
    int maxLength = Integer.parseInt(args[1]);
    boolean b2a = false;
    TreebankLangParserParams tlpp = new ArabicTreebankParserParams();
    if (args[2].equals("-b2a")) {
      b2a = true;
    } else {
      tlpp.setOptionFlag(args,2);
    }
    DiskTreebank trees = tlpp.diskTreebank();
    trees.loadPath(args[0]);
    PrintWriter pw = tlpp.pw();
    TreeTransformer mapper = new TreeTransformer() {

      private Buckwalter buck = new Buckwalter();

      public Tree transformTree(Tree t) {
        for (Tree tr : t) {
          if (tr.isLeaf()) {
            //System.err.println("Changing " + tr.value() + " to " +
            //                   buck.buckwalterToUnicode(tr.value()));
            tr.setValue(buck.buckwalterToUnicode(tr.value()));
          }
        }
        return t;
      }
    };

    for (Tree t : trees) {
      if (t.yield().size() <= maxLength) {
        pw.println(t);
        if (b2a) {
          mapper.transformTree(t).pennPrint(pw);
        }
      }
    }
  }

}
