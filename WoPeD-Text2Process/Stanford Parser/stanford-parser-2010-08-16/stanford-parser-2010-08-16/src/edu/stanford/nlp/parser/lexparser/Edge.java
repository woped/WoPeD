package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.util.Numberer;

/** Class for parse edges.
 *
 *  @author Dan Klein
 */
public class Edge extends Item {

  public Hook backHook;

  @Override
  public boolean isEdge() {
    return true;
  }

  @Override
  public String toString() {
    return "Edge(" + Numberer.getGlobalNumberer("states").object(state) + ":" + start + "-" + end + "," + head + "/" + Numberer.getGlobalNumberer("tags").object(tag) + ")";
  }

  @Override
  public int hashCode() {
    return (state << 1) ^ (head << 8) ^ (tag << 16) ^ (start << 4) ^ (end << 24);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o instanceof Edge) {
      Edge e = (Edge) o;
      if (state == e.state && head == e.head && tag == e.tag && start == e.start && end == e.end) {
        return true;
      }
    }
    return false;
  }

}
