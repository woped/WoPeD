package org.woped.processmetrics.metricsCalculator;

import java.util.HashMap;
import java.util.Map;

import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.PetriNetModelElement;

/**
 * 
 * @author Holger Kraus
 *
 */

public class MetricsCalculator {

	private ModelElementContainer mec;
	
	public MetricsCalculator(ModelElementContainer mec){
		this.mec=mec;
	}
	
	public double calculateN(){
		return mec.getIdMap().size();
	}
	
	public double calculateT(){
		return mec.getElementsByType(PetriNetModelElement.TRANS_SIMPLE_TYPE).size()+mec.getElementsByType(PetriNetModelElement.TRANS_OPERATOR_TYPE).size();
	}
	
	public double calculateP(){ 
		return mec.getElementsByType(PetriNetModelElement.PLACE_TYPE).size();
	}
	
	public double calculateA(){
		return mec.getArcMap().size();
	}
	
	public double calculateSeqN(){
		HashMap<String, Integer> inboundLines = new HashMap<String, Integer>();
		HashMap<String, Integer> outboundLines = new HashMap<String, Integer>();
		Map<String, Map<String,Object>> origMap = mec.getIdMap();
		
		return 0;
	}
}
