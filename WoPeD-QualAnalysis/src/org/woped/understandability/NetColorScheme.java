package org.woped.understandability;

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;

import org.woped.core.analysis.NetAlgorithms;
import org.woped.core.analysis.StructuralAnalysis;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.petrinet.CombiOperatorTransitionModel;
import org.woped.core.model.petrinet.OperatorTransitionModel;
import org.woped.core.model.petrinet.PetriNetModelElement;
import org.woped.qualanalysis.simulation.controller.ReferenceProvider;


/**
 * @authors Matthias Mruzek and Markus Noeltner <br>
 * 
 * Depending on status of toolbar toggle button this class
 * does the coloring of handle pairs for each editor separately.
 * <br>
 * Created on 08.12.2008
 */

public class NetColorScheme implements INetColorScheme {
	
	private StructuralAnalysis structAnalysis = null;
	private ReferenceProvider MediatorReference  = new ReferenceProvider();
	private int maxColors = ConfigurationManager.getConfiguration().getUnderstandColors().length;
	private int currentColorNum = 0;
	private Color[] UnderstandColors = new Color[maxColors];
	
	
	public NetColorScheme () {
	}
	
	private int countEditors(){
		int result = MediatorReference.getMediatorReference().getUi().getAllEditors().size();
		return result;
	}
	
	public void update() {
		
		if (countEditors() >0){
			structAnalysis = new StructuralAnalysis(MediatorReference.getMediatorReference().getUi().getEditorFocus());
			resetColoringInformation();
			
			// Only apply coloring in case coloring is actually enabled.
			// This saves a lot of calculation time when coloring is not active
			if (ConfigurationManager.getConfiguration().getColorOn())
			{					
				startUnderstandColoring();			
			}
		}
	}

	
	public void startUnderstandColoring(){
		
		UnderstandColors = ConfigurationManager.getConfiguration().getUnderstandColors();
		// reset currentColorNum to first color in config colorset, otherwise loop over colors
		currentColorNum = 0;
		
		Iterator<Set<StructuralAnalysis.ClusterElement>> handleClusterSetIter = structAnalysis.getM_handleClusters().iterator();
		while (handleClusterSetIter.hasNext()){
			
			// Iterator for Handles
			Iterator<StructuralAnalysis.ClusterElement> handleClusterIter = handleClusterSetIter.next().iterator();
			while (handleClusterIter.hasNext()){
				// get current FlowNode element
				StructuralAnalysis.ClusterElement element = handleClusterIter.next();
				// refer from current FlowNode element to its parent PetriNet element
				// if the actual element cannot be found in the current focus window
				AbstractElementModel highlightElement = element.m_element;
				AbstractElementModel owningElement = null;
				if  ((!MediatorReference.getUIReference().getEditorFocus().getModelProcessor().getElementContainer().containsElement(element.m_element.getId())&&
						((owningElement=element.m_element.getRootOwningContainer().getOwningElement()) != null)))						
					highlightElement = owningElement; 
				
				//MN: If algorithm mode is set to 2 (with handle correction) do the "hack"				
				if (ConfigurationManager.getConfiguration().getAlgorithmMode() == 2){
					highlightElement = handleXorSplitPlaceRelation(highlightElement);
				}
				// activate coloring of this element
				highlightElement.setUnderstandColoringActive(ConfigurationManager.getConfiguration().getColorOn());
						
				Color newColor = UnderstandColors[currentColorNum];
	        	// Element is filled with color in AbstractElementView.java.
				// If element is a combi operator type and the element was found as a source,
				// set the secondary understandability color instead of the standard one
				if ((element.m_elementType == 1)&&
						(highlightElement instanceof CombiOperatorTransitionModel))
				{
					CombiOperatorTransitionModel combiOperator =
						(CombiOperatorTransitionModel)highlightElement;
					combiOperator.setSecondaryUnderstandabilityColor(newColor);
				}
				else
				{				
					highlightElement.setColor(newColor);
					// Special treatment for non-workflow net detection: 
					// All combi operators will need to have their secondary color
					// set even though they have not been detected to work as a sink.
					// The reason is that in non-workflow net detection mode
					// there is no information about whether the operator was a source or a sink
					// so it should consistently be displayed with the same color
					if ((ConfigurationManager.getConfiguration().getAlgorithmMode() != 0)&&
						(highlightElement instanceof CombiOperatorTransitionModel))
					{
						CombiOperatorTransitionModel combiOperator =
							(CombiOperatorTransitionModel)highlightElement;
						combiOperator.setSecondaryUnderstandabilityColor(newColor);						
					}
				}
			}
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
			currentElement.ResetUnderstandabilityColor();
		}
			
		//reset all transitions
		elementIter = structAnalysis.getTransitionsIterator();
		while (elementIter.hasNext()){
			currentElement = elementIter.next();
			currentElement.setUnderstandColoringActive(false);
			currentElement.ResetUnderstandabilityColor();
		}
		
		//reset all operators
		elementIter = structAnalysis.getOperatorsIterator();
		while (elementIter.hasNext()){
			currentElement = elementIter.next();
			currentElement.setUnderstandColoringActive(false);
			currentElement.ResetUnderstandabilityColor();
		}
	}
}