package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;


public abstract class TerminalElement<E extends TActivity> extends AbstractElement<E>
{
	public TerminalElement(E data)
	{
		super();
		this.setData(data);
	}
	
	@Override
	public boolean accept_post_object(AbstractElement<?> e)
	{
		if(Place.class.isInstance(e))return true; 
		return false;
	}

	@Override
	public boolean accept_pre_object(AbstractElement<?> e)
	{
		if(Place.class.isInstance(e))return true; 
		return false;
	}
	
}
