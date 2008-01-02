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
		//first transition
		if (begin == null)
			return null;
		if (Place.class.isInstance(begin))
			return null;
		if (XORSplitTransition.class.isInstance(begin)
				|| XORJoinTransition.class.isInstance(begin)
				|| ANDSplitTransition.class.isInstance(begin)
				|| ANDJoinTransition.class.isInstance(begin))
			return null;
		if (begin.count_post_objects() != 1)
			return null;
		TSequence iSeq = null;
		//if the begin transition is a SequenceTransition
		if(TSequence.class.isInstance(begin)){
			iSeq = (TSequence)begin;
		}
		//cases for the several transitions
		
		//place between transitions
		AbstractElement tmp = begin.get_first_post_element();
		if (!Place.class.isInstance(tmp))
			return null;
		if (tmp.count_post_objects() != 1)
			return null;
		if (tmp.count_pre_objects() != 1)
			return null;
		//last transition
		tmp = tmp.get_first_post_element();		
		if (tmp.count_pre_objects() != 1)
			return null;
		if (XORSplitTransition.class.isInstance(tmp)
				|| XORJoinTransition.class.isInstance(tmp)
				|| ANDSplitTransition.class.isInstance(tmp)
				|| ANDJoinTransition.class.isInstance(tmp))
			return null;
		//cases for the several transitions
		return iSeq;
	}

}
