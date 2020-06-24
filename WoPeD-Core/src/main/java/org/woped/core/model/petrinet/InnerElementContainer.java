package org.woped.core.model.petrinet;

import org.jgraph.graph.DefaultPort;
import org.woped.core.model.ModelElementContainer;

//! Interface exported by all elements that contain children
public interface InnerElementContainer {

	//! Retrieve the inner model element container
	//! @return model element container
    public ModelElementContainer getSimpleTransContainer();
	
    public AbstractPetriNetElementModel addElement(AbstractPetriNetElementModel element);
    public void addReference(String arcId, DefaultPort sourceId, DefaultPort targetId);
    public AbstractPetriNetElementModel getElement(Object elementId);

    //! Assign and return a new element ID 
    //! (global over the whole net incl. sub-nets)
    //! @return element ID string
    public String getNewElementId();

	
}
