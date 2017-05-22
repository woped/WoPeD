package edu.stanford.nlp.parser.lexparser;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.io.NumberRangesFileFilter;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.stats.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.international.pennchinese.RadicalMap;
import edu.stanford.nlp.util.Numberer;
import edu.stanford.nlp.util.StringUtils;
import edu.stanford.nlp.util.Timing;
import edu.stanford.nlp.process.WordSegmenter;


/**
 * @author Galen Andrew
 */
public class ChineseCharacterBasedLexicon implements Lexicon {

  protected static PrintWriter pw = null;

  private static double lengthPenalty = 5.0;
  // penaltyType should be set as follows:
  // 0: no length penalty
  // 1: quadratic length penalty
  // 2: penalty for continuation chars only
  private static int penaltyType = 0;

  private Map<List,Distribution> charDistributions;
  private Set<Symbol> knownChars;

  private Distribution<String> POSDistribution;

  private static boolean useUnknownCharModel = true;

  private static final int CONTEXT_LENGTH = 2;

  public static void printStats(Collection<Tree> trees) {
    ClassicCounter<Integer> wordLengthCounter = new ClassicCounter<Integer>();
    ClassicCounter<TaggedWord> wordCounter = new ClassicCounter<TaggedWord>();
    ClassicCounter<Symbol> charCounter = new ClassicCounter<Symbol>();
    int counter = 0;
    for (Tree tree : trees) {
      counter++;
      List taggedWords = tree.taggedYield(new ArrayList());
      for (int i = 0, size = taggedWords.size(); i < size; i++) {
        TaggedWord taggedWord = (TaggedWord) taggedWords.get(i);
        String word = taggedWord.word();
        if (word.equals(BOUNDARY)) {
          continue;
        }
        wordCounter.incrementCount(taggedWord);
        wordLengthCounter.incrementCount(Integer.valueOf(word.length()));
        for (int j = 0, length = word.length(); j < length; j++) {
          Symbol sym = Symbol.cannonicalSymbol(word.charAt(j));
          charCounter.incrementCount(sym);
        }
        charCounter.incrementCount(Symbol.END_WORD);
      }
    }

    Set<Symbol> singletonChars = Counters.keysBelow(charCounter, 1.5);
    Set<TaggedWord> singletonWords = Counters.keysBelow(wordCounter, 1.5);

    ClassicCounter<String> singletonWordPOSes = new ClassicCounter<String>();
    for (TaggedWord taggedWord : singletonWords) {
      singletonWordPOSes.incrementCount(taggedWord.tag());
    }
    Distribution<String> singletonWordPOSDist = Distribution.getDistribution(singletonWordPOSes);

    ClassicCounter<Character> singletonCharRads = new ClassicCounter<Character>();
    for (Symbol s : singletonChars) {
      singletonCharRads.incrementCount(Character.valueOf(RadicalMap.getRadical(s.getCh())));
    }
    Distribution<Character> singletonCharRadDist = Distribution.getDistribution(singletonCharRads);

    Distribution<Integer> wordLengthDist = Distribution.getDistribution(wordLengthCounter);

    NumberFormat percent = new DecimalFormat("##.##%");

    pw.println("There are " + singletonChars.size() + " singleton chars out of " + (int) charCounter.totalCount() + " tokens and " + charCounter.size() + " types found in " + counter + " trees.");
    pw.println("Thus singletonChars comprise " + percent.format(singletonChars.size() / charCounter.totalCount()) + " of tokens and " + percent.format((double) singletonChars.size() / charCounter.size()) + " of types.");
    pw.println();
    pw.println("There are " + singletonWords.size() + " singleton words out of " + (int) wordCounter.totalCount() + " tokens and " + wordCounter.size() + " types.");
    pw.println("Thus singletonWords comprise " + percent.format(singletonWords.size() / wordCounter.totalCount()) + " of tokens and " + percent.format((double) singletonWords.size() / wordCounter.size()) + " of types.");
    pw.println();
    pw.println("Distribution over singleton word POS:");
    pw.println(singletonWordPOSDist.toString());
    pw.println();
    pw.println("Distribution over singleton char radicals:");
    pw.println(singletonCharRadDist.toString());
    pw.println();
    pw.println("Distribution over word length:");
    pw.println(wordLengthDist);
  }

