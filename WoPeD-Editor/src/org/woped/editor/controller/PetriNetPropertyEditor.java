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
 * For contact information please visit http://woped.ba-karlsruhe.de
 *
 */
package org.woped.editor.controller;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.utilities.Messages;

/**
 * @author waschtl
 * 
 * TODO: DOCUMENTATION (waschtl) TODO: implement as VC (simon)
 */
public class PetriNetPropertyEditor extends JPanel implements ListSelectionListener
{
    private JScrollPane            contentPanel                           = null;

    // Resources
    private JPanel                 resourcePanel                          = null;
    private JSplitPane             resourceSplittPane                     = null;
    private JList                  resourceList                           = null;
    private JPanel                 resourceRightPanel                     = null;
    private JPanel                 resourceEditPanel                      = null;
    // ResourcesEditPanel
    private JLabel                 resourceEditNameLabel                  = null;
    private JLabel                 resourceEditDescriptionLabel           = null;
    private JTextField             resourceEditNameTextField              = null;
    private JTextField             resourceEditDescriptionTextField       = null;
    private JPanel                 resourceButtonPanel                    = null;
    private JButton                resourceEditButton                     = null;
    private JButton                resourceRemoveButton                   = null;
    private JButton                resourceAddButton                      = null;
    // ResourceAssignPanel
    private JPanel                 resourceAssignPanel                    = null;
    private JList                  resourceAssignedList                   = null;
    private JList                  resourceUnAssignedList                 = null;
    private JLabel                 resourceAssignedLabel                  = null;
    private JLabel                 resourceUnAssignedLabel                = null;
    private JButton                resourceAssignToButton                 = null;
    private JButton                resourceUnAssignButton                 = null;
    private ResourceModel          currentResource                        = null;
    private DefaultListModel       resourceListModel                      = null;
    private String                 currentUnAssignedResourceClass         = null;
    private String                 currentAssignedResourceClass           = null;

    // ResourceClasses

    private JSplitPane             resourceClassSplittPane                = null;
    private JList                  roleList                               = null;
    private JList                  orgUnitList                            = null;
    private JSplitPane             resourceClassLeftSplittPane            = null;
    private JPanel                 resourceClassLeftSplitPaneRolePane     = null;
    private JLabel                 resourceClassLeftSplitPaneRoleLabel    = null;
    private JPanel                 resourceClassLeftSplitPaneOrgUnitPane  = null;
    private JLabel                 resourceClassLeftSplitPaneOrgUnitLabel = null;
    private JPanel                 resourceClassRightPanel                = null;
    private JComboBox              resourceClassTypeJComboBox             = null;
    private JLabel                 resourceClassNameLabel                 = null;
    private JLabel                 resourceClassDescriptionLabel          = null;
    private JLabel                 resourceClassTypeLabel                 = null;
    public JTextField              resourceClassNameTextField             = null;
    private JTextField             resourceClassDescriptionTextField      = null;
    private JButton                resourceClassEditButton                = null;
    private JButton                resourceClassRemoveButton              = null;
    private JButton                resourceClassNewButton                 = null;
    private JPanel                 resourceClassButtonPanel               = null;
    private DefaultListModel       orgUnitListModel                       = null;
    private DefaultListModel       roleListModel                          = null;
    private DefaultListModel       assignedListModel                      = null;
    private DefaultListModel       unassignedListModel                    = null;

    private static final String    COMBOBOX_ROLE_TEXT                     = Messages.getString("PetriNet.Resources.Role");
    private static final String    COMBOBOX_ORGUNIT_TEXT                  = Messages.getString("PetriNet.Resources.OrganizationalUnit");
    private static final Object[]  ResourceClassTypeA                     = { COMBOBOX_ROLE_TEXT, COMBOBOX_ORGUNIT_TEXT };
    private static final String    BUTTON_EDIT_TEXT                       = Messages.getString("Button.Edit.Title");
    private static final String    BUTTON_OK_TEXT                         = Messages.getString("Button.OK.Title");
    private static final String    BUTTON_NEW_TEXT                        = Messages.getString("Button.New.Title");
    private static final String    BUTTON_REMOVE_TEXT                     = Messages.getString("Button.Remove.Title");
    private static final String    BUTTON_ASSIGN_TEXT                     = "-->";
    private static final String    BUTTON_UNASSIGN_TEXT                   = "<--";

    private EditorVC               editor                                 = null;
    private PetriNetModelProcessor petrinet;

    public PetriNetModelProcessor getPetrinet()
    {
        return petrinet;
    }

    public PetriNetPropertyEditor(PetriNetModelProcessor petrinet)
    {
        this.petrinet = petrinet;
        initialize();

    }

