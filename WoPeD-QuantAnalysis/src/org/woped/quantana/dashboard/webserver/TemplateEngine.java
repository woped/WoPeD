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

import com.apple.eio.FileManager;

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
	    	 
	    	  String strApp = FileManager.getPathToApplicationBundle();
	    	  FileReader fr;
			try {
				fr = new FileReader( strApp + "/Contents/Java/dashboardpages/"+strFile);
				for (int n;(n = fr.read()) != -1;buffer.append((char) n));
				   fr.close();
				
				    zusammen = buffer.toString(); 
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			   
	    	  
	      }
	      catch(IOException e){
	    	  return "";
	      }
	      return getI18nText(zusammen);    
	}
	
	public byte[] getImageContent(String strFile){
		    
		    byte[] _bytes = null;
		    
		    try {
		    	Path path = Paths.get("../WoPeD-GUI/src/org/woped/gui/images/"+ strFile);
		    	_bytes = Files.readAllBytes(path);
		    
		    } catch (IOException e) {
		    	
		    	try {
		    		
			    	String strApp = FileManager.getPathToApplicationBundle();
			    	Path path = Paths.get(strApp + "/Contents/Java/dashboardpages/GUI/"+strFile);
		    		_bytes = Files.readAllBytes(path);
		    		
				} catch (IOException e1) {
					return new byte[0];
				}
  	
			}
		    
		    return _bytes;  
	
	}	
	
	public byte[] getImageSVGContent(String strFile){
			  
	    
	    byte[] _bytes = null;
	    
	    try {
	    	Path path = Paths.get("../WoPeD-GUI/src/org/woped/gui/images/svg/"+ strFile);
	    	_bytes = Files.readAllBytes(path);
	    
	    } catch (IOException e) {
	    	
	    	try {
	    		
		    	String strApp = FileManager.getPathToApplicationBundle();
		    	Path path = Paths.get(strApp + "/Contents/Java/dashboardpages/SVG/"+strFile);
	    		_bytes = Files.readAllBytes(path);
	    		
			} catch (IOException e1) {
				return new byte[0];
			}
	
		}
	    
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
