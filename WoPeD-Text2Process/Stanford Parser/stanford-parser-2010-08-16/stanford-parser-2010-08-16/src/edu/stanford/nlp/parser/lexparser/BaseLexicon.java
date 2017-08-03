
package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.io.NumberRangesFileFilter;
import edu.stanford.nlp.io.EncodingPrintWriter;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.Treebank;
import edu.stanford.nlp.trees.DiskTreebank;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.util.ErasureUtils;
import edu.stanford.nlp.util.Numberer;
import edu.stanford.nlp.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * This is the default concrete instantiation of the Lexicon interface. It was
 * originally built for Penn Treebank English.
 *
 * @author Dan Klein
 * @author Galen Andrew
 * @author Christopher Manning
 */
public class BaseLexicon implements Lexicon {

  protected UnknownWordModel uwModel;
  protected static final boolean DEBUG_LEXICON = false;
  protected static final boolean DEBUG_LEXICON_SCORE = false;

  protected static final int nullWord = -1;

  protected static final short nullTag = -1;


  /**
   * If a word has been seen more than this many times, then relative
   * frequencies of tags are used for POS assignment; if not, they are smoothed
   * with tag priors.
   */
  protected int smoothInUnknownsThreshold;

  /**
   * Have tags changeable based on statistics on word types having various
   * taggings.
   */
  protected boolean smartMutation;


  /** An array of Lists of rules (IntTaggedWord), indexed by word. */
  public transient List<IntTaggedWord>[] rulesWithWord;

  // protected transient Set<IntTaggedWord> rules = new
  // HashSet<IntTaggedWord>();
  // When it existed, rules somehow held a few less things than rulesWithWord
  // I never figured out why [cdm, Dec 2004]

  /** Set of all tags as IntTaggedWord. Alive in both train and runtime
   *  phases, but transient.
   */
  protected transient Set<IntTaggedWord> tags = new HashSet<IntTaggedWord>();

  protected transient Set<IntTaggedWord> words = new HashSet<IntTaggedWord>();

  // protected transient Set<IntTaggedWord> sigs=new HashSet<IntTaggedWord>();

  /** Records the number of times word/tag pair was seen in training data.
   *  Includes word/tag pairs where one is a wildcard not a real word/tag.
   */
  public ClassicCounter<IntTaggedWord> seenCounter = new ClassicCounter<IntTaggedWord>();

  /**
   * We cache the last signature looked up, because it asks for the same one
   * many times when an unknown word is encountered! (Note that under the
   * current scheme, one unknown word, if seen sentence-initially and
   * non-initially, will be parsed with two different signatures....)
   */
  protected transient int lastSignatureIndex = -1;

  protected transient int lastSentencePosition = -1;

  protected transient int lastWordToSignaturize = -1;

  double[] smooth = { 1.0, 1.0 };

  // these next two are used for smartMutation calculation
  transient double[][] m_TT; // = null;

  transient double[] m_T; // = null;

  private boolean flexiTag;

  private boolean useSignatureForKnownSmoothing;


  public BaseLexicon() {
    this(new Options.LexOptions());
  }

  public BaseLexicon(Options.LexOptions op) {
    flexiTag = op.flexiTag;
    useSignatureForKnownSmoothing = op.useSignatureForKnownSmoothing;
    this.smoothInUnknownsThreshold = op.smoothInUnknownsThreshold;
    this.smartMutation = op.smartMutation;
    // Construct UnknownWordModel by reflection -- a right pain
    // Lexicons and UnknownWordModels aren't very well encapsulated from each other!
    if (op.uwModel == null) {
      op.uwModel = "edu.stanford.nlp.parser.lexparser.BaseUnknownWordModel";
    }

    try {
      Class<?> clas = Class.forName(op.uwModel);
      Class<?>[] argClasses = new Class<?>[2];
      argClasses[0] = Options.LexOptions.class;
      argClasses[1] = Lexicon.class;
      Object[] args = new Object[2];
      args[0] = op;
      args[1] = this;
      Constructor<?> constr = clas.getConstructor(argClasses);
      uwModel = (UnknownWordModel) constr.newInstance(args);
    } catch (ClassNotFoundException e) {
      System.err.println("Class not found: " + op.uwModel);
      e.printStackTrace();
    } catch (NoSuchMethodException nsme) {
      System.err.println("Can't construct: " + op.uwModel);
      nsme.printStackTrace();
    } catch (InvocationTargetException ite) {
      System.err.println("Can't construct: " + op.uwModel);
      ite.printStackTrace();
    } catch (InstantiationException e) {
      System.err.println("Couldn't instantiate: " + op.uwModel);
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      System.err.println("Illegal access to " + op.uwModel);
      e.printStackTrace();
    } finally {
      if (uwModel == null) {
        uwModel = new BaseUnknownWordModel(op, this);
      }
    }
  }

