package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.fsm.TransducerGraph;
import edu.stanford.nlp.fsm.TransducerGraph.Arc;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Distribution;
import edu.stanford.nlp.util.Numberer;
import edu.stanford.nlp.util.Pair;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Teg Grenager (grenager@cs.stanford.edu)
 */
public abstract class GrammarCompactor {

  // so that the grammar remembers its graphs after compacting them
  Set<TransducerGraph> compactedGraphs;

  public static final Object RAW_COUNTS = new Object();
  public static final Object NORMALIZED_LOG_PROBABILITIES = new Object();

  public Object outputType = RAW_COUNTS; // default value

  protected Numberer stateNumberer;
  protected Numberer newStateNumberer;
  protected String stateSpace;

  // String rawBaseDir = "raw";
  // String compactedBaseDir = "compacted";
  // boolean writeToFile = false;
  protected Distribution<String> inputPrior;
  private static Object END = "END";
  private static Object EPSILON = "EPSILON";
  protected boolean verbose = false;


  protected abstract TransducerGraph doCompaction(TransducerGraph graph, List<List<String>> trainPaths, List<List<String>> testPaths);

  public Pair<UnaryGrammar,BinaryGrammar> compactGrammar(Pair<UnaryGrammar,BinaryGrammar> grammar) {
    return compactGrammar(grammar, new HashMap<String, List<List<String>>>(), new HashMap<String, List<List<String>>>());
  }

  /**
   * Compacts the grammar specified by the Pair.
   *
   * @param grammar       a Pair of grammars, ordered UnaryGrammar BinaryGrammar.
   * @param allTrainPaths a Map from String passive constituents to Lists of paths
   * @param allTestPaths  a Map from String passive constituents to Lists of paths
   * @return a Pair of grammars, ordered UnaryGrammar BinaryGrammar.
   */
  public Pair<UnaryGrammar,BinaryGrammar> compactGrammar(Pair<UnaryGrammar,BinaryGrammar> grammar, Map<String, List<List<String>>> allTrainPaths, Map<String, List<List<String>>> allTestPaths) {
    inputPrior = computeInputPrior(allTrainPaths); // computed once for the whole grammar
    BinaryGrammar bg = grammar.second;
    stateSpace = bg.stateSpace();
    stateNumberer = Numberer.getGlobalNumberer(stateSpace);
    List<List<String>> trainPaths, testPaths;
    Set<UnaryRule> unaryRules = new HashSet<UnaryRule>();
    Set<BinaryRule> binaryRules = new HashSet<BinaryRule>();
    Map<String, TransducerGraph> graphs = convertGrammarToGraphs(grammar, unaryRules, binaryRules);
    compactedGraphs = new HashSet<TransducerGraph>();
    if (verbose) {
      System.out.println("There are " + graphs.size() + " categories to compact.");
    }
    int i = 0;
    for (Iterator<Entry<String, TransducerGraph>> graphIter = graphs.entrySet().iterator(); graphIter.hasNext();) {
      Map.Entry<String, TransducerGraph> entry = graphIter.next();
      String cat = entry.getKey();
      TransducerGraph graph = entry.getValue();
      if (verbose) {
        System.out.println("About to compact grammar for " + cat + " with numNodes=" + graph.getNodes().size());
      }
      trainPaths = allTrainPaths.remove(cat);// to save memory
      if (trainPaths == null) {
        trainPaths = new ArrayList<List<String>>();
      }
      testPaths = allTestPaths.remove(cat);// to save memory
      if (testPaths == null) {
        testPaths = new ArrayList<List<String>>();
      }
      TransducerGraph compactedGraph = doCompaction(graph, trainPaths, testPaths);
      i++;
      if (verbose) {
        System.out.println(i + ". Compacted grammar for " + cat + " from " + graph.getArcs().size() + " arcs to " + compactedGraph.getArcs().size() + " arcs.");
      }
      graphIter.remove(); // to save memory, remove the last thing
      compactedGraphs.add(compactedGraph);
    }
    return convertGraphsToGrammar(compactedGraphs, unaryRules, binaryRules);
  }

  protected Distribution<String> computeInputPrior(Map<String, List<List<String>>> allTrainPaths) {
    ClassicCounter<String> result = new ClassicCounter<String>();
    for (Iterator<List<List<String>>> catI = allTrainPaths.values().iterator(); catI.hasNext();) {
      List<List<String>> pathList = catI.next();
      for (List<String> path : pathList) {
        for (String input : path) {
          result.incrementCount(input);
        }
      }
    }
    return Distribution.laplaceSmoothedDistribution(result, result.size() * 2, 0.5);
  }

  private double smartNegate(double output) {
    if (outputType == NORMALIZED_LOG_PROBABILITIES) {
      return -output;
    }
    return output;
  }

