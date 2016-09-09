package org.woped.core.model.petrinet;

import org.jgraph.graph.DefaultPort;
import org.woped.core.Constants;
import org.woped.core.model.CreationMap;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.utilities.LoggerManager;

import java.util.Iterator;

@SuppressWarnings("serial")
public class XORJoinOperatorTransitionModel extends OperatorTransitionModel {

	public XORJoinOperatorTransitionModel(CreationMap map) {
		super(map);
	}

	@Override
	public void registerIncomingConnection(
    		PetriNetModelProcessor processor,
			AbstractPetriNetElementModel sourceModel) 
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
						AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
						.size() == 1)
		{
			// get simple trans
			TransitionModel simpleTrans = (TransitionModel) getSimpleTransContainer().getElementsByType(
					AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
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
            // SimpleTransContainer
            addReference(getNextFreeArcID(processor),
					(DefaultPort) sourceModel.getChildAt(0),
					(DefaultPort) simpleTrans.getChildAt(0));

			// create a new arc from the new SimpleTrans to EACH Target
			// of the Operator
			Iterator<String> targetIter = outerContainer.getTargetElements(getId())
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
    		AbstractPetriNetElementModel targetModel) 
	{
		// create a connection to target for each simple transition
		Iterator<AbstractPetriNetElementModel> simpleRootIter = getSimpleTransContainer().getRootElements()
		.iterator();
		while (simpleRootIter.hasNext())
		{
			AbstractPetriNetElementModel pec = simpleRootIter
			.next();
			if (pec.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
				addReference(getNextFreeArcID(processor), pec
						.getPort(), targetModel.getPort());
		}
	}
	
    public void registerIncomingConnectionRemoval(
    		PetriNetModelProcessor processor,
    		AbstractPetriNetElementModel otherModel)
    {
		// TARGET IS XOR-JOIN OPERATOR => delete inner Transition that
		// is Target to place ID more than 1 inner transition
    	if (getSimpleTransContainer()
    			.getElementsByType(
    					AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE)
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
