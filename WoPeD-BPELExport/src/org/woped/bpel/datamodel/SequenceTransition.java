package org.woped.bpel.datamodel;

import org.apache.xmlbeans.XmlCursor;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TInvoke;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReceive;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReply;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSequence;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;
import org.woped.bpel.BPEL;
public class SequenceTransition extends TerminalElement
{
	AbstractElement begin;
	TSequence seq = null;

	public SequenceTransition(AbstractElement begin)
	{
		super("test");
		this.begin = begin;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(AbstractElement e)
	{
		if (!SequenceTransition.class.isInstance(e))
			return false;
		if (this.getID() != e.getID())
			return false;
		return true;
	}

	/*
	 * Bpel code for a minimum sequence of 2 transitions
	 * 
	 */
	public TSequence getBpelCode()
	{
		if(this.seq != null) return this.seq;
		TSequence iSeq= null;
		
		
		//1.transition
		//if the 1.transition is a SequenceTransition
		if (SequenceTransition.class.isInstance(begin)){
			iSeq = (TSequence)begin.getBpelCode();
			System.out.println("Sequence found vorne");
		}	
		else{
			System.out.println("Something else found vorne.");
			iSeq = BPEL.genBpelProsses().addNewSequence();
			//transitions case (1.transition)		
			if(TAssign.class.isInstance(begin.getBpelCode())){
				TAssign iAss = iSeq.addNewAssign();
				iAss.set(begin.getBpelCode());			
			}		
			else if(TEmpty.class.isInstance(begin.getBpelCode())){
				TEmpty iEmpty = iSeq.addNewEmpty();
				iEmpty.set(begin.getBpelCode());
			}
			else if(TWait.class.isInstance(begin.getBpelCode())){
				TWait iWait = iSeq.addNewWait();
				iWait.set(begin.getBpelCode());
			}
			else if(TReceive.class.isInstance(begin.getBpelCode())){
				TReceive iReceive = iSeq.addNewReceive();
				iReceive.set(begin.getBpelCode());
			}
			else if(TReply.class.isInstance(begin.getBpelCode())){
				TReply iReply = iSeq.addNewReply();
				iReply.set(begin.getBpelCode());
			}
			else if(TInvoke.class.isInstance(begin.getBpelCode())){
				TInvoke iInvoke = iSeq.addNewInvoke();
				iInvoke.set(begin.getBpelCode());
			}				
		}			
		
		//place between transitions
		AbstractElement tmp = begin.get_first_post_element();		
		
		
		//2.transition
		tmp = tmp.get_first_post_element();	
		
		//if the 2.transition is a SequenceTransition
		if (SequenceTransition.class.isInstance(tmp)){
			TSequence helpSequence = (TSequence)tmp.getBpelCode();
			XmlCursor cursSeq = iSeq.newCursor();
			XmlCursor cursSub = helpSequence.newCursor();
			cursSeq.toEndToken();
			cursSub.copyXmlContents(cursSeq);
			System.out.println("Sequence found hinten");
		}
		
		else{
			System.out.println("Something else found hinten.");
			//transitions case (2.transition)		
			if(TAssign.class.isInstance(tmp.getBpelCode())){
				TAssign iAss = iSeq.addNewAssign();
				iAss.set(tmp.getBpelCode());			
			}		
			else if(TEmpty.class.isInstance(tmp.getBpelCode())){
				TEmpty iEmpty = iSeq.addNewEmpty();
				iEmpty.set(tmp.getBpelCode());
			}
			else if(TWait.class.isInstance(tmp.getBpelCode())){
				TWait iWait = iSeq.addNewWait();
				iWait.set(tmp.getBpelCode());
			}
			else if(TReceive.class.isInstance(tmp.getBpelCode())){
				TReceive iReceive = iSeq.addNewReceive();
				iReceive.set(tmp.getBpelCode());
			}
			else if(TReply.class.isInstance(tmp.getBpelCode())){
				TReply iReply = iSeq.addNewReply();
				iReply.set(tmp.getBpelCode());
			}
			else if(TInvoke.class.isInstance(tmp.getBpelCode())){
				TInvoke iInvoke = iSeq.addNewInvoke();
				iInvoke.set(tmp.getBpelCode());
			}		
		}		
		
		this.seq = iSeq;
		return this.seq;
	}

}