  public static boolean writeFile(TransducerGraph graph, String dir, String name) {
    try {
      File baseDir = new File(dir);
      if (baseDir.exists()) {
        if (!baseDir.isDirectory()) {
          return false;
        }
      } else {
        if (!baseDir.mkdirs()) {
          return false;
        }
      }
      File file = new File(baseDir, name + ".dot");
      PrintWriter w;
      try {
        w = new PrintWriter(new FileWriter(file));
        String dotString = graph.asDOTString();
        w.print(dotString);
        w.flush();
        w.close();
      } catch (FileNotFoundException e) {
        System.err.println("Failed to open file in writeToDOTfile: " + file);
        return false;
      } catch (IOException e) {
        System.err.println("Failed to open file in writeToDOTfile: " + file);
        return false;
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   *
   */
  public Map<String, TransducerGraph> convertGrammarToGraphs(Pair<UnaryGrammar,BinaryGrammar> grammar, Set<UnaryRule> unaryRules, Set<BinaryRule> binaryRules) {
    int numRules = 0;
    UnaryGrammar ug = grammar.first;
    BinaryGrammar bg = grammar.second;
    Map<String, TransducerGraph> graphs = new HashMap<String, TransducerGraph>();
    // go through the BinaryGrammar and add everything
    for (BinaryRule rule : bg) {
      numRules++;
      boolean wasAdded = addOneBinaryRule(rule, graphs);
      if (!wasAdded)
      // add it for later, since we don't make graphs for these
      {
        binaryRules.add(rule);
      }
    }
    // now we need to use the UnaryGrammar to
    // add start and end Arcs to the graphs
    for (UnaryRule rule : ug) {
      numRules++;
      boolean wasAdded = addOneUnaryRule(rule, graphs);
      if (!wasAdded)
      // add it for later, since we don't make graphs for these
      {
        unaryRules.add(rule);
      }
    }
    if (verbose) {
      System.out.println("Number of raw rules: " + numRules);
      System.out.println("Number of raw states: " + stateNumberer.total());
    }
    return graphs;
  }

  protected TransducerGraph getGraphFromMap(Map<String, TransducerGraph> m, String o) {
    TransducerGraph graph = m.get(o);
    if (graph == null) {
      graph = new TransducerGraph();
      graph.setEndNode(o);
      m.put(o, graph);
    }
    return graph;
  }

  protected String getTopCategoryOfSyntheticState(String s) {
    if (s.charAt(0) != '@') {
      return null;
    }
    int bar = s.indexOf('|');
    if (bar < 0) {
      throw new RuntimeException("Grammar format error. Expected bar in state name: " + s);
    }
    String topcat = s.substring(1, bar);
    return topcat;
  }

  protected boolean addOneUnaryRule(UnaryRule rule, Map<String, TransducerGraph> graphs) {
    String parentString = (String) stateNumberer.object(rule.parent);
    String childString = (String) stateNumberer.object(rule.child);
    if (isSyntheticState(parentString)) {
      String topcat = getTopCategoryOfSyntheticState(parentString);
      TransducerGraph graph = getGraphFromMap(graphs, topcat);
      Double output = new Double(smartNegate(rule.score()));
      graph.addArc(graph.getStartNode(), parentString, childString, output);
      return true;
    } else if (isSyntheticState(childString)) {
      // need to add Arc from synthetic state to endState
      TransducerGraph graph = getGraphFromMap(graphs, parentString);
      Double output = new Double(smartNegate(rule.score()));
      graph.addArc(childString, parentString, END, output); // parentString should the the same as endState
      graph.setEndNode(parentString);
      return true;
    } else {
      return false;
    }
  }

  protected boolean addOneBinaryRule(BinaryRule rule, Map<String, TransducerGraph> graphs) {
    // parent has to be synthetic in BinaryRule
    String parentString = (String) stateNumberer.object(rule.parent);
    String leftString = (String) stateNumberer.object(rule.leftChild);
    String rightString = (String) stateNumberer.object(rule.rightChild);
    String source, target, input;
    String bracket = null;
    if (Train.markFinalStates) {
      bracket = parentString.substring(parentString.length() - 1, parentString.length());
    }
    // the below test is not necessary with left to right grammars
    if (isSyntheticState(leftString)) {
      source = leftString;
      input = rightString + (bracket == null ? ">" : bracket);
    } else if (isSyntheticState(rightString)) {
      source = rightString;
      input = leftString + (bracket == null ? "<" : bracket);
    } else {
      // we don't know what to do with this rule
      return false;
    }
    target = parentString;
    Double output = new Double(smartNegate(rule.score())); // makes it a real  0 <= k <= infty
    String topcat = getTopCategoryOfSyntheticState(source);
    if (topcat == null) {
      throw new RuntimeException("can't have null topcat");
    }
    TransducerGraph graph = getGraphFromMap(graphs, topcat);
    graph.addArc(source, target, input, output);
    return true;
  }

  protected boolean isSyntheticState(String state) {
    return state.charAt(0) == '@';
  }


  /**
   * @param graphs      a Map from String categories to TransducerGraph objects
   * @param unaryRules  is a Set of UnaryRule objects that we need to add
   * @param binaryRules is a Set of BinaryRule objects that we need to add
   * @return a new Pair of UnaryGrammar, BinaryGrammar
   */
  public Pair<UnaryGrammar,BinaryGrammar> convertGraphsToGrammar(Set<TransducerGraph> graphs, Set<UnaryRule> unaryRules, Set<BinaryRule> binaryRules) {
    // first go through all the existing rules and number them with new numberer
    newStateNumberer = new Numberer();
    for (UnaryRule rule : unaryRules) {
      Object parent = stateNumberer.object(rule.parent);
      rule.parent = newStateNumberer.number(parent);
      Object child = stateNumberer.object(rule.child);
      rule.child = newStateNumberer.number(child);
    }
    for (BinaryRule rule : binaryRules) {
      Object parent = stateNumberer.object(rule.parent);
      rule.parent = newStateNumberer.number(parent);
      Object leftChild = stateNumberer.object(rule.leftChild);
      rule.leftChild = newStateNumberer.number(leftChild);
      Object rightChild = stateNumberer.object(rule.rightChild);
      rule.rightChild = newStateNumberer.number(rightChild);
    }


    // put the new (smaller) numberer in place of the old one
    Map<String, Numberer> numbs = Numberer.getNumberers();
    numbs.put(stateSpace, newStateNumberer);

    // now go through the graphs and add the rules
    for (Iterator<TransducerGraph> graphIter = graphs.iterator(); graphIter.hasNext();) {
      TransducerGraph graph = graphIter.next();
      Object startNode = graph.getStartNode();
      for (Arc arc : graph.getArcs()) {
        Object source = arc.getSourceNode();
        Object target = arc.getTargetNode();
        Object input = arc.getInput();
        String inputString = input.toString();
        double output = ((Double) arc.getOutput()).doubleValue();
        if (source.equals(startNode)) {
          // make a UnaryRule
          UnaryRule ur = new UnaryRule(newStateNumberer.number(target), newStateNumberer.number(inputString), smartNegate(output));
          unaryRules.add(ur);
        } else if (inputString.equals(END) || inputString.equals(EPSILON)) {
          // make a UnaryRule
          UnaryRule ur = new UnaryRule(newStateNumberer.number(target), newStateNumberer.number(source), smartNegate(output));
          unaryRules.add(ur);
        } else {
          // make a BinaryRule
          // figure out whether the input was generated on the left or right
          int length = inputString.length();
          char leftOrRight = inputString.charAt(length - 1);
          inputString = inputString.substring(0, length - 1);
          BinaryRule br;
          if (leftOrRight == '<' || leftOrRight == '[') {
            br = new BinaryRule(newStateNumberer.number(target), newStateNumberer.number(inputString), newStateNumberer.number(source), smartNegate(output));
          } else if (leftOrRight == '>' || leftOrRight == ']') {
            br = new BinaryRule(newStateNumberer.number(target), newStateNumberer.number(source), newStateNumberer.number(inputString), smartNegate(output));
          } else {
            throw new RuntimeException("Arc input is in unexpected format: " + arc);
          }
          binaryRules.add(br);
        }
      }
    }
    // by now, the unaryRules and binaryRules Sets have old untouched and new rules with scores
    ClassicCounter<String> symbolCounter = new ClassicCounter<String>();
    if (outputType == RAW_COUNTS) {
      // now we take the sets of rules and turn them into grammars
      // the scores of the rules we are given are actually counts
      // so we count parent symbol occurences
      for (UnaryRule rule : unaryRules) {
        symbolCounter.incrementCount((String)newStateNumberer.object(rule.parent), rule.score);
      }
      for (BinaryRule rule : binaryRules) {
        symbolCounter.incrementCount((String)newStateNumberer.object(rule.parent), rule.score);
      }
    }
    // now we put the rules in the grammars
    int numStates = newStateNumberer.total();     // this should be smaller than last one
    int numRules = 0;
    UnaryGrammar ug = new UnaryGrammar(numStates);
    BinaryGrammar bg = new BinaryGrammar(numStates);
    for (UnaryRule rule : unaryRules) {
      if (outputType == RAW_COUNTS) {
        double count = symbolCounter.getCount((String)newStateNumberer.object(rule.parent));
        rule.score = (float) Math.log(rule.score / count);
      }
      ug.addRule(rule);
      numRules++;
    }
    for (BinaryRule rule : binaryRules) {
      if (outputType == RAW_COUNTS) {
        double count = symbolCounter.getCount((String)newStateNumberer.object(rule.parent));
        rule.score = (float) Math.log((rule.score - Train.ruleDiscount) / count);
      }
      bg.addRule(rule);
      numRules++;
    }
    if (verbose) {
      System.out.println("Number of minimized rules: " + numRules);
      System.out.println("Number of minimized states: " + newStateNumberer.total());
    }

    ug.purgeRules();
    bg.splitRules();
    return new Pair<UnaryGrammar,BinaryGrammar>(ug, bg);
  }

}
