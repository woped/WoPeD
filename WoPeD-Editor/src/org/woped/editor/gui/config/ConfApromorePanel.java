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

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
/* Test */
@SuppressWarnings("serial")
public class ConfApromorePanel extends AbstractConfPanel
{
    private JCheckBox      	useBox = null;
    private JPanel         	enabledPanel    = null;
    private JPanel			settingsPanel = null;
    private JTextField      serverText = null;
    private JLabel         	serverLabel    = null;
    private JTextField      usernameText = null;
    private JLabel         	usernameLabel    = null;
    private JTextField      proxyNameText = null;
    private JLabel         	proxyNameLabel    = null;
    private JTextField      proxyPortText = null;
    private JLabel         	proxyPortLabel    = null;
    private JLabel 			useProxyLabel = null;
    private JCheckBox 		useProxyBox = null;
    private JLabel 			serverPortLabel = null;
    private JTextField 		serverPortText = null;
    private JPanel			proxyPanel = null;
    private JTextField      managerUrlText = null;
    private JLabel         	managerUrlLabel    = null;
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
    public boolean applyConfiguration()
    {
            JOptionPane.showMessageDialog(null, Messages.getString("Configuration.Apromore.Dialog.Restart.Message"), Messages.getString("Configuration.Apromore.Dialog.Restart.Title"),
                    JOptionPane.INFORMATION_MESSAGE);
            ConfigurationManager.getConfiguration().setApromoreServer(getServerText().getText());
            ConfigurationManager.getConfiguration().setApromoreManagerUrl(getManagerUrlText().getText());
            ConfigurationManager.getConfiguration().setApromoreProxyName(getProxyNameText().getText());
            ConfigurationManager.getConfiguration().setApromoreProxyPort(Integer.parseInt(getProxyPortText().getText()));
            ConfigurationManager.getConfiguration().setApromoreUsername(getUsernameText().getText());
            ConfigurationManager.getConfiguration().setApromoreServerPort(Integer.parseInt(getServerPortText().getText()));
            ConfigurationManager.getConfiguration().setApromoreUseProxy(useProxyBox.isSelected());
            ConfigurationManager.getConfiguration().setApromoreUse(useBox.isSelected());
            
            String hostname = this.getURL();
            String port = this.getPort();
            String managerUrl= this.getmanagerUrl();
            Writer writer = null;
            try
            {
             writer = new FileWriter( "src/org/woped/starter/utilities/apromore-client.properties");
              Properties prop1 = new Properties();
              prop1.setProperty("hostname",hostname);
              prop1.setProperty("port", port);
              prop1.setProperty("manager-url", managerUrl);
              prop1.store(writer, null);
            }
            catch ( IOException e )
            {
              e.printStackTrace();
            }
            finally
            {
              try { writer.close(); } catch ( Exception e ) { }
            } 
        return true;
    }

    /**
     * @see AbstractConfPanel#readConfiguration()
     */
    public void readConfiguration()
    {
    	
    	getServerText().setText(ConfigurationManager.getConfiguration().getApromoreServer());
    	getManagerUrlText().setText(ConfigurationManager.getConfiguration().getApromoreManagerUrl());
    	getServerPortText().setText(""+ConfigurationManager.getConfiguration().getApromoreServerPort());
    	getProxyNameText().setText(ConfigurationManager.getConfiguration().getApromoreProxyName());
    	getProxyPortText().setText(""+ConfigurationManager.getConfiguration().getApromoreProxyPort());
    	getUsernameText().setText(ConfigurationManager.getConfiguration().getApromoreUsername());
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

    private JTextField getServerText()
    {
        if (serverText == null)
        {
        	serverText = new JTextField();
        	serverText.setColumns(25);
        	serverText.setEnabled(true);
        	serverText.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.ServerName") + "</html>");
        }
        return serverText;
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
            settingsPanel.add(getServerLabel(), c);

            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 0;
            settingsPanel.add(getServerText(), c);
            
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
            settingsPanel.add(getManagerUrlLabel(), c);
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 2;
            settingsPanel.add(getManagerUrlText(), c);
            
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
            settingsPanel.add(getUseProxyLabel(), c);
            
            c.weightx = 1;
            c.gridx = 1;
            c.gridy = 4;
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

    private JLabel getServerLabel()
    {
        if (serverLabel == null)
        {
            serverLabel = new JLabel("<html>" + Messages.getString("Configuration.Apromore.Label.ServerName") + "</html>");
            serverLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return serverLabel;
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
    	JTextField url = this.getServerText();
    	return url.getText();
    }
    
    private String getmanagerUrl(){
    	JTextField managerUrl = this.getManagerUrlText();
    	return managerUrl.getText();
    }
    
    private JLabel getManagerUrlLabel()
    {
        if (managerUrlLabel == null)
        {
        	managerUrlLabel = new JLabel("<html>" + Messages.getString("Configuration.Apromore.Label.ManagerUrl") + "</html>");
        	managerUrlLabel.setHorizontalAlignment(JLabel.RIGHT);
        }
        return managerUrlLabel;
    }
    
    private JTextField getManagerUrlText()
    {
        if (managerUrlText == null)
        {
        	managerUrlText = new JTextField();
        	managerUrlText.setColumns(15);
        	managerUrlText.setEnabled(true);
        	managerUrlText.setToolTipText("<html>" + Messages.getString("Configuration.Apromore.Label.ManagerUrl") + "</html>");
        }
        return managerUrlText;
    }
}
