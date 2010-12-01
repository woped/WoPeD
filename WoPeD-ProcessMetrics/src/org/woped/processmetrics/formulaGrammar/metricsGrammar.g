grammar metricsGrammar;

options {
  language = Java;
}
@header {
  package org.woped.processmetrics.formulaGrammar;
}

@lexer::header {
  package org.woped.processmetrics.formulaGrammar;
}
evaluator returns [double result]
	:	expression EOF { $result = $expression.result; }
	;
	
// expressions -- fun time!

term returns [double result]
	:	IDENT {$result = 0;}
	|	'(' expression ')' {$result = $expression.result;}
	|	DOUBLE {$result = Double.parseDouble($DOUBLE.text);}
	| sqrt {$result = $sqrt.result;}
	| pow {$result = $pow.result;} 
	| log2 {$result = $log2.result;}
	;

log2 returns [double result]
	: 'log2(' expression ')' {$result = Math.log($expression.result) / Math.log(2);};

pow returns [double result]
	:	'pow(' expression ',' DOUBLE  ')'	{$result = Math.pow($expression.result,Double.parseDouble($DOUBLE.text));};
	
sqrt returns [double result]
	: 'sqrt(' expression ')' {$result = Math.sqrt($expression.result);};
	
unary returns [double result]
	:	{ boolean positive = true; }
		('+' | '-' { positive = !positive; })* term
		{
			$result = $term.result;
			if (!positive)
				$result = -$result;
		}
	;

mult returns [double result]
	:	op1=unary { $result = $op1.result; }
		(	'*' op2=unary { $result = $result * $op2.result; }
		|	'/' op2=unary { $result = $result / $op2.result; }
		|	'mod' op2=unary { $result = $result \% $op2.result; }
		)*
	;
	
expression returns [double result]
	:	op1=mult { $result = $op1.result; }
		(	'+' op2=mult { $result = $result + $op2.result; }
		|	'-' op2=mult { $result = $result - $op2.result; }
		)*
	;


MULTILINE_COMMENT : '/*' .* '*/' {$channel = HIDDEN;} ;

STRING_LITERAL
	:	'"'
		{ StringBuilder b = new StringBuilder(); }
		(	'"' '"'				{ b.appendCodePoint('"');}
		|	c=~('"'|'\r'|'\n')	{ b.appendCodePoint(c);}
		)*
		'"'
		{ setText(b.toString()); }
	;
	
CHAR_LITERAL
	:	'\'' . '\'' {setText(getText().substring(1,2));}
	;

fragment LETTER : ('a'..'z' | 'A'..'Z') ;
fragment DIGIT : '0'..'9';
DOUBLE : ('-')? (DIGIT)+ ('.' (DIGIT)+)?;
IDENT : LETTER (LETTER | DIGIT)*;
WS : (' ' | '\t' | '\n' | '\r' | '\f')+ {$channel = HIDDEN;};
COMMENT : '//' .* ('\n'|'\r') {$channel = HIDDEN;};