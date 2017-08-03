package edu.stanford.nlp.international.arabic;

import java.util.HashMap;
import java.util.Properties;
import java.io.*;

import edu.stanford.nlp.io.EncodingPrintWriter;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.util.StringUtils;
import edu.stanford.nlp.process.SerializableFunction;
import edu.stanford.nlp.trees.*;


/** This class can convert between Unicode and Buckwalter encodings of
 *  Arabic.
 *
 *  @author Christopher Manning
 */
public class Buckwalter implements SerializableFunction<String,String> {

  private static final long serialVersionUID = 1L;

  /**
   * If true (include flag "-outputUnicodeValues"), outputs space separated
   * unicode values (e.g., "\u0621" rather than the character version of those values.
   * Only applicable for Buckwalter to Arabic conversion.
   */
  boolean outputUnicodeValues = false;

  // Sources
  // http://www.ldc.upenn.edu/myl/morph/buckwalter.html
  // http://www.qamus.org/transliteration.htm (Tim Buckwalter's site)
  // http://www.livingflowers.com/Arabic_transliteration (many but hard to use)
  // http://www.cis.upenn.edu/~cis639/arabic/info/romanization.html
  // http://www.nongnu.org/aramorph/english/index.html (Java AraMorph)
  // http://www.xrce.xerox.com/competencies/content-analysis/arabic/info/romanization.html
  // http://www.xrce.xerox.com/competencies/content-analysis/arabic/info/buckwalter-about.html
  // BBN's MBuckWalter2Unicode.tab
  // see also my GALE-NOTES.txt file for other mappings ROSETTA people do.
  // http://www.xrce.xerox.com/competencies/content-analysis/arabic-inxight/arabic-surf-lang-unicode.pdf
  // Normalization of decomposed characters to composed:
  // ARABIC LETTER ALEF (\u0627), ARABIC MADDAH ABOVE (\u0653) ->
  //   ARABIC LETTER ALEF WITH MADDA ABOVE
  // ARABIC LETTER ALEF (\u0627), ARABIC HAMZA ABOVE (\u0654) ->
  //   ARABIC LETTER ALEF WITH HAMZA ABOVE (\u0623)
  // ARABIC LETTER WAW, ARABIC HAMZA ABOVE ->
  //    ARABIC LETTER WAW WITH HAMZA ABOVE
  // ARABIC LETTER ALEF, ARABIC HAMZA BELOW (\u0655) ->
  //    ARABIC LETTER ALEF WITH HAMZA BELOW
  // ARABIC LETTER YEH, ARABIC HAMZA ABOVE ->
  //    ARABIC LETTER YEH WITH HAMZA ABOVE

  private char[] arabicChars = {
    '\u0621', '\u0622', '\u0623', '\u0624', '\u0625', '\u0626', '\u0627',
    '\u0628', '\u0629', '\u062A', '\u062B',
    '\u062C', '\u062D', '\u062E', '\u062F',
    '\u0630', '\u0631', '\u0632', '\u0633',
    '\u0634', '\u0635', '\u0636', '\u0637', '\u0638', '\u0639', '\u063A',
    '\u0640', '\u0641', '\u0642', '\u0643',
    '\u0644', '\u0645', '\u0646', '\u0647',
    '\u0648', '\u0649', '\u064A', '\u064B',
    '\u064C', '\u064D', '\u064E', '\u064F',
    '\u0650', '\u0651', '\u0652',
    '\u0670', '\u0671',
    '\u067E', '\u0686', '\u0698', '\u06A4', '\u06AF',
    '\u0625', '\u0623', '\u0624',    // add Tim's "XML-friendly" just in case
    '\u060C', '\u061B', '\u061F', // from BBN script; status unknown
    '\u066A', '\u066B', '\u066C', '\u06D4', // from IBM script
    '0', '1', '2', '3', '4', // numbers, allow ASCII Arabic, or Arabic Arabic
    '5', '6', '7', '8', '9', // numbers, allow ASCII Arabic, or Arabic Arabic
    '\u0660', '\u0661', '\u0662', '\u0663', '\u0664',
    '\u0665', '\u0666', '\u0667', '\u0668', '\u0669',
    '\u00AB', '\u00BB' // French quotes that Chris added
  };

