package edu.stanford.nlp.parser.lexparser;

import java.util.Collection;
import java.util.Iterator;

import edu.stanford.nlp.util.Function;
import edu.stanford.nlp.trees.Tree;

/**
 * An abstract superclass for parser classes that extract counts from Trees.
 * @author grenager
 * @author Sarah Spikes (sdspikes@cs.stanford.edu) - cleanup and filling in types
 */

public abstract class AbstractTreeExtractor<T> implements Extractor<T> {

  @SuppressWarnings({"UnusedDeclaration"})
  protected void tallyLeaf(Tree lt) {
  }

  protected void tallyPreTerminal(Tree lt) {
  }

  protected void tallyInternalNode(Tree lt) {
  }

  protected void tallyRoot(Tree lt) {
  }

  public T formResult() {
    return null;
  }

  protected void tallyLocalTree(Tree lt) {
    // printTrainTree(null, "Tallying local tree:", lt);

    if (lt.isLeaf()) {
      //      System.out.println("it's a leaf");
      tallyLeaf(lt);
    } else if (lt.isPreTerminal()) {
      //      System.out.println("it's a preterminal");
      tallyPreTerminal(lt);
    } else {
      //      System.out.println("it's a internal node");
      tallyInternalNode(lt);
    }
  }

  public void tallyTree(Tree t) {
    tallyRoot(t);
    for (Tree localTree : t.subTreeList()) {
      tallyLocalTree(localTree);
    }
  }

  protected void tallyTrees(Collection<Tree> trees) {
    for (Tree tree : trees) {
      tallyTree(tree);
    }
  }

  protected void tallyTreeIterator(Iterator<Tree> treeIterator, Function<Tree, Tree> f) {
    while (treeIterator.hasNext()) {
      Tree tree = treeIterator.next();
      try {
        tree = f.apply(tree);
      } catch (Exception e) {
        if (Test.verbose) {
          e.printStackTrace();
        }
      }
      tallyTree(tree);
    }
  }

  public T extract() {
    return formResult();
  }

  public T extract(Collection<Tree> treeList) {
    tallyTrees(treeList);
    return formResult();
  }

  protected double weight = 1.0;

  public T extract(Collection<Tree> trees1, Collection<Tree> trees2, double weight) {
    this.weight = 1.0; // mg 2008: in case extract() is called 2+ times with weight != 1.0
    tallyTrees(trees1);
    double oldWeight = this.weight;
    this.weight = weight;
    tallyTrees(trees2);
    this.weight = oldWeight;
    return formResult();
  }

  public T extract(Iterator<Tree> treeIterator, Function<Tree, Tree> f) {
    tallyTreeIterator(treeIterator, f);
    return formResult();
  }

}
