package org.woped.bpel.gui.transitionproperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Esther Landes
 * 
 * This is a panel in the transition properties, which enables the user to maintain data for an "assign" BPEL activity.
 *
 * Created on 15.01.2008
 */

public class BPELassignPanel extends JPanel{
	
	public BPELassignPanel(){
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
		add(new JComboBox(), c);
		
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		add(new JButton("new"), c);

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
		add(new JComboBox(), c);
		
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(5, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		add(new JButton("new"), c);
	}
}
