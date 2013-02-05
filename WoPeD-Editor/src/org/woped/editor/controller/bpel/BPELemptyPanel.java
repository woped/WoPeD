package org.woped.editor.controller.bpel;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;

import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.gui.translations.Messages;

public class BPELemptyPanel extends BPELadditionalPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BPELemptyPanel(TransitionPropertyEditor t_editor,
			TransitionModel transition) {
		super(t_editor, transition);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return Messages.getString("Transition.Properties.BPEL.NoActivity");
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveInfomation() {
		this.transition.setBaseActivity(new Empty(this.transition.getNameValue()));
	}

	@Override
	public void showPanel(JPanel panel, GridBagConstraints c) {
		panel.add(this,c);
	}

	@Override
	public boolean allFieldsFilled() {
		return true;
	}

}
