// Stanford Parser -- a probabilistic lexicalized NL CFG parser
// Copyright (c) 2002, 2003, 2004, 2005 The Board of Trustees of
// The Leland Stanford Junior University. All Rights Reserved.
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//
// For more information, bug reports, fixes, contact:
//    Christopher Manning
//    Dept of Computer Science, Gates 1A
//    Stanford CA 94305-9010
//    USA
//    parser-support@lists.stanford.edu
//    http://nlp.stanford.edu/software/lex-parser.shtml

package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.io.NumberRangeFileFilter;
import edu.stanford.nlp.io.NumberRangesFileFilter;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.KBestViterbiParser;
import edu.stanford.nlp.parser.ViterbiParserWithOptions;
import edu.stanford.nlp.parser.metrics.AbstractEval;
import edu.stanford.nlp.parser.metrics.DependencyEval;
import edu.stanford.nlp.parser.metrics.EvalbByCat;
import edu.stanford.nlp.parser.metrics.Evalb;
import edu.stanford.nlp.parser.metrics.TaggingEval;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.util.Function;
import edu.stanford.nlp.process.WhitespaceTokenizer;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.international.arabic.ArabicTreebankLanguagePack;
import edu.stanford.nlp.util.Generics;
import edu.stanford.nlp.util.Numberer;
import edu.stanford.nlp.util.Pair;
import edu.stanford.nlp.util.Timing;
import edu.stanford.nlp.util.ScoredObject;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.lang.reflect.Method;

// Miscellaneous documentation by Roger (please correct any errors in this documentation!)
//
// I believe that a lexicalized parser is always constructed by creating a ParserData object and then calling
// makeParsers().
//

/**
 * This class provides the top-level API and command-line interface to a set
 * of reasonably good treebank-trained parsers.  The name reflects the main
 * factored parsing model, which provides a lexicalized PCFG parser
 * implemented as a product
 * model of a plain PCFG parser and a lexicalized dependency parser.
 * But you can also run either component parser alone.  In particular, it
 * is often useful to do unlexicalized PCFG parsing by using just that
 * component parser.
 * <p>
 * See the package documentation for more details and examples of use.
 * See the main method documentation for details of invoking the parser.
 * <p>
 * Note that training on a 1 million word treebank requires a fair amount of
 * memory to run.  Try -mx1500m.
 *
 * @author Dan Klein (original version)
 * @author Christopher Manning (better features, ParserParams, serialization)
 * @author Roger Levy (internationalization)
 * @author Teg Grenager (grammar compaction, tokenization, etc.)
 * @author Galen Andrew (considerable refactoring)
 */
public class LexicalizedParser implements ViterbiParserWithOptions, Function<Object,Tree> {

  private boolean fallbackToPCFG = false;

  /** The PCFG parser. */
  protected ExhaustivePCFGParser pparser;
  /** The dependency parser. */
  protected ExhaustiveDependencyParser dparser;
  /** The factored parser that combines the dependency and PCFG parsers. */
  protected KBestViterbiParser bparser;
  protected TreeTransformer debinarizer;
  private TreeTransformer subcategoryStripper;

  private static boolean parseSucceeded = false;

  private static int trainLengthLimit = 100000;

  /** The tagger optionally used before parsing. */
  protected static SentenceProcessor<HasWord, Word> tagger;

  private transient ParserData pd;

  public Options getOp() {
    return op;
  }
  private Options op;

  private static final String SERIALIZED_PARSER_PROPERTY = "edu.stanford.nlp.SerializedLexicalizedParser";
  private static final String DEFAULT_PARSER_LOC = "/u/nlp/data/lexparser/englishPCFG.ser.gz";


  /**
   * Construct a new LexicalizedParser object from a previously serialized
   * grammar read from a property
   * <code>edu.stanford.nlp.SerializedLexicalizedParser</code>,
   * or a default file location.
   */
  public LexicalizedParser() {
    this(new Options());
  }

  /**
   * Construct a new LexicalizedParser object from a previously serialized
   * grammar read from a System property
   * <code>edu.stanford.nlp.SerializedLexicalizedParser</code>,
   * or a default file location
   * (<code>/u/nlp/data/lexparser/englishPCFG.ser.gz</code>).
   *
   * @param op Options to the parser.  These get overwritten by the
   *           Options read from the serialized parser; I think the only
   *           thing determined by them is the encoding of the grammar
   *           iff it is a text grammar
   */
  public LexicalizedParser(Options op) {
    this.op = op;
    String source = System.getProperty(SERIALIZED_PARSER_PROPERTY);
    if (source == null) {
      source = DEFAULT_PARSER_LOC;
    }
    pd = getParserDataFromFile(source, op);
    this.op = pd.pt;
    makeParsers();
  }

  public LexicalizedParser(String parserFileOrUrl) {
    this(parserFileOrUrl, new Options());
  }

  /**
   * Construct a new LexicalizedParser.  This loads a grammar that
   * was previously assembled and stored.
   * @param parserFileOrUrl Filename/URL to load parser from
   * @param op Options for this parser. These will normally be overwritten
   *     by options stored in the file
   * @throws IllegalArgumentException If parser data cannot be loaded
   */
  public LexicalizedParser(String parserFileOrUrl, Options op) {
    this.op = op;
    //    System.err.print("Loading parser from file " + parserFileOrUrl);
    pd = getParserDataFromFile(parserFileOrUrl, op);
    this.op = pd.pt; // in case a serialized options was read in
    makeParsers();
  }

  /**
   * Construct a new LexicalizedParser.  This loads a grammar that
   * was previously assembled and stored.
   *
   * @throws IllegalArgumentException If parser data cannot be loaded
   */
  public LexicalizedParser(String parserFileOrUrl, boolean isTextGrammar, Options op) {
    this.op = op;
    if (isTextGrammar) {
      pd = getParserDataFromTextFile(parserFileOrUrl, op);
    } else {
      pd = getParserDataFromSerializedFile(parserFileOrUrl);
      this.op = pd.pt;
    }
    makeParsers();
  }

  /**
   * Construct a new LexicalizedParser object from a previously assembled
   * grammar.
   *
   * @param pd A <code>ParserData</code> object (not <code>null</code>)
   */
  public LexicalizedParser(ParserData pd) {
    this.pd = pd;
    makeParsers();
  }

  /**
   * Construct a new LexicalizedParser object from a previously assembled
   * grammar read from an InputStream.
   * One (ParserData) object is read from the stream.  It isn't closed.
   *
   * @param in The ObjectInputStream
   */
  public LexicalizedParser(ObjectInputStream in) throws Exception {
    this((ParserData) in.readObject());
  }

  /**
   * Construct a new LexicalizedParser.
   *
   * @param trainTreebank a treebank to train from
   */
  public LexicalizedParser(Treebank trainTreebank, GrammarCompactor compactor, Options op) {
    this(trainTreebank, compactor, op, null);
  }

  public LexicalizedParser(String treebankPath, FileFilter filt, Options op) {
    this(makeTreebank(treebankPath, op, filt), op);
  }

  public LexicalizedParser(Treebank trainTreebank, Options op) {
    this(trainTreebank, null, op);
  }

  /**
   * Construct a new LexicalizedParser.
   *
   * @param trainTreebank a treebank to train from
   * @param compactor A class for compacting grammars. May be null.
   * @param op Options for how the grammar is built from the treebank
   * @param tuneTreebank  a treebank to tune free params on (may be null)
   */
  public LexicalizedParser(Treebank trainTreebank, GrammarCompactor compactor, Options op,
      Treebank tuneTreebank) {
    this.op = op;
    pd = getParserDataFromTreebank(trainTreebank, compactor, tuneTreebank);
    makeParsers();
  }

  public LexicalizedParser(Treebank trainTreebank, DiskTreebank secondaryTrainTreebank, double weight, GrammarCompactor compactor, Options op) {
    this.op = op;
    pd = getParserDataFromTreebank(trainTreebank, secondaryTrainTreebank, weight, compactor);
    makeParsers();
  }



  /**
   * Converts a Sentence/List/String into a Tree.  If it can't be parsed,
   * it is made into a trivial tree in which each word is attached to a
   * dummy tag ("X") and then to a start nonterminal (also "X").
   *
   * @param in The input Sentence/List/String
   * @return A Tree that is the parse tree for the sentence.  If the parser
   *         fails, a new Tree is synthesized which attaches all words to the
   *         root.
   * @throws IllegalArgumentException If argument isn't a List or String
   */
  public Tree apply(Object in) {
    List<? extends HasWord> lst;
    if (in instanceof String) {
      DocumentPreprocessor dp = new DocumentPreprocessor(op.tlpParams.treebankLanguagePack().getTokenizerFactory());
      lst = dp.getWordsFromString((String) in);
    } else if (in instanceof List) {
      lst = (List<? extends HasWord>) in;
    } else {
      throw new IllegalArgumentException("Can only parse Sentence/List/String");
    }

    try {
      if (parse(lst)) {
        Tree bestparse = getBestParse();
        bestparse.setScore(getPCFGScore() % -10000.0); // -10000 denotes unknown words
        return bestparse;
      }
    } catch (Exception e) {
      System.err.println("Following exception caught during parsing:");
      e.printStackTrace();
      System.err.println("Recovering using fall through strategy: will construct an (X ...) tree.");
    }
    // if can't parse or exception, fall through
    TreeFactory lstf = new LabeledScoredTreeFactory();
    List<Tree> lst2 = new ArrayList<Tree>();
    for (HasWord obj : lst) {
      String s = obj.word();
      Tree t = lstf.newLeaf(s);
      Tree t2 = lstf.newTreeNode("X", Collections.singletonList(t));
      lst2.add(t2);
    }
    return lstf.newTreeNode("X", lst2);
  }

  /** Return a TreePrint for formatting parsed output trees.
   *  @return A TreePrint for formatting parsed output trees.
   */
  public TreePrint getTreePrint() {
    return Test.treePrint(op.tlpParams);
  }

  /**
   * Parse a Sentence.
   * This hasn't yet been implemented.  At present the goal is ignored.
   *
   * @param sentence The words to parse
   * @param goal What category to parse the words as
   * @return true iff it could be parsed
   */
  public boolean parse(List<? extends HasWord> sentence, String goal) {
    return parse(sentence);
  }

  /**
   * Tokenize and parse a sentence.
   *
   * @param sentence The sentence as a regular String
   * @return true iff it could be parsed
   */

  public boolean parse(String sentence) {
    DocumentPreprocessor dp = new DocumentPreprocessor(op.tlpParams.treebankLanguagePack().getTokenizerFactory());

    return parse(dp.getWordsFromString(sentence));
  }

  /**
   * Parse a sentence represented as a List of tokens.
   * The text must already have been tokenized and
   * normalized into tokens that are appropriate to the treebank
   * which was used to train the parser.  The tokens can be of
   * multiple types, and the list items need not be homogeneous as to type
   * (in particular, only some words might be given tags):
   * <ul>
   * <li>If a token implements HasWord, then the word to be parsed is
   * given by its word() value.</li>
   * <li>If a token implements HasTag and the tag() value is not
   * null or the empty String, then the parser is strongly advised to assign
   * a part of speech tag that <i>begins</i> with this String.</li>
   * <li>Otherwise toString() is called on the token, and the returned
   * value is used as the word to be parsed.  In particular, if the
   * token is already a String, this means that the String is used as
   * the word to be parsed.</li>
   * </ul>
   *
   * @param sentence The sentence to parse
   * @return true Iff the sentence was accepted by the grammar
   * @throws UnsupportedOperationException If the Sentence is too long or
   *                                       of zero length or the parse
   *                                       otherwise fails for resource reasons
   */
  public boolean parse(List<? extends HasWord> sentence) {
    int length = sentence.size();
    if (length == 0) {
      throw new UnsupportedOperationException("Can't parse a zero-length sentence!");
    }
    List<HasWord> sentenceB = new ArrayList<HasWord>(sentence);
    if (Test.addMissingFinalPunctuation) {
      addSentenceFinalPunctIfNeeded(sentenceB, length);
    }
    if (length > Test.maxLength) {
      throw new UnsupportedOperationException("Sentence too long: length " + length);
    }
    TreePrint treePrint = getTreePrint();
    PrintWriter pwOut = op.tlpParams.pw();
    parseSucceeded = false;
    sentenceB.add(new Word(Lexicon.BOUNDARY));
    if (op.doPCFG) {
      if (!pparser.parse(sentenceB)) {
        return parseSucceeded;
      }
      if (Test.verbose) {
        System.out.println("PParser output");
        // debinarizer.transformTree(pparser.getBestParse()).pennPrint(pwOut); // with scores on nodes
        treePrint.printTree(debinarizer.transformTree(pparser.getBestParse()), pwOut); // without scores on nodes
      }
    }
    if (op.doDep && ! Test.useFastFactored) {
      if ( ! dparser.parse(sentenceB)) {
        return parseSucceeded;
      }
      // cdm nov 2006: should move these printing bits to the main printing section,
      // so don't calculate the best parse twice!
      if (Test.verbose) {
        System.out.println("DParser output");
        treePrint.printTree(dparser.getBestParse(), pwOut);
      }
    }
    if (op.doPCFG && op.doDep) {
      if ( ! bparser.parse(sentenceB)) {
        return parseSucceeded;
      } else {
        parseSucceeded = true;
      }
    }
    return true;
  }

