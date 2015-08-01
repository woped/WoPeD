package org.woped.quantana.dashboard.webserver;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import org.woped.gui.translations.Messages;

public class TemplateEngine {
	
	String patternString = "%%.*%%";
	
	public String getTemplateContent(String strFile){
		 String zusammen = "";
		 String strApp = "";
		 FileReader fr = null;
		 
		 
		 //extract file name only
		 if(true == strFile.contains("/"))
			 strFile = strFile.split("/")[1];
		 
		
	 
			 try {
				strApp = Paths.get(".").toRealPath().toAbsolutePath().normalize().toString();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			 
			 strApp = strApp.replace("\\", "/");
			 			 
			 // eclipse project
			 String strFileName = "";  
			 
			 if(zusammen.length() == 0){
				 
				 strFileName = strApp + "/../WoPeD-QuantAnalysis/pages/"+ strFile;
		    	 
				 zusammen = getTextFileContent(strFileName);
			 }
			 
			 if(zusammen.length() == 0){
				 	 
				 strFileName = strApp + "/Contents/Java/dashboardpages/" + strFile;
			 
				 zusammen = getTextFileContent(strFileName);
			 }
			 
			
	      return getI18nText(zusammen);    
	}
	
	private String getTextFileContent(String strFileName){
		StringBuffer buffer = new StringBuffer();
		String zusammen = "";
		FileReader fr;
		
		if (true == new File(strFileName).exists()){
			 
			try {
				fr = new FileReader(strFileName);
			
   			 
	   		  	for (int n;(n = fr.read()) != -1;buffer.append((char) n));
		   		  	fr.close();
		   		
	   		    zusammen = buffer.toString(); 
		   		    
	   		    return zusammen;
	    	
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return zusammen;
	}
	
	public byte[] getImageContent(String strFile){
		    
		    byte[] _bytes = null;
		    
		    try {
		    	Path path = Paths.get("../WoPeD-GUI/src/org/woped/gui/images/"+ strFile);
		    	_bytes = Files.readAllBytes(path);
		    
		    } catch (IOException e) {
		    	
		    	try {
		    		String strApp;
		    		
					strApp = new File(".").getCanonicalPath();
			    	
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
	    		
	    		String strApp;
	    		
				strApp = new File(".").getCanonicalPath();
	    		
				Path path = Paths.get(strApp + "/Contents/Java/dashboardpages/GUI/"+strFile);
				
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
