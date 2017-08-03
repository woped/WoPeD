package edu.stanford.nlp.trees;

import edu.stanford.nlp.trees.international.pennchinese.CharacterLevelTagExtender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Galen Andrew
 */
public class TreeToBracketProcessor {
  public List evalTypes = new ArrayList();
  public static CharacterLevelTagExtender ext = new CharacterLevelTagExtender();

  public TreeToBracketProcessor(List evalTypes) {
    this.evalTypes = evalTypes;
  }

  public Collection allBrackets(Tree root) {
    boolean words = evalTypes.contains(WordCatConstituent.wordType);
    boolean tags = evalTypes.contains(WordCatConstituent.tagType);
    boolean cats = evalTypes.contains(WordCatConstituent.catType);
    List brackets = new ArrayList();
    if (words || cats || tags) {
      root = ext.transformTree(root);
      for (Iterator iterator = root.iterator(); iterator.hasNext();) {
        Tree tree = (Tree) iterator.next();
        if (tree.isPrePreTerminal() && !tree.value().equals("ROOT")) {
          if (words) {
            brackets.add(new WordCatConstituent(tree, root, WordCatConstituent.wordType));
          }
          if (tags) {
            brackets.add(new WordCatConstituent(tree, root, WordCatConstituent.tagType));
          }
        } else if (cats && tree.isPhrasal() && !tree.value().equals("ROOT")) {
          brackets.add(new WordCatConstituent(tree, root, WordCatConstituent.catType));
        }
      }
    }

    return brackets;
  }

  public Collection commonWordTagTypeBrackets(Tree root1, Tree root2) {
    root1 = ext.transformTree(root1);
    root2 = ext.transformTree(root2);

    List firstPreTerms = new ArrayList();
    for (Iterator iterator = root1.iterator(); iterator.hasNext();) {
      Tree tree = (Tree) iterator.next();
      if (tree.isPrePreTerminal()) {
        firstPreTerms.add(tree);
      }
    }

    List brackets = new ArrayList();
    for (Iterator pretermIter = firstPreTerms.iterator(); pretermIter.hasNext();) {
      Tree preTerm = (Tree) pretermIter.next();
      for (Iterator iter = root2.iterator(); iter.hasNext();) {
        Tree tree = (Tree) iter.next();
        if (!tree.isPrePreTerminal()) {
          continue;
        }
        if (Trees.leftEdge(tree, root2) == Trees.leftEdge(preTerm, root1) && Trees.rightEdge(tree, root2) == Trees.rightEdge(preTerm, root1)) {
          brackets.add(new WordCatConstituent(preTerm, root1, WordCatConstituent.goodWordTagType));
          break;
        }
      }
    }

    return brackets;
  }
}
