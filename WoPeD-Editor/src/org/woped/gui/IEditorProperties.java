package org.woped.gui;

import org.woped.model.petrinet.AbstractPetriNetModelElement;

public interface IEditorProperties
{
    public void show(AbstractPetriNetModelElement element);
    public void save();
}
