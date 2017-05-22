package edu.stanford.nlp.parser.lexparser;

import java.util.*;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.math.SloppyMath;
import edu.stanford.nlp.parser.KBestViterbiParser;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeFactory;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.util.*;


/** Implements Eisner and Satta style algorithms for bilexical
 *  PCFG parsing.  The basic class provides O(n<sup>4</sup>)
 *  parsing, with the passed in PCFG and dependency parsers
 *  providing outside scores in an efficient A* search.
 *
 *  @author Dan Klein
 */
public class BiLexPCFGParser implements KBestViterbiParser {

  protected static final boolean VERBOSE = false;
  protected static final boolean VERY_VERBOSE = false;

  protected HookChart chart;
  protected Heap<Item> agenda;
  protected int length;
  protected int[] words;
  protected Edge goal;
  protected Interner interner;
  protected Scorer scorer;
  protected ExhaustivePCFGParser fscorer;
  protected ExhaustiveDependencyParser dparser;
  protected GrammarProjection projection;
  //pair dep scores

  protected BinaryGrammar bg;
  protected UnaryGrammar ug;
  protected DependencyGrammar dg;
  protected Lexicon lex;
  protected Options op;
  protected List<IntTaggedWord>[] taggedWordList;

  protected Numberer wordNumberer = Numberer.getGlobalNumberer("words");
  protected Numberer tagNumberer = Numberer.getGlobalNumberer("tags");
  protected Numberer stateNumberer = Numberer.getGlobalNumberer("states");

  protected TreeFactory tf = new LabeledScoredTreeFactory();

  // temp
  protected long relaxHook1 = 0;
  protected long relaxHook2 = 0;
  protected long relaxHook3 = 0;
  protected long relaxHook4 = 0;

  protected long builtHooks = 0;
  protected long builtEdges = 0;
  protected long extractedHooks = 0;
  protected long extractedEdges = 0;


  private static final double TOL = 1e-10;

  protected static boolean better(double x, double y) {
    return ((x - y) / (Math.abs(x) + Math.abs(y) + 1e-100) > TOL);
  }


  public double getBestScore() {
    if (goal == null) {
      return Double.NEGATIVE_INFINITY;
    } else {
      return goal.score();
    }
  }


  protected Tree extractParse(Edge edge) {
    String head = (String) wordNumberer.object(words[edge.head]);
    String tag = (String) tagNumberer.object(edge.tag);
    String state = (String) stateNumberer.object(edge.state);
    Label label = new CategoryWordTag(state, head, tag);
    if (edge.backEdge == null && edge.backHook == null) {
      // leaf, but needs word terminal
      List<Tree> childList = Collections.singletonList(tf.newLeaf(new StringLabel(head)));
      return tf.newTreeNode(label, childList);
    }
    if (edge.backHook == null) {
      // unary
      List<Tree> childList = Collections.singletonList(extractParse(edge.backEdge));
      return tf.newTreeNode(label, childList);
    }
    // binary
    List<Tree> children = new ArrayList<Tree>();
    if (edge.backHook.isPreHook()) {
      children.add(extractParse(edge.backEdge));
      children.add(extractParse(edge.backHook.backEdge));
    } else {
      children.add(extractParse(edge.backHook.backEdge));
      children.add(extractParse(edge.backEdge));
    }
    return tf.newTreeNode(label, children);
  }

  /**
   * Return the best parse of the sentence most recently parsed.
   *
   * @return The best (highest score) tree
   */
  public Tree getBestParse() {
    return extractParse(goal);
  }


  public boolean hasParse() {
    return goal != null && goal.iScore != Double.NEGATIVE_INFINITY;
  }


  // Added by Dan Zeman to store the list of N best trees.
  protected List<Edge> nGoodTrees = new LinkedList<Edge>();



  /**
   * Return the list of k "good" parses of the sentence most recently parsed.
   * (The first is guaranteed to be the best, but later ones are only
   * guaranteed the best subject to the possibilities that disappear because
   * the PCFG/Dep charts only store the best over each span.)
   *
   * @return The list of k best trees
   */
  public List<ScoredObject<Tree>> getKGoodParses(int k) {
    List<ScoredObject<Tree>> nGoodTreesList = new ArrayList<ScoredObject<Tree>>(Test.printFactoredKGood);
    for (Edge e : nGoodTrees) {
      nGoodTreesList.add(new ScoredObject<Tree>(extractParse(e), e.iScore));
    }
    return nGoodTreesList;
  }

  /** Get the exact k best parses for the sentence.
   *
   *  @param k The number of best parses to return
   *  @return The exact k best parses for the sentence, with
   *         each accompanied by its score (typically a
   *         negative log probability).
   */
  public List<ScoredObject<Tree>> getKBestParses(int k) {
    throw new UnsupportedOperationException("BiLexPCFGParser doesn't support k best parses");
  }


