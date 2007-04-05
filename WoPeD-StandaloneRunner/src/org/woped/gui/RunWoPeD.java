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

import java.util.Locale;

import org.apache.log4j.xml.DOMConfigurator;
import org.woped.config.WoPeDConfiguration;
import org.woped.core.utilities.LoggerManager;
import org.woped.gui.controller.DefaultApplicationMediator;
import org.woped.gui.utilities.WopedLogger;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * This ist the starter Class for WoPeD. <br>
 * You need to start WoPeD with this Class, if you want to enable the log.
 * 
 * 29.04.2003
 */
public class RunWoPeD
{

    /**
     * 
     * Main Entry Point. Starts the GUI.
     *  
     */
    public static void main(String[] args)
    {
        try
        {
            // Loading Logger!
            try
            {
                DOMConfigurator.configure(RunWoPeD.class.getResource("/org/woped/gui/utilities/log4j.xml"));
                LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(Constants.GUI_LOGGER)), Constants.GUI_LOGGER);
                LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.editor.Constants.EDITOR_LOGGER)), org.woped.editor.Constants.EDITOR_LOGGER);
                LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.file.Constants.FILE_LOGGER)), org.woped.file.Constants.FILE_LOGGER);
                LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.config.Constants.CONFIG_LOGGER)), org.woped.config.Constants.CONFIG_LOGGER);
                LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.core.Constants.CORE_LOGGER)), org.woped.core.Constants.CORE_LOGGER);
                LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.woflan.Constants.WOFLAN_LOGGER)), org.woped.woflan.Constants.WOFLAN_LOGGER);
                LoggerManager.register(new WopedLogger(org.apache.log4j.Logger.getLogger(org.woped.quantanalysis.Constants.QUANTANA_LOGGER)), org.woped.quantanalysis.Constants.QUANTANA_LOGGER);
                LoggerManager.info(Constants.GUI_LOGGER, "INIT APPLICATION");
            } catch (Exception e)
            {
                System.err.println("ERROR ACTIVATING LOGGER");
            }
            // create & init GUI
            Locale.setDefault(Locale.ENGLISH);
            new DefaultApplicationMediator(null, new WoPeDConfiguration(), args);
        } catch (RuntimeException e1)
        {
            e1.printStackTrace();
            // Error occured while init -> quit
            System.exit(1);
        }
    }

}