    private void initialize()
    {
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        this.add(getResourceClassSplittPane(), c);

        c.gridx = 0;
        c.gridy = 1;
        this.add(getResourcePanel(), c);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++ResourceClass++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private JSplitPane getResourceClassSplittPane()
    {

        // create resourceClassRightPanel and put it in Scrollpanel
        boolean shouldFill = true;
        resourceClassRightPanel = new JPanel();
        resourceClassRightPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 20, 0, 0);
        resourceClassRightPanel.add(getResourceClassNameLabel(), c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 0);
        resourceClassRightPanel.add(getResourceClassNameTextField(), c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 20, 0, 0);
        resourceClassRightPanel.add(getResourceClassDescriptionLabel(), c);

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 0);
        resourceClassRightPanel.add(getResourceClassDescriptionTextField(), c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 20, 0, 0);
        resourceClassRightPanel.add(getResourceClassTypeLabel(), c);

        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(0, 0, 0, 0);
        resourceClassRightPanel.add(getResourceClassTypeJComboBox(), c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        resourceClassRightPanel.add(getResourceClassButtonPanel(), c);

        if (resourceClassSplittPane == null)
        {
            resourceClassSplittPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getResourceClassLeftSplittPane(), resourceClassRightPanel);
            resourceClassSplittPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("PetriNet.Resources.ResourceClass")), BorderFactory
                    .createEmptyBorder(5, 5, 10, 5)));
            resourceClassSplittPane.setMinimumSize(new Dimension(800, 200));
            resourceClassSplittPane.setPreferredSize(new Dimension(800, 200));
            resourceClassSplittPane.setDividerLocation(200);

        }
        return resourceClassSplittPane;
    }

    private JSplitPane getResourceClassLeftSplittPane()
    {
        JScrollPane listScrollPane3 = new JScrollPane(getRoleList());
        JScrollPane listScrollPane4 = new JScrollPane(getOrgUnitList());
        if (resourceClassLeftSplittPane == null)
        {
            resourceClassLeftSplittPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listScrollPane3, listScrollPane4);
            resourceClassLeftSplittPane.setDividerLocation(80);
        }
        return resourceClassLeftSplittPane;

    }

    private JList getRoleList()
    {
        if (roleList == null)
        {
            roleListModel = new DefaultListModel();
            for (int i = 0; i < getPetrinet().getRoles().size(); i++)
            {
                roleListModel.addElement((ResourceClassModel) getPetrinet().getRoles().get(i));
            }
            roleList = new JList(roleListModel);
            roleList.setMinimumSize(new Dimension(80, 60));
            roleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            roleList.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e)
                {
                    if (!e.getValueIsAdjusting() && roleList.getSelectedIndex() > -1)
                    {

                        getResourceClassNameTextField().setText(roleList.getModel().getElementAt(roleList.getSelectedIndex()).toString());
                        resourceClassTypeJComboBox.setSelectedItem(COMBOBOX_ROLE_TEXT);
                    }
                    getOrgUnitList().clearSelection();
                }
            });
        }
        return roleList;
    }

    private JList getOrgUnitList()
    {
        if (orgUnitList == null)
        {
            orgUnitListModel = new DefaultListModel();
            for (int i = 0; i < getPetrinet().getOrganizationUnits().size(); i++)
            {
                orgUnitListModel.addElement((ResourceClassModel) getPetrinet().getOrganizationUnits().get(i));
            }

            orgUnitList = new JList(orgUnitListModel);
            orgUnitList.setMinimumSize(new Dimension(80, 60));
            orgUnitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            orgUnitList.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e)
                {
                    if (!e.getValueIsAdjusting() && orgUnitList.getSelectedIndex() > -1)
                    {

                        getResourceClassNameTextField().setText(orgUnitList.getModel().getElementAt(orgUnitList.getSelectedIndex()).toString());
                        resourceClassTypeJComboBox.setSelectedItem(COMBOBOX_ORGUNIT_TEXT);
                    }
                    getRoleList().clearSelection();
                }
            });
        }
        return orgUnitList;
    }

    private JLabel getResourceClassNameLabel()
    {
        if (resourceClassNameLabel == null)
        {
            resourceClassNameLabel = new JLabel(Messages.getString("PetriNet.Resources.ResourceClassName") + ":");
        }

        return resourceClassNameLabel;
    }

    private JLabel getResourceClassDescriptionLabel()
    {
        if (resourceClassDescriptionLabel == null)
        {
            resourceClassDescriptionLabel = new JLabel(Messages.getString("PetriNet.Resources.ResourceClassDescription") + ":");
        }

        return resourceClassDescriptionLabel;
    }

    private JLabel getResourceClassTypeLabel()
    {
        if (resourceClassTypeLabel == null)
        {
            resourceClassTypeLabel = new JLabel(Messages.getString("PetriNet.Resources.ResourceClassType") + ":");
        }

        return resourceClassTypeLabel;
    }

    private JTextField getResourceClassNameTextField()
    {
        if (resourceClassNameTextField == null)
        {
            resourceClassNameTextField = new JTextField();
            resourceClassNameTextField.setPreferredSize(new Dimension(150, 20));
            resourceClassNameTextField.setMinimumSize(new Dimension(150, 20));
            resourceClassNameTextField.setEditable(false);

        }
        return resourceClassNameTextField;
    }

    private JTextField getResourceClassDescriptionTextField()
    {
        if (resourceClassDescriptionTextField == null)
        {
            resourceClassDescriptionTextField = new JTextField();
            resourceClassDescriptionTextField.setPreferredSize(new Dimension(150, 20));
            resourceClassDescriptionTextField.setMinimumSize(new Dimension(150, 20));
            resourceClassDescriptionTextField.setEditable(false);
        }
        return resourceClassDescriptionTextField;
    }

    private JComboBox getResourceClassTypeJComboBox()
    {
        if (resourceClassTypeJComboBox == null)
        {
            resourceClassTypeJComboBox = new JComboBox(ResourceClassTypeA);
            resourceClassTypeJComboBox.setMinimumSize(new Dimension(150, 20));
            resourceClassTypeJComboBox.setEnabled(false);
        }
        return resourceClassTypeJComboBox;
    }

    private JPanel getResourceClassButtonPanel()
    {
        if (resourceClassButtonPanel == null)
        {
            resourceClassButtonPanel = new JPanel();
            resourceClassButtonPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 0;
            c.gridy = 0;
            resourceClassButtonPanel.add(getResourceClassNewButton(), c);

            c.gridx = 1;
            c.gridy = 0;
            resourceClassButtonPanel.add(getResourceClassEditButton(), c);

            c.gridx = 2;
            c.gridy = 0;
            resourceClassButtonPanel.add(getResourceClassRemoveButton(), c);
        }
        return resourceClassButtonPanel;
    }

    private JButton getResourceClassNewButton()
    {
        if (resourceClassNewButton == null)
        {
            resourceClassNewButton = new JButton();
            resourceClassNewButton.setText(BUTTON_NEW_TEXT);
            resourceClassNewButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (resourceClassNewButton.getText().equals(BUTTON_NEW_TEXT))
                    {
                        getResourceClassNameTextField().setEditable(true);
                        getResourceClassNameTextField().setText("");
                        resourceClassEditButton.setEnabled(false);
                        resourceClassRemoveButton.setEnabled(false);
                        resourceClassTypeJComboBox.setEnabled(true);
                        resourceClassNewButton.setText(BUTTON_OK_TEXT);
                        getRoleList().setEnabled(false);
                        getOrgUnitList().setEnabled(false);
                    } else if (resourceClassNewButton.getText().equals(BUTTON_OK_TEXT) && getResourceClassNameTextField().getText().length() > 0)
                    {
                        getResourceClassNameTextField().setEditable(false);
                        resourceClassEditButton.setEnabled(true);
                        resourceClassRemoveButton.setEnabled(true);
                        if (resourceClassTypeJComboBox.getSelectedItem() == COMBOBOX_ROLE_TEXT)
                        {
                            ResourceClassModel newRole = new ResourceClassModel(getResourceClassNameTextField().getText(), 0);
                            getPetrinet().addRole(newRole);
                            roleListModel.addElement((ResourceClassModel) newRole);
                            resourceClassNewButton.setText(BUTTON_NEW_TEXT);
                            getRoleList().setEnabled(true);
                            getOrgUnitList().setEnabled(true);
                            getRoleList().clearSelection();
                            getOrgUnitList().clearSelection();
                            getResourceClassNameTextField().setText("");
                            getResourceClassTypeJComboBox().setEnabled(false);
                        }
                        if (resourceClassTypeJComboBox.getSelectedItem() == COMBOBOX_ORGUNIT_TEXT)
                        {
                            ResourceClassModel newOrgUnit = new ResourceClassModel(getResourceClassNameTextField().getText(), 1);
                            getPetrinet().addOrgUnit(newOrgUnit);
                            orgUnitListModel.addElement((ResourceClassModel) newOrgUnit);
                            resourceClassNewButton.setText(BUTTON_NEW_TEXT);
                            getRoleList().setEnabled(true);
                            getOrgUnitList().setEnabled(true);
                            getRoleList().clearSelection();
                            getOrgUnitList().clearSelection();
                            getResourceClassNameTextField().setText("");
                            getResourceClassTypeJComboBox().setEnabled(false);
                        }
                    }
                }
            });

        }
        return resourceClassNewButton;
    }

    private JButton getResourceClassEditButton()
    {
        if (resourceClassEditButton == null)
        {
            resourceClassEditButton = new JButton();
            resourceClassEditButton.setText(BUTTON_EDIT_TEXT);
            resourceClassEditButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (!(roleList.isSelectionEmpty() && orgUnitList.isSelectionEmpty()))
                    {
                        if (resourceClassEditButton.getText().equals(BUTTON_EDIT_TEXT))
                        {
                            getResourceClassNameTextField().setEditable(true);
                            getResourceClassNewButton().setEnabled(false);
                            resourceClassRemoveButton.setEnabled(false);
                            getResourceClassTypeJComboBox().setEnabled(true);
                            resourceClassEditButton.setText(BUTTON_OK_TEXT);
                            getOrgUnitList().setEnabled(false);
                            getRoleList().setEnabled(false);

                        } else if (resourceClassEditButton.getText().equals(BUTTON_OK_TEXT))
                        {
                            getResourceClassNameTextField().setEditable(false);
                            getResourceClassNewButton().setEnabled(true);
                            resourceClassRemoveButton.setEnabled(true);
                            resourceClassEditButton.setText(BUTTON_EDIT_TEXT);
                            getOrgUnitList().setEnabled(true);
                            getRoleList().setEnabled(true);
                            getResourceClassTypeJComboBox().setEnabled(false);
                            if (roleList.isSelectionEmpty())
                            {
                                String oldName = getOrgUnitList().getSelectedValue().toString();
                                String newName = getResourceClassNameTextField().getText();
                                ResourceClassModel orgUnitModel = (ResourceClassModel) getPetrinet().getOrganizationUnits().get(getPetrinet().containsOrgunit(oldName));
                                // name changed
                                if (!oldName.equals(newName))
                                {
                                    orgUnitModel.setName(newName);
                                    getPetrinet().replaceResourceClassMapping(oldName, newName);
                                    int index = getOrgUnitList().getSelectedIndex();
                                    orgUnitListModel.set(index, orgUnitModel);
                                }
                                // name unchanged
                                if (resourceClassTypeJComboBox.getSelectedItem() == COMBOBOX_ROLE_TEXT)
                                {
                                    orgUnitModel.setType(ResourceClassModel.TYPE_ROLE);
                                    getPetrinet().getOrganizationUnits().remove(orgUnitModel);
                                    getPetrinet().getRoles().add(orgUnitModel);
                                    roleListModel.addElement((ResourceClassModel) orgUnitModel);
                                    int index4 = getOrgUnitList().getSelectedIndex();
                                    orgUnitListModel.remove(index4);
                                }

                            } else
                            {
                                String oldName = getRoleList().getSelectedValue().toString();
                                String newName = getResourceClassNameTextField().getText();
                                ResourceClassModel roleModel = (ResourceClassModel) getPetrinet().getRoles().get(getPetrinet().containsRole(oldName));
                                // name changed
                                if (!oldName.equals(newName))
                                {
                                    roleModel.setName(newName);
                                    getPetrinet().replaceResourceClassMapping(oldName, newName);
                                    int index3 = getRoleList().getSelectedIndex();
                                    roleListModel.set(index3, roleModel);
                                }
                                // name unchanged
                                if (resourceClassTypeJComboBox.getSelectedItem() == COMBOBOX_ORGUNIT_TEXT)
                                {
                                    roleModel.setType(ResourceClassModel.TYPE_ORGUNIT);
                                    getPetrinet().getRoles().remove(roleModel);
                                    getPetrinet().getOrganizationUnits().add(roleModel);
                                    orgUnitListModel.addElement((ResourceClassModel) roleModel);
                                    int index2 = getRoleList().getSelectedIndex();
                                    roleListModel.remove(index2);
                                }
                            }
                            // getOrgUnitList().repaint();
                            // getRoleList().repaint();
                            getResourceClassNameTextField().setText("");
                            getOrgUnitList().clearSelection();
                            getRoleList().clearSelection();
                        }
                    }
                }
            });
        }
        return resourceClassEditButton;
    }

    private JButton getResourceClassRemoveButton()
    {
        if (resourceClassRemoveButton == null)
        {
            resourceClassRemoveButton = new JButton();
            resourceClassRemoveButton.setText(BUTTON_REMOVE_TEXT);
            resourceClassRemoveButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (!(roleList.isSelectionEmpty() && orgUnitList.isSelectionEmpty()))
                    {
                        if (roleList.isSelectionEmpty())
                        {
                            String orgUnit2remove = getOrgUnitList().getSelectedValue().toString();
                            int j = getPetrinet().containsOrgunit(orgUnit2remove);
                            getPetrinet().getOrganizationUnits().remove(j);
                            int index = getOrgUnitList().getSelectedIndex();
                            orgUnitListModel.remove(index);
                            int size = orgUnitListModel.getSize();
                            if (size == 0)
                            { // Nobody's left, disable button.
                                resourceClassRemoveButton.setEnabled(false);

                            } else
                            { // Select an index.
                                if (index == orgUnitListModel.getSize())
                                {
                                    // removed item in last position
                                    index--;
                                }
                                getOrgUnitList().setSelectedIndex(index);
                                getOrgUnitList().ensureIndexIsVisible(index);
                            }
                            // Mapping löschen
                            getPetrinet().getResourceMapping().remove(orgUnit2remove);
                            getResourceClassNameTextField().setText("");

                        } else
                        {
                            String role2remove = getRoleList().getSelectedValue().toString();
                            int j = getPetrinet().containsRole(role2remove);
                            getPetrinet().getRoles().remove(j);
                            int index2 = getRoleList().getSelectedIndex();
                            roleListModel.remove(index2);
                            int size2 = roleListModel.getSize();
                            if (size2 == 0)
                            { // Nobody's left, disable button.
                                resourceClassRemoveButton.setEnabled(false);

                            } else
                            { // Select an index.
                                if (index2 == roleListModel.getSize())
                                {
                                    // removed item in last position
                                    index2--;
                                }
                                getRoleList().setSelectedIndex(index2);
                                getRoleList().ensureIndexIsVisible(index2);
                            }

                            // Mapping löschen
                            getPetrinet().getResourceMapping().remove(role2remove);
                            getResourceClassNameTextField().setText("");
                        }
                    }
                }
            });
        }
        return resourceClassRemoveButton;
    }

    // ***************************************************Resources*************************************************************
    private JSplitPane getResourcePanel()
    {
        JScrollPane listScrollPane2 = new JScrollPane(getResourceList());

        // create resourceRightPanel and put it in Scrollpanel
        resourceRightPanel = new JPanel();
        resourceRightPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        resourceRightPanel.add(getResourceEditPanel(), c);

        c.gridx = 1;
        c.gridy = 0;
        resourceRightPanel.add(getResourceAssignPanel(), c);

        if (resourceSplittPane == null)
        {
            resourceSplittPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane2, resourceRightPanel);
            resourceSplittPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("PetriNet.Resources.Resource")), BorderFactory.createEmptyBorder(5, 5,
                    10, 5)));
            resourceSplittPane.setMinimumSize(new Dimension(800, 250));
            resourceSplittPane.setPreferredSize(new Dimension(800, 250));
            resourceSplittPane.setDividerLocation(200);
        }

        return resourceSplittPane;
    }

    private JList getResourceList()
    {
        if (resourceList == null)
        {
            resourceListModel = new DefaultListModel();
            for (int i = 0; i < getPetrinet().getResources().size(); i++)
            {
                resourceListModel.addElement((ResourceModel) getPetrinet().getResources().get(i));
            }

            resourceList = new JList(resourceListModel);
            resourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            resourceList.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e)
                {
                    // System.out.println(resourceList.getSelectedIndex());
                    unassignedListModel.removeAllElements();
                    assignedListModel.removeAllElements();
                    if (!e.getValueIsAdjusting() && resourceList.getSelectedIndex() > -1)
                    {
                        getResourceEditNameTextField().setText(resourceList.getModel().getElementAt(resourceList.getSelectedIndex()).toString());
                        // 1 & 2
                        for (Iterator iter = getPetrinet().getOrganizationUnits().iterator(); iter.hasNext();)
                        {
                            unassignedListModel.addElement(iter.next().toString());
                        }
                        for (Iterator iter = getPetrinet().getRoles().iterator(); iter.hasNext();)
                        {
                            unassignedListModel.addElement(iter.next().toString());
                        }
                        // 3
                        currentResource = (ResourceModel) resourceList.getModel().getElementAt(resourceList.getSelectedIndex());
                        Vector assignedClasses = getPetrinet().getResourceClassesResourceIsAssignedTo(currentResource.toString());
                        // 4 & 5
                        Object ass;
                        for (Iterator iter = assignedClasses.iterator(); iter.hasNext();)
                        {
                            ass = iter.next();
                            assignedListModel.addElement(ass);
                            if (unassignedListModel.contains(ass)) unassignedListModel.removeElement(ass);
                        }
                        /*
                         * 
                         * assignedListModel.removeAllElements();
                         * unassignedListModel.removeAllElements();
                         * getResourceEditNameTextField().setText(resourceList.getModel().getElementAt(resourceList.getSelectedIndex()).toString());
                         * currentResource = (ResourceModel)
                         * resourceList.getModel().getElementAt(resourceList.getSelectedIndex());
                         * if
                         * (getPetrinet().getResourceClassesResourceIsAssignedTo(currentResource.toString()).isEmpty()) {
                         * for (int k = 0; k <
                         * getPetrinet().getResourceClassesResourceIsNotAssignedTo(currentResource.toString()).size();
                         * k++) {
                         * 
                         * unassignedListModel.addElement(((ResourceClassModel)
                         * getPetrinet().getResourceClassesResourceIsNotAssignedTo(currentResource.toString()).get(k))); } }
                         * else { for (int j = 0; j <
                         * getPetrinet().getResourceClassesResourceIsAssignedTo(currentResource.toString()).size();
                         * j++) { assignedListModel.addElement(((String)
                         * getPetrinet().getResourceClassesResourceIsAssignedTo(currentResource.toString()).get(j))); }
                         * for (int i = 0; i <
                         * getPetrinet().getResourceClassesResourceIsNotAssignedTo(currentResource.toString()).size();
                         * i++) {
                         * 
                         * unassignedListModel.addElement(((ResourceClassModel)
                         * getPetrinet().getResourceClassesResourceIsNotAssignedTo(currentResource.toString()).get(i))); } }
                         */
                    }

                }
            });
        }
        return resourceList;
    }

    private JPanel getResourceEditPanel()
    {
        if (resourceEditPanel == null)
        {
            resourceEditPanel = new JPanel();
            resourceEditPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;

            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 20, 0, 0);
            resourceEditPanel.add(getResourceEditNameLabel(), c);

            c.gridx = 1;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 0);
            resourceEditPanel.add(getResourceEditNameTextField(), c);

            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(0, 20, 0, 0);
            resourceEditPanel.add(getResourceEditDescriptionLabel(), c);

            c.gridx = 1;
            c.gridy = 1;
            c.insets = new Insets(0, 0, 0, 0);
            resourceEditPanel.add(getResourceEditDescriptionTextField(), c);

            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = 2;
            resourceEditPanel.add(getResourceButtonPanel(), c);
        }
        return resourceEditPanel;
    }

    private JPanel getResourceAssignPanel()
    {
        if (resourceAssignPanel == null)
        {
            resourceAssignPanel = new JPanel();
            resourceAssignPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            resourceAssignPanel.add(getResourceUnAssignedLabel(), c);

            c.gridx = 1;
            c.gridy = 0;
            resourceAssignPanel.add(getResourceAssignedLabel(), c);

            c.gridx = 0;
            c.gridy = 1;
            resourceAssignPanel.add(getResourceUnAssignedScrollPane(), c);

            c.gridx = 1;
            c.gridy = 1;
            resourceAssignPanel.add(getResourceAssignedScrollPane(), c);

            c.gridx = 0;
            c.gridy = 2;
            c.insets = new Insets(5, 0, 0, 0);
            resourceAssignPanel.add(getResourceAssignToButton(), c);

            c.gridx = 1;
            c.gridy = 2;
            resourceAssignPanel.add(getResourceUnAssignButton(), c);

        }
        return resourceAssignPanel;
    }

    private JLabel getResourceEditNameLabel()
    {
        if (resourceEditNameLabel == null)
        {
            resourceEditNameLabel = new JLabel(Messages.getString("PetriNet.Resources.ResourceName") + ":");
        }

        return resourceEditNameLabel;
    }

    private JLabel getResourceEditDescriptionLabel()
    {
        if (resourceEditDescriptionLabel == null)
        {
            resourceEditDescriptionLabel = new JLabel(Messages.getString("PetriNet.Resources.ResourceDescription") + ":");
        }

        return resourceEditDescriptionLabel;
    }

    private JTextField getResourceEditNameTextField()
    {
        if (resourceEditNameTextField == null)
        {
            resourceEditNameTextField = new JTextField();
            resourceEditNameTextField.setPreferredSize(new Dimension(100, 20));
            resourceEditNameTextField.setMinimumSize(new Dimension(100, 20));
            resourceEditNameTextField.setEditable(false);
        }
        return resourceEditNameTextField;
    }

    private JTextField getResourceEditDescriptionTextField()
    {
        if (resourceEditDescriptionTextField == null)
        {
            resourceEditDescriptionTextField = new JTextField();
            resourceEditDescriptionTextField.setPreferredSize(new Dimension(100, 20));
            resourceEditDescriptionTextField.setMinimumSize(new Dimension(100, 20));
            resourceEditDescriptionTextField.setEditable(false);
        }
        return resourceEditDescriptionTextField;
    }

    private JPanel getResourceButtonPanel()
    {
        if (resourceButtonPanel == null)
        {
            resourceButtonPanel = new JPanel();
            resourceButtonPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.weightx = 1;
            c.weighty = 0;
            c.gridx = 0;
            c.gridy = 0;
            resourceButtonPanel.add(getResourceNewButton(), c);

            c.gridx = 1;
            c.gridy = 0;
            resourceButtonPanel.add(getResourceEditButton(), c);

            c.gridx = 2;
            c.gridy = 0;
            resourceButtonPanel.add(getResourceRemoveButton(), c);
        }

        return resourceButtonPanel;
    }

    private JButton getResourceNewButton()
    {
        if (resourceAddButton == null)
        {
            resourceAddButton = new JButton(BUTTON_NEW_TEXT);
            resourceAddButton.setMinimumSize(new Dimension(80, 23));
            resourceAddButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (resourceAddButton.getText().equals(BUTTON_NEW_TEXT))
                    {
                        getResourceEditNameTextField().setEditable(true);
                        getResourceEditDescriptionTextField().setText("");
                        getResourceEditNameTextField().setText("");
                        getResourceEditButton().setEnabled(false);
                        getResourceRemoveButton().setEnabled(false);
                        resourceAddButton.setText(BUTTON_OK_TEXT);
                        getResourceList().setEnabled(false);
                    } else if (resourceAddButton.getText().equals(BUTTON_OK_TEXT) && getResourceEditNameTextField().getText().length() > 0)
                    {
                        getResourceEditNameTextField().setEditable(false);
                        getResourceEditButton().setEnabled(true);
                        getResourceRemoveButton().setEnabled(true);
                        ResourceModel newResource = new ResourceModel(getResourceEditNameTextField().getText());
                        getPetrinet().addResource(newResource);
                        resourceListModel.addElement((ResourceModel) newResource);
                        resourceAddButton.setText(BUTTON_NEW_TEXT);
                        getResourceList().setEnabled(true);
                        getResourceList().clearSelection();
                        getResourceEditNameTextField().setText("");

                    }
                }
            });

        }
        return resourceAddButton;
    }

    private JButton getResourceEditButton()
    {
        if (resourceEditButton == null)
        {
            resourceEditButton = new JButton(BUTTON_EDIT_TEXT);
            resourceEditButton.setMinimumSize(new Dimension(80, 23));
            resourceEditButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (!resourceList.isSelectionEmpty())
                    {
                        if (resourceEditButton.getText().equals(BUTTON_EDIT_TEXT))
                        {
                            getResourceEditNameTextField().setEditable(true);
                            getResourceNewButton().setEnabled(false);
                            getResourceRemoveButton().setEnabled(false);
                            resourceEditButton.setText(BUTTON_OK_TEXT);
                            getResourceList().setEnabled(false);
                        } else if (resourceEditButton.getText().equals(BUTTON_OK_TEXT))
                        {
                            getResourceEditNameTextField().setEditable(false);
                            getResourceNewButton().setEnabled(true);
                            getResourceRemoveButton().setEnabled(true);
                            resourceEditButton.setText(BUTTON_EDIT_TEXT);
                            getResourceList().setEnabled(true);
                            String oldName = getResourceList().getSelectedValue().toString();
                            String newName = getResourceEditNameTextField().getText();
                            if (!oldName.equals(newName))
                            {
                                getPetrinet().replaceResourceMapping(oldName, newName);
                                ResourceModel resourceModel = (ResourceModel) getPetrinet().getResources().get(getPetrinet().containsResource(oldName));
                                resourceModel.setName(newName);
                                int index = resourceList.getSelectedIndex();
                                resourceListModel.set(index, resourceModel);
                            }

                        }
                    }
                }
            });

        }
        return resourceEditButton;
    }

    private JButton getResourceRemoveButton()
    {
        if (resourceRemoveButton == null)
        {
            resourceRemoveButton = new JButton(BUTTON_REMOVE_TEXT);
            resourceRemoveButton.setMinimumSize(new Dimension(80, 23));
            resourceRemoveButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (!resourceList.isSelectionEmpty())
                    {
                        String re2remove = getResourceList().getSelectedValue().toString();
                        int j = getPetrinet().containsResource(re2remove);
                        getPetrinet().getResources().remove(j);
                        int index = getResourceList().getSelectedIndex();
                        resourceListModel.remove(index);
                        int size = resourceListModel.getSize();
                        if (size == 0)
                        { // Nobody's left, disable button.
                            resourceRemoveButton.setEnabled(false);

                        } else
                        { // Select an index.
                            if (index == resourceListModel.getSize())
                            {
                                // removed item in last position
                                index--;
                            }
                            getResourceList().setSelectedIndex(index);
                            getResourceList().ensureIndexIsVisible(index);
                        }
                        // getResourceList().repaint();
                        resourceEditNameTextField.setText("");
                        // Mapping löschen
                        for (Iterator iter = getPetrinet().getResourceMapping().keySet().iterator(); iter.hasNext();)
                        {
                            Vector resourcevalues = (Vector) getPetrinet().getResourceMapping().get(iter.next());
                            for (int i = 0; i < resourcevalues.size(); i++)
                            {
                                if (resourcevalues.get(i).equals(re2remove)) resourcevalues.remove(i);
                            }
                        }
                    }
                }
            });

        }
        return resourceRemoveButton;
    }

    private JLabel getResourceUnAssignedLabel()
    {
        if (resourceUnAssignedLabel == null)
        {
            resourceUnAssignedLabel = new JLabel(Messages.getString("PetriNet.Resources.Resource.Unassigned") + ":");
        }

        return resourceUnAssignedLabel;
    }

    private JLabel getResourceAssignedLabel()
    {
        if (resourceAssignedLabel == null)
        {
            resourceAssignedLabel = new JLabel(Messages.getString("PetriNet.Resources.Resource.Assigned") + ":");
        }

        return resourceAssignedLabel;
    }

    private JScrollPane getResourceUnAssignedScrollPane()
    {

        JScrollPane listScrollPane3 = new JScrollPane(getResourceUnAssignedList());
        listScrollPane3.setMinimumSize(new Dimension(110, 130));
        listScrollPane3.setPreferredSize(new Dimension(110, 130));

        return listScrollPane3;

    }

    private JScrollPane getResourceAssignedScrollPane()
    {
        JScrollPane listScrollPane4 = new JScrollPane(getResourceAssignedList());
        listScrollPane4.setMinimumSize(new Dimension(110, 130));
        listScrollPane4.setPreferredSize(new Dimension(110, 130));

        return listScrollPane4;

    }

    private JList getResourceAssignedList()
    {
        if (resourceAssignedList == null)
        {
            assignedListModel = new DefaultListModel();

            resourceAssignedList = new JList(assignedListModel);
            resourceAssignedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            resourceAssignedList.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e)
                {
                    if (!e.getValueIsAdjusting() && resourceAssignedList.getSelectedIndex() > -1)
                    {
                        String tempResourceClassName = (String) resourceAssignedList.getModel().getElementAt(resourceAssignedList.getSelectedIndex());
                        int index;
                        if (getPetrinet().containsOrgunit(tempResourceClassName) == -1)
                        {
                            index = getPetrinet().containsRole(tempResourceClassName);
                            currentAssignedResourceClass = getPetrinet().getRoles().get(index).toString();
                        } else
                        {
                            index = getPetrinet().containsOrgunit(tempResourceClassName);
                            currentAssignedResourceClass = getPetrinet().getOrganizationUnits().get(index).toString();
                        }
                        // currentAssignedResourceClass = (ResourceClassModel)
                        // resourceAssignedList.getModel().getElementAt(resourceAssignedList.getSelectedIndex());
                        getResourceUnAssignedList().clearSelection();
                    }

                }
            });

        }
        return resourceAssignedList;
    }

    private JList getResourceUnAssignedList()
    {
        if (resourceUnAssignedList == null)
        {
            unassignedListModel = new DefaultListModel();
            resourceUnAssignedList = new JList(unassignedListModel);
            resourceUnAssignedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            resourceUnAssignedList.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e)
                {
                    if (!e.getValueIsAdjusting() && resourceUnAssignedList.getSelectedIndex() > -1)
                    {

                        currentUnAssignedResourceClass = resourceUnAssignedList.getModel().getElementAt(resourceUnAssignedList.getSelectedIndex()).toString();
                        getResourceAssignedList().clearSelection();
                    }

                }
            });
        }
        return resourceUnAssignedList;
    }

    private JButton getResourceAssignToButton()
    {
        if (resourceAssignToButton == null)
        {
            resourceAssignToButton = new JButton();
            resourceAssignToButton.setMinimumSize(new Dimension(75, 20));
            resourceAssignToButton.setText(BUTTON_ASSIGN_TEXT);
            resourceAssignToButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (getResourceUnAssignedList().getSelectedIndex() > -1)
                    {
                        getPetrinet().addResourceMapping(currentUnAssignedResourceClass, currentResource.getName());
                        assignedListModel.addElement(currentUnAssignedResourceClass);
                        // assignedListModel.addElement((ResourceClassModel)
                        // currentUnAssignedResourceClass);
                        unassignedListModel.removeElement(currentUnAssignedResourceClass);
                    }
                }
            });

        }
        return resourceAssignToButton;
    }

    private JButton getResourceUnAssignButton()
    {
        if (resourceUnAssignButton == null)
        {
            resourceUnAssignButton = new JButton();
            resourceUnAssignButton.setMinimumSize(new Dimension(75, 20));
            resourceUnAssignButton.setText(BUTTON_UNASSIGN_TEXT);
            resourceUnAssignButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (getResourceAssignedList().getSelectedIndex() > -1)
                    {
                        getPetrinet().removeResourceMapping(currentAssignedResourceClass, currentResource.getName());
                        unassignedListModel.addElement(currentAssignedResourceClass);
                        assignedListModel.removeElement((String) currentAssignedResourceClass);
                    }
                }

            });

        }

        return resourceUnAssignButton;
    }

    //
    // //
    // public static void main(String[] args)
    // //
    // {
    // //
    // PetriNetPropertyEditor p = new PetriNetPropertyEditor();
    // //
    // JFrame f = new JFrame();
    // //
    //
    // //
    // f.getContentPane().add(p);
    // //
    // f.setSize(700, 700);
    // //
    // f.setVisible(true);
    // //
    // }

    public void valueChanged(ListSelectionEvent e)
    {
        ListSelectionModel lsm = (ListSelectionModel) e.getSource();

        // boolean isAdjusting = e.getValueIsAdjusting();
        resourceClassNameTextField.setText(lsm.toString());

    }

    /**
     * @return Returns the editor.
     */
    public EditorVC getEditor()
    {
        return editor;
    }

}