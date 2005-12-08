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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.woped.editor.action.DisposeWindowAction;
import org.woped.editor.utilities.Messages;
import org.woped.gui.action.help.LaunchDefaultBrowserAction;

/**
 * @author <a href="mailto:freytag@ba-karlsruhe.de">Thomas Freytag </a> <br>
 *         <br>
 *         TODO: DOCUMENTATION (tfreytag)
 * 
 * 17.01.2005
 */
public class AboutUI extends JDialog
{
    private JLabel              logoLabel       = null;
    private JLabel              aboutTextLabel  = null;
    private JLabel              homepageLabel   = null;
    private JLabel              mailtoLabel     = null;
    private JLabel              sfLabel         = null;
    private JButton             closeButton     = null;
    private JButton             aboutButton     = null;
    private JButton             changelogButton = null;

    private JScrollPane         aboutPanel      = null;
    private JScrollPane         changeLogPanel  = null;
    private JPanel              buttonPanel     = null;

    private static String[] aboutArgs = {Messages.getWoPeDVersion(true)};
    private static final String aboutText       = "<html><p>" +Messages.getStringReplaced("About.Text", aboutArgs) + "</p></html>";

    public AboutUI()
    {
        this(null);
    }

    /**
     * Constructor for AboutUI.
     * 
     * @param owner
     * @throws HeadlessException
     */
    public AboutUI(Frame owner) throws HeadlessException
    {
        super(owner, true);
        initialize();
    }

 /*   public static void main(String[] args)
    {
        new AboutUI(null);
    }
*/
    /**
     * This method initializes and layouts the about information
     * 
     * @return void
     */
    private void initialize()
    {
        GridBagConstraints c = new GridBagConstraints();
        this.setVisible(false);
        this.getContentPane().setLayout(new BorderLayout());
        this.setUndecorated(true);
        this.getContentPane().add(getAboutPanel(), BorderLayout.NORTH);
        this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
        this.pack();

        if (getOwner() != null)
        {
            this.setLocation(getOwner().getX() + ((getOwner().getWidth() - this.getWidth()) / 2), getOwner().getY() + ((getOwner().getHeight() - this.getHeight()) / 2));
        } else
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
        }

        this.setSize(this.getWidth(), this.getHeight());
        this.setVisible(true);
    }

    private JScrollPane getAboutPanel()
    {
        if (aboutPanel == null)
        {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            // this.getContentPane().setBackground(Color.WHITE);
            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            c.insets = new Insets(10, 10, 10, 10);
            logoLabel = new JLabel(new ImageIcon(getClass().getResource(Messages.getString("Splash.Image"))));
            panel.add(logoLabel, c);

            c.gridy = 1;
            c.insets = new Insets(0, 10, 0, 10);
            aboutTextLabel = new JLabel(aboutText);
            panel.add(aboutTextLabel, c);

            c.gridy = 2;
            c.insets = new Insets(0, 10, 0, 10);
            // TODO: move in propertie files (tfreytag)
            homepageLabel = new JLabel("<html><p>" + Messages.getString("About.Homepage")+ "</p></html>");
            homepageLabel.addMouseListener(new LaunchDefaultBrowserAction("http://www.woped.org", homepageLabel));
            panel.add(homepageLabel, c);

            c.gridy = 3;
            c.insets = new Insets(0, 10, 0, 10);
            // TODO: move in propertie files (tfreytag)
            mailtoLabel = new JLabel("<html><p>" + Messages.getString("About.Email") + "</p></html>");
            mailtoLabel.addMouseListener(new LaunchDefaultBrowserAction("mailto:info@woped.org", mailtoLabel));
            panel.add(mailtoLabel, c);

            c.gridy = 4;
            c.insets = new Insets(0, 10, 0, 10);
            // TODO: move in propertie files (tfreytag)
            sfLabel = new JLabel("<html><p>" + Messages.getString("About.Development")+ "</p></html>");
            sfLabel.addMouseListener(new LaunchDefaultBrowserAction("http://sourceforge.net/projects/woped", sfLabel));
            panel.add(sfLabel, c);
            aboutPanel = new JScrollPane(panel);
        }
        return aboutPanel;
    }

    private JScrollPane getChangeLogPanel()
    {
        if (changeLogPanel == null)
        {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();
            c1.gridy = 0;
            c1.gridx = 0;
            c1.insets = new Insets(10, 10, 10, 10);
            c1.anchor = GridBagConstraints.WEST;

            // TODO: read changelog information out of the file !!! (silenco)
            String changeLog = "<html><p><font size=3>"+
                "<b>WoPeD 0.8.0</b> (2005/03/28)" +
                "<br>- first official binary release" +
                "<br><b>WoPeD untagged versions</b><br>- since May 2003.</font></p></html>";
            JLabel text = new JLabel(changeLog, JLabel.LEFT);
            panel.add(text, c1);
            JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel2.add(panel);
            changeLogPanel = new JScrollPane(panel2);
            changeLogPanel.setPreferredSize(getAboutPanel().getSize());
        }
        return changeLogPanel;
    }

    private JPanel getButtonPanel()
    {
        if (buttonPanel == null)
        {
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            GridBagConstraints c1 = new GridBagConstraints();

            /* About Button */
            aboutButton = new JButton(new AbstractAction()
            {
                public void actionPerformed(ActionEvent arg0)
                {
                    getContentPane().remove(getChangeLogPanel());
                    getContentPane().add(getAboutPanel(), BorderLayout.CENTER, 0);
                    aboutButton.setEnabled(false);
                    changelogButton.setEnabled(true);
                    pack();
                    repaint();
                }
            });

            aboutButton.setMnemonic(KeyEvent.VK_A);
            aboutButton.setIcon(new ImageIcon(getClass().getResource(Messages.getString("Action.ShowAbout.Icon"))));
            aboutButton.setText("About");
            aboutButton.setEnabled(false);
            c1.gridy = 0;
            c1.gridx = 0;
            c1.insets = new Insets(10, 10, 10, 10);
            c1.anchor = GridBagConstraints.WEST;
            buttonPanel.add(aboutButton, c1);

            /* Changelog Button */
            changelogButton = new JButton(new AbstractAction()
            {
                public void actionPerformed(ActionEvent arg0)
                {
                    getContentPane().remove(getAboutPanel());
                    getContentPane().add(getChangeLogPanel(), BorderLayout.CENTER, 0);
                    aboutButton.setEnabled(true);
                    changelogButton.setEnabled(false);
                    pack();
                    repaint();
                }
            });
            
            changelogButton.setMnemonic(KeyEvent.VK_L);
            changelogButton.setText("Versions");
            changelogButton.setIcon(new ImageIcon(getClass().getResource("/org/woped/editor/gui/images/changelog16.gif")));
            c1.gridy = 0;
            c1.gridx = 1;
            c1.insets = new Insets(0, 0, 0, 0);
            c1.anchor = GridBagConstraints.CENTER;
            buttonPanel.add(changelogButton, c1);

            /* Close Button */
            closeButton = new JButton(new DisposeWindowAction());
            closeButton.setMnemonic(KeyEvent.VK_C);
            closeButton.requestFocus();

            c1.gridy = 0;
            c1.gridx = 2;
            c1.insets = new Insets(10, 10, 10, 10);
            c1.anchor = GridBagConstraints.EAST;
            buttonPanel.add(closeButton, c1);
        }       
        return buttonPanel;
    }} // @jve:visual-info decl-index=0 visual-constraint="0,0"
