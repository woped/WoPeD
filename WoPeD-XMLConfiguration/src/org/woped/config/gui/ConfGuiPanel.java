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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.woped.core.config.ConfigurationManager;
import org.woped.editor.gui.config.AbstractConfPanel;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         The <code>ConfGuiPanel</code> ist the
 *         <code>AbstractConfPanel</code> for the gui configruation. <br>
 *         Created on: 26.11.2004 Last Change on: 26.11.2004
 */

@SuppressWarnings("serial")
public class ConfGuiPanel extends AbstractConfPanel
{
    private Component guiObject;
    // LookAndFeel
    private JComboBox lnfChooser = null;
    private Vector<String>    lnfClasses = new Vector<String>();
    private JPanel    lnfPanel   = null;

    /**
     * Constructor for ConfGuiPanel.
     * 
     * @param guiObject
     */
    public ConfGuiPanel(String name, Component guiObject)
    {
        super(name);
        this.guiObject = guiObject;
        initialize();
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
        contentPanel.add(getLnfPanel(), c);
        // dummy
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        c.gridy = 1;
        contentPanel.add(new JPanel(), c);
        setMainPanel(contentPanel);
    }

    private String getSelectedLnfClassName()
    {
        return lnfClasses.get(getLnfChooser().getSelectedIndex()).toString();
    }

    private void setSelectedLnfClassName(String name)
    {
        for (int i = 0; i < lnfClasses.size(); i++)
        {
            if (lnfClasses.get(i).equals(name))
            {
                getLnfChooser().setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * @see AbstractConfPanel#applyConfiguration()
     */
    public boolean applyConfiguration()
    {
        /* ################## VIEW ################# */
        try
        {
            if (!getSelectedLnfClassName().equals(ConfigurationManager.getConfiguration().getLookAndFeel()))
            {
                // Change LookAndFeel
                Dimension saveDim = guiObject.getSize();
                UIManager.setLookAndFeel(getSelectedLnfClassName());
                SwingUtilities.updateComponentTreeUI(guiObject);
                if (guiObject instanceof Window) ((Window) guiObject).pack();
                // TODO: Do this via ConfigrationChangedListener
                guiObject.setSize(saveDim);
                ConfigurationManager.getConfiguration().setLookAndFeel(getSelectedLnfClassName());
            }
            return true;
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, "Could not load Look and Feel", "Configuration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * @see AbstractConfPanel#readConfigruation()
     */
    public void readConfigruation()
    {
        setSelectedLnfClassName(UIManager.getLookAndFeel().getClass().getName());
    }

    // ##################### GUI Components ########################### */
    /**
     * @return Returns the lnfChooser.
     */
    private JComboBox getLnfChooser()
    {
        if (lnfChooser == null)
        {
            lnfChooser = new JComboBox();
            //DefaultListModel model = new DefaultListModel();
            lnfChooser.addItem("System");
            lnfClasses.add(0, UIManager.getSystemLookAndFeelClassName());
            lnfChooser.addItem("Metal");
            lnfClasses.add(1, "javax.swing.plaf.metal.MetalLookAndFeel");
            lnfChooser.addItem("Motif");
            lnfClasses.add(2, "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            lnfChooser.setPreferredSize(new Dimension(100, 20));
            lnfChooser.setToolTipText("<HTML>Choose favourite toolkit.<br>NOTE: Some changes may need application restart.</HTML>");
            //lnfChooser.setFont(ConfigureUI.CONFIG_FONT);
            // ODO: find more cool LNF

        }
        return lnfChooser;
    }

    /**
     * @return Returns the lfnLabel.
     */
    private JPanel getLnfPanel()
    {
        if (lnfPanel == null)
        {
            lnfPanel = new JPanel();
            lnfPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            lnfPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Look and Feel"), BorderFactory.createEmptyBorder(5, 5, 10, 5)));
            JLabel lnfLabel = new JLabel("Select toolkit: ");
            lnfLabel.setToolTipText("<HTML>Choose favourite toolkit.<br>NOTE: Some changes may need application restart.</HTML>");
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            lnfPanel.add(lnfLabel, c);
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            lnfPanel.add(getLnfChooser(), c);
        }
        return lnfPanel;
    }

}