package edu.stanford.nlp.parser.metrics;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.nlp.io.EncodingPrintWriter;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.trees.Dependency;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.UnnamedDependency;
import edu.stanford.nlp.util.Filter;

/** 
 * Evaluates the dependency accuracy of a tree (based on HeadFinder
 *  dependency judgments).
 *  
 *  TODO CDM Mar 2004: This should be rewritten so as to root a word at an
 *  index position; otherwise it doesn't work correctly when you get two
 *  identical dependents (like with "I went to Greece to see the ruins").
 *  
 *  TODO spence Feb 2010: Trees not only need to implement the HasIndex
 *  interface, but also the head finding algorithm must set the index()
 *  annotation for each phrasal node according to the head.
 *  
 *  @author Dan Klein
 *  
 */
public class DependencyEval extends AbstractEval {

  private static final boolean DEBUG = false;
  Filter<String> punctFilter;

  /** 
   * @param punctFilter A filter that accepts punctuation words.
   */
  public DependencyEval(String str, boolean runningAverages, Filter<String> punctFilter) {
    super(str, runningAverages);
    this.punctFilter = punctFilter;
  }

  /**
   * Build the set of dependencies for evaluation.  This set excludes
   * all dependencies for which the argument is a punctuation tag.
   */
  @Override
  protected
  Set<?> makeObjects(Tree tree) {
    Set<Dependency<Label, Label, Object>> deps = new HashSet<Dependency<Label, Label, Object>>();
    for (Tree node : tree.subTreeList()) {
      if (DEBUG) EncodingPrintWriter.err.println("Considering " + node.label());
      // every child with a different head is an argument, as are ones with
      // the same head after the first one found
      if (node.isLeaf() || node.children().length < 2) {
        continue;
      }
      // System.err.println("XXX node is " + node + "; label type is " +
      //                         node.label().getClass().getName());
      String head = ((HasWord) node.label()).word();
      boolean seenHead = false;
      for (int cNum = 0; cNum < node.children().length; cNum++) {
        Tree child = node.children()[cNum];
        String arg = ((HasWord) child.label()).word();
        if (DEBUG) EncodingPrintWriter.err.println("Considering " + head + " --> " + arg);
        if (head.equals(arg) && !seenHead) {
          seenHead = true;
          if (DEBUG) EncodingPrintWriter.err.println("  ... is head");
        } else if (!punctFilter.accept(arg)) {
          deps.add(new UnnamedDependency(head, arg));
          if (DEBUG) EncodingPrintWriter.err.println("  ... added");
        } else if (DEBUG) {
          if (DEBUG) EncodingPrintWriter.err.println("  ... is punct dep");
        }
      }
    }
    if (DEBUG) {
      EncodingPrintWriter.err.println("Deps: " + deps);
    }
    return deps;
  }

}