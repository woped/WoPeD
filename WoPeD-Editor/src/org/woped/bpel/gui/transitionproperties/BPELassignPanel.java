package org.woped.bpel.gui.transitionproperties;

import java.awt.Component;
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

import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.TransitionPropertyEditor;

/**
 * @author Esther Landes
 * 
 * This is a panel in the transition properties, which enables the user to maintain data for an "assign" BPEL activity.
 *
 * Created on 15.01.2008
 */

public class BPELassignPanel extends BPELadditionalPanel{
	
	JComboBox fromVariableComboBox = null;
	JButton newFromVariableButton = null;
	JComboBox toVariableComboBox = null;
	JButton newToVariableButton = null;
	
	JDialog dialogPartner = null;
	
	public BPELassignPanel(TransitionPropertyEditor t_editor, TransitionModel transition){
		
		super(t_editor,transition);
		
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
	
	
	private JComboBox getFromVariableComboBox(){
		if (fromVariableComboBox == null) {
			fromVariableComboBox = new JComboBox();
		}
		return fromVariableComboBox;
	}
	
	private JButton getNewFromVariableButton(){
		if (newFromVariableButton == null) {
			newFromVariableButton = new JButton("new");		
			
			newFromVariableButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					showNewVariableDialog();
//					dialogPartner = showNewVariableDialog(dialogPartner);
//					dialogPartner.setVisible(true);
				}
			});
		}
		return newFromVariableButton;
	}
	
	private JComboBox getToVariableComboBox(){
		if (toVariableComboBox == null) {
			toVariableComboBox = new JComboBox();
		}
		return toVariableComboBox;
	}
	
	private JButton getNewToVariableButton(){
		if (newToVariableButton == null) {
			newToVariableButton = new JButton("new");
			
			newToVariableButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					showNewVariableDialog();
				}
			});
			getFromVariableContent();
		}
		return newToVariableButton;
	}
	
	
	
	//	***************** content getter methods  **************************
	
	public String getFromVariableContent(){
		if (fromVariableComboBox.getSelectedItem() == null)
			return null;
		return fromVariableComboBox.getSelectedItem().toString();
	}
	
	public String getToVariableContent(){
		if (toVariableComboBox.getSelectedItem() == null)
			return null;
		return toVariableComboBox.getSelectedItem().toString();
	}
	
}
