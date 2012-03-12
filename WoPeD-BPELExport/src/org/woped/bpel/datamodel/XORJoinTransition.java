package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.woped.core.model.petrinet.XORJoinOperatorTransitionModel;

public class XORJoinTransition extends
		Transition<XORJoinOperatorTransitionModel>
{

	public XORJoinTransition(XORJoinOperatorTransitionModel data)
	{
		super(data);
	}

	@Override
	public boolean equals(AbstractElement<?> e)
	{
		if (!XORJoinTransition.class.isInstance(e))
			return false;
		if (!this.getData().getId().equals(((XORJoinTransition) e).getData().getId()))
			return false;
		return true;
	}

	@Override
	public TActivity getBpelCode()
	{
		return null;
	}
	
	public String toString()
	{
		return XORJoinTransition.class.getSimpleName() + " Stored element " + this.getData().getId();
	}

}
