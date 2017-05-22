package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.util.Numberer;
import edu.stanford.nlp.util.StringUtils;

/**
 * Unary grammar rules (with ints for parent and child).
 *
 * @author Dan Klein
 */
public class UnaryRule extends Rule implements Comparable<UnaryRule> {

  public int child = -1;

  /** Fields are set to: -1, -1, Float.NaN. */
  public UnaryRule() {
  }

  /** The score is set to Float.NaN by default. */
  public UnaryRule(int parent, int child) {
    this.parent = parent;
    this.child = child;
  }

  public UnaryRule(int parent, int child, double score) {
    this.parent = parent;
    this.child = child;
    this.score = (float) score;
  }

  /** Decode a UnaryRule out of a String representation with help from
   *  a Numberer.
   */
  public UnaryRule(String s, Numberer n) {
    String[] fields = StringUtils.splitOnCharWithQuoting(s, ' ', '\"', '\\');
    //    System.out.println("fields:\n" + fields[0] + "\n" + fields[2] + "\n" + fields[3]);
    this.parent = n.number(fields[0]);
    this.child = n.number(fields[2]);
    this.score = Float.parseFloat(fields[3]);
  }

  @Override
  public boolean isUnary() {
    return true;
  }

  @Override
  public int hashCode() {
    return (parent << 16) ^ child;
  }

  /** A UnaryRule is equal to another UnaryRule with the same parent and child.
   *  The score is not included in the equality computation.
   *
   * @param o Object to be compared with
   * @return Whether the object is equal to this
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof UnaryRule) {
      UnaryRule ur = (UnaryRule) o;
      if (parent == ur.parent && child == ur.child) {
        return true;
      }
    }
    return false;
  }

  public int compareTo(UnaryRule ur) {
    if (parent < ur.parent) {
      return -1;
    }
    if (parent > ur.parent) {
      return 1;
    }
    if (child < ur.child) {
      return -1;
    }
    if (child > ur.child) {
      return 1;
    }
    return 0;
  }

  private static final char[] charsToEscape = new char[]{'\"'};

  @Override
  public String toString() {
    Numberer n = Numberer.getGlobalNumberer("states");
    return "\"" + StringUtils.escapeString(n.object(parent).toString(), charsToEscape, '\\') + "\" -> \"" + StringUtils.escapeString(n.object(child).toString(), charsToEscape, '\\') + "\" " + score;
  }

  private transient String cached = null;
  
  public String toStringNoScore() {
    if (cached == null) {
      Numberer n = Numberer.getGlobalNumberer("states");
      cached =  "\"" + StringUtils.escapeString(n.object(parent).toString(), charsToEscape, '\\') + "\" -> \"" + StringUtils.escapeString(n.object(child).toString(), charsToEscape, '\\');
    }
    return cached;
  }

  private static final long serialVersionUID = 1L;

}

