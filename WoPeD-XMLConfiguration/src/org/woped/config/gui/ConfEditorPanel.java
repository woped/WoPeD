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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.woped.core.config.ConfigurationManager;
import org.woped.editor.gui.config.AbstractConfPanel;
import org.woped.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         The <code>ConfEditorPanel</code> ist the
 *         <code>AbstractConfPanel</code> for the configruation of the editor.
 *         <br>
 *         Created on: 26.11.2004 Last Change on: 16.12.2004
 */

@SuppressWarnings("serial")
public class ConfEditorPanel extends AbstractConfPanel
{

    //  On Creation JPanel
    private JPanel                onCreationPanel        = null;
    private JCheckBox             editOnCreationCheckBox = null;
    private JCheckBox             insertCopyCheckBox     = null;
    private JCheckBox             smartEditingCheckBox   = null;
    // Editing Panel
    private JPanel                editingPanel           = null;
    private JLabel                showGridSizeLabel      = null;
    private JComboBox             showGridSizeComboBox   = null;
    private JCheckBox             showGridCheckBox       = null;
    // Arc Panel
    private JPanel                arcPanel               = null;
    private JCheckBox             arrowFillHeadCheckBox  = null;
    private JLabel                arrowWidthJLabel       = null;
    private JComboBox             arrowWidthJComboBox    = null;
    private JLabel                arrowheadSizeJLabel    = null;
    private JComboBox             arrowheadSizeJComboBox = null;
    private JLabel                rountingLabel          = null;
    private JRadioButton          roundRounting          = null;
    private JRadioButton          squaredRouting         = null;
    private ButtonGroup           buttonGroup            = null;

