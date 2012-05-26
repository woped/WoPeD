package org.woped.qualanalysis.paraphrasing.controller;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.ws.WebServiceException;

import org.apache.xmlbeans.XmlException;
import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;
import org.woped.pnml.NetType;
import org.woped.pnml.PhraseType;
import org.woped.pnml.PnmlDocument;
import org.woped.pnml.PnmlType;
import org.woped.pnml.TextType;
import org.woped.qualanalysis.paraphrasing.Constants;
import org.woped.qualanalysis.paraphrasing.view.ParaphrasingPanel;
import org.woped.qualanalysis.paraphrasing.webservice.ProcessToTextImplService;
import org.woped.translations.Messages;

public class WebServiceThread extends Thread{
	
	private ParaphrasingPanel paraphrasingPanel = null;
	private String[][] result = null;
	
	public WebServiceThread(ParaphrasingPanel paraphrasingPanel){
		this.paraphrasingPanel = paraphrasingPanel;
	}
	

	public void run(){
		IEditor editor = paraphrasingPanel.getEditor();
		paraphrasingPanel.enableButtons(false);
		
		if(paraphrasingPanel.getThreadInProgress() == false){
			paraphrasingPanel.setThreadInProgress(true);
			if(editor.getModelProcessor().getElementContainer().getRootElements().size() > 3){
				try{
					
					CurrentNetPnml currentNetPnml = new CurrentNetPnml(editor);	
					currentNetPnml.setPnmlString();
					
					ProcessToTextImplService pttService =  new ProcessToTextImplService();
					String output = pttService.getProcessToTextImplPort().toText(currentNetPnml.getPnmlString());
					if(!output.isEmpty()){
						this.result = null;
						extractDescription(output);
						
						if(this.result != null){
							DefaultTableModel defaultTableModel = paraphrasingPanel.getParaphrasingOutput().getDefaultTableModel();
							defaultTableModel.setRowCount(0);

							for(int i = 0; i < this.result.length; i++){
								String[] tmp = {this.result[i][0],this.result[i][1]};
								defaultTableModel.addRow(tmp);
								tmp=null;
							}
							paraphrasingPanel.getParaphrasingOutput().updateElementContainer();
							LoggerManager.debug(Constants.PARAPHRASING_LOGGER, "   ... Webservice called.");	
							
						}
					}
				}
				catch(WebServiceException wsEx){	
					JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.Error.Webserviceexception.Message"),
							Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
				}
				catch(Exception ex){	
					JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.Error.Exception.Message"),
							Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
					
				}
				finally{
					this.paraphrasingPanel.getParaphrasingOutput().setTableVisible();
					paraphrasingPanel.enableButtons(true);
					paraphrasingPanel.setThreadInProgress(false);
				}
			}
			else{
				JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.Numberelements.Message"),
						Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else{
			JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.ThreadInProgress.Message"),
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
    private void extractDescription(String xmlString) throws XmlException{
    	
//    	try{
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
        	
//    	}
//    	catch(Exception ex){
//    		JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.Parsing.Error.Message"),
//					Messages.getString("Paraphrasing.Webservice.Parsing.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
//    	}
    	
    }
}
