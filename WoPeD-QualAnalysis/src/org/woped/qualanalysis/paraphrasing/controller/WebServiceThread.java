package org.woped.qualanalysis.paraphrasing.controller;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.gui.translations.Messages;
import org.woped.p2t.textGenerator.TextGenerator;
import org.woped.qualanalysis.p2t.P2TSideBar;
import org.woped.qualanalysis.p2t.Process2Text;
import org.woped.qualanalysis.paraphrasing.webservice.PNMLExport;

import javax.swing.*;
import javax.xml.ws.WebServiceException;
import java.io.ByteArrayOutputStream;

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
				+ ConfigurationManager.getConfiguration().getProcess2TextServerURI()
				+ "/generate";

		String[] arg = {url};

		if (editor.getModelProcessor().getElementContainer().getRootElements().size() > 3) {
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				new PNMLExport().saveToStream(editor, stream);
				String text = stream.toString();
				String output = "";

				// Use WebService to call P2T
/*				HttpRequest req = new HttpRequest(url, text);
				HttpResponse res = req.getResponse();
				output = res.getBody();*/
				// End of call for WebService

				// Alternatively call P2T directly with bypass of WebService
				TextGenerator tg = new TextGenerator();
				try {
					output = tg.toText(text, true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				// End of alternative code

				//End Comment here!
				//Do Not Comment the Following!!
				output = output.replaceAll("\\s*\n\\s*", "");
				isFinished = true;
				paraphrasingPanel.setNaturalTextParser(new Process2Text(output));
				paraphrasingPanel.setThreadInProgress(false);
			} catch (WebServiceException wsEx) {
				isFinished = true;
				JOptionPane.showMessageDialog(null,
						Messages.getString("Paraphrasing.Webservice.Error.Webserviceexception.Message", arg),
						Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				isFinished = true;
				JOptionPane.showMessageDialog(null,
						Messages.getString("Paraphrasing.Webservice.Error.Exception.Message", arg),
						Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);

			} finally {
				paraphrasingPanel.showLoadingAnimation(false);
				paraphrasingPanel.enableButtons(true);
				paraphrasingPanel.setThreadInProgress(false);
			}

//	Alternative code for calling P2T locally (not via Webservice)
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
			paraphrasingPanel.setThreadInProgress(false);

		} else {
			JOptionPane.showMessageDialog(null, Messages.getString("Paraphrasing.Webservice.Numberelements.Message"),
					Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
		}*/
		}
	}
}