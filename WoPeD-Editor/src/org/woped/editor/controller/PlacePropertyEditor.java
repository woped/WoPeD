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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
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
import org.woped.editor.controller.vc.EditorVC;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

/**
 * @author tfreytag
 */

@SuppressWarnings("serial")
public class PlacePropertyEditor extends JDialog
{
    // General
    private PlaceModel    place            = null;
    private EditorVC      editor           = null;
    private JPanel        contentPanel     = null;

    // Name
    private JPanel        namePanel        = null;
    private JLabel        nameLabel        = null;
    private JTextField    nameTextField    = null;
    private JLabel        idLabel          = null;
    private JTextField    idTextField      = null;

    // Marking
    private JPanel        markingPanel     = null;
    private JLabel        markingLabel     = null;
    private JTextField    markingTextField = null;

    // Buttons
    private JPanel        buttonPanel      = null;
    private WopedButton     buttonOk         = null;
    private WopedButton     buttonCancel     = null;

    public PlacePropertyEditor(Frame owner, Point position, PlaceModel place, EditorVC editor)
    {
        super(owner, true);
        this.place = place;
        this.editor = editor;
        this.setVisible(false);
        initialize();
        this.setSize(400, 210);
		this.setLocation(new Point(position.x + 50, position.y));
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
            namePanel.setBorder(BorderFactory
                    .createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Place.Properties.Identification")), BorderFactory.createEmptyBorder(5, 5, 0, 5)));

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
            nameTextField.setPreferredSize(new Dimension(150, 25));
            nameTextField.setMinimumSize(new Dimension(150, 25));
            nameTextField.setMaximumSize(new Dimension(150, 25));
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
            idLabel = new JLabel("ID#: ");
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
            idTextField.setPreferredSize(new Dimension(100, 25));
        }

        return idTextField;
    }

    // ******************************Marking
    // Panel*****************************************
    private JPanel getMarkingPanel()
    {
        if (markingPanel == null)
        {
            markingPanel = new JPanel();
            markingPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            markingPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Place.Properties.Marking")), BorderFactory.createEmptyBorder(5, 5, 0, 5)));

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
            markingTextField.setPreferredSize(new Dimension(70, 25));
            markingTextField.setMinimumSize(new Dimension(70, 25));
            markingTextField.setMaximumSize(new Dimension(70, 25));
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

    private WopedButton getButtonOk()
    {
        if (buttonOk == null)
        {
            buttonOk = new WopedButton();
            buttonOk.setIcon(Messages.getImageIcon("Button.Ok"));
            buttonOk.setText(Messages.getTitle("Button.Ok"));

            buttonOk.setMnemonic(Messages.getMnemonic("Button.Ok"));
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

    private WopedButton getButtonCancel()
    {
        if (buttonCancel == null)
        {
            buttonCancel = new WopedButton();
            buttonCancel.setText(Messages.getTitle("Button.Cancel"));
            buttonCancel.setIcon(Messages.getImageIcon("Button.Cancel"));
            buttonCancel.setMnemonic(Messages.getMnemonic("Button.Cancel"));
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

    private void apply()
    {
		// name changing handling
        place.setNameValue(getNameTextField().getText());
        place.setTokens(Integer.valueOf(markingTextField.getText()).intValue());
        getEditor().setSaved(false);
        getEditor().updateNet();

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