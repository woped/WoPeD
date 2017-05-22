package edu.stanford.nlp.trees.international.negra;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeFactory;
import edu.stanford.nlp.trees.TreeNormalizer;
import edu.stanford.nlp.trees.TreebankLanguagePack;

import java.util.Arrays;
import java.util.List;


/**
 * Tree normalizer for Negra Penn Treebank format.
 *
 * @author Roger Levy
 */
public class NegraPennTreeNormalizer extends TreeNormalizer {
  /** How to clean up node labels: 0 = do nothing, 1 = keep category and
   *  function, 2 = just category
   */
  private final int nodeCleanup;
  private final String root; //= "ROOT";
  private static final String nonUnaryRoot = "NUR"; // non-unary root

  public String rootSymbol() {
    return root;
  }

  public String nonUnaryRootSymbol() {
    return nonUnaryRoot;
  }


  protected final TreebankLanguagePack tlp;

  private boolean leaveGF = false;
  private boolean insertNPinPP = false;

  public void setLeaveGF(boolean b) {
    leaveGF = b;
  }

  public boolean getLeaveGF() {
    return leaveGF;
  }

  public void setInsertNPinPP(boolean b) {
    insertNPinPP = b;
  }

  public boolean getInsertNPinPP() {
    return insertNPinPP;
  }


  public NegraPennTreeNormalizer() {
    this(new NegraPennLanguagePack());
  }

  public NegraPennTreeNormalizer(TreebankLanguagePack tlp) {
    this(tlp, 0);
  }

  public NegraPennTreeNormalizer(TreebankLanguagePack tlp, int nodeCleanup) {
    this.tlp = tlp;
    this.nodeCleanup = nodeCleanup;
    this.root = tlp.startSymbol();
  }


  /**
   * Normalizes a leaf contents.
   * This implementation interns the leaf.
   */
  @Override
  public String normalizeTerminal(String leaf) {
    // We could unquote * and / with backslash \ in front of them
    return leaf.intern();
  }


  private static final String junkCPP = "---CJ";
  private static final String cpp = "CPP";

  /**
   * Normalizes a nonterminal contents.
   * This implementation strips functional tags, etc. and interns the
   * nonterminal.
   */
  @Override
  public String normalizeNonterminal(String category) {
    if (junkCPP.equals(category)) // one garbage category cleanup here.
    {
      category = cpp;
    }
    return cleanUpLabel(category).intern();
  }


