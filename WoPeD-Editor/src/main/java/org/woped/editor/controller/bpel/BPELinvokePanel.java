package org.woped.editor.controller.bpel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.woped.core.model.bpel.BpelVariable;
import org.woped.core.model.bpel.Partnerlink;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.editor.controller.wsdl.NoPortTypeFoundException;
import org.woped.editor.controller.wsdl.Operation;
import org.woped.editor.controller.wsdl.Wsdl;
import org.woped.gui.translations.Messages;

/**
 * @author Esther Landes / Kristian Kindler
 *
 * This is a panel in the transition properties, which enables the user to
 * maintain data for an "invoke" BPEL activity.
 *
 * Created on 08.01.2008
 */

public class BPELinvokePanel extends BPELadditionalPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final String PANELNAME = "invoke";

	JLabel partnerLinkLabel = null;

	JComboBox partnerLinkComboBox = null;

	JButton newPartnerLinkButton = null;

	JLabel operationLabel = null;

	JComboBox operationComboBox = null;

	JLabel inVariableLabel = null;

	JComboBox inVariableComboBox = null;

	JButton newInVariableButton = null;

	JLabel outVariableLabel = null;

	JComboBox outVariableComboBox = null;

	JButton newOutVariableButton = null;

	public BPELinvokePanel(TransitionPropertyEditor t_editor,
			TransitionModel transition) {

		super(t_editor, transition);

		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(getPartnerLinkLabel(), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(getPartnerLinkComboBox(), c);

		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		add(getNewPartnerLinkButton(), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		add(getOperationLabel(), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(getOperationComboBox(), c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(getInVariableLabel(), c);

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(getInVariableComboBox(), c);

		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		add(getNewInVariableButton(), c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0); // beim 3. 10
		c.fill = GridBagConstraints.HORIZONTAL;
		add(getOutVariableLabel(), c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0); // beim 3. 10
		add(getOutVariableComboBox(), c);

		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0); // beim 3. 10
		c.fill = GridBagConstraints.NONE;
		add(getNewOutVariableButton(), c);
	}

	private JLabel getPartnerLinkLabel() {
		if (partnerLinkLabel == null) {
			partnerLinkLabel = new JLabel("Partner Link:");
			partnerLinkLabel.setPreferredSize(dimension);
		}
		return partnerLinkLabel;
	}


	private JComboBox getPartnerLinkComboBox() {
		if (partnerLinkComboBox == null) {
			partnerLinkComboBox = new JComboBox();
			partnerLinkComboBox.setPreferredSize(dimension);

			defineContentOfPartnerLinkComboBox();
			partnerLinkComboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {

					/*
					 * defineContentOfOperationComboBox(((Partnerlink)
					 * e.getItem()). getWsdlUrl(), ((Partnerlink) e.getItem()).
					 * getTPartnerlink().getPartnerRole());
					 */

					if (e.getItem() instanceof Partnerlink){
						defineContentOfOperationComboBox(((Partnerlink) e.getItem()).
							 getWsdlUrl(), ((Partnerlink) e.getItem()).
							 	getTPartnerlink().getPartnerRole());
					}

				}
			});
		}
		return partnerLinkComboBox;
	}

	private JButton getNewPartnerLinkButton() {
		if (newPartnerLinkButton == null) {
			setLinkToBPELinvokePanel(this);
			newPartnerLinkButton = new JButton(NEW);
			newPartnerLinkButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showNewPartnerLinkDialog();
				}
			});
		}
		return newPartnerLinkButton;
	}

	private JLabel getOperationLabel() {
		if (operationLabel == null) {
			operationLabel = new JLabel("Operation:");
			operationLabel.setPreferredSize(dimension);
		}
		return operationLabel;
	}

	private JComboBox getOperationComboBox() {
		if (operationComboBox == null) {
			operationComboBox = new JComboBox();
			operationComboBox.setPreferredSize(dimension);
		}
		return operationComboBox;
	}

	private JLabel getInVariableLabel() {
		if (inVariableLabel == null) {
			inVariableLabel = new JLabel("Input Variable:");
			inVariableLabel.setPreferredSize(dimension);
		}
		return inVariableLabel;
	}

	private JComboBox getInVariableComboBox() {
		if (inVariableComboBox == null) {
			inVariableComboBox = new JComboBox();
			inVariableComboBox.setPreferredSize(dimension);
			this.fillVariableToComboBox(inVariableComboBox);
		}
		return inVariableComboBox;
	}

	private JButton getNewInVariableButton() {
		if (newInVariableButton == null) {
			newInVariableButton = new JButton(NEW);

			newInVariableButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NewVariableDialog dialog = new NewVariableDialog(t_editor);
					if (dialog.getActivButton() == NewVariableDialog._OKBUTTON) {
						fillVariableToComboBox(inVariableComboBox);
						BpelVariable var = t_editor.getEditor()
								.getModelProcessor().getElementContainer()
								.findBpelVariableByName(
										dialog.getNewVariableName());
						inVariableComboBox.setSelectedItem(var);

						Object o = outVariableComboBox.getSelectedItem();
						fillVariableToComboBox(outVariableComboBox);
						outVariableComboBox.setSelectedItem(o);
					}
				}
			});

		}
		return newInVariableButton;
	}

	private JLabel getOutVariableLabel() {
		if (outVariableLabel == null) {
			outVariableLabel = new JLabel("Output Variable:");
			outVariableLabel.setPreferredSize(dimension);
		}
		return outVariableLabel;
	}

	private JComboBox getOutVariableComboBox() {
		if (outVariableComboBox == null) {
			outVariableComboBox = new JComboBox();
			outVariableComboBox.setPreferredSize(dimension);
			this.fillVariableToComboBox(outVariableComboBox);
		}
		return outVariableComboBox;
	}

	private JButton getNewOutVariableButton() {
		if (newOutVariableButton == null) {
			newOutVariableButton = new JButton(NEW);

			newOutVariableButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NewVariableDialog dialog = new NewVariableDialog(t_editor);
					if (dialog.getActivButton() == NewVariableDialog._OKBUTTON) {
						fillVariableToComboBox(outVariableComboBox);
						BpelVariable var = t_editor.getEditor()
								.getModelProcessor().getElementContainer()
								.findBpelVariableByName(
										dialog.getNewVariableName());
						outVariableComboBox.setSelectedItem(var);

						Object o = inVariableComboBox.getSelectedItem();
						fillVariableToComboBox(inVariableComboBox);
						inVariableComboBox.setSelectedItem(o);
					}
				}
			});
		}
		return newOutVariableButton;
	}

	// fill partnerLinkComboBox with partner links
	public void defineContentOfPartnerLinkComboBox() {
		partnerLinkComboBox.removeAllItems();
		HashSet<Partnerlink> partnerlinkList = modelElementContainer
				.getPartnerlinkList().getPartnerlinkList();
		Iterator<Partnerlink> i = partnerlinkList.iterator();
		while (i.hasNext()) {
			partnerLinkComboBox.addItem(i.next());
		}
	}

	public void defineContentOfOperationComboBox(String pathToWsdlFile,
			String roleName) {
		ArrayList<Operation> operations;


		if (wsdlFileRepresentation == null){
			try {
				wsdlFileRepresentation = new Wsdl().readDataFromWSDL(pathToWsdlFile);
			}
			catch (Exception e) {
				e.printStackTrace();
				showErrorPopup(
						Messages.getString("Transition.Properties.BPEL.ErrorWhileReadingWsdlFileTitle"),
						Messages.getString("Transition.Properties.BPEL.ErrorWhileReadingVariables"));
			}
		}
		String portTypeName = wsdlFileRepresentation.getPortTypeNameByRoleName(roleName);
		try {
			operationComboBox.removeAllItems();

			wsdlFileRepresentation = wsdl.readDataFromWSDL(pathToWsdlFile);
			operations = wsdlFileRepresentation.getPortType(portTypeName)
					.getOperations();
			for (Operation operation : operations) {
				setOperation(operation.getOperationName());
			}

		} catch (NoPortTypeFoundException e1) {
			// This exception won't be raised because there will definitely be a
			// port type.
		} catch (Exception e) {
			showErrorPopup(
					Messages
							.getString("Transition.Properties.BPEL.ErrorWhileReadingWsdlFileTitle"),
					Messages
							.getString("Transition.Properties.BPEL.ErrorWhileReadingOperation"));
		}
	}


	// ***************** content getter methods **************************

	public String getPartnerLink() {
		if (partnerLinkComboBox.getSelectedItem() == null)
			return "";
		return partnerLinkComboBox.getSelectedItem().toString();
	}

	public String getOperation() {
		if (operationComboBox.getSelectedItem() == null)
			return "";
		return operationComboBox.getSelectedItem().toString();
	}

	public String getPortType() {
		// TODO
		return "";
	}

	public String getInVariable() {
		if (inVariableComboBox.getSelectedItem() == null)
			return "";
		return inVariableComboBox.getSelectedItem().toString();
	}

	public String getOutVariable() {
		if (outVariableComboBox.getSelectedItem() == null)
			return "";
		return outVariableComboBox.getSelectedItem().toString();
	}

	public boolean allFieldsFilled() {
		if (partnerLinkComboBox.getSelectedIndex() == 0
				|| operationComboBox.getSelectedIndex() == 0
				|| inVariableComboBox.getSelectedIndex() == 0
				|| outVariableComboBox.getSelectedIndex() == 0)
			return false;
		return true;
	}

	// ***************** content setter methods **************************

	public void setPartnerLink(String partnerLink) {
		partnerLinkComboBox.addItem(partnerLink);
	}

	public void setOperation(String operation) {
		operationComboBox.addItem(operation);
	}

	public void setPortType(String porttype) {
		// TODO
	}

	public void setInVariable(String inVariable) {
		inVariableComboBox.addItem(inVariable);
	}

	public void setOutVariable(String outVariable) {
		outVariableComboBox.addItem(outVariable);
	}

	@Override
	public void refresh() {
		defineContentOfPartnerLinkComboBox();
		Object o = this.inVariableComboBox.getSelectedItem();
		this.fillVariableToComboBox(this.inVariableComboBox);
		this.inVariableComboBox.setSelectedItem(o);
		o = this.outVariableComboBox.getSelectedItem();
		this.fillVariableToComboBox(this.outVariableComboBox);
		this.outVariableComboBox.setSelectedItem(o);

		if (Invoke.class.isInstance(this.transition.getBpelData())) {
			Invoke in = (Invoke) this.transition.getBpelData();
			this.inVariableComboBox.setSelectedItem(this.t_editor.getEditor().getModelProcessor().getElementContainer().findBpelVariableByName(in.getInputVariable()));
			this.outVariableComboBox.setSelectedItem(this.t_editor.getEditor().getModelProcessor().getElementContainer().findBpelVariableByName(in.getOutputVariable()));
			this.setPartnerLink(in.getPartnerLink());
			this.setOperation(in.getOperation());
		}
		this.repaint();
	}

	@Override
	public void saveInfomation() {

//		if (allFieldsFilled() == false) {
//			new PopUpDialog(
//					t_editor,
//					true,
//					Messages.getString("Transition.Properties.BPEL.Error"),
//					Messages
//							.getString("Transition.Properties.BPEL.ErrorDuringFieldCheck"))
//					.setVisible(true);
//		} else {
			this.transition.setBaseActivity(new Invoke(this.transition
					.getNameValue(), this.getPartnerLink(),
					this.getOperation(), this.getPortType(), this
							.getInVariable(), this.getOutVariable()));
//		}
	}
	//	TODO: empty initial entry must be added

	@Override
	public String getName() {
		return this.PANELNAME;
	}

	@Override
	public void showPanel(JPanel panel, GridBagConstraints c) {
		this.refresh();
		panel.add(this, c);
	}
}
