package org.woped.bpel.gui.transitionproperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.woped.translations.Messages;
import org.woped.editor.controller.*;


/**
 * @author Esther Landes
 * 
 * This is a panel in the transition properties, which enables the user to maintain data for an "invoke" BPEL activity.
 *
 * Created on 08.01.2008
 */

public class BPELinvokePanel extends JPanel{

	JComboBox partnerLinkComboBox = null;
	JButton newPartnerLinkButton = null;
	JComboBox operationComboBox = null;
	JComboBox inVariableComboBox = null;
	JButton newInVariableButton = null;
	JComboBox outVariableComboBox = null;
	JButton newOutVariableButton = null;
	
	TransitionPropertyEditor t_editor = null;
	JDialog dialogPartner = null;
	JDialog dialogVariable = null;
	
	public BPELinvokePanel(TransitionPropertyEditor t_editor){
		
		this.t_editor = t_editor;
		
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
		c.insets = new Insets(5, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Output Variable:"), c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		add(getOutVariableComboBox(), c);
		
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		add(getNewOutVariableButton(), c);
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
					dialogPartner = new JDialog(t_editor, true);
					dialogPartner.setVisible(false);
					dialogPartner.setTitle("Create Partner Link");
					dialogPartner.setSize(400,200);
					dialogPartner.setLocation(150,150);
					dialogPartner.setLayout(new GridBagLayout());
					GridBagConstraints c = new GridBagConstraints();
					
					c.fill = GridBagConstraints.HORIZONTAL;
					c.anchor = GridBagConstraints.WEST;
					c.weightx = 1;
					c.weighty = 1;

					c.gridx = 0;
					c.gridy = 0;
					c.gridwidth = 1;
					c.insets = new Insets(0, 5, 0, 0);
					dialogPartner.add(new JLabel("WSDL file:"), c);
					
					c.gridx = 1;
					c.gridy = 0;
					c.gridwidth = 1;
					c.insets = new Insets(0, 5, 0, 0);
					dialogPartner.add(new JComboBox(), c);
					
					c.gridx = 2;
					c.gridy = 0;
					c.gridwidth = 1;
					c.insets = new Insets(0, 5, 0, 0);
					c.fill = GridBagConstraints.NONE;
					dialogPartner.add(new JButton("Browse"), c);
					//evt noch zweiter Button für Internet/WWW-Adresse eingeben

					c.gridx = 0;
					c.gridy = 1;
					c.gridwidth = 1;
					c.insets = new Insets(0, 5, 0, 0);
					c.fill = GridBagConstraints.HORIZONTAL;
					dialogPartner.add(new JLabel("Partner Link Type:"), c);
					
					c.gridx = 1;
					c.gridy = 1;
					c.gridwidth = 1;
					c.insets = new Insets(0, 5, 0, 0);
					dialogPartner.add(new JComboBox(), c);
					
					c.gridx = 0;
					c.gridy = 2;
					c.gridwidth = 1;
					c.insets = new Insets(0, 5, 0, 0);
					dialogPartner.add(new JLabel("Partner Role:"), c);
					
					c.gridx = 1;
					c.gridy = 2;
					c.gridwidth = 1;
					c.insets = new Insets(0, 5, 0, 0);
					dialogPartner.add(new JComboBox(), c);
					
					c.gridx = 0;
					c.gridy = 3;
					c.gridwidth = 1;
					c.insets = new Insets(0, 5, 0, 0);
					dialogPartner.add(new JLabel("My Role:"), c);
					
					c.gridx = 1;
					c.gridy = 3;
					c.gridwidth = 1;
					c.insets = new Insets(0, 5, 0, 0);
					dialogPartner.add(new JComboBox(), c);
					
					c.gridx = 0;
					c.gridy = 4;
					c.gridwidth = 1;
					c.insets = new Insets(0, 5, 0, 0);
					c.fill = GridBagConstraints.NONE;
					dialogPartner.add(new JButton("OK"), c);
					
					dialogPartner.setVisible(true);
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
	
	private JComboBox getInVariableComboBox(){
		if (inVariableComboBox == null) {
			inVariableComboBox = new JComboBox();
		}
		return inVariableComboBox;
	}
	
	private JButton getNewInVariableButton(){
		if (newInVariableButton == null) {
			newInVariableButton = new JButton("new");
			
			newInVariableButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					showNewVariableDialog();
				}
			});
			
		}
		return newInVariableButton;
	}
	
	private JComboBox getOutVariableComboBox(){
		if (outVariableComboBox == null) {
			outVariableComboBox = new JComboBox();
		}
		return outVariableComboBox;
	}
	
	private JButton getNewOutVariableButton(){
		if (newOutVariableButton == null) {
			newOutVariableButton = new JButton("new");
			
			newOutVariableButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					showNewVariableDialog();
				}
			});
		}
		return newOutVariableButton;
	}
	
	private void showNewVariableDialog(){
		dialogVariable = new JDialog(t_editor, true); //Dialog
		dialogVariable.setVisible(false);
		dialogVariable.setTitle("Create Variable");
		dialogVariable.setSize(400,150);
		dialogVariable.setLocation(150,150);
		dialogVariable.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogVariable.add(new JLabel("Name:"), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 5);
		dialogVariable.add(new JComboBox(), c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogVariable.add(new JLabel("Type:"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 5);
		dialogVariable.add(new JComboBox(), c);
		
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogVariable.add(new JButton("OK"), c);
		
		dialogVariable.setVisible(true);
	}
	
}
