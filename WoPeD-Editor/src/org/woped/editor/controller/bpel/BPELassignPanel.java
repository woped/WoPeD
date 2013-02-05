package org.woped.editor.controller.bpel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.bpel.BpelVariable;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.editor.gui.PopUpDialog;
import org.woped.gui.translations.Messages;

/**
 * @author Esther Landes
 *
 * This is a panel in the transition properties, which enables the user to
 * maintain data for an "assign" BPEL activity.
 *
 * Created on 15.01.2008
 */

public class BPELassignPanel extends BPELadditionalPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final String _PANELNAME = "assign";

	JLabel fromVariableLabel = null;

	JComboBox fromVariableComboBox = null;

	JButton newFromVariableButton = null;

	JLabel toVariableLabel = null;

	JComboBox toVariableComboBox = null;

	JButton newToVariableButton = null;

	JDialog dialogPartner = null;

	/**
	 *
	 * @param t_editor
	 * @param transition
	 */
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
		add(getFromVariableLabel(), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(getFromVariableComboBox(), c);

		c.weightx = 0;
		c.weighty = 0;
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
		add(getToVariableLabel(), c);

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

	/**
	 *
	 * @return
	 */
	private JLabel getFromVariableLabel() {
		if (fromVariableLabel == null) {
			fromVariableLabel = new JLabel("Variable:");
			fromVariableLabel.setPreferredSize(dimension);
		}
		return fromVariableLabel;
	}

	/**
	 *
	 * @return
	 */
	private JComboBox getFromVariableComboBox() {
		if (fromVariableComboBox == null) {
			fromVariableComboBox = new JComboBox();
			fromVariableComboBox.setPreferredSize(dimension);
			this.fillVariableToComboBox(this.fromVariableComboBox);
		}
		return fromVariableComboBox;
	}

	/**
	 *
	 * @return
	 */
	private JButton getNewFromVariableButton() {
		if (newFromVariableButton == null) {
			newFromVariableButton = new JButton(NEW);

			newFromVariableButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NewVariableDialog dialog = new NewVariableDialog(t_editor);
					if (dialog.getActivButton() == NewVariableDialog._OKBUTTON) {
						fillVariableToComboBox(fromVariableComboBox);
						BpelVariable var = t_editor.getEditor()
								.getModelProcessor().getElementContainer()
								.findBpelVariableByName(
										dialog.getNewVariableName());
						fromVariableComboBox.setSelectedItem(var);

						Object o = toVariableComboBox.getSelectedItem();
						fillVariableToComboBox(toVariableComboBox);
						toVariableComboBox.setSelectedItem(o);
					}
				}
			});
		}
		return newFromVariableButton;
	}

	/**
	 *
	 * @return
	 */
	private JLabel getToVariableLabel() {
		if (toVariableLabel == null) {
			toVariableLabel = new JLabel("Variable:");
			toVariableLabel.setPreferredSize(dimension);
		}
		return toVariableLabel;
	}

	private JComboBox getToVariableComboBox() {
		if (toVariableComboBox == null) {
			toVariableComboBox = new JComboBox();
			toVariableComboBox.setPreferredSize(dimension);
			this.fillVariableToComboBox(this.toVariableComboBox);
		}
		return toVariableComboBox;
	}

	/**
	 *
	 * @return
	 */
	private JButton getNewToVariableButton() {
		if (newToVariableButton == null) {
			newToVariableButton = new JButton(NEW);
			fromVariableComboBox.setPreferredSize(dimension);
			fromVariableComboBox.addItem("-none-");

			newToVariableButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NewVariableDialog dialog = new NewVariableDialog(t_editor);
					if (dialog.getActivButton() == NewVariableDialog._OKBUTTON) {
						fillVariableToComboBox(toVariableComboBox);
						BpelVariable var = t_editor.getEditor()
								.getModelProcessor().getElementContainer()
								.findBpelVariableByName(
										dialog.getNewVariableName());
						toVariableComboBox.setSelectedItem(var);

						Object o = fromVariableComboBox.getSelectedItem();
						fillVariableToComboBox(fromVariableComboBox);
						fromVariableComboBox.setSelectedItem(o);
					}
				}
			});
		}
		return newToVariableButton;
	}

	// ***************** content getter methods **************************

	/**
	 *
	 */
	public String getFromVariable() {
		if (fromVariableComboBox.getSelectedItem() == null
				|| !BpelVariable.class.isInstance(fromVariableComboBox
						.getSelectedItem()))
			return "";
		return ((BpelVariable) fromVariableComboBox.getSelectedItem())
				.getName();
	}

	/**
	 *
	 * @return
	 */
	public String getToVariable() {
		if (toVariableComboBox.getSelectedItem() == null)
			return "";
		return toVariableComboBox.getSelectedItem().toString();
	}

	/**
	 *
	 * @return
	 */
	public boolean allFieldsFilled() {
		if (fromVariableComboBox.getSelectedIndex() == 0
				|| toVariableComboBox.getSelectedIndex() == 0) {
			return false;
		} else
			return true;
	}

	// ***************** content setter methods **************************

	/**
	 *
	 */
	public void setFromVariable(String variable) {
		fromVariableComboBox.addItem(this.t_editor.getEditor()
				.getModelProcessor().getElementContainer()
				.findBpelVariableByName(variable));
	}

	/**
	 *
	 * @param variable
	 */
	public void setToVariable(String variable) {
		toVariableComboBox.addItem(this.t_editor.getEditor()
				.getModelProcessor().getElementContainer()
				.findBpelVariableByName(variable));
	}

	@Override
	public void refresh() {
		Object o = this.fromVariableComboBox.getSelectedItem();
		this.fillVariableToComboBox(this.fromVariableComboBox);
		this.fromVariableComboBox.setSelectedItem(o);
		o = this.toVariableComboBox.getSelectedItem();
		this.fillVariableToComboBox(this.toVariableComboBox);
		this.toVariableComboBox.setSelectedItem(o);

		if (Assign.class.isInstance(this.transition.getBpelData())) {
			Assign assign = (Assign) this.transition.getBpelData();
			ModelElementContainer model = this.t_editor.getEditor()
					.getModelProcessor().getElementContainer();
			BpelVariable var = model.findBpelVariableByName(assign
					.getFromVariable());
			this.fromVariableComboBox.setSelectedItem(var);
			var = model.findBpelVariableByName(assign.getToVariable());
			this.toVariableComboBox.setSelectedItem(var);
		}
		this.repaint();
	}

	@Override
	public void saveInfomation() {
		if (allFieldsFilled() == false) {
			new PopUpDialog(
					t_editor,
					true,
					Messages.getString("Transition.Properties.BPEL.Error"),
					Messages
							.getString("Transition.Properties.BPEL.ErrorDuringFieldCheck"))
					.setVisible(true);
		} else {
			this.transition.setBaseActivity(new Assign(this.transition
					.getNameValue(), this.getFromVariable(), this
					.getToVariable()));
		}
	}

	@Override
	public String getName() {
		return this._PANELNAME;
	}

	@Override
	public void showPanel(JPanel panel, GridBagConstraints c) {
		this.refresh();
		panel.add(this, c);
	}
}
