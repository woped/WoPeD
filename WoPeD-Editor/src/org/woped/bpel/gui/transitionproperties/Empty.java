package org.woped.bpel.gui.transitionproperties;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;

/**
 * 
 * @author Alexander Rosswog, Frank Schüler
 *
 */
public class Empty extends BaseActivity<TEmpty>
{

	public Empty(String Name)
	{
		super(Name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void genTActivity(String Name) {
		this.setActivity(TEmpty.Factory.newInstance());
		this.getActivity().setName(Name);		
	}

	@Override
	public BaseActivity<?> saveInformation(BPELadditionalPanel bip) {
		return this;
	}

	@Override
	public void setInformationToPanel(BPELadditionalPanel bip) {
		
	}

}
