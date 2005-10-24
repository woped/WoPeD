/*
 * Created on 21.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package org.woped.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.woped.action.DisposeWindowAction;
import org.woped.controller.vc.EditorVC;
import org.woped.model.CreationMap;
import org.woped.model.PetriNetModelProcessor;
import org.woped.model.petrinet.GroupModel;
import org.woped.model.petrinet.PetriNetModelElement;
import org.woped.model.petrinet.TransitionModel;
import org.woped.model.petrinet.TriggerModel;

/**
 * @author waschtl
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TransitionPropertyEditor extends JDialog implements ActionListener
{
    private JPanel                 contentPanel                  = null;

    private JPanel                 namePanel                     = null;
    private JLabel                 nameLabel                     = null;
    private JTextField             nameTextField                 = null;
    // Trigger
    private JPanel                 triggerPanel                  = null;
    private JLabel                 triggerLabel                  = null;
    private JRadioButton           triggerNoneRadioButton        = null;
    private JRadioButton           triggerResourceRadioButton    = null;
    private JRadioButton           triggerMessageRadioButton     = null;
    private JRadioButton           triggerTimeRadioButton        = null;
    private ButtonGroup            triggerButtonGroup            = null;
    private DefaultButtonModel     triggerButtonModel            = null;
    // Branching
    private JPanel                 branchingPanel                = null;
    private JLabel                 branchingLabel                = null;
    private JRadioButton           branchingNoneRadioButton      = null;
    private JRadioButton           branchingAndSplittRadioButton = null;
    private JRadioButton           branchingAndJoinRadioButton   = null;
    private JRadioButton           branchingXorSplittRadioButton = null;
    private JRadioButton           branchingXorJoinRadioButton   = null;
    private ButtonGroup            branchingButtonGroup          = null;

    // Duration
    private JPanel                 durationPanel                 = null;
    private JTextField             durationTextField             = null;
    private JLabel                 durationLabel                 = null;
    private JComboBox              durationComboBox              = null;
    private static final String    COMBOBOX_HOURS_TEXT           = "hours";
    private static final String    COMBOBOX_MINUTES_TEXT         = "minutes";
    private static final String    COMBOBOX_SECONDS_TEXT         = "seconds";
    private static final Object[]  durationComboBoxA             = { COMBOBOX_HOURS_TEXT, COMBOBOX_MINUTES_TEXT, COMBOBOX_SECONDS_TEXT };
    // Resource
    private JPanel                 resourcePanel                 = null;
    private JLabel                 resourceLabel                 = null;
    private JLabel                 resourceRoleLabel             = null;
    private JLabel                 resourceOrgUnitLabel          = null;
    private JComboBox              resourceRoleComboBox          = null;
    private JComboBox              resourceOrgUnitComboBox       = null;
    private DefaultComboBoxModel   roleComboBoxModel             = null;
    private DefaultComboBoxModel   orgUnitComboBoxModel          = null;

    private static final String    TRIGGER_NONE                  = "None";
    private static final String    TRIGGER_MESSAGE               = "Message";
    private static final String    TRIGGER_RESOURCE              = "Resource";
    private static final String    TRIGGER_TIME                  = "Time";

    // Button Panel
    private JPanel                 buttonPanel                   = null;
    private JButton                buttonOk                      = null;
    private JButton                buttonCancel                  = null;
    private JButton                buttonApply                   = null;
    // allgemein
    private Object[]               selection;
    private EditorVC               editor                        = null;
    PetriNetModelElement           element;
    private PetriNetModelProcessor petrinet;

    public PetriNetModelProcessor getPetrinet()
    {
        return petrinet;
    }

    public TransitionPropertyEditor(PetriNetModelProcessor petrinet, Object[] selection)
    {
        super();
        this.petrinet = petrinet;
        this.setVisible(false);
        this.editor = editor;
        this.selection = selection;
        initialize();
        readTriggerConfiguration();
        this.setSize(280, 400);
    }

    private void initialize()
    {
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        if (selection != null)
        {
            if (selection.length == 1)
            {
                if (selection[0] instanceof GroupModel)
                {
                    selection[0] = ((GroupModel) selection[0]).getMainElement();
                }
                if (selection[0] instanceof PetriNetModelElement)
                {
                    element = (PetriNetModelElement) selection[0];
                    if (element instanceof TransitionModel)
                    {
                        c.weightx = 1;
                        c.gridx = 0;
                        c.gridy = 0;
                        contentPanel.add(getNamePanel(), c);

                        c.gridx = 0;
                        c.gridy = 1;
                        contentPanel.add(getTriggerPanel(), c);

                        c.gridx = 0;
                        c.gridy = 2;
                        contentPanel.add(getBranchingPanel(), c);

                        c.gridx = 0;
                        c.gridy = 3;
                        contentPanel.add(getDurationPanel(), c);

                        c.gridx = 0;
                        c.gridy = 4;
                        contentPanel.add(getResourcePanel(), c);
                    }
                } else
                {
                    contentPanel.add(new JLabel("TODO"));
                }
            } else if (selection.length > 1)
            {
                throw new IllegalArgumentException("Not suported!");
            } else
            {
                contentPanel.add(new JLabel("TODO"));
            }
        } else
        {
            throw new IllegalArgumentException("No Selection!");
        }

        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
        this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);

    }

    private void readTriggerConfiguration()
    {
        if (selection[0] instanceof TransitionModel)
        {
            if (((TransitionModel) selection[0]).getToolSpecific().getTrigger() == null)
            {
                getTriggerNoneRadioButton().setSelected(true);
                actionPerformed(new ActionEvent(getTriggerNoneRadioButton(), -1, TRIGGER_NONE));
            } else if (((TransitionModel) selection[0]).hasTrigger())
            {
                if (((TransitionModel) selection[0]).getToolSpecific().getTrigger().getTriggertype() == TriggerModel.TRIGGER_RESOURCE)
                {
                    getTriggerResourceRadioButton().setSelected(true);
                    actionPerformed(new ActionEvent(getTriggerResourceRadioButton(), -1, TRIGGER_RESOURCE));
                } else if (((TransitionModel) selection[0]).getToolSpecific().getTrigger().getTriggertype() == TriggerModel.TRIGGER_EXTERNAL)
                {
                    getTriggerMessageRadioButton().setSelected(true);
                    actionPerformed(new ActionEvent(getTriggerMessageRadioButton(), -1, TRIGGER_MESSAGE));
                } else if (((TransitionModel) selection[0]).getToolSpecific().getTrigger().getTriggertype() == TriggerModel.TRIGGER_TIME)
                {
                    getTriggerTimeRadioButton().setSelected(true);
                    actionPerformed(new ActionEvent(getTriggerTimeRadioButton(), -1, TRIGGER_TIME));
                }
            }
        }
    }

    // **************************NamePanel******************************
    private JPanel getNamePanel()
    {
        if (namePanel == null)
        {
            namePanel = new JPanel();
            namePanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            namePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Name"), BorderFactory.createEmptyBorder(5, 5, 10, 5)));
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            namePanel.add(getNameTextField(), c);

        }
        return namePanel;
    }

    private JTextField getNameTextField()
    {
        if (nameTextField == null)
        {
            element = (PetriNetModelElement) selection[0];
            nameTextField = new JTextField(element.getNameValue());

            nameTextField.setPreferredSize(new Dimension(150, 20));
            nameTextField.setMinimumSize(new Dimension(150, 20));
        }
        return nameTextField;
    }

    // ******************************TriggerPanel*****************************************
    private JPanel getTriggerPanel()
    {
        if (triggerPanel == null)
        {
            triggerPanel = new JPanel();
            triggerPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            triggerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Trigger"), BorderFactory.createEmptyBorder(5, 5, 10, 5)));
            triggerButtonGroup = new ButtonGroup();
            triggerButtonGroup.add(getTriggerNoneRadioButton());
            triggerButtonGroup.add(getTriggerResourceRadioButton());
            triggerButtonGroup.add(getTriggerMessageRadioButton());
            triggerButtonGroup.add(getTriggerTimeRadioButton());

            c.fill = GridBagConstraints.HORIZONTAL;

            // c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            triggerPanel.add(getTriggerNoneRadioButton(), c);

            c.gridx = 1;
            c.gridy = 0;
            triggerPanel.add(getTriggerResourceRadioButton(), c);

            c.gridx = 2;
            c.gridy = 0;
            triggerPanel.add(getTriggerMessageRadioButton(), c);

            c.gridx = 3;
            c.gridy = 0;
            triggerPanel.add(getTriggerTimeRadioButton(), c);

        }
        return triggerPanel;
    }

    private JRadioButton getTriggerNoneRadioButton()
    {
        if (triggerNoneRadioButton == null)
        {
            triggerNoneRadioButton = new JRadioButton(TRIGGER_NONE);
            triggerNoneRadioButton.setActionCommand(TRIGGER_NONE);
            triggerNoneRadioButton.addActionListener(this);
        }
        return triggerNoneRadioButton;
    }

    private JRadioButton getTriggerResourceRadioButton()
    {
        if (triggerResourceRadioButton == null)
        {
            triggerResourceRadioButton = new JRadioButton(TRIGGER_RESOURCE);
            triggerResourceRadioButton.setActionCommand(TRIGGER_RESOURCE);
            triggerResourceRadioButton.addActionListener(this);
        }
        return triggerResourceRadioButton;
    }

    private JRadioButton getTriggerMessageRadioButton()
    {
        if (triggerMessageRadioButton == null)
        {
            triggerMessageRadioButton = new JRadioButton(TRIGGER_MESSAGE);
            triggerMessageRadioButton.setActionCommand(TRIGGER_MESSAGE);
            triggerMessageRadioButton.addActionListener(this);
        }
        return triggerMessageRadioButton;
    }

    private JRadioButton getTriggerTimeRadioButton()
    {
        if (triggerTimeRadioButton == null)
        {
            triggerTimeRadioButton = new JRadioButton(TRIGGER_TIME);
            triggerTimeRadioButton.setActionCommand(TRIGGER_TIME);
            triggerTimeRadioButton.addActionListener(this);
        }
        return triggerTimeRadioButton;
    }

    // ******************************BranchingPanel****************************************
    private JPanel getBranchingPanel()
    {
        if (branchingPanel == null)
        {
            branchingPanel = new JPanel();
            branchingPanel.setLayout(new GridBagLayout());
            // branchingPanel.setMinimumSize(new Dimension(345, 90));
            // branchingPanel.setPreferredSize(new Dimension(345, 90));
            GridBagConstraints c = new GridBagConstraints();
            branchingPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Branching"), BorderFactory.createEmptyBorder(5, 5, 10, 5)));
            // c.anchor = GridBagConstraints.WEST;
            branchingButtonGroup = new ButtonGroup();
            branchingButtonGroup.add(getBranchingNoneRadioButton());
            branchingButtonGroup.add(getBranchingAndSplitRadioButton());
            branchingButtonGroup.add(getBranchingAndJoinRadioButton());
            branchingButtonGroup.add(getBranchingXorSplitRadioButton());
            branchingButtonGroup.add(getBranchingXorJoinRadioButton());

            // c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            branchingPanel.add(getBranchingNoneRadioButton(), c);
            c.gridx = 1;
            c.gridy = 0;
            branchingPanel.add(getBranchingAndJoinRadioButton(), c);
            c.gridx = 1;
            c.gridy = 1;
            branchingPanel.add(getBranchingAndSplitRadioButton(), c);
            c.gridx = 2;
            c.gridy = 0;
            branchingPanel.add(getBranchingXorSplitRadioButton(), c);
            c.gridx = 2;
            c.gridy = 1;
            branchingPanel.add(getBranchingXorJoinRadioButton(), c);

        }
        return branchingPanel;
    }

    private JRadioButton getBranchingNoneRadioButton()
    {
        if (branchingNoneRadioButton == null)
        {
            branchingNoneRadioButton = new JRadioButton("None");
            // TODO:
            branchingNoneRadioButton.setEnabled(false);
        }
        return branchingNoneRadioButton;
    }

    private JRadioButton getBranchingAndJoinRadioButton()
    {
        if (branchingAndJoinRadioButton == null)
        {
            branchingAndJoinRadioButton = new JRadioButton("AND-Join");
            // TODO:
            branchingAndJoinRadioButton.setEnabled(false);
        }
        return branchingAndJoinRadioButton;
    }

    private JRadioButton getBranchingAndSplitRadioButton()
    {
        if (branchingAndSplittRadioButton == null)
        {
            branchingAndSplittRadioButton = new JRadioButton("AND-Split");
            // TODO:
            branchingAndSplittRadioButton.setEnabled(false);
        }
        return branchingAndSplittRadioButton;
    }

    private JRadioButton getBranchingXorSplitRadioButton()
    {
        if (branchingXorSplittRadioButton == null)
        {
            branchingXorSplittRadioButton = new JRadioButton("XOR-Split");
            // TODO:
            branchingXorSplittRadioButton.setEnabled(false);
        }
        return branchingXorSplittRadioButton;
    }

    private JRadioButton getBranchingXorJoinRadioButton()
    {
        if (branchingXorJoinRadioButton == null)
        {
            branchingXorJoinRadioButton = new JRadioButton("XOR-Join");
            // TODO:
            branchingXorJoinRadioButton.setEnabled(false);
        }
        return branchingXorJoinRadioButton;
    }

    // *********************************DurationPanel*****************************************************
    private JPanel getDurationPanel()
    {
        if (durationPanel == null)
        {
            durationPanel = new JPanel();
            durationPanel.setLayout(new GridBagLayout());
            durationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Duration"), BorderFactory.createEmptyBorder(5, 5, 10, 5)));
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            durationPanel.add(getDurationTextfield(), c);
            c.weightx = 0;
            c.gridx = 1;
            c.gridy = 0;
            durationPanel.add(getDurationComboBox(), c);
        }

        return durationPanel;
    }

    private JTextField getDurationTextfield()
    {
        if (durationTextField == null)
        {
            durationTextField = new JTextField();
            durationTextField.setText("");
            durationTextField.setEnabled(false);
        }
        return durationTextField;
    }

    private JComboBox getDurationComboBox()
    {
        if (durationComboBox == null)
        {
            durationComboBox = new JComboBox(durationComboBoxA);
            durationComboBox.setPreferredSize(new Dimension(70, 20));
            durationComboBox.setMinimumSize(new Dimension(70, 20));
            durationComboBox.setEnabled(false);
        }
        return durationComboBox;
    }

    // **********************************************ResourcePanel******************************************************
    private JPanel getResourcePanel()
    {
        if (resourcePanel == null)
        {
            resourcePanel = new JPanel();
            resourcePanel.setLayout(new GridBagLayout());
            resourcePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Resources"), BorderFactory.createEmptyBorder(5, 5, 10, 5)));
            // resourcePanel.setMinimumSize(new Dimension(345, 40));
            // resourcePanel.setPreferredSize(new Dimension(345, 40));
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;

            // c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            resourcePanel.add(getResourceRoleLabel(), c);

            c.gridx = 1;
            c.gridy = 0;
            resourcePanel.add(getResourceRoleComboBox(), c);

            c.gridx = 2;
            c.gridy = 0;
            resourcePanel.add(getResourceOrgUnitLabel(), c);

            c.gridx = 3;
            c.gridy = 0;
            resourcePanel.add(getResourceOrgUnitComboBox(), c);

        }
        return resourcePanel;
    }

    private JLabel getResourceRoleLabel()
    {
        if (resourceRoleLabel == null)
        {
            resourceRoleLabel = new JLabel(" role: ");
        }
        return resourceRoleLabel;
    }

    private JLabel getResourceOrgUnitLabel()
    {
        if (resourceOrgUnitLabel == null)
        {
            resourceOrgUnitLabel = new JLabel(" group: ");
        }
        return resourceOrgUnitLabel;
    }

    private DefaultComboBoxModel getRoleComboxBoxModel()
    {
        if (roleComboBoxModel == null)
        {
            if (getPetrinet().getRoles() != null)
            {
                roleComboBoxModel = new DefaultComboBoxModel();
                roleComboBoxModel.addElement(TRIGGER_NONE);
                for (Iterator iter = getPetrinet().getRoles().iterator(); iter.hasNext();)
                {
                    roleComboBoxModel.addElement(iter.next());
                }
                if (!((TransitionModel) selection[0]).hasResource())
                {
                    roleComboBoxModel.setSelectedItem(TRIGGER_NONE);
                } else
                {
                    Object cell = getEditor().getGraph().getSelectionCell();
                    if (cell instanceof GroupModel)
                    {
                        cell = ((GroupModel) cell).getMainElement();
                    }

                    TransitionModel transModel = (TransitionModel) cell;
                    String transRole = transModel.getToolSpecific().getTransResource().getTransRoleName();

                    roleComboBoxModel.setSelectedItem(transRole);
                }

            }
        }
        return roleComboBoxModel;
    }

    private DefaultComboBoxModel getOrgUnitComboxBoxModel()
    {
        if (orgUnitComboBoxModel == null)
        {
            if (getPetrinet().getOrganizationUnits() != null)
            {
                orgUnitComboBoxModel = new DefaultComboBoxModel();
                orgUnitComboBoxModel.addElement(TRIGGER_NONE);
                for (Iterator iter = getPetrinet().getOrganizationUnits().iterator(); iter.hasNext();)
                {
                    orgUnitComboBoxModel.addElement(iter.next());
                }
                if (!((TransitionModel) selection[0]).hasResource())
                {
                    orgUnitComboBoxModel.setSelectedItem(TRIGGER_NONE);
                } else
                {
                    Object cell = getEditor().getGraph().getSelectionCell();
                    if (cell instanceof GroupModel)
                    {
                        cell = ((GroupModel) cell).getMainElement();
                    }
                    TransitionModel transModel = (TransitionModel) cell;
                    String transOrgUnit = transModel.getToolSpecific().getTransResource().getTransOrgUnitName();

                    orgUnitComboBoxModel.setSelectedItem(transOrgUnit);
                }

            }
        }
        return orgUnitComboBoxModel;
    }

    private JComboBox getResourceRoleComboBox()
    {
        if (resourceRoleComboBox == null)
        {
            resourceRoleComboBox = new JComboBox(getRoleComboxBoxModel());
            resourceRoleComboBox.setPreferredSize(new Dimension(90, 20));
            resourceRoleComboBox.setMinimumSize(new Dimension(90, 20));

        }
        return resourceRoleComboBox;
    }

    private JComboBox getResourceOrgUnitComboBox()
    {
        if (resourceOrgUnitComboBox == null)
        {
            resourceOrgUnitComboBox = new JComboBox(getOrgUnitComboxBoxModel());
            resourceOrgUnitComboBox.setPreferredSize(new Dimension(90, 20));
            resourceOrgUnitComboBox.setMinimumSize(new Dimension(90, 20));
        }
        return resourceOrgUnitComboBox;
    }

    // *****************************************************ButtonPanel****************************************************
    private JPanel getButtonPanel()
    {
        if (buttonPanel == null)
        {
            buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(getButtonOk());
            buttonPanel.add(getButtonCancel());
            buttonPanel.add(getButtonApply());
        }
        return buttonPanel;
    }

    private JButton getButtonOk()
    {
        if (buttonOk == null)
        {
            buttonOk = new JButton();
            buttonOk.setPreferredSize(new Dimension(80, 23));
            buttonOk.setText("OK");
            buttonOk.setMnemonic(KeyEvent.VK_O);
            buttonOk.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    apply();
                    TransitionPropertyEditor.this.dispose();
                }

            });

        }
        return buttonOk;
    }

    private JButton getButtonCancel()
    {
        if (buttonCancel == null)
        {
            buttonCancel = new JButton(new DisposeWindowAction());
            buttonCancel.setIcon(null);
            buttonCancel.setPreferredSize(new Dimension(80, 23));
            buttonCancel.setMnemonic(KeyEvent.VK_C);
            buttonCancel.setText("Cancel");
        }
        return buttonCancel;
    }

    private JButton getButtonApply()
    {
        if (buttonApply == null)
        {
            buttonApply = new JButton();
            buttonApply.setPreferredSize(new Dimension(80, 23));
            buttonApply.setMnemonic(KeyEvent.VK_A);
            buttonApply.setText("Apply");
            buttonApply.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    apply();
                }
            });

        }
        return buttonApply;
    }

    // public static void main(String[] args)
    // {
    // TransitionPropertyEditor p = new TransitionPropertyEditor();
    // JFrame f = new JFrame();
    // f.getContentPane().add(p);
    // f.setSize(350, 350);
    // f.setVisible(true);
    // }

    private void apply()
    {
        // Trigger Handling
        Object cell = getEditor().getGraph().getSelectionCell();
        TransitionModel transModel = null;
        if (cell instanceof GroupModel)
        {
            cell = ((GroupModel) cell).getMainElement();
            if (cell instanceof TransitionModel)
            {
                transModel = (TransitionModel) cell;
            }
        }
        if (transModel != null)
        {
            int selectedTriggerType = -1;
            if (getTriggerNoneRadioButton().isSelected())
            {
                if (transModel.hasTrigger())
                {
                    editor.deleteCell(transModel.getToolSpecific().getTrigger(), true);
                }
            } else if (getTriggerResourceRadioButton().isSelected())
            {
                selectedTriggerType = TriggerModel.TRIGGER_RESOURCE;
            } else if (getTriggerTimeRadioButton().isSelected())
            {
                selectedTriggerType = TriggerModel.TRIGGER_TIME;
            } else if (getTriggerMessageRadioButton().isSelected())
            {
                selectedTriggerType = TriggerModel.TRIGGER_EXTERNAL;
            }
            // Rescource Handling
            if (selectedTriggerType == TriggerModel.TRIGGER_RESOURCE)
            {
                String selectedRole = getRoleComboxBoxModel().getSelectedItem().toString();
                String selectedOrgUnit = getOrgUnitComboxBoxModel().getSelectedItem().toString();
                if (!selectedRole.equals(TRIGGER_NONE) && !selectedOrgUnit.equals(TRIGGER_NONE))
                {
                    CreationMap map = transModel.getCreationMap();
                    map.setResourceOrgUnit(selectedOrgUnit);
                    map.setResourceRole(selectedRole);
                    if (transModel.hasResource())
                    {
                        map.setResourcePosition(transModel.getToolSpecific().getTransResource().getPosition());
                    }
                    editor.createTransitionResource(map);
                } else
                {
                    editor.deleteCell(transModel.getToolSpecific().getTransResource(), true);
                }
            }
            // trigger Handling
            CreationMap map = transModel.getCreationMap();
            map.setTriggerType(selectedTriggerType);
            if (transModel.hasTrigger())
            {
                if (transModel.getToolSpecific().getTrigger().getTriggertype() != selectedTriggerType)
                {
                    Point p = transModel.getToolSpecific().getTrigger().getPosition();
                    editor.deleteCell(transModel.getToolSpecific().getTrigger(), true);
                    map.setTriggerPosition(p.x, p.y);
                    editor.createTrigger(map);
                }

            } else if (selectedTriggerType != -1)
            {
                editor.createTrigger(map);
            }
            // name changing handling
            transModel.setNameValue(getNameTextField().getText());
        }

        // editor.createTransitionResource()

    }

    /**
     * @return Returns the editor.
     */
    public EditorVC getEditor()
    {
        return editor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals(TRIGGER_MESSAGE) || e.getActionCommand().equals(TRIGGER_TIME) || e.getActionCommand().equals(TRIGGER_NONE))
        {
            getResourceRoleComboBox().setEnabled(false);
            getResourceOrgUnitComboBox().setEnabled(false);
        } else if (e.getActionCommand().equals(TRIGGER_RESOURCE))
        {
            getResourceRoleComboBox().setEnabled(true);
            getResourceOrgUnitComboBox().setEnabled(true);
        }

    }
}