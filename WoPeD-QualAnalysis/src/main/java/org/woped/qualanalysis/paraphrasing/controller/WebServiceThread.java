package org.woped.qualanalysis.paraphrasing.controller;

import java.io.ByteArrayOutputStream;

import javax.swing.JOptionPane;
import javax.xml.ws.WebServiceException;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.p2t.P2TSideBar;
import org.woped.qualanalysis.p2t.Process2Text;
import org.woped.qualanalysis.paraphrasing.webservice.PNMLExport;
import org.woped.qualanalysis.paraphrasing.webservice.ProcessToTextWebService;
import org.woped.qualanalysis.paraphrasing.webservice.ProcessToTextWebServiceImpl;

public class WebServiceThread extends Thread {

	private P2TSideBar paraphrasingPanel = null;
	private String[][] result = null;
	private boolean isFinished;

	public WebServiceThread(P2TSideBar paraphrasingPanel) {
		this.paraphrasingPanel = paraphrasingPanel;
		isFinished = false;
	}

	public boolean getIsFinished() {
		return isFinished;
	}

	public void run() {
		IEditor editor = paraphrasingPanel.getEditor();
		String url = "http://" + ConfigurationManager.getConfiguration().getProcess2TextServerHost() + ":"
				+ ConfigurationManager.getConfiguration().getProcess2TextServerPort()
				+ ConfigurationManager.getConfiguration().getProcess2TextServerURI() + "/ProcessToTextWebService?wsdl";

		String[] arg = {url};

		int numOfNodes = editor.getModelProcessor().getElementContainer().getRootElements().size();
		if (numOfNodes > 3) {

			// Start of alternative code to use Webservice to call P2T
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				new PNMLExport().saveToStream(editor, stream);
				String text = stream.toString();
				ProcessToTextWebServiceImpl pttService = new ProcessToTextWebServiceImpl();
				ProcessToTextWebService port = pttService.getProcessToTextWebServicePort();
				String output = port.generateTextFromProcessSpecification(text);
				output = output.replaceAll("\\s*\n\\s*", "");
				isFinished = true;
				paraphrasingPanel.setNaturalTextParser(new Process2Text(output));
				paraphrasingPanel.setThreadInProgress(false);
			}
			catch (WebServiceException wsEx) {
				isFinished = true;
				JOptionPane.showMessageDialog(null,
						Messages.getString("Paraphrasing.Webservice.Error.Webserviceexception.Message", arg),
						Messages.getString("Paraphrasing.Webservice.Error.Webserviceexception.Title"), JOptionPane.INFORMATION_MESSAGE);
			}
			catch (Exception ex) {
				isFinished = true;
				JOptionPane.showMessageDialog(null,
						Messages.getString("Paraphrasing.Webservice.Error.Exception.Message", arg),
						Messages.getString("Paraphrasing.Webservice.Error.Exception.Title"), JOptionPane.INFORMATION_MESSAGE);

			}
			finally {
				paraphrasingPanel.showLoadingAnimation(false);
				paraphrasingPanel.enableButtons(true);
				paraphrasingPanel.setThreadInProgress(false);
			}
			// End of alternative to use Webservice to call P2T*/

			// Alternative code for calling P2T locally (not via Webservice)
/*
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			new PNMLExport().saveToStream(editor, stream);
			String text = stream.toString();
			String output = "";
			org.woped.p2t.textGenerator.TextGenerator tg = new org.woped.p2t.textGenerator.TextGenerator();
			try {
				output = tg.toText(text, true);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			isFinished = true;
			output = output.replaceAll("\\s*\n\\s*", "");
			paraphrasingPanel.setNaturalTextParser(new Process2Text(output));
			paraphrasingPanel.setThreadInProgress(false);
			paraphrasingPanel.showLoadingAnimation(false);
			paraphrasingPanel.enableButtons(true);
			paraphrasingPanel.setThreadInProgress(false);*/
			// End of alternative code for calling P2T locally (not via Webservice)
		}
		else { // numOfNodes of Petri net in editor is 3 or smaller
 			isFinished = true;
			paraphrasingPanel.showLoadingAnimation(false);
			paraphrasingPanel.enableButtons(true);
			paraphrasingPanel.setThreadInProgress(false);
			JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.Numberelements.Message"),
					Messages.getString("Paraphrasing.Webservice.Numberelements.Title"), JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