  /** Get a complete set of the maximally scoring parses for a sentence,
   *  rather than one chosen at random.  This set may be of size 1 or larger.
   *
   *  @return All the equal best parses for a sentence, with each
   *         accompanied by its score
   */
  public List<ScoredObject<Tree>> getBestParses() {
    throw new UnsupportedOperationException("BiLexPCFGParser doesn't support best parses");
  }

  /** Get k parse samples for the sentence.  It is expected that the
   *  parses are sampled based on their relative probability.
   *
   *  @param k The number of sampled parses to return
   *  @return A list of k parse samples for the sentence, with
   *         each accompanied by its score
   */
  public List<ScoredObject<Tree>> getKSampledParses(int k) {
    throw new UnsupportedOperationException("BiLexPCFGParser doesn't support k sampled parses");
  }


  protected Edge tempEdge = new Edge();

  protected void combine(Edge edge, Hook hook) {
    if (VERBOSE) {
      System.err.println("Combining: " + edge + " and " + hook);
    }
    // make result edge
    if (hook.isPreHook()) {
      tempEdge.start = edge.start;
      tempEdge.end = hook.end;
    } else {
      tempEdge.start = hook.start;
      tempEdge.end = edge.end;
    }
    tempEdge.state = hook.state;
    tempEdge.head = hook.head;
    tempEdge.tag = hook.tag;
    tempEdge.iScore = hook.iScore + edge.iScore;
    tempEdge.backEdge = edge;
    tempEdge.backHook = hook;
    relaxTempEdge();
  }

  protected void relaxTempEdge() {
    // if (tempEdge.iScore > scorer.iScore(tempEdge)+1e-4) {
    //   System.err.println(tempEdge+" has i "+tempEdge.iScore+" iE "+scorer.iScore(tempEdge));
    // }
    Edge resultEdge = (Edge) interner.intern(tempEdge);
    if (VERBOSE) {
      System.err.printf("Formed %s %s %.2f was %.2f better? %b\n", (resultEdge == tempEdge ? "new" : "pre-existing"), resultEdge, tempEdge.iScore, resultEdge.iScore, better(tempEdge.iScore, resultEdge.iScore));
    }
    if (resultEdge == tempEdge) {
      tempEdge = new Edge();
      discoverEdge(resultEdge);
    } else {
      if (better(tempEdge.iScore, resultEdge.iScore) && resultEdge.oScore > Double.NEGATIVE_INFINITY) {
        // we've found a better way of making an edge that may make a parse
        double back = resultEdge.iScore;
        Edge backE = resultEdge.backEdge;
        Hook backH = resultEdge.backHook;
        resultEdge.iScore = tempEdge.iScore;
        resultEdge.backEdge = tempEdge.backEdge;
        resultEdge.backHook = tempEdge.backHook;
        try {
          agenda.decreaseKey(resultEdge);
        } catch (NullPointerException e) {
          if (false) {
            System.err.println("");
            System.err.println("Old backEdge: " + backE + " i " + backE.iScore + " o " + backE.oScore + " s " + backE.score());
            System.err.println("Old backEdge: " + backE + " iE " + scorer.iScore(backE));
            System.err.println("Old backHook: " + backH + " i " + backH.iScore + " o " + backH.oScore + " s " + backH.score());
            System.err.println("New backEdge: " + tempEdge.backEdge + " i " + tempEdge.backEdge.iScore + " o " + tempEdge.backEdge.oScore + " s " + tempEdge.backEdge.score());
            System.err.println("New backEdge: " + tempEdge.backEdge + " iE " + scorer.iScore(tempEdge.backEdge));
            System.err.println("New backHook: " + tempEdge.backHook + " i " + tempEdge.backHook.iScore + " o " + tempEdge.backHook.oScore + " s " + tempEdge.backHook.score());
            System.err.println("ERROR: Formed " + resultEdge + " i " + tempEdge.iScore + " o " + resultEdge.oScore + " s " + resultEdge.score());
            System.err.println("ERROR: Formed " + resultEdge + " " + (resultEdge == tempEdge ? "new" : "old") + " " + tempEdge.iScore + " was " + back + " better? " + better(tempEdge.iScore, back));
          }
        }
      }
    }
  }

  protected void discoverEdge(Edge edge) {
    // create new edge
    edge.oScore = scorer.oScore(edge);
    agenda.add(edge);
    builtEdges++;
  }

  protected void discoverHook(Hook hook) {
    hook.oScore = buildOScore(hook);
    if (hook.oScore == Double.NEGATIVE_INFINITY) {
      relaxHook4++;
    }
    builtHooks++;
    agenda.add(hook);
  }

  protected Edge iTemp = new Edge();
  protected Edge oTemp = new Edge();

