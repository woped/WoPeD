package ToolWrapper;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Test;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.TreebankLanguagePack;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

public class StanfordParserInitializer {

    private static StanfordParserInitializer SPinitializer;

    private DocumentPreprocessor dpp = new DocumentPreprocessor();
    private LexicalizedParser parser;
    private TreebankLanguagePack tlp;
    private GrammaticalStructureFactory gsf;

    private StanfordParserInitializer(){
    }
    public synchronized static StanfordParserInitializer getInstance(){
        if (SPinitializer == null){
            synchronized (StanfordParserInitializer.class) {
                if (SPinitializer == null) {
                    SPinitializer = new StanfordParserInitializer();
                    SPinitializer.init();
                }
            }

        }
        return SPinitializer;
    }

    public static synchronized void resetInstance(){
        SPinitializer=null;
    }

    public synchronized DocumentPreprocessor getDpp() {
        return dpp;
    }

    public synchronized LexicalizedParser getParser() {
        return parser;
    }

    public synchronized TreebankLanguagePack getTlp() {
        return tlp;
    }

    public synchronized GrammaticalStructureFactory getGsf() {
        return gsf;
    }

    private void init(){
        try {
            ObjectInputStream in;
            InputStream is;
            URL u = StanfordParserInitializer.class.getResource("/StanfordParser/englishFactored.ser.gz");
            URLConnection uc = u.openConnection();
            is = uc.getInputStream();
            in = new ObjectInputStream(new GZIPInputStream(new BufferedInputStream(is)));
            parser = new LexicalizedParser(in);
            tlp = new PennTreebankLanguagePack(); //new ChineseTreebankLanguagePack();
            gsf = tlp.grammaticalStructureFactory();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        //option flags as in the Parser example, but without maxlength
        parser.setOptionFlags(new String[]{"-retainTmpSubcategories"});
        //f_parser.setOptionFlags(new String[]{"-segmentMarkov"});
        Test.MAX_ITEMS = 4000000; //enables parsing of long sentences
    }

}
