package org.woped.bpel.datamodel;

public class IfTransition extends TerminalElement
{

	public IfTransition(String data)
	{
		super(data);
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
