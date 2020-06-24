package org.woped.quantana.dashboard.webserver;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import org.woped.core.utilities.LoggerManager;
import org.woped.gui.translations.Messages;
import org.woped.quantana.dashboard.storage.Constants;



public class TemplateEngine {
	
	
	
	
	static String strRessourcePath = "";
	
	public String getTemplateContent(String strFile){

		 
		 String strContent = (String)getSourceLocation(strFile);
		 
		 //return getI18nText(strContent);    
		 if(getExtension(strFile).equals("html")){
			 return getI18nText(strContent);
		 }
		 else{
			 return (strContent);
		 }
	}
	/*
	private String getTextFileContent(String strFileName){
		
		return getI18nText((String)this.getSourceLocation(strFileName));	
	}
	*/
	public byte[] getImageContent(String strFileName){
		
		return (byte[])getSourceLocation(strFileName);	
	}	
	
	
	public byte[] getImageSVGContent(String strFileName){
			 
		byte[] b = new byte[0];
		
		Object o = getSourceLocation(strFileName);
		if( o != null && !o.equals("")){
			return (byte[])o;
		}else  return b; //(byte[])o;  
	
	}	
	
	private String getI18nText(String strText){
		 
		
		/*
		 * 
		 * Replace Message-Placeholders
		 * 
		 */
		String patternString = "%%.*%%";
		
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
		
		strText = strText.replaceAll("Ö", "&Ouml;")
				.replaceAll("ö", "&ouml;")
				.replaceAll("Ü", "&Üuml;")
				.replaceAll("ü", "&uuml;")
				.replaceAll("Ä", "&Auml;")
				.replaceAll("ä", "&auml;");
		
		
		
		return strText;
	}
	


	private enum FileType {UNKNOWN, TEXT, BINARY};
	
	private String getExtension(String strFileName){
		
		String ext="";
		
		int i;
		
		i = strFileName.lastIndexOf('.');
		
		if ( i > 0){
			ext = strFileName.substring(i+1).toLowerCase();
		}
		return ext;
	}
	
	private Object getSourceLocation(String filename){
	
		// declare path of the desired file
		
		String extension = "";
		
		FileType filetype = FileType.UNKNOWN;
		
		String strFilePathFS = "";
		
		String strFilePathJAR = "";
		
		String strContent = "";
			
		int i = filename.lastIndexOf('/');
		if ( i > 0){
			return "";
			
		}
		
		filename = filename.replace("/","");
		
		extension = getExtension(filename);
		
		if 		(extension.contains("htm") || 
				extension.contains("js") || 
				extension.contains("css") ){
			
			filetype = FileType.TEXT;
			
			strFilePathJAR = "";
			
			strFilePathFS = "../WoPeD-QuantAnalysis/pages/";
			
		}else if (extension.contains("png") || extension.contains("gif")) {
		
			filetype = FileType.BINARY;
			
			strFilePathJAR = "org/woped/gui/images/";
			
			strFilePathFS = "../WoPeD-GUI/src/org/woped/gui/images/";
			
		}else if (extension.contains("svg")){

			filetype = FileType.BINARY;
			
			strFilePathJAR = "org/woped/gui/images/svg/";
			
			strFilePathFS = "../WoPeD-GUI/src/org/woped/gui/images/svg/";
		}
		else{
			
			LoggerManager.info(Constants.DASHBOARDSTORE_LOGGER, "unknown file type for extension " + extension + " of file " + filename );
			
		}
		
				
		if (strRessourcePath.equals("") ){ 
	
			strRessourcePath = this.getClass().getResource("TemplateEngine.class").toExternalForm();
		}
		
		

		if (strRessourcePath.length()> 0) {	
			
	
			///
			// JAR FILE ACCESS
			///
			if (strRessourcePath.indexOf("jar:file:") != -1) {
					
				String fn = strRessourcePath.replaceAll("jar:file:", "");
				
				// find jar start
				int n = fn.indexOf("!");
				
				if (n == -1)
					n = fn.length();
				
				// read Jar File Name
				fn = fn.substring(0, n);
				
				// Replace all whitespaces in filename
				fn = fn.replaceAll("%20", " ");
			
				String strFileName= fn + strFilePathJAR + filename;
				
				if(filetype == FileType.TEXT ){
					
					
					try {

						JarFile jf = new JarFile(fn);
										
						ZipEntry ze = jf.getEntry(strFilePathJAR + filename);
						
						InputStream in;
						
						in = jf.getInputStream(ze);
					
						InputStreamReader isr =  new InputStreamReader(in);
						
						BufferedReader reader = new BufferedReader(isr);
							
						StringBuilder out = new StringBuilder();

						String line ;

						while ((line = reader.readLine())!= null){
							
							out.append(line);
							out.append("\n");
						}
					
						reader.close();
						
						jf.close();
						
						return out.toString();
					
					} catch (FileNotFoundException e1) {
						LoggerManager.warn(Constants.DASHBOARDSTORE_LOGGER, "file not found: " + strFileName );
						
					} catch (IOException e) {
						LoggerManager.error(Constants.DASHBOARDSTORE_LOGGER, "problems during reading find (txt): " + strFileName );
						
					}
					
				}
				else if (filetype == FileType.BINARY){
					
					InputStream in;
					
					try {
						JarFile jf = new JarFile(fn);
						
						ZipEntry ze = jf.getEntry(strFilePathJAR + filename);
					
						in = jf.getInputStream(ze);
						
						ByteArrayOutputStream buffer = new ByteArrayOutputStream();
						
						int nRead;
						
						byte[] data = new byte[16384];
						
						while((nRead = in.read(data,0,data.length))!= -1){
							buffer.write(data, 0, nRead);
						}
						buffer.flush();
						
						jf.close();
						
						return buffer.toByteArray();
						
					} catch (FileNotFoundException e1) {
						LoggerManager.warn(Constants.DASHBOARDSTORE_LOGGER, "file not found (bin): " + strFileName );
						
					} catch (IOException e) {
						LoggerManager.warn(Constants.DASHBOARDSTORE_LOGGER, "problems during reading find (bin): " + strFileName );
					}
					
					
				}
			}
			///
			//  DIRECTORY-ACCESS
			///
			else {
				
				filename  = strFilePathFS + filename;
				
				if (true == new File(filename).exists()){
					 
					if( filetype == FileType.TEXT ){
						FileReader fr;
						
						try {
							fr = new FileReader(filename);
										
							StringBuffer buffer = new StringBuffer();
			   			 
				   		  	for (int n;(n = fr.read()) != -1;buffer.append((char) n));

							fr.close();
							
					   		strContent = buffer.toString(); 
		
						} catch (FileNotFoundException e) {
							LoggerManager.warn(Constants.DASHBOARDSTORE_LOGGER, "cannot find file specified: " + filename );
						} catch (IOException e) {
							LoggerManager.warn(Constants.DASHBOARDSTORE_LOGGER, "IOException with file : " + filename );
						}
				   		
	
			   		    return strContent;
					}
					else if( filetype == FileType.BINARY ){
						
						byte[] _bytes = null;
						
						Path mypath = Paths.get(filename);
						
						try {
							_bytes = Files.readAllBytes(mypath);
						} catch (IOException e) {
							
							e.printStackTrace();
						}
						
						return _bytes;
						
					}
				}
				else{
					LoggerManager.warn(Constants.DASHBOARDSTORE_LOGGER, "file does not exist: " + filename );
				}
			}			
		}
		return "";
	}
}
