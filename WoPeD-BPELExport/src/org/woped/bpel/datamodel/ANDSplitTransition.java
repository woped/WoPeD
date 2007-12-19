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
		return false;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
