package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.ANDJoinOperatorTransitionModel;

public class ANDJoinTransition extends
		Transition<ANDJoinOperatorTransitionModel>
{

	public ANDJoinTransition(ANDJoinOperatorTransitionModel data)
	{
		super(data);
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		if(!ANDJoinTransition.class.isInstance(e))return false;
		if(this.getData().getId() != ((ANDJoinTransition)e).getData().getId()) return false;
		return true;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}
	
	public String toString()
	{
		return ANDJoinTransition.class.getSimpleName() + " Stored element " + this.getData().getId();
	}

}
