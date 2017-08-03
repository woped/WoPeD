package processing;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import text.T2PSentence;
import text.Text;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Test;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;

/**
 * A Wrapper for the calls to the stanford API
 *
 */
public class T2PStanfordWrapper {
	
	private DocumentPreprocessor f_dpp = new DocumentPreprocessor();
	private LexicalizedParser f_parser;
	private TreebankLanguagePack f_tlp;
    private GrammaticalStructureFactory f_gsf;
	/**
	 * 
	 */
	public T2PStanfordWrapper() {
		try {
			ObjectInputStream in;
		    InputStream is;
		    URL u = T2PStanfordWrapper.class.getResource("/englishFactored.ser.gz");
		    URLConnection uc = u.openConnection();
		    is = uc.getInputStream();
		    in = new ObjectInputStream(new GZIPInputStream(new BufferedInputStream(is)));   		
		    f_parser = new LexicalizedParser(in);
			f_tlp = new PennTreebankLanguagePack(); //new ChineseTreebankLanguagePack();
		    f_gsf = f_tlp.grammaticalStructureFactory();
		}catch(Exception ex) {
			ex.printStackTrace();
		}	    
		//option flags as in the Parser example, but without maxlength
		f_parser.setOptionFlags(new String[]{"-retainTmpSubcategories"});				
		//f_parser.setOptionFlags(new String[]{"-segmentMarkov"});				
		Test.MAX_ITEMS = 4000000; //enables parsing of long sentences
	}
	
	public Text createText(String input){
		return createText(input, null);
	}
	
	public Text createText(File file) throws IOException{
		return createText(file, null);
	}
	
	public Text createText(String input, ITextParsingStatusListener listener){
			Text _result = new Text();
			
			InputStream inputStream = new ByteArrayInputStream(input.getBytes());
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
			List<List<? extends HasWord>> _sentences = f_dpp.getSentencesFromText(reader);

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
	
	public Text createText(File file, ITextParsingStatusListener listener) throws IOException{
			Text _result = new Text();
			List<List<? extends HasWord>> _sentences = f_dpp.getSentencesFromText(file.getAbsolutePath());
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
		Tree _parse = f_parser.apply(_s);
		_s.setTree(_parse);
		GrammaticalStructure _gs = f_gsf.newGrammaticalStructure(_parse);
		_s.setGrammaticalStructure(_gs);
		return _s;
	}

}
