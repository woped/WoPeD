package org.woped.qualanalysis.p2t;

import java.util.LinkedHashMap;
import java.util.Vector;

public class Process2Text {
	private LinkedHashMap<String, String> textelements = null;
	private String xmlText = null;
	private String htmlText = null;
			
	private static final String phraseDelimiter = "</phrase>";
	private static final String startTag = "<text>";
	private static final String stopTag = "</text>";
	private static final String endOfNetTag = "</net>";
	
	public Process2Text(String xml){
		super();
		this.xmlText = xml;
		parseXmlToHtml();
	}
	
	/**
	 * Returns all text passages the ID is linking to.
	 * @param id
	 * @return
	 */
	public String [] getText (String id){
		Vector <String> temptexts = new Vector <String> (0);
		//search every pair .get(Object) won't work for there can be multiple IDs within a key
		for (String ids : textelements.keySet().toArray(new String [textelements.keySet().size()])){
			for (String tempID : ids.split(",")){
				if (tempID.equals(id)){
					temptexts.addElement(textelements.get(ids));
					//if there are multiple IDs one match is sufficient and a break will prevent a single text to be added multiple times  
					break;
				}
			}
		}
		
		return temptexts.toArray(new String [temptexts.size()]);
	}
	
	private void parseXmlToHtml(){
		int start = -1;
		int stop = -1;
		int endOfNet = -1;
		String temp = null;
		
//		endOfNet = xmlText.indexOf(endOfNetTag) + endOfNetTag.length();
//		temp = xmlText.substring(endOfNet);

		temp = xmlText;
		
		start = temp.indexOf(startTag) + startTag.length(); // beginning of the content
		stop = temp.indexOf(stopTag);	// end of the content
		
		// if both tags can be found / there is some content continue
		if (start >= startTag.length() && stop > start){
			temp = temp.substring(start, stop); // the content
			temp = temp.replace("\n", "");
			temp = temp.replace("\t", "");
			textelements = createMap(temp);	// creates the map-structure
			createHTML(); // creates the text to be shown in HTML-format
		}
	}
	
	/**
	 * Creates a HTML-formated text with hyperlinks based on the previous created structure.
	 */
	private void createHTML(){
		StringBuilder sb = new StringBuilder(0);
		
		sb.append("<html><head></head><body style=\"font-family:verdana;font-size: 11pt\"><p>");
		
		for (String key : textelements.keySet()){
			// Black not underlined hyperlinks
			sb.append("<a href=\"" + key + "\" style=\"text-decoration:none;color:#000000\">" + textelements.get(key) + "</a><br> ");
		}

		sb.append("</p></body></html>");
		
		htmlText = sb.toString();
	}
	
	private LinkedHashMap<String, String> createMap(String textcontent){
		String[] temp = null;
		LinkedHashMap<String, String> tempMap = new LinkedHashMap<String, String>();
		String tempID = null;
		String tempText = null;
		
		temp = textcontent.split(phraseDelimiter);
		
		for (String phraseline : temp){	
			tempID = getIDOfPhrase(phraseline);
			tempText = getTextOfPhrase(phraseline);		
			tempMap.put(tempID, tempText);	
		}
		return tempMap;
	}
	
	/**
	 * Returns the ID(s) of the phrase
	 * @param phraseline
	 * @return
	 */
	private String getIDOfPhrase(String phraseline){
		String id = null;
		String idstart = "ids=\"";
		
		String declaration = phraseline.substring(0, phraseline.indexOf(">")); // the content between the beginning of the line and the first ">"
		int start = declaration.indexOf(idstart) + idstart.length(); // the position right after "id=""/start of the declaration
		id = declaration.substring(start, declaration.indexOf("\"", start) );// end of the declaration
		
		return id;
	}
	
	/**
	 * Returns the text of the phrase
	 * @param phraseline
	 * @return
	 */
	private String getTextOfPhrase(String phraseline){
		String text = null;
		int start = 0;
		
		start = phraseline.indexOf('>') + 1; // Position after the first ">"/ the beginning of the content
		text = phraseline.substring(start);
		text = text.replace(phraseDelimiter, ""); // removes the delimiter
		text = text.trim();
		
		return text;
	}
	
	public String[] getLinkedTexts(String id){
		String [] singleIDs = null;
		Vector<String> linkedTexts = new Vector<String>(0);
		
		for (String ids : textelements.keySet().toArray(new String [textelements.keySet().size()])){
			singleIDs = ids.split(","); //there can be multiple IDs for a single phrase
			for (String singleID : singleIDs){
				if (singleID.equals(id)){
					linkedTexts.addElement(textelements.get(ids));
					break;
				}
			}
		}
		
		return linkedTexts.toArray(new String[linkedTexts.size()]);
	}
	
	public LinkedHashMap<String, String> getTextelements() {
		return textelements;
	}
	
	public String getXmlText() {
		return xmlText;
	}
	
	public String getHtmlText() {
		return htmlText;
	}	
	
}
