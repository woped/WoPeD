package org.woped.bpel.datamodel;

import java.util.Iterator;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TBooleanExpr;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TIf;

public class IfTransition extends TerminalElement
{
	private AbstractElement begin;

	public IfTransition(AbstractElement begin)
	{
		super("test");
		this.begin = begin;
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		if(!IfTransition.class.isInstance(e)) return false;
		if(this.getID() != e.getID()) return false;
		return true;
	}

	@Override
	public TActivity getBpelCode()
	{
		AbstractElement tmp = null;
		TIf iIf = null;
		Iterator<AbstractElement> list = begin.get_all_post_objects().iterator();
		while (list.hasNext())
		{
			tmp = list.next();			
			
			for (int i = 0; i < 3; i++)
			{					
				//transition case
				tmp = tmp.get_first_post_element();
			}			
		}
		return iIf;
	}

}
