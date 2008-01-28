package org.woped.bpel.gui.transitionproperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import javax.swing.JLabel;

import org.woped.bpel.wsdl.exceptions.NoPortTypeFoundException;
import org.woped.bpel.wsdl.wsdlFileRepresentation.Message;
import org.woped.bpel.wsdl.wsdlFileRepresentation.Operation;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.*;
import org.woped.translations.Messages;

/**
 * @author Esther Landes / Kristian Kindler
 *
 * This is a panel in the transition properties, which enables the user to
 * maintain data for an "invoke" BPEL activity.
 *
 * Created on 08.01.2008
 */

@SuppressWarnings("serial")
public class BPELinvokePanel extends BPELadditionalPanel {

	JComboBox partnerLinkComboBox = null;
	JButton newPartnerLinkButton = null;
	JComboBox operationComboBox = null;
	JComboBox inVariableComboBox = null;
	JButton newInVariableButton = null;
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
		add(new JLabel("Partner Link:"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(getPartnerLinkComboBox(), c);

		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		add(getNewPartnerLinkButton(), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Operation:"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(getOperationComboBox(), c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(new JLabel("Input Variable:"), c);

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
		c.insets = new Insets(5, 5, 10, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Output Variable:"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 10, 0);
		add(getOutVariableComboBox(), c);

		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 10, 0);
		c.fill = GridBagConstraints.NONE;
		add(getNewOutVariableButton(), c);
	}

	private JComboBox getPartnerLinkComboBox() {
		if (partnerLinkComboBox == null) {
			partnerLinkComboBox = new JComboBox();
			partnerLinkComboBox.setPreferredSize(dimension);
			defineContentOfPartnerLinkComboBox();
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

	private JComboBox getOperationComboBox() {
		if (operationComboBox == null) {
			operationComboBox = new JComboBox();
			operationComboBox.setPreferredSize(dimension);
		}
		return operationComboBox;
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
						inVariableComboBox.addItem(dialog.getNewVariableName());
						inVariableComboBox.setSelectedIndex(inVariableComboBox
								.getItemCount() - 1);
					}
				}
			});

		}
		return newInVariableButton;
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
						outVariableComboBox
								.addItem(dialog.getNewVariableName());
						outVariableComboBox
								.setSelectedIndex(outVariableComboBox
										.getItemCount() - 1);
					}
				}
			});
		}
		return newOutVariableButton;
	}


//	fill partnerLinkComboBox with partner links
	public void defineContentOfPartnerLinkComboBox(){
		partnerLinkComboBox.removeAllItems();
		String[] partnerLinks = modelElementContainer.getPartnerLinkList();
		for(String partnerLink : partnerLinks){
			partnerLinkComboBox.addItem(partnerLink);
		}
	}


	public void defineContentOfOperationComboBox(String pathToWsdlFile, String roleName){
    	ArrayList<Operation> operations;
    	String portTypeName = wsdlFileRepresentation.getPortTypeNameByRoleName(roleName);
		try {
			operationComboBox.removeAllItems();

			wsdlFileRepresentation = wsdl.readDataFromWSDL(pathToWsdlFile);
			operations = wsdlFileRepresentation.getPortType(portTypeName).getOperations();
			for(Operation operation : operations){
				setOperation(operation.getOperationName());
			}

		} catch (NoPortTypeFoundException e1) {
			// This exception won't be raised because there will definitely be a port type.
		} catch (Exception e) {
			showErrorPopup(Messages.getString("Transition.Properties.BPEL.ErrorWhileReadingWsdlFileTitle"),
					   	   Messages.getString("Transition.Properties.BPEL.ErrorWhileReadingOperation"));
		}
	}


	public void defineVariablesForInputOutputComboBoxes(String pathToWsdlFile){
		ArrayList<Message> messages;
		try {
			System.out.println("inVariableComboBox: " + inVariableComboBox.getComponentCount());
			for(int i=inVariableComboBox.getComponentCount()-1; i>0; i--){
				inVariableComboBox.remove(i);
				outVariableComboBox.remove(i);
			}

			wsdlFileRepresentation = wsdl.readDataFromWSDL(pathToWsdlFile);
			messages = wsdlFileRepresentation.getMessages();
			for(Message message : messages){
				setInVariable("var_" + message.getMessageName());
				setOutVariable("var_" + message.getMessageName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			showErrorPopup(Messages.getString("Transition.Properties.BPEL.ErrorWhileReadingWsdlFileTitle"),
						   Messages.getString("Transition.Properties.BPEL.ErrorWhileReadingVariables"));
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
	
	public boolean allFieldsFilled(){
		if (partnerLinkComboBox.getSelectedItem() == null || operationComboBox.getSelectedItem() == null || inVariableComboBox.getSelectedItem() == null || outVariableComboBox.getSelectedItem() == null){
			return false;
		}
		else
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
		// TODO Auto-generated method stub
		this.repaint();
	}

	@Override
	public void saveInfomation() {
		this.transition
				.setBaseActivity(new Invoke(this.transition.getNameValue(),
						this.getPartnerLink(), this.getOperation(), this
								.getPortType(), this.getInVariable(), this
								.getOutVariable()));
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "invoke";
	}

	@Override
	public void showPanel(JPanel panel, GridBagConstraints c) {
		panel.add(this,c);
	}
}
