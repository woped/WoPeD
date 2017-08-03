package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.Tree;

import java.util.Collection;

/**
 * A lexicon class for Chinese.  Extends the (English) BaseLexicon class,
 * overriding its score and train methods to include a
 * ChineseUnknownWordModel.
 *
 * @author Roger Levy
 */
public class ChineseLexicon extends BaseLexicon {

  private static final long serialVersionUID = -7836464391021114960L;

  private static final boolean useRandomWalk = false;
  public static boolean useCharBasedUnknownWordModel = false;
  // public static boolean useMaxentUnknownWordModel = false;
  public static boolean useGoodTuringUnknownWordModel = false;

  //private ChineseUnknownWordModel unknown;
  // private ChineseMaxentLexicon cml;
  private static final int STEPS = 1;
  private RandomWalk probRandomWalk;


  public ChineseLexicon(Options.LexOptions op) {
    super(op);
    // if (useMaxentUnknownWordModel) {
    //  cml = new ChineseMaxentLexicon();
    // } else {
    //unknown = new ChineseUnknownWordModel();
    //this.setUnknownWordModel(new ChineseUnknownWordModel(op));
    // this.getUnknownWordModel().setLexicon(this);
    // }
  }


  /** Trains a lexicon on a collection of trees. */
  @Override
  public void train(Collection<Tree> trees) {
    super.train(trees);
    if (useRandomWalk) {
      probRandomWalk = new RandomWalk(trees, STEPS);
    }
    // if (useMaxentUnknownWordModel) {
    //  cml.trainUnknownWordModel(trees);
    // } else {
    //  getUnknownWordModel().train(trees);
    // }
  }


  @Override
  public float score(IntTaggedWord iTW, int loc) {
    double c_W = seenCounter.getCount(iTW);
    boolean seen = (c_W > 0.0);

    if (seen) {
      if (useRandomWalk) {
        return (float) scoreRandomWalk(iTW);
      } else {
        return super.score(iTW, loc);
      }
    } else {
      float score;
      // if (useMaxentUnknownWordModel) {
      //  score = cml.score(iTW, 0);
      // } else {
      score = this.getUnknownWordModel().score(iTW, loc, 0.0, 0.0, 0.0); // ChineseUnknownWordModel doesn't use the final three params
      // }
      return score;
    }
  }


  private double scoreRandomWalk(IntTaggedWord itw) {
    TaggedWord tw = itw.toTaggedWord();
    String word = tw.value();
    String tag = tw.tag();
    return probRandomWalk.score(tag, word);
  }

}
