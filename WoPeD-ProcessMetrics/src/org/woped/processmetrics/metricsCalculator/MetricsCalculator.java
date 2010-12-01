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
		return calculateT()+calculateP();
	}
	
	public double calculateT(){
		return mec.getElementsByType(PetriNetModelElement.SUBP_TYPE).size()+mec.getElementsByType(PetriNetModelElement.TRANS_SIMPLE_TYPE).size()+mec.getElementsByType(PetriNetModelElement.TRANS_OPERATOR_TYPE).size();
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
		double seqn = 0;
		
		for(String key:origMap.keySet()){
			// Number of children
			outboundLines.put(key, origMap.get(key).size()-1);
			// Adds itself as a parent reference for its children
			for(String subkey:origMap.get(key).keySet()){
				if(inboundLines.containsKey(subkey))
					inboundLines.put(subkey, inboundLines.get(subkey)+1);
				else
					inboundLines.put(subkey, 1);
			}
		}
		for(String key:origMap.keySet()){
			if(outboundLines.get(key)<2 && inboundLines.get(key)<2)
				seqn++;
		}
		return seqn;
	}
	
	public double calculate(String formula){
		return MetricsInterpreter.interpretString(formula, this);
	}
	
	public double calculateVariable(String token){
	     if (token.equalsIgnoreCase("N")){
         	return this.calculateN();
         }else if(token.equalsIgnoreCase("P")){
         	return this.calculateP();
         }else if(token.equalsIgnoreCase("T")){
         	return this.calculateT();
         }else if(token.equalsIgnoreCase("A")){
         	return this.calculateA();
         }else if(token.equalsIgnoreCase("D")){
          	return this.calculate("A / (N * (N-1))");
         }else{
        	 //TODO: Recursiv call of the formula
        	 return 0;
         }
		
	     
	}
}
