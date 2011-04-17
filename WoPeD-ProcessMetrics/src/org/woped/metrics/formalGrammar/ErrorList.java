package org.woped.metrics.formalGrammar;

import java.util.ArrayList;

import org.woped.metrics.exceptions.CalculateFormulaException;

/**
 * @author Tobias Lorentz
 *
 */
public class ErrorList {
	private ArrayList<CalculateFormulaException> errorList;
	private static ErrorList instance;
	
	
	private ErrorList(){
		errorList = new ArrayList<CalculateFormulaException>();
	}
	
	public static ErrorList getInstance(){
		if (instance == null){
			instance = new ErrorList();
		}
		return instance;
	}
	
	public void addException(CalculateFormulaException e){
		errorList.add(e);
	}
	
	public ArrayList<CalculateFormulaException> getErrorList(){
		return this.errorList;
	}
	
	public void clear(){
		errorList.clear();
	}

	public boolean isEmpty() {
		return errorList.isEmpty();
	}
	
	
}
