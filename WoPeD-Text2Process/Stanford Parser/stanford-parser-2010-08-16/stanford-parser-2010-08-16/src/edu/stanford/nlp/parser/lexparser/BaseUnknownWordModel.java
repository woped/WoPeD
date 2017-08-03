package edu.stanford.nlp.parser.lexparser;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.Tag;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Pair;


/**
 *  An unknown word model for a generic language.  This was originally designed for
 *  German, changing only to remove German-specific numeric features.  Models unknown
 *  words based on their prefix and suffixes, as well as capital letters.
 *
 * @author Roger Levy
 * @author Greg Donaker (corrections and modeling improvements)
 * @author Christopher Manning (generalized and improved what Greg did)
 * @author Anna Rafferty
 *
 */
public class BaseUnknownWordModel implements UnknownWordModel {

  private static final long serialVersionUID = 6355171148751673822L;

  protected static final boolean VERBOSE = false;

  protected boolean useFirst = false; //= true;
  private boolean useEnd = true;
  protected boolean useGT = false;
  private boolean useFirstCap = true; // Only care if first is capitalized

  private int endLength = 2; // only used if useEnd==true

  /** What type of equivalence classing is done in getSignature */
  protected int unknownLevel;

  protected static final String unknown = "UNK";

  protected static final int nullWord = -1;
  protected static final short nullTag = -1;

  /**
   * Has counts for taggings in terms of unseen signatures. The IntTagWords are
   * for (tag,sig), (tag,null), (null,sig), (null,null). (None for basic UNK if
   * there are signatures.)
   */
  protected ClassicCounter<IntTaggedWord> unSeenCounter = new ClassicCounter<IntTaggedWord>();

  /** This maps from a tag (as a label) to a Counter from word signatures to
   *  their P(sig|tag), as estimated in the model. For Chinese, the word
   *  signature is just the first character or its unicode type for things
   *  that aren't Chinese characters.
   */
  protected HashMap<Label,ClassicCounter<String>> tagHash = new HashMap<Label,ClassicCounter<String>>();

  /** This is the set of all signatures that we have seen. */
  private Set<String> seenEnd = new HashSet<String>();

  private HashMap<Label,Float> unknownGT = new HashMap<Label,Float>();

  /** All classes that implement UnknownWordModel must call the constructor that initializes this variable. */
  private final Lexicon lex;


  public BaseUnknownWordModel(Options.LexOptions op, Lexicon lex) {
    endLength = op.unknownSuffixSize;
    useEnd = op.unknownSuffixSize > 0 && op.useUnknownWordSignatures > 0;
    useFirstCap = op.useUnknownWordSignatures > 0;
    this.lex = lex;
  }


  /**
   * Currently we don't consider loc or the other parameters in determining
   * score in the default implementation; only English uses them.
   */
  public float score(IntTaggedWord itw, int loc, double c_Tseen, double total, double smooth) {
    return score(itw);
  }


  // cdm 2010: todo: Recheck that this method really does the right thing in making a P(W|T) estimate....
  public float score(IntTaggedWord itw) {
    float logProb;

    String word = itw.wordString();
    // Label tag = itw.tagLabel();
    String tagStr = itw.tagString();
    Label tag = new Tag(tagStr);

    // testing
    //EncodingPrintWriter.out.println("Scoring unknown word " + word + " with tag " + tag,encoding);
    // end testing

    if (useEnd || useFirst || useFirstCap) {
      String end = getSignature(word, -1); // The getSignature here doesn't use sentence position
      if (useGT && ! seenEnd.contains(end)) {
        logProb = scoreGT(tag);
      } else {
        if ( ! seenEnd.contains(end)) {
          end = unknown;
        }
        //System.out.println("using end-character model for for unknown word "+  word + " for tag " + tag);

        /* get the Counter of terminal rewrites for the relevant tag */
        ClassicCounter<String> wordProbs = tagHash.get(tag);
        /* if the proposed tag has never been seen before, issue a
         * warning and return probability 0
         */
        if (wordProbs == null) {
          System.err.println("Warning: proposed tag is unseen in training data:\t"+tag);
          logProb = Float.NEGATIVE_INFINITY;
        } else if (wordProbs.keySet().contains(end)) {
          logProb = (float) wordProbs.getCount(end);
        } else {
          logProb = (float) wordProbs.getCount(unknown);
        }
      }
    } else if (useGT) {
      logProb = scoreGT(tag);
    } else {
      System.err.println("Warning: no unknown word model in place!\nGiving the combination " + word + ' ' + tag + " zero probability.");
      logProb = Float.NEGATIVE_INFINITY; // should never get this!
    }

    //EncodingPrintWriter.out.println("Unknown word estimate for " + word + " as " + tag + ": " + logProb,encoding); //debugging
    return logProb;
  }


