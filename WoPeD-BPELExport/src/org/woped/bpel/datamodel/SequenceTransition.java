package org.woped.bpel.datamodel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSequence;

public class SequenceTransition extends TerminalElement
{
	AbstractElement begin;

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

	/*
	 * Bpel code for a minimum sequence of 2 transitions
	 * 
	 */
	public TSequence getBpelCode()
	{
		TSequence iSeq = null;
		//1.transition
		//if the 1.transition is a SequenceTransition
		if (SequenceTransition.class.isInstance(begin))
		{
			iSeq = (TSequence)begin.getBpelCode();			
		}
		//transitions case (1.transition)
		
		//place between transitions
		AbstractElement tmp = begin.get_first_post_element();
		
		//2.transition
		tmp = tmp.get_first_post_element();			
		//if the 2.transition is a SequenceTransition
		if (SequenceTransition.class.isInstance(tmp)){
			TSequence helpSequence = (TSequence)tmp.getBpelCode();			
		}
		//transitions case (2.transition)
		return iSeq;
	}

}
