package org.woped.bpel.datamodel;


public abstract class TerminalElement extends AbstractElement<String>
{
	public TerminalElement(String data)
	{
		super();
		this.setData(data);
	}
	
	@Override
	public boolean accept_post_object(AbstractElement e)
	{
		if(Place.class.isInstance(e))return true; 
		return false;
	}

	@Override
	public boolean accept_pre_object(AbstractElement e)
	{
		if(Place.class.isInstance(e))return true; 
		return false;
	}
	
}
