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
package org.woped.core.model.petrinet;

import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.AbstractModelProcessor;
import org.woped.core.model.CreationMap;

/**
 * @author <a href="mailto:slandes@kybeidos.de">Simon Landes </a> <br>
 *         <br>
 *         Note: (from JGraph) <br>
 *         When combining the attributes from a GraphCell with the attributes
 *         from the CellView, the graph cell’s attributes have precedence over
 *         the view’s attributes. The special value attribute is in sync with
 *         the cell’s user object.
 * 
 * 09.10.2003
 */
public abstract class AbstractPetriNetModelElement extends AbstractElementModel
{
    // Petrinet Types
    public static final int PLACE_TYPE          = 1;
    public static final int TRANS_SIMPLE_TYPE   = 2;
    public static final int TRANS_OPERATOR_TYPE = 3;
    public static final int SUBP_TYPE           = 4;
    public static final int TRIGGER_TYPE        = 5;
    public static final int NAME_TYPE           = 6;
    public static final int GROUP_TYPE          = 7;
    public static final int RESOURCE_TYPE       = 8;
    public static final int SIMULATION_TYPE		= 9;

    /**
     * Constructor for PetriNetElementModel.
     */
    public AbstractPetriNetModelElement(CreationMap creationMap)
    {
        this(creationMap, creationMap.getName());
    }

    /**
     * Constructor for PetriNetElementModel.
     */
    public AbstractPetriNetModelElement(CreationMap creationMap, Object userObject)
    {
        super(creationMap, userObject, AbstractModelProcessor.MODEL_PROCESSOR_PETRINET);
    }

}