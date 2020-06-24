package org.woped.metrics.formulaEnhancement;

import org.woped.gui.translations.Messages;
import org.woped.metrics.exceptions.CalculateFormulaException;

public class EnhancementException extends CalculateFormulaException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7934775633474696308L;
	String enhancedFormula;
	double value;
	
	public EnhancementException(String enhancedFormula, double value) {
		super();
		this.enhancedFormula 	= enhancedFormula;
		this.value				= value;
	}

	public String getEnhancedFormula() {
		return enhancedFormula;
	}
	
	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return Messages.getString("Metrics.Calculate.Error.EnhancementException") +"\""+ enhancedFormula + "\"";
	}

	@Override
	 public String getLocalizedMessage() {
		  return Messages.getString("Metrics.Calculate.Error.EnhancementException") +"\""+ enhancedFormula + "\"";
		 }
}
