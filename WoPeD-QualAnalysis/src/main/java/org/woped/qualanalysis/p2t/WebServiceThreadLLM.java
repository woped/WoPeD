package org.woped.qualanalysis.p2t;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.gui.translations.Messages;

public class WebServiceThreadLLM extends Thread {

    private P2TSideBar paraphrasingPanel;
    private String[][] result = null;
    private boolean isFinished;
    private String apiKey;
    private String prompt;
    private String gptModel;

    public WebServiceThreadLLM(P2TSideBar paraphrasingPanel) {
        this.paraphrasingPanel = paraphrasingPanel;
        isFinished = false;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public void run() {
        apiKey = ConfigurationManager.getConfiguration().getGptApiKey();
        prompt = ConfigurationManager.getConfiguration().getGptPrompt();
        gptModel = "gpt-4-turbo";

        IEditor editor = paraphrasingPanel.getEditor();
        paraphrasingPanel.showLoadingAnimation(true);

        String testUrl = "http://localhost:8080/p2t/generateTextLLM";
        String url =
                "http://"
                        + ConfigurationManager.getConfiguration().getProcess2TextServerHost()
                        + ":"
                        + ConfigurationManager.getConfiguration().getProcess2TextServerPort()
                        + ConfigurationManager.getConfiguration().getProcess2TextServerURI()
                        + "/generateTextLLM";



        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        new PNMLExport().saveToStream(editor, stream);
        String text = stream.toString();
        System.out.println(text);
        String output;

        //TODO Diese logik in die Buttons implementieren!!

        try {
            // URL-Parameter kodieren
            String encodedApiKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8.toString());
            String encodedPrompt = URLEncoder.encode(prompt, StandardCharsets.UTF_8.toString());
            String encodedGptModel = URLEncoder.encode(gptModel, StandardCharsets.UTF_8.toString());

            // URL mit Parametern aufbauen
            String urlWithParams = String.format("%s?apiKey=%s&prompt=%s&gptModel=%s",
                    testUrl, encodedApiKey, encodedPrompt, encodedGptModel);
            URL urlObj = new URL(urlWithParams);

            // Verbindung aufbauen
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            // Request-Body senden
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = text.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Antwort lesen
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8.name())) {
                    output = scanner.useDelimiter("\\A").next();
                    output = output.replaceAll("\\s*\n\\s*", "");
                    paraphrasingPanel.setNaturalTextParser(new Process2Text(output));
                    System.out.println(output);
                }
            } else {
                output = "Request failed. Response Code: " + responseCode;
                System.out.println(output);
            }

            // Fehlerbehandlung basierend auf Response Code
            switch (responseCode) {
                case HttpServletResponse.SC_NO_CONTENT:
                case HttpServletResponse.SC_REQUEST_TIMEOUT:
                case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
                    JOptionPane.showMessageDialog(
                            null,
                            Messages.getString("Paraphrasing.Webservice.Error.TryAgain"),
                            Messages.getString("Paraphrasing.Webservice.Error.Title"),
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
                case HttpServletResponse.SC_SERVICE_UNAVAILABLE:
                case HttpServletResponse.SC_NOT_FOUND:
                case HttpServletResponse.SC_METHOD_NOT_ALLOWED:
                case -1:
                    JOptionPane.showMessageDialog(
                            null,
                            Messages.getString("Paraphrasing.Webservice.Error.Contact")
                                    + "\n"
                                    + Messages.getString("Paraphrasing.Webservice.Settings"),
                            Messages.getString("Paraphrasing.Webservice.Error.Title"),
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Error processing request: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            isFinished = true;
            paraphrasingPanel.showLoadingAnimation(false);
            paraphrasingPanel.enableButtons(true);
            paraphrasingPanel.setThreadInProgress(false);
        }
    }
}
