package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.TransitionModel;

public class MessageTransition extends Transition<TransitionModel>
{

	public MessageTransition(TransitionModel data)
	{
		super(data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		if (!MessageTransition.class.isInstance(e))
			return false;
		if (this.getData().getId() != ((MessageTransition) e).getData().getId())
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
