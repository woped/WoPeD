package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.util.Numberer;

/** Class for parse table hooks.  A "hook" is the parse item that Eisner and
 *  Satta introduced to reduce the complexity of lexicalized parsing to 
 *  O(n^4).
 *
 *  @author Dan Klein
 */
public class Hook extends Item {

  public int subState;

  @Override
  public boolean isPreHook() {
    return head < start;
  }

  @Override
  public boolean isPostHook() {
    return head >= end;
  }

  @Override
  public String toString() {
    return (isPreHook() ? "Pre" : "Post") + "Hook(" + Numberer.getGlobalNumberer("states").object(state) + "|" + Numberer.getGlobalNumberer("states").object(subState) + ":" + start + "-" + end + "," + head + "/" + Numberer.getGlobalNumberer("tags").object(tag) + ")";
  }

  @Override
  public int hashCode() {
    return 1 + (state << 14) ^ (subState << 16) ^ (head << 22) ^ (tag << 27) ^ (start << 1) ^ (end << 7);
  }

  /** Hooks are equal if they have same state, substate, head, tag, start, 
   *  and end.
   */
  @Override
  public boolean equals(Object o) {
    // System.out.println("\nCHECKING HOOKS: " + this + " vs. " + o);
    if (this == o) {
      return true;
    }
    if (o instanceof Hook) {
      Hook e = (Hook) o;
      if (state == e.state && subState == e.subState && head == e.head &&
          tag == e.tag && start == e.start && end == e.end) {
        return true;
      }
    }
    return false;
  }

}
