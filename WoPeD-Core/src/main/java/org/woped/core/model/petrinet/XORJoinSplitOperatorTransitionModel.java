package org.woped.core.model.petrinet;

import org.jgraph.graph.DefaultPort;
import org.woped.core.model.CreationMap;
import org.woped.core.model.PetriNetModelProcessor;

@SuppressWarnings("serial")
public class XORJoinSplitOperatorTransitionModel extends
		CombiOperatorTransitionModel {

	public XORJoinSplitOperatorTransitionModel(CreationMap map) {
		super(map, XORJOIN_XORSPLIT_TYPE);
    }

	@Override
	public void registerIncomingConnection(
    		PetriNetModelProcessor processor,
			AbstractPetriNetElementModel sourceModel) 
	{
		// For EACH incoming connection we need a new simpletrans
		// as source for centerplace
		TransitionModel simpleTrans;
		if (getSimpleTransContainer().getArcMap()
				.size() > 0)
		{
			simpleTrans = addNewSimpleTrans();
		} else
		{
			simpleTrans = (TransitionModel) getSimpleTransContainer().getElementsByType(
					AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
					.values().iterator().next();
		}
		PlaceModel centerPlace = getCenterPlace();
		// simpletrans with centerplace
		addReference(getNextFreeArcID(processor),
				(DefaultPort) simpleTrans.getChildAt(0),
				(DefaultPort) centerPlace.getChildAt(0));
		// source with simpletrans
		addReference(getNextFreeArcID(processor),
				(DefaultPort) sourceModel.getChildAt(0),
				(DefaultPort) simpleTrans.getChildAt(0));
	}

	@Override
	public void registerOutgoingConnection(
    		PetriNetModelProcessor processor,
			AbstractPetriNetElementModel targetModel) 
	{
		// For EACH outgoing connection we need a new simpletrans as
		// target for centerplace
		TransitionModel simpleTrans;
		if (getSimpleTransContainer().getArcMap()
				.size() > 0)
		{
			simpleTrans = addNewSimpleTrans();
		} else
		{
			simpleTrans = (TransitionModel) getSimpleTransContainer().getElementsByType(
					AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
					.values().iterator().next();
		}
		PlaceModel centerPlace = getCenterPlace();
		// centerplace with simpletrans
		addReference(getNextFreeArcID(processor),
				(DefaultPort) centerPlace.getChildAt(0),
				(DefaultPort) simpleTrans.getChildAt(0));
		// simpletrans with target
		addReference(getNextFreeArcID(processor),
				(DefaultPort) simpleTrans.getChildAt(0),
				(DefaultPort) targetModel.getChildAt(0));
	}

    public void registerOutgoingConnectionRemoval(
    		PetriNetModelProcessor processor,
    		AbstractPetriNetElementModel otherModel)
    {    	
		/* IF SOURCE IS XOR SPLITJOIN */
    	// remove the source simpleTrans for this arc!
    	// However, keep the last inner transition as it is always required!
    	if (getSimpleTransContainer()
    			.getElementsByType(
    					AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
    					.size() != 1)
    		getSimpleTransContainer().removeAllSourceElements(otherModel.getId());
    }	
    
    public void registerIncomingConnectionRemoval(
    		PetriNetModelProcessor processor,
    		AbstractPetriNetElementModel otherModel)
    {
		// remove the target simpleTrans for this arc!
    	// However, keep the last inner transition as it is always required!
    	if (getSimpleTransContainer()
    			.getElementsByType(
    					AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
    					.size() != 1)
		getSimpleTransContainer()
				.removeAllTargetElements(otherModel.getId());
    }

	/**
	 * Checks if the transition can fire.
	 * <p>
	 * In contrast to the other transitions the XOR SPLIT JOIN Operator doesn't require that all of its source places
	 * contain the necessary amount of tokens (according to the weight of the arc). It is activated if at least
	 * one of its source places does fulfill this demand.
	 * <p>
	 * In addition, to fire this transition is not an atomic operation. It needs two actions to complete.
	 * <ol>
	 * <li>Fire incoming arc
	 * <li>Fire outgoing arc
	 * </ol>
	 * Because of this the transition is also active, if a fire operation has not finished yet even if there are no
	 * source elements which fulfills the requirement. (The center place contains this tokens).
	 */
	@Override
    public boolean isActivated() {

        // Check ongoing operations
        int currentOperations = getCenterPlace().getVirtualTokenCount();
        if (currentOperations > 0) return true;

        // Check waiting operations
        int waitingOperations = getNumIncomingActivePlaces();
        return waitingOperations > 0;
    }
}
