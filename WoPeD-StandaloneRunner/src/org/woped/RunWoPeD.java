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
package org.woped;

import org.woped.config.WoPeDConfiguration;
import org.woped.controller.DefaultApplicationMediator;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * This ist the starter Class for WoPeD. <br>
 * You need to start WoPeD with this Class, if you want to enable the log.
 * 
 * 29.04.2003
 */
public class RunWoPeD implements WoPeDLogger
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
            // create & init GUI
            DefaultApplicationMediator mediator = new DefaultApplicationMediator(null, new WoPeDConfiguration(), args);
        } catch (RuntimeException e1)
        {
            e1.printStackTrace();
            // Error occured while init -> quit
            System.exit(1);
        }
    }

}