    private static final Object[] arrowWidth             = { new String("default"), new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5) };
    private static final Object[] arrowHeadSize          = { new String("default"), new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7),
            new Integer(8)                              };
    private static final Object[] gridSizes              = { new String("default"), new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7),
            new Integer(8)                              };

    /**
     * Constructor for ConfEditorPanel.
     * 
     * @param name
     */
    public ConfEditorPanel(String name)
    {
        super(name);
        initialize();
    }

    private void initialize()
    {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        //c.anchor = GridBagConstraints.NORTH;

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        contentPanel.add(getOnCreationPanel(), c);

        c.gridx = 0;
        c.gridy = 1;
        contentPanel.add(getEditingPanel(), c);

        c.gridx = 0;
        c.gridy = 2;
        contentPanel.add(getArcPanel(), c);
        // dummy
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        c.gridy = 3;
        contentPanel.add(new JPanel(), c);

        setMainPanel(contentPanel);
    }

    /**
     * @see AbstractConfPanel#applyConfiguration()
     */
    public boolean applyConfiguration()
    {
        ConfigurationManager.getConfiguration().setEditingOnCreation(getEditOnCreationCheckBox().isSelected());
        ConfigurationManager.getConfiguration().setSmartEditing(getSmartEditingCheckBox().isSelected());
        ConfigurationManager.getConfiguration().setInsertCOPYwhenCopied(getInsertCopyCheckBox().isSelected());
        ConfigurationManager.getConfiguration().setShowGrid(getShowGridCheckBox().isSelected());
        ConfigurationManager.getConfiguration().setArrowheadSize(getArrowheadSizeJComboBox().getSelectedIndex());
        ConfigurationManager.getConfiguration().setArrowWidth(getArrowWidthJComboBox().getSelectedIndex());
        ConfigurationManager.getConfiguration().setFillArrowHead(getArrowFillHeadCheckBox().isSelected());
        ConfigurationManager.getConfiguration().setRoundRouting(getRoundRounting().isSelected());

        return true;
    }

    /**
     * @see AbstractConfPanel#readConfiguration()
     */
    public void readConfiguration()
    {
        getEditOnCreationCheckBox().setSelected(ConfigurationManager.getConfiguration().isEditingOnCreation());
        getSmartEditingCheckBox().setSelected(ConfigurationManager.getConfiguration().isSmartEditing());
        getInsertCopyCheckBox().setSelected(ConfigurationManager.getConfiguration().isInsertCOPYwhenCopied());
        getShowGridCheckBox().setSelected(ConfigurationManager.getConfiguration().isShowGrid());
        if (ConfigurationManager.getConfiguration().getArrowheadSize() == ConfigurationManager.getStandardConfiguration().getArrowheadSize()) getArrowheadSizeJComboBox().setSelectedIndex(0);
        else if (0 < ConfigurationManager.getConfiguration().getArrowheadSize() && ConfigurationManager.getConfiguration().getArrowheadSize() <= arrowHeadSize.length) getArrowheadSizeJComboBox()
                .setSelectedItem(arrowHeadSize[ConfigurationManager.getConfiguration().getArrowheadSize()]);
        if (ConfigurationManager.getConfiguration().getArrowWidth() == ConfigurationManager.getStandardConfiguration().getArrowWidth()) getArrowWidthJComboBox().setSelectedIndex(0);
        else if (0 < ConfigurationManager.getConfiguration().getArrowWidth() && ConfigurationManager.getConfiguration().getArrowWidth() <= arrowWidth.length) getArrowWidthJComboBox().setSelectedItem(
                arrowWidth[ConfigurationManager.getConfiguration().getArrowWidth()]);
        getArrowFillHeadCheckBox().setSelected(ConfigurationManager.getConfiguration().isFillArrowHead());
        getRoundRounting().setSelected(ConfigurationManager.getConfiguration().isRoundRouting());
        getSquaredRouting().setSelected(!ConfigurationManager.getConfiguration().isRoundRouting());
    }

    // ######################## GUI COMPONENTS ####################### */
    private JCheckBox getEditOnCreationCheckBox()
    {
        if (editOnCreationCheckBox == null)
        {
            editOnCreationCheckBox = new JCheckBox();
            editOnCreationCheckBox.setText(Messages.getString("Configuration.Editor.Panel.SmartEdit.Text.SmartNameEditing"));
            editOnCreationCheckBox.setToolTipText("<html>" + Messages.getString("Configuration.Editor.Panel.SmartEdit.Text.SmartNameEditing.ToolTip") + "</html>");
        }
        return editOnCreationCheckBox;
    }

    private JCheckBox getInsertCopyCheckBox()
    {
        if (insertCopyCheckBox == null)
        {
            insertCopyCheckBox = new JCheckBox();
            insertCopyCheckBox.setText(Messages.getString("Configuration.Editor.Panel.SmartEdit.Text.Add_copy"));
            insertCopyCheckBox.setToolTipText("<html>" + Messages.getString("Configuration.Editor.Panel.SmartEdit.Text.Add_copy.ToolTip") + "</html>");
        }

        return insertCopyCheckBox;
    }

    private JCheckBox getSmartEditingCheckBox()
    {
        if (smartEditingCheckBox == null)
        {
            smartEditingCheckBox = new JCheckBox();
            smartEditingCheckBox.setText(Messages.getString("Configuration.Editor.Panel.SmartEdit.Text.SmartArcDrawing"));
            smartEditingCheckBox.setToolTipText("<html>" + Messages.getString("Configuration.Editor.Panel.SmartEdit.Text.SmartArcDrawing.ToolTip") + "</html>");
        }
        return smartEditingCheckBox;
    }

    private JPanel getOnCreationPanel()
    {
        if (onCreationPanel == null)
        {
            onCreationPanel = new JPanel();
            onCreationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Editor.Panel.SmartEdit.Title")), BorderFactory
                    .createEmptyBorder(5, 5, 10, 5)));
            onCreationPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTH;
            c.fill = GridBagConstraints.HORIZONTAL;

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            onCreationPanel.add(getEditOnCreationCheckBox(), c);

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 1;
            onCreationPanel.add(getSmartEditingCheckBox(), c);

            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 2;
            onCreationPanel.add(getInsertCopyCheckBox(), c);

        }
        return onCreationPanel;
    }

    private JPanel getEditingPanel()
    {
        if (editingPanel == null)
        {
            editingPanel = new JPanel();
            editingPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            editingPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Editor.Panel.Grid.Title")), BorderFactory.createEmptyBorder(5,
                    5, 10, 5)));

            c.weightx = 1;
            c.weighty = 0;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            editingPanel.add(getShowGridCheckBox(), c);

            c.gridwidth = 1;
            c.insets = new Insets(0, 4, 0, 0);
            c.weightx = 1;
            c.weighty = 0;
            c.gridx = 0;
            c.gridy = 1;
            editingPanel.add(getShowGridSizeLabel(), c);

            c.weightx = 1;
            c.weighty = 0;
            c.gridx = 1;
            c.gridy = 1;
            editingPanel.add(getShowGridSizeComboBox(), c);
        }
        return editingPanel;
    }

    private JCheckBox getShowGridCheckBox()
    {
        if (showGridCheckBox == null)
        {
            showGridCheckBox = new JCheckBox();
            showGridCheckBox.setText(Messages.getString("Configuration.Editor.Panel.Grid.Text.ShowGrid"));
            showGridCheckBox.setToolTipText("<html>" + Messages.getString("Configuration.Editor.Panel.Grid.Text.ShowGrid") + "</html>");
        }
        return showGridCheckBox;
    }

    private JLabel getShowGridSizeLabel()
    {
        if (showGridSizeLabel == null)
        {
            showGridSizeLabel = new JLabel(Messages.getString("Configuration.Editor.Panel.Grid.Text.GridSize"));
            showGridSizeLabel.setToolTipText("<html>" + Messages.getString("Configuration.Editor.Panel.Grid.Text.ShowGrid") + "</html>");
        }
        return showGridSizeLabel;
    }

    private JComboBox getShowGridSizeComboBox()
    {
        if (showGridSizeComboBox == null)
        {
            showGridSizeComboBox = new JComboBox(gridSizes);
            showGridSizeComboBox.setEnabled(false);
            showGridSizeComboBox.setToolTipText("<html>" + Messages.getString("Configuration.Editor.Panel.Grid.Text.GridSize") + "</html>");
        }
        return showGridSizeComboBox;
    }

    private JPanel getArcPanel()
    {
        if (arcPanel == null)
        {
            arcPanel = new JPanel();
            arcPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            arcPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Editor.Panel.Arcs.Title")), BorderFactory.createEmptyBorder(5, 5,
                    10, 5)));
            buttonGroup = new ButtonGroup();
            buttonGroup.add(getRoundRounting());
            buttonGroup.add(getSquaredRouting());

            c.weightx = 1;
            c.weighty = 0;

            c.gridx = 0;
            c.gridy = 0;
            c.gridheight = 2;
            arcPanel.add(getRoutingLabel(), c);

            c.gridx = 1;
            c.gridy = 0;
            c.gridheight = 1;
            arcPanel.add(getRoundRounting(), c);

            //c.insets = new Insets(0, 4, 0, 0);
            c.gridwidth = 1;
            c.weightx = 1;
            c.weighty = 0;
            c.gridx = 2;
            c.gridy = 0;
            arcPanel.add(getArrowWidthJLabel(), c);

            c.gridx = 3;
            c.gridy = 0;
            arcPanel.add(getArrowWidthJComboBox(), c);

            c.gridx = 1;
            c.gridy = 1;
            arcPanel.add(getSquaredRouting(), c);

            c.gridx = 2;
            c.gridy = 1;
            arcPanel.add(getArrowheadSizeJLabel(), c);

            c.gridx = 3;
            c.gridy = 1;
            arcPanel.add(getArrowheadSizeJComboBox(), c);

            c.gridwidth = 2;
            c.gridx = 2;
            c.gridy = 2;
            arcPanel.add(getArrowFillHeadCheckBox(), c);

        }
        return arcPanel;
    }

    private JLabel getArrowWidthJLabel()
    {
        if (arrowWidthJLabel == null)
        {
            arrowWidthJLabel = new JLabel(Messages.getString("Configuration.Editor.Panel.Arcs.Label.LineWidth"));
            arrowWidthJLabel.setToolTipText("<html>" + Messages.getString("Configuration.Editor.Panel.Arcs.Label.LineWidth.ToolTip") + "</html>");
        }
        return arrowWidthJLabel;
    }

    private JComboBox getArrowWidthJComboBox()
    {
        if (arrowWidthJComboBox == null)
        {
            arrowWidthJComboBox = new JComboBox(arrowWidth);
            arrowWidthJComboBox.setToolTipText("<html>" + Messages.getString("Configuration.Editor.Panel.Arcs.Label.LineWidth.ToolTip") + "</html>");
        }
        return arrowWidthJComboBox;
    }

    private JLabel getArrowheadSizeJLabel()
    {
        if (arrowheadSizeJLabel == null)
        {
            arrowheadSizeJLabel = new JLabel(Messages.getString("Configuration.Editor.Panel.Arcs.Label.ArrowHeadSize"));
            arrowheadSizeJLabel.setToolTipText("<html>" + Messages.getString("Configuration.Editor.Panel.Arcs.Label.ArrowHeadSize.ToolTip") + "</html>");
        }
        return arrowheadSizeJLabel;
    }

    private JComboBox getArrowheadSizeJComboBox()
    {
        if (arrowheadSizeJComboBox == null)
        {
            arrowheadSizeJComboBox = new JComboBox(arrowHeadSize);
            arrowheadSizeJComboBox.setToolTipText("<html>" + Messages.getString("Configuration.Editor.Panel.Arcs.Label.ArrowHeadSize.ToolTip") + "</html>");
        }
        return arrowheadSizeJComboBox;
    }

    private JCheckBox getArrowFillHeadCheckBox()
    {
        if (arrowFillHeadCheckBox == null)
        {
            arrowFillHeadCheckBox = new JCheckBox();
            arrowFillHeadCheckBox.setText(Messages.getString("Configuration.Editor.Panel.Arcs.Text.FillArrowHeads"));
            arrowFillHeadCheckBox.setToolTipText("<html>" + Messages.getString("Configuration.Editor.Panel.Arcs.Text.FillArrowHeads") + "</html>");
        }
        return arrowFillHeadCheckBox;
    }

    private JLabel getRoutingLabel()
    {
        if (rountingLabel == null)
        {
            rountingLabel = new JLabel(Messages.getString("Configuration.Editor.Panel.Arcs.Label.Routing"));
            rountingLabel.setToolTipText("<html>" + Messages.getString("Configuration.Editor.Panel.Arcs.Label.Routing.ToolTip") + "</html>");
        }
        return rountingLabel;
    }

    private JRadioButton getRoundRounting()
    {
        if (roundRounting == null)
        {
            roundRounting = new JRadioButton(Messages.getString("Configuration.Editor.Panel.Arcs.Button.Rounded"));
        }
        return roundRounting;
    }

    private JRadioButton getSquaredRouting()
    {
        if (squaredRouting == null)
        {
            squaredRouting = new JRadioButton(Messages.getString("Configuration.Editor.Panel.Arcs.Button.Straight"));
        }
        return squaredRouting;
    }
}