  private char[] buckChars = {
    '\'', '|', '>', '&', '<', '}', 'A',
    'b', 'p', 't', 'v',
    'j', 'H', 'x', 'd', // end 062x
    '*', 'r', 'z', 's',
    '$', 'S', 'D', 'T', 'Z', 'E', 'g', // end 063x
    '_', 'f', 'q', 'k',
    'l', 'm', 'n', 'h',
    'w', 'Y', 'y', 'F',
    'N', 'K', 'a', 'u', // end 0064x
    'i', '~', 'o',
    '`', '{',
    'P', 'J', 'R', 'V', 'G', // U+0698 is Persian Jeh: R according to Perl module
    'I', 'O', 'W',   // add Tim's "XML-friendly" versions just in case
    ',', ';', '?', // from BBN script; status unknown
    '%', '.', ',', '.', // from IBM script
    '0', '1', '2', '3', '4', // numbers, allow ASCII Arabic, or Arabic Arabic
    '5', '6', '7', '8', '9', // numbers, allow ASCII Arabic, or Arabic Arabic
    '0', '1', '2', '3', '4', // numbers, allow ASCII Arabic, or Arabic Arabic
    '5', '6', '7', '8', '9', // numbers, allow ASCII Arabic, or Arabic Arabic
    '"', '"' // French quotes that Chris added
  };
  /* BBN also maps to @: 0x007B 0x066C 0x066D 0x0660 0x0661 0x0662 0x0663
                         0x0664 0x0665 0x0666 0x0667 0x0668 0x0669 0x066A
                         0x0686 0x06AF 0x066D 0x06AF 0x06AA 0x06AB 0x06B1
                         0x06F0 0x06EC 0x06DF 0x06DF 0x06F4 0x002A 0x274A
                         0x00E9 0x00C9 0x00AB 0x00BB 0x00A0 0x00A4
  */
  /* BBNWalter dispreferring punct chars:
     '\u0624', '\u0625', '\u0626',  -> 'L', 'M', 'Q',
     '\u0630', -> 'C', '\u0640', -> '@', '\u0651', -> 'B',
   */
  /* IBM also deletes: 654 655 670 */

  private boolean u2b;
  private HashMap<Character,Character> a2b;
  private HashMap<Character,Character> b2a;
  private ClassicCounter<String> unmappable;

  private static final boolean DEBUG = true;
  private static final boolean PASS_ASCII_IN_UNICODE = true;
  private static final boolean SUPPRESS_DIGIT_MAPPING_IN_B2A = true;

  { // initialization block
    if (arabicChars.length != buckChars.length) {
      throw new RuntimeException("Buckwalter: Bad char arrays");
    }
    a2b = new HashMap<Character,Character>(arabicChars.length);
    b2a = new HashMap<Character,Character>(buckChars.length);
    for (int i = 0; i < arabicChars.length; i++) {
      Character ca = Character.valueOf(arabicChars[i]);
      Character cb = Character.valueOf(buckChars[i]);
      a2b.put(ca, cb);
      b2a.put(cb, ca);
    }
    if (DEBUG) {
      unmappable = new ClassicCounter<String>();
    }
  }


  public String apply(String in) {
    return convert(in, u2b);
  }

  private String convert(String in, boolean unicodeToBuckwalter) {
    // System.err.println("convert u2b: " + unicodeToBuckwalter + " on " + in);
    StringBuilder res = new StringBuilder(in.length());
    for (int i = 0, sz = in.length(); i < sz; i++) {
      Character inCh = Character.valueOf(in.charAt(i));
      Character outCh;
      if (unicodeToBuckwalter) {
        if (PASS_ASCII_IN_UNICODE && inCh.charValue() < 127) {
          outCh = inCh;
        } else {
          outCh = a2b.get(inCh);
        }
      } else {
        if (SUPPRESS_DIGIT_MAPPING_IN_B2A && (inCh >= '0' && inCh <= '9')) {
          outCh = inCh;
        } else {
          outCh = b2a.get(inCh);
        }
      }
      if (outCh == null) {
        if (DEBUG) {
          String key = inCh + "[U+" +
            StringUtils.padLeft(Integer.toString(inCh, 16).toUpperCase(), 4, '0') + ']';
          unmappable.incrementCount(key);
        }
        res.append(inCh);  // pass through char
      } else {
        if(!outputUnicodeValues) {
          res.append(outCh);
        } else {
          res.append("\\u").append(StringUtils.padLeft(Integer.toString(inCh, 16).toUpperCase(), 4, '0'));
        }

      }
    }
    return res.toString();
  }

