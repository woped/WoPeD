package org.woped.metrics.metricsCalculation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IMetricsConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.metrics.exceptions.CalculateFormulaException;
import org.woped.metrics.exceptions.FormulaVariableNotFoundException;
import org.woped.metrics.exceptions.InfiniteRecursiveFormulaCallException;
import org.woped.metrics.exceptions.NaNException;
import org.woped.metrics.jbpt.RPSTHandler;
import org.woped.qualanalysis.reachabilitygraph.data.AbstractReachabilityGraphModel;
import org.woped.qualanalysis.reachabilitygraph.data.ReachabilityGraphModelUsingMarkingNet;
import org.woped.qualanalysis.reachabilitygraph.gui.ReachabilityJGraph;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;
import org.woped.qualanalysis.soundness.algorithms.AlgorithmFactory;
import org.woped.qualanalysis.soundness.marking.Marking;
import org.woped.qualanalysis.soundness.marking.MarkingNet;

/**
 * 
 * @author Holger Kraus
 * @author Tobias Lorentz
 *
 */

public class MetricsCalculator {

	private ModelElementContainer mec;
	private LinearPathDescriptor lpd;
	private RPSTHandler rpst;
	private IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();
	private IEditor editor;
	private String currentDynamicCall = "";
	private boolean doNotdisplay;
	/**
	 * @author Tobias Lorentz
	 * Key 		= Variable-Name
	 * Value 	= Variable-Value 
	 */
	private HashMap<String, Double> variableValueBuffer;
	
	public MetricsCalculator(IEditor editor){
		mec= editor.getModelProcessor().getElementContainer();
		new RPSTHandler(mec);
		this.editor = editor;
		this.variableValueBuffer = new HashMap<String, Double>();
	}
	
	@SuppressWarnings("unused")
	private double calculateN(){
		return calculateT() + calculateP();
	}
	
	public double calculateT(){
		Map<String, AbstractElementModel> transitions = getTransitions();
		// Highlighting
		Set<String> nodeids = new HashSet<String>();
		for(String key:transitions.keySet())
			nodeids.add(transitions.get(key).getId());
		MetricsHighlighter.addHighlight(currentDynamicCall, nodeids, null, doNotdisplay);
		
		return transitions.size();
	}
	
	private double calculateP(){ 
		Map<String, AbstractElementModel> places =  mec.getElementsByType(PetriNetModelElement.PLACE_TYPE);
		// Highlighting
		Set<String> nodeids = new HashSet<String>();
		for(String key:places.keySet())
			nodeids.add(places.get(key).getId());
		MetricsHighlighter.addHighlight(currentDynamicCall, nodeids, null, doNotdisplay);
		
		return places.size();
	}
	
	public double calculateA(){
		Map<String, ArcModel> arcs = mec.getArcMap();
		
		// Highlighting
		Set<String> arcids = new HashSet<String>();
		for(String key:arcs.keySet())
			arcids.add(arcs.get(key).getId());
		MetricsHighlighter.addHighlight(currentDynamicCall, null, arcids, doNotdisplay);
		
		return arcs.size();
	}
	
	@SuppressWarnings("unused")
	private double calculateTreeComp(){
		if(rpst == null) rpst = new RPSTHandler(mec);
		return rpst.getComonents();
	}
	
	@SuppressWarnings("unused")
	private double calculateTrivialComp(){
		if(rpst == null) rpst = new RPSTHandler(mec);
		return rpst.getTrivial();
	}
	
	@SuppressWarnings("unused")
	private double calculatePolyComp(){
		if(rpst == null) rpst = new RPSTHandler(mec);
		return rpst.getPolygon();
	}
	
	@SuppressWarnings("unused")
	private double calculateBondComp(){
		if(rpst == null) rpst = new RPSTHandler(mec);
		return rpst.getBond();
	}
	
	@SuppressWarnings("unused")
	private double calculateRigidComp(){
		if(rpst == null) rpst = new RPSTHandler(mec);
		return rpst.getRigid();
	}
	
	@SuppressWarnings("unused")
	private double calculateNrigid(){
		if(rpst == null) rpst = new RPSTHandler(mec);
		return rpst.getNodesInRigid();
	}
	
