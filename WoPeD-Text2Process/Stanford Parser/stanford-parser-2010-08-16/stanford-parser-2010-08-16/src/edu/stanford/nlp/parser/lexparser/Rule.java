package edu.stanford.nlp.parser.lexparser;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Parent class for unary and binary rules.
 *
 * @author Dan Klein
 */
public class Rule implements Serializable {

  public int parent = -1;

  public float score = Float.NaN;

  public Rule() {}

  public float score() {
    return score;
  }

  public boolean isUnary() {
    return false;
  }

  static class ScoreComparator implements Comparator<Rule> {

    public int compare(Rule r1, Rule r2) {
      if (r1.score() < r2.score()) {
        return -1;
      } else if (r1.score() == r2.score()) {
        return 0;
      } else {
        return 1;
      }
    }

    ScoreComparator() {}

  }

  private static Comparator<Rule> scoreComparator = new ScoreComparator();

  public static Comparator<Rule> scoreComparator() {
    return scoreComparator;
  }

  // upped 1 to 2 when changed to float
  private static final long serialVersionUID = 2L;  

}
