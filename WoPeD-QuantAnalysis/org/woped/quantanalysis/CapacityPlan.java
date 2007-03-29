package org.woped.quantanalysis;

import org.woped.core.model.ModelElementContainer;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.controller.vc.EditorVC;

public class CapacityPlan {

	EditorVC editor = null;
	ModelElementContainer mec = null;
	QuantitativeAnalysis qa = null;
	
	/**
	 * @param args
	 */
	public CapacityPlan(EditorVC editor) {
		this.editor = editor;
		mec = editor.getModelProcessor().getElementContainer();
		
		LoggerManager.info(Constants.QUANTANA_LOGGER, "Quantitative Analyse läuft.");
		
		qa = new QuantitativeAnalysis(null ,true, editor);
//		qa.setAlwaysOnTop(true);
		qa.setVisible(true);
	}
	
	public void message(String msg)
	{
		System.out.println(msg);
	}
}
