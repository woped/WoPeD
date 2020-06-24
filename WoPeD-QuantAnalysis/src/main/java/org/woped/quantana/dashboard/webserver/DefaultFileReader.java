package org.woped.quantana.dashboard.webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class DefaultFileReader implements ThinServerFileReader {

	@Override
	public byte[] getFileContent(String filename) {
		File file = new File("."+filename);
		if(!file.exists()){
			return new byte[0];
		}else{
			try {
				InputStream fis = new FileInputStream(file);
				byte[] content = new byte[(int) file.length()];
				fis.read(content);
				fis.close();
				return content;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new byte[0];
			
		}
	}

	@Override
	public String getContentType(String filename) {
		return "application/x-unknown";
	}

	

}
