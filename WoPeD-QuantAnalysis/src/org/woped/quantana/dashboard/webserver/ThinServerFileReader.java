package org.woped.quantana.dashboard.webserver;



public interface ThinServerFileReader {
	public byte[] 	getFileContent(String filename);
	public String 	getContentType(String filename);
}
