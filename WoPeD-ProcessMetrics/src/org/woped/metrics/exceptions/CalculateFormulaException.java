package org.woped.metrics.exceptions;

import org.woped.gui.translations.Messages;

/**
 * @author Tobias Lorentz
 *
 */
public class CalculateFormulaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4048062193204362939L;

	public CalculateFormulaException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CalculateFormulaException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CalculateFormulaException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CalculateFormulaException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	public static String getShortError(){
		return Messages.getString("Metrics.Calculate.Error.Short");
	}
	

}