  public String buckwalterToUnicode(String in) {
    return convert(in, false);
  }

  public String unicodeToBuckwalter(String in) {
    return convert(in, true);
  }

  public Buckwalter() {
    this(false);
  }

  public Buckwalter(boolean unicodeToBuckwalter) {
    u2b = unicodeToBuckwalter;
  }

  public static void main(String[] args) throws IOException {
    if (args.length < 1 ||
        ! (args[0].equals("-a2b") || args[0].equals("-b2a"))) {
      System.err.println("usage: java Buckwalter [-a2b|-b2a] words+ OR, as a filter, just [-a2b|-b2a]");
      return;
    }
    Properties p = StringUtils.argsToProperties(args);
    Buckwalter b;
    b = new Buckwalter(args[0].equals("-a2b"));
    if(p.containsKey("outputUnicodeValues"))
      b.outputUnicodeValues = true;
    int j = (p.containsKey("outputUnicodeValues") ? 2 : 1);
    if (j < args.length) {
      for (; j < args.length; j++) {
        EncodingPrintWriter.out.println(args[j] + " -> " + b.apply(args[j]), "utf-8");
      }
    } else {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
      String line;
      while ((line = br.readLine()) != null) {
        EncodingPrintWriter.out.println(b.apply(line), "utf-8");
      }
    }
    if (DEBUG) {
      if ( ! b.unmappable.keySet().isEmpty()) {
        EncodingPrintWriter.err.println("Characters that could not be converted [passed through!]:", "utf-8");
        EncodingPrintWriter.err.println(b.unmappable.toString(), "utf-8");
      } else {
        EncodingPrintWriter.err.println("All characters successfully converted!", "utf-8");
      }
    }
  }


  /** Converts Penn-style trees from Buckwalter to utf-8 Arabic and back.
   *  This is a TreeMangler.  It changes the trees that it visits.
   */
  public static class TreeConverter implements TreeVisitor, Serializable {

    private static final long serialVersionUID = 1L;

    private final Buckwalter b;

    public TreeConverter(boolean a2b) {
      b = new Buckwalter(a2b);
    }

    public void visitTree(Tree t) {
       for (Tree node : t) {
         if (node.isPreTerminal()) {
           String tag = node.label().value();
           Tree kid = node.firstChild();
           String word = kid.label().value();
           if ("PUNC".equals(tag) || "LATIN".equals(tag) || "-RRB-".equals(word)) {
             // do nothing
           } else {
             kid.label().setValue(b.apply(word));
           }
         }
       }
    }

    public void printDebug() {
      if (DEBUG) {
        if ( ! b.unmappable.keySet().isEmpty()) {
          EncodingPrintWriter.err.println("Characters that could not be converted [passed through!]:", "utf-8");
          EncodingPrintWriter.err.println(b.unmappable.toString(), "utf-8");
        } else {
          EncodingPrintWriter.err.println("All characters successfully converted!", "utf-8");
        }
      }
    }
    
    public static void main(String[] args) throws IOException {
      // cheat by using
      if (args.length < 1 ||
          ! (args[0].equals("-a2b") || args[0].equals("-b2a"))) {
        System.err.println("usage: java Buckwalter\\$TreeConverter [-a2b|-b2a] treebankPath");
        return;
      }

      TreeConverter tconv = new TreeConverter(args[0].equals("-a2b"));
      TreeReaderFactory trf = new TreeReaderFactory() {
            public TreeReader newTreeReader(Reader in) {
              return new PennTreeReader(in, new LabeledScoredTreeFactory());
            }
          };

      Treebank treebank = new DiskTreebank(trf, "utf-8");
      treebank.loadPath(args[1]);
      System.err.println("Read " + treebank.size() + " trees.");
      PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out, "utf-8"), true);

      for (Tree t : treebank) {
        tconv.visitTree(t);
        t.pennPrint(pw);
      }
      
      tconv.printDebug();
    } // end main

  } // end static class TreeConverter

}
