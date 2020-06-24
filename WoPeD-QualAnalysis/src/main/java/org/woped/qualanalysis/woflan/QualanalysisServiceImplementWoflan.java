package org.woped.qualanalysis.woflan;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.service.AbstractQualanalysisService;

/**
 * class for qualanalysis servies basing on woflan.dll
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class QualanalysisServiceImplementWoflan extends AbstractQualanalysisService {

    private WoflanAnalysis wA;

    /**
     * 
     * @param editor the editor the service is referring to
     */
    public QualanalysisServiceImplementWoflan(IEditor editor) {
        super(editor);

        wA = new WoflanAnalysis(editor);
        sComponent = wA;
        soundnessCheck = wA;
    }

    /**
     * Call the woflan object's cleanup method to get rid of temporary files etc.
     */
    public void cleanup() {
        if (wA != null) {
            wA.cleanup();
            wA = null;
        }
    }
}
