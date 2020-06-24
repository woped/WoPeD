package org.woped.metrics.exceptions;

import org.woped.gui.translations.Messages;

/**
 * @author Tobias Lorentz
 *
 */
public class InfiniteRecursiveFormulaCallException extends CalculateFormulaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7271880222400627168L;
	
	private String recursivToken;

	public InfiniteRecursiveFormulaCallException(String recursivToken) {
		super();
		this.recursivToken = recursivToken;
	}

	public String getRecursivToken() {
		return recursivToken;
	}
	@Override
	public String getLocalizedMessage() {
		return Messages.getString("Metrics.Calculate.Error.InfiniteRecursivFormula") + " " + this.getRecursivToken(); 
	}
	
	
	
}
