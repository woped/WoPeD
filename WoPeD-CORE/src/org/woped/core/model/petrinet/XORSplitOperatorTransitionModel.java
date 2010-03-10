package org.woped.core.model.petrinet;

import java.util.Iterator;

import org.jgraph.graph.DefaultPort;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;

@SuppressWarnings("serial")
public class XORSplitOperatorTransitionModel extends OperatorTransitionModel {

	public XORSplitOperatorTransitionModel(CreationMap map) {
		super(map, XOR_SPLIT_TYPE);
	}
	
    public void registerIncomingConnection(
    		PetriNetModelProcessor processor,
    		AbstractElementModel sourceModel)
    {
    	// create & add new arc From EACH Source to EACH SimpleTrans
    	// of Operator
    	ModelElementContainer outerContainer = processor.getElementContainer();
    	Iterator<String> sourceIter = outerContainer.getSourceElements(getId()).keySet()
    	.iterator();
    	while (sourceIter.hasNext())
    	{
    		Iterator<AbstractElementModel> simpleRootIter = getSimpleTransContainer().getRootElements()
    		.iterator();
    		AbstractElementModel tempSource = outerContainer
    		.getElementById(sourceIter.next());
    		while (simpleRootIter.hasNext())
    		{
    			// wenn simpletrans dann reference
    			PetriNetModelElement tempTarget = (PetriNetModelElement) simpleRootIter
    			.next();
    			if (tempTarget.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE)
    				addReference(getNextFreeArcID(processor),
    						(DefaultPort) tempSource.getChildAt(0),
    						(DefaultPort) tempTarget.getChildAt(0));
    		}
    	}    	
    }
    public void registerOutgoingConnection(
    		PetriNetModelProcessor processor,
    		AbstractElementModel targetModel)
    {
    	ModelElementContainer upperContainer = processor.getElementContainer();
    	
    	// Wenn die Referenztabelle von dem Operator nur diesen
    	// neuen Eintrag hat
    	if (upperContainer.getTargetElements(getId()).size() == 1
    			&& getSimpleTransContainer().getElementsByType(
    					PetriNetModelElement.TRANS_SIMPLE_TYPE)
    					.size() == 1)
    	{
    		// get simple trans
    		TransitionModel simpleTrans = (TransitionModel)getSimpleTransContainer().getElementsByType(
    				PetriNetModelElement.TRANS_SIMPLE_TYPE)
    				.values().iterator().next();
    		// create an reference entry
    		addReference(getNextFreeArcID(processor),
    				(DefaultPort) simpleTrans.getChildAt(0),
    				(DefaultPort) targetModel.getChildAt(0));
    		
    	} else
    	{
    		
    		// create simple trans
    		TransitionModel simpleTrans = addNewSimpleTrans();
    		// create an reference entry for the existing
    		// simpleTrans
    		addReference(getNextFreeArcID(processor),
    				(DefaultPort) simpleTrans.getChildAt(0),
    				(DefaultPort) targetModel.getChildAt(0));
    		
    		// create reference from each source to each inner
    		// transition
    		Iterator<String> sourceIter = upperContainer.getSourceElements(getId()).keySet().iterator();
    		while (sourceIter.hasNext())
    		{
    			addReference(getNextFreeArcID(processor),
    					(DefaultPort) upperContainer.getElementById(sourceIter.next()).getChildAt(0),
    					(DefaultPort) simpleTrans.getChildAt(0));
    		}
    	}
    }
    
    
    public void registerOutgoingConnectionRemoval(
    		PetriNetModelProcessor processor,
    		AbstractElementModel otherModel)
    {    
    	// SOURCE IS XOR-SPLIT OPERATOR => delete inner Transition that
    	// is source to place IF more than 1 inner transition
    	
    	if (getSimpleTransContainer()
    			.getElementsByType(
    					PetriNetModelElement.TRANS_SIMPLE_TYPE)
    					.size() != 1)
    	{
    		getSimpleTransContainer().removeAllSourceElements(otherModel.getId());
    	}
    }    
}
