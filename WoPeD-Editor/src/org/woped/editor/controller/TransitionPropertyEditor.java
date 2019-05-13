/*
 *
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.editor.controller;

import org.jgraph.graph.DefaultGraphCell;
import org.woped.core.controller.IDialog;
import org.woped.core.model.ArcModel;
import org.woped.core.model.CreationMap;
import org.woped.core.model.petrinet.*;
import org.woped.core.model.petrinet.Toolspecific.OperatorPosition;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.controller.bpel.*;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * @author waschtl
 */

@SuppressWarnings("serial")
public class TransitionPropertyEditor extends JDialog implements
		ActionListener, IDialog {

    private static final String COMBOBOX_YEARS_TEXT = Messages.getString("Transition.Properties.Years");
    private static final String COMBOBOX_MONTHS_TEXT = Messages.getString("Transition.Properties.Months");
    private static final String COMBOBOX_WEEKS_TEXT = Messages.getString("Transition.Properties.Weeks");
    private static final String COMBOBOX_DAYS_TEXT = Messages.getString("Transition.Properties.Days");
    private static final String COMBOBOX_HOURS_TEXT = Messages.getString("Transition.Properties.Hours");
    private static final String COMBOBOX_MINUTES_TEXT = Messages.getString("Transition.Properties.Minutes");
    private static final String COMBOBOX_SECONDS_TEXT = Messages.getString("Transition.Properties.Seconds");
    private static final String[] serviceTimeValues = {COMBOBOX_SECONDS_TEXT, COMBOBOX_MINUTES_TEXT, COMBOBOX_HOURS_TEXT, COMBOBOX_DAYS_TEXT, COMBOBOX_WEEKS_TEXT, COMBOBOX_MONTHS_TEXT, COMBOBOX_YEARS_TEXT};
    private static final String TRIGGER_NONE = Messages.getString("Transition.Properties.Trigger.None");
    private static final String TRIGGER_MESSAGE = Messages.getString("Transition.Properties.Trigger.Message");
    private static final String TRIGGER_RESOURCE = Messages.getString("Transition.Properties.Trigger.Resource");
    private static final String TRIGGER_TIME = Messages.getString("Transition.Properties.Trigger.Time");
    private static final String ORIENTATION_NORTH = Messages.getString("Transition.Properties.Orientation.North");
    private static final String ORIENTATION_EAST = Messages.getString("Transition.Properties.Orientation.East");
    private static final String ORIENTATION_SOUTH = Messages.getString("Transition.Properties.Orientation.South");
    private static final String ORIENTATION_WEST = Messages.getString("Transition.Properties.Orientation.West");
    private static final String ROLE_NONE = Messages.getString("Transition.Properties.Role.None");
    private static final String GROUP_NONE = Messages.getString("Transition.Properties.Group.None");
    Object activityType = null;
	// General
	private TransitionModel transition = null;
	private EditorVC editor = null;
	private JPanel contentPanel = null;
	private JPanel bpelContentPanel = null;
	private JPanel bpelPanel = null;
	private JTabbedPane tabPane = null;
	private JPanel activityChoosePanel = null;

	private JComboBox<BPELadditionalPanel> activityChooseComboBox = null;

	private JLabel activityChooseLabel = null;
	private BPELemptyPanel emptyPanel = null;
	private BPELassignPanel assignPanel = null;
	private BPELinvokePanel invokePanel = null;
	private BPELreceivePanel receivePanel = null;
	private BPELreplyPanel replyPanel = null;
	private BPELwaitPanel waitPanel = null;
	private GridBagConstraints c1 = new GridBagConstraints();
	private GridBagConstraints c2 = new GridBagConstraints();
	// Name
	private JPanel namePanel = null;
	private JLabel nameLabel = null;
	private JTextField nameTextField = null;
	private JLabel idLabel = null;
	private JTextField idTextField = null;
	// Branching
	private JPanel branchingPanel = null;
	private JRadioButton branchingNoneRadioButton = null;
	private JRadioButton branchingAndSplitRadioButton = null;
	private JRadioButton branchingAndJoinRadioButton = null;
	private JRadioButton branchingAndSplitJoinRadioButton = null;
	private JRadioButton branchingXorSplitRadioButton = null;
	private JRadioButton branchingXorJoinRadioButton = null;
	private JRadioButton branchingXorSplitJoinRadioButton = null;
	private JRadioButton branchingAndJoinXorSplitRadioButton = null;
	private JRadioButton branchingXorJoinAndSplitRadioButton = null;
	private ButtonGroup branchingButtonGroup = null;
	private JPanel branchingNoneEntry = null;
	private JPanel branchingAndSplitEntry = null;
	private JPanel branchingAndJoinEntry = null;
	private JPanel branchingAndSplitJoinEntry = null;
	private JPanel branchingXorSplitEntry = null;
	private JPanel branchingXorJoinEntry = null;
	private JPanel branchingXorSplitJoinEntry = null;
	private JPanel branchingAndJoinXorSplitEntry = null;
	private JPanel branchingXorJoinAndSplitEntry = null;
	private JLabel branchingNoneIcon = null;
	private JLabel branchingAndSplitIcon = null;
	private JLabel branchingAndJoinIcon = null;
	private JLabel branchingAndSplitJoinIcon = null;
	private JLabel branchingXorSplitIcon = null;
	private JLabel branchingXorJoinIcon = null;
	private JLabel branchingXorSplitJoinIcon = null;
	private JLabel branchingAndJoinXorSplitIcon = null;
	private JLabel branchingXorJoinAndSplitIcon = null;
	// Service time
	private JPanel serviceTimePanel = null;
	private JLabel serviceTimeLabel = null;
	private JTextField serviceTimeTextField = null;

	private JComboBox<String> serviceTimeComboBox = null;

	private String oldTime;
	private String oldTimeUnit;
	// Trigger
	private JPanel triggerPanel = null;
	private JRadioButton triggerNoneRadioButton = null;
	private JRadioButton triggerResourceRadioButton = null;
	private JRadioButton triggerMessageRadioButton = null;
	private JRadioButton triggerTimeRadioButton = null;
	private ButtonGroup triggerButtonGroup = null;
	private JPanel triggerNoneEntry = null;
	private JPanel triggerResourceEntry = null;
	private JPanel triggerMessageEntry = null;
	private JPanel triggerTimeEntry = null;
	private JLabel triggerResourceIcon = null;
	private JLabel triggerMessageIcon = null;
	private JLabel triggerTimeIcon = null;
	// Orientation
	private JPanel orientationPanel = null;
	private JRadioButton orientationNorthRadioButton = null;
	private JRadioButton orientationEastRadioButton = null;
	private JRadioButton orientationSouthRadioButton = null;
	private JRadioButton orientationWestRadioButton = null;
	private ButtonGroup orientationButtonGroup = null;
	private JLabel orientationIcon = null;
	private OperatorPosition pos = null;
	private boolean orientationChanged = false;
	// Resource
	private JPanel resourcePanel = null;
	private JLabel resourceRoleLabel = null;
	private JLabel resourceGroupLabel = null;
	private JComboBox<String> resourceRoleComboBox = null;
	private JComboBox<String> resourceGroupComboBox = null;
	private DefaultComboBoxModel<String> roleComboBoxModel = null;
	private DefaultComboBoxModel<String> groupComboBoxModel = null;
	private JLabel numResourcesLabel = null;
	private JTextField numResourcesTextField = null;
	// Buttons
	private JPanel buttonPanel = null;

	private WopedButton buttonOk = null;
	
	private WopedButton buttonCancel = null;

	public TransitionPropertyEditor(Frame owner, Point position, TransitionModel transition, EditorVC editor) {
		super(owner, true);
		this.transition = transition;
		this.editor = editor;
		this.setVisible(false);
		initialize();
		this.setSize(640, 600);
		this.setLocationRelativeTo(null); // center dialog on screen
		this.setVisible(true);
	}

	private void initialize() {
		this.setTitle(Messages.getString("Transition.Properties"));
		this.getContentPane().add(getContentPanel(), BorderLayout.NORTH);
		this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		this.oldTime = serviceTimeTextField.getText();
		this.oldTimeUnit = serviceTimeComboBox.getSelectedItem().toString();
		getNameTextField().requestFocus();
	}

	private JTabbedPane getTabbedPane() {
		if (tabPane == null) {
			tabPane = new JTabbedPane();
			tabPane.addTab(Messages.getString("Transition.Properties.General"),
					getContentPanel());
			tabPane.addTab(Messages.getString("Transition.Properties.BPEL"),
					getBPELContentPanel());
		}
		return tabPane;
	}

	// ********************main panel 1: ContentPanel********************

	private JPanel getContentPanel() {

		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			contentPanel.setLayout(new GridBagLayout());
			c1.fill = GridBagConstraints.BOTH;
			c1.weightx = 1;
			c1.weighty = 1;

			c1.gridx = 0;
			c1.gridy = 0;
			c1.insets = new Insets(0, 0, 0, 0);
			contentPanel.add(getNamePanel(), c1);

			c1.gridx = 0;
			c1.gridy = 1;
			c1.insets = new Insets(0, 0, 0, 0);
			contentPanel.add(getBranchingPanel(), c1);

			c1.gridx = 0;
			c1.gridy = 2;
			c1.insets = new Insets(0, 0, 0, 0);
			contentPanel.add(getTriggerPanel(), c1);

			c1.gridx = 0;
			c1.gridy = 3;
			c1.insets = new Insets(0, 0, 0, 0);
			contentPanel.add(getOrientationPanel(), c1);

			c1.gridx = 0;
			c1.gridy = 4;
			c1.insets = new Insets(0, 0, 0, 0);
			contentPanel.add(getResourcePanel(), c1);

			c1.gridx = 0;
			c1.gridy = 5;
			c1.insets = new Insets(0, 0, 0, 0);
			contentPanel.add(getServicetimePanel(), c1);
		}

		return contentPanel;
	}

	// **************************NamePanel******************************
	private JPanel getNamePanel() {
		if (namePanel == null) {
			namePanel = new JPanel();
			namePanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			TitledBorder border = new TitledBorder(Messages.getString("Transition.Properties.Identification"));
			namePanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.WEST;
			c.fill = GridBagConstraints.HORIZONTAL;

			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 2, 0, 0);
			namePanel.add(getNameLabel(), c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 2;
			c.insets = new Insets(0, 10, 0, 10);
			namePanel.add(getNameTextField(), c);

			c.gridx = 3;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 10, 0, 0);
			namePanel.add(getIdLabel(), c);

			c.gridx = 4;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 10, 0, 10);
			namePanel.add(getIdTextField(), c);

			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			c.insets = new Insets(0, 10, 0, 10);
		}

		return namePanel;
	}

	private JLabel getNameLabel() {
		if (nameLabel == null) {
			nameLabel = new JLabel(
					Messages.getString("Transition.Properties.Name") + ":");
		}

		return nameLabel;
	}

	private JTextField getNameTextField() {
		if (nameTextField == null) {
			nameTextField = new JTextField(transition.getNameValue());
			nameTextField.setPreferredSize(new Dimension(150, 25));
			nameTextField.setMinimumSize(new Dimension(150, 25));
			nameTextField.setMaximumSize(new Dimension(150, 25));
			nameTextField.addKeyListener(new KeyListener() {
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
			});
		}

		return nameTextField;
	}

	private JLabel getIdLabel() {
		if (idLabel == null) {
			idLabel = new JLabel("ID#: ");
		}

		return idLabel;
	}

	private JTextField getIdTextField() {
		if (idTextField == null) {
			idTextField = new JTextField();
			idTextField.setText("" + transition.getId());
			idTextField.setEditable(false);
			idTextField.setPreferredSize(new Dimension(100, 25));
		}
		return idTextField;
	}

	// ******************************TriggerPanel*****************************************
	private JPanel getTriggerPanel() {
		if (triggerPanel == null) {
			triggerPanel = new JPanel();
			TitledBorder border = new TitledBorder(Messages.getString("Transition.Properties.Trigger"));
			triggerPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			triggerButtonGroup = new ButtonGroup();
			triggerButtonGroup.add(getTriggerNoneRadioButton());
			triggerButtonGroup.add(getTriggerResourceRadioButton());
			triggerButtonGroup.add(getTriggerMessageRadioButton());
			triggerButtonGroup.add(getTriggerTimeRadioButton());

			triggerPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;

			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(0, 0, 0, 20);
			triggerPanel.add(getTriggerNoneEntry(), c);

			c.gridx = 1;
			c.gridy = 0;
			c.insets = new Insets(0, 20, 0, 0);
			triggerPanel.add(getTriggerResourceEntry(), c);

			c.gridx = 0;
			c.gridy = 1;
			c.insets = new Insets(0, 0, 0, 20);
			triggerPanel.add(getTriggerMessageEntry(), c);

			c.gridx = 1;
			c.gridy = 1;
			c.insets = new Insets(0, 20, 0, 0);
			triggerPanel.add(getTriggerTimeEntry(), c);

			c.gridx = 2;
			c.gridy = 0;
			c.insets = new Insets(0, 20, 0, 0);
			triggerPanel.add(new JLabel("                            "), c);

			setTriggerConfiguration();
		}

		return triggerPanel;
	}

	private void setTriggerConfiguration() {
		if (transition.getToolSpecific().getTrigger() == null) {
			getTriggerNoneRadioButton().setSelected(true);
			actionPerformed(new ActionEvent(getTriggerNoneRadioButton(), -1,
					TRIGGER_NONE));
		} else if (transition.hasTrigger()) {
			if (transition.getToolSpecific().getTrigger().getTriggertype() == TriggerModel.TRIGGER_RESOURCE) {
				getTriggerResourceRadioButton().setSelected(true);
				actionPerformed(new ActionEvent(
						getTriggerResourceRadioButton(), -1, TRIGGER_RESOURCE));
			} else if (transition.getToolSpecific().getTrigger()
					.getTriggertype() == TriggerModel.TRIGGER_MESSAGE) {
				getTriggerMessageRadioButton().setSelected(true);
				actionPerformed(new ActionEvent(getTriggerMessageRadioButton(),
						-1, TRIGGER_MESSAGE));
			} else if (transition.getToolSpecific().getTrigger()
					.getTriggertype() == TriggerModel.TRIGGER_TIME) {
				getTriggerTimeRadioButton().setSelected(true);
				actionPerformed(new ActionEvent(getTriggerTimeRadioButton(),
						-1, TRIGGER_TIME));
			}
		}
	}

	private JPanel getTriggerNoneEntry() {
		if (triggerNoneEntry == null) {
			triggerNoneEntry = new JPanel();
			triggerNoneEntry.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 0;
			triggerNoneEntry.add(getTriggerNoneRadioButton(), c);
		}

		return triggerNoneEntry;
	}
	
	private JPanel getTriggerResourceEntry() {
		if (triggerResourceEntry == null) {
			triggerResourceEntry = new JPanel();
			triggerResourceEntry.setLayout(new BorderLayout());
			JPanel j1 = new JPanel();
			j1.setLayout(new BorderLayout());
			j1.add(getTriggerResourceRadioButton(), BorderLayout.LINE_START);
			JPanel j2 = new JPanel();
			j2.setLayout(new BorderLayout());
			j2.add(getTriggerResourceIcon(), BorderLayout.LINE_END);
			triggerResourceEntry.add(j1, BorderLayout.LINE_START);
			triggerResourceEntry.add(j2, BorderLayout.CENTER);
			triggerResourceEntry.add(new JLabel("  "), BorderLayout.LINE_END);
		}

		return triggerResourceEntry;
	}

	private JPanel getTriggerMessageEntry() {
		if (triggerMessageEntry == null) {
			triggerMessageEntry = new JPanel();
			triggerMessageEntry.setLayout(new BorderLayout());
			JPanel j1 = new JPanel();
			j1.setLayout(new BorderLayout());
			j1.add(getTriggerMessageRadioButton(), BorderLayout.LINE_START);
			JPanel j2 = new JPanel();
			j2.setLayout(new BorderLayout());
			j2.add(getTriggerMessageIcon(), BorderLayout.LINE_END);
			triggerMessageEntry.add(j1, BorderLayout.LINE_START);
			triggerMessageEntry.add(j2, BorderLayout.CENTER);
			triggerMessageEntry.add(new JLabel("  "), BorderLayout.LINE_END);
		}

		return triggerMessageEntry;
	}

	private JPanel getTriggerTimeEntry() {
		if (triggerTimeEntry == null) {
			triggerTimeEntry = new JPanel();
			triggerTimeEntry.setLayout(new BorderLayout());
			JPanel j1 = new JPanel();
			j1.setLayout(new BorderLayout());
			j1.add(getTriggerTimeRadioButton(), BorderLayout.LINE_START);
			JPanel j2 = new JPanel();
			j2.setLayout(new BorderLayout());
			j2.add(getTriggerTimeIcon(), BorderLayout.LINE_END);
			triggerTimeEntry.add(j1, BorderLayout.LINE_START);
			triggerTimeEntry.add(j2, BorderLayout.CENTER);
			triggerTimeEntry.add(new JLabel("  "), BorderLayout.LINE_END);
		}

		return triggerTimeEntry;
	}

	private JLabel getTriggerResourceIcon() {
		if (triggerResourceIcon == null) {
			triggerResourceIcon = new JLabel(
					Messages.getImageIcon("Popup.Trigger.AddResource"));
		}

		return triggerResourceIcon;
	}

	private JLabel getTriggerMessageIcon() {
		if (triggerMessageIcon == null) {
			triggerMessageIcon = new JLabel(
					Messages.getImageIcon("Popup.Trigger.AddExternal"));
		}

		return triggerMessageIcon;
	}

	private JLabel getTriggerTimeIcon() {
		if (triggerTimeIcon == null) {
			triggerTimeIcon = new JLabel(
					Messages.getImageIcon("Popup.Trigger.AddTime"));
		}

		return triggerTimeIcon;
	}

	private JRadioButton getTriggerNoneRadioButton() {
		if (triggerNoneRadioButton == null) {
			triggerNoneRadioButton = new JRadioButton(TRIGGER_NONE);
			triggerNoneRadioButton.setActionCommand(TRIGGER_NONE);
			triggerNoneRadioButton.addActionListener(this);
		}
		return triggerNoneRadioButton;
	}

	private JRadioButton getTriggerResourceRadioButton() {
		if (triggerResourceRadioButton == null) {
			triggerResourceRadioButton = new JRadioButton(TRIGGER_RESOURCE);
			triggerResourceRadioButton.setActionCommand(TRIGGER_RESOURCE);
			triggerResourceRadioButton.addActionListener(this);
		}
		return triggerResourceRadioButton;
	}

	private JRadioButton getTriggerMessageRadioButton() {
		if (triggerMessageRadioButton == null) {
			triggerMessageRadioButton = new JRadioButton(TRIGGER_MESSAGE);
			triggerMessageRadioButton.setActionCommand(TRIGGER_MESSAGE);
			triggerMessageRadioButton.addActionListener(this);
		}
		return triggerMessageRadioButton;
	}

	private JRadioButton getTriggerTimeRadioButton() {
		if (triggerTimeRadioButton == null) {
			triggerTimeRadioButton = new JRadioButton(TRIGGER_TIME);
			triggerTimeRadioButton.setActionCommand(TRIGGER_TIME);
			triggerTimeRadioButton.addActionListener(this);
		}
		return triggerTimeRadioButton;
	}

	// ******************************OrientationPanel*****************************************
	private JPanel getOrientationPanel() {
		if (orientationPanel == null) {
			orientationPanel = new JPanel();
			
			TitledBorder border = new TitledBorder(Messages.getString("Transition.Properties.Orientation"));
			orientationPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			orientationButtonGroup = new ButtonGroup();
			orientationButtonGroup.add(getOrientationNorthRadioButton());
			orientationButtonGroup.add(getOrientationEastRadioButton());
			orientationButtonGroup.add(getOrientationSouthRadioButton());
			orientationButtonGroup.add(getOrientationWestRadioButton());

			orientationPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();

			c.gridx = 1;
			c.gridy = 0;
			orientationPanel.add(getOrientationNorthRadioButton(), c);

			c.gridx = 2;
			c.gridy = 1;
			orientationPanel.add(getOrientationEastRadioButton(), c);

			c.gridx = 1;
			c.gridy = 2;
			orientationPanel.add(getOrientationSouthRadioButton(), c);

			c.gridx = 0;
			c.gridy = 1;
			orientationPanel.add(getOrientationWestRadioButton(), c);

			c.gridx = 1;
			c.gridy = 1;
			orientationPanel.add(getOrientationIcon(), c);

			getOrientationNorthRadioButton().addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							pos = OperatorPosition.NORTH;
							orientationChanged = true;
						}
					});

			getOrientationSouthRadioButton().addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							pos = OperatorPosition.SOUTH;
							orientationChanged = true;
						}
					});
			getOrientationWestRadioButton().addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							pos = OperatorPosition.WEST;
							orientationChanged = true;
						}

					});
			getOrientationEastRadioButton().addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							pos = OperatorPosition.EAST;
							orientationChanged = true;
						}

					});

			setOrientationConfiguration();
		}

		return orientationPanel;
	}

	public void flipNameResourceTrigger(TransitionModel selectedTransition,
			OperatorPosition oldposition) {
		int currentPosition = selectedTransition.getToolSpecific()
				.getOperatorPosition().ordinal();

		// North and South
		if (currentPosition == 0 || currentPosition == 2) {
			selectedTransition.getNameModel().setPosition(
					selectedTransition.getX() + selectedTransition.getWidth(),
					selectedTransition.getY());
			if (selectedTransition.hasTrigger()) {
				selectedTransition
						.getToolSpecific()
						.setTrigger(
								selectedTransition.getToolSpecific()
										.getTrigger())
						.setPosition(selectedTransition.getX() - 25,
								selectedTransition.getY() + 10);
			}
			if (selectedTransition.hasResource()) {
				selectedTransition
						.getToolSpecific()
						.setTransResource(
								selectedTransition.getToolSpecific()
										.getTransResource())
						.setPosition(selectedTransition.getX() - 65,
								selectedTransition.getY() - 17);
			}
		}
		// East and West
		else if (currentPosition == 1 || currentPosition == 3) {
			selectedTransition.getNameModel().setPosition(
					selectedTransition.getX(),
					selectedTransition.getY() + selectedTransition.getHeight());
			if (selectedTransition.hasTrigger()) {
				selectedTransition
						.getToolSpecific()
						.setTrigger(
								selectedTransition.getToolSpecific()
										.getTrigger())
						.setPosition(selectedTransition.getX() + 10,
								selectedTransition.getY() - 20);
			}
			if (selectedTransition.hasResource()) {
				selectedTransition
						.getToolSpecific()
						.setTransResource(
								selectedTransition.getToolSpecific()
										.getTransResource())
						.setPosition(selectedTransition.getX() + 10,
								selectedTransition.getY() - 47);
			}
		}
		editor.getGraph().drawNet(editor.getModelProcessor());
	}

	private void setOrientationConfiguration() {
		if (transition.getToolSpecific().getOperatorPosition() == OperatorPosition.NORTH) {
			getOrientationNorthRadioButton().setSelected(true);
			actionPerformed(new ActionEvent(getOrientationNorthRadioButton(),
					-1, ORIENTATION_NORTH));
		} else if (transition.getToolSpecific().getOperatorPosition() == OperatorPosition.EAST) {
			getOrientationEastRadioButton().setSelected(true);
			actionPerformed(new ActionEvent(getOrientationEastRadioButton(),
					-1, ORIENTATION_EAST));
		} else if (transition.getToolSpecific().getOperatorPosition() == OperatorPosition.SOUTH) {
			getOrientationSouthRadioButton().setSelected(true);
			actionPerformed(new ActionEvent(getOrientationSouthRadioButton(),
					-1, ORIENTATION_SOUTH));
		} else if (transition.getToolSpecific().getOperatorPosition() == OperatorPosition.WEST) {
			getOrientationWestRadioButton().setSelected(true);
			actionPerformed(new ActionEvent(getOrientationWestRadioButton(),
					-1, ORIENTATION_WEST));
		}
	}

	private JLabel getOrientationIcon() {
		if (orientationIcon == null) {
			orientationIcon = new JLabel(
					Messages.getImageIcon("Popup.Add.Orientation"));
		}
		return orientationIcon;
	}

	private JRadioButton getOrientationNorthRadioButton() {
		if (orientationNorthRadioButton == null) {
			orientationNorthRadioButton = new JRadioButton(ORIENTATION_NORTH);
			orientationNorthRadioButton.setActionCommand(ORIENTATION_NORTH);
			orientationNorthRadioButton.addActionListener(this);
		}
		return orientationNorthRadioButton;
	}

	private JRadioButton getOrientationEastRadioButton() {
		if (orientationEastRadioButton == null) {
			orientationEastRadioButton = new JRadioButton(ORIENTATION_EAST);
			orientationEastRadioButton.setActionCommand(ORIENTATION_EAST);
			orientationEastRadioButton.addActionListener(this);
		}
		return orientationEastRadioButton;
	}

	private JRadioButton getOrientationSouthRadioButton() {
		if (orientationSouthRadioButton == null) {
			orientationSouthRadioButton = new JRadioButton(ORIENTATION_SOUTH);
			orientationSouthRadioButton.setActionCommand(ORIENTATION_SOUTH);
			orientationSouthRadioButton.addActionListener(this);
		}
		return orientationSouthRadioButton;
	}

	private JRadioButton getOrientationWestRadioButton() {
		if (orientationWestRadioButton == null) {
			orientationWestRadioButton = new JRadioButton(ORIENTATION_WEST);
			orientationWestRadioButton.setActionCommand(ORIENTATION_WEST);
			orientationWestRadioButton.addActionListener(this);
		}
		return orientationWestRadioButton;
	}

	// ******************************BranchingPanel****************************************
	private JPanel getBranchingPanel() {
		if (branchingPanel == null) {
			branchingPanel = new JPanel();
			
			TitledBorder border = new TitledBorder(Messages.getString("Transition.Properties.Branching"));
			branchingPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			branchingButtonGroup = new ButtonGroup();
			branchingButtonGroup.add(getBranchingNoneRadioButton());
			branchingButtonGroup.add(getBranchingAndSplitRadioButton());
			branchingButtonGroup.add(getBranchingAndJoinRadioButton());
			branchingButtonGroup.add(getBranchingAndSplitJoinRadioButton());
			branchingButtonGroup.add(getBranchingXorSplitRadioButton());
			branchingButtonGroup.add(getBranchingXorJoinRadioButton());
			branchingButtonGroup.add(getBranchingXorSplitJoinRadioButton());
			branchingButtonGroup.add(getBranchingAndJoinXorSplitRadioButton());
			branchingButtonGroup.add(getBranchingXorJoinAndSplitRadioButton());

			branchingPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.WEST;

			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(0, 0, 0, 0);
			branchingPanel.add(getBranchingNoneEntry(), c);
			c.gridx = 1;
			c.gridy = 0;
			c.insets = new Insets(0, 20, 0, 20);
			branchingPanel.add(getBranchingAndSplitEntry(), c);

			c.gridx = 2;
			c.gridy = 0;
			c.insets = new Insets(0, 0, 0, 0);
			branchingPanel.add(getBranchingAndJoinEntry(), c);

			c.gridx = 1;
			c.gridy = 1;
			c.insets = new Insets(0, 20, 0, 20);
			branchingPanel.add(getBranchingXorSplitEntry(), c);
			c.gridx = 2;
			c.gridy = 1;
			c.insets = new Insets(0, 0, 0, 0);
			branchingPanel.add(getBranchingXorJoinEntry(), c);

			c.gridx = 1;
			c.gridy = 2;
			c.insets = new Insets(0, 20, 0, 20);
			branchingPanel.add(getBranchingXorSplitJoinEntry(), c);
			c.gridx = 2;
			c.gridy = 2;
			c.insets = new Insets(0, 0, 0, 0);
			branchingPanel.add(getBranchingAndSplitJoinEntry(), c);

			c.gridx = 1;
			c.gridy = 3;
			c.insets = new Insets(0, 20, 0, 20);
			branchingPanel.add(getBranchingAndJoinXorSplitEntry(), c);
			c.gridx = 2;
			c.gridy = 3;
			c.insets = new Insets(0, 0, 0, 0);
			branchingPanel.add(getBranchingXorJoinAndSplitEntry(), c);

			checkBranching();
		}

		return branchingPanel;
	}

	private void checkBranching() {
		switch (transition.getType()) {
		case AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE:
			getBranchingNoneRadioButton().setSelected(true);
			break;
		case AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE:
			switch (transition.getToolSpecific().getOperatorType()) {
			case OperatorTransitionModel.AND_SPLIT_TYPE:
				getBranchingAndSplitRadioButton().setSelected(true);
				break;
			case OperatorTransitionModel.XOR_SPLIT_TYPE:
				getBranchingXorSplitRadioButton().setSelected(true);
				break;
			case OperatorTransitionModel.AND_JOIN_TYPE:
				getBranchingAndJoinRadioButton().setSelected(true);
				break;
			case OperatorTransitionModel.XOR_JOIN_TYPE:
				getBranchingXorJoinRadioButton().setSelected(true);
				break;
                case OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE:
                    getBranchingXorSplitJoinRadioButton().setSelected(true);
				break;
			case OperatorTransitionModel.AND_SPLITJOIN_TYPE:
				getBranchingAndSplitJoinRadioButton().setSelected(true);
				break;
			case OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE:
				getBranchingAndJoinXorSplitRadioButton().setSelected(true);
				break;
			case OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE:
				getBranchingXorJoinAndSplitRadioButton().setSelected(true);
				break;
			default:
			}
		default:
		}
	}

	private JRadioButton getBranchingNoneRadioButton() {
		if (branchingNoneRadioButton == null) {
			branchingNoneRadioButton = new JRadioButton(
					Messages.getString("Transition.Properties.Branching.None"));
			branchingNoneRadioButton.setActionCommand(Messages
					.getString("Transition.Properties.Branching.None"));
			branchingNoneRadioButton.addActionListener(this);
		}
		return branchingNoneRadioButton;
	}

	private JRadioButton getBranchingAndJoinRadioButton() {
		if (branchingAndJoinRadioButton == null) {
			branchingAndJoinRadioButton = new JRadioButton(
					Messages.getString("Transition.Properties.Branching.AndJoin"));
			branchingAndJoinRadioButton.setActionCommand(Messages
					.getString("Transition.Properties.Branching.AndJoin"));
			branchingAndJoinRadioButton.addActionListener(this);
		}
		return branchingAndJoinRadioButton;
	}

	private JRadioButton getBranchingAndSplitRadioButton() {
		if (branchingAndSplitRadioButton == null) {
			branchingAndSplitRadioButton = new JRadioButton(
					Messages.getString("Transition.Properties.Branching.AndSplit"));
			branchingAndSplitRadioButton.setActionCommand(Messages
					.getString("Transition.Properties.Branching.AndSplit"));
			branchingAndSplitRadioButton.addActionListener(this);
		}
		return branchingAndSplitRadioButton;
	}

	private JRadioButton getBranchingAndSplitJoinRadioButton() {
		if (branchingAndSplitJoinRadioButton == null) {
			branchingAndSplitJoinRadioButton = new JRadioButton(
					Messages.getString("Transition.Properties.Branching.AndSplitJoin"));
			branchingAndSplitJoinRadioButton.setActionCommand(Messages
					.getString("Transition.Properties.Branching.AndSplitJoin"));
			branchingAndSplitJoinRadioButton.addActionListener(this);
		}
		return branchingAndSplitJoinRadioButton;
	}

	private JRadioButton getBranchingXorSplitRadioButton() {
		if (branchingXorSplitRadioButton == null) {
			branchingXorSplitRadioButton = new JRadioButton(
					Messages.getString("Transition.Properties.Branching.XorSplit"));
			branchingXorSplitRadioButton.setActionCommand(Messages
					.getString("Transition.Properties.Branching.XorSplit"));
			branchingXorSplitRadioButton.addActionListener(this);
		}
		return branchingXorSplitRadioButton;
	}

	private JRadioButton getBranchingXorJoinRadioButton() {
		if (branchingXorJoinRadioButton == null) {
			branchingXorJoinRadioButton = new JRadioButton(
					Messages.getString("Transition.Properties.Branching.XorJoin"));
			branchingXorJoinRadioButton.setActionCommand(Messages
					.getString("Transition.Properties.Branching.XorJoin"));
			branchingXorJoinRadioButton.addActionListener(this);
		}
		return branchingXorJoinRadioButton;
	}

	private JRadioButton getBranchingXorSplitJoinRadioButton() {
		if (branchingXorSplitJoinRadioButton == null) {
			branchingXorSplitJoinRadioButton = new JRadioButton(
					Messages.getString("Transition.Properties.Branching.XorSplitJoin"));
			branchingXorSplitJoinRadioButton.setActionCommand(Messages
					.getString("Transition.Properties.Branching.XorSplitJoin"));
			branchingXorSplitJoinRadioButton.addActionListener(this);
		}
		return branchingXorSplitJoinRadioButton;
	}

	private JRadioButton getBranchingAndJoinXorSplitRadioButton() {
		if (branchingAndJoinXorSplitRadioButton == null) {
			branchingAndJoinXorSplitRadioButton = new JRadioButton(
					Messages.getString("Transition.Properties.Branching.AndJoinXorSplit"));
			branchingAndJoinXorSplitRadioButton
					.setActionCommand(Messages
							.getString("Transition.Properties.Branching.AndJoinXorSplit"));
			branchingAndJoinXorSplitRadioButton.addActionListener(this);
		}
		return branchingAndJoinXorSplitRadioButton;
	}

	private JRadioButton getBranchingXorJoinAndSplitRadioButton() {
		if (branchingXorJoinAndSplitRadioButton == null) {
			branchingXorJoinAndSplitRadioButton = new JRadioButton(
					Messages.getString("Transition.Properties.Branching.XorJoinAndSplit"));
			branchingXorJoinAndSplitRadioButton
					.setActionCommand(Messages
							.getString("Transition.Properties.Branching.XorJoinAndSplit"));
			branchingXorJoinAndSplitRadioButton.addActionListener(this);
		}
		return branchingXorJoinAndSplitRadioButton;
	}

	private JPanel getBranchingNoneEntry() {
		if (branchingNoneEntry == null) {
			branchingNoneEntry = new JPanel();
			branchingNoneEntry.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 0;
			branchingNoneEntry.add(getBranchingNoneRadioButton(), c);
			c.gridx = 1;
			c.gridy = 0;
			c.insets = new Insets(0, 20, 0, 0);
			branchingNoneEntry.add(getBranchingNoneIcon(), c);
		}

		return branchingNoneEntry;
	}

	private JPanel getBranchingAndSplitEntry() {
		if (branchingAndSplitEntry == null) {
			branchingAndSplitEntry = new JPanel();
			branchingAndSplitEntry.setLayout(new BorderLayout());
			JPanel j1 = new JPanel();
			j1.setLayout(new BorderLayout());
			j1.add(getBranchingAndSplitRadioButton(), BorderLayout.LINE_START);
			JPanel j2 = new JPanel();
			j2.setLayout(new BorderLayout());
			j2.add(getBranchingAndSplitIcon(), BorderLayout.LINE_END);
			branchingAndSplitEntry.add(j1, BorderLayout.LINE_START);
			branchingAndSplitEntry.add(j2, BorderLayout.CENTER);
			branchingAndSplitEntry.add(new JLabel("  "), BorderLayout.LINE_END);
		}

		return branchingAndSplitEntry;
	}

	private JPanel getBranchingAndJoinEntry() {
		if (branchingAndJoinEntry == null) {
			branchingAndJoinEntry = new JPanel();
			branchingAndJoinEntry.setLayout(new BorderLayout());
			JPanel j1 = new JPanel();
			j1.setLayout(new BorderLayout());
			j1.add(getBranchingAndJoinRadioButton(), BorderLayout.LINE_START);
			JPanel j2 = new JPanel();
			j2.setLayout(new BorderLayout());
			j2.add(getBranchingAndJoinIcon(), BorderLayout.LINE_END);
			branchingAndJoinEntry.add(j1, BorderLayout.LINE_START);
			branchingAndJoinEntry.add(j2, BorderLayout.CENTER);
			branchingAndJoinEntry.add(new JLabel("  "), BorderLayout.LINE_END);
		}

		return branchingAndJoinEntry;
	}

	private JPanel getBranchingAndSplitJoinEntry() {
		if (branchingAndSplitJoinEntry == null) {
			branchingAndSplitJoinEntry = new JPanel();
			branchingAndSplitJoinEntry.setLayout(new BorderLayout());
			JPanel j1 = new JPanel();
			j1.setLayout(new BorderLayout());
			j1.add(getBranchingAndSplitJoinRadioButton(),
					BorderLayout.LINE_START);
			JPanel j2 = new JPanel();
			j2.setLayout(new BorderLayout());
			j2.add(getBranchingAndSplitJoinIcon(), BorderLayout.LINE_END);
			branchingAndSplitJoinEntry.add(j1, BorderLayout.LINE_START);
			branchingAndSplitJoinEntry.add(j2, BorderLayout.CENTER);
			branchingAndSplitJoinEntry.add(new JLabel("  "),
					BorderLayout.LINE_END);
		}

		return branchingAndSplitJoinEntry;
	}

	private JPanel getBranchingXorSplitEntry() {
		if (branchingXorSplitEntry == null) {
			branchingXorSplitEntry = new JPanel();
			branchingXorSplitEntry.setLayout(new BorderLayout());
			JPanel j1 = new JPanel();
			j1.setLayout(new BorderLayout());
			j1.add(getBranchingXorSplitRadioButton(), BorderLayout.LINE_START);
			JPanel j2 = new JPanel();
			j2.setLayout(new BorderLayout());
			j2.add(getBranchingXorSplitIcon(), BorderLayout.LINE_END);
			branchingXorSplitEntry.add(j1, BorderLayout.LINE_START);
			branchingXorSplitEntry.add(j2, BorderLayout.CENTER);
			branchingXorSplitEntry.add(new JLabel("  "), BorderLayout.LINE_END);
		}

		return branchingXorSplitEntry;
	}

	private JPanel getBranchingXorJoinEntry() {
		if (branchingXorJoinEntry == null) {
			branchingXorJoinEntry = new JPanel();
			branchingXorJoinEntry.setLayout(new BorderLayout());
			JPanel j1 = new JPanel();
			j1.setLayout(new BorderLayout());
			j1.add(getBranchingXorJoinRadioButton(), BorderLayout.LINE_START);
			JPanel j2 = new JPanel();
			j2.setLayout(new BorderLayout());
			j2.add(getBranchingXorJoinIcon(), BorderLayout.LINE_END);
			branchingXorJoinEntry.add(j1, BorderLayout.LINE_START);
			branchingXorJoinEntry.add(j2, BorderLayout.CENTER);
			branchingXorJoinEntry.add(new JLabel("  "), BorderLayout.LINE_END);
		}

		return branchingXorJoinEntry;
	}

	private JPanel getBranchingXorSplitJoinEntry() {
		if (branchingXorSplitJoinEntry == null) {
			branchingXorSplitJoinEntry = new JPanel();
			branchingXorSplitJoinEntry.setLayout(new BorderLayout());
			JPanel j1 = new JPanel();
			j1.setLayout(new BorderLayout());
			j1.add(getBranchingXorSplitJoinRadioButton(),
					BorderLayout.LINE_START);
			JPanel j2 = new JPanel();
			j2.setLayout(new BorderLayout());
			j2.add(getBranchingXorSplitJoinIcon(), BorderLayout.LINE_END);
			branchingXorSplitJoinEntry.add(j1, BorderLayout.LINE_START);
			branchingXorSplitJoinEntry.add(j2, BorderLayout.CENTER);
			branchingXorSplitJoinEntry.add(new JLabel("  "),
					BorderLayout.LINE_END);
		}

		return branchingXorSplitJoinEntry;
	}

	private JPanel getBranchingAndJoinXorSplitEntry() {
		if (branchingAndJoinXorSplitEntry == null) {
			branchingAndJoinXorSplitEntry = new JPanel();
			branchingAndJoinXorSplitEntry.setLayout(new BorderLayout());
			JPanel j1 = new JPanel();
			j1.setLayout(new BorderLayout());
			j1.add(getBranchingAndJoinXorSplitRadioButton(),
					BorderLayout.LINE_START);
			JPanel j2 = new JPanel();
			j2.setLayout(new BorderLayout());
			j2.add(getBranchingAndJoinXorSplitIcon(), BorderLayout.LINE_END);
			branchingAndJoinXorSplitEntry.add(j1, BorderLayout.LINE_START);
			branchingAndJoinXorSplitEntry.add(j2, BorderLayout.CENTER);
			branchingAndJoinXorSplitEntry.add(new JLabel("  "),
					BorderLayout.LINE_END);
		}

		return branchingAndJoinXorSplitEntry;
	}

	private JPanel getBranchingXorJoinAndSplitEntry() {
		if (branchingXorJoinAndSplitEntry == null) {
			branchingXorJoinAndSplitEntry = new JPanel();
			branchingXorJoinAndSplitEntry.setLayout(new BorderLayout());
			JPanel j1 = new JPanel();
			j1.setLayout(new BorderLayout());
			j1.add(getBranchingXorJoinAndSplitRadioButton(),
					BorderLayout.LINE_START);
			JPanel j2 = new JPanel();
			j2.setLayout(new BorderLayout());
			j2.add(getBranchingXorJoinAndSplitIcon(), BorderLayout.LINE_END);
			branchingXorJoinAndSplitEntry.add(j1, BorderLayout.LINE_START);
			branchingXorJoinAndSplitEntry.add(j2, BorderLayout.CENTER);
			branchingXorJoinAndSplitEntry.add(new JLabel("  "),
					BorderLayout.LINE_END);
		}

		return branchingXorJoinAndSplitEntry;
	}

	private JLabel getBranchingNoneIcon() {
		if (branchingNoneIcon == null) {
			branchingNoneIcon = new JLabel(
					Messages.getImageIcon("Popup.Add.Transition"));
		}

		return branchingNoneIcon;
	}

	private JLabel getBranchingAndSplitIcon() {
		if (branchingAndSplitIcon == null) {
			branchingAndSplitIcon = new JLabel(
					Messages.getImageIcon("Popup.Add.AndSplit"));
		}

		return branchingAndSplitIcon;
	}

	private JLabel getBranchingAndJoinIcon() {
		if (branchingAndJoinIcon == null) {
			branchingAndJoinIcon = new JLabel(
					Messages.getImageIcon("Popup.Add.AndJoin"));
		}

		return branchingAndJoinIcon;
	}

	private JLabel getBranchingAndSplitJoinIcon() {
		if (branchingAndSplitJoinIcon == null)
			branchingAndSplitJoinIcon = new JLabel(
					Messages.getImageIcon("Popup.Add.AndSplitJoin"));
		return branchingAndSplitJoinIcon;
	}

	private JLabel getBranchingXorSplitIcon() {
		if (branchingXorSplitIcon == null) {
			branchingXorSplitIcon = new JLabel(
					Messages.getImageIcon("Popup.Add.XorSplit"));
		}

		return branchingXorSplitIcon;
	}

	private JLabel getBranchingXorJoinIcon() {
		if (branchingXorJoinIcon == null) {
			branchingXorJoinIcon = new JLabel(
					Messages.getImageIcon("Popup.Add.XorJoin"));
		}

		return branchingXorJoinIcon;
	}

	private JLabel getBranchingXorSplitJoinIcon() {
		if (branchingXorSplitJoinIcon == null)
			branchingXorSplitJoinIcon = new JLabel(
					Messages.getImageIcon("Popup.Add.XorSplitJoin"));
		return branchingXorSplitJoinIcon;
	}

	private JLabel getBranchingAndJoinXorSplitIcon() {
		if (branchingAndJoinXorSplitIcon == null)
			branchingAndJoinXorSplitIcon = new JLabel(
					Messages.getImageIcon("Popup.Add.AndJoinXorSplit"));
		return branchingAndJoinXorSplitIcon;
	}

	private JLabel getBranchingXorJoinAndSplitIcon() {
		if (branchingXorJoinAndSplitIcon == null)
			branchingXorJoinAndSplitIcon = new JLabel(
					Messages.getImageIcon("Popup.Add.XorJoinAndSplit"));
		return branchingXorJoinAndSplitIcon;
	}

	// *********************************ServiceTimePanel*****************************************************
	private JPanel getServicetimePanel() {
		if (serviceTimePanel == null) {
			serviceTimePanel = new JPanel();
			serviceTimePanel.setLayout(new GridBagLayout());

			TitledBorder border = new TitledBorder(Messages.getString("Transition.Properties.ServiceTime"));
			serviceTimePanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 0, 5)));

			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.WEST;
			c.weightx = 1;
			c.weighty = 1;

			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(0, 2, 0, 0);
			serviceTimePanel.add(getserviceTimeLabel(), c);
			c.gridx = 1;
			c.gridy = 0;
			c.insets = new Insets(0, 0, 0, 10);
			serviceTimePanel.add(getserviceTimeTextfield(), c);
			c.gridx = 2;
			c.gridy = 0;
			c.insets = new Insets(0, 0, 0, 5);
			serviceTimePanel.add(getserviceTimeComboBox(), c);
		}

		return serviceTimePanel;
	}

	private JLabel getserviceTimeLabel() {
		if (serviceTimeLabel == null) {
			serviceTimeLabel = new JLabel(
					Messages.getString("Transition.Properties.Average") + ":");
		}

		return serviceTimeLabel;
	}

	private JTextField getserviceTimeTextfield() {
		if (serviceTimeTextField == null) {
			serviceTimeTextField = new JTextField();
			serviceTimeTextField.setText(Integer.toString(transition
					.getToolSpecific().getTime()));
			serviceTimeTextField.setMinimumSize(new Dimension(50, 25));
			serviceTimeTextField.setMaximumSize(new Dimension(50, 25));
			serviceTimeTextField.setPreferredSize(new Dimension(50, 25));
			serviceTimeTextField.setEnabled(true);
		}

		return serviceTimeTextField;
	}

	private JComboBox<String> getserviceTimeComboBox() {
		if (serviceTimeComboBox == null) {
			serviceTimeComboBox = new JComboBox<String>(serviceTimeValues);
			serviceTimeComboBox.setMinimumSize(new Dimension(80, 25));
			serviceTimeComboBox.setSelectedIndex(transition.getToolSpecific()
					.getTimeUnit());
			serviceTimeComboBox.setEnabled(true);
		}

		return serviceTimeComboBox;
	}

	// **********************************************ResourcePanel******************************************************
	private JPanel getResourcePanel() {
		if (resourcePanel == null) {
			resourcePanel = new JPanel();
			resourcePanel.setLayout(new GridBagLayout());

			TitledBorder border = new TitledBorder(Messages.getString("Transition.Properties.ResourceAssignment"));
			resourcePanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 0, 5)));
			
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.WEST;
			c.weightx = 1;
			c.weighty = 1;

			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(0, 0, 0, 0);
			resourcePanel.add(getResourceRoleLabel(), c);

			c.gridx = 1;
			c.gridy = 0;
			c.insets = new Insets(0, 5, 0, 5);
			resourcePanel.add(getResourceRoleComboBox(), c);

			c.gridx = 2;
			c.gridy = 0;
			c.insets = new Insets(0, 0, 0, 0);
			resourcePanel.add(getResourceGroupLabel(), c);

			c.gridx = 3;
			c.gridy = 0;
			c.insets = new Insets(0, 5, 0, 5);
			resourcePanel.add(getResourceGroupComboBox(), c);

			c.fill = GridBagConstraints.NONE;
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 3;
			c.insets = new Insets(5, 2, 5, 0);
			resourcePanel.add(getNumResourcesLabel(), c);

			c.gridx = 3;
			c.gridy = 1;
			c.gridwidth = 1;
			c.insets = new Insets(5, 5, 5, 0);
			resourcePanel.add(getNumResourcesTextField(), c);
			
			this.setResourceTriggerGUI(this.getTriggerResourceRadioButton().isSelected());
		}

		return resourcePanel;
	}

	private JLabel getNumResourcesLabel() {
		if (numResourcesLabel == null) {
			numResourcesLabel = new JLabel(
					Messages.getString("Transition.Properties.AssignedResources")
							+ ": ");
		}

		return numResourcesLabel;
	}

	private JTextField getNumResourcesTextField() {
		if (numResourcesTextField == null) {
			numResourcesTextField = new JTextField();
			numResourcesTextField.setEditable(false);
			numResourcesTextField.setAlignmentY(JTextField.RIGHT_ALIGNMENT);
			numResourcesTextField.setMinimumSize(new Dimension(50, 25));
			numResourcesTextField.setMaximumSize(new Dimension(50, 25));
			numResourcesTextField.setPreferredSize(new Dimension(50, 25));
			updateNumResources();
		}

		return numResourcesTextField;
	}

	private void updateNumResources() {
		String role = resourceRoleComboBox.getSelectedItem().toString();
		String group = resourceGroupComboBox.getSelectedItem().toString();

        Vector<?> res = editor.getModelProcessor().getResources();
		int count = 0;
		Vector<?> rlist;

		for (int i = 0; i < res.size(); i++) {
			String name = res.get(i).toString();
            rlist = editor.getModelProcessor().getResourceClassesResourceIsAssignedTo(name);

			if (rlist.contains(role) && rlist.contains(group)) {
				count++;
			}
		}

		numResourcesTextField.setText("" + count);
	}

	private JLabel getResourceRoleLabel() {
		if (resourceRoleLabel == null) {
			resourceRoleLabel = new JLabel(" "
					+ Messages.getString("Transition.Properties.Role") + ": ");
		}

		return resourceRoleLabel;
	}

	private JLabel getResourceGroupLabel() {
		if (resourceGroupLabel == null) {
			resourceGroupLabel = new JLabel(" "
					+ Messages.getString("Transition.Properties.Group") + ": ");
		}

		return resourceGroupLabel;
	}

	private DefaultComboBoxModel<String> getRoleComboxBoxModel() {
		if (roleComboBoxModel == null) {
            if (getEditor().getModelProcessor().getRoles() != null) {
				roleComboBoxModel = new DefaultComboBoxModel<String>();
				roleComboBoxModel.addElement(ROLE_NONE);

                for (Iterator<ResourceClassModel> iter = getEditor().getModelProcessor().getRoles().iterator(); iter.hasNext();) {
					roleComboBoxModel.addElement(iter.next().getName());
				}

				if (!transition.hasResource()) {
					roleComboBoxModel.setSelectedItem(ROLE_NONE);
				} else {
					String transRole = transition.getToolSpecific()
							.getTransResource().getTransRoleName();
					roleComboBoxModel.setSelectedItem(transRole);
				}
			}
		}

		return roleComboBoxModel;
	}

	private DefaultComboBoxModel<String> getGroupComboxBoxModel() {
		if (groupComboBoxModel == null) {
            if (getEditor().getModelProcessor().getOrganizationUnits() != null) {
				groupComboBoxModel = new DefaultComboBoxModel<String>();
				groupComboBoxModel.addElement(GROUP_NONE);

                for (Iterator<ResourceClassModel> iter = getEditor().getModelProcessor().getOrganizationUnits().iterator(); iter.hasNext();) {
					groupComboBoxModel.addElement(iter.next().getName());
				}

				if (!transition.hasResource()) {
					groupComboBoxModel.setSelectedItem(GROUP_NONE);
				} else {
					String transGroup = transition.getToolSpecific()
							.getTransResource().getTransOrgUnitName();
					groupComboBoxModel.setSelectedItem(transGroup);
				}
			}
		}

		return groupComboBoxModel;
	}

	private JComboBox<String> getResourceRoleComboBox() {
		if (resourceRoleComboBox == null) {
			resourceRoleComboBox = new JComboBox<String>(getRoleComboxBoxModel());
			resourceRoleComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					updateNumResources();
				}

			});
			resourceRoleComboBox.setMinimumSize(new Dimension(130, 5));
			resourceRoleComboBox.setMaximumSize(new Dimension(130, 25));
			resourceRoleComboBox.setPreferredSize(new Dimension(130, 25));
		}

		return resourceRoleComboBox;
	}

	private JComboBox<String> getResourceGroupComboBox() {
		if (resourceGroupComboBox == null) {
			resourceGroupComboBox = new JComboBox<String>(getGroupComboxBoxModel());
			resourceGroupComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					updateNumResources();
				}

			});
			resourceGroupComboBox.setMinimumSize(new Dimension(130, 25));
			resourceGroupComboBox.setMaximumSize(new Dimension(130, 25));
			resourceGroupComboBox.setPreferredSize(new Dimension(130, 25));
		}

		return resourceGroupComboBox;
	}

	// ********************main panel 2: BPELPanel*************************

	private JPanel getBPELContentPanel() {
		if (bpelContentPanel == null) {
			bpelContentPanel = new JPanel();
			bpelContentPanel.setLayout(new BorderLayout());
			bpelContentPanel.add(BorderLayout.NORTH, getBPELPanel());
		}
		return bpelContentPanel;
	}

	// ********************main panel 2: BPELPanel*************************
	private JPanel getBPELPanel() {

		if (bpelPanel == null) {
			bpelPanel = new JPanel();
			bpelPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			bpelPanel.setLayout(new GridBagLayout());

			c2.weightx = 1;
			c2.weighty = 1;

			c2.anchor = GridBagConstraints.NORTH;
			c2.fill = GridBagConstraints.HORIZONTAL;

			c2.gridx = 0;
			c2.gridy = 0;
			c2.insets = new Insets(0, 0, 0, 0);
			bpelPanel.add(getBPELActivityChoosePanel(), c2);

			c2.gridx = 0;
			c2.gridy = 1;
			c2.insets = new Insets(0, 0, 0, 0);
			c2.anchor = GridBagConstraints.NORTH;
			c2.fill = GridBagConstraints.HORIZONTAL;

			((BPELadditionalPanel) this.activityChooseComboBox
					.getSelectedItem()).showPanel(this.getBPELPanel(), c2);
			this.repaint();
		}

		return bpelPanel;
	}

	// **************************BPELActivityChoosePanel*****************
	private JPanel getBPELActivityChoosePanel() {
		if (activityChoosePanel == null) {
			activityChoosePanel = new JPanel();
			activityChoosePanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			activityChoosePanel
					.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory
									.createTitledBorder(Messages
											.getString("Transition.Properties.BPEL.ActivityChoice")),
							BorderFactory.createEmptyBorder(5, 5, 0, 5)));
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.WEST;
			c.fill = GridBagConstraints.HORIZONTAL;

			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			c.insets = new Insets(0, 2, 10, 0);
			activityChoosePanel.add(getActivityLabel(), c);

			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 2;
			c.insets = new Insets(0, 10, 10, 10);
			activityChoosePanel.add(getActivityComboBox(), c);

		}
		return activityChoosePanel;
	}

	/***************************************************************************
	 * //**************************BPELActivityChoosePanel private JPanel
	 * getBPELActivityChoosePanel(){ if (activityChoosePanel == null){
	 * activityChoosePanel = new JPanel(); activityChoosePanel.setLayout(new
	 * GridBagLayout()); GridBagConstraints c = new GridBagConstraints();
	 * activityChoosePanel .setBorder(BorderFactory .createCompoundBorder(
	 * BorderFactory .createTitledBorder(Messages
	 * .getString("Transition.Properties.BPEL.ActivityChoice")),
	 * BorderFactory.createEmptyBorder(5, 5, 0, 5)));
	 * 
	 * c.anchor = GridBagConstraints.NORTH; c.fill = GridBagConstraints.WEST;
	 * 
	 * c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.insets = new Insets(0, 2, 0,
	 * 0); activityChoosePanel.add(getActivityLabel(), c);
	 * 
	 * c.gridx = 1; c.gridy = 0; c.gridwidth = 2; c.insets = new Insets(0, 10,
	 * 0, 10); activityChoosePanel.add(getActivityComboBox(), c); } return
	 * activityChoosePanel; }
	 */

	private JComboBox<BPELadditionalPanel> getActivityComboBox() {
		if (activityChooseComboBox == null) {
			activityChooseComboBox = new JComboBox<BPELadditionalPanel>();
			this.activityChooseComboBox.addItem(this.getEmptyPanel());
			this.activityChooseComboBox.addItem(this.getAssignPanel());
			this.activityChooseComboBox.addItem(this.getInvokePanel());
			this.activityChooseComboBox.addItem(this.getReceivePanel());
			this.activityChooseComboBox.addItem(this.getReplyPanel());
			this.activityChooseComboBox.addItem(this.getWaitPanel());

			this.activityChooseComboBox.setSelectedItem(this.getEmptyPanel());

			// cases to select the right panel at first run
			if (Empty.class.isInstance(this.transition.getBpelData())
					|| this.transition.getBpelData() == null)
				this.activityChooseComboBox.setSelectedItem(this
						.getEmptyPanel());
			else if (Assign.class.isInstance(this.transition.getBpelData()))
				this.activityChooseComboBox.setSelectedItem(this
						.getAssignPanel());
			else if (Invoke.class.isInstance(this.transition.getBpelData()))
				this.activityChooseComboBox.setSelectedItem(this
						.getInvokePanel());
			else if (Receive.class.isInstance(this.transition.getBpelData()))
				this.activityChooseComboBox.setSelectedItem(this
						.getReceivePanel());
			else if (Reply.class.isInstance(this.transition.getBpelData()))
				this.activityChooseComboBox.setSelectedItem(this
						.getReplyPanel());
			else if (Wait.class.isInstance(this.transition.getBpelData()))
				this.activityChooseComboBox
						.setSelectedItem(this.getWaitPanel());

			activityChooseComboBox.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						((BPELadditionalPanel) e.getItem()).setVisible(false);
						getBPELPanel()
								.remove((BPELadditionalPanel) e.getItem());
					} else {
						((BPELadditionalPanel) e.getItem()).setVisible(true);
						((BPELadditionalPanel) e.getItem()).showPanel(
								getBPELPanel(), c2);
					}
					repaint();
				}
			});
		}
		return activityChooseComboBox;
	}

	private JLabel getActivityLabel() {
		if (activityChooseLabel == null) {
			activityChooseLabel = new JLabel(
					Messages.getString("Transition.Properties.BPEL.Activity")
							+ ":");
		}
		return activityChooseLabel;
	}

	// *****************************SelectedActivityPanel*****************
	private BPELemptyPanel getEmptyPanel() {
		if (this.emptyPanel == null) {
			this.emptyPanel = new BPELemptyPanel(this, this.transition);

			/*
			 * this.emptyPanel.setBorder(BorderFactory.createCompoundBorder(
			 * BorderFactory.createTitledBorder(Messages
			 * .getString("Transition.Properties.BPEL.Empty")),
			 * BorderFactory.createEmptyBorder(5, 5, 0, 5)));
			 */
		}

		return this.emptyPanel;
	}

	private BPELassignPanel getAssignPanel() {
		if (assignPanel == null) {
			assignPanel = new BPELassignPanel(this, this.transition);
			assignPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Transition.Properties.BPEL.Assign")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));
		}

		return this.assignPanel;
	}

	private BPELinvokePanel getInvokePanel() {
		if (invokePanel == null) {
			invokePanel = new BPELinvokePanel(this, this.transition);
			invokePanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Transition.Properties.BPEL.Invoke")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));
		}
		return this.invokePanel;
	}

	private BPELreceivePanel getReceivePanel() {
		if (receivePanel == null) {
			receivePanel = new BPELreceivePanel(this, this.transition);
			receivePanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Transition.Properties.BPEL.Receive")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));

		}
		return this.receivePanel;
	}

	private BPELreplyPanel getReplyPanel() {
		if (replyPanel == null) {
			replyPanel = new BPELreplyPanel(this, this.transition);
			replyPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Transition.Properties.BPEL.Reply")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));

		}
		return this.replyPanel;
	}

	private BPELwaitPanel getWaitPanel() {
		if (waitPanel == null) {
			waitPanel = new BPELwaitPanel(this, this.transition);
			waitPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getString("Transition.Properties.BPEL.Wait")),
					BorderFactory.createEmptyBorder(5, 5, 0, 5)));

		}
		return this.waitPanel;
	}

	// *****************************************************ButtonPanel****************************************************
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.add(getButtonOk());
			buttonPanel.add(getButtonCancel());
		}

		return buttonPanel;
	}

	private JButton getButtonOk() {
		if (buttonOk == null) {
			buttonOk = new WopedButton();
			buttonOk.setIcon(Messages.getImageIcon("Button.Ok"));
			buttonOk.setText(Messages.getTitle("Button.Ok"));
			buttonOk.setMnemonic(Messages.getMnemonic("Button.Ok"));
			buttonOk.setPreferredSize(new Dimension(120, 25));
			
			buttonOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String selectedRole = getRoleComboxBoxModel().getSelectedItem().toString();
					String selectedGroup = getGroupComboxBoxModel().getSelectedItem().toString();

	//				((BPELadditionalPanel) activityChooseComboBox.getSelectedItem()).saveInfomation();

					if (selectedRole.equals(ROLE_NONE) && selectedGroup.equals(GROUP_NONE) || !selectedRole.equals(ROLE_NONE) && !selectedGroup.equals(GROUP_NONE)) {
						apply();
						TransitionPropertyEditor.this.dispose();
					} 
					else {
						JOptionPane.showMessageDialog(
								getContentPanel(),
								Messages.getString("TransitionEditor.Properties.ResourceError.Text"),
								Messages.getString("TransitionEditor.Properties.ResourceError.Title"),
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}

		return buttonOk;
	}

	private JButton getButtonCancel() {
		if (buttonCancel == null) {
			buttonCancel = new WopedButton();
			buttonCancel.setText(Messages.getTitle("Button.Cancel"));
			buttonCancel.setIcon(Messages.getImageIcon("Button.Cancel"));
			buttonCancel.setMnemonic(Messages.getMnemonic("Button.Cancel"));
			buttonCancel.setPreferredSize(new Dimension(120, 25));
			buttonCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TransitionPropertyEditor.this.dispose();
				}
			});
		}

		return buttonCancel;
	}

	private void apply() {
		CreationMap map = transition.getCreationMap();
		map.setTriggerPosition(transition.getTriggerPosition());
		map.setResourcePosition(transition.getResourcePosition());

		// Remove old trigger plus resource classes if existing
		// Remember them here as deleteCell will cross-update the tool-specific
		// info
		DefaultGraphCell trigger = transition.hasTrigger() ? transition
				.getToolSpecific().getTrigger() : null;
		DefaultGraphCell resource = transition.hasResource() ? transition
				.getToolSpecific().getTransResource() : null;
		if (trigger != null) {
			getEditor().deleteCell(trigger, true);
		}

		if (resource != null) {
			getEditor().deleteCell(resource, true);
		}

		// Set new trigger and resource information
		if (getTriggerResourceRadioButton().isSelected()) {
			map.setTriggerType(TriggerModel.TRIGGER_RESOURCE);
			getEditor().createTrigger(map);

			String selectedRole = getRoleComboxBoxModel().getSelectedItem()
					.toString();
			String selectedGroup = getGroupComboxBoxModel().getSelectedItem()
					.toString();

			if (!selectedRole.equals(ROLE_NONE)
					&& !selectedGroup.equals(GROUP_NONE)) {
				map.setResourceOrgUnit(selectedGroup);
				map.setResourceRole(selectedRole);
				getEditor().createTransitionResource(map);
			}
		} else if (getTriggerTimeRadioButton().isSelected()) {
			map.setTriggerType(TriggerModel.TRIGGER_TIME);
			getEditor().createTrigger(map);
		} else if (getTriggerMessageRadioButton().isSelected()) {
			map.setTriggerType(TriggerModel.TRIGGER_MESSAGE);
			getEditor().createTrigger(map);
		}

		// Name change handling
		transition.setNameValue(getNameTextField().getText());
		
		// Service time
		if (!oldTime.equals(serviceTimeTextField.getText())) {
			transition.getToolSpecific().setTime(
					Integer.parseInt(serviceTimeTextField.getText()));
			map.setTransitionTime(Integer.parseInt(serviceTimeTextField.getText()));
		}

		if (!oldTimeUnit.equals(serviceTimeComboBox.getSelectedItem()
				.toString())) {
			transition.getToolSpecific().setTimeUnit(
					serviceTimeComboBox.getSelectedIndex());
			map.setTransitionTimeUnit(serviceTimeComboBox.getSelectedIndex());
		}
		
  		if (orientationChanged) {
			OperatorPosition oldposition = transition.getToolSpecific()
					.getOperatorPosition();
			transition.getToolSpecific().setOperatorPosition(pos);
			flipNameResourceTrigger(transition, oldposition);
		}
		
		String command = getBranchingButtonGroup().getSelection().getActionCommand();
		int type = transition.getToolSpecific().getOperatorType();

		if (command.equals(Messages.getString("Transition.Properties.Branching.None")) && type != -1 && type != OperatorTransitionModel.TRANS_SIMPLE_TYPE)
		{
			transformTransition(OperatorTransitionModel.TRANS_SIMPLE_TYPE, -1);
		}
		else if (command.equals(Messages.getString("Transition.Properties.Branching.AndJoin")) && type != OperatorTransitionModel.AND_JOIN_TYPE)
		{
			transformTransition(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE, OperatorTransitionModel.AND_JOIN_TYPE);
		}
		else if (command.equals(Messages.getString("Transition.Properties.Branching.AndSplit")) && type != OperatorTransitionModel.AND_SPLIT_TYPE)
		{
			transformTransition(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE, OperatorTransitionModel.AND_SPLIT_TYPE);
		}
		else if (command.equals(Messages.getString("Transition.Properties.Branching.AndSplitJoin")) && type != OperatorTransitionModel.AND_SPLITJOIN_TYPE)
		{
			transformTransition(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE, OperatorTransitionModel.AND_SPLITJOIN_TYPE);
		}
		else if (command.equals(Messages.getString("Transition.Properties.Branching.XorSplit")) && type != OperatorTransitionModel.XOR_SPLIT_TYPE)
		{
			transformTransition(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE,  OperatorTransitionModel.XOR_SPLIT_TYPE);
		}
		else if (command.equals(Messages.getString("Transition.Properties.Branching.XorJoin")) && type != OperatorTransitionModel.XOR_JOIN_TYPE)
		{
			transformTransition(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE, OperatorTransitionModel.XOR_JOIN_TYPE);
		} else if (command.equals(Messages.getString("Transition.Properties.Branching.XorSplitJoin")) && type != OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE) {
            transformTransition(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE, OperatorTransitionModel.XORJOIN_XORSPLIT_TYPE);
        }
		else if (command.equals(Messages.getString("Transition.Properties.Branching.AndJoinXorSplit")) && type != OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE)
		{
			transformTransition(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE, OperatorTransitionModel.ANDJOIN_XORSPLIT_TYPE);
		}
		else if (command.equals(Messages.getString("Transition.Properties.Branching.XorJoinAndSplit")) && type != OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE)
		{
			transformTransition(AbstractPetriNetElementModel.TRANS_OPERATOR_TYPE, OperatorTransitionModel.XORJOIN_ANDSPLIT_TYPE);
		}

		// Set change flag
		getEditor().setSaved(false);
		getEditor().updateNet();
	}

	private void setResourceTriggerGUI(boolean b) {
		getResourceRoleComboBox().setEnabled(b);
		getResourceGroupComboBox().setEnabled(b);
		getNumResourcesLabel().setEnabled(b);
		getResourceRoleLabel().setEnabled(b);
		getResourceGroupLabel().setEnabled(b);
		getNumResourcesTextField().setEnabled(b);
		getserviceTimeLabel().setEnabled(b);
		getserviceTimeTextfield().setEnabled(b);
		getserviceTimeComboBox().setEnabled(b);
	}

	/*
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(TRIGGER_MESSAGE) || e.getActionCommand().equals(TRIGGER_NONE) ||
				e.getActionCommand().equals(TRIGGER_TIME)) {
			// Disable all elements
			setResourceTriggerGUI(false);
		} else {
			// Enable all elements for resource triggered transition
			setResourceTriggerGUI(true);
		}

		if (transition.getToolSpecific().isSubprocess()
				|| transition.getType() == AbstractPetriNetElementModel.TRANS_SIMPLE_TYPE) {
			getOrientationEastRadioButton().setEnabled(false);
			getOrientationWestRadioButton().setEnabled(false);
			getOrientationNorthRadioButton().setEnabled(false);
			getOrientationSouthRadioButton().setEnabled(false);
		}

		if (e.getActionCommand().equals(
				Messages.getString("Transition.Properties.Branching.None"))) {
			getOrientationEastRadioButton().setEnabled(false);
			getOrientationWestRadioButton().setEnabled(false);
			getOrientationNorthRadioButton().setEnabled(false);
			getOrientationSouthRadioButton().setEnabled(false);
		}

		if (e.getActionCommand().equals(
				Messages.getString("Transition.Properties.Branching.AndJoin"))
				|| e.getActionCommand()
						.equals(Messages
								.getString("Transition.Properties.Branching.AndSplit"))
				|| e.getActionCommand()
						.equals(Messages
								.getString("Transition.Properties.Branching.AndSplitJoin"))
				|| e.getActionCommand()
						.equals(Messages
								.getString("Transition.Properties.Branching.XorSplit"))
				|| e.getActionCommand()
						.equals(Messages
								.getString("Transition.Properties.Branching.XorJoin"))
				|| e.getActionCommand()
						.equals(Messages
								.getString("Transition.Properties.Branching.XorSplitJoin"))
				|| e.getActionCommand()
						.equals(Messages
								.getString("Transition.Properties.Branching.AndJoinXorSplit"))
				|| e.getActionCommand()
						.equals(Messages
								.getString("Transition.Properties.Branching.XorJoinAndSplit"))) {
			getOrientationEastRadioButton().setEnabled(true);
			getOrientationWestRadioButton().setEnabled(true);
			getOrientationNorthRadioButton().setEnabled(true);
			getOrientationSouthRadioButton().setEnabled(true);
		}
	}

	/**
	 * @return Returns the editor.
	 */
	public EditorVC getEditor() {
		return editor;
	}

	public ButtonGroup getBranchingButtonGroup() {
		return branchingButtonGroup;
	}

	public void repaintTabPane() {
		repaint();
	}
	
	private void transformTransition(int nodeType, int operatorType)
	{
		LoggerManager.debug(Constants.EDITOR_LOGGER, "transformTransition()");

		CreationMap oldMap = transition.getCreationMap();
		CreationMap newMap = CreationMap.createMap();

		newMap = (CreationMap)oldMap.clone();
		// Most of the settings are just taken from the old model, but some 
		// settings need to be different, of course (operator type,...)
		newMap.setType(nodeType);
		newMap.setOperatorType(operatorType);		
		
		ArrayList<CreationMap> incAcrs = new ArrayList<CreationMap>();
		ArrayList<CreationMap> outAcrs = new ArrayList<CreationMap>();

		Map<String, ArcModel> arcMap = editor.getModelProcessor().getElementContainer().getArcMap();

		Iterator<String> arcIterator = arcMap.keySet().iterator();

		while (arcIterator.hasNext())
		{
            ArcModel curArc = arcMap.get(arcIterator.next());

			if (curArc.getSourceId().equals(oldMap.getId()))
				outAcrs.add(curArc.getCreationMap());

			if (curArc.getTargetId().equals(oldMap.getId()))
				incAcrs.add(curArc.getCreationMap());

		}

		// Remove the old transition
		// If we are part of a group (we always should be if we're a transition),
		// we delete the group to make sure we delete name and any other cells
		// that might be grouped along with us
		DefaultGraphCell transitionCell = transition;
		if (transition.getParent() instanceof GroupModel) {
			transitionCell = (DefaultGraphCell) transition.getParent();
		}
		editor.deleteCell(transitionCell, true);
		editor.create(newMap, false);

		for (int i = 0; i < outAcrs.size(); i++)
		{
			ArcModel am = editor.createArc(outAcrs.get(i),true);
			// Some special behavior: As this method is all about changing
			// the operator type, what could happen to us is that the operator type
			// changed to a non-XOR-split type and previously a probability was 
			// displayed for the arc. Catch this and disable the display 
			if (!am.isXORsplit(editor.getModelProcessor())) {
				am.setProbability(1);
                am.displayProbability(false);
            }
			else {
				if (am.getProbability() != 1) am.displayProbability(true);
            }
					
		}

		for (int i = 0; i < incAcrs.size(); i++)
			editor.createArc(incAcrs.get(i), true);
		
		// Refresh the net to display any copied triggers and resources...
		editor.getGraph().drawNet(editor.getModelProcessor());
        editor.getEditorPanel().getUnderstandColoring().update();
    }
}