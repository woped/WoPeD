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
package org.woped.editor.gui.config;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
//import java.awt.event.ItemEvent;
//import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * <br>
 *         The <code>ConfEditorPanel</code> is the
 *         <code>AbstractConfPanel</code> for the configuration of the editor. <br>
 *         Created on: 26.11.2004 Last Change on: 16.12.2004
 */

@SuppressWarnings("serial")
public class ConfEditorPanel extends AbstractConfPanel {
	// General Panel
	private JPanel generalPanel = null;
	private JComboBox lnfChooser = null;

	// On Creation JPanel
	private JPanel onCreationPanel = null;
	private JCheckBox editOnCreationCheckBox = null;
	private JCheckBox insertCopyCheckBox = null;
	private JCheckBox smartEditingCheckBox = null;
	// Editing Panel
	// private JLabel showGridSizeLabel = null;
	// private JComboBox showGridSizeComboBox = null;
	private JCheckBox showGridCheckBox = null;
	// Arc Panel
	private JPanel arcPanel = null;
	private JCheckBox arrowFillHeadCheckBox = null;
	private JLabel arrowWidthJLabel = null;
	private JComboBox arrowWidthJComboBox = null;
	private JLabel arrowheadSizeJLabel = null;
	private JComboBox arrowheadSizeJComboBox = null;

	private static final Object[] arrowWidth = { new String("default"),
			new Integer(1), new Integer(2), new Integer(3), new Integer(4),
			new Integer(5) };
	private static final Object[] arrowHeadSize = { new String("default"),
			new Integer(1), new Integer(2), new Integer(3), new Integer(4),
			new Integer(5), new Integer(6), new Integer(7), new Integer(8) };
	// private static final Object[] gridSizes = { new String("default"), new
	// Integer(1), new Integer(2), new Integer(3), new Integer(4), new
	// Integer(5), new Integer(6), new Integer(7),
	// new Integer(8) };

	private Map<String, String> lnfClasses = new HashMap<String, String>();

	/**
	 * Constructor for ConfEditorPanel.
	 * 
	 * @param name
	 */
	public ConfEditorPanel(String name) {
		super(name);
		initialize();
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
		contentPanel.add(getOnCreationPanel(), c);

		/*
		 * c.gridx = 0; c.gridy = 1; contentPanel.add(getEditingPanel(), c);
		 */

		c.gridx = 0;
		c.gridy = 1;
		contentPanel.add(getArcPanel(), c);

		c.gridx = 0;
		c.gridy = 2;
		contentPanel.add(getGeneralPanel(), c);

		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 1;
		c.gridy = 3;
		contentPanel.add(new JPanel(), c);

		setMainPanel(contentPanel);
	}

	/**
	 * @see AbstractConfPanel#applyConfiguration()
	 */
	public boolean applyConfiguration() {
		ConfigurationManager.getConfiguration().setEditingOnCreation(
				getEditOnCreationCheckBox().isSelected());
		ConfigurationManager.getConfiguration().setSmartEditing(
				getSmartEditingCheckBox().isSelected());
		ConfigurationManager.getConfiguration().setInsertCOPYwhenCopied(
				getInsertCopyCheckBox().isSelected());
		ConfigurationManager.getConfiguration().setShowGrid(
				getShowGridCheckBox().isSelected());
		ConfigurationManager.getConfiguration().setArrowheadSize(
				getArrowheadSizeJComboBox().getSelectedIndex());
		ConfigurationManager.getConfiguration().setArrowWidth(
				getArrowWidthJComboBox().getSelectedIndex());
		ConfigurationManager.getConfiguration().setFillArrowHead(
				getArrowFillHeadCheckBox().isSelected());

		boolean changed = !lnfClasses.get(getLnfChooser().getSelectedItem())
				.equals(ConfigurationManager.getConfiguration()
						.getLookAndFeel());

		if (changed) {
			ConfigurationManager.getConfiguration().setLookAndFeel(
					lnfClasses.get(getLnfChooser().getSelectedItem()));
			JOptionPane
					.showMessageDialog(
							this,
							Messages.getString("Configuration.Editor.Dialog.Restart.Message"),
							Messages.getString("Configuration.Editor.Dialog.Restart.Title"),
							JOptionPane.INFORMATION_MESSAGE);
		}

		return true;
	}

