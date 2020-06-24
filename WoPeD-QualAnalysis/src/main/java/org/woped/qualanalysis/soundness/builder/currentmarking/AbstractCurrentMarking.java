package org.woped.qualanalysis.soundness.builder.currentmarking;

import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.marking.IMarking;

public class AbstractCurrentMarking{

    ILowLevelPetriNet lolNet = null;
    Boolean useVirtualTokens = false;
    IMarking marking = null;

    public AbstractCurrentMarking(ILowLevelPetriNet lolNet, Boolean useVirtualTokens) {
        this.lolNet = lolNet;
        this.useVirtualTokens = useVirtualTokens;
    }
	
	public IMarking getMarking(){
		return marking;
	}
	
	
}
