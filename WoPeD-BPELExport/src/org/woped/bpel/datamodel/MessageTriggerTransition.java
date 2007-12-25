package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.TransitionModel;

public class MessageTriggerTransition extends Transition<TransitionModel>
{

	public MessageTriggerTransition(TransitionModel data)
	{
		super(data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		if (!MessageTriggerTransition.class.isInstance(e))
			return false;
		if (this.getData().getId() != ((MessageTriggerTransition) e).getData().getId())
			return false;
		return true;
	}

	@Override
	public String getBpelCode()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
