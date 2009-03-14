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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.PetriNetModelProcessor;
import org.woped.core.model.petrinet.AbstractPetriNetModelElement;
import org.woped.core.model.petrinet.ResourceClassModel;
import org.woped.core.model.petrinet.ResourceModel;
import org.woped.core.model.petrinet.TransitionModel;
import org.woped.core.utilities.SwingUtils;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.translations.Messages;

/**
 * @author waschtl
 * 
 * TODO: DOCUMENTATION (waschtl) 
 * TODO: implement as VC (simon)
 */

@SuppressWarnings("serial")
public class PetriNetResourceEditorOld extends JPanel implements ListSelectionListener
{
    // Resource classes
    private JPanel                 resourceClassPanel                = null;

    private JSplitPane             resourceClassSplitPane            = null;
    private JPanel                 resourceClassRolesPanel           = null;
    private JPanel                 resourceClassGroupsPanel          = null;
    private JLabel                 resourceClassRolesLabel           = null;
    private JLabel                 resourceClassGroupsLabel          = null;
    private JScrollPane            resourceClassRolesScrollPane      = null;
    private JScrollPane            resourceClassGroupsScrollPane     = null;

    private JPanel                 resourceClassButtonPanel          = null;
    private JButton                resourceClassRemoveButton         = null;
    private JButton                resourceClassNewButton            = null;
    private JButton                resourceClassOkButton             = null;

    private JPanel                 resourceClassEditLabelPanel       = null;
    private JLabel                 resourceClassNameLabel            = null;
    private JLabel                 resourceClassDescriptionLabel     = null;
    private JLabel                 resourceClassTypeLabel            = null;

    private JPanel                 resourceClassEditFieldPanel       = null;
    private JTextField             resourceClassNameTextField        = null;
    private JTextField             resourceClassDescriptionTextField = null;
    private JComboBox              resourceClassTypeJComboBox        = null;

    private JList                  roleList                          = null;
    private JList                  groupList                         = null;
    private DefaultListModel       groupListModel                    = null;
    private DefaultListModel       roleListModel                     = null;

    // Resources
    private JPanel                 resourcePanel                     = null;
    private JList                  resourceList                      = null;
    private JLabel                 resourceListLabel                 = null;
    private JPanel                 resourceListPanel                 = null;
    private JPanel                 resourcePropertiesPanel           = null;
    private JScrollPane            resourceListScrollPane            = null;
    private JPanel                 resourceEditPanel                 = null;
    private JLabel                 resourceEditNameLabel             = null;
    private JLabel                 resourceEditDescriptionLabel      = null;
    private JTextField             resourceEditNameTextField         = null;
    private JTextField             resourceEditDescriptionTextField  = null;
    private JPanel                 resourceButtonPanel               = null;
    private JButton                resourceNewButton                 = null;
    private JButton                resourceRemoveButton              = null;
    private JPanel                 resourceAssignPanel               = null;
    private JList                  resourceAssignedList              = null;
    private JList                  resourceUnAssignedList            = null;
    private JLabel                 resourceAssignedLabel             = null;
    private JLabel                 resourceUnAssignedLabel           = null;
    private JButton                resourceAssignToButton            = null;
    private JButton                resourceUnAssignButton            = null;
    private JButton                resourceOkButton                  = null;
    private ResourceModel          currentResource                   = null;
    private DefaultListModel       resourceListModel                 = null;
    private String                 currentUnAssignedResourceClass    = null;
    private String                 currentAssignedResourceClass      = null;
    private DefaultListModel       assignedListModel                 = null;
    private DefaultListModel       unassignedListModel               = null;

    private static final String    COMBOBOX_ROLE_TEXT                = Messages.getString("PetriNet.Resources.Role");
    private static final String    COMBOBOX_GROUP_TEXT               = Messages.getString("PetriNet.Resources.Group");
    private static final Object[]  ResourceClassTypeA                = { COMBOBOX_ROLE_TEXT, COMBOBOX_GROUP_TEXT };

    private PetriNetModelProcessor petrinet;
    private EditorVC               editor;

    public EditorVC getEditor()
    {
        return editor;
    }

    public PetriNetModelProcessor getPetrinet()
    {
        return petrinet;
    }

    public PetriNetResourceEditorOld(EditorVC editor)
    {
        this.editor = editor;
        this.petrinet = (PetriNetModelProcessor) editor.getModelProcessor();
        initialize();
    }

    private void initialize()
    {
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        this.setMaximumSize(new Dimension(720, 520));
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        this.add(getResourceClassPanel(), c);

        c.gridx = 0;
        c.gridy = 1;
        this.add(getResourcePanel(), c);
    }

    public void reset()
    {
        resetClassEditing();
        resetEditing();
        refreshFromModel(); 
    }
    
    //! This rather important method will refresh the resource editor view
    //! if the resources have changed in the Petri-Net model
    //! It is called when the resource editor gains focus    
    private void refreshFromModel()
    {
    	refreshRoleListFromModel();
    	refreshGroupListFromModel();
    	refreshResourceListFromModel();
    }