  protected double buildOScore(Hook hook) {
    double bestOScore = Double.NEGATIVE_INFINITY;
    iTemp.head = hook.head;
    iTemp.tag = hook.tag;
    iTemp.state = hook.subState;
    oTemp.head = hook.head;
    oTemp.tag = hook.tag;
    oTemp.state = hook.state;
    if (hook.isPreHook()) {
      iTemp.end = hook.start;
      oTemp.end = hook.end;
      for (int start = 0; start <= hook.head; start++) {
        iTemp.start = start;
        oTemp.start = start;
        double oScore = scorer.oScore(oTemp) + scorer.iScore(iTemp);
        //System.err.println("Score for "+hook+" is i "+iTemp+" ("+scorer.iScore(iTemp)+") o "+oTemp+" ("+scorer.oScore(oTemp)+")");
        bestOScore = SloppyMath.max(bestOScore, oScore);
      }
    } else {
      iTemp.start = hook.end;
      oTemp.start = hook.start;
      for (int end = hook.head + 1; end <= length; end++) {
        iTemp.end = end;
        oTemp.end = end;
        double oScore = scorer.oScore(oTemp) + scorer.iScore(iTemp);
        bestOScore = SloppyMath.max(bestOScore, oScore);
      }
    }
    return bestOScore;
  }

  protected Hook tempHook = new Hook();

  protected void projectHooks(Edge edge) {
    // form hooks
    // POST HOOKS
    //for (Iterator rI = bg.ruleIteratorByLeftChild(edge.state);
    //      rI.hasNext(); ) {
    List<BinaryRule> ruleList = bg.ruleListByLeftChild(edge.state);
    for (int r = 0, rsz = ruleList.size(); r < rsz; r++) {
      //BinaryRule br = rI.next();
      BinaryRule br = ruleList.get(r);
      if (!fscorer.oPossibleL(project(br.parent), edge.start) || !fscorer.iPossibleL(project(br.rightChild), edge.end)) {
        if (!Test.exhaustiveTest) {
          continue;
        }
      }
      for (int head = edge.end; head < length; head++) {
        // cdm Apr 2006: avoid Iterator allocation
        // for (Iterator iTWI = taggedWordList[head].iterator(); iTWI.hasNext();) {
        // IntTaggedWord iTW = (IntTaggedWord) iTWI.next();
        for (int hdi = 0, sz = taggedWordList[head].size(); hdi < sz; hdi++) {
          IntTaggedWord iTW = taggedWordList[head].get(hdi);
          int tag = iTW.tag;
          tempHook.start = edge.start;
          tempHook.end = edge.end;
          tempHook.head = head;
          tempHook.tag = tag;
          tempHook.state = br.parent;
          tempHook.subState = br.rightChild;
          if (!chart.isBuiltL(tempHook.subState, tempHook.end, tempHook.head, tempHook.tag)) {
            continue;
          }
          tempHook.iScore = edge.iScore + br.score + dparser.headScore[dparser.binDistance[head][edge.end]][head][dg.tagBin(tag)][edge.head][dg.tagBin(edge.tag)] + dparser.headStop[edge.head][dg.tagBin(edge.tag)][edge.start] + dparser.headStop[edge.head][dg.tagBin(edge.tag)][edge.end];
          tempHook.backEdge = edge;
          relaxTempHook();
        }
      }
    }
    // PRE HOOKS
    //for (Iterator<BinaryRule> rI = bg.ruleIteratorByRightChild(edge.state);
    //     rI.hasNext(); ) {
    ruleList = bg.ruleListByRightChild(edge.state);
    for (int r = 0, rlSize = ruleList.size(); r < rlSize; r++) {
      //BinaryRule br = rI.next();
      BinaryRule br = ruleList.get(r);
      if (!fscorer.oPossibleR(project(br.parent), edge.end) || !fscorer.iPossibleR(project(br.leftChild), edge.start)) {
        if (!Test.exhaustiveTest) {
          continue;
        }
      }
      for (int head = 0; head < edge.start; head++) {
        // cdm Apr 2006: avoid Iterator allocation
        // for (Iterator iTWI = taggedWordList[head].iterator(); iTWI.hasNext();) {
        //IntTaggedWord iTW = (IntTaggedWord) iTWI.next();
        for (int hdi = 0, sz = taggedWordList[head].size(); hdi < sz; hdi++) {
          IntTaggedWord iTW = taggedWordList[head].get(hdi);
          int tag = iTW.tag;
          tempHook.start = edge.start;
          tempHook.end = edge.end;
          tempHook.head = head;
          tempHook.tag = tag;
          tempHook.state = br.parent;
          tempHook.subState = br.leftChild;
          if (!chart.isBuiltR(tempHook.subState, tempHook.start, tempHook.head, tempHook.tag)) {
            continue;
          }
          tempHook.iScore = edge.iScore + br.score + dparser.headScore[dparser.binDistance[head][edge.start]][head][dg.tagBin(tag)][edge.head][dg.tagBin(edge.tag)] + dparser.headStop[edge.head][dg.tagBin(edge.tag)][edge.start] + dparser.headStop[edge.head][dg.tagBin(edge.tag)][edge.end];
          tempHook.backEdge = edge;
          relaxTempHook();
        }
      }
    }
  }

