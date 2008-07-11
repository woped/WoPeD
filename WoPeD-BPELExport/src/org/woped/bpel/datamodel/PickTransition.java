package org.woped.bpel.datamodel;

import java.util.HashSet;
import java.util.Iterator;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFlow;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TIf;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TInvoke;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnMessage;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReceive;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReply;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSequence;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWhile;

public class PickTransition extends TerminalElement<TPick>
{

	HashSet<AbstractElement<?>>	begin;

	public PickTransition(HashSet<AbstractElement<?>> begin)
	{
		super(null);
		this.begin = begin;
	}

	public boolean equals(AbstractElement<?> e)
	{
		if (!PickTransition.class.isInstance(e))
			return false;
		if (this.getID() != e.getID())
			return false;
		return true;
	}

	public TPick getBpelCode()
	{
		if (this.getData() != null)
			return this.getData();
		TPick iPick = TPick.Factory.newInstance();
		AbstractElement<?> tmp;
		Iterator<AbstractElement<?>> list = begin.iterator();

		while (list.hasNext())
		{
			tmp = list.next();

			// 1.element
			if (TimeTriggerTransition.class.isInstance(tmp))
			{
				TOnAlarmPick iOnAlarmPick = iPick.addNewOnAlarm();
				// transition case (1.transition)
				if (tmp.count_post_objects() != 0)
				{
					tmp = tmp.get_first_post_element();
					tmp = tmp.get_first_post_element();
					this.addToOnAlarm(iOnAlarmPick, tmp);
				} else
				{
					this.addToOnAlarm(iOnAlarmPick, tmp);
				}
				// triggerTransition = iOnAlarmPick;
			}
			// else if (ResourceTriggerTransition.class.isInstance(tmp)
			// || MessageTriggerTransition.class.isInstance(tmp))
			else
			{
				TOnMessage iOnMessage = iPick.addNewOnMessage();
				// transition case (1.transition)
				if (tmp.count_post_objects() != 0)
				{
					tmp = tmp.get_first_post_element();
					tmp = tmp.get_first_post_element();
					this.addToOnMessage(iOnMessage, tmp);
				} else
				{
					this.addToOnMessage(iOnMessage, tmp);
				}				
				// triggerTransition = iOnMessage;
			}
		}
		this.setData(iPick);
		return this.getData();
	}

	private void addToOnAlarm(TOnAlarmPick iOnAlarmPick, AbstractElement<?> tmp)
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
		} else if (TWhile.class.isInstance(tmp.getBpelCode()))
		{
			TWhile iwhile = iOnAlarmPick.addNewWhile();
			iwhile.set(tmp.getBpelCode());
		}
	}

	private void addToOnMessage(TOnMessage iOnMessage, AbstractElement<?> tmp)
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
		} else if (TWhile.class.isInstance(tmp.getBpelCode()))
		{
			TWhile iwhile = iOnMessage.addNewWhile();
			iwhile.set(tmp.getBpelCode());
		}
	}
}
