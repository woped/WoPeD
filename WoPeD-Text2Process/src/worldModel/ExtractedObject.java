/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package worldModel;

import text.T2PSentence;

public abstract class ExtractedObject extends SpecifiedElement{


	private boolean f_subjectRole = true;
	private String f_determiner = null;
	private SpecifiedElement f_reference = null;
	private boolean f_needsResolve = false;
	
	/**
	 * @param origin
	 * @param wordInSentence
	 * @param name
	 */
	public ExtractedObject(T2PSentence origin, int wordInSentence, String name) {
		super(origin, wordInSentence, name);		
	}
	
	public void setReference(SpecifiedElement reference) {
		this.f_reference = reference;
	}

	public SpecifiedElement getReference() {
		return f_reference;
	}
	
	public boolean needsResolve() {
		return f_needsResolve;
	}
	
	public void setSubjectRole(boolean subjectRole) {
		this.f_subjectRole = subjectRole;
	}

	public boolean isSubjectRole() {
		return f_subjectRole;
	}
	
	/**
	 * @param b
	 */
	public void setResolve(boolean value) {
		f_needsResolve  = value;
	}
	
	/**
	 * sets the determiner of this Object
	 * Usually an article or possessive pronoun.
	 * @param f_determiner
	 */
	public void setDeterminer(String f_determiner) {
		this.f_determiner = f_determiner;
	}

	public String getDeterminer() {
		return f_determiner;
	}
	
	@Override
	public String toString() {
		StringBuilder _b = new StringBuilder();
		if(f_determiner != null) {
			_b.append("["+f_determiner+"]");
			_b.append(" ");
		}		
		return super.toString(_b.toString(),getName());
	}
	
	
	
}