  protected void registerReal(Edge real) {
    chart.registerRealEdge(real);
  }

  protected void triggerHooks(Edge edge) {
    // we might have built a synth edge, enabling some old real edges to project hooks (the difference between this method and triggerAllHooks is that here we look only at realEdges)
    boolean newL = !chart.isBuiltL(edge.state, edge.start, edge.head, edge.tag);
    boolean newR = !chart.isBuiltR(edge.state, edge.end, edge.head, edge.tag);
    if (VERY_VERBOSE) {
      if (newL) {
        System.err.println("Triggering on L: " + edge);
      }
      if (newR) {
        System.err.println("Triggering on R: " + edge);
      }
    }
    chart.registerEdgeIndexes(edge);
    if (newR) {
      // PRE HOOKS
      BinaryRule[] rules = bg.splitRulesWithLC(edge.state);
      for (BinaryRule br : rules) {
        Collection<Edge> realEdges = chart.getRealEdgesWithL(br.rightChild, edge.end);
        for (Edge real : realEdges) {
          tempHook.start = real.start;
          tempHook.end = real.end;
          tempHook.state = br.parent;
          tempHook.subState = br.leftChild;
          tempHook.head = edge.head;
          tempHook.tag = edge.tag;
          tempHook.backEdge = real;
          tempHook.iScore = real.iScore + br.score + dparser.headScore[dparser.binDistance[edge.head][edge.end]][edge.head][dg.tagBin(edge.tag)][real.head][dg.tagBin(real.tag)] + dparser.headStop[real.head][dg.tagBin(real.tag)][real.start] + dparser.headStop[real.head][dg.tagBin(real.tag)][real.end];
          relaxTempHook();
        }
      }
    }
    if (newL) {
      // POST HOOKS
      BinaryRule[] rules = bg.splitRulesWithRC(edge.state);
      for (BinaryRule br : rules) {
        Collection<Edge> realEdges = chart.getRealEdgesWithR(br.leftChild, edge.start);
        for (Edge real : realEdges) {
          tempHook.start = real.start;
          tempHook.end = real.end;
          tempHook.state = br.parent;
          tempHook.subState = br.rightChild;
          tempHook.head = edge.head;
          tempHook.tag = edge.tag;
          tempHook.backEdge = real;
          tempHook.iScore = real.iScore + br.score + dparser.headScore[dparser.binDistance[edge.head][edge.start]][edge.head][dg.tagBin(edge.tag)][real.head][dg.tagBin(real.tag)] + dparser.headStop[real.head][dg.tagBin(real.tag)][real.start] + dparser.headStop[real.head][dg.tagBin(real.tag)][real.end];
          relaxTempHook();
        }
      }
    }
  }

  protected void triggerAllHooks(Edge edge) {
    // we might have built a new edge, enabling some old edges to project hooks
    boolean newL = !chart.isBuiltL(edge.state, edge.start, edge.head, edge.tag);
    boolean newR = !chart.isBuiltR(edge.state, edge.end, edge.head, edge.tag);
    if (VERY_VERBOSE) {
      if (newL) {
        System.err.println("Triggering on L: " + edge);
      }
      if (newR) {
        System.err.println("Triggering on R: " + edge);
      }
    }
    chart.registerEdgeIndexes(edge);
    if (newR) {
      // PRE HOOKS
      for (Iterator<BinaryRule> rI = bg.ruleIteratorByLeftChild(edge.state); rI.hasNext();) {
        BinaryRule br = rI.next();
        Collection<Edge> edges = chart.getRealEdgesWithL(br.rightChild, edge.end);
        for (Edge real : edges) {
          tempHook.start = real.start;
          tempHook.end = real.end;
          tempHook.state = br.parent;
          tempHook.subState = br.leftChild;
          tempHook.head = edge.head;
          tempHook.tag = edge.tag;
          tempHook.backEdge = real;
          tempHook.iScore = real.iScore + br.score + dparser.headScore[dparser.binDistance[edge.head][edge.end]][edge.head][dg.tagBin(edge.tag)][real.head][dg.tagBin(real.tag)] + dparser.headStop[real.head][dg.tagBin(real.tag)][real.start] + dparser.headStop[real.head][dg.tagBin(real.tag)][real.end];
          relaxTempHook();
        }
      }
    }
    if (newL) {
      // POST HOOKS
      for (Iterator rI = bg.ruleIteratorByRightChild(edge.state); rI.hasNext();) {
        BinaryRule br = (BinaryRule) rI.next();
        Collection<Edge> edges = chart.getRealEdgesWithR(br.leftChild, edge.start);
        if (VERBOSE) {
          System.err.println("Looking for: " + Numberer.getGlobalNumberer("states").object(br.leftChild) + " ending at " + edge.start);
          System.err.println("Found: " + edges);
        }
        for (Edge real : edges) {
          tempHook.start = real.start;
          tempHook.end = real.end;
          tempHook.state = br.parent;
          tempHook.subState = br.rightChild;
          tempHook.head = edge.head;
          tempHook.tag = edge.tag;
          tempHook.backEdge = real;
          tempHook.iScore = real.iScore + br.score + dparser.headScore[dparser.binDistance[edge.head][edge.start]][edge.head][dg.tagBin(edge.tag)][real.head][dg.tagBin(real.tag)] + dparser.headStop[real.head][dg.tagBin(real.tag)][real.start] + dparser.headStop[real.head][dg.tagBin(real.tag)][real.end];
          relaxTempHook();
        }
      }
    }
  }


