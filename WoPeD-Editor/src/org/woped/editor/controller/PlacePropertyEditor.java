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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.woped.core.model.petrinet.PlaceModel;
import org.woped.core.utilities.Utils;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.editor.gui.ToolBarButton;
import org.woped.editor.utilities.Messages;

/**
 * @author tfreytag
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PlacePropertyEditor extends JDialog
{
    // General
    private PlaceModel            place                         = null;
    private EditorVC              editor                        = null;
    private JPanel                contentPanel                  = null;

    // Name
    private JPanel                namePanel                     = null;
    private JLabel                nameLabel                     = null;
    private JTextField            nameTextField                 = null;
    private JLabel                idLabel                       = null;
    private JTextField            idTextField                   = null;

    // Marking
    private JPanel                markingPanel                  = null;
    private JLabel                markingLabel                  = null;
    private JTextField            markingTextField              = null;

    // Buttons
    private JPanel                buttonPanel                   = null;
    private ToolBarButton         buttonOk                      = null;
    private ToolBarButton         buttonCancel                  = null;
    private ToolBarButton         buttonApply                   = null;
  
    public PlacePropertyEditor(Frame owner, PlaceModel place, EditorVC editor)
    {
        super(owner, true);
        this.place = place;
        this.editor = editor;
        this.setVisible(false);
        initialize();
        this.setSize(350, 180);
        this.setLocation(Utils.getCenterPoint(owner.getBounds(), this.getSize()));
        this.setVisible(true);
    }

    private void initialize()
    {
        this.setTitle(Messages.getString("Place.Properties"));
        this.getContentPane().add(getContentPanel(), BorderLayout.NORTH);
        this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
        getNameTextField().requestFocus();
    }

    private JPanel getContentPanel()
    {
        if (contentPanel == null)
        {
            contentPanel = new JPanel();
            contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            contentPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1;
            c.weighty = 1;
    
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 0);
            contentPanel.add(getNamePanel(), c);

            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(0, 0, 0, 0);
            contentPanel.add(getMarkingPanel(), c);
        }
        
        return contentPanel;
    }
    
    // **************************NamePanel******************************
    private JPanel getNamePanel()
    {
        if (namePanel == null)
        {
            namePanel = new JPanel();
            namePanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            namePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(Messages.getString("Place.Properties.Identification")), 
                    BorderFactory.createEmptyBorder(5, 5, 0, 5)));

            c.weightx = 1;
            c.weighty = 1;
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.insets = new Insets(0, 0, 0, 0);
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
            c.insets = new Insets(0, 10, 0, 0);
            namePanel.add(getIdTextField(), c);
       }
        
        return namePanel;
    }

    private JLabel getNameLabel()
    {
        if (nameLabel == null)
        {
            nameLabel = new JLabel(Messages.getString("Place.Properties.Name") + ":");
        }
        
        return nameLabel;
    }

    private JTextField getNameTextField()
    {
        if (nameTextField == null)
        {
            nameTextField = new JTextField(place.getNameValue());
            nameTextField.setPreferredSize(new Dimension(150, 20));
            nameTextField.setMinimumSize(new Dimension(150, 20));
            nameTextField.setMaximumSize(new Dimension(150, 20));
            nameTextField.addKeyListener(new KeyListener()
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
                                apply();
                                PlacePropertyEditor.this.dispose();
                            }
                        }
                    });
        }
        
        return nameTextField;
    }

    private JLabel getIdLabel()
    {
        if (idLabel == null)
        {
            idLabel = new JLabel("Id#: ");
        }
    
        return idLabel;
    }

    private JTextField getIdTextField()
    {
        if (idTextField == null)
        {
            idTextField = new JTextField();
            idTextField.setText("" + place.getId());
            idTextField.setEditable(false);
            idTextField.setPreferredSize(new Dimension(40,20));
        }
    
        return idTextField;
    }
    
    // ******************************Marking Panel*****************************************
    private JPanel getMarkingPanel()
    {
        if (markingPanel == null)
        {
            markingPanel = new JPanel();
            markingPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            markingPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(Messages.getString("Place.Properties.Marking")), 
                    BorderFactory.createEmptyBorder(5, 5, 0, 5)));

            c.weightx = 1;
            c.weighty = 1;
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.insets = new Insets(0, 0, 0, 0);
            markingPanel.add(getMarkingLabel(), c);

            c.gridx = 2;
            c.gridy = 0;
            c.gridwidth = 2;
            c.insets = new Insets(0, 10, 0, 0);
            markingPanel.add(getMarkingTextField(), c);
        }
        
        return markingPanel;
    }

    private JLabel getMarkingLabel()
    {
        if (markingLabel == null)
        {
            markingLabel = new JLabel(Messages.getString("Place.Properties.NumberTokens") + ":");
        }
        
        return markingLabel;
    }

    private JTextField getMarkingTextField()
    {
        if (markingTextField == null)
        {
            markingTextField = new JTextField("" + place.getTokenCount());
            markingTextField.setPreferredSize(new Dimension(70, 20));
            markingTextField.setMinimumSize(new Dimension(70, 20));
            markingTextField.setMaximumSize(new Dimension(70, 20));
            markingTextField.addKeyListener(new KeyListener()
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
                                apply();
                                PlacePropertyEditor.this.dispose();
                            }
                        }
                    });
        }
        
        return markingTextField;
    }

    // *****************************************************ButtonPanel****************************************************
    private JPanel getButtonPanel()
    {
        if (buttonPanel == null)
        {
            buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(getButtonOk());
            buttonPanel.add(getButtonCancel());
//            buttonPanel.add(getButtonApply());
        }
        return buttonPanel;
    }

    private ToolBarButton getButtonOk()
    {
        if (buttonOk == null)
        {
            buttonOk = new ToolBarButton(Messages.getImageIcon("Button.Ok"), 
                    Messages.getString("Button.Ok.Title"), 
                    ToolBarButton.TEXTORIENTATION_RIGHT);
            
            buttonOk.setMnemonic(KeyEvent.VK_O);
            buttonOk.setPreferredSize(new Dimension(100, 25));
            buttonOk.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    apply();
                    PlacePropertyEditor.this.dispose();
                }
            });

        }
        return buttonOk;
    }

    private ToolBarButton getButtonCancel()
    {
        if (buttonCancel == null)
        {
            buttonCancel = new ToolBarButton(Messages.getImageIcon("Button.Cancel"), 
                    Messages.getString("Button.Cancel.Title"), 
                    ToolBarButton.TEXTORIENTATION_RIGHT);
            buttonCancel.setMnemonic(KeyEvent.VK_C);
            buttonCancel.setPreferredSize(new Dimension(100, 25));
            buttonCancel.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    PlacePropertyEditor.this.dispose();
                }
            });
        }
 
        return buttonCancel;
    }

    private ToolBarButton getButtonApply()
    {
        if (buttonApply == null)
        {
            buttonApply = new ToolBarButton(Messages.getImageIcon("Button.Apply"), 
                    Messages.getString("Button.Apply.Title"), 
                    ToolBarButton.TEXTORIENTATION_RIGHT);
            buttonApply.setMnemonic(KeyEvent.VK_A);
            buttonApply.setPreferredSize(new Dimension(100, 25));
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
        // name changing handling
        place.setNameValue(getNameTextField().getText());
        place.setTokens(Integer.valueOf(markingTextField.getText()).intValue());
    } 

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
     /**
     * @return Returns the editor.
     */
    public EditorVC getEditor()
    {
        return editor;
    }
}