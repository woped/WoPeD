package org.woped.core.gui;

import org.woped.core.controller.IEditor;

public interface IEditorAware
{

    public void addEditor(IEditor editor);

    public void removeEditor(IEditor editor);

    public void selectEditor(IEditor editor);
    
    //! This method is called if an IEditor object is renamed
    //! This gives editor-aware components a chance to update
    //! their display of the editor reference (e.g. the task bar)
    //! @param editor specifies the editor whose name has changed
    public void renameEditor(IEditor editor);

	public void addTextEditor(IEditor editor);
}
