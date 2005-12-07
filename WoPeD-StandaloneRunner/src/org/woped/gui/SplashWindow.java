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
package org.woped.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import org.woped.editor.utilities.Messages;

/**
 * TODO: DOCUMENTATION (xraven)
 * 
 * @author Thomas Pohl
 */

class WindowKiller extends Thread
{
  private JWindow wnd;
  private long time;

  public WindowKiller(JWindow wnd, long time)
  {
    this.wnd = wnd;
    this.time = time;
    start();
  }

  public void run()
  {
    try {
      Thread.sleep(time);
    } 
    catch (InterruptedException e) 
    {
      //nothing
    }
    if (wnd.isVisible()) {
      wnd.setVisible(false);
      wnd.dispose();
    }
  }
}
public class SplashWindow extends JWindow
{
    private JPanel jPanel       = null;
    private URL    imageURL     = getClass().getResource(Messages.getString("Splash.Image"));
    private String delayValue   = Messages.getString("Splash.Delay");
    private JLabel logoLabel    = null;
    private JLabel versionLabel = null;
    private JLabel copyrightLabel = null;
    private long   delayTime = 0;
    private WindowKiller killer = null; 

    /**
     * TODO: DOCUMENTATION (xraven)
     * 
     * @param f
     */
    public SplashWindow()
    {
        initialize();
    }

    private void initialize()
    {
        delayTime = Long.valueOf(delayValue).longValue();
        killer = new WindowKiller(this, delayTime);
        setVisible(false);
        setContentPane(getJPanel());
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getLogoLabel().getWidth())/2, 
                    (screenSize.height - getLogoLabel().getHeight())/2);
        setVisible(true);
    }
    
    public void kill()
    {
        killer.run();        
    }

    /**
     * TODO: DOCUMENTATION (xraven)
     *  
     */
    public void close()
    {
        setVisible(false);
        dispose();
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
            gridBagConstraints2.insets = new Insets(0, 1, 0, 0);
            
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.anchor = GridBagConstraints.SOUTHEAST;
            gridBagConstraints3.insets = new Insets(0, 0, 0, 1);
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
            versionLabel = new JLabel("Version " + Messages.getWoPeDVersion(false));
            versionLabel.setFont(new Font("Verdana", Font.PLAIN, 10));
            versionLabel.setForeground(Color.WHITE);
        }
        return versionLabel;
    }
    
    private JLabel getCopyrightLabel()
    {
        if (copyrightLabel == null)
        {
            copyrightLabel = new JLabel("woped.org (c) 2003-2005");
            copyrightLabel.setFont(new Font("Verdana", Font.PLAIN, 10));
            copyrightLabel.setForeground(Color.WHITE);
        }
        return copyrightLabel;
    }
}