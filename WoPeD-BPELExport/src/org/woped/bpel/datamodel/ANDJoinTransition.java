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
		return false;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
