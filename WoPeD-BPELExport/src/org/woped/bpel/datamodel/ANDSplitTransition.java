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
		if(ANDSplitTransition.class.isInstance(e))return true;
		if(this.getData().getId() == ((ANDSplitTransition)e).getData().getId()) return true;
		return false;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
