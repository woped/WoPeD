package org.woped.bpel.gui.transitionproperties;

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
	}
	
	public void setInformation(BPELreplyPanel brp)
	{
		
	}
	
}