  protected void relaxTempHook() {
    relaxHook1++;
    if (VERBOSE) {
      System.err.println("Considering: " + tempHook + " iP: " + scorer.iPossible(tempHook) + " oP: " + scorer.oPossible(tempHook));
    }
    if (!Test.exhaustiveTest) {
      if (!scorer.oPossible(tempHook) || !scorer.iPossible(tempHook)) {
        return;
      }
    }
    relaxHook2++;
    Hook resultHook = (Hook) interner.intern(tempHook);
    if (VERBOSE) {
      System.err.printf("Formed %s %s %f was %f\n", resultHook, (resultHook == tempHook ? "new" : "old"), tempHook.iScore, resultHook.iScore);
      if (resultHook.backEdge != null) {
        System.err.println("  Backtrace: " + resultHook.backEdge);
      }
    }
    if (resultHook == tempHook) {
      relaxHook3++;
      tempHook = new Hook();
      discoverHook(resultHook);
    }
    if (better(tempHook.iScore, resultHook.iScore)) {
      resultHook.iScore = tempHook.iScore;
      resultHook.backEdge = tempHook.backEdge;
      try {
        agenda.decreaseKey(resultHook);
      } catch (NullPointerException e) {
      }
    }
  }

  protected void projectUnaries(Edge edge) {
    for (Iterator rI = ug.ruleIteratorByChild(edge.state); rI.hasNext();) {
      UnaryRule ur = (UnaryRule) rI.next();
      if (ur.child == ur.parent) {
        continue;
      }
      tempEdge.start = edge.start;
      tempEdge.end = edge.end;
      tempEdge.head = edge.head;
      tempEdge.tag = edge.tag;
      tempEdge.state = ur.parent;
      tempEdge.backEdge = edge;
      tempEdge.backHook = null;
      tempEdge.iScore = edge.iScore + ur.score;
      relaxTempEdge();
    }
  }

  protected void processEdge(Edge edge) {
    // add to chart
    if (VERBOSE) {
      System.err.println("Adding to chart: " + edge);
    }
    chart.addEdge(edge);
    // fetch existing hooks that can combine with it and combine them
    for (Hook hook : chart.getPreHooks(edge)) {
      combine(edge, hook);
    }
    for (Hook hook : chart.getPostHooks(edge)) {
      combine(edge, hook);
    }
    // do projections
    //if (VERBOSE) System.err.println("Projecting: "+edge);
    projectUnaries(edge);
    if (!bg.isSynthetic(edge.state) && !op.freeDependencies) {
      projectHooks(edge);
      registerReal(edge);
    }
    if (op.freeDependencies) {
      projectHooks(edge);
      registerReal(edge);
      triggerAllHooks(edge);
    } else {
      triggerHooks(edge);
    }
  }

  protected void processHook(Hook hook) {
    // add to chart
    //if (VERBOSE) System.err.println("Adding to chart: "+hook);
    chart.addHook(hook);
    Collection<Edge> edges = chart.getEdges(hook);
    for (Edge edge : edges) {
      combine(edge, hook);
    }
  }

  protected void processItem(Item item) {
    if (item.isEdge()) {
      processEdge((Edge) item);
    } else {
      processHook((Hook) item);
    }
  }

  protected void discoverItem(Item item) {
    if (item.isEdge()) {
      discoverEdge((Edge) item);
    } else {
      discoverHook((Hook) item);
    }
  }

  protected static Item makeInitialItem(int pos, int tag, int state, double iScore) {
    Edge edge = new Edge();
    edge.start = pos;
    edge.end = pos + 1;
    edge.state = state;
    edge.head = pos;
    edge.tag = tag;
    edge.iScore = iScore;
    return edge;
  }

