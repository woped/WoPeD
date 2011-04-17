package org.woped.metrics.exceptions;

public class AlgorithmIDTranslateException extends CalculateFormulaException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2911249440277600190L;
	private String descString;
	private String idString;
	public AlgorithmIDTranslateException(String descString, String idString) {
		super();
		this.descString = descString;
		this.idString = idString;
	}
	
	public String getDescString(){
		return this.descString;		
	}
	public String getIdString(){
		return this.idString;
	}

	@Override
	public String getLocalizedMessage() {
		return "Algorith-Description should be replaced with Alogrith-ID";
	}
	
	
}