  /**
   * Parse a (speech) lattice with the PCFG parser.
   *
   * @param lr a lattice to parse
   * @return Whether the lattice could be parsed by the grammar
   */
  public boolean parse(HTKLatticeReader lr) {
    TreePrint treePrint = getTreePrint();
    PrintWriter pwOut = op.tlpParams.pw();
    parseSucceeded = false;
    if (lr.getNumStates() > Test.maxLength + 1) {  // + 1 for boundary symbol
      throw new UnsupportedOperationException("Lattice too big: " + lr.getNumStates());
    }
    if (op.doPCFG) {
      if (!pparser.parse(lr)) {
        return parseSucceeded;
      }
      if (Test.verbose) {
        System.out.println("PParser output");
        treePrint.printTree(debinarizer.transformTree(pparser.getBestParse()), pwOut);
      }
    }
    return true;
  }

  /** Adds a sentence final punctuation mark to sentences that lack one.
   *  This method adds a period (the first sentence final punctuation word
   *  in a parser language pack) to sentences that don't have one within
   *  the last 3 words (to allow for close parentheses, etc.).  It checks
   *  tags for punctuation, if available, otherwise words.
   *  @param sentence The sentence to check
   *  @param length The length of the sentence (just to avoid recomputation)
   */
  private void addSentenceFinalPunctIfNeeded(List<HasWord> sentence, int length) {
    int start = length - 3;
    if (start < 0) start = 0;
    TreebankLanguagePack tlp = op.tlpParams.treebankLanguagePack();
    for (int i = length - 1; i >= start; i--) {
      Object item = sentence.get(i);
      // An object (e.g., MapLabel) can implement HasTag but not actually store
      // a tag so we need to check that there is something there for this case.
      // If there is, use only it, since word tokens can be ambiguous.
      String tag = null;
      if (item instanceof HasTag) {
        tag = ((HasTag) item).tag();
      }
      if (tag != null && ! "".equals(tag)) {
        if (tlp.isSentenceFinalPunctuationTag(tag)) {
          return;
        }
      } else if (item instanceof HasWord) {
        String str = ((HasWord) item).word();
        if (tlp.isPunctuationWord(str)) {
          return;
        }
      } else {
        String str = item.toString();
        if (tlp.isPunctuationWord(str)) {
          return;
        }
      }
    }
    // none found so add one.
    if (Test.verbose) {
      System.err.println("Adding missing final punctuation to sentence.");
    }
    String[] sfpWords = tlp.sentenceFinalPunctuationWords();
    if (sfpWords.length > 0) {
      sentence.add(new Word(sfpWords[0]));
    }
  }


  /**
   * Return the best parse of the sentence most recently parsed.
   * This will be from the factored parser, if it was used and it succeeded
   * else from the PCFG if it was used and succeed, else from the dependency
   * parser.
   *
   * @return The best tree
   * @throws NoSuchElementException If no previously successfully parsed
   *                                sentence
   */
  public Tree getBestParse() {
    if (bparser != null && parseSucceeded) {
      Tree binaryTree = bparser.getBestParse();

      Tree tree = debinarizer.transformTree(binaryTree);
      if (op.nodePrune) {
        NodePruner np = new NodePruner(pparser, debinarizer);
        tree = np.prune(tree);
      }
      return subcategoryStripper.transformTree(tree);
    } else if (pparser != null && pparser.hasParse() && fallbackToPCFG) {
      return getBestPCFGParse();
    } else if (dparser != null && dparser.hasParse()) { // && fallbackToDG
      // Should we strip subcategorize like this?  Traditionally haven't...
      // return subcategoryStripper.transformTree(getBestDependencyParse(true));
      return getBestDependencyParse(true);
    } else {
      throw new NoSuchElementException();
    }
  }


  public List<ScoredObject<Tree>> getKGoodFactoredParses(int k) {
    if (bparser == null) {
      return null;
    }
    List<ScoredObject<Tree>> binaryTrees = bparser.getKGoodParses(k);
    if (binaryTrees == null) {
      return null;
    }
    List<ScoredObject<Tree>> trees = new ArrayList<ScoredObject<Tree>>(k);
    for (ScoredObject<Tree> tp : binaryTrees) {
      Tree t = debinarizer.transformTree(tp.object());
      t = subcategoryStripper.transformTree(t);
      trees.add(new ScoredObject<Tree>(t, tp.score()));
    }
    return trees;
  }

  /**
   * Returns the trees (and scores) corresponding to the
   * k-best derivations of the sentence.  This cannot be
   * a Counter because frequently there will be multiple
   * derivations which lead to the same parse tree.
   *
   * @param k The number of best parses to return
   * @return The list of trees with their scores (neg log prob).
   */
  public List<ScoredObject<Tree>> getKBestPCFGParses(int k) {
    if (pparser == null) {
      return null;
    }
    List<ScoredObject<Tree>> binaryTrees = pparser.getKBestParses(k);
    if (binaryTrees == null) {
      return null;
    }
    List<ScoredObject<Tree>> trees = new ArrayList<ScoredObject<Tree>>(k);
    for (ScoredObject<Tree> p : binaryTrees) {
      Tree t = debinarizer.transformTree(p.object());
      t = subcategoryStripper.transformTree(t);
      trees.add(new ScoredObject<Tree>(t, p.score()));
    }
    return trees;
  }


  public Tree getBestPCFGParse() {
    return getBestPCFGParse(true);
  }

  public Tree getBestPCFGParse(boolean stripSubcategories) {
    if (pparser == null) {
      return null;
    }
    Tree binaryTree = pparser.getBestParse();

    if (binaryTree == null) {
      return null;
    }
    Tree t = debinarizer.transformTree(binaryTree);
    if (stripSubcategories) {
      t = subcategoryStripper.transformTree(t);
    }
    return t;
  }

  public double getPCFGScore() {
    return pparser.getBestScore();
  }

  public double getPCFGScore(String goalStr) {
    return pparser.getBestScore(goalStr);
  }

  public Tree getBestDependencyParse() {
    return getBestDependencyParse(false);
  }

  public Tree getBestDependencyParse(boolean debinarize) {
    Tree t = dparser != null ? dparser.getBestParse() : null;
    if (debinarize && t != null) {
      t = debinarizer.transformTree(t);
    }
    return t;
  }

  /**
   * Set the maximum length of a sentence that the parser will be willing
   * to parse.  Sentences longer than this will not be parsed (an Exception
   * will be thrown).
   *
   * @param maxLength The maximum length sentence to parse
   */
  public void setMaxLength(int maxLength) {
    Test.maxLength = maxLength;
  }

  public static ParserData getParserDataFromFile(String parserFileOrUrl, Options op) {
    ParserData pd = getParserDataFromSerializedFile(parserFileOrUrl);
    if (pd == null) {
      pd = getParserDataFromTextFile(parserFileOrUrl, op);
    }
    return pd;
  }

  private static Treebank makeTreebank(String treebankPath, Options op, FileFilter filt) {
    System.err.println("Training a parser from treebank dir: " + treebankPath);
    Treebank trainTreebank = op.tlpParams.diskTreebank();
    System.err.print("Reading trees...");
    if (filt == null) {
      trainTreebank.loadPath(treebankPath);
    } else {
      trainTreebank.loadPath(treebankPath, filt);
    }

    Timing.tick("done [read " + trainTreebank.size() + " trees].");
    return trainTreebank;
  }

  private static DiskTreebank makeSecondaryTreebank(String treebankPath, Options op, FileFilter filt) {
    System.err.println("Additionally training using secondary disk treebank: " + treebankPath + ' ' + filt);
    DiskTreebank trainTreebank = op.tlpParams.diskTreebank();
    if (filt == null) {
      trainTreebank.loadPath(treebankPath);
    } else {
      trainTreebank.loadPath(treebankPath, filt);
    }
    return trainTreebank;
  }

  public ParserData parserData() {
    return pd;
  }

  public Lexicon getLexicon() {
    return pd.lex;
  }

  static void saveParserDataToSerialized(ParserData pd, String filename) {
    try {
      System.err.print("Writing parser in serialized format to file " + filename + ' ');
      ObjectOutputStream out = IOUtils.writeStreamFromString(filename);
      out.writeObject(pd);
      out.close();
      System.err.println("done.");
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  static void saveParserDataToText(ParserData pd, String filename) {
    try {
      System.err.print("Writing parser in text grammar format to file " + filename);
      OutputStream os;
      if (filename.endsWith(".gz")) {
        // it's faster to do the buffering _outside_ the gzipping as here
        os = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(filename)));
      } else {
        os = new BufferedOutputStream(new FileOutputStream(filename));
      }
      PrintWriter out = new PrintWriter(os);
      String prefix = "BEGIN ";
      out.println(prefix + "OPTIONS");
      if (pd.pt != null) {
        pd.pt.writeData(out);
      }
      out.println();
      System.err.print(".");
      out.println(prefix + "LEXICON");
      if (pd.lex != null) {
        pd.lex.writeData(out);
      }
      out.println();
      System.err.print(".");
      out.println(prefix + "UNARY_GRAMMAR");
      if (pd.ug != null) {
        pd.ug.writeData(out);
      }
      out.println();
      System.err.print(".");
      out.println(prefix + "BINARY_GRAMMAR");
      if (pd.bg != null) {
        pd.bg.writeData(out);
      }
      out.println();
      System.err.print(".");
      out.println(prefix + "DEPENDENCY_GRAMMAR");
      if (pd.dg != null) {
        pd.dg.writeData(out);
      }
      out.println();
      System.err.print(".");
      out.flush();
      out.close();
      System.err.println("done.");
    } catch (IOException e) {
      System.err.println("Trouble saving parser data to ASCII format.");
      e.printStackTrace();
    }
  }

