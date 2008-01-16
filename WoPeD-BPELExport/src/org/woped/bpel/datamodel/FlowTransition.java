package org.woped.bpel.datamodel;

import java.util.Iterator;

import org.oasisOpen.docs.wsbpel.x20.process.executable.*;
import org.woped.bpel.BPEL;

public class FlowTransition extends TerminalElement
{
	private AbstractElement begin;

	public FlowTransition(AbstractElement begin)
	{
		super("test");
		this.begin = begin;
	}

	public boolean equals(AbstractElement e)
	{
		if(!FlowTransition.class.isInstance(e)) return false;
		if(this.getID() != e.getID()) return false;
		return true;
	}
	
	public TActivity getBpelCode()
	{
		AbstractElement tmp = null;
		TFlow iFlow = BPEL.genBpelProsses().addNewFlow();
		Iterator<AbstractElement> list = begin.get_all_post_objects().iterator();
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
		}			
		return iFlow;
	}

}
