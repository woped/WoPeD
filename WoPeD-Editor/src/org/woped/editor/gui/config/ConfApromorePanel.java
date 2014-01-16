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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         The <code>ConfLanguagePanel</code> is the
 *         <code>AbstractConfPanel</code> for the configuration of the
 *         language. <br>
 *         Created on: 26.11.2004 Last Change on: 14.11.2005
 */
@SuppressWarnings("serial")
public class ConfApromorePanel extends AbstractConfPanel
{
    private JCheckBox      	useBox = null;
    private JPanel         	enabledPanel    = null;
    private JPanel			settingsPanel = null;
    private JTextField      serverURLText = null;
    private JLabel         	serverURLLabel    = null;
    private JTextField      usernameText = null;
    private JLabel         	usernameLabel    = null;
    private JPasswordField  passwordText = null;
    private JLabel         	passwordLabel    = null;
    private JTextField      proxyNameText = null;
    private JLabel         	proxyNameLabel    = null;
    private JTextField      proxyPortText = null;
    private JLabel         	proxyPortLabel    = null;
    private JLabel 			useProxyLabel = null;
    private JCheckBox 		useProxyBox = null;
    private JLabel 			serverPortLabel = null;
    private JTextField 		serverPortText = null;
    private JPanel			proxyPanel = null;
    private JTextField      managerPathText = null;
    private JLabel         	managerPathLabel    = null;
    /**
     * Constructor for ConfToolsPanel.
     */
    public ConfApromorePanel(String name)
    {
        super(name);
        initialize();
    }

