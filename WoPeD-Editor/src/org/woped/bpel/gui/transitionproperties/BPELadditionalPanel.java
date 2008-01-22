package org.woped.bpel.gui.transitionproperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.stream.XMLStreamException;

import org.woped.bpel.wsdl.Wsdl;
import org.woped.bpel.wsdl.wsdlFileRepresentation.PartnerLinkType;
import org.woped.bpel.wsdl.wsdlFileRepresentation.Role;
import org.woped.bpel.wsdl.wsdlFileRepresentation.WsdlFileRepresentation;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.translations.Messages;

public abstract class BPELadditionalPanel extends JPanel{

	TransitionPropertyEditor t_editor;
	JDialog dialogVariable = null;
	JDialog dialogPartner = null;
	JPanel dialogButtons = null;

	JComboBox partnerLinkTypeComboBox = null;
	JComboBox partnerRoleComboBox = null;
	JComboBox myRoleComboBox = null;
	
	static final String NEW= Messages.getString("Transition.Properties.BPEL.Buttons.New");
	
;	ArrayList<PartnerLinkType> partnerLinkTypes;
	ArrayList<Role> roles;

	TransitionModel transition = null;

	WsdlFileRepresentation wsdlFileRepresentation;


	public BPELadditionalPanel(TransitionPropertyEditor t_editor, TransitionModel transition){
		this.t_editor = t_editor;
		this.transition = transition;
	}


	//	************** display dialog box "New Partner Link" *****************

	protected void showNewPartnerLinkDialog(){
		Wsdl wsdl = new Wsdl();
		try {
			wsdlFileRepresentation = wsdl.readDataFromWSDL("http://www.esther-landes.de/wsdlFiles/example.wsdl");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dialogPartner = new JDialog(t_editor, true);
		dialogPartner.setVisible(false);
		dialogPartner.setTitle(Messages.getString("Transition.Properties.BPEL.NewPartnerLink"));
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
		dialogPartner.add(getPartnerLinkTypeComboBox(), c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogPartner.add(new JLabel("Partner Role:"), c);

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogPartner.add(getPartnerRoleComboBox(), c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogPartner.add(new JLabel("My Role:"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogPartner.add(getMyRoleComboBox(), c);

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
		dialogVariable.setTitle(Messages.getString("Transition.Properties.BPEL.NewVariable"));
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


	private JComboBox getPartnerLinkTypeComboBox(){
		if (partnerLinkTypeComboBox == null) {
			partnerLinkTypeComboBox = new JComboBox();

			partnerLinkTypes = wsdlFileRepresentation.getPartnerLinkTypes();

			for(PartnerLinkType partnerLinkType : partnerLinkTypes){
				partnerLinkTypeComboBox.addItem(partnerLinkType.getName());
			}
		}
		return partnerLinkTypeComboBox;
	}


	private JComboBox getPartnerRoleComboBox(){
		if (partnerRoleComboBox == null) {
			partnerRoleComboBox = new JComboBox();

			// If there aren't any partner link types --> there can't be any subelements
			System.out.println(partnerLinkTypes.size());
			if (partnerLinkTypes.size() != 0){
				roles = partnerLinkTypes.get(partnerLinkTypeComboBox.getSelectedIndex()).getRoles();
				for(Role role : roles){
					partnerRoleComboBox.addItem(role.getRoleName());
				}
			}
		}
		return partnerRoleComboBox;
	}


	private JComboBox getMyRoleComboBox(){
		if (myRoleComboBox == null) {
			myRoleComboBox = new JComboBox();
		}
		return myRoleComboBox;
	}


}