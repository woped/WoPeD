package org.woped.editor.controller.bpel;

import java.awt.Dimension;
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
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.xml.stream.XMLStreamException;

import org.woped.core.model.bpel.BpelVariable;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.editor.controller.wsdl.Wsdl;
import org.woped.editor.controller.wsdl.Message;
import org.woped.editor.controller.wsdl.PartnerLinkType;
import org.woped.editor.controller.wsdl.Role;
import org.woped.editor.controller.wsdl.WsdlFileRepresentation;
import org.woped.gui.translations.Messages;

/**
 * @author Esther Landes / Kristian Kindler / Alexander Ro�wog
 *
 * This is the basic class for the different BPEL activity panels. It contains
 * methods and data that is used in the activity panels' dialogs.
 *
 * Created on 16.01.2008
 */

@SuppressWarnings("serial")
public abstract class BPELadditionalPanel extends JPanel {

	TransitionPropertyEditor t_editor 	= null;
	ModelElementContainer modelElementContainer = null;

	JDialog dialog 						= null;
	JPanel dialogButtons 				= null;

	JDialog errorPopup 					= null;

	JComboBox variableTypesComboBox 	= null;

	JTextField partnerLinkNameTextField = null;
	JTextField wsdlFileTextField 		= null;
	JTextField VariableName				= null;

	JComboBox partnerLinkTypeComboBox 	= null;
	JComboBox partnerRoleComboBox 		= null;
	JComboBox myRoleComboBox 			= null;
	JButton okPartnerButton 			= null;
	JButton cancelPartnerButton 		= null;
	JButton okVariableButton 			= null;
	JButton cancelVariableButton 		= null;
	JButton searchLocalWSDLButton 		= null;
	JButton readWSDLButton				= null;
	JButton searchByUDDIButton			= null;

	BPELinvokePanel bpelInvokePanel   	= null;
	BPELreceivePanel bpelReceivePanel 	= null;
	BPELreplyPanel bpelReplyPanel     	= null;

	Dimension dimension = new Dimension(50, 22);

	static final String NEW = Messages
			.getString("Transition.Properties.BPEL.Buttons.New");

	ArrayList<PartnerLinkType> partnerLinkTypes = null;;
	ArrayList<Role> roles = null;;

	TransitionModel transition = null;

	Wsdl wsdl = null;
	WsdlFileRepresentation wsdlFileRepresentation = null;

	public BPELadditionalPanel(TransitionPropertyEditor t_editor,
			TransitionModel transition) {
		this.t_editor = t_editor;
		this.transition = transition;
		this.modelElementContainer = t_editor.getEditor().getModelProcessor().getElementContainer();
		wsdl = new Wsdl();
	}

	public abstract String getName();

	public String toString()
	{
		return getName();
	}

	public void setLinkToBPELinvokePanel(BPELinvokePanel bpelInvokePanel){
		this.bpelInvokePanel = bpelInvokePanel;
	}

	public void setLinkToBPELreceivePanel(BPELreceivePanel bpelReceivePanel){
		this.bpelReceivePanel = bpelReceivePanel;
	}

	public void setLinkToBPELreplyPanel(BPELreplyPanel bpelReplyPanel){
		this.bpelReplyPanel = bpelReplyPanel;
	}

	// ************** display dialog box "New Partner Link" *****************

