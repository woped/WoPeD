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

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * <br>
 *         The <code>ConfLanguagePanel</code> is the
 *         <code>AbstractConfPanel</code> for the configuration of the language. <br>
 *         Created on: 26.11.2004 Last Change on: 14.11.2005
 */
@SuppressWarnings("serial")
public class ConfBusinessDashboardPanel extends AbstractConfPanel {

	private JPanel settingsPanel = null;
	private JCheckBox useByDefaultBox = null;
//	private JTextField serverURLText = null;
//	private JLabel serverURLLabel = null;
	private JLabel serverPortLabel = null;
	private JTextField serverPortText = null;

	private JLabel maxValuesLabel = null;
	private JTextField maxValuesText = null;
	
	/**
	 * Constructor for ConfToolsPanel.
	 */
	public ConfBusinessDashboardPanel(String name) {
		super(name);
		initialize();
	}

	/**
	 * @see AbstractConfPanel#applyConfiguration()
	 */
	public boolean applyConfiguration() {
		boolean newsetting = useByDefaultBox.isSelected();
		boolean oldsetting = ConfigurationManager.getConfiguration().getBusinessDashboardUseByDefault();

		if (newsetting != oldsetting) {

			ConfigurationManager.getConfiguration().setBusinessDashboardServerPort(
					Integer.parseInt(getServerPortText().getText()));
			
			ConfigurationManager.getConfiguration().setBusinessDashboardUseByDefault(
					useByDefaultBox.isSelected());	
			
			ConfigurationManager.getConfiguration().setBusinessDashboardMaxValues(
					Integer.parseInt(getMaxValuesText().getText()));
		}
		
		return true;
	}

	/**
	 * @see AbstractConfPanel#readConfiguration()
	 */
	public void readConfiguration() {
		getServerPortText().setText(""+
				ConfigurationManager.getConfiguration().getBusinessDashboardServerPort());
	
		getUseByDefaultBox().setSelected(
				ConfigurationManager.getConfiguration().getBusinessDashboardUseByDefault());
		
		getMaxValuesText().setText("" + 
				ConfigurationManager.getConfiguration().getBusinessDashboardMaxValues());
	}

	private void initialize() {
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;

	
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		contentPanel.add(getSettingsPanel(), c);

		// dummy
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 1;
		c.gridy = 2;
		contentPanel.add(new JPanel(), c);

		setMainPanel(contentPanel);
	}

	public JPanel getSettingsPanel() {
		if (settingsPanel == null) {
			settingsPanel = new JPanel();
			settingsPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.WEST;

			settingsPanel
					.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory
									.createTitledBorder(Messages
											.getString("Configuration.BusinessDashboard.ChangeSettings.Panel.Title")),
							BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			settingsPanel.add(getServerPortLabel(), c);

			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 1;
			settingsPanel.add(getServerPortText(), c);
			
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 1;
			settingsPanel.add(getMaxValuesLabel(), c);

			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 2;
			settingsPanel.add(getMaxValuesText(), c);	
			
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 0;
		
			settingsPanel.add(getUseByDefaultBox(),c);
		}

//		settingsPanel.setVisible(getUseByDefaultBox().isSelected());
		return settingsPanel;
	}

	public void setSettingsPanel(JPanel settingsPanel) {
		this.settingsPanel = settingsPanel;
	}

	public JLabel getServerPortLabel() {
		if (serverPortLabel == null) {
			serverPortLabel = new JLabel("<html>"
					+ Messages.getString("Configuration.BusinessDashboard.Label.ServerPort")
					+ "</html>");
			serverPortLabel.setHorizontalAlignment(JLabel.RIGHT);
		}
		return serverPortLabel;
	}

	public void setServerPortLabel(JLabel serverPortLabel) {
		this.serverPortLabel = serverPortLabel;
	}

	public JTextField getServerPortText() {
		if (serverPortText == null) {
			serverPortText = new JTextField();
			serverPortText.setColumns(4);
			serverPortText.setEnabled(false);
			serverPortText.setToolTipText("<html>"
					+ Messages.getString("Configuration.BusinessDashboard.Label.ServerPort")
					+ "</html>");
		}
		return serverPortText;
	}

	public JCheckBox getUseByDefaultBox() {
		if (useByDefaultBox == null) {
			useByDefaultBox = new JCheckBox(
					Messages.getString("Configuration.BusinessDashboard.Label.UseByDefault"));
			useByDefaultBox.setEnabled(true);
			useByDefaultBox.setToolTipText("<html>"
					+ Messages.getString("Configuration.BusinessDashboard.Label.UseByDefault")
					+ "</html>");
			
		}

		return useByDefaultBox;
	}

	public void setUseByDefaultBox(JCheckBox useByDefaultBox) {
		this.useByDefaultBox = useByDefaultBox;
	}

	public JLabel getMaxValuesLabel() {
		if (maxValuesLabel == null) {
			maxValuesLabel = new JLabel("<html>"
					+ Messages.getString("Configuration.BusinessDashboard.Label.MaxValues")
					+ "</html>");
			maxValuesLabel.setHorizontalAlignment(JLabel.RIGHT);
		}
		return maxValuesLabel;
	}

	public void setMaxValuesLabel(JLabel maxValuesLabel) {
		this.maxValuesLabel = maxValuesLabel;
	}

	public JTextField getMaxValuesText() {
		if (maxValuesText == null) {
			maxValuesText = new JTextField();
			maxValuesText.setColumns(4);
			maxValuesText.setEnabled(true);
			maxValuesText.setToolTipText("<html>"
					+ 1000 
					+ "</html>");
		}
		return maxValuesText;
	}

	public void setMaxValuesText(JTextField maxValuesText) {
		this.maxValuesText = maxValuesText;
	}



	// ################## GUI COMPONENTS #################### */
}