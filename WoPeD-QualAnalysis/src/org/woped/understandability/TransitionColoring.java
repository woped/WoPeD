package org.woped.understandability;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.text.Highlighter;

import org.woped.core.analysis.NetAlgorithms;
import org.woped.core.analysis.StructuralAnalysis;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ArcModel;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.qualanalysis.simulation.controller.ReferenceProvider;


/**
 * @authors Matthias Mruzek and Markus Noeltner <br>
 * 
 * this class walks through the net and collects
 * all and/xor split/join transitions 
 * then a method sets the color
 * <br>
 * Created on 08.12.2008
 */

public class TransitionColoring implements ITransitionColoring {
	
	private StructuralAnalysis structAnalysis = null;
	private IEditor focusedEditor = null;
	private ReferenceProvider MediatorReference  = new ReferenceProvider();
	private int maxColors = ConfigurationManager.getConfiguration().getUnderstandColors().length;
	private int currentColorNum = 0;
	private Color[] UnderstandColors = new Color[maxColors];
	
	
	public TransitionColoring () {
	}
	
	private int countEditors(){
		int result = MediatorReference.getMediatorReference().getUi().getAllEditors().size();
		return result;
	}
	
	public void update() {
		
		// check if an editor exists
		if (countEditors() >0){
			// Create a new structural analysis object for
			// the modified net
			structAnalysis = new StructuralAnalysis(MediatorReference.getMediatorReference().getUi().getEditorFocus());
			resetColoringInformation();
			startUnderstandColoring();
			MediatorReference.getUIReference().getEditorFocus().getGraph().updateUI();
		}
	}

	public void startUnderstandColoring(){
		
		// get the colors from config colorset
		UnderstandColors = ConfigurationManager.getConfiguration().getUnderstandColors();
		
		// reset currentColorNum to first color in config colorset, otherwise loop over colors
		currentColorNum = 0;
		
		// getArcconfiguration
		NetAlgorithms.ArcConfiguration arcConfig = new NetAlgorithms.ArcConfiguration();
		
		
		// Iterator for Set of Handles in Cluster
		Iterator<Set<AbstractElementModel>> handleClusterSetIter = structAnalysis.getM_handleClusters().iterator();
		while (handleClusterSetIter.hasNext()){
			
			// Iterator for Handles
			Iterator<AbstractElementModel> handleClusterIter = handleClusterSetIter.next().iterator();
			while (handleClusterIter.hasNext()){
				
				// get current FlowNode element
				AbstractElementModel element = (AbstractElementModel) handleClusterIter.next();
								
				// refer from current FlowNode element to its parent PetriNet element
				// if the actual element cannot be found in the current focus window
				AbstractElementModel highlightElement = element;
				AbstractElementModel owningElement = null;
				if  ((!MediatorReference.getUIReference().getEditorFocus().getModelProcessor().getElementContainer().containsElement(element.getId())&&
						((owningElement=element.getRootOwningContainer().getOwningElement()) != null)))						
					element = owningElement; 
				
				// HACK: 2 options for better understandability
				// ! not the algorithm correct element from the petrinet will be highlighted, but the element before
				// 1 check: if current element is a Place and next is only one XOR-SPLIT then color only the XOR-SPLIT and not the Place
				// check: if current element is a Place and predecessor is only one XOR-JOIN then color only the XOR-JOIN
				
				//MN: If algorithm mode is set to 0 (with handle correction) do the "hack"				
				if (ConfigurationManager.getConfiguration().getAlgorithmMode() == 0){
					highlightElement = handleXorSplitPlaceRelation(highlightElement);
				}
				
				// activate coloring of this element
				highlightElement.setUnderstandColoringActive(ConfigurationManager.getConfiguration().getColorOn());
						
	        	// element is filled with color in AbstractElementView.java
	        	highlightElement.setColor(UnderstandColors[currentColorNum]);
			
			}
			// increment color from config colorset
			currentColorNum++;
			
			// infinite loop over all colors
			if (currentColorNum == maxColors) {
				currentColorNum = 0;
			}
		}
	}
	
