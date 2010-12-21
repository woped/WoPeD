package org.woped.metrics.exceptions;

import org.woped.translations.Messages;

public class FormulaVariableNotFoundException extends CalculateFormulaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3548418885943793131L;
	
	private String variable;
	
	public FormulaVariableNotFoundException(String variable) {
		super();
		this.variable = variable;
	}

	public String getVariable() {
		return variable;
	}

	@Override
	public String getLocalizedMessage() {
		return Messages.getString("Metrics.Calculate.Error.FormulaVariableNotFound") + " " + this.getVariable();
	}
	
	
}