	@SuppressWarnings("unused")
	private double calculateDepth(){
		if(rpst == null) rpst = new RPSTHandler(mec);
		return rpst.getDepth();
	}
	
	@SuppressWarnings("unused")
	private double calculateSeqN(){
		return calculateSequence(0);
	}
	
	@SuppressWarnings("unused")
	private double calculateSeqT(){
		return calculateSequence(1);
	}
	
	@SuppressWarnings("unused")
	private double calculateSeqP(){
		return calculateSequence(2);
	}
	
	@SuppressWarnings("unused")
	private double calculateCycN(){
		if(lpd == null) lpd = new LinearPathDescriptor(mec.getIdMap(), mec.getArcMap());
		double cyclic = lpd.getCyclicNodes();
		
		// Highlighting
		Set<String> nodeids = lpd.getHighlightedNodes();
		MetricsHighlighter.addHighlight(currentDynamicCall, nodeids, null, doNotdisplay);
		return cyclic;
	}
	
	@SuppressWarnings("unused")
	private double calculateDia() throws NaNException{
		if(lpd == null) lpd = new LinearPathDescriptor(mec.getIdMap(), mec.getArcMap());
		double lpmax = lpd.longestPath();
		// Highlighting
		Set<String> nodeids = lpd.getHighlightedNodes();
		Set<String> arcids = lpd.calculateHighlightedArcs();
		MetricsHighlighter.addHighlight(currentDynamicCall, nodeids, arcids, doNotdisplay);
		return lpmax;
	}
	
	@SuppressWarnings("unused")
	private double calculateTS(){
		Map<String, AbstractElementModel> operators = mec.getElementsByType(PetriNetModelElement.TRANS_OPERATOR_TYPE);
		Map<String, Map<String,Object>> origMap = mec.getIdMap();
		double ts = 0;
		
		Set<String> nodeids = lpd.getHighlightedNodes();
		
		for(String key:operators.keySet())
			if(origMap.get(key).size() > 2) {
				ts += origMap.get(key).size()-2;
				nodeids.add(key);
			}
		
		MetricsHighlighter.addHighlight(currentDynamicCall, nodeids, null, doNotdisplay);
		
		return ts;
	}
	
	@SuppressWarnings("unused")
	private double calculateCut() throws NaNException{
		if(lpd == null) lpd = new LinearPathDescriptor(mec.getIdMap(), mec.getArcMap());
		double cutVert = lpd.cutVertices();
		
		//Highlighting
		MetricsHighlighter.addHighlight(currentDynamicCall, lpd.getHighlightedNodes(), null, doNotdisplay);
		return cutVert;
	}
	
	@SuppressWarnings("unused")
	private double calculateTP(){
		IQualanalysisService qualanService = QualAnalysisServiceFactory.createNewQualAnalysisService(editor);
		return qualanService.getTPHandles().size();
	}
	
	@SuppressWarnings("unused")
	private double calculatePT(){
		IQualanalysisService qualanService = QualAnalysisServiceFactory.createNewQualAnalysisService(editor);
		return qualanService.getPTHandles().size();
	}
	
	@SuppressWarnings("unused")
	private double calculateTM(){
		double max = 0;
		ReachabilityGraphModelUsingMarkingNet reach = new ReachabilityGraphModelUsingMarkingNet(editor);
		MarkingNet net = reach.getMarkingNet();
		Set<Marking> markings = net.getMarkings();
		for(Marking mark:markings){
			Map<String, Integer> markingMap = mark.getMarking(); 
			for(boolean infinite: mark.getPlaceUnlimited())
				if(infinite) return Double.POSITIVE_INFINITY;
			for(String id:markingMap.keySet())
				max = Math.max(max,markingMap.get(id));}
			
		return max;
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
		
		Set<String> nodeids = new HashSet<String>(); // Highlighting
		for(String key:map)
			if(outboundLines.get(key) < 2 && (!inboundLines.containsKey(key) || inboundLines.get(key) < 2) ){
				seqn++;
				// Highlighting
				nodeids.add(key);
			}
				
		MetricsHighlighter.addHighlight(currentDynamicCall, nodeids, null, doNotdisplay);
		return seqn;
	}
	
