package org.woped.quantana.simulation;

import java.util.logging.Handler;
import java.util.logging.XMLFormatter;

public class SimXMLFormatter extends XMLFormatter {
	
	private String path;
	
	public SimXMLFormatter(String dtdPath){
		super();
		path = dtdPath;
	}
	
	public String getHead(Handler h){
		String head = "";
		head += "<?xml version=\"1.0\" encoding=\"windows-1252\" standalone=\"no\"?>\n<!DOCTYPE log SYSTEM \"" + path + "logger.dtd\">\n<log>\n";
		
		return head;
	}
}
