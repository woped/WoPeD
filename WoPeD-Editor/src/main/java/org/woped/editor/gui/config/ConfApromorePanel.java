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

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.woped.apromore.ApromoreAccess;
import org.woped.apromore.ApromorePasswordSecurity;
import org.woped.config.ApromoreServer;
import org.woped.core.config.ConfigurationManager;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         The <code>ConfApromorePanel</code> is the
 *         <code>AbstractConfPanel</code> for the configuration of the apromore
 *         servers. <br>
 *         Created on: 26.11.2004 Last Change on: 15.01.2015 by Sascha Schneider
 */
@SuppressWarnings("serial")
public class ConfApromorePanel extends AbstractConfPanel {

	// Panels
	private JPanel enabledPanel = null;
	private JPanel listPanel = null;
	private JPanel buttonPanel = null;
	private JPanel settingsPanel = null;
	private JPanel proxyPanel = null;

	// Boxes
	private JComboBox<String> serverComboBox = null;
	private JCheckBox useBox = null;
	private JCheckBox useProxyBox = null;

	// Buttons
	private WopedButton addButton = null;
	private WopedButton changeButton = null;
	private WopedButton deleteButton = null;
	private WopedButton saveButton = null;
	private WopedButton testButton = null;
	// Test DEUTIZ
	private WopedButton cancelButton = null;

	// Labels
	private JLabel serverNameLabel = null;
	private JLabel serverNameLabelUser = null;

	private JLabel serverURLLabel = null;
	private JLabel serverPortLabel = null;
	private JLabel managerPathLabel = null;
	private JLabel usernameLabel = null;
	private JLabel passwordLabel = null;
	private JLabel useProxyLabel = null;
	private JLabel proxyNameLabel = null;
	private JLabel proxyPortLabel = null;

	// TextFields
	private JTextField serverNameText = null;
	private JTextField serverNameTextUser = null;
	private JTextField serverURLText = null;
	private JTextField serverPortText = null;
	private JTextField managerPathText = null;
	private JTextField usernameText = null;
	private JPasswordField passwordText = null;
	private JTextField proxyNameText = null;
	private JTextField proxyPortText = null;

	private int currentIndex;
	private String name;
	private String url;
	private int port;
	private String path;
	private String user;
	private String password;
	private boolean useProxy;
	private String proxyUrl;
	private int proxyPort;
	private String[] serverBoxItems;
	private ApromoreServer[] servers;
	private boolean addServer = false;
	private boolean changeServer = false;
	private boolean inputOK = true;
	private int apromoreServerID;
	private int selected;
	private boolean nameAlreadyInUse = false;

	/**
	 * Constructor for ConfToolsPanel.
	 */
	public ConfApromorePanel(String name) {
		super(name);
		initialize();

	}

	/**
	 * @see AbstractConfPanel#applyConfiguration() saves actual configuration in
	 *      xml-file
	 */
	public boolean applyConfiguration() {

		boolean newsetting = useBox.isSelected();
		boolean oldsetting = ConfigurationManager.getConfiguration().getApromoreUse();

		if (newsetting != oldsetting) {
			ConfigurationManager.getConfiguration().setApromoreUse(newsetting);
			JOptionPane.showMessageDialog(this, Messages.getString("Configuration.Apromore.Dialog.Restart.Message"),
					Messages.getString("Configuration.Apromore.Dialog.Restart.Title"), JOptionPane.INFORMATION_MESSAGE);
		}

		if (saveButton.isEnabled()) {
			int result = JOptionPane.showConfirmDialog(this,
					Messages.getString("Configuration.Apromore.Dialog.SaveChanges.Message"),
					Messages.getString("Configuration.Apromore.Dialog.SaveChanges.Title"), JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {

				if (checkInput()) {

					if (getChangeServer() == true) {
						changeApromoreServer(getApromoreServerID());
						ConfigurationManager.getConfiguration().saveConfig();
						saveConfig();
					}
					if (getAddServer() == true) {
						addApromoreServer(getApromoreServerID());
						ConfigurationManager.getConfiguration().saveConfig();
						saveConfig();
					}

				}

			} else {

			}

		} else {
			
			checkInput();
			
		}

		return true;

	}

	public void saveConfig() {

		if (ConfigurationManager.getConfiguration().isSetApromoreServers()) {
			currentIndex = ConfigurationManager.getConfiguration().getCurrentApromoreIndex();
			serverComboBox.setSelectedIndex(currentIndex);
			servers = ConfigurationManager.getConfiguration().getApromoreServers();
			setTextFields();
		} else {
			setDefaultApromoreServer();
			servers = ConfigurationManager.getConfiguration().getApromoreServers();
			ConfigurationManager.getConfiguration().setApromoreUse(useBox.isSelected());
			ConfigurationManager.getConfiguration().setCurrentApromoreIndex(serverComboBox.getSelectedIndex());
			ConfigurationManager.getConfiguration().setApromoreServerName(// getServerNameText().getText()
																			// +
					getServerNameTextUser().getText());
			ConfigurationManager.getConfiguration().setApromoreServerURL(getServerURLText().getText());
			ConfigurationManager.getConfiguration()
					.setApromoreServerPort(Integer.parseInt(getServerPortText().getText()));
			ConfigurationManager.getConfiguration().setApromoreManagerPath(getManagerPathText().getText());
			ConfigurationManager.getConfiguration().setApromoreUsername(getUsernameText().getText());
			try {
				password = ApromorePasswordSecurity.encode(passwordText.getPassword().toString());
				ConfigurationManager.getConfiguration().setApromorePassword(password);
			} catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException
					| NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
				e.printStackTrace();
			}
			ConfigurationManager.getConfiguration().setApromoreUseProxy(useProxyBox.isSelected());
			ConfigurationManager.getConfiguration().setApromoreProxyName(getProxyNameText().getText());
			ConfigurationManager.getConfiguration()
					.setApromoreProxyPort(Integer.parseInt(getProxyPortText().getText()));
		}

	}

