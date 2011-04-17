package org.woped.metrics.exceptions;

import java.util.ArrayList;
import java.util.Iterator;

import org.antlr.runtime.MismatchedTokenException;
import org.woped.metrics.formulaEnhancement.EnhancementException;

public class NestedCalculateFormulaException extends CalculateFormulaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3543263763837642671L;
	private ArrayList<CalculateFormulaException> errorList;
	
	/**
	 * @param errorList
	 */
	public NestedCalculateFormulaException(
			ArrayList<CalculateFormulaException> errorList) {
		super();
		this.errorList = errorList;
	}


	/* (non-Javadoc)
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getLocalizedMessage() {
		String s="";
		String newline = System.getProperty("line.separator");
		for(Iterator<CalculateFormulaException> iter = errorList.iterator(); iter.hasNext();){
			if (s.isEmpty()){
				s = iter.next().getLocalizedMessage();
			}else{
				s= s + newline + iter.next();
			}			
		}
		return s;
	}

	public String[][] getExceptionsAsTable() {
		String[][] table = new String[errorList.size()][1];
		int i = 0;
		for(Iterator<CalculateFormulaException> iter = errorList.iterator(); iter.hasNext();){
			table[i][0] = iter.next().getLocalizedMessage();
			i++;
		}		
		return table;
	}
	public int getNumberOfFormulaVariableNotFoundExceptions(){
		int i=0;
		for(CalculateFormulaException cfe:this.errorList){
			if(cfe instanceof FormulaVariableNotFoundException){
				i++;
			}
		}
		return i;
	}
	public ArrayList<FormulaVariableNotFoundException> getFormulaVariableNotFoundExceptions(){
		ArrayList<FormulaVariableNotFoundException> list = new ArrayList<FormulaVariableNotFoundException>();
		for(CalculateFormulaException cfe:this.errorList){
			if(cfe instanceof FormulaVariableNotFoundException){
				list.add((FormulaVariableNotFoundException)cfe);
			}
		}
		return list;
	}
	public ArrayList<EnhancementException> getEnhancementExceptions(){
		ArrayList<EnhancementException> list = new ArrayList<EnhancementException>();
		for(CalculateFormulaException cfe:this.errorList){
			if(cfe instanceof EnhancementException){
				list.add((EnhancementException)cfe);
			}
		}
		return list;
	}
	
	public ArrayList<MismatchedTokenException> getMismatchedTokenExceptions(){
		ArrayList<MismatchedTokenException> list = new ArrayList<MismatchedTokenException>();
		for(CalculateFormulaException cfe:this.errorList){
			if(cfe instanceof AntlrException){
				if(((AntlrException)cfe).getException() instanceof MismatchedTokenException)
				list.add((MismatchedTokenException)((AntlrException)cfe).getException());
			}
		}
		return list;
		
	}
	
	public ArrayList<AlgorithmIDTranslateException> getAlgorithmIDTranslateExceptions(){
		ArrayList<AlgorithmIDTranslateException> list = new ArrayList<AlgorithmIDTranslateException>();
		for(CalculateFormulaException cfe:this.errorList){
			if(cfe instanceof AlgorithmIDTranslateException){
				list.add((AlgorithmIDTranslateException)cfe);
			}
		}
		return list;		
	}
	
	public boolean hasEnhancementExceptions(){
		for(CalculateFormulaException cfe:this.errorList){
			if(cfe instanceof EnhancementException){
				return true;
			}
		}
		return false;
	}
	public void addError(CalculateFormulaException exception){
		this.errorList.add(exception);
	}
	

}
