package org.woped.qualanalysis.service;

import org.woped.core.controller.IEditor;
import org.woped.qualanalysis.soundness.SComponentImplement;
import org.woped.qualanalysis.soundness.SoundnessCheckImplement;

/**
 * class for qualanalysis servies implemented completely in java.
 * 
 * @author Patrick Spies, Patrick Kirchgaessner, Joern Liebau, Enrico Moeller, Sebastian Fuss
 * 
 */
public class QualanalysisServiceImplement extends AbstractQualanalysisService {

	/**
	 * 
	 * @param editor
	 *            the editor the service is referring to
	 */
	public QualanalysisServiceImplement(IEditor editor) {
		super(editor);
		sComponent = new SComponentImplement(editor);
		soundnessCheck = new SoundnessCheckImplement(editor);
	}

	public void cleanup() {
	}
}