	protected void showNewPartnerLinkDialog() {

        // clear all input fields and combo boxes before we start (because of old data)
        	getPartnerLinkNameTextField().setText("");
        	getWSDLFileTextField().setText("");
        	getPartnerLinkTypeComboBox().removeAllItems();
        	getPartnerRoleComboBox().removeAllItems();
        	getMyRoleComboBox().removeAllItems();

		// here we go ...
		dialog = new JDialog(t_editor, true);
		dialog.setVisible(false);
		dialog.setTitle(Messages.getString("Transition.Properties.BPEL.NewPartnerLink"));
		dialog.setSize(450, 250);
		dialog.setLocation(150, 150);
		dialog.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(new JLabel(Messages.getString("Transition.Properties.BPEL.NewVariable.Name") + ":"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(getPartnerLinkNameTextField(), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(new JLabel(Messages.getString("Transition.Properties.BPEL.WSDL") + ":"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(getWSDLFileTextField(), c);

		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		dialog.add(getReadWSDLButton(), c);

		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		dialog.add(getLocalWSDLButton(), c);

		c.gridx = 4;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		dialog.add(getUDDIButton(),c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		dialog.add(new JLabel(Messages.getString("Transition.Properties.BPEL.PartnerLinkType") + ":"), c);

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(getPartnerLinkTypeComboBox(), c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(new JLabel(Messages.getString("Transition.Properties.BPEL.PartnerRole") + ":"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(getPartnerRoleComboBox(), c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(new JLabel(Messages.getString("Transition.Properties.BPEL.MyRole") + ":"), c);

		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(getMyRoleComboBox(), c);

		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.NONE;
		dialog.add(addPartnerDialogButtons(), c);

		dialog.setVisible(true);
	}



	// ************** display dialog box "New Variable" ************************

	protected void showNewVariableDialog() {
		dialog = new JDialog(t_editor, true);
		dialog.setVisible(false);
		dialog.setTitle(Messages
				.getString("Transition.Properties.BPEL.NewVariable"));
		dialog.setSize(400, 150);
		dialog.setLocation(150, 150);
		dialog.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(new JLabel(Messages
				.getString("Transition.Properties.BPEL.NewVariable.Name")
				+ ":"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 5);
		if (this.VariableName == null)
			this.VariableName = new JTextField("");
		dialog.add(this.VariableName, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(new JLabel(Messages
				.getString("Transition.Properties.BPEL.NewVariable.Type")
				+ ":"), c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 5);
		dialog.add(getVariableTypesComboBox(), c);

		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(addVariableDialogButtons(), c);

		dialog.setVisible(true);
	}

	// ************** display buttons in dialog boxes *****************

	public JPanel addPartnerDialogButtons() {
		if (dialogButtons == null) {
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

	public JPanel addVariableDialogButtons() {
		if (dialogButtons == null) {
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

	private JComboBox getVariableTypesComboBox() {
		if (variableTypesComboBox == null) {
			variableTypesComboBox = new JComboBox();
			String[] variables = BpelVariable.getTypes();
			for (int i = 0; i < variables.length; i++) {
				variableTypesComboBox.addItem(variables[i]);
			}
		}
		return variableTypesComboBox;
	}

	// ************** reading WSDL data *****************

	private JTextField getPartnerLinkNameTextField() {
		if (partnerLinkNameTextField == null) {
			partnerLinkNameTextField = new JTextField();
		}
		return partnerLinkNameTextField;
	}

	private JTextField getWSDLFileTextField() {
		if (wsdlFileTextField == null) {
			wsdlFileTextField = new JTextField();
			wsdlFileTextField.addKeyListener(new KeyListener() {
				// User has entered the path to a wsdl file and has pressed
				// "Enter"
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						tryToGetDataFromWsdl();
					}
				}

				public void keyTyped(KeyEvent e) { /* nothing happens */
				}

				public void keyPressed(KeyEvent e) { /* nothing happens */
				}

			});
		}
		return wsdlFileTextField;
	}

	private JButton getLocalWSDLButton() {
		if (searchLocalWSDLButton == null) {
			searchLocalWSDLButton = new JButton();
			searchLocalWSDLButton
					.setIcon(Messages.getImageIcon("ToolBar.Open"));
			searchLocalWSDLButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser chooser = new JFileChooser();
					chooser.addChoosableFileFilter(new FileFilter() {
						public boolean accept(File f) {
							if (f.isDirectory()) {
								return true;
							}
							return f.getName().toLowerCase().endsWith(".wsdl");
						}

						public String getDescription() {
							return "Web Service Definition Language (*.wsdl)";
						}
					});
					chooser.setMultiSelectionEnabled(false);
					chooser.setAcceptAllFileFilterUsed(false);
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						wsdlFileTextField.setText(""
								+ chooser.getSelectedFile());
						tryToGetDataFromWsdl();
					}
				}
			});
		}
		return searchLocalWSDLButton;
	}


	private JButton getReadWSDLButton() {
		if (readWSDLButton == null) {
			readWSDLButton = new JButton();
			readWSDLButton.setIcon(Messages.getImageIcon("Button.Ok"));
			readWSDLButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tryToGetDataFromWsdl();
				}
			});
		}
		return readWSDLButton;
	}


	private JButton getUDDIButton()
	{
		if (searchByUDDIButton == null)
		{
			searchByUDDIButton = new JButton();
			searchByUDDIButton.setText("UDDI");
			searchByUDDIButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					new UDDIDialog(t_editor, wsdlFileTextField, modelElementContainer);
				}
			});
		}
		return searchByUDDIButton;
	}

	private JComboBox getPartnerLinkTypeComboBox() {
		if (partnerLinkTypeComboBox == null) {
			partnerLinkTypeComboBox = new JComboBox();

			partnerLinkTypeComboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (partnerLinkTypeComboBox.getSelectedIndex() != -1) {
						partnerRoleComboBoxAddItems();
						myRoleComboBoxAddItems();
					}
				}
			});

			// if there isn't a wsdl file path in the input field there aren't
			// any subelements
			if (wsdlFileRepresentation != null) {
				partnerLinkTypeComboBoxAddItems();
			}
		}
		return partnerLinkTypeComboBox;
	}


	private JComboBox getPartnerRoleComboBox() {
		if (partnerRoleComboBox == null) {
			partnerRoleComboBox = new JComboBox();

			// if there isn't a wsdl file path in the input field there aren't
			// any subelements
			if (wsdlFileRepresentation != null) {
				partnerRoleComboBoxAddItems();
			}

		}
		return partnerRoleComboBox;
	}


	private JComboBox getMyRoleComboBox() {
		if (myRoleComboBox == null) {
			myRoleComboBox = new JComboBox();

			// if there isn't a wsdl file path in the input field there aren't
			// any subelements
			if (wsdlFileRepresentation != null) {
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

                        		// Important: Delete whitespaces at the beginning and at the end of strings.
                        		String name      = partnerLinkNameTextField.getText().trim();
                                String wsdlUrl   = wsdlFileTextField.getText();

                                // Check if combo boxes are filled with data
                                if ( (partnerLinkTypeComboBox.getItemCount() != 0) && (partnerRoleComboBox.getItemCount() != 0) ){
                                        String partnerLinkType     = partnerLinkTypeComboBox.getSelectedItem().toString().trim();
                                        String partnerRole         = partnerRoleComboBox.getSelectedItem().toString().trim();
                                        String myRole              = myRoleComboBox.getSelectedItem().toString().trim();

                                        if ( name.equals("") ||
                                                 wsdlUrl.equals("") ||
                                                 ( partnerRole.equals(Messages.getString("Transition.Properties.BPEL.NoRole"))
                                                   &&
                                                   myRole.equals(Messages.getString("Transition.Properties.BPEL.NoRole"))
                                                 )
                                           ) {
                                			showErrorPopup(Messages.getString("Transition.Properties.BPEL.MissingEntries"),
                                						   Messages.getString("Transition.Properties.BPEL.MissingEntries_2"));
                                        }
                                        // check if a partner link with this name has already been created
                                        else if ( modelElementContainer.existPLName(name) ){
                                			showErrorPopup(Messages.getString("Transition.Properties.BPEL.MissingEntries"),
                                						   Messages.getString("Transition.Properties.BPEL.MissingEntries_2"));
                                        }
                                        else {
                                            	String namespace = wsdlFileRepresentation.getNamespace();

                                            	// partner role NOT entered / my role ENTERED
                                                if(partnerRole.equals(Messages.getString("Transition.Properties.BPEL.NoRole")) &&
                                                   !myRole.equals(Messages.getString("Transition.Properties.BPEL.NoRole"))
                                                ){
                                                	modelElementContainer.addPartnerLinkWithoutPartnerRole(
                                                                        name, namespace, partnerLinkType, myRole, wsdlUrl);
                                                }

                                                // partner role ENTERED / my role NOT entered
                                                else if(!partnerRole.equals(Messages.getString("Transition.Properties.BPEL.NoRole")) &&
                                                   myRole.equals(Messages.getString("Transition.Properties.BPEL.NoRole"))
                                                ){
                                                	modelElementContainer.addPartnerLinkWithoutMyRole(
                                                		name, namespace, partnerLinkType, partnerRole, wsdlUrl);
                                                }

                                                // partner role ENTERED / my role ENTERED
                                                else{
                                                	modelElementContainer.addPartnerLink(
    													name, namespace, partnerLinkType, partnerRole, myRole, wsdlUrl);
                                                }

                                                // if the messages of the WSDL file were already saved as variables in the datamodel don't save them for a second time.
                                                if(!modelElementContainer.existWsdlUrl(wsdlUrl)){
                                                	addVariablesToModelElementContainer(wsdlUrl);
                                                }

                                                refresh();
                                                dialog.dispose();
                                        }
                                }
                                else{
                        			showErrorPopup(Messages.getString("Transition.Properties.BPEL.NoPartnerLinks"),
                 						   		   Messages.getString("Transition.Properties.BPEL.NoPartnerLinks_2"));
                                }
                        }
                });
        }
        return okPartnerButton;
	}


	private JButton getPartnerCancelButton() {
		if (cancelPartnerButton == null) {
			cancelPartnerButton = new JButton(Messages
					.getString("Transition.Properties.BPEL.Buttons.Cancel"));
			cancelPartnerButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.dispose();
				}
			});
		}
		return cancelPartnerButton;
	}

	private JButton getVariableOKButton() {
		okVariableButton = new JButton(Messages
				.getString("Transition.Properties.BPEL.Buttons.OK"));
		okVariableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modelElementContainer
						.addVariable(
								VariableName.getText(),
								getVariableTypesComboBox().getSelectedItem()
										.toString());
				dialog.dispose();
			}
		});
		return okVariableButton;
	}

	private JButton getVariableCancelButton() {
		if (cancelVariableButton == null) {
			cancelVariableButton = new JButton(Messages
					.getString("Transition.Properties.BPEL.Buttons.Cancel"));
			cancelVariableButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.dispose();
				}
			});
		}
		return cancelVariableButton;
	}

	// ***************** methods to fill comboBoxes with data from the wsdl file
	// **************************

	private void fillAllComboBoxesWithData() {

		// add new items
		partnerLinkTypeComboBoxAddItems();
		partnerRoleComboBoxAddItems();
		myRoleComboBoxAddItems();
	}

	private void partnerLinkTypeComboBoxAddItems() {
		partnerLinkTypeComboBox.removeAllItems();

		partnerLinkTypes = wsdlFileRepresentation.getPartnerLinkTypes();

		if (partnerLinkTypes.size() != 0) {

			for (PartnerLinkType partnerLinkType : partnerLinkTypes) {
				partnerLinkTypeComboBox.addItem(partnerLinkType.getName());
			}

		}
	}

	private void partnerRoleComboBoxAddItems() {
        	partnerRoleComboBox = getPartnerRoleComboBox();
        	partnerRoleComboBox.removeAllItems();

			// If there aren't any partner link types --> there can't be any
			// subelements
			if (partnerLinkTypes.size() != 0) {
				roles = partnerLinkTypes.get(
						partnerLinkTypeComboBox.getSelectedIndex()).getRoles();
				for (Role role : roles) {
					partnerRoleComboBox.addItem(role.getRoleName());
				}
				partnerRoleComboBox.addItem(Messages
						.getString("Transition.Properties.BPEL.NoRole"));
			}
	}

	private void myRoleComboBoxAddItems() {
			myRoleComboBox = getMyRoleComboBox();
        	myRoleComboBox.removeAllItems();

			// If there aren't any partner link types --> there can't be any
			// subelements
			if (partnerLinkTypes.size() != 0) {
				roles = partnerLinkTypes.get(
						partnerLinkTypeComboBox.getSelectedIndex()).getRoles();
				for (Role role : roles) {
					myRoleComboBox.addItem(role.getRoleName());
				}
				myRoleComboBox.addItem(Messages
						.getString("Transition.Properties.BPEL.NoRole"));
			}
	}


	public void addVariablesToModelElementContainer(String pathToWsdlFile){
		try {
			wsdlFileRepresentation = wsdl.readDataFromWSDL(pathToWsdlFile);
			ArrayList<Message> messages = wsdlFileRepresentation.getMessages();
			for (Message message : messages) {
				modelElementContainer.addVariable( ("var_" + message.getMessageName() ), message.getMessageName() );;
			}
		}
		catch (Exception e) {
		e.printStackTrace();
		showErrorPopup(
				Messages.getString("Transition.Properties.BPEL.ErrorWhileReadingWsdlFileTitle"),
				Messages.getString("Transition.Properties.BPEL.ErrorWhileReadingVariables"));
		}
	}

	// ***************** content getter methods **************************

	public String getTransitionName() {
		return this.transition.getNameValue();
	}


	public String getPartnerLinkName() {
		if (partnerLinkNameTextField.getText() == null)
			return null;
		return partnerLinkNameTextField.getText().toString();
	}

	public String getPartnerLinkType() {
		if (partnerLinkTypeComboBox.getSelectedItem() == null)
			return null;
		return partnerLinkTypeComboBox.getSelectedItem().toString();
	}

	public String getPartnerRole() {
		if (partnerRoleComboBox.getSelectedItem() == null)
			return null;
		return partnerRoleComboBox.getSelectedItem().toString();
	}

	public String getMyRole() {
		if (myRoleComboBox.getSelectedItem() == null)
			return null;
		return myRoleComboBox.getSelectedItem().toString();
	}

	public void tryToGetDataFromWsdl() {

		if (wsdlFileTextField.getText().length() == 0) {
			partnerLinkTypeComboBox.removeAllItems();
			partnerRoleComboBox.removeAllItems();
			myRoleComboBox.removeAllItems();
		} else if (wsdlFileTextField.getText().length() < 10) {
			partnerLinkTypeComboBox.removeAllItems();
			partnerRoleComboBox.removeAllItems();
			myRoleComboBox.removeAllItems();

			showErrorPopup(Messages.getString("Transition.Properties.BPEL.InvalidFilePath"),
						   Messages.getString("Transition.Properties.BPEL.InvalidFilePathEntered"));
			wsdlFileTextField.setText("");
		} else {
			try {
				wsdlFileRepresentation = wsdl
						.readDataFromWSDL(wsdlFileTextField.getText());
				fillAllComboBoxesWithData();
			} catch (MalformedURLException e) {
				showErrorPopup(Messages.getString("Transition.Properties.BPEL.InvalidFilePath"),
						   	   Messages.getString("Transition.Properties.BPEL.InvalidFilePathEntered"));

				partnerLinkTypeComboBox.removeAllItems();
				partnerRoleComboBox.removeAllItems();
				myRoleComboBox.removeAllItems();

				wsdlFileTextField.setText("");

			} catch (FileNotFoundException e) {
				showErrorPopup(Messages.getString("Transition.Properties.BPEL.InvalidFilePath"),
						       Messages.getString("Transition.Properties.BPEL.InvalidFilePathEntered"));

				partnerLinkTypeComboBox.removeAllItems();
				partnerRoleComboBox.removeAllItems();
				myRoleComboBox.removeAllItems();

				wsdlFileTextField.setText("");

			} catch (IOException e) {
				showErrorPopup(Messages.getString("Transition.Properties.BPEL.InvalidFilePath"),
							   Messages.getString("Transition.Properties.BPEL.InvalidFilePathEntered"));

				partnerLinkTypeComboBox.removeAllItems();
				partnerRoleComboBox.removeAllItems();
				myRoleComboBox.removeAllItems();

				wsdlFileTextField.setText("");

			} catch (XMLStreamException e) {
				showErrorPopup(Messages.getString("Transition.Properties.BPEL.ErrorWhileReadingWsdlFile"),
							   Messages.getString("Transition.Properties.BPEL.ErrorWhileReadingWsdlFileTitle"));

				partnerLinkTypeComboBox.removeAllItems();
				partnerRoleComboBox.removeAllItems();
				myRoleComboBox.removeAllItems();

				wsdlFileTextField.setText("");
			}
		}

	}

	protected void showErrorPopup(String title, String message) {
		errorPopup = new JDialog(dialog, true);
		errorPopup.setVisible(false);
		errorPopup.setTitle(title);
		errorPopup.setSize(300, 140);
		errorPopup.setLocation(dialog.getLocation().x + 90, dialog
				.getLocation().y + 50);
		errorPopup.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(10, 10, 0, 10);
		errorPopup.add(new JLabel(message), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.CENTER;

		JButton okButton = new JButton(Messages
				.getString("Transition.Properties.BPEL.Buttons.OK"));
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				errorPopup.dispose();
			}

		});

		errorPopup.add(okButton, c);
		errorPopup.setVisible(true);
	}

	/**
	 *
	 * @param box
	 */
	protected void fillVariableToComboBox(JComboBox box) {
		HashSet<BpelVariable> list = modelElementContainer
			.getVariableList()
				.getBpelVariableList();
		box.removeAllItems();
		box.addItem(Messages.getString("Transition.Properties.BPEL.No"));
		Iterator<BpelVariable> iter = list.iterator();
		while (iter.hasNext())
			box.addItem(iter.next());
	}


	public abstract void refresh();

	public abstract void saveInfomation();

	public abstract boolean allFieldsFilled();

	public abstract void showPanel(JPanel panel, GridBagConstraints c);

	public boolean equals(BPELadditionalPanel panel)
	{
		return this.getName().compareToIgnoreCase(panel.getName()) == 0;
	}

}