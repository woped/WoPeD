package org.woped.core.gui;

import org.woped.core.controller.IEditor;

public interface IEditorList
{

    public void addEditor(IEditor editor);

    public void removeEditor(IEditor editor);

    public void selectEditor(IEditor editor);
}
