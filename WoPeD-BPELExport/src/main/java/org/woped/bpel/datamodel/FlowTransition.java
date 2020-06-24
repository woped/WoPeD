package org.woped.bpel.datamodel;

import java.util.Iterator;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
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
import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

public class FlowTransition extends TerminalElement<TFlow>
{
	private AbstractElement<?> begin;

	public FlowTransition(AbstractElement<?> begin)
	{
		super(null);
		this.begin = begin;
	}

	public boolean equals(AbstractElement<?> e)
	{
		if(!FlowTransition.class.isInstance(e)) return false;
		if(this.getID() != e.getID()) return false;
		return true;
	}

	public TFlow getBpelCode()
	{
		if(this.getData() != null) return this.getData();
		AbstractElement<?> tmp = null;
		TFlow iFlow = TFlow.Factory.newInstance();
		if(AbstractPetriNetElementModel.class.isInstance(begin.getData())){
			iFlow.setName(""+((AbstractPetriNetElementModel)begin.getData()).getNameValue());
		}
		Iterator<AbstractElement<?>> list = begin.get_all_post_objects().iterator();
		while (list.hasNext())
		{

			tmp = list.next();
			tmp=tmp.get_first_post_element();

			if(TPick.class.isInstance(tmp.getBpelCode())){
				TPick iPick = iFlow.addNewPick();
				iPick.set(tmp.getBpelCode());
			}
			else if(TSequence.class.isInstance(tmp.getBpelCode())){
				TSequence iSeq = iFlow.addNewSequence();
				iSeq.set(tmp.getBpelCode());
			}
			else if(TIf.class.isInstance(tmp.getBpelCode())){
				TIf iIf = iFlow.addNewIf();
				iIf.set(tmp.getBpelCode());
			}
			else if(TAssign.class.isInstance(tmp.getBpelCode())){
				TAssign iAss = iFlow.addNewAssign();
				iAss.set(tmp.getBpelCode());
			}
			else if(TFlow.class.isInstance(tmp.getBpelCode())){
				TFlow iFlowSub = iFlow.addNewFlow();
				iFlowSub.set(tmp.getBpelCode());
			}
			else if(TEmpty.class.isInstance(tmp.getBpelCode())){
				TEmpty iEmpty = iFlow.addNewEmpty();
				iEmpty.set(tmp.getBpelCode());
			}
			else if(TWait.class.isInstance(tmp.getBpelCode())){
				TWait iWait = iFlow.addNewWait();
				iWait.set(tmp.getBpelCode());
			}
			else if(TReceive.class.isInstance(tmp.getBpelCode())){
				TReceive iReceive = iFlow.addNewReceive();
				iReceive.set(tmp.getBpelCode());
			}
			else if(TReply.class.isInstance(tmp.getBpelCode())){
				TReply iReply = iFlow.addNewReply();
				iReply.set(tmp.getBpelCode());
			}
			else if(TInvoke.class.isInstance(tmp.getBpelCode())){
				TInvoke iInvoke = iFlow.addNewInvoke();
				iInvoke.set(tmp.getBpelCode());
			}
			else if(TWhile.class.isInstance(tmp.getBpelCode()))
			{
				TWhile iwhile = iFlow.addNewWhile();
				iwhile.set(tmp.getBpelCode());
			}
		}
		this.setData(iFlow);
		return this.getData();
	}

}
