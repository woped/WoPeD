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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.woped.core.config.ConfigurationManager;
import org.woped.editor.gui.config.AbstractConfPanel;
import org.woped.language.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         The <code>ConfFilePanel</code> ist the
 *         <code>AbstractConfPanel</code> for the configruation of the file
 *         handling. <br>
 *         Created on: 26.11.2004 Last Change on: 26.11.2004
 */

@SuppressWarnings("serial")
public class ConfFilePanel extends AbstractConfPanel
{
    // PNML
    private JPanel      PNMLPanel                      = null;
    private JCheckBox   exportToolspecCheckBox         = null;
    private JCheckBox   importToolspecCheckBox         = null;
    // TPN
    private JPanel      TPNPanel                       = null;
    private JCheckBox   exportTpnElementAsNameCheckBox = null;
    // HomeDir
    private JCheckBox   homeDirDefaultCheckBox         = null;
    private JPanel      homeDirPanel                   = null;
    private JTextField  homeDirTextField               = null;
    private JButton     homeDirChoose                  = null;
    // recent File
    private JPanel      recentFilePanel                = null;
    private JButton     recentFileDelete               = null;

    /**
     * Constructor for ConfFilePanel.
     */
    public ConfFilePanel(String name)
    {
        super(name);
        initialize();
    }

    private void initialize()
    {
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        contentPanel.add(getHomeDirPanel(), c);

        c.gridx = 0;
        c.gridy = 1;
        contentPanel.add(getRecentFilesPanel(), c);

        c.gridx = 0;
        c.gridy = 2;
        contentPanel.add(getPNMLPanel(), c);

        c.gridx = 0;
        c.gridy = 3;
        contentPanel.add(getTPNPanel(), c);
        // dummy
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        c.gridy = 4;
        contentPanel.add(new JPanel(), c);

        setMainPanel(contentPanel);
    }

