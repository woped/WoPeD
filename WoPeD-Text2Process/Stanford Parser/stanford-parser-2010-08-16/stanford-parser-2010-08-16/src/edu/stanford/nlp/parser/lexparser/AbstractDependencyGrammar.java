package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.util.Numberer;

import static edu.stanford.nlp.parser.lexparser.IntTaggedWord.ANY_WORD_INT;
import static edu.stanford.nlp.parser.lexparser.IntTaggedWord.ANY_TAG_INT;
import static edu.stanford.nlp.parser.lexparser.IntTaggedWord.STOP_WORD_INT;
import static edu.stanford.nlp.parser.lexparser.IntTaggedWord.STOP_TAG_INT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstract base class for dependency grammars.  The only thing you have
 * to implement in a subclass is scoreTB (score a "tag binned" dependency
 * in the tagProjection space).  A subclass also has to either call
 * super() in its constructor, or otherwise initialize the tagBin array.
 * The call to initTagBins() (in the constructor) must be made after all
 * keys have been entered into Numberer.getGlobalNumberer("tags").
 *
 * @author Galen Andrew
 */
public abstract class AbstractDependencyGrammar implements DependencyGrammar {

  protected TagProjection tagProjection;
  private static Numberer tagNumberer;
  private static Numberer wordNumberer;
  protected int numTagBins;
  protected int[] tagBin;
  protected TreebankLanguagePack tlp;
  protected boolean directional;
  protected boolean useDistance;
  protected boolean useCoarseDistance;

  protected Lexicon lex;

  protected static final IntTaggedWord stopTW = new IntTaggedWord(STOP_WORD_INT, STOP_TAG_INT);
  protected static final IntTaggedWord wildTW = new IntTaggedWord(ANY_WORD_INT, ANY_TAG_INT);

  /** A temp holder for efficiency. Its value is restored in the readObject
   *  method, so it works after deserialization.
   */
  private transient IntDependency tempDependency
    = new IntDependency(-2, -2, -2, -2, false, 0);
  private transient IntDependency internTempDependency = null;
  protected transient Map<IntDependency,IntDependency> expandDependencyMap =
      new HashMap<IntDependency,IntDependency>();

  private static final boolean DEBUG = false;

  protected static int[] coarseDistanceBins = new int[] {0, 2, 5};
  protected static int[] regDistanceBins = new int[] {0, 1, 5, 10};

  public AbstractDependencyGrammar(TreebankLanguagePack tlp, TagProjection tagProjection, boolean directional, boolean useDistance, boolean useCoarseDistance) {
    this.tlp = tlp;
    this.tagProjection = tagProjection;
    this.directional = directional;
    this.useDistance = useDistance;
    this.useCoarseDistance = useCoarseDistance;
    initTagBins();
  }

  public void setLexicon(Lexicon lexicon) {
    lex = lexicon;
  }

  protected static Numberer tagNumberer() {
    if (tagNumberer == null) {
      tagNumberer = Numberer.getGlobalNumberer("tags");
    }
    return tagNumberer;
  }

  protected static Numberer wordNumberer() {
    if (wordNumberer == null) {
      wordNumberer = Numberer.getGlobalNumberer("words");
    }
    return wordNumberer;
  }

  /**
   * Default is no-op.
   */
  public void tune(Collection<Tree> trees) {
  }

  public int numTagBins() {
    return numTagBins;
  }

  public int tagBin(int tag) {
    if (tag < 0) {
      return tag;
    } else {
      return tagBin[tag];
    }
  }

  public static boolean rootTW(IntTaggedWord rTW) {
    // System.out.println("rootTW: checking if " + rTW.toString("verbose") +
    // " == " + Lexicon.BOUNDARY_TAG + "[" +
    // tagNumberer().number(Lexicon.BOUNDARY_TAG) + "]" + ": " +
    // (rTW.tag == tagNumberer().number(Lexicon.BOUNDARY_TAG)));
    return rTW.tag == tagNumberer().number(Lexicon.BOUNDARY_TAG);
  }

  protected short valenceBin(int distance) {
    if (!useDistance) {
      return 0;
    }
    if (distance < 0) {
      return -1;
    }
    if (distance == 0) {
      return 0;
    }
    return 1;
  }

  public int numDistBins() {
    return useCoarseDistance ? 4 : 5;
  }

  public short distanceBin(int distance) {
    if (!useDistance) {
      return 0;
    } else if (useCoarseDistance) {
      return coarseDistanceBin(distance);
    } else {
      return regDistanceBin(distance);
    }
  }

  public static short regDistanceBin(int distance) {
    for(short i=0; i<regDistanceBins.length; ++i)
      if (distance <= regDistanceBins[i])
        return i;
    return (short) regDistanceBins.length;
  }