    /**
     * @see AbstractConfPanel#applyConfiguration()
     */
	public boolean applyConfiguration() {
		boolean changed = useBox.isSelected() != ConfigurationManager
				.getConfiguration().getApromoreUse();

		if (changed) {
			JOptionPane
					.showMessageDialog(
							null,
							Messages.getString("Configuration.Apromore.Dialog.Restart.Message"),
							Messages.getString("Configuration.Apromore.Dialog.Restart.Title"),
							JOptionPane.INFORMATION_MESSAGE);
		}
		ConfigurationManager.getConfiguration().setApromoreServerURL(
				getServerURLText().getText());
		ConfigurationManager.getConfiguration().setApromoreManagerPath(
				getManagerPathText().getText());
		ConfigurationManager.getConfiguration().setApromoreProxyName(
				getProxyNameText().getText());
		ConfigurationManager.getConfiguration().setApromoreProxyPort(
				Integer.parseInt(getProxyPortText().getText()));
		ConfigurationManager.getConfiguration().setApromoreUsername(
				getUsernameText().getText());

		byte[] hashArray = hash(getPasswordText().getText());
		ConfigurationManager.getConfiguration().setApromorePassword(hashArray.toString());
		
		ConfigurationManager.getConfiguration().setApromoreServerPort(
				Integer.parseInt(getServerPortText().getText()));
		ConfigurationManager.getConfiguration().setApromoreUseProxy(
				useProxyBox.isSelected());
		ConfigurationManager.getConfiguration().setApromoreUse(
				useBox.isSelected());

		String hostname = this.getURL();
		String port = this.getPort();
		String managerUrl = this.getmanagerUrl();
		Writer writer = null;
		try {
			writer = new FileWriter(
					"src/org/woped/starter/utilities/apromore-client.properties");
			Properties prop1 = new Properties();
			prop1.setProperty("hostname", hostname);
			prop1.setProperty("port", port);
			prop1.setProperty("manager-url", managerUrl);
			prop1.store(writer, null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
		return true;
	}

	private byte[] hash(String str) {
		MessageDigest md;

		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e) {
			return null;
		} 
		return md.digest(str.getBytes());
	}
	
    /**
     * @see AbstractConfPanel#readConfiguration()
     */
    public void readConfiguration()
    {
    	
    	getServerURLText().setText(ConfigurationManager.getConfiguration().getApromoreServerURL());
    	getManagerPathText().setText(ConfigurationManager.getConfiguration().getApromoreManagerPath());
    	getServerPortText().setText(""+ConfigurationManager.getConfiguration().getApromoreServerPort());
    	getProxyNameText().setText(ConfigurationManager.getConfiguration().getApromoreProxyName());
    	getProxyPortText().setText(""+ConfigurationManager.getConfiguration().getApromoreProxyPort());
    	getUsernameText().setText(ConfigurationManager.getConfiguration().getApromoreUsername());
    	getPasswordText().setText(ConfigurationManager.getConfiguration().getApromorePassword());
    	getUseProxyCheckbox().setSelected(ConfigurationManager.getConfiguration().getApromoreUseProxy());
    	getUseBox().setSelected(ConfigurationManager.getConfiguration().getApromoreUse());
        
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
        contentPanel.add(getEnabledPanel(), c);

        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 1;
        contentPanel.add(getSettingsPanel(), c);
        
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 2;
        contentPanel.add(getProxyPanel(), c);
        
        // dummy
       c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        c.gridy = 2;
        contentPanel.add(new JPanel(), c);
        
        setMainPanel(contentPanel);
    }

    // ################## GUI COMPONENTS #################### */

    private JTextField getServerURLText()
    {
        if (serverURLText == null)
        {
        	serverURLText = new JTextField();
        	serverURLText.setColumns(25);
        	serverURLText.setEnabled(true);
        	serverURLText.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.ServerName") + "</html>");
        }
        return serverURLText;
    }

    private JPanel getEnabledPanel() {
        if (enabledPanel == null)
        {
        	enabledPanel = new JPanel();
        	enabledPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;

            enabledPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Metrics.Panel.MetricsUsage.Title")), BorderFactory
                    .createEmptyBorder(5, 5, 10, 5)));

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            enabledPanel.add(getUseBox(), c);

        }
        return enabledPanel;
    }
      
    private JPanel getSettingsPanel() 
    {
    	if (settingsPanel == null) 
        {
            settingsPanel = new JPanel();
            settingsPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;

            settingsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Apromore.Settings.Panel.Title")), 
            		BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 0;
            settingsPanel.add(getServerURLLabel(), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            settingsPanel.add(getServerURLText(), c);
            
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 1;
            settingsPanel.add(getServerPortLabel(), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 1;
            settingsPanel.add(getServerPortText(), c);
            
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 2;
            settingsPanel.add(getManagerPathLabel(), c);
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 2;
            settingsPanel.add(getManagerPathText(), c);
            
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 3;
            settingsPanel.add(getUsernameLabel(), c);
            
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 3;
            settingsPanel.add(getUsernameText(), c);
            
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 4;
            settingsPanel.add(getPasswordLabel(), c);
            
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 4;
            settingsPanel.add(getPasswordText(), c);
            
            c.weightx = 1;
            c.gridx = 0;
            c.gridy = 5;
            settingsPanel.add(getUseProxyLabel(), c);
            
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 5;
            settingsPanel.add(getUseProxyCheckbox(), c);           
        }
    	
		settingsPanel.setVisible(getUseBox().isSelected());
        return settingsPanel;
    }
    
    private JPanel getProxyPanel() 
    {
    	if (proxyPanel == null) 
        {
    		proxyPanel = new JPanel();
    		proxyPanel.setLayout(new GridBagLayout());
            GridBagConstraints d = new GridBagConstraints();
            d.anchor = GridBagConstraints.WEST;

            proxyPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Messages.getString("Configuration.Apromore.ProxySettings.Panel.Title")), 
            		BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            
            d.weightx = 1;
            d.gridx = 0;
            d.gridy = 5;
            proxyPanel.add(getProxyNameLabel(), d);
            
            d.weightx = 1;
            d.gridx = 1;
            d.gridy = 5;
            proxyPanel.add(getProxyNameText(), d);
            
            d.weightx = 1;
            d.gridx = 0;
            d.gridy = 6;
            proxyPanel.add(getProxyPortLabel(), d);
            
            d.weightx = 1;
            d.gridx = 1;
            d.gridy = 6;
            proxyPanel.add(getProxyPortText(), d);
            
        }
    	
    	proxyPanel.setVisible(getUseProxyCheckbox().isSelected());
        return proxyPanel;
    }
    
	class CheckboxListener implements ItemListener{					

		public void itemStateChanged(ItemEvent ie) {
			JCheckBox jcb = (JCheckBox) ie.getSource();
			if (jcb==useBox){
			getSettingsPanel().setVisible(jcb.isSelected());
				if(jcb.isSelected()==false){
					getProxyPanel().setVisible(false);
				}else if (jcb.isSelected()==true && getUseProxyCheckbox().isSelected()==true){
					getProxyPanel().setVisible(true);
				}
			}else{
				getProxyPanel().setVisible(jcb.isSelected());
			}

		}
	}

    private JLabel getServerURLLabel()
    {
        if (serverURLLabel == null)
        {
            serverURLLabel = new JLabel("<html>" + Messages.getString("Configuration.Apromore.Label.ServerURL") + "</html>");
            serverURLLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return serverURLLabel;
    } 
    
    private JLabel getUsernameLabel()
    {
        if (usernameLabel == null)
        {
            usernameLabel = new JLabel("<html>" + Messages.getString("Configuration.Apromore.Label.UserName") + "</html>");
            usernameLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return usernameLabel;
    }
    
    private JLabel getPasswordLabel()
    {
        if (passwordLabel == null)
        {
        	passwordLabel = new JLabel("<html>" + Messages.getString("Configuration.Apromore.Label.Password") + "</html>");
        	passwordLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return passwordLabel;
    }
    
    private JTextField getUsernameText()
    {
        if (usernameText == null)
        {
            usernameText = new JTextField();
            usernameText.setColumns(15);
            usernameText.setEnabled(true);
            usernameText.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.UserName") + "</html>");
        }
        return usernameText;
    }
 
   private JTextField getPasswordText()
   {
       if (passwordText == null)
       {
    	   passwordText = new JPasswordField();
    	   passwordText.setColumns(15);
    	   passwordText.setEnabled(true);
    	   passwordText.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.Password") + "</html>");
       }
       return passwordText;
   }

   
   private JLabel getProxyNameLabel()
    {
        if (proxyNameLabel == null)
        {
        	proxyNameLabel = new JLabel("<html>" + Messages.getString("Configuration.Apromore.Label.ProxyName") + "</html>");
        	proxyNameLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return proxyNameLabel;
    }
    
    private JTextField getProxyNameText()
    {
        if (proxyNameText == null)
        {
        	proxyNameText = new JTextField();
        	proxyNameText.setColumns(15);
        	proxyNameText.setEnabled(true);
        	proxyNameText.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.ProxyName") + "</html>");
        }
        return proxyNameText;
    }
    private JLabel getProxyPortLabel()
    {
        if (proxyPortLabel == null)
        {
        	proxyPortLabel = new JLabel("<html>" + Messages.getString("Configuration.Apromore.Label.ProxyPort") + "</html>");
        	proxyPortLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return proxyPortLabel;
    }
    
    private JTextField getProxyPortText()
    {
        if (proxyPortText == null)
        {
        	proxyPortText = new JTextField();
        	proxyPortText.setColumns(4);
        	proxyPortText.setEnabled(true);
        	proxyPortText.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.ProxyPort") + "</html>");
        }
        return proxyPortText;
    }    
    
    private JLabel getServerPortLabel()
    {
        if (serverPortLabel == null)
        {
        	serverPortLabel = new JLabel("<html>" + Messages.getString("Configuration.Apromore.Label.ServerPort") + "</html>");
        	serverPortLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return serverPortLabel;
    }
    
    private JTextField getServerPortText()
    {
        if (proxyPortText == null)
        {
        	serverPortText = new JTextField();
        	serverPortText.setColumns(4);
        	serverPortText.setEnabled(true);
        	serverPortText.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.ServerPort") + "</html>");
        }
        return serverPortText;
    }    
    private JLabel getUseProxyLabel()
    {
        if (useProxyLabel == null)
        {
        	useProxyLabel = new JLabel("<html>" + Messages.getString("Configuration.Apromore.Label.UseProxy") + "</html>");
        	useProxyLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return useProxyLabel;
    }
    
    private JCheckBox getUseProxyCheckbox()
    {
        if (useProxyBox == null)
        {
        	useProxyBox = new JCheckBox();
        	useProxyBox.setEnabled(true);
        	useProxyBox.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.UseProxy") + "</html>");
            CheckboxListener cbl = new CheckboxListener();
    		useProxyBox.addItemListener(cbl);
        }
        return useProxyBox;
    }    
    
    private JCheckBox getUseBox()
    {
        if (useBox == null)
        {
        	useBox = new JCheckBox(Messages.getString("Configuration.Apromore.Label.Use"));
        	useBox.setEnabled(true);
        	useBox.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.Use") + "</html>");
            CheckboxListener cbl = new CheckboxListener();
    		useBox.addItemListener(cbl);
       }
		        
        return useBox;
    }
    private String getPort(){
    	JTextField port = this.getServerPortText();
    	return port.getText();
    }
    
    private String getURL(){
    	JTextField url = this.getServerURLText();
    	return url.getText();
    }
    
    private String getmanagerUrl(){
    	JTextField managerUrl = this.getManagerPathText();
    	return managerUrl.getText();
    }
    
    private JLabel getManagerPathLabel()
    {
        if (managerPathLabel == null)
        {
        	managerPathLabel = new JLabel("<html>" + Messages.getString("Configuration.Apromore.Label.ManagerPath") + "</html>");
        	managerPathLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return managerPathLabel;
    }
    
    private JTextField getManagerPathText()
    {
        if (managerPathText == null)
        {
        	managerPathText = new JTextField();
        	managerPathText.setColumns(15);
        	managerPathText.setEnabled(true);
        	managerPathText.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.ManagerPath") + "</html>");
        }
        return managerPathText;
    }
}
