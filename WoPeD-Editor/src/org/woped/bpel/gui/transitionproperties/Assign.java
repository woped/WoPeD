package org.woped.bpel.gui.transitionproperties;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCopy;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFrom;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TTo;

/**
 * 
 * @author Alexander Rosswog, Frank Schüler(redesign)
 * 
 * 
 *
 */
public class Assign extends BaseActivity<TAssign>
{
	private TFrom _from = null;
	private TTo _to = null;

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
		TAssign assign = this.getActivity();
		assign.setName(Name);
		TCopy copy = assign.addNewCopy();
		this._from = copy.addNewFrom();
		this._to = copy.addNewTo();
	}
	
	/**
	 * 
	 * @param FromVariable
	 * @param ToVariable
	 */
	public final void fillAssign(String FromVariable, String ToVariable)
	{
		this._from.setVariable("" + FromVariable);
		this._to.setVariable("" + ToVariable);
	}

	/**
	 * 
	 * @param bip
	 */
	public void saveInformation(BPELadditionalPanel bip)
	{
		if(!BPELassignPanel.class.isInstance(bip))return;
		BPELassignPanel panel = (BPELassignPanel)bip;
		this.fillAssign(panel.getFromVariable(), panel.getToVariable());
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
		return this._from.getVariable();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getToVariable()
	{
		return this._to.getVariable();
	}
}
