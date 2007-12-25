package org.woped.bpel.datamodel;

public class SequenceTransition extends TerminalElement
{

	public SequenceTransition(AbstractElement begin)
	{
		super("test");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		if (!SequenceTransition.class.isInstance(e))
			return false;
		if (this.getID() != e.getID())
			return false;
		return true;
	}

	@Override
	public String getBpelCode()
	{
		return null;
	}

}