    /**
     * @see AbstractConfPanel#applyConfiguration()
     */
    public boolean applyConfiguration()
    {
        if (checkHomeDir())
        {
            if (!getHomeDirDefaultCheckBox().isSelected()) ConfigurationManager.getConfiguration().setHomedir(getHomeDirTextField().getText());
            else ConfigurationManager.getConfiguration().setHomedir(null);
            ConfigurationManager.getConfiguration().setExportToolspecific(getExportToolspecCheckBox().isSelected());
            ConfigurationManager.getConfiguration().setImportToolspecific(getImportToolspecCheckBox().isSelected());
            ConfigurationManager.getConfiguration().setTpnSaveElementAsName(getExportTpnElementAsNameCheckBox().isSelected());
            return true;
        } else
        {
            if (JOptionPane.showConfirmDialog(this, Messages.getString("Configuration.Files.Error.InvalidHomeDirectory"), Messages.getString("Configuration.Error.General.Title"),
                    JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION)
            {
                if (!new File(ConfigurationManager.getStandardConfiguration().getHomedir()).exists() && !new File(ConfigurationManager.getStandardConfiguration().getHomedir()).mkdir())
                {
                    JOptionPane.showMessageDialog(this, Messages.getString("Configuration.Files.Error.NoDefaultDirectory"), Messages.getString("Configuration.Error.General.Title"),
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                } else
                {
                    ConfigurationManager.getConfiguration().setHomedir(null);
                    getHomeDirDefaultCheckBox().setSelected(true);
                    return true;
                }
            } else
            {
                //JOptionPane.showMessageDialog(this, "DEFAULT DIR NOT
                // CREATED", "Configuration Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }

    /**
     * @see AbstractConfPanel#readConfigruation()
     */
    public void readConfigruation()
    {
        if (!ConfigurationManager.getConfiguration().isHomedirSet()) getHomeDirDefaultCheckBox().setSelected(true);
        getHomeDirTextField().setText(ConfigurationManager.getConfiguration().getHomedir());
        getExportToolspecCheckBox().setSelected(ConfigurationManager.getConfiguration().isExportToolspecific());
        getImportToolspecCheckBox().setSelected(ConfigurationManager.getConfiguration().isImportToolspecific());
        getExportTpnElementAsNameCheckBox().setSelected(ConfigurationManager.getConfiguration().isTpnSaveElementAsName());
    }

    private boolean checkHomeDir()
    {
        if (new File(getHomeDirTextField().getText()).isDirectory()) return true;
        else return false;
    }

    // ####################### GUI COMPONENTS ###################### */

    private JPanel getHomeDirPanel()
    {
        if (homeDirPanel == null)
        {
            homeDirPanel = new JPanel();
            homeDirPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            homeDirPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Files.Panel.HomeDirectory.Title")), BorderFactory
                    .createEmptyBorder(5, 5, 10, 5)));

            c.weightx = 1;
            c.weighty = 0;
            c.gridx = 0;
            c.gridy = 0;
            homeDirPanel.add(getHomeDirDefaultCheckBox(), c);

            c.gridx = 0;
            c.gridy = 1;
            c.insets = new Insets(0, 4, 0, 0);
            homeDirPanel.add(getHomeDirTextField(), c);

            c.gridx = 2;
            c.gridy = 1;
            c.insets = new Insets(0, 0, 0, 0);
            homeDirPanel.add(getHomeDirChoose(), c);
        }
        return homeDirPanel;
    }

    private JTextField getHomeDirTextField()
    {
        if (homeDirTextField == null)
        {
            homeDirTextField = new JTextField();
            //homeDirTextField.setFont(ConfigureUI.CONFIG_FONT);
            //homeDirTextField.setColumns(30);
            homeDirTextField.setPreferredSize(new Dimension(300, 20));
            homeDirTextField.setToolTipText("<HTML>" + Messages.getString("Configuration.Files.Panel.HomeDirectory.Text.HomeDir.ToolTip") + "</HTML>");
            homeDirTextField.addKeyListener(new KeyListener()
            {
                public void keyPressed(KeyEvent e)
                {}

                public void keyReleased(KeyEvent e)
                {
                    checkHomeDir();
                }

                public void keyTyped(KeyEvent e)
                {}
            });
        }
        return homeDirTextField;
    }

    private JButton getHomeDirChoose()
    {
        if (homeDirChoose == null)
        {
            homeDirChoose = new JButton();
            homeDirChoose.setIcon(Messages.getImageIcon("Button.Browse"));
            homeDirChoose.setText(Messages.getString("Button.Browse.Title"));
            homeDirChoose.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JFileChooser jfc;
                    if (checkHomeDir())
                    {
                        jfc = new JFileChooser(new File(getHomeDirTextField().getText()));
                    } else if (new File(ConfigurationManager.getConfiguration().getHomedir()).isDirectory())
                    {
                        jfc = new JFileChooser(new File(ConfigurationManager.getConfiguration().getHomedir()));
                    } else
                    {
                        jfc = new JFileChooser(new File(ConfigurationManager.getStandardConfiguration().getHomedir()));

                    }
                    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    jfc.showDialog(null, Messages.getString("Button.Ok.Title"));
                    if (jfc.getSelectedFile() != null)
                    {
                        getHomeDirTextField().setText(jfc.getSelectedFile().getPath());
                    }
                }
            });
        }
        return homeDirChoose;
    }

