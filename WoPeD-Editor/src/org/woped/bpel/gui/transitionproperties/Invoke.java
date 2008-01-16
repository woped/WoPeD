package org.woped.bpel.gui.transitionproperties;

//package org.woped.bpel.gui.transitionproperties;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
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
		TProcess p = BaseActivity.genBpelProsses();
		TInvoke invoke = p.addNewInvoke();
		//invoke.setPartnerLink(bip.getPartnerLink());
		//invoke.setOperation(bip.getOperation());
		//invoke.setPortType(bip.getPortType());????
		//invoke.setInputVariable(bip.getInputVariable());
		//invoke.setOutputVariable(bip.getInputVariable());
	}
	
	public void setInformation(BPELinvokePanel bip)
	{
		TInvoke invoke = (TInvoke) this.getActivity();
		
		//bip.setPartnerLink(invoke.getPartnerLink());
		//bip.setOperation(invoke.getOperation());
		//bip.getPortType(invoke.getPortType());
		//bip.setInputVariable(invoke.getInputVariable());
		//bip.setOutputVariable(invoke.getOutputVariable());
	}

}
