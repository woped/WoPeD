package org.woped.editor.gui;

import org.woped.core.model.petrinet.AbstractPetriNetModelElement;

public interface IEditorProperties
{
    public void show(AbstractPetriNetModelElement element);
    public void save();
}
