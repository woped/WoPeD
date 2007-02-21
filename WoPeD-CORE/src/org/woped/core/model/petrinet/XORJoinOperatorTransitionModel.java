package org.woped.core.model.petrinet;

import java.util.Iterator;

import org.jgraph.graph.DefaultPort;
import org.woped.core.Constants;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.utilities.LoggerManager;

@SuppressWarnings("serial")
public class XORJoinOperatorTransitionModel extends OperatorTransitionModel {

	public XORJoinOperatorTransitionModel(CreationMap map) {
		super(map, XOR_JOIN_TYPE);
	}

	@Override
	public void registerIncomingConnection(
    		PetriNetModelProcessor processor,
			AbstractElementModel sourceModel) 
	{
		ModelElementContainer outerContainer = processor.getElementContainer();
		/*
		 * If operator have only one Source AND container does only
		 * have one simple trans
		 */
		if (outerContainer.getSourceElements(
				getId()).size() == 1
				&& getSimpleTransContainer()
				.getElementsByType(
						PetriNetModelElement.TRANS_SIMPLE_TYPE)
						.size() == 1)
		{
			// get simple trans
			TransitionModel simpleTrans = (TransitionModel) getSimpleTransContainer().getElementsByType(
					PetriNetModelElement.TRANS_SIMPLE_TYPE)
					.values().iterator().next();
			// dann f�ge nur die Reference hinzu
			addReference(getNextFreeArcID(processor),
					(DefaultPort) sourceModel.getChildAt(0),
					(DefaultPort) simpleTrans.getChildAt(0));
		} else
		{
			// create new SimpleTrans
			// add simpletrans to SimpleTransContainer
			TransitionModel simpleTrans = addNewSimpleTrans();
			
			// add new arc from new source to cew simpleTrans in
			// SimpleTransConatainer
			addReference(getNextFreeArcID(processor),
					(DefaultPort) sourceModel.getChildAt(0),
					(DefaultPort) simpleTrans.getChildAt(0));
			
			// create a new arc from EACH SimpleTrans to EACH Target
			// of the Operator
			Iterator targetIter = outerContainer.getTargetElements(getId())
			.keySet().iterator();
			while (targetIter.hasNext())
			{
				addReference(getNextFreeArcID(processor),
						(DefaultPort) simpleTrans.getChildAt(0),
						(DefaultPort) outerContainer
						.getElementById(targetIter.next())
						.getChildAt(0));
			}
		}
	}

	@Override
	public void registerOutgoingConnection(
    		PetriNetModelProcessor processor,
    		AbstractElementModel targetModel) 
	{
		// f�r jede simpletrans ne connection auf das target
		Iterator simpleRootIter = getSimpleTransContainer().getRootElements()
		.iterator();
		while (simpleRootIter.hasNext())
		{
			PetriNetModelElement pec = (PetriNetModelElement) simpleRootIter
			.next();
			if (pec.getType() == PetriNetModelElement.TRANS_SIMPLE_TYPE)
				addReference(getNextFreeArcID(processor), pec
						.getPort(), targetModel.getPort());
		}
	}
	
    public void registerIncomingConnectionRemoval(
    		PetriNetModelProcessor processor,
    		AbstractElementModel otherModel)
    {
		// TARGET IS XOR-JOIN OPERATOR => delete inner Transition that
		// is Target to place ID more than 1 inner transition
    	if (getSimpleTransContainer()
    			.getElementsByType(
    					PetriNetModelElement.TRANS_SIMPLE_TYPE)
    					.size() != 1)
    	{
    		getSimpleTransContainer()
    		.removeAllTargetElements(
    				otherModel.getId());
    		LoggerManager.debug(Constants.CORE_LOGGER,
    		"INNER Target Elements deleted");
    	}
    }	
}