	/**
	 * @see AbstractConfPanel#readConfiguration() reads actual configuration
	 *      from xml-file
	 */
	public void readConfiguration() {
		getUseBox().setSelected(ConfigurationManager.getConfiguration().getApromoreUse());
		if (ConfigurationManager.getConfiguration().isSetApromoreServers())
			getServersComboBox().setSelectedIndex(ConfigurationManager.getConfiguration().getCurrentApromoreIndex());

		getServerNameTextUser().setText(ConfigurationManager.getConfiguration().getApromoreServerName());
		String sname = ConfigurationManager.getConfiguration().getApromoreServerName();

		getServerNameText().setText(sname.substring(sname.indexOf("@") + 1));
		getServerNameTextUser().setText(ConfigurationManager.getConfiguration().getApromoreServerName());
		getServerURLText().setText(ConfigurationManager.getConfiguration().getApromoreServerURL());
		getServerPortText().setText("" + ConfigurationManager.getConfiguration().getApromoreServerPort());
		getManagerPathText().setText(ConfigurationManager.getConfiguration().getApromoreManagerPath());
		getUsernameText().setText(ConfigurationManager.getConfiguration().getApromoreUsername());
		if (ConfigurationManager.getConfiguration().isSetApromorePassword() == true)
			try {
				getPasswordText().setText(
						ApromorePasswordSecurity.decode(ConfigurationManager.getConfiguration().getApromorePassword()));
			} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException
					| BadPaddingException | IOException e) {
				e.printStackTrace();
			}
		getUseProxyCheckbox().setSelected(ConfigurationManager.getConfiguration().getApromoreUseProxy());
		if (getUseProxyCheckbox().isSelected() == true && getUseBox().isSelected() == true) {
			proxyPanel.setVisible(true);
		} else {
			proxyPanel.setVisible(false);
		}
		getProxyNameText().setText(ConfigurationManager.getConfiguration().getApromoreProxyName());
		getProxyPortText().setText("" + ConfigurationManager.getConfiguration().getApromoreProxyPort());
		getSaveButton().setEnabled(false);
		getCancelButton().setEnabled(false);

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
		contentPanel.add(getServerListPanel(), c);
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 2;
		contentPanel.add(getSettingsPanel(), c);
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 3;
		contentPanel.add(getProxyPanel(), c);
		// dummy

		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 1;
		c.gridy = 4;
		contentPanel.add(new JPanel(), c);
		setMainPanel(contentPanel);