    private JPanel getResourceClassPanel()
    {
        if (resourceClassPanel == null)
        {
            resourceClassPanel = new JPanel();
            resourceClassPanel.setBorder(BorderFactory
                    .createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("PetriNet.Resources.ResourceClass")), BorderFactory.createEmptyBorder()));
            SwingUtils.setFixedSize(resourceClassPanel, 750, 200);
            resourceClassPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 3;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 10, 0, 0);
            resourceClassPanel.add(getResourceClassSplitPane(), c);

            c.gridx = 3;
            c.gridy = 0;
            c.gridwidth = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, 30, 0, 30);
            resourceClassPanel.add(getResourceClassButtonPanel(), c);

            c.gridx = 4;
            c.gridy = 0;
            c.gridwidth = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, 0, 0, 0);
            resourceClassPanel.add(getResourceClassEditLabelPanel(), c);

            c.gridx = 5;
            c.gridy = 0;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, 0, 0, 10);
            resourceClassPanel.add(getResourceClassEditFieldPanel(), c);
        }

        return resourceClassPanel;
    }

    private JLabel getResourceClassRolesLabel()
    {
        if (resourceClassRolesLabel == null)
        {
            resourceClassRolesLabel = new JLabel(Messages.getString("PetriNet.Resources.Roles") + ":");
            SwingUtils.setFixedWidth(resourceClassRolesLabel, 80);
        }

        return resourceClassRolesLabel;
    }

    private JLabel getResourceClassGroupsLabel()
    {
        if (resourceClassGroupsLabel == null)
        {
            resourceClassGroupsLabel = new JLabel(Messages.getString("PetriNet.Resources.Groups") + ":");
            SwingUtils.setFixedWidth(resourceClassGroupsLabel, 80);
        }

        return resourceClassGroupsLabel;
    }

    private JSplitPane getResourceClassSplitPane()
    {
        if (resourceClassSplitPane == null)
        {
            resourceClassSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getResourceClassRolesPanel(), getResourceClassGroupsPanel());
            resourceClassSplitPane.setDividerLocation(0.5);
            BasicSplitPaneUI ui = (BasicSplitPaneUI) resourceClassSplitPane.getUI();
            ui.getDivider().setBorder(BorderFactory.createEmptyBorder());
            resourceClassSplitPane.setBorder(BorderFactory.createEmptyBorder());
        }

        return resourceClassSplitPane;
    }

    private JPanel getResourceClassRolesPanel()
    {
        if (resourceClassRolesPanel == null)
        {
            resourceClassRolesPanel = new JPanel();
            resourceClassRolesPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            resourceClassRolesPanel.add(getResourceClassRolesLabel(), c);

            c.gridx = 1;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            resourceClassRolesPanel.add(getResourceClassRolesScrollPane(), c);
        }

        return resourceClassRolesPanel;
    }

    private JPanel getResourceClassGroupsPanel()
    {
        if (resourceClassGroupsPanel == null)
        {
            resourceClassGroupsPanel = new JPanel();
            resourceClassGroupsPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            resourceClassGroupsPanel.add(getResourceClassGroupsLabel(), c);

            c.gridx = 1;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            resourceClassGroupsPanel.add(getResourceClassGroupsScrollPane(), c);
        }

        return resourceClassGroupsPanel;
    }

    private JScrollPane getResourceClassRolesScrollPane()
    {
        if (resourceClassRolesScrollPane == null)
        {
            resourceClassRolesScrollPane = new JScrollPane(getRoleList());
            resourceClassRolesScrollPane.setPreferredSize(new Dimension(150, 80));
            resourceClassRolesScrollPane.setMinimumSize(new Dimension(150, 20));
            resourceClassRolesScrollPane.setMaximumSize(new Dimension(150, 20));
        }

        return resourceClassRolesScrollPane;
    }

    private JScrollPane getResourceClassGroupsScrollPane()
    {
        if (resourceClassGroupsScrollPane == null)
        {
            resourceClassGroupsScrollPane = new JScrollPane(getGroupList());
            resourceClassGroupsScrollPane.setPreferredSize(new Dimension(150, 80));
            resourceClassGroupsScrollPane.setMinimumSize(new Dimension(150, 20));
            resourceClassGroupsScrollPane.setMaximumSize(new Dimension(150, 20));
        }

        return resourceClassGroupsScrollPane;
    }

    private void refreshRoleListFromModel()
    {
    	if (roleListModel == null)
    		return;
    		
    	roleListModel.clear();
        for (int i = 0; i < getPetrinet().getRoles().size(); i++)
        {
            roleListModel.addElement((ResourceClassModel) getPetrinet().getRoles().get(i));
        }
    }
    
    private JList getRoleList()
    {
        if (roleList == null)
        {
            roleListModel = new DefaultListModel();
            roleList = new JList(roleListModel);
            roleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            refreshRoleListFromModel();

            roleList.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e)
                {
                    if (!e.getValueIsAdjusting() && roleList.getSelectedIndex() > -1)
                    {
                        getResourceClassRemoveButton().setEnabled(true);
                        editResourceClass();
                        getResourceClassNameTextField().setText(roleList.getModel().getElementAt(roleList.getSelectedIndex()).toString());
                        resourceClassTypeJComboBox.setSelectedItem(COMBOBOX_ROLE_TEXT);
                    }
                    getGroupList().clearSelection();
//                    getResourceList().clearSelection();
                }
            });
        }

        return roleList;
    }
    
    private void refreshGroupListFromModel()
    {
    	if (groupListModel == null)
    		return;
    	
    	groupListModel.clear();
//    	System.out.println(getPetrinet().getOrganizationUnits().size());
        for (int i = 0; i < getPetrinet().getOrganizationUnits().size(); i++)
        {
            groupListModel.addElement((ResourceClassModel) getPetrinet().getOrganizationUnits().get(i));
        }    	
    }

    private JList getGroupList()
    {
        if (groupList == null)
        {
            groupListModel = new DefaultListModel();

            groupList = new JList(groupListModel);
            groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            refreshGroupListFromModel();

            groupList.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e)
                {
                    if (!e.getValueIsAdjusting() && groupList.getSelectedIndex() > -1)
                    {
                        getResourceClassRemoveButton().setEnabled(true);
                        editResourceClass();
                        getResourceClassNameTextField().setText(groupList.getModel().getElementAt(groupList.getSelectedIndex()).toString());
                        resourceClassTypeJComboBox.setSelectedItem(COMBOBOX_GROUP_TEXT);
                    }
                    getRoleList().clearSelection();
 //                   getResourceList().clearSelection();
                }
            });
        }

        return groupList;
    }

    private JPanel getResourceClassButtonPanel()
    {
        if (resourceClassButtonPanel == null)
        {
            resourceClassButtonPanel = new JPanel();
            resourceClassButtonPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(10, 0, 10, 0);
            resourceClassButtonPanel.add(getResourceClassNewButton(), c);
            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(0, 0, 0, 0);
            resourceClassButtonPanel.add(getResourceClassOkButton(), c);
            c.gridx = 0;
            c.gridy = 2;
            c.insets = new Insets(10, 0, 10, 0);
            resourceClassButtonPanel.add(getResourceClassRemoveButton(), c);
        }

        return resourceClassButtonPanel;
    }

    private JButton getResourceClassNewButton()
    {
        if (resourceClassNewButton == null)
        {
            resourceClassNewButton = new JButton();
            resourceClassNewButton.setIcon(Messages.getImageIcon("PetriNet.Resources.New"));
            resourceClassNewButton.setText(Messages.getString("PetriNet.Resources.New.Title"));
            resourceClassNewButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    editNewResourceClass();
                }
            });
        }

        return resourceClassNewButton;
    }

    private JButton getResourceClassOkButton()
    {
        if (resourceClassOkButton == null)
        {
            resourceClassOkButton = new JButton();
            resourceClassOkButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Ok"));
            resourceClassOkButton.setText(Messages.getString("PetriNet.Resources.Ok.Title"));
 
            resourceClassOkButton.setEnabled(false);
            resourceClassOkButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    addResourceClass();
                }
            });
        }

        return resourceClassOkButton;
    }

    private JButton getResourceClassRemoveButton()
    {
        if (resourceClassRemoveButton == null)
        {
            resourceClassRemoveButton = new JButton();
            resourceClassRemoveButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
            resourceClassRemoveButton.setText(Messages.getString("PetriNet.Resources.Delete.Title"));
            
            resourceClassRemoveButton.setEnabled(false);
            resourceClassRemoveButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (!(roleList.isSelectionEmpty() && groupList.isSelectionEmpty()))
                    {
                        removeResourceClass();
                    }
                }
            });
        }

        return resourceClassRemoveButton;
    }

    private JPanel getResourceClassEditLabelPanel()
    {
        if (resourceClassEditLabelPanel == null)
        {
            resourceClassEditLabelPanel = new JPanel();
            resourceClassEditLabelPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 20);
            c.anchor = GridBagConstraints.WEST;
            resourceClassEditLabelPanel.add(getResourceClassNameLabel(), c);

            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(25, 0, 25, 20);
            resourceClassEditLabelPanel.add(getResourceClassDescriptionLabel(), c);

            c.gridx = 0;
            c.gridy = 2;
            c.insets = new Insets(0, 0, 0, 20);
            resourceClassEditLabelPanel.add(getResourceClassTypeLabel(), c);
        }

        return resourceClassEditLabelPanel;
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

    private JPanel getResourceClassEditFieldPanel()
    {
        if (resourceClassEditFieldPanel == null)
        {
            resourceClassEditFieldPanel = new JPanel();
            resourceClassEditFieldPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.weightx = 1;
            c.weighty = 1;
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 0);
            resourceClassEditFieldPanel.add(getResourceClassNameTextField(), c);

            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(20, 0, 20, 0);
            resourceClassEditFieldPanel.add(getResourceClassDescriptionTextField(), c);

            c.gridx = 0;
            c.gridy = 2;
            c.insets = new Insets(0, 0, 0, 0);
            resourceClassEditFieldPanel.add(getResourceClassTypeJComboBox(), c);
        }

        return resourceClassEditFieldPanel;
    }

    private JTextField getResourceClassNameTextField()
    {
        if (resourceClassNameTextField == null)
        {
            resourceClassNameTextField = new JTextField();
            SwingUtils.setFixedWidth(resourceClassNameTextField, 200);
            resourceClassNameTextField.setEditable(false);
            resourceClassNameTextField.addKeyListener(new KeyListener()
            {
                public void keyPressed(KeyEvent e)
                {
                    keyReleased(e);
                }

                public void keyTyped(KeyEvent e)
                {
                    keyReleased(e);
                }

                public void keyReleased(KeyEvent e)
                {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        addResourceClass();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    {
                        resetClassEditing();
                    }
                }
            });
        }

        return resourceClassNameTextField;
    }

    private JTextField getResourceClassDescriptionTextField()
    {
        if (resourceClassDescriptionTextField == null)
        {
            resourceClassDescriptionTextField = new JTextField();
            SwingUtils.setFixedWidth(resourceClassDescriptionTextField, 200);
            resourceClassDescriptionTextField.setEditable(false);
        }

        return resourceClassDescriptionTextField;
    }

    private JComboBox getResourceClassTypeJComboBox()
    {
        if (resourceClassTypeJComboBox == null)
        {
            resourceClassTypeJComboBox = new JComboBox(ResourceClassTypeA);
            SwingUtils.setFixedWidth(resourceClassTypeJComboBox, 200);
            resourceClassTypeJComboBox.setEnabled(false);
        }

        return resourceClassTypeJComboBox;
    }

    private void editNewResourceClass()
    {
        getResourceClassNameTextField().setText("");
        editResourceClass();
        getRoleList().clearSelection();
        getGroupList().clearSelection();
        resourceClassRemoveButton.setEnabled(false);
        resourceClassNewButton.setEnabled(false);
        getRoleList().setEnabled(false);
        getGroupList().setEnabled(false);
        getResourceClassNameTextField().requestFocus();
    }

    private void editResourceClass()
    {
        resetEditing();
        getResourceClassOkButton().setEnabled(true);
        getResourceClassTypeJComboBox().setEnabled(true);
        getResourceClassNameTextField().setEditable(true);
        getResourceClassNameTextField().requestFocus();
    }

    private boolean checkClassSyntax(String str)
    {
        boolean nameExists = false;

        if (str.equals(""))
        {
            JOptionPane.showMessageDialog(this, Messages.getString("ResourceEditor.Error.EmptyResourceClass.Text"), Messages.getString("ResourceEditor.Error.EmptyResourceClass.Title"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        for (Iterator iter = getPetrinet().getOrganizationUnits().iterator(); !nameExists & iter.hasNext();)
        {
            if (iter.next().toString().equals(str)) 
                nameExists = true;
        }
        for (Iterator iter = getPetrinet().getRoles().iterator(); !nameExists & iter.hasNext();)
        {
            if (iter.next().toString().equals(str)) 
                nameExists = true;
        }

        if (nameExists)
        {
            JOptionPane.showMessageDialog(this, Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Text"), Messages.getString("ResourceEditor.Error.DuplicateResourceClass.Title"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void addResourceClass()
    {        
        // No existing class to be changed - add new
        if (roleList.isSelectionEmpty() && groupList.isSelectionEmpty())
        {
           String newName = getResourceClassNameTextField().getText();
           if (checkClassSyntax(newName))
           {
                // add new role
                if (resourceClassTypeJComboBox.getSelectedItem() == COMBOBOX_ROLE_TEXT)
                {
                    ResourceClassModel newRole = new ResourceClassModel(newName, ResourceClassModel.TYPE_ROLE);
                    getPetrinet().addRole(newRole);
                    roleListModel.addElement(newRole);
                } 
                // add new group
                else
                {
                    ResourceClassModel newGroup = new ResourceClassModel(newName, ResourceClassModel.TYPE_ORGUNIT);
                    getPetrinet().addOrgUnit(newGroup);
                    groupListModel.addElement(newGroup);         
                }
            }
        }

        // Role to be changed - edit
        if (!roleList.isSelectionEmpty())
        {
            String newName = getResourceClassNameTextField().getText();
            String oldName = getRoleList().getSelectedValue().toString();            
 
            if (!oldName.equals(newName) && !checkClassSyntax(newName))
               return;

            // change role into group 
            if (resourceClassTypeJComboBox.getSelectedItem() == COMBOBOX_GROUP_TEXT)
            {
                if (!roleIsUsed(oldName))
                {
                    ResourceClassModel roleModel = (ResourceClassModel) getPetrinet().getRoles().get(getPetrinet().containsRole(oldName));
                    getPetrinet().getRoles().remove(roleModel);
                    roleListModel.remove(getRoleList().getSelectedIndex());
                    
                    ResourceClassModel groupModel = new ResourceClassModel(newName, ResourceClassModel.TYPE_ORGUNIT);
                    getPetrinet().getOrganizationUnits().add(groupModel);
                    groupListModel.addElement(groupModel);
                }
                else
                {
                    JOptionPane.showMessageDialog(this, 
                            Messages.getString("ResourceEditor.Error.UnmovableResourceClass.Text"), 
                            Messages.getString("ResourceEditor.Error.UnmovableResourceClass.Title"),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            else
            {
                // change name of role
                if (!oldName.equals(newName))
                {
                    ResourceClassModel roleModel = (ResourceClassModel) getPetrinet().getRoles().get(getPetrinet().containsRole(oldName));
                    roleModel.setName(newName);
                    roleListModel.set(getRoleList().getSelectedIndex(), roleModel);
                    updateRolesInPetrinet(oldName, newName);
                }
            }
        }
        
        // Role to be changed - edit
        if (!groupList.isSelectionEmpty())
        {
            String newName = getResourceClassNameTextField().getText();
            String oldName = getGroupList().getSelectedValue().toString();

            if (!oldName.equals(newName) && !checkClassSyntax(newName))
                return;

            // change group into role 
            if (resourceClassTypeJComboBox.getSelectedItem() == COMBOBOX_ROLE_TEXT)
            {
                if (!groupIsUsed(oldName))
                {
                    ResourceClassModel groupModel = (ResourceClassModel) getPetrinet().getOrganizationUnits().get(getPetrinet().containsOrgunit(oldName));
                    getPetrinet().getOrganizationUnits().remove(groupModel);
                    groupListModel.remove(getGroupList().getSelectedIndex());
                                       
                    ResourceClassModel roleModel = new ResourceClassModel(newName, ResourceClassModel.TYPE_ROLE);
                    getPetrinet().getRoles().add(roleModel);
                    roleListModel.addElement(roleModel);
                }
                else
                {
                    JOptionPane.showMessageDialog(this, 
                        Messages.getString("ResourceEditor.Error.UnmovableResourceClass.Text"), 
                        Messages.getString("ResourceEditor.Error.UnmovableResourceClass.Title"),
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            else
            {
                // change name of group
                if (!oldName.equals(newName))
                {
                    ResourceClassModel groupModel = (ResourceClassModel) getPetrinet().getOrganizationUnits().get(getPetrinet().containsOrgunit(oldName));
                    groupModel.setName(newName);
                    groupListModel.set(getGroupList().getSelectedIndex(), groupModel);
                    updateGroupsInPetrinet(oldName, newName);
                } 
            }
        }
        
        resetClassEditing();
        getEditor().setSaved(false);
    }

    private void updateGroupsInPetrinet(String oldName, String newName)
    {
        HashMap<String, AbstractElementModel> alltrans = new HashMap<String, AbstractElementModel>();
        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE));
        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE));

        for (Iterator transIter = alltrans.values().iterator(); transIter.hasNext();)
        {
            TransitionModel transition = (TransitionModel)(transIter.next());
            if (transition.getToolSpecific() != null &&
                    transition.getToolSpecific().getTransResource() != null &&
                    transition.getToolSpecific().getTransResource().getTransOrgUnitName() != null &&
                    transition.getToolSpecific().getTransResource().getTransOrgUnitName().equals(oldName))
            {
                transition.getToolSpecific().getTransResource().setTransOrgUnitName(newName);
            }
        }   
        
        getPetrinet().replaceResourceClassMapping(oldName, newName);
    }

    private void updateRolesInPetrinet(String oldName, String newName)
    {
        HashMap<String, AbstractElementModel> alltrans = new HashMap<String, AbstractElementModel>();
        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE));
        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE));

        for (Iterator transIter = alltrans.values().iterator(); transIter.hasNext();)
        {
            TransitionModel transition = (TransitionModel)(transIter.next());
            if (transition.getToolSpecific() != null &&
                    transition.getToolSpecific().getTransResource() != null &&
                    transition.getToolSpecific().getTransResource().getTransRoleName() != null &&
                    transition.getToolSpecific().getTransResource().getTransRoleName().equals(oldName))
            {
                transition.getToolSpecific().getTransResource().setTransRoleName(newName);
            }
        }    
        
        getPetrinet().replaceResourceClassMapping(oldName, newName);
    }

    // Check if group is used by any transition
    private boolean groupIsUsed(String groupName)
    {
        boolean isUsed = false;
               
        HashMap<String, AbstractElementModel> alltrans = new HashMap<String, AbstractElementModel>();
        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE));
        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE));

        for (Iterator transIter = alltrans.values().iterator(); transIter.hasNext() & !isUsed;)
        {
            TransitionModel transition = (TransitionModel)(transIter.next());
            if (transition.getToolSpecific() != null &&
                    transition.getToolSpecific().getTransResource() != null &&
                    transition.getToolSpecific().getTransResource().getTransOrgUnitName() != null &&
                    transition.getToolSpecific().getTransResource().getTransOrgUnitName().equals(groupName))
            {
                isUsed = true;
            }
         }
        
        return isUsed;
    }

    // Check if role is used by any transition
    private boolean roleIsUsed(String roleName)
    {
        boolean isUsed = false;
        HashMap<String, AbstractElementModel> alltrans = new HashMap<String, AbstractElementModel>();
        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_SIMPLE_TYPE));
        alltrans.putAll(getPetrinet().getElementContainer().getElementsByType(AbstractPetriNetModelElement.TRANS_OPERATOR_TYPE));
        
        for (Iterator transIter = alltrans.values().iterator(); transIter.hasNext() & !isUsed;)
        {
            TransitionModel transition = (TransitionModel)(transIter.next());
            if (transition.getToolSpecific() != null &&
                transition.getToolSpecific().getTransResource() != null &&
                transition.getToolSpecific().getTransResource().getTransRoleName() != null &&
                transition.getToolSpecific().getTransResource().getTransRoleName().equals(roleName))
            {
                isUsed = true;
            }
         }
        
        return isUsed;
    }

    private void removeResourceClass()
    {
        if (roleList.isSelectionEmpty())
        {
            // Remove group
            String group2remove = getGroupList().getSelectedValue().toString();
            if (!groupIsUsed(group2remove))
            {
                int j = getPetrinet().containsOrgunit(group2remove);
                getPetrinet().getOrganizationUnits().remove(j);
                int index = getGroupList().getSelectedIndex();
                groupListModel.remove(index);
                int size = groupListModel.getSize();
                if (size == 0)
                {
                    // Nobody's left, disable button.
                    resourceClassRemoveButton.setEnabled(false);

                } else
                {
                    // Select an index.
                    if (index == groupListModel.getSize())
                    {
                        // removed item in last position
                        index--;
                    }
                    getGroupList().setSelectedIndex(index);
                    getGroupList().ensureIndexIsVisible(index);
                }

                // Mapping löschen
                getPetrinet().getResourceMapping().remove(group2remove);
                getResourceClassNameTextField().setText("");
            } 
            else
            {
                JOptionPane.showMessageDialog(this, Messages.getString("ResourceEditor.Error.UsedResourceClass.Text"), Messages.getString("ResourceEditor.Error.UsedResourceClass.Title"),
                        JOptionPane.ERROR_MESSAGE);
            }
        } 
        else
        {
            // Remove role
            String role2remove = getRoleList().getSelectedValue().toString();
            if (!roleIsUsed(role2remove))
            {
                int j = getPetrinet().containsRole(role2remove);
                getPetrinet().getRoles().remove(j);
                int index2 = getRoleList().getSelectedIndex();
                roleListModel.remove(index2);
                int size2 = roleListModel.getSize();
                if (size2 == 0)
                {
                    // Nobody's left, disable button.
                    resourceClassRemoveButton.setEnabled(false);
                } else
                {
                    // Select an index.
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
            else
            {
                JOptionPane.showMessageDialog(this, Messages.getString("ResourceEditor.Error.UsedResourceClass.Text"), Messages.getString("ResourceEditor.Error.UsedResourceClass.Title"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        resetClassEditing();
        getEditor().setSaved(false);
    }

    private void resetClassEditing()
    {
        getRoleList().setEnabled(true);
        getGroupList().setEnabled(true);
        getRoleList().clearSelection();
        getGroupList().clearSelection();
        getResourceClassNameTextField().setEditable(false);
        getResourceClassNameTextField().setText("");
        getResourceClassTypeJComboBox().setEnabled(false);
        getResourceClassRemoveButton().setEnabled(false);
        getResourceClassNewButton().setEnabled(true);
        getResourceClassOkButton().setEnabled(false);
        getResourceClassNewButton().requestFocus();
     }

    /* Resources ------------------------------------------------------------- */
    private JPanel getResourcePanel()
    {
        if (resourcePanel == null)
        {
            resourcePanel = new JPanel();
            resourcePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("PetriNet.Resources.Objects")), BorderFactory.createEmptyBorder()));
            SwingUtils.setFixedSize(resourcePanel, 750, 300);
            resourcePanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 3;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 10, 0, 0);
            resourcePanel.add(getResourceListPanel(), c);

            c.gridx = 3;
            c.gridy = 0;
            c.gridwidth = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, 30, 0, 30);
            resourcePanel.add(getResourceButtonPanel(), c);

            c.gridx = 4;
            c.gridy = 0;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, 0, 0, 5);
            resourcePanel.add(getResourcePropertiesPanel(), c);
        }

        return resourcePanel;
    }

    private JLabel getResourceListLabel()
    {
        if (resourceListLabel == null)
        {
            resourceListLabel = new JLabel(Messages.getString("PetriNet.Resources.Resource") + ":");
            SwingUtils.setFixedWidth(resourceListLabel, 80);
        }

        return resourceListLabel;
    }

    private JPanel getResourceListPanel()
    {
        if (resourceListPanel == null)
        {
            resourceListPanel = new JPanel();
            resourceListPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            resourceListPanel.add(getResourceListLabel(), c);

            c.gridx = 1;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            resourceListPanel.add(getResourceListScrollPane(), c);
        }

        return resourceListPanel;
    }

    private JScrollPane getResourceListScrollPane()
    {
        if (resourceListScrollPane == null)
        {
            resourceListScrollPane = new JScrollPane(getResourceList());
            SwingUtils.setFixedSize(resourceListScrollPane, 150, 160);
        }

        return resourceListScrollPane;
    }

    private JPanel getResourceButtonPanel()
    {
        if (resourceButtonPanel == null)
        {
            resourceButtonPanel = new JPanel();
            resourceButtonPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(10, 0, 10, 0);
            resourceButtonPanel.add(getResourceNewButton(), c);
            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(0, 0, 0, 0);
            resourceButtonPanel.add(getResourceOkButton(), c);
            c.gridx = 0;
            c.gridy = 2;
            c.insets = new Insets(10, 0, 10, 0);
            resourceButtonPanel.add(getResourceRemoveButton(), c);
        }

        return resourceButtonPanel;
    }

    private void refreshResourceListFromModel()
    {
    	if (resourceListModel == null)
    		return;
    	
    	resourceListModel.clear();
    	
        for (int i = 0; i < getPetrinet().getResources().size(); i++)
        {
            resourceListModel.addElement((ResourceModel) getPetrinet().getResources().get(i));
        }
    }
    
    private JList getResourceList()
    {
        if (resourceList == null)
        {
            resourceListModel = new DefaultListModel();

            resourceList = new JList(resourceListModel);
            resourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            refreshResourceListFromModel();
            
            resourceList.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent e)
                {
                    if (!e.getValueIsAdjusting() && resourceList.getSelectedIndex() > -1)
                    {
                        getResourceRemoveButton().setEnabled(true);
                        editResource();
                        getResourceEditNameTextField().setText(resourceList.getModel().getElementAt(resourceList.getSelectedIndex()).toString());
                    }
                }
            });
        }

        return resourceList;
    }

    private void clearResourceAssignments()
    {
        unassignedListModel.removeAllElements();
        assignedListModel.removeAllElements();
    }

    private void showResourceAssignments()
    {
        clearResourceAssignments();
        // 1 & 2
        for (Iterator iter = getPetrinet().getOrganizationUnits().iterator(); iter.hasNext();)
        {
            unassignedListModel.addElement(iter.next().toString());
        }
        for (Iterator iter = getPetrinet().getRoles().iterator(); iter.hasNext();)
        {
            unassignedListModel.addElement(iter.next().toString());
        }

        if (!resourceList.isSelectionEmpty())
        {
            // 3
            currentResource = (ResourceModel) resourceList.getModel().getElementAt(resourceList.getSelectedIndex());
            Vector assignedClasses = getPetrinet().getResourceClassesResourceIsAssignedTo(currentResource.toString());
            // 4 & 5
            Object ass;
            for (Iterator iter = assignedClasses.iterator(); iter.hasNext();)
            {
                ass = iter.next();
                assignedListModel.addElement(ass);
                if (unassignedListModel.contains(ass))
                {
                    unassignedListModel.removeElement(ass);
                }
            }
        }
    }

    private JPanel getResourcePropertiesPanel()
    {
        if (resourcePropertiesPanel == null)
        {
            resourcePropertiesPanel = new JPanel();
            resourcePropertiesPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.weightx = 1;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            resourcePropertiesPanel.add(getResourceEditPanel(), c);

            c.gridx = 0;
            c.gridy = 1;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(10, 0, 0, 0);
            resourcePropertiesPanel.add(getResourceAssignPanel(), c);
        }

        return resourcePropertiesPanel;
    }

    private JPanel getResourceEditPanel()
    {
        if (resourceEditPanel == null)
        {
            resourceEditPanel = new JPanel();
            resourceEditPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1;
            c.weighty = 1;

            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 5, 0, 20);
            resourceEditPanel.add(getResourceEditNameLabel(), c);

            c.gridx = 1;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            resourceEditPanel.add(getResourceEditNameTextField(), c);

            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(10, 5, 0, 20);
            resourceEditPanel.add(getResourceEditDescriptionLabel(), c);

            c.gridx = 1;
            c.gridy = 1;
            c.insets = new Insets(10, 0, 0, 0);
            resourceEditPanel.add(getResourceEditDescriptionTextField(), c);
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
            c.weightx = 1;
            c.weighty = 1;

            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(5, 0, 5, 5);
            resourceAssignPanel.add(getResourceUnAssignedLabel(), c);

            c.gridx = 1;
            c.gridy = 0;
            c.insets = new Insets(5, 5, 5, 0);
            resourceAssignPanel.add(getResourceAssignedLabel(), c);

            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(0, 0, 0, 5);
            resourceAssignPanel.add(getResourceUnAssignedScrollPane(), c);

            c.gridx = 1;
            c.gridy = 1;
            c.insets = new Insets(0, 5, 0, 0);
            resourceAssignPanel.add(getResourceAssignedScrollPane(), c);

            c.gridx = 0;
            c.gridy = 2;
            c.insets = new Insets(5, 0, 0, 5);
            resourceAssignPanel.add(getResourceAssignToButton(), c);

            c.gridx = 1;
            c.gridy = 2;
            c.insets = new Insets(5, 5, 0, 0);
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
            SwingUtils.setFixedWidth(resourceEditNameTextField, 200);
            resourceEditNameTextField.setEditable(false);
            resourceEditNameTextField.addKeyListener(new KeyListener()
            {
                public void keyPressed(KeyEvent e)
                {
                    keyReleased(e);
                }

                public void keyTyped(KeyEvent e)
                {
                    keyReleased(e);
                }

                public void keyReleased(KeyEvent e)
                {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        addResource();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    {
                        resetEditing();
                    }
                }
            });
        }

        return resourceEditNameTextField;
    }

    private JTextField getResourceEditDescriptionTextField()
    {
        if (resourceEditDescriptionTextField == null)
        {
            resourceEditDescriptionTextField = new JTextField();
            SwingUtils.setFixedWidth(resourceEditDescriptionTextField, 200);
            resourceEditDescriptionTextField.setEditable(false);
        }

        return resourceEditDescriptionTextField;
    }

    private JButton getResourceNewButton()
    {
        if (resourceNewButton == null)
        {
            resourceNewButton = new JButton();
            resourceNewButton.setIcon(Messages.getImageIcon("PetriNet.Resources.New"));
            resourceNewButton.setText(Messages.getString("PetriNet.Resources.New.Title"));
            
            resourceNewButton.setEnabled(true);
            resourceNewButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    editNewResource();
                }
            });
        }

        return resourceNewButton;
    }

    private JButton getResourceOkButton()
    {
        if (resourceOkButton == null)
        {
            resourceOkButton = new JButton();
            resourceOkButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Ok"));
            resourceOkButton.setText(Messages.getString("PetriNet.Resources.Ok.Title"));
            resourceOkButton.setEnabled(false);
             resourceOkButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    addResource();
                    getEditor().setSaved(false);
                }
            });
        }

        return resourceOkButton;
    }

    private JButton getResourceRemoveButton()
    {
        if (resourceRemoveButton == null)
        {
            resourceRemoveButton = new JButton();
            resourceRemoveButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Delete"));
            resourceRemoveButton.setText(Messages.getString("PetriNet.Resources.Delete.Title"));

            resourceRemoveButton.setEnabled(false);
            resourceRemoveButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    removeResource();
                    getEditor().setSaved(false);
                }
            });
        }

        return resourceRemoveButton;
    }

    private void removeResource()
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
    
    private void editNewResource()
    {
        getResourceEditNameTextField().setText("");
        getResourceNewButton().setEnabled(false);
        getResourceRemoveButton().setEnabled(false);
        getResourceOkButton().setEnabled(true);
        getResourceList().setEnabled(false);
        getResourceList().clearSelection();
        getResourceEditNameTextField().setEditable(true);
        resetClassEditing();
        getResourceEditNameTextField().requestFocus();
    }

    private void editResource()
    {
        showResourceAssignments();
        resetClassEditing();
        getResourceEditNameTextField().setEditable(true);
        getResourceOkButton().setEnabled(true);
        getResourceEditNameTextField().requestFocus();
    }

    private boolean checkSyntax(String str)
    {
        boolean nameExists = false;
        if (str.equals(""))
        {
            JOptionPane.showMessageDialog(this, Messages.getString("ResourceEditor.Error.EmptyResource.Text"), Messages.getString("ResourceEditor.Error.EmptyResource.Title"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        for (Iterator iter = getPetrinet().getResources().iterator(); !nameExists & iter.hasNext();)
        {
            if (iter.next().toString().equals(str)) nameExists = true;
        }

        if (nameExists)
        {
            JOptionPane.showMessageDialog(this, Messages.getString("ResourceEditor.Error.DuplicateResource.Text"), Messages.getString("ResourceEditor.Error.DuplicateResource.Title"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void addResource()
    {
        if (resourceList.isSelectionEmpty())
        {
            if (checkSyntax(getResourceEditNameTextField().getText()))
            {
                ResourceModel newResource = new ResourceModel(getResourceEditNameTextField().getText());
                getPetrinet().addResource(newResource);
                resourceListModel.addElement((ResourceModel) newResource);
                getResourceList().setEnabled(true);
                getResourceList().clearSelection();
                getResourceEditNameTextField().setText("");
            }
        } 
        else
        {
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
        
        resetEditing();
    }

    private void resetEditing()
    {
        getResourceEditNameTextField().setEditable(false);
        getResourceEditNameTextField().setText("");
        getResourceRemoveButton().setEnabled(false);
        clearResourceAssignments();
        getResourceNewButton().setEnabled(true);
        getResourceOkButton().setEnabled(false);
        getResourceNewButton().requestFocus();
        getResourceList().setEnabled(true);
        getResourceList().clearSelection();
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
        SwingUtils.setFixedSize(listScrollPane3, 145, 140);
        return listScrollPane3;

    }

    private JScrollPane getResourceAssignedScrollPane()
    {
        JScrollPane listScrollPane4 = new JScrollPane(getResourceAssignedList());
        SwingUtils.setFixedSize(listScrollPane4, 145, 140);
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
            resourceAssignToButton.setText(Messages.getString("PetriNet.Resources.Assign.Title"));
            resourceAssignToButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Assign"));
            resourceAssignToButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (getResourceUnAssignedList().getSelectedIndex() > -1)
                    {
                        currentResource = (ResourceModel) resourceList.getModel().getElementAt(resourceList.getSelectedIndex());
                        currentUnAssignedResourceClass = resourceUnAssignedList.getModel().getElementAt(resourceUnAssignedList.getSelectedIndex()).toString();
                        getPetrinet().addResourceMapping(currentUnAssignedResourceClass, currentResource.getName());
                        assignedListModel.addElement(currentUnAssignedResourceClass);
                        // assignedListModel.addElement((ResourceClassModel)
                        // currentUnAssignedResourceClass);
                        unassignedListModel.removeElement(currentUnAssignedResourceClass);
                        getEditor().setSaved(false);
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
            resourceUnAssignButton.setIcon(Messages.getImageIcon("PetriNet.Resources.Unassign"));
            resourceUnAssignButton.setText(Messages.getString("PetriNet.Resources.Unassign.Title"));
            resourceUnAssignButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (getResourceAssignedList().getSelectedIndex() > -1)
                    {
                        currentResource = (ResourceModel) resourceList.getModel().getElementAt(resourceList.getSelectedIndex());
                        currentAssignedResourceClass = resourceAssignedList.getModel().getElementAt(resourceAssignedList.getSelectedIndex()).toString();
                        getPetrinet().removeResourceMapping(currentAssignedResourceClass, currentResource.getName());
                        unassignedListModel.addElement(currentAssignedResourceClass);
                        assignedListModel.removeElement((String) currentAssignedResourceClass);
                        getEditor().setSaved(false);
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
    // PetriNetResourceEditor p = new PetriNetResourceEditor();
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
}