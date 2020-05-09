package ToolWrapper;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.TreebankLanguagePack;

import java.util.Properties;

public class StanfordParserInitializer {

    private static StanfordParserInitializer SPinitializer;
    private static StanfordCoreNLP pipeline;
    TreebankLanguagePack tlp;
    GrammaticalStructureFactory gsf;

    private StanfordParserInitializer(){}

    public synchronized  static StanfordParserInitializer getInstance(){
        if(SPinitializer == null){
            synchronized (StanfordParserInitializer.class){
                if(SPinitializer == null){
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

    public synchronized StanfordCoreNLP getPipeline() {return pipeline;};
    public synchronized GrammaticalStructureFactory getGrammaticalStructure() {return gsf;};

    private void init(){
        try{
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse");
            //props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,dcoref");
            props.setProperty("parse.model", "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
            //props.setProperty("depparse.model", "edu/stanford/nlp/models/parser/nndep/english_UD.gz");
            pipeline = new StanfordCoreNLP(props);
            tlp = new PennTreebankLanguagePack();
            gsf = tlp.grammaticalStructureFactory();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }




}
