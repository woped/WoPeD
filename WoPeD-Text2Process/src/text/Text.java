package text;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import transform.ListUtils;

public class Text {

	private ArrayList<T2PSentence> f_sentences = new ArrayList<T2PSentence>();
	
	public Text() {
		
	}
	
	public void addSentence(T2PSentence _s){
		f_sentences.add(_s);
	}
	
	public void removeSentence(T2PSentence s){
		f_sentences.remove(s);
	}
	
	public List<T2PSentence> getSentences(){
		return f_sentences;
	}
	
	public int getSize(){
		return f_sentences.size();
	}
	
	@Override
	public String toString() {
		return "Text ("+f_sentences.size()+" sentences)";
	}	
	
	public void printToStream(PrintStream ps){
		int i = 1;
		for(T2PSentence s:f_sentences){
			ps.println((i++) + ": " + s.toString());
		}
	}

	/**
	 * @param _id
	 * @return 
	 */
	public T2PSentence getSentence(int _id) {
		return f_sentences.get(_id);
	}

	private List<String> f_punct = ListUtils.getList(",",".","!","?","-","(",")");
	
	/**
	 * @return
	 */
	public double getAvgSentenceLength() {
		double _result = 0.0;
		for(T2PSentence sent:f_sentences) {
			int _lenght = 0;
			for(int i=0;i<sent.size();i++) {
				if(!f_punct.contains(sent.get(i)))
					_lenght++;
			}
			_result += _lenght;
		}
		_result /= f_sentences.size();
		return _result;
	}
	
}
