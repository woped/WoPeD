/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package worldModel;

import processing.FrameNetWrapper.PhraseType;
import text.T2PSentence;

import de.saar.coli.salsa.reiter.framenet.FrameElement;

public class Specifier extends SpecifiedElement{
	
	public enum SpecifierType{
		PP,
		AMOD,
		NUM, //only for objects (20 minutes)
		NN,
		NNAFTER,
		SBAR,
		DIRECT,
		INFMOD,
		PARTMOD,
		COP,
		DOBJ, //for passive sentences (the employee is given a chance by the supervisor)
		RCMOD,
		IOBJ
	};
	
	
	private SpecifierType f_type = SpecifierType.DIRECT; //how this was found,
	private String f_headWord = null;
	private ExtractedObject f_object = null;
	//type of this phrase (used for deciding if it can be used in a label or not)
	private PhraseType f_pt = PhraseType.UNKNOWN;
	//if it was possible to detect, the FrameElement is set here, for further analysis
	private FrameElement f_fe;
	
	/**
	 * @param origin
	 * @param wordInSentence
	 */
	public Specifier(T2PSentence origin, int wordInSentence, String phrase) {
		super(origin, wordInSentence,phrase.toLowerCase());		
	}
	
	public SpecifierType getType() {
		return f_type;
	}
	
	public String getPhrase() {
		return getName();
	}
	
	public void setSpecifierType(SpecifierType type) {
		f_type = type;
	}
	
	@Override
	public String toString() {
		return "["+getPhrase()+"]";
	}

	public void setObject(ExtractedObject object) {
		this.f_object = object;
	}

	public ExtractedObject getObject() {
		return f_object;
	}

	public void setHeadWord(String headWord) {
		this.f_headWord = headWord;
	}

	public String getHeadWord() {
		return f_headWord;
	}

	/**
	 * @param genetive
	 */
	public void setPhraseType(PhraseType pt) {
		f_pt = pt;
	}

	public PhraseType getPhraseType() {
		return f_pt;
	}
	
	public void setFrameElement(FrameElement fe) {
		f_fe = fe;
	}

	public FrameElement getFrameElement() {
		return f_fe;
	}
	

}
