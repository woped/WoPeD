package org.woped.bpel.datamodel;

public class FlowTransition extends TerminalElement
{

	public FlowTransition(AbstractElement begin)
	{
		super("test");
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
