package org.woped.bpel.gui.transitionproperties;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCopy;

/**
 * 
 * @author Alexander Rosswog, Frank Sch�ler(redesign)
 * 
 * 
 *
 */
public class Assign extends BaseActivity<TAssign>
{
	/**
	 * 
	 * @param Name
	 */
	public Assign(String Name)
	{	
		super(Name);
	}
	
	/**
	 * 
	 * @param Name
	 * @param FromVariable
	 * @param ToVariable
	 */
	public Assign(String Name ,String FromVariable, String ToVariable)
	{
		this(Name);
		this.fillAssign(FromVariable, ToVariable);
	}
	
	/**
	 * 
	 * @param Name
	 */
	protected final void genTActivity(String Name)
	{
		this.setActivity(TAssign.Factory.newInstance());
		this.getActivity().setName(Name);
	}
	
	/**
	 * 
	 * @param FromVariable
	 * @param ToVariable
	 */
	public final void fillAssign(String FromVariable, String ToVariable)
	{
		TCopy copy = this.getActivity().getCopyArray(0);
		if(copy == null)
		{
			copy = this.getActivity().addNewCopy();
			copy.addNewFrom().setVariable("" + FromVariable);
			copy.addNewTo().setVariable("" + ToVariable);
		}
		else
		{
			copy.getFrom().setVariable("" + FromVariable);
			copy.getTo().setVariable("" + ToVariable);
		}
				
	}

	/**
	 * 
	 * @param bip
	 */
	public BaseActivity<?> saveInformation(BPELadditionalPanel bip)
	{
		if(!BPELassignPanel.class.isInstance(bip))return this;
		BPELassignPanel panel = (BPELassignPanel)bip;
		this.fillAssign(panel.getFromVariable(), panel.getToVariable());
		return this;
	}

	/**
	 * 
	 * @param bip
	 */
	public void setInformationToPanel(BPELadditionalPanel bip)
	{
		if(!BPELassignPanel.class.isInstance(bip))return;
		BPELassignPanel panel = (BPELassignPanel)bip;
		panel.setFromVariable(this.getFromVariable());
		panel.setToVariable(this.getToVariable());
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFromVariable()
	{
		return this.getActivity().getCopyArray(0).getFrom().getVariable();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getToVariable()
	{
		return this.getActivity().getCopyArray(0).getTo().getVariable();
	}
}
