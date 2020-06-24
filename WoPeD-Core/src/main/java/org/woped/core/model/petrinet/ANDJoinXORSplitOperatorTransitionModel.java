package org.woped.core.model.petrinet;

import java.util.Map;

import org.jgraph.graph.DefaultPort;
import org.woped.core.model.CreationMap;
import org.woped.core.model.PetriNetModelProcessor;

@SuppressWarnings("serial")
public class ANDJoinXORSplitOperatorTransitionModel extends
	CombiOperatorTransitionModel {

	public ANDJoinXORSplitOperatorTransitionModel(CreationMap map) {
		super(map, ANDJOIN_XORSPLIT_TYPE);
	}

	@Override
	public void registerIncomingConnection(
    		PetriNetModelProcessor processor,
			AbstractPetriNetElementModel sourceModel) 
	{
		TransitionModel inTransition =
			getCreateINTransition(processor);			
		
		addReference(getNextFreeArcID(processor),    			
				(DefaultPort) sourceModel.getChildAt(0),
				(DefaultPort) inTransition.getChildAt(0));
	}

	@Override
	public void registerOutgoingConnection(
    		PetriNetModelProcessor processor,
    		AbstractPetriNetElementModel targetModel) 
	{
		// Create a new out transition for the XOR split part of this operator
		TransitionModel outTransition =
			getCreateUnusedSimpleTrans();
		// Connect the new out transition to our center place
		addReference(getNextFreeArcID(processor),    			
				(DefaultPort) getCenterPlace().getChildAt(0),
				(DefaultPort) outTransition.getChildAt(0));
		// Connect the out transition to the target model
		addReference(getNextFreeArcID(processor),    			
				(DefaultPort) outTransition.getChildAt(0),
				(DefaultPort) targetModel.getChildAt(0));
	}

    public void registerOutgoingConnectionRemoval(
    		PetriNetModelProcessor processor,
    		AbstractPetriNetElementModel otherModel)
    {    
    	// SOURCE IS XOR-SPLIT OPERATOR => delete inner Transition that
    	// is source to place IF more than 1 inner transition
    	
    	if (getSimpleTransContainer()
    			.getElementsByType(
    					AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
    					.size() != 1)
    	{
    		getSimpleTransContainer().removeAllSourceElements(otherModel.getId());
     	}
    }    
    
    
    //! Get or create the single IN transition that exists for this operator
    private TransitionModel getCreateINTransition(PetriNetModelProcessor processor)
    {
    	TransitionModel result = null;
    	// First check, if the IN transition already exists.
    	// If so, we simply return it
    	PlaceModel centerPlace = getCenterPlace();
    	Map<String, AbstractPetriNetElementModel> centerSourceElements = 
    		getSimpleTransContainer().getSourceElements(centerPlace.getId());
    	if ((centerSourceElements!=null)&&(!centerSourceElements.isEmpty()))
    		result = (TransitionModel)getSimpleTransContainer().
    			getElementById(centerSourceElements.keySet().iterator().next());
    	if (result == null)
    	{
    		// It seems like we have to create a new IN transition.
    		result = getCreateUnusedSimpleTrans();
    		// Connect it to the center place
			addReference(getNextFreeArcID(processor),
					(DefaultPort) result.getChildAt(0),
					(DefaultPort) centerPlace.getChildAt(0));
    	}
    	return result;
    }
 

}
