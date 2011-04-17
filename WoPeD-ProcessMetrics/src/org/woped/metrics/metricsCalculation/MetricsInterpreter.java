package org.woped.metrics.metricsCalculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.woped.metrics.exceptions.AlgorithmIDTranslateException;
import org.woped.metrics.exceptions.AntlrException;
import org.woped.metrics.exceptions.CalculateFormulaException;
import org.woped.metrics.exceptions.FormulaMathFunctionNotFoundException;
import org.woped.metrics.exceptions.FormulaVariableNotFoundException;
import org.woped.metrics.exceptions.NestedCalculateFormulaException;
import org.woped.metrics.exceptions.ParsingException;
import org.woped.metrics.formalGrammar.metricsGrammarLexer;
import org.woped.metrics.formalGrammar.metricsGrammarParser;
import org.woped.metrics.formulaEnhancement.EnhancementException;

/**
 * @author Tobias Lorentz
 * This class handels the parsing-process from technical side,
 * tries to handle exception or to solve problems automatically
 */
public class MetricsInterpreter {

	/**
	 * Interprets a string in accordance with Lexer grammer
	 * 
	 * @param formula		Formula to be interpreted
	 * @param mc			Metrics calculator to calculate results
	 * @param stack			Stack of previously called parent tokens
	 * @param doNotDisplay	Whether or not the results should be highlighted
	 * @param syntaxCheck	Whether or not the syntax should be checked
	 * @return				Result of the interpretation and calculation
	 * @throws CalculateFormulaException	Error during the calculation (e.g. invalid variabl name)
	 */
	public static double interpretString(String formula, MetricsCalculator mc,
			HashSet<String> stack, boolean doNotDisplay, boolean syntaxCheck)
			throws CalculateFormulaException {
		try {
			CharStream stream = new ANTLRStringStream(formula);
			metricsGrammarLexer lexer = new metricsGrammarLexer(stream);
			TokenStream tokenStream = new CommonTokenStream(lexer);
			metricsGrammarParser parser = new metricsGrammarParser(tokenStream,
					mc, stack, doNotDisplay, syntaxCheck);
			return parser.evaluator();
		} catch (RecognitionException re) {
			//This Exception will only be thrown if there is a general Error like a letter which is not allowed
			return caughtRecognitionException(re, formula, mc, stack,
					doNotDisplay, syntaxCheck);
		} catch (FormulaVariableNotFoundException fvnfe) {
			//This Exception will be thrown if the Variable does not exist. If a math-function is false spelled,
			// this error will also be thrown!!!
			return caughtFormulaVariableNotFoundException(fvnfe, formula, mc,
					stack, doNotDisplay, syntaxCheck);
		}catch (NestedCalculateFormulaException ncfe){
			return caughtNestedCalculateFormulaException(ncfe, formula, mc,
					stack, doNotDisplay, syntaxCheck);
		}

	}

	/**
	 * Converts Math functions to lower case
	 * 
	 * @param formula	Formula to be converted
	 * @return			Conversion result
	 */
	private static String mathToLowerCase(String formula) {
		String newFormula = formula;
		int pos = 0;
		while (true) {
			pos = newFormula.indexOf('(', pos);
			if (pos == -1) {
				return newFormula;
			}

			int checkpos = pos - 1;
			while ((checkpos >= 0) && (newFormula.charAt(checkpos) != ' ')) {
				if ((newFormula.charAt(checkpos) >= 65)
						&& (newFormula.charAt(checkpos) <= 90)) {
					char c = (char) (newFormula.charAt(checkpos) + 32);
					newFormula = newFormula.substring(0, checkpos) + c
							+ newFormula.substring(checkpos + 1);
				}
				checkpos--;
			}
			pos++;
		}

	}

	private static double caughtRecognitionException(RecognitionException e,
			String formula, MetricsCalculator mc, HashSet<String> stack,
			boolean doNotDisplay, boolean syntaxCheck) throws CalculateFormulaException {
		// Try if the Problem could be solved by checking if the usage of
		// the math framework is correct
		formula = mathToLowerCase(formula);
		CharStream stream = new ANTLRStringStream(formula);
		metricsGrammarLexer lexer = new metricsGrammarLexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		metricsGrammarParser parser = new metricsGrammarParser(tokenStream, mc,
				stack, doNotDisplay, syntaxCheck);
		try {
			double value = parser.evaluator();
			// The new Formula worked well. The user should be informed
			// that he should chance the formula. Therefore we throw a EnhancementException. 
			// We have to find the ID of the formula
			throw new EnhancementException(formula, value);
		} catch (RecognitionException e2) {
			// If the error appears again --> Throw an Parsing-Error
			//throw new ParsingException(e, formula);
			throw new AntlrException(e);
		}		
	}

