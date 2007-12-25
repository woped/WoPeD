package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.TransitionModel;


public class ResourceTransition extends Transition<TransitionModel>
{
	public ResourceTransition(TransitionModel data)
	{
		super(data);
	}

	@Override
	public boolean equals(AbstractElement e)
	{if (!ResourceTransition.class.isInstance(e))
		return false;
	if (this.getData().getId() != ((ResourceTransition) e).getData().getId())
		return false;
	return true;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
