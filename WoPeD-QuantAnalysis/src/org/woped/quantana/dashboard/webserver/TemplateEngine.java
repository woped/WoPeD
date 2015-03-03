package org.woped.quantana.dashboard.webserver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TemplateEngine {
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
	      return zusammen;    
	}
}
