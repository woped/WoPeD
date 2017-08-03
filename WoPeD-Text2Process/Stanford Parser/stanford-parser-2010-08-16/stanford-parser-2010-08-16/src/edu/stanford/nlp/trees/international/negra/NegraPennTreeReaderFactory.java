package edu.stanford.nlp.trees.international.negra;

import java.io.Reader;
import java.io.Serializable;

import edu.stanford.nlp.ling.StringLabelFactory;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.TreeReader;
import edu.stanford.nlp.trees.TreeReaderFactory;
import edu.stanford.nlp.trees.TreebankLanguagePack;


/** A TreeReaderFactory for the Negra and Tiger treebanks in their
 *  Penn Treebank compatible export format.
 *
 *  @author Roger Levy
 */
public class NegraPennTreeReaderFactory implements TreeReaderFactory, Serializable {

  int nodeCleanup; // = 0;
  TreebankLanguagePack tlp;
  boolean treeNormalizerInsertNPinPP; // = false;


  public NegraPennTreeReaderFactory(TreebankLanguagePack tlp) {
    this(0, false, false, tlp);
  }


  public NegraPennTreeReaderFactory(int nodeCleanup, boolean treeNormalizerInsertNPinPP,
                                    boolean treeNormalizerLeaveGF, TreebankLanguagePack tlp) {
    this.nodeCleanup = nodeCleanup;
    this.treeNormalizerInsertNPinPP = treeNormalizerInsertNPinPP;
    this.tlp = tlp;
    // System.out.println("Node cleanup = " + nodeCleanup);
  }

  public TreeReader newTreeReader(Reader in) {
    final NegraPennTreeNormalizer tn = new NegraPennTreeNormalizer(tlp, nodeCleanup);
    if (treeNormalizerInsertNPinPP) {
      tn.setInsertNPinPP(true);
    }

    return new PennTreeReader(in, new LabeledScoredTreeFactory(new StringLabelFactory()), tn, new NegraPennTokenizer(in));
  }

  private static final long serialVersionUID = 1L;

} // end class NegraPennTreeReaderFactory

