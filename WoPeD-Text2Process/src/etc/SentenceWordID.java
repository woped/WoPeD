/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package etc;

import worldModel.SpecifiedElement;

/**
 * An object to store the reference to a particular word in a particular sentence
 * e.g. for manual reference resolution
 *
 */
public class SentenceWordID {
	
	private int f_sentenceID;
	private int f_wordID;
	
	
	/**
	 * 
	 */
	public SentenceWordID(int sID,int wID) {
		f_sentenceID = sID;
		f_wordID = wID;
	}
	
	@Override
	public int hashCode() {
		return f_sentenceID *1000000 + f_wordID;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SentenceWordID) {
			SentenceWordID _other = (SentenceWordID) obj;
			if(_other.getSentenceID() == f_sentenceID) {
				if(_other.getWordID() == f_wordID) {
					return true;
				}
			}
			
		}
		return false;
	}
	
	public SentenceWordID(SpecifiedElement element) {
		f_sentenceID = element.getOrigin().getID();
		f_wordID = element.getWordIndex();
	}
	
	public void setWordID(int wordID) {
		this.f_wordID = wordID;
	}
	public int getWordID() {
		return f_wordID;
	}
	public void setSentenceID(int sentenceID) {
		this.f_sentenceID = sentenceID;
	}
	public int getSentenceID() {
		return f_sentenceID;
	}
	
	
	@Override
	public String toString() {
		return "SentenceWordID ("+f_sentenceID+","+f_wordID+")";
	}

}