    private JPanel getRecentFilesPanel()
    {
        if (recentFilePanel == null)
        {
            recentFilePanel = new JPanel();
            recentFilePanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.gridx = 0;
            c.weightx = 1;
            recentFilePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Files.Panel.RecentFiles.Title")), BorderFactory
                    .createEmptyBorder(5, 5, 10, 5)));
            recentFilePanel.add(getRecentFilesDelete(), c);

        }
        return recentFilePanel;
    }

    private JButton getRecentFilesDelete()
    {
        if (recentFileDelete == null)
        {
            recentFileDelete = new JButton();
            recentFileDelete.setText(Messages.getString("Configuration.Files.Panel.RecentFiles.Text.DeleteRecentFiles"));
            recentFileDelete.setEnabled(ConfigurationManager.getConfiguration().getRecentFiles().size() != 0);
            recentFileDelete.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    ConfigurationManager.getConfiguration().removeAllRecentFiles();
                    //TODO: Menubar ((MenuBarVC) ((OLDUserInterface)
                    // guiObject).getJMenuBar()).updateRecentMenu();
                    recentFileDelete.setEnabled(false);
                }
            });
        }
        return recentFileDelete;
    }

    private JCheckBox getExportTpnElementAsNameCheckBox()
    {
        if (exportTpnElementAsNameCheckBox == null)
        {
            exportTpnElementAsNameCheckBox = new JCheckBox();
            exportTpnElementAsNameCheckBox.setText(Messages.getString("Configuration.Files.Panel.TPN.Text.ExportID"));
            exportTpnElementAsNameCheckBox.setToolTipText("<HTML>" + Messages.getString("Configuration.Files.Panel.TPN.Text.ExportID.ToolTip") + "</HTML>");
        }
        return exportTpnElementAsNameCheckBox;
    }

    private JPanel getTPNPanel()
    {
        if (TPNPanel == null)
        {
            TPNPanel = new JPanel();
            TPNPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            TPNPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Files.Panel.TPN.Title")), BorderFactory.createEmptyBorder(5, 5,
                    10, 5)));
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            TPNPanel.add(getExportTpnElementAsNameCheckBox(), c);

        }
        return TPNPanel;
    }

    private JCheckBox getImportToolspecCheckBox()
    {
        if (importToolspecCheckBox == null)
        {
            importToolspecCheckBox = new JCheckBox();
            importToolspecCheckBox.setText(Messages.getString("Configuration.Files.Panel.PNML.Text.Import"));
            importToolspecCheckBox.setToolTipText("<HTML>" + Messages.getString("Configuration.Files.Panel.PNML.Text.Import.ToolTip") + "</HTML>");
        }
        return importToolspecCheckBox;
    }

    private JPanel getPNMLPanel()
    {
        if (PNMLPanel == null)
        {
            PNMLPanel = new JPanel();
            PNMLPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            PNMLPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Files.Panel.PNML.Title")), BorderFactory.createEmptyBorder(5, 5,
                    10, 5)));
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            PNMLPanel.add(getImportToolspecCheckBox(), c);
            c.gridx = 0;
            c.gridy = 1;
            PNMLPanel.add(getExportToolspecCheckBox(), c);
        }
        return PNMLPanel;
    }

    private JCheckBox getExportToolspecCheckBox()
    {
        if (exportToolspecCheckBox == null)
        {
            exportToolspecCheckBox = new JCheckBox();
            exportToolspecCheckBox.setText(Messages.getString("Configuration.Files.Panel.PNML.Text.Export"));
            exportToolspecCheckBox.setToolTipText("<HTML>" + Messages.getString("Configuration.Files.Panel.PNML.Text.Export.ToolTip") + "</HTML>");
        }
        return exportToolspecCheckBox;
    }

    private JCheckBox getHomeDirDefaultCheckBox()
    {
        if (homeDirDefaultCheckBox == null)
        {
            homeDirDefaultCheckBox = new JCheckBox();
            homeDirDefaultCheckBox.setText(Messages.getString("Configuration.Files.Panel.HomeDirectory.Text.UseDefault"));
            homeDirDefaultCheckBox.addChangeListener(new ChangeListener()
            {
                public void stateChanged(ChangeEvent e)
                {
                    if (getHomeDirDefaultCheckBox().isSelected())
                    {
                        if (new File(ConfigurationManager.getConfiguration().getHomedir()).isDirectory())
                        {
                            getHomeDirTextField().setText(ConfigurationManager.getConfiguration().getHomedir());
                            getHomeDirTextField().setEditable(false);
                            getHomeDirChoose().setEnabled(false);
                            checkHomeDir();
                        } else
                        {
                            getHomeDirTextField().setText(Messages.getString("Configuration.Files.Error.HomeDirectory") + ConfigurationManager.getConfiguration().getHomedir());
                        }
                    } else
                    {
                        getHomeDirTextField().setEditable(true);
                        getHomeDirChoose().setEnabled(true);
                    }
                }
            });
        }
        return homeDirDefaultCheckBox;
    }

}