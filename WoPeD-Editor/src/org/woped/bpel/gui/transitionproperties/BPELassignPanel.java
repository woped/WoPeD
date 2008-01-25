package org.woped.bpel.gui.transitionproperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.bpel.BpelVariable;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.TransitionPropertyEditor;

/**
 * @author Esther Landes
 * 
 * This is a panel in the transition properties, which enables the user to
 * maintain data for an "assign" BPEL activity.
 * 
 * Created on 15.01.2008
 */

@SuppressWarnings("serial")
public class BPELassignPanel extends BPELadditionalPanel {

	JComboBox fromVariableComboBox = null;
	JButton newFromVariableButton = null;
	JComboBox toVariableComboBox = null;
	JButton newToVariableButton = null;

	JDialog dialogPartner = null;

	public BPELassignPanel(TransitionPropertyEditor t_editor,
			TransitionModel transition) {

		super(t_editor, transition);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(new JLabel("From:"), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(new JLabel("Variable:"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(getFromVariableComboBox(), c);

		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		add(getNewFromVariableButton(), c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(15, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("To:"), c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(new JLabel("Variable:"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(getToVariableComboBox(), c);

		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		add(getNewToVariableButton(), c);
	}

	private JComboBox getFromVariableComboBox() {
		if (fromVariableComboBox == null) {
			fromVariableComboBox = new JComboBox();
			fromVariableComboBox.setPreferredSize(dimension);
			this.fillVariableToComboBox(this.fromVariableComboBox);
		}
		return fromVariableComboBox;
	}

	private JButton getNewFromVariableButton() {
		if (newFromVariableButton == null) {
			newFromVariableButton = new JButton(NEW);

			newFromVariableButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NewVaraibleDialog dialog = new NewVaraibleDialog(t_editor);
					if (dialog.getActivButton() == NewVaraibleDialog._OKBUTTON) {
						fromVariableComboBox.addItem(dialog
								.getNewVariableName());
						fromVariableComboBox
								.setSelectedIndex(fromVariableComboBox
										.getItemCount() - 1);
						fillVariableToComboBox(fromVariableComboBox);
						BpelVariable var = t_editor.getEditor()
								.getModelProcessor().getElementContainer()
								.findBpelVariableByName(
										dialog.getNewVariableName());
						fromVariableComboBox.setSelectedItem(var);
					}
				}
			});
		}
		return newFromVariableButton;
	}

	private JComboBox getToVariableComboBox() {
		if (toVariableComboBox == null) {
			toVariableComboBox = new JComboBox();
			toVariableComboBox.setPreferredSize(dimension);
			this.fillVariableToComboBox(this.toVariableComboBox);
		}
		return toVariableComboBox;
	}

	private JButton getNewToVariableButton() {
		if (newToVariableButton == null) {
			newToVariableButton = new JButton(NEW);

			newToVariableButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NewVaraibleDialog dialog = new NewVaraibleDialog(t_editor);
					if (dialog.getActivButton() == NewVaraibleDialog._OKBUTTON) {
						toVariableComboBox.addItem(dialog.getNewVariableName());
						toVariableComboBox.setSelectedIndex(toVariableComboBox
								.getItemCount() - 1);
					}
				}
			});
		}
		return newToVariableButton;
	}

	// ***************** content getter methods **************************

	public String getFromVariable() {
		if (fromVariableComboBox.getSelectedItem() == null)
			return "";
		return fromVariableComboBox.getSelectedItem().toString();
	}

	public String getToVariable() {
		if (toVariableComboBox.getSelectedItem() == null)
			return "";
		return toVariableComboBox.getSelectedItem().toString();
	}

	// ***************** content setter methods **************************

	public void setFromVariable(String variable) {
		fromVariableComboBox.addItem(variable);
	}

	public void setToVariable(String variable) {
		toVariableComboBox.addItem(variable);
	}

	@Override
	public void refresh() {
		this.fillVariableToComboBox(this.fromVariableComboBox);
		this.fillVariableToComboBox(this.toVariableComboBox);

		if (Assign.class.isInstance(this.transition.getBpelData())) {
			Assign assign = (Assign) this.transition.getBpelData();
			ModelElementContainer model = this.t_editor.getEditor()
					.getModelProcessor().getElementContainer();
			BpelVariable var = model.findBpelVariableByName(assign.getFromVariable());
		}
	}
}
