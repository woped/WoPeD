grammar metricsGrammar;

options {
  language = Java;
}
@header {
  package antlrGrammar;
}

@lexer::header {
  package antlrGrammar;
}
evaluator returns [double result]
	:	expression EOF { $result = $expression.result; }
	;


term returns [double result]
	:	IDENT {$result = 0;}
	|	'(' expression ')' {$result = $expression.result;}
	|	DOUBLE {$result = Double.parseDouble($DOUBLE.text);}
	| function {$result = $function.result;}
	;
function returns [double result]
	: javaMathFunctions {$result = $javaMathFunctions.result;} 
	| ownFunctions {$result = $ownFunctions.result;}
	;
ownFunctions returns [double result]
	: doNotDisplay {$result = $doNotDisplay.result;}
	;

doNotDisplay returns [double result]
	: '$' IDENT {$result = 0;}
	;

javaMathFunctions returns [double result]
	: sqrt {$result = $sqrt.result;}
	| pow {$result = $pow.result;} 
	| log2 {$result = $log2.result;}
	| abs {$result = $abs.result; }
	| acos {$result = $acos.result;}
	| asin {$result = $asin.result;}
	| atan {$result = $atan.result;}
	| atan2 {$result = $atan2.result;}
	| ceil {$result = $ceil.result;}
	| cos {$result = $cos.result;}
	| exp {$result = $exp.result;}
	| floor {$result = $floor.result;}
	| ieeeremainder {$result = $ieeeremainder.result;}
	| max {$result = $max.result;}
	| min {$result = $min.result;}
	| random {$result = $random.result;}
	| rint {$result = $rint.result;}
	| sin {$result = $sin.result;}
	| tan {$result = $tan.result;}
	| toDegrees {$result = $toDegrees.result;}
	| toRadians {$result = $toRadians.result;}
	| e {$result = $e.result;}
	| pi {$result = $pi.result;}
	;
	
pi returns [double result]
	: 'PI' {$result = Math.PI;}
	;
	
e returns [double result]
	: 'E' {$result = Math.E;}
	;

toRadians returns [double result]
	: 'toRadians(' expression ')' {$result = Math.toRadians($expression.result);}
	;

toDegrees returns [double result]
	: 'toDegrees(' expression ')' {$result = Math.toDegrees($expression.result);}
	;

tan returns [double result]
	: 'tan(' expression ')' {$result = Math.tan($expression.result);}
	;

sin returns [double result]
	: 'sin(' expression ')' {$result = Math.sin($expression.result);}
	;

rint returns [double result]
	: 'rint(' expression ')' {$result = Math.rint($expression.result);}
	;
	
random returns [double result]
	: 'random()' {$result = Math.random();}
	;
	
min returns [double result]
	: 'min(' e1=expression ',' e2=expression ')' {$result = Math.min($e1.result , $e2.result);}
	;

max returns [double result]
	: 'max(' e1=expression ',' e2=expression ')' {$result = Math.max($e1.result , $e2.result);}
	;

ieeeremainder returns [double result]
	: 'IEEEremainder(' f1=expression ',' f2=expression ')' {$result = Math.IEEEremainder($f1.result,$f2.result);}
	;
	
floor returns [double result]
	: 'floor(' expression ')' {$result = Math.floor($expression.result);}
	;

exp returns [double result]
	: 'exp(' expression ')' {$result = Math.exp($expression.result);}
	;	
	
cos returns [double result]
	: 'cos(' expression ')' {$result = Math.cos($expression.result);}
	;
	
ceil returns [double result]
	: 'ceil(' expression ')' {$result = Math.ceil($expression.result);}
	;


atan2 returns [double result]
	: 'atan2(' e1=expression ',' e2=expression ')' {$result = Math.atan2($e1.result,$e2.result);}
	;


atan returns [double result]
	: 'atan(' expression ')' {$result = Math.atan($expression.result);}
	;	
	
asin returns [double result]
	: 'asin(' expression ')' {$result = Math.asin($expression.result);}
	;

acos returns [double result]
	: 'acos(' expression ')' {$result = Math.acos($expression.result);}
	;

abs returns [double result]
	: 'abs(' expression ')' {$result = Math.abs($expression.result);}
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
IDENT : LETTER (LETTER | DIGIT | '_')*;
WS : (' ' | '\t' | '\n' | '\r' | '\f')+ {$channel = HIDDEN;};
COMMENT : '//' .* ('\n'|'\r') {$channel = HIDDEN;};