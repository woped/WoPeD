package org.woped.metrics.metricsCalculation;

import org.antlr.runtime.RecognitionException;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.config.IMetricsConfiguration;
import org.woped.core.controller.IEditor;
import org.woped.core.model.ArcModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;
import org.woped.metrics.exceptions.CalculateFormulaException;
import org.woped.metrics.exceptions.FormulaVariableNotFoundException;
import org.woped.metrics.exceptions.InfiniteRecursiveFormulaCallException;
import org.woped.metrics.exceptions.NaNException;
import org.woped.metrics.formulaEnhancement.EnhancementException;
import org.woped.metrics.formulaEnhancement.FormulaEnhancementList;
import org.woped.metrics.jbpt.RPSTHandler;
import org.woped.qualanalysis.coverabilitygraph.model.ReachabilityGraphModelUsingMarkingNet;
import org.woped.qualanalysis.coverabilitygraph.gui.CoverabilityGraph;
import org.woped.qualanalysis.service.IQualanalysisService;
import org.woped.qualanalysis.service.QualAnalysisServiceFactory;
import org.woped.qualanalysis.soundness.algorithms.AlgorithmFactory;
import org.woped.qualanalysis.soundness.datamodel.PlaceNode;
import org.woped.qualanalysis.soundness.marking.IMarking;
import org.woped.qualanalysis.soundness.marking.IMarkingNet;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
	private Set<String> currentDynamicCall = null;
	private boolean doNotdisplay;
	private MetricsHighlighter highlight = new MetricsHighlighter();
	private boolean algorithmHighlighting = ConfigurationManager.getConfiguration().isUseAlgorithmHighlighting();
	/**
	 * @author Tobias Lorentz
	 * Key 		= Variable-Name
	 * Value 	= Variable-Value 
	 */
	private HashMap<String, Double> variableValueBuffer;
	
	/**
	 * Creates a metrics calculator, to hold information such as prior results, highlighting information etc.
	 * 
	 * @param editor	Editor on which's model the analysis will be based
	 */
	public MetricsCalculator(IEditor editor){
		mec= editor.getModelProcessor().getElementContainer();
		new RPSTHandler(mec);
		this.editor = editor;
		this.variableValueBuffer = new HashMap<String, Double>();
		highlight = new MetricsHighlighter();
	}

	/**
	 * This Method should be used only to validate a formula;
	 * The formula is valid, if you do not get an exception
	 *
	 * @param formula a Metrics-Formula
	 * @throws CalculateFormulaException
	 */
	public static void checkFormula(String formula) throws CalculateFormulaException {
		MetricsInterpreter.interpretString(formula, null, null, false, true);
	}

	/**
	 * @param variableName
	 * @return True if the Variable exists
	 */
	public static boolean isVariableValid(String variableName) {
		IMetricsConfiguration metricsConfig = ConfigurationManager.getMetricsConfiguration();

		if (metricsConfig.hasAlgorithmFormula(variableName)) {
			return true;
		}

		if (metricsConfig.hasAlgorithmMethod(variableName)) {
			return true;
		}

		if (metricsConfig.hasVariableFormula(variableName)) {
			return true;
		}

		return metricsConfig.hasVariableMethod(variableName);

	}
	
	public MetricsHighlighter getHighlighter(){
		return highlight;
	}
	
	/*
	 * ATTENTION:
	 * The following block contains dynamically called calculation methods.
	 * They all seem unused because their method names are invoked based on XML input - at runtime they will get invoked!
	 *
	 * If highlight.addHighlight is called, the method contains highlighting information, otherwise it does not.
	 * Logic descriptions are given in the called methods and classes where required (See Javadocs of each)
	 *
	 * ----- BEGIN OF GENERICALLY CALLED METHODS -----
	 *
	 */
	@SuppressWarnings("unused")
	private double calculateN(){
		return calculateT() + calculateP();
	}
	
	public double calculateT(){
		Map<String, AbstractPetriNetElementModel> transitions = getTransitions();
		// Highlighting
		Set<String> nodeids = new HashSet<String>();
		for(String key:transitions.keySet())
			nodeids.add(transitions.get(key).getId());
		highlight.addHighlight(currentDynamicCall, nodeids, null, doNotdisplay);

		return transitions.size();
	}

	private double calculateP(){
		Map<String, AbstractPetriNetElementModel> places =  mec.getElementsByType(AbstractPetriNetElementModel.PLACE_TYPE);
		// Highlighting
		Set<String> nodeids = new HashSet<String>();
		for(String key:places.keySet())
			nodeids.add(places.get(key).getId());
		highlight.addHighlight(currentDynamicCall, nodeids, null, doNotdisplay);

		return places.size();
	}
	
	public double calculateA(){
		Map<String, ArcModel> arcs = mec.getArcMap();

		// Highlighting
		Set<String> arcids = new HashSet<String>();
		for(String key:arcs.keySet())
			arcids.add(arcs.get(key).getId());
		highlight.addHighlight(currentDynamicCall, null, arcids, doNotdisplay);

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
		double cyclic = lpd.getNumberOfCyclicNodes();

		// Highlighting
		Set<String> nodeids = lpd.getHighlightedNodes();
		highlight.addHighlight(currentDynamicCall, nodeids, null, doNotdisplay);
		return cyclic;
	}
	
	@SuppressWarnings("unused")
	private double calculateDia() throws NaNException{
		if(lpd == null) lpd = new LinearPathDescriptor(mec.getIdMap(), mec.getArcMap());
		double lpmax = lpd.longestPath();
		// Highlighting
		Set<String> nodeids = lpd.getHighlightedNodes();
		Set<String> arcids = lpd.getHighlightedArcIds();
		highlight.addHighlight(currentDynamicCall, nodeids, arcids, doNotdisplay);
		return lpmax;
	}
	
	@SuppressWarnings("unused")
	private double calculateTS(){
		Map<String, AbstractPetriNetElementModel> operators = mec.getElementsByType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
		Map<String, Map<String,Object>> origMap = mec.getIdMap();
		double ts = 0;

		Set<String> nodeids = new HashSet<String>();

		for(String key:operators.keySet())
			if(origMap.get(key).size() > 2) {
				ts += origMap.get(key).size()-2;
				nodeids.add(key);
			}

		highlight.addHighlight(currentDynamicCall, nodeids, null, doNotdisplay);

		return ts;
	}
	
	@SuppressWarnings("unused")
	private double calculateCut() throws NaNException{
		if(lpd == null) lpd = new LinearPathDescriptor(mec.getIdMap(), mec.getArcMap());
		double cutVert = lpd.cutVertices();

		//Highlighting
		highlight.addHighlight(currentDynamicCall, lpd.getHighlightedNodes(), null, doNotdisplay);
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
		IMarkingNet net = reach.getMarkingNet();
		Set<IMarking> markings = net.getMarkings();
		for (IMarking mark : markings) {
			for(PlaceNode place: mark.getPlaces()){
				if(mark.isPlaceUnbound(place)) return Double.POSITIVE_INFINITY;
			}
			Map<String, Integer> markingMap = mark.getMarking();
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
		else if (type == 2) map = mec.getElementsByType(AbstractPetriNetElementModel.PLACE_TYPE).keySet();
		else map = origMap.keySet();

		Set<String> nodeids = new HashSet<String>(); // Highlighting
		for(String key:map)
			if(outboundLines.get(key) < 2 && (!inboundLines.containsKey(key) || inboundLines.get(key) < 2) ){
				seqn++;
				// Highlighting
				nodeids.add(key);
			}

		highlight.addHighlight(currentDynamicCall, nodeids, null, doNotdisplay);
		return seqn;
	}

	/**
	 * ----- END OF GENERICALLY CALLED METHODS -----
	 */
	
	@SuppressWarnings("unused")
	private double calculateCM(){
		ReachabilityGraphModelUsingMarkingNet reach = new ReachabilityGraphModelUsingMarkingNet(editor);
		IMarkingNet net = reach.getMarkingNet();
		CoverabilityGraph graph = reach.getGraph();
		int edgeCount = reach.getEdges().size();
		int verticesCount = reach.getNodes().size();
		double strongReaches = calculateStrongReaches(net);
		return edgeCount - verticesCount + strongReaches;
	}
	
	private double calculateStrongReaches(IMarkingNet net){
		return AlgorithmFactory.createSccTest(net).getStronglyConnectedComponents().size();
	}
	
	/**
	 * Returns a map of all nodes that can be considered transitions
	 */
	private Map<String,AbstractPetriNetElementModel> getTransitions(){
		Map<String, AbstractPetriNetElementModel> transitions = new HashMap<String, AbstractPetriNetElementModel>();
		Map<String, AbstractPetriNetElementModel> partialTransitions;
		partialTransitions = mec.getElementsByType(AbstractPetriNetElementModel.SUBP_TYPE);
		for(String key:partialTransitions.keySet())
			transitions.put(key,partialTransitions.get(key));
		partialTransitions = mec.getElementsByType(AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE);
		for(String key:partialTransitions.keySet())
			transitions.put(key,partialTransitions.get(key));
		partialTransitions = mec.getElementsByType(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE);
		for(String key:partialTransitions.keySet())
			transitions.put(key,partialTransitions.get(key));

		return transitions;
	}
	
	/**
	 * Calculates a formula (recursion where necessary)
	 *
	 * @param formula	Formula to be calculated
	 * @return			Result of the calculation
	 * @throws CalculateFormulaException	Exception thrown (e.g. invalid formula)
	 */
	public double calculate(String formula)throws CalculateFormulaException{
		return calculate(formula, false);
	}

	/**
	 *
	 * Calculates a formula (recursion where necessary)
	 *
	 * @param formula		Formula to be calculated
	 * @param syntaxCheck	Whether or not the syntax should be checked
	 * @return				Result of the calculation
	 * @throws CalculateFormulaException	Exception thrown (e.g. invalid formula)
	 */
	public double calculate(String formula, boolean syntaxCheck) throws CalculateFormulaException{
		return calculate(formula, new HashSet<String>(), false, syntaxCheck);
	}

	/**
	 *
	 * Calculates a formula (recursion where necessary)
	 *
	 * @param formula		Formula to be calculated
	 * @param stack			Stack of previously called parent elements in the formula (e.g. if this is C in A = B * C, the stack will contain A)
	 * @param doNotDisplay	Whether or not the highlighting should be displayed
	 * @return				Result of the calculation
	 * @throws CalculateFormulaException	Exception thrown (e.g. invalid formula)
	 */
	public double calculate(String formula, HashSet<String> stack, boolean doNotDisplay) throws CalculateFormulaException{
		return MetricsInterpreter.interpretString(formula, this, stack,doNotDisplay, false);
	}

	/**
	 *
	 * Calculates a formula (recursion where necessary)
	 *
	 * @param formula		Formula to be calculated
	 * @param stack			Stack of previously called parent elements in the formula (e.g. if this is C in A = B * C, the stack will contain A)
	 * @param doNotDisplay	Whether or not the highlighting should be displayed
	 * @param syntaxCheck	Whether or not the syntax should be checked
	 * @return				Result of the calculation
	 * @throws CalculateFormulaException	Exception thrown (e.g. invalid formula)
	 */
	public double calculate(String formula, HashSet<String> stack, boolean doNotDisplay, boolean syntaxCheck) throws CalculateFormulaException{
		String specialHighlighting = metricsConfig.getHighlightingFormula(formula);
		if(specialHighlighting.length()>0){
			List<String> addHighlights = new ArrayList<String>();
			List<String> removeHighlights = new ArrayList<String>();
			for(String s:specialHighlighting.split(","))
				if(s.trim().startsWith("!"))
					removeHighlights.add(s.trim().substring(1));
				else
					addHighlights.add(s.trim());
			HashSet<String> fakeStack = new HashSet<String>();
			fakeStack.add(formula);
			currentDynamicCall = fakeStack;
			for(String highlight:addHighlights)
				MetricsInterpreter.interpretString(highlight, this, fakeStack,false, false);
			highlight.setInverted(true);
			for(String highlight:removeHighlights)
				MetricsInterpreter.interpretString(highlight, this, fakeStack,false, false);
			highlight.setInverted(false);
		}
		currentDynamicCall = new HashSet<String>();
		return MetricsInterpreter.interpretString(formula, this, stack,doNotDisplay, syntaxCheck);
	}
	
	/**
	 * @param token the name of the variable which should be calculated
	 * @param stack list from the top-level-variable down to the level where the actual variable is in
	 * @param doNotDisplay if this flag is true, those elements should not be highlighed on the screen
	 * @return the calculated value
	 * @throws CalculateFormulaException
	 * @author Tobias Lorentz
	 * @author Holger Kraus
	 * @throws RecognitionException
	 */
	public double calculateVariable(String token, HashSet<String> stack, boolean doNotDisplay) throws CalculateFormulaException, RecognitionException {

		double result;
		if(algorithmHighlighting)currentDynamicCall.add(token);
		if(this.isVariableAlreadyCalculated(token, stack)){
			//This Formula is already called--> unless recursion --> Error
			throw new InfiniteRecursiveFormulaCallException(token);
		}

		try{
		//Put the value of the local doNotDisplay Variable into the global one for accesing the value by the dynamic called functions
		this.doNotdisplay = doNotDisplay;

		//Clone HashSet
 		HashSet<String> newStack = (HashSet<String>) stack.clone();

		//Add the actual Token to the Stack
		newStack.add(token);

		if (variableValueBuffer.containsKey(token)){
			highlight.mergeHighlights(currentDynamicCall, token, doNotdisplay);
			currentDynamicCall.remove(token);
			return variableValueBuffer.get(token);
		}
		else if (metricsConfig.hasAlgorithmFormula(token)){
			try{
				result =  calculate(metricsConfig.getAlgorithmFormula(token), newStack, doNotDisplay);
			}catch(EnhancementException ee){
				//The formula was enhanced. The value for this enhanced Formula
				// is stored inside the exception object.
				/** @author Tobias Lorentz */
				result = ee.getValue();
				//Add this formula to the EnhancementList
				FormulaEnhancementList.getInstance().AddFormula(token, ee.getEnhancedFormula());
			}
			variableValueBuffer.put(token, result);
			currentDynamicCall.remove(token);
			return result;
		}
		else if (metricsConfig.hasAlgorithmMethod(token)){
			currentDynamicCall.add(token);
			result = Double.parseDouble(getClass().getDeclaredMethod(metricsConfig.getAlgorithmMethod(token), new Class[0]).invoke(this, new Object[0]).toString());
			variableValueBuffer.put(token, result);
			currentDynamicCall.remove(token);
			return result;
		}
		else if(metricsConfig.hasVariableFormula(token)){
			try{
				result = calculate(metricsConfig.getVariableFormula(token), newStack, doNotDisplay);
			}catch(EnhancementException ee){
				//The formula was enhanced. The value for this enhanced Formula
				// is stored inside the exception object.
				/** @author Tobias Lorentz */
				result = ee.getValue();
				//Add this formula to the EnhancementList
				FormulaEnhancementList.getInstance().AddFormula(token, ee.getEnhancedFormula());
			}
			variableValueBuffer.put(token, result);
			return result;
		}
		else if(metricsConfig.hasVariableMethod(token)){
			currentDynamicCall.add(token);
			result = Double.parseDouble(getClass().getDeclaredMethod(metricsConfig.getVariableMethod(token), new Class[0]).invoke(this,new Object[0]).toString());
			variableValueBuffer.put(token, result);
			currentDynamicCall.remove(token);
			return result;
		}
		}catch(InvocationTargetException ite){
			if(ite.getCause() instanceof CalculateFormulaException)
				throw (CalculateFormulaException)ite.getCause();
		}catch (CalculateFormulaException cfe){
			throw cfe;
		}catch(Exception e){
			System.err.println("Invalid method name: "+token+" ("+metricsConfig.getVariableMethod(token)+")");
			throw new CalculateFormulaException();
		}

		//Variable is not found --> Throw exception
		throw new FormulaVariableNotFoundException(token);

	}
	
	/**
	 * Checks whether a variable has already been calculated
	 *
	 * @param token	ID of the variable
	 * @param stack	Stack of previous calls
	 * @return		Boolean containing whether or not the variable had already been calculated
	 */
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
