package org.woped.qualanalysis.service;

import org.woped.core.controller.IEditor;

/**
 * factory class of QualanalysisService
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class QualAnalysisServiceFactory {

    /**
     * 
     * @param editor creates service for the provided editor.
     * @return QualanalysisServiceObject
     */
    public static IQualanalysisService createNewQualAnalysisService(IEditor editor) {
        return new QualanalysisServiceImplement(editor);
    }
}