  protected List<Item> makeInitialItems(List wordList) {
    List<Item> itemList = new ArrayList<Item>();
    int length = wordList.size();
    int numTags = tagNumberer.total();
    words = new int[length];
    taggedWordList = new List[length];
    int terminalCount = 0;
    for (int i = 0; i < length; i++) {
      taggedWordList[i] = new ArrayList<IntTaggedWord>(numTags);
      String wordStr = "";
      Object wordObject = wordList.get(i);
      if (wordObject instanceof HasWord) {
        wordStr = ((HasWord) wordObject).word();
      } else {
        wordStr = wordObject.toString();
      }
      //String tagStr = "";
      //if (wordObject instanceof HasTag) {
      //  tagStr = ((HasTag)wordObject).tag();
      //}
      if (!wordNumberer.hasSeen(wordStr)) {
        wordStr = Lexicon.UNKNOWN_WORD;
      }
      int word = wordNumberer.number(wordStr);
      words[i] = word;
      for (Iterator<IntTaggedWord> tagI = lex.ruleIteratorByWord(word, i); tagI.hasNext(); ) {
        IntTaggedWord tagging = tagI.next();
        int tag = tagging.tag;
        //String curTagStr = (String)tagNumberer.object(tag);
        //if (!tagStr.equals("") && !tagStr.equals(curTagStr))
        //  continue;
        int state = Numberer.translate("tags", "states", tag);
        //itemList.add(makeInitialItem(i,tag,state,1.0*tagging.score));
        // THIS WILL CAUSE BUGS!!!  Don't use with another A* scorer
        tempEdge.state = state;
        tempEdge.head = i;
        tempEdge.start = i;
        tempEdge.end = i + 1;
        tempEdge.tag = tag;
        itemList.add(makeInitialItem(i, tag, state, scorer.iScore(tempEdge)));
        terminalCount++;
        taggedWordList[i].add(new IntTaggedWord(word, tag));
      }
    }
    if (Test.verbose) {
      System.err.println("Terminals (# of tag edges in chart): " +
                         terminalCount);
    }
    return itemList;
  }

  protected void scoreDependencies() {
    // just leach it off the dparser for now...
    /*
    IntDependency dependency = new IntDependency();
    for (int head = 0; head < words.length; head++) {
      for (int hTag = 0; hTag < tagNumberer.total(); hTag++) {
        for (int arg = 0; arg < words.length; arg++) {
          for (int aTag = 0; aTag < tagNumberer.total(); aTag++) {
            Arrays.fill(depScore[head][hTag][arg][aTag],Float.NEGATIVE_INFINITY);
          }
        }
      }
    }
    for (int head = 0; head < words.length; head++) {
      for (int arg = 0; arg < words.length; arg++) {
        if (head == arg)
          continue;
        for (Iterator<IntTaggedWord> headTWI=taggedWordList[head].iterator(); headTWI.hasNext();) {
          IntTaggedWord headTW = headTWI.next();
          for (Iterator<IntTaggedWord> argTWI=taggedWordList[arg].iterator(); argTWI.hasNext();) {
            IntTaggedWord argTW = argTWI.next();
            dependency.head = headTW;
            dependency.arg = argTW;
            dependency.leftHeaded = (head < arg);
            dependency.distance = Math.abs(head-arg);
            depScore[head][headTW.tag][arg][argTW.tag] =
              dg.score(dependency);
            if (false && depScore[head][headTW.tag][arg][argTW.tag] > -100)
              System.err.println(wordNumberer.object(headTW.word)+"/"+tagNumberer.object(headTW.tag)+" -> "+wordNumberer.object(argTW.word)+"/"+tagNumberer.object(argTW.tag)+" score "+depScore[head][headTW.tag][arg][argTW.tag]);
          }
        }
      }
    }
    */
  }

  protected void setGoal(int length) {
    goal = new Edge();
    goal.start = 0;
    goal.end = length;
    goal.state = stateNumberer.number(op.langpack().startSymbol());
    goal.tag = tagNumberer.number(Lexicon.BOUNDARY_TAG);
    goal.head = length - 1;
    //goal = (Edge)interner.intern(goal);
  }

  protected void initialize(List words) {
    length = words.size();
    interner = new Interner();
    agenda = new ArrayHeap<Item>(ScoredComparator.DESCENDING_COMPARATOR);
    chart = new HookChart();
    setGoal(length);
    List<Item> initialItems = makeInitialItems(words);
    scoreDependencies();
    for (int i = 0, iiSize = initialItems.size(); i < iiSize; i++) {
      Item item = initialItems.get(i);
      item = (Item) interner.intern(item);
      //if (VERBOSE) System.err.println("Initial: "+item);
      discoverItem(item);
    }
  }


