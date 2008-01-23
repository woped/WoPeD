package org.woped.bpel.datamodel;

import java.util.Iterator;

import org.oasisOpen.docs.wsbpel.x20.process.executable.*;

import org.woped.bpel.BPEL;

public class IfTransition extends TerminalElement
{
	private AbstractElement begin;

	public IfTransition(AbstractElement begin)
	{
		super("test");
		this.begin = begin;
	}

	public boolean equals(AbstractElement e)
	{
		if(!IfTransition.class.isInstance(e)) return false;
		if(this.getID() != e.getID()) return false;
		return true;
	}

	public TActivity getBpelCode()
	{
		AbstractElement tmp = null;
		TIf iIf = BPEL.genBpelProsses().addNewIf();
		iIf.setName(""+this.getData());
		Iterator<AbstractElement> list = begin.get_all_post_objects().iterator();
		for (int i=0;list.hasNext();i++)
		{
			tmp = list.next();
			tmp=tmp.get_first_post_element();			
				
			if(i==0){
				if(TPick.class.isInstance(tmp.getBpelCode())){
					TPick iPick = iIf.addNewPick();
					iPick.set(tmp.getBpelCode());			
				}
				else if(TSequence.class.isInstance(tmp.getBpelCode())){
					TSequence iSeq = iIf.addNewSequence();
					iSeq.set(tmp.getBpelCode());
				}
				else if(TIf.class.isInstance(tmp.getBpelCode())){
					TIf iIfSub = iIf.addNewIf();
					iIfSub.set(tmp.getBpelCode());
				}
				else if(TAssign.class.isInstance(tmp.getBpelCode())){
					TAssign iAss = iIf.addNewAssign();
					iAss.set(tmp.getBpelCode());			
				}
				else if(TFlow.class.isInstance(tmp.getBpelCode())){
					TFlow iFlow = iIf.addNewFlow();
					iFlow.set(tmp.getBpelCode());
				}
				else if(TEmpty.class.isInstance(tmp.getBpelCode())){
					TEmpty iEmpty = iIf.addNewEmpty();
					iEmpty.set(tmp.getBpelCode());
				}
				else if(TWait.class.isInstance(tmp.getBpelCode())){
					TWait iWait = iIf.addNewWait();
					iWait.set(tmp.getBpelCode());
				}
				else if(TReceive.class.isInstance(tmp.getBpelCode())){
					TReceive iReceive = iIf.addNewReceive();
					iReceive.set(tmp.getBpelCode());
				}
				else if(TReply.class.isInstance(tmp.getBpelCode())){
					TReply iReply = iIf.addNewReply();
					iReply.set(tmp.getBpelCode());
				}
				else if(TInvoke.class.isInstance(tmp.getBpelCode())){
					TInvoke iInvoke = iIf.addNewInvoke();
					iInvoke.set(tmp.getBpelCode());
				}
			}
			else{
				TElseif iElseIF = iIf.addNewElseif();
				if(TPick.class.isInstance(tmp.getBpelCode())){
					TPick iPick = iElseIF.addNewPick();
					iPick.set(tmp.getBpelCode());			
				}
				else if(TSequence.class.isInstance(tmp.getBpelCode())){
					TSequence iSeq = iElseIF.addNewSequence();
					iSeq.set(tmp.getBpelCode());
				}
				else if(TIf.class.isInstance(tmp.getBpelCode())){
					TIf iIfSub = iElseIF.addNewIf();
					iIfSub.set(tmp.getBpelCode());
				}
				else if(TAssign.class.isInstance(tmp.getBpelCode())){
					TAssign iAss = iElseIF.addNewAssign();
					iAss.set(tmp.getBpelCode());			
				}
				else if(TFlow.class.isInstance(tmp.getBpelCode())){
					TFlow iFlow = iElseIF.addNewFlow();
					iFlow.set(tmp.getBpelCode());
				}
				else if(TEmpty.class.isInstance(tmp.getBpelCode())){
					TEmpty iEmpty = iElseIF.addNewEmpty();
					iEmpty.set(tmp.getBpelCode());
				}
				else if(TWait.class.isInstance(tmp.getBpelCode())){
					TWait iWait = iElseIF.addNewWait();
					iWait.set(tmp.getBpelCode());
				}
				else if(TReceive.class.isInstance(tmp.getBpelCode())){
					TReceive iReceive = iElseIF.addNewReceive();
					iReceive.set(tmp.getBpelCode());
				}
				else if(TReply.class.isInstance(tmp.getBpelCode())){
					TReply iReply = iElseIF.addNewReply();
					iReply.set(tmp.getBpelCode());
				}
				else if(TInvoke.class.isInstance(tmp.getBpelCode())){
					TInvoke iInvoke = iElseIF.addNewInvoke();
					iInvoke.set(tmp.getBpelCode());
				}
			}
		}			
		return iIf;
	}

}
