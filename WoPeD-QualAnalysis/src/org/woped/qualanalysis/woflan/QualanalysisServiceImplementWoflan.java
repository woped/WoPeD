package org.woped.qualanalysis.woflan;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.service.AbstractQualanalysisService;

public class QualanalysisServiceImplementWoflan extends AbstractQualanalysisService {

    private WoflanAnalysis wA;

    public QualanalysisServiceImplementWoflan(IEditor editor) {

        super(editor);

        wA = new WoflanAnalysis(editor);
        sComponent = wA;
        soundnessCheck = wA;

        init();
    }

    /**
     * Call the woflan object's cleanup method to get rid of temporary files etc.
     */
    public void cleanup() {
        if (wA != null) {
            wA.Cleanup();
            wA = null;
        }
    }
}
