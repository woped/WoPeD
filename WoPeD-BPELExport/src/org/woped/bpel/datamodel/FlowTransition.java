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
		if(!FlowTransition.class.isInstance(e)) return false;
		if(this.getID() != e.getID()) return false;
		return true;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
