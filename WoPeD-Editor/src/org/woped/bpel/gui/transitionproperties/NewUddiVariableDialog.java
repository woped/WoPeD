package org.woped.bpel.gui.transitionproperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.woped.bpel.gui.transitionproperties.NewVaraibleDialog.ButtonEvent;
import org.woped.core.model.bpel.BpelVariable;
import org.woped.core.model.bpel.BpelVariableList;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.editor.gui.PopUpDialog;
import org.woped.translations.Messages;

/*
 * @author: Alexander Roﬂwog
 */
public class NewUddiVariableDialog extends JDialog
{
	static final int _OKBUTTON = 0;
	static final int _CANCELBUTTON = 1;

	private JTextField VariableName;
	private JTextField VariableURL;
	private JPanel dialogButtons = null;
	private int activbutton = -1;

	private JComboBox variableTypesComboBox = null;

	TransitionPropertyEditor _editor;

	/**
	 * @param arg0
	 *            TransitionPropertyEditor
	 * @param arg1
	 *            boolean
	 * @throws HeadlessException
	 */
	public NewUddiVariableDialog(TransitionPropertyEditor Editor)
			throws HeadlessException {
		super(Editor, true);
		this._editor = Editor;
		this.init();
	}

	public void init() {
		this.setVisible(false);
		this.setTitle(Messages
				.getString("Transition.Properties.BPEL.NewVariable"));
		this.setSize(400, 150);
		this.setLocation(this.getOwner().getLocation().x + 50, this.getOwner().getLocation().y + 50);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		this.add(new JLabel(Messages
				.getString("Transition.Properties.BPEL.NewVariable.Name")
				+ ":"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 5);
		if (this.VariableName == null)
			this.VariableName = new JTextField("");
		this.add(this.VariableName, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		this.add(new JLabel(Messages
				.getString("Transition.Properties.BPEL.NewVariable.Type")
				+ ":"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 5);
		this.add(VariableURL, c);

		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		this.add(addVariableDialogButtons(), c);

		this.setVisible(true);
	}

	public JPanel addVariableDialogButtons() {
		if (dialogButtons == null) {
			dialogButtons = new JPanel();

			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.NONE;
			c.anchor = GridBagConstraints.WEST;
			c.weightx = 1;
			c.weighty = 1;

			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 5, 0, 0);
			JButton b = new JButton(Messages
					.getString("Transition.Properties.BPEL.Buttons.OK"));
			dialogButtons.add(b, c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 5, 0, 0);
			b = new JButton(Messages
					.getString("Transition.Properties.BPEL.Buttons.Cancel"));
			dialogButtons.add(b, c);
		}
		return dialogButtons;
	}
}
