package org.woped.file.p2t;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.swing.*;

import org.json.simple.parser.ParseException;
import org.woped.core.config.ConfigurationManager;
import org.woped.core.controller.AbstractApplicationMediator;
import org.woped.core.controller.AbstractViewEvent;
import org.woped.core.controller.ViewEvent;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;
import org.woped.editor.action.WoPeDAction;
import org.woped.editor.controller.ActionFactory;
import org.woped.editor.tools.ApiHelper;
import org.woped.gui.translations.Messages;

/**
 * Dialog for configuring LLM-based Process2Text generation before opening the sidebar.
 */
public class P2TUI extends JDialog {

    private JTextField apiKeyField;
    private JTextArea promptField;
    private JCheckBox enablePromptCheckBox;
    private JCheckBox showAgainCheckBox;
    private JCheckBox ragEnabledCheckBox;
    private JComboBox<String> modelComboBox;
    private JComboBox<String> providerComboBox;
    private JLabel apiKeyLabel;
    private JLabel promptLabel;
    private JLabel gptModelLabel;
    private JLabel providerLabel;
    private JButton fetchModelsButton;
    private JScrollPane promptScrollPane;

    private static final String DEFAULT_PROMPT = Messages.getString("P2T.prompt.text");
    private static final String PROVIDER_OPENAI = "openAi";
    private static final String PROVIDER_GEMINI = "gemini";
    private static final String PROVIDER_LMSTUDIO = "lmStudio";

    public P2TUI() {
        initialize();
    }

    public P2TUI(AbstractApplicationMediator mediator) {
        this(null, mediator);
    }

    public P2TUI(Frame owner, AbstractApplicationMediator mediator) throws HeadlessException {
        super(owner, true);
        initialize();
    }

