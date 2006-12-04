package org.woped.core.model.petrinet;

import org.jgraph.graph.DefaultPort;
import org.woped.core.model.AbstractElementModel;
import org.woped.core.model.ModelElementContainer;
import org.woped.core.model.ModelElementFactory;

//! Interface exported by all elements that contain children
public interface InnerElementContainer {

	//! Retrieve the inner model element container
	//! @return model element container
    public ModelElementContainer getSimpleTransContainer();
	
    public AbstractElementModel addElement(AbstractElementModel element);
    public void addReference(String arcId, DefaultPort sourceId, DefaultPort targetId);
    public AbstractElementModel getElement(Object elementId);

    //! Assign and return a new element ID 
    //! (global over the whole net incl. sub-nets)
    //! @return element ID string
    public String getNewElementId();

	
}