  public void train(Collection<Tree> trees) {
    Numberer tagNumberer = Numberer.getGlobalNumberer("tags");

    Timing.tick("Counting characters...");
    ClassicCounter<Symbol> charCounter = new ClassicCounter<Symbol>();

    // first find all chars that occur only once
    for (Tree tree : trees) {
      List labels = tree.yield(new ArrayList());
      for (int i = 0, size = labels.size(); i < size; i++) {
        String word = ((Label) labels.get(i)).value();
        if (word.equals(BOUNDARY)) {
          continue;
        }
        for (int j = 0, length = word.length(); j < length; j++) {
          Symbol sym = Symbol.cannonicalSymbol(word.charAt(j));
          charCounter.incrementCount(sym);
        }
        charCounter.incrementCount(Symbol.END_WORD);
      }
    }

    Set<Symbol> singletons = Counters.keysBelow(charCounter, 1.5);
    knownChars = new HashSet<Symbol>(charCounter.keySet());

    Timing.tick("Counting nGrams...");
    GeneralizedCounter[] POSspecificCharNGrams = new GeneralizedCounter[CONTEXT_LENGTH + 1];
    for (int i = 0; i <= CONTEXT_LENGTH; i++) {
      POSspecificCharNGrams[i] = new GeneralizedCounter(i + 2);
    }

    ClassicCounter<String> POSCounter = new ClassicCounter<String>();
    List<Serializable> context = new ArrayList<Serializable>(CONTEXT_LENGTH + 1);
    for (Tree tree : trees) {
      //      tree.pennPrint();
      List<TaggedWord> words = tree.taggedYield();
      for (TaggedWord taggedWord : words) {
        String word = taggedWord.word();
        String tag = taggedWord.tag();
        tagNumberer.number(tag);
        if (word.equals(BOUNDARY)) {
          continue;
        }
        POSCounter.incrementCount(tag);
        for (int i = 0, size = word.length(); i <= size; i++) {
          Symbol sym;
          Symbol unknownCharClass = null;
          context.clear();
          context.add(tag);
          if (i < size) {
            char thisCh = word.charAt(i);
            sym = Symbol.cannonicalSymbol(thisCh);
            if (singletons.contains(sym)) {
              unknownCharClass = unknownCharClass(sym);
              charCounter.incrementCount(unknownCharClass);
            }
          } else {
            sym = Symbol.END_WORD;
          }
          POSspecificCharNGrams[0].incrementCount(context, sym); // POS-specific 1-gram
          if (unknownCharClass != null) {
            POSspecificCharNGrams[0].incrementCount(context, unknownCharClass); // for unknown ch model
          }

          // context is constructed incrementally:
          // tag prevChar prevPrevChar
          // this could be made faster using .sublist like in score
          for (int j = 1; j <= CONTEXT_LENGTH; j++) { // poly grams
            if (i - j < 0) {
              context.add(Symbol.BEGIN_WORD);
              POSspecificCharNGrams[j].incrementCount(context, sym);
              if (unknownCharClass != null) {
                POSspecificCharNGrams[j].incrementCount(context, unknownCharClass); // for unknown ch model
              }
              break;
            } else {
              Symbol prev = Symbol.cannonicalSymbol(word.charAt(i - j));
              if (singletons.contains(prev)) {
                context.add(unknownCharClass(prev));
              } else {
                context.add(prev);
              }
              POSspecificCharNGrams[j].incrementCount(context, sym);
              if (unknownCharClass != null) {
                POSspecificCharNGrams[j].incrementCount(context, unknownCharClass); // for unknown ch model
              }
            }
          }
        }
      }
    }

    POSDistribution = Distribution.getDistribution(POSCounter);
    Timing.tick("Creating character prior distribution...");

    charDistributions = new HashMap<List, Distribution>();
    //    charDistributions = new HashMap<List, Distribution>();  // 1.5
    //    charCounter.incrementCount(Symbol.UNKNOWN, singletons.size());
    int numberOfKeys = charCounter.size() + singletons.size();
    Distribution<Symbol> prior = Distribution.goodTuringSmoothedCounter(charCounter, numberOfKeys);
    charDistributions.put(Collections.EMPTY_LIST, prior);

    for (int i = 0; i <= CONTEXT_LENGTH; i++) {
      Set counterEntries = POSspecificCharNGrams[i].lowestLevelCounterEntrySet();
      Timing.tick("Creating " + counterEntries.size() + " character " + (i + 1) + "-gram distributions...");
      for (Iterator it = counterEntries.iterator(); it.hasNext();) {
        Map.Entry<List,ClassicCounter> entry = (Map.Entry<List,ClassicCounter>) it.next();
        context = entry.getKey();
        ClassicCounter<Symbol> c = entry.getValue();
        Distribution<Symbol> thisPrior = charDistributions.get(context.subList(0, context.size() - 1));
        double priorWeight = thisPrior.getNumberOfKeys() / 200.0;
        Distribution<Symbol> newDist = Distribution.dynamicCounterWithDirichletPrior(c, thisPrior, priorWeight);
        charDistributions.put(context, newDist);
      }
    }
  }

