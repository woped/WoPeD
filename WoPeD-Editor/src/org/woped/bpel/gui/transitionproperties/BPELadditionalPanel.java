package org.woped.bpel.gui.transitionproperties;

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

import org.woped.bpel.wsdl.Wsdl;
import org.woped.bpel.wsdl.wsdlFileRepresentation.PartnerLinkType;
import org.woped.bpel.wsdl.wsdlFileRepresentation.Role;
import org.woped.bpel.wsdl.wsdlFileRepresentation.WsdlFileRepresentation;
import org.woped.core.model.bpel.BpelVariable;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.translations.Messages;

/**
 * @author Esther Landes / Kristian Kindler
 *
 * This is the basic class for the different BPEL activity panels. It contains
 * methods and data that is used in the activity panels' dialogs.
 *
 * Created on 16.01.2008
 */

@SuppressWarnings("serial")
public abstract class BPELadditionalPanel extends JPanel {

	TransitionPropertyEditor t_editor;
	ModelElementContainer modelElementContainer = null;
	// JDialog dialogVariable = null;
	// JDialog dialogPartner = null;
	JDialog dialog = null;
	JPanel dialogButtons = null;

	JDialog errorPopup = null;

	JComboBox variableTypesComboBox = null;

	JTextField partnerLinkNameTextField = null;
	JTextField wsdlFileTextField = null;
	JTextField VariableName = null;
	JButton searchLocalWSDLButton = null;
	JComboBox partnerLinkTypeComboBox = null;
	JComboBox partnerRoleComboBox = null;
	JComboBox myRoleComboBox = null;
	JButton okPartnerButton = null;
	JButton cancelPartnerButton = null;
	JButton okVariableButton = null;
	JButton cancelVariableButton = null;

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
	}

	// ************** display dialog box "New Partner Link" *****************

	protected void showNewPartnerLinkDialog() {

        // clear all input fields and combo boxes before we start (because of old data)
        try {
                partnerLinkNameTextField.setText("");
                wsdlFileTextField.setText("");
                partnerLinkTypeComboBox.removeAllItems();
                partnerRoleComboBox.removeAllItems();
                myRoleComboBox.removeAllItems();
        } catch (NullPointerException e) {
                /* If a NullPointerException is catched the fields and combo boxes have not been
                   initialized yet so they are empty anyways.*/
        }

		// here we go ...
		wsdl = new Wsdl();

		dialog = new JDialog(t_editor, true);
		dialog.setVisible(false);
		dialog.setTitle(Messages
				.getString("Transition.Properties.BPEL.NewPartnerLink"));
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
		dialog.add(new JLabel("Name:"), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(getPartnerLinkNameTextField(), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(new JLabel("WSDL:"), c);

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
		dialog.add(getLocalWSDLButton(), c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		dialog.add(new JLabel("Partner Link Type:"), c);

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(getPartnerLinkTypeComboBox(), c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(new JLabel("Partner Role:"), c);

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(getPartnerRoleComboBox(), c);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.insets = new Insets(0, 5, 0, 0);
		dialog.add(new JLabel("My Role:"), c);

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
						// TODO KEY LISTENER - Kommentar löschen
						tryToGetDataFromWsdl();
					}
				}
			});
		}
		return searchLocalWSDLButton;
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
                                // TODO speichern mithilfe von Alex' Klassen, die auf content getter methoden zugreifen

                                String name    = partnerLinkNameTextField.getText();
                                String wsdlUrl = wsdlFileTextField.getText();
                                                                // Check if combo boxes are filled with data
                                if ( (partnerLinkTypeComboBox.getItemCount() != 0) && (partnerRoleComboBox.getItemCount() != 0) ){
                                        String partnerLinkType = partnerLinkTypeComboBox.getSelectedItem().toString();
                                        String partnerRole         = partnerRoleComboBox.getSelectedItem().toString();
                                        String myRole              = myRoleComboBox.getSelectedItem().toString();

                                        // TODO führende Leerzeichen entfernen
                                        if ( name.equals("") ||
                                                 wsdlUrl.equals("") ||
                                                 ( partnerRole.equals(Messages.getString("Transition.Properties.BPEL.NoRole"))
                                                   &&
                                                   myRole.equals(Messages.getString("Transition.Properties.BPEL.NoRole"))
                                                 )
                                           ) {
                                                // TODO Fehler ausgeben
                                        }
                                        else {
                                                // TODO 2x namespace
                                                if(partnerRole.equals(Messages.getString("Transition.Properties.BPEL.NoRole"))){
                                               	     modelElementContainer.addPartnerLinkWithoutPartnerRole(
                                                                        name, "namespace:" /*namespace*/, partnerLinkType, myRole, wsdlUrl);
                                                }
                                                else{
                                                        modelElementContainer.addPartnerLink(
                                                                        name, "namespace:" /*namespace*/, partnerLinkType, partnerRole, myRole, wsdlUrl);
                                                }
                                        }
                                }
                                else{
                                        //TODO Fehlermeldung für fehlende Einträge
                                }

                                dialog.dispose();
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
				// TODO speichern mithilfe von Alex' Klassen, die auf content
				// getter methoden zugreifen

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

	// ***************** content getter methods **************************

	public String getTransitionName() {
		return this.transition.getNameValue();
	}

	// folgendes noch mit Alex abzuklären (Esther)

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

			showErrorPopup(Messages
					.getString("Transition.Properties.BPEL.InvalidFilePathEntered"));
			wsdlFileTextField.setText("");
		} else {
			try {
				wsdlFileRepresentation = wsdl
						.readDataFromWSDL(wsdlFileTextField.getText());
				fillAllComboBoxesWithData();
			} catch (MalformedURLException e) {
				showErrorPopup(Messages
						.getString("Transition.Properties.BPEL.InvalidFilePathEntered"));

				partnerLinkTypeComboBox.removeAllItems();
				partnerRoleComboBox.removeAllItems();
				myRoleComboBox.removeAllItems();

				wsdlFileTextField.setText("");

			} catch (FileNotFoundException e) {
				showErrorPopup(Messages
						.getString("Transition.Properties.BPEL.InvalidFilePathEntered"));

				partnerLinkTypeComboBox.removeAllItems();
				partnerRoleComboBox.removeAllItems();
				myRoleComboBox.removeAllItems();

				wsdlFileTextField.setText("");

			} catch (IOException e) {
				showErrorPopup(Messages
						.getString("Transition.Properties.BPEL.InvalidFilePathEntered"));

				partnerLinkTypeComboBox.removeAllItems();
				partnerRoleComboBox.removeAllItems();
				myRoleComboBox.removeAllItems();

				wsdlFileTextField.setText("");

			} catch (XMLStreamException e) {
				showErrorPopup(Messages
						.getString("Transition.Properties.BPEL.ErrorWhileReadingWsdlFile"));

				partnerLinkTypeComboBox.removeAllItems();
				partnerRoleComboBox.removeAllItems();
				myRoleComboBox.removeAllItems();

				wsdlFileTextField.setText("");
			}
		}

	}

	private void showErrorPopup(String message) {
		errorPopup = new JDialog(dialog, true);
		errorPopup.setVisible(false);
		errorPopup.setTitle(Messages
				.getString("Transition.Properties.BPEL.InvalidFilePath"));
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
				// TODO speichern mithilfe von Alex' Klassen, die auf content
				// getter methoden zugreifen
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
		box.addItem("");
		Iterator<BpelVariable> iter = list.iterator();
		while (iter.hasNext())
			box.addItem(iter.next());
	}

	public abstract void refresh();
	
	public abstract void saveInfomation();
}