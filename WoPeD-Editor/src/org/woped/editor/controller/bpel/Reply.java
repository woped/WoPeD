package org.woped.editor.controller.bpel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TReply;

/**
 * 
 * @author Frank Schüler, Alexander Roßwog
 * 
 */
public class Reply extends BaseActivity<TReply> {

	/**
	 * 
	 * @param Name
	 */
	public Reply(String Name) {
		super(Name);
	}

	/**
	 * 
	 * @param Name
	 * @param PartnerLink
	 * @param Operation
	 * @param Variable
	 */
	public Reply(String Name, String PartnerLink, String Operation,
			String Variable) {
		this(Name);
		this.fillReply(PartnerLink, Operation, Variable);
	}

	/**
	 * 
	 * @param PartnerLink
	 * @param Operation
	 * @param Variable
	 */
	public final void fillReply(String PartnerLink, String Operation,
			String Variable) {
		this.getActivity().setPartnerLink(PartnerLink);
		this.getActivity().setOperation(Operation);
		this.getActivity().setVariable(Variable);
	}

	/**
	 * @param Name
	 */
	protected final void genTActivity(String Name) {
		this.setActivity(TReply.Factory.newInstance());
		this.getActivity().setName(Name);
	}

	/**
	 * @param bip
	 */
	public BaseActivity<?> saveInformation(BPELadditionalPanel bip) {
		if (!BPELreplyPanel.class.isInstance(bip))
			return this;
		BPELreplyPanel panel = (BPELreplyPanel) bip;
		this.fillReply(panel.getPartnerLink(), panel.getOperation(), panel
				.getVariable());
		return this;
	}

	/**
	 * @param bip
	 */
	public void setInformationToPanel(BPELadditionalPanel bip) {
		if (!BPELreplyPanel.class.isInstance(bip))
			return;
		BPELreplyPanel panel = (BPELreplyPanel) bip;
		panel.setPartnerLink(this.getPartnerLink());
		panel.setOperation(this.getOperation());
		panel.setVariable(this.getVariable());
	}

	/**
	 * 
	 * @return
	 */
	public final String getPartnerLink() {
		return this.getActivity().getPartnerLink();
	}

	/**
	 * 
	 * @return
	 */
	public final String getOperation() {
		return this.getActivity().getOperation();
	}

	/**
	 * 
	 * @return
	 */
	public final String getVariable() {
		return this.getActivity().getVariable();
	}

}