  public Distribution<String> getPOSDistribution() {
    return POSDistribution;
  }

  public static boolean isForeign(String s) {
    for (int i = 0; i < s.length(); i++) {
      int num = Character.getNumericValue(s.charAt(i));
      if (num < 10 || num > 35) {
        return false;
      }
    }
    return true;
  }

  private Symbol unknownCharClass(Symbol ch) {
    if (useUnknownCharModel) {
      return new Symbol(Character.toString(RadicalMap.getRadical(ch.getCh()))).intern();
    } else {
      return Symbol.UNKNOWN;
    }
  }

  protected static final NumberFormat formatter = new DecimalFormat("0.000");

  public float score(IntTaggedWord iTW, int loc) {
    TaggedWord tw = iTW.toTaggedWord();
    String word = tw.word();
    String tag = tw.tag();
    assert !word.equals(BOUNDARY);
    char[] chars = word.toCharArray();
    List<Serializable> charList = new ArrayList<Serializable>(chars.length + CONTEXT_LENGTH + 1); // this starts of storing Symbol's and then starts storing String's. Clean this up someday!

    // charList is constructed backward
    // END_WORD char[length-1] char[length-2] ... char[0] BEGIN_WORD BEGIN_WORD
    charList.add(Symbol.END_WORD);
    for (int i = chars.length - 1; i >= 0; i--) {
      Symbol ch = Symbol.cannonicalSymbol(chars[i]);
      if (knownChars.contains(ch)) {
        charList.add(ch);
      } else {
        charList.add(unknownCharClass(ch));
      }
    }
    for (int i = 0; i < CONTEXT_LENGTH; i++) {
      charList.add(Symbol.BEGIN_WORD);
    }

    double score = 0.0;
    for (int i = 0, size = charList.size(); i < size - CONTEXT_LENGTH; i++) {
      Symbol nextChar = (Symbol) charList.get(i);
      charList.set(i, tag);
      double charScore = getBackedOffDist(charList.subList(i, i + CONTEXT_LENGTH + 1)).probabilityOf(nextChar);
      score += Math.log(charScore);
    }

    switch (penaltyType) {
      case 0:
        break;

      case 1:
        score -= (chars.length * (chars.length + 1)) * (lengthPenalty / 2);
        break;

      case 2:
        score -= (chars.length - 1) * lengthPenalty;
        break;
    }
    return (float) score;
  }


  // this is where we do backing off for unseen contexts
  // (backing off for rarely seen contexts is done implicitly
  // because the distributions are smoothed)
  private Distribution<Symbol> getBackedOffDist(List<Serializable> context) {
    // context contains [tag prevChar prevPrevChar]
    for (int i = CONTEXT_LENGTH + 1; i >= 0; i--) {
      List<Serializable> l = context.subList(0, i);
      if (charDistributions.containsKey(l)) {
        return charDistributions.get(l);
      }
    }
    throw new RuntimeException("OOPS... no prior distribution...?");
  }

