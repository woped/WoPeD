/*
 *
 * Copyright (C) 2004-2005, see @author in JavaDoc for the author
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * For contact information please visit http://woped.dhbw-karlsruhe.de
 *
 */
package org.woped.file.t2p;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.IEditor;
import org.woped.core.utilities.SslTrustStoreInitializer;
import org.woped.editor.controller.vc.EditorVC;
import org.woped.file.PNMLImport;
import org.woped.gui.lookAndFeel.WopedButton;
import org.woped.gui.translations.Messages;

/**
 * @author <a href="mailto:freytag@dhbw-karlsruhe.de">Thomas Freytag </a> <br>
 *         <br>
 *         TODO: DOCUMENTATION (tfreytag)
 *         <p>
 *         17.01.2005
 */
@SuppressWarnings("serial")
public class T2PUI extends JDialog {
    private JTextAreaWithHint textArea;

    private JDialog loadDialog;

    private AbstractApplicationMediator mediator;

    private boolean requested = false;
    private SwingWorker<HttpURLConnection, Void> bgTask;

    private String inputText;
    private JComboBox<String> llmProviderBox;

    static final String OPENAI_PROVIDER = "OpenAI";
    static final String GEMINI_PROVIDER = "Gemini";

    public T2PUI(AbstractApplicationMediator mediator) {
        this(null, mediator);
    }

    public T2PUI(Frame owner, AbstractApplicationMediator mediator) throws HeadlessException {
        super(owner, true);
        this.mediator = mediator;
        initialize();
    }

    private void initialize() {
        this.setVisible(false);
        this.getContentPane().setLayout(new BorderLayout());
        this.setUndecorated(false);
        this.setResizable(true);

        textArea = new JTextAreaWithHint();

        this.setTitle(Messages.getString("T2P.tooltip"));
        this.getContentPane().add(wrapTextArea(initializeTextArea(textArea)), BorderLayout.CENTER);
        this.getContentPane().add(initializeButtonsPanel(), BorderLayout.SOUTH);

        this.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(
                (screenSize.width - this.getWidth()) / 3, (screenSize.height - this.getHeight()) / 3);
        Dimension size = new Dimension(800, 440);
        this.setSize(size);

        int index = 0;
        boolean doesContain = false;

        if (mediator.getViewControllers().containsKey("EDITOR_VC_" + index)) {
            doesContain = true;
            while (mediator.getViewControllers().containsKey("EDITOR_VC_" + index)) {
                index++;
            }
            index--;
        }

        if (doesContain) {
            String lastTextInput = ((EditorVC) mediator.getViewControllers().get("EDITOR_VC_" + index))
                    .getEditorPanel()
                    .getT2PText();
            textArea.setText(lastTextInput);
        }
    }

    private JTextAreaWithHint initializeTextArea(JTextAreaWithHint ta) {
        Font f = new Font("Lucia Grande", Font.PLAIN, 13);
        String hint = Messages.getString("T2PUI.HowTo");

        ta.setFont(f);
        ta.changeHintText(hint);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.requestFocus();
        ta.requestFocusInWindow();
        ta.setMargin(new Insets(10, 10, 10, 10));

        return ta;
    }

    private JScrollPane wrapTextArea(JTextAreaWithHint ta) {
        JScrollPane scrollPane = new JScrollPane(ta);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel initializeButtonsPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        String[] lang = { Messages.getString("T2PUI.Lang"), Messages.getString("T2PUI.Lang.English") };
        JComboBox<String> langBox = new JComboBox<String>(lang);
        langBox.setSelectedIndex(1);

        String[] llmProviders = { OPENAI_PROVIDER, GEMINI_PROVIDER };
        llmProviderBox = new JComboBox<String>(llmProviders);
        selectConfiguredLlmProvider();

        WopedButton btnGenerate = new WopedButton(
                new AbstractAction() {
                    public void actionPerformed(ActionEvent arg0) {
                        request();
                    }
                });
        btnGenerate.setMnemonic(KeyEvent.VK_A);
        btnGenerate.setText(Messages.getString("T2PUI.Button.Generate.Text"));
        btnGenerate.setIcon(
                new ImageIcon(getClass().getResource(Messages.getString("T2PUI.Button.Generate.Icon"))));

        WopedButton btnUpload = new WopedButton(
                new AbstractAction() {
                    public void actionPerformed(ActionEvent arg0) {
                        readFile();
                    }
                });
        btnUpload.setMnemonic(KeyEvent.VK_C);
        btnUpload.setText(Messages.getString("T2PUI.Button.Read.Text"));
        btnUpload.setIcon(
                new ImageIcon(getClass().getResource(Messages.getString("T2PUI.Button.Read.Icon"))));

        WopedButton btnErase = new WopedButton(
                new AbstractAction() {
                    public void actionPerformed(ActionEvent arg0) {
                        clearTextArea();
                    }
                });
        btnErase.setMnemonic(KeyEvent.VK_L);
        btnErase.setText(Messages.getString("T2PUI.Button.Clear.Text"));
        btnErase.setIcon(
                new ImageIcon(getClass().getResource(Messages.getString("T2PUI.Button.Clear.Icon"))));

        buttonPanel.add(btnUpload);
        buttonPanel.add(btnErase);
        buttonPanel.add(langBox);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(new JLabel(Messages.getString("T2PUI.LLM.Provider.label")));
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(llmProviderBox);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(btnGenerate);

        return buttonPanel;
    }

