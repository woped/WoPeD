package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.XORJoinOperatorTransitionModel;

public class XORJoinTransition extends
		Transition<XORJoinOperatorTransitionModel>
{

	public XORJoinTransition(XORJoinOperatorTransitionModel data)
	{
		super(data);
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		if (XORJoinTransition.class.isInstance(e))
			return true;
		if (this.getData().getId() == ((XORJoinTransition) e).getData().getId())
			return true;
		return false;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
