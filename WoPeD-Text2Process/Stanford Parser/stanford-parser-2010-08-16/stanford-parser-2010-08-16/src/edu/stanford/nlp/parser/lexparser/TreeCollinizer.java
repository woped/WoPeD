package edu.stanford.nlp.parser.lexparser;

import java.util.List;
import java.util.ArrayList;

import edu.stanford.nlp.trees.*;


/**
 * Does detransformations to a parsed sentence to map it back to the
 * standard treebank form for output or evaluation.
 * This version has Penn-Treebank-English-specific details, but can probably
 * be used without harm on other treebanks.
 * Returns labels to their basic category, removes punctuation (should be with
 * respect to a gold tree, but currently isn't), deletes the boundary symbol,
 * changes PRT labels to ADVP.
 * 
 * @author Dan Klein
 * @author Christopher Manning
 */
public class TreeCollinizer implements TreeTransformer {

  private TreebankLanguagePack tlp;
  private final boolean deletePunct;
  private final boolean fixCollinsBaseNP;

  public TreeCollinizer(TreebankLanguagePack tlp) {
    this(tlp, true, false);
  }

  public TreeCollinizer(TreebankLanguagePack tlp, boolean deletePunct,
                        boolean fixCollinsBaseNP) {
    this.tlp = tlp;
    this.deletePunct = deletePunct;
    this.fixCollinsBaseNP = fixCollinsBaseNP;
  }

  protected TreeFactory tf = new LabeledScoredTreeFactory();

  public Tree transformTree(Tree tree) {
    String s = tree.value();
    if (tree.isLeaf()) {
      return tf.newLeaf(tree.value());  // make it a StringLabel
    }
    s = tlp.basicCategory(s);
    if (deletePunct) {
      // this is broken as it's not the right thing to do when there
      // is any tag ambiguity -- and there is for ' (POS/'').  Sentences
      // can then have more or less words.  It's also unnecessary for EVALB,
      // since it ignores punctuation anyway (though in the same wrong way)
      if (tree.isPreTerminal() && tlp.isEvalBIgnoredPunctuationTag(s)) {
        return null;
      }
    }
    // remove the extra NPs inserted in the collinsBaseNP option
    if (fixCollinsBaseNP && s.equals("NP")) {
      Tree[] kids = tree.children();
      if (kids.length == 1 && tlp.basicCategory(kids[0].value()).equals("NP")) {
        return transformTree(kids[0]);
      }
    }
    // XXXX below isn't correct if root isn't unary.
    if (tlp.isStartSymbol(s)) {
      // NB: This deletes the boundary symbol, which is in the tree!
      return transformTree(tree.children()[0]);
    }
    // Magerman erased this decision, and everyone else has followed like sheep
    if (s.equals("PRT")) {
      s = "ADVP";
    }
    List<Tree> children = new ArrayList<Tree>();
    for (int cNum = 0, numKids = tree.numChildren(); cNum < numKids; cNum++) {
      Tree child = tree.children()[cNum];
      Tree newChild = transformTree(child);
      if (newChild != null) {
        children.add(newChild);
      }
    }
    if (children.size() == 0) {
      return null;
    }
    return tf.newTreeNode(s, children);
  }

} // end class TreeCollinizer
