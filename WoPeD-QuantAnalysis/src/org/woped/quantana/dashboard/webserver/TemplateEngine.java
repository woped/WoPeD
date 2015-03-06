package org.woped.quantana.dashboard.webserver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.woped.gui.translations.Messages;

public class TemplateEngine {
	
	String patternString = "%%.*%%";
	
	public String getTemplateContent(String strFile){
		 String zusammen = "";
		 StringBuffer buffer = new StringBuffer();
	      try{
			  
	    	  FileReader in = new FileReader( "../WoPeD-QuantAnalysis/pages/"+strFile);
			   for (int n;(n = in.read()) != -1;buffer.append((char) n));
			   in.close();
			
			    zusammen = buffer.toString(); 
	      }
	      catch(FileNotFoundException e){
	      }
	      catch(IOException e){
	      }
	      return getI18nText(zusammen);    
	}
	
	public byte[] getImageContent(String strFile){
		
		
		   
		
		  
		  
		    Path path = Paths.get("../WoPeD-GUI/src/org/woped/gui/images/"+ strFile);
		    byte[] _bytes = null;
		    try {
		    	_bytes = Files.readAllBytes(path);
		    
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    //String file_string = "";
/*
		    for(int i = 0; i < _bytes.length; i++)
		    {
		        file_string += (char)_bytes[i];
		    }
		    return new String(_bytes)
*/
		    return _bytes;  
		    
		   
		
	}	
	
	public byte[] getImageSVGContent(String strFile){
		
		
		   
		
		  
		  
	    Path path = Paths.get("../WoPeD-GUI/src/org/woped/gui/images/svg/"+ strFile);
	    byte[] _bytes = null;
	    try {
	    	_bytes = Files.readAllBytes(path);
	    
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //String file_string = "";
/*
	    for(int i = 0; i < _bytes.length; i++)
	    {
	        file_string += (char)_bytes[i];
	    }
	    return new String(_bytes)
*/
	    return _bytes;  
	    
	   
	
}	
	private String getI18nText(String strText){
		 
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(strText);
		
		while(matcher.find()) {
			
			String strFound = matcher.group();
			String strToFindInMessagesTable = strFound.replace("%%", "");
			if(Messages.exists(strToFindInMessagesTable)){
				String strMessagesEntry = Messages.getString(strToFindInMessagesTable);
		    	strText = strText.replaceAll(strFound, strMessagesEntry);
		    }
		    	    
		}

		return strText;
	}
	
}
