package org.woped.bpel.gui.transitionproperties;

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
		
		String sPartnerLink = invoke.getPartnerLink();
		//bip.setPartnerLink(sPartnerLink);
		
		String sOperation = invoke.getOperation();
		//bip.setOperation(sOperation);
		
		//String sPortType = invoke.getPortType();
		//bip.getPortType(sPortType);
		
		String sInputVariable = invoke.getInputVariable();
		//bip.setInputVariable(sInputVariable);
		
		String sOutputVariable = invoke.getOutputVariable();
		//bip.setOutputVariable(sOutputVariable);
	}

}
