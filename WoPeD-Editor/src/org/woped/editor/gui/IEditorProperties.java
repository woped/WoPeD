package org.woped.editor.gui;

import org.woped.core.model.petrinet.AbstractPetriNetElementModel;

public interface IEditorProperties
{
    public void show(AbstractPetriNetElementModel element);

    public void save();
}
