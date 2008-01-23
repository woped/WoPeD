package org.woped.bpel.gui.transitionproperties;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCopy;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFrom;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TInvoke;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TTo;


public class Assign extends BaseActivity
{
	
	public Assign()
	{
		
	}
	
	public void saveInformation(BPELassignPanel bip)
	{
		TProcess p = BaseActivity.genBpelProsses();
		TAssign assign = p.addNewAssign();
		assign.setName(""+bip.transition.getNameValue());
		TCopy copy = assign.addNewCopy();
		TFrom from = copy.addNewFrom();
		//from.setVariable(bip.getFromVariable());

		from.setVariable(bip.getFromVariable());
		TTo to = copy.addNewTo();
		to.setVariable(bip.getToVariable());
		this.setActivity(assign);
	}
	
	public void setInformationToPanel(BPELassignPanel bip)
	{
		TAssign assign = (TAssign) this.getActivity();
		TCopy copy = assign.getCopyArray(0);
		TFrom from = copy.getFrom();
		String sFromVariable = from.getVariable();
		bip.setFromVariable(sFromVariable);
		
		TTo to = copy.getTo();
		String sToVariable = to.getVariable();
		bip.setToVariable(sToVariable);	
	}

}
