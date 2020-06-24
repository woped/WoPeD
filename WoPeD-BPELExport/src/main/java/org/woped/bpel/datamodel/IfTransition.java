package org.woped.bpel.datamodel;

import java.util.Iterator;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TElseif;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFlow;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TIf;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TInvoke;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReceive;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReply;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSequence;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWhile;
import org.woped.core.model.petrinet.XORSplitOperatorTransitionModel;

public class IfTransition extends TerminalElement<TIf>
{

	private AbstractElement<?>	begin;

	public IfTransition(AbstractElement<?> begin)
	{
		super(null);
		this.begin = begin;
	}

	public boolean equals(AbstractElement<?> e)
	{
		if (!IfTransition.class.isInstance(e))
			return false;
		if (this.getID() != e.getID())
			return false;
		return true;
	}

	public TIf getBpelCode()
	{
		if (this.getData() != null)
			return this.getData();
		AbstractElement<?> tmp = null;
		TIf iIf = TIf.Factory.newInstance();
		if (XORSplitOperatorTransitionModel.class.isInstance(begin.getData()))
		{
			iIf.setName(""
					+ ((XORSplitTransition) begin).getData().getNameValue());
		}
		Iterator<AbstractElement<?>> list = begin.get_all_post_objects()
				.iterator();
		for (int i = 0; list.hasNext(); i++)
		{
			tmp = list.next();
			tmp = tmp.get_first_post_element();

			if (i == 0)
			{
				if (TPick.class.isInstance(tmp.getBpelCode()))
				{
					TPick iPick = iIf.addNewPick();
					iPick.set(tmp.getBpelCode());
				} else if (TSequence.class.isInstance(tmp.getBpelCode()))
				{
					TSequence iSeq = iIf.addNewSequence();
					iSeq.set(tmp.getBpelCode());
				} else if (TIf.class.isInstance(tmp.getBpelCode()))
				{
					TIf iIfSub = iIf.addNewIf();
					iIfSub.set(tmp.getBpelCode());
				} else if (TAssign.class.isInstance(tmp.getBpelCode()))
				{
					TAssign iAss = iIf.addNewAssign();
					iAss.set(tmp.getBpelCode());
				} else if (TFlow.class.isInstance(tmp.getBpelCode()))
				{
					TFlow iFlow = iIf.addNewFlow();
					iFlow.set(tmp.getBpelCode());
				} else if (TEmpty.class.isInstance(tmp.getBpelCode()))
				{
					TEmpty iEmpty = iIf.addNewEmpty();
					iEmpty.set(tmp.getBpelCode());
				} else if (TWait.class.isInstance(tmp.getBpelCode()))
				{
					TWait iWait = iIf.addNewWait();
					iWait.set(tmp.getBpelCode());
				} else if (TReceive.class.isInstance(tmp.getBpelCode()))
				{
					TReceive iReceive = iIf.addNewReceive();
					iReceive.set(tmp.getBpelCode());
				} else if (TReply.class.isInstance(tmp.getBpelCode()))
				{
					TReply iReply = iIf.addNewReply();
					iReply.set(tmp.getBpelCode());
				} else if (TInvoke.class.isInstance(tmp.getBpelCode()))
				{
					TInvoke iInvoke = iIf.addNewInvoke();
					iInvoke.set(tmp.getBpelCode());
				} else if (TWhile.class.isInstance(tmp.getBpelCode()))
				{
					TWhile iwhile = iIf.addNewWhile();
					iwhile.set(tmp.getBpelCode());
				}
			} else
			{
				TElseif iElseIF = iIf.addNewElseif();
				if (TPick.class.isInstance(tmp.getBpelCode()))
				{
					TPick iPick = iElseIF.addNewPick();
					iPick.set(tmp.getBpelCode());
				} else if (TSequence.class.isInstance(tmp.getBpelCode()))
				{
					TSequence iSeq = iElseIF.addNewSequence();
					iSeq.set(tmp.getBpelCode());
				} else if (TIf.class.isInstance(tmp.getBpelCode()))
				{
					TIf iIfSub = iElseIF.addNewIf();
					iIfSub.set(tmp.getBpelCode());
				} else if (TAssign.class.isInstance(tmp.getBpelCode()))
				{
					TAssign iAss = iElseIF.addNewAssign();
					iAss.set(tmp.getBpelCode());
				} else if (TFlow.class.isInstance(tmp.getBpelCode()))
				{
					TFlow iFlow = iElseIF.addNewFlow();
					iFlow.set(tmp.getBpelCode());
				} else if (TEmpty.class.isInstance(tmp.getBpelCode()))
				{
					TEmpty iEmpty = iElseIF.addNewEmpty();
					iEmpty.set(tmp.getBpelCode());
				} else if (TWait.class.isInstance(tmp.getBpelCode()))
				{
					TWait iWait = iElseIF.addNewWait();
					iWait.set(tmp.getBpelCode());
				} else if (TReceive.class.isInstance(tmp.getBpelCode()))
				{
					TReceive iReceive = iElseIF.addNewReceive();
					iReceive.set(tmp.getBpelCode());
				} else if (TReply.class.isInstance(tmp.getBpelCode()))
				{
					TReply iReply = iElseIF.addNewReply();
					iReply.set(tmp.getBpelCode());
				} else if (TInvoke.class.isInstance(tmp.getBpelCode()))
				{
					TInvoke iInvoke = iElseIF.addNewInvoke();
					iInvoke.set(tmp.getBpelCode());
				} else if (TWhile.class.isInstance(tmp.getBpelCode()))
				{
					TWhile iwhile = iIf.addNewWhile();
					iwhile.set(tmp.getBpelCode());
				}
			}
		}
		this.setData(iIf);
		return this.getData();
	}

}
