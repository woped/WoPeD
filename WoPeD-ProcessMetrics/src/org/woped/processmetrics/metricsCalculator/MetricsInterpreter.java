package org.woped.processmetrics.metricsCalculator;


import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.woped.processmetrics.formulaGrammar.metricsGrammarLexer;
import org.woped.processmetrics.formulaGrammar.metricsGrammarParser;



public class MetricsInterpreter {
	
	public static double interpretString(String formula){
		CharStream stream =
			new ANTLRStringStream(formula);
		metricsGrammarLexer lexer = new metricsGrammarLexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		metricsGrammarParser parser = new metricsGrammarParser(tokenStream);
		try {
			return parser.evaluator();
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
}


