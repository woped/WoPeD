package org.woped.qualanalysis.p2t;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.service.QualanalysisServiceImplement;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import javax.xml.ws.WebServiceException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WebServiceThread extends Thread {

	private P2TSideBar paraphrasingPanel = null;
	private String[][] result = null;
	private boolean isFinished;
	private HttpRequest request;
	private HttpResponse response;

	public WebServiceThread(P2TSideBar paraphrasingPanel) {
		this.paraphrasingPanel = paraphrasingPanel;
		isFinished = false;
	}

	public boolean getIsFinished() {
		return isFinished;
	}

	public void run() {
		IEditor editor = paraphrasingPanel.getEditor();
		paraphrasingPanel.showLoadingAnimation(true);
		String url = "http://" + ConfigurationManager.getConfiguration().getProcess2TextServerHost() + ":"
				+ ConfigurationManager.getConfiguration().getProcess2TextServerPort()
				+ ConfigurationManager.getConfiguration().getProcess2TextServerURI() + "/generateText";

		String[] arg = { url, "P2T" };

		if (editor.getModelProcessor().getElementContainer().getRootElements().size() > 3) {
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				new PNMLExport().saveToStream(editor, stream);
				String text = stream.toString();
				String output = "";
				QualanalysisServiceImplement soundnesscheck = new QualanalysisServiceImplement(editor);
				if (soundnesscheck.isSound()) {
					// Use WebService to call P2T
				request = new HttpRequest(url, text);
				response = request.getResponse();
				output = response.getBody();

					// End of call for WebService
					// Alternatively call P2T directly with bypass of WebService
//					 TextGenerator tg = new TextGenerator(new java.io.File(".").getCanonicalPath() + "/WoPeD-Process2Text/bin");
//					try {
//						output = tg.toText(text);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
					// End of alternative code
				}
				// End Comment here!
				// Do Not Comment the Following!!
				output = output.replaceAll("\\s*\n\\s*", "");
				isFinished = true;
				paraphrasingPanel.setNaturalTextParser(new Process2Text(output));
//			} catch (IOException e) {
//				e.printStackTrace();
			} finally {
				switch (response.responseCode) {
				case HttpServletResponse.SC_NO_CONTENT:
				case HttpServletResponse.SC_REQUEST_TIMEOUT:
				case HttpServletResponse.SC_INTERNAL_SERVER_ERROR: // Could be more specific -> text generator should
																	// send the error in body
					JOptionPane.showMessageDialog(null,
							Messages.getString("Paraphrasing.Webservice.Error.TryAgain", arg),
							Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
					break;
				case HttpServletResponse.SC_SERVICE_UNAVAILABLE:
				case -1:
					JOptionPane.showMessageDialog(null,
							Messages.getString("Paraphrasing.Webservice.Error.Contact", arg)
									+ Messages.getString("Paraphrasing.Webservice.Settings"),
							Messages.getString("Paraphrasing.Webservice.Error.Title"), JOptionPane.INFORMATION_MESSAGE);
					break;
				}
				paraphrasingPanel.showLoadingAnimation(false);
				paraphrasingPanel.enableButtons(true);
				paraphrasingPanel.setThreadInProgress(false);
			}

//	Alternative code for calling P2T locally (not via Webservice)
			/*
			 * ByteArrayOutputStream stream = new ByteArrayOutputStream(); new
			 * PNMLExport().saveToStream(editor, stream); String text = stream.toString();
			 * String output = ""; org.woped.p2t.textGenerator.TextGenerator tg = new
			 * org.woped.p2t.textGenerator.TextGenerator(); try { output = tg.toText(text,
			 * true); } catch (Exception e) { e.printStackTrace(); }
			 * 
			 * isFinished = true; output = output.replaceAll("\\s*\n\\s*", "");
			 * paraphrasingPanel.setNaturalTextParser(new Process2Text(output));
			 * paraphrasingPanel.setThreadInProgress(false);
			 * paraphrasingPanel.showLoadingAnimation(false);
			 * paraphrasingPanel.enableButtons(true);
			 * paraphrasingPanel.setThreadInProgress(false); } else {
			 * JOptionPane.showMessageDialog(null,
			 * Messages.getString("Paraphrasing.Webservice.Numberelements.Message"),
			 * Messages.getString("Paraphrasing.Webservice.Error.Title"),
			 * JOptionPane.INFORMATION_MESSAGE); }
			 */
		}
	}
}