	/**
	 * @see AbstractConfPanel#readConfiguration()
	 */
	public void readConfiguration() {
		getEditOnCreationCheckBox().setSelected(
				ConfigurationManager.getConfiguration().isEditingOnCreation());
		getSmartEditingCheckBox().setSelected(
				ConfigurationManager.getConfiguration().isSmartEditing());
		getInsertCopyCheckBox().setSelected(
				ConfigurationManager.getConfiguration()
						.isInsertCOPYwhenCopied());
		getShowGridCheckBox().setSelected(
				ConfigurationManager.getConfiguration().isShowGrid());
		if (ConfigurationManager.getConfiguration().getArrowheadSize() == ConfigurationManager
				.getStandardConfiguration().getArrowheadSize())
			getArrowheadSizeJComboBox().setSelectedIndex(0);
		else if (0 < ConfigurationManager.getConfiguration().getArrowheadSize()
				&& ConfigurationManager.getConfiguration().getArrowheadSize() <= arrowHeadSize.length)
			getArrowheadSizeJComboBox().setSelectedItem(
					arrowHeadSize[ConfigurationManager.getConfiguration()
							.getArrowheadSize()]);
		if (ConfigurationManager.getConfiguration().getArrowWidth() == ConfigurationManager
				.getStandardConfiguration().getArrowWidth())
			getArrowWidthJComboBox().setSelectedIndex(0);
		else if (0 < ConfigurationManager.getConfiguration().getArrowWidth()
				&& ConfigurationManager.getConfiguration().getArrowWidth() <= arrowWidth.length)
			getArrowWidthJComboBox().setSelectedItem(
					arrowWidth[ConfigurationManager.getConfiguration()
							.getArrowWidth()]);
		getArrowFillHeadCheckBox().setSelected(
				ConfigurationManager.getConfiguration().isFillArrowHead());
		try {
			setSelectedLNF(ConfigurationManager.getConfiguration()
					.getLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setSelectedLNF(String lnf) {
		for (String key : lnfClasses.keySet())
			if (lnfClasses.get(key).equals(lnf)) {
				getLnfChooser().setSelectedItem(key);
				return;
			}

	}

	// ######################## GUI COMPONENTS ####################### */
	private JCheckBox getEditOnCreationCheckBox() {
		if (editOnCreationCheckBox == null) {
			editOnCreationCheckBox = new JCheckBox();
			editOnCreationCheckBox
					.setText(Messages
							.getString("Configuration.Editor.Panel.SmartEdit.Text.SmartNameEditing"));
			editOnCreationCheckBox
					.setToolTipText("<html>"
							+ Messages
									.getString("Configuration.Editor.Panel.SmartEdit.Text.SmartNameEditing.ToolTip")
							+ "</html>");
		}
		return editOnCreationCheckBox;
	}

	private JCheckBox getInsertCopyCheckBox() {
		if (insertCopyCheckBox == null) {
			insertCopyCheckBox = new JCheckBox();
			insertCopyCheckBox
					.setText(Messages
							.getString("Configuration.Editor.Panel.SmartEdit.Text.Add_copy"));
			insertCopyCheckBox
					.setToolTipText("<html>"
							+ Messages
									.getString("Configuration.Editor.Panel.SmartEdit.Text.Add_copy.ToolTip")
							+ "</html>");
		}

		return insertCopyCheckBox;
	}

	private JCheckBox getSmartEditingCheckBox() {
		if (smartEditingCheckBox == null) {
			smartEditingCheckBox = new JCheckBox();
			smartEditingCheckBox
					.setText(Messages
							.getString("Configuration.Editor.Panel.SmartEdit.Text.SmartArcDrawing"));
			smartEditingCheckBox
					.setToolTipText("<html>"
							+ Messages
									.getString("Configuration.Editor.Panel.SmartEdit.Text.SmartArcDrawing.ToolTip")
							+ "</html>");
		}
		return smartEditingCheckBox;
	}

	private JCheckBox getShowGridCheckBox() {
		if (showGridCheckBox == null) {
			showGridCheckBox = new JCheckBox();
			showGridCheckBox
					.setText(Messages
							.getString("Configuration.Editor.Panel.Grid.Text.ShowGrid"));
			showGridCheckBox
					.setToolTipText("<html>"
							+ Messages
									.getString("Configuration.Editor.Panel.Grid.Text.ShowGrid")
							+ "</html>");
		}

		return showGridCheckBox;
	}

	private JPanel getOnCreationPanel() {
		if (onCreationPanel == null) {
			onCreationPanel = new JPanel();
			onCreationPanel
					.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory
									.createTitledBorder(Messages
											.getString("Configuration.Editor.Panel.SmartEdit.Title")),
							BorderFactory.createEmptyBorder(5, 5, 10, 5)));
			onCreationPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.NORTH;
			c.fill = GridBagConstraints.HORIZONTAL;

			c.weightx = 1;

			c.gridx = 0;
			c.gridy = 0;
			onCreationPanel.add(getEditOnCreationCheckBox(), c);

			// c.weightx = 1;
			c.gridx = 0;
			c.gridy = 1;
			onCreationPanel.add(getSmartEditingCheckBox(), c);

			// c.weightx = 1;
			c.gridx = 0;
			c.gridy = 2;
			onCreationPanel.add(getInsertCopyCheckBox(), c);

			c.gridx = 0;
			c.gridy = 3;
			onCreationPanel.add(getShowGridCheckBox(), c);
		}
		return onCreationPanel;
	}

	private JPanel getGeneralPanel() {
		if (generalPanel == null) {
			generalPanel = new JPanel();
			generalPanel
					.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory
									.createTitledBorder(Messages
											.getString("Configuration.Editor.Panel.LNF.Title")),
							BorderFactory.createEmptyBorder(5, 5, 10, 5)));
			generalPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.NORTH;
			c.fill = GridBagConstraints.HORIZONTAL;

			c.weightx = 1;
			// c.insets = new Insets(0, 4, 0, 0);

			c.gridx = 0;
			c.gridy = 0;
			generalPanel.add(getLnfChooser(), c);
		}
		return generalPanel;
	}

	private JComboBox getLnfChooser() {
		if (lnfChooser == null) {
			lnfChooser = new JComboBox();
			for (LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
				lnfChooser.addItem(lafi.getName());
				lnfClasses.put(lafi.getName(), lafi.getClassName());
			}
			lnfChooser
					.setToolTipText("<html>"
							+ Messages
									.getString("Configuration.Editor.Panel.LNF.Tooltip")
							+ "</html>");
		}
		return lnfChooser;
	}

	private JPanel getArcPanel() {
		if (arcPanel == null) {
			arcPanel = new JPanel();
			arcPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory
							.createTitledBorder(Messages
									.getString("Configuration.Editor.Panel.Arcs.Title")),
					BorderFactory.createEmptyBorder(5, 5, 10, 5)));

			arcPanel.setLayout(new GridLayout(2, 2));

			arcPanel.add(getArrowWidthJLabel());

			arcPanel.add(getArrowWidthJComboBox());

			arcPanel.add(getArrowheadSizeJLabel());

			arcPanel.add(getArrowheadSizeJComboBox());
		}
		return arcPanel;
	}

	private JLabel getArrowWidthJLabel() {
		if (arrowWidthJLabel == null) {
			arrowWidthJLabel = new JLabel(
					Messages.getString("Configuration.Editor.Panel.Arcs.Label.LineWidth"));
			arrowWidthJLabel
					.setToolTipText("<html>"
							+ Messages
									.getString("Configuration.Editor.Panel.Arcs.Label.LineWidth.ToolTip")
							+ "</html>");
		}
		return arrowWidthJLabel;
	}

	private JComboBox getArrowWidthJComboBox() {
		if (arrowWidthJComboBox == null) {
			arrowWidthJComboBox = new JComboBox(arrowWidth);
			arrowWidthJComboBox
					.setToolTipText("<html>"
							+ Messages
									.getString("Configuration.Editor.Panel.Arcs.Label.LineWidth.ToolTip")
							+ "</html>");
		}
		return arrowWidthJComboBox;
	}

	private JLabel getArrowheadSizeJLabel() {
		if (arrowheadSizeJLabel == null) {
			arrowheadSizeJLabel = new JLabel(
					Messages.getString("Configuration.Editor.Panel.Arcs.Label.ArrowHeadSize"));
			arrowheadSizeJLabel
					.setToolTipText("<html>"
							+ Messages
									.getString("Configuration.Editor.Panel.Arcs.Label.ArrowHeadSize.ToolTip")
							+ "</html>");
		}
		return arrowheadSizeJLabel;
	}

	private JComboBox getArrowheadSizeJComboBox() {
		if (arrowheadSizeJComboBox == null) {
			arrowheadSizeJComboBox = new JComboBox(arrowHeadSize);
			arrowheadSizeJComboBox
					.setToolTipText("<html>"
							+ Messages
									.getString("Configuration.Editor.Panel.Arcs.Label.ArrowHeadSize.ToolTip")
							+ "</html>");
		}
		return arrowheadSizeJComboBox;
	}

	private JCheckBox getArrowFillHeadCheckBox() {
		if (arrowFillHeadCheckBox == null) {
			arrowFillHeadCheckBox = new JCheckBox();
			arrowFillHeadCheckBox
					.setText(Messages
							.getString("Configuration.Editor.Panel.Arcs.Text.FillArrowHeads"));
			arrowFillHeadCheckBox
					.setToolTipText("<html>"
							+ Messages
									.getString("Configuration.Editor.Panel.Arcs.Text.FillArrowHeads")
							+ "</html>");
		}
		return arrowFillHeadCheckBox;
	}

}