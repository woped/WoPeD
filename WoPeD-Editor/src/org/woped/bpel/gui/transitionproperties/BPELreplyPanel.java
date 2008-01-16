package org.woped.bpel.gui.transitionproperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.TransitionPropertyEditor;

/**
 * @author Esther Landes
 * 
 * This is a panel in the transition properties, which enables the user to maintain data for a "reply" BPEL activity.
 *
 * Created on 14.01.2008
 */

public class BPELreplyPanel extends BPELadditionalPanel{

	JComboBox partnerLinkComboBox = null;
	JButton newPartnerLinkButton = null;
	JComboBox operationComboBox = null;
	JComboBox variableComboBox = null;
	JButton newVariableButton = null;
	
	
	public BPELreplyPanel(TransitionPropertyEditor t_editor, TransitionModel transition){
		
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
		add(new JLabel("Variable:"), c);

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(getVariableComboBox(), c);
		
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		add(getNewVariableButton(), c);
	}
	
	
	private JComboBox getPartnerLinkComboBox(){
		if (partnerLinkComboBox == null) {
			partnerLinkComboBox = new JComboBox();
		}
		return partnerLinkComboBox;
	}
	
	private JButton getNewPartnerLinkButton(){
		if (newPartnerLinkButton == null) {
			newPartnerLinkButton = new JButton("new");		
			
			newPartnerLinkButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					showNewPartnerLinkDialog();
				}
			});
		}
		return newPartnerLinkButton;
	}
	
	private JComboBox getOperationComboBox(){
		if (operationComboBox == null) {
			operationComboBox = new JComboBox();
		}
		return operationComboBox;
	}
	
	private JComboBox getVariableComboBox(){
		if (variableComboBox == null) {
			variableComboBox = new JComboBox();
		}
		return variableComboBox;
	}
	
	private JButton getNewVariableButton(){
		if (newVariableButton == null) {
			newVariableButton = new JButton("new");
			
			newVariableButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					showNewVariableDialog();
				}
			});
			
		}
		return newVariableButton;
	}
	
	
	//	***************** content getter methods  **************************
	
	public String getPartnerLink(){
		if (partnerLinkComboBox.getSelectedItem() == null)
			return null;
		return partnerLinkComboBox.getSelectedItem().toString();
	}
	
	public String getOperation(){
		if (operationComboBox.getSelectedItem() == null)
			return null;
		return operationComboBox.getSelectedItem().toString();
	}
	
	public String getVariable(){
		if (variableComboBox.getSelectedItem() == null)
			return null;
		return variableComboBox.getSelectedItem().toString();
	}
	
	
	//	***************** content setter methods  **************************
	
	public void setPartnerLink(String partnerLink) {
		partnerLinkComboBox.addItem(partnerLink);
	}

	public void setOperation(String operation) {
		operationComboBox.addItem(operation);
	}

	public void setVariable(String variable) {
		variableComboBox.addItem(variable);
	}
}
