package org.woped.qualanalysis.paraphrasing.controller;

import java.io.StringReader;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.WebServiceException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.xmlbeans.XmlException;
import org.w3c.dom.Document;
import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;
import org.woped.pnml.NetType;
import org.woped.pnml.PhraseType;
import org.woped.pnml.PnmlDocument;
import org.woped.pnml.PnmlType;
import org.woped.pnml.TextType;
import org.woped.qualanalysis.p2t.P2TSideBar;
import org.woped.qualanalysis.p2t.Process2Text;
import org.woped.qualanalysis.paraphrasing.Constants;
import org.woped.qualanalysis.paraphrasing.view.ParaphrasingPanel;
import org.woped.qualanalysis.paraphrasing.webservice.ProcessToTextWebServiceImpl;
import org.woped.gui.translations.Messages;
import org.xml.sax.InputSource;

public class WebServiceThread extends Thread{
	
	private P2TSideBar paraphrasingPanel = null;
	private String[][] result = null;
	private boolean isFinished;
	
	public WebServiceThread(P2TSideBar paraphrasingPanel){
		this.paraphrasingPanel = paraphrasingPanel;
		isFinished = false;
	}	

	public boolean getIsFinished(){
		return isFinished;
	}
	
	public void run(){
		IEditor editor = paraphrasingPanel.getEditor();

		if(editor.getModelProcessor().getElementContainer().getRootElements().size() > 3){
			try{
				
				CurrentNetPnml currentNetPnml = new CurrentNetPnml(editor);	
				currentNetPnml.setPnmlString();
				
				if(currentNetPnml.isProcessable() == true){
					ProcessToTextWebServiceImpl pttService =  new ProcessToTextWebServiceImpl();
					String output = pttService.getProcessToTextWebServicePort().generateTextFromProcessSpecification(currentNetPnml.getPnmlString());
					isFinished=true;
					paraphrasingPanel.setNaturalTextParser(new org.woped.qualanalysis.p2t.Process2Text(output));	
					
				}
			}				
			catch(WebServiceException wsEx){	
				isFinished=true;
				JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.Error.Webserviceexception.Message"),
						Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
			}
			catch(Exception ex){	
				isFinished=true;
				JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.Error.Exception.Message"),
						Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
				
			}
//			finally{
////				this.paraphrasingPanel.getParaphrasingOutput().setTableVisible();
////				paraphrasingPanel.enableButtons(true);
////				paraphrasingPanel.setThreadInProgress(false);
//			}
		}
		else{
			JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.Numberelements.Message"),
					Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
		}

		
		
	}
	
	/**
	 * Extracts the phrases from an pnml file
	 * and saves it to the result variable
	 * 
	 * @author Martin Meitz
	 * @throws XmlException 
	 *
	 * 
	 */
	
	private String extractDescriptionFromWebservice(String xmlString) throws XmlException{
		DocumentBuilderFactory xmlBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder xmlBuilder;
		String result = null;
		try  
		    {  
		        xmlBuilder = xmlBuilderFactory.newDocumentBuilder();
		        Document document = xmlBuilder.parse( new InputSource( new StringReader( xmlString ) ) );
		        XPath xPath = XPathFactory.newInstance().newXPath();
		        result = xPath.evaluate("/pnml/text", document.getChildNodes());	        
		        
		    } catch (Exception e) {  
		        e.printStackTrace();  
		    }		
		return result;
	}
	
    private void extractDescription(String xmlString) throws XmlException{

    	PnmlDocument pnmlDoc = PnmlDocument.Factory.parse(xmlString); 	
    	PnmlType pnmlTag = pnmlDoc.getPnml();
   	        	
    	if(pnmlTag.getNetArray().length > 0){
    		NetType netTag = pnmlTag.getNetArray(0);
        	
        	if(netTag.isSetText()){
        		TextType textTag = netTag.getText();
        		
            	if(textTag.getPhraseArray().length > 0){
            		PhraseType[] phraseTag = textTag.getPhraseArray();
                	
                	this.result = new String[phraseTag.length][2];        	
                	for(int i = 0; i < phraseTag.length; i++){
                		this.result[i][0] = phraseTag[i].getIds().trim();
                		this.result[i][1] = phraseTag[i].getStringValue().trim();	
                	}
            	}  
            	else{
            		JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.Parsing.Empty.Message"),
        					Messages.getString("Paraphrasing.Webservice.Parsing.Empty.Title"), JOptionPane.INFORMATION_MESSAGE);
            	}
        	}
    	}    	
    }
}
