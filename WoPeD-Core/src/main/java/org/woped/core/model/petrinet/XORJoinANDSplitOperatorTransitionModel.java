package org.woped.core.model.petrinet;

import java.util.Map;

import org.jgraph.graph.DefaultPort;
import org.woped.core.model.CreationMap;
import org.woped.core.model.PetriNetModelProcessor;

@SuppressWarnings("serial")
public class XORJoinANDSplitOperatorTransitionModel extends
		CombiOperatorTransitionModel {

	public XORJoinANDSplitOperatorTransitionModel(CreationMap map) {
		super(map, XORJOIN_ANDSPLIT_TYPE);
	}

	@Override
	public void registerIncomingConnection(
    		PetriNetModelProcessor processor,
			AbstractPetriNetElementModel sourceModel) 
	{
		// Create a new IN transition for the XOR join part of this operator
		TransitionModel inTransition =
			getCreateUnusedSimpleTrans();
		// Connect the new IN transition to our center place
		addReference(getNextFreeArcID(processor),
				(DefaultPort) inTransition.getChildAt(0),
				(DefaultPort) getCenterPlace().getChildAt(0));
		// Connect the source model to the in transition
		addReference(getNextFreeArcID(processor),
				(DefaultPort) sourceModel.getChildAt(0),
				(DefaultPort) inTransition.getChildAt(0));	
	}

	@Override
	public void registerOutgoingConnection(
    		PetriNetModelProcessor processor,
    		AbstractPetriNetElementModel targetModel) 
	{
		TransitionModel outTransition =
			getCreateOUTTransition(processor);
		// Connect our OUT transition to the new target object
		addReference(getNextFreeArcID(processor),    		
				(DefaultPort) outTransition.getChildAt(0),
				(DefaultPort) targetModel.getChildAt(0));
	}

    public void registerIncomingConnectionRemoval(
    		PetriNetModelProcessor processor,
    		AbstractPetriNetElementModel otherModel)
    {    
    	// TARGET IS XOR-JOIN OPERATOR => delete inner Transition associated with the incoming arc
    	
    	if (getSimpleTransContainer()
    			.getElementsByType(
    					AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
    					.size() != 1)
    	{
    		getSimpleTransContainer().removeAllTargetElements(otherModel.getId());
    	}
    }    
    
    
    //! Get or create the single OUT transition that exists for this operator
    private TransitionModel getCreateOUTTransition(PetriNetModelProcessor processor)
    {
    	TransitionModel result = null;
    	// First check, if the OUT transition already exists.
    	// If so, we simply return it
    	PlaceModel centerPlace = getCenterPlace();
    	Map<String, AbstractPetriNetElementModel> centerTargetElements = 
    		getSimpleTransContainer().getTargetElements(centerPlace.getId());
    	if ((centerTargetElements!=null)&&(!centerTargetElements.isEmpty()))
    		result = (TransitionModel)getSimpleTransContainer().
    			getElementById(centerTargetElements.keySet().iterator().next());
    	if (result == null)
    	{
    		// It seems like we have to create a new OUT transition.
    		result = getCreateUnusedSimpleTrans();
    		// Create connection from center place to OUT transition
    		addReference(getNextFreeArcID(processor),					
    				(DefaultPort) centerPlace.getChildAt(0),
    				(DefaultPort) result.getChildAt(0));
    	}
    	return result;
    }	
}