  /**
   * Parse a Sentence.
   * This hasn't yet been implemented.  At present the goal is ignored.
   *
   * @return true iff it could be parsed
   */
  public boolean parse(List<? extends HasWord> sentence, String goal) {
    return parse(sentence);
  }


  /**
   * Parse a Sentence.
   *
   * @return true iff it could be parsed
   */
  public boolean parse(List<? extends HasWord> words) {
    int nGoodRemaining = 0;
    if (Test.printFactoredKGood > 0) {
      nGoodRemaining = Test.printFactoredKGood;
      nGoodTrees.clear();
    }

    int spanFound = 0;
    long last = 0;
    int exHook = 0;
    relaxHook1 = 0;
    relaxHook2 = 0;
    relaxHook3 = 0;
    relaxHook4 = 0;
    builtHooks = 0;
    builtEdges = 0;
    extractedHooks = 0;
    extractedEdges = 0;
    if (Test.verbose) {
      Timing.tick("Starting combined parse.");
    }
    dparser.binDistance = dparser.binDistance; // THIS IS TERRIBLE, BUT SAVES MEMORY
    initialize(words);
    while (!agenda.isEmpty()) {
      Item item = agenda.extractMin();
      if (!item.isEdge()) {
        exHook++;
        extractedHooks++;
      } else {
        extractedEdges++;
      }
      if (relaxHook1 > last + 1000000) {
        last = relaxHook1;
        if (Test.verbose) {
          System.err.println("Proposed hooks:   " + relaxHook1);
          System.err.println("Unfiltered hooks: " + relaxHook2);
          System.err.println("Built hooks:      " + relaxHook3);
          System.err.println("Waste hooks:      " + relaxHook4);
          System.err.println("Extracted hooks:  " + exHook);
        }
      }
      if (item.end - item.start > spanFound) {
        spanFound = item.end - item.start;
        if (Test.verbose) {
          System.err.print(spanFound + " ");
        }
      }
      //if (item.end < 5) System.err.println("Extracted: "+item+" iScore "+item.iScore+" oScore "+item.oScore+" score "+item.score());
      if (item.equals(goal)) {
        if (Test.verbose) {
          System.err.println("Found goal!");
          System.err.println("Comb iScore " + item.iScore); // was goal.iScore
          Timing.tick("Done, parse found.");
          System.err.println("Built items:      " + (builtEdges + builtHooks));
          System.err.println("Built hooks:      " + builtHooks);
          System.err.println("Built edges:      " + builtEdges);
          System.err.println("Extracted items:  " + (extractedEdges + extractedHooks));
          System.err.println("Extracted hooks:  " + extractedHooks);
          System.err.println("Extracted edges:  " + extractedEdges);
          //postMortem();
        }
        if (Test.printFactoredKGood <= 0) {
          goal = (Edge) item;
          interner = null;
          agenda = null;
          return true;
        } else {
          // Store the parse
          goal = (Edge) item;
          nGoodTrees.add(goal);
          nGoodRemaining--;
          if (nGoodRemaining > 0) {
            if (VERBOSE) {
              System.err.println("Found parse! Number of remaining trees to find = " + nGoodRemaining);
            }
          } else {
            if (VERBOSE) {
              System.err.println("Found last parse!");
            }
            interner = null;
            agenda = null;
            return true;
          }
        }
      }
      // Is the currently best item acceptable at all?
      if (item.score() == Double.NEGATIVE_INFINITY) {
        // Do not report failure in nGood mode if we found something earlier.
        if (nGoodTrees.size() > 0) {
          if (VERBOSE) {
            System.err.println("Aborting kGood search because of an unacceptable (-Inf) item: " + item);
          }
          goal = nGoodTrees.get(0);
          interner = null;
          agenda = null;
          return true;
        }
        System.err.println("FactoredParser: no consistent parse [hit A*-blocked edges, aborting].");
        if (Test.verbose) {
          Timing.tick("FactoredParser: no consistent parse [hit A*-blocked edges, aborting].");
        }
        return false;
      }
      // Keep the number of items from getting too large
      if (Test.MAX_ITEMS > 0 && (builtEdges + builtHooks) >= Test.MAX_ITEMS) {
        // Do not report failure in kGood mode if we found something earlier.
        if (nGoodTrees.size() > 0) {
          System.err.println("DEBUG: aborting search because of reaching the MAX_ITEMS work limit [" +
                             Test.MAX_ITEMS + " items]");
          goal = nGoodTrees.get(0);
          interner = null;
          agenda = null;
          return true;
        }
        System.err.println("FactoredParser: exceeded MAX_ITEMS work limit [" +
                           Test.MAX_ITEMS + " items]; aborting.");
        if (Test.verbose) {
          Timing.tick("FactoredParser: exceeded MAX_ITEMS work limit [" +
                      Test.MAX_ITEMS + " items]; aborting.");
        }
        return false;
      }
      if (VERBOSE && item.score() != Double.NEGATIVE_INFINITY) {
        System.err.printf("Removing from agenda: %s score i %.2f + o %.2f = %.2f\n", item, item.iScore, item.oScore, item.score());
        if (item.backEdge != null) {
          System.err.println("  Backtrace: " + item.backEdge.toString() + " " + (item.isEdge() ? (((Edge) item).backHook != null ? ((Edge) item).backHook.toString() : "") : ""));
        }
      }
      processItem(item);
    } // end while agenda is not empty
    // If we are here, the agenda is empty.
    // Do not report failure if we found something earlier.
    if (nGoodTrees.size() > 0) {
      System.err.println("DEBUG: aborting search because of empty agenda");
      goal = nGoodTrees.get(0);
      interner = null;
      agenda = null;
      return true;
    }
    System.err.println("FactoredParser: emptied agenda, no parse found!");
    if (Test.verbose) {
      Timing.tick("FactoredParser: emptied agenda, no parse found!");
    }
    return false;
  }


