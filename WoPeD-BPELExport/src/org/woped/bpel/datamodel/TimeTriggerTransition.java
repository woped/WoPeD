package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.TransitionModel;


public class TimeTriggerTransition extends Transition<TransitionModel>
{


	public TimeTriggerTransition(TransitionModel data)
	{
		super(data);
	}
	
	@Override
	public boolean equals(AbstractElement e)
	{
		if (!TimeTriggerTransition.class.isInstance(e))
			return false;
		if (this.getData().getId() != ((TimeTriggerTransition) e).getData().getId())
			return false;
		return true;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