  /**
   * Checks whether a word is in the lexicon. This version will compile the
   * lexicon into the rulesWithWord array, if that hasn't already happened
   *
   * @param word The word as an int index to a Numberer
   * @return Whether the word is in the lexicon
   */
  public boolean isKnown(int word) {
    if (rulesWithWord == null) {
      initRulesWithWord();
    }
    return (word < rulesWithWord.length && ! rulesWithWord[word].isEmpty());
  }

  /**
   * Checks whether a word is in the lexicon. This version works even while
   * compiling lexicon with current counters (rather than using the compiled
   * rulesWithWord array).
   *
   * @param word The word as a String
   * @return Whether the word is in the lexicon
   */
  public boolean isKnown(String word) {
    IntTaggedWord iW = new IntTaggedWord(wordNumberer().number(word), nullTag);
    return seenCounter.getCount(iW) > 0.0;
  }

  /**
   * Returns the possible POS taggings for a word.
   *
   * @param word The word, represented as an integer in Numberer
   * @param loc  The position of the word in the sentence (counting from 0).
   *          <i>Implementation note: The BaseLexicon class doesn't actually
   *          make use of this position information.</i>
   * @return An Iterator over a List ofIntTaggedWords, which pair the word with
   *         possible taggings as integer pairs. (Each can be thought of as a
   *         <code>tag -&gt; word<code> rule.)
   */
  public Iterator<IntTaggedWord> ruleIteratorByWord(String word, int loc) {
    return ruleIteratorByWord(wordNumberer().number(word), loc);
  }

  /** Generate the possible taggings for a word at a sentence position.
   *  This may either be based on a strict lexicon or an expanded generous
   *  set of possible taggings. <p>
   *  <i>Implementation note:</i> Expanded sets of possible taggings are
   *  calculated dynamically at runtime, so as to reduce the memory used by
   *  the lexicon (a space/time tradeoff).
   *
   *  @param word The word (as an int)
   *  @param loc  Its index in the sentence (usually only relevant for unknown words)
   *  @return A list of possible taggings
   */
  public Iterator<IntTaggedWord> ruleIteratorByWord(int word, int loc) {
    // if (rulesWithWord == null) { // tested in isKnown already
    // initRulesWithWord();
    // }
    List<IntTaggedWord> wordTaggings;
    if (isKnown(word)) {
      if ( ! flexiTag) {
        // Strict lexical tagging for seen items
        wordTaggings = rulesWithWord[word];
      } else {
        /* Allow all tags with same basicCategory */
        /* Allow all scored taggings, unless very common */
        IntTaggedWord iW = new IntTaggedWord(word, nullTag);
        if (seenCounter.getCount(iW) > smoothInUnknownsThreshold) {
          return rulesWithWord[word].iterator();
        } else {
          // give it flexible tagging not just lexicon
          wordTaggings = new ArrayList<IntTaggedWord>(40);
          for (IntTaggedWord iTW2 : tags) {
            IntTaggedWord iTW = new IntTaggedWord(word, iTW2.tag);
            if (score(iTW, loc) > Float.NEGATIVE_INFINITY) {
              wordTaggings.add(iTW);
            }
          }
        }
      }
    } else {
      // we copy list so we can insert correct word in each item
      wordTaggings = new ArrayList<IntTaggedWord>(40);
      for (IntTaggedWord iTW : rulesWithWord[wordNumberer.number(UNKNOWN_WORD)]) {
        wordTaggings.add(new IntTaggedWord(word, iTW.tag));
      }
    }
    if (DEBUG_LEXICON) {
      EncodingPrintWriter.err.println("Lexicon: " + wordNumberer().object(word) + " (" +
              (isKnown(word) ? "known": "unknown") + ", loc=" + loc + ", n=" +
              (isKnown(word) ? word: wordNumberer.number(UNKNOWN_WORD)) + ") " +
              (flexiTag ? "flexi": "lexicon") + " taggings: " + wordTaggings, "UTF-8");
    }
    return wordTaggings.iterator();
 }