  protected void postMortem() {
    int numHooks = 0;
    int numEdges = 0;
    int numUnmatchedHooks = 0;
    int total = agenda.size();
    int done = 0;
    while (!agenda.isEmpty()) {
      Item item = agenda.extractMin();
      done++;
      //if(done % (total/10) == 0)
      //        System.err.println("Scanning: "+100*done/total);
      if (item.isEdge()) {
        numEdges++;
      } else {
        numHooks++;
        Collection edges = chart.getEdges((Hook) item);
        if (edges.size() == 0) {
          numUnmatchedHooks++;
        }
      }
    }
    System.err.println("--- Agenda Post-Mortem ---");
    System.err.println("Edges:           " + numEdges);
    System.err.println("Hooks:           " + numHooks);
    System.err.println("Unmatched Hooks: " + numUnmatchedHooks);
  }

  protected int project(int state) {
    return projection.project(state);
  }

  BiLexPCFGParser(Scorer scorer, ExhaustivePCFGParser fscorer, ExhaustiveDependencyParser dparser, BinaryGrammar bg, UnaryGrammar ug, DependencyGrammar dg, Lexicon lex, Options op) {
    this(scorer, fscorer, dparser, bg, ug, dg, lex, op, new NullGrammarProjection(bg, ug));
  }

  BiLexPCFGParser(Scorer scorer, ExhaustivePCFGParser fscorer, ExhaustiveDependencyParser dparser, BinaryGrammar bg, UnaryGrammar ug, DependencyGrammar dg, Lexicon lex, Options op, GrammarProjection projection) {
    this.fscorer = fscorer;
    this.projection = projection;
    this.dparser = dparser;
    this.scorer = scorer;
    this.bg = bg;
    this.ug = ug;
    this.dg = dg;
    this.lex = lex;
    this.op = op;
  }

  public static class N5BiLexPCFGParser extends BiLexPCFGParser {

    @Override
    protected void relaxTempHook() {
      relaxHook1++;
      if (VERBOSE) {
        System.err.println("Considering: " + tempHook + " iP: " + scorer.iPossible(tempHook) + " oP: " + scorer.oPossible(tempHook));
      }
      if (!Test.exhaustiveTest) {
        if (!scorer.oPossible(tempHook) || !scorer.iPossible(tempHook)) {
          return;
        }
      }
      relaxHook2++;
      Hook resultHook = tempHook;
      //Hook resultHook = (Hook)interner.intern(tempHook);
      if (VERBOSE) {
        System.err.println("Formed " + resultHook + " " + (resultHook == tempHook ? "new" : "old") + " " + tempHook.iScore + " was " + resultHook.iScore);
      }
      if (resultHook == tempHook) {
        relaxHook3++;
        tempHook = new Hook();
        processHook(resultHook);
        builtHooks++;
      }
    }

    N5BiLexPCFGParser(Scorer scorer, ExhaustivePCFGParser fscorer, ExhaustiveDependencyParser leach, BinaryGrammar bg, UnaryGrammar ug, DependencyGrammar dg, Lexicon lex, Options op) {
      super(scorer, fscorer, leach, bg, ug, dg, lex, op, new NullGrammarProjection(bg, ug));
    }

    N5BiLexPCFGParser(Scorer scorer, ExhaustivePCFGParser fscorer, ExhaustiveDependencyParser leach, BinaryGrammar bg, UnaryGrammar ug, DependencyGrammar dg, Lexicon lex, Options op, GrammarProjection proj) {
      super(scorer, fscorer, leach, bg, ug, dg, lex, op, proj);
    }

  } // end class N5BiLexPCFGParser

} // end class BiLexPCFGParser