    void initialize() {
        try {
            this.setVisible(false);
            this.getContentPane().setLayout(new BorderLayout());
            this.setUndecorated(false);
            this.setResizable(true);
            this.setTitle(Messages.getString("P2T.openP2T.text"));

            this.getContentPane().add(initializeSettingsPanel(), BorderLayout.NORTH);
            this.getContentPane().add(initializeSingleButtonPanel(), BorderLayout.SOUTH);

            this.pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation((screenSize.width - this.getWidth()) / 3, (screenSize.height - this.getHeight()) / 3);

            Dimension size = new Dimension(650, 500);
            this.setSize(size);

            LoggerManager.info(Constants.EDITOR_LOGGER, "P2TUI initialized successfully");
        } catch (Exception e) {
            LoggerManager.error(Constants.EDITOR_LOGGER, "Error initializing P2TUI: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /** Panel with LLM configuration fields (provider, API key, prompt, model, RAG). */
    JPanel initializeSettingsPanel() {
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel fieldsPanel = new JPanel(new GridBagLayout());

        providerLabel = new JLabel(Messages.getString("P2T.provider.title") + ":");
        providerComboBox = new JComboBox<>();
        providerComboBox.addItem(PROVIDER_OPENAI);
        providerComboBox.addItem(PROVIDER_GEMINI);
        providerComboBox.addItem(PROVIDER_LMSTUDIO);
        providerComboBox.setPreferredSize(new Dimension(150, 25));

        apiKeyLabel = new JLabel(Messages.getString("P2T.apikey.title") + ":");
        apiKeyField = new JTextField();
        apiKeyField.setPreferredSize(new Dimension(300, 25));

        promptLabel = new JLabel(Messages.getString("P2T.prompt.title") + ":");
        promptField = new JTextArea(DEFAULT_PROMPT);
        promptField.setLineWrap(true);
        promptField.setWrapStyleWord(true);
        promptField.setRows(5);
        promptField.setEnabled(false);
        promptField.setText(ConfigurationManager.getConfiguration().getGptPrompt());

        promptScrollPane = new JScrollPane(promptField);
        promptScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        promptScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        promptScrollPane.setPreferredSize(new Dimension(200, 100));

        enablePromptCheckBox = new JCheckBox(Messages.getString("P2T.prompt.checkbox.enable.title"));
        enablePromptCheckBox.setSelected(false);
        enablePromptCheckBox.addActionListener(
                e -> {
                    promptField.setEnabled(enablePromptCheckBox.isSelected());
                    LoggerManager.info(
                            Constants.EDITOR_LOGGER,
                            "Prompt Editing "
                                    + (enablePromptCheckBox.isSelected() ? "Enabled" : "Disabled"));
                });

        ragEnabledCheckBox = new JCheckBox(Messages.getString("P2T.rag.checkbox.enable.title"));
        ragEnabledCheckBox.setSelected(ConfigurationManager.getConfiguration().getRagOption());
        ragEnabledCheckBox.addActionListener(
                e -> {
                    ConfigurationManager.getConfiguration().setRagOption(ragEnabledCheckBox.isSelected());
                    LoggerManager.info(
                            Constants.EDITOR_LOGGER,
                            "RAG " + (ragEnabledCheckBox.isSelected() ? "Enabled" : "Disabled"));
                });

        gptModelLabel = new JLabel(Messages.getString("P2T.get.GPTmodel.title"));
        modelComboBox = new JComboBox<>();
        modelComboBox.setPreferredSize(new Dimension(150, 25));

        fetchModelsButton = new JButton(Messages.getString("P2T.fetchmodels.button"));
        fetchModelsButton.setPreferredSize(new Dimension(120, 25));
        fetchModelsButton.addActionListener(e -> fetchAndFillModels());

        showAgainCheckBox = new JCheckBox(Messages.getString("P2T.popup.show.again.title"));
        showAgainCheckBox.setToolTipText(Messages.getString("P2T.popup.tool.tip.text"));
        showAgainCheckBox.setSelected(ConfigurationManager.getConfiguration().getGptShowAgain());

        String savedProvider = ConfigurationManager.getConfiguration().getLlmProvider();
        if (savedProvider != null && !savedProvider.isEmpty()) {
            providerComboBox.setSelectedItem(savedProvider);
        }
        apiKeyField.setText(ConfigurationManager.getConfiguration().getGptApiKey());
        String savedModel = ConfigurationManager.getConfiguration().getGptModel();
        if (savedModel != null && !savedModel.isEmpty()) {
            modelComboBox.addItem(savedModel);
            modelComboBox.setSelectedItem(savedModel);
        }

        providerComboBox.addActionListener(e -> updateProviderDependentFields());
        updateProviderDependentFields();

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        fieldsPanel.add(providerLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(providerComboBox, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        fieldsPanel.add(apiKeyLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(apiKeyField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        fieldsPanel.add(promptLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        fieldsPanel.add(promptScrollPane, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(enablePromptCheckBox, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        fieldsPanel.add(ragEnabledCheckBox, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(gptModelLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(modelComboBox, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.5;
        fieldsPanel.add(fetchModelsButton, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fieldsPanel.add(showAgainCheckBox, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        settingsPanel.add(fieldsPanel, gbc);

        return settingsPanel;
    }

    /** @deprecated Use {@link #initializeSettingsPanel()} instead. Kept for unit tests. */
    @Deprecated
    JPanel initializeSwitchButtonPanel() {
        return initializeSettingsPanel();
    }

    private void updateProviderDependentFields() {
        String selectedProvider = (String) providerComboBox.getSelectedItem();
        boolean needsApiKey = !PROVIDER_LMSTUDIO.equals(selectedProvider);

        apiKeyLabel.setVisible(needsApiKey);
        apiKeyField.setVisible(needsApiKey);

        if (needsApiKey) {
            apiKeyField.setText(ConfigurationManager.getConfiguration().getGptApiKey());
        }

        revalidate();
        repaint();
    }

    JPanel initializeSingleButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton singleButton = new JButton();
        singleButton.setMnemonic(KeyEvent.VK_A);
        singleButton.setText(Messages.getString("P2T.text"));
        buttonPanel.add(singleButton, BorderLayout.CENTER);

        singleButton.addActionListener(
                e -> {
                    String selectedProvider = (String) providerComboBox.getSelectedItem();

                    if (!PROVIDER_LMSTUDIO.equals(selectedProvider) && !validateAPIKey()) {
                        return;
                    }

                    saveConfiguration();
                    executeAction();
                    dispose();
                });

        return buttonPanel;
    }

    private void saveConfiguration() {
        String selectedProvider = (String) providerComboBox.getSelectedItem();

        if (PROVIDER_OPENAI.equals(selectedProvider) || PROVIDER_GEMINI.equals(selectedProvider)) {
            ConfigurationManager.getConfiguration().setGptApiKey(apiKeyField.getText());
        }

        ConfigurationManager.getConfiguration().setLlmProvider(selectedProvider);
        ConfigurationManager.getConfiguration().setGptPrompt(promptField.getText());
        ConfigurationManager.getConfiguration().setGptUseNew(true);
        ConfigurationManager.getConfiguration().setRagOption(ragEnabledCheckBox.isSelected());

        if (modelComboBox.getSelectedItem() != null) {
            ConfigurationManager.getConfiguration().setGptModel(modelComboBox.getSelectedItem().toString());
        }

        if (!showAgainCheckBox.isSelected()) {
            ConfigurationManager.getConfiguration().setGptShowAgain(false);
        }
    }

    public void executeAction() {
        ConfigurationManager.getConfiguration().setGptUseNew(true);
        WoPeDAction action = ActionFactory.getStaticAction(ActionFactory.ACTIONID_P2T_OLD);
        action.actionPerformed(
                new ViewEvent(this, AbstractViewEvent.VIEWEVENTTYPE_EDIT, AbstractViewEvent.P2T, null));
    }

    void fetchAndFillModels() {
        String selectedProvider = (String) providerComboBox.getSelectedItem();

        new Thread(
                        () -> {
                            try {
                                List<String> models;

                                if (PROVIDER_LMSTUDIO.equals(selectedProvider)) {
                                    models = ApiHelper.fetchModels(null, selectedProvider);
                                } else if (PROVIDER_OPENAI.equals(selectedProvider)) {
                                    models = ApiHelper.fetchModels(apiKeyField.getText(), selectedProvider);
                                } else if (PROVIDER_GEMINI.equals(selectedProvider)) {
                                    models = ApiHelper.fetchModels(apiKeyField.getText(), selectedProvider);
                                } else {
                                    throw new IOException("Unsupported provider: " + selectedProvider);
                                }

                                SwingUtilities.invokeLater(
                                        () -> {
                                            modelComboBox.removeAllItems();
                                            for (String model : models) {
                                                modelComboBox.addItem(model);
                                            }
                                            String savedModel = ConfigurationManager.getConfiguration().getGptModel();
                                            if (savedModel != null) {
                                                modelComboBox.setSelectedItem(savedModel);
                                            }
                                        });
                            } catch (IOException | ParseException e) {
                                SwingUtilities.invokeLater(
                                        () ->
                                                JOptionPane.showMessageDialog(
                                                        this,
                                                        Messages.getString("P2T.exception.fail.fetch.models")
                                                                + e.getMessage(),
                                                        Messages.getString("P2T.exception.fetch.models"),
                                                        JOptionPane.ERROR_MESSAGE));
                            }
                        })
                .start();
    }

    boolean validateAPIKey() {
        String selectedProvider = (String) providerComboBox.getSelectedItem();
        String apiKey = apiKeyField.getText();

        if (apiKey == null || apiKey.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    Messages.getString("P2T.apikey.title") + " (" + selectedProvider + ")",
                    Messages.getString("P2T.apikey.invalid.title"),
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        boolean apiKeyValid = false;

        if (PROVIDER_OPENAI.equals(selectedProvider)) {
            apiKeyValid = isOpenAiAPIKeyValid(apiKey);
        } else if (PROVIDER_GEMINI.equals(selectedProvider)) {
            apiKeyValid = isGeminiAPIKeyValid(apiKey);
        }

        if (!apiKeyValid) {
            JOptionPane.showMessageDialog(
                    this,
                    Messages.getString("P2T.apikey.invalid"),
                    Messages.getString("P2T.apikey.invalid.title"),
                    JOptionPane.ERROR_MESSAGE);
        }

        return apiKeyValid;
    }

    public static boolean isOpenAiAPIKeyValid(String apiKey) {
        final String TEST_URL = "https://api.openai.com/v1/models";
        try {
            URL url = new URL(TEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            int responseCode = connection.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isGeminiAPIKeyValid(String apiKey) {
        final String TEST_URL = "https://generativelanguage.googleapis.com/v1beta/models?key=" + apiKey;
        try {
            URL url = new URL(TEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isAPIKeyValid(String apiKey) {
        return isOpenAiAPIKeyValid(apiKey);
    }
}