  protected void initRulesWithWord() {
    if (Test.verbose || DEBUG_LEXICON) {
      System.err.print("\nInitializing lexicon scores ... ");
    }
    // int numWords = words.size()+sigs.size()+1;
    int unkWord = wordNumberer().number(UNKNOWN_WORD);
    int numWords = wordNumberer().total();
    rulesWithWord = new List[numWords];
    for (int w = 0; w < numWords; w++) {
      rulesWithWord[w] = new ArrayList<IntTaggedWord>(1); // most have 1 or 2
                                                          // items in them
    }
    // for (Iterator ruleI = rules.iterator(); ruleI.hasNext();) {
    tags = new HashSet<IntTaggedWord>();
    for (IntTaggedWord iTW : seenCounter.keySet()) {
      if (iTW.word() == nullWord && iTW.tag() != nullTag) {
        tags.add(iTW);
      }
    }

    // tags for unknown words
    if (DEBUG_LEXICON) {
      System.err.println("Lexicon initializing tags for UNKNOWN WORD (" +
                         Lexicon.UNKNOWN_WORD + ", " + unkWord + ')');
    }
    if (DEBUG_LEXICON) System.err.println("unSeenCounter is: " + uwModel.unSeenCounter());
    if (DEBUG_LEXICON) System.err.println("Train.openClassTypesThreshold is " + Train.openClassTypesThreshold);
    for (IntTaggedWord iT : tags) {
      if (DEBUG_LEXICON) System.err.println("Entry for " + iT + " is " + uwModel.unSeenCounter().getCount(iT));
      double types = uwModel.unSeenCounter().getCount(iT);
      if (types > Train.openClassTypesThreshold) {
        // Number of types before it's treated as open class
        IntTaggedWord iTW = new IntTaggedWord(unkWord, iT.tag);
        rulesWithWord[iTW.word].add(iTW);
      }
    }
    if (Test.verbose || DEBUG_LEXICON) {
      System.err.print("The " + rulesWithWord[unkWord].size() + " open class tags are: [");
      for (IntTaggedWord item : rulesWithWord[unkWord]) {
        System.err.print(" " + tagNumberer().object(item.tag()));
        if (DEBUG_LEXICON) {
          IntTaggedWord iTprint = new IntTaggedWord(nullWord, item.tag);
          System.err.print(" (tag " + item.tag() + ", type count is " +
                           uwModel.unSeenCounter().getCount(iTprint) + ')');
        }
      }
      System.err.println(" ] ");
    }

    for (IntTaggedWord iTW : seenCounter.keySet()) {
      if (iTW.tag() != nullTag && iTW.word() != nullWord) {
        rulesWithWord[iTW.word].add(iTW);
      }
    }
  }