	@SuppressWarnings("unused")
	private double calculateCM(){
		ReachabilityGraphModelUsingMarkingNet reach = new ReachabilityGraphModelUsingMarkingNet(editor);
		MarkingNet net = reach.getMarkingNet();
		ReachabilityJGraph graph = reach.getGraph();
		return AbstractReachabilityGraphModel.edgeCount(graph)-AbstractReachabilityGraphModel.verticeCount(graph)+calculateStrongReaches(net);
	}
	
	private double calculateStrongReaches(MarkingNet net){
		return AlgorithmFactory.createSccTest(net).getStronglyConnectedComponents().size();
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
	
	public double calculate(String formula) throws CalculateFormulaException{
			return calculate(formula, new HashSet<String>(), false);
	}
	public double calculate(String formula, HashSet<String> stack, boolean doNotDisplay) throws CalculateFormulaException{
		return MetricsInterpreter.interpretString(formula, this, stack,doNotDisplay);
	}
	
	/**
	 * @param token the name of the variable which should be calculated
	 * @param stack list from the top-level-variable down to the level where the actual variable is in
	 * @param doNotDisplay if this flag is true, those elements should not be highlighed on the screen
	 * @return the calculated value
	 * @throws CalculateFormulaException
	 * @author Tobias Lorentz
	 * @author Holger Kraus
	 */
	public double calculateVariable(String token, HashSet<String> stack, boolean doNotDisplay) throws CalculateFormulaException {
		
		double result;
		
		if(this.isVariableAlreadyCalculated(token, stack)){
			//This Formula is already called--> unless recursion --> Error
			throw new InfiniteRecursiveFormulaCallException(token);
		}
		
		try{
		//Put the value of the local doNotDisplay Variable into the global one for accesing the value by the dynamic called functions
		this.doNotdisplay = doNotDisplay;
			
		//Clone HashSet
		@SuppressWarnings("unchecked")
		HashSet<String> newStack = (HashSet<String>) stack.clone();
		
		//Add the actual Token to the Stack
		newStack.add(token);
		
		if (variableValueBuffer.containsKey(token)){
			return variableValueBuffer.get(token);
		}		
		else if (metricsConfig.hasAlgorithmFormula(token)){
			result =  calculate(metricsConfig.getAlgorithmFormula(token), newStack, doNotDisplay);
			variableValueBuffer.put(token, result);
			return result;
		}			
		else if (metricsConfig.hasAlgorithmMethod(token)){
			currentDynamicCall = token;
			result = Double.parseDouble(getClass().getDeclaredMethod(metricsConfig.getAlgorithmMethod(token), new Class[0]).invoke(this,new Object[0]).toString()); 
			variableValueBuffer.put(token, result);
			return result;
		}
		else if(metricsConfig.hasVariableFormula(token)){
			result = calculate(metricsConfig.getVariableFormula(token), newStack, doNotDisplay);
			variableValueBuffer.put(token, result);
			return result;
		}
		else if(metricsConfig.hasVariableMethod(token)){
			currentDynamicCall = token;
			result = Double.parseDouble(getClass().getDeclaredMethod(metricsConfig.getVariableMethod(token), new Class[0]).invoke(this,new Object[0]).toString());
			variableValueBuffer.put(token, result);
			return result;
		}
		}catch(InvocationTargetException ite){
			if(ite.getCause() instanceof CalculateFormulaException)
				throw (CalculateFormulaException)ite.getCause();
		}catch (CalculateFormulaException cfe){
			throw cfe;
		}catch(Exception e){
			System.err.println("Invalid method name: "+token+" ("+metricsConfig.getVariableMethod(token)+")");
		}
	    
		//Variable is not found --> Throw exception
		throw new FormulaVariableNotFoundException(token);
	     
	}
	private boolean isVariableAlreadyCalculated(String token, HashSet<String> stack){
		//Check if the Variable exists at the Stack
		for(Iterator<String> iter = stack.iterator();iter.hasNext();){
			if (iter.next().equalsIgnoreCase(token)){
				return true;
			}
		}
		
		return false;
		
	}
	
	/**
	 * @author Tobias Lorentz
	 * The Buffer has to be cleared to recalculate the values if the net has changed
	 */
	public void clearVariableValueBuffer(){
		this.variableValueBuffer.clear();
	}

}
