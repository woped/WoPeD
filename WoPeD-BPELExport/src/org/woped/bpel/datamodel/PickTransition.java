package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;

public class PickTransition extends TerminalElement
{

	public PickTransition(AbstractElement begin)
	{
		super("test");
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		if (!PickTransition.class.isInstance(e))
			return false;
		if (this.getID() != e.getID())
			return false;
		return true;
	}

	@Override
	public TActivity getBpelCode()
	{
		return null;
	}

}