  /** Calculate P(Tag|Signature) with Bayesian smoothing via just P(Tag|Unknown) */
  public double scoreProbTagGivenWordSignature(IntTaggedWord iTW, int loc, double smooth) {
    throw new UnsupportedOperationException();
  }


  protected float scoreGT(Label tag) {
    if (VERBOSE) System.err.println("using GT for unknown word and tag " + tag);
    if (unknownGT.containsKey(tag)) {
      return unknownGT.get(tag).floatValue();
    } else {
      return Float.NEGATIVE_INFINITY;
    }
  }

  /**
   * Signature for a specific word; loc parameter is ignored.
   * @param word The word
   * @param loc Its sentence position
   * @return A "signature" (which represents an equivalence class of Strings), e.g., a suffix of the string
   */
  public String getSignature(String word, int loc) {
    StringBuilder subStr = new StringBuilder("UNK-");
    int n = word.length() - 1;
    char first = word.charAt(0);
    if (useFirstCap) {
      if (Character.isUpperCase(first) || Character.isTitleCase(first)) {
        subStr.append('C');
      } else {
        subStr.append('c');
      }
    }
    if (useFirst) {
      subStr.append(first);
    }
    if (useEnd) {
      subStr.append(word.substring(n - endLength > 0 ? n - endLength : 0, n));
    }
    return subStr.toString();
  }

  public int getSignatureIndex(int wordIndex, int sentencePosition) {
    return 0;
  }


  /**
   * trains the end-character based unknown word model.
   *
   * @param trees the collection of trees to be trained over
   */
  public void train(Collection<Tree> trees) {
    // Records the number of times word/tag pair was seen in training data.
    ClassicCounter<IntTaggedWord> seenCounter = new ClassicCounter<IntTaggedWord>();
    // Counts of each tag (stored as a Label) on unknown words.
    ClassicCounter<Label> tc = new ClassicCounter<Label>();

    if (useFirst) {
      System.err.println("Including first letter for unknown words.");
    }
    if (useFirstCap) {
      System.err.println("Including whether first letter is capitalized for unknown words");
    }
    if (useEnd) {
      System.err.println("Classing unknown word as the average of their equivalents by identity of last " + endLength + " letters.");
    }
    if (useGT) {
      System.err.println("Using Good-Turing smoothing for unknown words.");
    }

    trainUnknownGT(trees);

    HashMap<Label,ClassicCounter<String>> c = new HashMap<Label,ClassicCounter<String>>(); // tag (Label) --> signature --> count

    // scan data
    int tNum = 0;
    int tSize = trees.size();
    int indexToStartUnkCounting = (int) (tSize * Train.fractionBeforeUnseenCounting);
    IntTaggedWord iTotal = new IntTaggedWord(nullWord, nullTag);
    for (Tree t : trees) {
      tNum++;
      for (Tree node : t) {
        if (node.isPreTerminal()) {
          String word = node.firstChild().label().value();
          String subString = getSignature(word, -1); // The getSignature here doesn't use sentence position

          Label tag = node.label();
          if ( ! c.containsKey(tag)) {
            c.put(tag, new ClassicCounter<String>());
          }
          c.get(tag).incrementCount(subString);

          tc.incrementCount(tag);

          seenEnd.add(subString);

          String tagStr = node.label().value();
          IntTaggedWord iW = new IntTaggedWord(word, IntTaggedWord.ANY);
          seenCounter.incrementCount(iW);
          if (tNum > indexToStartUnkCounting) {
            // start doing this once some way through trees; tNum is 1 based counting
            if (seenCounter.getCount(iW) < 2) {
              IntTaggedWord iT = new IntTaggedWord(IntTaggedWord.ANY, tagStr);
              unSeenCounter.incrementCount(iT);
              unSeenCounter.incrementCount(iTotal);
            }
          }
        }
      }
    }

    for (Label tag : c.keySet()) {
      /* outer iteration is over tags */
      ClassicCounter<String> wc = c.get(tag); // counts for words given a tag

      if (!tagHash.containsKey(tag)) {
        tagHash.put(tag, new ClassicCounter<String>());
      }

      /* the UNKNOWN sequence is assumed to be seen once in each tag */
      // This is sort of broken, but you can regard it as a Dirichlet prior.
      tc.incrementCount(tag);
      wc.setCount(unknown, 1.0);

      /* inner iteration is over words */
      for (String end : wc.keySet()) {
        double prob = Math.log((wc.getCount(end)) / (tc.getCount(tag)));  // p(sig|tag)
        tagHash.get(tag).setCount(end, prob);
        //if (Test.verbose)
        //EncodingPrintWriter.out.println(tag + " rewrites as " + end + " endchar with probability " + prob,encoding);
      }
    }
  }


