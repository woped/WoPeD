package org.woped.bpel.gui.transitionproperties;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCopy;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReply;


public class Reply extends BaseActivity
{

	public Reply()
	{
		
	}
	
	public void saveInformation(BPELreplyPanel bip)
	{
		TProcess p = BaseActivity.genBpelProsses();
		TReply reply = p.addNewReply();
		
		//PartnerLink
		reply.setPartnerLink(bip.getPartnerLink());
		//Operation
		reply.setOperation(bip.getOperation());
		//InputVariable
		reply.setVariable(bip.getVariable());
	}
	
	public void setInformation(BPELreplyPanel brp)
	{
		TReply reply = (TReply) this.getActivity();
		
	}
	
}
