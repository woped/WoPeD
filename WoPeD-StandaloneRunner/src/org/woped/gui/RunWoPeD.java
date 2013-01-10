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
package org.woped.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

import org.apache.log4j.xml.DOMConfigurator;
import org.woped.config.general.WoPeDGeneralConfiguration;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.Platform;
import org.woped.editor.controller.vep.ViewEvent;
import org.woped.gui.controller.vc.DefaultApplicationMediator;
import org.woped.gui.utilities.WopedLogger;
import org.woped.translations.Messages;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * This is the starter Class for WoPeD. <br>
 * You need to start WoPeD with this Class, if you want to enable the log.
 * 
 * 29.04.2003
 */
@SuppressWarnings("serial")
public class RunWoPeD extends JFrame {
	
	private 	   String[] files;
	private static RunWoPeD instance = null;
	private        DefaultApplicationMediator dam = null;
	
	 /**
     * 
     * Main Entry Point. Starts WoPeD and the GUI.
     *  
     */
    public static void main(String[] args) {      	
		instance = new RunWoPeD(args);
		
    	if (args.length > 0 && args[0].equals("-delay")) {
    		instance.WaitForSetupFinished();
    	}
    	
        SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			instance.run();
    		}
    	});
    }
	
	/**
	 * Constructor managing files passed over the command line (or over openFileEvent on MacOS)
	**/
    private RunWoPeD(String[] args) {
		dam = new DefaultApplicationMediator(null, new WoPeDGeneralConfiguration());
		
		/* Check if we are running on a Mac */
        if (Platform.isMac()) {            
            /* Set PNML open document handling in MacOS */
            OSXAdapter.setOpenFileHandler(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		files = new String[1];
            		files[0] = e.getActionCommand();
            	}
            });
 
            OSXAdapter.setQuitHandler(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		System.out.println("QuitHandler");
					dam.fireViewEvent(
							new ViewEvent(
									dam,
									AbstractViewEvent.VIEWEVENTTYPE_GUI,
									AbstractViewEvent.EXIT));

            	}
            });
           
            OSXAdapter.setAboutHandler(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		System.out.println("QuitHandler");
					dam.fireViewEvent(
							new ViewEvent(
									dam,
									AbstractViewEvent.VIEWEVENTTYPE_GUI,
									AbstractViewEvent.ABOUT));

            	}
            });
            
        	System.setProperty("apple.laf.useScreenMenuBar", "true");
        	System.setProperty("com.apple.mrj.application.apple.menu.about.name", "WoPeD");
        }
        else {
        	/* On other platforms simple command line argument passing is sufficient */
            files = args;
        }

        /* Adjust some font sizes for non-Windows platforms */
        if (!Platform.isWindows()) {            
        	UIManager.put("Button.font", new Font(Font.SANS_SERIF, 0, 11));
        	UIManager.put("Label.font", new Font(Font.SANS_SERIF, 0, 12));
        }
    }
    
	/**
	 * Init loggers for different WoPeD projects
	**/
    void initLogging() throws Exception {
    	DOMConfigurator.configure(RunWoPeD.class.getResource("/org/woped/gui/utilities/log4j.xml"));
	
    	LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(
				Constants.GUI_LOGGER)), Constants.GUI_LOGGER);
    	LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(
				org.woped.editor.Constants.EDITOR_LOGGER)),
				org.woped.editor.Constants.EDITOR_LOGGER);
    	LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(
				org.woped.file.Constants.FILE_LOGGER)),
				org.woped.file.Constants.FILE_LOGGER);
    	LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(
				org.woped.config.Constants.CONFIG_LOGGER)),
				org.woped.config.Constants.CONFIG_LOGGER);
    	LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(
				org.woped.core.Constants.CORE_LOGGER)),
				org.woped.core.Constants.CORE_LOGGER);				
    	LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(
				org.woped.quantana.Constants.QUANTANA_LOGGER)),
				org.woped.quantana.Constants.QUANTANA_LOGGER);
    	LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(
				org.woped.qualanalysis.Constants.QUALANALYSIS_LOGGER)),
				org.woped.qualanalysis.Constants.QUALANALYSIS_LOGGER);
    	LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(
				org.woped.translations.Constants.TRANSLATIONS_LOGGER)),
				org.woped.translations.Constants.TRANSLATIONS_LOGGER);
		
    	LoggerManager.info(Constants.GUI_LOGGER, "INIT APPLICATION");
    	Locale.setDefault(Locale.ENGLISH);
    }
    
	/**
	 * Run WoPeD by starting Default Application Mediator
	**/
	private void run() { 			
		try {
			initLogging();
			dam.startUI(files);
		} 
    	catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void WaitForSetupFinished() {
		
		// Wait until WoPeD setup utility has completely terminated
		try {
			while (!setupHasFinished()) {
				Thread.sleep(1000);
			}
		}
		catch(InterruptedException e){ } 

		int result = JOptionPane.showConfirmDialog(
				null, 
				Messages.getString("Dialog.StartWoPeD.Text"),
				Messages.getString("Dialog.StartWoPeD.Title"),
               JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.NO_OPTION) {
			System.exit(0);
		}
	}
	
	private boolean setupHasFinished() {
		try {
			String 	line;	
			String  pattern;
			Process p;
			
			if (Platform.isWindows()) {		
				p = Runtime.getRuntime().exec
			        	(System.getenv("windir") +"\\system32\\"+"tasklist.exe -v");
				pattern = "IzPack -";
			}
			else if (Platform.isMac()) {		
				p = Runtime.getRuntime().exec("ps -e");
				pattern = "Installer";
			}
			else {
				p = Runtime.getRuntime().exec("ps -e");
				pattern = "WoPeD-install-";				
			}

			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while ((line = input.readLine()) != null) {
				if (line.contains(pattern))
					return false;
			}
			
			input.close();
			
		} catch (Exception err) {
			err.printStackTrace();
		}
		
		return true;
	}
}
