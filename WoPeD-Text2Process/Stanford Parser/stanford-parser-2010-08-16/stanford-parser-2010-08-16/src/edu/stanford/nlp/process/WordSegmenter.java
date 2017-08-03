package edu.stanford.nlp.process;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.trees.Tree;

import java.io.Serializable;
import java.util.Collection;

/** An interface for segmenting strings into words
 *  (in unwordsegmented languages).
 *
 *  @author Galen Andrew
 */
public interface WordSegmenter extends Serializable {

  void train(Collection<Tree> trees);

  void loadSegmenter(String filename);

  Sentence<Word> segmentWords(String s);

}