  public static short coarseDistanceBin(int distance) {
    for(short i=0; i<coarseDistanceBins.length; ++i)
      if (distance <= coarseDistanceBins[i])
        return i;
    return (short) coarseDistanceBins.length;
  }

  static void setCoarseDistanceBins(int[] bins) {
    assert(bins.length == 3);
    coarseDistanceBins = bins;
  }

  static void setRegDistanceBins(int[] bins) {
    assert(bins.length == 4);
    regDistanceBins = bins;
  }

  protected void initTagBins() {
    Numberer tagBinNumberer = new Numberer();
    if (DEBUG) {
      System.err.println();
      System.err.println("There are " + tagNumberer().total() + " tags.");
    }
    tagBin = new int[tagNumberer().total()];
    for (int t = 0; t < tagBin.length; t++) {
      String tagStr = (String) tagNumberer().object(t);
      String binStr;
      if (tagProjection == null) {
        binStr = tagStr;
      } else {
        binStr = tagProjection.project(tagStr);
      }
      tagBin[t] = tagBinNumberer.number(binStr);
      if (DEBUG) {
        System.err.println("initTagBins: Mapped " + tagStr + " (" + t +
                           ") to " + binStr + " (" + tagBin[t] + ")");
      }
    }
    numTagBins = tagBinNumberer.total();
    if (DEBUG) {
      System.err.println("initTagBins: tags " + tagBin.length + " bins " +
                         numTagBins);
      System.err.println("tagBins: " + tagBinNumberer);
    }
  }

  public double score(IntDependency dependency) {
    short hTBackup = dependency.head.tag;
    short aTBackup = dependency.arg.tag;

    dependency.head.tag = (short) tagBin(dependency.head.tag);
    dependency.arg.tag = (short) tagBin(dependency.arg.tag);

    double s = scoreTB(dependency);

    dependency.head.tag = hTBackup;
    dependency.arg.tag = aTBackup;

    return s;
  }

  // currently unused
  public double score(int headWord, int headTag, int argWord, int argTag, boolean leftHeaded, int dist) {
    tempDependency.head.word = headWord;
    tempDependency.head.tag = (short) headTag;
    tempDependency.arg.word = argWord;
    tempDependency.arg.tag = (short) argTag;
    tempDependency.leftHeaded = leftHeaded;
    tempDependency.distance = (short) dist;
    return score(tempDependency); // this method tag bins
  }

  public double scoreTB(int headWord, int headTag, int argWord, int argTag, boolean leftHeaded, int dist) {
    tempDependency.head.word = headWord;
    tempDependency.head.tag = (short) headTag;
    tempDependency.arg.word = argWord;
    tempDependency.arg.tag = (short) argTag;
    tempDependency.leftHeaded = leftHeaded;
    tempDependency.distance = (short) dist;
    return scoreTB(tempDependency);
  }

  private void readObject(ObjectInputStream ois)
    throws IOException, ClassNotFoundException {
    ois.defaultReadObject();
    // reinitialize the transient tempDependency object
    tempDependency = new IntDependency(-2, -2, -2, -2, false, 0);
  }

  /**
   * Default is to throw exception.
   * @throws IOException
   */
  public void readData(BufferedReader in) throws IOException {
    throw new UnsupportedOperationException();
  }

  /**
   * Default is to throw exception.
   * @throws IOException
   */
  public void writeData(PrintWriter out) throws IOException {
    throw new UnsupportedOperationException();
  }

  /** This is a custom interner that simultaneously creates and interns an IntDependency.
   *  It uses the standard Klein trick of a temporary transient variable to reduce
   *  memory allocations at the cost of making the code not thread-safe.
   *
   * @return An interned IntDependency
   */
  protected IntDependency intern(IntTaggedWord headTW, IntTaggedWord argTW, boolean leftHeaded, short dist) {
    Map<IntDependency,IntDependency> map = expandDependencyMap;
    if (internTempDependency == null) {
      internTempDependency = new IntDependency();
    }
    internTempDependency.head = edu.stanford.nlp.util.Interner.globalIntern(headTW);
    internTempDependency.arg = edu.stanford.nlp.util.Interner.globalIntern(argTW);
    internTempDependency.leftHeaded = leftHeaded;
    internTempDependency.distance = dist;
    IntDependency returnDependency = internTempDependency;
    if (map != null) {
      returnDependency = map.get(internTempDependency);
      if (returnDependency == null) {
        map.put(internTempDependency, internTempDependency);
        returnDependency = internTempDependency;
      }
    }
    if (returnDependency == internTempDependency) {
      internTempDependency = null;
    }
    return returnDependency;
  }

  private static final long serialVersionUID = 2L;

}