  protected List<IntTaggedWord> treeToEvents(Tree tree, boolean keepTagsAsLabels) {
    if (!keepTagsAsLabels) { return treeToEvents(tree); }
    List<LabeledWord> labeledWords = tree.labeledYield();
//     for (LabeledWord tw : labeledWords) {
//       System.err.println(tw);
//     }
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

  /** Not yet implemented. */
  public void addAll(List<TaggedWord> tagWords) {
    addAll(tagWords, 1.0);
  }

  /** Not yet implemented. */
  public void addAll(List<TaggedWord> taggedWords, double weight) {
    List<IntTaggedWord> tagWords = listToEvents(taggedWords);
  }

  /** Not yet implemented. */
  public void trainWithExpansion(Collection<TaggedWord> taggedWords) {
  }

  /**
   * Trains this lexicon on the Collection of trees.
   */
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

  /**
   * Trains this lexicon on the Collection of trees.
   * Also trains the unknown word model pointed to by this lexicon.
   */
  public void train(Collection<Tree> trees, double weight, boolean keepTagsAsLabels) {
    getUnknownWordModel().train(trees);

    // scan data
    for (Tree tree : trees) {
      List<IntTaggedWord> taggedWords = treeToEvents(tree, keepTagsAsLabels);
      for (int w = 0, sz = taggedWords.size(); w < sz; w++) {
        IntTaggedWord iTW = taggedWords.get(w);
        seenCounter.incrementCount(iTW, weight);
        IntTaggedWord iT = new IntTaggedWord(nullWord, iTW.tag);
        seenCounter.incrementCount(iT, weight);
        IntTaggedWord iW = new IntTaggedWord(iTW.word, nullTag);
        seenCounter.incrementCount(iW, weight);
        IntTaggedWord i = new IntTaggedWord(nullWord, nullTag);
        seenCounter.incrementCount(i, weight);
        // rules.add(iTW);
        tags.add(iT);
        words.add(iW);
      }
    }
    // index the possible tags for each word
    // numWords = wordNumberer.total();
    // unknownWordIndex = Numberer.number("words",Lexicon.UNKNOWN_WORD);
    // initRulesWithWord();

    tune(trees);
    if (DEBUG_LEXICON) {
      printLexStats();
    }
  }

  /**
   * Adds the tagging with count to the data structures in this Lexicon.
   */
  protected void addTagging(boolean seen, IntTaggedWord itw, double count) {
    if (seen) {
      seenCounter.incrementCount(itw, count);
      if (itw.tag() == nullTag) {
        words.add(itw);
      } else if (itw.word() == nullWord) {
        tags.add(itw);
      } else {
        // rules.add(itw);
      }
    } else {
      uwModel.addTagging(seen, itw, count);
      // if (itw.tag() == nullTag) {
      // sigs.add(itw);
      // }
    }
  }



  /**
   * This records how likely it is for a word with one tag to also have another
   * tag. This won't work after serialization/deserialization, but that is how
   * it is currently called....
   */
  void buildPT_T() {
    int numTags = tagNumberer().total();
    m_TT = new double[numTags][numTags];
    m_T = new double[numTags];
    double[] tmp = new double[numTags];
    for (IntTaggedWord word : words) {
      IntTaggedWord iTW = new IntTaggedWord(word.word, nullTag);
      double tot = 0.0;
      for (int t = 0; t < numTags; t++) {
        iTW.tag = (short) t;
        tmp[t] = seenCounter.getCount(iTW);
        tot += tmp[t];
      }
      if (tot < 10) {
        continue;
      }
      for (int t = 0; t < numTags; t++) {
        for (int t2 = 0; t2 < numTags; t2++) {
          if (tmp[t2] > 0.0) {
            double c = tmp[t] / tot;
            m_T[t] += c;
            m_TT[t2][t] += c;
          }
        }
      }
    }
  }


  /**
   * Get the score of this word with this tag (as an IntTaggedWord) at this
   * location. (Presumably an estimate of P(word | tag).)
   * <p>
   * <i>Implementation documentation:</i>
   * Seen:
   * c_W = count(W)      c_TW = count(T,W)
   * c_T = count(T)      c_Tunseen = count(T) among new words in 2nd half
   * total = count(seen words)   totalUnseen = count("unseen" words)
   * p_T_U = Pmle(T|"unseen")
   * pb_T_W = P(T|W). If (c_W &gt; smoothInUnknownsThreshold) = c_TW/c_W
   * Else (if not smart mutation) pb_T_W = bayes prior smooth[1] with p_T_U
   * p_T= Pmle(T)          p_W = Pmle(W)
   * pb_W_T = log(pb_T_W * p_W / p_T) [Bayes rule]
   * Note that this doesn't really properly reserve mass to unknowns.
   *
   * Unseen:
   * c_TS = count(T,Sig|Unseen)      c_S = count(Sig)   c_T = count(T|Unseen)
   * c_U = totalUnseen above
   * p_T_U = Pmle(T|Unseen)
   * pb_T_S = Bayes smooth of Pmle(T|S) with P(T|Unseen) [smooth[0]]
   * pb_W_T = log(P(W|T)) inverted
   *
   * @param iTW An IntTaggedWord pairing a word and POS tag
   * @param loc The position in the sentence. <i>In the default implementation
   *          this is used only for unknown words to change their probability
   *          distribution when sentence initial</i>
   * @return A float score, usually, log P(word|tag)
   */
  public float score(IntTaggedWord iTW, int loc) {
    int word = iTW.word;
    short tag = iTW.tag;

    // both actual
    double c_TW = seenCounter.getCount(iTW);
    // double x_TW = xferCounter.getCount(iTW);
    iTW.tag = nullTag;
    // word counts
    double c_W = seenCounter.getCount(iTW);
    // double x_W = xferCounter.getCount(iTW);
    iTW.word = nullWord;
    // totals
    double total = seenCounter.getCount(iTW);
    double totalUnseen = uwModel.unSeenCounter().getCount(iTW);
    iTW.tag = tag;
    // tag counts
    double c_T = seenCounter.getCount(iTW);
    double c_Tunseen = uwModel.unSeenCounter().getCount(iTW);
    iTW.word = word;

    double pb_W_T; // always set below

    if (DEBUG_LEXICON) {
      // dump info about last word
      if (iTW.word != debugLastWord) {
        if (debugLastWord >= 0 && debugPrefix != null) {
          // the 2nd conjunct in test above handles older serialized files
          EncodingPrintWriter.err.println(debugPrefix + debugProbs + debugNoProbs, "UTF-8");
        }
      }
    }

    boolean seen = (c_W > 0.0);

    if (seen) {

      // known word model for P(T|W)
      if (DEBUG_LEXICON_SCORE) {
        System.err.println("Lexicon.score " + wordNumberer().object(word) + "/" + tagNumberer.object(tag) + " as known word.");
      }

      // c_TW = Math.sqrt(c_TW); [cdm: funny math scaling? dunno who played with this]
      // c_TW += 0.5;

      double p_T_U;
      if (useSignatureForKnownSmoothing) { // only works for English currently
        p_T_U = getUnknownWordModel().scoreProbTagGivenWordSignature(iTW, loc, smooth[0]);
        if (DEBUG_LEXICON_SCORE) System.err.println("With useSignatureForKnownSmoothing, P(T|U) is " + p_T_U + " rather than " + (c_Tunseen / totalUnseen));
      } else {
        p_T_U = c_Tunseen / totalUnseen;
      }
      double pb_T_W; // always set below

      if (DEBUG_LEXICON_SCORE) {
        System.err.println("c_W is " + c_W  + " mle = " + (c_TW/c_W)+ " smoothInUnknownsThresh is " +
                smoothInUnknownsThreshold + " base p_T_U is " + c_Tunseen + "/" + totalUnseen + " = " + p_T_U);
      }
      if (c_W > smoothInUnknownsThreshold) {
        // we've seen the word enough times to have confidence in its tagging
        pb_T_W = c_TW / c_W;
      } else {
        // we haven't seen the word enough times to have confidence in its
        // tagging
        if (smartMutation) {
          int numTags = tagNumberer().total();
          if (m_TT == null || numTags != m_T.length) {
            buildPT_T();
          }
          p_T_U *= 0.1;
          // System.out.println("Checking "+iTW);
          for (int t = 0; t < numTags; t++) {
            IntTaggedWord iTW2 = new IntTaggedWord(word, t);
            double p_T_W2 = seenCounter.getCount(iTW2) / c_W;
            if (p_T_W2 > 0) {
              // System.out.println(" Observation of "+tagNumberer.object(t)+"
              // ("+seenCounter.getCount(iTW2)+") mutated to
              // "+tagNumberer.object(iTW.tag)+" at rate
              // "+(m_TT[tag][t]/m_T[t]));
              p_T_U += p_T_W2 * m_TT[tag][t] / m_T[t] * 0.9;
            }
          }
        }
        if (DEBUG_LEXICON_SCORE) {
          System.err.println("c_TW = " + c_TW + " c_W = " + c_W +
                  " p_T_U = " + p_T_U);
        }
        // double pb_T_W = (c_TW+smooth[1]*x_TW)/(c_W+smooth[1]*x_W);
        pb_T_W = (c_TW + smooth[1] * p_T_U) / (c_W + smooth[1]);
      }
      double p_T = (c_T / total);
      double p_W = (c_W / total);
      pb_W_T = Math.log(pb_T_W * p_W / p_T);

      if (DEBUG_LEXICON) {
        if (iTW.word != debugLastWord) {
          debugLastWord = iTW.word;
          debugLoc = loc;
          debugProbs = new StringBuilder();
          debugNoProbs = new StringBuilder("impossible: ");
          debugPrefix = "Lexicon: " + wordNumberer().object(debugLastWord) + " (known): ";
        }
        if (pb_W_T > Double.NEGATIVE_INFINITY) {
          NumberFormat nf = NumberFormat.getNumberInstance();
          nf.setMaximumFractionDigits(3);
          debugProbs.append(tagNumberer().object(tag) + ": cTW=" + c_TW + " c_T=" + c_T
                  + " pb_T_W=" + nf.format(pb_T_W) + " log pb_W_T=" + nf.format(pb_W_T)
                  + ", ");
          // debugProbs.append("\n" + "smartMutation=" + smartMutation + "
          // smoothInUnknownsThreshold=" + smoothInUnknownsThreshold + "
          // smooth0=" + smooth[0] + "smooth1=" + smooth[1] + " p_T_U=" + p_T_U
          // + " c_W=" + c_W);
        } else {
          debugNoProbs.append(tagNumberer().object(tag)).append(' ');
        }
      } // end if (DEBUG_LEXICON)

    } else { // when unseen
      if (loc >= 0) {
        pb_W_T = getUnknownWordModel().score(iTW, loc, c_T, total, smooth[0]);
      } else {
        // For negative we now do a weighted average for the dependency grammar :-)
        double pb_W0_T = getUnknownWordModel().score(iTW, 0, c_T, total, smooth[0]);
        double pb_W1_T = getUnknownWordModel().score(iTW, 1, c_T, total, smooth[0]);
        pb_W_T = Math.log((Math.exp(pb_W0_T) + 2 * Math.exp(pb_W1_T))/3);
      }
    }

    // Categorical cutoff if score is too low
    if (pb_W_T > -100.0) {
      return (float) pb_W_T;
    }
    return Float.NEGATIVE_INFINITY;
  } // end score()


  private transient int debugLastWord = -1;

  private transient int debugLoc = -1;

  private transient StringBuilder debugProbs;

  private transient StringBuilder debugNoProbs;

  private transient String debugPrefix;

  public void tune(Collection<Tree> trees) {
    double bestScore = Double.NEGATIVE_INFINITY;
    double[] bestSmooth = { 0.0, 0.0 };
    for (smooth[0] = 1; smooth[0] <= 1; smooth[0] *= 2.0) {// 64
      for (smooth[1] = 0.2; smooth[1] <= 0.2; smooth[1] *= 2.0) {// 3
        // for (smooth[0]=0.5; smooth[0]<=64; smooth[0] *= 2.0) {//64
        // for (smooth[1]=0.1; smooth[1]<=12.8; smooth[1] *= 2.0) {//3
        double score = 0.0;
        // score = scoreAll(trees);
        if (Test.verbose) {
          System.out.println("Tuning lexicon: s0 " + smooth[0] + " s1 " + smooth[1] + " is "
                             + score + ' ' + trees.size() + " trees.");
        }
        if (score > bestScore) {
          System.arraycopy(smooth, 0, bestSmooth, 0, smooth.length);
          bestScore = score;
        }
      }
    }
    System.arraycopy(bestSmooth, 0, smooth, 0, bestSmooth.length);
    if (smartMutation) {
      smooth[0] = 8.0;
      // smooth[1] = 1.6;
      // smooth[0] = 0.5;
      smooth[1] = 0.1;
    }
    if (Test.unseenSmooth > 0.0) {
      smooth[0] = Test.unseenSmooth;
    }
    if (Test.verbose) {
      System.out.println("Tuning selected smoothUnseen " + smooth[0] + " smoothSeen " + smooth[1]
                         + " at " + bestScore);
    }
  }

  /**
   * Populates data in this Lexicon from the character stream given by the
   * Reader r.
   */
  public void readData(BufferedReader in) throws IOException {
    final String SEEN = "SEEN";
    String line;
    int lineNum = 1;
    // all lines have one tagging with raw count per line
    line = in.readLine();
    Pattern p = Pattern.compile("^smooth\\[([0-9])\\] = (.*)$");
    while (line != null && line.length() > 0) {
      try {
        Matcher m = p.matcher(line);
        if (m.matches()) {
          int i = Integer.parseInt(m.group(1));
          smooth[i] = Double.parseDouble(m.group(2));
        } else {
          // split on spaces, quote with doublequote, and escape with backslash
          String[] fields = StringUtils.splitOnCharWithQuoting(line, ' ', '\"', '\\');
          // System.out.println("fields:\n" + fields[0] + "\n" + fields[1] +
          // "\n" + fields[2] + "\n" + fields[3] + "\n" + fields[4]);
          boolean seen = fields[3].equals(SEEN);
          addTagging(seen, new IntTaggedWord(fields[2], fields[0]), Double.parseDouble(fields[4]));
        }
      } catch (RuntimeException e) {
        throw new IOException("Error on line " + lineNum + ": " + line);
      }
      lineNum++;
      line = in.readLine();
    }
  }

  /**
   * Writes out data from this Object to the Writer w. Rules are separated by
   * newline, and rule elements are delimited by \t.
   */
  public void writeData(Writer w) throws IOException {
    PrintWriter out = new PrintWriter(w);

    for (IntTaggedWord itw : seenCounter.keySet()) {
      out.println(itw.toLexicalEntry() + " SEEN " + seenCounter.getCount(itw));
    }
    for (IntTaggedWord itw : getUnknownWordModel().unSeenCounter().keySet()) {
      out.println(itw.toLexicalEntry() + " UNSEEN " + getUnknownWordModel().unSeenCounter().getCount(itw));
    }
    for (int i = 0; i < smooth.length; i++) {
      out.println("smooth[" + i + "] = " + smooth[i]);
    }
    out.flush();
  }

  /** Returns the number of rules (tag rewrites as word) in the Lexicon.
   *  This method assumes that the lexicon has been initialized.
   */
  public int numRules() {
    if (rulesWithWord == null) {
      initRulesWithWord();
    }
    int accumulated = 0;
    for (List<IntTaggedWord> lis : rulesWithWord) {
      accumulated += lis.size();
    }
    return accumulated;
  }


  private static final int STATS_BINS = 15;

  /** Print some statistics about this lexicon. */
  public void printLexStats() {
    if (rulesWithWord == null) {
      initRulesWithWord();
    }
    System.out.println("BaseLexicon statistics");
    System.out.println("unknownLevel is " + getUnknownWordModel().getUnknownLevel());
    // System.out.println("Rules size: " + rules.size());
    System.out.println("Sum of rulesWithWord: " + numRules());
    System.out.println("Tags size: " + tags.size());
    int wsize = words.size();
    System.out.println("Words size: " + wsize);
    // System.out.println("Unseen Sigs size: " + sigs.size() +
    // " [number of unknown equivalence classes]");
    System.out.println("rulesWithWord length: " + rulesWithWord.length
                       + " [should be sum of words + unknown sigs]");
    int[] lengths = new int[STATS_BINS];
    ArrayList[] wArr = new ArrayList[STATS_BINS];
    for (int j = 0; j < STATS_BINS; j++) {
      wArr[j] = new ArrayList();
    }
    for (int i = 0; i < rulesWithWord.length; i++) {
      int num = rulesWithWord[i].size();
      if (num > STATS_BINS - 1) {
        num = STATS_BINS - 1;
      }
      lengths[num]++;
      if (wsize <= 20 || num >= STATS_BINS / 2) {
        wArr[num].add(wordNumberer().object(i));
      }
    }
    System.out.println("Stats on how many taggings for how many words");
    for (int j = 0; j < STATS_BINS; j++) {
      System.out.print(j + " taggings: " + lengths[j] + " words ");
      if (wsize <= 20 || j >= STATS_BINS / 2) {
        System.out.print(wArr[j]);
      }
      System.out.println();
    }
    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMaximumFractionDigits(0);
    System.out.println("Unseen counter: " + Counters.toString(uwModel.unSeenCounter(), nf));

    if (wsize < 50 && tags.size() < 10) {
      nf.setMaximumFractionDigits(3);
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      pw.println("Tagging probabilities log P(word|tag)");
      for (int t = 0; t < tags.size(); t++) {
        pw.print('\t');
        pw.print(tagNumberer.object(t));
      }
      pw.println();
      for (int w = 0; w < wsize; w++) {
        pw.print(wordNumberer.object(w));
        pw.print('\t');
        for (int t = 0; t < tags.size(); t++) {
          IntTaggedWord iTW = new IntTaggedWord(w, t);
          pw.print(nf.format(score(iTW, 1)));
          if (t == tags.size() -1) {
            pw.println();
          } else
            pw.print('\t');
        }
      }
      pw.close();
      System.out.println(sw.toString());
    }
  }

  /**
   * Evaluates how many words (= terminals) in a collection of trees are
   * covered by the lexicon. First arg is the collection of trees; second
   * through fourth args get the results. Currently unused; this probably
   * only works if train and test at same time so tags and words variables
   * are initialized.
   */
  public double evaluateCoverage(Collection<Tree> trees, Set<String> missingWords,
                                 Set<String> missingTags, Set<IntTaggedWord> missingTW) {

    List<IntTaggedWord> iTW1 = new ArrayList<IntTaggedWord>();
    for (Tree t : trees) {
      iTW1.addAll(treeToEvents(t));
    }

    int total = 0;
    int unseen = 0;

    for (IntTaggedWord itw : iTW1) {
      total++;
      if (!words.contains(new IntTaggedWord(itw.word(), nullTag))) {
        missingWords.add((String) Numberer.object("word", itw.word()));
      }
      if (!tags.contains(new IntTaggedWord(nullWord, itw.tag()))) {
        missingTags.add((String) Numberer.object("tag", itw.tag()));
      }
      // if (!rules.contains(itw)) {
      if (seenCounter.getCount(itw) == 0.0) {
        unseen++;
        missingTW.add(itw);
      }
    }
    return (double) unseen / total;
  }

  private static final long serialVersionUID = 40L;

  int[] tagsToBaseTags = null;

  public int getBaseTag(int tag, TreebankLanguagePack tlp) {
    if (tagsToBaseTags == null) {
      populateTagsToBaseTags(tlp);
    }
    return tagsToBaseTags[tag];
  }

  private void populateTagsToBaseTags(TreebankLanguagePack tlp) {
    Numberer tagNumberer = tagNumberer();
    int total = tagNumberer.total();
    tagsToBaseTags = new int[total];
    for (int i = 0; i < total; i++) {
      String tag = (String) tagNumberer.object(i);
      String baseTag = tlp.basicCategory(tag);
      int j = tagNumberer.number(baseTag);
      tagsToBaseTags[i] = j;
    }
  }

  /** Provides some testing and opportunities for exploration of the
   *  probabilities of a BaseLexicon.  What's here currently probably
   *  only works for the English Penn Treeebank, as it uses default
   *  constructors.  Of the words given to test on,
   *  the first is treated as sentence initial, and the rest as not
   *  sentence initial.
   *
   *  @param args The command line arguments:
   *     java BaseLexicon treebankPath fileRange unknownWordModel words*
   */
  public static void main(String[] args) {
    if (args.length < 3) {
      System.err.println("java BaseLexicon treebankPath fileRange unknownWordModel words*");
      return;
    }
    System.out.print("Training BaseLexicon from " + args[0] + ' ' + args[1] + " ... ");
    Treebank tb = new DiskTreebank();
    tb.loadPath(args[0], new NumberRangesFileFilter(args[1], true));
    BaseLexicon lex = new BaseLexicon();
    lex.getUnknownWordModel().setUnknownLevel(Integer.parseInt(args[2]));
    lex.train(tb);
    System.out.println("done.");
    System.out.println();
    Numberer numb = Numberer.getGlobalNumberer("tags");
    Numberer wNumb = Numberer.getGlobalNumberer("words");
    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMaximumFractionDigits(4);
    List<String> impos = new ArrayList<String>();
    for (int i = 3; i < args.length; i++) {
      if (lex.isKnown(args[i])) {
        System.out.println(args[i] + " is a known word.  Log probabilities [log P(w|t)] for its taggings are:");
        for (Iterator<IntTaggedWord> it = lex.ruleIteratorByWord(wNumb.number(args[i]), i - 3); it.hasNext(); ) {
          IntTaggedWord iTW = it.next();
          System.out.println(StringUtils.pad(iTW, 24) + nf.format(lex.score(iTW, i - 3)));
        }
      } else {
        String sig = lex.getUnknownWordModel().getSignature(args[i], i-3);
        System.out.println(args[i] + " is an unknown word.  Signature with uwm " + lex.getUnknownWordModel().getUnknownLevel() + ((i == 3) ? " init": "non-init") + " is: " + sig);
        Set<String> tags = ErasureUtils.uncheckedCast(numb.objects());
        impos.clear();
        List<String> lis = new ArrayList<String>(tags);
        Collections.sort(lis);
        for (String tStr : lis) {
          IntTaggedWord iTW = new IntTaggedWord(args[i], tStr);
          double score = lex.score(iTW, 1);
          if (score == Float.NEGATIVE_INFINITY) {
            impos.add(tStr);
          } else {
            System.out.println(StringUtils.pad(iTW, 24) + nf.format(score));
          }
        }
        if (impos.size() > 0) {
          System.out.println(args[i] + " impossible tags: " + impos);
        }
      }
      System.out.println();
    }
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

  public void setWordNumberer(Numberer wordNumberer) {
    this.wordNumberer = wordNumberer;
    IntTaggedWord.setWordNumberer(wordNumberer);
  }

  public void setTagNumberer(Numberer tagNumberer) {
    this.tagNumberer = tagNumberer;
    IntTaggedWord.setTagNumberer(tagNumberer);
  }

  public UnknownWordModel getUnknownWordModel() {
    return uwModel;
  }

  public final void setUnknownWordModel(UnknownWordModel uwm) {
    this.uwModel = uwm;
  }


}

