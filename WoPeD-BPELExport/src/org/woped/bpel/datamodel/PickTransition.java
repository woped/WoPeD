package org.woped.bpel.datamodel;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.xmlbeans.XmlObject;
import org.oasisOpen.docs.wsbpel.x20.process.executable.*;
import org.woped.bpel.BPEL;

public class PickTransition extends TerminalElement
{

	HashSet<AbstractElement>	begin;
	TPick						pick	= null;

	public PickTransition(HashSet<AbstractElement> begin)
	{
		super("test");
		this.begin = begin;
	}

	public boolean equals(AbstractElement e)
	{
		if (!PickTransition.class.isInstance(e))
			return false;
		if (this.getID() != e.getID())
			return false;
		return true;
	}

	public TActivity getBpelCode()
	{
		TPick iPick = BPEL.genBpelProcess().addNewPick();
		XmlObject triggerTransition = null;
		AbstractElement tmp;
		Iterator<AbstractElement> list = begin.iterator();

		while (list.hasNext())
		{
			tmp = list.next();

			// 1.element
			if (TimeTriggerTransition.class.isInstance(tmp))
			{
				TOnAlarmPick iOnAlarmPick = iPick.addNewOnAlarm();

				// transition case (1.transition)
				tmp = tmp.get_first_post_element();
				tmp = tmp.get_first_post_element();
				this.addToOnAlarm(iOnAlarmPick, tmp);
				triggerTransition = iOnAlarmPick;
			} else if (ResourceTriggerTransition.class.isInstance(tmp)
					|| MessageTriggerTransition.class.isInstance(tmp))
			{
				TOnMessage iOnMessage = iPick.addNewOnMessage();

				// transition case (1.transition)
				tmp = tmp.get_first_post_element();
				tmp = tmp.get_first_post_element();
				this.addToOnMessage(iOnMessage, tmp);
				triggerTransition = iOnMessage;
			}
		}
		this.pick = iPick;
		return pick;
	}

	private void addToOnAlarm(TOnAlarmPick iOnAlarmPick, AbstractElement tmp)
	{
		if (TPick.class.isInstance(tmp.getBpelCode()))
		{
			TPick iPickSub = iOnAlarmPick.addNewPick();
			iPickSub.set(tmp.getBpelCode());
		} else if (TSequence.class.isInstance(tmp.getBpelCode()))
		{
			TSequence iSeq = iOnAlarmPick.addNewSequence();
			iSeq.set(tmp.getBpelCode());
		} else if (TIf.class.isInstance(tmp.getBpelCode()))
		{
			TIf iIf = iOnAlarmPick.addNewIf();
			iIf.set(tmp.getBpelCode());
		} else if (TAssign.class.isInstance(tmp.getBpelCode()))
		{
			TAssign iAss = iOnAlarmPick.addNewAssign();
			iAss.set(tmp.getBpelCode());
		} else if (TFlow.class.isInstance(tmp.getBpelCode()))
		{
			TFlow iFlowSub = iOnAlarmPick.addNewFlow();
			iFlowSub.set(tmp.getBpelCode());
		} else if (TEmpty.class.isInstance(tmp.getBpelCode()))
		{
			TEmpty iEmpty = iOnAlarmPick.addNewEmpty();
			iEmpty.set(tmp.getBpelCode());
		} else if (TWait.class.isInstance(tmp.getBpelCode()))
		{
			TWait iWait = iOnAlarmPick.addNewWait();
			iWait.set(tmp.getBpelCode());
		} else if (TReceive.class.isInstance(tmp.getBpelCode()))
		{
			TReceive iReceive = iOnAlarmPick.addNewReceive();
			iReceive.set(tmp.getBpelCode());
		} else if (TReply.class.isInstance(tmp.getBpelCode()))
		{
			TReply iReply = iOnAlarmPick.addNewReply();
			iReply.set(tmp.getBpelCode());
		} else if (TInvoke.class.isInstance(tmp.getBpelCode()))
		{
			TInvoke iInvoke = iOnAlarmPick.addNewInvoke();
			iInvoke.set(tmp.getBpelCode());
		}
	}

	private void addToOnMessage(TOnMessage iOnMessage, AbstractElement tmp)
	{
		if (TPick.class.isInstance(tmp.getBpelCode()))
		{
			TPick iPickSub = iOnMessage.addNewPick();
			iPickSub.set(tmp.getBpelCode());
		} else if (TSequence.class.isInstance(tmp.getBpelCode()))
		{
			TSequence iSeq = iOnMessage.addNewSequence();
			iSeq.set(tmp.getBpelCode());
		} else if (TIf.class.isInstance(tmp.getBpelCode()))
		{
			TIf iIf = iOnMessage.addNewIf();
			iIf.set(tmp.getBpelCode());
		} else if (TAssign.class.isInstance(tmp.getBpelCode()))
		{
			TAssign iAss = iOnMessage.addNewAssign();
			iAss.set(tmp.getBpelCode());
		} else if (TFlow.class.isInstance(tmp.getBpelCode()))
		{
			TFlow iFlowSub = iOnMessage.addNewFlow();
			iFlowSub.set(tmp.getBpelCode());
		} else if (TEmpty.class.isInstance(tmp.getBpelCode()))
		{
			TEmpty iEmpty = iOnMessage.addNewEmpty();
			iEmpty.set(tmp.getBpelCode());
		} else if (TWait.class.isInstance(tmp.getBpelCode()))
		{
			TWait iWait = iOnMessage.addNewWait();
			iWait.set(tmp.getBpelCode());
		} else if (TReceive.class.isInstance(tmp.getBpelCode()))
		{
			TReceive iReceive = iOnMessage.addNewReceive();
			iReceive.set(tmp.getBpelCode());
		} else if (TReply.class.isInstance(tmp.getBpelCode()))
		{
			TReply iReply = iOnMessage.addNewReply();
			iReply.set(tmp.getBpelCode());
		} else if (TInvoke.class.isInstance(tmp.getBpelCode()))
		{
			TInvoke iInvoke = iOnMessage.addNewInvoke();
			iInvoke.set(tmp.getBpelCode());
		}
	}
}