    private String mapLlmProviderToApiValue(String displayName) {
        if (GEMINI_PROVIDER.equals(displayName)) {
            return "gemini";
        }
        return "openai";
    }

    private void selectConfiguredLlmProvider() {
        String configuredProvider = ConfigurationManager.getConfiguration().getLlmProvider();
        if (configuredProvider != null && configuredProvider.equalsIgnoreCase("gemini")) {
            llmProviderBox.setSelectedItem(GEMINI_PROVIDER);
        } else {
            llmProviderBox.setSelectedItem(OPENAI_PROVIDER);
        }
    }

    private String resolveModel() {
        String model = ConfigurationManager.getConfiguration().getGptModel();
        return (model == null || model.isBlank()) ? "gpt-4o-mini" : model;
    }

    void request() {
        if (requested) {
            return;
        }
        requested = true;

        inputText = textArea.getText();

        if (!inputText.isEmpty()) {
            String apiKey = ConfigurationManager.getConfiguration().getGptApiKey();
            String selectedProvider = (String) llmProviderBox.getSelectedItem();

            while (apiKey == null || apiKey.equals("test") || apiKey.isEmpty()
                    || !isApiKeyValid(apiKey, selectedProvider)) {
                apiKey = promptForApiKey(selectedProvider);
                if (apiKey == null || apiKey.isEmpty()) {
                    requested = false;
                    return;
                }
                ConfigurationManager.getConfiguration().setGptApiKey(apiKey);
            }

            llmBackgroundWorker(inputText, apiKey, selectedProvider);
            showLoadingBox();
        } else {
            showErrorPopUp("T2PUI.NoText.Title", "T2PUI.NoText.Text");
        }

        requested = false;
    }

    String promptForApiKey(String provider) {
        return JOptionPane.showInputDialog(
                this,
                Messages.getString("T2PUI.LLM.ApiKey.input.Message", new String[] {provider}),
                Messages.getString("T2PUI.LLM.ApiKey.input.Title"),
                JOptionPane.QUESTION_MESSAGE);
    }

    private boolean isApiKeyValid(String apiKey, String provider) {
        try {
            String testUrl;
            if (GEMINI_PROVIDER.equals(provider)) {
                testUrl = "https://generativelanguage.googleapis.com/v1/models?key=" + apiKey;
            } else {
                testUrl = "https://api.openai.com/v1/models";
            }

            HttpURLConnection connection = (HttpURLConnection) new URL(testUrl).openConnection();
            connection.setRequestMethod("GET");
            if (!GEMINI_PROVIDER.equals(provider)) {
                connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            }
            connection.connect();
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            return responseCode == 200;
        } catch (IOException e) {
            return false;
        }
    }

    void showLoadingBox() {
        JOptionPane jop = new JOptionPane();
        jop.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        jop.setMessage(Messages.getString("T2PUI.Loading.Text"));

        loadDialog = jop.createDialog(this, Messages.getString("T2PUI.Loading.Title"));
        jop.setOptions(new String[] { Messages.getString("T2PUI.Loading.Cancel") });
        loadDialog.setVisible(true);

        if (bgTask != null && !bgTask.isDone() && !bgTask.isCancelled()) {
            bgTask.cancel(true);
        }
    }

