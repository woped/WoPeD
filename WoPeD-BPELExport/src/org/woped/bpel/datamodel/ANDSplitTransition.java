package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.woped.core.model.petrinet.ANDSplitOperatorTransitionModel;

public class ANDSplitTransition extends
		Transition<ANDSplitOperatorTransitionModel>
{

	public ANDSplitTransition(ANDSplitOperatorTransitionModel data)
	{
		super(data);
	}

	@Override
	public boolean equals(AbstractElement<?> e)
	{
		if (!ANDSplitTransition.class.isInstance(e))return false;
		if (!this.getData().getId().equals(((ANDSplitTransition)e).getData().getId())) return false;
		return true;
	}

	@Override
	public TActivity getBpelCode()
	{
		return null;
	}

	public String toString()
	{
		return ANDSplitTransition.class.getSimpleName() + " Stored element " + this.getData().getId();
	}
}
