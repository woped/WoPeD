package org.woped.bpel.gui.transitionproperties;

//package org.woped.bpel.gui.transitionproperties;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TInvoke;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;


public class Invoke extends BaseActivity
{
	
	public Invoke()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void saveInformation(BPELinvokePanel bip)
	{
		//erzeuge XML-Baum
		TProcess p = BaseActivity.genBpelProcess();
		TInvoke invoke = p.addNewInvoke();
		invoke.setName(""+bip.transition.getNameValue());
		invoke.setPartnerLink(bip.getPartnerLink());
		invoke.setOperation(bip.getOperation());
		//invoke.setPortType(bip.getPortType());
		invoke.setInputVariable(bip.getInVariable());
		invoke.setOutputVariable(bip.getOutVariable());
		this.setActivity(invoke);
	}
	
	public void setInformationToPanel(BPELinvokePanel bip)
	{
		TInvoke invoke = (TInvoke) this.getActivity();
		
		bip.setPartnerLink(invoke.getPartnerLink());
		bip.setOperation(invoke.getOperation());
		//bip.setPortType(invoke.getPortType());
		bip.setInVariable(invoke.getInputVariable());
		bip.setOutVariable(invoke.getOutputVariable());
	}

}