    void displayPNML(String pnml) {
        PNMLImport pnmlImport = new PNMLImport(mediator);
        InputStream stream = new ByteArrayInputStream(pnml.getBytes(StandardCharsets.UTF_8));
        pnmlImport.run(stream, Messages.getString("Document.T2P.Output"), true);

        IEditor[] editor = pnmlImport.getEditor();
        EditorVC evc = ((EditorVC) editor[0]);

        try {
            if (inputText != null) {
                evc.getEditorPanel().showT2PBar(inputText);
            }
            evc.startBeautify(0, 0, 0);
        } catch (ArithmeticException exc) {
            close();
        }
        close();
    }

    void showErrorPopUp(String titleId, String msgId) {
        String text[] = { Messages.getString("Dialog.Ok") };
        JOptionPane.showOptionDialog(
                null,
                Messages.getStringReplaced(msgId, null),
                Messages.getString(titleId),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                text,
                text[0]);
    }

    void llmBackgroundWorker(String text, String apiKey, String provider) {
        SslTrustStoreInitializer.initialize();

        int portNum = ConfigurationManager.getConfiguration().getT2PLlmServicePort();
        String host = ConfigurationManager.getConfiguration().getT2PLlmServiceHost().trim();
        String protocol = (portNum == 443 || portNum == 0) ? "https://" : "http://";
        String port = "";
        if (portNum > 0
                && !((protocol.equals("https://") && portNum == 443)
                        || (protocol.equals("http://") && portNum == 80))) {
            port = ":" + portNum;
        }

        String connectionStr =
                protocol
                        + host
                        + port
                        + ConfigurationManager.getConfiguration().getT2PLlmServiceUri()
                        + "/v2/generate/pnml";
        String providerApi = mapLlmProviderToApiValue(provider);
        String model = resolveModel();

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("text", text);
        requestBody.addProperty("provider", providerApi);
        requestBody.addProperty("model", model);
        final String jsonInputString = requestBody.toString();

        System.out.println("Connecting to T2P v2 service at: " + connectionStr);

        bgTask = new SwingWorker<HttpURLConnection, Void>() {
            @Override
            protected HttpURLConnection doInBackground() throws IOException {
                HttpURLConnection connection = (HttpURLConnection) new URL(connectionStr).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + apiKey);
                connection.setDoOutput(true);
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(60000);

                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
                }
                return connection;
            }

            @Override
            protected void done() {
                HttpURLConnection connection = null;
                try {
                    if (loadDialog != null) {
                        loadDialog.dispose();
                    }
                    connection = get();
                    int responseCode = connection.getResponseCode();
                    String responseBody = readResponseBody(connection, responseCode);

                    if (responseCode == 200) {
                        String pnml = T2PResponseParser.extractPnml(responseBody);
                        if (pnml != null && !pnml.isEmpty()) {
                            displayPNML(pnml);
                        } else {
                            showErrorPopUp("T2PUI.LLMError.Title", "T2PUI.InvalidResponse.Text");
                        }
                    } else {
                        String details = T2PResponseParser.extractErrorMessage(responseBody);
                        if (details == null || details.isBlank()) {
                            details = Messages.getString("T2PUI.GeneralError.Text");
                        }
                        JOptionPane.showMessageDialog(
                                null,
                                details,
                                Messages.getString(
                                        responseCode == 400
                                                ? "T2PUI.400Error.Title"
                                                : responseCode == 401
                                                        ? "T2PUI.401Error.Title"
                                                        : "T2PUI.GeneralError.Title"),
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (JsonSyntaxException e) {
                    showErrorPopUp("T2PUI.LLMError.Title", "T2PUI.InvalidResponse.Text");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            null,
                            Messages.getString(
                                    "T2PUI.LLM.Connection.Error.Message", new String[] {e.getMessage()}),
                            Messages.getString("T2PUI.LLM.Connection.Error.Title"),
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        };

        bgTask.execute();
    }

    private static String readResponseBody(HttpURLConnection connection, int responseCode) throws IOException {
        InputStream stream =
                responseCode >= HttpURLConnection.HTTP_BAD_REQUEST
                        ? connection.getErrorStream()
                        : connection.getInputStream();
        if (stream == null) {
            return "";
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            return body.toString();
        }
    }

    void close() {
        this.dispose();
    }

    public void clearTextArea() {
        if (textArea.getText() != null) {
            textArea.setText(null);
        }
    }

    public void readFile() {
        PlainTextFileReader r = new PlainTextFileReader();
        String txt = r.read();
        if (txt != null) {
            textArea.setText(txt);
        }
    }
}