	private static double caughtFormulaVariableNotFoundException(
			FormulaVariableNotFoundException e, String formula,
			MetricsCalculator mc, HashSet<String> stack, boolean doNotDisplay, boolean syntaxCheck)
			throws CalculateFormulaException {
		// If you don't spell a math function correctly,
		// the parser interpret this as a variable
		// Try if the Problem could be solved by checking if the usage of
		// the math framework is correct
		formula = mathToLowerCase(formula);
		CharStream stream = new ANTLRStringStream(formula);
		metricsGrammarLexer lexer = new metricsGrammarLexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		metricsGrammarParser parser = new metricsGrammarParser(tokenStream, mc,
				stack, doNotDisplay, syntaxCheck);
		try {
			double value = parser.evaluator();
			// The new Formula worked well. The user should be informed
			// that he should chance the formula. Therefore we throw a EnhancementException. 
			// We have to find the ID of the formula
			throw new EnhancementException(formula, value);
		} catch (FormulaVariableNotFoundException fvnfe) {
			// If the error appears again --> Throw the error again
			if (formula.contains(fvnfe.getVariable())) {
				int pos = formula.indexOf(fvnfe.getVariable())
						+ fvnfe.getVariable().length();
				//Look at the next Char
				switch (formula.charAt(pos)) {
				case ' ':
					// This is really the end of the variable -->
					// "FormulaVariableNotFoundException" is the right exception
					throw e;
				case '(':
					// A Function is false spelled, the problem is bigger then
					// the case-sensitive problem
					throw new FormulaMathFunctionNotFoundException(
							fvnfe.getVariable());
				default:
					throw new ParsingException(formula, formula.indexOf(fvnfe.getVariable()));
				}
			} else {
				// Exception comes from a deeper level --> Throw it again
				throw e;
			}
		} catch (RecognitionException e2) {
			// Try to solve it
			return caughtRecognitionException(e2, formula, mc, stack,
					doNotDisplay, syntaxCheck);
		}
		
	}
	
	
	/**
	 * @param ncfe
	 * @param formula
	 * @param mc
	 * @param stack
	 * @param doNotDisplay
	 * @param syntaxCheck
	 * @return
	 * @throws CalculateFormulaException
	 */
	private static double caughtNestedCalculateFormulaException(
			NestedCalculateFormulaException ncfe, String formula,
			MetricsCalculator mc, HashSet<String> stack, boolean doNotDisplay, boolean syntaxCheck)
			throws CalculateFormulaException {
		String originalFormula = formula;
		String enhancedFormula = formula;
		
		enhancedFormula = AlgorithmIDTranslateEnhancements(ncfe,
				enhancedFormula, mc, stack, doNotDisplay, syntaxCheck);
		
		enhancedFormula = mathEnhancements(ncfe, enhancedFormula, mc,
				stack, doNotDisplay, syntaxCheck);
		
		enhancedFormula = EOFEnhancements(ncfe,enhancedFormula, mc,
				stack, doNotDisplay, syntaxCheck);
		
		
		if(originalFormula.equals(enhancedFormula)==false){
			ncfe.addError(new EnhancementException(enhancedFormula,0.0));
			throw ncfe;
		}else{
			throw ncfe;
		}
	
	}
	private static String mathEnhancements(
			NestedCalculateFormulaException ncfe, String formula,
			MetricsCalculator mc, HashSet<String> stack, boolean doNotDisplay, boolean syntaxCheck)
			throws CalculateFormulaException {
		String originalFormula = formula;
		ArrayList<FormulaVariableNotFoundException> oldList = ncfe.getFormulaVariableNotFoundExceptions();
		HashMap<String, String> variablelist = new HashMap<String, String>();
		for(FormulaVariableNotFoundException fvnfe: oldList){
			String variable = fvnfe.getVariable();
			int index = formula.indexOf(variable) + variable.length();
			if(getNextRealChar(formula, index)=='('){
				formula = formula.replace(variable, variable.toLowerCase());
				variablelist.put(variable.toLowerCase(), variable);
			}
			
		}		
		CharStream stream = new ANTLRStringStream(formula);
		metricsGrammarLexer lexer = new metricsGrammarLexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		metricsGrammarParser parser = new metricsGrammarParser(tokenStream, mc,
				stack, doNotDisplay, syntaxCheck);
		try {
			parser.evaluator();
		}catch(NestedCalculateFormulaException ncfe2){
			ArrayList<FormulaVariableNotFoundException> newList = ncfe2.getFormulaVariableNotFoundExceptions();
			for(FormulaVariableNotFoundException fvnfe: newList){
				String variable = fvnfe.getVariable();
				if(variablelist.containsKey(variable)){
					variablelist.remove(variable);
				}
				
			}
		}catch(Exception e){
			throw ncfe;
		}
		
		//Check if there are variable where the change was helpfull
		if (variablelist.size() > 0){
			//Rerun the replacements for only have the helpfull variables changed at the formula
			for(String variableNew:variablelist.keySet()){
				String variableOld = variablelist.get(variableNew);
				originalFormula = originalFormula.replace(variableOld, variableNew);
			}			
		}
		
		return originalFormula;
	}
	private static char getNextRealChar(String formula, int pos){
		char c;
		try {
			do {
				c = formula.charAt(pos);
				pos++;
			} while (Character.isWhitespace(c));
		} catch (StringIndexOutOfBoundsException e) {
			c = 0;
		}
		return c;
	}