	private AbstractElementModel handleXorSplitPlaceRelation(AbstractElementModel highlightElement){
		if (highlightElement.getType() == PetriNetModelElement.PLACE_TYPE){
			// 1 Nachbereich
			Set<AbstractElementModel> postset = NetAlgorithms.getDirectlyConnectedNodes(highlightElement, NetAlgorithms.connectionTypeOUTBOUND);
			// Vorbereich
			Set<AbstractElementModel> preset = NetAlgorithms.getDirectlyConnectedNodes(highlightElement, NetAlgorithms.connectionTypeINBOUND);
			
			OperatorTransitionModel tempOpTransModel;
			
			if (postset.size() == 2) {
										
			Iterator<AbstractElementModel> postsetIter = postset.iterator();
				
				AbstractElementModel postsetCheck1, postsetCheck2;
					
					postsetCheck1 = postsetIter.next();
					postsetCheck2 = postsetIter.next();
										
					// wenn beide Transitionen vom Typ 2 sind, dann hole aus dem owning Container das VdA Element.
					postsetCheck1 =  (postsetCheck1.getRootOwningContainer().getOwningElement() != null)
					? postsetCheck1.getRootOwningContainer().getOwningElement() 
					: postsetCheck1;
					
					postsetCheck2 =  (postsetCheck2.getRootOwningContainer().getOwningElement() != null)
					? postsetCheck2.getRootOwningContainer().getOwningElement() 
					: postsetCheck2;
					
					if (postsetCheck1.equals(postsetCheck2) && postsetCheck1.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE) {
						
						tempOpTransModel = (OperatorTransitionModel) postsetCheck1;
						
						if (tempOpTransModel.getOperatorType() == OperatorTransitionModel.XOR_SPLIT_TYPE) {
							// System.out.println("Nachbereich geschaft");
							
							// activate coloring of this element
							highlightElement = tempOpTransModel;
						}
					}
			}
			
			
			else{
			
				
				if (preset.size() == 2) {
											
				Iterator<AbstractElementModel> presetIter = preset.iterator();
					
					AbstractElementModel presetCheck1, presetCheck2;
						
						presetCheck1 = presetIter.next();
						presetCheck2 = presetIter.next();
											
						// wenn beide Transitionen vom Typ 2 sind, dann hole aus dem owning Container das VdA Element.
						presetCheck1 =  (presetCheck1.getRootOwningContainer().getOwningElement() != null)
						? presetCheck1.getRootOwningContainer().getOwningElement() 
						: presetCheck1;
						
						presetCheck2 =  (presetCheck2.getRootOwningContainer().getOwningElement() != null)
						? presetCheck2.getRootOwningContainer().getOwningElement() 
						: presetCheck2;
						
						if (presetCheck1.equals(presetCheck2) && presetCheck1.getType() == PetriNetModelElement.TRANS_OPERATOR_TYPE) {
							
							tempOpTransModel = (OperatorTransitionModel) presetCheck1;
							
							if (tempOpTransModel.getOperatorType() == OperatorTransitionModel.XOR_JOIN_TYPE) {
								// System.out.println("Vorbereich fertig");
								
								// activate coloring of this element
								highlightElement = tempOpTransModel;
							}
						}
		
					
				}
			}
		}
		return highlightElement;
	}
	
	//! Reset all understandability coloring Information in the current petrinet  
	private void resetColoringInformation() {
		Iterator<AbstractElementModel> elementIter = structAnalysis.getPlacesIterator();
		AbstractElementModel currentElement = null;
		
		//reset all places
		while (elementIter.hasNext()){
			currentElement = elementIter.next();
			currentElement.setUnderstandColoringActive(false);
			currentElement.setColor(Color.WHITE);
		}
			
		//reset all transitions
		elementIter = structAnalysis.getTransitionsIterator();
		while (elementIter.hasNext()){
			currentElement = elementIter.next();
			currentElement.setUnderstandColoringActive(false);
			currentElement.setColor(Color.WHITE);
		}
		
		//reset all operators
		elementIter = structAnalysis.getOperatorsIterator();
		while (elementIter.hasNext()){
			currentElement = elementIter.next();
			currentElement.setUnderstandColoringActive(false);
			currentElement.setColor(Color.WHITE);
		}
	}
}