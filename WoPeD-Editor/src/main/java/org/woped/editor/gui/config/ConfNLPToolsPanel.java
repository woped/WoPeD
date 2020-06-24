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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.woped.core.config.ConfigurationManager;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 * <br>
 *         The <code>ConfLanguagePanel</code> is the
 *         <code>AbstractConfPanel</code> for the configuration of the language. <br>
 *         Created on: 26.11.2004 Last Change on: 14.11.2005
 */
@SuppressWarnings("serial")
public class ConfNLPToolsPanel extends AbstractConfPanel {
	private JCheckBox useBox = null;
	private JPanel enabledPanel = null;
	private JPanel settingsPanel = null;
	private JPanel settingsPanel_T2P = null;

	private JTextField serverURLText = null;
	private JLabel serverURLLabel = null;
	private JLabel serverPortLabel = null;
	private JTextField serverPortText = null;
	private JTextField managerPathText = null;
	private JLabel managerPathLabel = null;
	private WopedButton testButton = null;
	private WopedButton defaultButton = null;
	private JTextField serverURLText_T2P = null;
	private JLabel serverURLLabel_T2P = null;
	private JLabel serverPortLabel_T2P = null;
	private JTextField serverPortText_T2P = null;
	private JTextField managerPathText_T2P = null;
	private JLabel managerPathLabel_T2P = null;
	private WopedButton testButton_T2P = null;
	private WopedButton defaultButton_T2P = null;

	/**
	 * Constructor for ConfToolsPanel.
	 */
	public ConfNLPToolsPanel(String name) {
		super(name);
		initialize();
	}

	/**
	 * @see AbstractConfPanel#applyConfiguration()
	 */
	public boolean applyConfiguration() {
		boolean newsetting = useBox.isSelected();
		boolean oldsetting = ConfigurationManager.getConfiguration().getProcess2TextUse();

		if (newsetting != oldsetting) {
			ConfigurationManager.getConfiguration().setProcess2TextUse(newsetting);
			JOptionPane
					.showMessageDialog(
							this,
							Messages.getString("Configuration.P2T.Dialog.Restart.Message"),
							Messages.getString("Configuration.P2T.Dialog.Restart.Title"),
							JOptionPane.INFORMATION_MESSAGE);
		}
		ConfigurationManager.getConfiguration().setProcess2TextServerHost(
				getServerURLText().getText());
		ConfigurationManager.getConfiguration().setProcess2TextServerURI(
				getManagerPathText().getText());
		if (getServerPortText().getText().equals("")) {
			ConfigurationManager.getConfiguration()
					.setProcess2TextServerPort(0);
		} else
			ConfigurationManager.getConfiguration().setProcess2TextServerPort(
					Integer.parseInt(getServerPortText().getText()));
		ConfigurationManager.getConfiguration().setProcess2TextUse(
				useBox.isSelected());

		ConfigurationManager.getConfiguration().setText2ProcessServerHost(
				getServerURLText_T2P().getText());

		ConfigurationManager.getConfiguration().setText2ProcessServerURI(
				getManagerPathText_T2P().getText());

		if (getServerPortText_T2P().getText().equals("")) {
			ConfigurationManager.getConfiguration()
					.setText2ProcessServerPort(0);
		} else
			ConfigurationManager.getConfiguration().setText2ProcessServerPort(
					Integer.parseInt(getServerPortText_T2P().getText()));

		return true;
	}

	/**
	 * @see AbstractConfPanel#readConfiguration()
	 */
	public void readConfiguration() {

		getServerURLText().setText(
				ConfigurationManager.getConfiguration()
						.getProcess2TextServerHost());
		getManagerPathText().setText(
				ConfigurationManager.getConfiguration()
						.getProcess2TextServerURI());
		getServerPortText().setText(
				""
						+ ConfigurationManager.getConfiguration()
						.getProcess2TextServerPort());
		getUseBox().setSelected(
				ConfigurationManager.getConfiguration().getProcess2TextUse());

		getServerURLText_T2P().setText(
				ConfigurationManager.getConfiguration()
						.getText2ProcessServerHost());
		getManagerPathText_T2P().setText(
				ConfigurationManager.getConfiguration()
						.getText2ProcessServerURI());

		getServerPortText_T2P().setText(
				""
						+ ConfigurationManager.getConfiguration()
						.getText2ProcessServerPort());
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
		contentPanel.add(getEnabledPanel(), c);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		contentPanel.add(getSettingsPanel(), c);

		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 3;
		contentPanel.add(getSettingsPanel_T2P(), c);


		// dummy
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 1;
		c.gridy = 4;
		contentPanel.add(new JPanel(), c);

		setMainPanel(contentPanel);
	}

