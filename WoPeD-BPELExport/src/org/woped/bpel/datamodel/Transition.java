package org.woped.bpel.datamodel;

import org.woped.core.model.petrinet.TransitionModel;

abstract public class Transition<E extends TransitionModel> extends
		NonterminalElement<TransitionModel>
{

	public Transition(E data)
	{
		super(data);
	}

	@Override
	public boolean accept_post_object(AbstractElement<?> e)
	{
		if (Place.class.isInstance(e))
			return true;
		return false;
	}
	
	@Override
	public boolean accept_pre_object(AbstractElement<?> e)
	{
		if (Place.class.isInstance(e))
			return true;
		return false;
	}	
}
