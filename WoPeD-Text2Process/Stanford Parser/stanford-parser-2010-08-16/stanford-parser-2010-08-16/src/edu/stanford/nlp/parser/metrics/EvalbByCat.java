package edu.stanford.nlp.parser.metrics;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.trees.Constituent;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.stats.ClassicCounter;

import java.io.PrintWriter;
import java.util.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 * Computes labeled precision and recall (evalb) at the constituent category level.
 * 
 * @author Roger Levy
 * @author Spence Green
 */
public class EvalbByCat extends AbstractEval {

  private final Evalb evalb;

  private final ClassicCounter<Label> precisions;
  private final ClassicCounter<Label> recalls;
  private final ClassicCounter<Label> f1s;

  private final ClassicCounter<Label> precisions2;
  private final ClassicCounter<Label> recalls2;
  private final ClassicCounter<Label> pnums2;
  private final ClassicCounter<Label> rnums2;


  public EvalbByCat(String str, boolean runningAverages) {
    super(str, runningAverages);

    evalb = new Evalb(str, false);
    precisions = new ClassicCounter<Label>();
    recalls = new ClassicCounter<Label>();
    f1s = new ClassicCounter<Label>();

    precisions2 = new ClassicCounter<Label>();
    recalls2 = new ClassicCounter<Label>();
    pnums2 = new ClassicCounter<Label>();
    rnums2 = new ClassicCounter<Label>();
  }


  @Override
  protected Set<Constituent> makeObjects(Tree tree) {
    return evalb.makeObjects(tree);
  }

  private Map<Label,Set<Constituent>> makeObjectsByCat(Tree t) {
    Map<Label,Set<Constituent>> objMap = new HashMap<Label,Set<Constituent>>();
    Set<Constituent> objSet = makeObjects(t);
    for (Constituent lc : objSet) {
      Label l = lc.label();
      if (!objMap.keySet().contains(l)) {
        objMap.put(l, new HashSet<Constituent>());
      }
      objMap.get(l).add(lc);
    }
    return objMap;
  }

  @Override
  public void evaluate(Tree guess, Tree gold, PrintWriter pw) {
  	if(gold == null || guess == null) {
      System.err.printf("%s: Cannot compare against a null gold or guess tree!\n",this.getClass().getName());
      return;
    }
  	
  	Map<Label,Set<Constituent>> guessDeps = makeObjectsByCat(guess);
    Map<Label,Set<Constituent>> goldDeps = makeObjectsByCat(gold);

    Set<Label> cats = new HashSet<Label>();
    cats.addAll(guessDeps.keySet());
    cats.addAll(goldDeps.keySet());

    if (pw != null && runningAverages) {
      pw.println("========================================");
      pw.println("Labeled Bracketed Evaluation by Category");
      pw.println("========================================");
    }

    num += 1.0;

    for (Label cat : cats) {
      Set<Constituent> thisGuessDeps = guessDeps.get(cat);
      Set<Constituent> thisGoldDeps = goldDeps.get(cat);

      if (thisGuessDeps == null)
        thisGuessDeps = new HashSet<Constituent>();
      if (thisGoldDeps == null)
        thisGoldDeps = new HashSet<Constituent>();

      double currentPrecision = precision(thisGuessDeps, thisGoldDeps);
      double currentRecall = precision(thisGoldDeps, thisGuessDeps);

      double currentF1 = (currentPrecision > 0.0 && currentRecall > 0.0 ? 2.0 / (1.0 / currentPrecision + 1.0 / currentRecall) : 0.0);

      precisions.incrementCount(cat, currentPrecision);
      recalls.incrementCount(cat, currentRecall);
      f1s.incrementCount(cat, currentF1);

      precisions2.incrementCount(cat, thisGuessDeps.size() * currentPrecision);
      pnums2.incrementCount(cat, thisGuessDeps.size());

      recalls2.incrementCount(cat, thisGoldDeps.size() * currentRecall);
      rnums2.incrementCount(cat, thisGoldDeps.size());

      if (pw != null && runningAverages) {
        pw.println(cat + "\tP: " + ((int) (currentPrecision * 10000)) / 100.0 + " (sent ave " + ((int) (precisions.getCount(cat) * 10000 / num)) / 100.0 + ") (evalb " + ((int) (precisions2.getCount(cat) * 10000 / pnums2.getCount(cat))) / 100.0 + ")");
        pw.println("\tR: " + ((int) (currentRecall * 10000)) / 100.0 + " (sent ave " + ((int) (recalls.getCount(cat) * 10000 / num)) / 100.0 + ") (evalb " + ((int) (recalls2.getCount(cat) * 10000 / rnums2.getCount(cat))) / 100.0 + ")");
        double cF1 = 2.0 / (rnums2.getCount(cat) / recalls2.getCount(cat) + pnums2.getCount(cat) / precisions2.getCount(cat));
        String emit = str + " F1: " + ((int) (currentF1 * 10000)) / 100.0 + " (sent ave " + ((int) (10000 * f1s.getCount(cat) / num)) / 100.0 + ", evalb " + ((int) (10000 * cF1)) / 100.0 + ")";
        pw.println(emit);
      }
    }
    if (pw != null && runningAverages) {
      pw.println("========================================");
    }
  }

  @Override
  public void display(boolean verbose, PrintWriter pw) {
    NumberFormat nf = new DecimalFormat("0.00");
    Set<Label> cats = new HashSet<Label>();
    cats.addAll(precisions.keySet());
    cats.addAll(recalls.keySet());
    
    Map<Double,Label> f1Map = new TreeMap<Double,Label>();
    for (Label cat : cats) {
      double pnum2 = pnums2.getCount(cat);
      double rnum2 = rnums2.getCount(cat);
      double prec = precisions2.getCount(cat) / pnum2;//(num > 0.0 ? precision/num : 0.0);
      double rec = recalls2.getCount(cat) / rnum2;//(num > 0.0 ? recall/num : 0.0);
      double f1 = 2.0 / (1.0 / prec + 1.0 / rec);//(num > 0.0 ? f1/num : 0.0);
    
      if(new Double(f1).equals(Double.NaN)) f1 = -1.0;
      f1Map.put(f1, cat);
    }
    
    pw.println("============================================================");
    pw.println("Labeled Bracketed Evaluation by Category -- final statistics");
    pw.println("============================================================");

    for (Label cat : f1Map.values()) {
      double pnum2 = pnums2.getCount(cat);
      double rnum2 = rnums2.getCount(cat);
      double prec = precisions2.getCount(cat) / pnum2;//(num > 0.0 ? precision/num : 0.0);
      double rec = recalls2.getCount(cat) / rnum2;//(num > 0.0 ? recall/num : 0.0);
      double f1 = 2.0 / (1.0 / prec + 1.0 / rec);//(num > 0.0 ? f1/num : 0.0);

      pw.println(cat + "\tLP: " + ((pnum2 == 0.0) ? " N/A": nf.format(prec)) + "\tguessed: " + (int) pnum2 +
          "\tLR: " + ((rnum2 == 0.0) ? " N/A": nf.format(rec)) + "\tgold:  " + (int) rnum2 +
          "\tF1: " + ((pnum2 == 0.0 || rnum2 == 0.0) ? " N/A": nf.format(f1)));
    }
   
    pw.println("============================================================");
  }

}
