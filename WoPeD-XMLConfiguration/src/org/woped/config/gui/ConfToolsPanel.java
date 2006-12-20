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
package org.woped.config.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.woped.core.config.ConfigurationManager;
import org.woped.editor.gui.config.AbstractConfPanel;
import org.woped.editor.utilities.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         The <code>ConfToolsPanel</code> ist the
 *         <code>AbstractConfPanel</code> for configruation of the tools .
 *         <br>
 *         Created on: 26.11.2004
 */
public class ConfToolsPanel extends AbstractConfPanel
{
    // WOflan
    private JPanel     woflanPanel     = null;
    private JCheckBox  woflanCheckBox  = null;
    private JTextField woflanTextField = null;
    private JButton    woflanBrowse    = null;
    private JLabel     getWoflanLabel  = null;

    /**
     * Constructor for ConfToolsPanel.
     */
    public ConfToolsPanel(String name)
    {
        super(name);
        initialize();
    }

    /**
     * @see AbstractConfPanel#applyConfiguration()
     */
    public boolean applyConfiguration()
    {
        if (isValidDir())
        {
            ConfigurationManager.getConfiguration().setUseWoflan(getWoflanCheckBox().isSelected());
            ConfigurationManager.getConfiguration().setWoflanPath(getWoflanTextField().getText());
            return true;
        } else
        {
            JOptionPane
                    .showMessageDialog(this, Messages.getString("Configuration.Tools.Error.WoflanPathInvalid"), Messages.getString("Configuration.Error.General.Title"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * @see AbstractConfPanel#readConfigruation()
     */
    public void readConfigruation()
    {
        getWoflanCheckBox().setSelected(ConfigurationManager.getConfiguration().isUseWoflan());
        getWoflanTextField().setText(ConfigurationManager.getConfiguration().getWoflanPath());
        checkUseWoflan();
    }

    private void initialize()
    {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        contentPanel.add(getWoflanPanel(), c);
        // dummy
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        c.gridy = 1;
        contentPanel.add(new JPanel(), c);
        setMainPanel(contentPanel);
    }

    // ################## GUI COMPONENTS #################### */
    private JCheckBox getWoflanCheckBox()
    {
        if (woflanCheckBox == null)
        {
            woflanCheckBox = new JCheckBox();
            woflanCheckBox.setText(Messages.getString("Configuration.Tools.Panel.Woflan.UseWoflan.Text"));
            woflanCheckBox.setToolTipText("<HTML>" + Messages.getString("Configuration.Tools.Panel.Woflan.UseWoflan.Text.ToolTip") + "</HTML>");
            woflanCheckBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    checkUseWoflan();
                }
            });
        }
        return woflanCheckBox;
    }

    private void checkUseWoflan()
    {
        if (woflanCheckBox.isSelected())
        {
            getWoflanTextField().setEditable(true);
            getWoflanBrowse().setEnabled(true);
        } else
        {
            getWoflanTextField().setEditable(false);
            getWoflanBrowse().setEnabled(false);
        }
    }

    private JPanel getWoflanPanel()
    {
        if (woflanPanel == null)
        {
            woflanPanel = new JPanel();
            woflanPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;

            woflanPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Tools.Panel.Woflan.Title")), BorderFactory.createEmptyBorder(
                    5, 5, 10, 5)));

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            woflanPanel.add(getWoflanCheckBox(), c);

            /*
             * c.weightx = 1; c.gridx = 1; c.gridy = 0;
             * woflanPanel.add(getGetWoflanLabel(), c);
             */

            c.weightx = 1;
            c.gridx = 0;
            c.gridwidth = 2;
            c.gridy = 1;
            c.insets = new Insets(0, 4, 0, 0);
            woflanPanel.add(getWoflanTextField(), c);

            c.weightx = 0.5;
            c.gridx = 2;
            c.gridy = 1;
            c.gridwidth = 1;
            c.insets = new Insets(0, 0, 0, 0);
            woflanPanel.add(getWoflanBrowse(), c);
        }
        return woflanPanel;
    }

    private JTextField getWoflanTextField()
    {
        if (woflanTextField == null)
        {
            woflanTextField = new JTextField();
            woflanTextField.setPreferredSize(new Dimension(300, 20));
            //woflanTextField.setFont(ConfigureUI.CONFIG_FONT);
            woflanTextField.setToolTipText("<HTML>" + Messages.getString("Configuration.Tools.Panel.Woflan.WoflanPath.Text.ToolTip") + "</HTML>");
            //woflanTextField.setColumns(25);
        }
        return woflanTextField;
    }

    private boolean isValidDir()
    {
        if (new File(getWoflanTextField().getText()).isFile() || !getWoflanCheckBox().isSelected())
        {
            return true;
        } else
        {
            return false;
        }
    }

    private JButton getWoflanBrowse()
    {
        if (woflanBrowse == null)
        {
            woflanBrowse = new JButton();
            //woflanBrowse.setFont(ConfigureUI.CONFIG_FONT);
            woflanBrowse.setIcon(Messages.getImageIcon("Button.Browse"));
            woflanBrowse.setText(Messages.getString("Button.Browse.Title"));
            woflanBrowse.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JFileChooser jfc;
                    if (isValid())
                    {
                        jfc = new JFileChooser(new File(getWoflanTextField().getText()));
                    } else if (new File(ConfigurationManager.getConfiguration().getHomedir()).isDirectory())
                    {
                        jfc = new JFileChooser(new File(ConfigurationManager.getConfiguration().getHomedir()));
                    } else
                    {
                        jfc = new JFileChooser(new File(ConfigurationManager.getStandardConfiguration().getHomedir()));

                    }
                    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    jfc.showDialog(null, Messages.getString("Button.Ok.Title"));
                    if (jfc.getSelectedFile() != null)
                    {
                        getWoflanTextField().setText(jfc.getSelectedFile().getPath());
                    }
                }
            });
        }
        return woflanBrowse;
    }

    private JLabel getGetWoflanLabel()
    {
        if (getWoflanLabel == null)
        {
            getWoflanLabel = new JLabel("<html><u>" + Messages.getString("Configuration.Tools.Panel.Woflan.WhatsWoflan.Label") + "</u></html>");
            //getWoflanLabel.getFont().getFamily(),
            // getWoflanLabel.getFont().getSize(), Font.);
            getWoflanLabel.setHorizontalAlignment(JLabel.RIGHT);
            getWoflanLabel.addMouseListener(new MouseListener()
            {
                public void mouseClicked(MouseEvent arg0)
                {
                //                    // TODO: USE ACTION INSTEAD
                //                    OLDUserInterface.getInstance().showIndex("woflan.htm");
                }

                public void mouseEntered(MouseEvent arg0)
                {
                    getWoflanLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                public void mouseExited(MouseEvent arg0)
                {
                    getWoflanLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                public void mousePressed(MouseEvent arg0)
                {

                }

                public void mouseReleased(MouseEvent arg0)
                {

                }
            });
        }
        return getWoflanLabel;
    }
}