  /**
   * Samples from the distribution over words with this POS according to the lexicon.
   *
   * @param tag the POS of the word to sample
   * @return a sampled word
   */
  public String sampleFrom(String tag) {
    StringBuilder buf = new StringBuilder();
    List<Serializable> context = new ArrayList<Serializable>(CONTEXT_LENGTH + 1);

    // context must contain [tag prevChar prevPrevChar]
    context.add(tag);
    for (int i = 0; i < CONTEXT_LENGTH; i++) {
      context.add(Symbol.BEGIN_WORD);
    }
    Distribution<Symbol> d = getBackedOffDist(context);
    Symbol gen = d.sampleFrom();
    genLoop:
    while (gen != Symbol.END_WORD) {
      buf.append(gen.getCh());
      switch (penaltyType) {
        case 1:
          if (Math.random() > Math.pow(lengthPenalty, buf.length())) {
            break genLoop;
          }
          break;
        case 2:
          if (Math.random() > lengthPenalty) {
            break genLoop;
          }
          break;
      }
      for (int i = 1; i < CONTEXT_LENGTH; i++) {
        context.set(i + 1, context.get(i));
      }
      context.set(1, gen);
      d = getBackedOffDist(context);
      gen = d.sampleFrom();
    }

    return buf.toString();
  }

  /**
   * Samples over words regardless of POS: first samples POS, then samples
   * word according to that POS
   *
   * @return a sampled word
   */
  public String sampleFrom() {
    String POS = POSDistribution.sampleFrom();
    return sampleFrom(POS);
  }

  // don't think this should be used, but just in case...
  public Iterator<IntTaggedWord> ruleIteratorByWord(int word, int loc) {
    throw new UnsupportedOperationException("ChineseCharacterBasedLexicon has no rule iterator!");
  }

  /** Returns the number of rules (tag rewrites as word) in the Lexicon.
   *  This method isn't yet implemented in this class.
   *  It currently just returns 0, which may or may not be helpful.
   */
  public int numRules() {
    return 0;
  }

  public void tune(List trees) {
  }

  private Distribution<Integer> getWordLengthDistribution() {
    int samples = 0;
    ClassicCounter<Integer> c = new ClassicCounter<Integer>();
    while (samples++ < 10000) {
      String s = sampleFrom();
      c.incrementCount(Integer.valueOf(s.length()));
      if (samples % 1000 == 0) {
        System.out.print(".");
      }
    }
    System.out.println();
    Distribution<Integer> genWordLengthDist = Distribution.getDistribution(c);
    return genWordLengthDist;
  }

