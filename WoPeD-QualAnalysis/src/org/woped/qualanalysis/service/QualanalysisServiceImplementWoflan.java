package org.woped.qualanalysis.service;

import java.io.File;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.WoflanAnalysis;

public class QualanalysisServiceImplementWoflan extends AbstractQualanalysisService {

	private WoflanAnalysis wA;

	public QualanalysisServiceImplementWoflan(IEditor editor, File temporaryFile) {

		super(editor);

		wA = new WoflanAnalysis(editor, temporaryFile);
		sComponent = wA;
		soundnessCheck = wA;

		init();
	}

	/**
	 * Call the woflan object's cleanup method to get rid of temporary files
	 * etc.
	 */
	public void cleanup() {
		if (wA != null) {
			wA.Cleanup();
			wA = null;
		}
	}
}
