package org.woped.bpel.datamodel;


public class PickTransition extends TerminalElement
{

	public PickTransition(AbstractElement begin)
	{
		super("test");
	}

	@Override
	public boolean accept_post_object(AbstractElement e)
	{
		return false;
	}

	@Override
	public boolean accept_pre_object(AbstractElement e)
	{
		return false;
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		return false;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
