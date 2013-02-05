package org.woped.metrics.exceptions;

import org.woped.gui.translations.Messages;

public class NaNException extends CalculateFormulaException{

	private static final long serialVersionUID = -3777609671032281869L;

	@Override
	public String getLocalizedMessage() {
		return Messages.getString("Metrics.Calculate.Error.NaN");
	}
	
}
