package org.woped.bpel.gui.transitionproperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.xml.stream.XMLStreamException;

import org.woped.bpel.wsdl.Wsdl;
import org.woped.bpel.wsdl.wsdlFileRepresentation.PartnerLinkType;
import org.woped.bpel.wsdl.wsdlFileRepresentation.Role;
import org.woped.bpel.wsdl.wsdlFileRepresentation.WsdlFileRepresentation;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.translations.Messages;

/**
 * @author Esther Landes / Kristian Kindler
 *
 * This is the basic class for the different BPEL activity panels.
 * It contains methods and data that is used in the activity panels' dialogs.
 *
 * Created on 16.01.2008
 */

public abstract class BPELadditionalPanel extends JPanel{

	TransitionPropertyEditor t_editor;
	JDialog dialogVariable = null;
	JDialog dialogPartner = null;
	JPanel dialogButtons = null;

	JTextField partnerLinkNameTextField = null;
	JTextField wsdlFileTextField = null;
	JButton searchLocalWSDLButton = null;
	JComboBox partnerLinkTypeComboBox = null;
	JComboBox partnerRoleComboBox = null;
	JComboBox myRoleComboBox = null;
	JButton okButton = null;
	JButton cancelButton = null;


	static final String NEW = Messages.getString("Transition.Properties.BPEL.Buttons.New");

	ArrayList<PartnerLinkType> partnerLinkTypes;
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
		dialogPartner.setSize(450,250);
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
		dialogPartner.add(new JLabel("Name:"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogPartner.add(getPartnerLinkNameTextField(), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogPartner.add(new JLabel("WSDL:"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogPartner.add(getWSDLFileTextField(), c);

		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		dialogPartner.add(getLocalWSDLButton(), c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		dialogPartner.add(new JLabel("Partner Link Type:"), c);

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogPartner.add(getPartnerLinkTypeComboBox(), c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogPartner.add(new JLabel("Partner Role:"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogPartner.add(getPartnerRoleComboBox(), c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogPartner.add(new JLabel("My Role:"), c);

		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogPartner.add(getMyRoleComboBox(), c);

		c.gridx = 1;
		c.gridy = 5;
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
		dialogVariable.add(new JLabel(Messages.getString("Transition.Properties.BPEL.NewVariable.Name") + ":"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 5);
		dialogVariable.add(new JTextField(), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialogVariable.add(new JLabel(Messages.getString("Transition.Properties.BPEL.NewVariable.Type") + ":"), c);

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
			dialogButtons.add(getOKButton(), c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 5, 0, 0);
			dialogButtons.add(getCancelButton(), c);
		}
		return dialogButtons;
	}




	//	************** reading WSDL data *****************

	private JTextField getPartnerLinkNameTextField(){
		if(partnerLinkNameTextField == null){
			partnerLinkNameTextField = new JTextField();
		}
		return partnerLinkNameTextField;
	}

	private JTextField getWSDLFileTextField(){
		if(wsdlFileTextField == null){
			wsdlFileTextField = new JTextField();
		}
		return wsdlFileTextField;
	}

	private JButton getLocalWSDLButton(){
		if (searchLocalWSDLButton == null){
			searchLocalWSDLButton = new JButton();
			searchLocalWSDLButton.setIcon(Messages.getImageIcon("ToolBar.Open"));
			searchLocalWSDLButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JFileChooser chooser = new JFileChooser();
					chooser.addChoosableFileFilter(new FileFilter() {
					    public boolean accept(File f) {
					      if (f.isDirectory()){
					    	  return true;
					      }
					      return f.getName().toLowerCase().endsWith(".wsdl");
					    }
					    public String getDescription () {
					    	return "Web Service Definition Language (*.wsdl)";
					    }
					});
					chooser.setMultiSelectionEnabled(false);
					chooser.setAcceptAllFileFilterUsed(false);
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
						wsdlFileTextField.setText(""+chooser.getSelectedFile());
					}
				}
			});
		}
		return searchLocalWSDLButton;
	}


	private JComboBox getPartnerLinkTypeComboBox(){
		if (partnerLinkTypeComboBox == null) {
			partnerLinkTypeComboBox = new JComboBox();

			partnerLinkTypes = wsdlFileRepresentation.getPartnerLinkTypes();

			for(PartnerLinkType partnerLinkType : partnerLinkTypes){
				partnerLinkTypeComboBox.addItem(partnerLinkType.getName());
			}

		    partnerLinkTypeComboBox.addItemListener(new ItemListener() {
		        public void itemStateChanged(ItemEvent e) {
		            partnerRoleComboBox.removeAllItems();
		            myRoleComboBox.removeAllItems();
		            if ( partnerLinkTypes.size() != 0){
		                roles = partnerLinkTypes.get(partnerLinkTypeComboBox.getSelectedIndex()).getRoles();
		                for(Role role : roles){
		                     partnerRoleComboBox.addItem(role.getRoleName());
		                     myRoleComboBox.addItem(role.getRoleName());
		                }
		            }

		        }
		    });
		}
		return partnerLinkTypeComboBox;
	}




	private JComboBox getPartnerRoleComboBox(){
		if (partnerRoleComboBox == null) {
			partnerRoleComboBox = new JComboBox();

			// If there aren't any partner link types --> there can't be any subelements
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

			// If there aren't any partner link types --> there can't be any subelements
			if (partnerLinkTypes.size() != 0){
				roles = partnerLinkTypes.get(partnerLinkTypeComboBox.getSelectedIndex()).getRoles();
				for(Role role : roles){
					myRoleComboBox.addItem(role.getRoleName());
				}
			}
		}
		return myRoleComboBox;
	}

	private JButton getOKButton(){
		if (okButton == null) {
			okButton = new JButton(Messages.getString("Transition.Properties.BPEL.Buttons.OK"));
			okButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//todo: speichern mithilfe von Alex' Klassen, die auf content getter methoden zugreifen
					dialogPartner.dispose();
				}
			});
		}
		return okButton;
	}

	private JButton getCancelButton(){
		if (cancelButton == null) {
			cancelButton = new JButton(Messages.getString("Transition.Properties.BPEL.Buttons.Cancel"));
			cancelButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					dialogPartner.dispose();
				}
			});
		}
		return cancelButton;
	}



	//	***************** content getter methods  **************************

	public String getTransitionName(){
		return this.transition.getNameValue();
	}


	// folgendes noch mit Alex abzuklären (Esther)

	public String getPartnerLinkName(){
		if (partnerLinkNameTextField.getText() == null)
			return null;
		return partnerLinkNameTextField.getText().toString();
	}

	public String getPartnerLinkType(){
		if (partnerLinkTypeComboBox.getSelectedItem() == null)
			return null;
		return partnerLinkTypeComboBox.getSelectedItem().toString();
	}

	public String getPartnerRole(){
		if (partnerRoleComboBox.getSelectedItem() == null)
			return null;
		return partnerRoleComboBox.getSelectedItem().toString();
	}

	public String getMyRole(){
		if (myRoleComboBox.getSelectedItem() == null)
			return null;
		return myRoleComboBox.getSelectedItem().toString();
	}



}