  protected static ParserData getParserDataFromPetrovFiles(String grammarFile, String lexiconFile) {
    try {
      Options op = new Options();
      // TODO: do something to the options

      // for the grammar, we have to read through once and store it
      // so we can number all the states, before we create the grammar
      BufferedReader gIn = new BufferedReader(new FileReader(grammarFile));
      String line = gIn.readLine();
      Numberer stateNumberer = Numberer.getGlobalNumberer("states");
      List<UnaryRule> unaryRules = new ArrayList<UnaryRule>();
      List<BinaryRule> binaryRules = new ArrayList<BinaryRule>();
      while (line!=null) {
        String[] fields = line.split("\\s+");
        if (fields.length==4) {
          // unary rule
          double score = Double.parseDouble(fields[3]);
          UnaryRule ur = new UnaryRule(stateNumberer.number(new String(fields[0])), stateNumberer.number(new String(fields[2])), score);
          unaryRules.add(ur);
        } else if (fields.length==5){
          // binary rule
          double score = Double.parseDouble(fields[4]);
          BinaryRule br = new BinaryRule(stateNumberer.number(new String(fields[0])), stateNumberer.number(new String(fields[2])),
              stateNumberer.number(new String(fields[3])), score);
          binaryRules.add(br);
        } else {
          throw new RuntimeException("Bad line format: " + line);
        }
        line = gIn.readLine();
      }
      // now make the binary grammar
      BinaryGrammar bg = new BinaryGrammar(stateNumberer.total());
      for (BinaryRule br : binaryRules) {
        bg.addRule(br);
      }
      bg.splitRules();
      // now make the unary grammar
      UnaryGrammar ug = new UnaryGrammar(stateNumberer.total());
      for (UnaryRule ur : unaryRules) {
        ug.addRule(ur);
      }
      ug.purgeRules();

      // now we handle the lexicon
      Lexicon lex = new PetrovLexicon();
      lex.readData(new BufferedReader(new FileReader(lexiconFile)));

      return new ParserData(lex, bg, ug, null, null, op); // TODO, numbs?
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static void confirmBeginBlock(String file, String line) {
    if (line == null) {
      throw new RuntimeException(file + ": expecting BEGIN block; got end of file.");
    } else if (! line.startsWith("BEGIN")) {
      throw new RuntimeException(file + ": expecting BEGIN block; got " + line);
    }
  }

  protected static ParserData getParserDataFromTextFile(String textFileOrUrl, Options op) {
    try {
      Timing tim = new Timing();
      System.err.print("Loading parser from text file " + textFileOrUrl + ' ');
      BufferedReader in = IOUtils.readReaderFromString(textFileOrUrl);
      Timing.startTime();
      String line = in.readLine();
      confirmBeginBlock(textFileOrUrl, line);
      op.readData(in);
      System.err.print(".");
      line = in.readLine();
      confirmBeginBlock(textFileOrUrl, line);
      Lexicon lex = op.tlpParams.lex(op.lexOptions);
      lex.readData(in);
      System.err.print(".");
      line = in.readLine();
      confirmBeginBlock(textFileOrUrl, line);
      UnaryGrammar ug = new UnaryGrammar(op.numStates);
      ug.readData(in);
      System.err.print(".");
      line = in.readLine();
      confirmBeginBlock(textFileOrUrl, line);
      BinaryGrammar bg = new BinaryGrammar(op.numStates);
      bg.readData(in);
      System.err.print(".");
      line = in.readLine();
      confirmBeginBlock(textFileOrUrl, line);
      DependencyGrammar dg = new MLEDependencyGrammar(op.tlpParams, op.directional, op.distance, op.coarseDistance);
      dg.readData(in);
      System.err.print(".");
      Map<String,Numberer> numbs = Numberer.getNumberers();
      in.close();
      System.err.println(" done [" + tim.toSecondsString() + " sec].");
      return new ParserData(lex, bg, ug, dg, numbs, op);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  public static ParserData getParserDataFromSerializedFile(String serializedFileOrUrl) {
    try {
      Timing tim = new Timing();
      System.err.print("Loading parser from serialized file " + serializedFileOrUrl + " ...");
      ObjectInputStream in = IOUtils.readStreamFromString(serializedFileOrUrl);
      ParserData pd = (ParserData) in.readObject();
      //      Numberer.setNumberers(pd.numbs); // will happen later in makeParsers()
      in.close();
      System.err.println(" done [" + tim.toSecondsString() + " sec].");
      return pd;
    } catch (InvalidClassException ice) {
      // For this, it's not a good idea to continue and try it as a text file!
      System.err.println();   // as in middle of line from above message
      throw new RuntimeException("Invalid class in file: " + serializedFileOrUrl, ice);
    } catch (FileNotFoundException fnfe) {
      // For this, it's not a good idea to continue and try it as a text file!
      System.err.println();   // as in middle of line from above message
      throw new RuntimeException("File not found: " + serializedFileOrUrl, fnfe);
    } catch (StreamCorruptedException sce) {
      // suppress error message, on the assumption that we've really got
      // a text grammar, and that'll be tried next
      System.err.println();
    } catch (Exception e) {
      System.err.println();   // as in middle of line from above message
      e.printStackTrace();
    }
    return null;
  }


  private static void printOptions(boolean train, Options op) {
    op.display();
    if (train) {
      Train.display();
    } else {
      Test.display();
    }
    op.tlpParams.display();
  }


  /** @return a pair of binaryTrainTreebank,binaryTuneTreebank.
   */
  public static Pair<List<Tree>,List<Tree>> getAnnotatedBinaryTreebankFromTreebank(Treebank trainTreebank,
      Treebank tuneTreebank,
      Options op) {
    Timing.startTime();
    // setup tree transforms
    TreebankLangParserParams tlpParams = op.tlpParams;
    TreebankLanguagePack tlp = tlpParams.treebankLanguagePack();

    if (Test.verbose) {
      PrintWriter pwErr = tlpParams.pw(System.err);
      pwErr.print("Training ");
      pwErr.println(trainTreebank.textualSummary(tlp));
    }

    System.err.print("Binarizing trees...");
    TreeAnnotatorAndBinarizer binarizer;
    if (!Train.leftToRight) {
      binarizer = new TreeAnnotatorAndBinarizer(tlpParams, op.forceCNF, !Train.outsideFactor(), true);
    } else {
      binarizer = new TreeAnnotatorAndBinarizer(tlpParams.headFinder(), new LeftHeadFinder(), tlpParams, op.forceCNF, !Train.outsideFactor(), true);
    }
    CollinsPuncTransformer collinsPuncTransformer = null;
    if (Train.collinsPunc) {
      collinsPuncTransformer = new CollinsPuncTransformer(tlp);
    }
    List<Tree> binaryTrainTrees = new ArrayList<Tree>();
    List<Tree> binaryTuneTrees = new ArrayList<Tree>();

    if (Train.selectiveSplit) {
      Train.splitters = ParentAnnotationStats.getSplitCategories(trainTreebank, Train.tagSelectiveSplit, 0, Train.selectiveSplitCutOff, Train.tagSelectiveSplitCutOff, tlp);
      removeDeleteSplittersFromSplitters(tlp);
      if (Test.verbose) {
        List<String> list = new ArrayList<String>(Train.splitters);
        Collections.sort(list);
        System.err.println("Parent split categories: " + list);
      }
    }
    if (Train.selectivePostSplit) {
      // Do all the transformations once just to learn selective splits on annotated categories
      TreeTransformer myTransformer = new TreeAnnotator(tlpParams.headFinder(), tlpParams);
      Treebank annotatedTB = trainTreebank.transform(myTransformer);
      Train.postSplitters = ParentAnnotationStats.getSplitCategories(annotatedTB, true, 0, Train.selectivePostSplitCutOff, Train.tagSelectivePostSplitCutOff, tlp);
      if (Test.verbose) {
        System.err.println("Parent post annotation split categories: " + Train.postSplitters);
      }
    }
    if (Train.hSelSplit) {
      // We run through all the trees once just to gather counts for hSelSplit!
      int ptt = Train.printTreeTransformations;
      Train.printTreeTransformations = 0;
      binarizer.setDoSelectiveSplit(false);
      for (Tree tree : trainTreebank) {
        if (Train.collinsPunc) {
          tree = collinsPuncTransformer.transformTree(tree);
        }
        binarizer.transformTree(tree);
      }
      binarizer.setDoSelectiveSplit(true);
      Train.printTreeTransformations = ptt;
    }
    // we've done all the setup now. here's where the train treebank is transformed.
    for (Tree tree : trainTreebank) {
      if (Train.collinsPunc) {
        tree = collinsPuncTransformer.transformTree(tree);
      }
      tree = binarizer.transformTree(tree);
      if (tree.yield().size() - 1 <= trainLengthLimit) {
        // TEG: have to subtract off the boundary symbol!
        binaryTrainTrees.add(tree);
      }
    }
    if (Train.printAnnotatedStateCounts) {
      binarizer.printStateCounts();
    }
    if (Train.printAnnotatedRuleCounts) {
      binarizer.printRuleCounts();
    }

    if (tuneTreebank != null) {
      for (Tree tree : tuneTreebank) {
        if (Train.collinsPunc) {
          tree = collinsPuncTransformer.transformTree(tree);
        }
        tree = binarizer.transformTree(tree);
        if (tree.yield().size() - 1 <= trainLengthLimit) {
          binaryTuneTrees.add(tree);
        }
      }
    }

    Timing.tick("done.");
    if (Test.verbose) {
      binarizer.dumpStats();
    }

    return new Pair<List<Tree>,List<Tree>>(binaryTrainTrees, binaryTuneTrees);
  }

  private static void removeDeleteSplittersFromSplitters(TreebankLanguagePack tlp) {
    if (Train.deleteSplitters != null) {
      List<String> deleted = new ArrayList<String>();
      for (String del : Train.deleteSplitters) {
        String baseDel = tlp.basicCategory(del);
        boolean checkBasic = del.equals(baseDel);
        for (Iterator<String> it = Train.splitters.iterator(); it.hasNext(); ) {
          String elem = it.next();
          String baseElem = tlp.basicCategory(elem);
          boolean delStr = checkBasic && baseElem.equals(baseDel) || elem.equals(del);
          if (delStr) {
            it.remove();
            deleted.add(elem);
          }
        }
      }
      if (Test.verbose) {
        System.err.println("Removed from vertical splitters: " + deleted);
      }
    }
  }


  public final ParserData getParserDataFromTreebank(Treebank trainTreebank,
      GrammarCompactor compactor,
      Treebank tuneTreebank) {
    System.err.println("Currently " + new Date());
    printOptions(true, op);
    Pair<List<Tree>,List<Tree>> pair = getAnnotatedBinaryTreebankFromTreebank(trainTreebank, tuneTreebank, op);
    List<Tree> binaryTrainTrees = pair.first();
    List<Tree> binaryTuneTrees = pair.second();

    // extract grammars
    //Extractor bgExtractor = new SmoothedBinaryGrammarExtractor();
    Extractor<Pair<UnaryGrammar,BinaryGrammar>> bgExtractor = new BinaryGrammarExtractor();
    // Extractor lexExtractor = new LexiconExtractor();
    Extractor<DependencyGrammar> dgExtractor = op.tlpParams.dependencyGrammarExtractor(op);
    //TreeExtractor uwmExtractor = new UnknownWordModelExtractor(binaryTrainTrees.size());
    System.err.print("Extracting PCFG...");
    Pair<UnaryGrammar,BinaryGrammar> bgug = bgExtractor.extract(binaryTrainTrees);
    Timing.tick("done.");
    if (compactor != null) {
      System.err.print("Compacting grammar...");
      bgug = compactor.compactGrammar(bgug);
      Timing.tick("done.");
    }
    System.err.print("Compiling grammar...");
    BinaryGrammar bg = bgug.second;
    bg.splitRules();
    UnaryGrammar ug = bgug.first;
    // System.err.println("\nUnary grammar built by BinaryGrammarExtractor");
    // ug.writeAllData(new OutputStreamWriter(System.err));
    ug.purgeRules();
    // System.err.println("Unary grammar after purgeRules");
    // ug.writeAllData(new OutputStreamWriter(System.err));
    Timing.tick("done");

    System.err.print("Extracting Lexicon...");
    Lexicon lex = op.tlpParams.lex(op.lexOptions);
    lex.train(binaryTrainTrees);
    Timing.tick("done.");

    DependencyGrammar dg = null;
    if (op.doDep) {
      System.err.print("Extracting Dependencies...");
      dg = dgExtractor.extract(binaryTrainTrees);
      //      ((ChineseSimWordAvgDepGrammar)dg).setLex(lex);
      Timing.tick("done.");
      //System.out.println(dg);
      //System.err.print("Extracting Unknown Word Model...");
      //UnknownWordModel uwm = (UnknownWordModel)uwmExtractor.extract(binaryTrainTrees);
      //Timing.tick("done.");
      if (tuneTreebank != null) {
        System.err.print("Tuning Dependency Model...");
        dg.setLexicon(lex); // MG2008: needed if using PwGt model
        dg.tune(binaryTuneTrees);
        Timing.tick("done.");
      }
    }
    Map<String,Numberer> numbs = Numberer.getNumberers();

    System.err.println("Done training parser.");
    if (Train.trainTreeFile!=null) {
      try {
        System.err.print("Writing out binary trees to "+ Train.trainTreeFile+"...");
        IOUtils.writeObjectToFile(binaryTrainTrees, Train.trainTreeFile);
        Timing.tick("done.");
      } catch (Exception e) {
        System.err.println("Problem writing out binary trees.");
      }
    }
    return new ParserData(lex, bg, ug, dg, numbs, op);
  }

  // TODO: Unify the below method with the method above
  // TODO: Make below method work with arbitrarily large secondary treebank via iteration
  // TODO: Have weight implemented for training lexicon

  /**
   * A method for training from two different treebanks, the second of which is presumed
   * to be orders of magnitude larger.
   * <p/>
   * Trees are not read into memory but processed as they are read from disk.
   * <p/>
   * A weight (typically &lt;= 1) can be put on the second treebank.
   */
  protected final ParserData getParserDataFromTreebank(Treebank trainTreebank, DiskTreebank secondaryTrainTreebank, double weight, GrammarCompactor compactor) {
    System.err.println("Currently " + new Date());
    printOptions(true, op);
    Timing.startTime();

    // setup tree transforms
    TreebankLangParserParams tlpParams = op.tlpParams;
    TreebankLanguagePack tlp = tlpParams.treebankLanguagePack();

    if (Test.verbose) {
      PrintWriter pwErr = tlpParams.pw(System.err);
      pwErr.print("Training ");
      pwErr.println(trainTreebank.textualSummary(tlp));
      pwErr.print("Secondary training ");
      pwErr.println(secondaryTrainTreebank.textualSummary(tlp));
    }

    CompositeTreeTransformer trainTransformer = new CompositeTreeTransformer();

    if (Train.collinsPunc) {
      CollinsPuncTransformer collinsPuncTransformer = new CollinsPuncTransformer(tlp);
      trainTransformer.addTransformer(collinsPuncTransformer);
    }

    TreeAnnotatorAndBinarizer binarizer;
    if (!Train.leftToRight) {
      binarizer = new TreeAnnotatorAndBinarizer(tlpParams, op.forceCNF, !Train.outsideFactor(), true);
    } else {
      binarizer = new TreeAnnotatorAndBinarizer(tlpParams.headFinder(), new LeftHeadFinder(), tlpParams, op.forceCNF, !Train.outsideFactor(), true);
    }
    trainTransformer.addTransformer(binarizer);

    CompositeTreebank wholeTreebank = new CompositeTreebank(trainTreebank, secondaryTrainTreebank);
    if (Train.selectiveSplit) {
      Train.splitters = ParentAnnotationStats.getSplitCategories(wholeTreebank, Train.tagSelectiveSplit, 0, Train.selectiveSplitCutOff, Train.tagSelectiveSplitCutOff, tlp);
      removeDeleteSplittersFromSplitters(tlp);
      if (Test.verbose) {
        List<String> list = new ArrayList<String>(Train.splitters);
        Collections.sort(list);
        System.err.println("Parent split categories: " + list);
      }
    }

    Treebank transformedWholeTreebank = wholeTreebank;

    if (Train.selectivePostSplit) {
      // Do all the transformations once just to learn selective splits on annotated categories
      TreeTransformer annotator = new TreeAnnotator(tlpParams.headFinder(), tlpParams);
      //wholeTreebank.transformOnRead(annotator);
      transformedWholeTreebank = transformedWholeTreebank.transform(annotator);
      Train.postSplitters = ParentAnnotationStats.getSplitCategories(transformedWholeTreebank, true, 0, Train.selectivePostSplitCutOff, Train.tagSelectivePostSplitCutOff, tlp);
      if (Test.verbose) {
        System.err.println("Parent post annotation split categories: " + Train.postSplitters);
      }
    }
    if (Train.hSelSplit) {
      // We run through all the trees once just to gather counts for hSelSplit!
      int ptt = Train.printTreeTransformations;
      Train.printTreeTransformations = 0;
      binarizer.setDoSelectiveSplit(false);
      for (Tree tree : transformedWholeTreebank) {
        trainTransformer.transformTree(tree);
      }
      binarizer.setDoSelectiveSplit(true);
      Train.printTreeTransformations = ptt;
    }

    trainTreebank = trainTreebank.transform(trainTransformer);
    //secondaryTrainTreebank.transformOnRead(trainTransformer);
    Treebank transformedSecondaryTrainTreebank = secondaryTrainTreebank.transform(trainTransformer);

    // extract grammars
    //Extractor bgExtractor = new SmoothedBinaryGrammarExtractor();
    BinaryGrammarExtractor bgExtractor = new BinaryGrammarExtractor();
    // Extractor lexExtractor = new LexiconExtractor();
    AbstractTreeExtractor<DependencyGrammar> dgExtractor = new MLEDependencyGrammarExtractor(op);
    //TreeExtractor uwmExtractor = new UnknownWordModelExtractor(binaryTrainTrees.size());
    System.err.print("Extracting PCFG...");
    Pair<UnaryGrammar,BinaryGrammar> bgug = bgExtractor.extract(trainTreebank, transformedSecondaryTrainTreebank, weight);
    Timing.tick("done.");
    if (compactor != null) {
      System.err.print("Compacting grammar...");
      bgug = compactor.compactGrammar(bgug);
      Timing.tick("done.");
    }
    System.err.print("Compiling grammar...");
    BinaryGrammar bg = bgug.second;
    bg.splitRules();
    UnaryGrammar ug = bgug.first;
    ug.purgeRules();
    Timing.tick("done");
    System.err.print("Extracting Lexicon...");
    Lexicon lex = op.tlpParams.lex(op.lexOptions);
    // lex.train(wholeTreebank);
    //    lex.train(trainTreebank);
    //    ((BaseLexicon)lex).train(secondaryTrainTreebank, weight);
    // todo. BUG: With this implementation the lexicon is always trained with the
    // secondaryTreebank having a weight of 1.0.  But at least the code now is basically
    // correct rather than totally broken.  CDM Dec 2006.
    CompositeTreebank wholeBinaryTreebank = new CompositeTreebank(trainTreebank, transformedSecondaryTrainTreebank);
    lex.train(wholeBinaryTreebank);
    Timing.tick("done.");

    DependencyGrammar dg = null;
    if (op.doDep) {
      System.err.print("Extracting Dependencies...");
      dg = dgExtractor.extract(trainTreebank, transformedSecondaryTrainTreebank, weight);
      Timing.tick("done.");
    }
    Map<String,Numberer> numbs = Numberer.getNumberers();

    System.err.println("Done training parser.");
    return new ParserData(lex, bg, ug, dg, numbs, op);
  }


  /**
   * Reinitializes the parser.
   *
   * As of (6/14/2006) the creation of multiple LexicalParser objects
   * is problematic as various global variables interfere with each other
   *
   * Depending on which parsers are loaded this either causes the
   * parser to crash, or alternatively it just causes the system
   * to just generate some bad/weird results (e.g. bad typed dependencies,
   * and, in one case, I think I even saw a verb tagged as being
   * punctuation, PU). In many ways, crashing is better since it highlights
   * the fact that something really is broken, rather than just silently
   * generating bad results.
   *
   * In any case, ideally, this should be fixed. But, until then, this
   * method is provided so that each loaded parser object can be reinitialized
   * prior to being used. -cer
   */
  public void reset() {
    makeParsers();
  }

  private void makeParsers() {
    if (pd == null) {
      throw new IllegalArgumentException("Error loading parser data: pd null");
    }
    Numberer.setNumberers(pd.numbs);
    BinaryGrammar bg = pd.bg;
    bg.splitRules();
    UnaryGrammar ug = pd.ug;
    Lexicon lex = pd.lex;
    DependencyGrammar dg = pd.dg;
    //  This checks to see if commandline options for the arabic tokenizer, in which case they
    //      override the serialized versions.
    if(pd.pt.tlpParams.treebankLanguagePack() instanceof ArabicTreebankLanguagePack){
      ArabicTreebankLanguagePack tlp = ((ArabicTreebankLanguagePack) op.tlpParams.treebankLanguagePack());
      if ( tlp.getTokenizerFactory() != null ){
        try {
          ((ArabicTreebankLanguagePack) pd.pt.tlpParams.treebankLanguagePack()).setTokenizerFactory(tlp.getTokenizerFactory());
        } catch( Exception e){
          System.err.println(" Attempt to apply command line options to serialized parser failed; " + e.toString() );
        }
      }
    }
    op = pd.pt;
    if (op.doPCFG) {
      if (Test.iterativeCKY) {
        pparser = new IterativeCKYPCFGParser(bg, ug, lex, op);
      } else {
        pparser = new ExhaustivePCFGParser(bg, ug, lex, op);
      }
    }
    if (op.doDep) {
      dg.setLexicon(lex);
      if ( ! Test.useFastFactored) {
        dparser = new ExhaustiveDependencyParser(dg, lex, op);
      }
      if (op.doPCFG) {
        if (Test.useFastFactored) {
          MLEDependencyGrammar mledg = (MLEDependencyGrammar) dg;
          int numToFind = 1;
          if (Test.printFactoredKGood > 0) {
            numToFind = Test.printFactoredKGood;
          }
          bparser = new FastFactoredParser(pparser, mledg, op, numToFind);
        } else {
          Scorer scorer = new TwinScorer(pparser, dparser);
          //Scorer scorer = parser;
          if (Test.useN5) {
            bparser = new BiLexPCFGParser.N5BiLexPCFGParser(scorer, pparser, dparser, bg, ug, dg, lex, op);
          } else {
            bparser = new BiLexPCFGParser(scorer, pparser, dparser, bg, ug, dg, lex, op);
          }
        }
      }
    }
    fallbackToPCFG = true;

    debinarizer = new Debinarizer(op.forceCNF, new CategoryWordTagFactory());
    subcategoryStripper = op.tlpParams.subcategoryStripper();
  }


  /**
   * Returns the input sentence for the parser.
   */
  static Sentence<? extends HasWord> getInputSentence(Tree t) {
    if (Test.forceTags) {
      if (Test.preTag) {
        Sentence s = tagger.processSentence(t.yield());
        if(Test.verbose) {
          System.err.println("Guess tags: "+Arrays.toString(s.toArray()));
          System.err.println("Gold tags: "+t.labeledYield().toString());
        }
        return s;
      } else if(Test.noFunctionalForcing) {
        Sentence<? extends HasWord> s = t.taggedYield();
        for(HasWord word : s) {
          String tag = ((HasTag) word).tag();
          tag = tag.split("-")[0];
          ((HasTag) word).setTag(tag);
        }
        return s;
      } else {
        return t.taggedYield();
      }
    } else {
      return t.yield();
    }
  }

  // helper function
  private static int numSubArgs(String[] args, int index) {
    int i = index;
    while (i + 1 < args.length && args[i + 1].charAt(0) != '-') {
      i++;
    }
    return i - index;
  }

  private static void printOutOfMemory(PrintWriter pw) {
    pw.println();
    pw.println("*******************************************************");
    pw.println("***  WARNING!! OUT OF MEMORY! THERE WAS NOT ENOUGH  ***");
    pw.println("***  MEMORY TO RUN ALL PARSERS.  EITHER GIVE THE    ***");
    pw.println("***  JVM MORE MEMORY, SET THE MAXIMUM SENTENCE      ***");
    pw.println("***  LENGTH WITH -maxLength, OR PERHAPS YOU ARE     ***");
    pw.println("***  HAPPY TO HAVE THE PARSER FALL BACK TO USING    ***");
    pw.println("***  A SIMPLER PARSER FOR VERY LONG SENTENCES.      ***");
    pw.println("*******************************************************");
    pw.println();
  }


  /** Test the parser on a treebank. Parses will be written to stdout, and
   *  various other information will be written to stderr and stdout,
   *  particularly if <code>Test.verbose</code> is true.
   *
   *  @param testTreebank The treebank to parse
   *  @return The labeled precision/recall F<sub>1</sub> (EVALB measure)
   *          of the parser on the treebank.
   */
  public double testOnTreebank(Treebank testTreebank) {
    System.err.println("Testing on treebank");
    Timing treebankTotalTtimer = new Timing();
    TreePrint treePrint = getTreePrint();
    TreebankLangParserParams tlpParams = op.tlpParams;
    TreebankLanguagePack tlp = op.langpack();
    PrintWriter pwOut = tlpParams.pw();
    PrintWriter pwErr = tlpParams.pw(System.err);
    if (Test.verbose) {
      pwErr.print("Testing ");
      pwErr.println(testTreebank.textualSummary(tlp));
    }
    if (Test.evalb) {
      EvalbFormatWriter.initEVALBfiles(tlpParams);
    }
    TreeTransformer tc = tlpParams.collinizer();
    TreeTransformer br = new BoundaryRemover();

    // evaluation setup
    boolean runningAverages = Boolean.parseBoolean(Test.evals.getProperty("runningAverages"));
    boolean summary = Boolean.parseBoolean(Test.evals.getProperty("summary"));
    boolean tsv = Boolean.parseBoolean(Test.evals.getProperty("tsv"));
    tlpParams.setupForEval();
    subcategoryStripper = tlpParams.subcategoryStripper();

    AbstractEval pcfgLB = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("pcfgLB"))) {
      pcfgLB = new Evalb("pcfg LP/LR", runningAverages);
    }
    AbstractEval pcfgCB = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("pcfgCB"))) {
      pcfgCB = new Evalb.CBEval("pcfg CB", runningAverages);
    }
    AbstractEval pcfgDA = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("pcfgDA"))) {
      pcfgDA = new DependencyEval("pcfg DA", runningAverages, tlp.punctuationWordAcceptFilter());
    }
    AbstractEval pcfgTA = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("pcfgTA"))) {
      pcfgTA = new TaggingEval("pcfg Tag", runningAverages, pd.lex);
    }
    AbstractEval depDA = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("depDA"))) {
      depDA = new DependencyEval("dep DA", runningAverages, tlp.punctuationWordAcceptFilter());
    }
    AbstractEval depTA = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("depTA"))) {
      depTA = new TaggingEval("dep Tag", runningAverages, pd.lex, true);
    }
    Evalb factLB = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("factLB"))) {
      factLB = new Evalb("factor LP/LR", runningAverages);
    }
    AbstractEval factCB = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("factCB"))) {
      factCB = new Evalb.CBEval("fact CB", runningAverages);
    }
    AbstractEval factDA = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("factDA"))) {
      factDA = new DependencyEval("factor DA", runningAverages, tlp.punctuationWordAcceptFilter());
    }
    AbstractEval factTA = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("factTA"))) {
      if (op.doPCFG) {
        factTA = new TaggingEval("factor Tag", runningAverages, pd.lex);
      } else {
        // only doing dep parser, and need to get tags out in special way....
        factTA = new TaggingEval("factor Tag", runningAverages, pd.lex, true);
      }
    }
    AbstractEval pcfgRUO = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("pcfgRUO"))) {
      pcfgRUO = new AbstractEval.RuleErrorEval("pcfg Rule under/over");
    }
    AbstractEval pcfgCUO = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("pcfgCUO"))) {
      pcfgCUO = new AbstractEval.CatErrorEval("pcfg Category under/over");
    }
    AbstractEval pcfgCatE = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("pcfgCatE"))) {
      pcfgCatE = new EvalbByCat("pcfg Category Eval", runningAverages);
    }
    AbstractEval.ScoreEval pcfgLL = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("pcfgLL"))) {
      pcfgLL = new AbstractEval.ScoreEval("pcfgLL", runningAverages);
    }
    AbstractEval.ScoreEval depLL = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("depLL"))) {
      depLL = new AbstractEval.ScoreEval("depLL", runningAverages);
    }
    AbstractEval.ScoreEval factLL = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("factLL"))) {
      factLL = new AbstractEval.ScoreEval("factLL", runningAverages);
    }
    // this one is for the various k Good/Best options.  Just for individual results
    AbstractEval kGoodLB = new Evalb("kGood LP/LR", false);

    // no annotation
    TreeAnnotatorAndBinarizer binarizerOnly;
    if (!Train.leftToRight) {
      binarizerOnly = new TreeAnnotatorAndBinarizer(tlpParams, op.forceCNF, false, false);
    } else {
      binarizerOnly = new TreeAnnotatorAndBinarizer(tlpParams.headFinder(), new LeftHeadFinder(), tlpParams, op.forceCNF, false, false);
    }

    if(Test.preTag) {
      try {
        Class[] argsClass = new Class[]{String.class};
        Object[] arguments = new Object[]{Test.taggerSerializedFile};
        System.err.printf("Loading tagger from serialized file %s ...\n",Test.taggerSerializedFile);
        tagger = (SentenceProcessor<HasWord, Word>)
        Class.forName("edu.stanford.nlp.tagger.maxent.MaxentTagger").getConstructor(argsClass).newInstance(arguments);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    boolean saidMemMessage = false;
    Timing timer = new Timing();
    for (Tree goldTree : testTreebank) {
      boolean outsideLengthBound = false;
      Sentence<? extends HasWord> s = getInputSentence(goldTree);
      timer.start();
      pwErr.println("Parsing [len. " + s.length() + "]: " + s);
      Tree tree = null;
      try {
        if ( ! parse(s)) {
          pwErr.print("Sentence couldn't be parsed by grammar.");
          if (pparser != null && pparser.hasParse() && fallbackToPCFG) {
            pwErr.println("... falling back to PCFG parse.");
            tree = getBestPCFGParse();
          } else {
            pwErr.println();
          }
        } else {
          tree = getBestParse();
          if (bparser != null) pwErr.println("FactoredParser parse score is " + bparser.getBestScore());
        }
      } catch (OutOfMemoryError e) {
        if (Test.maxLength != -0xDEADBEEF) {
          // this means they explicitly asked for a length they cannot handle.
          // Throw exception.  Avoid string concatenation before throw it.
          pwErr.print("NOT ENOUGH MEMORY TO PARSE SENTENCES OF LENGTH ");
          pwErr.println(Test.maxLength);
          throw e;
        } else {
          if ( ! saidMemMessage) {
            printOutOfMemory(pwErr);
            saidMemMessage = true;
          }
          if (pparser.hasParse() && fallbackToPCFG) {
            try {
              String what = "dependency";
              if (dparser.hasParse()) {
                what = "factored";
              }
              pwErr.println("Sentence too long for " + what + " parser.  Falling back to PCFG parse...");
              tree = getBestPCFGParse();
            } catch (OutOfMemoryError oome) {
              oome.printStackTrace();
              pwErr.println("No memory to gather PCFG parse. Skipping...");
              pparser.nudgeDownArraySize();
            }
          } else {
            pwErr.println("Sentence has no parse using PCFG grammar (or no PCFG fallback).  Skipping...");
          }
          pwErr.println();
        }
      } catch (UnsupportedOperationException uoe) {
        pwErr.println("Sentence too long (or zero words).");
        outsideLengthBound = true;
      }

      //combo parse goes to System.out
      if (Test.verbose) {
        pwOut.println("ComboParser best");
        Tree ot = tree;
        if (ot != null && ! tlpParams.treebankLanguagePack().isStartSymbol(ot.value())) {
          ot = ot.treeFactory().newTreeNode(tlpParams.treebankLanguagePack().startSymbol(), Collections.singletonList(ot));
        }
        treePrint.printTree(ot, pwOut);
      } else {
        treePrint.printTree(tree, pwOut);
      }

      if (tree != null) {
        // print various n-best like outputs
        // the options here should be rethought someday....
        if (Test.printAllBestParses) {
          List<ScoredObject<Tree>> parses = pparser.getBestParses();
          int sz = parses.size();
          if (sz > 1) {
            pwOut.println("There were " + sz + " best PCFG parses with score " + parses.get(0).score() + '.');
            Tree transGoldTree = tc.transformTree(goldTree);
            int iii = 0;
            for (ScoredObject<Tree> sot : parses) {
              iii++;
              Tree tb = sot.object();
              Tree tbd = debinarizer.transformTree(tb);
              tbd = subcategoryStripper.transformTree(tbd);
              pwOut.println("PCFG Parse #" + iii + " with score " + tbd.score());
              tbd.pennPrint(pwOut);
              Tree tbtr = tc.transformTree(tbd);
              // pwOut.println("Tree size = " + tbtr.size() + "; depth = " + tbtr.depth());
              kGoodLB.evaluate(tbtr, transGoldTree, pwErr);
            }
          }
        }
        if (Test.printPCFGkBest > 0) {
          List<ScoredObject<Tree>> trees = getKBestPCFGParses(Test.printPCFGkBest);
          Tree transGoldTree = tc.transformTree(goldTree);
          int i = 0;
          for (ScoredObject<Tree> tp : trees) {
            i++;
            pwOut.println("PCFG Parse #" + i + " with score " + tp.score());
            Tree tbd = tp.object();
            tbd.pennPrint(pwOut);
            Tree tbtr = tc.transformTree(tbd);
            kGoodLB.evaluate(tbtr, transGoldTree, pwErr);
          }
        }
        if (Test.printFactoredKGood > 0 && bparser.hasParse()) {
          // DZ: debug n best trees
          List<ScoredObject<Tree>> trees = getKGoodFactoredParses(Test.printFactoredKGood);
          Tree transGoldTree = tc.transformTree(goldTree);
          int ii = 0;
          for (ScoredObject<Tree> tp : trees) {
            ii++;
            pwOut.println("Factored Parse #" + ii + " with score " + tp.score());
            Tree tbd = tp.object();
            tbd.pennPrint(pwOut);
            Tree tbtr = tc.transformTree(tbd);
            kGoodLB.evaluate(tbtr, transGoldTree, pwOut);
          }
        }
      }
      if (Test.verbose && ! outsideLengthBound) {
        pwOut.println("Correct parse");
        // if (pparser != null) {  // for printing probs in tree
        //   pwErr.println("Score: " + pparser.scoreBinarizedTree(binarizer.transformTree(goldTree),true));
        //   pwOut.println(pparser.scoreNonBinarizedTree(goldTree));
        // }
        Tree evalTree = subcategoryStripper.transformTree(goldTree);
        treePrint.printTree(evalTree, pwOut);
      }

      timer.report("Parsing Sentence");

      if (tree != null) {
        Tree transGoldTree = tc.transformTree(goldTree);
        if(transGoldTree != null)
          transGoldTree = subcategoryStripper.transformTree(transGoldTree);
        if (transGoldTree == null) {
          pwErr.println("Couldn't transform gold tree for evaluation, skipping eval. Gold tree was:");
          goldTree.pennPrint(pwErr);
          continue;
        }
        Tree treePCFG = getBestPCFGParse();
        if (treePCFG != null) {
          Tree treePCFGeval = tc.transformTree(treePCFG);

          if (pcfgLB != null) {
            pcfgLB.evaluate(treePCFGeval, transGoldTree, pwErr);
          }
          if (pcfgCB != null) {
            pcfgCB.evaluate(treePCFGeval, transGoldTree, pwErr);
          }
          if (pcfgDA != null) {
            // this still doesn't work yet!  HeadFinder doesn't work on binarized tree
            Tree pcfgTreeB = pparser.getBestParse();
            Tree cwtPCFGTreeB = pcfgTreeB.deeperCopy(new LabeledScoredTreeFactory(),
                new CategoryWordTagFactory());
            cwtPCFGTreeB.percolateHeads(tlpParams.headFinder());
            Tree goldTreeB = binarizerOnly.transformTree(goldTree);
            pcfgDA.evaluate(cwtPCFGTreeB, goldTreeB, pwErr);
          }
          if (pcfgTA != null) {
            pcfgTA.evaluate(treePCFGeval, transGoldTree, pwErr);
          }
          if (pcfgLL != null && pparser != null) {
            pcfgLL.recordScore(pparser, pwErr);
          }
          if (pcfgRUO != null) {
            pcfgRUO.evaluate(treePCFGeval, transGoldTree, pwErr);
          }
          if (pcfgCUO != null) {
            pcfgCUO.evaluate(treePCFGeval, transGoldTree, pwErr);
          }
          if (pcfgCatE != null) {
            pcfgCatE.evaluate(treePCFGeval, transGoldTree, pwErr);
          }

        }
        Tree treeDep = getBestDependencyParse();
        if (treeDep != null) {
          Tree goldTreeB = binarizerOnly.transformTree(goldTree);
          if (depDA != null) {
            depDA.evaluate(treeDep, goldTreeB, pwErr);
          }
          if (depTA != null) {
            Tree undoneTree = debinarizer.transformTree(treeDep);
            undoneTree = subcategoryStripper.transformTree(undoneTree);
            // System.err.println("subcategoryStripped tree: " + undoneTree.toStructureDebugString());
            depTA.evaluate(undoneTree, goldTree, pwErr);
          }
          if (depLL != null && dparser != null) {
            depLL.recordScore(dparser, pwErr);
          }
          Tree factTreeB;
          if (bparser != null && parseSucceeded) {
            factTreeB = bparser.getBestParse();
          } else {
            factTreeB = treeDep;
          }
          if (factDA != null) {
            factDA.evaluate(factTreeB, goldTreeB, pwErr);
          }
        }

        Tree treeFact = tc.transformTree(tree);
        if (factLB != null) {
          factLB.evaluate(treeFact, transGoldTree, pwErr);
        }
        if (factTA != null) {
          factTA.evaluate(tree, br.transformTree(goldTree), pwErr);
        }
        if (factLL != null && bparser != null) {
          factLL.recordScore(bparser, pwErr);
        }
        if (factCB != null) {
          factCB.evaluate(treeFact, transGoldTree, pwErr);
        }
        if (Test.evalb) {
          // empty out scores just in case
          nanScores(tree);
          EvalbFormatWriter.writeEVALBline(treeFact, transGoldTree);
        }
      }
      pwErr.println();
    } // for tree iterator
    treebankTotalTtimer.done("Testing on treebank");
    if (saidMemMessage) {
      printOutOfMemory(pwErr);
    }
    if (Test.evalb) {
      EvalbFormatWriter.closeEVALBfiles();
    }
    if (summary) {
      if (pcfgLB != null) pcfgLB.display(false, pwErr);
      if (pcfgCB != null) pcfgCB.display(false, pwErr);
      if (pcfgDA != null) pcfgDA.display(false, pwErr);
      if (pcfgTA != null) pcfgTA.display(false, pwErr);
      if (pcfgLL != null && pparser != null) pcfgLL.display(false, pwErr);
      if (depDA != null) depDA.display(false, pwErr);
      if (depTA != null) depTA.display(false, pwErr);
      if (depLL != null && dparser != null) depLL.display(false, pwErr);
      if (factLB != null) factLB.display(false, pwErr);
      if (factCB != null) factCB.display(false, pwErr);
      if (factDA != null) factDA.display(false, pwErr);
      if (factTA != null) factTA.display(false, pwErr);
      if (factLL != null && bparser != null) factLL.display(false, pwErr);
      if (pcfgCatE != null) pcfgCatE.display(false, pwErr);
    }
    // these ones only have a display mode, so display if turned on!!
    if (pcfgRUO != null) pcfgRUO.display(true, pwErr);
    if (pcfgCUO != null) pcfgCUO.display(true, pwErr);
    if (tsv) {
      NumberFormat nf = new DecimalFormat("0.00");
      pwErr.println("factF1\tfactDA\tfactEx\tpcfgF1\tdepDA\tfactTA\tnum");
      if (factLB != null) pwErr.print(nf.format(factLB.getEvalbF1Percent()));
      pwErr.print("\t");
      if (dparser != null && factDA != null) pwErr.print(nf.format(factDA.getEvalbF1Percent()));
      pwErr.print("\t");
      if (factLB != null) pwErr.print(nf.format(factLB.getExactPercent()));
      pwErr.print("\t");
      if (pcfgLB != null) pwErr.print(nf.format(pcfgLB.getEvalbF1Percent()));
      pwErr.print("\t");
      if (dparser != null && depDA != null) pwErr.print(nf.format(depDA.getEvalbF1Percent()));
      pwErr.print("\t");
      if (pparser != null && factTA != null) pwErr.print(nf.format(factTA.getEvalbF1Percent()));
      pwErr.print("\t");
      if (factLB != null) pwErr.print(factLB.getNum());
      pwErr.println();
    }

    double f1 = 0.0;
    if (factLB != null) {
      f1 = factLB.getEvalbF1();
    }
    return f1;
  } // end testOnTreebank()

  // Remove tree scores, so they don't print.
  // TODO: The printing architecture should be fixed up in the trees package
  // sometime.
  private static void nanScores(Tree tree) {
    tree.setScore(Double.NaN);
    Tree[] kids = tree.children();
    for (int i = 0; i < kids.length; i++) {
      nanScores(kids[i]);
    }
  }


  /** Parse the files with names given in the String array args elements from
   *  index argIndex on.
   */
  private void parseFiles(String[] args, int argIndex, boolean tokenized, TokenizerFactory<? extends HasWord> tokenizerFactory, DocumentPreprocessor documentPreprocessor, String elementDelimiter, String sentenceDelimiter, Function<List<HasWord>, List<HasWord>> escaper, int tagDelimiter) {
    PrintWriter pwOut = op.tlpParams.pw();
    PrintWriter pwErr = op.tlpParams.pw(System.err);
    TreePrint treePrint = getTreePrint();
    int numWords = 0;
    int numSents = 0;
    int numUnparsable = 0;
    int numNoMemory = 0;
    int numFallback = 0;
    int numSkipped = 0;
    Timing timer = new Timing();
    TreebankLanguagePack tlp = op.tlpParams.treebankLanguagePack();
    // set the tokenizer
    if (tokenized) {
      tokenizerFactory = WhitespaceTokenizer.factory();
    }
    if (tokenizerFactory == null) {
      tokenizerFactory = tlp.getTokenizerFactory();
    }
    if (Test.verbose) {
      System.err.println("parseFiles: Tokenizer factory is: " + tokenizerFactory);
      System.err.println("Sentence final words are: " + Arrays.asList(tlp.sentenceFinalPunctuationWords()));
      System.err.println("File encoding is: " + op.tlpParams.getInputEncoding());
    }
    documentPreprocessor.setTokenizerFactory(tokenizerFactory);
    documentPreprocessor.setSentenceFinalPuncWords(tlp.sentenceFinalPunctuationWords());
    documentPreprocessor.setEncoding(op.tlpParams.getInputEncoding());
    boolean saidMemMessage = false;

    // evaluation setup
    boolean runningAverages = Boolean.parseBoolean(Test.evals.getProperty("runningAverages"));
    boolean summary = Boolean.parseBoolean(Test.evals.getProperty("summary"));
    AbstractEval.ScoreEval pcfgLL = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("pcfgLL"))) {
      pcfgLL = new AbstractEval.ScoreEval("pcfgLL", runningAverages);
    }
    AbstractEval.ScoreEval depLL = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("depLL"))) {
      depLL = new AbstractEval.ScoreEval("depLL", runningAverages);
    }
    AbstractEval.ScoreEval factLL = null;
    if (Boolean.parseBoolean(Test.evals.getProperty("factLL"))) {
      factLL = new AbstractEval.ScoreEval("factLL", runningAverages);
    }

    timer.start();
    for (int i = argIndex; i < args.length; i++) {
      String filename = args[i];
      try {
        BufferedReader in = null;
        List<List<? extends HasWord>> document; // initialized just below
        if(filename.equals("-")) {
          document = Generics.newArrayList();
          in = new BufferedReader(new InputStreamReader(System.in));
        } else if (elementDelimiter != null) {
          document = documentPreprocessor.getSentencesFromXML(filename, escaper, elementDelimiter, sentenceDelimiter);
        } else {
          document = documentPreprocessor.getSentencesFromText(filename, escaper, sentenceDelimiter, tagDelimiter);
        }
        System.err.println("Parsing file: " + filename + " with " + document.size() + " sentences.");
        PrintWriter pwo = pwOut;
        if (Test.writeOutputFiles) {
          String ext = Test.outputFilesExtension == null ? "stp":
            Test.outputFilesExtension;
          String fname = filename + '.' + ext;
          if (Test.outputFilesDirectory != null) {
            String fseparator = System.getProperty("file.separator");
            if (fseparator == null || "".equals(fseparator)) {
              fseparator = "/";
            }
            int ind = fname.lastIndexOf(fseparator);
            fname = fname.substring(ind + 1);
            if ( ! "".equals(Test.outputFilesDirectory)) {
              fname = Test.outputFilesDirectory + fseparator + fname;
            }
          }
          try {
            pwo = op.tlpParams.pw(new FileOutputStream(fname));
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        }
        treePrint.printHeader(pwo, op.tlpParams.getOutputEncoding());
        int num = 0;
        do {
          if(in != null) {
            document.clear();
            String line = in.readLine();
            if(line == null) {
              in.close();
              in = null;
              break;
            }
            document.add(documentPreprocessor.getWordsFromString(line));
          }
          for (List<? extends HasWord> sentence : document) {
            num++;
            numSents++;
            int len = sentence.size();
            numWords += len;
            pwErr.println("Parsing [sent. " + num + " len. " + len + "]: " + sentence);
            //+ unicode debugging
            // edu.stanford.nlp.misc.SeeChars.seeList(sentence, op.tlpParams.getOutputEncoding());
            //- unicode debugging
            Tree ansTree = null;
            try {
              if ( ! parse(sentence)) {
                pwErr.print("Sentence couldn't be parsed by grammar.");
                if (pparser != null && pparser.hasParse() && fallbackToPCFG) {
                  pwErr.println("... falling back to PCFG parse.");
                  ansTree = getBestPCFGParse();
                  numFallback++;
                } else {
                  pwErr.println();
                  numUnparsable++;
                }
              } else {
                // System.out.println("Score: " + lp.pparser.bestScore);
                ansTree = getBestParse();
              }
              if (pcfgLL != null && pparser != null) {
                pcfgLL.recordScore(pparser, pwErr);
              }
              if (depLL != null && dparser != null) {
                depLL.recordScore(dparser, pwErr);
              }
              if (factLL != null && bparser != null) {
                factLL.recordScore(bparser, pwErr);
              }
            } catch (OutOfMemoryError e) {
              if (Test.maxLength != -0xDEADBEEF) {
                // this means they explicitly asked for a length they cannot handle. Throw exception.
                pwErr.println("NOT ENOUGH MEMORY TO PARSE SENTENCES OF LENGTH " + Test.maxLength);
                pwo.println("NOT ENOUGH MEMORY TO PARSE SENTENCES OF LENGTH " + Test.maxLength);
                throw e;
              } else {
                if ( ! saidMemMessage) {
                  printOutOfMemory(pwErr);
                  saidMemMessage = true;
                }
                if (pparser.hasParse() && fallbackToPCFG) {
                  try {
                    String what = "dependency";
                    if (dparser.hasParse()) {
                      what = "factored";
                    }
                    pwErr.println("Sentence too long for " + what + " parser.  Falling back to PCFG parse...");
                    ansTree = getBestPCFGParse();
                    numFallback++;
                  } catch (OutOfMemoryError oome) {
                    oome.printStackTrace();
                    numNoMemory++;
                    pwErr.println("No memory to gather PCFG parse. Skipping...");
                    pwo.println("Sentence skipped:  no PCFG fallback.");
                    pparser.nudgeDownArraySize();
                  }
                } else {
                  pwErr.println("Sentence has no parse using PCFG grammar (or no PCFG fallback).  Skipping...");
                  pwo.println("Sentence skipped: no PCFG fallback.");
                  numSkipped++;
                }
              }
            } catch (UnsupportedOperationException uoe) {
              pwErr.println("Sentence too long (or zero words).");
              //pwo.println("Sentence skipped: too long (or zero words).");
              numWords -= len;
              numSkipped++;
            }
            try {
              treePrint.printTree(ansTree, Integer.toString(num), pwo);
            } catch (RuntimeException re) {
              pwErr.println("TreePrint.printTree skipped: out of memory (or other error)");
              re.printStackTrace();
              numNoMemory++;
              try {
                treePrint.printTree(null, Integer.toString(num), pwo);
              } catch (Exception e) {
                pwo.println("Sentence skipped: out of memory and error calling TreePrint.");
                e.printStackTrace();
              }
            }
            // crude addition of k-best tree printing
            if (Test.printPCFGkBest > 0 && pparser.hasParse()) {
              List<ScoredObject<Tree>> trees = getKBestPCFGParses(Test.printPCFGkBest);
              treePrint.printTrees(trees, Integer.toString(num), pwo);
            } else if (Test.printFactoredKGood > 0 && bparser.hasParse()) {
              // DZ: debug n best trees
              List<ScoredObject<Tree>> trees = getKGoodFactoredParses(Test.printFactoredKGood);
              treePrint.printTrees(trees, Integer.toString(num), pwo);
            }
          } // for sentence : document
        } while(in != null);
        treePrint.printFooter(pwo);
        if (Test.writeOutputFiles) {
          pwo.close();
        }
        System.err.println("Parsed file: " + filename + " [" + num + " sentences].");

      } catch (IOException e) {
        pwErr.println("ERROR: Couldn't open file: " + filename);
      }
    } // end for each file args[argIndex]
    long millis = timer.stop();

    if (summary) {
      if (pcfgLL != null) pcfgLL.display(false, pwErr);
      if (depLL != null) depLL.display(false, pwErr);
      if (factLL != null) factLL.display(false, pwErr);
    }

    if ( saidMemMessage) {
      printOutOfMemory(pwErr);
    }
    double wordspersec = numWords / (((double) millis) / 1000);
    double sentspersec = numSents / (((double) millis) / 1000);
    NumberFormat nf = new DecimalFormat("0.00"); // easier way!
    pwErr.println("Parsed " + numWords + " words in " + numSents +
        " sentences (" + nf.format(wordspersec) + " wds/sec; " +
        nf.format(sentspersec) + " sents/sec).");
    if (numFallback > 0) {
      pwErr.println("  " + numFallback + " sentences were parsed by fallback to PCFG.");
    }
    if (numUnparsable > 0 || numNoMemory > 0 || numSkipped > 0) {
      pwErr.println("  " + (numUnparsable + numNoMemory + numSkipped) + " sentences were not parsed:");
      if (numUnparsable > 0) {
        pwErr.println("    " + numUnparsable + " were not parsable with non-zero probability.");
      }
      if (numNoMemory > 0) {
        pwErr.println("    " + numNoMemory + " were skipped because of insufficient memory.");
      }
      if (numSkipped > 0) {
        pwErr.println("    " + numSkipped + " were skipped as length 0 or greater than " + Test.maxLength);
      }
    }
  } // end parseFiles


  /**
   * This will set options to the parser, in a way exactly equivalent to
   * passing in the same sequence of command-line arguments.  This is a useful
   * convenience method when building a parser programmatically. The options
   * passed in should
   * be specified like command-line arguments, including with an initial
   * minus sign.
   * <p/>
   * <i>Notes:</i> This can be used to set parsing-time flags for a
   * serialized parser.  You can also still change things serialized in
   * Options,
   * but this will probably degrade parsing performance.  The vast majority
   * of command line flags can be passed to this method, but you cannot
   * pass in options that specify the treebank or grammar to be loaded,
   * the grammar to be written, trees or files to be parsed or details of
   * their encoding, nor the TreebankLangParserParams (<code>-tLPP</code>)
   * to use. The TreebankLangParserParams should be set up on construction of
   * a LexicalizedParser, by constructing an Options that uses the required
   * TreebankLangParserParams, and passing that to a LexicalizedParser
   * constructor.  Note that despite
   * this method being an instance method, many flags are actually set as
   * static class variables.
   *
   * @param flags Arguments to the parser, for example,
   *              {"-outputFormat", "typedDependencies", "-maxLength", "70"}
   * @throws IllegalArgumentException If an unknown flag is passed in
   */
  public void setOptionFlags(String... flags) {
    op.setOptions(flags);
  }


  private static void printArgs(String[] args, PrintStream ps) {
    ps.print("LexicalizedParser invoked with arguments:");
    for (String arg : args) {
      ps.print(' ' + arg);
    }
    ps.println();
  }

  /**
   * A main program for using the parser with various options.
   * This program can be used for building and serializing
   * a parser from treebank data, for parsing sentences from a file
   * or URL using a serialized or text grammar parser,
   * and (mainly for parser quality testing)
   * for training and testing a parser on a treebank all in one go. <p>
   * Sample Usages: <br><code>
   * java -mx1500m edu.stanford.nlp.parser.lexparser.LexicalizedParser [-v]
   * -train trainFilesPath fileRange -saveToSerializedFile
   * serializedGrammarFilename</code><p>
   * <code> java -mx1500m edu.stanford.nlp.parser.lexparser.LexicalizedParser
   * [-v] -train trainFilesPath fileRange
   * -testTreebank testFilePath fileRange</code><p>
   * <code>java -mx512m edu.stanford.nlp.parser.lexparser.LexicalizedParser
   * [-v] serializedGrammarPath filename+</code><p>
   * <code>java -mx512m edu.stanford.nlp.parser.lexparser.LexicalizedParser
   * [-v] -loadFromSerializedFile serializedGrammarPath
   * -testTreebank testFilePath fileRange
   * </code><p>
   * If the <code>serializedGrammarPath</code> ends in <code>.gz</code>,
   * then the grammar is written and read as a compressed file (GZip).
   * If the <code>serializedGrammarPath</code> is a URL, starting with
   * <code>http://</code>, then the parser is read from the URL.
   * A fileRange specifies a numeric value that must be included within a
   * filename for it to be used in training or testing (this works well with
   * most current treebanks).  It can be specified like a range of pages to be
   * printed, for instance as <code>200-2199</code> or
   * <code>1-300,500-725,9000</code> or just as <code>1</code> (if all your
   * trees are in a single file, just give a dummy argument such as
   * <code>0</code> or <code>1</code>).
   * The parser can write a grammar as either a serialized Java object file
   * or in a text format (or as both), specified with the following options:
   * <p>
   * <code>java edu.stanford.nlp.parser.lexparser.LexicalizedParser [-v] -train
   * trainFilesPath [fileRange] [-saveToSerializedFile grammarPath]
   * [-saveToTextFile grammarPath]</code><p>
   * If no files are supplied to parse, then a hardwired sentence
   * is parsed. <p>
   *
   * In the same position as the verbose flag (<code>-v</code>), many other
   * options can be specified.  The most useful to an end user are:
   * <UL>
   * <LI><code>-tLPP class</code> Specify a different
   * TreebankLangParserParams, for when using a different language or
   * treebank (the default is English Penn Treebank). <i>This option MUST occur
   * before any other language-specific options that are used (or else they
   * are ignored!).</i>
   * (It's usually a good idea to specify this option even when loading a
   * serialized grammar; it is necessary if the language pack specifies a
   * needed character encoding or you wish to specify language-specific
   * options on the command line.)</LI>
   * <LI><code>-encoding charset</code> Specify the character encoding of the
   * input and output files.  This will override the value in the
   * <code>TreebankLangParserParams</code>, provided this option appears
   * <i>after</i> any <code>-tLPP</code> option.</LI>
   * <LI><code>-tokenized</code> Says that the input is already separated
   * into whitespace-delimited tokens.  If this option is specified, any
   * tokenizer specified for the language is ignored, and a universal (Unicode)
   * tokenizer, which divides only on whitespace, is used.
   * Unless you also specify
   * <code>-escaper</code>, the tokens <i>must</i> all be correctly
   * tokenized tokens of the appropriate treebank for the parser to work
   * well (for instance, if using the Penn English Treebank, you must have
   * coded "(" as "-LRB-", "3/4" as "3\/4", etc.)</LI>
   * <li><code>-escaper class</code> Specify a class of type
   * {@link Function}&lt;List&lt;HasWord&gt;,List&lt;HasWord&gt;&gt; to do
   * customized escaping of tokenized text.  This class will be run over the
   * tokenized text and can fix the representation of tokens. For instance,
   * it could change "(" to "-LRB-" for the Penn English Treebank.  A
   * provided escaper that does such things for the Penn English Treebank is
   * <code>edu.stanford.nlp.process.PTBEscapingProcessor</code>
   * <li><code>-tokenizerFactory class</code> Specifies a
   * TokenizerFactory class to be used for tokenization</li>
   * <li><code>-tokenizerOptions options</code> Specifies options to a
   * TokenizerFactory class to be used for tokenization.   A comma-separated
   * list. For PTBTokenizer, options of interest include 
   * <code>americanize=false</code> and <code>asciiQuotes</code> (for German).
   * Note that any choice of tokenizer options that conflicts with the
   * tokenization used in the parser training data will likely degrade parser
   * performance. </li>
   * <li><code>-sentences token </code> Specifies a token that marks sentence
   * boundaries.  A value of <code>newline</code> causes sentence breaking on
   * newlines.  A value of <code>onePerElement</code> causes each element
   * (using the XML <code>-parseInside</code> option) to be treated as a
   * sentence. All other tokens will be interpreted literally, and must be
   * exactly the same as tokens returned by the tokenizer.  For example,
   * you might specify "|||" and put that symbol sequence as a token between
   * sentences.
   * If no explicit sentence breaking option is chosen, sentence breaking
   * is done based on a set of language-particular sentence-ending patterns.
   * </li>
   * <LI><code>-parseInside element</code> Specifies that parsing should only
   * be done for tokens inside the indicated XML-style
   * elements (done as simple pattern matching, rather than XML parsing).
   * For example, if this is specified as <code>sentence</code>, then
   * the text inside the <code>sentence</code> element
   * would be parsed.
   * Using "-parseInside s" gives you support for the input format of
   * Charniak's parser. Sentences cannot span elements. Whether the
   * contents of the element are treated as one sentence or potentially
   * multiple sentences is controlled by the <code>-sentences</code> flag.
   * The default is potentially multiple sentences.
   * This option gives support for extracting and parsing
   * text from very simple SGML and XML documents, and is provided as a
   * user convenience for that purpose. If you want to really parse XML
   * documents before NLP parsing them, you should use an XML parser, and then
   * call to a LexicalizedParser on appropriate CDATA.
   * <LI><code>-tagSeparator char</code> Specifies to look for tags on words
   * following the word and separated from it by a special character
   * <code>char</code>.  For instance, many tagged corpora have the
   * representation "house/NN" and you would use <code>-tagSeparator /</code>.
   * Notes: This option requires that the input be pretokenized.
   * The separator has to be only a single character, and there is no
   * escaping mechanism. However, splitting is done on the <i>last</i>
   * instance of the character in the token, so that cases like
   * "3\/4/CD" are handled correctly.  The parser will in all normal
   * circumstances use the tag you provide, but will override it in the
   * case of very common words in cases where the tag that you provide
   * is not one that it regards as a possible tagging for the word.
   * The parser supports a format where only some of the words in a sentence
   * have a tag (if you are calling the parser programmatically, you indicate
   * them by having them implement the <code>HasTag</code> interface).
   * You can do this at the command-line by only having tags after some words,
   * but you are limited by the fact that there is no way to escape the
   * tagSeparator character.</LI>
   * <LI><code>-maxLength leng</code> Specify the longest sentence that
   * will be parsed (and hence indirectly the amount of memory
   * needed for the parser). If this is not specified, the parser will
   * try to dynamically grow its parse chart when long sentence are
   * encountered, but may run out of memory trying to do so.</LI>
   * <LI><code>-outputFormat styles</code> Choose the style(s) of output
   * sentences: <code>penn</code> for prettyprinting as in the Penn
   * treebank files, or <code>oneline</code> for printing sentences one
   * per line, <code>words</code>, <code>wordsAndTags</code>,
   * <code>dependencies</code>, <code>typedDependencies</code>,
   * or <code>typedDependenciesCollapsed</code>.
   * Multiple options may be specified as a comma-separated
   * list.  See TreePrint class for further documentation.</LI>
   * <LI><code>-outputFormatOptions</code> Provide options that control the
   * behavior of various <code>-outputFormat</code> choices, such as
   * <code>lexicalize</code>, <code>stem</code>, <code>markHeadNodes</code>,
   * or <code>xml</code>.
   * Options are specified as a comma-separated list.</LI>
   * <LI><code>-writeOutputFiles</code> Write output files corresponding
   * to the input files, with the same name but a <code>".stp"</code>
   * file extension.  The format of these files depends on the
   * <code>outputFormat</code> option.  (If not specified, output is sent
   * to stdout.)</LI>
   * <LI><code>-outputFilesExtension</code> The extension that is appended to
   * the filename that is being parsed to produce an output file name (with the
   * -writeOutputFiles option). The default is <code>stp</code>.  Don't
   * include the period.
   * <LI><code>-outputFilesDirectory</code> The directory in which output
   * files are written (when the -writeOutputFiles option is specified).
   * If not specified, output files are written in the same directory as the
   * input files.
   * </UL>
   * See also the package documentation for more details and examples of use.
   *
   * @param args Command line arguments, as above
   */
  public static void main(String[] args) {
    boolean train = false;
    boolean saveToSerializedFile = false;
    boolean saveToTextFile = false;
    String serializedInputFileOrUrl = null;
    String textInputFileOrUrl = null;
    String serializedOutputFileOrUrl = null;
    String textOutputFileOrUrl = null;
    String treebankPath = null;
    Treebank testTreebank = null;
    Treebank tuneTreebank = null;
    String testPath = null;
    FileFilter testFilter = null;
    String tunePath = null;
    FileFilter tuneFilter = null;
    FileFilter trainFilter = null;
    String secondaryTreebankPath = null;
    double secondaryTreebankWeight = 1.0;
    FileFilter secondaryTrainFilter = null;

    // variables needed to process the files to be parsed
    TokenizerFactory<? extends HasWord> tokenizerFactory = null;
    String tokenizerOptions = null;
    String tokenizerFactoryClass = null;
    DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor();
    boolean tokenized = false; // whether or not the input file has already been tokenized
    Function<List<HasWord>, List<HasWord>> escaper = null;
    int tagDelimiter = -1;
    String sentenceDelimiter = null;
    String elementDelimiter = null;
    int argIndex = 0;
    if (args.length < 1) {
      System.err.println("Basic usage (see Javadoc for more): java edu.stanford.nlp.parser.lexparser.LexicalizedParser parserFileOrUrl filename*");
      return;
    }

    Options op = new Options();
    String encoding = null;
    // while loop through option arguments
    while (argIndex < args.length && args[argIndex].charAt(0) == '-') {
      if (args[argIndex].equalsIgnoreCase("-train") ||
          args[argIndex].equalsIgnoreCase("-trainTreebank")) {
        train = true;
        int numSubArgs = numSubArgs(args, argIndex);
        argIndex++;
        if (numSubArgs >= 1) {
          treebankPath = args[argIndex];
          argIndex++;
        } else {
          throw new RuntimeException("Error: -train option must have treebankPath as first argument.");
        }
        if (numSubArgs == 2) {
          trainFilter = new NumberRangesFileFilter(args[argIndex++], true);
        } else if (numSubArgs >= 3) {
          try {
            int low = Integer.parseInt(args[argIndex]);
            int high = Integer.parseInt(args[argIndex + 1]);
            trainFilter = new NumberRangeFileFilter(low, high, true);
            argIndex += 2;
          } catch (NumberFormatException e) {
            // maybe it's a ranges expression?
            trainFilter = new NumberRangesFileFilter(args[argIndex], true);
            argIndex++;
          }
        }
      } else if (args[argIndex].equalsIgnoreCase("-train2")) {
        // train = true;     // cdm july 2005: should require -train for this
        int numSubArgs = numSubArgs(args, argIndex);
        argIndex++;
        if (numSubArgs < 2) {
          throw new RuntimeException("Error: -train2 <treebankPath> [<ranges>] <weight>.");
        }
        secondaryTreebankPath = args[argIndex++];
        secondaryTrainFilter = (numSubArgs == 3) ? new NumberRangesFileFilter(args[argIndex++], true) : null;
        secondaryTreebankWeight = Double.parseDouble(args[argIndex++]);
      } else if (args[argIndex].equalsIgnoreCase("-tLPP") && (argIndex + 1 < args.length)) {
        try {
          op.tlpParams = (TreebankLangParserParams) Class.forName(args[argIndex + 1]).newInstance();
        } catch (ClassNotFoundException e) {
          System.err.println("Class not found: " + args[argIndex + 1]);
        } catch (InstantiationException e) {
          System.err.println("Couldn't instantiate: " + args[argIndex + 1] + ": " + e.toString());
        } catch (IllegalAccessException e) {
          System.err.println("Illegal access" + e);
        }
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-encoding")) {
        // sets encoding for TreebankLangParserParams
        // redone later to override any serialized parser one read in
        encoding = args[argIndex + 1];
        op.tlpParams.setInputEncoding(encoding);
        op.tlpParams.setOutputEncoding(encoding);
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-tokenized")) {
        tokenized = true;
        argIndex += 1;
      } else if (args[argIndex].equalsIgnoreCase("-escaper")) {
        try {
          escaper = (Function<List<HasWord>, List<HasWord>>) Class.forName(args[argIndex + 1]).newInstance();
        } catch (Exception e) {
          System.err.println("Couldn't instantiate escaper " + args[argIndex + 1] + ": " + e);
        }
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-tokenizerOptions")) {
        tokenizerOptions = args[argIndex + 1];
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-tokenizerFactory")) {
        tokenizerFactoryClass = args[argIndex + 1];
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-sentences")) {
        sentenceDelimiter = args[argIndex + 1];
        if (sentenceDelimiter.equalsIgnoreCase("newline")) {
          sentenceDelimiter = "\n";
        }
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-parseInside")) {
        elementDelimiter = args[argIndex + 1];
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-tagSeparator")) {
        tagDelimiter = args[argIndex + 1].charAt(0);
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-loadFromSerializedFile")) {
        // load the parser from a binary serialized file
        // the next argument must be the path to the parser file
        serializedInputFileOrUrl = args[argIndex + 1];
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-loadFromTextFile")) {
        // load the parser from declarative text file
        // the next argument must be the path to the parser file
        textInputFileOrUrl = args[argIndex + 1];
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-saveToSerializedFile")) {
        saveToSerializedFile = true;
        if (numSubArgs(args, argIndex) < 1) {
          System.err.println("Missing path: -saveToSerialized filename");
        } else {
          serializedOutputFileOrUrl = args[argIndex + 1];
        }
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-saveToTextFile")) {
        // save the parser to declarative text file
        saveToTextFile = true;
        textOutputFileOrUrl = args[argIndex + 1];
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-saveTrainTrees")) {
        // save the training trees to a binary file
        Train.trainTreeFile = args[argIndex + 1];
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-trainLength")) {
        // train on only short sentences
        trainLengthLimit = Integer.parseInt(args[argIndex + 1]);
        argIndex += 2;
      } else if (args[argIndex].equalsIgnoreCase("-lengthNormalization")) {
        Test.lengthNormalization = true;
        argIndex++;
      }
      else if (args[argIndex].equalsIgnoreCase("-treebank") ||
          args[argIndex].equalsIgnoreCase("-testTreebank") ||
          args[argIndex].equalsIgnoreCase("-test")) {
        // the next arguments are the treebank path and maybe the range for testing
        int numSubArgs = numSubArgs(args, argIndex);
        if (numSubArgs > 0 && numSubArgs < 3) {
          argIndex++;
          testPath = args[argIndex++];
          if (numSubArgs == 2) {
            testFilter = new NumberRangesFileFilter(args[argIndex++], true);
          } else if (numSubArgs == 3) {
            try {
              int low = Integer.parseInt(args[argIndex]);
              int high = Integer.parseInt(args[argIndex + 1]);
              testFilter = new NumberRangeFileFilter(low, high, true);
              argIndex += 2;
            } catch (NumberFormatException e) {
              // maybe it's a ranges expression?
              testFilter = new NumberRangesFileFilter(args[argIndex++], true);
            }
          }
        } else {
          throw new IllegalArgumentException("Bad arguments after -testTreebank");
        }
      } else if (args[argIndex].equalsIgnoreCase("-tune")) {
        // the next argument is the treebank path and range for tuning
        int numSubArgs = numSubArgs(args, argIndex);
        argIndex++;
        if (numSubArgs == 1) {
          tuneFilter = new NumberRangesFileFilter(args[argIndex++], true);
        } else if (numSubArgs > 1) {
          tunePath = args[argIndex++];
          if (numSubArgs == 2) {
            tuneFilter = new NumberRangesFileFilter(args[argIndex++], true);
          } else if (numSubArgs >= 3) {
            try {
              int low = Integer.parseInt(args[argIndex]);
              int high = Integer.parseInt(args[argIndex + 1]);
              tuneFilter = new NumberRangeFileFilter(low, high, true);
              argIndex += 2;
            } catch (NumberFormatException e) {
              // maybe it's a ranges expression?
              tuneFilter = new NumberRangesFileFilter(args[argIndex++], true);
            }
          }
        }
      } else {
        argIndex = op.setOptionOrWarn(args, argIndex);
      }
    } // end while loop through arguments

    // set up tokenizerFactory with options if provided
    if (tokenizerFactoryClass != null || tokenizerOptions != null) {
      try {
        if (tokenizerFactoryClass != null) {
          Class<TokenizerFactory<? extends HasWord>> clazz = (Class<TokenizerFactory<? extends HasWord>>) Class.forName(tokenizerFactoryClass);
          Method factoryMethod;
          if (tokenizerOptions != null) {
            factoryMethod = clazz.getMethod("newWordTokenizerFactory", String.class);
            tokenizerFactory = (TokenizerFactory<? extends HasWord>) factoryMethod.invoke(null, tokenizerOptions);
          } else {
            factoryMethod = clazz.getMethod("newTokenizerFactory");
            tokenizerFactory = (TokenizerFactory<? extends HasWord>) factoryMethod.invoke(null);
          }
        } else {
          // have options but no tokenizer factory; default to PTB
          tokenizerFactory = PTBTokenizer.PTBTokenizerFactory.newWordTokenizerFactory(tokenizerOptions);
        }
      } catch (Exception e) {
        System.err.println("Couldn't instantiate TokenizerFactory " + tokenizerFactoryClass + " with options " + tokenizerOptions);
        e.printStackTrace();
      }
    }

    // all other arguments are order dependent and
    // are processed in order below

    if (tuneFilter != null) {
      if (tunePath == null) {
        if (treebankPath == null) {
          throw new RuntimeException("No tune treebank path specified...");
        } else {
          System.err.println("No tune treebank path specified.  Using train path: \"" + treebankPath + '\"');
          tunePath = treebankPath;
        }
      }
      tuneTreebank = op.tlpParams.testMemoryTreebank();
      tuneTreebank.loadPath(tunePath, tuneFilter);
    }

    if (!train && Test.verbose) {
      System.err.println("Currently " + new Date());
      printArgs(args, System.err);
    }
    LexicalizedParser lp = null;
    if (train) {
      printArgs(args, System.err);
      // so we train a parser using the treebank
      GrammarCompactor compactor = null;
      if (Train.compactGrammar() == 3) {
        compactor = new ExactGrammarCompactor(false, false);
      }
      Treebank trainTreebank = makeTreebank(treebankPath, op, trainFilter);
      if (secondaryTreebankPath != null) {
        DiskTreebank secondaryTrainTreebank = makeSecondaryTreebank(secondaryTreebankPath, op, secondaryTrainFilter);
        lp = new LexicalizedParser(trainTreebank, secondaryTrainTreebank, secondaryTreebankWeight, compactor, op);
      } else {
        lp = new LexicalizedParser(trainTreebank, compactor, op, tuneTreebank);
      }
    } else if (textInputFileOrUrl != null) {
      // so we load the parser from a text grammar file
      lp = new LexicalizedParser(textInputFileOrUrl, true, op);
    } else {
      // so we load a serialized parser
      if (serializedInputFileOrUrl == null && argIndex < args.length) {
        // the next argument must be the path to the serialized parser
        serializedInputFileOrUrl = args[argIndex];
        argIndex++;
      }
      if (serializedInputFileOrUrl == null) {
        System.err.println("No grammar specified, exiting...");
        return;
      }
      try {
        lp = new LexicalizedParser(serializedInputFileOrUrl, op);
        op = lp.op;
      } catch (IllegalArgumentException e) {
        System.err.println("Error loading parser, exiting...");
        return;
      }
    }

    // the following has to go after reading parser to make sure
    // op and tlpParams are the same for train and test
    // THIS IS BUTT UGLY BUT IT STOPS USER SPECIFIED ENCODING BEING
    // OVERWRITTEN BY ONE SPECIFIED IN SERIALIZED PARSER
    if (encoding != null) {
      op.tlpParams.setInputEncoding(encoding);
      op.tlpParams.setOutputEncoding(encoding);
    }

    if (testFilter != null || testPath != null) {
      if (testPath == null) {
        if (treebankPath == null) {
          throw new RuntimeException("No test treebank path specified...");
        } else {
          System.err.println("No test treebank path specified.  Using train path: \"" + treebankPath + '\"');
          testPath = treebankPath;
        }
      }
      testTreebank = op.tlpParams.testMemoryTreebank();
      testTreebank.loadPath(testPath, testFilter);
    }

    Train.sisterSplitters = new HashSet<String>(Arrays.asList(op.tlpParams.sisterSplitters()));

    // at this point we should be sure that op.tlpParams is
    // set appropriately (from command line, or from grammar file),
    // and will never change again.  -- Roger

    // Now what do we do with the parser we've made
    if (saveToTextFile) {
      // save the parser to textGrammar format
      if (textOutputFileOrUrl != null) {
        saveParserDataToText(lp.pd, textOutputFileOrUrl);
      } else {
        System.err.println("Usage: must specify a text grammar output path");
      }
    }
    if (saveToSerializedFile) {
      if (serializedOutputFileOrUrl != null) {
        saveParserDataToSerialized(lp.pd, serializedOutputFileOrUrl);
      } else if (textOutputFileOrUrl == null && testTreebank == null) {
        // no saving/parsing request has been specified
        System.err.println("usage: " + "java edu.stanford.nlp.parser.lexparser.LexicalizedParser " + "-train trainFilesPath [fileRange] -saveToSerializedFile serializedParserFilename");
      }
    }

    if (Test.verbose || train) {
      // Tell the user a little or a lot about what we have made
      // get lexicon size separately as it may have it's own prints in it....
      String lexNumRules = lp.pparser != null ? Integer.toString(lp.pparser.lex.numRules()): "";
      System.err.println("Grammar\tStates\tTags\tWords\tUnaryR\tBinaryR\tTaggings");
      System.err.println("Grammar\t" +
          Numberer.getGlobalNumberer("states").total() + '\t' +
          Numberer.getGlobalNumberer("tags").total() + '\t' +
          Numberer.getGlobalNumberer("words").total() + '\t' +
          (lp.pparser != null ? lp.pparser.ug.numRules(): "") + '\t' +
          (lp.pparser != null ? lp.pparser.bg.numRules(): "") + '\t' +
          lexNumRules);
      System.err.println("ParserPack is " + op.tlpParams.getClass().getName());
      System.err.println("Lexicon is " + lp.pd.lex.getClass().getName());
      if (Test.verbose) {
        System.err.println("Tags are: " + Numberer.getGlobalNumberer("tags"));
        // System.err.println("States are: " + Numberer.getGlobalNumberer("states")); // This is too verbose. It was already printed out by the below printOptions command if the flag -printStates is given!
      }
      printOptions(false, op);
    }

    if (testTreebank != null) {
      // test parser on treebank
      lp.testOnTreebank(testTreebank);
    } else if (argIndex >= args.length) {
      // no more arguments, so we just parse our own test sentence
      PrintWriter pwOut = op.tlpParams.pw();
      PrintWriter pwErr = op.tlpParams.pw(System.err);
      if (lp.parse(op.tlpParams.defaultTestSentence())) {
        Test.treePrint(op.tlpParams).printTree(lp.getBestParse(), pwOut);
      } else {
        pwErr.println("Error. Can't parse test sentence: " +
            op.tlpParams.defaultTestSentence());
      }
    } else {
      // We parse filenames given by the remaining arguments
      lp.parseFiles(args, argIndex, tokenized, tokenizerFactory, documentPreprocessor, elementDelimiter, sentenceDelimiter, escaper, tagDelimiter);
    }
  } // end main

} // end class LexicalizedParser
