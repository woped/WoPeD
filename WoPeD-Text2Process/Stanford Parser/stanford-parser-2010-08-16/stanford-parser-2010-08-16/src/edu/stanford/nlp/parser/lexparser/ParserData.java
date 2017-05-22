package edu.stanford.nlp.parser.lexparser;

import java.io.Serializable;
import java.util.Map;

import edu.stanford.nlp.util.Numberer;

/**
 * Stores the serialized material representing the grammar and lexicon of a 
 * parser, and an Options that specifies things like how unknown words were
 * handled and how distances were binned that will also be needed to parse
 * with the grammar.
 *
 * @author Dan Klein
 * @author Christopher Manning
 */
public class ParserData implements Serializable {

  public Lexicon lex;
  public BinaryGrammar bg;
  public UnaryGrammar ug;
  public DependencyGrammar dg;
  public Map<String,Numberer> numbs;
  public Options pt;

  public ParserData(Lexicon lex, BinaryGrammar bg, UnaryGrammar ug, DependencyGrammar dg, Map<String,Numberer> numbs, Options pt) {
    this.lex = lex;
    this.bg = bg;
    this.ug = ug;
    this.dg = dg;
    this.numbs = numbs;
    this.pt = pt;
  }

  private static final long serialVersionUID = 1;

}
