package edu.stanford.nlp.parser.metrics;

import java.io.PrintWriter;
import java.util.*;

import edu.stanford.nlp.ling.HasTag;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.WordTag;
import edu.stanford.nlp.parser.lexparser.Lexicon;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Pair;

/**
 * Computes POS tagging P/R/F1 from guess/gold trees. This version assumes that the yields match. For
 * trees with potentially different yields, use {@link #TsarfatyEval}. 
 * 
 * @author Dan Klein
 * @author Spence Green
 *
 */
public class TaggingEval extends AbstractEval {

  private static final boolean DEBUG = false;
  private static final boolean DEBUG_MORE = false;
  private final Lexicon lex;
  private final boolean useTag;

  @Override
  protected
  Set<?> makeObjects(Tree tree) {
    List<TaggedWord> twList;
    if (useTag) {
      twList = myExtractor(tree);
    } else {
      twList = tree.taggedYield();
    }
    Set<Pair<Integer,WordTag>> set = new HashSet<Pair<Integer,WordTag>>();
    for (int i = 0, sz = twList.size(); i < sz; i++) {
      TaggedWord tw = twList.get(i);
      //IntTaggedWord iTW = new IntTaggedWord(Numberer.number("words",tw.word()), Numberer.number("tags",tw.tag()));
      Pair<Integer,WordTag> positionWT = new Pair<Integer,WordTag>(Integer.valueOf(i), new WordTag(tw.value(), tw.tag()));
      //WordTag positionWT = new WordTag(tw.value(),tw.tag());
      //System.out.println(iTW);
      //if (! tw.tag.equals("*"))
      set.add(positionWT);
    }
    if (DEBUG_MORE) System.err.println("Tags: " + set);
    return set;
  }

  public TaggingEval(String str) {
    this(str, true, null);
  }

  public TaggingEval(String str, boolean runningAverages, Lexicon lex) {
    this(str, runningAverages, lex, false);
  }

  /** @param useTag If true, use a special way of getting
   *    the tags out of a dependency tree, when the usual
   *    Tree code doesn't work.
   */
  public TaggingEval(String str, boolean runningAverages, Lexicon lex, boolean useTag) {
    super(str, runningAverages);
    this.lex = lex;
    this.useTag = useTag;
  }

  private static Sentence<TaggedWord> myExtractor(Tree t) {
    return myExtractor(t, new Sentence<TaggedWord>());
  }

  private static Sentence<TaggedWord> myExtractor(Tree t, Sentence<TaggedWord> ty) {
    Tree[] kids = t.children();
    // this inlines the content of t.isPreTerminal()
    if (kids.length == 1 && kids[0].isLeaf()) {
      if (t.label() instanceof HasTag) {
        //   System.err.println("Object is: " + ((CategoryWordTag) t.label()).toString("full"));
        ty.add(new TaggedWord(kids[0].label().value(), ((HasTag) t.label()).tag()));
      } else {
        //   System.err.println("Object is: " + StringUtils.getShortClassName(t.label()) + " " + t.label());
        ty.add(new TaggedWord(kids[0].label().value(), t.label().value()));
      }
    } else {
      for (int i = 0; i < kids.length; i++) {
        myExtractor(kids[i], ty);
      }
    }
    return ty;
  }

  @Override
  public void evaluate(Tree guess, Tree gold, PrintWriter pw) {
    Sentence<TaggedWord> sGold = gold.taggedYield();
    Sentence<TaggedWord> sGuess;
    if (useTag) {
      sGuess = myExtractor(guess);
    } else {
      sGuess = guess.taggedYield();
    }
    if (sGuess.size() != sGold.size()) {
      pw.println("Warning: yield length differs:");
      pw.println("Guess: " + sGuess);
      pw.println("Gold:  " + sGold);
    } else {
      if (DEBUG) {
        for (Iterator<TaggedWord> goldIt = sGold.iterator(), guessIt = sGuess.iterator(); goldIt.hasNext();) {
          TaggedWord goldNext = goldIt.next();
          TaggedWord guessNext = guessIt.next();
          if (!goldNext.tag().equals(guessNext.tag())) {
            pw.print("TaggingError ");
            if (lex != null && lex.isKnown(goldNext.word())) {
              pw.print("seen ");
            } else {
              pw.print("unseen ");
            }
            pw.println(goldNext.word() + " correct " + goldNext.tag() + " chose " + guessNext.tag());
          }
        }
      }
    }
    super.evaluate(guess, gold, pw);
  }

}