package org.woped.bpel.gui.transitionproperties;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.woped.core.controller.IDialog;
import org.woped.translations.Messages;

/**
 * @author Esther Landes
 * 
 * This is a panel in the transition properties, which enables the user to choose which BPEL activity should be added.
 *
 * Created on 16.12.2007
 */

public class BPELActivityChoosePanel extends JPanel {
	
	private JLabel activityLabel = null;
	private JComboBox activityComboBox = null;
	private DefaultComboBoxModel activityComboBoxModel = null;
	private JLabel nameLabel = null;
	private JTextField nameTextField = null;
	private JButton buttonEdit = null;
	
//	private JPanel testPanel = null;
	
	private IDialog editor = null;
	
	private JPanel contentPanel = null;
	private GridBagConstraints c_ContentPanel = null;
	
	
	public BPELActivityChoosePanel(IDialog editor, JPanel transitionPropertiesPanel, GridBagConstraints transitionPropertiesConstraints){
		
		this.editor = editor;
		contentPanel = transitionPropertiesPanel;
		c_ContentPanel = transitionPropertiesConstraints;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		setBorder(BorderFactory
						.createCompoundBorder(
								BorderFactory
										.createTitledBorder(Messages
												.getString("Transition.Properties.BPELActivity")),
								BorderFactory.createEmptyBorder(5, 5, 0, 5)));

		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 2, 0, 0);
		add(getActivityLabel(), c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.insets = new Insets(0, 10, 0, 10);
		add(getActivityComboBox(), c);
		
		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 10, 0, 0);
		add(getNameLabel(), c);
		
		c.gridx = 5;
		c.gridy = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0, 10, 0, 10);
		add(getNameTextField(), c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(10, 0, 5, 0);
		add(getButtonEdit(), c);
	}
	
	private JLabel getActivityLabel()
    {
        if (activityLabel == null)
        {
        	activityLabel = new JLabel(Messages.getString("Transition.Properties.BPELActivity.Activity") + ":");
        }
        
        return activityLabel;
    }
	
	private JComboBox getActivityComboBox() {
		if (activityComboBox == null) {
			activityComboBox = new JComboBox(getActivityComboBoxModel());
			activityComboBox.setSelectedIndex(2);
			activityComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
//					updateNumResources();
/*					c.gridx = 0;
					c.gridy = 6;
					c.gridwidth = 1;
					c.insets = new Insets(0, 10, 0, 10);
					contentPanel.add(getTestPanel(), c);
					pack();*/
				}
			});
			activityComboBox.setMinimumSize(new Dimension(130, 20));
			activityComboBox.setMaximumSize(new Dimension(130, 20));
			activityComboBox.setPreferredSize(new Dimension(130, 20));
		}

		return activityComboBox;
	}
	
	private DefaultComboBoxModel getActivityComboBoxModel() {
		if (activityComboBoxModel == null) {
				activityComboBoxModel = new DefaultComboBoxModel();	
				activityComboBoxModel.addElement("assign");
				activityComboBoxModel.addElement("invoke");
				activityComboBoxModel.addElement("receive");
				activityComboBoxModel.addElement("reply");
				activityComboBoxModel.addElement("wait");
		}
		return activityComboBoxModel;
	}
	
	private JLabel getNameLabel()
    {
        if (nameLabel == null)
        {
        	nameLabel = new JLabel(Messages.getString("Transition.Properties.BPELActivity.Name") + ":");
        }

        return nameLabel;
    }
	
	 private JTextField getNameTextField() {
			if (nameTextField == null) {
				nameTextField = new JTextField();
				nameTextField.setPreferredSize(new Dimension(100, 20));
				nameTextField.setMinimumSize(new Dimension(150, 20));
				nameTextField.setMaximumSize(new Dimension(150, 20));
				/*nameTextField.addKeyListener(new KeyListener() {
					public void keyPressed(KeyEvent e) {
						keyReleased(e);
					}

					public void keyTyped(KeyEvent e) {
						keyReleased(e);
					}

					public void keyReleased(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							apply();
							TransitionPropertyEditor.this.dispose();
						}
					}
				});*/
			}

			return nameTextField;
	}
	 
		private JButton getButtonEdit() {
			if (buttonEdit == null) {
				buttonEdit = new JButton(Messages.getString("Transition.Properties.BPELActivity.Button")); //Text wird später durch das unten ersetzt
				buttonEdit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String selectedActivity = getActivityComboBoxModel().getSelectedItem().toString();
						if (selectedActivity.equals("wait")){
							c_ContentPanel.gridx = 0;
							c_ContentPanel.gridy = 6;
							c_ContentPanel.gridwidth = 1;
							c_ContentPanel.insets = new Insets(0, 10, 0, 10);
							contentPanel.add(new BPELwaitPanel(editor, contentPanel, c_ContentPanel), c_ContentPanel);
						}
					}
				/*buttonEdit.setIcon(Messages.getImageIcon("Button.Ok"));
				buttonEdit.setText(Messages.getTitle("Button.Ok"));

				buttonEdit.setMnemonic(Messages.getMnemonic("Button.Ok"));
				buttonEdit.setPreferredSize(new Dimension(120, 25));
				buttonEdit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String selectedRole = getRoleComboxBoxModel()
								.getSelectedItem().toString();
						String selectedGroup = getGroupComboxBoxModel()
								.getSelectedItem().toString();

						if (selectedRole.equals(ROLE_NONE)
								&& selectedGroup.equals(GROUP_NONE)
								|| !selectedRole.equals(ROLE_NONE)
								&& !selectedGroup.equals(GROUP_NONE)) {
							apply();
							exitType = etOK;
							TransitionPropertyEditor.this.dispose();
						} else {
							JOptionPane
									.showMessageDialog(
											getContentPanel(),
											Messages
													.getString("TransitionEditor.Properties.ResourceError.Text"),
											Messages
													.getString("TransitionEditor.Properties.ResourceError.Title"),
											JOptionPane.ERROR_MESSAGE);
						}
					}*/
				});
			}

			return buttonEdit;
		}
		
/*		private JPanel getAdditionalPanel() {
			if (testPanel == null) {
				testPanel = new JPanel();
				testPanel.setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				testPanel
						.setBorder(BorderFactory
								.createCompoundBorder(
										BorderFactory
												.createTitledBorder(Messages
														.getString("Transition.Properties.Test")),
										BorderFactory.createEmptyBorder(5, 5, 0, 5)));

				c.weightx = 1;
				c.weighty = 1;
				c.anchor = GridBagConstraints.WEST;
				c.fill = GridBagConstraints.HORIZONTAL;

				c.gridx = 0;
				c.gridy = 0;
				c.gridwidth = 1;
				c.insets = new Insets(0, 2, 0, 0);
				testPanel.add(new JLabel("Test"), c);

				c.gridx = 1;
				c.gridy = 0;
				c.gridwidth = 2;
				c.insets = new Insets(0, 10, 0, 10);
				testPanel.add(new JLabel("Testtest"), c);
			}

			return testPanel;
		}*/
	
}
