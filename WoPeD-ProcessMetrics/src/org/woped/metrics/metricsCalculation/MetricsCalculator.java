package org.woped.metrics.metricsCalculation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IMetricsConfiguration;
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
	private IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
	
	public MetricsCalculator(ModelElementContainer mec){
		this.mec=mec;
	}
	
	private double calculateN(){
		return calculateT() + calculateP();
	}
	
	private double calculateT(){
		return getTransitions().size();
	}
	
	private double calculateP(){ 
		return mec.getElementsByType(PetriNetModelElement.PLACE_TYPE).size();
	}
	
	private double calculateA(){
		return mec.getArcMap().size();
	}
	
	private double calculateSeqN(){
		return calculateSequence(0);
	}
	
	private double calculateSeqT(){
		return calculateSequence(1);
	}
	
	private double calculateSeqP(){
		return calculateSequence(2);
	}
	
	private double calculateCycN(){
		if(lpd == null) lpd = new LinearPathDescriptor(mec.getIdMap(), mec.getArcMap());
		return lpd.getCyclicNodes();
	}
	
	private double calculateDia(){
		if(lpd == null) lpd = new LinearPathDescriptor(mec.getIdMap(), mec.getArcMap());
		return lpd.longestPath();
	}
	
	private double calculateTS(){
		Map<String, AbstractElementModel> operators = mec.getElementsByType(PetriNetModelElement.TRANS_OPERATOR_TYPE);
		Map<String, Map<String,Object>> origMap = mec.getIdMap();
		double ts = 0;
		
		for(String key:operators.keySet())
			if(origMap.get(key).size() > 2) ts += origMap.get(key).size()-2;
		
		return ts;
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
			outboundLines.put(key, origMap.get(key).size() - 1);
			// Adds itself as a parent reference for its children
			for(String subkey:origMap.get(key).keySet()){
				ArcModel arc = mec.getArcMap().get(subkey);
				if(arc == null) continue;
				String childKey = arc.getTargetId();
				
				if(inboundLines.containsKey(childKey))
					inboundLines.put(childKey, inboundLines.get(childKey) + 1);
				else
					inboundLines.put(childKey, 1);
			}
		}
		
		Set<String> map;
		if(type == 1) map = getTransitions().keySet();
		else if (type == 2) map = mec.getElementsByType(PetriNetModelElement.PLACE_TYPE).keySet();
		else map = origMap.keySet();
		for(String key:map)
			if(outboundLines.get(key) < 2 && (!inboundLines.containsKey(key) || inboundLines.get(key) < 2) )
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
		try{
		if (metricsConfig.hasAlgorithmFormula(token))
			return calculate(metricsConfig.getAlgorithmFormula(token));
		else if (metricsConfig.hasAlgorithmMethod(token))
	         return Double.parseDouble(getClass().getDeclaredMethod(metricsConfig.getAlgorithmMethod(token), new Class[0]).invoke(this,new Object[0]).toString());
		else if(metricsConfig.hasVariableFormula(token))
			return calculate(metricsConfig.getVariableFormula(token));
		else if(metricsConfig.hasVariableMethod(token))
			return Double.parseDouble(getClass().getDeclaredMethod(metricsConfig.getVariableMethod(token), new Class[0]).invoke(this,new Object[0]).toString());
		}catch(Exception e){
			// One of the many kinds of invocation exception occurred
			e.printStackTrace();
		}
	    
		return -1;
	     
	}

}
