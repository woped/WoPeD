/**
 * 
 */
package org.woped.bpel.datamodel;

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

/**
 * @author Frank Schüler
 * 
 */
public class WhileTransition extends TerminalElement<TWhile>
{

	private AbstractElement<?>	begin;

	/**
	 * @param
	 */
	public WhileTransition(AbstractElement<?> Begin)
	{
		super(null);
		this.begin = Begin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.bpel.datamodel.AbstractElement#equals(org.woped.bpel.datamodel.AbstractElement)
	 */
	@Override
	public boolean equals(AbstractElement<?> e)
	{
		if (!WhileTransition.class.isInstance(e))
			return false;
		if (this.getID() != e.getID())
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.woped.bpel.datamodel.AbstractElement#getBpelCode()
	 */
	@Override
	public TWhile getBpelCode()
	{
		if (this.getData() != null)
			return this.getData();
		TWhile iWhile = TWhile.Factory.newInstance();
		AbstractElement<?> tmp = this.begin.get_first_post_element().get_first_post_element();
		if(TPick.class.isInstance(tmp.getBpelCode())){
			TPick iPick = iWhile.addNewPick();
			iPick.set(tmp.getBpelCode());			
		}
		else if(TSequence.class.isInstance(tmp.getBpelCode())){
			TSequence iSeq = iWhile.addNewSequence();
			iSeq.set(tmp.getBpelCode());
		}
		else if(TIf.class.isInstance(tmp.getBpelCode())){
			TIf iIfSub = iWhile.addNewIf();
			iIfSub.set(tmp.getBpelCode());
		}
		else if(TAssign.class.isInstance(tmp.getBpelCode())){
			TAssign iAss = iWhile.addNewAssign();
			iAss.set(tmp.getBpelCode());			
		}
		else if(TFlow.class.isInstance(tmp.getBpelCode())){
			TFlow iFlow = iWhile.addNewFlow();
			iFlow.set(tmp.getBpelCode());
		}
		else if(TEmpty.class.isInstance(tmp.getBpelCode())){
			TEmpty iEmpty = iWhile.addNewEmpty();
			iEmpty.set(tmp.getBpelCode());
		}
		else if(TWait.class.isInstance(tmp.getBpelCode())){
			TWait iWait = iWhile.addNewWait();
			iWait.set(tmp.getBpelCode());
		}
		else if(TReceive.class.isInstance(tmp.getBpelCode())){
			TReceive iReceive = iWhile.addNewReceive();
			iReceive.set(tmp.getBpelCode());
		}
		else if(TReply.class.isInstance(tmp.getBpelCode())){
			TReply iReply = iWhile.addNewReply();
			iReply.set(tmp.getBpelCode());
		}
		else if(TInvoke.class.isInstance(tmp.getBpelCode())){
			TInvoke iInvoke = iWhile.addNewInvoke();
			iInvoke.set(tmp.getBpelCode());
		}
		else if(TWhile.class.isInstance(tmp.getBpelCode()))
		{
			TWhile iwhile = iWhile.addNewWhile();
			iwhile.set(tmp.getBpelCode());
		}
		this.setData(iWhile);
		return this.getData();
	}

}
