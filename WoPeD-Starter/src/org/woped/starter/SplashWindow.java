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
package org.woped.starter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;

import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.gui.translations.Messages;

/**
 * TODO: DOCUMENTATION (xraven)
 * 
 * @author Thomas Pohl
 */

class WindowKiller extends Thread
{
    private JWindow wnd;
    private long    time;

    public WindowKiller(JWindow wnd, long time)
    {
        this.wnd = wnd;
        this.time = time;
        start();
    }

    public void run()
    {
        try
        {
            Thread.sleep(time);
        } catch (InterruptedException e)
        {
            //nothing
        }
        if (wnd.isVisible())
        {
            wnd.setVisible(false);
            wnd.dispose();
        }
    }
}

@SuppressWarnings("serial")
public class SplashWindow extends JWindow
{
    private JPanel       jPanel         = null;
    private URL          imageURL       = getClass().getResource(Messages.getString("Splash.Image"));
    private String       delayValue     = Messages.getString("Splash.Delay");
    private JLabel       logoLabel      = null;
    private JLabel       versionLabel   = null;
    private JLabel       copyrightLabel = null;
    private Timer        timer          = null;
    private boolean      closed         = false;
    private JFrame		 parent			= null;

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param f
     */
    public SplashWindow(JFrame owner)
    {
        super(owner);
        parent = owner;
        initialize();
    }

    private void initialize()
    {
        setVisible(false);
        setContentPane(getJPanel());
        pack();

        if (getOwner() != null)
        {
            this.setLocation(getOwner().getX() + ((getOwner().getWidth() - this.getWidth()) / 2), getOwner().getY() + ((getOwner().getHeight() - this.getHeight()) / 2));
        } else
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
        }

        setVisible(true);
        
        // Set a timer that will close the splash screen after a while.
        int delayTime = Integer.valueOf(delayValue).intValue();
		timer = new Timer(delayTime, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		timer.start();

		// Close the splash screen when the user clicks it.
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				close();
			}
		});
    }

    /**
     * Closes the splash screen.
     *  
     */
    public void close()
    {
    	// Prevent nested calls to close().
    	if (!closed) {
    		closed = true;
    		
	    	timer.stop();
	        setVisible(false);
	        dispose();
	        parent.setState(JFrame.NORMAL);
    	}
    }

    private JPanel getJPanel()
    {
        if (jPanel == null)
        {
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.setBackground(new Color(236, 233, 216));
            // Grid logo
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.gridwidth = 2;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            // Grid version
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.anchor = GridBagConstraints.SOUTHWEST;
            gridBagConstraints2.insets = new Insets(0, 6, 4, 0);

            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.anchor = GridBagConstraints.SOUTHEAST;
            gridBagConstraints3.insets = new Insets(0, 0, 4, 6);
            jPanel.add(getCopyrightLabel(), gridBagConstraints3);
            jPanel.add(getVersionLabel(), gridBagConstraints2);
            jPanel.add(getLogoLabel(), gridBagConstraints1);
        }
        return jPanel;
    }

    private JLabel getLogoLabel()
    {
        if (logoLabel == null)
        {
            if (imageURL == null)
            {
                logoLabel = new JLabel();
            } else
            {
                logoLabel = new JLabel(new ImageIcon(imageURL));
            }
        }
        return logoLabel;
    }

    private JLabel getVersionLabel()
    {
        if (versionLabel == null)
        {
            versionLabel = new JLabel("Version " + Messages.getString("Application.Version"));
            versionLabel.setFont(DefaultStaticConfiguration.DEFAULT_SMALLLABEL_FONT);
            versionLabel.setForeground(DefaultStaticConfiguration.DEFAULT_INVERSE_COLOR);
        }
        return versionLabel;
    }

    private JLabel getCopyrightLabel()
    {
        if (copyrightLabel == null)
        {
            copyrightLabel = new JLabel(Messages.getString("Splash.Text.Copyright"));
            copyrightLabel.setFont(DefaultStaticConfiguration.DEFAULT_SMALLLABEL_FONT);
            copyrightLabel.setForeground(DefaultStaticConfiguration.DEFAULT_INVERSE_COLOR);
        }
        return copyrightLabel;
    }
}