		// saveConfig();
		// updateSettingsPanel();

	}

	// ################## GUI COMPONENTS #################### */

	// Panels

	private JPanel getEnabledPanel() {
		if (enabledPanel == null) {
			enabledPanel = new JPanel();
			enabledPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.WEST;
			enabledPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages.getTitle("Configuration.Apromore.Enabled.Panel")),
					BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			c.weightx = 1;
			c.gridx = 1;
			c.gridy = 0;
			enabledPanel.add(getUseBox(), c);
		}
		return enabledPanel;
	}

	private JPanel getServerListPanel() {
		if (listPanel == null) {
			listPanel = new JPanel();
			listPanel.setLayout(new GridBagLayout());
			GridBagConstraints d = new GridBagConstraints();
			d.anchor = GridBagConstraints.CENTER;
			listPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory
							.createTitledBorder(Messages.getString("Configuration.Apromore.ServerList.Panel.Title")),
					BorderFactory.createEmptyBorder(10, 10, 10, 10)));

			d.weightx = 1;
			d.ipady = 5;
			d.gridx = 0;
			d.gridy = 0;
			listPanel.add(getServersComboBox(), d);
			d.weightx = 1;
			d.gridx = 0;
			d.gridy = 1;
			listPanel.add(getButtonPanel(), d);
		}
		listPanel.setVisible(getUseBox().isSelected());
		return listPanel;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			GridBagConstraints e = new GridBagConstraints();
			e.anchor = GridBagConstraints.CENTER;
			e.weightx = 0;
			e.gridx = 0;
			e.gridy = 0;
			buttonPanel.add(getAddButton(), e);
			e.weightx = 0;
			e.gridx = 1;
			e.gridy = 0;
			buttonPanel.add(getChangeButton(), e);
			e.weightx = 0;
			e.gridx = 2;
			e.gridy = 0;
			buttonPanel.add(getDeleteButton(), e);
			e.weightx = 0;
			e.gridx = 3;
			e.gridy = 0;
			buttonPanel.add(getSaveButton(), e);
			e.weightx = 0;
			e.gridx = 4;
			e.gridy = 0;
			buttonPanel.add(getCancelButton(), e);
			e.weightx = 0;
			e.gridx = 5;
			e.gridy = 0;
			buttonPanel.add(getTestButton(), e);
		}
		return buttonPanel;
	}

	private JPanel getSettingsPanel() {
		if (settingsPanel == null) {
			settingsPanel = new JPanel();
			settingsPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.anchor = GridBagConstraints.WEST;
			settingsPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(Messages.getString("Configuration.Apromore.Settings.Panel.Title")),
					BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			c.weightx = 0.5;
			// c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 0;
			settingsPanel.add(getServerNameLabelUser(), c);
			// c.weightx = 1;
			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 1;
			// settingsPanel.add(getServerNamePanel(), c);
			settingsPanel.add(getServerNameTextUser(), c);
			// c.weightx = 1;
			c.gridx = 0;
			c.gridy = 1;
			settingsPanel.add(getServerNameLabel(), c);
			// c.weightx = 1;
			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 1;
			// settingsPanel.add(getServerNamePanel(), c);
			settingsPanel.add(getServerNameText(), c);
			// c.weightx = 1;
			c.gridx = 0;
			c.gridy = 2;
			settingsPanel.add(getServerURLLabel(), c);
			// c.weightx = 1;
			c.gridx = 1;
			c.gridy = 2;
			c.gridwidth = 1;
			settingsPanel.add(getServerURLText(), c);
			// c.weightx = 1;
			c.gridx = 0;
			c.gridy = 3;
			c.gridwidth = 1;
			settingsPanel.add(getServerPortLabel(), c);
			// c.weightx = 1;
			c.gridx = 1;
			c.gridy = 3;
			settingsPanel.add(getServerPortText(), c);

			// c.weightx = 1;
			c.gridx = 0;
			c.gridy = 4;
			settingsPanel.add(getManagerPathLabel(), c);
			// c.weightx = 1;
			c.gridx = 1;
			c.gridy = 4;
			c.gridwidth = 1;
			settingsPanel.add(getManagerPathText(), c);
			// c.weightx = 1;
			c.gridx = 0;
			c.gridy = 5;
			c.gridwidth = 1;
			settingsPanel.add(getUsernameLabel(), c);
			// c.weightx = 1;
			c.gridx = 1;
			c.gridy = 5;
			c.gridwidth = 1;
			settingsPanel.add(getUsernameText(), c);
			// c.weightx = 1;
			c.gridx = 0;
			c.gridy = 6;
			c.gridwidth = 1;
			settingsPanel.add(getPasswordLabel(), c);
			// c.weightx = 1;
			c.gridx = 1;
			c.gridy = 6;
			c.gridwidth = 1;
			settingsPanel.add(getPasswordText(), c);
			// c.weightx = 1;
			c.gridx = 0;
			c.gridy = 7;
			c.gridwidth = 1;
			settingsPanel.add(getUseProxyLabel(), c);
			// c.weightx = 1;
			c.gridx = 1;
			c.gridy = 7;
			c.gridwidth = 1;
			settingsPanel.add(getUseProxyCheckbox(), c);
		}
		settingsPanel.setVisible(getUseBox().isSelected());
		return settingsPanel;
	}

	private JPanel getProxyPanel() {
		if (proxyPanel == null) {
			proxyPanel = new JPanel();
			proxyPanel.setLayout(new GridBagLayout());
			GridBagConstraints d = new GridBagConstraints();
			d.anchor = GridBagConstraints.WEST;
			proxyPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory
							.createTitledBorder(Messages.getString("Configuration.Apromore.ProxySettings.Panel.Title")),
					BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			d.weightx = 0.5;
			// d.fill = GridBagConstraints.HORIZONTAL;
			d.gridx = 0;
			d.gridy = 0;
			proxyPanel.add(getProxyNameLabel(), d);
			// d.weightx = 1;
			d.gridx = 1;
			d.gridy = 0;
			proxyPanel.add(getProxyNameText(), d);
			// d.weightx = 1;
			// d.gridx = 2;
			// d.gridy = 0;
			// proxyPanel.add(new JPanel(), d);
			// d.weightx = 1;
			d.gridx = 0;
			d.gridy = 1;
			d.gridwidth = 1;
			proxyPanel.add(getProxyPortLabel(), d);
			// d.weightx = 1;
			d.gridx = 1;
			d.gridy = 1;
			d.gridwidth = 1;
			proxyPanel.add(getProxyPortText(), d);
		}
		if (getUseProxyCheckbox().isSelected() == true && getUseBox().isSelected() == true) {
			proxyPanel.setVisible(true);
		} else {
			proxyPanel.setVisible(false);
		}

		return proxyPanel;
	}

	// Boxes

	private JComboBox<String> getServersComboBox() {

		if (serverComboBox == null) {
			if (!ConfigurationManager.getConfiguration().isSetApromoreServers()) {
				String[] serverNames = new String[1];
				serverNames[0] = ConfigurationManager.getConfiguration().getApromoreServerName();
				serverComboBox = new JComboBox<String>(serverNames);
				serverComboBox.setPreferredSize(new Dimension(500, 25));
				serverComboBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						updateSettingsPanel();
					}
				});
			} else {
				servers = ConfigurationManager.getConfiguration().getApromoreServers();
				serverBoxItems = ConfigurationManager.getConfiguration().getApromoreServerNames();
				for (int i = 0; i < servers.length; i++) {
					serverBoxItems[i] = servers[i].getApromoreServerName();
				}
				serverComboBox = new JComboBox<String>(serverBoxItems);
				serverComboBox.setPreferredSize(new Dimension(500, 25));
				serverComboBox.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						updateSettingsPanel();
					}

				});
			}
		}

		return serverComboBox;
	}

	private JCheckBox getUseBox() {
		if (useBox == null) {
			useBox = new JCheckBox(Messages.getString("Configuration.Apromore.Label.Use"));
			useBox.setEnabled(true);
			useBox.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.Use") + "</html>");
			CheckboxListener cbl = new CheckboxListener();
			useBox.addItemListener(cbl);
		}
		return useBox;
	}

	private JCheckBox getUseProxyCheckbox() {
		if (useProxyBox == null) {
			useProxyBox = new JCheckBox();
			useProxyBox.setEnabled(false);
			useProxyBox
					.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.UseProxy") + "</html>");
			CheckboxListener cbl = new CheckboxListener();
			useProxyBox.addItemListener(cbl);
		}
		return useProxyBox;
	}

	// Buttons

	private WopedButton getAddButton() {
		if (addButton == null) {
			addButton = new WopedButton();
			addButton.setText(Messages.getTitle("Button.AddServer"));
			addButton.setPreferredSize(new Dimension(100, 25));
			addButton.setMnemonic(Messages.getMnemonic("Button.AddServer"));
			addButton.setIcon(Messages.getImageIcon("Button.AddServer"));
			addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setAddServer(true);
					apromoreServerID = manageApromoreServerID();
					setApromoreServerID(apromoreServerID);
					setButtonsToAddOrChange();
					clearTextFields();
					serverPortText.setText("9000");
					managerPathText.setText("manager/services/manager");
				}
			});
		}
		return addButton;
	}

	private WopedButton getChangeButton() {
		if (changeButton == null) {
			changeButton = new WopedButton();
			changeButton.setText(Messages.getTitle("Button.ChangeSettings"));
			changeButton.setPreferredSize(new Dimension(100, 25));
			changeButton.setMnemonic(Messages.getMnemonic("Button.ChangeSettings"));
			changeButton.setIcon(Messages.getImageIcon("Button.ChangeSettings"));
			changeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setChangeServer(true);
					if (servers != null) {
						setApromoreServerID(servers[serverComboBox.getSelectedIndex()].getApromoreServerID());
					}
					setButtonsToAddOrChange();
					enableServerSettings();
					addKeyListeners();
				}
			});
		}
		return changeButton;
	}

	private WopedButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new WopedButton();
			deleteButton.setText(Messages.getTitle("Button.DeleteServer"));
			deleteButton.setPreferredSize(new Dimension(100, 25));
			deleteButton.setMnemonic(Messages.getMnemonic("Button.DeleteServer"));
			deleteButton.setIcon(Messages.getImageIcon("Button.DeleteServer"));
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteServer();
					ConfigurationManager.getConfiguration().saveConfig();
				}
			});
		}
		return deleteButton;
	}

	private WopedButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new WopedButton();
			saveButton.setText(Messages.getTitle("Button.SaveSettings"));
			saveButton.setPreferredSize(new Dimension(100, 25));
			saveButton.setMnemonic(Messages.getMnemonic("Button.SaveSettings"));
			saveButton.setIcon(Messages.getImageIcon("Button.SaveSettings"));
			saveButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (checkInput()) {
						if (getChangeServer() == true) {
							changeApromoreServer(getApromoreServerID());
							ConfigurationManager.getConfiguration().saveConfig();
						}
						if (getAddServer() == true) {
							addApromoreServer(getApromoreServerID());
							ConfigurationManager.getConfiguration().saveConfig();
						}
					}
				}
			});
		}
		return saveButton;
	}

	private WopedButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new WopedButton();
			cancelButton.setText(Messages.getTitle("Button.Cancel"));
			cancelButton.setPreferredSize(new Dimension(110, 25));
			cancelButton.setMnemonic(Messages.getMnemonic("Button.Cancel"));
			cancelButton.setIcon(Messages.getImageIcon("Button.Cancel"));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateServerComboBoxAfterChange();
					updateSettingsPanel();
				}
			});
		}
		return cancelButton;
	}

	private WopedButton getTestButton() {
		if (testButton == null) {
			testButton = new WopedButton();
			testButton.setText(Messages.getTitle("Button.TestConnection"));
			testButton.setIcon(Messages.getImageIcon("Button.TestConnection"));
			testButton.setMnemonic(Messages.getMnemonic("Button.TestConnection"));
			testButton.setPreferredSize(new Dimension(160, 25));
			testButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					testApromoreConnection();
				}
			});
		}
		return testButton;
	}

	// Labels

	private JLabel getServerNameLabel() {
		if (serverNameLabel == null) {
			serverNameLabel = new JLabel(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ServerName") + "</html>");
			serverNameLabel.setHorizontalAlignment(JLabel.LEFT);
			serverNameLabel.setPreferredSize(new Dimension(150, 20));
		}
		return serverNameLabel;
	}

	private JLabel getServerNameLabelUser() {
		if (serverNameLabelUser == null) {
			serverNameLabelUser = new JLabel(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ServerNameDisplayed") + "</html>");
			serverNameLabelUser.setHorizontalAlignment(JLabel.LEFT);
			serverNameLabelUser.setPreferredSize(new Dimension(150, 20));
		}
		return serverNameLabelUser;
	}

	private JLabel getServerURLLabel() {
		if (serverURLLabel == null) {
			serverURLLabel = new JLabel(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ServerURL") + "</html>");
			serverURLLabel.setHorizontalAlignment(JLabel.LEFT);
			serverURLLabel.setPreferredSize(new Dimension(150, 20));
		}
		return serverURLLabel;
	}

	private JLabel getServerPortLabel() {
		if (serverPortLabel == null) {
			serverPortLabel = new JLabel(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ServerPort") + "</html>");
			serverPortLabel.setHorizontalAlignment(JLabel.LEFT);
			serverPortLabel.setPreferredSize(new Dimension(150, 20));
		}
		return serverPortLabel;
	}

	private JLabel getManagerPathLabel() {
		if (managerPathLabel == null) {
			managerPathLabel = new JLabel(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ManagerPath") + "</html>");
			managerPathLabel.setHorizontalAlignment(JLabel.LEFT);
			managerPathLabel.setPreferredSize(new Dimension(150, 20));
		}
		return managerPathLabel;
	}

	private JLabel getUsernameLabel() {
		if (usernameLabel == null) {
			usernameLabel = new JLabel(
					"<html>" + Messages.getString("Configuration.Apromore.Label.UserName") + "</html>");
			usernameLabel.setHorizontalAlignment(JLabel.LEFT);
			usernameLabel.setPreferredSize(new Dimension(150, 20));
		}
		return usernameLabel;
	}

	private JLabel getPasswordLabel() {
		if (passwordLabel == null) {
			passwordLabel = new JLabel(
					"<html>" + Messages.getString("Configuration.Apromore.Label.Password") + "</html>");
			passwordLabel.setHorizontalAlignment(JLabel.LEFT);
			passwordLabel.setPreferredSize(new Dimension(150, 20));
		}
		return passwordLabel;
	}

	private JLabel getUseProxyLabel() {
		if (useProxyLabel == null) {
			useProxyLabel = new JLabel(
					"<html>" + Messages.getString("Configuration.Apromore.Label.UseProxy") + "</html>");
			useProxyLabel.setHorizontalAlignment(JLabel.LEFT);
			useProxyLabel.setPreferredSize(new Dimension(150, 20));
		}
		return useProxyLabel;
	}

	private JLabel getProxyNameLabel() {
		if (proxyNameLabel == null) {
			proxyNameLabel = new JLabel(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ProxyName") + "</html>");
			proxyNameLabel.setHorizontalAlignment(JLabel.RIGHT);
		}
		proxyNameLabel.setHorizontalAlignment(JLabel.LEFT);
		proxyNameLabel.setPreferredSize(new Dimension(150, 20));
		return proxyNameLabel;
	}

	private JLabel getProxyPortLabel() {
		if (proxyPortLabel == null) {
			proxyPortLabel = new JLabel(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ProxyPort") + "</html>");
			proxyPortLabel.setHorizontalAlignment(JLabel.LEFT);
			proxyPortLabel.setPreferredSize(new Dimension(150, 20));
		}
		return proxyPortLabel;
	}

	// TextFields

	private JTextField getServerNameText() {
		if (serverNameText == null) {
			serverNameText = new JTextField();
			serverNameText.setColumns(25);
			serverNameText.setEnabled(false);
			serverNameText.setToolTipText(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ServerName") + "</html>");
			// Listener to update displayed servername
			serverNameText.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					updateServerName(usernameText.getText(), serverNameText.getText());
				}

				public void removeUpdate(DocumentEvent e) {
					updateServerName(usernameText.getText(), serverNameText.getText());
				}

				public void insertUpdate(DocumentEvent e) {
					updateServerName(usernameText.getText(), serverNameText.getText());
				}
			});
		}
		return serverNameText;
	}

	private JTextField getServerNameTextUser() {
		if (serverNameTextUser == null) {
			serverNameTextUser = new JTextField();
			serverNameTextUser.setColumns(25);
			serverNameTextUser.setEnabled(false);
			serverNameTextUser.setToolTipText(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ServerNameDisplayed") + "</html>");
		}
		return serverNameTextUser;
	}

	private JTextField getServerURLText() {
		if (serverURLText == null) {
			serverURLText = new JTextField();
			serverURLText.setColumns(25);
			serverURLText.setEnabled(false);
			serverURLText.setToolTipText(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ServerURL") + "</html>");
		}
		return serverURLText;
	}

	private JTextField getServerPortText() {
		if (proxyPortText == null) {

			serverPortText = new JTextField();
			serverPortText.setColumns(5);
			serverPortText.setEnabled(false);
			serverPortText.setToolTipText(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ServerPort") + "</html>");

			/*
			 * Das Textfeld soll nur Zahlen zulassen und als Maximum soll
			 * maximal eine fünfstellige Zahl im Textfeld stehen.
			 */
			serverPortText.setDocument(new PlainDocument() {
				private static final long serialVersionUID = 1L;

				public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
					// Prüfen, ob bereits 5 Zeichen drin stehen
					if (this.getLength() >= 5)
						return;

					// Prüfen, ob die einzufügenden Zeichen Zahlen sind
					for (int i = 0; i < str.length(); i++) {
						if (!Character.isDigit(str.charAt(i)))
							return;
					}

					// Zahl(en) einfügen
					super.insertString(offs, str, a);
				}
			});

		}
		return serverPortText;
	}

	private JTextField getManagerPathText() {
		if (managerPathText == null) {
			managerPathText = new JTextField();
			managerPathText.setColumns(25);
			managerPathText.setEnabled(false);
			managerPathText.setToolTipText(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ManagerPath") + "</html>");
		}
		return managerPathText;
	}

	private JTextField getUsernameText() {
		if (usernameText == null) {
			usernameText = new JTextField();
			usernameText.setColumns(25);
			usernameText.setEnabled(false);
			usernameText
					.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.UserName") + "</html>");

			usernameText.setDocument(new PlainDocument() {
				private static final long serialVersionUID = 1L;

				public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
					// Prüfen, ob bereits 5 Zeichen drin stehen
					if (this.getLength() >= 45)
						return;

					// String einfügen
					super.insertString(offs, str, a);
				}
			});

			// Listener to add username to servername
			usernameText.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					updateServerName(usernameText.getText(), serverNameText.getText());
				}

				public void removeUpdate(DocumentEvent e) {
					updateServerName(usernameText.getText(), serverNameText.getText());
				}

				public void insertUpdate(DocumentEvent e) {
					updateServerName(usernameText.getText(), serverNameText.getText());
				}
			});
		}
		return usernameText;
	}

	private void updateServerName(String username, String servername) {

		if (serverNameText.isEnabled()) {
			if (!username.equals("") && !servername.equals("")) {
				serverNameTextUser.setText(username + "@" + servername);
			} else if (username.equals("") && !servername.equals("")) {
				serverNameTextUser.setText("PleaseRegister" + "@" + servername);
			} else {
				serverNameTextUser.setText("");
			}

		}

	}

	private JTextField getPasswordText() {
		if (passwordText == null) {
			passwordText = new JPasswordField();
			passwordText.setColumns(25);
			passwordText.setEnabled(false);
			passwordText
					.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.Password") + "</html>");
		}
		return passwordText;
	}

	private JTextField getProxyNameText() {
		if (proxyNameText == null) {
			proxyNameText = new JTextField();
			proxyNameText.setColumns(25);
			proxyNameText.setEnabled(false);
			proxyNameText.setToolTipText(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ProxyName") + "</html>");
		}
		return proxyNameText;
	}

	private JTextField getProxyPortText() {
		if (proxyPortText == null) {
			proxyPortText = new JTextField();
			proxyPortText.setColumns(5);
			proxyPortText.setEnabled(false);
			proxyPortText.setToolTipText(
					"<html>" + Messages.getString("Configuration.Apromore.Label.ProxyPort") + "</html>");

			proxyPortText.setDocument(new PlainDocument() {
				private static final long serialVersionUID = 1L;

				public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
					// Prüfen, ob bereits 5 Zeichen drin stehen
					if (this.getLength() >= 5)
						return;

					// Prüfen, ob die einzufügenden Zeichen Zahlen sind
					for (int i = 0; i < str.length(); i++) {
						if (!Character.isDigit(str.charAt(i)))
							return;
					}

					// Zahl(en) einfügen
					super.insertString(offs, str, a);
				}
			});

		}
		return proxyPortText;
	}

	// ################## SERVER ADMINISTRATION LOGIC #################### */

	class CheckboxListener implements ItemListener {

		public void itemStateChanged(ItemEvent ie) {
			JCheckBox jcb = (JCheckBox) ie.getSource();
			if (jcb == useBox) {
				getServerListPanel().setVisible(jcb.isSelected());
				getSettingsPanel().setVisible(jcb.isSelected());
				// applyConfiguration();
				if (jcb.isSelected() == false) {
					getProxyPanel().setVisible(false);
				} else if (jcb.isSelected() == true && getUseProxyCheckbox().isSelected() == true) {
					getProxyPanel().setVisible(true);
				}
			} else {
				getProxyPanel().setVisible(jcb.isSelected());
			}
		}
	}

	private void setTextFields() {

		serverNameTextUser.setText(servers[currentIndex].getApromoreServerName());

		String sname = servers[currentIndex].getApromoreServerName();
		serverNameText.setText(sname.substring(sname.indexOf("@") + 1));

		serverURLText.setText(servers[currentIndex].getApromoreServerURL());
		serverPortText.setText(String.valueOf(servers[currentIndex].getApromoreServerPort()));

		managerPathText.setText(servers[currentIndex].getApromoreManagerPath());
		usernameText.setText(servers[currentIndex].getApromoreUserName());
		try {
			passwordText.setText(ApromorePasswordSecurity.decode(servers[currentIndex].getApromorePassword()));
		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			e.printStackTrace();
		}
		useProxyBox.setSelected(servers[currentIndex].getApromoreUseProxy());
		proxyNameText.setText(servers[currentIndex].getApromoreProxyName());
		proxyPortText.setText(String.valueOf(servers[currentIndex].getApromoreProxyPort()));
	}

	private void setAttributeForCurrentApromoreServer(int index) {

		ConfigurationManager.getConfiguration().setCurrentApromoreIndex(index);
		ConfigurationManager.getConfiguration().setApromoreServerName(// getServerNameText().getText()
																		// +
				getServerNameTextUser().getText());
		ConfigurationManager.getConfiguration().setApromoreServerURL(getServerURLText().getText());
		ConfigurationManager.getConfiguration().setApromoreServerPort(Integer.parseInt(getServerPortText().getText()));
		ConfigurationManager.getConfiguration().setApromoreManagerPath(getManagerPathText().getText());
		ConfigurationManager.getConfiguration().setApromoreUsername(getUsernameText().getText());
		try {
			password = ApromorePasswordSecurity.encode(passwordText.getPassword().toString());
			ConfigurationManager.getConfiguration().setApromorePassword(password);
		} catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		ConfigurationManager.getConfiguration().setApromoreUseProxy(useProxyBox.isSelected());
		ConfigurationManager.getConfiguration().setApromoreProxyName(getProxyNameText().getText());
		ConfigurationManager.getConfiguration().setApromoreProxyPort(Integer.parseInt(getProxyPortText().getText()));
	}

	private void updateSettingsPanel() {

		currentIndex = serverComboBox.getSelectedIndex();
		setButtonsToDefault();
		disableServerSettings();
		setTextFields();
		setAttributeForCurrentApromoreServer(currentIndex);
		setAddServer(false);
		setChangeServer(false);
		setNameAlreadyInUse(false);
	}

	private void clearTextFields() {
		enableServerSettings();
		serverNameText.setText("");
		serverURLText.setText("");
		serverPortText.setText("0");
		managerPathText.setText("");
		usernameText.setText("");
		passwordText.setText("");
		useProxyBox.setEnabled(true);
		useProxyBox.setSelected(false);
		proxyNameText.setText("");
		proxyPortText.setText("0");
		addKeyListeners();
	}

	private void returnToTextFields() {
		enableServerSettings();
		serverNameText.setText(name);
		serverURLText.setText(url);
		serverPortText.setText(String.valueOf(port));
		managerPathText.setText(path);
		usernameText.setText(user);
		passwordText.setText(password);
		useProxyBox.setEnabled(true);
		useProxyBox.setSelected(useProxy);
		proxyNameText.setText(proxyUrl);
		proxyPortText.setText(String.valueOf(proxyPort));
		addKeyListeners();
	}

	private void addKeyListeners() {
		serverNameText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				addServerAttribute();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		serverURLText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				addServerAttribute();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		serverPortText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				addServerAttribute();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		managerPathText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				addServerAttribute();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		usernameText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				addServerAttribute();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		passwordText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				addServerAttribute();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		useProxyBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addServerAttribute();
			}
		});
		proxyNameText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				addServerAttribute();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		proxyPortText.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				addServerAttribute();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}

	private void addServerAttribute() {
		name = // getServerNameText().getText() +
		getServerNameTextUser().getText();
		url = getServerURLText().getText();
		try {
			port = Integer.parseInt(getServerPortText().getText());
		} catch (Exception e) {
		}
		path = getManagerPathText().getText();
		user = getUsernameText().getText();
		try {
			password = ApromorePasswordSecurity.encode(passwordText.getPassword().toString());
		} catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		useProxy = useProxyBox.isSelected();
		proxyUrl = getProxyNameText().getText();
		try {
			proxyPort = Integer.parseInt(getProxyPortText().getText());
		} catch (Exception e) {
		}
	}

	// Test für aktuell eingegebene Werte
	private void testApromoreConnection() {
		ApromoreAccess aproAccess = new ApromoreAccess(this);
		aproAccess.test(getServerURLText().getText(), getServerPortText().getText(), getManagerPathText().getText(),
				getUsernameText().getText());
	}

	private void enableServerSettings() {
		serverNameText.setEnabled(true);
		serverURLText.setEnabled(true);
		serverPortText.setEnabled(true);
		managerPathText.setEnabled(true);
		usernameText.setEnabled(true);
		passwordText.setEnabled(true);
		useProxyBox.setEnabled(true);
		proxyNameText.setEnabled(true);
		proxyPortText.setEnabled(true);
	}

	public void disableServerSettings() {
		serverNameText.setEnabled(false);
		serverURLText.setEnabled(false);
		serverPortText.setEnabled(false);
		managerPathText.setEnabled(false);
		usernameText.setEnabled(false);
		passwordText.setEnabled(false);
		useProxyBox.setEnabled(false);
		proxyNameText.setEnabled(false);
		proxyPortText.setEnabled(false);
	}

	private void deleteServer() {

		if (serverComboBox.getItemCount() == 1) {
			JOptionPane.showMessageDialog(this,
					Messages.getString("Configuration.Apromore.Dialog.CanNotDeleteServer.Message"),
					Messages.getString("Configuration.Apromore.Dialog.CanNotDeleteServer.Title"),
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int result = JOptionPane.showConfirmDialog(this,
				Messages.getString("Configuration.Apromore.Validation.Warning.DeleteServer1") + serverNameText.getText()
						+ Messages.getString("Configuration.Apromore.Validation.Warning.DeleteServer2"),
				Messages.getString("Configuration.Apromore.Validation.Warning.DeleteServer.Title"),
				JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION) {

			selected = serverComboBox.getSelectedIndex();
			ConfigurationManager.getConfiguration().removeApromoreServer(selected);
			updateServerComboBoxAfterDelete();
			updateSettingsPanel();
		}
	}

	private void updateServerComboBoxAfterDelete() {
		servers = ConfigurationManager.getConfiguration().getApromoreServers();
		serverBoxItems = ConfigurationManager.getConfiguration().getApromoreServerNames();
		for (int i = 0; i < servers.length; i++) {
			serverBoxItems[i] = servers[i].getApromoreServerName();
		}
		serverComboBox.setModel(new DefaultComboBoxModel<String>(serverBoxItems));
	}

	private void updateServerComboBoxAfterChange() {
		selected = serverComboBox.getSelectedIndex();
		servers = ConfigurationManager.getConfiguration().getApromoreServers();
		for (int i = 0; i < servers.length; i++) {
			serverBoxItems[i] = servers[i].getApromoreServerName();
		}
		serverComboBox.setModel(new DefaultComboBoxModel<String>(serverBoxItems));
		serverComboBox.setSelectedIndex(selected);
	}

	private void updateServerComboBoxAfterDefaultChange() {
		selected = serverComboBox.getSelectedIndex();
		servers = ConfigurationManager.getConfiguration().getApromoreServers();
		serverBoxItems = ConfigurationManager.getConfiguration().getApromoreServerNames();
		for (int i = 0; i < servers.length; i++) {
			serverBoxItems[i] = servers[i].getApromoreServerName();
		}
		serverComboBox.setModel(new DefaultComboBoxModel<String>(serverBoxItems));
		serverComboBox.setSelectedIndex(selected);
	}

	private void updateServerComboBoxAfterAdd() {
		servers = ConfigurationManager.getConfiguration().getApromoreServers();
		serverBoxItems = ConfigurationManager.getConfiguration().getApromoreServerNames();
		for (int i = 0; i < servers.length; i++) {
			serverBoxItems[i] = servers[i].getApromoreServerName();
		}
		serverComboBox.setModel(new DefaultComboBoxModel<String>(serverBoxItems));
		serverComboBox.setSelectedIndex(servers.length - 1);
	}

	public void changeApromoreServer(int ID) {
		addServerAttribute();
		servers = ConfigurationManager.getConfiguration().getApromoreServers();
		for (int i = 0; i < servers.length; i++) {
			if (servers[i].getApromoreServerName().equals(name) && servers[i].getApromoreServerID() != ID) {
				setNameAlreadyInUse(true);
				returnToTextFields();
				JOptionPane.showMessageDialog(this,
						Messages.getString("Configuration.Apromore.Dialog.NameAlreadyInUse.Message"),
						Messages.getString("Configuration.Apromore.Dialog.NameAlreadyInUse.Title"),
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		setNameAlreadyInUse(false);
		ConfigurationManager.getConfiguration().changeApromoreServerSettings(ID, name, url, port, path, user, password,
				useProxy, proxyUrl, proxyPort);
		setChangeServer(false);
		if (ID == 0) {
			setButtonsToDefault();
			updateServerComboBoxAfterDefaultChange();
		} else {
			setButtonsToDefault();
			updateServerComboBoxAfterChange();
		}
		JOptionPane.showMessageDialog(this,
				Messages.getString("Configuration.Apromore.Dialog.ConfigurationSaved.Message"),
				Messages.getString("Configuration.Apromore.Dialog.ConfigurationSaved.Title"),
				JOptionPane.INFORMATION_MESSAGE);
	}

	public boolean checkInput() {

		inputOK = true;
		boolean addButton = false;
		String message = "";
		String url = "";

		if (useBox.isSelected() && (serverURLText.getText().equals("") || serverPortText.getText().equals(""))) {
			inputOK = false;
			message = Messages.getString("Apromore.UI.MandatoryFields");
		}
		
		if (useBox.isSelected() && (usernameText.getText().equals("") || passwordText.getPassword().length == 0 )) {
			if (inputOK) {
				url = serverURLText.getText() + ":" + serverPortText.getText() + "/portal/login.zul";
				message = Messages.getString("Apromore.UI.MandatoryFields")
						+ Messages.getString("Apromore.UI.OptionCreateUser");
				addButton = true;
			} else {
				message = Messages.getString("Apromore.UI.MandatoryFields");
			}
		}
		if ((useBox.isSelected() && useProxyBox.isSelected()
				&& (proxyNameText.getText().equals("") || proxyPortText.getText().equals("")))
				|| (useBox.isSelected()
						&& (serverNameText.getText().equals("") || managerPathText.getText().equals("")))) {
			inputOK = false;
			if (message.equals("")) {

				message = Messages.getString("Apromore.UI.MandatoryFields");
			}
		}

		if (!message.equals("")) {

			if (addButton) {
				Object[] options = { Messages.getString("Apromore.UI.CreateUser"), Messages.getString("Dialog.Ok") };
				int option = JOptionPane.showOptionDialog(this, message,
						Messages.getString("Apromore.UI.MandatoryFields"), JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, options[1]);

				if (option == 0) {
					try {
						Desktop.getDesktop().browse(new URL(url).toURI());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, message, Messages.getString("Apromore.UI.MandatoryFields"),
						JOptionPane.WARNING_MESSAGE);
			}

		}

		return inputOK;
	}

	public boolean getInputOK() {
		return inputOK;
	}

	public boolean getChangeServer() {
		return changeServer;
	}

	private void setChangeServer(boolean changeServer) {
		this.changeServer = changeServer;
	}

	public void addApromoreServer(int ID) {
		servers = ConfigurationManager.getConfiguration().getApromoreServers();
		for (int i = 0; i < servers.length; i++) {
			if (servers[i].getApromoreServerName().equals(name)) {
				setNameAlreadyInUse(true);
				returnToTextFields();
				JOptionPane.showMessageDialog(this,
						Messages.getString("Configuration.Apromore.Dialog.NameAlreadyInUse.Message"),
						Messages.getString("Configuration.Apromore.Dialog.NameAlreadyInUse.Title"),
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		setNameAlreadyInUse(false);
		ConfigurationManager.getConfiguration().addApromoreServer(ID, name, url, port, path, user, password, useProxy,
				proxyUrl, proxyPort);
		setAddServer(false);
		setButtonsToDefault();
		updateServerComboBoxAfterAdd();

		JOptionPane.showMessageDialog(this,
				Messages.getString("Configuration.Apromore.Dialog.ConfigurationSaved.Message"),
				Messages.getString("Configuration.Apromore.Dialog.ConfigurationSaved.Title"),
				JOptionPane.INFORMATION_MESSAGE);
	}

	public boolean getAddServer() {
		return addServer;
	}

	private void setAddServer(boolean addServer) {
		this.addServer = addServer;
	}

	public int getApromoreServerID() {
		return apromoreServerID;
	}

	private void setApromoreServerID(int ID) {
		this.apromoreServerID = ID;
	}

	public boolean getNameAlreadyInUse() {
		return nameAlreadyInUse;
	}

	private void setNameAlreadyInUse(boolean nameAlreadyInUse) {
		this.nameAlreadyInUse = nameAlreadyInUse;
	}

	private int manageApromoreServerID() {
		servers = ConfigurationManager.getConfiguration().getApromoreServers();
		apromoreServerID = servers[servers.length - 1].getApromoreServerID() + 1;
		return apromoreServerID;
	}

	private void setDefaultApromoreServer() {
		setApromoreServerID(0);
		addServerAttribute();
		ConfigurationManager.getConfiguration().addApromoreServer(getApromoreServerID(), name, url, port, path, user,
				password, useProxy, proxyUrl, proxyPort);
	}

	private void setButtonsToAddOrChange() {
		getSaveButton().setEnabled(true);
		getCancelButton().setEnabled(true);
		getAddButton().setEnabled(false);
		getChangeButton().setEnabled(false);
		getDeleteButton().setEnabled(false);
	}

	public void setButtonsToDefault() {
		getAddButton().setEnabled(true);
		getChangeButton().setEnabled(true);
		getDeleteButton().setEnabled(true);
		getSaveButton().setEnabled(false);
		getCancelButton().setEnabled(false);
	}
}