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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.woped.core.utilities.Platform;
import org.woped.editor.action.DisposeWindowAction;
import org.woped.editor.help.action.LaunchDefaultBrowserAction;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:freytag@dhbw-karlsruhe.de">Thomas Freytag </a> <br>
 *         <br>
 *         TODO: DOCUMENTATION (tfreytag)
 * 
 *         17.01.2005
 */

@SuppressWarnings("serial")
public class AboutUI extends JDialog {
	private JLabel logoLabel = null;
	private JLabel aboutTextLabel = null;
	private JLabel javaTextLabel = null;
	private JLabel homepageLabel = null;
	private JLabel mailtoLabel = null;
	private JLabel sfLabel = null;
	private JLabel icLabel = null;
	private WopedButton closeButton = null;
	private WopedButton aboutButton = null;
	private WopedButton changelogButton = null;

	private JScrollPane aboutPanel = null;
	private JScrollPane changeLogPanel = null;
	private JPanel buttonPanel = null;

	public AboutUI() {
		this(null);
	}

	/**
	 * Constructor for AboutUI.
	 * 
	 * @param owner
	 * @throws HeadlessException
	 */
	public AboutUI(Frame owner) throws HeadlessException {
		super(owner, true);
		initialize();
	}

	/*
	 * public static void main(String[] args) { new AboutUI(null); }
	 */
	/**
	 * This method initializes and layouts the about information
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setVisible(false);
		this.getContentPane().setLayout(new BorderLayout());
		this.setUndecorated(false);
		this.setResizable(false);
		this.getContentPane().add(getAboutPanel(), BorderLayout.NORTH);
		this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
		this.setTitle(Messages.getTitle("Action.ShowAbout"));
		this.pack();

		if (getOwner() != null) {
			this.setLocation(getOwner().getX() + ((getOwner().getWidth() - this.getWidth()) / 2),
					getOwner().getY() + ((getOwner().getHeight() - this.getHeight()) / 2));
		} else {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
		}

		this.setSize(this.getWidth(), this.getHeight());
	}

	private JScrollPane getAboutPanel() {
		String[] aboutArgs = { Messages.getWoPeDVersionWithTimestamp() };
		String aboutText = "<html><p>" + Messages.getStringReplaced("About.Text", aboutArgs)
				+ Messages.getString("About.Members") + Messages.getString("About.Thanks") + "</p></html>";
		String javaText = "<html><p><b>" + Messages.getString("About.Java") + ": </b>"
				+ System.getProperty("java.version") + "</p></html>";

		if (aboutPanel == null) {
			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();

			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.CENTER;
			c.insets = new Insets(10, 10, 10, 10);
			logoLabel = new JLabel(new ImageIcon(getClass().getResource(Messages.getString("Window.About.Image"))));
			panel.add(logoLabel, c);

			c.gridy = 1;
			c.insets = new Insets(0, 10, 0, 10);
			c.anchor = GridBagConstraints.WEST;
			javaTextLabel = new JLabel(javaText);
			panel.add(javaTextLabel, c);

			c.gridy = 2;
			c.insets = new Insets(0, 10, 0, 10);
			c.anchor = GridBagConstraints.WEST;
			aboutTextLabel = new JLabel(aboutText);
			panel.add(aboutTextLabel, c);

			c.gridy = 3;
			c.insets = new Insets(0, 10, 0, 10);
			homepageLabel = new JLabel("<html><p>" + Messages.getString("About.Homepage") + "</p></html>");
			homepageLabel.addMouseListener(
					new LaunchDefaultBrowserAction(Messages.getString("About.Homepage.Link"), homepageLabel));
			panel.add(homepageLabel, c);

			c.gridy = 4;
			c.insets = new Insets(0, 10, 0, 10);
			mailtoLabel = new JLabel("<html><p>" + Messages.getString("About.Email") + "</p></html>");
			mailtoLabel.addMouseListener(
					new LaunchDefaultBrowserAction(Messages.getString("About.Email.Link"), mailtoLabel));
			panel.add(mailtoLabel, c);

			c.gridy = 5;
			c.insets = new Insets(0, 10, 0, 10);
			sfLabel = new JLabel("<html><p>" + Messages.getString("About.Development") + "</p></html>");
			sfLabel.addMouseListener(
					new LaunchDefaultBrowserAction(Messages.getString("About.Development.Link"), sfLabel));
			panel.add(sfLabel, c);

			c.gridy = 6;
			c.insets = new Insets(0, 10, 0, 10);
			icLabel = new JLabel("<html><p>" + Messages.getString("About.Iconset") + "</p></html>");
			icLabel.addMouseListener(new LaunchDefaultBrowserAction(Messages.getString("About.Iconset.Link"), icLabel));
			panel.add(icLabel, c);

			aboutPanel = new JScrollPane(panel);
		}
		return aboutPanel;
	}

	private JScrollPane getChangeLogPanel() throws IOException {
		if (changeLogPanel == null) {

			// Try to find the directory where Changelog.txt is located
			String changeLog = "";
			String path = new java.io.File(".").getCanonicalPath();
			URL main = AboutUI.class.getResource("RunWoPeD.class");
			File file = new File(main.getPath());
			String mainpath = file.getAbsolutePath();
			
			if (mainpath.contains("!") && Platform.isMac()) {
				
				int pos = mainpath.indexOf("WoPeD-classes");
				if (pos > -1) {
					// Remove URL prefix
					int ff = mainpath.indexOf("file:") + 5;
					mainpath = mainpath.substring(ff);
					mainpath = mainpath.substring(0, pos - ff);
					mainpath = mainpath.replace("%20", " ");
					path = mainpath;
				}
			} else {
				// Running inside Eclipse, no WoPeD app
				int pos = path.indexOf("WoPeD-Starter");
				if (pos > -1) {
					path = path.substring(0, pos);
					path += "WoPeD-Installer" + File.separator + "build-tools";
				}
			}

			path += File.separator + "Changelog.txt";

			try {
				int c;
				FileReader f = new FileReader(path);
				while ((c = f.read()) != -1) {
					changeLog += (char) c;
				}
				f.close();
			} catch (IOException e) {
				changeLog = path + " not found";
			}

			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());
			GridBagConstraints c1 = new GridBagConstraints();
			c1.gridy = 0;
			c1.gridx = 0;
			c1.insets = new Insets(5, 5, 5, 5);
			c1.anchor = GridBagConstraints.WEST;

			JTextArea text = new JTextArea();
			text.setBackground(panel.getBackground());
			text.append(changeLog);
			panel.add(text, c1);
			JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel2.add(panel);
			changeLogPanel = new JScrollPane(panel2);
			changeLogPanel.setPreferredSize(getAboutPanel().getSize());
		}
		return changeLogPanel;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			GridBagConstraints c1 = new GridBagConstraints();

			/* About Button */
			aboutButton = new WopedButton(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						getContentPane().remove(getChangeLogPanel());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					getContentPane().add(getAboutPanel(), BorderLayout.CENTER, 0);
					aboutButton.setEnabled(false);
					changelogButton.setEnabled(true);
					pack();
					repaint();
				}
			});

			aboutButton.setMnemonic(KeyEvent.VK_A);
			aboutButton.setIcon(new ImageIcon(getClass().getResource(Messages.getString("Action.ShowAbout.Icon"))));
			aboutButton.setText(Messages.getString("Action.ShowAbout.Title"));
			aboutButton.setEnabled(false);
			c1.gridy = 0;
			c1.gridx = 0;
			c1.insets = new Insets(10, 10, 10, 10);
			c1.anchor = GridBagConstraints.WEST;
			buttonPanel.add(aboutButton, c1);

			/* Changelog Button */
			changelogButton = new WopedButton(new AbstractAction() {
				public void actionPerformed(ActionEvent arg0) {
					getContentPane().remove(getAboutPanel());
					try {
						getContentPane().add(getChangeLogPanel(), BorderLayout.CENTER, 0);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					aboutButton.setEnabled(true);
					changelogButton.setEnabled(false);
					pack();
					repaint();
				}
			});

			changelogButton.setMnemonic(KeyEvent.VK_L);
			changelogButton.setText(Messages.getString("Window.About.Versions"));
			changelogButton.setIcon(Messages.getImageIcon("Window.About.Versions"));
			c1.gridy = 0;
			c1.gridx = 1;
			c1.insets = new Insets(0, 0, 0, 0);
			c1.anchor = GridBagConstraints.CENTER;
			buttonPanel.add(changelogButton, c1);

			/* Close Button */
			closeButton = new WopedButton(new DisposeWindowAction());
			closeButton.setMnemonic(KeyEvent.VK_C);
			closeButton.requestFocus();

			c1.gridy = 0;
			c1.gridx = 2;
			c1.insets = new Insets(10, 10, 10, 10);
			c1.anchor = GridBagConstraints.EAST;
			buttonPanel.add(closeButton, c1);
		}
		return buttonPanel;
	}

} // @jve:visual-info decl-index=0 visual-constraint="0,0"
