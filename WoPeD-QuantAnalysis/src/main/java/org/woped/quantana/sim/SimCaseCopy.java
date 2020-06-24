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
	
	public int getorigid() {
		SimCase o = original;
		while (o instanceof SimCaseCopy) {
			o = ((SimCaseCopy)o).getOriginal();
		}
		return o.getid();
	}
}
