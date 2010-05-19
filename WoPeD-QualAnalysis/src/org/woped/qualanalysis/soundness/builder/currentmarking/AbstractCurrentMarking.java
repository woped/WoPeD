package org.woped.qualanalysis.soundness.builder.currentmarking;

import org.woped.qualanalysis.soundness.datamodel.LowLevelPetriNet;
import org.woped.qualanalysis.soundness.marking.IMarking;

public class AbstractCurrentMarking{

	LowLevelPetriNet lolNet = null;
	Boolean useVirtualTokens = false;
	IMarking marking = null;
	
	public AbstractCurrentMarking(LowLevelPetriNet lolNet, Boolean useVirtualTokens){
		this.lolNet = lolNet;
		this.useVirtualTokens = useVirtualTokens;
	}
	
	public IMarking getMarking(){
		return marking;
	}
	
	
}
