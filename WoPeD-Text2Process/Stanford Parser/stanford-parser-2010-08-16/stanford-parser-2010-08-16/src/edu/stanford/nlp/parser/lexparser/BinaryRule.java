package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.util.Numberer;
import edu.stanford.nlp.util.StringUtils;

import java.io.Serializable;

/**
 * Binary rules (ints for parent, left and right children)
 *
 * @author Dan Klein
 */

public class BinaryRule extends Rule implements Serializable, Comparable {

  public int leftChild = -1;
  public int rightChild = -1;

  public BinaryRule() {
  }

  /** Create a new BinaryRule with the parent and children coded as ints.
   *  Score defaults to Float.NaN.
   *  @param parent The parent int
   *  @param leftChild The left child int
   *  @param rightChild The right child int
   */
  public BinaryRule(int parent, int leftChild, int rightChild) {
    this.parent = parent;
    this.leftChild = leftChild;
    this.rightChild = rightChild;
  }

  public BinaryRule(int parent, int leftChild, int rightChild, double score) {
    this.parent = parent;
    this.leftChild = leftChild;
    this.rightChild = rightChild;
    this.score = (float) score;
  }

  /**
   * Creates a BinaryRule from String s, assuming it was created using toString().
   *
   * @param s A String in which the binary rule is represented as parent,
   *     left-child, right-child, score, with the items quoted as needed
   * @param n Number used to convert String names to ints
   */
  public BinaryRule(String s, Numberer n) {
    String[] fields = StringUtils.splitOnCharWithQuoting(s, ' ', '\"', '\\');
    //    System.out.println("fields:\n" + fields[0] + "\n" + fields[2] + "\n" + fields[3] + "\n" + fields[4]);
    this.parent = n.number(fields[0]);
    this.leftChild = n.number(fields[2]);
    this.rightChild = n.number(fields[3]);
    this.score = Float.parseFloat(fields[4]);
  }

  private int hashCode = -1;
  @Override
  public int hashCode() {
    if (hashCode < 0) {
      hashCode = (parent << 16) ^ (leftChild << 8) ^ rightChild;
    }
    return hashCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof BinaryRule) {
      BinaryRule br = (BinaryRule) o;
      if (parent == br.parent && leftChild == br.leftChild && rightChild == br.rightChild) {
        return true;
      }
    }
    return false;
  }

  private static final char[] charsToEscape = new char[]{'\"'};


  @Override
  public String toString() {
    Numberer n = Numberer.getGlobalNumberer("states");
    return "\"" + StringUtils.escapeString(n.object(parent).toString(), charsToEscape, '\\') + "\" -> \"" + StringUtils.escapeString(n.object(leftChild).toString(), charsToEscape, '\\') + "\" \"" + StringUtils.escapeString(n.object(rightChild).toString(), charsToEscape, '\\') + "\" " + score;
  }

  private transient String cached = null;

  public String toStringNoScore() {
    if (cached == null) {
      Numberer n = Numberer.getGlobalNumberer("states");
      cached =  "\"" + StringUtils.escapeString(n.object(parent).toString(), charsToEscape, '\\') + "\" -> \"" + StringUtils.escapeString(n.object(leftChild).toString(), charsToEscape, '\\') + "\" \"" + StringUtils.escapeString(n.object(rightChild).toString(), charsToEscape, '\\');
    }
    return cached;
  }

  public int compareTo(Object o) {
    BinaryRule ur = (BinaryRule) o;
    if (parent < ur.parent) {
      return -1;
    }
    if (parent > ur.parent) {
      return 1;
    }
    if (leftChild < ur.leftChild) {
      return -1;
    }
    if (leftChild > ur.leftChild) {
      return 1;
    }
    if (rightChild < ur.rightChild) {
      return -1;
    }
    if (rightChild > ur.rightChild) {
      return 1;
    }
    return 0;
  }

  private static final long serialVersionUID = 1L;

}
