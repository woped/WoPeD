package org.woped.metrics.exceptions;

import org.antlr.runtime.RecognitionException;

public class AntlrException extends CalculateFormulaException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5770557694101217879L;
	private RecognitionException exception;
	private String shortText;
	private static String[] tokens;
	
	/**
	 * @param exception
	 * @param shortText
	 */
	public AntlrException(RecognitionException exception, String shortText, String[] tokens) {
		super();
		this.exception 	= exception;
		this.shortText 	= shortText;
		AntlrException.tokens		= tokens;
	}
	/**
	 * @param exception
	 */
	public AntlrException(RecognitionException exception) {
		super();
		this.exception = exception;
	}
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	@Override
	public String getLocalizedMessage() {
		return shortText;
	}
	/**
	 * @return the exception
	 */
	public RecognitionException getException() {
		return exception;
	}
	/**
	 * @return the shortText
	 */
	public String getShortText() {
		return shortText;
	}
	
	public static String[] getTokenList(){
		return tokens;
	}
		
}
