/**
 * modified taken from https://github.com/FabianFriedrich/Text2Process
 */
package TextToWorldModel.transform;

import worldModel.SpecifiedElement;

public class ConjunctionElement {
	
	public enum ConjunctionType {
		AND,
		OR,
		ANDOR,
		MIXED
	}

	private SpecifiedElement f_to;
	private SpecifiedElement f_from;
	private ConjunctionType f_type;;
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ConjunctionElement) {
			ConjunctionElement _link = (ConjunctionElement)obj;
			if(_link.getFrom().getWordIndex() == this.getFrom().getWordIndex() 
				&& _link.getTo().getWordIndex() == this.getTo().getWordIndex() &&
				_link.getType().equals(this.getType())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 */
	public ConjunctionElement(SpecifiedElement from, SpecifiedElement to,ConjunctionType type) {
		setFrom(from);
		setTo(to);
		setType(type);
	}

	public void setTo(SpecifiedElement f_to) {
		this.f_to = f_to;
	}

	public SpecifiedElement getTo() {
		return f_to;
	}

	public void setFrom(SpecifiedElement f_from) {
		this.f_from = f_from;
	}

	public SpecifiedElement getFrom() {
		return f_from;
	}

	public void setType(ConjunctionType f_type) {
		this.f_type = f_type;
	}

	public ConjunctionType getType() {
		return f_type;
	}
	
	@Override
	public String toString() {
		return f_to.toString() +"-"+f_type+"-"+f_from.toString();
	}

}
