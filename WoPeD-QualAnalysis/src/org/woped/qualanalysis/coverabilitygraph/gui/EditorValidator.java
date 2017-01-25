package org.woped.qualanalysis.coverabilitygraph.gui;

import org.woped.core.controller.IEditor;

/**
 * Provides functionality to validate the related editor.
 */
public class EditorValidator {

    private final IEditor editor;
    private String lastFingerprint;

    public EditorValidator(IEditor editor){
        this.editor = editor;
        lastFingerprint = getLogicalFingerprint();
    }

    /**
     * Gets if the petri net in the editor has changed since the last check.
     *
     * @return true if the petriNet has changed, otherwise false
     */
    public boolean hasChanged(){
        return !lastFingerprint.equals(getLogicalFingerprint());
    }

    /**
     * Accepts the changes in the petri net and set the current state of the net valid.
     */
    public void acceptChanges(){
        this.lastFingerprint = getLogicalFingerprint();
    }

    private String getLogicalFingerprint() {
        return editor.getModelProcessor().getLogicalFingerprint();
    }
}
