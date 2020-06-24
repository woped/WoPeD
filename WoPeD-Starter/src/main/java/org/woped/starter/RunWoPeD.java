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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.xml.DOMConfigurator;
import org.woped.config.general.WoPeDGeneralConfiguration;
import org.woped.core.config.DefaultStaticConfiguration;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.ViewEvent;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Platform;
import org.woped.gui.translations.Messages;
import org.woped.starter.controller.vc.DefaultApplicationMediator;
import org.woped.starter.utilities.WopedLogger;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 *         This is the starter Class for WoPeD. <br>
 *         You need to start WoPeD with this Class, if you want to enable the
 *         log.
 * 
 *         29.04.2003
 */

@SuppressWarnings("serial")
public class RunWoPeD extends JFrame {

	private static RunWoPeD m_instance = null;

	private String[] m_filesToOpen = null;
	private DefaultApplicationMediator m_dam = null;

	/**
	 * 
	 * Main Entry Point. Starts WoPeD and the GUI.
	 * 
	 */
	public static void main(String[] args) {
		
		if (!System.getProperty("java.version").startsWith("1.8"))
		{
			JOptionPane.showMessageDialog(null, Messages.getString("Wrong.Java.Version.Text", new Object[] {System.getProperty("java.version")}),
					Messages.getString("Wrong.Java.Version.Title"), JOptionPane.OK_OPTION);
			System.exit(0);
		}
		
		boolean startDelayed = false;
		boolean forceGerman = false;
		boolean forceEnglish = false;

		for (String arg : args) {

			if (arg.equals("-delay")) {
				startDelayed = true;
			}
			if (arg.equals("-german")) {
				forceGerman = true;
			}
			if (arg.equals("-english")) {
				forceEnglish = true;
			}
		}

		if (startDelayed || forceGerman || forceEnglish)
			args = null;

		if (forceGerman)
			Locale.setDefault(Locale.GERMANY);
		if (forceEnglish)
			Locale.setDefault(Locale.ENGLISH);

		m_instance = new RunWoPeD(args);

		if (startDelayed) {
			m_instance.WaitForSetupFinished();
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				m_instance.run();
			}
		});
	}

	/**
	 * Constructor
	 **/
	private RunWoPeD(String[] args) {
		m_filesToOpen = args;

		initLogging();
		m_dam = new DefaultApplicationMediator(null, new WoPeDGeneralConfiguration());
		
		initUI();
	}

	/**
	 * Initialize GUI
	 **/
	private void initUI() {
		/* If we are running on a Mac, set associated screen menu handlers */
		if (Platform.isMac()) {
			OSXAdapter.setOpenFileHandler(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					m_filesToOpen = new String[1];
					m_filesToOpen[0] = e.getActionCommand();
				}
			});

			OSXAdapter.setQuitHandler(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					m_dam.fireViewEvent(
							new ViewEvent(m_dam, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.EXIT));

				}
			});

			OSXAdapter.setAboutHandler(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					m_dam.fireViewEvent(
							new ViewEvent(m_dam, AbstractViewEvent.VIEWEVENTTYPE_GUI, AbstractViewEvent.ABOUT));

				}
			});

			OSXAdapter.setPreferencesHandler(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					m_dam.fireViewEvent(new ViewEvent(m_dam, AbstractViewEvent.VIEWEVENTTYPE_APPLICATION,
							AbstractViewEvent.CONFIG));

				}
			});
		}

		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "WoPeD");

		// Set some fonts to make WoPeD look better on Mac and Linux
		if (!Platform.isWindows()) {
			UIManager.put("Button.font", DefaultStaticConfiguration.DEFAULT_LABEL_FONT);
			UIManager.put("MenuItem.font", DefaultStaticConfiguration.DEFAULT_LABEL_FONT);
			UIManager.put("Label.font", DefaultStaticConfiguration.DEFAULT_BIGLABEL_FONT);
			UIManager.put("RadioButton.font", DefaultStaticConfiguration.DEFAULT_BIGLABEL_FONT);
			UIManager.put("CheckBox.font", DefaultStaticConfiguration.DEFAULT_BIGLABEL_FONT);
			UIManager.put("CheckBox.font", DefaultStaticConfiguration.DEFAULT_BIGLABEL_FONT);
			UIManager.put("TextField.font", DefaultStaticConfiguration.DEFAULT_BIGLABEL_FONT);
			UIManager.put("TextArea.font", DefaultStaticConfiguration.DEFAULT_LABEL_FONT);
			UIManager.put("ComboBox.font", DefaultStaticConfiguration.DEFAULT_BIGLABEL_FONT);
			UIManager.put("PopupMenu.font", DefaultStaticConfiguration.DEFAULT_BIGLABEL_FONT);
			UIManager.put("TitledBorder.font", DefaultStaticConfiguration.DEFAULT_LABEL_BOLDFONT);
			UIManager.put("TabbedPane.font", DefaultStaticConfiguration.DEFAULT_BIGLABEL_FONT);
		}
	}

	/**
	 * Init loggers for different WoPeD components
	 **/
	private void initLogging() {

		DOMConfigurator.configure(RunWoPeD.class.getResource("/log4j.xml"));

		LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(Constants.GUI_LOGGER)),
				Constants.GUI_LOGGER);
		LoggerManager.register(
				new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.editor.Constants.EDITOR_LOGGER)),
				org.woped.editor.Constants.EDITOR_LOGGER);
		LoggerManager.register(
				new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.config.Constants.CONFIG_LOGGER)),
				org.woped.config.Constants.CONFIG_LOGGER);
		LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.file.Constants.FILE_LOGGER)),
				org.woped.file.Constants.FILE_LOGGER);
		LoggerManager.register(
				new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.config.Constants.CONFIG_LOGGER)),
				org.woped.config.Constants.CONFIG_LOGGER);
		LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.core.Constants.CORE_LOGGER)),
				org.woped.core.Constants.CORE_LOGGER);
		LoggerManager.register(
				new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.quantana.Constants.QUANTANA_LOGGER)),
				org.woped.quantana.Constants.QUANTANA_LOGGER);
		LoggerManager.register(
				new WopedLogger(
						org.apache.log4j.Logger.getLogger(org.woped.qualanalysis.Constants.QUALANALYSIS_LOGGER)),
				org.woped.qualanalysis.Constants.QUALANALYSIS_LOGGER);
		LoggerManager.register(
				new WopedLogger(
						org.apache.log4j.Logger.getLogger(org.woped.gui.translations.Constants.TRANSLATIONS_LOGGER)),
				org.woped.gui.translations.Constants.TRANSLATIONS_LOGGER);
		LoggerManager.register(
				new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.apromore.Constants.APROMORE_LOGGER)),
				org.woped.apromore.Constants.APROMORE_LOGGER);
		LoggerManager.register(
				new WopedLogger(org.apache.log4j.Logger
						.getLogger(org.woped.quantana.dashboard.webserver.Constants.DASHBOARDWEBSRV_LOGGER)),
				org.woped.quantana.dashboard.webserver.Constants.DASHBOARDWEBSRV_LOGGER);
		LoggerManager.register(
				new WopedLogger(org.apache.log4j.Logger
						.getLogger(org.woped.quantana.dashboard.storage.Constants.DASHBOARDSTORE_LOGGER)),
				org.woped.quantana.dashboard.storage.Constants.DASHBOARDSTORE_LOGGER);

		LoggerManager.info(Constants.GUI_LOGGER, "INIT APPLICATION");
	}

	/**
	 * Run WoPeD by starting Default Application Mediator
	 **/
	private void run() {
		try {
			m_dam.startUI(m_filesToOpen);
		} catch (Exception e) {
			LoggerManager.error(Constants.GUI_LOGGER, "Could not start WoPeD");
			System.exit(1);
		}
	}

	/**
	 * Wait until WoPeD setup utility has completely terminated
	 **/
	private void WaitForSetupFinished() {

		try {
			while (!setupHasFinished()) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException ignored) {
		}

		new AskToStartWoPeDUI(this).setVisible(true);
	}

	/**
	 * Check for process ID of Mac Installer app
	 **/
	private boolean setupHasFinished() {
		try {
			String line;
			String pattern;
			Process p;

			if (Platform.isMac()) {
				p = Runtime.getRuntime().exec("ps -e");
				pattern = "Installer";
				BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					if (line.contains(pattern))
						return false;
				}

				input.close();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}

		return true;
	}
}