	// ################## GUI COMPONENTS #################### */

	private JTextField getServerURLText() {
		if (serverURLText == null) {
			serverURLText = new JTextField();
			serverURLText.setColumns(40);
			serverURLText.setEnabled(true);
			serverURLText.setToolTipText("<html>"
					+ Messages.getString("Configuration.P2T.Label.ServerHost")
					+ "</html>");
		}
		return serverURLText;
	}

	private JTextField getServerURLText_T2P() {
		if (serverURLText_T2P == null) {
			serverURLText_T2P = new JTextField();
			serverURLText_T2P.setColumns(40);
			serverURLText_T2P.setEnabled(true);
			serverURLText_T2P.setToolTipText("<html>"
					+ Messages.getString("Configuration.T2P.Label.ServerHost")
					+ "</html>");
		}
		return serverURLText_T2P;
	}





	private JPanel getEnabledPanel() {
		if (enabledPanel == null) {
			enabledPanel = new JPanel();
			enabledPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.WEST;

			enabledPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages
							.getTitle("Configuration.P2T.Enabled.Panel")),
					BorderFactory.createEmptyBorder(10, 10, 10, 10)));

			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 0;
			enabledPanel.add(getUseBox(), c);

		}
		return enabledPanel;
	}

	private JPanel getSettingsPanel() {
		if (settingsPanel == null) {
			settingsPanel = new JPanel();
			settingsPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.WEST;

			settingsPanel
					.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory
									.createTitledBorder(Messages
											.getString("Configuration.P2T.Settings.Panel.Title")),
							BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 0;
			settingsPanel.add(getServerURLLabel(), c);

			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 2;
			settingsPanel.add(getServerURLText(), c);

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
			c.gridx = 2;
			c.gridy = 1;
			settingsPanel.add(getTestButton(), c);

			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 2;
			settingsPanel.add(getManagerPathLabel(), c);

			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 2;
			c.gridwidth = 2;
			settingsPanel.add(getManagerPathText(), c);

			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 4;
			settingsPanel.add(getDefaultButton(), c);

		}

		settingsPanel.setVisible(getUseBox().isSelected());
		return settingsPanel;
	}

	private JPanel getSettingsPanel_T2P() {
		if (settingsPanel_T2P == null) {
			settingsPanel_T2P = new JPanel();
			settingsPanel_T2P.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.WEST;

			settingsPanel_T2P
					.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory
									.createTitledBorder(Messages
											.getString("Configuration.T2P.Settings.Panel.Title")),
							BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 0;
			settingsPanel_T2P.add(getServerURLLabel_T2P(), c);

			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 2;
			settingsPanel_T2P.add(getServerURLText_T2P(), c);

			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			settingsPanel_T2P.add(getServerPortLabel_T2P(), c);

			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 1;
			settingsPanel_T2P.add(getServerPortText_T2P(), c);

			c.weightx = 1;
			c.gridx = 2;
			c.gridy = 1;
			settingsPanel_T2P.add(getTestButton_T2P(), c);

			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 2;
			settingsPanel_T2P.add(getManagerPathLabel_T2P(), c);

			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 2;
			c.gridwidth = 2;
			settingsPanel_T2P.add(getManagerPathText_T2P(), c);

			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 4;
			settingsPanel_T2P.add(getDefaultButton_T2P(), c);

		}

		settingsPanel_T2P.setVisible(getUseBox_T2P().isSelected());
		return settingsPanel_T2P;
	}

	class CheckboxListener implements ItemListener {

		public void itemStateChanged(ItemEvent ie) {
			JCheckBox jcb = (JCheckBox) ie.getSource();
			if (jcb == useBox) {
				getSettingsPanel().setVisible(jcb.isSelected());
				getSettingsPanel_T2P().setVisible(jcb.isSelected());
			}
		}
	}

	private JLabel getServerURLLabel() {
		if (serverURLLabel == null) {
			serverURLLabel = new JLabel("<html>"
					+ Messages.getString("Configuration.P2T.Label.ServerHost")
					+ "</html>");
			serverURLLabel.setHorizontalAlignment(JLabel.RIGHT);
		}
		return serverURLLabel;
	}

	private JLabel getServerURLLabel_T2P() {
		if (serverURLLabel_T2P == null) {
			serverURLLabel_T2P = new JLabel("<html>"
					+ Messages.getString("Configuration.T2P.Label.ServerHost")
					+ "</html>");
			serverURLLabel_T2P.setHorizontalAlignment(JLabel.RIGHT);
		}
		return serverURLLabel_T2P;
	}

	private JLabel getServerPortLabel() {
		if (serverPortLabel == null) {
			serverPortLabel = new JLabel("<html>"
					+ Messages.getString("Configuration.P2T.Label.ServerPort")
					+ "</html>");
			serverPortLabel.setHorizontalAlignment(JLabel.RIGHT);
		}
		return serverPortLabel;
	}

	private JLabel getServerPortLabel_T2P() {
		if (serverPortLabel_T2P == null) {
			serverPortLabel_T2P = new JLabel("<html>"
					+ Messages.getString("Configuration.T2P.Label.ServerPort")
					+ "</html>");
			serverPortLabel_T2P.setHorizontalAlignment(JLabel.RIGHT);
		}
		return serverPortLabel_T2P;
	}

	private JTextField getServerPortText() {
		if (serverPortText == null) {
			serverPortText = new JTextField();
			serverPortText.setColumns(4);
			serverPortText.setEnabled(true);
			serverPortText.setToolTipText("<html>"
					+ Messages.getString("Configuration.P2T.Label.ServerPort")
					+ "</html>");
		}
		return serverPortText;
	}

	private JTextField getServerPortText_T2P() {
		if (serverPortText_T2P == null) {
			serverPortText_T2P = new JTextField();
			serverPortText_T2P.setColumns(4);
			serverPortText_T2P.setEnabled(true);
			serverPortText_T2P.setToolTipText("<html>"
					+ Messages.getString("Configuration.T2P.Label.ServerPort")
					+ "</html>");
		}
		return serverPortText_T2P;
	}

	private JCheckBox getUseBox() {
		if (useBox == null) {
			useBox = new JCheckBox(
					Messages.getString("Configuration.P2T.Label.Use"));
			useBox.setEnabled(true);
			useBox.setToolTipText("<html>"
					+ Messages.getString("Configuration.P2T.Label.Use")
					+ "</html>");
			CheckboxListener cbl = new CheckboxListener();
			useBox.addItemListener(cbl);
		}

		return useBox;
	}

	private JCheckBox getUseBox_T2P() {
		if (useBox == null) {
			useBox = new JCheckBox(
					Messages.getString("Configuration.P2T.Label.Use"));
			useBox.setEnabled(true);
			useBox.setToolTipText("<html>"
					+ Messages.getString("Configuration.T2P.Label.Use")
					+ "</html>");
			CheckboxListener cbl = new CheckboxListener();
			useBox.addItemListener(cbl);
		}

		return useBox;
	}

	private JLabel getManagerPathLabel() {
		if (managerPathLabel == null) {
			managerPathLabel = new JLabel("<html>"
					+ Messages.getString("Configuration.P2T.Label.ServerURI")
					+ "</html>");
			managerPathLabel.setHorizontalAlignment(JLabel.RIGHT);
		}
		return managerPathLabel;
	}

	private JLabel getManagerPathLabel_T2P() {
		if (managerPathLabel_T2P == null) {
			managerPathLabel_T2P = new JLabel("<html>"
					+ Messages.getString("Configuration.T2P.Label.ServerURI")
					+ "</html>");
			managerPathLabel_T2P.setHorizontalAlignment(JLabel.RIGHT);
		}
		return managerPathLabel_T2P;
	}

	private JTextField getManagerPathText() {
		if (managerPathText == null) {
			managerPathText = new JTextField();
			managerPathText.setColumns(40);
			managerPathText.setEnabled(true);
			managerPathText.setToolTipText("<html>"
					+ Messages.getString("Configuration.P2T.Label.ServerURI")
					+ "</html>");
		}
		return managerPathText;
	}

	private JTextField getManagerPathText_T2P() {
		if (managerPathText_T2P == null) {
			managerPathText_T2P = new JTextField();
			managerPathText_T2P.setColumns(40);
			managerPathText_T2P.setEnabled(true);
			managerPathText_T2P.setToolTipText("<html>"
					+ Messages.getString("Configuration.T2P.Label.ServerURI")
					+ "</html>");
		}
		return managerPathText_T2P;
	}

	private WopedButton getTestButton() {
		if (testButton == null) {
			testButton = new WopedButton();
			testButton.setText(Messages.getTitle("Button.TestConnection"));
			testButton.setIcon(Messages.getImageIcon("Button.TestConnection"));
			testButton.setMnemonic(Messages
					.getMnemonic("Button.TestConnection"));
			testButton.setPreferredSize(new Dimension(160, 25));
			testButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					testProcess2TextConnection();
				}
			});

		}

		return testButton;
	}

	private WopedButton getTestButton_T2P() {
		if (testButton_T2P == null) {
			testButton_T2P = new WopedButton();
			testButton_T2P.setText(Messages.getTitle("Button.TestConnection"));
			testButton_T2P.setIcon(Messages.getImageIcon("Button.TestConnection"));
			testButton_T2P.setMnemonic(Messages
					.getMnemonic("Button.TestConnection"));
			testButton_T2P.setPreferredSize(new Dimension(160, 25));
			testButton_T2P.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					testText2ProcessConnection();
				}
			});

		}

		return testButton_T2P;
	}

	private WopedButton getDefaultButton() {
		if (defaultButton == null) {
			defaultButton = new WopedButton();
			defaultButton.setText(Messages.getTitle("Button.SetToDefault"));
			defaultButton.setPreferredSize(new Dimension(200, 25));
			defaultButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setDefaultValues();
				}
			});
		}
		return defaultButton;
	}

	private WopedButton getDefaultButton_T2P() {
		if (defaultButton_T2P == null) {
			defaultButton_T2P = new WopedButton();
			defaultButton_T2P.setText(Messages.getTitle("Button.SetToDefault"));
			defaultButton_T2P.setPreferredSize(new Dimension(200, 25));
			defaultButton_T2P.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setDefaultValues_T2P();
				}
			});
		}
		return defaultButton_T2P;
	}

	private void testProcess2TextConnection() {
		URL url = null;
		String connection = "http://" + getServerURLText().getText() + ":"
				+ getServerPortText().getText()
				+ getManagerPathText().getText();
		String arg[] = {connection, ""};

		try {
			url = new URL(connection);
			URLConnection urlConnection = url.openConnection();

			if (urlConnection.getContent() != null) {
				arg[1] = "P2T";
				JOptionPane
						.showMessageDialog(
								this.getSettingsPanel(),
								Messages.getString("Paraphrasing.Webservice.Success.Message", arg),
								Messages.getString("Paraphrasing.Webservice.Success.Title"),
								JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (MalformedURLException mue) {
			JOptionPane
					.showMessageDialog(
							this.getSettingsPanel(),
							Messages.getString("Paraphrasing.Webservice.Error.Webserviceexception.Message", arg),
							Messages.getString("Paraphrasing.Webservice.Error.Title"),
							JOptionPane.ERROR_MESSAGE);
		} catch (IOException ex) {
			JOptionPane
					.showMessageDialog(
							this.getSettingsPanel(),
							Messages.getString("Paraphrasing.Webservice.Error.Webserviceexception.Message", arg),
							Messages.getString("Paraphrasing.Webservice.Error.Title"),
							JOptionPane.ERROR_MESSAGE);
		}

	}

	private void testText2ProcessConnection() {
		URL url = null;
		String connection = "http://" + getServerURLText_T2P().getText() + ":"
				+ getServerPortText_T2P().getText()
				+ getManagerPathText_T2P().getText()
				+ "";
		String arg[] = {connection, ""};

		try {
			url = new URL(connection);
			URLConnection urlConnection = url.openConnection();
			if (urlConnection.getContent() != null) {
				arg[1] = "T2P";
				JOptionPane
						.showMessageDialog(
								this.getSettingsPanel_T2P(),
								Messages.getString("Paraphrasing.Webservice.Success.Message", arg),
								Messages.getString("Paraphrasing.Webservice.Success.Title"),
								JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (MalformedURLException mue) {
			JOptionPane
					.showMessageDialog(
							this.getSettingsPanel_T2P(),
							Messages.getString("Paraphrasing.Webservice.Error.Webserviceexception.Message", arg),
							Messages.getString("Paraphrasing.Webservice.Error.Title"),
							JOptionPane.ERROR_MESSAGE);
		} catch (IOException ex) {
			JOptionPane
					.showMessageDialog(
							this.getSettingsPanel_T2P(),
							Messages.getString("Paraphrasing.Webservice.Error.Webserviceexception.Message", arg),
							Messages.getString("Paraphrasing.Webservice.Error.Title"),
							JOptionPane.ERROR_MESSAGE);
		}

	}

	private void setDefaultValues() {
		getServerURLText().setText(
				ConfigurationManager.getStandardConfiguration()
						.getProcess2TextServerHost());
		getManagerPathText().setText(
				ConfigurationManager.getStandardConfiguration()
						.getProcess2TextServerURI());
		getServerPortText().setText(
				""
						+ ConfigurationManager.getStandardConfiguration()
						.getProcess2TextServerPort());
	}

	private void setDefaultValues_T2P() {
		getServerURLText_T2P().setText(
				ConfigurationManager.getStandardConfiguration()
						.getText2ProcessServerHost());
		getManagerPathText_T2P().setText(
				ConfigurationManager.getStandardConfiguration()
						.getText2ProcessServerURI());
		getServerPortText_T2P().setText(
				""
						+ ConfigurationManager.getStandardConfiguration()
						.getText2ProcessServerPort());
	}

}