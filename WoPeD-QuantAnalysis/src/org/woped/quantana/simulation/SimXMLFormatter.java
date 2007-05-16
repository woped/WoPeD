package org.woped.quantana.simulation;

import java.util.logging.Handler;
import java.util.logging.XMLFormatter;

public class SimXMLFormatter extends XMLFormatter {
	
//	private String path;
	
	public SimXMLFormatter(String dtdPath){
		super();
//		path = dtdPath;
	}
	
	public String getHead(Handler h){
		String head = "";
		head += "<?xml version=\"1.0\" encoding=\"windows-1252\" standalone=\"no\"?>\n";
		head += "<!DOCTYPE log [\n"; //SYSTEM \"" + path + "logger.dtd\">\n";
		head += "\t<!ELEMENT log (record*)>\n";
		head += "\t<!ELEMENT record (date, millis, sequence, logger?, level, class?, method?, thread?, message, key?, catalog?, param*, exception?)>\n";
		head += "\t<!ELEMENT date (#PCDATA)>\n";
		head += "\t<!ELEMENT millis (#PCDATA)>\n";
		head += "\t<!ELEMENT sequence (#PCDATA)>\n";
		head += "\t<!ELEMENT logger (#PCDATA)>\n";
		head += "\t<!ELEMENT level (#PCDATA)>\n";
		head += "\t<!ELEMENT class (#PCDATA)>\n";
		head += "\t<!ELEMENT method (#PCDATA)>\n";
		head += "\t<!ELEMENT thread (#PCDATA)>\n";
		head += "\t<!ELEMENT message (#PCDATA)>\n";
		head += "\t<!ELEMENT key (#PCDATA)>\n";
		head += "\t<!ELEMENT catalog (#PCDATA)>\n";
		head += "\t<!ELEMENT param (#PCDATA)>\n";
		head += "\t<!ELEMENT exception (message?, frame+)>\n";
		head += "\t<!ELEMENT frame (class, method, line?)>\n";
		head += "\t<!ELEMENT line (#PCDATA)>\n";
		head += "]>\n";
		
		head += "<log>\n";
		
		return head;
	}
}