  /**
   * Normalize a whole tree -- one can assume that this is the
   * root.  This implementation deletes empty elements (ones with
   * nonterminal tag label starting with '*T') from the tree.  It
   * does work for a null tree.
   * <p/>
   * The NegraPennTreeNormalizer also changes the Label to a
   * NegraLabel and notes the functional marking as part of the
   * NegraLabel.
   */
  @Override
  public Tree normalizeWholeTree(Tree tree, TreeFactory tf) {
    // System.out.println("normalizeWholeTree input");
    // tree.pennPrint();

    // add an extra root to non-unary roots
    if (tree.label().value().equals(root) && tree.children().length > 1) {
      //System.out.println("Fixing tree with non-unary root.  Before...");
      //tree.pennPrint();
      Tree underRoot = tree.treeFactory().newTreeNode(nonUnaryRoot, tree.getChildrenAsList());
      tree.setChildren(new Tree[1]);
      tree.children()[0] = underRoot;
      //System.out.println("After...");
      //tree.pennPrint();
    }

    // insert NPs in PPs if you're supposed to do that
    if (insertNPinPP) {
      insertNPinPPall(tree);
    }

    for (Tree t : tree) {
      if (! leaveGF) {
        // make the Label a NegraLabel, and move functional annotation to it
        // terminals don't have functional annotation
        if (t.isLeaf()) {
          continue;
        }

        // there's also a '--' category
        if (t.label().value().matches("--.*")) {
          continue;
        }

        String[] catEdge = {t.label().value()};
        //System.out.println(t.label().value() + " " + catEdge.length);
        String label = catEdge.length > 0 ? catEdge[0] : "-";
        NegraLabel l = new NegraLabel(label);

        if (catEdge.length > 1) //if there is a functional label, use it
        {
          l.setEdge(catEdge[1]);
        }
        t.setLabel(l);
      }

      // fix a bug in the ACL08 German tiger treebank
      String cat = t.label().value();
      if (cat == null || cat.equals("")) {
        if (t.numChildren() == 3 && t.firstChild().label().value().equals("NN") && t.getChild(1).label().value().equals("$.")) {
          System.err.println("Correcting treebank error: giving phrase label DL to " + t);
          t.label().setValue("DL");
        }
      }
    }

    // now prune empties and A over A
    Tree fixed = tree.prune(new edu.stanford.nlp.util.Filter<Tree>() {

      private static final long serialVersionUID = -606371737889816130L;

      public boolean accept(Tree t) {
        Tree[] kids = t.children();
        Label l = t.label();
        if ((l != null) && l.value() != null && (l.value().matches("^\\*T.*$")) && !t.isLeaf() && kids.length == 1 && kids[0].isLeaf()) {
          // Delete empty/trace nodes (ones marked '*T-x*')
          return false;
        }
        return true;
      }
    }, tf).spliceOut(new edu.stanford.nlp.util.Filter<Tree>() {

      private static final long serialVersionUID = -606371737889816130L;

      public boolean accept(Tree t) {
        if (t.isLeaf() || t.isPreTerminal() || t.children().length != 1) {
          return true;
        }
        if (t.label() != null && t.label().equals(t.children()[0].label())) {
          return false;
        }
        return true;
      }
    }, tf);
    // System.out.println("normalizeWholeTree output");
    // fixed.pennPrint();
    return fixed;
  }


  private List prepositionTags = Arrays.asList(new String[]{"APPR", "APPRART"});
  private List postpositionTags = Arrays.asList(new String[]{"APPO", "APZR"});


  private void insertNPinPPall(Tree t) {
    Tree[] kids = t.children();
    for (int i = 0, n = kids.length; i < n; i++) {
      insertNPinPPall(kids[i]);
    }
    insertNPinPP(t);
  }


  private void insertNPinPP(Tree t) {
    if (tlp.basicCategory(t.label().value()).equals("PP")) {
      Tree[] kids = t.children();
      int i = 0;
      int j = kids.length - 1;
      while (i < j && prepositionTags.contains(tlp.basicCategory(kids[i].label().value()))) {
        i++;
      } // i now indexes first dtr of new NP
      while (i < j && postpositionTags.contains(tlp.basicCategory(kids[j].label().value()))) {
        j--;
      } // j now indexes last dtr of new NP

      if (i > j) {
        System.err.println("##### Warning -- no NP material here!");
        return; // there is no NP material!
      }

      int npKidsLength = j - i + 1;
      Tree[] npKids = new Tree[npKidsLength];
      System.arraycopy(kids, i, npKids, 0, npKidsLength);
      Tree np = t.treeFactory().newTreeNode(t.label().labelFactory().newLabel("NP"), Arrays.asList(npKids));
      Tree[] newPPkids = new Tree[kids.length - npKidsLength + 1];
      System.arraycopy(kids, 0, newPPkids, 0, i + 1);
      newPPkids[i] = np;
      System.arraycopy(kids, j + 1, newPPkids, i + 1, kids.length - j - 1);
      t.setChildren(newPPkids);
      System.out.println("#### inserted NP in PP");
      t.pennPrint();
    }
  }


  /**
   * Remove things like hyphened functional tags and equals from the
   * end of a node label.
   */
  protected String cleanUpLabel(String label) {
    if (label == null) {
      label = root;
      // String constants are always interned
    } else if (nodeCleanup == 1) {
      return tlp.categoryAndFunction(label);
    } else if (nodeCleanup == 2) {
      return tlp.basicCategory(label);
    } else {
      return label;
    }
    return label;
  }

  private static final long serialVersionUID = 8529514903815041064L;

}
