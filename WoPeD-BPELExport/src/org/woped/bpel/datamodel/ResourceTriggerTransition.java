package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.woped.core.model.petrinet.TransitionModel;


public class ResourceTriggerTransition extends Transition<TransitionModel>
{
	public ResourceTriggerTransition(TransitionModel data)
	{
		super(data);
	}

	@Override
	public boolean equals(AbstractElement e)
	{if (!ResourceTriggerTransition.class.isInstance(e))
		return false;
	if (this.getData().getId() != ((ResourceTriggerTransition) e).getData().getId())
		return false;
	return true;
	}

	@Override
	public TActivity getBpelCode()
	{
		return null;
	}

}
