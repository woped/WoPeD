package org.woped.bpel.datamodel;

import java.util.Iterator;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPick;

public class PickTransition extends TerminalElement
{
	private AbstractElement begin;

	public PickTransition(AbstractElement begin)
	{
		super("test");
		this.begin = begin;
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
		if (begin == null)
			return null;
		if (!Place.class.isInstance(begin))
			return null;
		if (begin.count_post_objects() < 2)
			return null;
		AbstractElement end = null;
		AbstractElement tmp;
		Iterator<AbstractElement> list = begin.get_all_post_objects().iterator();
		boolean firstrun = true;
		boolean timetrigger = false;
		boolean hastrigger = false;
		TPick iPick = null;
		while (list.hasNext())
		{
			tmp = list.next();

			// test 1, element
			if (TimeTriggerTransition.class.isInstance(tmp)
					|| ResourceTriggerTransition.class.isInstance(tmp)
					|| MessageTriggerTransition.class.isInstance(tmp))
				hastrigger = true;
			if (TimeTriggerTransition.class.isInstance(tmp) && timetrigger)
				return null;
			else if (TimeTriggerTransition.class.isInstance(tmp))
				timetrigger = true;

			if (tmp.count_post_objects() != 1)
				return null;
			if (tmp.count_pre_objects() != 1)
				return null;
			
			if(timetrigger){
				TOnAlarmPick iOnAlarmPick = iPick.addNewOnAlarm();
			}
			
			//cases for the several transitions
			
			// test 2. element, it is a place
			tmp = tmp.get_first_post_element();
			if (!Place.class.isInstance(tmp))
				return null;
			if ((tmp.count_post_objects() != 1)
					|| (tmp.count_pre_objects() != 1))
				return null;
			// test 3. element
			tmp = tmp.get_first_post_element();
			if (TimeTriggerTransition.class.isInstance(tmp)
					&& ResourceTriggerTransition.class.isInstance(tmp)
					&& MessageTriggerTransition.class.isInstance(tmp))
				return null;
			if ((tmp.count_post_objects() != 1)
					|| (tmp.count_pre_objects() != 1))
				return null;
			// test endelement
			tmp = tmp.get_first_post_element();
			if (firstrun)
			{
				end = tmp;
				firstrun = false;
			} else if (!end.equals(tmp))
				return null;
		}

		if (begin.count_post_objects() != end.count_pre_objects())
			return null;
		if (!hastrigger)
			return null;
		return iPick;
	}
}
