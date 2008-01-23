package org.woped.bpel.gui.transitionproperties;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReceive;


public class Receive extends BaseActivity
{

	public Receive()
	{
		// TODO Auto-generated constructor stub
	}

	public void saveInformation(BPELreceivePanel brp){

		TProcess p = BaseActivity.genBpelProsses();
		TReceive receive = p.addNewReceive();
		receive.setName(""+brp.transition.getNameValue());
		receive.setPartnerLink(brp.getPartnerLink());
		receive.setOperation(brp.getOperation());
		receive.setVariable(brp.getVariable());
		this.setActivity(receive);
	}

	public void setInformationToPanel(BPELreceivePanel brp){

		TReceive receive = (TReceive)this.getActivity();
		brp.setPartnerLink(receive.getPartnerLink());
		brp.setOperation(receive.getOperation());
		brp.setVariable(receive.getVariable());
	}
}
