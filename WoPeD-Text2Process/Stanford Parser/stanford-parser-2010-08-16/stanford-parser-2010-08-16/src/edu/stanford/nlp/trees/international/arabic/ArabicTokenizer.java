package edu.stanford.nlp.trees.international.arabic;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.AbstractTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.WhitespaceTokenizer;

import java.io.*;
import java.util.Iterator;

/**
 * An ArabicTokenizer is a simple tokenizer that splits off a few punctuation characters,
 * and otherwise just splits on and discards whitespace characters.
 * This implementation returns Word objects. It has a parameter for whether
 * to make EOL a token or whether to treat EOL characters as whitespace.
 * If an EOL is a token, the class returns it as a Word with String value "\n".
 *
 * @author Christopher Manning
 */
public class ArabicTokenizer extends AbstractTokenizer<Word> {

  /**
   * A factory which vends ArabicTokenizers.
   *
   * @author Christopher Manning
   */
  public static class ArabicTokenizerFactory implements TokenizerFactory<Word>, Serializable {
    private static final long serialVersionUID = 6759533831515214642L;

    private final boolean eolIsSignificant;

    /**
     * Constructs a new TokenizerFactory that returns Word objects and
     * treats carriage returns as normal whitespace.
     * THIS METHOD IS INVOKED BY REFLECTION BY SOME OF THE JAVANLP
     * CODE TO LOAD A TOKENIZER FACTORY.  IT SHOULD BE PRESENT IN A
     * TokenizerFactory.
     *
     * @return A TokenizerFactory that returns Word objects
     */
    public static TokenizerFactory<Word> newTokenizerFactory() {
      return new ArabicTokenizerFactory();
    }

    public ArabicTokenizerFactory() {
      this(false);
    }

    public ArabicTokenizerFactory(boolean eolIsSignificant) {
      this.eolIsSignificant = eolIsSignificant;
    }

    public Iterator<Word> getIterator(Reader r) {
      return getTokenizer(r);
    }

    public Tokenizer<Word> getTokenizer(Reader r) {
      return new ArabicTokenizer(r, eolIsSignificant);
    }

  } // end class ArabicTokenizerFactory


  private ArabicLexer lexer;
  private final boolean eolIsSignificant;

  /**
   * Internally fetches the next token.
   *
   * @return the next token in the token stream, or null if none exists.
   */
  @Override
  protected Word getNext() {
    Word token = null;
    if (lexer == null) {
      return token;
    }
    try {
      token = lexer.next();
      while (token == ArabicLexer.crValue) {
        if (eolIsSignificant) {
          return token;
        } else {
          token = lexer.next();
        }
      }
    } catch (IOException e) {
      // do nothing, return null
    }
    return token;
  }

  /**
   * Constructs a new ArabicTokenizer
   *
   * @param r The Reader r that is its source.
   */
  public ArabicTokenizer(Reader r) {
    this(r, false);
  }


  /**
   * Constructs a new ArabicTokenizer
   * @param r The Reader that is its source.
   * @param eolIsSignificant Whether eol tokens should be returned.
   */
  public ArabicTokenizer(Reader r, boolean eolIsSignificant) {
    this.eolIsSignificant = eolIsSignificant;
    // The conditional below is perhaps currently needed in LexicalizedParser, since
    // it passes in a null arg while doing type-checking for sentence escaping
    // but StreamTokenizer barfs on that.  But maybe shouldn't be here.
    if (r != null) {
      lexer = new ArabicLexer(r);
    }
  }

  /* ----
   * Sets the source of this Tokenizer to be the Reader r.

  private void setSource(Reader r) {
    lexer = new ArabicLexer(r);
  }
  ---- */

  public static TokenizerFactory<Word> factory() {
    return new ArabicTokenizerFactory(false);
  }

  public static TokenizerFactory<Word> factory(boolean eolIsSignificant) {
    return new ArabicTokenizerFactory(eolIsSignificant);
  }

  /**
   * Reads a file from the argument and prints its tokens one per line.
   * This is mainly as a testing aid, but it can also be quite useful
   * standalone to turn a corpus into a one token per line file of tokens.
   * <p/>
   * Usage: <code>java edu.stanford.nlp.process.ArabicTokenizer filename
   * </code>
   *
   * @param args Command line arguments
   * @throws IOException If can't open files, etc.
   */
  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      System.err.println("usage: java edu.stanford.nlp.process.ArabicTokenizer [-cr] filename");
      return;
    }
    ArabicTokenizer tokenizer = new ArabicTokenizer(new InputStreamReader(new FileInputStream(args[args.length - 1]), "UTF-8"), args[0].equals("-cr"));
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"), true);
    while (tokenizer.hasNext()) {
      Word w = tokenizer.next();
      if (w == ArabicLexer.crValue) {
        pw.println("***CR***");
      } else {
        pw.println(w);
      }
    }
  }

} // end class ArabicTokenizer

