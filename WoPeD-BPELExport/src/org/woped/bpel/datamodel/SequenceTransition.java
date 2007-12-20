package org.woped.bpel.datamodel;


public class SequenceTransition extends TerminalElement
{

	public SequenceTransition(AbstractElement begin)
	{
		super("test");
		// TODO Auto-generated constructor stub
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
