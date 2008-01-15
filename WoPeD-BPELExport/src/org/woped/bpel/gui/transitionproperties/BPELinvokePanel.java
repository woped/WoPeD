package org.woped.bpel.gui.transitionproperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.woped.translations.Messages;

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
	
	public BPELinvokePanel(){
		
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
		add(getNewPartnerLinkButton(), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
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
		add(getNewInVariableButton(), c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
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
		}
		return newOutVariableButton;
	}
	
}
