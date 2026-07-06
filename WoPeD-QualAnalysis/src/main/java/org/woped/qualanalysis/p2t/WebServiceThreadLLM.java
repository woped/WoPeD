package org.woped.qualanalysis.p2t;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.IEditor;
import org.woped.core.utilities.LoggerManager;
import org.woped.core.utilities.SslTrustStoreInitializer;
import org.woped.gui.translations.Messages;
import org.woped.qualanalysis.paraphrasing.Constants;

public class WebServiceThreadLLM extends Thread {

    private P2TSideBar paraphrasingPanel;
    private boolean isFinished;
    private String apiKey;
    private String prompt;
    private String gptModel;
    private String provider;
    private String useRag;
    private String text;
    private String errorMessage;

    public WebServiceThreadLLM(P2TSideBar paraphrasingPanel) {
        this.paraphrasingPanel = paraphrasingPanel;
        isFinished = false;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void run() {
        apiKey = ConfigurationManager.getConfiguration().getGptApiKey();
        prompt = ConfigurationManager.getConfiguration().getGptPrompt();
        gptModel = ConfigurationManager.getConfiguration().getGptModel();
        provider = ConfigurationManager.getConfiguration().getLlmProvider();
        // WFC-US15 (#24): RAG is disabled; always send false regardless of stored config.
        useRag = "false";

        IEditor editor = paraphrasingPanel.getEditor();
        paraphrasingPanel.showLoadingAnimation(true);

        // P2T LLM is served by the Process2Text server (upper section in NLP Tools),
        // not by the Text2Process LLM endpoint (/t2p-2.0 on port 443).
        SslTrustStoreInitializer.initialize();

        String rawHost = ConfigurationManager.getConfiguration().getProcess2TextServerHost();
        int port       = ConfigurationManager.getConfiguration().getProcess2TextServerPort();
        String rawUri  = ConfigurationManager.getConfiguration().getProcess2TextServerURI();

        if (rawHost == null) rawHost = "";
        if (rawUri  == null) rawUri  = "";
        rawHost = rawHost.trim();
        rawUri  = rawUri.trim();

        // Fallback: keep legacy localhost endpoint reachable for dev when no host is configured.
        if (rawHost.isEmpty()) {
            rawHost = "localhost";
            if (port <= 0) port = 8080;
            if (rawUri.isEmpty()) rawUri = "/p2t";
        }

        // Respect explicit scheme in the host field; otherwise pick https for port 443, http for the rest.
        boolean hostHasScheme = rawHost.startsWith("http://") || rawHost.startsWith("https://");
        String scheme   = hostHasScheme ? "" : ((port == 443) ? "https://" : "http://");
        String portPart = (port > 0) ? ":" + port : "";

        // Normalize URI: ensure leading slash, strip trailing slash before appending endpoint.
        String normalizedUri = rawUri.isEmpty()
                ? ""
                : (rawUri.startsWith("/") ? rawUri : "/" + rawUri);
        if (normalizedUri.endsWith("/")) {
            normalizedUri = normalizedUri.substring(0, normalizedUri.length() - 1);
        }

        String url = scheme + rawHost + portPart + normalizedUri + "/generateTextLLM";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        new PNMLExport().saveToStream(editor, stream);
        String text = stream.toString();
        String output;

        try {
            // Guard against unset configuration values before URL-encoding (WFC-US8 #11):
            if (apiKey   == null) apiKey   = "";
            if (prompt   == null) prompt   = "";
            if (gptModel == null) gptModel = "";
            if (provider == null) provider = "";
            if (useRag   == null) useRag   = "false";

            String encodedApiKey = URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
            String encodedPrompt = URLEncoder.encode(prompt, StandardCharsets.UTF_8);
            String encodedGptModel = URLEncoder.encode(gptModel, StandardCharsets.UTF_8);
            String encodedProvider = URLEncoder.encode(provider, StandardCharsets.UTF_8);
            String encodedUseRag = URLEncoder.encode(useRag, StandardCharsets.UTF_8);
            String urlWithParams = String.format("%s?apiKey=%s&prompt=%s&gptModel=%s&provider=%s&useRag=%s",
                    url, encodedApiKey, encodedPrompt, encodedGptModel, encodedProvider, encodedUseRag);

            LoggerManager.info(Constants.PARAPHRASING_LOGGER, "Calling P2T LLM service: " + url);

            URL urlObj = new URL(urlWithParams);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = text.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            String responseBody = readResponseBody(conn, responseCode);
            LoggerManager.info(Constants.PARAPHRASING_LOGGER, "P2T LLM response code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                if (responseBody == null || responseBody.isBlank()) {
                    throw new IOException(Messages.getString("P2T.Error.Webservice.EmptyResponse"));
                }
                output = responseBody.replaceAll("\\s*\n\\s*", "");
                paraphrasingPanel.setNaturalTextParser(new Process2Text(output));
                setText(output);
            } else {
                output = "Request failed. Response Code: " + responseCode;
                errorMessage = buildWebserviceErrorMessage(responseCode, url, responseBody);
                LoggerManager.error(Constants.PARAPHRASING_LOGGER, errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            errorMessage =
                    Messages.getString("P2T.Error.Webservice.Request", new Object[] {message});
            LoggerManager.error(Constants.PARAPHRASING_LOGGER, errorMessage);
        } finally {
            isFinished = true;
            paraphrasingPanel.showLoadingAnimation(false);
            paraphrasingPanel.enableButtons(true);
            paraphrasingPanel.setThreadInProgress(false);
        }
    }

    private static String readResponseBody(HttpURLConnection conn, int responseCode) throws IOException {
        InputStream stream =
                responseCode >= HttpURLConnection.HTTP_BAD_REQUEST
                        ? conn.getErrorStream()
                        : conn.getInputStream();
        if (stream == null) {
            return "";
        }
        try (Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        }
    }

    private static String buildWebserviceErrorMessage(int responseCode, String serverUrl, String responseBody) {
        String details = extractErrorMessage(responseBody);
        StringBuilder message =
                new StringBuilder(
                        Messages.getString(
                                "P2T.Error.Webservice.Http", new Object[] {responseCode}));

        if (details != null && !details.isBlank()) {
            message.append("\n\n").append(details);
        }

        message.append("\n\n").append(Messages.getString("P2T.Error.Webservice.Server", new Object[] {serverUrl}));
        message.append("\n\n").append(Messages.getString("P2T.Error.Webservice.P2THint"));
        return message.toString();
    }

    private static String extractErrorMessage(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return null;
        }
        int messageIndex = responseBody.indexOf("\"message\"");
        if (messageIndex >= 0) {
            int start = responseBody.indexOf(':', messageIndex) + 1;
            int firstQuote = responseBody.indexOf('"', start);
            int secondQuote = responseBody.indexOf('"', firstQuote + 1);
            if (firstQuote >= 0 && secondQuote > firstQuote) {
                return responseBody.substring(firstQuote + 1, secondQuote);
            }
        }
        return responseBody.length() > 500 ? responseBody.substring(0, 500) + "..." : responseBody;
    }

    public void setText(String output) {
        this.text = output;
    }

    public String getText() {
        return this.text;
    }
}