  public static void main(String[] args) throws IOException {
    Map<String,Integer> flagsToNumArgs = new HashMap<String, Integer>();
    flagsToNumArgs.put("-parser", Integer.valueOf(3));
    flagsToNumArgs.put("-lex", Integer.valueOf(3));
    flagsToNumArgs.put("-test", Integer.valueOf(2));
    flagsToNumArgs.put("-out", Integer.valueOf(1));
    flagsToNumArgs.put("-lengthPenalty", Integer.valueOf(1));
    flagsToNumArgs.put("-penaltyType", Integer.valueOf(1));
    flagsToNumArgs.put("-maxLength", Integer.valueOf(1));
    flagsToNumArgs.put("-stats", Integer.valueOf(2));

    Map<String,String[]> argMap = StringUtils.argsToMap(args, flagsToNumArgs);

    boolean eval = argMap.containsKey("-eval");

    if (argMap.containsKey("-out")) {
      pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream((argMap.get("-out"))[0]), "GB18030"), true);
    }

    System.err.println("ChineseCharacterBasedLexicon called with args:");

    ChineseTreebankParserParams ctpp = new ChineseTreebankParserParams();
    for (int i = 0; i < args.length; i++) {
      ctpp.setOptionFlag(args, i);
      System.err.print(" " + args[i]);
    }
    System.err.println();
    Options op = new Options(ctpp);

    if (argMap.containsKey("-stats")) {
      String[] statArgs = (argMap.get("-stats"));
      MemoryTreebank rawTrainTreebank = op.tlpParams.memoryTreebank();
      FileFilter trainFilt = new NumberRangesFileFilter(statArgs[1], false);
      rawTrainTreebank.loadPath(new File(statArgs[0]), trainFilt);
      System.err.println("Done reading trees.");
      MemoryTreebank trainTreebank;
      if (argMap.containsKey("-annotate")) {
        trainTreebank = new MemoryTreebank();
        TreeAnnotator annotator = new TreeAnnotator(ctpp.headFinder(), ctpp);
        for (Tree tree : rawTrainTreebank) {
          trainTreebank.add(annotator.transformTree(tree));
        }
        System.err.println("Done annotating trees.");
      } else {
        trainTreebank = rawTrainTreebank;
      }
      printStats(trainTreebank);
      System.exit(0);
    }

    int maxLength = 1000000;
    //    Test.verbose = true;
    if (argMap.containsKey("-norm")) {
      Test.lengthNormalization = true;
    }
    if (argMap.containsKey("-maxLength")) {
      maxLength = Integer.parseInt((argMap.get("-maxLength"))[0]);
    }
    Test.maxLength = 120;
    boolean combo = argMap.containsKey("-combo");
    if (combo) {
      ctpp.useCharacterBasedLexicon = true;
      Test.maxSpanForTags = 10;
      op.doDep = false;
      op.dcTags = false;
    }

    if (argMap.containsKey("-rad")) {
      useUnknownCharModel = true;
    }

    LexicalizedParser lp = null;
    Lexicon lex = null;
    if (argMap.containsKey("-parser")) {
      String[] parserArgs = (argMap.get("-parser"));
      if (parserArgs.length > 1) {
        FileFilter trainFilt = new NumberRangesFileFilter(parserArgs[1], false);
        lp = new LexicalizedParser(parserArgs[0], trainFilt, op);
        if (parserArgs.length == 3) {
          String filename = parserArgs[2];
          System.err.println("Writing parser in serialized format to file " + filename + " ");
          System.err.flush();
          ObjectOutputStream out = IOUtils.writeStreamFromString(filename);
          out.writeObject(lp.parserData());
          out.close();
          System.err.println("done.");
        }
      } else {
        String parserFile = parserArgs[0];
        lp = new LexicalizedParser(parserFile, op);
      }
      lex = lp.getLexicon();
      op = lp.getOp();
      ctpp = (ChineseTreebankParserParams) op.tlpParams;
    }

    if (argMap.containsKey("-lex")) {
      String[] lexArgs = (argMap.get("-lex"));
      if (lexArgs.length > 1) {
        lex = ctpp.lex(op.lexOptions);
        MemoryTreebank rawTrainTreebank = op.tlpParams.memoryTreebank();
        FileFilter trainFilt = new NumberRangesFileFilter(lexArgs[1], false);
        rawTrainTreebank.loadPath(new File(lexArgs[0]), trainFilt);
        System.err.println("Done reading trees.");
        MemoryTreebank trainTreebank;
        if (argMap.containsKey("-annotate")) {
          trainTreebank = new MemoryTreebank();
          TreeAnnotator annotator = new TreeAnnotator(ctpp.headFinder(), ctpp);
          for (Iterator iter = rawTrainTreebank.iterator(); iter.hasNext();) {
            Tree tree = (Tree) iter.next();
            tree = annotator.transformTree(tree);
            trainTreebank.add(tree);
          }
          System.err.println("Done annotating trees.");
        } else {
          trainTreebank = rawTrainTreebank;
        }
        lex.train(trainTreebank);
        System.err.println("Done training lexicon.");
        if (lexArgs.length == 3) {
          String filename = lexArgs.length == 3 ? lexArgs[2] : "parsers/chineseCharLex.ser.gz";
          System.err.println("Writing lexicon in serialized format to file " + filename + " ");
          System.err.flush();
          ObjectOutputStream out = IOUtils.writeStreamFromString(filename);
          out.writeObject(lex);
          out.close();
          System.err.println("done.");
        }
      } else {
        String lexFile = lexArgs.length == 1 ? lexArgs[0] : "parsers/chineseCharLex.ser.gz";
        System.err.println("Reading Lexicon from file " + lexFile);
        ObjectInputStream in = IOUtils.readStreamFromString(lexFile);
        try {
          lex = (Lexicon) in.readObject();
        } catch (ClassNotFoundException e) {
          throw new RuntimeException("Bad serialized file: " + lexFile);
        }
        in.close();
      }
    }

    if (argMap.containsKey("-lengthPenalty")) {
      lengthPenalty = Double.parseDouble((argMap.get("-lengthPenalty"))[0]);
    }

    if (argMap.containsKey("-penaltyType")) {
      penaltyType = Integer.parseInt((argMap.get("-penaltyType"))[0]);
    }

    if (argMap.containsKey("-test")) {
      boolean segmentWords = (ctpp.segmentMarkov || ctpp.segmentMaxMatch);
      boolean parse = lp != null;
      assert (parse || segmentWords);
      //      WordCatConstituent.collinizeWords = argMap.containsKey("-collinizeWords");
      //      WordCatConstituent.collinizeTags = argMap.containsKey("-collinizeTags");
      WordSegmenter seg = null;
      if (segmentWords) {
        seg = (WordSegmenter) lex;
      }
      String[] testArgs = (argMap.get("-test"));
      MemoryTreebank testTreebank = op.tlpParams.memoryTreebank();
      FileFilter testFilt = new NumberRangesFileFilter(testArgs[1], false);
      testTreebank.loadPath(new File(testArgs[0]), testFilt);
      TreeTransformer subcategoryStripper = op.tlpParams.subcategoryStripper();
      TreeTransformer collinizer = ctpp.collinizer();

      WordCatEquivalenceClasser eqclass = new WordCatEquivalenceClasser();
      WordCatEqualityChecker eqcheck = new WordCatEqualityChecker();
      EquivalenceClassEval basicEval = new EquivalenceClassEval(eqclass, eqcheck, "basic");
      EquivalenceClassEval collinsEval = new EquivalenceClassEval(eqclass, eqcheck, "collinized");
      List<String> evalTypes = new ArrayList<String>(3);
      boolean goodPOS = false;
      if (segmentWords) {
        evalTypes.add(WordCatConstituent.wordType);
        if (ctpp.segmentMarkov && !parse) {
          evalTypes.add(WordCatConstituent.tagType);
          goodPOS = true;
        }
      }
      if (parse) {
        evalTypes.add(WordCatConstituent.tagType);
        evalTypes.add(WordCatConstituent.catType);
        if (combo) {
          evalTypes.add(WordCatConstituent.wordType);
          goodPOS = true;
        }
      }
      TreeToBracketProcessor proc = new TreeToBracketProcessor(evalTypes);

      System.err.println("Testing...");
      for (Tree goldTop : testTreebank) {
        Tree gold = goldTop.firstChild();
        Sentence goldSentence = gold.yield();
        if (goldSentence.length() > maxLength) {
          System.err.println("Skipping sentence; too long: " + goldSentence.length());
          continue;
        } else {
          System.err.println("Processing sentence; length: " + goldSentence.length());
        }
        Sentence s;
        if (segmentWords) {
          StringBuilder goldCharBuf = new StringBuilder();
          for (Iterator wordIter = goldSentence.iterator(); wordIter.hasNext();) {
            StringLabel word = (StringLabel) wordIter.next();
            goldCharBuf.append(word.value());
          }
          String goldChars = goldCharBuf.toString();
          s = seg.segmentWords(goldChars);
        } else {
          s = goldSentence;
        }
        Tree tree;
        if (parse) {
          lp.parse(s);
          tree = lp.getBestParse();
          if (tree == null) {
            throw new RuntimeException("PARSER RETURNED NULL!!!");
          }
        } else {
          tree = Trees.toFlatTree(s);
          tree = subcategoryStripper.transformTree(tree);
        }

        if (pw != null) {
          if (parse) {
            tree.pennPrint(pw);
          } else {
            Iterator sentIter = s.iterator();
            for (; ;) {
              Word word = (Word) sentIter.next();
              pw.print(word.word());
              if (sentIter.hasNext()) {
                pw.print(" ");
              } else {
                break;
              }
            }
          }
          pw.println();
        }

        if (eval) {
          Collection ourBrackets, goldBrackets;
          ourBrackets = proc.allBrackets(tree);
          goldBrackets = proc.allBrackets(gold);
          if (goodPOS) {
            ourBrackets.addAll(proc.commonWordTagTypeBrackets(tree, gold));
            goldBrackets.addAll(proc.commonWordTagTypeBrackets(gold, tree));
          }

          basicEval.eval(ourBrackets, goldBrackets);
          System.out.println("\nScores:");
          basicEval.displayLast();

          Tree collinsTree = collinizer.transformTree(tree);
          Tree collinsGold = collinizer.transformTree(gold);
          ourBrackets = proc.allBrackets(collinsTree);
          goldBrackets = proc.allBrackets(collinsGold);
          if (goodPOS) {
            ourBrackets.addAll(proc.commonWordTagTypeBrackets(collinsTree, collinsGold));
            goldBrackets.addAll(proc.commonWordTagTypeBrackets(collinsGold, collinsTree));
          }

          collinsEval.eval(ourBrackets, goldBrackets);
          System.out.println("\nCollinized scores:");
          collinsEval.displayLast();

          System.out.println();
        }
      }
      if (eval) {
        basicEval.display();
        System.out.println();
        collinsEval.display();
      }
    }
  }

  public void readData(BufferedReader in) throws IOException {
    throw new UnsupportedOperationException();
  }

  public void writeData(Writer w) throws IOException {
    throw new UnsupportedOperationException();
  }

  public boolean isKnown(int word) {
    throw new UnsupportedOperationException();
  }

  public boolean isKnown(String word) {
    throw new UnsupportedOperationException();
  }

  private static class Symbol implements Serializable {
    private static final int UNKNOWN_TYPE = 0;
    private static final int DIGIT_TYPE = 1;
    private static final int LETTER_TYPE = 2;
    private static final int BEGIN_WORD_TYPE = 3;
    private static final int END_WORD_TYPE = 4;
    private static final int CHAR_TYPE = 5;
    private static final int UNK_CLASS_TYPE = 6;

    private char ch;
    private String unkClass;

    int type;

    public static final Symbol UNKNOWN = new Symbol(UNKNOWN_TYPE);
    public static final Symbol DIGIT = new Symbol(DIGIT_TYPE);
    public static final Symbol LETTER = new Symbol(LETTER_TYPE);
    public static final Symbol BEGIN_WORD = new Symbol(BEGIN_WORD_TYPE);
    public static final Symbol END_WORD = new Symbol(END_WORD_TYPE);

    public static Interner<Symbol> interner = new Interner<Symbol>();

    public Symbol(char ch) {
      type = CHAR_TYPE;
      this.ch = ch;
    }

    public Symbol(String unkClass) {
      type = UNK_CLASS_TYPE;
      this.unkClass = unkClass;
    }

    public Symbol(int type) {
      assert type != CHAR_TYPE;
      this.type = type;
    }

    public static Symbol cannonicalSymbol(char ch) {
      if (Character.isDigit(ch)) {
        return DIGIT; //{ Digits.add(new Character(ch)); return DIGIT; }
      }

      if (Character.getNumericValue(ch) >= 10 && Character.getNumericValue(ch) <= 35) {
        return LETTER; //{ Letters.add(new Character(ch)); return LETTER; }
      }

      return new Symbol(ch);
    }

    public char getCh() {
      if (type == CHAR_TYPE) {
        return ch;
      } else {
        return '*';
      }
    }

    public Symbol intern() {
      return interner.intern(this);
    }

    @Override
    public String toString() {
      if (type == CHAR_TYPE) {
        return "[u" + (int) ch + "]";
      } else if (type == UNK_CLASS_TYPE) {
        return "UNK:" + unkClass;
      } else {
        return Integer.toString(type);
      }
    }

    private Object readResolve() throws ObjectStreamException {
      switch (type) {
        case CHAR_TYPE:
          return intern();
        case UNK_CLASS_TYPE:
          return intern();
        case UNKNOWN_TYPE:
          return UNKNOWN;
        case DIGIT_TYPE:
          return DIGIT;
        case LETTER_TYPE:
          return LETTER;
        case BEGIN_WORD_TYPE:
          return BEGIN_WORD;
        case END_WORD_TYPE:
          return END_WORD;
        default: // impossible...
          throw new InvalidObjectException("ILLEGAL VALUE IN SERIALIZED SYMBOL");
      }
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Symbol)) {
        return false;
      }

      final Symbol symbol = (Symbol) o;

      if (ch != symbol.ch) {
        return false;
      }
      if (type != symbol.type) {
        return false;
      }
      if (unkClass != null ? !unkClass.equals(symbol.unkClass) : symbol.unkClass != null) {
        return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      int result;
      result = ch;
      result = 29 * result + (unkClass != null ? unkClass.hashCode() : 0);
      result = 29 * result + type;
      return result;
    }

    private static final long serialVersionUID = 8925032621317022510L;

  } // end class Symbol

  private static final long serialVersionUID = -5357655683145854069L;

  public UnknownWordModel getUnknownWordModel() {
    // TODO Auto-generated method stub
    return null;
  }

  public void setUnknownWordModel(UnknownWordModel uwm) {
    // TODO Auto-generated method stub

  }

} // end class ChineseCharacterBasedLexicon
