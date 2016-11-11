package org.woped.qualanalysis.soundness.builder.currentmarking;

import org.woped.qualanalysis.soundness.datamodel.ILowLevelPetriNet;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.marking.Marking;

public class CurrentMarking extends AbstractCurrentMarking{

    public CurrentMarking(ILowLevelPetriNet lolNet, Boolean useVirtualTokens) {
        super(lolNet, useVirtualTokens);
        calcCurrentMarking();
    }
	
	private void calcCurrentMarking(){
		PlaceNode[] places = lolNet.getPlaces().toArray(new PlaceNode[lolNet.getPlaces().size()]);
        int[] tokens = new int[places.length];
        boolean[] placeUnlimited = new boolean[places.length];
        for (int i = 0; i < tokens.length; i++) {
        	if(useVirtualTokens){
        		tokens[i] = places[i].getVirtualTokenCount();
        	} else {
        		tokens[i] = places[i].getTokenCount();
        	}
            placeUnlimited[i] = false;
        }
        marking = new Marking(tokens, places, placeUnlimited);
	}
}
