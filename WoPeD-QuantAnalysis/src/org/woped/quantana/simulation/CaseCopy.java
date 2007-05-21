package org.woped.quantana.simulation;

public class CaseCopy extends Case {
	
	private Case original;
	
	public CaseCopy (int id, Case orig){
		super(id);
		original = orig;
	}

	public Case getOriginal() {
		return original;
	}

	public void setOriginal(Case original) {
		this.original = original;
	}
}
