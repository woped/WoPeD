package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.ANDSplitOperatorTransitionModel;

public class ANDSplitTransition extends
		Transition<ANDSplitOperatorTransitionModel>
{

	public ANDSplitTransition(ANDSplitOperatorTransitionModel data)
	{
		super(data);
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		if(!ANDSplitTransition.class.isInstance(e))return false;
		if(this.getData().getId() != ((ANDSplitTransition)e).getData().getId()) return false;
		return true;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

	public String toString()
	{
		return ANDSplitTransition.class.getSimpleName() + " Stored element " + this.getData().getId();
	}
}
