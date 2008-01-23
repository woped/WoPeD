package org.woped.bpel.gui.transitionproperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

public class BPELadditionalPanel extends JPanel{

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
	JButton okPartnerButton = null;
	JButton cancelPartnerButton = null;
	JButton okVariableButton = null;
	JButton cancelVariableButton = null;


	static final String NEW = Messages.getString("Transition.Properties.BPEL.Buttons.New");

	ArrayList<PartnerLinkType> partnerLinkTypes = null;;
	ArrayList<Role> roles = null;;

	TransitionModel transition = null;

	Wsdl wsdl = null;
	WsdlFileRepresentation wsdlFileRepresentation = null;


	public BPELadditionalPanel(TransitionPropertyEditor t_editor, TransitionModel transition){
		this.t_editor = t_editor;
		this.transition = transition;
	}


	//	************** display dialog box "New Partner Link" *****************

	protected void showNewPartnerLinkDialog(){
		wsdl = new Wsdl();
/*		try {
			wsdlFileRepresentation = wsdl.readDataFromWSDL("http://www.esther-landes.de/wsdlFiles/example.wsdl");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			partnerLinkTypeComboBox.removeAllItems();
			partnerRoleComboBox.removeAllItems();
			myRoleComboBox.removeAllItems();
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

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
		dialogPartner.add(addPartnerDialogButtons(), c);

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
		dialogVariable.add(addVariableDialogButtons(), c);

		dialogVariable.setVisible(true);
	}


	//	************** display buttons in dialog boxes *****************

	public JPanel addPartnerDialogButtons(){
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
			dialogButtons.add(getPartnerOKButton(), c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 5, 0, 0);
			dialogButtons.add(getPartnerCancelButton(), c);
		}
		return dialogButtons;
	}

	public JPanel addVariableDialogButtons(){
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
			dialogButtons.add(getVariableOKButton(), c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 5, 0, 0);
			dialogButtons.add(getVariableCancelButton(), c);
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
			wsdlFileTextField.addKeyListener(new KeyListener() {
				// User has entered the path to a wsdl file and has pressed "Enter"
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER){
						try {
							wsdlFileRepresentation = wsdl.readDataFromWSDL(wsdlFileTextField.getText());
							fillAllComboBoxesWithData();
						} catch (MalformedURLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (FileNotFoundException e1) {
							partnerLinkTypeComboBox.removeAllItems();
							partnerRoleComboBox.removeAllItems();
							myRoleComboBox.removeAllItems();
							// TODO Auto-generated catch block
							//e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (XMLStreamException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

				public void keyTyped(KeyEvent e)   { /* nothing happens */ }
				public void keyPressed(KeyEvent e) { /* nothing happens */ }

			});
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
						try {
							wsdlFileRepresentation = wsdl.readDataFromWSDL(wsdlFileTextField.getText());
							fillAllComboBoxesWithData();
						} catch (MalformedURLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (FileNotFoundException e1) {
							partnerLinkTypeComboBox.removeAllItems();
							partnerRoleComboBox.removeAllItems();
							myRoleComboBox.removeAllItems();
							// TODO Auto-generated catch block
							//e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (XMLStreamException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
		}
		return searchLocalWSDLButton;
	}


	private JComboBox getPartnerLinkTypeComboBox(){
		if (partnerLinkTypeComboBox == null) {
			partnerLinkTypeComboBox = new JComboBox();

			partnerLinkTypeComboBox.addItemListener(new ItemListener() {
		        public void itemStateChanged(ItemEvent e) {
		        	if (partnerLinkTypeComboBox.getSelectedIndex() != -1){
			            partnerRoleComboBoxAddItems();
			            myRoleComboBoxAddItems();
		        	}
		        }
		    });

			// if there isn't a wsdl file path in the input field there aren't any subelements
			if (wsdlFileRepresentation != null){
				partnerLinkTypeComboBoxAddItems();
			}

		}
		return partnerLinkTypeComboBox;
	}


	private JComboBox getPartnerRoleComboBox(){
		if (partnerRoleComboBox == null) {
			partnerRoleComboBox = new JComboBox();

			// if there isn't a wsdl file path in the input field there aren't any subelements
			if (wsdlFileRepresentation != null){
				partnerRoleComboBoxAddItems();
			}

		}
		return partnerRoleComboBox;
	}


	private JComboBox getMyRoleComboBox(){
		if (myRoleComboBox == null) {
			myRoleComboBox = new JComboBox();

			// if there isn't a wsdl file path in the input field there aren't any subelements
			if (wsdlFileRepresentation != null){
				myRoleComboBoxAddItems();
			}

		}
		return myRoleComboBox;
	}


	private JButton getPartnerOKButton(){
		if (okPartnerButton == null) {
			okPartnerButton = new JButton(Messages.getString("Transition.Properties.BPEL.Buttons.OK"));
			okPartnerButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					// TODO speichern mithilfe von Alex' Klassen, die auf content getter methoden zugreifen
//					t_editor.
					dialogPartner.dispose();
				}
			});
		}
		return okPartnerButton;
	}

	private JButton getPartnerCancelButton(){
		cancelPartnerButton = new JButton(Messages.getString("Transition.Properties.BPEL.Buttons.Cancel"));
		cancelPartnerButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialogPartner.dispose();
			}
		});
		return cancelPartnerButton;
	}

	private JButton getVariableOKButton(){
		okVariableButton = new JButton(Messages.getString("Transition.Properties.BPEL.Buttons.OK"));
		okVariableButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO speichern mithilfe von Alex' Klassen, die auf content getter methoden zugreifen
				dialogVariable.dispose();
			}
			});
		return okVariableButton;
	}

	private JButton getVariableCancelButton(){
		cancelVariableButton = new JButton(Messages.getString("Transition.Properties.BPEL.Buttons.Cancel"));
		cancelVariableButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialogVariable.dispose();
			}
		});
		return cancelVariableButton;
	}


	//	***************** methods to fill comboBoxes with data from the wsdl file **************************

	private void fillAllComboBoxesWithData(){

		// add new items
		partnerLinkTypeComboBoxAddItems();
		partnerRoleComboBoxAddItems();
		myRoleComboBoxAddItems();
	}


	private void partnerLinkTypeComboBoxAddItems(){
		partnerLinkTypeComboBox.removeAllItems();

		partnerLinkTypes = wsdlFileRepresentation.getPartnerLinkTypes();

		if ( partnerLinkTypes.size() != 0){

			for(PartnerLinkType partnerLinkType : partnerLinkTypes){
				partnerLinkTypeComboBox.addItem(partnerLinkType.getName());
			}

	    }
	}


	private void partnerRoleComboBoxAddItems(){
		partnerRoleComboBox.removeAllItems();
		// If there aren't any partner link types --> there can't be any subelements
		if (partnerLinkTypes.size() != 0){
			roles = partnerLinkTypes.get(partnerLinkTypeComboBox.getSelectedIndex()).getRoles();
			for(Role role : roles){
				partnerRoleComboBox.addItem(role.getRoleName());
			}
		}
	}


	private void myRoleComboBoxAddItems(){
		myRoleComboBox.removeAllItems();
		// If there aren't any partner link types --> there can't be any subelements
		if (partnerLinkTypes.size() != 0){
			roles = partnerLinkTypes.get(partnerLinkTypeComboBox.getSelectedIndex()).getRoles();
			for(Role role : roles){
				myRoleComboBox.addItem(role.getRoleName());
			}
		}
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