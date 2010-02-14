package org.woped.qualanalysis.service;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.soundness.SComponentImplement;
import org.woped.qualanalysis.soundness.SoundnessCheckImplement;

public class QualanalysisServiceImplement extends AbstractQualanalysisService {

    public QualanalysisServiceImplement(IEditor editor) {
        super(editor);
        sComponent = new SComponentImplement(editor);
        soundnessCheck = new SoundnessCheckImplement(editor);
        init();
    }

    /**
     * Call the woflan object's cleanup method to get rid of temporary files etc.
     */
    public void cleanup() {}
}
