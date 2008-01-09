package org.woped.bpel.datamodel;

import java.util.Iterator;

import org.apache.xmlbeans.XmlObject;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnMessage;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSequence;

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
		TPick iPick = null;
		XmlObject triggerTransition = null;
		AbstractElement tmp;
		Iterator<AbstractElement> list = begin.get_all_post_objects().iterator();		
		
		while (list.hasNext())
		{
			tmp = list.next();

			//1.element
			if (TimeTriggerTransition.class.isInstance(tmp)){
				TOnAlarmPick iOnAlarmPick = iPick.addNewOnAlarm();
				triggerTransition = iOnAlarmPick;
			}
			else if (ResourceTriggerTransition.class.isInstance(tmp)||MessageTriggerTransition.class.isInstance(tmp)){
				TOnMessage iOnMessage = iPick.addNewOnMessage();
				triggerTransition = iOnMessage;
			}				
			//transition case (1.transition)
			
			//place between the transitions
			tmp = tmp.get_first_post_element();
			
			//3.element
			tmp = tmp.get_first_post_element();				
			
			if(TOnAlarmPick.class.isInstance(triggerTransition)){
				TOnAlarmPick onAlarmPick = (TOnAlarmPick)triggerTransition;
				//transition case (2.transition)				
			}
			else if(TOnMessage.class.isInstance(triggerTransition)){
				TOnMessage onMessage = (TOnMessage)triggerTransition;
				//transition case (2.transition)
			}			
		}			
		return iPick;
	}
}
