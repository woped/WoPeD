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

import java.rmi.RemoteException;
import java.util.Locale;

import javax.swing.JApplet;
import javax.swing.JOptionPane;

import org.apache.log4j.xml.DOMConfigurator;
import org.woped.config.WoPeDConfiguration;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.help.HelpBrowser;
import org.woped.gui.controller.DefaultApplicationMediator;
import org.woped.gui.utilities.WopedLogger;
import org.woped.qualanalysis.simulation.controller.ReferenceProvider;
import org.woped.server.ServerLoader;
import org.woped.server.holder.UserHolder;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * This ist the starter Class for WoPeD. <br>
 * You need to start WoPeD with this Class, if you want to enable the log.
 * 
 * 29.04.2003
 */
public class RunWoPeD extends JApplet {
	
	// flag for Applet
	private static boolean isApplet = false;
	
	// instance from RunWoPeD
	private static RunWoPeD applet;
	
	
	 /**
     * 
     * Main Entry Point. Starts the GUI.
     *  
     */
    public static void main(String[] args)
    {
    	final String arguments[] = args;
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			RunWoPeD.run(arguments);
    		}
    	});
    }
	
	/**
	 * 
	 * Main Entry Point. Starts the GUI.
	 * 
	 */
	public void init() {
		// Run as Applet
		isApplet = true;
		// Instanze des Applets
		applet = this;
		// Extract Arguments
		try {
			UserHolder.setUserID(ServerLoader.getInstance().getUserID(getParameter("sessionid")));
			if (UserHolder.getUserID() == -1) {
				JOptionPane.showMessageDialog(this,"Session not valid!");
			}
		} catch (RemoteException e) {
			// fatal close applet
			JOptionPane.showMessageDialog(this, "Error during initialisation! \n" + e.getMessage());
			System.exit(0);
		}catch (NullPointerException e){
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		// Arguments Field
		final String arguments[] = new String[1];
		arguments[0] = getParameter("modellid");
		if (UserHolder.getUserID() != -1) {
			RunWoPeD.run(arguments);
		} else {
			RunWoPeD.run(null);
		}		
		
	}

	 private static void run(String[] args)
    { 	try {
			// Loading Logger!
			try {
				DOMConfigurator.configure(RunWoPeD.class
						.getResource("/org/woped/gui/utilities/log4j.xml"));
				LoggerManager
						.register(new WopedLogger(org.apache.log4j.Logger
								.getLogger(Constants.GUI_LOGGER)),
								Constants.GUI_LOGGER);
				LoggerManager.register(new WopedLogger(org.apache.log4j.Logger
						.getLogger(org.woped.editor.Constants.EDITOR_LOGGER)),
						org.woped.editor.Constants.EDITOR_LOGGER);
				LoggerManager.register(new WopedLogger(org.apache.log4j.Logger
						.getLogger(org.woped.file.Constants.FILE_LOGGER)),
						org.woped.file.Constants.FILE_LOGGER);
				LoggerManager.register(new WopedLogger(org.apache.log4j.Logger
						.getLogger(org.woped.config.Constants.CONFIG_LOGGER)),
						org.woped.config.Constants.CONFIG_LOGGER);
				LoggerManager.register(new WopedLogger(org.apache.log4j.Logger
						.getLogger(org.woped.core.Constants.CORE_LOGGER)),
						org.woped.core.Constants.CORE_LOGGER);
				LoggerManager
						.register(
								new WopedLogger(
										org.apache.log4j.Logger
												.getLogger(org.woped.quantana.Constants.QUANTANA_LOGGER)),
								org.woped.quantana.Constants.QUANTANA_LOGGER);
				LoggerManager
						.register(
								new WopedLogger(
										org.apache.log4j.Logger
												.getLogger(org.woped.qualanalysis.Constants.QUALANALYSIS_LOGGER)),
								org.woped.qualanalysis.Constants.QUALANALYSIS_LOGGER);
				LoggerManager
						.register(
								new WopedLogger(
										org.apache.log4j.Logger
												.getLogger(org.woped.translations.Constants.TRANSLATIONS_LOGGER)),
								org.woped.translations.Constants.TRANSLATIONS_LOGGER);
				LoggerManager
				.register(
						new WopedLogger(
								org.apache.log4j.Logger
										.getLogger(org.woped.applet.Constants.APPLET_LOGGER)),
						org.woped.applet.Constants.APPLET_LOGGER);
				LoggerManager.info(Constants.GUI_LOGGER, "INIT APPLICATION");
			} catch (Exception e) {
				System.err.println("ERROR ACTIVATING LOGGER");
			}

			// Enable Mac specific behaviour.
			// not when started as Applet because of setting the system
			// information
			if (!isApplet) {
				// The menu bar goes to the top of the screen, instead of the
				// top of the window.
				try {
					System.setProperty("apple.laf.useScreenMenuBar", "true");
				} catch (Exception e) {
					LoggerManager.info(Constants.GUI_LOGGER, "apple.laf.useScreenMenuBar");
				}
				Locale.setDefault(Locale.ENGLISH);
			}

			// create & init GUI
			DefaultApplicationMediator mainwindow = new DefaultApplicationMediator(null, new WoPeDConfiguration(isApplet), null);

			ReferenceProvider helper = new ReferenceProvider();
			helper.setMediatorReference(mainwindow);

			// set codebase that helpfiles can be found
			if (isApplet) {
				HelpBrowser.getInstance().setStartedAsApplet(true);
				HelpBrowser.getInstance().setCodeBase(applet.getCodeBase());
				System.out.println(applet.getCodeBase());
			}
		} catch (RuntimeException e1) {
			e1.printStackTrace();
			// Error occured while init -> quit
			System.exit(1);
		}
	}
	
	
	/**
	 * isApplet()
	 * <p>
	 * checks if the Application run as Applet
	 * @return
	 */
	static public boolean isApplet() {
		return isApplet;
	}
	
	
	
}
