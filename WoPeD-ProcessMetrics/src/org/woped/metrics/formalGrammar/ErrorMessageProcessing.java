package org.woped.metrics.formalGrammar;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedNotSetException;
import org.antlr.runtime.MismatchedRangeException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.woped.core.config.ConfigurationManager;
import org.woped.gui.translations.Messages;

public class ErrorMessageProcessing {
	public static String getErrorMessage(RecognitionException e, String[] tokens, BaseRecognizer processor) {
		
		//Check if AdvancedErrorMessages are Activated
		if(ConfigurationManager.getConfiguration().isShowAdvancedErrorMessages())
		{
			//Show the Default exact Error Messages (not translatable)
			return processor.getErrorMessage( e, tokens);
		}
		//AdvancedErrorMessages are not actived --> show Custom Error-Messages
        if ( e instanceof org.antlr.runtime.MismatchedTokenException ) {
        	MismatchedTokenException mte = (MismatchedTokenException) e;
        	String msg = "";
        	//If Token = EOF -->Special Error Message
        	if(mte.c == Token.EOF){
        		msg = Messages.getString("Metrics.Calculate.Error.Antlr.EOF_Error_Part1");
        		try{
        			msg = msg + " " + tokens[mte.expecting];
        	    }catch(ArrayIndexOutOfBoundsException aioob){
        	    	msg = msg + " " + Messages.getString("Metrics.Calculate.Error.Antlr.unknownChar");
        	    }
        		msg = msg + " " + Messages.getString("Metrics.Calculate.Error.Antlr.EOF_Error_Part2");
        	}else{
	            msg = Messages.getString("Metrics.Calculate.Error.Antlr.mismatchedCharacter")+getCharErrorDisplay(mte.c, tokens,processor); //$NON-NLS-1$
	            try{
	            	msg = msg + " " + Messages.getString("Metrics.Calculate.Error.Antlr.expecting")+ tokens[mte.expecting]; //$NON-NLS-1$
	            }catch(ArrayIndexOutOfBoundsException aioob){
	            	
	            }
        	}
            return msg;
        }
        else if ( e instanceof org.antlr.runtime.NoViableAltException ) {
            return Messages.getString("Metrics.Calculate.Error.Antlr.NoViableAlt")+ getCharErrorDisplay(e.c, tokens,processor); //$NON-NLS-1$
        }
        else if ( e instanceof org.antlr.runtime.EarlyExitException ) {
            return Messages.getString("Metrics.Calculate.Error.Antlr.requiredLoop")+ getCharErrorDisplay(e.c, tokens,processor); //$NON-NLS-1$
        }
        else if ( e instanceof org.antlr.runtime.MismatchedNotSetException ) {
        	MismatchedNotSetException  mnse = (MismatchedNotSetException) e;
            return Messages.getString("Metrics.Calculate.Error.Antlr.mismatchedCharacter")+getCharErrorDisplay(e.c, tokens,processor)+Messages.getString("Metrics.Calculate.Error.Antlr.ExpectingSet")+mnse.expecting; //$NON-NLS-1$ //$NON-NLS-2$
        }
        else if ( e instanceof org.antlr.runtime.MismatchedSetException ) {
        	MismatchedSetException mmse = (MismatchedSetException) e;
            return Messages.getString("Metrics.Calculate.Error.Antlr.mismatchedCharacter")+getCharErrorDisplay(e.c, tokens,processor)+Messages.getString("Metrics.Calculate.Error.Antlr.ExpectingSet")+mmse.expecting; //$NON-NLS-1$ //$NON-NLS-2$
        }
        else if ( e instanceof org.antlr.runtime.MismatchedRangeException ) {
        	MismatchedRangeException mre = (MismatchedRangeException) e;
            return Messages.getString("Metrics.Calculate.Error.Antlr.mismatchedCharacter")+getCharErrorDisplay(e.c, tokens,processor)+Messages.getString("Metrics.Calculate.Error.Antlr.ExpectingSet")+ //$NON-NLS-1$ //$NON-NLS-2$
                getCharErrorDisplay(mre.a, tokens,processor)+".."+getCharErrorDisplay(mre.b, tokens,processor);
        }
        else {
            return processor.getErrorMessage( e, tokens);
        }

    }
	private static String getCharErrorDisplay(int c, String[] tokens, BaseRecognizer processor) {
        String s="";
        if(processor instanceof Lexer){
        	try{
        		char letter = (char) c;
        		s = "" + letter;
        	}catch(Exception e){
        		s=Messages.getString("Metrics.Calculate.Error.Antlr.unknownChar");
        	}
        }else if(processor instanceof Parser){        
	        switch ( c ) {
	            case Token.EOF :
	            	s=Messages.getString("Metrics.Calculate.Error.Antlr.EOF");
	                break;
	            default:
	            	try{
	            	s = tokens[c];
	            	}catch(ArrayIndexOutOfBoundsException e){
	            		s=Messages.getString("Metrics.Calculate.Error.Antlr.unknownChar");
	            	}catch(NullPointerException npe){
	            		s=Messages.getString("Metrics.Calculate.Error.Antlr.unknownChar");
	            	}
	        }	       
        }
        return "'"+s+"'";
    }


	public static String getErrorHeader(RecognitionException e) {
		return Messages.getString("Metrics.Calculate.Error.Antlr.Position") + " " + (e.charPositionInLine + 1) + ":"; //$NON-NLS-1$
	}

}