  /** Trains Good-Turing estimation of unknown words.
   *
   *  @param trees Trees to train model from
   */
  protected void trainUnknownGT(Collection<Tree> trees) {

    ClassicCounter<Pair<String,Label>> wtCount = new ClassicCounter<Pair<String,Label>>();
    ClassicCounter<Label> tagCount = new ClassicCounter<Label>();
    ClassicCounter<Label> r1 = new ClassicCounter<Label>(); // for each tag, # of words seen once
    ClassicCounter<Label> r0 = new ClassicCounter<Label>(); // for each tag, # of words not seen
    Set<String> seenWords = new HashSet<String>();

    int tokens = 0;

    /* get TaggedWord and total tag counts, and get set of all
     * words attested in training
     */
    for (Tree t : trees) {
      // List<TaggedWord> words = t.taggedYield();
      for (Tree node : t) {
        if (node.isPreTerminal()) {
          tokens++;
          String word = node.getChild(0).label().value();
          Label tag = node.label();

          Pair<String,Label> wt = new Pair<String,Label>(word, tag);

          //String word = wt.word();

          //if (Test.verbose) EncodingPrintWriter.out.println("recording instance of " + wt.toString(),encoding); // testing

          wtCount.incrementCount(wt);// TaggedWord has crummy equality conditions
          //if (Test.verbose) EncodingPrintWriter.out.println("This is the " + wtCount.getCount(wt) + "th occurrence of" + wt.toString(),encoding); // testing
          tagCount.incrementCount(tag);
          //boolean alreadySeen = seenWords.add(word);
          seenWords.add(word);
          // if (Test.verbose) if(! alreadySeen) EncodingPrintWriter.out.println("already seen " + wt.toString(),encoding); // testing
        }
      }
    }

    // testing: get some stats here
    System.out.println("Total tokens: " + tokens);
    System.out.println("Total WordTag types: " + wtCount.keySet().size());
    System.out.println("Total tag types: " + tagCount.keySet().size());
    System.out.println("Total word types: " + seenWords.size());


    /* find # of once-seen words for each tag */
    for (Pair<String,Label> wt : wtCount.keySet()) {
      if (wtCount.getCount(wt) == 1) {
        r1.incrementCount(wt.second());
      }
    }

    /* find # of unseen words for each tag */
    for (Label tag : tagCount.keySet()) {
      for (String word : seenWords) {
        Pair<String,Label> wt = new Pair<String,Label>(word, tag);
        //EncodingPrintWriter.out.println("seeking " + wt.toString(),encoding); // testing
        if (!(wtCount.keySet().contains(wt))) {
          r0.incrementCount(tag);
          //EncodingPrintWriter.out.println("unseen " + wt.toString(),encoding); // testing
        } else {
          //EncodingPrintWriter.out.println("count for " + wt.toString() + " is " + wtCount.getCount(wt),encoding);
        }
      }
    }

    /* set unseen word probability for each tag */
    for (Label tag : tagCount.keySet()) {
      //System.out.println("Tag " + tag + ".  Word types for which seen once: " + r1.getCount(tag) + ".  Word types for which unseen: " + r0.getCount(tag) + ".  Total count token for tag: " + tagCount.getCount(tag)); // testing

      float logprob = (float) Math.log(r1.getCount(tag) / (tagCount.getCount(tag) * r0.getCount(tag)));

      unknownGT.put(tag, Float.valueOf(logprob));
    }

    /* testing only: print the GT-smoothed model */
    //System.out.println("The GT-smoothing model:");
    //System.out.println(unknownGT.toString());
    //EncodingPrintWriter.out.println(wtCount.toString(),encoding);

  }

  // private static WordTag toWordTag(TaggedWord tw) {
  //   return new WordTag(tw.word(), tw.tag());
  // }


  /**
   * Get the lexicon associated with this unknown word model; usually not used, but
   * might be useful to tell you if a related word is known or unknown, for example.
   */
  public Lexicon getLexicon() {
    return lex;
  }


  public int getUnknownLevel() {
    return unknownLevel;
  }

  public void setUnknownLevel(int unknownLevel) {
    this.unknownLevel = unknownLevel;
  }


  /**
   * Adds the tagging with count to the data structures in this Lexicon.
   */
  public void addTagging(boolean seen, IntTaggedWord itw, double count) {
    if (seen) {
      System.err.println("UWM.addTagging: Shouldn't call with seen word!");
   } else {
      unSeenCounter.incrementCount(itw, count);
      // if (itw.tag() == nullTag) {
      // sigs.add(itw);
      // }
    }
  }

  public Counter<IntTaggedWord> unSeenCounter() {
    return unSeenCounter;
  }

}