	private static String EOFEnhancements(NestedCalculateFormulaException ncfeOriginal,String formula, MetricsCalculator mc,
			HashSet<String> stack, boolean doNotDisplay, boolean syntaxCheck)
			throws CalculateFormulaException {
		String originalFormula = formula;
		int maxTries = 10;
		// Get actual Errors
		CharStream stream = new ANTLRStringStream(formula);
		metricsGrammarLexer lexer = new metricsGrammarLexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		metricsGrammarParser parser = new metricsGrammarParser(tokenStream, mc,
				stack, doNotDisplay, syntaxCheck);
		try {
			parser.evaluator();
		} catch (NestedCalculateFormulaException ncfe) {

			ArrayList<MismatchedTokenException> oldList = ncfe
					.getMismatchedTokenExceptions();
			ArrayList<String> missingChar = new ArrayList<String>();
			int numberOfItemsStart;
			int numberOfItemsEnd;

			do {
				maxTries--;
				numberOfItemsStart = missingChar.size();

				for (MismatchedTokenException mmte : oldList) {
					if (mmte.c == Token.EOF) {
						try {
							if (AntlrException.getTokenList()[mmte.expecting]
									.equals("')'")) {
								formula = formula + ")";
								missingChar.add(")");
							}
						} catch (ArrayIndexOutOfBoundsException aioob) {

						}
					}
				}

				CharStream stream2 = new ANTLRStringStream(formula);
				metricsGrammarLexer lexer2 = new metricsGrammarLexer(stream2);
				TokenStream tokenStream2 = new CommonTokenStream(lexer2);
				metricsGrammarParser parser2 = new metricsGrammarParser(
						tokenStream2, mc, stack, doNotDisplay, syntaxCheck);
				try {
					parser2.evaluator();
					oldList.clear();
				} catch (NestedCalculateFormulaException ncfe2) {
					oldList = ncfe2	.getMismatchedTokenExceptions();
				} catch (Exception e) {
					throw ncfeOriginal;
				}

				numberOfItemsEnd = missingChar.size();

			} while ((numberOfItemsStart < numberOfItemsEnd)&&(maxTries>=0));

			// Check if there are variable where the change was helpfull
			if (missingChar.size() > 0) {
				// Rerun the replacements for only have the helpfull tokens
				// changed at the formula
				// Sort by charPosition
				for (String s : missingChar) {
					originalFormula = originalFormula + s;
				}

			}
		}catch(Exception e){
			throw ncfeOriginal;
		}

		return originalFormula;

	}
	
	private static String AlgorithmIDTranslateEnhancements(NestedCalculateFormulaException ncfeOriginal,String formula, MetricsCalculator mc,
			HashSet<String> stack, boolean doNotDisplay, boolean syntaxCheck)
			throws CalculateFormulaException {
				
		for(AlgorithmIDTranslateException e: ncfeOriginal.getAlgorithmIDTranslateExceptions()){
			formula = formula.replace(e.getDescString(), e.getIdString());			
		}
		
		return formula;

	}

}