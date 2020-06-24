package org.woped.starter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.woped.core.config.ConfigurationManager;
import org.woped.editor.controller.VisualController;
import org.woped.editor.help.action.LaunchDefaultBrowserAction;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

public class RegistrationUI extends JDialog {

	/**
	 * 
	 */
	private static final long 	serialVersionUID = 1L;
	private JLabel 			 	logoLabel 					= null;
	private JLabel  			registrationTextLabel 		= null;
	private JCheckBox 			showAgainCheckBox 			= null;
	private WopedButton 			closeButton 				= null;
	private WopedButton 			registerButton		 		= null;
	private JPanel		 		registrationTextPanel 		= null;
	private JPanel			    socialMediaPanel			= null;
	private JPanel 				buttonPanel 				= null;
	private boolean			    initialStart 				= true;
		
	public RegistrationUI (JFrame parent, boolean initialStart) {	
		super(parent, Messages.getString("Registration.Title"), true);
		this.initialStart = initialStart;
		initialize();
	}
	
	private void initialize() {
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.getContentPane().setLayout(new BorderLayout());

		this.getContentPane().add(getRegistrationTextPanel(), BorderLayout.NORTH);
		this.getContentPane().add(getSocialMediaPanel(), BorderLayout.CENTER);
		
		this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.pack();
		this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2); 
		setVisible(true);
	}
	
	private JPanel getRegistrationTextPanel(){

		if (registrationTextPanel == null) {
			registrationTextPanel = new JPanel();
			registrationTextPanel.setLayout(new GridBagLayout());
           
            GridBagConstraints c = new GridBagConstraints();           
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.NORTH;
            c.insets = new Insets(5, 5, 5, 5);
            logoLabel = new JLabel(new ImageIcon(getClass().getResource(Messages.getString("Window.Logo.Image"))));
            registrationTextPanel.add(logoLabel, c);          
            
            c.gridy = 1;
            c.anchor = GridBagConstraints.SOUTH;
            c.insets = new Insets(5, 5, 5, 5);
            c.fill = GridBagConstraints.BOTH;
            
            registrationTextLabel = new JLabel("<html><p>" + 
            			(initialStart ? Messages.getString("Registration.Text.FirstTime") : "") +
            			Messages.getString("Registration.Text") + "</p></html>");
            registrationTextPanel.add(registrationTextLabel, c);
            
            if (initialStart) {
                c.gridy = 2;
            	registrationTextPanel.add(getShowAgainCheckBox(), c);
            }
        }
		
        return registrationTextPanel;
	}
	
	private JPanel getSocialMediaPanel() {
		
		if (socialMediaPanel == null) {
			socialMediaPanel = new JPanel();
			socialMediaPanel.setLayout(new GridBagLayout());
			TitledBorder border = new TitledBorder(Messages.getString("Registration.Panel.SocialMedia.Text"));
			socialMediaPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 0, 5)));


            GridBagConstraints c = new GridBagConstraints();           
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(5, 5, 5, 5);
			JLabel facebookLink = new JLabel(Messages.getImageIcon("Action.Facebook"));
			facebookLink.addMouseListener(new LaunchDefaultBrowserAction(Messages.getString("Community.Facebook.link"), facebookLink));
			socialMediaPanel.add(facebookLink, c);
        
            c.gridx = 1;
            c.gridy = 0;
            c.insets = new Insets(5, 5, 5, 5);
			JLabel googlePlusLink = new JLabel(Messages.getImageIcon("Action.Googleplus"));
			googlePlusLink.addMouseListener(new LaunchDefaultBrowserAction(Messages.getString("Community.Googleplus.link"), googlePlusLink));
			socialMediaPanel.add(googlePlusLink, c);  
        
            c.gridx = 2;
            c.gridy = 0;
            c.insets = new Insets(5, 5, 5, 5);
			JLabel twitterLink = new JLabel(Messages.getImageIcon("Action.Twitter"));
			twitterLink.addMouseListener(new LaunchDefaultBrowserAction(Messages.getString("Community.Twitter.link"), twitterLink));
			socialMediaPanel.add(twitterLink, c);   
        }
		
		return socialMediaPanel;
	}
	
	private JCheckBox getShowAgainCheckBox() {
		
		if (showAgainCheckBox == null) {
			showAgainCheckBox = new JCheckBox(Messages.getString("Registration.ShowOnStartup"));
		}
		
		return showAgainCheckBox;
	}
	
	private JButton getRegisterButton() {
		
		if (registerButton == null) {
			registerButton = new WopedButton(new AbstractAction() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 0L;

				public void actionPerformed(ActionEvent arg0) {
					String osVersion = System.getProperty("os.name").replace(" ", "");
					String javaVersion = System.getProperty("java.version").replace(" ", "");
					String wopedVersion = Messages.getString("Application.Version".replace(" ", ""));
					String url = Messages.getString("Registration.ServerURL") + 
							     "?java=" + javaVersion + "&os=" + osVersion + "&woped=" + wopedVersion;
					new LaunchDefaultBrowserAction(url, registerButton).displayURL();
					ConfigurationManager.getConfiguration().setRegistered(true);
					VisualController.getInstance().propertyChange(new PropertyChangeEvent(this, "Registration", null, null));
					
					if (initialStart) {
						ConfigurationManager.getConfiguration().setShowOnStartup(!showAgainCheckBox.isSelected());
					}
					
					dispose();
				}
			});
			
			registerButton.setIcon(Messages.getImageIcon("Action.Register"));
			registerButton.setText(Messages.getTitle("Action.Register"));
		}
		
		return registerButton;
	}
	
	
	private JButton getCloseButton() {
		
		if (closeButton == null) {
			closeButton = new WopedButton(new AbstractAction() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 0L;

				public void actionPerformed(ActionEvent arg0) {
					if (initialStart) {
						ConfigurationManager.getConfiguration().setShowOnStartup(!showAgainCheckBox.isSelected());
					}
					
					dispose();
				}
			});
			
			closeButton.setIcon(Messages.getImageIcon("Action.Close"));
			closeButton.setText(Messages.getTitle("Action.Close"));
		}
			
		return closeButton;
	}

	private JPanel getButtonPanel() {
		
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
 
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.EAST;
            c.insets = new Insets(5, 5, 5, 5);
            
            buttonPanel.add(getRegisterButton(), c);                                          
            c.gridx = 1;
            buttonPanel.add(getCloseButton(), c);
		}
		
		return buttonPanel;
	}
}
