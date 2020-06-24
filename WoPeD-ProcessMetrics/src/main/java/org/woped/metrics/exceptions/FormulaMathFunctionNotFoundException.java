package org.woped.metrics.exceptions;

import org.woped.gui.translations.Messages;

public class FormulaMathFunctionNotFoundException extends
		CalculateFormulaException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2149987458778832817L;
	private String function;
	@Override
	public String getLocalizedMessage() {
		return Messages.getString("Metrics.Calculate.Error.FormulaMathFunctionNotFound") + " : " + function;
	}
	public FormulaMathFunctionNotFoundException(String function) {
		super();
		this.function = function;
	}
	
	
	
}
