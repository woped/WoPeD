package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.util.Scored;


/** Abstract class for parse items.
 *
 *  @author Dan Klein
 */
abstract class Item implements Scored {

  public int start;
  public int end;
  public int state;
  public int head;
  public int tag;
  public Edge backEdge;
  public double iScore = Double.NEGATIVE_INFINITY;
  public double oScore = Double.NEGATIVE_INFINITY;

  public double score() {
    if (Test.exhaustiveTest) {
      return iScore;
    } else {
      return iScore + oScore;
    }
  }

  public boolean isEdge() {
    return false;
  }

  public boolean isPreHook() {
    return false;
  }

  public boolean isPostHook() {
    return false;
  }

}
