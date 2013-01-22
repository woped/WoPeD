package org.woped.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.woped.editor.action.DisposeWindowAction;
import org.woped.editor.help.action.LaunchDefaultBrowserAction;
import org.woped.translations.Messages;

public class RegistrationUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel logoLabel 					= null;
	private JLabel proregistrationTextLabel 	= null;
	private JButton registrationLinkLabel 		= null;

	private JCheckBox dontRemeberMe 			= null;

	private JButton closeButton 				= null;

	private JScrollPane registrationTextPanel 	= null;
	private JPanel buttonPanel 					= null;
	
	//F��r M��glichkeit 1
	private JLabel facebookLink					= null;
	private JLabel googlePlus					= null;
	private JLabel twitter						= null;
	
	//F��r M��glichkeit 2
	private JLabel allIcons						= null;
	
	

	public RegistrationUI(){
		
		
		initialize();
	}
	
	public void initialize(){
		  	this.setVisible(true);
	        this.getContentPane().setLayout(new BorderLayout());
	        //this.setUndecorated(false);  //f���hrt zu Fehler
	        this.setResizable(false);
	        this.getContentPane().add(getregistrationTextPanel(), BorderLayout.NORTH);
	        this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
	        this.setTitle("Already Signed Up?");
	        this.pack();
	        /*
	        if (getOwner() != null)
	        {
	            this.setLocation(getOwner().getX() + ((getOwner().getWidth() - this.getWidth()) / 2), getOwner().getY() + ((getOwner().getHeight() - this.getHeight()) / 2));
	        } else
	        {
	            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	            this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
	        }
			*/
	        setLocationRelativeTo(null);
	        this.setSize(this.getWidth(), this.getHeight());
	}
	
	public  JScrollPane getregistrationTextPanel(){
		String[] aboutArgs       	= { Messages.getWoPeDVersionWithTimestamp() };
       	String   registrationText   = "<html><p>" + Messages.getStringReplaced("Registration.Text", aboutArgs) + "</p></html>";
            	
       	
       	
    	if (registrationTextPanel == null)
        {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setBackground(Color.WHITE);
            
            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.CENTER;
            //c.insets = new Insets(10, 10, 10, 10);
            logoLabel = new JLabel(new ImageIcon(getClass().getResource(Messages.getString("Window.LogoNEW.Image"))));
            panel.add(logoLabel, c);

           
            
            c.gridy = 1;
            c.insets = new Insets(4, 10, 3, 10);
            c.anchor = GridBagConstraints.CENTER;
            proregistrationTextLabel = new JLabel(registrationText);
            panel.add(proregistrationTextLabel, c);
           
            
            c.gridy = 2;
            c.insets = new Insets(2, 35, 1, 5);           
            c.anchor = GridBagConstraints.WEST;
            /* --- M���glichkeit 1 zur Darstellung
            facebookLink = new JLabel("<html><p>" + Messages.getString("Community.Facebook.Text") + "</p></html>");
            facebookLink.addMouseListener(new LaunchDefaultBrowserAction(Messages.getString("Community.Facebook.Link"), facebookLink));
            panel.add(facebookLink, c);
            
            c.gridy = 3;
            c.anchor = GridBagConstraints.WEST;
            googlePlus = new JLabel("<html><p>" + Messages.getString("Community.GooglePlus.Text") + "</p></html>");
            googlePlus.addMouseListener(new LaunchDefaultBrowserAction(Messages.getString("Community.GooglePlus.Link"), googlePlus));
            panel.add(googlePlus, c);
            
            
            c.gridy = 4;
            c.anchor = GridBagConstraints.WEST;
            twitter = new JLabel("<html><p>" + Messages.getString("Community.Twitter.Text") + "</p></html>");
            twitter.addMouseListener(new LaunchDefaultBrowserAction(Messages.getString("Community.Twitter.Link"), twitter));
            panel.add(twitter, c);
            */
            
            /* --- M���glichkeit 2 ---*/
            allIcons = new JLabel(new ImageIcon(getClass().getResource(Messages.getString("Window.AllIcons.Image"))));
            panel.add(allIcons, c);
            
            registrationTextPanel = new JScrollPane(panel);
        }
        return registrationTextPanel;
	}
	
	public JPanel getButtonPanel() {
		
		if(buttonPanel == null){
			buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            //buttonPanel.setBackground(Color.GRAY);
            GridBagConstraints c = new GridBagConstraints();
            
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.EAST;
            c.insets = new Insets(5, 5, 5, 5);
            
            dontRemeberMe = new JCheckBox("I'm already a WoPeD fan");
            buttonPanel.add(dontRemeberMe, c);
            
            c.gridx = 1;
            registrationLinkLabel = new JButton("Join Now");
            registrationLinkLabel.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Registration reg = new Registration();
					String info[] = reg.getOS();
					new LaunchDefaultBrowserAction(
							"http://b-arnold.net/woped/registration/index.php?java="+info[1]+"&os="+info[0]+"&woped="+info[2], registrationLinkLabel);
					dispose();
				}
			});
            //registrationLinkLabel.setText("Join Now");
            
            buttonPanel.add(registrationLinkLabel, c);
            
            c.gridx = 2;
            closeButton = new JButton(new DisposeWindowAction());
            closeButton.setText("Close");
            buttonPanel.add(closeButton, c);
		}
		return buttonPanel;
	}
	
}
