package org.woped.qualanalysis.structure.components;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

public class RouteInfo {
	// ! Store a reference to the predecessor
    // ! on the route back to the source
    // ! null if no connection to the source exists
    public RouteInfo m_predecessor = null;
    // ! stores the number of arcs between the
    // ! source and this element
    // ! or -1 if no connection exists
    public int m_nDistanceToSource = -1;
    // ! Stores a reference to the actual petri-net
    // ! element this entry has been created for
    public AbstractPetriNetElementModel m_thisElement = null;
}
