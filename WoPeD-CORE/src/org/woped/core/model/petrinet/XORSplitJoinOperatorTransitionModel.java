package org.woped.core.model.petrinet;

import org.jgraph.graph.DefaultPort;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.PetriNetModelProcessor;

@SuppressWarnings("serial")
public class XORSplitJoinOperatorTransitionModel extends
		OperatorTransitionModel {

	public XORSplitJoinOperatorTransitionModel(CreationMap map) {
		super(map, XOR_SPLITJOIN_TYPE);
	}

	@Override
	public void registerIncomingConnection(
    		PetriNetModelProcessor processor,
			AbstractElementModel sourceModel) 
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
					PetriNetModelElement.TRANS_SIMPLE_TYPE)
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
			AbstractElementModel targetModel) 
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
					PetriNetModelElement.TRANS_SIMPLE_TYPE)
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
    		AbstractElementModel otherModel)
    {    	
		/* IF SOURCE IS XOR SPLITJOIN */
    	// remove the source simpleTrans for this arc!
    	getSimpleTransContainer().removeAllSourceElements(otherModel.getId());
    }	
    
    public void registerIncomingConnectionRemoval(
    		PetriNetModelProcessor processor,
    		AbstractElementModel otherModel)
    {
		// remove the target simpleTrans for this arc!
		getSimpleTransContainer()
				.removeAllTargetElements(otherModel.getId());
	}    
}
