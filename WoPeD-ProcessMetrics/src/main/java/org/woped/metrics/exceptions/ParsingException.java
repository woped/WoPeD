package org.woped.metrics.exceptions;

import org.woped.gui.translations.Messages;

public class ParsingException extends CalculateFormulaException {
	private String formula;
	private int position;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7082605733283512108L;
	public ParsingException(String formula, int position) {
		super();
		this.formula	= formula;
		this.position	= position;
	}
	@Override
	public String getLocalizedMessage() {
		return Messages.getString("Metrics.Calculate.Error.Parsing_Part1") + " " + formula + 
		Messages.getString("Metrics.Calculate.Error.Parsing_Part2") + " " + position;
	}
	public String getFormula() {
		return formula;
	}
	public int getPosition() {
		return position;
	}
	
	
	
}
