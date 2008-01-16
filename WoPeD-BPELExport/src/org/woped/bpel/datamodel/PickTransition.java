package org.woped.bpel.datamodel;

import java.util.Iterator;

import org.apache.xmlbeans.XmlObject;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TInvoke;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnMessage;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReceive;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReply;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSequence;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;

public class PickTransition extends TerminalElement
{
	AbstractElement begin;
	TPick pick = null;

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
				
				//transition case (1.transition)			
				if(TAssign.class.isInstance(begin.getBpelCode())){
					TAssign iAss = iOnAlarmPick.addNewAssign();
					iAss.set(begin.getBpelCode());			
				}		
				else if(TEmpty.class.isInstance(begin.getBpelCode())){
					TEmpty iEmpty = iOnAlarmPick.addNewEmpty();
					iEmpty.set(begin.getBpelCode());
				}
				else if(TWait.class.isInstance(begin.getBpelCode())){
					TWait iWait = iOnAlarmPick.addNewWait();
					iWait.set(begin.getBpelCode());
				}
				else if(TReceive.class.isInstance(begin.getBpelCode())){
					TReceive iReceive = iOnAlarmPick.addNewReceive();
					iReceive.set(begin.getBpelCode());
				}
				else if(TReply.class.isInstance(begin.getBpelCode())){
					TReply iReply = iOnAlarmPick.addNewReply();
					iReply.set(begin.getBpelCode());
				}
				else if(TInvoke.class.isInstance(begin.getBpelCode())){
					TInvoke iInvoke = iOnAlarmPick.addNewInvoke();
					iInvoke.set(begin.getBpelCode());
				}
				triggerTransition = iOnAlarmPick;
			}
			else if (ResourceTriggerTransition.class.isInstance(tmp)||MessageTriggerTransition.class.isInstance(tmp)){
				TOnMessage iOnMessage = iPick.addNewOnMessage();
				
				//transition case (1.transition)			
				if(TAssign.class.isInstance(begin.getBpelCode())){
					TAssign iAss = iOnMessage.addNewAssign();
					iAss.set(begin.getBpelCode());			
				}		
				else if(TEmpty.class.isInstance(begin.getBpelCode())){
					TEmpty iEmpty = iOnMessage.addNewEmpty();
					iEmpty.set(begin.getBpelCode());
				}
				else if(TWait.class.isInstance(begin.getBpelCode())){
					TWait iWait = iOnMessage.addNewWait();
					iWait.set(begin.getBpelCode());
				}
				else if(TReceive.class.isInstance(begin.getBpelCode())){
					TReceive iReceive = iOnMessage.addNewReceive();
					iReceive.set(begin.getBpelCode());
				}
				else if(TReply.class.isInstance(begin.getBpelCode())){
					TReply iReply = iOnMessage.addNewReply();
					iReply.set(begin.getBpelCode());
				}
				else if(TInvoke.class.isInstance(begin.getBpelCode())){
					TInvoke iInvoke = iOnMessage.addNewInvoke();
					iInvoke.set(begin.getBpelCode());
				}
				triggerTransition = iOnMessage;
			}				
			
			
			//place between the transitions
			tmp = tmp.get_first_post_element();
			
			//3.element
			tmp = tmp.get_first_post_element();				
			
			if(TOnAlarmPick.class.isInstance(triggerTransition)){
				TOnAlarmPick onAlarmPick = (TOnAlarmPick)triggerTransition;
				
				//transitions case (2.transition)
				if(TAssign.class.isInstance(begin.getBpelCode())){
					TAssign iAss = onAlarmPick.addNewAssign();
					iAss.set(begin.getBpelCode());			
				}		
				else if(TEmpty.class.isInstance(begin.getBpelCode())){
					TEmpty iEmpty = onAlarmPick.addNewEmpty();
					iEmpty.set(begin.getBpelCode());
				}
				else if(TWait.class.isInstance(begin.getBpelCode())){
					TWait iWait = onAlarmPick.addNewWait();
					iWait.set(begin.getBpelCode());
				}
				else if(TReceive.class.isInstance(begin.getBpelCode())){
					TReceive iReceive = onAlarmPick.addNewReceive();
					iReceive.set(begin.getBpelCode());
				}
				else if(TReply.class.isInstance(begin.getBpelCode())){
					TReply iReply = onAlarmPick.addNewReply();
					iReply.set(begin.getBpelCode());
				}
				else if(TInvoke.class.isInstance(begin.getBpelCode())){
					TInvoke iInvoke = onAlarmPick.addNewInvoke();
					iInvoke.set(begin.getBpelCode());
				}
			}
			else if(TOnMessage.class.isInstance(triggerTransition)){
				TOnMessage onMessage = (TOnMessage)triggerTransition;
				//transition case (2.transition)
				if(TAssign.class.isInstance(begin.getBpelCode())){
					TAssign iAss = onMessage.addNewAssign();
					iAss.set(begin.getBpelCode());			
				}		
				else if(TEmpty.class.isInstance(begin.getBpelCode())){
					TEmpty iEmpty = onMessage.addNewEmpty();
					iEmpty.set(begin.getBpelCode());
				}
				else if(TWait.class.isInstance(begin.getBpelCode())){
					TWait iWait = onMessage.addNewWait();
					iWait.set(begin.getBpelCode());
				}
				else if(TReceive.class.isInstance(begin.getBpelCode())){
					TReceive iReceive = onMessage.addNewReceive();
					iReceive.set(begin.getBpelCode());
				}
				else if(TReply.class.isInstance(begin.getBpelCode())){
					TReply iReply = onMessage.addNewReply();
					iReply.set(begin.getBpelCode());
				}
				else if(TInvoke.class.isInstance(begin.getBpelCode())){
					TInvoke iInvoke = onMessage.addNewInvoke();
					iInvoke.set(begin.getBpelCode());
				}
			}			
		}
		this.pick = iPick;
		return pick;
	}
}
