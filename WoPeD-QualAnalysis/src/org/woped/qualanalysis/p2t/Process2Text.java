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
	/**
	 * 
	 * @param xml the xml to be used as a base for the parsing
	 */
	public Process2Text(String xml){
		super();
		this.xmlText = xml;
		parseXmlToHtml();
	}
	
	/**
	 * 
	 * @param id	the ID of which the text passages are to be searched
	 * @return		an array of the text-passages the id is linked to
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
	/**
	 * The xml is used to create the html-text with hyperlinks
	 */
	private void parseXmlToHtml(){
		int start = -1;
		int stop = -1;
		int endOfNet = -1;
		String temp = null;
		
		endOfNet = xmlText.indexOf(endOfNetTag) + endOfNetTag.length();
		temp = xmlText.substring(endOfNet);
		
		start = temp.indexOf(startTag) + startTag.length(); //beginning of the content
		stop = temp.indexOf(stopTag);	//end of the content
		
		//if both tags cab be found /there is some contend continue
		if (start >= startTag.length() && stop > start){
			temp = temp.substring(start, stop); //the content
			temp = temp.replace("\n", "");
			temp = temp.replace("\t", "");
			textelements = createMap(temp);	//creates the map-structure
			createHTML(); // creates the text to be shown in html-format
		}
	}
	
	/**
	 * creates a html-formated text with hyperlinks based on the previous created structure
	 */
	private void createHTML(){
		StringBuilder sb = new StringBuilder(0);
		
		sb.append("<html><head></head><body><p>");
		
		for (String key : textelements.keySet()){
			//black not underlined hyperlinks
			sb.append("<a href=\"" + key + "\" style=\"text-decoration:none;color:#000000\">" + textelements.get(key) + "</a><br> ");
		}

		sb.append("</p></body></html>");
		
		htmlText = sb.toString();
	}
	
	/**
	 * 
	 * @param textcontent the xml-text is parsed into a map. The id(s) are used as keys, the values are the text-passages
	 * @return
	 */
	private LinkedHashMap<String, String> createMap(String textcontent){
		String[] temp = null;
		LinkedHashMap<String, String> tempMap = new LinkedHashMap<String, String>();
		String tempID = null;
		String tempText = null;
		
		temp = textcontent.split(phraseDelimiter);
		
		for (String phraseline : temp){	
			tempID = getIdOfPhrase(phraseline);
			tempText = getTextOfPhrase(phraseline);		
			tempMap.put(tempID, tempText);	
		}
		return tempMap;
	}
	
	/**
	 * 
	 * @param phraseline	a single line to be searched for the id(s)
	 * @return				the id(s) that is(are) found in the line
	 */
	private String getIdOfPhrase(String phraseline){
		String id = null;
		String idstart = "ids=\"";
		
		String declaration = phraseline.substring(0, phraseline.indexOf(">")); //the content between the beginning of the loine and the first ">"
		int start = declaration.indexOf(idstart) + idstart.length();//the position right after "id=""/start of the declaration
		id = declaration.substring(start, declaration.indexOf("\"", start) );//end of the declaration
		
		return id;
	}
	
	/**
	 * 
	 * @param phraseline	a single line to be searched for the contained text-passages
	 * @return				the text-passage within the given line
	 */
	private String getTextOfPhrase(String phraseline){
		String text = null;
		int start = 0;
		if (phraseline != null) {
			start = phraseline.indexOf('>') + 1;//Position after the first ">"/ the beginning of the content
			text = phraseline.substring(start);
			text = text.replace(phraseDelimiter, "");//removes the delimiter
			text = text.trim();
		}
		
		return text;
	}
	/**
	 * 
	 * @param id	the id of which the linked texts are to be searched
	 * @return		an array of the found linked texts
	 */
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
	/**
	 * 
	 * @return the maps of the ids and the linked texts
	 */
	public LinkedHashMap<String, String> getTextelements() {
		return textelements;
	}
	/**
	 * 
	 * @return	the original/given xml-text
	 */
	public String getXmlText() {
		return xmlText;
	}
	/**
	 * 
	 * @return	an html-text with hiperlinks, wo be used in an JEditorPane
	 */
	public String getHtmlText() {
		return htmlText;
	}
	
}
