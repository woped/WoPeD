package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.TransitionModel;


public class TriggerTransition extends Transition<TransitionModel>
{


	public TriggerTransition(TransitionModel data)
	{
		super(data);
	}
	
	@Override
	public boolean equals(AbstractElement e)
	{
		if (!TriggerTransition.class.isInstance(e))
			return false;
		if (this.getData().getId() != ((TriggerTransition) e).getData().getId())
			return false;
		return true;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
