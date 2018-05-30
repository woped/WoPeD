package org.woped.editor.controller.bpel;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TInvoke;

/*
 * 
 * @author Alexander Roßwog, Frank Schüler
 * 
 * */

public class Invoke extends BaseActivity<TInvoke> {

	/**
	 * 
	 * @param Name
	 */
	public Invoke(String Name) {
		super(Name);
	}

	/**
	 * 
	 * @param Name
	 * @param PartnerLink
	 * @param Operation
	 * @param PortType
	 * @param InVariable
	 * @param OutVariable
	 */
	public Invoke(String Name, String PartnerLink, String Operation,
			String PortType, String InVariable, String OutVariable) {
		this(Name);
		this.fillInvoke(PartnerLink, Operation, PortType, InVariable,
				OutVariable);
	}

	/**
	 * 
	 * @param Name
	 */
	protected final void genTActivity(String Name) {
		this.setActivity(TInvoke.Factory.newInstance());
		this.getActivity().setName(Name);
	}

	/**
	 * 
	 * @param PartnerLink
	 * @param Operation
	 * @param PortType
	 * @param InVariable
	 * @param OutVariable
	 */
	public final void fillInvoke(String PartnerLink, String Operation,
			String PortType, String InVariable, String OutVariable) {
		this.getActivity().setPartnerLink("" + PartnerLink);
		this.getActivity().setOperation("" + Operation);
		// this.getActivity().setPortType("" + PortType);
		this.getActivity().setInputVariable("" + InVariable);
		this.getActivity().setOutputVariable("" + OutVariable);
	}

	/**
	 * @param bip
	 */
	public BaseActivity<?> saveInformation(BPELadditionalPanel bip) {
		if (!BPELinvokePanel.class.isInstance(bip))
			return this;
		BPELinvokePanel panel = (BPELinvokePanel) bip;
		this.fillInvoke(panel.getPartnerLink(), panel.getOperation(), panel
				.getPortType(), panel.getInVariable(), panel.getOutVariable());
		return this;
	}

	/**
	 * @param bip
	 */
	public void setInformationToPanel(BPELadditionalPanel bip) {
		if (!BPELinvokePanel.class.isInstance(bip))
			return;
		BPELinvokePanel panel = (BPELinvokePanel) bip;

		panel.setPartnerLink("" + this.getActivity().getPartnerLink());
		panel.setOperation("" + this.getActivity().getOperation());
		// panel.setPortType("" + this.getActivity().getPortType());
		panel.setInVariable("" + this.getActivity().getInputVariable());
		panel.setOutVariable("" + this.getActivity().getOutputVariable());
	}

	/**
	 * 
	 * @return
	 */
	public String getPartnerLink() {
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
	public final String getPortType() {
		return this.getActivity().getPortType().getLocalPart();
	}

	/**
	 * 
	 * @return
	 */
	public final String getInputVariable() {
		return this.getActivity().getInputVariable();
	}

	/**
	 * 
	 * @return
	 */
	public final String getOutputVariable() {
		return this.getActivity().getOutputVariable();
	}
}
