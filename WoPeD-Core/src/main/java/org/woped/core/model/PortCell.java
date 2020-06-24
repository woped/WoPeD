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
package org.woped.core.model;

import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Port;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 * 
 * 
 * 14.05.2003
 */

@SuppressWarnings("serial")
public class PortCell extends DefaultPort
{

    /**
     * Constructor for PetriPortCell.
     */
    public PortCell()
    {
        super();
    }

    /**
     * Constructor for PetriPortCell.
     * 
     * @param userObject
     */
    public PortCell(Object userObject)
    {
        super(userObject);
    }

    /**
     * Constructor for PetriPortCell.
     * 
     * @param userObject
     * @param anchor
     */
    public PortCell(Object userObject, Port anchor)
    {
        super(userObject, anchor);
    }

}