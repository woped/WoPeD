package org.woped.quantanalysis;

import org.woped.core.model.ModelElementContainer;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.vc.EditorVC;

public class QuantAnaStart {

	EditorVC editor = null;
	ModelElementContainer mec = null;
	QuantitativeAnalysisDialog qa = null;
	
	/**
	 * @param args
	 */
	public QuantAnaStart(EditorVC editor) {
		this.editor = editor;
		mec = editor.getModelProcessor().getElementContainer();
		LoggerManager.info(Constants.QUANTANA_LOGGER, "Quantitative Analyse läuft.");
		
		qa = new QuantitativeAnalysisDialog(null ,true, editor);
	}
	
	public void message(String msg)
	{
		System.out.println(msg);
	}
}
