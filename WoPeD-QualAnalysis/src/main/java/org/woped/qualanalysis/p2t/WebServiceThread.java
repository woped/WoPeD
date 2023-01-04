package org.woped.qualanalysis.p2t;

import java.io.ByteArrayOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.gui.translations.Messages;

public class WebServiceThread extends Thread {

  private P2TSideBar paraphrasingPanel;
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
    String url =
        "http://"
            + ConfigurationManager.getConfiguration().getProcess2TextServerHost()
            + ":"
            + ConfigurationManager.getConfiguration().getProcess2TextServerPort()
            + ConfigurationManager.getConfiguration().getProcess2TextServerURI()
            + "/generateText";

    String[] arg = {url, "P2T"};
    try {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      new PNMLExport().saveToStream(editor, stream);
      String text = stream.toString();
      String output;
      request = new HttpRequest(url, text);
      response = request.getResponse();
      output = response.getBody();
      output = output.replaceAll("\\s*\n\\s*", "");
      isFinished = true;
      paraphrasingPanel.setNaturalTextParser(new Process2Text(output));
    } finally {
    }
    ;

    switch (response.responseCode) {
      case HttpServletResponse.SC_NO_CONTENT:
      case HttpServletResponse.SC_REQUEST_TIMEOUT:
      case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
        JOptionPane.showMessageDialog(
            null,
            Messages.getString("Paraphrasing.Webservice.Error.TryAgain", arg),
            Messages.getString("Paraphrasing.Webservice.Error.Title"),
            JOptionPane.INFORMATION_MESSAGE);
        break;
      case HttpServletResponse.SC_SERVICE_UNAVAILABLE:
      case HttpServletResponse.SC_NOT_FOUND:
      case HttpServletResponse.SC_METHOD_NOT_ALLOWED:
      case -1:
        JOptionPane.showMessageDialog(
            null,
            Messages.getString("Paraphrasing.Webservice.Error.Contact", arg)
                + "\n"
                + Messages.getString("Paraphrasing.Webservice.Settings"),
            Messages.getString("Paraphrasing.Webservice.Error.Title"),
            JOptionPane.INFORMATION_MESSAGE);
    }

    paraphrasingPanel.showLoadingAnimation(false);
    paraphrasingPanel.enableButtons(true);
    paraphrasingPanel.setThreadInProgress(false);
  }
}
