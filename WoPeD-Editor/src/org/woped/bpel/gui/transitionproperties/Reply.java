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
	
	public void saveInformation(BPELreplyPanel brp)
	{
		TProcess p = BaseActivity.genBpelProsses();
		TReply reply = p.addNewReply();
		reply.setName(""+brp.transition.getNameValue());
		
		//PartnerLink
		reply.setPartnerLink(brp.getPartnerLink());
		//Operation
		reply.setOperation(brp.getOperation());
		//InputVariable
		reply.setVariable(brp.getVariable());
		this.setActivity(reply);
	}
	
	public void setInformationToPanel(BPELreplyPanel brp)
	{
		TReply reply = (TReply) this.getActivity();
		brp.setPartnerLink(reply.getPartnerLink());
		brp.setOperation(reply.getOperation());
		brp.setVariable(reply.getVariable());
	}
	
}
