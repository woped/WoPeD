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
package org.woped.core.view;


import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.GraphCellEditor;
import org.jgraph.graph.VertexView;

/**
 * @author Simon Landes
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
@SuppressWarnings("serial")
public abstract class AbstractElementView extends VertexView {

    /**
     * Constructor for PetriNetElementView.
     * 
     * @param cell
     * @param graph
     * @param mapper
     */
    public AbstractElementView(Object cell) {
    	super(cell);
        setAttributes(new AttributeMap());
    }

    /**
     * @see com.jgraph.graph.CellView#getEditor()
     */
    public GraphCellEditor getEditor() {
    	return cellEditor;
    }
}