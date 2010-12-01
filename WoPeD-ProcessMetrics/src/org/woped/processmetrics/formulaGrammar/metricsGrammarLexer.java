

  package org.woped.processmetrics.formulaGrammar;


import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

public class metricsGrammarLexer extends Lexer {
    public static final int T__23=23;
    public static final int LETTER=9;
    public static final int T__22=22;
    public static final int T__21=21;
    public static final int T__20=20;
    public static final int EOF=-1;
    public static final int T__19=19;
    public static final int MULTILINE_COMMENT=6;
    public static final int WS=11;
    public static final int T__16=16;
    public static final int STRING_LITERAL=7;
    public static final int T__15=15;
    public static final int T__18=18;
    public static final int T__17=17;
    public static final int CHAR_LITERAL=8;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int DOUBLE=5;
    public static final int IDENT=4;
    public static final int DIGIT=10;
    public static final int COMMENT=12;

    // delegates
    // delegators

    public metricsGrammarLexer() {;} 
    public metricsGrammarLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public metricsGrammarLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g"; }

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:11:7: ( '(' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:11:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:12:7: ( ')' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:12:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:13:7: ( 'log2(' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:13:9: 'log2('
            {
            match("log2("); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:14:7: ( 'pow(' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:14:9: 'pow('
            {
            match("pow("); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:15:7: ( ',' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:15:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:16:7: ( 'sqrt(' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:16:9: 'sqrt('
            {
            match("sqrt("); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:17:7: ( '+' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:17:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:18:7: ( '-' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:18:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:19:7: ( '*' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:19:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:20:7: ( '/' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:20:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:21:7: ( 'mod' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:21:9: 'mod'
            {
            match("mod"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "MULTILINE_COMMENT"
    public final void mMULTILINE_COMMENT() throws RecognitionException {
        try {
            int _type = MULTILINE_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:63:19: ( '/*' ( . )* '*/' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:63:21: '/*' ( . )* '*/'
            {
            match("/*"); 

            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:63:26: ( . )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='*') ) {
                    int LA1_1 = input.LA(2);

                    if ( (LA1_1=='/') ) {
                        alt1=2;
                    }
                    else if ( ((LA1_1>='\u0000' && LA1_1<='.')||(LA1_1>='0' && LA1_1<='\uFFFF')) ) {
                        alt1=1;
                    }


                }
                else if ( ((LA1_0>='\u0000' && LA1_0<=')')||(LA1_0>='+' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:63:26: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match("*/"); 

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MULTILINE_COMMENT"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int c;

            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:66:2: ( '\"' ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )* '\"' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:66:4: '\"' ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )* '\"'
            {
            match('\"'); 
             StringBuilder b = new StringBuilder(); 
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:68:3: ( '\"' '\"' | c=~ ( '\"' | '\\r' | '\\n' ) )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='\"') ) {
                    int LA2_1 = input.LA(2);

                    if ( (LA2_1=='\"') ) {
                        alt2=1;
                    }


                }
                else if ( ((LA2_0>='\u0000' && LA2_0<='\t')||(LA2_0>='\u000B' && LA2_0<='\f')||(LA2_0>='\u000E' && LA2_0<='!')||(LA2_0>='#' && LA2_0<='\uFFFF')) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:68:5: '\"' '\"'
            	    {
            	    match('\"'); 
            	    match('\"'); 
            	     b.appendCodePoint('"');

            	    }
            	    break;
            	case 2 :
            	    // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:69:5: c=~ ( '\"' | '\\r' | '\\n' )
            	    {
            	    c= input.LA(1);
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}

            	     b.appendCodePoint(c);

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match('\"'); 
             setText(b.toString()); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING_LITERAL"

    // $ANTLR start "CHAR_LITERAL"
    public final void mCHAR_LITERAL() throws RecognitionException {
        try {
            int _type = CHAR_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:76:2: ( '\\'' . '\\'' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:76:4: '\\'' . '\\''
            {
            match('\''); 
            matchAny(); 
            match('\''); 
            setText(getText().substring(1,2));

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CHAR_LITERAL"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:79:17: ( ( 'a' .. 'z' | 'A' .. 'Z' ) )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:79:19: ( 'a' .. 'z' | 'A' .. 'Z' )
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:80:16: ( '0' .. '9' )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:80:18: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "DOUBLE"
    public final void mDOUBLE() throws RecognitionException {
        try {
            int _type = DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:81:8: ( ( '-' )? ( DIGIT )+ ( '.' ( DIGIT )+ )? )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:81:10: ( '-' )? ( DIGIT )+ ( '.' ( DIGIT )+ )?
            {
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:81:10: ( '-' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='-') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:81:11: '-'
                    {
                    match('-'); 

                    }
                    break;

            }

            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:81:17: ( DIGIT )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:81:18: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);

            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:81:26: ( '.' ( DIGIT )+ )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='.') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:81:27: '.' ( DIGIT )+
                    {
                    match('.'); 
                    // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:81:31: ( DIGIT )+
                    int cnt5=0;
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:81:32: DIGIT
                    	    {
                    	    mDIGIT(); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt5 >= 1 ) break loop5;
                                EarlyExitException eee =
                                    new EarlyExitException(5, input);
                                throw eee;
                        }
                        cnt5++;
                    } while (true);


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLE"

    // $ANTLR start "IDENT"
    public final void mIDENT() throws RecognitionException {
        try {
            int _type = IDENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:82:7: ( LETTER ( LETTER | DIGIT )* )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:82:9: LETTER ( LETTER | DIGIT )*
            {
            mLETTER(); 
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:82:16: ( LETTER | DIGIT )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')||(LA7_0>='A' && LA7_0<='Z')||(LA7_0>='a' && LA7_0<='z')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDENT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:83:4: ( ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:83:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
            {
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:83:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='\t' && LA8_0<='\n')||(LA8_0>='\f' && LA8_0<='\r')||LA8_0==' ') ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:
            	    {
            	    if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:84:9: ( '//' ( . )* ( '\\n' | '\\r' ) )
            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:84:11: '//' ( . )* ( '\\n' | '\\r' )
            {
            match("//"); 

            // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:84:16: ( . )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0=='\n'||LA9_0=='\r') ) {
                    alt9=2;
                }
                else if ( ((LA9_0>='\u0000' && LA9_0<='\t')||(LA9_0>='\u000B' && LA9_0<='\f')||(LA9_0>='\u000E' && LA9_0<='\uFFFF')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:84:16: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);

            if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    public void mTokens() throws RecognitionException {
        // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:8: ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | MULTILINE_COMMENT | STRING_LITERAL | CHAR_LITERAL | DOUBLE | IDENT | WS | COMMENT )
        int alt10=18;
        alt10 = dfa10.predict(input);
        switch (alt10) {
            case 1 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:10: T__13
                {
                mT__13(); 

                }
                break;
            case 2 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:16: T__14
                {
                mT__14(); 

                }
                break;
            case 3 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:22: T__15
                {
                mT__15(); 

                }
                break;
            case 4 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:28: T__16
                {
                mT__16(); 

                }
                break;
            case 5 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:34: T__17
                {
                mT__17(); 

                }
                break;
            case 6 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:40: T__18
                {
                mT__18(); 

                }
                break;
            case 7 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:46: T__19
                {
                mT__19(); 

                }
                break;
            case 8 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:52: T__20
                {
                mT__20(); 

                }
                break;
            case 9 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:58: T__21
                {
                mT__21(); 

                }
                break;
            case 10 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:64: T__22
                {
                mT__22(); 

                }
                break;
            case 11 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:70: T__23
                {
                mT__23(); 

                }
                break;
            case 12 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:76: MULTILINE_COMMENT
                {
                mMULTILINE_COMMENT(); 

                }
                break;
            case 13 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:94: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 14 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:109: CHAR_LITERAL
                {
                mCHAR_LITERAL(); 

                }
                break;
            case 15 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:122: DOUBLE
                {
                mDOUBLE(); 

                }
                break;
            case 16 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:129: IDENT
                {
                mIDENT(); 

                }
                break;
            case 17 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:135: WS
                {
                mWS(); 

                }
                break;
            case 18 :
                // /home/tobias/workspace/Woped/src/antlrGrammar/metricsGrammar.g:1:138: COMMENT
                {
                mCOMMENT(); 

                }
                break;

        }

    }


    protected DFA10 dfa10 = new DFA10(this);
    static final String DFA10_eotS =
        "\3\uffff\2\17\1\uffff\1\17\1\uffff\1\24\1\uffff\1\27\1\17\5\uffff"+
        "\3\17\4\uffff\4\17\1\40\1\17\1\uffff\1\17\3\uffff";
    static final String DFA10_eofS =
        "\43\uffff";
    static final String DFA10_minS =
        "\1\11\2\uffff\2\157\1\uffff\1\161\1\uffff\1\60\1\uffff\1\52\1\157"+
        "\5\uffff\1\147\1\167\1\162\4\uffff\1\144\1\62\1\50\1\164\1\60\1"+
        "\50\1\uffff\1\50\3\uffff";
    static final String DFA10_maxS =
        "\1\172\2\uffff\2\157\1\uffff\1\161\1\uffff\1\71\1\uffff\1\57\1\157"+
        "\5\uffff\1\147\1\167\1\162\4\uffff\1\144\1\62\1\50\1\164\1\172\1"+
        "\50\1\uffff\1\50\3\uffff";
    static final String DFA10_acceptS =
        "\1\uffff\1\1\1\2\2\uffff\1\5\1\uffff\1\7\1\uffff\1\11\2\uffff\1"+
        "\15\1\16\1\17\1\20\1\21\3\uffff\1\10\1\14\1\22\1\12\6\uffff\1\4"+
        "\1\uffff\1\13\1\3\1\6";
    static final String DFA10_specialS =
        "\43\uffff}>";
    static final String[] DFA10_transitionS = {
            "\2\20\1\uffff\2\20\22\uffff\1\20\1\uffff\1\14\4\uffff\1\15\1"+
            "\1\1\2\1\11\1\7\1\5\1\10\1\uffff\1\12\12\16\7\uffff\32\17\6"+
            "\uffff\13\17\1\3\1\13\2\17\1\4\2\17\1\6\7\17",
            "",
            "",
            "\1\21",
            "\1\22",
            "",
            "\1\23",
            "",
            "\12\16",
            "",
            "\1\25\4\uffff\1\26",
            "\1\30",
            "",
            "",
            "",
            "",
            "",
            "\1\31",
            "\1\32",
            "\1\33",
            "",
            "",
            "",
            "",
            "\1\34",
            "\1\35",
            "\1\36",
            "\1\37",
            "\12\17\7\uffff\32\17\6\uffff\32\17",
            "\1\41",
            "",
            "\1\42",
            "",
            "",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | MULTILINE_COMMENT | STRING_LITERAL | CHAR_LITERAL | DOUBLE | IDENT | WS | COMMENT );";
        }
    }
 

}