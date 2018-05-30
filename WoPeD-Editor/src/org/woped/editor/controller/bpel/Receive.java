package org.woped.editor.controller.bpel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TReceive;

/**
 * 
 * @author Frank Schüler
 * 
 */
/**
 * @author Titus
 *
 */
/**
 * @author Titus
 *
 */
public class Receive extends BaseActivity<TReceive> {

	/**
	 * 
	 * @param Name
	 */
	public Receive(String Name) {
		super(Name);
	}

	/**
	 * 
	 * @param Name
	 * @param PartnerLink
	 * @param Opetration
	 * @param Variable
	 */
	public Receive(String Name, String PartnerLink, String Opetration,
			String Variable) {
		this(Name);
		this.fillReceive(PartnerLink, Opetration, Variable);
	}

	
	/**
	 * @param PartnerLink
	 * @param Operation
	 * @param Variable
	 */
	public final void fillReceive(String PartnerLink, String Operation,
			String Variable) {
		this.getActivity().setPartnerLink(PartnerLink);
		this.getActivity().setOperation(Operation);
		this.getActivity().setVariable(Variable);
	}
	
	/**
	 * @param Name
	 */
	protected final void genTActivity(String Name) {
		this.setActivity(TReceive.Factory.newInstance());
		this.getActivity().setName(Name);
	}

	/**
	 * @param bip
	 */
	public BaseActivity<?> saveInformation(BPELadditionalPanel bip) {
		if (!BPELreceivePanel.class.isInstance(bip))
			return this;;
		BPELreceivePanel panel = (BPELreceivePanel) bip;
		this.fillReceive(panel.getPartnerLink(), panel.getOperation(), panel
				.getVariable());
		return this;
	}

	/**
	 * @param bip
	 */
	public void setInformationToPanel(BPELadditionalPanel bip) {
		if (!BPELreceivePanel.class.isInstance(bip))
			return;
		BPELreceivePanel panel = (BPELreceivePanel) bip;
		panel.setPartnerLink(this.getPartnerLink());
		panel.setOperation(this.getOperation());
		panel.setVariable(this.getVariable());
	}
	
	/**
	 * 
	 * @return
	 */
	public final String getPartnerLink()
	{
		return this.getActivity().getPartnerLink();
	}
	
	/**
	 * 
	 * @return
	 */
	public final String getOperation()
	{
		return this.getActivity().getOperation();
	}
	
	/**
	 * 
	 * @return
	 */
	public final String getVariable()
	{
		return this.getActivity().getVariable();
	}
}
