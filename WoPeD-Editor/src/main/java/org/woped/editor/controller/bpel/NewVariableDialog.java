/**
 * 
 */
package org.woped.editor.controller.bpel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.woped.core.model.bpel.BpelVariable;
import org.woped.core.model.bpel.BpelVariableList;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.editor.gui.PopUpDialog;
import org.woped.gui.translations.Messages;

/**
 * @author Frank Sch���ler, Ester Landes
 * 
 */


@SuppressWarnings("serial")
public class NewVariableDialog extends JDialog {

	static final int _OKBUTTON = 0;
	static final int _CANCELBUTTON = 1;

	private JTextField VariableName;
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
	public NewVariableDialog(TransitionPropertyEditor Editor)
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
		this.add(getVariableTypesComboBox(), c);

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
			b.addActionListener(new ButtonEvent(this,
					NewVariableDialog._OKBUTTON));
			dialogButtons.add(b, c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 5, 0, 0);
			b = new JButton(Messages
					.getString("Transition.Properties.BPEL.Buttons.Cancel"));
			b.addActionListener(new ButtonEvent(this,
					NewVariableDialog._CANCELBUTTON));
			dialogButtons.add(b, c);
		}
		return dialogButtons;
	}

	private JComboBox getVariableTypesComboBox() {
		if (variableTypesComboBox == null) {
			variableTypesComboBox = new JComboBox();
			String[] variables = BpelVariable.getTypes();
			for (int i = 0; i < variables.length; i++) {
				variableTypesComboBox.addItem(variables[i]);
			}
		}
		return variableTypesComboBox;
	}

	public String getNewVariableName() {
		return "" + this.VariableName.getText();
	}

	public TransitionPropertyEditor getTransitionPropertyEditor() {
		return this._editor;
	}

	public void setActivButton(int Buttontype) {
		this.activbutton = Buttontype;
	}

	public int getActivButton() {
		return this.activbutton;
	}

	public boolean isInputOk() {
		BpelVariableList list = this._editor.getEditor().getModelProcessor()
				.getElementContainer().getVariableList();
		if(this.VariableName.getText().length() == 0)
		{
			new PopUpDialog(this,true,"Fehler","Keinen Variablenamen eingeben!").setVisible(true);
			return false;
		}
		if(list.findBpelVaraibleByName(this.VariableName.getText()) != null)
		{
			new PopUpDialog(this,true,"Fehler", "Fehler: Die zu erstellende Bpel-Variable existiert bereits!").setVisible(true);
			return false;
		}
		return true;
	}

	class ButtonEvent implements ActionListener {
		private NewVariableDialog _adaptee;
		private int _buttontype = -1;

		public ButtonEvent(NewVariableDialog Adaptee, int Buttontype) {
			this._adaptee = Adaptee;
			this._buttontype = Buttontype;
		}

		public void actionPerformed(ActionEvent arg0) {
			if (this._buttontype == NewVariableDialog._OKBUTTON) {
				if (!this._adaptee.isInputOk())
					return;
				this._adaptee.getTransitionPropertyEditor().getEditor()
						.getModelProcessor().getElementContainer().addVariable(
								VariableName.getText(),
								getVariableTypesComboBox().getSelectedItem()
										.toString());
			}
			this._adaptee.setActivButton(this._buttontype);
			this._adaptee.dispose();
		}
	}
}
