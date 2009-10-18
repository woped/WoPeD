package org.woped.quantana.sim;

public class SimCaseCopy extends SimCase {
	SimCase original;
		
	public SimCaseCopy (int id, SimCase orig){
		super(id);
		original = orig;
	}

	public SimCase getOriginal() {
		return original;
	}

	public void setOriginal(SimCase original) {
		this.original = original;
	}	
}
