package org.woped.processmetrics.metricsCalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.PetriNetModelElement;

/**
 * 
 * @author Holger Kraus
 *
 */

public class MetricsCalculator {

	private ModelElementContainer mec;
	private LinearPathDescriptor lpd;
	
	public MetricsCalculator(ModelElementContainer mec){
		this.mec=mec;
	}
	
	public double calculateN(){
		return calculateT()+calculateP();
	}
	
	public double calculateT(){
		return getTransitions().size();
	}
	
	public double calculateP(){ 
		return mec.getElementsByType(PetriNetModelElement.PLACE_TYPE).size();
	}
	
	public double calculateA(){
		return mec.getArcMap().size();
	}
	
	public double calculateSeqN(){
		return calculateSequence(0);
	}
	
	public double calculateSeqT(){
		return calculateSequence(1);
	}
	
	public double calculateSeqP(){
		return calculateSequence(2);
	}
	
	public double calculateCycN(){
		if(lpd == null) lpd = new LinearPathDescriptor(mec.getIdMap(), mec.getArcMap());
		return lpd.getCyclicNodes();
	}
	
	/**
	 * 
	 * @param type
	 * 	0 = all, 1 = transitions, 2 = places
	 * @return
	 */
	private double calculateSequence(int type){
		HashMap<String, Integer> inboundLines = new HashMap<String, Integer>();
		HashMap<String, Integer> outboundLines = new HashMap<String, Integer>();
		Map<String, Map<String,Object>> origMap = mec.getIdMap();
		double seqn = 0;
		
		for(String key:origMap.keySet()){
			// Number of children
			outboundLines.put(key, origMap.get(key).size()-1);
			// Adds itself as a parent reference for its children
			for(String subkey:origMap.get(key).keySet()){
				ArcModel arc = mec.getArcMap().get(subkey);
				if(arc == null) continue;
				String childKey = arc.getTargetId();
				
				if(inboundLines.containsKey(childKey))
					inboundLines.put(childKey, inboundLines.get(childKey)+1);
				else
					inboundLines.put(childKey, 1);
			}
		}
		
		Set<String> map;
		if(type == 1) map = getTransitions().keySet();
		else if (type == 2) map = mec.getElementsByType(PetriNetModelElement.PLACE_TYPE).keySet();
		else map = origMap.keySet();
		for(String key:map)
			if(outboundLines.get(key)<2 && (!inboundLines.containsKey(key) || inboundLines.get(key)<2) )
				seqn++;

		return seqn;
	}
	
	
	private Map<String,AbstractElementModel> getTransitions(){
		Map<String, AbstractElementModel> transitions = new HashMap<String, AbstractElementModel>();
		Map<String, AbstractElementModel> partialTransitions;
		partialTransitions = mec.getElementsByType(PetriNetModelElement.SUBP_TYPE);
		for(String key:partialTransitions.keySet())
			transitions.put(key,partialTransitions.get(key));
		partialTransitions = mec.getElementsByType(PetriNetModelElement.TRANS_SIMPLE_TYPE);
		for(String key:partialTransitions.keySet())
			transitions.put(key,partialTransitions.get(key));
		partialTransitions = mec.getElementsByType(PetriNetModelElement.TRANS_OPERATOR_TYPE);
		for(String key:partialTransitions.keySet())
			transitions.put(key,partialTransitions.get(key));
		return transitions;
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
         }else if(token.equalsIgnoreCase("SeqN")){
          	return this.calculateSeqN();
         }else if(token.equalsIgnoreCase("SeqP")){
           	return this.calculateSeqP();
         }else if(token.equalsIgnoreCase("SeqT")){
          	return this.calculateSeqT();
         }else if(token.equalsIgnoreCase("RN")){
           	return this.calculate("N - SeqN");
         }else if(token.equalsIgnoreCase("RP")){
            	return this.calculate("P - SeqP");
         }else if(token.equalsIgnoreCase("RT")){
            	return this.calculate("T - SeqT");
         }else if(token.equalsIgnoreCase("CycN")){
         	return this.calculateCycN();
          	
          	
         }else if(token.equalsIgnoreCase("D")){
          	return this.calculate("A / (N * (N - 1))");
         }else if(token.equalsIgnoreCase("CNC")){
           	return this.calculate("(A + 1) / N");
         }else if(token.equalsIgnoreCase("DeN")){
            	return this.calculate("A / N");
         }else if(token.equalsIgnoreCase("DeP")){
         	return this.calculate("A / P");
         }else if(token.equalsIgnoreCase("DeT")){
         	return this.calculate("A / T");
         }else if(token.equalsIgnoreCase("Seq")){
          	return this.calculate("SeqN / N");
         }else if(token.equalsIgnoreCase("RR")){
           	return this.calculate("RN / N");
         }else if(token.equalsIgnoreCase("CH")){
            	return this.calculate("RT / T * log2( RT / T ) + RP / P * log2( RP / P)");
         }else if(token.equalsIgnoreCase("Cyc")){
         	return this.calculate("CycN / N ");   	
         }else{
        	 //TODO: Recursive call of the formula
        	 return 0;
         }
		
	     
	}

}
