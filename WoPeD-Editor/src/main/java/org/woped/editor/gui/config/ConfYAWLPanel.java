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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.editor.gui.config;

import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ConfYAWLPanel extends AbstractConfPanel {

    // Panels
    private JPanel YAWLEnabledPanel = null;
    private JPanel YAWLOptionsPanel = null;

    // Boxes
    private JCheckBox yawlEnabledBox = null;
    private JCheckBox yawlExportExplicitPlacesCheckBox = null;
    private JCheckBox yawlExportGroupsCheckBox = null;

    /**
     * Constructor for ConfYAWLPanel.
     */
    public ConfYAWLPanel(String name) {
        super(name);
        initialize();
    }

    /**
     * @see AbstractConfPanel#applyConfiguration()
     */
    public boolean applyConfiguration() {
        ConfigurationManager.getConfiguration().setYAWLEnabled(getYAWLEnabledBox().isSelected());
        ConfigurationManager.getConfiguration().setYAWLExportExplicitPlaces(getYAWLExportExplicitPlacesCheckBox().isSelected());
        ConfigurationManager.getConfiguration().setYAWLExportGroups(getYAWLExportGroupsCheckBox().isSelected());

        return true;
    }

    /**
     * @see AbstractConfPanel#readConfiguration()
     */
    public void readConfiguration() {
        getYAWLEnabledBox().setSelected(ConfigurationManager.getConfiguration().isYAWLEnabled());
        getYAWLExportExplicitPlacesCheckBox().setSelected(ConfigurationManager.getConfiguration().isYAWLExportExplicitPlaces());
        getYAWLExportGroupsCheckBox().setSelected(ConfigurationManager.getConfiguration().isYAWLExportGroups());

        if(getYAWLEnabledBox().isSelected()) {
            getYAWLOptionsPanel().setVisible(true);
        }
        else {
            getYAWLOptionsPanel().setVisible(false);
        }
    }

    private void initialize() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        contentPanel.add(getYAWLEnabledPanel(), c);

        c.gridx = 0;
        c.gridy = 1;
        contentPanel.add(getYAWLOptionsPanel(), c);

        // dummy
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        c.gridy = 2;
        contentPanel.add(new JPanel(), c);

        setMainPanel(contentPanel);
    }

    // Panels

    /**
     * Returns the YAWlEnabledPanel
     *
     * @return JPanel
     */
    private JPanel getYAWLEnabledPanel() {
        if (YAWLEnabledPanel == null) {
            YAWLEnabledPanel = new JPanel();
            YAWLEnabledPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            YAWLEnabledPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(Messages.getString("Configuration.YAWL.Panel.YAWL.Title")),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            YAWLEnabledPanel.add(getYAWLEnabledBox(), c);
        }
        return YAWLEnabledPanel;
    }

    /**
     * Returns the YAWLPanel
     *
     * @return JPanel
     */
    private JPanel getYAWLOptionsPanel() {
        if (YAWLOptionsPanel == null)
        {
            YAWLOptionsPanel = new JPanel();
            YAWLOptionsPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            YAWLOptionsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.YAWL.Panel.YAWL.Options.Title")), BorderFactory.createEmptyBorder(5, 5,
                    10, 5)));
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            YAWLOptionsPanel.add(getYAWLExportExplicitPlacesCheckBox(), c);
            c.gridx = 0;
            c.gridy = 1;
            YAWLOptionsPanel.add(getYAWLExportGroupsCheckBox(), c);
        }
        return YAWLOptionsPanel;
    }

    /**
     * Returns the checkbox for switching between implicit and explicit places to YAWL
     *
     * @return  JCheckBox
     */
    private JCheckBox getYAWLExportExplicitPlacesCheckBox() {
        if (yawlExportExplicitPlacesCheckBox == null)
        {
            yawlExportExplicitPlacesCheckBox = new JCheckBox();
            yawlExportExplicitPlacesCheckBox.setText(Messages.getString("Configuration.YAWL.Panel.Export.ExplicitPlaces"));
            yawlExportExplicitPlacesCheckBox.setToolTipText("<HTML>" + Messages.getString("Configuration.YAWL.Panel.Export.ExplicitPlaces.ToolTip") + "</HTML>");
        }
        return yawlExportExplicitPlacesCheckBox;
    }

    /**
     * Returns the checkbox for switching between exporting groups
     *
     * @return  JCheckBox
     */
    private JCheckBox getYAWLExportGroupsCheckBox() {
        if (yawlExportGroupsCheckBox == null)
        {
            yawlExportGroupsCheckBox = new JCheckBox();
            yawlExportGroupsCheckBox.setText(Messages.getString("Configuration.YAWL.Panel.Export.Groups"));
            yawlExportGroupsCheckBox.setToolTipText("<HTML>" + Messages.getString("Configuration.YAWL.Panel.Export.Groups.ToolTip") + "</HTML>");
        }
        return yawlExportGroupsCheckBox;
    }

    /**
     * Returns the checkbox for activating the YAWL export suppport
     *
     * @return JCheckBox
     */
    private JCheckBox getYAWLEnabledBox() {
        if (yawlEnabledBox == null) {
            yawlEnabledBox = new JCheckBox(Messages.getString("Configuration.YAWL.Panel.Enabled"));
            yawlEnabledBox.setEnabled(true);
            yawlEnabledBox.setToolTipText("<html>" + Messages.getString("Configuration.YAWL.Panel.Enabled.Tooltip") + "</html>");
            ItemListener listener = new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    JCheckBox box = (JCheckBox) e.getSource();
                    if(box == yawlEnabledBox) {
                        if(box.isSelected()) {
                            getYAWLOptionsPanel().setVisible(true);
                        }
                        else {
                            getYAWLOptionsPanel().setVisible(false);
                        }
                    }
                }
            };
            yawlEnabledBox.addItemListener(listener);
        }
        return yawlEnabledBox;
    }
}