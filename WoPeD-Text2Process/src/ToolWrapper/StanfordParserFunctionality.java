package ToolWrapper;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import TextToWorldModel.processing.ITextParsingStatusListener;
import worldModel.T2PSentence;
import worldModel.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StanfordParserFunctionality {

    private StanfordParserInitializer SPInitializer = StanfordParserInitializer.getInstance();
    private DocumentPreprocessor dpp;
    private GrammaticalStructureFactory gsf;
    private LexicalizedParser parser;
    private TreebankLanguagePack tlp;

   public StanfordParserFunctionality(){
       dpp = SPInitializer.getDpp();
       gsf = SPInitializer.getGsf();
       parser = SPInitializer.getParser();
       tlp = SPInitializer.getTlp();
   }

    public Text createText(String input){
        return createText(input, null);
    }

    public Text createText(String input, ITextParsingStatusListener listener){
        Text _result = new Text();

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        List<List<? extends HasWord>> _sentences = dpp.getSentencesFromText(reader);

        if(listener != null) listener.setNumberOfSentences(_sentences.size());
        int _sentenceNumber = 1;
        int sentenceOffset = 0;
        for(List<? extends HasWord> _sentence:_sentences){
            if(_sentence.get(0).word().equals("#")) {
                //comment line - skip
                if(listener != null) listener.sentenceParsed(_sentenceNumber++);
                sentenceOffset += ((Word)_sentence.get(_sentence.size()-1)).endPosition();
                continue;
            }
            ArrayList<Word> _list = new ArrayList<Word>();
            for(HasWord w:_sentence){
                if(w instanceof Word){
                    _list.add((Word) w);
                }else{
                    System.out.println("Error occured while creating a Word!");
                }
            }
            T2PSentence _s = createSentence(_list);
            _s.setCommentOffset(sentenceOffset);
            _result.addSentence(_s);
            if(listener != null) listener.sentenceParsed(_sentenceNumber++);
        }
        return _result;
    }


    private T2PSentence createSentence(ArrayList<Word> _list) {
        T2PSentence _s = new T2PSentence(_list);
        Tree _parse = parser.apply(_s);
        _s.setTree(_parse);
        GrammaticalStructure _gs = gsf.newGrammaticalStructure(_parse);
        _s.setGrammaticalStructure(_gs);
        return _s;
    }

}
