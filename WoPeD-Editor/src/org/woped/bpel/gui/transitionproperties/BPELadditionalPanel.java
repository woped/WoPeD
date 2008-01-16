package org.woped.bpel.gui.transitionproperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.TransitionPropertyEditor;

public abstract class BPELadditionalPanel extends JPanel{

	TransitionPropertyEditor t_editor;
	JDialog dialogVariable = null;
	JDialog dialogPartner = null;
	JPanel dialogButtons = null;

	TransitionModel transition = null;
	
	public BPELadditionalPanel(TransitionPropertyEditor t_editor, TransitionModel transition){
		this.t_editor = t_editor;
		this.transition = transition;
	}

	
	//	************** display dialog box "New Partner Link" *****************
	
	protected void showNewPartnerLinkDialog(){
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
		dialogPartner.add(new JTextField(), c);
		
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
		
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		dialogPartner.add(addDialogButtons(), c);
		
		dialogPartner.setVisible(true);
	}
	
	
//	************** display dialog box "New Variable" ************************
	
	protected void showNewVariableDialog(){
		dialogVariable = new JDialog(t_editor, true);
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
		dialogVariable.add(new JTextField(), c);
		
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
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogVariable.add(addDialogButtons(), c);
		
		dialogVariable.setVisible(true);
	}
	
	
	//	************** display buttons in dialog boxes *****************
	
	public JPanel addDialogButtons(){
		if (dialogButtons == null){
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
			dialogButtons.add(new JButton("OK"), c);
			
			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 5, 0, 0);
			dialogButtons.add(new JButton("Abbruch"), c);
		}
		return dialogButtons;
	}
	
	
	

	
	
	//	***************** content getter methods  **************************
	public String getTransitionName(){
		return this.transition.getNameValue();
	}
	
	
}
