package edu.stanford.nlp.process;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.WordSegmenter;

import java.io.Reader;
import java.io.Serializable;
import java.util.Iterator;


/** A tokenizer that works by calling a WordSegmenter.
 *  This is used for Chinese and Arabic.
 *
 *  @author Galen Andrew
 */
public class WordSegmentingTokenizer extends AbstractTokenizer<Word> {
  private Iterator<Word> wordIter;
  private Tokenizer<Word> tok;
  private WordSegmenter wordSegmenter;

  @Override
  protected Word getNext() {
    while (wordIter == null || ! wordIter.hasNext()) {
      if ( ! tok.hasNext()) {
        return null;
      }
      String s = tok.next().word();
      if (s == null) {
        return null;
      }
      Sentence<Word> se = segmentWords(s);
      wordIter = se.iterator();
    }
    return wordIter.next();
  }

  public WordSegmentingTokenizer(WordSegmenter wordSegmenter, Reader r) {
    this.wordSegmenter = wordSegmenter;
    tok = new WhitespaceTokenizer(r);
  }

  public Sentence<Word> segmentWords(String s) {
    return wordSegmenter.segmentWords(s);
  }

  public static TokenizerFactory<Word> factory(WordSegmenter wordSegmenter) {
    return new WordSegmentingTokenizerFactory(wordSegmenter);
  }

  private static class WordSegmentingTokenizerFactory implements TokenizerFactory<Word>, Serializable {
    WordSegmenter wordSegmenter;

    public WordSegmentingTokenizerFactory(WordSegmenter wordSegmenter) {
      this.wordSegmenter = wordSegmenter;
    }

    public Iterator<Word> getIterator(Reader r) {
      return getTokenizer(r);
    }

    public Tokenizer<Word> getTokenizer(Reader r) {
      return new WordSegmentingTokenizer(wordSegmenter, r);
    }

    private static final long serialVersionUID = 1L;

  }

}
